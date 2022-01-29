package dev.somepineaple.phantom.main.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class EventCancellable extends Event {
    private boolean isCancelled = false;

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public boolean isCanceled() {
        return isCancelled;
    }
}
