package com.example.dailyexpenseapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etAmount, etDescription;
    Spinner spCategory;
    RadioGroup rgPayment;
    Button btnAdd, btnCall, btnSMS, btnEmail;
    ListView listView;

    ArrayList<String> expenseList;
    ArrayAdapter<String> adapter;

    String accountantNumber = "9876543210";

    String[] categories = {
            "Office Supplies",
            "Travel / Taxi",
            "Client Meeting",
            "Printing / Xerox",
            "Internet / Phone Bill"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        spCategory = findViewById(R.id.spCategory);
        rgPayment = findViewById(R.id.rgPayment);
        btnAdd = findViewById(R.id.btnAdd);
        btnCall = findViewById(R.id.btnCall);
        btnSMS = findViewById(R.id.btnSMS);
        btnEmail = findViewById(R.id.btnEmail);
        listView = findViewById(R.id.listView);

        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories);
        spCategory.setAdapter(spinnerAdapter);

        expenseList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                expenseList);
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> addExpense());

        btnCall.setOnClickListener(v -> showDialog("call"));
        btnSMS.setOnClickListener(v -> showDialog("sms"));

        btnEmail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{"accountant@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,
                    "Daily Expense Details");
            startActivity(emailIntent);
        });
    }

    private void addExpense() {
        String amount = etAmount.getText().toString();
        String description = etDescription.getText().toString();
        String category = spCategory.getSelectedItem().toString();

        int selectedId = rgPayment.getCheckedRadioButtonId();
        RadioButton rb = findViewById(selectedId);
        String paymentMode = rb.getText().toString();

        String record = "₹" + amount +
                "\nDesc: " + description +
                "\nCategory: " + category +
                "\nPayment: " + paymentMode;

        expenseList.add(record);
        adapter.notifyDataSetChanged();

        etAmount.setText("");
        etDescription.setText("");
    }

    private void showDialog(String action) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to " + action + "?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    if (action.equals("call")) {
                        Intent callIntent =
                                new Intent(Intent.ACTION_DIAL,
                                        Uri.parse("tel:" + accountantNumber));
                        startActivity(callIntent);
                    } else {
                        Intent smsIntent =
                                new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("sms:" + accountantNumber));
                        startActivity(smsIntent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}