package com.Jeff.notebook_app;

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

public class SearchActivity extends AppCompatActivity {
    private ListView listView;
    private ImageView check, backHome;
    private EditText searchText;
    private String key;
    private List<Note> resuList;
    private MyDBhelper myDBhelper;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = this.getIntent();
        key = intent.getStringExtra("key");

        listView = (ListView) findViewById(R.id.listview);
        searchText = (EditText) findViewById(R.id.search_text);
        check = (ImageView) findViewById(R.id.check);
        backHome = (ImageView) findViewById(R.id.backHome);

        init();

        backHome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            // 再次检索
            @Override
            public void onClick(View v) {
                key = searchText.getText().toString();
                init();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 点击进行修改
                Note note = (Note) adapterView.getAdapter().getItem(position);
                Intent intent = new Intent(SearchActivity.this, RecordActivity.class);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("删除笔记")
                        .setMessage("确认要删除该条笔记吗？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note note = (Note) myAdapter.getItem(position);
                                String deleteId = note.getId();
                                if (myDBhelper.deleteNote(deleteId)) {
                                    init();
                                    Toast.makeText(SearchActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SearchActivity.this, "删除失败", Toast.LENGTH_LONG).show();
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
        myDBhelper = new MyDBhelper(SearchActivity.this, "note.db", null, 1);
        resuList = myDBhelper.searchNotes(key);

        myAdapter = new MyAdapter(resuList, SearchActivity.this);
        listView.setAdapter(myAdapter);
    }
}