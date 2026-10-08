package com.example.myapplication;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConvert extends AppCompatActivity {

    Spinner spinnerFrom, spinnerTo;
    EditText Amount;
    TextView Result;
    Button Convert;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;


    HashMap<String, Double> ratesToUSD = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_convert);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Monex");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else if (id == R.id.chat) {
                startActivity(new Intent(this, ChatActivity.class));
            } else if (id == R.id.logout) {
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }

            drawerLayout.closeDrawer(Gravity.LEFT);
            return true;
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            }

            if (id == R.id.chat) {
                startActivity(new Intent(this, ChatActivity.class));
            }

            if (id == R.id.logout) {
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }

            drawerLayout.closeDrawers();
            return true;
        });

        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        Amount = findViewById(R.id.etAmount);
        Result = findViewById(R.id.tvResult);
        Convert = findViewById(R.id.btnConvert);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        String[] currencies = {
                "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD",
                "MXN", "SGD", "HKD", "NOK", "KRW", "TRY", "INR", "RUB", "ZAR", "BRL",
                "EGP", "AED", "KWD", "QAR", "LBP", "SAR"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_layout, currencies
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);


        ratesToUSD.put("USD", 1.0);
        ratesToUSD.put("EUR", 1.08);
        ratesToUSD.put("GBP", 1.25);
        ratesToUSD.put("JPY", 0.0063);
        ratesToUSD.put("AUD", 0.66);
        ratesToUSD.put("CAD", 0.74);
        ratesToUSD.put("CHF", 1.10);
        ratesToUSD.put("CNY", 0.14);
        ratesToUSD.put("SEK", 0.094);
        ratesToUSD.put("NZD", 0.62);
        ratesToUSD.put("MXN", 0.055);
        ratesToUSD.put("SGD", 0.74);
        ratesToUSD.put("HKD", 0.13);
        ratesToUSD.put("NOK", 0.091);
        ratesToUSD.put("KRW", 0.00077);
        ratesToUSD.put("TRY", 0.034);
        ratesToUSD.put("INR", 0.012);
        ratesToUSD.put("RUB", 0.011);
        ratesToUSD.put("ZAR", 0.055);
        ratesToUSD.put("BRL", 0.20);
        ratesToUSD.put("EGP", 0.020);
        ratesToUSD.put("AED", 0.27);
        ratesToUSD.put("KWD", 3.24);
        ratesToUSD.put("QAR", 0.27);
        ratesToUSD.put("LBP", 0.000011);
        ratesToUSD.put("SAR", 0.27);


        Convert.setOnClickListener(v -> convertCurrency());
    }

    private void convertCurrency() {
        String from = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();
        String amountStr = Amount.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Amount.setError("Enter a number");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (Exception e) {
            Amount.setError("Invalid input");
            return;
        }

        double rateFrom = ratesToUSD.get(from);
        double rateTo = ratesToUSD.get(to);


        double result = amount * rateFrom / rateTo;

        Result.setText(String.format("%,.2f %s", result, to));
    }
}

