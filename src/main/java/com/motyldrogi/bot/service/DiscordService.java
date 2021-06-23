package com.motyldrogi.bot.service;

import javax.security.auth.login.LoginException;
import com.motyldrogi.bot.command.defaults.Command;
import org.springframework.stereotype.Service;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Service
public interface DiscordService {

  void startBot() throws LoginException, InterruptedException;

  void shutdownBot();

  void registerListeners(Object... listeners);

  JDA getJda();

  void processMessage(MessageReceivedEvent event, Command command);

  void registerCommand(Command botCommand);

}
