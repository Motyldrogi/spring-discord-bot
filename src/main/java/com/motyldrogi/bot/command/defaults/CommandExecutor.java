package com.motyldrogi.bot.command.defaults;

import java.util.List;

public interface CommandExecutor {

  void execute(CommandSender commandSender, List<String> args);

}
