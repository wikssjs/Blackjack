package com.ebookfrenzy.blackjack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, gameActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.getMonetId).setOnClickListener(v -> {
        });

    }

    /***
     * This method is used to exit the application.
     */
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}