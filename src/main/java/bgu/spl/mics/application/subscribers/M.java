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
				diary.printToFile("diaryOutputFile.json");
				terminate();
			} else {
				currentTick = tick.getTickNumber();
			}
		});
		subscribeEvent(MissionReceivedEvent.class,(MissionReceivedEvent e)->{
			MissionInfo info = e.getInfo();
			while (true) { // while() is implemented in order to abort if something fails. only 1 iteration is executed
				Future<Map<String,Object>> tryAcquireAgents = publish.sendEvent(new AgentsAvailableEvent(info.getSerialAgentsNumbers()));
				if ( tryAcquireAgents.get() == null || (Integer) tryAcquireAgents.get().get("acquired") == 0) {
					break;
				}
				Future<Map<String,Integer>> tryAcquireGadget = publish.sendEvent(new GadgetAvailableEvent(info.getGadget()));
				if ( tryAcquireGadget.get()==null || tryAcquireGadget.get().get("acquired") == 0 || currentTick >= info.getTimeExpired()) {
					publish.sendEvent(new ReleaseAgentsEvent(info.getSerialAgentsNumbers()));
					break;
				}

				publish.sendEvent(new SendAgentsEvent(info.getSerialAgentsNumbers(),info.getDuration()));
				Report report = createReport(info, tryAcquireAgents, tryAcquireGadget);
				//ass method to creat report & make code readable
				diary.addReport(report);
				break;	//finished handling the mission
			}
			diary.incrementTotal(); //incrementing whether it succeed or not
			//end of callback
		});
		
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
		report.setMissionName(info.getMissionName());
		report.setM(this.serial);
		report.setMoneypenny((Integer) tryAcquireAgents.get().get("serial"));
		report.setAgentsSerialNumbersNumber(info.getSerialAgentsNumbers());
		report.setAgentsNames((List<String>) tryAcquireAgents.get().get("names")); //TODO ALON: check what is wrong
		report.setGadgetName(info.getGadget());
		report.setTimeIssued(info.getTimeIssued());
		report.setQTime(tryAcquireGadget.get().get("timeTick"));
		report.setTimeCreated(this.currentTick);
		return report;
	}

}
