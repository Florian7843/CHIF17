package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.commands.cmds.GameSystem.Game;
import at.florian7843.chif17bot.commands.cmds.GameSystem.games.GAMEEncrypt;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CMDGame implements Command {

  @Getter
  private static HashMap<String, Game> games = new HashMap<String, Game>() {{
    this.put("encrypt", new GAMEEncrypt());
  }};

  @Getter
  private static HashMap<Member, Game> inGame = new HashMap<>();

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
      String game = "";
      for (Map.Entry<String, Game> games : getGames().entrySet()) {
        game += games.getKey() + " --> " + games.getValue().whatToDo();
      }
      game += "\nIf you need Tip then: .game tip";
      e.getTextChannel().sendMessage(new EmbedBuilder().setDescription(game).setTitle("Games: ").build()).queue();
      return CommandState.SUCESS;
    } else if (args.length == 1 && args[0].equalsIgnoreCase("tip")) {
      if (inGame.containsKey(e.getGuild().getMember(e.getAuthor()))) {
        e.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Tip to game: " + inGame.get(e.getGuild().getMember(e.getAuthor())).gameName()).setDescription(inGame.get(e.getGuild().getMember(e.getAuthor())).tip()).build()).queue();
        return CommandState.SUCESS;
      } else {
        e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("You are not in a Game!").build()).queue();
        return CommandState.SUCESS;
      }
    } else if (args.length >= 1 && args[0].toLowerCase() != "help") {
      if (getGames().containsKey(args[0].toLowerCase())) {
        if (!getInGame().containsKey(e.getGuild().getMember(e.getAuthor()))) {
          getGames().get(args[0]).startGame(args, e);
          getInGame().put(e.getGuild().getMember(e.getAuthor()), getGames().get(args[0]));
          return CommandState.SUCESS;
        } else if (getInGame().get(e.getGuild().getMember(e.getAuthor())) == getGames().get(args[0])) {
          boolean finished = getInGame().get(e.getGuild().getMember(e.getAuthor())).finishGame(args, e);
          if (finished == true) {
            getInGame().get(e.getGuild().getMember(e.getAuthor())).resetGameforPlayer(args, e);
            getInGame().remove(e.getGuild().getMember(e.getAuthor()));
            e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription("Congratulations! You solved the mystery!").build()).queue();
            return CommandState.SUCESS;
          }
          return CommandState.SUCESS;
        } else {
          e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("Du bist bereit in einem Game.").build()).queue();
          return CommandState.SUCESS;
        }
      }
    }
    return CommandState.PRINT_SYNTAX;
  }

  @Override
  public String help() {
    return "Game system";
  }

  @Override
  public String syntax() {
    return ".game <game/help/tip>";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.EVERYONE);
    }};
  }
}
