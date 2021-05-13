package com.hmin205.myfirstapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textAskUser = new TextView(this);
        textAskUser.setText(getString(R.string.fill_in));

        TextInputLayout nameLayout = new TextInputLayout(this);
        nameLayout.setId(R.id.firstName);
        nameLayout.setHelperTextEnabled(true);
        nameLayout.setHelperText(getString(R.string.firstName));
        nameLayout.setPlaceholderText(getString(R.string.firstName));

        TextInputLayout lastNameLayout = new TextInputLayout(this);
        lastNameLayout.setId(R.id.lastName);
        lastNameLayout.setHelperTextEnabled(true);
        lastNameLayout.setHelperText(getString(R.string.lastName));
        lastNameLayout.setPlaceholderText(getString(R.string.lastName));

        TextInputLayout ageLayout = new TextInputLayout(this);
        ageLayout.setId(R.id.age);
        ageLayout.setHelperTextEnabled(true);
        ageLayout.setHelperText(getString(R.string.age));
        ageLayout.setPlaceholderText(getString(R.string.age));

        TextInputLayout skillsFieldLayout = new TextInputLayout(this);
        skillsFieldLayout.setId(R.id.skillsField);
        skillsFieldLayout.setHelperTextEnabled(true);
        skillsFieldLayout.setHelperText(getString(R.string.skillsField));
        skillsFieldLayout.setPlaceholderText(getString(R.string.skillsField));

        TextInputLayout phoneLayout = new TextInputLayout(this);
        phoneLayout.setId(R.id.phone);
        phoneLayout.setHelperTextEnabled(true);
        phoneLayout.setHelperText(getString(R.string.phone));
        phoneLayout.setPlaceholderText(getString(R.string.phone));

        TextInputEditText nameText = new TextInputEditText(this);
        nameText.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        nameLayout.addView(nameText);

        TextInputEditText lastNameText = new TextInputEditText(this);
        lastNameText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        lastNameLayout.addView(lastNameText);

        TextInputEditText ageText = new TextInputEditText(this);
        ageText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        ageLayout.addView(ageText);

        TextInputEditText skillsFieldText = new TextInputEditText(this);
        skillsFieldText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        skillsFieldLayout.addView(skillsFieldText);

        TextInputEditText phoneText = new TextInputEditText(this);
        phoneText.setInputType(InputType.TYPE_CLASS_PHONE);
        phoneLayout.addView(phoneText);

        Button validateButton = new MaterialButton(this);
        validateButton.setText(getString(R.string.validate));
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.confirmation)
                        .setPositiveButton(R.string.confirmation, (dialog, id) -> {
                            validateButton.setBackgroundColor(getResources().getColor(R.color.teal_200));
                            onSendMessage(v);
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                            validateButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
                        });
                builder.create().show();
            }
        });

        linearLayout.addView(textAskUser);
        linearLayout.addView(nameLayout);
        linearLayout.addView(lastNameLayout);
        linearLayout.addView(ageLayout);
        linearLayout.addView(skillsFieldLayout);
        linearLayout.addView(phoneLayout);
        linearLayout.addView(validateButton);

        setContentView(linearLayout);
    }

    public void onSendMessage(View view) {
        Intent intent = new Intent(this, ReceiveMessageActivity.class);

        TextInputLayout firstNameView = findViewById(R.id.firstName);
        TextInputLayout lastNameView = findViewById(R.id.lastName);
        TextInputLayout ageView = findViewById(R.id.age);
        TextInputLayout skillsFieldView = findViewById(R.id.skillsField);
        TextInputLayout phoneView = findViewById(R.id.phone);

        String messageText = "";

        messageText += String.valueOf(firstNameView.getEditText().getText()) + "\n";
        messageText += String.valueOf(lastNameView.getEditText().getText()) + "\n";
        messageText += String.valueOf(ageView.getEditText().getText()) + "\n";
        messageText += String.valueOf(skillsFieldView.getEditText().getText()) + "\n";
        messageText += String.valueOf(phoneView.getEditText().getText()) + "\n";

        intent.putExtra(ReceiveMessageActivity.EXTRA_MESSAGE, messageText);
        startActivity(intent);
    }
}