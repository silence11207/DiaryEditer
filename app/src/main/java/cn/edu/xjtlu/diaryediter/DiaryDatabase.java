package cn.edu.xjtlu.diaryediter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DiaryDatabase extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "diaries";
    public static final String ID = "_id";
    public static final String CONTENT = "content";
    public static final String TIME = "time";
    public static final String MODE = "mode";


    public DiaryDatabase(@Nullable Context context) {
        super(context, "diaries", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME
                +"("
                +ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                +CONTENT + " TEXT NOT NULL,"
                +TIME + "TEXT NOT NULL,"
                +MODE + " INTEGER DEFAULT 1)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
