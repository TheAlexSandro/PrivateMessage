package org.pvtmsgbot;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import org.pvtmsgbot.handler.MessageHandler;
import org.pvtmsgbot.handler.CallbackHandler;
import org.pvtmsgbot.handler.InlineQueryHandler;
import java.lang.Exception;

public class Bot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient bot;
    private final MessageHandler messageUpdates = new MessageHandler();
    private final CallbackHandler callbackQueryUpdates = new CallbackHandler();
    private final InlineQueryHandler inlineQueryUpdates = new InlineQueryHandler();

    public Bot(String token) {
        bot = new OkHttpTelegramClient(token);
    }

    @Override
    public void consume(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message message = update.getMessage();
                messageUpdates.message(message, bot);
            }

            if (update.hasCallbackQuery()) {
                CallbackQuery callback = update.getCallbackQuery();
                callbackQueryUpdates.callback(callback, bot);
            }

            if (update.hasInlineQuery()) {
                InlineQuery inline_query = update.getInlineQuery();
                inlineQueryUpdates.inline_query(inline_query, bot);
            }
        } catch(Exception e)  {
            e.printStackTrace();
        }
    }
}
