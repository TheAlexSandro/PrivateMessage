package org.pvtmsgbot.handler;

import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.pvtmsgbot.resources.Helper;
import org.pvtmsgbot.resources.CacheStore;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Collections;

public class InlineQueryHandler extends TelegramBotsLongPollingApplication {

    private static final Dotenv env = Dotenv.load();
    private static final String bot_username = env.get("BOT_USERNAME");
    private final Helper helper = new Helper();

    public void inline_query(InlineQuery iq, TelegramClient bot) {
        try {
            String inlineID = iq.getId();
            String chatID = iq.getFrom().getId().toString();
            String query = iq.getQuery();

            Pattern QueryPattern = Pattern.compile("(.+)\\s+(.+)", Pattern.CASE_INSENSITIVE);
            Matcher QueryMatcher = QueryPattern.matcher(query);

            if (query.isEmpty()) {
                InlineQueryResultArticle result = InlineQueryResultArticle.builder()
                        .id("1")
                        .title("Target id and message is required.")
                        .description(helper.f("Usage: @%s @telegram hi", bot_username))
                        .inputMessageContent((InputTextMessageContent.builder()
                                .messageText("Parameter cannot be empty.")
                                .parseMode("HTML")
                                .build()))
                        .build();
                AnswerInlineQuery answer = AnswerInlineQuery.builder()
                        .inlineQueryId(inlineID)
                        .results(Collections.singletonList(result))
                        .cacheTime(0)
                        .build();

                bot.execute(answer);
                return;
            }

            if (QueryMatcher.matches()) {
                String target = QueryMatcher.group(1);
                String message = QueryMatcher.group(2);

                if (target.isEmpty() || message.isEmpty()) {
                    InlineQueryResultArticle result = InlineQueryResultArticle.builder()
                            .id("1")
                            .title("Target username and message is required.")
                            .description(helper.f("Usage: @%s @telegram hi", bot_username))
                            .inputMessageContent((InputTextMessageContent.builder()
                                    .messageText("Parameter cannot be empty.")
                                    .parseMode("HTML")
                                    .build()))
                            .build();
                    AnswerInlineQuery answer = AnswerInlineQuery.builder()
                            .inlineQueryId(inlineID)
                            .results(Collections.singletonList(result))
                            .cacheTime(0)
                            .build();

                    bot.execute(answer);
                    return;
                }

                if (!target.startsWith("@")) {
                    InlineQueryResultArticle result = InlineQueryResultArticle.builder()
                            .id("1")
                            .title("Target must start with @.")
                            .description(helper.f("Usage: @%s @telegram hi", bot_username))
                            .inputMessageContent((InputTextMessageContent.builder()
                                    .messageText("Invalid value.")
                                    .parseMode("HTML")
                                    .build()))
                            .build();
                    AnswerInlineQuery answer = AnswerInlineQuery.builder()
                            .inlineQueryId(inlineID)
                            .results(Collections.singletonList(result))
                            .cacheTime(0)
                            .build();

                    bot.execute(answer);
                    return;
                }

                String IDs = helper.createID(10);

                InlineQueryResultArticle result = InlineQueryResultArticle.builder()
                        .id("1")
                        .title("Click here!")
                        .description("Click here to send message.")
                        .inputMessageContent((InputTextMessageContent.builder()
                                .messageText(helper.f("🔒 There is a secret message for %s tap the button below to view the message.", target))
                                .parseMode("HTML")
                                .build()))
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(InlineKeyboardButton
                                                .builder()
                                                .text("👉 Tap Here 👈")
                                                .callbackData(helper.f("message|%s|%s|%s", IDs, chatID, target))
                                                .build()
                                        ))
                                .build()
                        )
                        .build();
                AnswerInlineQuery answer = AnswerInlineQuery.builder()
                        .inlineQueryId(inlineID)
                        .results(Collections.singletonList(result))
                        .cacheTime(0)
                        .build();
                CacheStore.put(helper.f("message_%s", IDs), message);

                bot.execute(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
