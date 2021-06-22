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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

@Component
public class FactCommand implements CommandExecutor {

  @CommandInfo("fact")
  @Override
  public void execute(CommandSender commandSender, List<String> args) {
    try {
      HttpResponse<JsonNode> httpResponse = Unirest.get(RestServiceType.ANIME_API_URL + "/fact")
          .header("Accept", "application/json")
          .asJsonAsync()
          .get();

      String factMessage = httpResponse.getBody().getObject().getString("fact");

      MessageEmbed messageEmbed = new EmbedBuilder()
          .setColor(Color.decode("#ff6666"))
          .setTitle("Did you know that..")
          .setDescription(factMessage)
          .build();

      commandSender.sendEmbedMessage(messageEmbed);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

}
