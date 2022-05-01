package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.Element.JNVM_STORE;

import org.infinispan.commons.configuration.BuiltBy;
import org.infinispan.commons.configuration.ConfigurationFor;
import org.infinispan.commons.configuration.ConfigurationInfo;
import org.infinispan.commons.configuration.attributes.Attribute;
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

    //public static final AttributeDefinition<String> LOCATION = AttributeDefinition.builder(eu.telecomsudparis.jnvm.infinispan.persistence.configuration.Attribute.PATH, null, String.class).immutable().autoPersist(false).xmlName("path").build();
    //public static final AttributeDefinition<Long> MAX_FILE_SIZE = AttributeDefinition.builder(eu.telecomsudparis.jnvm.infinispan.persistence.configuration.Attribute.MAX_FILE_SIZE, 1024 *1024*1024L).immutable().autoPersist(false).build();
    public static final AttributeDefinition<Integer> MAX_ENTRIES = AttributeDefinition.builder(eu.telecomsudparis.jnvm.infinispan.persistence.configuration.Attribute.MAX_ENTRIES, 20_000_000).immutable().build();

    public static AttributeSet attributeDefinitionSet() {
        return new AttributeSet(JNVMStoreConfiguration.class, AbstractStoreConfiguration.attributeDefinitionSet(), MAX_ENTRIES); //LOCATION, MAX_FILE_SIZE, MAX_ENTRIES);
    }

    public static ElementDefinition ELEMENT_DEFINITION = new DefaultElementDefinition(JNVM_STORE.getLocalName(), true, false);

    //private final Attribute<String> location;
    //private final Attribute<Long> maxFileSize;
    private final Attribute<Integer> maxEntries;

    public JNVMStoreConfiguration(AttributeSet attributes, AsyncStoreConfiguration async) {
        super(attributes, async);
        //location = attributes.attribute(LOCATION);
        //maxFileSize = attributes.attribute(MAX_FILE_SIZE);
        maxEntries = attributes.attribute(MAX_ENTRIES);
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

    /*
    public string location() {
        return location.get();
    }

    public long maxFileSize() {
        return maxFileSize.get();
    }
    */

    public int maxEntries() {
        return maxEntries.get();
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
