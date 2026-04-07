package pixel.event;

public class EventCancelable extends Event {
	private boolean cancelled = false;
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void cancel(boolean cancel) {
		cancelled = cancel;
	}
}
