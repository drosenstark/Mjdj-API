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

package com.confusionists.mjdjApi.midi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.ShortMessage;
import javax.swing.JButton;

import com.confusionists.mjdjApi.util.MjdjService;


/**
 * ChannelCc is a simple aggregate, since these two always go together in command messages. 
 * In more formal terms, they refer to "channel" and "data1".
 * @author DanielRosenstark [at_sign] confusionists.com
 *
 */
public class ChannelCc {
	public int channel;
	public int cc;
	
	public ChannelCc(int channel, int cc) {
		this.channel = channel;
		this.cc = cc;
	}

	public ChannelCc(ShortMessage message) {
		this.channel = message.getChannel();
		this.cc = message.getData1();
	}
	
	
	@Override
	public int hashCode() {
		return channel * 256 + cc;
	}
	@Override
	public boolean equals(final Object object) {
		if (object instanceof ChannelCc) {
			final ChannelCc  other = (ChannelCc)object;
			return  (other.cc == cc && other.channel == channel);
		} 
		return false;
	}
	
	public ChannelCc clone(final int newCc) {
		return new ChannelCc(channel, newCc);
	}
	public ChannelCc clonePlus(final int toAdd) {
		return new ChannelCc(channel, cc + toAdd);
	}

	/**
	 * @param service - an MjdjService instance.
	 * @param title
	 * @return a JButton for testing which sends a 127 on the present channelCc
	 */
	public JButton getButton(final MjdjService service, String title) {
		JButton retVal = new JButton(title);
		retVal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MessageWrapper message = MessageWrapper.newInstance(ChannelCc.this, 127);
				service.send(message);
			}
		});
		return retVal;
		
	}
	
	@Override
	public String toString() {
		return "channel=" + (channel+1) + ", cc=" + cc;
	}
	
	
}
