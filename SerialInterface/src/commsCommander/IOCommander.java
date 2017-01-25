package commsCommander;
import java.util.*;
public class IOCommander {
public static Runnable TimedSender;
/*planned, but not implemented currently
	public static int sendCommand(String command){
		switch(command){
		case "DCV":
		SerialWriter.Send("conf:volt:dc_min;conf:auto 1");
		
		break;
		
		case "ACV":
		break;
		
		}
		return 0;
	}
	*/
	public static void processCommand(String toProcess){
		
	//Stub method for invalid returns 	
	}
	public static void RequestData(){


}
	public static void SRQData(boolean run, double SEC){
		if(run){
		TimerTrash.ChangeThread(false);
		TimedSender = new TimerTrash(SEC);
		new Thread(TimedSender).start();
		}else{
			TimerTrash.ChangeThread(true);
		}
	
	
}}