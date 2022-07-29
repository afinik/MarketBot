package com.market;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.market.Constants.*;
import static com.market.DBWriting.createDBenviroment;

public class TelegramBot extends AbilityBot {

    MessageFactory messageFactory;

    public TelegramBot() {
        this(BOT_TOKEN, BOT_USERNAME);
    }

    private TelegramBot(String botToken, String botUsername) {
        super(botToken, botUsername);
        createDBenviroment();
        messageFactory = new MessageFactory(sender);
    }

    @Override
    public long creatorId() {
        return CREATOR_ID;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public void onUpdateReceived(Update update) {
        super.onUpdateReceived(update);
        messageFactory.sendAnswerToGuest(update);

    }

    @Override
    public void onClosing() {
        super.onClosing();
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}

