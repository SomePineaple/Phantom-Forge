package dev.somepineaple.phantom.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;

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
}
