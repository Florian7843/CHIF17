package at.florian7843.chif17bot.listeners;

import at.florian7843.chif17bot.utils.Constants;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EVENTListenerTempChannel extends ListenerAdapter {

  public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
    Constants.getTempChannelManager().addToTextChannel(e.getMember(), e.getChannelJoined());
    Constants.getLogger().log(Constants.getTempChannelManager().getTempChannels().get(e.getChannelJoined()).getName() + " added " + e.getMember().getNickname() + "!");
  }

  @Override
  public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
    Constants.getTempChannelManager().removeFromTextChannel(e.getMember(), e.getChannelLeft());
    if (e.getChannelLeft().getMembers().size() == 0) {
      Constants.getUtils().clearChannel(Constants.getTempChannelManager().getTempChannels().get(e.getChannelLeft()));
      Constants.getLogger().log(Constants.getTempChannelManager().getTempChannels().get(e.getChannelLeft()).getName() + " cleared!");
    }
  }

  @Override
  public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
    Constants.getTempChannelManager().addToTextChannel(e.getMember(), e.getChannelJoined());
    Constants.getTempChannelManager().removeFromTextChannel(e.getMember(), e.getChannelLeft());
    Constants.getLogger().log(Constants.getTempChannelManager().getTempChannels().get(e.getChannelJoined()).getName() + " added " + e.getMember().getNickname() + "!");
    if (e.getChannelLeft().getMembers().size() == 0) {
      Constants.getUtils().clearChannel(Constants.getTempChannelManager().getTempChannels().get(e.getChannelLeft()));
      Constants.getLogger().log(Constants.getTempChannelManager().getTempChannels().get(e.getChannelLeft()).getName() + " cleared!");
    }
  }

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent e) {
    for (VoiceChannel vc : e.getGuild().getVoiceChannels()) {
      for (Member m : e.getGuild().getMembers()) {
        if (Constants.getTempChannelManager().getTempChannels().get(vc).getPermissionOverride(m).getDeniedRaw() != Constants.getTempChannelManager().getPermlevel()) {
          Constants.getTempChannelManager().removeFromTextChannel(m, vc);
        }
      }
    }
  }

}
