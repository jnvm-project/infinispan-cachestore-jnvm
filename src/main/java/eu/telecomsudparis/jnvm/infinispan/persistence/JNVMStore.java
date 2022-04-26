package eu.telecomsudparis.jnvm.infinispan.persistence;

import java.util.concurrent.Executor;
import java.util.Map;
import java.nio.ByteBuffer;

import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.persistence.Store;
import org.infinispan.filter.KeyFilter;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.marshall.core.MarshalledEntryFactory;
import org.infinispan.commons.marshall.StreamingMarshaller;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
import org.infinispan.persistence.spi.InitializationContext;
import org.infinispan.persistence.TaskContextImpl;
import org.infinispan.persistence.spi.PersistenceException;
import org.infinispan.persistence.PersistenceUtil;
//import org.infinispan.commons.io.ByteBuffer;
import org.infinispan.commons.io.ByteBufferFactory;
import org.kohsuke.MetaInfServices;

import eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfiguration;
import eu.telecomsudparis.jnvm.util.persistent.RecoverableHashMap;
import eu.telecomsudparis.jnvm.offheap.OffHeap;
import eu.telecomsudparis.jnvm.offheap.OffHeapObject;
import eu.telecomsudparis.jnvm.offheap.OffHeapString;
import eu.telecomsudparis.jnvm.offheap.OffHeapByteArray;

@Store
@MetaInfServices
@ConfiguredBy(JNVMStoreConfiguration.class)
public class JNVMStore<K,V> implements AdvancedLoadWriteStore<K, V> {

    private static final long RMAP_OFFSET=16;
    private RecoverableHashMap<OffHeapString, OffHeapByteArray> backend;

    private InitializationContext ctx;
    private MarshalledEntryFactory marshalledEntryFactory;
    private ByteBufferFactory byteBufferFactory;
    private StreamingMarshaller marshaller;
    private JNVMStoreConfiguration configuration;

    @Override
    public boolean contains(Object o) {
        return backend.containsKey(new String(marshall(o)));
    }

    @Override
    public void init(InitializationContext initializationContext) {
        ctx = initializationContext;
        marshalledEntryFactory = ctx.getMarshalledEntryFactory();
        byteBufferFactory = ctx.getByteBufferFactory();
        marshaller = ctx.getMarshaller();
        configuration = ctx.getConfiguration();
    }

    @Override
    public MarshalledEntry<K, V> load(Object o) {
        OffHeapByteArray offheapData = backend.get(new String(marshall(o)));
        /*
        String dataString = offheapData.toString();
        byte[] dataBytes = dataString.getBytes();
        */
        byte[] dataBytes = offheapData.toArray();
        ByteBuffer data = ByteBuffer.wrap(dataBytes);
        int keyLength = data.getInt();
        int valueLength = data.getInt();
        int metadataLength = data.getInt();
        /*
        byte[] keyBytes = (keyLength == 0) ? null : new byte[keyLength];
        byte[] valueBytes = (valueLength == 0) ? null : new byte[valueLength];
        byte[] metadataBytes = (metadataLength == 0) ? null : new byte[metadataLength];
        if(keyLength != null)
            data.get(keyBytes, 0, keyLength);
        if(valueBytes != null)
            data.get(valueBytes, 0, valueLength);
        if(metadataBytes != null)
            data.get(metadataBytes, 0, metadataLength);
        */
        int len = keyLength + valueLength + metadataLength;
        byte[] bytes = new byte[len];
        data.get(bytes, 0, len);
        org.infinispan.commons.io.ByteBuffer key = byteBufferFactory.newByteBuffer(bytes, 0, keyLength);
        org.infinispan.commons.io.ByteBuffer value = null;
        org.infinispan.commons.io.ByteBuffer metadata = null;
        if(valueLength>0)
            value = byteBufferFactory.newByteBuffer(bytes, keyLength, valueLength);
        if(metadataLength>0)
            metadata = byteBufferFactory.newByteBuffer(bytes, keyLength + valueLength, metadataLength);
        /*
        org.infinispan.commons.io.ByteBuffer key = byteBufferFactory.newByteBuffer(keyBytes, 0, keyLength);
        org.infinispan.commons.io.ByteBuffer value = byteBufferFactory.newByteBuffer(valueBytes, 0, valueLength);
        org.infinispan.commons.io.ByteBuffer metadata = null;
        if(metadataBytes != null)
            metadata = byteBufferFactory.newByteBuffer(metadataBytes, 0, metadataLength);
        */
/*
        System.out.println("key " + keyLength);
        System.out.println("value " + valueLength);
        System.out.println("meta " + metadataLength);
*/
        return marshalledEntryFactory.newMarshalledEntry(key, value, metadata);
        //org.infinispan.commons.io.ByteBuffer value = byteBufferFactory.newByteBuffer(dataBytes, 0, dataBytes.length);
        //org.infinispan.commons.io.ByteBuffer metadata = null;
        //return marshalledEntryFactory.newMarshalledEntry(o, value, metadata);
    }

