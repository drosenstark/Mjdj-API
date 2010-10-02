/*
This file is part of the Mjdj API
The Mjdj API is the public interface for the Mjdj MIDI Morpher 
(www.confusionists.com/mjdj)
Copyright (C) 2009-2011 Daniel Rosenstark

Mjdj API is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
("GPL") version 2 as published by the Free Software Foundation.
See the file LICENSE.TXT for the full text of the GNU GPL, or see
http://www.gnu.org/licenses/gpl.txt

For using Mjdj API with software that can not be combined with 
the GNU GPL or any other queries, please contact Daniel Rosenstark 
(license_mjdj@confusionists.com).
*/

package com.confusionists.mjdj.api.morph;

import java.util.List;
import java.util.Timer;

import com.confusionists.mjdj.api.DeviceNotFoundException;
import com.confusionists.mjdj.api.MessageWrapper;
import com.confusionists.mjdj.api.MjdjService;

/**
 * Morph is the abstract base class for implementers of Midi Translators. 
 * @author DanielRosenstark [at_sign] confusionists.com
 *
 */
public abstract class Morph implements Comparable<Morph> {

	/* subclasses sum these to get their priority */
	public static final int MATCHES_DEVICE = 1;
	public static final int MATCHES_CHANNEL = 1;
	public static final int MATCHES_COMMAND = 1;
	public static final int MATCHES_CC_NUMBER = 1;
	private final Timer timer = new Timer();

	public Timer getTimer() {
		return timer;
	}

	protected boolean active = false;
	public boolean isDead = false; //don't touch this, please, we use it on startup
	/* Morph subclasses do NOT override these */
	public boolean playTimedTasks = false;
	protected MjdjService service = null;
	
	List<String> inDeviceNames;
	List<String> outDeviceNames;
	
	/**
	 * Call by Mjdj before init is called. Subclasses can generally inherit this default implementation.
	 * @param value is a List<String> of the in device names.
	 */
	public void setInDeviceNames(List<String> value) {
		this.inDeviceNames = value;
	}

	public List<String> getInDeviceNames() {
		return this.inDeviceNames;
	}
	
	/**
	 * Call by Mjdj before init is called. Subclasses should generally inherit this default implementation.
	 * @param value is a List<String> of the in device names.
	 */
	public void setOutDeviceNames(List<String> value) {
		this.outDeviceNames = value;
	}

	public List<String> getOutDeviceNames() {
		return this.outDeviceNames;
	}

	public final boolean isActive() {
		return active;
	}

	public final void setActive(boolean active) {
		this.active = active;
	}

	
	/**
	 * Mjdj will call this method to give the Morph instance access to the Mjdj system: this happens before init is called.
	 * @param service an MjdjService instance.
	 **/
	public void setService(MjdjService service) {
		this.service = service;
	}

	/** 
	 * subclasses override these: HIGHER number is higher priority. Sum the constants MATCHES_DEVICE etc. to get the match group. 
	 **/
	public abstract int getMatchGroup();

	
	/**
	 * @return name shown on Mjdj UI.
	 */
	public abstract String getName();

	/**
	 *  Call by the Mjdj system to allow for Morph initialization. Devices and service are already set.
	 *  
	 **/
	public abstract void init() throws DeviceNotFoundException;

	/**
	 * 
	 * @param message
	 * @param from a String with the input device: Morph can decide to process or not based on this (as well as other information).
	 * @return true if the Morph did process the message, or false if the message was not of interest
	 * @throws Exception
	 */
	public abstract boolean process(MessageWrapper message, String from) throws Exception;

	@Override
	/*
	 * These are used as sorted rules (order of specificity
	 */
	public final int compareTo(Morph o) {
		// backwards on purpose, we sort from high to low
		int retVal = new Integer(o.getMatchGroup()).compareTo(new Integer(getMatchGroup()));
		return retVal;
	}

	/**
	 * @return Any useful logging information that happens when the Morph checks that it is working correctly.
	 */
	public abstract String diagnose();

	/**
	 * Subclasses can produce a UI that is as complicated as they like for each Morph. It should become visible (and even always-on-top) when this method is called, or the opposite.
	 */
	public void toggleUi() {
		service.log(getName() + " has no UI to show.");
	}
	
	/**
	 * This is very limited: any class can be serialized, even one that doesn't implement Serializable, but the class cannot contain any reference to classes
	 * dynamically loaded in this Morph. Essentially, use Hashtable<String, String> or something similar.
	 * @return
	 */
	public abstract Object getSerializable();

	/**
	 * Morph subclasses should recover state from the object passed in.
	 * @param serializable
	 */
	public abstract void setSerializable(Object serializable);
		
}
