package com.example.todoapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvTodo;
    List<Todo> TodoList = new ArrayList<>();
    TodoAdapter todoAdapter;
    ActivityResultLauncher<Intent> launcherNewTodo;
    ActivityResultLauncher<Intent> launcherEditTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvTodo = (ListView) findViewById(R.id.lvTodo);
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

        registerForContextMenu(lvTodo);

        launcherEditTodo = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            Todo todo = (Todo) intent.getExtras().get("todo");
                            int position = intent.getIntExtra("position", -1);
                            if (position != -1 && todo != null) {
                                TodoList.set(position, todo);
                                todoAdapter.notifyDataSetChanged();
                            }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvTodo) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.editOption:
                Intent intent =new Intent(MainActivity.this, NewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("todo", TodoList.get(menuInfo.position));
                bundle.putInt("position", menuInfo.position);
                intent.putExtras(bundle);
                launcherEditTodo.launch(intent);
                break;
            case R.id.deleteOption:
                AlertDialog confirmDialog = new AlertDialog.Builder(this)
                        .setTitle("Xóa việc cần làm")
                        .setMessage("Bạn có chắc chắn muốn xóa việc cần làm này?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                TodoList.remove(menuInfo.position);
                                todoAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                confirmDialog.show();
                break;
            case R.id.markOption:
                Todo todo = TodoList.get(menuInfo.position);
                todo.setDone(true);
                todoAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }
}