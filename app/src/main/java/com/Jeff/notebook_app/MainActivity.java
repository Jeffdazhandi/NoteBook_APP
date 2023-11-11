package com.Jeff.notebook_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView add, check;
    private MyDBhelper myDBhelper;
    private MyAdapter myAdapter;
    private List<Note> resuList;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        searchText = (EditText) findViewById(R.id.search_text);
        check = (ImageView) findViewById(R.id.check);
        add = (ImageView) findViewById(R.id.add);

        check.setOnClickListener(new View.OnClickListener() {
            // 根据输入内容跳转到SearchActivity检索笔记
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                String key = searchText.getText().toString();
                intent.putExtra("key", key);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            // 点击添加按钮跳转到RecordActivity
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        init();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 点击进行修改
                Note note = (Note) adapterView.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                String noteId = note.getId();
                String title = note.getTitle();
                String content = note.getContent();
                String time = note.getNote_time();
                intent.putExtra("id", noteId);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("time", time);
                startActivityForResult(intent, 1);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            // 长按触发删除对话框
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog dialog = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("删除笔记")
                        .setMessage("确认要删除该条笔记吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note note = (Note) myAdapter.getItem(position);
                                String deleteId = note.getId();
                                if (myDBhelper.deleteNote(deleteId)) {
                                    init();
                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog = builder.create();
                dialog.show();

                return true;
            }
        });
    }

    private void init() {
        // 刷新列表
        if (resuList != null) {
            resuList.clear();
        }
        myDBhelper = new MyDBhelper(MainActivity.this, "note.db", null, 1);
        resuList = myDBhelper.queryNotes();

        myAdapter = new MyAdapter(resuList, MainActivity.this);
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            init();
        }
    }
}