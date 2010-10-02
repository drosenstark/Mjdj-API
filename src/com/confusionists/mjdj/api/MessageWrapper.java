package com.confusionists.mjdj.api;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

/*
 * Wrap class for MidiMessage. 99% of the time, the MidiMessage is a ShortMidiMessage. 
 * It can also hold a String (keystrokes) which represents keystrokes to be typed into the active window. 
 */
public class MessageWrapper {

	/* Static Factory Methods if you wish */
	public static MessageWrapper newInstance(MidiMessage message) {
		return new MessageWrapper(message);
	}

	public static MessageWrapper newInstance(byte[] bytes) {
		return new MessageWrapper(bytes);
	}

	public static MessageWrapper newInstance(String keystrokes) {
		return new MessageWrapper(keystrokes);
	}

	public static MessageWrapper newInstance(ChannelCc channelCc, int data2) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(ShortMessage.CONTROL_CHANGE, channelCc.channel, channelCc.cc, data2);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
		return new MessageWrapper(message);
	}


	// MessageWrapper wraps either a MIdiMessage or keystrokes
	private MidiMessage message = null;
	private String keystrokes = null;

	public MessageWrapper() {
	}

	private MessageWrapper(MidiMessage message) {
		this.message = message;
	}

	private MessageWrapper(String keystrokes) {
		this.keystrokes = keystrokes;
	}

	private MessageWrapper(byte[] bytes) {
		message = new MidiMessage(bytes) {

			@Override
			// this is not right, but is unproblematic.
			public Object clone() {
				return null;
			}
		};
	}

	public String getKeystrokes() {
		return keystrokes;
	}

	public void setMessage(MidiMessage message) {
		this.message = message;
	}

	public MidiMessage getMessage() {
		return message;
	}

	
	/**
	 * @return The stored ShortMessage or null if the stored message isn't a ShortMessage
	 */
	public ShortMessage getShortMessage() {
		if (!(message instanceof ShortMessage))
			return null;
		return (ShortMessage) message;
	}

	public boolean isShortMessage() {
		return (message instanceof ShortMessage);
	}

	/**
	 * filter method
	 * @param command - second half of first byte of MidiMessage. -1 values mean accept any.
	 * @param channelCc - channel and data1 of the MidiMessage. -1 values mean accept any.
	 * @return true if the message matches the command and channelCc
	 */
	public boolean filter(int command, ChannelCc channelCc) {
		ShortMessage retVal = getShortMessage();
		if (retVal == null)
			return false;
		if (command != -1 && retVal.getCommand() != command) {
			return false;
		}
		if (channelCc == null) return false;
		if (retVal.getChannel() != channelCc.channel) {
			return false;
		}
		if (retVal.getData1() != channelCc.cc) {
			return false;
		}
		return true;
	}

	/**
	 * filter method
	 * command is assumed to be ShortMessage.CONTROL_CHANGE
	 * @param channelCc - channel and data1 of the MidiMessage. -1 values mean accept any.
	 * @return true if the message matches the command and channelCc
	 */
	public boolean filterControlCommand(ChannelCc channelCc) {
		return filter(ShortMessage.CONTROL_CHANGE, channelCc);
	}

	/* good to have around */
	public static final int getInt(byte value) {
		return 0xFF & value;
	}

	public void alter2(int data1) {
		try {
			ShortMessage sMessage = getShortMessage();
			if (sMessage == null)
				throw new InvalidMidiDataException("Trying to set data2 on a non-shortmessage");
			sMessage.setMessage(sMessage.getCommand(), sMessage.getChannel(), data1, sMessage.getData2());
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}

	}

	public void alter(int data2) {
		try {
			ShortMessage sMessage = getShortMessage();
			if (sMessage == null)
				throw new InvalidMidiDataException("Trying to set data2 on a non-shortmessage");
			sMessage.setMessage(sMessage.getCommand(), sMessage.getChannel(), sMessage.getData1(), data2);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
	}

	public int getData2() {
		if (this.getShortMessage() == null)
			throw new NullPointerException("Trying to get data2, but this is not a ShortMessage");
		return this.getShortMessage().getData2();
	}

	public int getData1() {
		if (this.getShortMessage() == null)
			throw new NullPointerException("Trying to get data2, but this is not a ShortMessage");
		return this.getShortMessage().getData1();
	}

	public int getCommand() {
		if (this.getShortMessage() == null)
			throw new NullPointerException("Trying to get command, but this is not a ShortMessage");
		return this.getShortMessage().getCommand();
	}

	public boolean isControlChange() {
		return getCommand() == ShortMessage.CONTROL_CHANGE;
	}

	public boolean isNoteOn() {
		return getCommand() == ShortMessage.NOTE_ON;
	}

	public boolean isNoteOff() {
		return getCommand() == ShortMessage.NOTE_OFF;
	}

	public MessageWrapper deepClone() {
		MessageWrapper retVal = null;
		if (message instanceof ShortMessage) {
			ShortMessage oldShortMessage = this.getShortMessage(); // speed is
																	// of the
																	// essence
			ShortMessage newShortMessage = new ShortMessage();
			try {
				newShortMessage.setMessage(oldShortMessage.getCommand(), oldShortMessage.getChannel(), oldShortMessage.getData1(), oldShortMessage
						.getData2());
			} catch (InvalidMidiDataException e) {
				// this should be impossible, since we are coming from a message
				// to a message
				e.printStackTrace();
			}
			retVal = new MessageWrapper(newShortMessage);
		} else {
			throw new AssertionError("deepClone can only be called on short messages");
		}
		return retVal;
	}

	@Override
	public String toString() {
		if (!this.isShortMessage())
			return super.toString();
		return this.getShortMessage().getChannel() + " " + this.getShortMessage().getData1() + " " + this.getShortMessage().getData2();
	}

}
