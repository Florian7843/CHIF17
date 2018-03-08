package at.florian7843.chif17bot.utils;

import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.lib.TimeDate;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Utils {

  ArrayList<Character> chars = new ArrayList<Character>() {{
    this.add('_');
    this.add('*');
    this.add('`');
  }};

  public String escapeChars(String input) {
    StringBuilder stringBuilder = new StringBuilder(input);
    for (int i = 0; i < stringBuilder.length(); i++) {
      final char c = stringBuilder.charAt(i);
      if (chars.contains(c)) {
        stringBuilder.replace(i, i + 1, "\\" + c);
        i += 1;
      }
    }
    return stringBuilder.toString();
  }

  public void deletedelay(Message msg, int seconds) {
    Timer t = new Timer();
    t.schedule(new TimerTask() {
      @Override
      public void run() {
        msg.delete().queue();
      }
    }, seconds * 1000);
  }

  public long convertToSeconds(TimeUnit timeUnit, int delay) {
    long seconds = 0;
    switch (timeUnit) {
      case DAYS:
        seconds = delay * 24 * 60 * 60;
        break;
      case HOURS:
        seconds = delay * 60 * 60;
        break;
      case MINUTES:
        seconds = delay * 60;
        break;
      case SECONDS:
        seconds = delay;
        break;
      case MILLISECONDS:
        seconds = delay / 1000;
        break;
      case MICROSECONDS:
        seconds = delay / 1000 / 1000;
        break;
      case NANOSECONDS:
        seconds = delay / 1000 / 1000 / 1000;
        break;
      default:
        seconds = -1;
    }
    return seconds;
  }

  public boolean containsGroup(List<CommandGroup> groups, MessageReceivedEvent e) {
    for (CommandGroup group : groups) {
      for (String s : group.groupIDs) {
        for (Role role : e.getGuild().getMember(e.getAuthor()).getRoles()) {
          if (role.getId().equalsIgnoreCase(s)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean containsGroup(List<CommandGroup> groups, Member m) {
    for (CommandGroup group : groups) {
      for (String s : group.groupIDs) {
        for (Role role : m.getRoles()) {
          if (role.getId().equalsIgnoreCase(s)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public TimeDate getTimeDateOfSeconds(int seconds){
    return new TimeDate(seconds);
  }

  public void clearChannel(TextChannel textChannel) {
    try {
      MessageHistory messageHistory = new MessageHistory(textChannel);
      while (!messageHistory.retrievePast(100).complete().isEmpty()) {
        messageHistory = new MessageHistory(textChannel);
        List<Message> msgs = messageHistory.retrievePast(100).complete();
        textChannel.deleteMessages(msgs).queue();
      }
    } catch (IllegalArgumentException ex) {
      textChannel.sendMessage("Message for clearing!").queue();
      clearChannel(textChannel);
    }
  }

  public boolean isInt(String integer) {
    try {
      Integer.parseInt(integer);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }


}
