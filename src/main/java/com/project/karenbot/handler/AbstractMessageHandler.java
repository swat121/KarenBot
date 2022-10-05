package com.project.karenbot.handler;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
public abstract class AbstractMessageHandler {

    abstract public boolean canHandle(Update update, boolean user);

    abstract public SendMessage handleMessage(Update update);
}