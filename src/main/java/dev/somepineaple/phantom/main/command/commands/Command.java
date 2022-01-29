package dev.somepineaple.phantom.main.command.commands;

public abstract class Command {
	protected final String name;
	
	public Command(String name) {
		this.name = name;
	}
	
	public abstract void execute(String args[]);
	
	public String getName() {
		return name;
	}
}
