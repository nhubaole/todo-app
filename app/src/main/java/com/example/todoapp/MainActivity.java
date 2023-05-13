package com.example.todoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvTodo;
    List<Todo> TodoList = new ArrayList<>();
    TodoAdapter todoAdapter;
    ActivityResultLauncher<Intent> launcherNewTodo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvTodo = (ListView) findViewById(R.id.lvTodo);
        TodoList.add(new Todo("Chay deadline", "dl todo app", "17/5/2023", false));
        todoAdapter = new TodoAdapter(this, R.layout.list_item_todo, TodoList);
        lvTodo.setAdapter(todoAdapter);

        launcherNewTodo = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            Todo todo = (Todo) intent.getExtras().get("todo");
                            TodoList.add(todo);
                            todoAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.newOption){
            launcherNewTodo.launch(new Intent(MainActivity.this, NewActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}