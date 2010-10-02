package com.confusionists.mjdj.api;

import java.util.List;

import javax.sound.midi.InvalidMidiDataException;

/* Do NOT implement MjdjService: MJDJ makes one and hands it to you */
public abstract class MjdjService {

	/* Morphs use these methods to display to the Mjdj out */
	public abstract void log(String text);
	public abstract void log(String string, Exception e);
	
	
	/* Morphs use these methods to display to the Mjdj out if debugging is on */
	public abstract void debugLog(String text);
	public abstract void debugLog(String string, Exception e);

	/* used by MIDI devices to send messages into Mjdj for morphing */
	public abstract void morph(MessageWrapper message, String from);
	
	/* sends to all outbound MIDI devices, resample=true  */
	public abstract void send(MessageWrapper message);

	/* sends to all outbound MIDI devices with names in `to` list, resample=true  */
	public abstract void send(MessageWrapper message, List<String> to);

	public abstract void send(MessageWrapper message, String to, boolean resample);

	/* 
	 * sends to all outbound MIDI devices with names in `to` list. Resample specifies whether the 
	 * message gets sent to other Morphs (like loopers or robots) that want to resample
	 */
	public abstract void send(MessageWrapper message, List<String> to, boolean resample);

	public abstract void send(MessageWrapper message, String to);

	public abstract void send(byte[] bytes, List<String> to)
			throws InvalidMidiDataException;

	public abstract void send(byte[] bytes) throws InvalidMidiDataException;

	public abstract void setMorphActive(String name, boolean status);

	public abstract boolean isMorphActive(String name);

	/*
	 * sends most keystrokes, otherwise just use Java.awt.Robot yourself, no
	 * problems
	 */
	public abstract void sendKeystrokes(String keystrokes);

	public static final String toHexString(int thatInt) {
		return Integer.toHexString(thatInt);
	}

	public static final String spitByteArrayAsHex(byte[] bytes) {
		StringBuffer retVal = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int val = 0xFF & bytes[i];
			retVal.append(MjdjService.toHexString(val) + " ");
		}
		return retVal.toString();
	}

	/* returns true if scheduled, false if not */
	public abstract boolean schedule(MidiTimerTask task, float beatsBeforeLaunch);

	/* returns true if scheduled, false if not */
	public abstract boolean schedule(MidiTimerTask task, float beatsBeforeLaunch,
			float lastDelayAfterBeat);
	 
	public abstract boolean scheduleInMs(MidiTimerTask task, int delay);
	
	public abstract boolean isDebug();

}
