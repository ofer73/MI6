package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import com.google.gson.Gson;

import java.io.Reader;
import java.io.IOException;
import java.io.FileReader;
import java.util.Arrays;

/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
       Gson gson=new Gson();

       try(Reader reader = new FileReader("/users/studs/bsc/2020/mosesofe/IdeaProjects/MI6/inut201-2.json")){
           JsonParser jparser = gson.fromJson(reader,JsonParser.class);
           Inventory inventory = Inventory.getInstance();
           inventory.load(jparser.getInventory());
           Squad squad = Squad.getInstance();
           squad.load(jparser.getSquad());
           Services service = jparser.getServices();
       }


       catch (IOException e){}
    }
}
