package at.florian7843.chif17bot.listeners;

import lombok.Getter;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;

public class EVENTListenerMute extends ListenerAdapter {

  @Getter
  private static ArrayList<Member> muted = new ArrayList<>();

  @Override
  public void onMessageReceived(MessageReceivedEvent e) {
    if (muted.contains(e.getMember())) {
      e.getMessage().delete().queue();
      e.getAuthor().openPrivateChannel().complete().sendMessage("You are muted!").queue();
    }
  }

}
