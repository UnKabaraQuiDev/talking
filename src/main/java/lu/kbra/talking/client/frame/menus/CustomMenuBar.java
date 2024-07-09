package lu.kbra.talking.client.frame.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import lu.kbra.talking.client.TalkingClient;
import lu.kbra.talking.client.frame.AppFrame;
import lu.kbra.talking.client.frame.dialogs.ConnectDialog;
import lu.kbra.talking.consts.Consts;

public class CustomMenuBar extends JMenuBar {

	private JMenu serverMenu;
	private JMenu editMenu;
	private JMenu infoMenu;

	public CustomMenuBar() {
		serverMenu = new JMenu("Server");
		editMenu = new JMenu("Edit");
		infoMenu = new JMenu("Info");

		// Server Menu
		JMenuItem connectItem = new JMenuItem("Connect");
		connectItem.addActionListener((e) -> showConnectionDialog());
		JMenuItem disconnectItem = new JMenuItem("Disconnect");
		disconnectItem.addActionListener((e) -> disconnect());
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener((e) -> quit());
		serverMenu.add(connectItem);
		serverMenu.add(disconnectItem);
		serverMenu.add(exitItem);

		// Edit Menu
		JMenuItem preferencesItem = new JMenuItem("Preferences");
		preferencesItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openPreferences();
			}
		});
		editMenu.add(preferencesItem);

		// Info Menu
		JMenuItem versionItem = new JMenuItem("Version: " + Consts.VERSION);
		versionItem.setEnabled(false);
		infoMenu.add(versionItem);

		add(serverMenu);
		add(editMenu);
		add(infoMenu);
	}

	private void showConnectionDialog() {
		JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
		ConnectDialog dialog = new ConnectDialog(parent);
		dialog.setVisible(true);
	}

	private void disconnect() {
		TalkingClient.INSTANCE.disconnect();
	}

	private void openPreferences() {
		JOptionPane.showMessageDialog(null, "Preferences functionality is not implemented yet.");
	}
	
	private void quit() {
		AppFrame parent = (AppFrame) SwingUtilities.getWindowAncestor(this);
		parent.quitRequested();
	}

}
