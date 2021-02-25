package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.commons.configuration.elements.ElementDefinition;
import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;

public class JNVMStoreConfigurationBuilder extends AbstractStoreConfigurationBuilder<JNVMStoreConfiguration, JNVMStoreConfigurationBuilder>{

    public JNVMStoreConfigurationBuilder(PersistenceConfigurationBuilder builder) {
        super(builder, JNVMStoreConfiguration.attributeDefinitionSet());
    }

    public JNVMStoreConfigurationBuilder(PersistenceConfigurationBuilder builder, AttributeSet attributeSet) {
        super(builder, attributeSet);
    }

    @Override
    public JNVMStoreConfiguration create() {
        return new JNVMStoreConfiguration(attributes.protect(), async.create());
    }

    @Override
    public ElementDefinition getElementDefinition() {
        return JNVMStoreConfiguration.ELEMENT_DEFINITION;
    }

    @Override
    public AttributeSet attributes() {
        return attributes;
    }

    @Override
    public JNVMStoreConfigurationBuilder self() {
        return this;
    }
}
