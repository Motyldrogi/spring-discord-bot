package com.motyldrogi.bot.component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.motyldrogi.bot.command.defaults.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DiscordMessage {

    MessageReceivedEvent event;
    Command commandData;

    private String prefix;
    private String rawMessage;
    private String data;
    private String command;
    private String channel;
    private String sentBy;
    
    public DiscordMessage(MessageReceivedEvent event, Command commandData, String prefix) {
        this.event = event;
        this.commandData = commandData;
        this.prefix = prefix;
        this.parseMessage();
    }

    private void parseMessage() {
        // Get raw
        this.rawMessage = event.getMessage().getContentRaw();

        // Parse data
        List<String> args = Arrays.stream(event.getMessage().getContentRaw().split(" ")).skip(1)
            .collect(Collectors.toList());
        
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(arg).append(" ");
        }
        this.data = stringBuilder.toString();
        
        // Command data
        this.command = this.commandData.getName();
        this.channel = event.getChannel().getName();
        this.sentBy = event.getAuthor().getName();
    }

    public String getPrefix() {
        return prefix;
    }

    public Command getCommandData() {
        return commandData;
    }
    
    public String getRawMessage() {
        return rawMessage;
    }

    public String getData() {
        return data;
    }

    public String getCommand() {
        return command;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public String getChannel() {
        return channel;
    }

    public String getSentBy() {
        return sentBy;
    }
}
