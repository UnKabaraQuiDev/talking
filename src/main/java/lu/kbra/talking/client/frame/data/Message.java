package lu.kbra.talking.client.frame.data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Message {

	private String username;
	private LocalDateTime timestamp;
	private String content;
	private boolean isSentByUser = false;
	private boolean serverTrusted = false;
	private boolean userTrusted = false;
	private UUID senderUuid;

	public Message(String username, boolean serverTrusted, boolean userTrusted, String content, boolean isSentByUser, UUID senderUuid) {
		this.username = username;
		this.serverTrusted = serverTrusted;
		this.userTrusted = userTrusted;
		this.content = content;
		this.timestamp = LocalDateTime.now();
		this.isSentByUser = isSentByUser;
		this.senderUuid = senderUuid;
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

	public boolean isServerTrusted() {
		return serverTrusted;
	}

	public boolean isUserTrusted() {
		return userTrusted;
	}

	public UUID getSenderUuid() {
		return senderUuid;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String serverDot = "<span style='color: " + (serverTrusted ? "green" : "red") + ";'>•</span>";
		String userDot = "<span style='color: " + (userTrusted ? "green" : "red") + ";'>•</span>";

		return "<html><p style='text-align: " + (isSentByUser ? "right" : "left") + ";'>" + "<b>" + username + "</b> " + serverDot + " " + userDot + " " + "<span style='color: #808080;'>" + timestamp.format(formatter) + "</span><br>"
				+ "<span style='color: black;'>" + content + "</span>" + "</p></html>";
	}

}