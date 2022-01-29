package dev.somepineaple.phantom.main.utils;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import dev.somepineaple.phantom.mixins.IRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

public class RenderUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static void drawEntityBox(final EntityLivingBase entity, final Color color, final boolean outline) {
		final IRenderManager renderManager = (IRenderManager) mc.getRenderManager();
		// final Timer timer = ((MinecraftAccessor)mc).getTimer();
		
		 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		 GL11.glEnable(GL11.GL_BLEND);
	     GL11.glDisable(GL11.GL_TEXTURE_2D);
	     GL11.glDisable(GL11.GL_DEPTH_TEST);
	     GL11.glDepthMask(false);
	     
	     /*final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks
	                - renderManager.getRenderPosX();
	     
	     final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks
	                - renderManager.getRenderPosY();
	     
	     final double z = entity.lastTickPosY + (entity.posZ - entity.lastTickPosY) * timer.renderPartialTicks
	                - renderManager.getRenderPosZ();
	     */
	     
	     final double x = entity.posX - renderManager.getRenderPosX();
	     final double y = entity.posY - renderManager.getRenderPosY();
	     final double z = entity.posZ - renderManager.getRenderPosZ();
	     
	     final AxisAlignedBB entityBox = entity.getEntityBoundingBox();
	     
	     final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(
                entityBox.minX - entity.posX + x - 0.05D,
                entityBox.minY - entity.posY + y,
                entityBox.minZ - entity.posZ + z - 0.05D,
                entityBox.maxX - entity.posX + x + 0.05D,
                entityBox.maxY - entity.posY + y + 0.15D,
                entityBox.maxZ - entity.posZ + z + 0.05D
	     );
	     
	     if (outline) {
	    	 GL11.glLineWidth(1F);
	    	 GL11.glEnable(GL11.GL_LINE_SMOOTH);
	    	 GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.95f);
	    	 drawSelectionBoundingBox(axisAlignedBB);
	     }
	     
	     GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	     
	     if (outline) {
	    	 GL11.glDisable(GL11.GL_LINE_SMOOTH);
	     }
	     
	     GL11.glDisable(GL11.GL_BLEND);
	     GL11.glEnable(GL11.GL_TEXTURE_2D);
	     GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        // Lower Rectangle
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();

        // Upper Rectangle
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();

        // Upper Rectangle
        worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();

        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();

        worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();

        tessellator.draw();
    }
}
