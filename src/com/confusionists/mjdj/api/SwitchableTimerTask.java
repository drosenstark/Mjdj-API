package com.confusionists.mjdj.api;


/* only fires and reschedules if this.morph playTimedTasks is on */
public class SwitchableTimerTask extends MidiTimerTask {

	@Override
	public void runOnBeatBefore() {
        if (morph.playTimedTasks) {
            super.runOnBeatBefore();
            SwitchableTimerTask task = new SwitchableTimerTask();
            task.init(service, this.payload, morph, deviceNames);
            service.schedule(task, 8, afterBeat);
        }
	}
	
    public void run() {
        if (morph.playTimedTasks) {
            super.run();
        }
    }
}
