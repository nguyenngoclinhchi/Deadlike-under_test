package androidexample.com.deadlike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class ShowInfo_Activity extends AppCompatActivity {

    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        showDeadlineInfo(pos);
    }

    private void showDeadlineInfo(int pos){
        final Deadline d = Deadline.getDeadlineList().get(pos);
        setTitle(d.deadlineName);
        final View dateLayout_info = findViewById(R.id.dateLayout_info);
        final EditText subjectName_editText= (EditText) findViewById(R.id.subjectName_info_editText);
        final EditText description_editText = (EditText) findViewById(R.id.description_info_editText);
        subjectName_editText.setText(d.subjectName);
        subjectName_editText.setEnabled(false);


        if(!d.description.isEmpty())
        {
            description_editText.setText(d.description);
            description_editText.setEnabled(false);
        }

        Button deleteButton = (Button) findViewById(R.id.delete_button);
        if (deleteButton != null) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    d.removeDeadline();
                    Intent intent = new Intent(ShowInfo_Activity.this, OutputActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        setRemindBeforeText(d);

        MyApplication.setDateText(d.end, dateLayout_info);
        EditText dd_editText    = (EditText)   dateLayout_info.findViewById(R.id.dd_editText);
        EditText mm_editText    = (EditText)   dateLayout_info.findViewById(R.id.mm_editText);
        EditText yyyy_editText  = (EditText)   dateLayout_info.findViewById(R.id.yyyy_editText);
        EditText hh_editText    = (EditText)   dateLayout_info.findViewById(R.id.hh_editText);
        EditText min_editText   = (EditText)   dateLayout_info.findViewById(R.id.min_editText);
        dd_editText.setEnabled(false);
        mm_editText.setEnabled(false);
        yyyy_editText.setEnabled(false);
        hh_editText.setEnabled(false);
        min_editText.setEnabled(false);
    }

    private void setRemindBeforeText(Deadline d){
        View v = findViewById(R.id.remindBefore_layout_showInfo);
        EditText hh = (EditText) v.findViewById(R.id.hhRemind_editText);
        EditText dd = (EditText) v.findViewById(R.id.ddRemind_editText);
        EditText min = (EditText) v.findViewById(R.id.minRemind_editText);
        Calendar cal = MyApplication.cal;
        cal.setTime(d.remindBefore);
        hh.setText(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
        dd.setText(String.valueOf(cal.get(Calendar.DAY_OF_YEAR)));
        min.setText(String.valueOf(cal.get(Calendar.MINUTE)));

        hh.setEnabled(false);
        dd.setEnabled(false);
        min.setEnabled(false);

    }


    public void editMode(View v){
        Intent intent = new Intent(this, InputActivity.class);
        intent.putExtra("pos", pos);
        startActivity(intent);
    }

}
