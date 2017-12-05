package at.florian7843.chif17bot.lib;

import at.florian7843.chif17bot.utils.Constants;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Logger {

  @Getter
  private File logFile;

  public Logger() {
    try {
      this.logFile = FileManager.createTxtFile("log", "");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void log(String channel, String authorMention, String message) {
    Date date = new Date();
    try {
      FileManager.addTxtFileEntry(getLogFile(), "[" + Constants.getSimpleDateFormat().format(date) + "] " + channel + " -- " + authorMention.replaceAll("<", "").replaceAll(">", "") + " -- " + message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
