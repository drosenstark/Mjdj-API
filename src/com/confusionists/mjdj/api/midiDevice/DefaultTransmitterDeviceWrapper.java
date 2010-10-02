package com.confusionists.mjdj.api.midiDevice;

public abstract class DefaultTransmitterDeviceWrapper extends DefaultDeviceWrapper implements TransmitterDeviceWrapper {

	boolean clocksource; // whether this instance is being used as the clocksource my Mjdj

	@Override
	public boolean getIsClockSource() {
		return clocksource;
	}

	@Override
	public void setClockSource(boolean value) {
		this.clocksource = value;
	}


}
