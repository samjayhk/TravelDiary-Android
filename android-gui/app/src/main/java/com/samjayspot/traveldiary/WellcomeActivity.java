package com.samjayspot.traveldiary;

/**
 * THIS PROJECT IS A UNIVERSITY PROJECT AND MADE BY SCHOOL FOR HIGHER AND PROFESSIONAL EDUCATION WITH COVENTRY UNIVERSITY (UK).
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * AUTHOR INFORMATION:
 * NAME: TSANG Long Fung (187107130)
 * CONTACT NUMBER: (+852) 6679 2339
 * CONTACT EMAIL: 187107130@stu.vtc.edu.hk
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class WellcomeActivity extends SwipeBackActivity implements View.OnClickListener {

    ImageView btnBack;
    Button btnRegister, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnBack.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
                break;
            case R.id.btnRegister:
                Intent registerIntent = new Intent(WellcomeActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.btnLogin:
                Intent loginIntent = new Intent(WellcomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }
}
