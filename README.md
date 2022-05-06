# Spring Boot Discord Bot
Discord all-purpose bot, made using Spring Boot, JPA, Hibernate, REST, HikariCP, JDA.

**TravisCI**<br>
[![travis-icon]][travis]

Requirements for connection
---
Find the file `app.properties` in the resources folder and change following variables:

- **prefix**: The prefix for all command
- **discordToken**: Token from discord bot, https://discord.com/developers/applications

Adding a Command
---

Every bot command should be a part of the `com.motyldrogi.bot.command` package and implement the `CommandExecutor` class, implementing the `execute()` method at bare-minimum. The `execute()` method expectes two arguments:

- **dMessage (DiscordMessage)**: The `DiscordMessage` object which contains the full information about the message
- **commandSender (CommandSender)**: The class for sending messages and also for localization

The `execute()` method needs a `CommandInfo()` annotation to work, the `CommandInfo()` annotation can have the following arguments:

- **value**: The value that triggers the command, i.e. after the prefix
- **minArguments**: Minimum arguments count for the command, defaults to 0
- **maxArguments**: Maximum arguments count for the command, defaults to 0
- **usage**: Text that gets displayed if the command was not used correctly

For example, the following command echos back the message received in an embeded message:

```java
package com.motyldrogi.bot.command;

import com.motyldrogi.bot.command.defaults.CommandExecutor;
import com.motyldrogi.bot.command.defaults.CommandInfo;
import com.motyldrogi.bot.command.defaults.CommandSender;
import com.motyldrogi.bot.component.DiscordMessage;
import org.springframework.stereotype.Component;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

@Component
public class EchoCommand implements CommandExecutor {

  @CommandInfo(value = "echo", minArguments = 1, maxArguments = 1, usage = "<message>")
  @Override
  public void execute(DiscordMessage dMessage, CommandSender commandSender) {

    MessageEmbed messageEmbed = new EmbedBuilder()
          .setColor(Color.decode("#ffffff"))
          .setTitle(dMessage.getSentBy() + " said..")
          .setDescription(dMessage.getData())
          .build();

    commandSender.sendEmbedMessage(messageEmbed);
  }
}
```

If you created a command, you have to register it:
```java
this.commandRegistry.registerByExecutors(
    [...],
    new EchoCommand()
);
```

## Features
- [x] Storing all user's data to mysql database
- [x] Public rest api without oauth
- [x] Github command, that shows infos about your github profile
- [x] Say command, that talks in json

## Endpoints

| Method                                             | Optional query parameters      | Success status codes   | Error status codes |
| -------------------------------------------------- | --------------------------     | ---------------------  | ------------------ |                   
| **GET  /api/users**                                | page, size                     | 200                    |                    |
| **GET  /api/users/by-id/{id}**                     |                                | 200                    | 404                |
| **GET  /api/users/by-name/{name}**                 |                                | 200                    | 404                |

[travis-icon]: https://www.travis-ci.com/Motyldrogi/spring-discord-bot.svg?token=BAY6DRwNfoKsyPs22bzN&branch=main
[travis]: https://www.travis-ci.com/github/Motyldrogi/spring-discord-bot/
