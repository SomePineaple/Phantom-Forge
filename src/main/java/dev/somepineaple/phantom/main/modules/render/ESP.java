package dev.somepineaple.phantom.main.modules.render;

import java.awt.Color;

import dev.somepineaple.phantom.main.modules.Category;
import dev.somepineaple.phantom.main.modules.Module;
import dev.somepineaple.phantom.main.modules.Setting;
import dev.somepineaple.phantom.main.utils.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Module {
	private final Setting mode = addSetting(new Setting("Mode", "Box", "Outline", "Wireframe", "2D"));
	
	private final Setting r = addSetting(new Setting("R", 255, 0, 255));
	private final Setting g = addSetting(new Setting("G", 255, 0, 255));
	private final Setting b = addSetting(new Setting("B", 255, 0, 255));
	private final Setting a = addSetting(new Setting("A", 255, 0, 255));
	
	private final Setting players = addSetting(new Setting("Players", true));
	private final Setting mobs = addSetting(new Setting("Mobs", false));
	private final Setting passives = addSetting(new Setting("Passives", false));
	
	public ESP() {
		super("ESP", Category.RENDER, -1);
	}
	
	@Override
	public void render() {
		// boolean is2d = mode.getCurrentMultiSelect().equalsIgnoreCase("2d");
		
		// Timer timer = ((MinecraftAccessor) mc).getTimer();
		
		for (Entity e : mc.theWorld.loadedEntityList) {
			if (!(e instanceof EntityLivingBase) || e == mc.thePlayer)
				continue;
			
			if (!((e instanceof EntityPlayer && players.booleanVal()) || (e instanceof EntityMob && mobs.booleanVal()) || (e instanceof EntityAnimal && passives.booleanVal())))
				continue;
			
			EntityLivingBase entity = (EntityLivingBase) e;
			
			switch (mode.getCurrentMultiSelect()) {
			case "2D":
				break;
			case "Box":
				RenderUtils.drawEntityBox(entity, new Color(r.intVal(), g.intVal(), b.intVal(), a.intVal()), true);
				break;
			default:
				break;
			}
		}
	}
}
