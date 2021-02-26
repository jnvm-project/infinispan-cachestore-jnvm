package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfiguration.MAX_ENTRIES;
//import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfiguration.MAX_FILE_SIZE;
//import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfiguration.LOCATION;

import org.infinispan.commons.configuration.Builder;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.commons.configuration.elements.ElementDefinition;
import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;

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

    public JNVMStoreConfigurationBuilder maxEntries(int maxEntries) {
        attributes.attribute(MAX_ENTRIES).set(maxEntries);
        return this;
    }

/*
    public JNVMStoreConfigurationBuilder maxFileSize(long maxFileSize) {
        attributes.attribute(MAX_FILE_SIZE).set(maxFileSize);
        return this;
    }

    public JNVMStoreConfigurationBuilder location(String location) {
        attributes.attribute(LOCATION).set(location);
        return this;
    }

    @Override
    public void validate() {
        super.validate();
    }

    @Override
    public void validate(GlobalConfiguration globalConfig) {
        super.validate(globalConfig);
    }

    @Override
    public Builder<?> read(JNVMStoreConfiguration template) {
        super.read(template);
        return this;
    }
*/

}
