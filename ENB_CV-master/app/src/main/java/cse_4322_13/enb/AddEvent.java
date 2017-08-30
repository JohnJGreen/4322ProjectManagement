package cse_4322_13.enb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import cse_4322_13.enb.Database.EventsDataSource;

public class AddEvent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public void returnFunction(View view) {

        setResult(2);
        finish();
    }

    public void createEventFunction(View view) {

        // grab fields
        EditText name = (EditText) findViewById(R.id.eventTitleEditText);
        EditText clubName = (EditText) findViewById(R.id.clubNameEditText);
        DatePicker datePicker = (DatePicker) findViewById(R.id.eventDatePicker);
        EditText location = (EditText) findViewById(R.id.locationEditText);
        EditText description = (EditText) findViewById(R.id.descriptionEditText);
        EditText startTime = (EditText) findViewById(R.id.startTimeEditText);
        EditText endTime = (EditText) findViewById(R.id.endTimeEditText);

        // convert date to sql formatting
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        String date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);

        // create the intent
        Intent intent = new Intent();

        // pass data with the intent
        intent.putExtra("name", name.getText().toString());
        intent.putExtra("clubName", clubName.getText().toString());
        intent.putExtra("date", date);
        intent.putExtra("location", location.getText().toString());
        intent.putExtra("description", description.getText().toString());
        intent.putExtra("startTime", startTime.getText().toString());
        intent.putExtra("endTime", endTime.getText().toString());
        // execute the intent
        setResult(1, intent);
        finish();
    }
}
