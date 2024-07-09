package lu.kbra.talking.client.frame.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.data.Channel;
import lu.kbra.talking.packets.C2S_S2C_ChangeChannelPacket;

public class ChannelsPanel extends JPanel {

	private JList channelList;

	public ChannelsPanel() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(200, 400));

		channelList = new JList(new String[] { "Channel 1", "Channel 2", "Channel 3" });
		add(new JScrollPane(channelList), BorderLayout.CENTER);

		// Add double-click listener to the JList
		channelList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (channelList.getSelectedValue() != null) {
						String selectedChannel = (String) channelList.getSelectedValue();
						onChannelDoubleClicked(selectedChannel);
					}
				}
			}
		});
	}

	public void updateList() {
		if (TalkingClient.INSTANCE.isConnected() && TalkingClient.INSTANCE.getServerData() != null) {
			channelList.setListData(TalkingClient.INSTANCE.getServerData().getChannels().keySet().toArray());
		}
	}

	private void onChannelDoubleClicked(String channelName) {
		if (TalkingClient.INSTANCE.isConnected()) {
			Channel channel = TalkingClient.INSTANCE.getServerData().getChannels().values().stream().filter(c -> c.getName().equalsIgnoreCase(channelName)).findFirst().get();
			TalkingClient.INSTANCE.getClient().write(C2S_S2C_ChangeChannelPacket.to(channel.getUuid()));
		}
	}

}
