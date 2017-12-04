package at.florian7843.chif17bot.lib;

import lombok.Getter;
import lombok.Setter;

public class TimeDate {

  @Getter private int days;
  @Getter private int hours;
  @Getter private int minutes;
  @Getter private int seconds;

  public TimeDate(int seconds) {
    while (seconds >= 60) {
      if (seconds / 60 / 60 / 24 >= 1) {
        seconds -= 60 * 60 * 24;
        this.days++;
      } else if (seconds / 60 / 60 >= 1) {
        seconds -= 60 * 60;
        this.hours++;
      } else if (seconds / 60 >= 1) {
        seconds -= 60;
        this.minutes++;
      }
    }
      this.seconds = seconds;
  }
}
