package com.motyldrogi.bot.configuration;

import com.motyldrogi.bot.command.defaults.impl.CommandRegistry;
import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.service.DiscordService;
import com.motyldrogi.bot.service.impl.DiscordServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringApplicationConfiguration {

  private final MessageComponent messageComponent;
  private final AppProperties properties;

  public SpringApplicationConfiguration(MessageComponent messageComponent,
  AppProperties properties) {
    this.messageComponent = messageComponent;
    this.properties = properties;
  }

  @Bean
  public DiscordService botService() {
    return new DiscordServiceImpl(this.messageComponent, this.properties);
  }

  @Bean
  public CommandRegistry commandRegistryBean() {
    return new CommandRegistry(this.botService());
  }

}
