package com.team11.cse4322.redalertuvnotifications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AsyncResponse{

    //button listener
    Button  uvButton;

    //zip code from edit text
    EditText uvEdit;

    //text view to paste json
    TextView uvData;

    UV_Lookup uvLookup;

    Button showNotificationBut, stopNotificationBut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find Button for uv search
        uvButton = (Button)findViewById(R.id.button);

        // find text from uv edit text
        uvEdit = (EditText)findViewById(R.id.zipTextField);

        // text view to paste json
        uvData = (TextView)findViewById(R.id.uvJsonItem);

        showNotificationBut = (Button) findViewById(R.id.showNotificationBut);

        stopNotificationBut = (Button) findViewById(R.id.stopNotificationBut);

        //alertButton = (Button) findViewById(R.id.alertButton);
        // store it to a file

        // on clicking find uv button, app will go to the url provided and grab the json with the help of the UV_Lookup class
        uvButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        uvLookup = new UV_Lookup();
                        uvLookup.data = MainActivity.this;
                        uvLookup.execute("https://iaspub.epa.gov/enviro/efservice/getEnvirofactsUVDAILY/ZIP/"+ uvEdit.getText().toString() +"/JSON");

                    }
                });
    }

    @Override
    public void processFinish(String output) {
        uvData.setText(output);
    }


    public void showNotification(View view) {

        AlertReceiver.setupAlarm(getApplicationContext());

    }

    public void stopNotification(View view) {
            // to do
        }

}