    @Override
    public void start() {
        //TODO Properly register the map as a root object
        //     and auto-recover from previous offset on new
        if(OffHeap.getAllocator().size() <= 0) {
            backend = new RecoverableHashMap(1000000);
        } else {
            backend = new RecoverableHashMap(OffHeap.baseAddr() + RMAP_OFFSET);
        }
    }

    @Override
    public void stop() {
        backend = null;
    }

    @Override
    public boolean delete(Object o) {
        return backend.remove(o) != null;
    }

    @Override
    public void write(MarshalledEntry<? extends K, ? extends V> marshalledEntry) {
        org.infinispan.commons.io.ByteBuffer key = marshalledEntry.getKeyBytes();
        org.infinispan.commons.io.ByteBuffer value = marshalledEntry.getValueBytes();
        org.infinispan.commons.io.ByteBuffer metadata = marshalledEntry.getMetadataBytes();

        int metadataLength = (metadata == null) ? 0 : metadata.getLength();
        int valueLength = (value == null) ? 0 : value.getLength();
        int keyLength = (key == null) ? 0 : key.getLength();
        int length = 3 * Integer.BYTES + keyLength + valueLength + metadataLength;

        ByteBuffer data = ByteBuffer.allocate(length);
        data.putInt(keyLength);
        data.putInt(valueLength);
        data.putInt(metadataLength);
        data.put(key.getBuf(), key.getOffset(), key.getLength());
        if(value != null) {
            data.put(value.getBuf(), value.getOffset(), value.getLength());
        }
        if(metadata != null) {
            data.put(metadata.getBuf(), metadata.getOffset(), metadata.getLength());
        }
        data.flip();

        /*
        backend.put(new OffHeapString(new String(key.getBuf())),
                    new OffHeapString(new String(data.array())));
        */
        backend.put(new OffHeapString(new String(key.getBuf())),
                    new OffHeapByteArray(data.array()));
    }

    @Override
    public void process(
            KeyFilter<? super K> filter,
            org.infinispan.persistence.spi.AdvancedCacheLoader.CacheLoaderTask<K, V> task,
            Executor executor, boolean fetchValue, boolean fetchMetadata) {
        filter = PersistenceUtil.notNull(filter);
        final TaskContextImpl taskContext = new TaskContextImpl();
        for(Map.Entry<OffHeapString, OffHeapByteArray> entry : backend.entrySet()) {
            if(taskContext.isStopped()) {
                break;
            }
            K key = (K) unmarshall(entry.getKey().toString().getBytes());
            if(key != null && filter.accept(key)) {
               MarshalledEntry marshalledEntry = load(key);
               if(marshalledEntry != null) {
                   try {
                       task.processEntry(marshalledEntry, taskContext);
                   } catch(Exception e) {
                       throw new PersistenceException(e);
                   }
               }
            }
        }
    }

    @Override
    public int size() {
        return backend.size();
    }

    @Override
    public void clear() {
        backend.clear();
    }

    @Override
    public void purge(
            Executor arg0,
            org.infinispan.persistence.spi.AdvancedCacheWriter.PurgeListener<? super K> arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not implemented");
    }

    private byte[] marshall(Object o) {
        try {
            return marshaller.objectToByteBuffer(o);
        } catch(Exception e) {
            throw new PersistenceException(e);
        }
    }

    private Object unmarshall(byte[] byteArray) {
        try {
            return marshaller.objectFromByteBuffer(byteArray);
        } catch(Exception e) {
            throw new PersistenceException(e);
        }
    }

}
