package com.ebookfrenzy.blackjack;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class gameActivity extends AppCompatActivity {

    LinearLayout playerLayout;
    LinearLayout dealerLayout;
    LinearLayout playingLayout ;
    LinearLayout bettingLayout;
    Button replayButton;
    TextView betAmount;
    TextView moneyAmount;

    ImageView playerCard1;
    ImageView dealerCard1;
    ImageView playerCard2;
    ImageView dealerCard2;
    TextView playerCardValue;
    TextView dealerCardValue;
    int money;
    int betToInt;
    int dealerIndex=0;
    int playerIndex=0;
    final Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


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
        Button doubleDown = findViewById(R.id.doubleButtonId);
        replayButton = findViewById(R.id.replayButtonid);

        hit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitCard();
            }
        });

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(money>0&&betToInt>0) {
                    restart();
                }
                else{
                    Toast.makeText(gameActivity.this, "You have no money or bet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stand();
            }
        });

        doubleDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    doubleDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        money = Integer.parseInt(moneyAmount.getText().toString());

        Button betButton = findViewById(R.id.betButtonId);

        betButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (money>0&&betToInt>0) {

                    playingLayout.setVisibility(View.VISIBLE);
                    bettingLayout.setVisibility(View.GONE);
                    betButton.setVisibility(View.GONE);
                    startGame();
                }
                else {
                    Toast.makeText(gameActivity.this, "You don't have enough money or bet ", Toast.LENGTH_SHORT).show();
                }
            }
        });

            betToInt = Integer.parseInt(betAmount.getText().toString());


        chip1.setOnClickListener(this::onClick);
        chip5.setOnClickListener(this::onClick);
        chip25.setOnClickListener(this::onClick);
        chip50.setOnClickListener(this::onClick);
        chip100.setOnClickListener(this::onClick);


    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chi_oneId:
                if (money >= 1) {
                    money -= 1;
                    betToInt += 1;
                    betAmount.setText(String.valueOf(betToInt));
                    moneyAmount.setText(String.valueOf(money));
                }
                break;
            case R.id.chip_fiveId:
                if (money >= 5) {
                    money -= 5;
                    betToInt += 5;
                    betAmount.setText(String.valueOf(betToInt));
                    moneyAmount.setText(String.valueOf(money));
                }
                break;
            case R.id.chip_twentyFiveId:
                if (money >= 25) {
                    money -= 25;
                    betToInt += 25;
                    betAmount.setText(String.valueOf(betToInt));
                    moneyAmount.setText(String.valueOf(money));
                }
                break;
            case R.id.chip_fifthyId:
                if (money >= 50) {
                    money -= 50;
                    betToInt += 50;
                    betAmount.setText(String.valueOf(betToInt));
                    moneyAmount.setText(String.valueOf(money));
                }
                break;
            case R.id.chip_hundredId:
                if (money >= 100) {
                    money -= 100;
                    betToInt += 100;
                    betAmount.setText(String.valueOf(betToInt));
                    moneyAmount.setText(String.valueOf(money));
                }
                break;
            default:
                betAmount.setText("insufficient money");
                break;
        }
    }

    public void startGame() {
        distributeCards();

    }

    public void distributeCards() {
        CardPackage.getFourCards();

        dealerCard1.setImageResource(CardPackage.dealerHand.get(dealerIndex).getImage());
        dealerIndex++;
        dealerCard2.setImageResource(R.drawable.back);
        playerCard1.setImageResource(CardPackage.playerHand.get(playerIndex).getImage());
        playerIndex++;
        playerCard2.setImageResource(CardPackage.playerHand.get(playerIndex).getImage());
        playerIndex++;
        dealerIndex++;
        playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
        dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand()-CardPackage.dealerHand.get(dealerIndex-1).getValue()));
    }
    public  void hitCard() {
        ImageView newCard = new ImageView(this);
        CardPackage.getCard();
        newCard.setImageResource(CardPackage.playerHand.get(CardPackage.playerHand.size()-1).getImage());
        newCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,1));
        newCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        playerLayout.addView(newCard);
        playerCardValue.setText(String.valueOf(CardPackage.sumPlayerHand()));
        if(CardPackage.isBust()) {
            playerCardValue.setText("BUST");
            bustEvent();
            betToInt = 0;
            betAmount.setText(String.valueOf(betToInt));
            bettingLayout.setVisibility(View.VISIBLE);
        }
    }

    public void bustEvent() {
        Toast.makeText(this, "BUST", Toast.LENGTH_SHORT).show();
        playingLayout.setVisibility(View.GONE);
        bettingLayout.setVisibility(View.VISIBLE);
        replayButton.setVisibility(View.VISIBLE);
    }

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
        CardPackage.deletedCards.addAll(CardPackage.playerHand);
        CardPackage.playerHand.clear();
        CardPackage.deletedCards.addAll(CardPackage.dealerHand);
        CardPackage.dealerHand.clear();
        playerIndex = 0;
        dealerIndex = 0;
      distributeCards();

    }

    public void stand(){
        if(!CardPackage.isBust()) {
            while (CardPackage.sumDealerHand() < 17) {
                dealerCard2.setImageResource(CardPackage.dealerHand.get(CardPackage.dealerHand.size() - 1).getImage());
                CardPackage.addCardDealer();
                dealerCardValue.setText(String.valueOf(CardPackage.sumDealerHand()));

                ImageView newCard = new ImageView(this);
                newCard.setImageResource(CardPackage.dealerHand.get(CardPackage.dealerHand.size() - 1).getImage());
                newCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                newCard.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dealerLayout.addView(newCard);
                    }
                }, 100);
            }
        }
        if(CardPackage.sumDealerHand()>17){
            dealerCard2.setImageResource(CardPackage.dealerHand.get(1).getImage());
        }
        if(CardPackage.sumDealerHand() > 21){
            dealerCardValue.setText("BUST :"+String.valueOf(CardPackage.sumDealerHand()));
            bustEvent();
            money+= betToInt*2;
            moneyAmount.setText(String.valueOf(money));
            betAmount.setText("0");
            betToInt = 0;
        }
        if(CardPackage.sumPlayerHand()>21){
            playerCardValue.setText("BUST :"+String.valueOf(CardPackage.sumPlayerHand()));
            bustEvent();
            money-= betToInt;
            betToInt = 0;
            betAmount.setText(String.valueOf(betToInt));
            bettingLayout.setVisibility(View.VISIBLE);
        }
        else if(CardPackage.sumDealerHand() > CardPackage.sumPlayerHand()&& CardPackage.sumDealerHand()<=21){
            Toast.makeText(this, "DEALER WINS", Toast.LENGTH_SHORT).show();
            dealerCardValue.setText("WINS: "+String.valueOf(CardPackage.sumDealerHand()));
            dealerCard2.setImageResource(CardPackage.dealerHand.get(1).getImage());
            moneyAmount.setText(String.valueOf(money));
            betAmount.setText("0");
            betToInt = 0;
            bustEvent();
        }
        else if(CardPackage.sumDealerHand() == CardPackage.sumPlayerHand()){
            Toast.makeText(this, "DRAW", Toast.LENGTH_SHORT).show();
            dealerCardValue.setText("DRAW :"+String.valueOf(CardPackage.sumDealerHand()));
            playerCardValue.setText("DRAW :"+String.valueOf(CardPackage.sumPlayerHand()));
            dealerCard2.setImageResource(CardPackage.dealerHand.get(1).getImage());
            money+= betToInt;
            moneyAmount.setText(String.valueOf(money));
            betAmount.setText("0");
            betToInt = 0;
            bustEvent();
        }
        else{
            Toast.makeText(this, "PLAYER WINS", Toast.LENGTH_SHORT).show();
            playerCardValue.setText("WINS : "+String.valueOf(CardPackage.sumPlayerHand()));
            money+= betToInt*2;
            moneyAmount.setText(String.valueOf(money));
            betAmount.setText("0");
            betToInt = 0;
            dealerCard2.setImageResource(CardPackage.dealerHand.get(1).getImage());
            bustEvent();
        }
    }
   public void doubleDown() throws InterruptedException {
         if(money >= betToInt){
             money-= betToInt;
             betToInt*=2;
              moneyAmount.setText(String.valueOf(money));
              betAmount.setText(String.valueOf(betToInt));


             handler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                        hitCard();
                 }
             }, 2000);

             handler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     stand();
                 }
             }, 4000);
             playingLayout.setVisibility(View.GONE);
         }

         else{
              Toast.makeText(this, "insufficient money", Toast.LENGTH_SHORT).show();
         }
   }
            //TODO: Progress bar for the game level
            //TODO: Add sound effects for the game
            //TODO: Add animations for the game
            //TODO: Add music for the game
            //TODO: Sa
}
