package com.motyldrogi.bot;

import com.motyldrogi.bot.command.CommitCommand;
import com.motyldrogi.bot.command.EightBallCommand;
import com.motyldrogi.bot.command.FactCommand;
import com.motyldrogi.bot.command.GitHubCommand;
import com.motyldrogi.bot.command.RegisterAccountCommand;
import com.motyldrogi.bot.command.SayCommand;
import com.motyldrogi.bot.command.defaults.impl.CommandRegistry;
import com.motyldrogi.bot.listener.GuildMemberJoinListener;
import com.motyldrogi.bot.repository.UserRepository;
import com.motyldrogi.bot.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class ApplicationBootstrap implements CommandLineRunner {

  private final BotService botService;
  private final CommandRegistry commandRegistry;

  public ApplicationBootstrap(BotService botService, CommandRegistry commandRegistry) {
    this.botService = botService;
    this.commandRegistry = commandRegistry;
  }

  public static void main(String[] args) {
    SpringApplication.run(ApplicationBootstrap.class, args);
  }

  @Autowired
  private UserRepository userRepository;

  @Override
  public void run(String... args) throws Exception {
    this.botService.startBot();

    this.botService.registerListeners(
        new GuildMemberJoinListener(userRepository)
    );

    this.commandRegistry.registerByExecutors(
        new RegisterAccountCommand(userRepository),
        new GitHubCommand(),
        new CommitCommand(),
        new SayCommand(),
        new FactCommand(),
        new EightBallCommand()
    );
  }

}
