package com.confusionists.mjdj.api;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.midi.ShortMessage;
import javax.swing.JButton;


/**
 * ChannelCc is a simple aggregate, since these two always go together in command messages. 
 * They refer to "channel" and "data1" more formally
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
	
	public boolean equals(ChannelCc  other) {
		return  (other.cc == cc && other.channel == channel);
	}
	
	public ChannelCc clone(int newCc) {
		return new ChannelCc(channel, newCc);
	}
	public ChannelCc clonePlus(int toAdd) {
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
		return "channel=" + channel + ", cc=" + cc;
	}
	
	
}
