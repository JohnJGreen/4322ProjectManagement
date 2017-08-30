package cse_4322_13.enb.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // DATABASE

    public static final String DATABASE_NAME = "ClubMeetings.db";

    // TABLES

    public static final String TABLE_CLUBS = "clubs_table";
    public static final String TABLE_EVENTS = "events_table";

    // COLUMNS

    public static final String COL_ID = "ID";
    public static final String COL_NAME = "NAME";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_DATE = "DATE";
    public static final String COL_START_TIME = "START_TIME";
    public static final String COL_END_TIME = "END_TIME";
    public static final String COL_CLUB_NAME = "CLUB_NAME";
    public static final String COL_LOCATION = "LOCATION";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CLUB TABLE CREATION

        db.execSQL("create table " + TABLE_CLUBS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT)");

        //  EVENT TABLE CREATION

        db.execSQL("create table " + TABLE_EVENTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_NAME + " TEXT," +
                COL_DATE + " DATE," +
                COL_LOCATION + " TEXT," +
                COL_DESCRIPTION + " TEXT," +
                COL_CLUB_NAME + " TEXT," +
                COL_START_TIME + " TEXT," +
                COL_END_TIME + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // DROP TABLES

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CLUBS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EVENTS);

        // REBUILD TABLES

        onCreate(db);
    }

}