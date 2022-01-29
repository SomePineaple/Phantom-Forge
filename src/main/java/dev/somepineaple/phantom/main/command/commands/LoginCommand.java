package dev.somepineaple.phantom.main.command.commands;

import dev.somepineaple.phantom.main.utils.LoginUtils;
import dev.somepineaple.phantom.main.utils.PlayerUtils;

public class LoginCommand extends Command {
	public LoginCommand() {
		super(".login");
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 2) {
			PlayerUtils.sendMessage("usage: .login <email/username> <password>, The password is optional if you want to login in cracked mode");
			return;
		}
		
		if (args.length == 2) {
			LoginUtils.changeCrackedName(args[1]);
			return;
		}
		
		LoginUtils.login(args[1], args[2]);
	}
}
