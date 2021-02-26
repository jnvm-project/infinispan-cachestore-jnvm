package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import javax.xml.stream.XMLStreamException;

import org.infinispan.commons.util.Version;
import org.infinispan.configuration.serializing.AbstractStoreSerializer;
import org.infinispan.configuration.serializing.ConfigurationSerializer;
import org.infinispan.configuration.serializing.XMLExtendedStreamWriter;


public class JNVMStoreSerializer extends AbstractStoreSerializer implements ConfigurationSerializer<JNVMStoreConfiguration> {

    @Override
    public void serialize(XMLExtendedStreamWriter writer, JNVMStoreConfiguration configuration) throws XMLStreamException {
        writer.writeStartElement(Element.JNVM_STORE);
        writer.writeDefaultNamespace(JNVMStoreConfigurationParser.NAMESPACE + Version.getMajorMinor());
        configuration.attributes().write(writer);
        writeCommonStoreSubAttributes(writer, configuration);
        writeCommonStoreElements(writer, configuration);
        writer.writeEndElement();
    }

}
