package lu.kbra.talking.client.frame.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class StatisticsPanel extends JPanel {
	
	private JTextArea statisticsArea;

	public StatisticsPanel() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(200, 100));

		statisticsArea = new JTextArea("Statistics about the current channel will be shown here.");
		statisticsArea.setEditable(false);
		add(new JScrollPane(statisticsArea), BorderLayout.CENTER);
	}
	
}