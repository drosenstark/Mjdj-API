package com.confusionists.mjdj.api.midiDevice;

import com.confusionists.mjdj.api.MjdjService;

public abstract class DefaultDeviceWrapper implements DeviceWrapper {

	private boolean active;
	protected MjdjService service;

	@Override
	public void setService(MjdjService service) {
		this.service = service;
	}
	
	@Override
	public MjdjService getService() {
		return service;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}
	
    @Override
    public String toString() {
    		return getName();
    }
	

}
