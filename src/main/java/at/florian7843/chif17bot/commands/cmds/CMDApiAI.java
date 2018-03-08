package at.florian7843.chif17bot.commands.cmds;

import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import at.florian7843.chif17bot.commands.Command;
import at.florian7843.chif17bot.commands.CommandGroup;
import at.florian7843.chif17bot.commands.CommandState;
import at.florian7843.chif17bot.utils.Constants;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CMDApiAI implements Command {

  public String replaceVariables(String input) {
    input = input.replaceAll("%time", new SimpleDateFormat("HH:mm").format(new Date()));
    input = input.replaceAll("%date", new SimpleDateFormat("dd.MM.YYYY").format(new Date()));
    if (input.toLowerCase().startsWith("!ACTION!CURRENCYEXCHANGE".toLowerCase())) {
      return CurrencyReplaces(input);
    }
    return input;
  }

  @Override
  public CommandState executed(String[] args, MessageReceivedEvent e) {
    String question = Arrays.stream(args).map(s -> " " + s).collect(Collectors.joining()).substring(1);
    try {
      AIRequest request = new AIRequest(question);
      AIResponse response = Constants.getAiDataService().request(request);

      e.getTextChannel().sendMessage(replaceVariables(response.getResult().getFulfillment().getSpeech())).queue();
      return CommandState.SUCESS;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return CommandState.PRINT_SYNTAX;
  }

  @Override
  public String help() {
    return "Here you can ask the AI build in the bot some questions!";
  }

  @Override
  public String syntax() {
    return ".ai <question>";
  }

  @Override
  public List<CommandGroup> permissionGroups() {
    return new ArrayList<CommandGroup>() {{
      this.add(CommandGroup.EVERYONE);
    }};
  }

  /*
  !ACTION!CURRENCYEXCHANGE %num10 %nowEuro are %nafter in %afterUS Dollar.
   */
  public String CurrencyReplaces(String input) {
    String[] args = input.split("%");
    double moneyFirst = Double.parseDouble(args[1].replaceAll("num", "").replaceAll(",", "."));
    double moneyAfter;
    String nowCurr = args[2].replaceAll("now", "").replaceAll(" are about ", "");
    String afterCurr = args[4].replaceAll("after", "").replaceAll("\\.", "");

    input = input.replaceAll("!ACTION!CURRENCYEXCHANGE ", "");

    input = input.replaceAll("%num", "").replaceAll("%now", "").replaceAll("%after", "");

    moneyAfter = moneyFirst * Constants.getCurrencyManager().getRate(Constants.getCurrencyManager().getCurrency().get(nowCurr), Constants.getCurrencyManager().getCurrency().get(afterCurr));

    input = input.replaceAll("%nafter", new DecimalFormat("#.######").format(moneyAfter) + "");

    return input;
  }
}
