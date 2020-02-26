package eu.telecomsudparis.jnvm.infinispan.persistence;

import eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfigurationBuilder;

import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.persistence.spi.AdvancedLoadWriteStore;
import org.infinispan.persistence.spi.PersistenceException;
import org.infinispan.test.fwk.TestCacheManagerFactory;
import org.infinispan.persistence.BaseStoreTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;


@Test(testName = "JNVMStoreTest", groups = "functional")
public class JNVMStoreTest extends BaseStoreTest {

   @BeforeClass
   public static void setup() throws PersistenceException, IOException {
   }

   protected JNVMStoreConfigurationBuilder createCacheStoreConfig(PersistenceConfigurationBuilder lcb) {
      JNVMStoreConfigurationBuilder cfg = lcb.addStore(JNVMStoreConfigurationBuilder.class);
      return cfg;
   }

   @Override
   protected AdvancedLoadWriteStore createStore() throws Exception {
      JNVMStore js = new JNVMStore();
      ConfigurationBuilder cb = TestCacheManagerFactory.getDefaultCacheConfiguration(false);
      createCacheStoreConfig(cb.persistence());
      js.init(createContext(cb.build()));
      return js;
   }

   @Override
   protected boolean storePurgesAllExpired() {
      return false;
   }

}
