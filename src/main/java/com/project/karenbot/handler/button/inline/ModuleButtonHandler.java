package com.project.karenbot.handler.button.inline;

import com.project.karenbot.dto.board.Data;
import com.project.karenbot.dto.board.Device;
import com.project.karenbot.enums.Types;
import com.project.karenbot.handler.AbstractMessageHandler;
import com.project.karenbot.service.BoardConfigService;
import com.project.karenbot.service.ButtonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.project.karenbot.enums.Tags.ACTION_TAG;
import static com.project.karenbot.enums.Tags.MODULE_TAG;

@Component
@AllArgsConstructor
public class ModuleButtonHandler extends AbstractMessageHandler {

    private final BoardConfigService boardConfigService;
    private final ButtonService buttonService;


    @Override
    public boolean canHandle(Update update, boolean user) {
        return update.hasCallbackQuery()
                && Arrays.stream(update.getCallbackQuery().getData().split("_")).anyMatch((el) -> el.equalsIgnoreCase(MODULE_TAG.getTag()))
                && user;
    }

    @Override
    public SendMessage handleMessage(Update update) {
        String[] callbackData = update.getCallbackQuery().getData().split("_");
        String boardName = callbackData[0];
        String settingName = callbackData[1];
        String moduleName = callbackData[2];

        List<Device> devices = boardConfigService.getSettingByName(boardName, settingName);
        Device device = devices.stream().filter((el) -> el.getModuleName().equalsIgnoreCase(moduleName)).findFirst().orElse(null);
        HashMap<String, String> moduleData = new HashMap<>();
        for (Data data : device.getData()) {
            moduleData.put(moduleName + data.getModuleId(),
                    boardName + "_" +
                            settingName + "_" +
                            moduleName + "_" +
                            data.getModuleId() + "_" +
                            ACTION_TAG.getTag());
        }

        return buttonService.setInlineKeyboardButton(update.getCallbackQuery().getMessage(), moduleData, "Device data");
    }

    @Override
    public Types getTypeOfMethod() {
        return Types.SendMessage;
    }
}
