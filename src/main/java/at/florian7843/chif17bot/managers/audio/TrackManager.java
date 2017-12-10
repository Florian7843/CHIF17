package at.florian7843.chif17bot.managers.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter {

  private final AudioPlayer PLAYER;
  private final Queue<AudioInfo> QUEUE;

  public TrackManager(AudioPlayer PLAYER) {
    this.PLAYER = PLAYER;
    this.QUEUE = new LinkedBlockingQueue<>();
  }

  public void queue(AudioTrack track, Member author) {
    AudioInfo info = new AudioInfo(track, author);
    QUEUE.add(info);

    if (PLAYER.getPlayingTrack() == null) {
      PLAYER.playTrack(track);
    }
  }

  public Set<AudioInfo> getQueue() {
    return new LinkedHashSet<>(QUEUE);
  }

  public AudioInfo getInfo(AudioTrack track) {
    return QUEUE.stream()
        .filter(info -> info.getTRACK().equals(track))
        .findFirst().orElse(null);
  }

  public void clearQueue() {
    QUEUE.clear();
  }

  public void shuffleQueue() {
    List<AudioInfo> cQueue = new ArrayList<>(getQueue());
    AudioInfo current = cQueue.get(0);
    cQueue.remove(0);
    Collections.shuffle(cQueue);
    cQueue.add(0, current);
    clearQueue();
    QUEUE.addAll(cQueue);
  }

  @Override
  public void onTrackStart(AudioPlayer player, AudioTrack track) {
    AudioInfo info = QUEUE.element();
    VoiceChannel vc = info.getAUTHOR().getVoiceState().getChannel();

    if (vc == null) player.stopTrack();
    else info.getAUTHOR().getGuild().getAudioManager().openAudioConnection(vc);
  }

  @Override
  public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
    Guild g = QUEUE.poll().getAUTHOR().getGuild();

    if (QUEUE.isEmpty()) {
      Timer t = new Timer();
      t.schedule(
          new TimerTask() {
            @Override
            public void run() {
              g.getAudioManager().closeAudioConnection();
            }
          }, 2 * 100
      );
    } else player.playTrack(QUEUE.element().getTRACK());
  }

}
