package com.motyldrogi.bot.entity;

public interface Entity<T> {

  T getIdentifier();

  void setIdentifier(T identifier);

}
