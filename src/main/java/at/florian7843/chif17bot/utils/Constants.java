package at.florian7843.chif17bot.utils;

import at.florian7843.chif17bot.lib.FileManager;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Random;

public class Constants {

  @Getter @Setter private static String BotToken = "";
  @Getter private static String invoke = ".";

  @Getter private static FileManager FileManager = new FileManager();
  @Getter private static Utils Utils = new Utils();

  @Getter private static File configFile = new File("config.json");

  @Getter private static Random random = new Random();
  @Getter private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
}
