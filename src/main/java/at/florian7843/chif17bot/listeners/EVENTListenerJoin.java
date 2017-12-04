package at.florian7843.chif17bot.listeners;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EVENTListenerJoin extends ListenerAdapter {

  public void onGuildMemberJoin(GuildMemberJoinEvent e) {
    e.getGuild().getController().addRolesToMember(e.getMember(), e.getGuild().getRoleById("385390411667210241")).queue();
  }
}
