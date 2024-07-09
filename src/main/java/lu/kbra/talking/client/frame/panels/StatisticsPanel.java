package lu.kbra.talking.client.frame.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class StatisticsPanel extends JPanel {

	private JTable table;
	private DefaultTableModel tableModel;
	private Map<String, Integer> keyIndexMap;

	public StatisticsPanel() {
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(200, 100));

		keyIndexMap = new HashMap<>();

		tableModel = new DefaultTableModel(new Object[] { "Key", "Value" }, 0);
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);

		add(scrollPane, BorderLayout.CENTER);
	}

	public void set(String key, Object value) {
		if (keyIndexMap.containsKey(key)) {
			int rowIndex = keyIndexMap.get(key);
			tableModel.setValueAt(value, rowIndex, 1);
		} else {
			tableModel.addRow(new Object[] { key, value });
			keyIndexMap.put(key, tableModel.getRowCount() - 1);
		}
	}

	public JTable getTable() {
		return table;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

}