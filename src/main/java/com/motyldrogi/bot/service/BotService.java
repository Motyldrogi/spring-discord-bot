package com.motyldrogi.bot.service;

import javax.security.auth.login.LoginException;
import org.springframework.stereotype.Service;

import net.dv8tion.jda.api.JDA;

@Service
public interface BotService {

  void startBot() throws LoginException, InterruptedException;

  void shutdownBot();

  void registerListeners(Object... listeners);

  JDA getJda();

}
