package dev.somepineaple.phantom.main.utils;

import com.mojang.realmsclient.gui.ChatFormatting;

import dev.somepineaple.phantom.Phantom;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class PlayerUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static void sendMessage(String message) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(
				new ChatComponentText(ChatFormatting.BLUE + 
				Phantom.NAME + " " + Phantom.VERSION +
	            ChatFormatting.GRAY + ": " + 
				ChatFormatting.RESET +  message
		));
	}
	
	public static boolean teamsCheck(EntityPlayer otherPlayer) {
		if (!Phantom.getModuleManager().getModuleWithName("Teams").isEnabled())
			return true;
		
		String displayName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
		String otherName = otherPlayer.getDisplayName().getFormattedText().replace("§r", "");
		
		return !(otherName.charAt(1) == displayName.charAt(1));
	}
}
