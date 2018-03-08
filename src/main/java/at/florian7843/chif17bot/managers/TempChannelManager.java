package at.florian7843.chif17bot.managers;

import at.florian7843.chif17bot.utils.Constants;
import lombok.Getter;
import net.dv8tion.jda.core.entities.*;

import java.util.HashMap;

public class TempChannelManager {

  @Getter
  private HashMap<Guild, Category> category = new HashMap<>();
  @Getter
  private HashMap<VoiceChannel, TextChannel> tempChannels = new HashMap<>();
  @Getter
  private HashMap<Member, TextChannel> allowedChannels = new HashMap<>();
  @Getter
  private long permlevel = 523328;

  public void init() {
    createTempCategory();
    for (Guild g : Constants.getJda().getGuilds()) {
      for (VoiceChannel vc : g.getVoiceChannels()) {
        if (g.getTextChannelsByName("temp-" + vc.getName().replaceAll(" ", "-").replaceAll(":","").replaceAll("'", ""), true).size() == 0) {
          this.createTextChannel(vc);
        }
        getTempChannels().put(vc, g.getTextChannelsByName("temp-" +  vc.getName().replaceAll(" ", "-").replaceAll(":","").replaceAll("'", ""), true).get(0));

        for (Member m : g.getMembers()) {
          if (getTempChannels().get(vc).getPermissionOverride(m) == null && !vc.getMembers().contains(m)) {
            removeFromTextChannel(m, vc);
          }
          if (vc.getMembers().contains(m)) {
            addToTextChannel(m, vc);
          }
        }
      }
    }
  }

  public void createTempCategory() {
    for (Guild g : Constants.getJda().getGuilds()) {
      if (g.getCategoriesByName("temp-channel", true).size() == 0) {
        g.getController().createCategory("temp-channel").queue();
      }
      getCategory().put(g, g.getCategoriesByName("temp-channel", true).get(0));
    }
  }

  public void deleteTempCategory() {
    for (Guild g : Constants.getJda().getGuilds()) {
      for (Category c : g.getCategoriesByName("temp-channels", true)) {
        c.delete().queue();
      }
    }
  }

  private boolean existsCategory(Guild guild) {
    try {
      guild.getCategoriesByName("temp-channels", true).get(0);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  public boolean hasTextChannel(VoiceChannel vc) {
    if (getTempChannels().get(vc) != null) {
      return true;
    }
    return false;
  }

  public void createTextChannel(VoiceChannel vc) {
    TextChannel tc = (TextChannel) vc.getGuild().getController().createTextChannel("temp-" + vc.getName().replaceAll(" ", "-")).complete();
    tc.getManager().setParent(getCategory().get(vc.getGuild())).queue();
    getTempChannels().put(vc, tc);
    for (Member m : vc.getGuild().getMembers()) {
      if (m.getUser() != vc.getGuild().getSelfMember().getUser()) {
        removeFromTextChannel(m, vc);
      }
    }
  }

  public void addToTextChannel(Member m, VoiceChannel vc) {
    if (getTempChannels().get(vc).getPermissionOverride(m) != null) {
      getTempChannels().get(vc).getPermissionOverride(m).delete().queue();
    }
  }

  public void removeFromTextChannel(Member m, VoiceChannel vc) {
    if (getTempChannels().get(vc).getPermissionOverride(m) == null) {
      getTempChannels().get(vc).createPermissionOverride(m).setDeny(this.permlevel).queue();
    }
    if (getAllowedChannels().containsKey(m)) {
      getAllowedChannels().remove(m);
    }
  }

  public void deleteTextChannel(VoiceChannel vc) throws NullPointerException {
    getTempChannels().get(vc).delete().queue();
    getTempChannels().remove(vc);
  }

}
