package dev.somepineaple.phantom.main.events;

public class EventChat extends EventCancellable {
	private String message;
	
	public EventChat(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
