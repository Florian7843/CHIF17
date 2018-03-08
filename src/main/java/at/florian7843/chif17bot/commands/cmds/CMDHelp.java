package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.main.BotStarter;
import at.florian7843.chif17bot.utils.Constants;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class CMDHelp implements Command {

  public enum Emote {
    LEFT(new String(new byte[]{-30, -98, -95}, StandardCharsets.UTF_8)),
    RIGHT(new String(new byte[]{-30, -84, -123}, StandardCharsets.UTF_8));

    public final String emote;

    Emote(String emote) {
      this.emote = emote;
    }
  }

  private static final String ARROW_RIGHT_EMOTE = new String(new byte[]{-30, -98, -95}, StandardCharsets.UTF_8);
  private static final String ARROW_LEFT_EMOTE = new String(new byte[]{-30, -84, -123}, StandardCharsets.UTF_8);

  private ReactionClass reactionClass = new ReactionClass();

  public CMDHelp(JDABuilder builder) {
    builder.addEventListener(reactionClass);
  }

  public class ReactionClass extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
      if (e.getMember().getUser().isBot()) return;
      if (e.getReaction().getReactionEmote().getName().equals(ARROW_LEFT_EMOTE)) {
        updateMessage(e.getTextChannel().getMessageById(e.getMessageId()).complete(), CMDHelp.Emote.LEFT, e.getGuild().getMember(e.getUser()));
      } else if (e.getReaction().getReactionEmote().getName().equals(ARROW_RIGHT_EMOTE)) {
        updateMessage(e.getTextChannel().getMessageById(e.getMessageId()).complete(), Emote.RIGHT, e.getGuild().getMember(e.getUser()));
      }
    }
  }

  @Getter
  private static byte MAX_ENTRYS_PER_PAGE = 4;

  @Getter
  private HashMap<Message, Integer> messages = new HashMap<>();

  @Getter
  private HashMap<Message, Timer> timers = new HashMap<>();
  @Getter
  private List<Command> list = new ArrayList<Command>();

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    /*String commands = "";
    String syntax;
    for (Map.Entry<String, Command> cmd : BotStarter.getCommands().entrySet()) {
      if (Constants.getUtils().containsGroup(cmd.getValue().permissionGroups(), e)) {
        syntax = cmd.getValue().syntax();
        commands += syntax + " --> " + cmd.getValue().help() + "\n";
      }
    }

    e.getTextChannel().sendMessage(new EmbedBuilder().setDescription(commands).setTitle("Commands: ").setColor(Color.GREEN).build()).queue();*/

    createMessage(e.getTextChannel(), e.getMember());

    return CommandState.SUCESS;
  }

  public Message updateMessage(Message msg, Emote dir, Member m) {
    EmbedBuilder eb = new EmbedBuilder().setColor(Color.GRAY).setTitle("Commands: ");
    int page = getMessages().get(msg);
    TextChannel tc = msg.getTextChannel();
    if (dir == Emote.RIGHT) page++;
    if (dir == Emote.LEFT) page--;
    addCommandsToMessageEmbed(page, eb, list, m);
    msg.delete().queue();
    getMessages().remove(msg);
    Message nmsg = tc.sendMessage(eb.build()).complete();
    getMessages().put(nmsg, page);
    if (page * MAX_ENTRYS_PER_PAGE > MAX_ENTRYS_PER_PAGE) nmsg.addReaction(ARROW_LEFT_EMOTE).queue();
    if (list.size() > page * MAX_ENTRYS_PER_PAGE) nmsg.addReaction(ARROW_RIGHT_EMOTE).queue();
    return nmsg;
  }

  public Message createMessage(TextChannel tc, Member m) {
    EmbedBuilder eb = new EmbedBuilder().setColor(Color.GRAY).setTitle("Commands: ");
    addCommandsToMessageEmbed(1, eb, list, m);
    Message msg = tc.sendMessage(eb.build()).complete();
    if (list.size() > MAX_ENTRYS_PER_PAGE) msg.addReaction(ARROW_RIGHT_EMOTE).queue();
    getMessages().put(msg, 1);
    return msg;
  }

  private int addCommandsToMessageEmbed(int site, EmbedBuilder embedBuilder, List<Command> commandList, Member m) {
    if (list.isEmpty()) list.addAll(BotStarter.getCommands().values());
    final int offset = (site - 1) * MAX_ENTRYS_PER_PAGE;
    final int iterateTo = offset + MAX_ENTRYS_PER_PAGE;
    if (offset == iterateTo) {
      throw new IllegalStateException("Can not display site " + site + " because it would not contain any item!");
    }
    for (int i = offset; i < iterateTo && i != commandList.size(); i++) {
      final Command command = commandList.get(i);
      if (Constants.getUtils().containsGroup(command.permissionGroups(), m))
        embedBuilder.addField(command.syntax(), command.help(), false);
    }
    return iterateTo;
  }

  @Override
  public String help() {
    return "This command shows you every for you allowed Command!";
  }

  @Override
  public String syntax() {
    return ".help";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.EVERYONE);
    }};
  }
}
