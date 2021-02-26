package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfiguration.SAMPLE_ATTRIBUTE;

import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;

public class JNVMStoreConfigurationBuilder extends AbstractStoreConfigurationBuilder<JNVMStoreConfiguration, JNVMStoreConfigurationBuilder>{

    public JNVMStoreConfigurationBuilder(
          PersistenceConfigurationBuilder builder) {
        super(builder, JNVMStoreConfiguration.attributeDefinitionSet());
        // TODO Auto-generated constructor stub
    }

    public JNVMStoreConfigurationBuilder sampleAttribute(String sampleAttribute) {
        // TODO Auto-generated method stub
        attributes.attribute(SAMPLE_ATTRIBUTE).set(sampleAttribute);
        return this;
    }

    @Override
    public JNVMStoreConfiguration create() {
        // TODO Auto-generated method stub
        return new JNVMStoreConfiguration(attributes.protect(), async.create());
    }

    @Override
    public JNVMStoreConfigurationBuilder self() {
        // TODO Auto-generated method stub
        return this;
    }
}
