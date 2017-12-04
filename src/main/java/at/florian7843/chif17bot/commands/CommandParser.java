package at.florian7843.chif17bot.commands;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class CommandParser {

  public static class commandContainer {
    public final String raw;
    public final String cmd;
    public final String[] args;
    public final MessageReceivedEvent e;

    public commandContainer(String raw, String cmd, String[] args, MessageReceivedEvent e) {
      this.raw = raw;
      this.cmd = cmd;
      this.args = args;
      this.e = e;
    }
  }

  public static commandContainer pharse(MessageReceivedEvent e) {
    String raw = e.getMessage().getRawContent();
    String cmd = raw.split(" ")[0];
    ArrayList split = new ArrayList();

    for (String s : raw.split(" ")) {
      split.add(s);
    }

    String[] args = new String[split.size() - 1];
    split.subList(1, split.size()).toArray(args);

    return new commandContainer(raw, cmd, args, e);
  }

}
