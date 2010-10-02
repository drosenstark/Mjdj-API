package com.confusionists.mjdj.api.midiDevice;

import com.confusionists.mjdj.api.MjdjService;



public interface DeviceWrapper {

	public void setService(MjdjService service);
	
	public MjdjService getService();
	
	public  void setActive(boolean active);

	public  boolean isActive();

	public  void close();

	public  void open() throws DeviceUnavailableException;

	public  String toString();

	public  String getName();


}