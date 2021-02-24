package eu.telecomsudparis.jnvm.infinispan.persistence;

import java.util.concurrent.Executor;

import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.persistence.Store;
import org.infinispan.commons.util.IntSet;
import org.infinispan.persistence.spi.MarshallableEntry;
import org.infinispan.persistence.spi.MarshallableEntryFactory;
import org.infinispan.persistence.spi.SegmentedAdvancedLoadWriteStore;
import org.infinispan.persistence.spi.InitializationContext;
import org.kohsuke.MetaInfServices;
import java.util.function.Predicate;
import org.reactivestreams.Publisher;

import eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfiguration;
import eu.telecomsudparis.jnvm.util.persistent.RecoverableStrongHashMap;
import eu.telecomsudparis.jnvm.offheap.OffHeapObject;

@Store
@MetaInfServices
@ConfiguredBy(JNVMStoreConfiguration.class)
public class JNVMStore<K extends OffHeapObject, V extends OffHeapObject> implements SegmentedAdvancedLoadWriteStore<K, V> {

    private RecoverableStrongHashMap<K, V> backend = null;

    private InitializationContext ctx;
    private MarshallableEntryFactory entryFactory;
    private JNVMStoreConfiguration configuration;

    @Override
    public boolean contains(Object o) { return contains(0, o); }
    @Override
    public MarshallableEntry<K, V> loadEntry(Object o) { return get(0, o); }
    @Override
    public boolean delete(Object o) { return delete(0, o); }
    @Override
    public void write(MarshallableEntry<? extends K, ? extends V> marshallableEntry) { write(0, marshallableEntry); }
    @Override
    public Publisher<MarshallableEntry<K, V>> entryPublisher(Predicate<? super K> filter, boolean fetchValue, boolean fetchMetadata) { throw new UnsupportedOperationException("entryPublisher no segments"); }
    @Override
    public int size() { return backend.size(); }
    @Override
    public void clear() { backend.clear(); }

    @Override
    public boolean contains(int segment, Object o) {
        return backend.containsKey(o);
    }

    @Override
    public void init(InitializationContext initializationContext) {
        ctx = initializationContext;
        entryFactory = ctx.getMarshallableEntryFactory();
        configuration = ctx.getConfiguration();
    }

    @Override
    public MarshallableEntry<K, V> get(int segment, Object o) {
        return entryFactory.create(o, backend.get(o));
    }

    @Override
    public void start() {
        if (backend == null) {
            backend = RecoverableStrongHashMap.recover( ctx.getCache().getName(), 40000000 );
        }
    }

    @Override
    public void stop() {
        backend = null;
    }

    @Override
    public boolean delete(int segment, Object o) {
        return backend.remove(o) != null;
    }

    @Override
    public void write(int segment, MarshallableEntry<? extends K, ? extends V> marshallableEntry) {
        backend.put(marshallableEntry.getKey(), marshallableEntry.getValue());
    }

    @Override
    public int size(IntSet segments) {
        return backend.size();
    }

    @Override
    public void clear(IntSet segments) {
        backend.clear();
    }

    @Override
    public void purge(
            Executor arg0,
            org.infinispan.persistence.spi.AdvancedCacheWriter.PurgeListener<? super K> arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public Publisher<K> publishKeys(IntSet segments, Predicate<? super K> filter) {
        throw new UnsupportedOperationException("publishKeys");
    }

    @Override
    public Publisher<MarshallableEntry<K, V>> entryPublisher(IntSet segments,
                                                             Predicate<? super K> filter,
                                                             boolean fetchValue,
                                                             boolean fetchMetadata) {
        throw new UnsupportedOperationException("entryPublisher");
    }

}
