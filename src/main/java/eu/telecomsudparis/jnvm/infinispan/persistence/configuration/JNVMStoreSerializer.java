package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import org.infinispan.commons.configuration.io.ConfigurationWriter;
import org.infinispan.commons.util.Version;
import org.infinispan.configuration.serializing.AbstractStoreSerializer;
import org.infinispan.configuration.serializing.ConfigurationSerializer;


public class JNVMStoreSerializer extends AbstractStoreSerializer implements ConfigurationSerializer<JNVMStoreConfiguration> {

    @Override
    public void serialize(ConfigurationWriter writer, JNVMStoreConfiguration configuration) {
        writer.writeStartElement(Element.JNVM_STORE);
        writer.writeDefaultNamespace(JNVMStoreConfigurationParser.NAMESPACE + Version.getMajorMinor());
        configuration.attributes().write(writer);
        writeCommonStoreSubAttributes(writer, configuration);
        writeCommonStoreElements(writer, configuration);
        writer.writeEndElement();
    }

}
