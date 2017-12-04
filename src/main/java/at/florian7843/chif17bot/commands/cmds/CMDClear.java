package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.utils.Constants;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CMDClear implements Command {

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    if (args.length == 0) {
      MessageHistory messageHistory = new MessageHistory(e.getTextChannel());
      while (!messageHistory.retrievePast(100).complete().isEmpty()){
        messageHistory = new MessageHistory(e.getTextChannel());
        List<Message> msgs = messageHistory.retrievePast(100).complete();
        e.getTextChannel().deleteMessages(msgs).queue();
      }
      Message msg = e.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Die Nachrichten wurden gelöscht!").setColor(Color.ORANGE).build()).complete();
      Constants.getUtils().deletedelay(msg, 3);
      return CommandState.SUCESS;
    } else if (args.length == 1 && Constants.getUtils().isInt(args[0])) {
      if (Integer.parseInt(args[0]) <= 0) {
        Message msg = e.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Du kannst nur 1-100. Nachrichten Löschen!").setColor(Color.RED).build()).complete();
        Constants.getUtils().deletedelay(msg, 3);
        return CommandState.SUCESS;
      }
      MessageHistory messageHistory = new MessageHistory(e.getTextChannel());
      List<Message> msgs = messageHistory.retrievePast(Integer.parseInt(args[0]) + 1).complete();
      e.getTextChannel().deleteMessages(msgs).queue();
      Message msg = e.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Die letzte(n) " + args[0] + ". Nachrichten wurden gelöscht!").setColor(Color.ORANGE).build()).complete();
      Constants.getUtils().deletedelay(msg, 3);
      return CommandState.SUCESS;
    }
    return CommandState.PRINT_SYNTAX;
  }

  @Override
  public String help() {
    return "With this command you can clear Channels.";
  }

  @Override
  public String syntax() {
    return ".clear [1-99]";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    List<CommandGroup> groups = new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.ADMIN);
    }};
    return groups;
  }
}
