package com.motyldrogi.bot.listener;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

import com.motyldrogi.bot.entity.impl.UserEntityImpl;
import com.motyldrogi.bot.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

@Component
public class GuildMemberJoinListener {

  private final UserRepository userRepository;

  @Autowired
  public GuildMemberJoinListener(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public GuildMemberJoinListener() {
    this(null);
  }

  @SubscribeEvent
  public void execute(GuildMemberJoinEvent event) {
    User user = event.getUser();

    Optional<UserEntityImpl> optionalUserEntity = this.userRepository.findById(user.getIdLong());

    if (optionalUserEntity.isPresent()) {
      return;
    }

    UserEntityImpl userEntity = new UserEntityImpl.Builder()
        .withIdentifier(user.getIdLong())
        .withName(user.getName())
        .setRegistrationDate(Date.from(Instant.now()))
        .build();

    this.userRepository.save(userEntity);
  }

}
