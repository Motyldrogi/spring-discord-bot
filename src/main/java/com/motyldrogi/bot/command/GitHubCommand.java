package com.motyldrogi.bot.command;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.DiscordMessage;
import com.motyldrogi.bot.util.RestServiceType;

import java.awt.Color;
import java.util.concurrent.ExecutionException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class GitHubCommand implements CommandExecutor {

  @CommandInfo(value = "github", minArguments = 1, maxArguments = 1, usage = "<username>")
  @Override
  public void execute(DiscordMessage dMessage, CommandSender commandSender) {
    try {
      HttpResponse<JsonNode> httpResponse = Unirest.get(RestServiceType.GITHUB_API_URL)
          .header("Accept", "application/json")
          .routeParam("username", dMessage.getData())
          .asJsonAsync()
          .get();

      if (httpResponse.getStatus() == 404) {
        String footerMessage = commandSender.getMessage("wrong-username");
        MessageEmbed messageEmbeds = new EmbedBuilder()
            .setColor(Color.RED)
            .setFooter(footerMessage, null)
            .build();

        commandSender.sendEmbedMessage(messageEmbeds);
        return;
      }

      JSONObject jsonObject = httpResponse.getBody().getObject();

      String login = jsonObject.getString("login");
      String profileUrl = jsonObject.getString("html_url");
      String bio = jsonObject.isNull("bio") ? "" : String.valueOf(jsonObject.get("bio"));
      String location = jsonObject.isNull("location") ? "" : String.valueOf(jsonObject.get("location"));
      String registrationDate = jsonObject.getString("created_at");
      String avatarUrl = jsonObject.getString("avatar_url");
      int followers = jsonObject.getInt("followers");
      int following = jsonObject.getInt("following");
      int repositories = jsonObject.getInt("public_repos");
      int gists = jsonObject.getInt("public_gists");

      MessageEmbed messageEmbed = new EmbedBuilder()
          .setColor(Color.decode("#2b2b2b"))
          .setThumbnail(avatarUrl)
          .addField(commandSender.getMessage("name"), "[" + login + "]" + "(" + profileUrl + ")", true)
          .addField(commandSender.getMessage("bio"), bio, true)
          .addField(commandSender.getMessage("location"), location, true)
          .addField(commandSender.getMessage("registration"), registrationDate, true)
          .addField(commandSender.getMessage("followers"), String.valueOf(followers), true)
          .addField(commandSender.getMessage("following"), String.valueOf(following), true)
          .addField(commandSender.getMessage("repositories"), String.valueOf(repositories), true)
          .addField(commandSender.getMessage("gists"), String.valueOf(gists), true)
          .build();

      commandSender.sendEmbedMessage(messageEmbed);
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
  }

}
