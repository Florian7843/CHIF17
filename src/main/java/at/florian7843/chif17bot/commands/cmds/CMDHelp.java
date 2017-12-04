package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.main.BotStarter;
import at.florian7843.chif17bot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CMDHelp implements Command {
  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    String commands = "";
    String syntax;
    for (Map.Entry<String, Command> cmd : BotStarter.getCommands().entrySet()) {
      if (Constants.getUtils().containsGroup(cmd.getValue().permissionGroups(), e)) {
        syntax = cmd.getValue().syntax();
        commands += syntax + " --> " + cmd.getValue().help() + "\n";
      }
    }

    e.getTextChannel().sendMessage(new EmbedBuilder().setDescription(commands).setTitle("Commands: ").setColor(Color.GREEN).build()).queue();
    return CommandState.SUCESS;
  }

  @Override
  public String help() {
    return "This command shows you every for you allowed Command!";
  }

  @Override
  public String syntax() {
    return ".help";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.EVERYONE);
    }};
  }
}
