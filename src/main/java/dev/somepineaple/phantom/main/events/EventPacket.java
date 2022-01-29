package dev.somepineaple.phantom.main.events;

import net.minecraft.network.Packet;

@SuppressWarnings("rawtypes")
public class EventPacket extends EventCancellable {
    private final Packet packet;

    public EventPacket(Packet packet) {
        super();

        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public static class ReceivePacket extends EventPacket {
        public ReceivePacket(Packet packet) {
            super(packet);
        }
    }

    public static class SendPacket extends EventPacket {
        public SendPacket(Packet packet) {
            super(packet);
        }
    }
}