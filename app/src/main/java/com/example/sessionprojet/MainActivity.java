package com.example.sessionprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import database.DatabaseHelper;  // Import the DatabaseHelper class

public class MainActivity extends AppCompatActivity
{
    private DatabaseHelper dbHelper;
    private Button btnRoute, btnAbonnement, btnLivreur, btnLister, btnQuitter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        btnRoute = findViewById(R.id.btnRoute);
        btnAbonnement = findViewById(R.id.btnAbonnements);
        btnLivreur = findViewById(R.id.btnLivreurs);
        btnLister = findViewById(R.id.btnList);
        btnQuitter = findViewById(R.id.btnQuitter);

        btnLister.setEnabled(false);

        btnRoute.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(MainActivity.this, RouteActivity.class);
                startActivity(intent);
            }
        });

        btnAbonnement.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(MainActivity.this, AbonnementActivity.class);
                startActivity(intent);
            }
        });

        btnLivreur.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(MainActivity.this, LivreurActivity.class);
                startActivity(intent);
            }
        });

        btnLister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        btnQuitter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finishAffinity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLivreurAvailability();
    }

    private void checkLivreurAvailability() {
        if (dbHelper.getLivreurCount() > 0) {
            btnRoute.setEnabled(true);
            btnAbonnement.setEnabled(true);
        } else {
            btnRoute.setEnabled(false);
            btnAbonnement.setEnabled(false);
        }
    }
}
