package commsCommander;
import serialconnection.Datapool.*;
import java.lang.Thread;
public class TimerTrash implements Runnable {
public static long millis;
public static boolean run = false;

        public void run() {

             //   System.out.println(Thread.currentThread());
                System.out.println(millis);
                System.out.println(run);
                while(true){
                if (run){
                    break;
                  }
                SerialWriter.Send("read?");
               // System.out.println("sent");
                //System.out.println(Thread.currentThread());
                try {
					Thread.sleep(millis);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                }
        

        }
        public TimerTrash(double SEC) {
            // store parameter for later user
        	 millis = (long) (1000*SEC);
        }

        public static void ChangeThread(boolean changerun){
        	run = changerun;
        }
        
}
