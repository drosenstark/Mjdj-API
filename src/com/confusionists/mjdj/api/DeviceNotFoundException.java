package com.confusionists.mjdj.api;

/**
 * This class helps to avoid a dependence on  MidiUnavailableException et. al.
 * @author DanielRosenstark [at_sign] confusionists.com
 */
@SuppressWarnings("serial")
public class DeviceNotFoundException extends Exception {
	
	public String deviceName; 

	/**
	 * @param deviceName can be any String. This is used merely for logging and information purposes
	 */
public DeviceNotFoundException(String deviceName) {
		super("Device not found " + deviceName);
		this.deviceName = deviceName;
	}

}
