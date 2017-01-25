package serialconnection;
import java.util.ArrayList;

import gnu.io.*;
public class DiscoverPorts {
	
@SuppressWarnings({ "unchecked", "rawtypes" })
    public static ArrayList listPorts()
    {
	ArrayList ports = new ArrayList();
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while ( portEnum.hasMoreElements() ) 
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            if(getPortTypeName(portIdentifier.getPortType()) == true) ports.add(portIdentifier.getName());
        }
        return ports;
    }
    
    static boolean getPortTypeName (int portType)
    {
        switch (portType)
        {
            case CommPortIdentifier.PORT_SERIAL:
                return true;
            default:
                return false;
        }
    }
}
