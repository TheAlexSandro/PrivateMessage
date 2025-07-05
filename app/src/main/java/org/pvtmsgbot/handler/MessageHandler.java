package org.pvtmsgbot.handler;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageHandler extends TelegramBotsLongPollingApplication {

    public void message(Message message, TelegramClient bot) {
        try {
            String text = message.getText();
            String chatID = message.getChatId().toString();

            if ("/start".equals(text)) {
                SendMessage pesan = SendMessage
                        .builder()
                        .chatId(chatID)
                        .text("I am made with Java!")
                        .parseMode("HTML")
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(InlineKeyboardButton
                                                .builder()
                                                .text("Click Me!")
                                                .callbackData("click_me")
                                                .build()
                                        ))
                                .build()
                        )
                        .build();

                bot.execute(pesan);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
