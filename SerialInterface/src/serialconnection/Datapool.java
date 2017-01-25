package serialconnection;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import commsCommander.IOCommander;
import fileHandling.CSVFileHandling;

/**
 * This version of the TwoWaySerialComm example makes use of the 
 * SerialPortEventListener to avoid polling.
 *
 */

public class Datapool{
	//-- begin global vars
	public static int DataDestination = 0;
	public static boolean commActive;
	public static CommPort commPort;
	public static InputStream in;
	public static OutputStream out;
	//-- end global vars
	
    
    public void connect (String portName, int buadRate) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
        	
            System.out.println("Error: Port is currently in use");
        }
        else
        {
        	
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(buadRate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                               
                (new Thread(new SerialWriter(out))).start();
                serialPort.addEventListener(new SerialReader(in));
                serialPort.notifyOnDataAvailable(true);
        }
        
        }
 /*   public static void disconnect() {
        if (commPort != null) {
            try {
                // close the i/o streams.
            	
                out.close();
                in.close();
            } catch (IOException ex) {
                // don't care
            }
            // Close the port.
            commPort.close();
        }
    }
    */
    
    /**
     * Handles the input coming from the serial port. NL is the ending character.
     * 
     */
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        private byte[] buffer = new byte[1024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }
        
        public void serialEvent(SerialPortEvent arg0) {
            int data;
          
            try
            {
                int len = 0;
                while ( ( data = in.read()) > -1 )
                {
                    if ( data == '\n' ) {
                        break;
                    }
                    buffer[len++] = (byte) data;
                }
                //System.out.print(new String(buffer,0,len));
                //CSVFileHandling.AppendCSV(new String(buffer,0,len)); //prints data to be appended to CSV; moved to another part of the code for better handling
                SerialRead(new String(buffer,0,len));
                

            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }             
        }

    }

    /** */
    public static class SerialWriter implements Runnable 
    {
        static OutputStream out;
        
        public SerialWriter ( OutputStream out)
        {
            this.out = out;
            
        }
        public static void Send(String Command){
        	ArrayList<Character> listOfChars = new ArrayList<Character>();
        	try {
        		for(int i = 0; i < Command.length(); i++){
					listOfChars.add(Command.charAt(i));
        		}
        		for(int g = 0; g < listOfChars.size(); g++){
        			out.write((int) listOfChars.get(g));
        		}
        		//SerialWriter.out = out;
        		out.write(13);// 13 is CR 10 is LF
        		out.write(10);
                out.flush();
                listOfChars.clear();
            } catch (IOException e) {
                // TODO Auto-generated catch block; aka it's done for now :)
                e.printStackTrace();
                System.out.println("Oh Fiddlesticks! I could not write anything to the serial port or its buffer!");
            }
        }
        public void run ()
        {
            try
            {                
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }            
        }
    }
    
public static void SerialRead(String Recieved){
	if(Recieved.indexOf('E') != -1){ CSVFileHandling.AppendCSV(Recieved);
	System.out.println(Recieved);
	}//prints data to be appended to CSV
	else IOCommander.processCommand(Recieved);
}


    

}


