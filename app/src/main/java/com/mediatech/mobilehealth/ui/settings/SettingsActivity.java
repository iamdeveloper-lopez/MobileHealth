package com.mediatech.mobilehealth.ui.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.mediatech.mobilehealth.R;
import com.mediatech.mobilehealth.helper.DatabaseHelper;
import com.mediatech.mobilehealth.model.UserSetting;
import com.mediatech.mobilehealth.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.android.AndroidInjection;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_settings_radio_button_male)
    RadioButton buttonMale;
    @BindView(R.id.activity_settings_radio_button_female)
    RadioButton buttonFemale;
    @BindView(R.id.activity_settings_edit_text_height)
    EditText editTextHeight;

    @OnClick(R.id.activity_settings_button_save)
    void onSaveClicked() {
        String heightValue = editTextHeight.getText().toString();
        if (genderValue > 0f && !TextUtils.isEmpty(heightValue)) {
            UserSetting userSetting = new UserSetting(String.valueOf(genderValue), heightValue);
            DatabaseHelper.get().put("settings", userSetting.toJson());
            finish();
        } else {
            Toast.makeText(this, "Other fields empty or not modified", Toast.LENGTH_SHORT).show();
        }
    }

    float genderValue = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        buttonMale.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                genderValue = 0.415f;
            }
        });

        buttonFemale.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                genderValue = 0.413f;
            }
        });

        String settingsJson = DatabaseHelper.get().getString("settings", null);
        if (!TextUtils.isEmpty(settingsJson)) {
            UserSetting setting = new Gson().fromJson(settingsJson, UserSetting.class);
            if (Float.valueOf(setting.gender) == 0.415f) {
                buttonMale.setChecked(true);
            } else if (Float.valueOf(setting.gender) == 0.413f) {
                buttonFemale.setChecked(true);
            }
            editTextHeight.setText(setting.height);
        }

    }
}
