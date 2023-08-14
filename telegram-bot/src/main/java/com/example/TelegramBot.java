package com.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;
import java.net.URISyntaxException;


/**
 * A Telegram bot that provides various functionalities including chat, currency conversion, and jokes.
 */
public class TelegramBot extends TelegramLongPollingBot{


    private String token = "bot-token-goes-here";
    private String botUsername = "MultiT4skBot";

    final private String [] options = {"/info", "/1", "/2", "/3", "/4"};

    //private boolean infoFlag = false;
    private boolean chatFlag = false;
    private boolean converterFlag = false;
    private boolean jokeFlag = false;
    private boolean submenu = false;
    
    

    /**
     * Called when an update is received from the Telegram bot API.
     *
     * @param update The update received from the Telegram bot API.
     */
    @Override
    public void onUpdateReceived(Update update) 
    {
        ChatGpt chatgpt = new ChatGpt();
        CurrencyConversion converter = new CurrencyConversion();
        Jokes joke = new Jokes();

        // Check if update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String userId = message.getFrom().getId().toString();
            String userMessage = message.getText();
            

            // bot functionalities
            String info = "I have three functionalities:\n"
            + "/info - Get info\n"
            + "/1 - Chatgpt\n"
            + "/2 - Currency Converter(Give input as TRY/USD)\n"
            + "/3 - Generate joke\n"
            + "/4 - Back to menu";


            // info option
            if(userMessage.equals(options[0])) 
            {
                sendMessageToTelegram(userId, info);
            }

            // chatgpt option
            else if(userMessage.equals(options[1]) && !submenu) 
            {
                submenu = true;
                chatFlag = true;
                userMessage = "";
                converterFlag = false;
                jokeFlag = false;

            }

            // currency conversion option
            else if(userMessage.equals(options[2]) && !submenu)
            {
                submenu = true;
                converterFlag = true;
                userMessage = "";
                chatFlag = false;
                jokeFlag = false;
            }

            // joke generation option
            else if(userMessage.equals(options[3]) && !submenu) 
            {
                submenu = true;
                jokeFlag = true;
                userMessage = "";
                chatFlag = false;
                converterFlag = false;
            }

            // back to menu option
            else if(userMessage.equals(options[4]) && submenu) 
            {
                submenu = false;
                jokeFlag = false;
                userMessage = "";
                chatFlag = false;
                converterFlag = false;
            }

            // chatgpt generation part
            if(chatFlag && submenu && (!userMessage.equals(""))) 
            {
                try 
                {
                    String respond = chatgpt.generateResponse(userMessage);
                    sendMessageToTelegram(userId, respond);
                } catch(IOException e) 
                {
                    e.printStackTrace();
                } catch(URISyntaxException e) 
                {
                    e.printStackTrace();
                }

            }

            // conversion part
            else if (converterFlag && submenu && (!userMessage.equals(""))) {
                try {
                    String res = Double.toString(converter.makeConversion(userMessage));
                    sendMessageToTelegram(userId, userMessage + ": " + res);
                } catch (IllegalArgumentException e) {
                    sendMessageToTelegram(userId, "Invalid input format. Please provide the format 'CURRENCY1/CURRENCY2' like --> try/usd");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // joke part
            else if(jokeFlag && submenu) // this task is going to generate a joke whatever the input is
            {
                try
                { 
                
                    String res = joke.generateJoke();
                    sendMessageToTelegram(userId, res);
                
                } catch(URISyntaxException e) 
                {
                    e.printStackTrace();
                } catch(IOException e) 
                {
                    e.printStackTrace();
                }
            }
            
            // no option part
            else if(!chatFlag && !converterFlag && !jokeFlag && !submenu && !userMessage.equals("")) 
            {
                sendMessageToTelegram(userId, "Enter a task number.");
            }
            
        
        }

        
    }


    /**
     * Sends a message to a specified chat.
     *
     * @param chatId  The ID of the chat to which the message will be sent.
     * @param message The message text to send.
     */
    private void sendMessageToTelegram(String chatId, String message) 
    {
        SendMessage messageToSend = new SendMessage();
        messageToSend.setChatId(chatId);
        messageToSend.setText(message);

        try 
        {
            execute(messageToSend);
        } catch(TelegramApiException e) 
        {
            e.printStackTrace();
        }
        
    }

    /**
     * Gets the username of the bot.
     *
     * @return The username of the bot.
     */
    @Override
    public String getBotUsername() 
    {
        return botUsername;
    }

    /**
     * Gets the token of the bot.
     *
     * @return The token of the bot.
     */
    @Override
    public String getBotToken() 
    {
        return token;
    }



    
}
