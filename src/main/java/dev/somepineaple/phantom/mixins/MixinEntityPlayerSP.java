package dev.somepineaple.phantom.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.somepineaple.phantom.main.events.EventChat;
import dev.somepineaple.phantom.main.events.EventMotion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.common.MinecraftForge;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends MixinEntity {
	
	@Shadow
	public boolean serverSprintState;
	
	@Shadow
	private boolean serverSneakState;
	
	@Shadow
	private double lastReportedPosX;
	
	@Shadow
	private double lastReportedPosY;
	
	@Shadow
	private double lastReportedPosZ;
	
	@Shadow
	private float lastReportedYaw;
	
	@Shadow
	private float lastReportedPitch;
	
	@Shadow
	private int positionUpdateTicks;
	
	@Shadow 
	@Final 
	public NetHandlerPlayClient sendQueue;
	
	@Shadow
	protected boolean isCurrentViewEntity() {
		throw new RuntimeException("Called shadow method");
	}
	
	@Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
	private void sendChatMessage(String message, CallbackInfo info) {
		EventChat event = new EventChat(message);
		
		MinecraftForge.EVENT_BUS.post(event);
		
		if (event.isCanceled())
			info.cancel();
	}
	
	@Overwrite
	public void onUpdateWalkingPlayer() {
		EventMotion eventMotion = new EventMotion(this.posX, this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround);
    	
		MinecraftForge.EVENT_BUS.post(eventMotion);
    	
        boolean sprintFlag = Minecraft.getMinecraft().thePlayer.isSprinting();
        
        if (sprintFlag != this.serverSprintState)
        {
            if (sprintFlag)
            {
                this.sendQueue.addToSendQueue(new C0BPacketEntityAction((EntityPlayerSP) (Object) this, C0BPacketEntityAction.Action.START_SPRINTING));
            }
            else
            {
            	this.sendQueue.addToSendQueue(new C0BPacketEntityAction((EntityPlayerSP) (Object) this, C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = sprintFlag;
        }

        boolean sneakFlag = Minecraft.getMinecraft().thePlayer.isSneaking();

        if (sneakFlag != this.serverSneakState)
        {
            if (sneakFlag)
            {
            	this.sendQueue.addToSendQueue(new C0BPacketEntityAction((EntityPlayerSP) (Object) this, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            else
            {
            	this.sendQueue.addToSendQueue(new C0BPacketEntityAction((EntityPlayerSP) (Object) this, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = sneakFlag;
        }

        if (this.isCurrentViewEntity())
        {
            double d0 = eventMotion.getX() - this.lastReportedPosX;
            double d1 = eventMotion.getY() - this.lastReportedPosY;
            double d2 = eventMotion.getZ() - this.lastReportedPosZ;
            double d3 = (double)(eventMotion.getYaw() - this.lastReportedYaw);
            double d4 = (double)(eventMotion.getPitch() - this.lastReportedPitch);
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0D || d4 != 0.0D;

            if (this.ridingEntity == null)
            {
                if (flag2 && flag3)
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(eventMotion.getX(), eventMotion.getY(), eventMotion.getZ(), eventMotion.getYaw(), eventMotion.getPitch(), eventMotion.isOnGround()));
                }
                else if (flag2)
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(eventMotion.getX(), eventMotion.getY(), eventMotion.getZ(), eventMotion.isOnGround()));
                }
                else if (flag3)
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(eventMotion.getYaw(), eventMotion.getPitch(), eventMotion.isOnGround()));
                }
                else
                {
                    this.sendQueue.addToSendQueue(new C03PacketPlayer(eventMotion.isOnGround()));
                }
            }
            else
            {
                this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D, this.motionZ, eventMotion.getYaw(), eventMotion.getPitch(), eventMotion.isOnGround()));
                flag2 = false;
            }

            ++this.positionUpdateTicks;

            if (flag2)
            {
                this.lastReportedPosX = eventMotion.getX();
                this.lastReportedPosY = eventMotion.getY();
                this.lastReportedPosZ = eventMotion.getZ();
                this.positionUpdateTicks = 0;
            }

            if (flag3)
            {
                this.lastReportedYaw = eventMotion.getYaw();
                this.lastReportedPitch = eventMotion.getPitch();
            }
        }
	}
}
