package com.ebookfrenzy.blackjack;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class Card {

    private int value;
    private String suit;
    private int image;

    public Card(int value, String suit, int image) {
        this.value = value;
        this.suit = suit;
        this.image = image;
    }

    public int getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public int getImage() {
        return image;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public enum cardNumber {
        ACE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(10),
        QUEEN(10),
        KING(10);

        private int numVal;

        cardNumber(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }
    }
}
