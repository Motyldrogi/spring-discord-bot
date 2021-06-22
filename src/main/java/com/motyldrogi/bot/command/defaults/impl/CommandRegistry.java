package com.motyldrogi.bot.command.defaults.impl;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.motyldrogi.bot.command.defaults.Command;
import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.configuration.AppProperties;
import com.motyldrogi.bot.service.BotService;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class CommandRegistry {

  private final MessageComponent messageComponent;
  private final BotService botService;
  private final AppProperties properties;

  public CommandRegistry(MessageComponent messageComponent, BotService botService,
  AppProperties properties) {
    this.messageComponent = messageComponent;
    this.botService = botService;
    this.properties = properties;
  }

  @SafeVarargs
  public final void registerByClasses(Class<? extends CommandExecutor>... classes) {
    try {
      for (Class<? extends CommandExecutor> _class : classes) {
        CommandExecutor commandExecutor = _class.getDeclaredConstructor().newInstance();
        this.registerByExecutors(commandExecutor);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void registerByExecutors(CommandExecutor... commandExecutors) {
    for (CommandExecutor commandExecutor : commandExecutors) {
      Method[] methods = commandExecutor.getClass().getMethods();

      for (Method method : methods) {
        if (method.isAnnotationPresent(CommandInfo.class)) {
          CommandInfo commandInfo = method.getAnnotation(CommandInfo.class);

          Command command = new CommandBuilder()
              .withName(commandInfo.value())
              .withUsage(commandInfo.usage())
              .withMinArguments(commandInfo.minArguments())
              .withMaxArguments(commandInfo.maxArguments())
              .withCommandExecutor(commandExecutor)
              .build();

          this.botService.getJda().addEventListener(new AnnotatedEventManager() {

            @SubscribeEvent
            public void messageReceivedEvent(MessageReceivedEvent event) {
              MessageChannel messageChannel = event.getChannel();
              String messageContent = event.getMessage().getContentRaw();
              CommandSender commandSender = new CommandSenderImpl(messageChannel, messageComponent,
                  event.getAuthor());

              String prefix = properties.getPrefix();

              if (messageContent.startsWith(prefix + command.getName())) {
                List<String> args = Arrays.stream(messageContent.split(" ")).skip(1)
                    .collect(Collectors.toList());

                if ((args.size() < command.getMinArguments()) || (args.size() > command
                    .getMaxArguments())) {

                  String usage = prefix + command.getName() + " " + command.getUsage();
                  MessageEmbed messageEmbed = new EmbedBuilder()
                      .setColor(Color.RED)
                      .setFooter(messageComponent.get("invalid-pattern", usage), null)
                      .build();
                  messageChannel.sendMessage(messageEmbed).queue();
                  return;
                }

                commandExecutor.execute(commandSender, args);
              }
            }

          });

        }
      }
    }
  }

}
