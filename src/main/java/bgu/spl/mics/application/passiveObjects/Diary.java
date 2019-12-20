package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
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
	private List<Report> reportList = new LinkedList<Report>();
	private AtomicInteger total;
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
		synchronized (reportList){
			return reportList;
		} //TODO check if sync is needed
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public void addReport(Report reportToAdd){
		//TODO: Implement this
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public void printToFile(String filename){
		//TODO: Implement this
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		//TODO: Implement this
		return total.get();
	}
}
