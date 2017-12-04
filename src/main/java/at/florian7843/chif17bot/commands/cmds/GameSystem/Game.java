package at.florian7843.chif17bot.commands.cmds.GameSystem;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Game {

  void startGame(String[] args, MessageReceivedEvent e);

  boolean finishGame(String[] args, MessageReceivedEvent e);

  void resetGameforPlayer(String[] args, MessageReceivedEvent e);

  String tip();

  String gameName();

  String whatToDo();

}
