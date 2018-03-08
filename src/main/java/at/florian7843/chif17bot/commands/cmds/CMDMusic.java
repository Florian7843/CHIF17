package at.florian7843.chif17bot.commands.cmds;

import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.managers.audio.AudioInfo;
import at.florian7843.chif17bot.managers.audio.PlayerSendHandler;
import at.florian7843.chif17bot.managers.audio.TrackManager;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CMDMusic implements Command {

  private static final int PLAYLIST_LIMIT = 200;
  private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
  private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();

  public CMDMusic() {
    AudioSourceManagers.registerRemoteSources(MANAGER);
  }

  private AudioPlayer createPlayer(Guild g) {
    AudioPlayer p = MANAGER.createPlayer();
    TrackManager trackManager = new TrackManager(p);
    p.addListener(trackManager);

    g.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

    PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, trackManager));
    return p;
  }

  private boolean hasPlayer(Guild g) {
    return PLAYERS.containsKey(g);
  }

  private AudioPlayer getPlayer(Guild g) {
    if (hasPlayer(g)) return PLAYERS.get(g).getKey();
    else return createPlayer(g);
  }

  private TrackManager getManager(Guild g) {
    return PLAYERS.get(g).getValue();
  }

  private boolean isIdle(Guild g) {
    return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
  }

  private void loadTrack(String identifier, Member author) {
    Guild guild = author.getGuild();
    getPlayer(guild);

    MANAGER.setFrameBufferDuration(5000);
    MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {
      @Override
      public void trackLoaded(AudioTrack track) {
        getManager(guild).queue(track, author);
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist) {
        for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
          getManager(guild).queue(playlist.getTracks().get(i), author);
        }
      }

      @Override
      public void noMatches() {

      }

      @Override
      public void loadFailed(FriendlyException exception) {

      }
    });

  }

  private void skip(Guild g) {
    getPlayer(g).stopTrack();
  }

  private String getTimeStamp(long millis) {
    long seconds = millis / 1000;
    long hours = Math.floorDiv(seconds, 3600);
    seconds = seconds - (hours * 3600);
    long mins = Math.floorDiv(seconds, 60);
    seconds = seconds - (mins * 60);
    return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
  }

  private String buildQueueMessage(AudioInfo info) {
    AudioTrackInfo trackInfo = info.getTRACK().getInfo();
    String title = trackInfo.title;
    long lenght = trackInfo.length;
    return "[" + getTimeStamp(lenght) + "]   >>   " + title + "\n";
  }

  private void sendErrorMessage(MessageReceivedEvent e, String content) {
    e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(content).build()).queue();
  }

  private void sendSuccessMessage(MessageReceivedEvent e, String content) {
    e.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.GREEN).setDescription(content).build()).queue();
  }

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {

    if (args.length < 1) return CommandState.PRINT_SYNTAX;

    switch (args[0]) {
      case "play":
      case "-p":
        if (args.length < 2) return CommandState.PRINT_SYNTAX;
        String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

        if (!(input.startsWith("https://") || input.startsWith("http://") || input.startsWith("www."))) input = "ytsearch: " + input;

        loadTrack(input, e.getGuild().getMember(e.getAuthor()));
        sendSuccessMessage(e, "Loading track!");
        return CommandState.SUCESS;
      case "skip":
      case "-sk":
        skip(e.getGuild());
        sendSuccessMessage(e, "Skipped track!");
        return CommandState.SUCESS;
      case "stop":
      case "-st":
        getManager(e.getGuild()).clearQueue();
        skip(e.getGuild());
        Timer t = new Timer();
        t.schedule(new TimerTask() {
          @Override
          public void run() {
            e.getGuild().getAudioManager().closeAudioConnection();
          }
        }, 2 * 100);
        sendSuccessMessage(e, "Stopped the queue!");
        return CommandState.SUCESS;
      case "shuffle":
      case "-sh":
        getManager(e.getGuild()).shuffleQueue();
        sendSuccessMessage(e, "The queue is now shuffled!");
        return CommandState.SUCESS;
      case "queue":
      case "-q":
        EmbedBuilder queue = new EmbedBuilder().setTitle("Queue: ").setColor(Color.GRAY);
        if (!hasPlayer(e.getGuild()) || getManager(e.getGuild()).getQueue().isEmpty()) {
          sendErrorMessage(e, "Queue is empty!");
          return CommandState.SUCESS;
        }
        int entrys = 0;
        for (AudioInfo audioInfo : getManager(e.getGuild()).getQueue()) {
          queue.addField(buildQueueMessage(audioInfo), "", false);
          entrys++;
        }
        e.getTextChannel().sendMessage(queue.setFooter(entrys + "/" + getManager(e.getGuild()).getQueue().size(), null).build()).queue();
        return CommandState.SUCESS;
      case "info":
      case "-i":
        EmbedBuilder info = new EmbedBuilder().setTitle("Song Information: ").setColor(Color.GRAY);
        info.addField("Title:", getPlayer(e.getGuild()).getPlayingTrack().getInfo().title, false);
        info.addField("Duration:", getTimeStamp(getPlayer(e.getGuild()).getPlayingTrack().getInfo().length), false);
        info.addField("Requested by:", getManager(e.getGuild()).getInfo(getPlayer(e.getGuild()).getPlayingTrack()).getAUTHOR().getEffectiveName(), false);
        e.getTextChannel().sendMessage(info.build()).queue();
        return CommandState.SUCESS;
    }

    return CommandState.PRINT_SYNTAX;
  }

  @Override
  public String help() {
    return "With this command you can use the Music Bot!";
  }

  @Override
  public String syntax() {
    return ".music play <link/ytsearch>";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.ADMIN);
    }};
  }
}
