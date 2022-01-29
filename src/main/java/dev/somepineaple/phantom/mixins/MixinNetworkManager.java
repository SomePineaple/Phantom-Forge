package dev.somepineaple.phantom.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.somepineaple.phantom.main.events.EventPacket;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void receive(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        EventPacket receivePacket = new EventPacket.ReceivePacket(packet);

        MinecraftForge.EVENT_BUS.post(receivePacket);

        if (receivePacket.isCanceled()) {
            callback.cancel();
        }
    }

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        EventPacket sendPacket = new EventPacket.SendPacket(packet);

        MinecraftForge.EVENT_BUS.post(sendPacket);

        if (sendPacket.isCanceled()) {
            callback.cancel();
        }
    }
}
