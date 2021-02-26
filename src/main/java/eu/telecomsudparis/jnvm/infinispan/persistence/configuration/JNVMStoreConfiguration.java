package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import org.infinispan.commons.configuration.BuiltBy;
import org.infinispan.commons.configuration.ConfigurationFor;
import org.infinispan.commons.configuration.attributes.AttributeDefinition;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.configuration.cache.AbstractStoreConfiguration;
import org.infinispan.configuration.cache.AsyncStoreConfiguration;

import eu.telecomsudparis.jnvm.infinispan.persistence.JNVMStore;

@BuiltBy(JNVMStoreConfigurationBuilder.class)
@ConfigurationFor(JNVMStore.class)
public class JNVMStoreConfiguration extends AbstractStoreConfiguration {

    static final AttributeDefinition<String> SAMPLE_ATTRIBUTE = AttributeDefinition.builder("sampleAttribute", null, String.class).immutable().build();

    public static AttributeSet attributeDefinitionSet() {
        return new AttributeSet(JNVMStoreConfiguration.class, AbstractStoreConfiguration.attributeDefinitionSet(), SAMPLE_ATTRIBUTE);
    }

    public JNVMStoreConfiguration(AttributeSet attributes, AsyncStoreConfiguration async) {
        super(attributes, async);
        // TODO Auto-generated constructor stub
    }
}
