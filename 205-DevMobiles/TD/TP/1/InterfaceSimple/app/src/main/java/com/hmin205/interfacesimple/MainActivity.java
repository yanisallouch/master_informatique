package com.hmin205.interfacesimple;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class MainActivity extends FragmentActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textAskUser = new TextView(this);
        textAskUser.setText(getString(R.string.ask_user));
        textAskUser.setTextSize( COMPLEX_UNIT_SP, 20 );

        TextInputLayout nameLayout = new TextInputLayout(this);
        nameLayout.setHelperTextEnabled(true);
        nameLayout.setHelperText(getString(R.string.enter_name));
        nameLayout.setPlaceholderText(View.AUTOFILL_HINT_NAME);

        TextInputLayout lastNameLayout = new TextInputLayout(this);
        lastNameLayout.setHelperTextEnabled(true);
        lastNameLayout.setHelperText(getString(R.string.enter_lastName));
        lastNameLayout.setPlaceholderText(View.AUTOFILL_HINT_NAME);

        TextInputLayout ageLayout = new TextInputLayout(this);
        ageLayout.setHelperTextEnabled(true);
        ageLayout.setHelperText(getString(R.string.enter_age));

        TextInputLayout skillsFieldLayout = new TextInputLayout(this);
        skillsFieldLayout.setHelperTextEnabled(true);
        skillsFieldLayout.setHelperText(getString(R.string.enter_skillField));

        TextInputLayout phoneLayout = new TextInputLayout(this);
        phoneLayout.setHelperTextEnabled(true);
        phoneLayout.setHelperText(getString(R.string.enter_phone));

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

        Button alertDialogFragButton = new Button(this);
        alertDialogFragButton.setText(getString(R.string.enter_validate));
        alertDialogFragButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.dialog_ask_confirmation)
                    .setPositiveButton(R.string.dialog_confirmation, (dialog, id) -> {
                        alertDialogFragButton.setBackgroundColor(getResources().getColor(R.color.teal_200));
                    })
                    .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> {
                        alertDialogFragButton.setBackgroundColor(getResources().getColor(R.color.purple_200));
                    });
            builder.create().show();
        });

        linearLayout.addView(textAskUser);
        linearLayout.addView(nameLayout);
        linearLayout.addView(lastNameLayout);
        linearLayout.addView(ageLayout);
        linearLayout.addView(skillsFieldLayout);
        linearLayout.addView(phoneLayout);
        linearLayout.addView(alertDialogFragButton);

        setContentView(linearLayout);
    }
}