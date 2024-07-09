package lu.kbra.talking.client.frame.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.PrintStream;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogsPanel extends JPanel {

	private JTextArea logsArea;

	public LogsPanel() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(800, 100));

		logsArea = new JTextArea(new String());
		logsArea.setEditable(false);
		add(new JScrollPane(logsArea), BorderLayout.CENTER);

		System.setOut(new PrintStream(new CustomOutputStream(logsArea, 150)));
	}

	public JTextArea getLogsArea() {
		return logsArea;
	}

}
