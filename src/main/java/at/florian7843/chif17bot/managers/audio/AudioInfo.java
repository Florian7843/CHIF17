package at.florian7843.chif17bot.managers.audio;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;


public class AudioInfo {

  private final AudioTrack TRACK;
  private final Member AUTHOR;

  public AudioInfo(AudioTrack TRACK, Member AUTHOR) {
    this.TRACK = TRACK;
    this.AUTHOR = AUTHOR;
  }

  public AudioTrack getTRACK() {
    return TRACK;
  }

  public Member getAUTHOR() {
    return AUTHOR;
  }


}
