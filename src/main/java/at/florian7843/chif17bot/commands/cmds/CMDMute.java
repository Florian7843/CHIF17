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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class CMDMute implements Command {

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    if (args.length == 1) {
      String name = args[0].replaceAll("%", " ");
      for (Member m : e.getGuild().getMembersByEffectiveName(name, true)) {
        EVENTListenerMute.getMuted().add(m);
        e.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Muted " + Constants.getUtils().escapeChars(name)).build()).queue();
        unmuteAfter(5, TimeUnit.MINUTES, e);
      }
      return CommandState.SUCESS;
    }
    return CommandState.PRINT_SYNTAX;
  }

  @Override
  public String help() {
    return "With this command you can mute people. Use % for spaces in name!";
  }

  @Override
  public String syntax() {
    return ".mute <username> [minutes]";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.ADMIN);
    }};
  }

  private void unmuteAfter(int delay, TimeUnit timeUnit, MessageReceivedEvent e) {
    Timer t = new Timer();
    t.schedule(new TimerTask() {
      @Override
      public void run() {
        if (EVENTListenerMute.getMuted().contains(e.getMember())) {
          EVENTListenerMute.getMuted().remove(e.getMember());
          e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription(Constants.getUtils().escapeChars(e.getMember().getEffectiveName()) + " is now unmuted!").build()).queue();
        }
      }
    }, Constants.getUtils().convertToSeconds(timeUnit, delay) * 1000);
  }
}
