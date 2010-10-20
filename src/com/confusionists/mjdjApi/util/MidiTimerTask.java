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

package com.confusionists.mjdjApi.util;

import java.util.List;
import java.util.TimerTask;

import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.midi.ShortMessageWrapper;
import com.confusionists.mjdjApi.morph.Morph;

/**
 *  MjdjService instance has scheduling methods for this task. 
 * *  Instantiate a MidiTimerTask instance, init it, and then schedule it with your MjdjService instance. 
 * * Subclasses that do not use one of the given init methods must make sure all relevant instance variables have values.
 * */
public class MidiTimerTask extends TimerTask {

	private volatile MessageWrapper payload; // this must be declared as volatile so all changes to the payload are seen from all threads instantly
	protected transient MjdjService service;
	private Morph morph;
	protected transient List<String> deviceNames = null;
	private float afterBeat;


	/**
	 * @param service
	 *            an MjdjService instance (from the Mjdj system)
	 * @param payload
	 *            The MessageWrapper to be sent when the task is run.
	 * @param morph
	 *            This may be useful if a MidiTimerTask needs to access the
	 *            Morph instance.
	 */
	public void init(MjdjService service, MessageWrapper payload, Morph morph) {
		init(service, payload, morph, null);
	}

	/**
	 * @param service
	 * @param payload
	 * @param morph
	 * @param deviceNames
	 *            The device names for which the message is destined, as a List
	 *            of Strings.
	 */
	public void init(MjdjService service, MessageWrapper payload, Morph morph, List<String> deviceNames) {
		this.service = service;
		this.setPayload(payload);
		this.setMorph(morph);
		this.deviceNames = deviceNames;
	}

	/**
	 * Override this to set up future runs. It's too late to do it in the run
	 * method. This is the beat XX ms before the midi message is sent.
	 **/
	public void runOnBeatBefore() {
		// Override this to set up future runs if you wish
	}

	/**
	 * this method delivers the payload at the proportion of the beat after the
	 * beat as it was scheduled with the MjdjService instance. Subclasses may override this to do fancier
	 * and/or other functionality.
	 **/
	@Override
	public void run() {
		if (!morph.isPlayTimedTasks())
			return;
		if (getPayload().getMessage() != null)
			service.send(getPayload(), deviceNames);
		else if (getPayload().getKeystrokes() != null)
			service.sendKeystrokes(getPayload().getKeystrokes());
	}

	/**
	 * Standard getter
	 * 
	 * @return the MessageWrapper instance.
	 */
	public MessageWrapper getPayload() {
		return payload;
	}
	
	/**
	 * @return Whatever  {@link MessageWrapper#getAsShortMessageWrapper()} returns.
	 */
	public ShortMessageWrapper getPayloadAsShortMessageWrapper() {
		return payload.getAsShortMessageWrapper();
	}
	

	public void setPayload(MessageWrapper payload) {
		this.payload = payload;
	}

	public void setMorph(Morph morph) {
		this.morph = morph;
	}

	public Morph getMorph() {
		return morph;
	}

	/**
	 * @param afterBeat - proportion of a beat after which this task fires
	 */
	public void setAfterBeat(float afterBeat) {
		this.afterBeat = afterBeat;
	}

	/**
	 * @return the afterBeat returns the proportion of a beat after which this task fires. Useful for rescheduling the next one.
	 */
	public float getAfterBeat() {
		return afterBeat;
	}
}
