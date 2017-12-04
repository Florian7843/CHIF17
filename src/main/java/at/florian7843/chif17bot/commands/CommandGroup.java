package at.florian7843.chif17bot.commands;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public enum CommandGroup {

  ADMIN(new ArrayList<String>() {{
    this.add("384327664992911401");
    this.add("385836257575567360");
  }}),

  EVERYONE(new ArrayList<String>() {{
    this.add("384327664992911401");
    this.add("385836257575567360");
    this.add("385390411667210241");
    this.add("384306642277367819");
  }});

  public final ArrayList<String> groupIDs;

  CommandGroup(ArrayList<String> groupIDs) {
    this.groupIDs = groupIDs;
  }
}
