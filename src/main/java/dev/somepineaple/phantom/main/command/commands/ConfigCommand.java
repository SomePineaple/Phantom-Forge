package dev.somepineaple.phantom.main.command.commands;

import dev.somepineaple.phantom.Phantom;
import dev.somepineaple.phantom.main.utils.PlayerUtils;

public class ConfigCommand extends Command {
	public ConfigCommand() {
		super(".config");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 3) {
			PlayerUtils.sendMessage("usage: .config <save/load> <configname>");
			return;
		}
		
		switch (args[1]) {
		case "save":
			Phantom.getModuleManager().saveConfig(args[2]);
			break;
		case "load":
			Phantom.getModuleManager().loadConfig(args[2]);
			break;
		default:
			PlayerUtils.sendMessage("usage: .config <save/load> <configname>");
			break;
		}
	}
}
