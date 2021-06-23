package com.motyldrogi.bot.command;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.DiscordMessage;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class SayCommand implements CommandExecutor {

  @CommandInfo(value = "say", minArguments = 1, maxArguments = Integer.MAX_VALUE, usage = "(?:{\"type\":\")(.*?)(?:\",.\"message\":\")(.*?)(?:\"})")
  @Override
  public void execute(DiscordMessage dMessage, CommandSender commandSender) {

    Pattern pattern = Pattern
        .compile("\\{\\s*\"type\"\\s*:\\s*(.*?)\\s*,\\s*\"message\"\\s*:\\s*(.*?)}");
    Matcher matcher = pattern.matcher(dMessage.getData());

    if (!matcher.find()) {
      this.performMessageFooter(commandSender, commandSender.getMessage("invalid-json"), Color.RED);
      return;
    }

    JSONObject jsonObject = new JSONObject(dMessage.getData());

    switch (jsonObject.getString("type")) {
      case "normal":
        commandSender.sendRawMessage(String.valueOf(jsonObject.get("message")));
        break;
      case "footer":
        this.performMessageFooter(commandSender, String.valueOf(jsonObject.get("message")),
            Color.decode("#2b2b2b"));
        break;
      default:
        this.performMessageFooter(commandSender, commandSender.getMessage("wrong-message-type"), Color.RED);
    }
  }

  private void performMessageFooter(CommandSender commandSender, String message, Color color) {
    MessageEmbed messageEmbed = new EmbedBuilder()
        .setColor(color)
        .setFooter(message, null)
        .build();

    commandSender.sendEmbedMessage(messageEmbed);
  }

}
