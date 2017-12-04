package at.florian7843.chif17bot.main;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.cmds.*;
import at.florian7843.chif17bot.commands.cmds.CMDServerInfo;
import at.florian7843.chif17bot.lib.FileManager;
import at.florian7843.chif17bot.listeners.EVENTListenerCommands;
import at.florian7843.chif17bot.listeners.EVENTListenerJoin;
import at.florian7843.chif17bot.utils.Constants;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BotStarter {

  @Getter
  private static HashMap<String, Command> commands = new HashMap<String, Command>() {{
    this.put("clear".toLowerCase(), new CMDClear());
    this.put("roleids".toLowerCase(), new CMDRoleIDs());
    this.put("help".toLowerCase(), new CMDHelp());
    this.put("userinfo".toLowerCase(), new CMDUserInfo());
    this.put("serverinfo".toLowerCase(), new CMDServerInfo());
    this.put("game".toLowerCase(), new CMDGame());
    this.put("invites".toLowerCase(), new CMDInvites());
  }};

  static JDABuilder builder = new JDABuilder(AccountType.BOT);

  public static void main(String[] args) {
    try {
      doBasicFileSettings();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0x0001);
    }

    builder.setToken(Constants.getBotToken());
    builder.setAutoReconnect(true);
    builder.setStatus(OnlineStatus.ONLINE);

    loadListeners();

    try {
      JDA jda = builder.buildBlocking();
    } catch (LoginException e) {
      if (e.getMessage().equalsIgnoreCase("Provided token was null or empty!")) {
        System.err.println("The bot token isn't set in config file!");
        System.exit(0x0002);
      }
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (RateLimitedException e) {
      e.printStackTrace();
    }
  }

  public static void loadListeners() {
    builder.addEventListener(new EVENTListenerCommands());
    builder.addEventListener(new EVENTListenerJoin());
  }

  private static void doBasicFileSettings() throws IOException {
    if (!FileManager.isFileExisting(Constants.getConfigFile())) FileManager.createJsonFile("config", "");
    File config = Constants.getConfigFile();
    if (!FileManager.isJsonEntryExisting(config, "bot-token")) FileManager.addEntryToJsonFile(config, "bot-token", "");

    Constants.setBotToken(FileManager.getJsonEntry(config, "bot-token"));
  }
}
