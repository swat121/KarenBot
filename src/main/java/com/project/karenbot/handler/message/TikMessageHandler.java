package com.project.karenbot.handler.message;

import com.project.karenbot.handler.AbstractMessageHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@AllArgsConstructor
public class TikMessageHandler extends AbstractMessageHandler {

    @Override
    public boolean canHandle(Update update, boolean user) {
        return update.hasMessage()
                && "/tik".equals(update.getMessage().getText());
    }

    @Override
    public SendMessage handleMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText("tok");
        return sendMessage;
    }
}