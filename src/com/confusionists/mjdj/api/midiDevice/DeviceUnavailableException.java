package com.confusionists.mjdj.api.midiDevice;

@SuppressWarnings("serial")
public class DeviceUnavailableException extends Exception {

	public DeviceUnavailableException(Throwable cause) {
		super(cause);
	}

	public DeviceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

}
