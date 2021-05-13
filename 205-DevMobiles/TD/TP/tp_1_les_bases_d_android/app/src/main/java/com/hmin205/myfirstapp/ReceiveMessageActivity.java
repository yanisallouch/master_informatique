package com.hmin205.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class ReceiveMessageActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_message);
        Intent intent = getIntent();
        String messageText = intent.getStringExtra(EXTRA_MESSAGE);
        TextView messageView = (TextView)findViewById(R.id.message);
        messageView.setText(messageText);
    }

    public void onSendMessage(View view) {
        Intent intent = new Intent(this, CallActivity.class);
        TextView textView = findViewById(R.id.message);
        String messageText = String.valueOf(textView.getText());
        String[] fields = messageText.split("\n");
        String numberPhone = fields[fields.length-1];
        intent.putExtra(CallActivity.EXTRA_NUMBER, numberPhone);
        startActivity(intent);
    }

    public void onReturn(View view) {
        finish();
    }
}