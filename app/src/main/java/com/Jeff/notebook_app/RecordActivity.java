package com.Jeff.notebook_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backHome, delete, saveNote;
    private TextView title, showTime;
    private EditText et_title, et_content;
    private MyDBhelper myDBhelper;
    private String sendId; // 记录修改Id用于区别新建

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        init();

        backHome.setOnClickListener(this);
        delete.setOnClickListener(this);
        saveNote.setOnClickListener(this);

        Intent intent = this.getIntent();
        sendId = intent.getStringExtra("id");
        if (sendId != null) {
            String time = intent.getStringExtra("time");
            String note_title = intent.getStringExtra("title");
            String content = intent.getStringExtra("content");
            title.setText("修改...");
            showTime.setVisibility(View.VISIBLE);
            showTime.setText(time);
            et_title.setText(note_title);
            et_content.setText(content);
        }
    }

    private void init() {
        backHome = findViewById(R.id.backHome);
        delete = findViewById(R.id.delete);
        saveNote = findViewById(R.id.save_note);
        title = findViewById(R.id.title);
        et_title = findViewById(R.id.note_title);
        et_content = findViewById(R.id.note_content);
        showTime = findViewById(R.id.showTime);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.save_note) {
            if (sendId == null) {
                // 新建
                String note_title = et_title.getText().toString();
                String content = et_content.getText().toString();
                if (content == null) {
                    Toast.makeText(RecordActivity.this, "内容不能为空", Toast.LENGTH_LONG).show();
                } else {
                    myDBhelper = new MyDBhelper(RecordActivity.this, "note.db", null, 1);
                    boolean flag = myDBhelper.insertNote(note_title, content);
                    if (flag) {
                        setResult(2);
                        Toast.makeText(RecordActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordActivity.this, "添加失败", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                // 修改
                String note_title = et_title.getText().toString();
                String content = et_content.getText().toString();
                if (content == null) {
                    Toast.makeText(RecordActivity.this, "内容不能为空", Toast.LENGTH_LONG).show();
                } else {
                    myDBhelper = new MyDBhelper(RecordActivity.this, "note.db", null, 1);
                    boolean flag = myDBhelper.updateNote(sendId, note_title, content);
                    if (flag) {
                        setResult(2);
                        Toast.makeText(RecordActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RecordActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                }
            }

        } else if (view.getId() == R.id.backHome) {
            finish();
        } else if (view.getId() == R.id.delete) {
            et_title.setText("");
            et_content.setText("");
        }

    }
}