package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.listeners.EVENTListenerMute;
import at.florian7843.chif17bot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CMDUnmute implements Command {

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    if (args.length == 1) {
      String name = args[0].replaceAll("%", " ");
      for (Member m : e.getGuild().getMembersByEffectiveName(name, true)) {
        if (EVENTListenerMute.getMuted().contains(m)) {
          EVENTListenerMute.getMuted().remove(m);
          e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription(Constants.getUtils().escapeChars(name) + " is now unmuted!").build()).queue();
          return CommandState.SUCESS;
        } else {
          e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription("This user isn't muted!").build()).queue();
          return CommandState.SUCESS;
        }
      }
    }
    return CommandState.PRINT_SYNTAX;
  }

  @Override
  public String help() {
    return "With this command you can unmute people. Use % for spaces in name!";
  }

  @Override
  public String syntax() {
    return ".unmute <username>";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.ADMIN);
    }};
  }
}
