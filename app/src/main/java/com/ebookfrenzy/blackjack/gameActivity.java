package com.ebookfrenzy.blackjack;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class gameActivity extends AppCompatActivity {

    private LinearLayout playerLayout;
    private LinearLayout dealerLayout;
    private LinearLayout playingLayout ;
    private LinearLayout bettingLayout;
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
    private int money;
    private int betToInt;
    private int dealerIndex=0;
    private int playerIndex=0;
    private final Handler handler = new Handler();
    private static final String FILENAME = "money.txt";
    public static TextView level ;


    /***
     * This method is called when the activity is created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
money = 50000;
        new CardPackage(this);
         level = findViewById(R.id.levelId);

        playingLayout = findViewById(R.id.playingButton_layout);
        bettingLayout = findViewById(R.id.chipLayout);

        ImageButton chip1 = findViewById(R.id.chi_oneId);
        ImageButton chip5 = findViewById(R.id.chip_fiveId);
        ImageButton chip25 = findViewById(R.id.chip_twentyFiveId);
        ImageButton chip50 = findViewById(R.id.chip_fifthyId);
        ImageButton chip100 = findViewById(R.id.chip_hundredId);

        playerCard1 = findViewById(R.id.playerFirstCardId);
        dealerCard1= findViewById(R.id.dealerFirstCardId);
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

        replayButton.setOnClickListener(v -> {
            if(money>0&&betToInt>0) {
                try {
                    saveMoneyToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                restart();
                level.setText(CardPackage.deck.size()+"");
            }
            else{
                Toast.makeText(gameActivity.this, "You have no money or bet", Toast.LENGTH_SHORT).show();
            }
        });

        stand.setOnClickListener(v -> stand());

        doubleDown.setOnClickListener(v -> doubleDown());
        moneyAmount.setText("$"+money);

        Button betButton = findViewById(R.id.betButtonId);

        betButton.setOnClickListener(v -> {
            if (money>0&&betToInt>0&&CardPackage.playerHand.size()==0&&CardPackage.dealerHand.size()==0) {

                playingLayout.setVisibility(View.VISIBLE);
                bettingLayout.setVisibility(View.GONE);
                betButton.setVisibility(View.GONE);
                try {
                    saveMoneyToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startGame();
            }
            else {
                Toast.makeText(gameActivity.this, "You don't have enough money or bet ", Toast.LENGTH_SHORT).show();
            }
        });

            betToInt = Integer.parseInt(betAmount.getText().toString());

        getMoneyFromFile();

        chip1.setOnClickListener(this::onClick);
        chip5.setOnClickListener(this::onClick);
        chip25.setOnClickListener(this::onClick);
        chip50.setOnClickListener(this::onClick);
        chip100.setOnClickListener(this::onClick);

    }



    /***
     * This method is called when the user clicks on a chip.
     * @param v
     */
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
                betAmount.setText("insufficient money");
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
     * This method is used to distribute the cards to the player and dealer
     */
    public void distributeCards() {
        CardPackage.getFourCards();
        doubleDown.setVisibility(View.VISIBLE);
        dealerCard1.setImageResource(CardPackage.dealerHand.get(dealerIndex).getImage());
        dealerIndex++;
        dealerCard2.setImageResource(R.drawable.back);
        playerCard1.setImageResource(CardPackage.playerHand.get(playerIndex).getImage());
        playerIndex++;
        playerCard2.setImageResource(CardPackage.playerHand.get(playerIndex).getImage());
        playerIndex++;
        dealerIndex++;
        if(playerhasAce()&&CardPackage.sumPlayerHand()+10<=21){
            playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand())+
                            "/"+String.valueOf(CardPackage.sumPlayerHand()+10));
        }
        else{
            playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
        }

        dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand()-CardPackage.dealerHand.get(dealerIndex-1).getValue()));
        if(playerHasBlackJack()){
            Toast.makeText(this, "BLACKJACK", Toast.LENGTH_SHORT).show();
            playerCardValue.setText("BLACKJACK");
            dealerCard2.setImageResource(CardPackage.dealerHand.get(dealerIndex-1).getImage());
            money+= betToInt*2.5;
            moneyAmount.setText(String.valueOf(money));
            betAmount.setText("0");
            betToInt = 0;
            clearCards();
            gameEnd();
        }
        resetAceValue();
    }
    /***
     * This method is used to hit the card to the player
     */
    public  void hitCard() {
        doubleDown.setVisibility(View.GONE);
        ImageView newCard = new ImageView(this);
        Card card = CardPackage.getCard();
        newCard.setImageResource(CardPackage.playerHand.get(CardPackage.playerHand.size()-1).getImage());
        newCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,1));
        newCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        playerLayout.addView(newCard);
        if(playerhasAce()&&CardPackage.sumPlayerHand()+10<=21) {
            playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand())+
                                    "/"+String.valueOf(CardPackage.sumPlayerHand()+10));
        }
        else {
            playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
        }
        if(CardPackage.isBust()) {
            playerCardValue.setText("BUST");
            gameEnd();
            clearCards();
            betToInt = 0;
            betAmount.setText(String.valueOf(betToInt));
        }
    }

    /***
     * bust event
     */
    public void gameEnd() {
        playingLayout.setVisibility(View.GONE);
        bettingLayout.setVisibility(View.VISIBLE);
        replayButton.setVisibility(View.VISIBLE);
    }


    /***
     * This method is used to clear the cards from the player and dealer
     */
    public void clearCards(){
        CardPackage.deletedCards.addAll(CardPackage.playerHand);
        CardPackage.playerHand.clear();
        CardPackage.deletedCards.addAll(CardPackage.dealerHand);
        CardPackage.dealerHand.clear();
    }
    /***
     * This method is used to restart the game
     */
    public void restart(){
        playingLayout.setVisibility(View.VISIBLE);
        replayButton.setVisibility(View.GONE);
        bettingLayout.setVisibility(View.GONE);
        dealerCardValue.setText("0");
        playerCardValue.setText("0");

        for(int i=playerLayout.getChildCount()-1;i>1;i--){
            playerLayout.removeViewAt(i);
        }

        for(int i=dealerLayout.getChildCount()-1;i>1;i--){
            dealerLayout.removeViewAt(i);
        }

        playerIndex = 0;
        dealerIndex = 0;
      distributeCards();

    }


    /***
     * This method is stand the player hand
     */
    public void stand(){
            while (CardPackage.sumDealerHand() < 17) {
                dealerCard2.setImageResource(CardPackage.dealerHand.get(CardPackage.dealerHand.size() - 1).getImage());
                CardPackage.addCardDealer();
                dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand()));

                ImageView newCard = new ImageView(this);
                newCard.setImageResource(CardPackage.dealerHand.get(CardPackage.dealerHand.size() - 1).getImage());
                newCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                newCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                handler.postDelayed(() -> dealerLayout.addView(newCard), 100);
            }


        for (Card card:CardPackage.playerHand) {
            if(card.getValue()==1){
                if(CardPackage.sumPlayerHand()+10<=21){
                    card.setValue(11);
                    playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
                }
                else{
                    playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
                }
            }
        }
        if(CardPackage.sumDealerHand() > 21){
            dealerCardValue.setText("BUST :"+String.valueOf(CardPackage.sumDealerHand()));
            money+= betToInt*2;
        }
        if(CardPackage.sumPlayerHand()>21){
            playerCardValue.setText("BUST :"+String.valueOf(CardPackage.sumPlayerHand()));
            money-= betToInt;
        }
        else if(CardPackage.sumDealerHand() > CardPackage.sumPlayerHand()&& CardPackage.sumDealerHand()<=21){
            Toast.makeText(this, "DEALER WINS", Toast.LENGTH_SHORT).show();
            dealerCardValue.setText("WINS: "+String.valueOf(CardPackage.sumDealerHand()));

        }
        else if(CardPackage.sumDealerHand() == CardPackage.sumPlayerHand()){
            Toast.makeText(this, "DRAW", Toast.LENGTH_SHORT).show();
            dealerCardValue.setText("DRAW :"+String.valueOf(CardPackage.sumDealerHand()));
            playerCardValue.setText("DRAW :"+String.valueOf(CardPackage.sumPlayerHand()));
            money+= betToInt;
        }
        else if(CardPackage.sumDealerHand() < CardPackage.sumPlayerHand()&& CardPackage.sumPlayerHand()<=21){
            Toast.makeText(this, "PLAYER WINS", Toast.LENGTH_SHORT).show();
            playerCardValue.setText("WINS : "+String.valueOf(CardPackage.sumPlayerHand()));
            money+= betToInt*2;
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
   public void doubleDown(){
         if(money >= betToInt){
             money-= betToInt;
             betToInt*=2;
              moneyAmount.setText(String.valueOf(money));
              betAmount.setText(String.valueOf(betToInt));


             handler.postDelayed(() -> {
                 hitCard();
             }, 100);

             handler.postDelayed(
                        () -> {
                            stand();
                        }
                        , 3000);
             playingLayout.setVisibility(View.GONE);
         }

         else{
              Toast.makeText(this, "insufficient money", Toast.LENGTH_SHORT).show();
         }

   }

   /**
    * This method is to save the player's money
    */
   public void  saveMoneyToFile() throws IOException {
        FileOutputStream outputStream=null;
        outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        outputStream.write(String.valueOf(money).getBytes());
        outputStream.close();
   }
    /**
     * This method is to load the player's money
     */
   public void getMoneyFromFile(){
        FileInputStream inputStream=null;
        try {
            inputStream = openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            money = Integer.parseInt(stringBuilder.toString());
            moneyAmount.setText(String.valueOf(money));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        if(playingLayout.getVisibility()==View.VISIBLE){
            Toast.makeText(this, "If you press again you will lose your bet", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    public boolean playerHasBlackJack(){
       return CardPackage.playerHand.get(0).getValue()==1&&CardPackage.playerHand.get(1).getValue()==10||
               CardPackage.playerHand.get(0).getValue()==10&&CardPackage.playerHand.get(1).getValue()==1;
    }
    public boolean playerhasAce() {
        for (Card card : CardPackage.playerHand) {
            if (card.getValue() == 1) {
                return true;
            }
        }
        return false;
    }
    public void resetAceValue(){
        for (Card card:CardPackage.playerHand) {
            if(card.getValue()==11){
                card.setValue(1);
            }
        }
    }
    //TODO: Progress bar for the game level
    //TODO: Add sound effects for the game
            //TODO: Add animations for the game
            //TODO: Add music for the game
            //TODO: Sa
}
