package androidexample.com.deadlike;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Console;
import java.io.FileInputStream;
import java.util.Date;
import java.util.logging.ConsoleHandler;

public class OutputActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_output);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DeadlineAdapter adapter = new DeadlineAdapter(this, R.layout.deadline_layout, Deadline.getDeadlineList());

        ListView deadlineListView = (ListView) findViewById(R.id.deadlineListView);

        deadlineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                /*Deadline d = Deadline.getDeadlineList().get(position);
                if(d == null)
                    return;
                adapter.remove(d);
                d.removeDeadline();*/

                Intent intent = new Intent(MyApplication.getContext(), ShowInfo_Activity.class);
                intent.putExtra("pos", position);

                startActivity(intent);
                finish();

            }
        });

        deadlineListView.setAdapter(adapter);

    }



}