package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;

import java.io.Reader;
import java.io.IOException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
       Gson gson=new Gson();

       try(Reader reader = new FileReader( args[0] )) {
           JsonParser jsonParser = gson.fromJson(reader, JsonParser.class);
           Inventory inventory = Inventory.getInstance();
           inventory.load(jsonParser.getInventory());
           Squad squad = Squad.getInstance();
           Diary diary = Diary.getInstance();
           squad.load(jsonParser.getSquad());
           Services service = jsonParser.getServices();

           int mNumber = service.getM();
           int moneyPNumber = service.getMoneypenny();
           Intelligence[] intelligences = service.getIntelligence();
           int time = service.getTime();

           LinkedList<Thread> threads = new LinkedList<>();
           //init Q:
           threads.add(new Thread(new Q()));

           //init MoneyPenny:
           for (int i = 0; i < moneyPNumber; i++) {
               threads.add(new Thread(new Moneypenny(i)));
           }
           //init M:
           for (int i = 0; i < mNumber; i++) {
               threads.add(new Thread(new M(i)));
           }
           //init Intelligence:
           for (Intelligence newInfo : intelligences) {
               threads.add(new Thread(new Intelligence(newInfo.getMissions())));
           }
           GlobalCounter globalCounter = GlobalCounter.getInstance();
           globalCounter.setMax(threads.size());

           //all threads -> start()
           for (Thread t : threads) {
               t.start();
           }


           synchronized (globalCounter) {
               while (globalCounter.getCounter() < threads.size()) {


                      try {
                        globalCounter.wait();
                        //sleep(100);
                        }
                      catch (InterruptedException e) {

                      }
                   }
           }

           //init TimeService
           Thread timeService = new Thread(new TimeService(time)); //TODO: check is right
           timeService.start();


           for (Thread t : threads){
               t.join();
           }
           inventory.printToFile(args[1]);
           diary.printToFile(args[2]);

//           System.out.println("All participants initialized"); //TODO: delete before submission

       }
       catch (IOException | InterruptedException e){}



    }
}
