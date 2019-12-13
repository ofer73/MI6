package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;



import java.io.FileReader;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {

        Gson gson = new Gson();
        try (Reader reader = new FileReader("C:\\Users\\mosesofe\\Desktop\\SPL\\Ass2\\input201 - 2.json")) {
            // Convert JSON File to Java Object
            Inventory inventory= gson.fromJson(reader,Inventory.class);

            // print staff

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
}
