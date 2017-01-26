package commsCommander;
import serialconnection.Datapool.*;
import java.lang.Thread;
@SuppressWarnings("unused") // for Dev branch
public class TimerTrash implements Runnable {
public static long millis;
public static boolean stop = false;
public static int runMode = 0, points;
private static int pointsAquired = 0;
        public void run() {

             //   System.out.println(Thread.currentThread()); Debug stuff
              //  System.out.println(millis);
         //       System.out.println(stop);
               pointsAquired=0;
               switch(runMode){
               case 0:
            	   while(!stop){
            	   SerialWriter.Send("read?");
                   try {
   					Thread.sleep(millis);
   				} catch (InterruptedException e) {
   					// always going to try it out, but if I can't I'll catch, but I'll still not stop around and desert you! But I will make you cry, maybe even say goodbye, but I won't tell a lie and hurt you!
   					e.printStackTrace();
   				}
                   }
            	   break;
               case 1:
               case 2:
            	   for(pointsAquired = 0; pointsAquired < points && !stop; pointsAquired++){
            		   SerialWriter.Send("read?");
            		  // System.out.print(pointsAquired);
                       try {
       					Thread.sleep(millis);
       				} catch (InterruptedException e) {
       					e.printStackTrace();
       				}
            	   }
            	   
            	   break;
                }
               }
   
        public TimerTrash(double SEC, int mode, double parameter) {
            
        	 
        	 switch(mode){
        	 /*
        	  * Configures the mode for sampling
        	  * mode -1: only changes mode, ignores seconds
        	  * mode 0: free running acquisition
        	  * mode 1: sets to X sample mode, acquires X points then stops requesting more and thread closes; parameter is the number of points
        	  * mode 2: time calculates how many points are needed at their delayed execution; parameter is the amount of time in minutes, the number of points is computed from that.
        	  */
        	 case 0:
        		 millis = (long) (1000*SEC);// store millis for later use
        		 runMode = 0;
        		 points = -1;
        		 break;
        	 case 1:
        		 millis = (long) (1000*SEC);// store millis for later use
        		 runMode =1;
        		 points =(int) parameter+1;
        		 break;
        	 case 2:
        		 millis = (long) (1000*SEC);// store millis for later use
        		 runMode = 2;
        		 points = (int) Math.ceil(((60*parameter)/SEC));
        		// System.out.println(points);
        		 break;
        	 
        	 }
        	 
        }

        public static void ChangeThread(boolean changerun){
        	stop = changerun;
        }
        
}
