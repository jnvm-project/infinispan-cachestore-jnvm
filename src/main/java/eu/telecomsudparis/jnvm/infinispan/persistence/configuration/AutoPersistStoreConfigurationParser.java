package eu.telecomsudparis.jnvm.infinispan.persistence.configuration;
import javax.xml.stream.XMLStreamException;

import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ConfigurationParser;
import org.infinispan.configuration.parsing.Namespace;
import org.infinispan.configuration.parsing.XMLExtendedStreamReader;


public class AutoPersistStoreConfigurationParser implements ConfigurationParser {

    @Override
    public Namespace[] getNamespaces() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void readElement(XMLExtendedStreamReader arg0,
            ConfigurationBuilderHolder arg1) throws XMLStreamException {
        // TODO Auto-generated method stub

    }

}
