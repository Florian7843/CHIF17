package at.florian7843.chif17bot.commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public interface Command {

  CommandState executed(String[] args, MessageReceivedEvent e);

  String help();

  String syntax();

  List<CommandGroup> permissionGroups();

}
