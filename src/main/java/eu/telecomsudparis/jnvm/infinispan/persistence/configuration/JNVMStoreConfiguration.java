package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.Element.JNVM_STORE;

import org.infinispan.commons.configuration.BuiltBy;
import org.infinispan.commons.configuration.ConfigurationFor;
import org.infinispan.commons.configuration.ConfigurationInfo;
import org.infinispan.commons.configuration.attributes.AttributeDefinition;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.commons.configuration.elements.DefaultElementDefinition;
import org.infinispan.commons.configuration.elements.ElementDefinition;
import org.infinispan.configuration.cache.AbstractStoreConfiguration;
import org.infinispan.configuration.cache.AsyncStoreConfiguration;
import org.infinispan.configuration.cache.AbstractSegmentedStoreConfiguration;
import org.infinispan.configuration.serializing.SerializedWith;
import org.infinispan.persistence.spi.InitializationContext;

import java.util.List;
import java.util.Arrays;

import eu.telecomsudparis.jnvm.infinispan.persistence.JNVMStore;

@BuiltBy(JNVMStoreConfigurationBuilder.class)
@ConfigurationFor(JNVMStore.class)
@SerializedWith(JNVMStoreSerializer.class)
public class JNVMStoreConfiguration extends AbstractSegmentedStoreConfiguration<JNVMStoreConfiguration> implements ConfigurationInfo {

    static final AttributeDefinition<String> SAMPLE_ATTRIBUTE = AttributeDefinition.builder("sampleAttribute", null, String.class).immutable().build();

    public static AttributeSet attributeDefinitionSet() {
        return new AttributeSet(JNVMStoreConfiguration.class, AbstractStoreConfiguration.attributeDefinitionSet(), SAMPLE_ATTRIBUTE);
    }

    public static ElementDefinition ELEMENT_DEFINITION = new DefaultElementDefinition(JNVM_STORE.getLocalName(), true, false);

    public JNVMStoreConfiguration(AttributeSet attributes, AsyncStoreConfiguration async) {
        super(attributes, async);
    }

    @Override
    public ElementDefinition getElementDefinition() {
        return ELEMENT_DEFINITION;
    }

    @Override
    public List<ConfigurationInfo> subElements() {
        return Arrays.asList(async());
    }

    @Override
    public JNVMStoreConfiguration newConfigurationFrom(int segment, InitializationContext ctx) {
        AttributeSet set = JNVMStoreConfiguration.attributeDefinitionSet();
        set.read(attributes);
        return new JNVMStoreConfiguration(set.protect(), async());
    }

    @Override
    public AttributeSet attributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "JNVMStoreConfiguration [attributes=" + attributes + "]";
    }
}
