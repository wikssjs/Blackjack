package com.ebookfrenzy.blackjack;

public class Card {

    private int value;
    private String suit;
    private int image;


    /***
     * This is the constructor for the Card class.
     * @param value
     * @param suit
     * @param image
     */
    public Card(int value, String suit, int image) {
        this.value = value;
        this.suit = suit;
        this.image = image;
    }

    /***
     * This method is used to get the value of the card.
     * @return value of the card
     */
    public int getValue() {
        return value;
    }

    /***
     * This method is used to get the suit of the card.
     * @return suit of the card
     */
    public String getSuit() {
        return suit;
    }

    /***
     * This method is used to get the image of the card.
     * @return image of the card
     */
    public int getImage() {
        return image;
    }

    /***
     * This method is used to set the value of the card.
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /***
     * This method is used to set the suit of the card.
     * @param suit
     */
    public void setSuit(String suit) {
        this.suit = suit;
    }

    /***
     * This method is used to set the image of the card.
     * @param image
     */
    public void setImage(int image) {
        this.image = image;
    }


    }
