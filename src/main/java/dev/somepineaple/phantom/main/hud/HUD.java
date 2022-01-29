package dev.somepineaple.phantom.main.hud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.hud.elements.ArrayListElement;
import dev.somepineaple.phantom.main.hud.elements.HudElement;
import dev.somepineaple.phantom.main.hud.elements.TargetHud;
import dev.somepineaple.phantom.main.hud.elements.Watermark;
import dev.somepineaple.phantom.main.modules.render.HUDModule;
import dev.somepineaple.phantom.main.utils.FileUtils;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD {
private final ArrayList<HudElement> elements;
	
	public HUD() {
		elements = new ArrayList<>();
		
		elements.add(new Watermark(10, 10));
		elements.add(new ArrayListElement(20, 30));
		elements.add(new TargetHud(10, 50));
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent.Post event) {

		if (event.isCanceled()) {
			return;
		}

		// PineapleEventBus.EVENT_BUS.post(new EventGameOverlay(event.getPartialTicks(), new ScaledResolution(mc)));

		RenderGameOverlayEvent.ElementType target = RenderGameOverlayEvent.ElementType.EXPERIENCE;

		if (event.type == target) {
			draw();
		}
	}
	
	public void saveHudPositions() {
		StringBuilder builder = new StringBuilder();
		for (HudElement e : elements) {
			builder.append(e.getName() + ":" + e.getX() + ":" + e.getY() + "\n");
		}
		
		FileUtils.writeStringToFile("hud.txt", builder.toString());
	}
	
	public void loadHudPositions() {
		List<String> elementPositions = Arrays.asList(FileUtils.loadFileAsString("hud.txt").split("\n"));
		
		for (String elementPosition : elementPositions) {
			String positions[] = elementPosition.split(":");
			
			for (HudElement element : elements) {
				if (element.getName().equalsIgnoreCase(positions[0])) {
					element.setX(Integer.parseInt(positions[1]));
					element.setY(Integer.parseInt(positions[2]));
				}
			}
		}
	}
	
	public void draw() {
		HudElement.updateTextColor();
		
		HUDModule module = (HUDModule) Phantom.getModuleManager().getModuleWithName("HUD");

		if (!module.isEnabled())
			return;

		for (HudElement e : elements) {
			if (module.getSettingWithName(e.getName()).booleanVal())
				e.render();
		}
	}

	public ArrayList<HudElement> getElements() {
		return elements;
	}
}
