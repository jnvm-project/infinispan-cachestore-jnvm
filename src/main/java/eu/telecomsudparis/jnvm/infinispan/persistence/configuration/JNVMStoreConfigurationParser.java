package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;

import static eu.telecomsudparis.jnvm.infinispan.persistence.configuration.JNVMStoreConfigurationParser.NAMESPACE;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ConfigurationParser;
import org.infinispan.configuration.parsing.Namespace;
import org.infinispan.configuration.parsing.ParseUtils;
import org.infinispan.configuration.parsing.Parser;
import org.infinispan.commons.configuration.io.ConfigurationReader;
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
    public void readElement(ConfigurationReader reader, ConfigurationBuilderHolder holder) {
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

    private void parseJNVMStore(ConfigurationReader reader, JNVMStoreConfigurationBuilder builder) {
        String path = null;
        String relativeTo = null;
        for (int i=0; i < reader.getAttributeCount(); i++) {
            ParseUtils.requireNoNamespaceAttribute(reader, i);
            String value = reader.getAttributeValue(i);
            String attrName = reader.getAttributeName(i);
            Attribute attribute = Attribute.forName(attrName);

            switch (attribute) {
            /*
                case RELATIVE_TO:
                    relativeTo = ParseUtils.requireAttributeProperty(reader, i);
                    break;
                case PATH:
                    path = value;
                    break;
                case MAX_FILE_SIZE:
                    builder.maxFileSize(Long.parseLong(value));
                    break;
            */
                case MAX_ENTRIES:
                    builder.maxEntries(Integer.parseInt(value));
                    break;
                default:
                    Parser.parseStoreAttribute(reader, i, builder);
                    break;
            }
        }
        //ParseUtils.requireNoContent(reader);
        /*
        path = ParseUtils.resolvePath(path, relativeTo);
        if (path != null) {
            builder.location(path);
        }
        */
        while (reader.inTag()) {
            Element element = Element.forName(reader.getLocalName());
            switch (element) {
                default: {
                    Parser.parseStoreElement(reader, builder);
                }
            }
        }
    }

}
