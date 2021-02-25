package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfigurationParser.NAMESPACE;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamConstants;

import org.infinispan.commons.configuration.ConfigurationBuilderInfo;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ConfigurationParser;
import org.infinispan.configuration.parsing.Namespace;
import org.infinispan.configuration.parsing.Parser;
import org.infinispan.configuration.parsing.ParseUtils;
import org.infinispan.configuration.parsing.XMLExtendedStreamReader;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@Namespace(root = JNVMStoreConfigurationParser.ROOT_ELEMENT)
@Namespace(uri = NAMESPACE + "*", root = JNVMStoreConfigurationParser.ROOT_ELEMENT)
public class JNVMStoreConfigurationParser implements ConfigurationParser {

    public static final String ROOT_ELEMENT = "jnvm-store";
    //static final String NAMESPACE = Parser.NAMESPACE + "store:jnvm:";
    static final String NAMESPACE = "urn:infinispan:config:" + "store:jnvm:";

    public JNVMStoreConfigurationParser() {
    }

    @Override
    public void readElement(XMLExtendedStreamReader reader, ConfigurationBuilderHolder holder) throws XMLStreamException {
        ConfigurationBuilder builder = holder.getCurrentConfigurationBuilder();
        Element element = Element.forName(reader.getLocalName());
        switch (element) {
            case JNVM_STORE: {
                parseJNVMStore(reader, builder.persistence().addStore(JNVMStoreConfigurationBuilder.class));
                break;
            }
            default: {
                throw ParseUtils.unexpectedElement(reader);
            }
        }
    }

    @Override
    public Namespace[] getNamespaces() {
        return ParseUtils.getNamespaceAnnotations(getClass());
    }

    @Override
    public Class<? extends ConfigurationBuilderInfo> getConfigurationBuilderInfo() {
        return JNVMStoreConfigurationBuilder.class;
    }

    private void parseJNVMStore(XMLExtendedStreamReader reader, JNVMStoreConfigurationBuilder builder) throws XMLStreamException {
        for (int i=0; i < reader.getAttributeCount(); i++) {
            ParseUtils.requireNoNamespaceAttribute(reader, i);
            String value = reader.getAttributeValue(i);
            String attrName = reader.getAttributeLocalName(i);
            Attribute attribute = Attribute.forName(attrName);

            switch (attribute) {
                default:
                    Parser.parseStoreAttribute(reader, i, builder);
                    break;
            }
        }
        while (reader.hasNext() && (reader.nextTag() != XMLStreamConstants.END_ELEMENT)) {
            Element element = Element.forName(reader.getLocalName());
            switch (element) {
                default: {
                    Parser.parseStoreElement(reader, builder);
                }
            }
        }
    }

}
