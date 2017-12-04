package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class CMDRoleIDs implements Command {

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    String roles = "RoleIDs:\n```";
    for (Role role : e.getGuild().getRoles()) {
      roles = roles + role.getName() + " --> " + role.getId() + "\n";
    }

    roles += "```";

    e.getTextChannel().sendMessage(roles).queue();

    return CommandState.SUCESS;
  }

  @Override
  public String help() {
    return "With this command you can Show the RoleIDs";
  }

  @Override
  public String syntax() {
    return ".roleids";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    List<CommandGroup> groups = new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.ADMIN);
    }};
    return groups;
  }
}
