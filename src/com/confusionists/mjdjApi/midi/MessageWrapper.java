/**
Mjdj MIDI Morph API - Extension API for Mjdj MIDI Morph, an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)
Licensed with GPL 3.0 with Classpath Exception

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

Linking this library statically or dynamically with other modules is making a combined work based on this library. Thus, the terms and conditions of the GNU General Public License cover the whole combination. 

As a special exception, the copyright holders of this library give you permission to link this library with independent modules to produce an executable, regardless of the license terms of these independent modules, and to copy and distribute the resulting executable under terms of your choice, provided that you also meet, for each linked independent module, the terms and conditions of the license of that module. An independent module is a module which is not derived from or based on this library. If you modify this library, you may extend this exception to your version of the library, but you are not obligated to do so. If you do not wish to do so, delete this exception statement from your version.

You may contact the author at mjdj_api [at] confusionists.com
*/

package com.confusionists.mjdjApi.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;


/**
 * <p>MessageWrapper class for MidiMessage. See  {@link ShortMessageWrapper}.</p> 
 * <p>It can also hold a String (keystrokes) which represents keystrokes to be typed into the active window. </p>
 * <p>It holds either keystrokes <b>or</b> a MIDI message, but not both.</p>
 * <p>Client classes should use one of the factory methods to get an instance. </p>
 */
public class MessageWrapper { 

	/**
	 *  Static Factory Method 
	 */
	public static MessageWrapper newInstance(MidiMessage message) {
		if (message instanceof ShortMessage)
			return new ShortMessageWrapper((ShortMessage)message);
		return new MessageWrapper(message);
	}

	/**
	 *  Static Factory Method 
	 */
	public static MessageWrapper newInstance(byte[] bytes) {
		return new MessageWrapper(bytes);
	}

	/**
	 *  Static Factory Method 
	 */
	public static MessageWrapper newInstance(String keystrokes) {
		return new MessageWrapper(keystrokes);
	}

	/**
	 *  Static Factory Method 
	 */
	public static ShortMessageWrapper newInstance(ChannelCc channelCc, int data2) {
		final ShortMessage message = new ShortMessage();
		try {
			message.setMessage(ShortMessage.CONTROL_CHANGE, channelCc.channel, channelCc.cc, data2);
		} catch (InvalidMidiDataException e) {
			throw new RuntimeException(e);
		}
		return new ShortMessageWrapper(message);
	}


	// MessageWrapper wraps either a MIdiMessage or keystrokes
	private MidiMessage message = null;
	private String keystrokes = null; 
	/**
	 * used by Morphs to add functionality without subclassing
	 */
	public Object userData; 
	
	

	protected MessageWrapper(MidiMessage message) {
		this.message = message;
	}

	protected MessageWrapper(String keystrokes) {
		this.keystrokes = keystrokes;
	}

	protected MessageWrapper(byte[] bytes) {
		message = new SimpleMidiMessage(bytes.clone());
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
	 *  good to have around: from MidiMessage documentation: 
	 * 
	 * <code>MidiMessage</code> returns MIDI status bytes as integers.  If you are
	 * processing MIDI data that originated outside Java Sound and now
	 * is encoded as signed bytes, the bytes can
	 * can be converted to integers using this conversion:
	 * <center><code>int i = (int)(byte & 0xFF)</code></center>
	 *  
	 **/
	public static int getInt(byte value) {
		return 0xFF & value;
	}
	
	/**
	 * Convenience method.
	 * @return null if cast cannot be performed.
	 */
	public ShortMessageWrapper getAsShortMessageWrapper() {
		if (this instanceof ShortMessageWrapper)
			return (ShortMessageWrapper)this;
		else
			return null;
	}





	@Override
	public String toString() {
			return super.toString();
	}

}
