package com.motyldrogi.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:app.properties")
@ConfigurationProperties
public class AppProperties {

  private String prefix;

  private String discordToken;

  public String getPrefix() {
    return prefix;
  }

  public String getDiscordToken() {
    return discordToken;
  }

  public void setDiscordToken(String discordToken) {
    this.discordToken = discordToken;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

}
