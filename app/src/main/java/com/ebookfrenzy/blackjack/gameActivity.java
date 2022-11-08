package com.ebookfrenzy.blackjack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;


public class gameActivity extends AppCompatActivity {

    private LinearLayout playerLayout;
    private LinearLayout dealerLayout;
    private LinearLayout playingLayout;
    private LinearLayout chipsLayout;
    private Button replayButton;
    private TextView betAmount;
    private TextView moneyAmount;
    private Button doubleDown;
    private ImageView playerCard1;
    private ImageView dealerCard1;
    private ImageView playerCard2;
    private ImageView dealerCard2;
    private TextView playerCardValue;
    private TextView dealerCardValue;
    private TextView level;
    private ProgressBar leverProgress;
    private int progressCounter;
    private int maxProgress;
    private int money;
    private int betToInt;
    private int levelToint;
    private int dealerIndex = 0;
    private int playerIndex = 0;
    private final Handler handler = new Handler();
    private static final String MONEYFILENAME = "money.txt";
    private static final String LEVELFILENAME = "level.txt";
    private static final String PROGRESSCOUNTER = "progress_counter.txt";
    private static final String DIFFICULTY = "difficulty.txt";

    /***
     * This method is called when the activity is created.
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        money = 50000;
        level = findViewById(R.id.levelId);
        maxProgress = 1000;

        playingLayout = findViewById(R.id.playingButton_layout);
        chipsLayout = findViewById(R.id.chipLayout);

        ImageButton chip1 = findViewById(R.id.chi_oneId);
        ImageButton chip5 = findViewById(R.id.chip_fiveId);
        ImageButton chip25 = findViewById(R.id.chip_twentyFiveId);
        ImageButton chip50 = findViewById(R.id.chip_fifthyId);
        ImageButton chip100 = findViewById(R.id.chip_hundredId);

        playerCard1 = findViewById(R.id.playerFirstCardId);
        dealerCard1 = findViewById(R.id.dealerFirstCardId);
        playerCard2 = findViewById(R.id.playerSecondCardId);
        dealerCard2 = findViewById(R.id.dealerSecondCardId);
        playerCardValue = findViewById(R.id.playerSumId);
        dealerCardValue = findViewById(R.id.dealerSumId);
        playerLayout = findViewById(R.id.playerCardLayout);
        dealerLayout = findViewById(R.id.dealerCardLayout);

        betAmount = findViewById(R.id.betAmountId);
        moneyAmount = findViewById(R.id.moneyId);
        Button hit = findViewById(R.id.hitButtonId);
        Button stand = findViewById(R.id.standButtonId);
        doubleDown = findViewById(R.id.doubleButtonId);
        replayButton = findViewById(R.id.replayButtonid);
        hit.setOnClickListener(v -> hitCard());


        //bet Button
        Button betButton = findViewById(R.id.betButtonId);
        betButton.setOnClickListener(v -> {
            if (betToInt > 0 && CardPackage.playerHand.size() == 0 && CardPackage.dealerHand.size() == 0) {

                playingLayout.setVisibility(View.VISIBLE);
                chipsLayout.setVisibility(View.GONE);
                betButton.setVisibility(View.GONE);
                try {
                    saveToFile(MONEYFILENAME, money);
                    saveToFile(PROGRESSCOUNTER, progressCounter);
                    saveToFile(DIFFICULTY, maxProgress);
                    saveToFile(LEVELFILENAME, levelToint);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startGame();
            } else {
                Toast.makeText(gameActivity.this, "You don't have enough money or bet ", Toast.LENGTH_SHORT).show();
            }
        });

        //Rebet Button
        replayButton.setOnClickListener(v -> {
            if (money > 0 && betToInt > 0) {
                try {
                    saveToFile(MONEYFILENAME, money);
                    saveToFile(PROGRESSCOUNTER, progressCounter);
                    saveToFile(DIFFICULTY, maxProgress);
                    saveToFile(LEVELFILENAME, levelToint);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                restart();
            } else {
                Toast.makeText(gameActivity.this, "You have no money or bet", Toast.LENGTH_SHORT).show();
            }
        });

        betToInt = Integer.parseInt(betAmount.getText().toString());

        //get all user data
        try {
            money = Integer.parseInt(getFromFile(MONEYFILENAME, moneyAmount));
            levelToint = Integer.parseInt(getFromFile(LEVELFILENAME, level));
            progressCounter = Integer.parseInt(getFromFile(PROGRESSCOUNTER, null));
            maxProgress = Integer.parseInt(getFromFile(DIFFICULTY, null));
        } catch (Exception e) {
            e.printStackTrace();
        }

        level.setText(String.valueOf(levelToint));
        moneyAmount.setText(getString(R.string.dollar_symbol) + money);

        //Double down
        doubleDown.setOnClickListener(v -> doubleDown());

        //stand
        stand.setOnClickListener(v -> stand());

        //Chip on clicks
        chip1.setOnClickListener(this::onClick);
        chip5.setOnClickListener(this::onClick);
        chip25.setOnClickListener(this::onClick);
        chip50.setOnClickListener(this::onClick);
        chip100.setOnClickListener(this::onClick);

    }


    /***
     * This method is called when the user clicks on a chip.
     * @param v a view
     */
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chi_oneId:
                if (money >= 1) {
                    money -= 1;
                    betToInt += 1;
                }
                break;
            case R.id.chip_fiveId:
                if (money >= 5) {
                    money -= 5;
                    betToInt += 5;
                }
                break;
            case R.id.chip_twentyFiveId:
                if (money >= 25) {
                    money -= 25;
                    betToInt += 25;
                }
                break;
            case R.id.chip_fifthyId:
                if (money >= 50) {
                    money -= 50;
                    betToInt += 50;
                }
                break;
            case R.id.chip_hundredId:
                if (money >= 100) {
                    money -= 100;
                    betToInt += 100;
                }
                break;
            default:
                betAmount.setText(R.string.insufficient_moneyStr);
                break;
        }
        betAmount.setText(String.valueOf(betToInt));
        moneyAmount.setText(String.valueOf(money));
    }

    /***
     * This method is called when the user clicks on the bet button.
     */
    public void startGame() {
        distributeCards();
    }

    /***
     * This method is used to distribute the cards to player and dealer
     */
    public void distributeCards() {
        CardPackage.getFourCards();
        doubleDown.setVisibility(View.VISIBLE);
        dealerCard1.setImageResource(CardPackage.dealerHand.get(dealerIndex).getImage());
        dealerCard1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        dealerIndex++;
        dealerCard2.setImageResource(R.drawable.back);
        dealerCard2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        playerCard1.setImageResource(CardPackage.playerHand.get(playerIndex).getImage());
        playerCard1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        playerIndex++;
        playerCard2.setImageResource(CardPackage.playerHand.get(playerIndex).getImage());
        playerCard2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        playerIndex++;
        dealerIndex++;


        //player has ace
        if (playerHasAce() && CardPackage.sumPlayerHand() + 10 <= 21) {
            playerCardValue.setText(MessageFormat.format("{0}/{1}", CardPackage.sumPlayerHand(), CardPackage.sumPlayerHand() + 10));
        } else {
            playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
        }

        dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand() - CardPackage.dealerHand.get(dealerIndex - 1).getValue()));

        if (playerHasBlackJack()) {
            Toast.makeText(this, "BLACKJACK", Toast.LENGTH_SHORT).show();
            playerCardValue.setText(R.string.blackjackTextStr);
            dealerCard2.setImageResource(CardPackage.dealerHand.get(dealerIndex - 1).getImage());
            dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand()));
            money += betToInt * 2.5;
            moneyAmount.setText(String.valueOf(money));
            betAmount.setText("0");
            Progress(betToInt * 2.5);
            betToInt = 0;
            clearCards();
            gameEnd();
        }

        if (dealerHasBlackJack()) {
            Toast.makeText(this, "BLACKJACK", Toast.LENGTH_SHORT).show();
            dealerCardValue.setText(R.string.blackjackTextStr);
            dealerCard2.setImageResource(CardPackage.dealerHand.get(dealerIndex - 1).getImage());
            moneyAmount.setText(String.valueOf(money));
            betAmount.setText("0");
            Progress(betToInt);
            betToInt = 0;
            clearCards();
            gameEnd();
        }

        //reset ace value from 11 to 1
        resetAceValue();
    }

    /***
     * This method is used to hit the card to the player
     */
    public void hitCard() {
        doubleDown.setVisibility(View.GONE);
        ImageView newCard = new ImageView(this);
        CardPackage.getCard();
        newCard.setImageResource(CardPackage.playerHand.get(CardPackage.playerHand.size() - 1).getImage());
        newCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        newCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        playerLayout.addView(newCard);


        if (playerHasAce() && CardPackage.sumPlayerHand() + 10 <= 21) {
            playerCardValue.setText(MessageFormat.format("{0}/{1}", CardPackage.sumPlayerHand(), CardPackage.sumPlayerHand() + 10));
        } else {
            playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
        }

        //player bust
        if (CardPackage.isBust()) {
            playerCardValue.setText(R.string.bustTextStr);
            dealerCard2.setImageResource(CardPackage.dealerHand.get(1).getImage());
            dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand()));
            gameEnd();
            clearCards();
            Progress(betToInt);
            betToInt = 0;
            betAmount.setText(String.valueOf(betToInt));

        }

        if (CardPackage.sumPlayerHand() == 21) {
            stand();
        }
    }

    /***
     * bust event
     */
    public void gameEnd() {
        playingLayout.setVisibility(View.GONE);
        chipsLayout.setVisibility(View.VISIBLE);
        replayButton.setVisibility(View.VISIBLE);
    }


    /***
     * This method is used to clear the cards from the player and dealer
     */
    public void clearCards() {
        CardPackage.deletedCards.addAll(CardPackage.playerHand);
        CardPackage.playerHand.clear();
        CardPackage.deletedCards.addAll(CardPackage.dealerHand);
        CardPackage.dealerHand.clear();
    }

    /***
     * This method is used to restart the game
     */
    public void restart() {
        playingLayout.setVisibility(View.VISIBLE);
        replayButton.setVisibility(View.GONE);
        chipsLayout.setVisibility(View.GONE);
        dealerCardValue.setText("0");
        playerCardValue.setText("0");

        //remove player cards
        for (int i = playerLayout.getChildCount() - 1; i > 1; i--) {
            playerLayout.removeViewAt(i);
        }

        //remove dealer cards
        for (int i = dealerLayout.getChildCount() - 1; i > 1; i--) {
            dealerLayout.removeViewAt(i);
        }

        playerIndex = 0;
        dealerIndex = 0;
        distributeCards();

    }


    /***
     * This method is stand the player hand
     */
    public void stand() {
        while (CardPackage.sumDealerHand() < 17) {
            boolean pass = false;


            dealerCard2.setImageResource(CardPackage.dealerHand.get(CardPackage.dealerHand.size() - 1).getImage());

            //dealer has ace
            for (Card card : CardPackage.dealerHand) {
                if (card.getValue() == 1) {
                    dealerCardValue.setText(MessageFormat.format("{0}/{1}", CardPackage.sumDealerHand(), CardPackage.sumDealerHand() + 10));
                    if (CardPackage.sumDealerHand() + 10 <= 21 && CardPackage.sumDealerHand() + 10 >= 17) {
                        card.setValue(11);
                        pass = true;
                        break;
                    }
                }
            }
            if (pass) {
                break;
            }
            CardPackage.addCardDealer();
            ImageView newCard = new ImageView(this);
            newCard.setImageResource(CardPackage.dealerHand.get(CardPackage.dealerHand.size() - 1).getImage());
            newCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
            newCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            handler.postDelayed(() -> dealerLayout.addView(newCard), 100);
            dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand()));

        }
        dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand()));
        //player has ace
        for (Card card : CardPackage.playerHand) {
            if (card.getValue() == 1) {
                if (CardPackage.sumPlayerHand() + 10 <= 21) {
                    card.setValue(11);
                }
                playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
            }
        }


        //dealer bust
        if (CardPackage.sumDealerHand() > 21) {
            dealerCardValue.setText(MessageFormat.format("BUST :{0}", CardPackage.sumDealerHand()));
            money += betToInt * 2;
            Progress(betToInt * 2);
        }

        //player bust
        if (CardPackage.sumPlayerHand() > 21) {
            playerCardValue.setText(MessageFormat.format("BUST :{0}", CardPackage.sumPlayerHand()));
            money -= betToInt;
            Progress(betToInt);
        }

        //dealer wins
        if (CardPackage.sumDealerHand() > CardPackage.sumPlayerHand() && CardPackage.sumDealerHand() <= 21) {
            Toast.makeText(this, "DEALER WINS", Toast.LENGTH_SHORT).show();
            dealerCardValue.setText(MessageFormat.format("WINS: {0}", CardPackage.sumDealerHand()));
            Progress(betToInt);
        }


        //Draw
        if (CardPackage.sumDealerHand() == CardPackage.sumPlayerHand()) {
            Toast.makeText(this, "DRAW", Toast.LENGTH_SHORT).show();
            dealerCardValue.setText(MessageFormat.format("DRAW :{0}", CardPackage.sumDealerHand()));
            playerCardValue.setText(MessageFormat.format("DRAW :{0}", CardPackage.sumPlayerHand()));
            money += betToInt;
            Progress(betToInt);
        }

        //player wins
        if (CardPackage.sumDealerHand() < CardPackage.sumPlayerHand() && CardPackage.sumPlayerHand() <= 21) {
            Toast.makeText(this, "PLAYER WINS", Toast.LENGTH_SHORT).show();
            playerCardValue.setText(MessageFormat.format("WINS : {0}", CardPackage.sumPlayerHand()));
            money += betToInt * 2;
            Progress(betToInt * 2);
        }

        dealerCard2.setImageResource(CardPackage.dealerHand.get(1).getImage());
        clearCards();
        gameEnd();
        moneyAmount.setText(String.valueOf(money));
        betAmount.setText("0");
        betToInt = 0;
    }

    /***
     * This method is used to double the bet amount
     */
    public void doubleDown() {
        if (money >= betToInt) {
            money -= betToInt;
            betToInt *= 2;
            moneyAmount.setText(String.valueOf(money));
            betAmount.setText(String.valueOf(betToInt));


            handler.postDelayed(this::hitCard, 100);

            handler.postDelayed(this::stand, 3000);
            playingLayout.setVisibility(View.GONE);
        } else {
            Toast.makeText(this, "insufficient money", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This method is to save the player's money
     */
    public void saveToFile(String fileName, int value) throws IOException {
        FileOutputStream outputStream;
        outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
        outputStream.write(String.valueOf(value).getBytes());
        outputStream.close();
    }

    /**
     * This method is to load the player's money
     */
    public String getFromFile(String fileName, TextView tv) {
        FileInputStream inputStream;
        try {
            inputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
            //value = Integer.parseInt(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    boolean doubleBackToExitPressedOnce = false;


    /***
     * This method is used to exit the game
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        clearCards();
        if (playingLayout.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "If you press again you will lose your bet", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    public boolean playerHasBlackJack() {
        return CardPackage.playerHand.get(0).getValue() == 1 && CardPackage.playerHand.get(1).getValue() == 10 ||
                CardPackage.playerHand.get(0).getValue() == 10 && CardPackage.playerHand.get(1).getValue() == 1;
    }

    public boolean dealerHasBlackJack() {
        if (CardPackage.dealerHand.size() > 0) {
            return CardPackage.dealerHand.get(0).getValue() == 1 && CardPackage.dealerHand.get(1).getValue() == 10 ||
                    CardPackage.dealerHand.get(0).getValue() == 10 && CardPackage.dealerHand.get(1).getValue() == 1;
        }
        return false;
    }

    public boolean playerHasAce() {
        for (Card card : CardPackage.playerHand) {

            return card.getValue() == 1;
        }
        return false;
    }

    public void resetAceValue() {
        for (Card card : CardPackage.deck) {
            if (card.getValue() == 11) {
                card.setValue(1);
            }
        }
    }

    //progress for user level
    public void Progress(double progress) {
        leverProgress = findViewById(R.id.progressBar);

        progressCounter += progress;
        leverProgress.setProgress(progressCounter);
        leverProgress.setMax(maxProgress);
        if (progressCounter >= maxProgress) {
            levelToint++;
            maxProgress += 1000;
            leverProgress.setProgress(0);
            progressCounter = 0;
        }

        Log.i("progress", String.valueOf(progressCounter));
        Log.i("progress", String.valueOf(maxProgress) + " max");
        //save all user daa
        try {
            saveToFile(LEVELFILENAME, levelToint);
            saveToFile(PROGRESSCOUNTER, progressCounter);
            saveToFile(DIFFICULTY, maxProgress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        level.setText(String.valueOf(levelToint));
    }
}
        //TODO:Fix Progress bar for the game level
        //TODO: Add sound effects for the game
        //TODO: Add animations for the game
        //TODO: Add music for the game
