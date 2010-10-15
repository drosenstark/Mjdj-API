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
package com.confusionists.mjdjApi.morph;

import java.util.List;

import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.util.MidiTimerTask;
import com.confusionists.mjdjApi.util.MjdjService;

public interface Morph {

	/**
	 * Call by Mjdj before init is called. Subclasses can generally inherit this default implementation.
	 * @param value is a List<String> of the in device names.
	 */
	void setInDeviceNames(List<String> value);

	List<String> getInDeviceNames();

	/**
	 * Call by Mjdj before init is called. Subclasses should generally inherit this default implementation.
	 * @param value is a List<String> of the in device names.
	 */
	void setOutDeviceNames(List<String> value);

	List<String> getOutDeviceNames();

	/**
	 * Mjdj will call this method to give the Morph instance access to the Mjdj system: this happens before init is called.
	 * @param service an MjdjService instance.
	 **/
	void setService(MjdjService service);

	/**
	 * @return name shown on Mjdj UI.
	 */
	String getName();

	/**
	 * 	/**
	 *  Call by the Mjdj system to allow for Morph initialization. Devices and service are already set.
	 * @throws DeviceNotFoundException if the Morph cannot load because it hasn't found the devices it needs.
	 */
	void init() throws DeviceNotFoundException;
	
	void shutdown();	
	
	/**
	 * 
	 * @param message
	 * @param from a String with the input device: Morph can decide to process or not based on this (as well as other information).
	 * @return true if the Morph did process the message, or false if the message was not of interest
	 * @throws Exception
	 */
	boolean process(MessageWrapper message, String from) throws Throwable;

	/**
	 * @return Any useful logging information that implementing classes wish to return when the Morph checks that it is working correctly.
	 */
	String diagnose();

	/**
	 * Subclasses can produce a UI that is as complicated as they like for each Morph. It should become visible (and even always-on-top) when this method is called, or the opposite.
	 */
	void toggleUi();

	/**
	 * This is very limited: any class can be serialized, even one that doesn't implement Serializable, but the class cannot contain any reference to classes
	 * dynamically loaded in this Morph. Essentially, use Hashtable<String, String> or anything in the JDK itself.
	 * @return the serializable "piece" for configuring this Morph.
	 */
	Object getSerializable();

	/**
	 * Morph subclasses should recover state from the object passed in.
	 * @param serializable
	 */
	void setSerializable(Object serializable);

	/**
	 * @param playTimedTasks Used by the default implementation of {@link MidiTimerTask} to play or not play: true by default
	 */
	void setPlayTimedTasks(boolean playTimedTasks);

	/**
	 * @return true by default. Otherwise returns whether the morph expects timed events to play or not. used by  {@link MidiTimerTask}.
	 */
	boolean isPlayTimedTasks();

}