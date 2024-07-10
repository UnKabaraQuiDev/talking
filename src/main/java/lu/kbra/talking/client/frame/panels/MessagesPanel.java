package lu.kbra.talking.client.frame.panels;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.client.frame.data.Message;
import lu.kbra.talking.client.frame.renderer.MessageCellRenderer;
import lu.kbra.talking.packets.C2S_S2C_MessagePacket;
import lu.kbra.talking.packets.C2S_AskServerTrustPacket;

public class MessagesPanel extends JPanel {

	private DefaultListModel<Message> messagesListModel;
	private JList<Message> messagesList;
	private JPopupMenu contextMenu;

	private JTextField inputField;

	public MessagesPanel() {
		setLayout(new BorderLayout());

		messagesListModel = new DefaultListModel<>();
		messagesList = new JList<>(messagesListModel);
		messagesList.setCellRenderer(new MessageCellRenderer());
		add(new JScrollPane(messagesList), BorderLayout.CENTER);

		contextMenu = new JPopupMenu();
		JMenuItem copyItem = new JMenuItem("Copy");
		JMenuItem deleteItem = new JMenuItem("Delete (client)");
		JMenuItem userTrustUserItem = new JMenuItem("Trust user (client)");
		JMenuItem serverTrustUserItem = new JMenuItem("Ask server trust");
		contextMenu.add(copyItem);
		contextMenu.add(deleteItem);
		contextMenu.add(userTrustUserItem);
		contextMenu.add(serverTrustUserItem);

		// Add action listeners for menu items
		copyItem.addActionListener(e -> copyMessage());
		deleteItem.addActionListener(e -> deleteMessage());
		userTrustUserItem.addActionListener(e -> addToTrustedUsersMessage());
		serverTrustUserItem.addActionListener(e -> askServerTrust());

		JButton sendButton = new JButton(">>");
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				send();
			}
		});

		messagesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int index = messagesList.locationToIndex(e.getPoint());
					messagesList.setSelectedIndex(index);
					contextMenu.show(messagesList, e.getX(), e.getY());
				}
			}
		});

		inputField = new JTextField();
		inputField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick();
			}
		});

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(inputField, BorderLayout.CENTER);
		inputPanel.add(sendButton, BorderLayout.EAST);
		add(inputPanel, BorderLayout.SOUTH);
	}

	private void askServerTrust() {
		Message selectedMessage = messagesList.getSelectedValue();
		if (selectedMessage == null) {
			return;
		}
		
		TalkingClient.INSTANCE.getClient().write(C2S_AskServerTrustPacket.ask(selectedMessage.getSenderUuid()));
	}

	private void addToTrustedUsersMessage() {
		Message selectedMessage = messagesList.getSelectedValue();
		if (selectedMessage != null) {
			TalkingClient.INSTANCE.addClientTrusted(selectedMessage.getSenderUuid());
		}
	}

	private void copyMessage() {
		Message selectedMessage = messagesList.getSelectedValue();
		if (selectedMessage != null) {
			StringSelection stringSelection = new StringSelection(selectedMessage.getContent());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
	}

	private void deleteMessage() {
		int selectedIndex = messagesList.getSelectedIndex();
		if (selectedIndex != -1) {
			messagesListModel.remove(selectedIndex);
		}
	}

	protected void send() {
		final String msg = inputField.getText().trim();

		inputField.setText("");

		if (msg.isEmpty()) {
			return;
		}

		for (C_RemoteUserData u : TalkingClient.INSTANCE.getServerData().getRemoteUsers()) {
			TalkingClient.INSTANCE.getClient().write(C2S_S2C_MessagePacket.c2s(u, msg));
		}

		addMessage("You (" + TalkingClient.INSTANCE.getUserData().getUserName() + ")", null, msg, true);
	}

	public void addMessage(String username, UUID sender, String content, boolean isSentByUser) {
		Optional<C_RemoteUserData> remoteUser = TalkingClient.INSTANCE.getServerData().getRemoteUsers().stream().filter((c) -> c.getUUID().equals(sender)).findAny();

		if (remoteUser.isPresent()) {
			C_RemoteUserData u = remoteUser.get();
			messagesListModel.addElement(new Message(username, u.isServerTrusted(), TalkingClient.INSTANCE.isClientTrusted(u), content, isSentByUser, u.getUUID()));
		} else {
			messagesListModel.addElement(new Message(username, false, isSentByUser ? true : false, content, isSentByUser, null));
		}
	}

	public void clearMessages() {
		messagesListModel.clear();
	}

	public JList<Message> getMessagesList() {
		return messagesList;
	}

	public JTextField getInputField() {
		return inputField;
	}

}
