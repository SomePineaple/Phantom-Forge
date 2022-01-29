package dev.somepineaple.phantom.main.events;

public class EventRender3D extends EventCancellable {
	private float partialTicks;
	
	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}
	
	public float getPartialTicks() {
		return partialTicks;
	}
}
