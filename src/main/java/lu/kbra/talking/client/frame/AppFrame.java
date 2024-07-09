package lu.kbra.talking.client.frame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.client.frame.menus.CustomMenuBar;
import lu.kbra.talking.client.frame.panels.ChannelsPanel;
import lu.kbra.talking.client.frame.panels.LogsPanel;
import lu.kbra.talking.client.frame.panels.MessagesPanel;
import lu.kbra.talking.client.frame.panels.StatisticsPanel;
import lu.kbra.talking.consts.Consts;

public class AppFrame extends JFrame {

	public static AppFrame INSTANCE = null;

	private ChannelsPanel channelsPanel;
	private StatisticsPanel statisticsPanel;
	private LogsPanel logsPanel;
	private MessagesPanel messagesPanel;
	private CustomMenuBar customMenuBar;

	public AppFrame() {
		super();

		INSTANCE = this;

		setTitle(Consts.NAME + " - " + Consts.VERSION);
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		channelsPanel = new ChannelsPanel();
		statisticsPanel = new StatisticsPanel();
		logsPanel = new LogsPanel();
		messagesPanel = new MessagesPanel();
		customMenuBar = new CustomMenuBar();

		setJMenuBar(customMenuBar);

		JSplitPane content_Properties_Pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, messagesPanel, statisticsPanel);
		content_Properties_Pane.setResizeWeight(0.8);

		JSplitPane contentProperties_Logs_Pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, content_Properties_Pane, logsPanel);
		contentProperties_Logs_Pane.setResizeWeight(0.8);

		JSplitPane channels_contentPropertiesLogs_Pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, channelsPanel, contentProperties_Logs_Pane);
		channels_contentPropertiesLogs_Pane.setResizeWeight(0.2);

		add(channels_contentPropertiesLogs_Pane, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quitRequested();
			}
		});

		setVisible(true);
	}

	public void quitRequested() {
		TalkingClient.INSTANCE.disconnect();
	}

	public ChannelsPanel getChannelsPanel() {
		return channelsPanel;
	}

	public StatisticsPanel getStatisticsPanel() {
		return statisticsPanel;
	}

	public LogsPanel getLogsPanel() {
		return logsPanel;
	}

	public MessagesPanel getMessagesPanel() {
		return messagesPanel;
	}

	public CustomMenuBar getCustomMenuBar() {
		return customMenuBar;
	}

}