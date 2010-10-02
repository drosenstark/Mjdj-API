package com.confusionists.mjdj.api.midiDevice;

public interface TransmitterDeviceWrapper extends DeviceWrapper {
	
	public void setClockSource(boolean value);
	public boolean getIsClockSource();

}
