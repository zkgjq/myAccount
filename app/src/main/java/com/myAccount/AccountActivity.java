package com.myAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myAccount.adapter.Expense;
import com.myAccount.adapter.ExpenseAdapter;
import com.myAccount.utils.SpUtils;
import com.myAccount.utils.Utils;

public class AccountActivity extends AppCompatActivity {
  TextView mTotalExpense;
  EditText mEditText;
  Button mEnsureBtn;
  Button mRecordBtn;
  Button mCLearBtn;
  Spinner mSpinner;
  RecyclerView mRecyclerView;

  String total = "0.00";
  String food = "0.00";
  String taobao = "0.00";
  String traffic = "0.00";
  String daily = "0.00";
  String other = "0.00";
  List<String> dataset = new ArrayList<>();
  String[] classes = {"food", "taobao", "traffic", "daily", "other"};
  String currentItem = "吃饭";
  String currentNum = "0.00";
  InputMethodManager imm;
  ExpenseAdapter expenseAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().hide();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      getWindow().setStatusBarColor(getColor(R.color.light_green));
    }
    setContentView(R.layout.activity_account);
    doBindView();
    init();
    handleClick();
  }

  private void doBindView() {
    mTotalExpense = findViewById(R.id.total_expense);
    mEditText = findViewById(R.id.inputText);
    mEnsureBtn = findViewById(R.id.ensureBtn);
    mRecordBtn = findViewById(R.id.add_btn);
    mCLearBtn = findViewById(R.id.clearBtn);
    mSpinner = findViewById(R.id.classes_spinner);
    mRecyclerView = findViewById(R.id.list);
  }

  private void init() {
    initView();
    imm =
        (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

  }

  private void initView() {
    total = SpUtils.INSTANCE.getString("total", getApplicationContext());
    mTotalExpense.setText("¥" + total);
    food = SpUtils.INSTANCE.getString("food", getApplicationContext());
    taobao = SpUtils.INSTANCE.getString("taobao", getApplicationContext());
    traffic = SpUtils.INSTANCE.getString("traffic", getApplicationContext());
    daily = SpUtils.INSTANCE.getString("daily", getApplicationContext());
    other = SpUtils.INSTANCE.getString("other", getApplicationContext());
    Toast.makeText(this,
        "food:" + food + "\n" + "taobao:" + taobao + "\n" + "traffic:" + traffic + "\n" + "daily:" +
            daily + "\n" + "other:" + other, Toast.LENGTH_SHORT).show();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(linearLayoutManager);
    expenseAdapter = new ExpenseAdapter(dataset);
    mRecyclerView.setAdapter(expenseAdapter);

  }


  private void handleClick() {

    mRecordBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (imm != null) {
          mEditText.setVisibility(View.VISIBLE);
          mEnsureBtn.setVisibility(View.VISIBLE);
          mSpinner.setVisibility(View.VISIBLE);
          mEditText.requestFocus();
          imm.showSoftInput(mEditText, 0);
        }
      }
    });

    mEnsureBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        currentNum = mEditText.getText().toString();
        if (Utils.INSTANCE.submitEnabled(currentNum)) {
          total = Utils.INSTANCE.addStrings(total, currentNum);
          SpUtils.INSTANCE.putString("total", total, getApplicationContext());
          Toast.makeText(AccountActivity.this, "cash:" + currentNum + " class: " + currentItem,
              Toast.LENGTH_SHORT).show();
          mTotalExpense.setText("¥" + total);
          SpUtils.INSTANCE.putString(currentItem, currentNum, getApplicationContext());
          String detail = Utils.INSTANCE.getCurrentTime() + " " + currentItem + " ¥" + currentNum;
          SpUtils.INSTANCE.putString("detail",detail, getApplicationContext() );
          dataset.add(detail);
          expenseAdapter.notifyDataSetChanged();
          if (imm != null) {
            mEditText.setText("");
            mEditText.setVisibility(View.INVISIBLE);
            mEnsureBtn.setVisibility(View.INVISIBLE);
            mSpinner.setVisibility(View.INVISIBLE);
            imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
          }
        } else {
          Toast.makeText(AccountActivity.this, "输入不合法，请重新输入", Toast.LENGTH_SHORT).show();
        }

      }
    });

    mCLearBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        total = "0.00";
        mTotalExpense.setText("¥" + total);
        SpUtils.INSTANCE.putString("total", "0.00", getApplicationContext());
        for (int i = 0; i < classes.length; i++) {
          SpUtils.INSTANCE.putString(classes[i], "0.00", getApplicationContext());
        }
      }
    });

    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currentItem = classes[i];

      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
//        currentItem = "food";

      }
    });


  }


}