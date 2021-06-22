package com.motyldrogi.bot.configuration;

import com.motyldrogi.bot.command.defaults.impl.CommandRegistry;
import com.motyldrogi.bot.component.MessageComponent;
import com.motyldrogi.bot.service.BotService;
import com.motyldrogi.bot.service.impl.BotServiceImpl;

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
  public BotService botService() {
    return new BotServiceImpl();
  }

  @Bean
  public CommandRegistry commandRegistryBean() {
    return new CommandRegistry(this.messageComponent, this.botService(),
        this.properties);
  }

}
