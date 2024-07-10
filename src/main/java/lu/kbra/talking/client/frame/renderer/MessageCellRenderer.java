package lu.kbra.talking.client.frame.renderer;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import lu.kbra.talking.client.frame.data.Message;

public class MessageCellRenderer extends JPanel implements ListCellRenderer<Message> {

	private JLabel label;

	public MessageCellRenderer() {
		setLayout(new BorderLayout());
		label = new JLabel();
		label.setOpaque(true);
		add(label, BorderLayout.CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Message> list, Message message, int index, boolean isSelected, boolean cellHasFocus) {
		label.setText(message.toString());

		setAlignmentX(message.isSentByUser() ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
		label.setHorizontalAlignment(message.isSentByUser() ? SwingConstants.RIGHT : SwingConstants.LEFT);

		label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
		label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

		this.setToolTipText(message.toTooltipString());

		return this;
	}

}
