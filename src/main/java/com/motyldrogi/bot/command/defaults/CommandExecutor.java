package com.motyldrogi.bot.command.defaults;

import com.motyldrogi.bot.component.DiscordMessage;

public interface CommandExecutor {

  void execute(DiscordMessage dMessage, CommandSender commandSender);

}
