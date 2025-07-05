package org.pvtmsgbot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.objects.Update;

import io.github.cdimascio.dotenv.Dotenv;

import java.lang.Exception;
import java.time.LocalDateTime;

public class Main {

    private static final Dotenv env = Dotenv.load();
    private static final String token = env.get("BOT_TOKEN");
    private static final LocalDateTime time = LocalDateTime.now();

    public static void main(String[] args) {
        try {
            TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication();
            bot.registerBot(token, new Bot(token));
            System.out.println(String.format("Bot is running at %s", time));
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
