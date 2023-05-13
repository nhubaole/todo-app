package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class NewActivity extends AppCompatActivity {

    private EditText dateEdt;
    private EditText titleEdt;
    private EditText descEdt;
    private CheckBox doneCbox;
    private Button saveBtn;
    Todo todo;
    int pos = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        titleEdt = (EditText) findViewById(R.id.titleEdt);
        descEdt = (EditText) findViewById(R.id.descEdt);
        doneCbox = (CheckBox) findViewById(R.id.doneCbox);
        dateEdt = (EditText) findViewById(R.id.dateEdt);
        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dateEdt.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                todo = new Todo(titleEdt.getText().toString(), descEdt.getText().toString(), dateEdt.getText().toString(), doneCbox.isChecked());
                bundle.putSerializable("todo", todo);
                bundle.putInt("position", pos);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        try {
            Todo editTodo = (Todo) getIntent().getExtras().get("todo");
            int position = getIntent().getIntExtra("position", -1);
            pos = position;
            titleEdt.setText(editTodo.getTitle());
            descEdt.setText(editTodo.getDescription());
            doneCbox.setChecked(editTodo.isDone());
            dateEdt.setText(editTodo.getDate());
        }
        catch (Exception e){

        }
    }
}