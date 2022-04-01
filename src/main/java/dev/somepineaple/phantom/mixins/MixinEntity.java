package dev.somepineaple.phantom.mixins;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.modules.combat.HitBoxes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Entity.class)
public class MixinEntity {
	@Shadow
	public double posX;
	
	@Shadow
	public double posZ;
	
	@Shadow
    public float rotationPitch;

    @Shadow
    public float rotationYaw;
    
    @Shadow
	public boolean onGround;
    
    @Shadow
	public double motionX;
	
	@Shadow
	public double motionZ;
	
	@Shadow
	public Entity ridingEntity;
	
	@Shadow
	public AxisAlignedBB getEntityBoundingBox() {
		throw new RuntimeException("Called shadow method???");
	}

	@Inject(method = "getCollisionBorderSize", at = @At("HEAD"), cancellable = true)
	private void getCollisionBorderSize(final CallbackInfoReturnable<Float> callbackInfoReturnable) {
		final HitBoxes hitBoxes = (HitBoxes) Phantom.getModuleManager().getModuleWithName("HitBoxes");

		if (Objects.requireNonNull(hitBoxes).isEnabled())
			callbackInfoReturnable.setReturnValue(0.1F + hitBoxes.getSettingWithName("Expand").floatVal());
	}
}
