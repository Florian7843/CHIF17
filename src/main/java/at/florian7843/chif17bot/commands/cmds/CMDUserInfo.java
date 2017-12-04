package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.utils.Constants;
import at.florian7843.chif17bot.utils.Utils;
import com.sun.scenario.effect.Offset;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CMDUserInfo implements Command {

  Utils utils = Constants.getUtils();

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    if (args.length >= 1) {
      String name = "";
      for (String arg : args) {
        if (arg == args[args.length - 1]) {
          name += arg;
        } else {
          name += arg + " ";
        }
      }
      for (Member members : e.getGuild().getMembersByEffectiveName(name, true)) {
        String user = "";
        user += "Rollen: ";
        for (Role role : members.getRoles()) {
          if (members.getRoles().get(members.getRoles().size() - 1) == role) {
            user += role.getName() + "\n";
          } else {
            user += role.getName() + ", ";
          }
        }
        OffsetDateTime joindate = members.getJoinDate();
        user += "Beigetreten am: " + joindate.getDayOfMonth() + ". " + joindate.getMonth() + ". " + joindate.getYear() + " um " + joindate.getHour() + ":" + joindate.getMinute() + "\n";
        user += "Onlinestatus: " + members.getOnlineStatus() + "\n";
        if (members.getGame() != null) {
          user += "Spiel: " + members.getGame().getName() + "\n";
        }

        user = utils.escapeChars(user);
        e.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Infos über: " + utils.escapeChars(members.getEffectiveName())).setDescription(user).setColor(Color.GREEN).setImage(members.getUser().getAvatarUrl()).build()).queue();
        return CommandState.SUCESS;
      }
    }
    return CommandState.PRINT_SYNTAX;
  }

  @Override
  public String help() {
    return "With this command you can get info's from another User.";
  }

  @Override
  public String syntax() {
    return ".userinfo <name>";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.EVERYONE);
    }};
  }
}
