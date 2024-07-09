package lu.kbra.talking.client.frame.renderer;

import java.awt.Component;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import lu.kbra.talking.client.frame.data.Message;

public class MessageCellRenderer extends JLabel implements ListCellRenderer<Message> {

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public MessageCellRenderer() {
		super();
		setOpaque(true);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Message> list, Message message, int index, boolean isSelected, boolean cellHasFocus) {
		setText(message.toString());
		setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
		return this;
	}

}
