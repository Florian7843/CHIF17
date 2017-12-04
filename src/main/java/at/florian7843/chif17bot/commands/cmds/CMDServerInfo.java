package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CMDServerInfo implements Command {

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    String info = "";
    info += "Membercount: " + e.getGuild().getMembers().size();
    info += "\nCreation date: " + e.getGuild().getCreationTime().getDayOfMonth() + ". " + e.getGuild().getCreationTime().getMonth() + ". " + e.getGuild().getCreationTime().getYear() + ". um " + e.getGuild().getCreationTime().getHour() + ":" + e.getGuild().getCreationTime().getMinute();
    info += "\nRegion: " + e.getGuild().getRegion().getName();
    info += "\nServer name: " + e.getGuild().getName();
    e.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Server info: ").setImage(e.getGuild().getIconUrl()).setDescription(info).setColor(Color.GREEN).build()).queue();

    return CommandState.SUCESS;
  }

  @Override
  public String help() {
    return "With this command you get some infos of the Server.";
  }

  @Override
  public String syntax() {
    return ".serverinfo";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.EVERYONE);
    }};
  }
}
