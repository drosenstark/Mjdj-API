package com.confusionists.mjdjApi.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

public class ShortMessageWrapper extends MessageWrapper {

	
	
	protected ShortMessageWrapper(ShortMessage message) {
		super(message);
	}
	
	public ShortMessage getShortMessage() {
		return (ShortMessage)getMessage();
	}
	
	/**
	 * Convenience method to check if a message has a certain command, channel and data1 (cc).
	 * @param command - second half of first byte of MidiMessage. -1 value means accept any.
	 * @param channelCc - channel and data1 of the MidiMessage. -1 value means accept any.
	 * @return true if the message matches the command and channelCc, otherwise false. 
	 * <P>Will also return false if the message is not a ShortMessage instance.</P>
	 */
	public boolean filter(int command, ChannelCc channelCc) {
		if (channelCc == null) {
			return false;
		}
		final ShortMessage shortMessage = getShortMessage();
		
		// check backwards because it's usually faster
		if (channelCc.cc != -1 &&  shortMessage.getData1() != channelCc.cc) 
			return false;
		if (channelCc.channel != -1 && shortMessage.getChannel() != channelCc.channel) 
			return false;
		if (command != -1 && shortMessage.getCommand() != command) 
			return false;
		return true;
	}
	
	/**
	 * Same as filter (above), but here command is assumed to be ShortMessage.CONTROL_CHANGE
	 * @param channelCc - channel and data1 of the MidiMessage. -1 values mean accept any.
	 * @return true if the message matches the channelCc
	 */
	public boolean filterControlCommand(ChannelCc channelCc) {
		return filter(ShortMessage.CONTROL_CHANGE, channelCc);
	}
	
	
	/** 
	 * Replaces the guts of (the guts of) this wrapper.<BR>Particularly useful is more than one object shares this wrapper.
	 */
	public void alterCommand(int command) {
		try {
			final ShortMessage sMessage = getShortMessage();
			sMessage.setMessage(command, sMessage.getChannel(), sMessage.getData1(), sMessage.getData2());
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	
	/** 
	 * Replaces the guts of this wrapper.<BR>Particularly useful is more than one object shares this wrapper.
	 */
	public void alterData1(int data1) {
		try {
			final ShortMessage sMessage = getShortMessage();
			sMessage.setMessage(sMessage.getCommand(), sMessage.getChannel(), data1, sMessage.getData2());
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}

	}
	
	/** 
	 * Replaces the guts of this wrapper. Particularly useful is more than one object has a reference to this wrapper.
	 */
	public void alterData2(int data2) {
		try {
			final ShortMessage sMessage = getShortMessage();
			sMessage.setMessage(sMessage.getCommand(), sMessage.getChannel(), sMessage.getData1(), data2);
		} catch (InvalidMidiDataException e) { 
			// this will happen if values are below 0 or 127, etc. We just let the javax.sound.midi handle this.
			throw new RuntimeException(e);
		}
	}


	/**
	 * 
	 * @return data2 (note velocity in a note-on, parameter value in a command).
	 */
	public int getData2() {
		return this.getShortMessage().getData2();
	}

	public int getData1() {
		return this.getShortMessage().getData1();
	}

	public int getCommand() {
		return this.getShortMessage().getCommand();
	}

	public int getChannel() {
		return this.getShortMessage().getChannel();
	}
	
	
	public boolean isControlChange() {
		return getCommand() == ShortMessage.CONTROL_CHANGE;
	}
	
	public boolean isPitchBend() {
		return getCommand() == ShortMessage.PITCH_BEND;
	}
	

	public boolean isNoteOn() {
		return getCommand() == ShortMessage.NOTE_ON;
	}

	public boolean isNoteOff() {
		return getCommand() == ShortMessage.NOTE_OFF;
	}
	
	public boolean isNoteOnVolumeZero() {
		return isNoteOn() && getData2() == 0;
	}
	
	
	public boolean isSameChannelCcAs(ShortMessageWrapper wrapper) {
		if (wrapper.getChannel() != getChannel())
			return false;
		if (wrapper.getData1() != getData1())
			return false;
		return true;
	}

	/**
	 * This class does not clone the userData
	 * @return a clone with the guts cloned too
	 */
	public ShortMessageWrapper deepClone() {
		ShortMessageWrapper retVal = null;
		final ShortMessage oldShortMessage = this.getShortMessage(); 
		final ShortMessage newShortMessage = new ShortMessage();
		try {
			newShortMessage.setMessage(oldShortMessage.getCommand(), oldShortMessage.getChannel(), oldShortMessage.getData1(), oldShortMessage
					.getData2());
		} catch (InvalidMidiDataException e) {
			// this should be impossible, since we are coming from a message
			// to a message. Long live checked exceptions!
			e.printStackTrace(); // NOPMD by DanielRosenstark [at_sign] confusionists.com on 10/3/10 8:30 PM
		}
		retVal = new ShortMessageWrapper(newShortMessage);
		return retVal;
	}
	
	@Override
	/** 
	 * Convenience override for debugging.
	 */
	public String toString() {
		return (this.getShortMessage().getChannel()+1) + " " + this.getShortMessage().getData1() + " " + this.getShortMessage().getData2();
	}


}
