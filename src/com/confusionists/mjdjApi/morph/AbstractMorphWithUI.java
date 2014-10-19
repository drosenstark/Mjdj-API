/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdjApi.morph;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.confusionists.mjdjApi.midi.MessageWrapper;
import com.confusionists.mjdjApi.morph.ui.Ui;
import com.confusionists.mjdjApi.morph.ui.UiRow;


public abstract class AbstractMorphWithUI extends AbstractMorph {
    private final static String NONE = "None";
	private final static String ANY = "All Active";
	Ui ui = null;
	List<String> inDeviceNames;
	List<String> outDeviceNames;
	
	
	public abstract boolean allowInputFromOtherMorphs();
	public abstract boolean allowOutputToOtherMorphs();
	
 
	@Override
	public void init() throws DeviceNotFoundException {
		assert(ui == null);
		
		ui = new Ui(this, getInDeviceNames(), getOutDeviceNames());
	}
			
	@Override
	public void toggleUi() {
		ui.setVisible(true);
		
	}
	
	@Override
	public List<String> getInDeviceNames() {
		if (inDeviceNames == null) {
			List<String> original = super.inDeviceNames;
			inDeviceNames = new ArrayList<String>(original);
            inDeviceNames.add(0, ANY);
            inDeviceNames.add(0, NONE);
			if (this.allowInputFromOtherMorphs()) {
				inDeviceNames.add(OTHER_MORPHS);
			}
		}
		// TODO Auto-generated method stub
		return inDeviceNames;
	}
	
	@Override
	public List<String> getOutDeviceNames() {
		if (outDeviceNames == null) {
			List<String> original = super.outDeviceNames;
			outDeviceNames = new ArrayList<String>(original);
            outDeviceNames.add(0, ANY);
            outDeviceNames.add(0, NONE);
			if (this.allowOutputToOtherMorphs()) {
				outDeviceNames.add(OTHER_MORPHS);
			}
		}
		// TODO Auto-generated method stub
		return outDeviceNames;
	}

	
	

	protected abstract boolean processAndSend(MessageWrapper message, String from, String to);
	
	
	protected boolean defaultProcessAndSend(MessageWrapper message, String to) {
		getService().send(message, to);
		return true;
	}

	protected abstract boolean processAndSendToOtherMorphs(MessageWrapper message);
	
	protected  boolean defaultProcessAndSendToOtherMorphs(MessageWrapper message) {
		getService().morph(message, OTHER_MORPHS, this);
		return true;
	}
	
	
	@Override
	public boolean process(MessageWrapper message, String from) throws Exception {
		
		
		// we should put in a safeguard for not sending twice, but for now we'll leave it alone
		boolean retVal = false;
		for (UiRow row : ui.rows) {
			boolean enabled = row.isEnabled();
			String leftName = row.getLeftName();
			String rightName = row.getRightName();
			if (enabled &&  (leftName==ANY || leftName.equals(from))) {
				String toName = rightName == ANY ? null : rightName;
				if (toName == OTHER_MORPHS) {
					if (processAndSendToOtherMorphs(message)) {
						retVal = true;
					}
				} else {
					if (processAndSend(message, from, toName)) {
						retVal = true;
					}
				}
			}
		}
		
		return retVal;
	}

    public void sendToAllFeedback(MessageWrapper message) {
        for (UiRow row : ui.rows) {
            boolean enabled = row.isEnabled();
            String leftName = row.getLeftName();
            if (enabled) {
                String toName = leftName == ANY ? null : leftName;
                if (toName != OTHER_MORPHS) {
                    getService().send(message, toName);
                }
            }
        }

    }

	public void sendToAll(MessageWrapper message) {
		for (UiRow row : ui.rows) {
			boolean enabled = row.isEnabled();
			String rightName = row.getRightName();
			if (enabled) {
				String toName = rightName == ANY ? null : rightName;
				if (toName != OTHER_MORPHS) {
					getService().send(message, toName);
				}
			}
		}
		
	}

	@Override
	public String diagnose() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSerializable() { 
		ArrayList<Hashtable<String, String>> retVal = new ArrayList<Hashtable<String, String>>(); // this monstrosity is because XStream can't get access to our classes since they're not loaded
		if (ui == null) return null; // for testing mostly
		for (UiRow row : ui.rows) {
			retVal.add(row.getSerializable());
		}
		return retVal;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setSerializable(Object value) {
		if (value == null)
			return;
		ui.removeRows();
		ArrayList<Hashtable<String, String>>  serializable = (ArrayList<Hashtable<String, String>>)value;
		boolean isFirst = true;
		for (Hashtable<String, String> sRow : serializable) {
			UiRow row;

			if (isFirst) 
				row = ui.rows.get(0);
			else
				row = new UiRow(ui, getInDeviceNames(), getOutDeviceNames());
			
			row.setSerializable(sRow);
			
			if (isFirst) 
				isFirst = false;
			else
				ui.addRow(row);
		}
		
	}
	

}

