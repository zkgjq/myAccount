package com.myAccount;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {
   String totalExpense = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getWindow().setStatusBarColor(getColor(R.color.light_green));
    }
    setContentView(R.layout.activity_account);
    handleButton();
  }

  private void handleButton() {
    Button addBtn = findViewById(R.id.add_btn);
    addBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(
            Context.INPUT_METHOD_SERVICE);
        EditText editText = findViewById(R.id.inputText);
        Button ensureBrn = findViewById(R.id.ensureBtn);
        if (imm != null) {
          editText.setVisibility(View.VISIBLE);
          ensureBrn.setVisibility(View.VISIBLE);
          ensureBrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              TextView total = findViewById(R.id.total_expense);
              totalExpense = editText.getText().toString();
              total.setText("¥ "+totalExpense);
            }
          });
          editText.requestFocus();
          imm.showSoftInput(editText, 0);
        }
      }
    });

  }
}