package com.market;

import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.market.DBWriting.sendQuery;
import static com.market.KeyboardFactory.showButtons;

public class MessageFactory {
    private final MessageSender sender;
    private long telegramId;


    public MessageFactory(MessageSender sender) {
        this.sender = sender;
    }

    public void sendAnswerToGuest(Update update) {
        telegramId = 0;
        String item;
        String allItems = "";
        if (update.hasCallbackQuery()) {
            telegramId = update.getCallbackQuery().getMessage().getChatId();
            if (update.getCallbackQuery().getData().equals("toadd")) {
                hasCallBack(update, "Add new item in the item list",
                        showButtons(true, new String[]{"I have no any items:noanyitems"}, false));
                insertLastMessInDB('1');
                return;
            }
            if (update.getCallbackQuery().getData().equals("showitems")) {
                int k = 1;
                if (getItems().length != 0 && !getItems()[0].equals("")) {
                    insertLastMessInDB('0');
                    for (int i = 0; i < getItems().length; i++) {
                        allItems = allItems + '\n' + k +
                                ". " + getItems()[i].split(":")[0];
                        k++;
                    }
                    hasCallBack(update, "There are next items now: " + allItems, showButtons(true, getIdItems(), true));
                    return;
                } else {
                    insertLastMessInDB('0');
                    hasCallBack(update, "There are no items now.", showButtons(true, new String[]{""}, true));
                    return;
                }
            }
            if (update.getCallbackQuery().getData().equals("noanyitems") || update.getCallbackQuery().getData().equals("mainmenu")) {
                insertLastMessInDB('0');
                hasCallBack(update, "Hello!", showButtons(true, new String[]{"Add new items:toadd", "Show all items:showitems"}, false));
                return;
            }
            for (int i = 0; i < getIdItems().length; i++) {
                if (update.getCallbackQuery().getData().equals(getIdItems()[i].split(":")[1])) {
                    sendQuery("delete FROM items WHERE id = " + getIdItems()[i].split(":")[1] + ";");

                    if (getItems().length != 0 && !getItems()[0].equals("")) {
                        for (int j = 0; j < getItems().length; j++) {
                            allItems = allItems + '\n' + (j + 1) +
                                    ". " + getItems()[j].split(":")[0];
                        }
                        hasCallBack(update, "There are next items now: " + allItems, showButtons(true, getIdItems(), true));
                    } else hasCallBack(update, "There are no items now.", showButtons(true, new String[]{""}, true));
                    return;
                }
            }
            return;
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            telegramId = update.getMessage().getChatId();
            if (update.getMessage().getText().equals("/start")) {
                startMenu(update);
                return;
            }
            if (getLastMessageType() == '1') {
                item = update.getMessage().getText();
                sendQuery("INSERT INTO items (telegramid , item) VALUES (" + telegramId + ",  '" + item + " (@" +
                        update.getMessage().getChat().getUserName() + ")" + "');");
                hasMessage(update, "Add another item",
                        showButtons(true, new String[]{"I have no any items:noanyitems"}, false));
                insertLastMessInDB('1');
                return;
            }

        } else {
            return;
        }
    }

    private void hasCallBack(Update update, String textMessage, ReplyKeyboard answerButtons) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        long chatId = callbackQuery.getMessage().getChatId();
        SendMessage sendMessage;
        try {
            sendMessage = new SendMessage(String.valueOf(chatId), textMessage);
            if (!(((InlineKeyboardMarkup) answerButtons).getKeyboard().get(0).size() == 0)) {
                sendMessage.setReplyMarkup(answerButtons);
            }
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void hasMessage(Update update, String textmessage, ReplyKeyboard answerButtons) {
        Message message = update.getMessage();
        telegramId = message.getChatId();
        if (!update.getMessage().getText().equals("/start")) {
            try {
                SendMessage sendMessage = new SendMessage(String.valueOf(telegramId), textmessage);
                if (!(((InlineKeyboardMarkup) answerButtons).getKeyboard().get(0).size() == 0)) {
                    sendMessage.setReplyMarkup(answerButtons);
                }
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void insertLastMessInDB(char messageType) {
        sendQuery("INSERT INTO messages (telegramid , messagetype) VALUES (" + telegramId +
                ",  '" + messageType + "')");
    }

    private char getLastMessageType() {
        String string = sendQuery("select messagetype from messages where telegramId = " + telegramId + ";");
        return string.toCharArray()[string.length() - 1];
    }

    public static String[] getItems(long telegramId) {
        String regex = "FDfyue3:68hiIDOIUH";
        return sendQuery("select item, ':', id, '" + regex + "'  from items where telegramId = " + telegramId + ";")
                .split(regex);


    }

    public static String[] getItems() {
        String regex = "FDfyue3:68hiIDOIUH";
        return sendQuery("select item, ':', id, '" + regex + "'  from items;")
                .split(regex);

    }

    public static String[] getIdItems(long telegramId) {
        String regex = "FDfyue3:68hiIDOIUH";
        String[] strings = sendQuery("select id, ':', id, '" + regex + "'  from items where telegramId = "
                + telegramId + ";").split(regex);
        for (int i = 0; i < strings.length; i++) {
            strings[i] = (i + 1) + ":" + strings[i].split(":")[1];
        }
        return strings;
    }

    public static String[] getIdItems() {
        String regex = "FDfyue3:68hiIDOIUH";
        String[] strings = sendQuery("select id, ':', id, '" + regex + "'  from items;")
                .split(regex);
        for (int i = 0; i < strings.length; i++) {
            strings[i] = (i + 1) + ":" + strings[i].split(":")[1];
        }
        return strings;
    }


    private void startMenu(Update update) {
        SendMessage sendMessage = new SendMessage(String.valueOf(update.getMessage().getChatId()), "Hello!");
        sendMessage.setReplyMarkup(showButtons(true, new String[]{"Add new items:toadd",
                "Show all items:showitems"}, false));
        try {
            sender.execute(sendMessage);
            insertLastMessInDB('0');
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}