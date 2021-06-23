package com.motyldrogi.bot.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;
import java.awt.Color;
import com.motyldrogi.bot.command.defaults.Command;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.command.defaults.impl.CommandSenderImpl;
import com.motyldrogi.bot.component.DiscordMessage;
import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.configuration.AppProperties;
import com.motyldrogi.bot.service.DiscordService;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordServiceImpl implements DiscordService {

  private final MessageComponent messageComponent;
  private final AppProperties properties;

  private JDA jda;

  public DiscordServiceImpl(MessageComponent messageComponent, AppProperties properties) {
    this.messageComponent = messageComponent;
    this.properties = properties;
  }

  @Override
  public void startBot() throws LoginException, InterruptedException {
    this.jda = JDABuilder.createLight(properties.getDiscordToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
    .setEventManager(new AnnotatedEventManager())
    .setActivity(Activity.playing(properties.getPrefix()))
    .build();
  }

  @Override
  public void shutdownBot() {
    this.jda.shutdown();
  }

  @Override
  public void registerListeners(Object... listeners) {
    this.jda.addEventListener(listeners);
  }

  @Override
  public JDA getJda() {
    return this.jda;
  }

  @Override
  public void registerCommand(Command botCommand) {
    this.jda.addEventListener(new AnnotatedEventManager() {

      @SubscribeEvent
      public void messageReceivedEvent(MessageReceivedEvent event) {
        processMessage(event, botCommand);
      }

    });
  }

  public void processMessage(MessageReceivedEvent event, Command command) {
    DiscordMessage dMessage = new DiscordMessage(event, command, properties.getPrefix());

    String messageContent = event.getMessage().getContentRaw();
    if (messageContent.startsWith(properties.getPrefix() + command.getName())) {
      this.processCommand(dMessage);
    }
  }

  private void processCommand(DiscordMessage dMessage) {
    MessageChannel messageChannel = dMessage.getEvent().getChannel();
    CommandSender commandSender = new CommandSenderImpl(messageChannel, messageComponent);
    Command command = dMessage.getCommandData();

    List<String> args = Arrays.stream(dMessage.getEvent().getMessage().getContentRaw().split(" ")).skip(1)
      .collect(Collectors.toList());

    if ((args.size() < command.getMinArguments()) || (args.size() > command.getMaxArguments())) {
      String usage = properties.getPrefix() + command.getName() + " " + command.getUsage();
      MessageEmbed messageEmbed = new EmbedBuilder()
        .setColor(Color.RED)
        .setFooter(messageComponent.get("invalid-pattern", usage), null)
        .build();
      messageChannel.sendMessage(messageEmbed).queue();
      return;
    }

    dMessage.getCommandData().getExecutor().execute(dMessage, commandSender);
  }

}
