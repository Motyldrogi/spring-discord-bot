package com.motyldrogi.bot.command;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.util.RestServiceType;

import java.awt.Color;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

@Component
public class EightBallCommand implements CommandExecutor {

  @CommandInfo(value = "8ball", minArguments = 1, maxArguments = Integer.MAX_VALUE)
  @Override
  public void execute(CommandSender commandSender, List<String> args) {
    try {
      HttpResponse<JsonNode> httpResponse = Unirest
          .get(RestServiceType.ANIME_API_URL + "/img/8ball")
          .header("Accept", "application/json")
          .asJson();

      StringBuilder stringBuilder = new StringBuilder();

      for (String arg : args) {
        stringBuilder.append(arg).append(" ");
      }

      MessageChannel messageChannel = commandSender.getMessageChannel();
      String lastMessageId = messageChannel.getLatestMessageId();
      String lastMessage = messageChannel.retrieveMessageById(lastMessageId).complete().getContentRaw()
          .split(" ")[1];
      String properMessage = stringBuilder.toString();

      System.out.println("proper: " + properMessage);
      System.out.println("last: " + lastMessage);

      if (!properMessage.contains("?")) {
        MessageEmbed messageEmbed = new EmbedBuilder()
            .setColor(Color.RED)
            .setFooter("Do you know, that every question has a question mark at the end of it?", null)
            .build();

        commandSender.sendEmbedMessage(messageEmbed);
        return;
      }

      MessageEmbed messageEmbed = new EmbedBuilder()
          .setColor(Color.decode("#3b5998"))
          .setTitle(stringBuilder.toString())
          .setImage(httpResponse.getBody().getObject().getString("url"))
          .build();

      commandSender.sendEmbedMessage(messageEmbed);
    } catch (UnirestException e) {
      e.printStackTrace();
    }
  }

}
