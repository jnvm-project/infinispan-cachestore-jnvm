package eu.telecomsudparis.jnvm.infinispan.persistence;

import java.util.concurrent.Executor;

import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.persistence.Store;
import org.infinispan.filter.KeyFilter;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.marshall.core.MarshalledEntryFactory;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
import org.infinispan.persistence.spi.InitializationContext;
import org.kohsuke.MetaInfServices;

import eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfiguration;
import eu.telecomsudparis.jnvm.util.persistent.RecoverableHashMap;
import eu.telecomsudparis.jnvm.offheap.OffHeap;
import eu.telecomsudparis.jnvm.offheap.OffHeapObject;

@Store
@MetaInfServices
@ConfiguredBy(JNVMStoreConfiguration.class)
public class JNVMStore<K extends OffHeapObject, V extends OffHeapObject> implements AdvancedLoadWriteStore<K, V> {

    private static final long RMAP_OFFSET=16;
    private RecoverableHashMap<K, V> backend;

    private InitializationContext ctx;
    private MarshalledEntryFactory marshalledEntryFactory;
    private JNVMStoreConfiguration configuration;

    @Override
    public boolean contains(Object o) {
        return backend.containsKey(o);
    }

    @Override
    public void init(InitializationContext initializationContext) {
        ctx = initializationContext;
        marshalledEntryFactory = ctx.getMarshalledEntryFactory();
        configuration = ctx.getConfiguration();
    }

    @Override
    public MarshalledEntry<K, V> load(Object o) {
        return marshalledEntryFactory.newMarshalledEntry(o, backend.get(o), null);
    }

    @Override
    public void start() {
        //TODO Properly register the map as a root object
        //     and auto-recover from previous offset on new
        if(OffHeap.getAllocator().top() == 0) {
            backend = new RecoverableHashMap();
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
        backend.put(marshalledEntry.getKey(), marshalledEntry.getValue());
    }

    @Override
    public void process(
            KeyFilter<? super K> arg0,
            org.infinispan.persistence.spi.AdvancedCacheLoader.CacheLoaderTask<K, V> arg1,
            Executor arg2, boolean arg3, boolean arg4) {
        // TODO Auto-generated method stub

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

    }

}
