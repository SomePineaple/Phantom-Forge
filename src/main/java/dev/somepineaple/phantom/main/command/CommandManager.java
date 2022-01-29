package dev.somepineaple.phantom.main.command;

import java.util.ArrayList;

import dev.somepineaple.phantom.main.command.commands.Command;
import dev.somepineaple.phantom.main.command.commands.ConfigCommand;
import dev.somepineaple.phantom.main.command.commands.LoginCommand;
import dev.somepineaple.phantom.main.events.EventChat;
import dev.somepineaple.phantom.main.utils.PlayerUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommandManager {
	private final ArrayList<Command> commands;
	
	public CommandManager() {
		commands = new ArrayList<>();
		
		commands.add(new ConfigCommand());
		commands.add(new LoginCommand());
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onChat(EventChat e) {
		if (e.getMessage().startsWith(".")) {
			e.cancel();
			sendCommand(e.getMessage());
		}
	}
	
	private void sendCommand(String command) {
		String commandArgs[] = command.split(" ");
		
		boolean commandSent = false;
		
		for (Command c : commands) {
			if (c.getName().equalsIgnoreCase(commandArgs[0])) {
				c.execute(commandArgs);
				commandSent = true;
			}
		}
		
		if (!commandSent) {
			PlayerUtils.sendMessage("Command not found");
		}
	}
}
