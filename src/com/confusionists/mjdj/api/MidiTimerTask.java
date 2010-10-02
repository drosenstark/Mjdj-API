package com.confusionists.mjdj.api;

import java.util.List;
import java.util.TimerTask;

import com.confusionists.mjdj.api.morph.Morph;

/* MjdjService instance has scheduling methods for this task. */
public class MidiTimerTask extends TimerTask {

    protected volatile MessageWrapper payload;
    protected MjdjService service;
    protected Morph morph;
    protected List<String> deviceNames = null;
	public float afterBeat;	
                     

	// instantiate one, init it, and then schedule it with your MjdjService instance */
    public MidiTimerTask() { }
    
    public void init(MjdjService service, MessageWrapper payload, Morph morph) {
        init(service, payload, morph, null);
    }

    public void init(MjdjService service, MessageWrapper payload, Morph morph, List<String> deviceNames) {
        this.service = service;
        this.payload = payload;
        this.morph = morph;
        this.deviceNames = deviceNames;
    }

    /* Override this to set up future runs.  It's too late to do it in the run method! This is the beat XX ms before the midi message is sent. */
    public void runOnBeatBefore() {}

    /* this method delivers the payload at the milliseconds specified after the beat that it was scheduled on */
    @Override
    public void run() {
        if (payload.getKeystrokes() != null)
            service.sendKeystrokes(payload.getKeystrokes());
        if (payload.getMessage() != null)
            service.send(payload, deviceNames);
    }
    
    public MessageWrapper getPayload() { return payload; }
}
