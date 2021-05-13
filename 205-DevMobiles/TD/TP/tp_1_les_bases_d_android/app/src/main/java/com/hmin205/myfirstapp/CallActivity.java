package com.hmin205.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.URI;

public class CallActivity extends AppCompatActivity {

    public static final String EXTRA_NUMBER = "number";
    public static final String SPACE = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        ActivityCompat.requestPermissions(CallActivity.this,
                new String[]{Manifest.permission.CALL_PHONE},1);

        Intent intent = getIntent();
        String messageText = intent.getStringExtra(EXTRA_NUMBER);
        TextView messageView = (TextView)findViewById(R.id.message);
        messageView.setText(new StringBuilder().append(messageView.getText()).append(SPACE).append(messageText).toString());

    }

    public void onCallNumber(View view) {
        TextView textView = findViewById(R.id.message);
        String[] fields = String.valueOf(textView.getText()).split(SPACE);
        String number = fields[fields.length-1];
        Uri uri = Uri.parse("tel:"+number);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        intent.putExtra(Intent.EXTRA_PHONE_NUMBER, Integer.valueOf(number));
        startActivity(intent);
    }
}