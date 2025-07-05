package org.pvtmsgbot.handler;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.pvtmsgbot.resources.CacheStore;
import org.pvtmsgbot.resources.Helper;

import java.lang.Exception;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Optional;

public class CallbackHandler extends TelegramBotsLongPollingApplication {

    private final Helper helper = new Helper();

    public void callback(CallbackQuery cb, TelegramClient bot) {
        try {
            String data = cb.getData();
            String cbID = cb.getId();
            String chatID = Optional.ofNullable(cb.getMessage())
                    .map(msg -> msg.getChatId().toString())
                    .orElse(cb.getFrom().getId().toString());

            Pattern ClickMePattern = Pattern.compile("click_me", Pattern.CASE_INSENSITIVE);
            Matcher ClickMeMatch = ClickMePattern.matcher(data);
            if (ClickMeMatch.find()) {
                String messageID = cb.getMessage().getMessageId().toString();
                AnswerCallbackQuery answer = AnswerCallbackQuery
                        .builder()
                        .callbackQueryId(cbID)
                        .text("Hi!")
                        .showAlert(true)
                        .build();
                EditMessageText pesan = EditMessageText.builder()
                        .chatId(chatID)
                        .messageId(Integer.parseInt(messageID))
                        .text("Halo")
                        .build();

                bot.execute(pesan);
                bot.execute(answer);
                return;
            }

            Pattern MessagePattern = Pattern.compile("message\\|([^|]+)\\|([^|]+)\\|(.+)", Pattern.CASE_INSENSITIVE);
            Matcher MessageMatch = MessagePattern.matcher(data);
            if (MessageMatch.matches()) {
                String IDs = MessageMatch.group(1);
                String sender = MessageMatch.group(2);
                String target = MessageMatch.group(3);
                String username = cb.getFrom().getUserName();

                String getMessage = CacheStore.getString(helper.f("message_%s", IDs));
                if (getMessage == null) {
                    AnswerCallbackQuery answer = AnswerCallbackQuery
                            .builder()
                            .callbackQueryId(cbID)
                            .text("⚠️ Oh no! The message is gone.")
                            .showAlert(true)
                            .build();
                    bot.execute(answer);
                    return;
                }

                Boolean verify;
                if (sender.equals(chatID)) {
                    verify = true;
                } else {
                    verify = target.toLowerCase().replace("@", "").equals(username.toLowerCase());
                }

                if (!verify) {
                    AnswerCallbackQuery answer = AnswerCallbackQuery
                            .builder()
                            .callbackQueryId(cbID)
                            .text("Unauthorized!")
                            .showAlert(true)
                            .build();
                    bot.execute(answer);
                    return;
                }

                AnswerCallbackQuery answer = AnswerCallbackQuery
                        .builder()
                        .callbackQueryId(cbID)
                        .text(getMessage)
                        .showAlert(true)
                        .build();
                bot.execute(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
