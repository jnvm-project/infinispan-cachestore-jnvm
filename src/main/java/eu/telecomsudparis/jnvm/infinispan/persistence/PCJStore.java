package eu.telecomsudparis.jnvm.infinispan.persistence;

import java.util.concurrent.Executor;

import lib.util.persistent.AnyPersistent;
import lib.util.persistent.ObjectDirectory;
import lib.util.persistent.PersistentHashMap;
import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.persistence.Store;
import org.infinispan.filter.KeyFilter;
import org.infinispan.marshall.core.MarshalledEntry;
import org.infinispan.marshall.core.MarshalledEntryFactory;
import org.infinispan.marshall.core.MarshalledEntryFactoryImpl;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
import org.infinispan.persistence.spi.InitializationContext;
import org.kohsuke.MetaInfServices;

import eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfiguration;

@Store
@MetaInfServices
@ConfiguredBy(JNVMStoreConfiguration.class)
public class PCJStore<K extends AnyPersistent,V extends AnyPersistent> implements AdvancedLoadWriteStore<K, V> {

    String DATA_KEY = "PCJStore";
    PersistentHashMap<K, V> map = ObjectDirectory.get(DATA_KEY, PersistentHashMap.class);
    MarshalledEntryFactory marshalledEntryFactory = new MarshalledEntryFactoryImpl();

    @Override
    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    @Override
    public void init(InitializationContext initializationContext) {
        // empty
    }

    @Override
    public MarshalledEntry<K, V> load(Object key) {
        return marshalledEntryFactory.newMarshalledEntry(key, map.get(key), null);
    }

    @Override
    public void start() {
        // empty
    }

    @Override
    public void stop() {
        // empty

    }

    @Override
    public boolean delete(Object o) {
        if (map.remove(o) != null)
            return true;
        return false;
    }

    @Override
    public void write(MarshalledEntry<? extends K, ? extends V> marshalledEntry) {
        map.put(marshalledEntry.getKey(), marshalledEntry.getValue());
    }

    @Override
    public void process(
            KeyFilter<? super K> arg0,
            org.infinispan.persistence.spi.AdvancedCacheLoader.CacheLoaderTask<K, V> arg1,
            Executor arg2, boolean arg3, boolean arg4) {
        throw new RuntimeException();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.entrySet().clear();
    }

    @Override
    public void purge(
            Executor arg0,
            org.infinispan.persistence.spi.AdvancedCacheWriter.PurgeListener<? super K> arg1) {
        throw new RuntimeException();
    }

}
