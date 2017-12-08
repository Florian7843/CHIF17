package at.florian7843.chif17bot.utils;

import at.florian7843.chif17bot.lib.FileManager;
import at.florian7843.chif17bot.lib.Logger;
import at.florian7843.chif17bot.managers.TempChannelManager;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.JDA;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Constants {

  @Getter @Setter private static String BotToken = "";
  @Getter
  @Setter
  private static String invoke = ".";
  @Getter
  @Setter
  private static JDA jda;

  @Getter private static FileManager FileManager = new FileManager();
  @Getter private static Utils Utils = new Utils();
  @Getter
  private static TempChannelManager tempChannelManager = new TempChannelManager();
  @Getter
  private static Logger logger = new Logger();

  @Getter private static File configFile = new File("config.json");

  @Getter private static Random random = new Random();
  @Getter private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
}
