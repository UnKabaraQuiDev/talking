package lu.kbra.talking.client.frame.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
	
	private String username;
	private LocalDateTime timestamp;
	private String content;

	public Message(String username, String content) {
		this.username = username;
		this.content = content;
		this.timestamp = LocalDateTime.now();
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

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return "[" + timestamp.format(formatter) + "] " + username + ": " + content;
	}
	
}