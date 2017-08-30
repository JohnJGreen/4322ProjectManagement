package cse_4322_13.enb.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventsDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COL_ID, DatabaseHelper.COL_NAME,
            DatabaseHelper.COL_DATE, DatabaseHelper.COL_LOCATION,
            DatabaseHelper.COL_DESCRIPTION, DatabaseHelper.COL_CLUB_NAME,
            DatabaseHelper.COL_START_TIME, DatabaseHelper.COL_END_TIME};

    // instantiate helper

    public EventsDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // open database

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // close helper

    public void close() {
        dbHelper.close();
    }

    // create event

    public Event createEvents(String name, String description, String date, String location,
                              String clubName, String startTime, String endTime) {
        // set event values
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME, name);
        values.put(DatabaseHelper.COL_DESCRIPTION, description);
        values.put(DatabaseHelper.COL_DATE, date);
        values.put(DatabaseHelper.COL_LOCATION, location);
        values.put(DatabaseHelper.COL_CLUB_NAME, clubName);
        values.put(DatabaseHelper.COL_START_TIME, startTime);
        values.put(DatabaseHelper.COL_END_TIME, endTime);
        // add event to database
        long insertId = database.insert(DatabaseHelper.TABLE_EVENTS, null,
                values);
        // add event to cursor
        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS,
                allColumns, DatabaseHelper.COL_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        // create event object from cursor
        Event newEvents = cursorToEvents(cursor);
        cursor.close();
        return newEvents;
    }

    // update an event in the database

    public boolean updateEvents(Event Events) {
        // set event values
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAME, Events.getName());
        values.put(DatabaseHelper.COL_DESCRIPTION, Events.getDescription());
        values.put(DatabaseHelper.COL_DATE, Events.getDate());
        values.put(DatabaseHelper.COL_LOCATION, Events.getLocation());
        values.put(DatabaseHelper.COL_CLUB_NAME, Events.getClubName());
        values.put(DatabaseHelper.COL_START_TIME, Events.getStartTime());
        values.put(DatabaseHelper.COL_END_TIME, Events.getEndTime());
        // return true if update was successful
        return database.update(DatabaseHelper.TABLE_EVENTS, values, DatabaseHelper.COL_ID + "=" + Events.getID(), null) > 0;
    }

    // Delete event by ID

    public void deleteEvents(long id) {
        database.delete(DatabaseHelper.TABLE_EVENTS, DatabaseHelper.COL_ID
                + " = " + id, null);
    }

    // Pull all events from table and add to a list

    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> EventsList;
        EventsList = new ArrayList<>();

        // add query to cursor
        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        // traverse with cursor adding events to list
        while (!cursor.isAfterLast()) {
            Event Events = cursorToEvents(cursor);
            EventsList.add(Events);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return EventsList;
    }

    public ArrayList<Event> getAllEventsForClub(String clubName) {
        ArrayList<Event> EventsList;
        EventsList = new ArrayList<>();

        // add query to cursor
        Cursor cursor = database.query(DatabaseHelper.TABLE_EVENTS,
                allColumns, DatabaseHelper.COL_CLUB_NAME + "=?", new String[] {clubName}, null, null, null);

        cursor.moveToFirst();
        // traverse with cursor adding events to list
        while (!cursor.isAfterLast()) {
            Event Events = cursorToEvents(cursor);
            EventsList.add(Events);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return EventsList;
    }

    // get all club names

    public ArrayList<String> getAllClubs() {
        ArrayList<String> Clubs;

        Clubs = new ArrayList<>();

        // add query to cursor
        Cursor cursor = database.query(true,DatabaseHelper.TABLE_EVENTS,
                new String[]{DatabaseHelper.COL_CLUB_NAME}, null, null, DatabaseHelper.COL_CLUB_NAME, null, null,null);

        cursor.moveToFirst();
        // traverse with cursor adding events to list
        while (!cursor.isAfterLast()) {
            Clubs.add(cursor.getString(0));
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return Clubs;
    }

    // build event object from cursor

    private Event cursorToEvents(Cursor cursor) {
        Event Events = new Event();
        Events.setID(cursor.getLong(0));
        Events.setName(cursor.getString(1));
        Events.setDate(cursor.getString(2));
        Events.setLocation(cursor.getString(3));
        Events.setDescription(cursor.getString(4));
        Events.setClubName(cursor.getString(5));
        Events.setStartTime(cursor.getString(6));
        Events.setEndTime(cursor.getString(7));
        return Events;
    }
}
