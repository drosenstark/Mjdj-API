/*
Mjdj MIDI Morph - an extensible MIDI processor and translator.
Copyright (C) 2010 Confusionists, LLC (www.confusionists.com)

This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>. 

You may contact the author at mjdj_midi_morph [at] confusionists.com
*/
package com.confusionists.mjdjApi.morph.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.confusionists.mjdjApi.morph.AbstractMorphWithUI;
import com.confusionists.mjdjApi.morph.Morph;

import net.miginfocom.swing.MigLayout;



@SuppressWarnings("serial")
public class Ui extends JFrame {

	private Morph morph;
	public List<UiRow> rows = new ArrayList<UiRow>();
	JButton buttonUndo = new JButton("Undo");
	JButton buttonApply = new JButton("Close");
	List<String> inDevices;
	List<String> outDevices;
	private Object stateOnShow;	
	
	
	public Ui(AbstractMorphWithUI morph,  List<String> inDevices, List<String> outDevices)  {
		this.morph = morph;
		this.inDevices = inDevices;
		this.outDevices = outDevices;
		
		// SIZE is not set here, but rather check 		leftBox.setMaximumSize in the UIRow... we get it from them
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(),BoxLayout.Y_AXIS));
		
		UiRow row = new UiRow(this, inDevices, outDevices);
		rows.add(row);
		getContentPane().add(row);

		MigLayout migApplyButton = new MigLayout("", "[][]push[]", "");		
		JPanel applyButtonPanel = new JPanel(migApplyButton);
		applyButtonPanel.add(new JPanel());
		applyButtonPanel.setPreferredSize(rows.get(0).getPreferredSize());
		applyButtonPanel.add(buttonUndo, "gap push");
		applyButtonPanel.add(buttonApply, "gap push");
		getContentPane().add(applyButtonPanel);

		resizeEverything();
		setResizable(false);
		setAlwaysOnTop(true);
		
		buttonApply.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Ui.this.close();
				
			}
		});
		
		buttonUndo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Ui.this.undo();
				
			}
		});
		
	}

	

	private void resizeEverything() {
		Dimension sample = rows.get(0).getPreferredSize();
		this.setSize(new Dimension(sample.width, (int)(sample.height * rows.size() + sample.height * 1.5)));
		for (UiRow row : rows) {
			row.setStatus(rows); // so the row can display its button
		}
	}

	public void removeRow(UiRow uiRow) {
		rows.remove(uiRow);
		this.getContentPane().remove(uiRow);
		resizeEverything();
		repaint();
	}
	

	public void addRowAfter(UiRow uiRow) {
		UiRow row = new UiRow(this, inDevices, outDevices);
		int newIndex = rows.indexOf(uiRow) + 1;
		rows.add(newIndex,row);
		this.getContentPane().add(row, newIndex);
		resizeEverything();
	}
	
	public void addRow(UiRow row) {
		rows.add(row);
		this.getContentPane().add(row, rows.size() - 1);
		resizeEverything();
	}

	
	
	
//	public static void main(String[] args) {
//		ArrayList<String> inDevices = new ArrayList<String>();
//		ArrayList<String> outDevices = new ArrayList<String>();
//		for (int i=0; i<15; i++) {
//			inDevices.add("in " + i);
//			outDevices.add("out " + i);
//		}
//		
//		new Ui(new NullConnection(), inDevices, outDevices).setVisible(true);
//
//	}
	
	
	@Override
	public void setVisible(boolean visible) {
		if (!isVisible() && visible) {
			this.stateOnShow = morph.getSerializable();
		}
		super.setVisible(visible);
	}
	
	
	public void close() {
		setVisible(false);
	}

	public void undo() {
		morph.setSerializable(stateOnShow);
	}



	public void removeRows() {
		while (rows.size() > 1) {
			removeRow(rows.get(1));
		}
		
	}



	
	
	
}




