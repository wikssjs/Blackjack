package com.ebookfrenzy.blackjack;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class CardPackage {

    public static ArrayList<Card> deck = new ArrayList<>();
    public static ArrayList<Card> playerHand = new ArrayList<>();
    public static ArrayList<Card> dealerHand = new ArrayList<>();
    public static ArrayList<Card> deletedCards = new ArrayList<>();
    public static boolean isJoker = false;


    private static void addJokers() {


        int random = (int) ((Math.random() * (220 - 200)) + 200);
        Log.i("size: ", String.valueOf(random));
        deck.add(random, new Card(0, "joker", R.drawable.red_joker));
        random = (int) ((Math.random() * (320 - 300)) + 300);
        Log.i("size: ", String.valueOf(random));
        deck.add(random, new Card(0, "joker", R.drawable.black_joker));
    }

    public CardPackage() {
    }

    /***
     * This method is used to add all the cards to the deck.
     */
    public static void addAllCards() {
        deck.add(new Card(1, "spades", R.drawable.ace_of_spades));
        deck.add(new Card(2, "spades", R.drawable.two_of_spades));
        deck.add(new Card(3, "spades", R.drawable.three_of_spades));
        deck.add(new Card(4, "spades", R.drawable.four_of_spades));
        deck.add(new Card(5, "spades", R.drawable.five_of_spades));
        deck.add(new Card(6, "spades", R.drawable.six_of_spades));
        deck.add(new Card(7, "spades", R.drawable.seven_of_spades));
        deck.add(new Card(8, "spades", R.drawable.eight_of_spades));
        deck.add(new Card(9, "spades", R.drawable.nine_of_spades));
        deck.add(new Card(10, "spades", R.drawable.ten_of_spades));
        deck.add(new Card(10, "spades", R.drawable.jack_of_spades));
        deck.add(new Card(10, "spades", R.drawable.queen_of_spades));
        deck.add(new Card(10, "spades", R.drawable.king_of_spades));
        deck.add(new Card(1, "hearts", R.drawable.ace_of_hearts));
        deck.add(new Card(2, "hearts", R.drawable.two_of_hearts));
        deck.add(new Card(3, "hearts", R.drawable.three_of_hearts));
        deck.add(new Card(4, "hearts", R.drawable.four_of_hearts));
        deck.add(new Card(5, "hearts", R.drawable.five_of_hearts));
        deck.add(new Card(6, "hearts", R.drawable.six_of_hearts));
        deck.add(new Card(7, "hearts", R.drawable.seven_of_hearts));
        deck.add(new Card(8, "hearts", R.drawable.eight_of_hearts));
        deck.add(new Card(9, "hearts", R.drawable.nine_of_hearts));
        deck.add(new Card(10, "hearts", R.drawable.ten_of_hearts));
        deck.add(new Card(10, "hearts", R.drawable.jack_of_hearts));
        deck.add(new Card(10, "hearts", R.drawable.queen_of_hearts));
        deck.add(new Card(10, "hearts", R.drawable.king_of_hearts));
        deck.add(new Card(1, "diamonds", R.drawable.ace_of_diamonds));
        deck.add(new Card(2, "diamonds", R.drawable.two_of_diamonds));
        deck.add(new Card(3, "diamonds", R.drawable.three_of_diamonds));
        deck.add(new Card(4, "diamonds", R.drawable.four_of_diamonds));
        deck.add(new Card(5, "diamonds", R.drawable.five_of_diamonds));
        deck.add(new Card(6, "diamonds", R.drawable.six_of_diamonds));
        deck.add(new Card(7, "diamonds", R.drawable.seven_of_diamonds));
        deck.add(new Card(8, "diamonds", R.drawable.eight_of_diamonds));
        deck.add(new Card(9, "diamonds", R.drawable.nine_of_diamonds));
        deck.add(new Card(10, "diamonds", R.drawable.ten_of_diamonds));
        deck.add(new Card(10, "diamonds", R.drawable.jack_of_diamonds));
        deck.add(new Card(10, "diamonds", R.drawable.queen_of_diamonds));
        deck.add(new Card(10, "diamonds", R.drawable.king_of_diamonds));
        deck.add(new Card(1, "clubs", R.drawable.ace_of_clubs));
        deck.add(new Card(2, "clubs", R.drawable.two_of_clubs));
        deck.add(new Card(3, "clubs", R.drawable.three_of_clubs));
        deck.add(new Card(4, "clubs", R.drawable.four_of_clubs));
        deck.add(new Card(5, "clubs", R.drawable.five_of_clubs));
        deck.add(new Card(6, "clubs", R.drawable.six_of_clubs));
        deck.add(new Card(7, "clubs", R.drawable.seven_of_clubs));
        deck.add(new Card(8, "clubs", R.drawable.eight_of_clubs));
        deck.add(new Card(9, "clubs", R.drawable.nine_of_clubs));
        deck.add(new Card(10, "clubs", R.drawable.ten_of_clubs));
        deck.add(new Card(10, "clubs", R.drawable.jack_of_clubs));
        deck.add(new Card(10, "clubs", R.drawable.queen_of_clubs));
        deck.add(new Card(10, "clubs", R.drawable.king_of_clubs));
        Log.i("size : ", String.valueOf(deck.size()));
    }


    /***
     *  This method is used to get the first card in the deck.
     */
    public static void getCard() {

        Card card = deck.get((0));
        if (!card.getSuit().equals("joker")) {
            playerHand.add(card);
            deck.remove(0);
        }
    }


    /***
     * This method is used to get four cards from the deck.
     */
    public static void getFourCards() {
        if (isJoker) {
            deck.addAll(deletedCards);
            deletedCards.clear();
            shufle();
            Log.i("joker", "isJoker");
            addJokers();
        }
        isJoker = false;
        if (deck.size() == 0) {
            for (int i = 0; i < 8; i++) {
                addAllCards();
            }
            shufle();
            addJokers();
        }

        for (int i = 0; i < 2; i++) {
            Card card;
            do {
                card = deck.get(0);

                if (card.getValue() > 0) {
                    playerHand.add(card);
                    deck.remove(0);
                } else {
                    isJoker = true;
                    deck.remove(0);
                }
            } while (card.getSuit().equals("joker"));
            do {
                card = deck.get((0));

                if (card.getValue() > 0) {
                    dealerHand.add(card);
                    deck.remove(0);
                } else {
                    isJoker = true;
                    deck.remove(0);
                }
            } while (card.getSuit().equals("joker"));
        }
    }

    /***
     * This method is used to Shuflle the deck.
     */
    public static void shufle() {
        Collections.shuffle(deck);
    }

    /***
     * This method is used to get the sum of the cards in the player's hand.
     * @return sum of the player hand
     */
    public static int sumPlayerHand() {
        int sum = 0;
        for (int i = 0; i < playerHand.size(); i++) {
            sum += playerHand.get(i).getValue();
        }
        return sum;
    }

    /***
     * This method is used to get the sum of the cards in the dealer's hand.
     * @return sum of the dealer hand
     */
    public static int sumDealerHand() {
        int sum = 0;
        for (int i = 0; i < dealerHand.size(); i++) {
            sum += dealerHand.get(i).getValue();
        }
        return sum;
    }


    /***
     * This method is used to add a card to the dealer's hand.
     */
    public static void addCardDealer() {
        if (deck.get(0).getValue() <= 0) {
            shufle();
        }
        dealerHand.add(deck.get((0)));
        deck.remove(0);
    }

    /***
     * This method is to check if the player has busted.
     * @return true if the player has busted, false otherwise
     */
    public static boolean isBust() {
        return sumPlayerHand() > 21;
    }
}
