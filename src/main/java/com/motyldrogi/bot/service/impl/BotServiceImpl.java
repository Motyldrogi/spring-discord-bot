package com.motyldrogi.bot.service.impl;

import javax.security.auth.login.LoginException;

import com.motyldrogi.bot.configuration.AppProperties;
import com.motyldrogi.bot.service.BotService;

import org.springframework.beans.factory.annotation.Autowired;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class BotServiceImpl implements BotService {

  private JDA jda;

  @Autowired
  private AppProperties properties;

  @Override
  public void startBot() throws LoginException, InterruptedException {
    this.jda = JDABuilder.createLight(properties.getDiscordToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
    .setEventManager(new AnnotatedEventManager())
    .setActivity(Activity.playing("g!"))
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

}
