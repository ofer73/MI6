package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.SimplePublisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.Map;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
	private int currentTick;
	private int serial;
	private Diary diary;


	public M(int serial) {
		super("M");
		this.serial = serial;
		this.currentTick = 0;
		diary = Diary.getInstance();
	}

	@Override
	protected void initialize() {
		SimplePublisher publish = getSimplePublisher();
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
			if (tick.isFinalTick()) {
				System.out.println("terminate M " + serial + " executed"); //TODO: delete before submission
				//diary.printToFile("diaryOutputFile.json");
				terminate();
			} else {
				currentTick = tick.getTickNumber();
			}
		});
		subscribeEvent(MissionReceivedEvent.class,(MissionReceivedEvent e)->{
			MissionInfo info = e.getInfo();
			diary.incrementTotal(); //incrementing whether it succeed or not

			boolean isSucceed = false; //TODO ALON: added 22.12 11:00
			while (true) { // while() is implemented in order to abort if something fails. only 1 iteration is executed
				System.out.println("M " + serial + ": " + info.getName() + " -> start() \n" +
						"					requires: "+ e.getInfo().getGadget().toString() + " gadget, " + e.getInfo().getSerialAgentsNumbers().toString() + "agents \n 						M " + serial + " CurrentTick: " + currentTick ); //TODO: delete before submission


				Future<Map<String,Object>> tryAcquireAgents = publish.sendEvent(new AgentsAvailableEvent(info.getSerialAgentsNumbers(),info.getDuration() ) );

				if ( tryAcquireAgents == null || tryAcquireAgents.get() == null ) {
					System.out.println("M " + serial + " failed " + info.getName() + " : tryAcquireAgents + TERMINATE()"); //TODO: delete before submission
					terminate();
					//diary.printToFile("diaryOutputFile.json");
					break;
				} //it's a sign that Armageddon is here

				if ((Integer) tryAcquireAgents.get().get("acquired") == 0) {
					System.out.println("M " + serial + " failed " + info.getName() + " : tryAcquireAgents"); //TODO: delete before submission
					break;
				}

				Future<Map<String,Integer>> tryAcquireGadget = publish.sendEvent(new GadgetAvailableEvent(info.getGadget()));
				System.out.println((tryAcquireGadget == null) + " YUVAL YUVAL"); //TODO DELETE



				if ( tryAcquireGadget == null || tryAcquireGadget.get()==null) {
					System.out.println("	Yuval Margalit 42"); //TODO: delete before submission
					System.out.println("M " + serial + " failed " + info.getName() + " : tryAcquireGadget + TERMINATE()"); //TODO: delete before submission
					((Future<Boolean>) tryAcquireAgents.get().get("future")).resolve(null); //TODO: ALON 23.12 NEW IMPL
					terminate();
					//diary.printToFile("diaryOutputFile.json");
					break;
				}  //it's a sign that Armageddon is here
				System.out.println("	Yuval Margalit"); //TODO: delete before submission

				if ( tryAcquireGadget.get().get("acquired") == 0 || currentTick >= info.getTimeExpired()) {

					System.out.println("M " + serial + " failed " + info.getName() + " : tryAcquireGadget"); //TODO: delete before submission
					((Future<Boolean>) tryAcquireAgents.get().get("future")).resolve(false); //TODO: ALON 23.12 NEW IMPL
					break;
				}

				//all conditions ok, mission to be executed

				//TODO: ALON: 23.12 15:00 NEW IMPLEMENTATION Ofer Added 24.12
				if(tryAcquireGadget.get().get("acquired") == 1 ) {
					((Future<Boolean>) tryAcquireAgents.get().get("future")).resolve(true);
					//publish.sendEvent(new SendAgentsEvent(info.getSerialAgentsNumbers(),info.getDuration())); //TODO DELETE LINE


					Report report = createReport(info, tryAcquireAgents, tryAcquireGadget);
					//ass method to creat report & make code readable
					System.out.println("M " + serial + " add to report: " + info.getName()); //TODO: delete before submission
					diary.addReport(report);

					//TODO: ALON: 22.12 11:00
					isSucceed = true;
					break;    //finished handling the mission
				}

			}
			complete(e, isSucceed);//TODO: ALON: 22.12 11:00
			System.out.println("M " + serial + ": " + info.getName() + " -> end()"); //TODO: delete before submission
			System.out.println("M " + serial + ": succeeded mission " + info.getName() + "? " + isSucceed); //TODO: delete before submission


			//end of callback
		});
		System.out.println("M " + serial + " initialized"); //TODO: delete before submission
	}

	/**
	 * Assistant method to fill the report of the mission
	 * @param info : the information received from Intelligence
	 * @param tryAcquireAgents : Future of AgentAvailableEvent
	 * @param tryAcquireGadget : Future of GadgetAvailableEvent
	 * @return the report to be filled
	 */

	private Report createReport(MissionInfo info, Future<Map<String, Object>> tryAcquireAgents, Future<Map<String, Integer>> tryAcquireGadget) {
		Report report = new Report();
		report.setMissionName(info.getName());
		report.setM(this.serial);
		report.setMoneypenny((Integer) tryAcquireAgents.get().get("serial"));
		report.setAgentsSerialNumbersNumber(info.getSerialAgentsNumbers());
		report.setAgentsNames((List<String>) tryAcquireAgents.get().get("names")); //TODO ALON: check what is wrong
		report.setGadgetName(info.getGadget());
		report.setTimeIssued(info.getTimeIssued());
		report.setqTime(tryAcquireGadget.get().get("timeTick"));
		report.setTimeCreated(this.currentTick);
		return report;
	}

}
