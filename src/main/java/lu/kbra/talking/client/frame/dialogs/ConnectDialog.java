package lu.kbra.talking.client.frame.dialogs;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.consts.Consts;

public class ConnectDialog extends JDialog {

	private JTextField usernameField;
	private JPasswordField secretField;
	private JTextField ipField;
	private JTextField portField;
	private JButton connectButton;
	private JButton cancelButton;

	public ConnectDialog(JFrame parent) {
		super(parent, "Connect to Server", true);

		setLayout(new GridLayout(5, 2));

		JLabel usernameLabel = new JLabel("Username:");
		usernameField = new JTextField();

		JLabel secretLabel = new JLabel("Secret:");
		secretField = new JPasswordField();

		JLabel ipLabel = new JLabel("IP Address:");
		ipField = new JTextField();

		JLabel portLabel = new JLabel("Port (optional):");
		portField = new JTextField(Integer.toString(Consts.DEFAULT_PORT));

		connectButton = new JButton("Connect");
		cancelButton = new JButton("Cancel");

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectToServer();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		add(usernameLabel);
		add(usernameField);
		add(secretLabel);
		add(secretField);
		add(ipLabel);
		add(ipField);
		add(portLabel);
		add(portField);
		add(connectButton);
		add(cancelButton);

		pack();
		setLocationRelativeTo(parent);
	}

	private void connectToServer() {
		String username = usernameField.getText().trim();
		String ip = ipField.getText().trim();
		String password = new String(secretField.getPassword());
		int port;

		if (username.isEmpty() || ip.isEmpty() || password.isEmpty()) {
			JOptionPane.showConfirmDialog(this, "Username, password and IP Address are required.", "Error", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			port = Integer.parseInt(portField.getText().trim());
		} catch (NumberFormatException e) {
			port = Consts.DEFAULT_PORT;
		}

		try {
			TalkingClient.INSTANCE.connect(username, password, ip, port);
			dispose();
		} catch (Exception e) {
			JOptionPane.showConfirmDialog(null, "An error occured while trying to connect: " + e.getMessage() + ".", "Could not connect", JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

}
