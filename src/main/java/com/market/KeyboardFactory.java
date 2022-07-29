package com.market;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class KeyboardFactory {

    public static ReplyKeyboard showButtons(Boolean horizontal, String[] namesButtonsCallbackData, boolean mainMenuButton) {
        int maxButtonsRow = 8;
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        if (horizontal && namesButtonsCallbackData.length <= maxButtonsRow) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton[] inlineKeyboardButton = new InlineKeyboardButton[namesButtonsCallbackData.length];
            for (int i = 0; i < namesButtonsCallbackData.length; i++) {
                String nameButton;
                String callbackData;
                if (!namesButtonsCallbackData[i].equals("")) {
                    nameButton = namesButtonsCallbackData[i].split(":")[0];
                    callbackData = namesButtonsCallbackData[i].split(":")[1];
                    inlineKeyboardButton[i] = new InlineKeyboardButton(nameButton);
                    inlineKeyboardButton[i].setCallbackData(callbackData);
                    rowInline.add(inlineKeyboardButton[i]);
                }
            }
            if (!rowInline.isEmpty()) rowsInline.add(rowInline);

            if (mainMenuButton) {
                rowInline = new ArrayList<>();
                InlineKeyboardButton backButton = new InlineKeyboardButton("<- to main menu");
                backButton.setCallbackData("mainmenu");
                rowInline.add(backButton);
                rowsInline.add(rowInline);
            }

        } else if (horizontal && namesButtonsCallbackData.length > maxButtonsRow) {
            int numRows = namesButtonsCallbackData.length / maxButtonsRow;
            if (namesButtonsCallbackData.length % maxButtonsRow != 0) numRows++;
            int k = 0;

            List<InlineKeyboardButton> rowInline;
            InlineKeyboardButton[] inlineKeyboardButton;

            for (int j = 0; j < numRows; j++) {
                int max;
                if (namesButtonsCallbackData.length - k >= maxButtonsRow) {
                    inlineKeyboardButton = new InlineKeyboardButton[maxButtonsRow];
                    max = maxButtonsRow;
                } else {
                    inlineKeyboardButton = new InlineKeyboardButton[namesButtonsCallbackData.length % maxButtonsRow];
                    max = namesButtonsCallbackData.length % maxButtonsRow;
                }
                rowInline = new ArrayList<>();
                for (int i = 0; i < max; i++) {
                    String nameButton;
                    String callbackData;
                    if (!namesButtonsCallbackData[k].equals("")) {
                        nameButton = namesButtonsCallbackData[k].split(":")[0];
                        callbackData = namesButtonsCallbackData[k].split(":")[1];
                    } else {
                        nameButton = "";
                        callbackData = "no buttons";
                    }
                    inlineKeyboardButton[i] = new InlineKeyboardButton(nameButton);
                    inlineKeyboardButton[i].setCallbackData(callbackData);
                    rowInline.add(inlineKeyboardButton[i]);
                    k++;
                }
                rowsInline.add(rowInline);
            }
            if (mainMenuButton) {
                rowInline = new ArrayList<>();
                InlineKeyboardButton backButton = new InlineKeyboardButton("<- to main menu");
                backButton.setCallbackData("mainmenu");
                rowInline.add(backButton);
                rowsInline.add(rowInline);
            }


        } else {
            ArrayList<InlineKeyboardButton>[] rowInlines = new ArrayList[namesButtonsCallbackData.length];
            InlineKeyboardButton[] inlineKeyboardButtons = new InlineKeyboardButton[namesButtonsCallbackData.length];
            for (int i = 0; i < namesButtonsCallbackData.length; i++) {
                String nameButton = namesButtonsCallbackData[i].split(":")[0];
                String callbackData = namesButtonsCallbackData[i].split(":")[1];
                rowInlines[i] = new ArrayList<>();
                inlineKeyboardButtons[i] = new InlineKeyboardButton(new String(nameButton.getBytes(), StandardCharsets.UTF_8));
                inlineKeyboardButtons[i].setCallbackData(callbackData);
                rowInlines[i].add(inlineKeyboardButtons[i]);
                rowsInline.add(rowInlines[i]);
            }
            if (mainMenuButton) {
                ArrayList<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton backButton = new InlineKeyboardButton("<- to main menu");
                backButton.setCallbackData("mainmenu");
                rowInline.add(backButton);
                rowsInline.add(rowInline);
            }
        }
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }
}
