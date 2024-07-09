package lu.kbra.talking.client.frame.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.client.data.C_RemoteUserData;
import lu.kbra.talking.client.frame.data.Message;
import lu.kbra.talking.client.frame.renderer.MessageCellRenderer;
import lu.kbra.talking.packets.C2S_S2C_MessagePacket;

public class MessagesPanel extends JPanel {

	private DefaultListModel<Message> messagesListModel;
	private JList<Message> messagesList;

	private JTextField inputField;

	public MessagesPanel() {
		setLayout(new BorderLayout());

		messagesListModel = new DefaultListModel<>();
		messagesList = new JList<>(messagesListModel);
		messagesList.setCellRenderer(new MessageCellRenderer());
		add(new JScrollPane(messagesList), BorderLayout.CENTER);

		JButton sendButton = new JButton(">>");
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				send();
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

	protected void send() {
		final String msg = inputField.getText().trim();

		inputField.setText("");

		if (msg.isEmpty()) {
			return;
		}

		for (C_RemoteUserData u : TalkingClient.INSTANCE.getServerData().getRemoteUsers()) {
			TalkingClient.INSTANCE.getClient().write(C2S_S2C_MessagePacket.c2s(u, msg));
		}

		addMessage("You (" + TalkingClient.INSTANCE.getUserData().getUserName() + ")", msg);
	}

	public void addMessage(String username, String content) {
		Message message = new Message(username, content);
		messagesListModel.addElement(message);
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
