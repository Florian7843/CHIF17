package at.florian7843.chif17bot.listeners;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandParser;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.main.BotStarter;
import at.florian7843.chif17bot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class EVENTListenerCommands extends ListenerAdapter {

  public void onMessageReceived(MessageReceivedEvent e) {
    if (!e.getAuthor().isBot()) {
      if (e.getMessage().getRawContent().startsWith(Constants.getInvoke())) {
        String cmd = e.getMessage().getRawContent().replaceFirst(Constants.getInvoke(), "").split(" ")[0];
        System.out.println(e.getAuthor().getName() + " --> " + cmd);
        if (BotStarter.getCommands().containsKey(cmd.toLowerCase())) {
          Command command = BotStarter.getCommands().get(cmd.toLowerCase());
          if (Constants.getUtils().containsGroup(command.permissionGroups(), e)) {
            String[] args = CommandParser.pharse(e).args;
            CommandState state = BotStarter.getCommands().get(cmd.toLowerCase()).executed(args, e);
            if (state.equals(CommandState.PRINT_SYNTAX)) {
              Message msg = e.getMessage().getTextChannel().sendMessage(BotStarter.getCommands().get(cmd).syntax()).complete();
              Constants.getUtils().deletedelay(msg, 10);
            }
          } else {
            e.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Du hast dazu nicht die Berechtigungen!").build()).queue();
          }
        } else {
          e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("Diesen Command gibt es nicht!").build()).queue();
        }
      }
    }
  }
}
