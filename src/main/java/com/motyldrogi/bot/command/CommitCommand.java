package com.motyldrogi.bot.command;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.util.RestServiceType;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Component
public class CommitCommand implements CommandExecutor {

  @CommandInfo(value = "commit", minArguments = 1, maxArguments = 2, usage = "<username> [Yyyy-Mm-Dd]")
  @Override
  public void execute(CommandSender commandSender, List<String> args) {
    try {
      HttpResponse<JsonNode> httpResponse = Unirest
          .get(RestServiceType.GITHUB_ACTIVITY_API_URL)
          .header("Accept", "application/json")
          .routeParam("username", args.get(0))
          .asJsonAsync()
          .get();

      JSONArray yearsJsonArray = httpResponse.getBody().getObject().getJSONArray("years");

      if (yearsJsonArray.length() == 0) {
        String footerMessage = commandSender.getMessage("wrong-username");
        MessageEmbed messageEmbeds = new EmbedBuilder()
            .setColor(Color.RED)
            .setFooter(footerMessage, null)
            .build();

        commandSender.sendEmbedMessage(messageEmbeds);
        return;
      }

      String titleMessage = commandSender.getMessage("activity-title", args.get(0));

      if (args.size() == 1) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
            .setColor(Color.GREEN)
            .setTitle(titleMessage);

        for (int i = 0; i < yearsJsonArray.length(); i++) {
          String fieldMessage = commandSender
              .getMessage("year", yearsJsonArray.getJSONObject(i).getString("year"));
          embedBuilder.addField(fieldMessage,
              String.valueOf(yearsJsonArray.getJSONObject(i).getInt("total")), true);
        }

        commandSender.sendEmbedMessage(embedBuilder.build());
        return;
      }

      JSONArray contributionsJsonArray = httpResponse.getBody().getObject()
          .getJSONArray("contributions");

      String footerMessage = commandSender.getMessage("no-activity", args.get(1));
      EmbedBuilder embedBuilder = new EmbedBuilder()
          .setColor(Color.RED)
          .setFooter(footerMessage, null);

      for (int i = 0; i < contributionsJsonArray.length(); i++) {
        JSONObject jsonObject = contributionsJsonArray.getJSONObject(i);

        if (args.get(1).equals(jsonObject.getString("date"))) {
          embedBuilder.setTitle(titleMessage);
          embedBuilder.setColor(Color.decode(jsonObject.getString("color")));
          embedBuilder.addField(commandSender.getMessage("searching-date"), args.get(1), true);
          embedBuilder.addField(commandSender.getMessage("contributions"),
              String.valueOf(jsonObject.getInt("count")), true);
          embedBuilder.setFooter(null, null);
        }

      }

      commandSender.sendEmbedMessage(embedBuilder.build());
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

}
