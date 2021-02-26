package eu.telecomsudparis.jnvm.infinispan.persistence;

import java.util.concurrent.Executor;

import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.persistence.Store;
import org.infinispan.persistence.spi.MarshallableEntry;
import org.infinispan.persistence.spi.MarshallableEntryFactory;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
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
public class JNVMStore<K extends OffHeapObject, V extends OffHeapObject> implements AdvancedLoadWriteStore<K, V> {

    private RecoverableStrongHashMap<K, V> backend = null;

    private InitializationContext ctx;
    private MarshallableEntryFactory entryFactory;
    private JNVMStoreConfiguration configuration;

    @Override
    public boolean contains(Object o) {
        return backend.containsKey(o);
    }

    @Override
    public void init(InitializationContext initializationContext) {
        ctx = initializationContext;
        entryFactory = ctx.getMarshallableEntryFactory();
        configuration = ctx.getConfiguration();
    }

    @Override
    public MarshallableEntry<K, V> loadEntry(Object o) {
        return entryFactory.create(o, backend.get(o));
    }

    @Override
    public void start() {
        if (backend == null) {
            backend = RecoverableStrongHashMap.recover( ctx.getCache().getName(), configuration.maxEntries() );
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
    public void write(MarshallableEntry<? extends K, ? extends V> marshallableEntry) {
        backend.put(marshallableEntry.getKey(), marshallableEntry.getValue());
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

    @Override
    public Publisher<MarshallableEntry<K, V>> entryPublisher(Predicate<? super K> filter,
                                                             boolean fetchValue,
                                                             boolean fetchMetadata) {
        throw new UnsupportedOperationException("entryPublisher");
    }

}
