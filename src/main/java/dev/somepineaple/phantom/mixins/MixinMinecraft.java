package dev.somepineaple.phantom.mixins;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import dev.somepineaple.phantom.Phantom;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow
	private static final Logger logger  = LogManager.getLogger();
	
	@Shadow
	public PlayerControllerMP playerController;
	
	@Shadow
	private int rightClickDelayTimer;
	
	@Shadow
	public MovingObjectPosition objectMouseOver;
	
	@Shadow
	public WorldClient theWorld;
	
	@Shadow
	public EntityPlayerSP thePlayer;
	
	@Shadow
	public EntityRenderer entityRenderer;
	
	@Overwrite
	private void rightClickMouse()
    {
        if (!this.playerController.func_181040_m())
        {
            this.rightClickDelayTimer = 4;
            
            if (Phantom.getModuleManager().getModuleWithName("Fast Place").isEnabled())
            	this.rightClickDelayTimer = Phantom.getModuleManager().getModuleWithName("Fast Place").getSettingWithName("Delay").intVal();
            
            boolean flag = true;
            ItemStack itemstack = this.thePlayer.inventory.getCurrentItem();

            if (this.objectMouseOver == null)
            {
                logger.warn("Null returned as \'hitResult\', this shouldn\'t happen!");
            }
            else
            {
                switch (this.objectMouseOver.typeOfHit)
                {
                    case ENTITY:
                        if (this.playerController.func_178894_a(this.thePlayer, this.objectMouseOver.entityHit, this.objectMouseOver))
                        {
                            flag = false;
                        }
                        else if (this.playerController.interactWithEntitySendPacket(this.thePlayer, this.objectMouseOver.entityHit))
                        {
                            flag = false;
                        }

                        break;

                    case BLOCK:
                        BlockPos blockpos = this.objectMouseOver.getBlockPos();

                        if (this.theWorld.getBlockState(blockpos).getBlock().getMaterial() != Material.air)
                        {
                            int i = itemstack != null ? itemstack.stackSize : 0;

                            if (this.playerController.onPlayerRightClick(this.thePlayer, this.theWorld, itemstack, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec))
                            {
                                flag = false;
                                this.thePlayer.swingItem();
                            }

                            if (itemstack == null)
                            {
                                return;
                            }

                            if (itemstack.stackSize == 0)
                            {
                                this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                            }
                            else if (itemstack.stackSize != i || this.playerController.isInCreativeMode())
                            {
                                this.entityRenderer.itemRenderer.resetEquippedProgress();
                            }
                        }
					default:
						break;
                }
            }

            if (flag)
            {
                ItemStack itemstack1 = this.thePlayer.inventory.getCurrentItem();

                if (itemstack1 != null && this.playerController.sendUseItem(this.thePlayer, this.theWorld, itemstack1))
                {
                    this.entityRenderer.itemRenderer.resetEquippedProgress2();
                }
            }
        }
    }
}
