package androidexample.com.deadlike;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.lang.Integer;

public class InputActivity extends AppCompatActivity {
    private View dateLayout;
    private EditText deadlineName_editText, subjectName_editText, description_editText,
            dd_editText, mm_editText, yyyy_editText, hh_editText, min_editText,
            ddRemind_editText, hhRemind_editText, minRemind_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        init();

        Intent intent = getIntent();
        final int pos = intent.getIntExtra("pos", -1);
        if(pos == -1){
            MyApplication.setDateText(Calendar.getInstance().getTime(), dateLayout);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else{
            final Deadline d = Deadline.getDeadlineList().get(pos);
            setText(d);
            Button submit  = (Button) findViewById(R.id.submitInput_Button);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editSubmit(v, d);
                    Intent intent = new Intent(InputActivity.this, ShowInfo_Activity.class);
                    intent.putExtra("pos", pos);
                    startActivity(intent);
                    finish();
                }
            });
            setTitle("Edit Deadline");
        }

    }

    private void init(){
        dateLayout = findViewById(R.id.dateLayout_input);
        deadlineName_editText = (EditText) findViewById(R.id.deadlineName_editText);
        subjectName_editText = (EditText) findViewById(R.id.subjectName_editText);
        description_editText = (EditText) findViewById(R.id.description_EditText);
        dd_editText = (EditText) dateLayout.findViewById(R.id.dd_editText);
        mm_editText = (EditText) dateLayout.findViewById(R.id.mm_editText);
        yyyy_editText = (EditText) dateLayout.findViewById(R.id.yyyy_editText);
        hh_editText = (EditText) dateLayout.findViewById(R.id.hh_editText);
        min_editText = (EditText) dateLayout.findViewById(R.id.min_editText);
        View remindBefore = findViewById(R.id.remindLayout_input);
        ddRemind_editText = (EditText) remindBefore.findViewById(R.id.ddRemind_editText);
        hhRemind_editText = (EditText) remindBefore.findViewById(R.id.hhRemind_editText);
        minRemind_editText = (EditText) remindBefore.findViewById(R.id.minRemind_editText);


    }

    private void setText(Deadline d){
        deadlineName_editText.setText(d.deadlineName);
        subjectName_editText.setText(d.subjectName);
        description_editText.setText(d.description);
        MyApplication.setDateText(d.end, dateLayout);
        MyApplication.cal.setTime(d.remindBefore);
        ddRemind_editText.setText(String.valueOf(MyApplication.cal.get(Calendar.DAY_OF_YEAR)));
        hhRemind_editText.setText(String.valueOf(MyApplication.cal.get(Calendar.HOUR_OF_DAY)));
        minRemind_editText.setText(String.valueOf(MyApplication.cal.get(Calendar.MINUTE)));

    }
    public void submit(View view){

        Date end = getDate();
        if(end == null){
            MyApplication.printWarning("Invalid Date");
            return;
        }


        if(deadlineName_editText.getText().toString().isEmpty()){
            MyApplication.printWarning("You haven't entered deadline's name");
            return;
        }

        if(deadlineName_editText.getText().toString().isEmpty()){
            MyApplication.printWarning("You haven't entered deadline's name");
            return;
        }

        Date remindBefore = getRemindBefore();
        if(remindBefore == null){
            MyApplication.printWarning("Invalid remind before time");
            return;
        }

        Deadline newDeadline = new Deadline(
            end, subjectName_editText.getText().toString(),
                deadlineName_editText.getText().toString(),
                remindBefore, description_editText.getText().toString()
        );
        Deadline.updateDeadlineList(this);
        //---------------------------------------------------
        Intent intent = new Intent(this, OutputActivity.class);
        startActivity(intent);
        finish();
    }


    public void editSubmit(View v, Deadline d){
        Date end = getDate();
        if(end == null){
            MyApplication.printWarning("Invalid Date");
            return;
        }


        if(deadlineName_editText.getText().toString().isEmpty()){
            MyApplication.printWarning("You haven't entered deadline's name");
            return;
        }

        if(deadlineName_editText.getText().toString().isEmpty()){
            MyApplication.printWarning("You haven't entered deadline's name");
            return;
        }

        Date remindBefore = getRemindBefore();
        if(remindBefore == null){
            MyApplication.printWarning("Invalid remind before time");
            return;
        }



        d.deadlineName = deadlineName_editText.getText().toString();
        d.subjectName = subjectName_editText.getText().toString();
        d.description = description_editText.getText().toString();
        d.end = end;
        d.remindBefore = remindBefore;
    }


    private Date getDate(){
        int dd, mm, yyyy, hh, min;
        Date end;
        try{
            dd = Integer.parseInt(dd_editText.getText().toString());
            mm = Integer.parseInt(mm_editText.getText().toString());
            yyyy = Integer.parseInt(yyyy_editText.getText().toString());
            hh = Integer.parseInt(hh_editText.getText().toString());
            min = Integer.parseInt(min_editText.getText().toString());
            if(!isValidDate(dd, mm, yyyy, hh, min)){
                return null;
            }
            end = new Date(yyyy - 1900, mm - 1, dd, hh, min);

            return end;
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return null;

    }

    private Date getRemindBefore(){
        int dd, hh, min;
        Date before;
        try{
            dd = Integer.parseInt(ddRemind_editText.getText().toString());
            hh = Integer.parseInt(hhRemind_editText.getText().toString());
            min = Integer.parseInt(minRemind_editText.getText().toString());

            before = new Date(0, 0 , dd, hh, min);

            return before;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private boolean isValidDate(int day, int month, int year, int hh, int min){
        if(hh > 24 || hh < 0)
            return false;
        if(min > 60 || min < 0)
            return false;
        month = month+1;
        if (day <= 0 || month <= 0 || month > 12 || year < 0) return false;

        if ((year % 4 == 0 || year % 400 == 0) && month==2){
            if (day > 29) return false;
        }

        else if (month == 2) if (day >28) return false;

        if (month == 1|| month == 3|| month == 5|| month == 7|| month == 8|| month == 10|| month == 12) {
            if (day >31) return false;
        }

        if (month == 4|| month == 6|| month == 9|| month==11){
            if (day > 30) return false;
        }

        return true;
    }
}
