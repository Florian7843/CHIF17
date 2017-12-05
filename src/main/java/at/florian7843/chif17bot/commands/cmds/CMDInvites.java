package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.lib.TimeDate;
import at.florian7843.chif17bot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CMDInvites implements Command {

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    if (args.length == 0) {
      String text = "";
      for (Invite invite : e.getGuild().getInvites().complete()) {
        if (invite.getMaxAge() == 0) {
          text += invite.getCode() + " --> Infinite\n";
          continue;
        }
        int days = invite.getCreationTime().getDayOfMonth(), hours = invite.getCreationTime().getHour(), minutes = invite.getCreationTime().getMinute(), seconds = invite.getCreationTime().getSecond(), remaining = invite.getMaxAge();
        while (remaining >= 60) {
          if (remaining / 24 / 60 / 60 >= 1) {
            days++;
            remaining -= 24 * 60 * 60;
          }
          if (remaining / 60 / 60 >= 1) {
            hours++;
            remaining -= 60 * 60;
          }
          if (remaining / 60 >= 1) {
            minutes++;
            remaining -= 60;
          }
        }
        seconds += remaining;
        Date date;
        TimeDate tdate = Constants.getUtils().getTimeDateOfSeconds(0);
        try {
          date = Constants.getSimpleDateFormat().parse(days + "/" + invite.getCreationTime().getMonthValue() + "/" + invite.getCreationTime().getYear() + " " + hours + ":" + minutes + ":" + seconds);
          tdate = Constants.getUtils().getTimeDateOfSeconds((int)((date.getTime()-System.currentTimeMillis())/1000));
        } catch (ParseException e1) {
          e1.printStackTrace();
        }

        text += invite.getCode() + " --> " + (tdate.getHours()+1) + ":" + tdate.getMinutes() + ":" + tdate.getSeconds() + "\n";
      }
      e.getTextChannel().sendMessage("```" + text + "```").queue();
      return CommandState.SUCESS;
    } else if (args.length == 1) {
      boolean found = false;
      for (Invite invite : e.getGuild().getInvites().complete()) {
        if (invite.getCode().equals(args[0])) {
          found = true;
          e.getTextChannel().sendMessage("http://discord.gg/" + invite.getCode()).queue();
        }
      }
      if (found == false) {
        e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setDescription("Der Invite " + args[0] + " wurde nicht gefunden!").build()).queue();
      }
      return CommandState.SUCESS;
    }
    return CommandState.PRINT_SYNTAX;
  }

  @Override
  public String help() {
    return "With this command you can see and get existing Invites.";
  }

  @Override
  public String syntax() {
    return ".invites [inviteID]";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.EVERYONE);
    }};
  }

}

