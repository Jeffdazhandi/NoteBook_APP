package com.Jeff.notebook_app;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class MyDBhelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;

    public MyDBhelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE note (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,title TEXT,content TEXT,note_time TEXT);");

    }

    public boolean insertNote(String title, String content) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();

        String finalDate = sdf.format(date);
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("note_time", finalDate);
        long i = db.insert("note", null, contentValues);

        return i > 0;
    }

    public boolean deleteNote(String id) {
        int i = db.delete("note", "id=?", new String[]{id});

        return i > 0;
    }

    public boolean updateNote(String id, String title, String content) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = sdf.format(date);
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        contentValues.put("note_time", time);
        int i = db.update("note", contentValues, "id=?", new String[]{id});

        return i > 0;
    }

    public List<Note> queryNotes(){
        List<Note> list = new ArrayList<>();
        Cursor cursor = db.query("note", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            Note note = new Note();
            note.setId(String.valueOf(cursor.getInt(0)));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setNote_time(cursor.getString(3));
            list.add(note);
        }
        return list;
    }
    public List<Note> searchNotes(String key){
        List<Note> list = new ArrayList<>();
        db = this.getReadableDatabase(); // 获取可读的数据库引用

        // 构建查询条件，使用LIKE运算符进行模糊匹配
        String selection = "title LIKE ? OR content LIKE ?";
        String[] selectionArgs = new String[]{"%" + key + "%", "%" + key + "%"};

        // 查询数据库
        Cursor cursor = db.query("note", null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()){
            Note note = new Note();
            note.setId(String.valueOf(cursor.getInt(0)));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setNote_time(cursor.getString(3));
            list.add(note);
        }
//
//        // 关闭cursor和数据库连接
//        cursor.close();
//        db.close();

        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
