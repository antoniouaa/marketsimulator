package MarketSimulator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.util.*;

/**
*	The AdvancedSettings class is responsible for creating
*	the window holding the overcommitment trait for agents,
*	which is part of the settings UI.
*
*	@author Alexandros Antoniou
*/
@SuppressWarnings("serial")
public class AdvancedSettings extends JFrame implements ActionListener, ChangeListener {

	ArrayList<JSlider> sliders;
	JPanel riskPanel;
	JPanel buttonPanel;
	JPanel panel1;
	JPanel panel2;
	JPanel panel3;
	JSlider risk4_1;
	JSlider risk4_2;
	JSlider risk4_3;
	JButton apply;
	JButton cancel;
	JLabel labl1;
	JLabel labl2;
	JLabel labl3;
	TitledBorder advancedTitleBorder;
	EmptyBorder advancedMargins;
	GridLayout cellLayout;
	SimulationSettings theSimSet;

	AdvancedSettings(SimulationSettings simSet) {
		theSimSet = simSet;

		setTitle(MarketPlace.TITLE_STRING);
		showFrame();
	}

	/**
	*	All the components that are shown in the window are initialised here
	*	and subsequently added to the container
	*/
	void addComponents(Container pane) {

		sliders = new ArrayList<JSlider>();

		cellLayout = new GridLayout(1, 2);

		advancedTitleBorder = new TitledBorder(new LineBorder(Color.BLACK), "Advanced Settings");
		advancedMargins = new EmptyBorder(10, 10, 10, 10);

		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		panel1.setLayout(cellLayout);
		panel2.setLayout(cellLayout);
		panel3.setLayout(cellLayout);
		panel1.setBorder(advancedMargins);
		panel2.setBorder(advancedMargins);
		panel3.setBorder(advancedMargins);

		labl1 = new JLabel("% of agents with Overcommitment risk = 1");
		labl2 = new JLabel("% of agents with Overcommitment risk = 2");
		labl3 = new JLabel("% of agents with Overcommitment risk = 3");

		riskPanel = new JPanel();
		riskPanel.setBorder(new CompoundBorder(advancedMargins, advancedTitleBorder));
		riskPanel.setLayout(new GridLayout(3, 1));

		risk4_1 = new JSlider(JSlider.HORIZONTAL, 0, 100, 33);
		risk4_1.setMajorTickSpacing(25);
		risk4_1.setMinorTickSpacing(1);
		risk4_1.setPaintTicks(true);
		risk4_1.setPaintLabels(true);
		risk4_1.setSnapToTicks(true);
		risk4_1.addChangeListener(this);

		risk4_2 = new JSlider(JSlider.HORIZONTAL, 0, 100, 33);
		risk4_2.setMajorTickSpacing(25);
		risk4_2.setMinorTickSpacing(1);
		risk4_2.setPaintTicks(true);
		risk4_2.setPaintLabels(true);
		risk4_2.setSnapToTicks(true);
		risk4_2.addChangeListener(this);

		risk4_3 = new JSlider(JSlider.HORIZONTAL, 0, 100, 33);
		risk4_3.setMajorTickSpacing(25);
		risk4_3.setMinorTickSpacing(1);
		risk4_3.setPaintTicks(true);
		risk4_3.setPaintLabels(true);
		risk4_3.setSnapToTicks(true);
		risk4_3.addChangeListener(this);

		sliders.add(risk4_1);
		sliders.add(risk4_2);
		sliders.add(risk4_3);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(cellLayout);
		buttonPanel.setBorder(advancedMargins);

		apply = new JButton("Apply");
		apply.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);

		panel1.add(labl1);
		panel1.add(risk4_1);

		panel2.add(labl2);
		panel2.add(risk4_2);

		panel3.add(labl3);
		panel3.add(risk4_3);

		riskPanel.add(panel1);
		riskPanel.add(panel2);
		riskPanel.add(panel3);

		buttonPanel.add(apply);
		buttonPanel.add(cancel);

		pane.add(riskPanel, BorderLayout.NORTH);
		pane.add(buttonPanel, BorderLayout.SOUTH);
	}

	void showFrame() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addComponents(this.getContentPane());
		this.pack();
	}

	/**
	*	Removing and adding the slider listeners temporarily is key in 
	*	being able to adjust them automatically
	*/
	void removeListeners() {
		risk4_1.removeChangeListener(this);
		risk4_2.removeChangeListener(this);
		risk4_3.removeChangeListener(this);
	}

	void addListeners() {
		risk4_1.addChangeListener(this);
		risk4_2.addChangeListener(this);
		risk4_3.addChangeListener(this);
	}

	/**
	*	Every time a slider is moved, the other sliders need to adjust
	*	to a value totalling 100
	*/
	void updateSliders(JSlider sl) {
		if (sl==risk4_1) {
			removeListeners();
			int val = 100 - sl.getValue();
			if (val%2==0) {
				risk4_2.setValue(2*val/3); 
				risk4_3.setValue(val/3 + 1);
			} else {
				risk4_2.setValue((val-1)/3);
				risk4_3.setValue(2*(val-1)/3 + 1);
			}
			addListeners();
		} else if (sl==risk4_2) {
			removeListeners();
			int val = 100 - sl.getValue();
			if (val%2==0) {
				risk4_3.setValue(2*val/3);
				risk4_1.setValue(val/3 + 1);
			} else {
				risk4_1.setValue((val-1)/3);
				risk4_3.setValue(2*(val-1)/3 + 1);
			}
			addListeners();
		} else if (sl==risk4_3) {
			removeListeners();
			int val = 100 - sl.getValue();
			if (val%2==0) {
				risk4_2.setValue(val/3 + 1); 
				risk4_1.setValue(2*val/3);
			} else {
				risk4_1.setValue(2*(val-1)/3);
				risk4_2.setValue((val-1)/3 + 1);
			}
			addListeners();
		}
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (!source.getValueIsAdjusting()) {
			updateSliders(source);
		}
	}

	public void resetSliders() {
		removeListeners();
		risk4_1.setValue(33);
		risk4_2.setValue(33);
		risk4_3.setValue(33);
		addListeners();
	}

	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		if (source==cancel) {
			this.dispose();
		}
		if (source==apply) {
			int ocA = risk4_1.getValue();
			int ocB = risk4_2.getValue();
			int ocC = risk4_3.getValue();
			theSimSet.setOvercommitRisks(ocA, ocB, ocC);
			this.dispose();
		}
	}
}