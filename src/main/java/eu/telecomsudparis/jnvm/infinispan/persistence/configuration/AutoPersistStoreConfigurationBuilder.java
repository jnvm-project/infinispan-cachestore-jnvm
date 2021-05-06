package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.AutoPersistStoreConfiguration.SAMPLE_ATTRIBUTE;

import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;

public class AutoPersistStoreConfigurationBuilder extends AbstractStoreConfigurationBuilder<AutoPersistStoreConfiguration, AutoPersistStoreConfigurationBuilder>{

    public AutoPersistStoreConfigurationBuilder(
          PersistenceConfigurationBuilder builder) {
        super(builder, AutoPersistStoreConfiguration.attributeDefinitionSet());
        // TODO Auto-generated constructor stub
    }

    public AutoPersistStoreConfigurationBuilder sampleAttribute(String sampleAttribute) {
        // TODO Auto-generated method stub
        attributes.attribute(SAMPLE_ATTRIBUTE).set(sampleAttribute);
        return this;
    }

    @Override
    public AutoPersistStoreConfiguration create() {
        // TODO Auto-generated method stub
        return new AutoPersistStoreConfiguration(attributes.protect(), async.create(), singletonStore.create());
    }

    @Override
    public AutoPersistStoreConfigurationBuilder self() {
        // TODO Auto-generated method stub
        return this;
    }
}
