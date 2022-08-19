package com.project.karenbot.service;

import com.project.karenbot.config.UrlConfig;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class BotService extends TelegramLongPollingBot {
    private final UrlConfig urlConfig;
    private ArrayList<String> chatID;
    private boolean status;
    private final String url = "http://192.168.0.102:8080/";
    private final RestTemplate restTemplate;
    @Override
    public String getBotUsername() {
        return "@wikardBot";
    }

    @Override
    public String getBotToken() {
        return "5279096564:AAFZi7VtekLt6_MrFwA7cd7zuUuj6NOd4lM";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        chatID.add("250412288");
        chatID.add("600017498");
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            String chat = message.getChatId().toString();
                switch (message.getText()) {
                    case "/start":
                        sendMsgForButton(message, new String[]{"Gazebo"}, new String[]{});
                        break;
                    case "Gazebo":
                        for (int i = 0; i < chatID.size(); i++){
                            if (Objects.equals(chatID.get(i), chat)){
                                status = true;
                                break;
                            }else {
                                status = false;
                            }
                        }
                        if (status) {
                            ResponseEntity<String> response
                                    = restTemplate.getForEntity(url + "patric/setting/relay1", String.class);
                            execute(SendMessage
                                    .builder()
                                    .chatId(message.getChatId().toString())
                                    .text(response.getBody())
                                    .build());
                        }
                        break;
                    case "ID":
                        execute(SendMessage
                                .builder()
                                .chatId(message.getChatId().toString())
                                .text(message.getChatId().toString())
                                .build());
                        break;
            }
        }
    }
    @SneakyThrows
    public void sendMsgForButton(Message message, String[] nameFirstButtons, String[] nameSecondButtons) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText("okay :)");
        setButton(sendMessage, nameFirstButtons, nameSecondButtons);
        execute(sendMessage);
    }
    public void setButton(SendMessage sendMessage, String[] nameFirstButtons, String[] nameSecondButtons){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRowFirst = new KeyboardRow();
        KeyboardRow keyboardRowSecond = new KeyboardRow();

        for(int i=0;i< nameFirstButtons.length;i++){
            keyboardRowFirst.add(new KeyboardButton(nameFirstButtons[i]));
        }
        for(int i=0;i< nameSecondButtons.length;i++){
            keyboardRowSecond.add(new KeyboardButton(nameSecondButtons[i]));
        }
        keyboardRowList.add(keyboardRowFirst);
        keyboardRowList.add(keyboardRowSecond);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
