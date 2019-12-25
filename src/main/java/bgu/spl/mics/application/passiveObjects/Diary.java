package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {

	//TODO ALON: some implements in 19.12, not finished
	private List<Report> reports = new LinkedList<>();
	private AtomicInteger total = new AtomicInteger(0);
	//not sure if the right impl for the list


	/**
	 * Private class for singleton implementation
	 */
	private static class DiaryHolder {
		private static Diary instance = new Diary();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return DiaryHolder.instance;
	}

	/**
	 *  @return the list of the report
	 */

	public List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		if(reportToAdd!=null) {
			synchronized (reports) {
				reports.add(reportToAdd);
			}
		}
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		try {
			System.out.println("	diary -> printToFile()"); //TODO: delete before submission
			Writer writer = new FileWriter(filename);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(this,writer);
			writer.flush();
			writer.close();

		}
		catch (IOException e){}
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return total.get();
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		total.incrementAndGet();
	}
}
