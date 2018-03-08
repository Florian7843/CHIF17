package at.florian7843.chif17bot.listeners;

import at.florian7843.chif17bot.utils.Constants;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EVENTListenerLogger extends ListenerAdapter {

  public void onMessageReceived(MessageReceivedEvent e) {
    if (!e.getAuthor().isBot()) {
      Constants.getLogger().log(e.getTextChannel().getName() + " -- " + e.getMember().getUser().getName() + "#" + e.getMember().getUser().getDiscriminator() + " -- " + e.getMessage().getContentRaw());
      return;
    }
  }

}
