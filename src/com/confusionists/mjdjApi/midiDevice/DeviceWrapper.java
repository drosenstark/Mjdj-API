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

package com.confusionists.mjdjApi.midiDevice;

import java.util.ArrayList;

import com.confusionists.mjdjApi.util.MjdjService;

/**
 * Base interface for {@link ReceiverDeviceWrapper} and {@link TransmitterDeviceWrapper}. Implementers of devices must implement those two
 * interafaces (usually by extending  {@link AbstractReceiverDeviceWrapper}  and  {@link AbstractTransmitterDeviceWrapper}  
 * @author DanielRosenstark [at_sign] confusionists.com
 *
 */
public interface DeviceWrapper {

	void setService(MjdjService service);
	
	MjdjService getService();
	
	void setActive(boolean active);

	boolean isActive();

	void close();

	void open() throws DeviceUnavailableException;

	@Override
	String toString();

	 String getName();
	 
	 /**
	  * Implementers can just do nothing in this method if there is no UI to show 
	  */
	 void toggleUi();
	
      void makeNewId(ArrayList<String> existingIds);


}