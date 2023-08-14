package com.example;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;



/**
 * The entry point of the application that initializes and starts the Telegram bot.
 */
public class App 
{
    /**
     * The main method that initializes and starts the Telegram bot.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main( String[] args )
    {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    

    }
}
