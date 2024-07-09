package lu.kbra.talking.client.frame.panels;

import java.io.OutputStream;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class CustomOutputStream extends OutputStream {
	
	private JTextArea textArea;
	private int maxLines;
	private int linesCount;

	public CustomOutputStream(JTextArea textArea, int maxLines) {
		this.textArea = textArea;
		this.maxLines = maxLines;
		this.linesCount = 0;
	}

	@Override
	public void write(int b) {
		write(new byte[] { (byte) b }, 0, 1);
	}

	@Override
	public void write(byte[] b, int off, int len) {
		final String text = new String(b, off, len);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				textArea.append(text);
				linesCount += countLines(text);

				// Remove lines if the number of lines exceeds maxLines
				if (linesCount > maxLines) {
					removeExcessLines();
				}
			}
		});
	}

	private int countLines(String text) {
		int lines = 0;
		int pos = 0;
		while ((pos = text.indexOf('\n', pos) + 1) != 0) {
			lines++;
		}
		return lines;
	}

	private void removeExcessLines() {
		String content = textArea.getText();
		int linesToRemove = linesCount - maxLines;

		int pos = 0;
		for (int i = 0; i < linesToRemove; i++) {
			pos = content.indexOf('\n', pos) + 1;
		}

		textArea.replaceRange("", 0, pos);
		linesCount = maxLines;
	}
	
}
