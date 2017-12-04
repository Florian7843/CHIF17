package at.florian7843.chif17bot.commands.cmds.GameSystem.games;

import at.florian7843.chif17bot.commands.cmds.GameSystem.Game;
import at.florian7843.chif17bot.utils.Constants;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GAMEEncrypt implements Game {


  private HashMap<String, String> hashes = new HashMap<String, String>() {{
    this.put("ennis911", "D28FDC1943EC949E6E74E1934941485E");
    this.put("bluedolphin", "C2F34E1E64B7BFA5AEEE3A752FE41AD7");
    this.put("nono246", "6218efe64f5aceee6d257f9792691c6e");
    this.put("zylman", "5d207aa4d557511e073dc3535215ac0a");
    this.put("focika", "23d8ff390fde3bf958131f24665139a7");
    this.put("rybeks", "868fc9c6bc4509ac074c735b7b1018c2");
    this.put("hri001", "36a89fa7e01176af2c6105676c1d83ad");
    this.put("1193385174", "79916dd8832e77688eb08ce3a19f9e96");
    this.put("sany0305", "82ff23b42cc30ce7d800a3a99b4bbae3");
    this.put("laika57", "70337971fb6c4ecdc9c3079384f6bb11");
    this.put("arsen10", "7db912752cf14e2305a5aedc49dea217");
    this.put("trampek", "45e1cafa6b0370c617f6a08e2587b919");
    this.put("holatu", "a3d9cb2501c7b1a8890ac4348520019b");
    this.put("lee123", "f39c8f313f3449a39d36c761d028efc7");
    this.put("may526", "ee18e3692cf4977a4dfc92b81e830129");
    this.put("dorgal", "9422d8a4c924fbc61d0a816ca35f02cc");
  }};

  private ArrayList<String> hashables = new ArrayList<String>() {{
    for (Map.Entry<String, String> entry : hashes.entrySet()) {
      this.add(entry.getValue());
    }
  }};

  String nick;

  @Override
  public void startGame(String[] args, MessageReceivedEvent e) {
    nick = hashables.get(Constants.getRandom().nextInt(hashables.size()));
    e.getGuild().getController().setNickname(e.getGuild().getMember(e.getAuthor()), nick).queue();
    e.getTextChannel().sendMessage("Check your Discord name on this server. With it you can finish this mystery.").queue();
  }

  @Override
  public boolean finishGame(String[] args, MessageReceivedEvent e) {
    if (args.length == 2) {
      if (hashes.get(args[1]) != null && hashes.get(args[1]).equalsIgnoreCase(nick)) {
        e.getMessage().delete().queue();
        return true;
      }
    }
    e.getTextChannel().sendMessage(new EmbedBuilder().setDescription("Sorry. Thats wrong!").setColor(Color.RED).build()).queue();
    return false;
  }

  @Override
  public void resetGameforPlayer(String[] args, MessageReceivedEvent e) {
    e.getGuild().getController().setNickname(e.getGuild().getMember(e.getAuthor()), e.getAuthor().getName()).queue();
  }

  @Override
  public String tip() {
    return "MD5";
  }

  @Override
  public String gameName() {
    return "encrypt";
  }

  @Override
  public String whatToDo() {
    return ".game Encrypt <text to find out>";
  }
}
