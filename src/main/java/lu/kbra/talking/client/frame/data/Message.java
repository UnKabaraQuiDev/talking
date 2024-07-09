package lu.kbra.talking.client.frame.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {

	private String username;
	private LocalDateTime timestamp;
	private String content;
	private boolean isSentByUser = false;

	public Message(String username, String content, boolean isSentByUser) {
		this.username = username;
		this.content = content;
		this.timestamp = LocalDateTime.now();
		this.isSentByUser = isSentByUser;
	}

	public String getUsername() {
		return username;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getContent() {
		return content;
	}

	public boolean isSentByUser() {
		return isSentByUser;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return "<html><body style='text-align: " + (isSentByUser ? "right" : "left") + ";'>" + "<b>" + username + "</b> <span style='color: lightgrey;'>-</span> " + "<span style='color: grey;'>" + timestamp.format(formatter)
				+ "</span><br>" + "<span style='color: black;'>" + content + "</span>" + "</body></html>";
	}

}