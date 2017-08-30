package cse_4322_13.enb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import cse_4322_13.enb.Database.Event;
import cse_4322_13.enb.Database.EventsDataSource;

public class MainActivity extends AppCompatActivity {

    private EventsDataSource dataSource;
    private int position;
    Button createEventAdButton;
    ListView eventListView;
    EditText editText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        dataSource = new EventsDataSource(this);
        try {
            dataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }




        final TabHost tabs=(TabHost)findViewById(R.id.tabhost);

        tabs.setup();

        Button btn = new Button(MainActivity.this);
        btn.setText("Create Event");
        btn.setId(Integer.parseInt("3"));
        //btn.setOnClickListener(createEventAddFunction(MainActivity.this));

        // Create a new tab with a listview populated from the db

        ArrayList<String> clubList;
        clubList = dataSource.getAllClubs();

        for (final String club : clubList) {
            TabHost.TabSpec spec=tabs.newTabSpec(club);

            final ListView ls1 = new ListView(MainActivity.this);

            spec.setContent(new TabHost.TabContentFactory() {
                public View createTabContent(String tag)
                {
                    final ArrayList<Event> eventsList = dataSource.getAllEventsForClub(club);
                    final ArrayAdapter<Event> adp = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,eventsList);
                    ls1.setAdapter(adp);
                    ls1.setOnItemLongClickListener(
                            new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(final AdapterView<?> adapter,
                                                               View item, int pos, long id) {
                                    position = pos;
                                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                    alert.setTitle("DELETE");
                                    alert.setMessage("Are you sure to delete record");
                                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dataSource.deleteEvents(eventsList.get(position).getID());
                                            eventsList.remove(position);
                                            adp.notifyDataSetChanged();
                                            dialog.dismiss();
                                            MainActivity.this.recreate();

                                        }
                                    });
                                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    });

                                    alert.show();
                                    return true;
                                }

                            }
                    );

                    ls1.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                                    position = pos;
                                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                    alert.setTitle(eventsList.get(position).getName());
                                    String text = eventsList.get(position).getClubName()+" event -"+
                                            "\nDate: "+eventsList.get(position).getDate()+
                                            "\nStart: "+eventsList.get(position).getStartTime()+
                                            "\nEnd: "+eventsList.get(position).getEndTime()+
                                            "\nAt "+eventsList.get(position).getLocation()+
                                            "\n"+eventsList.get(position).getDescription();
                                    alert.setMessage(text);
                                    alert.setNeutralButton("Close", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();
                                        }
                                    });
                                    alert.show();
                                }
                            }
                    );

                    return ls1;
                }
            });
            spec.setIndicator(club);
            tabs.addTab(spec);
        }

//        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String eventPicked = "Selected " +
//                        String.valueOf(parent.getItemAtPosition(position));
//
//                Toast.makeText(MainActivity.this,eventPicked,Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.exit_app){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createEventAddFunction(View view) {

        Intent go = new Intent(this, AddEvent.class);
        int Result = 1;
        startActivityForResult(go, Result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Event event = dataSource.createEvents(data.getStringExtra("name"),
                    data.getStringExtra("description"),
                    data.getStringExtra("date"),
                    data.getStringExtra("location"),
                    data.getStringExtra("clubName"),
                    data.getStringExtra("startTime"),
                    data.getStringExtra("endTime"));
            MainActivity.this.recreate();
        }
    }
}