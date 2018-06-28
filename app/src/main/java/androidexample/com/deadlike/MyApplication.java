package androidexample.com.deadlike;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hung on 3/27/2018.
 */
public class MyApplication extends Application {

    public final int MAX_SUBJECT_NAME = 50;
    public final int MAX_DEADLINE_NAME = 50;
    private final int MAX_DESCRIPTION   = 80;
    public static int NOTI_ID = 1;
    public static Calendar cal;
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }


    @Override
    public void onCreate(){
        cal = Calendar.getInstance();
        instance = this;
        super.onCreate();
        loadDeadlineList();
    }

    private void loadDeadlineList(){
        FileInputStream in;
        String filename = "deadlist.dat";
        try{
            in = openFileInput(filename);
            readFromFile(in);
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readFromFile(FileInputStream inputStream){
        int bufferSize = MAX_DEADLINE_NAME + MAX_SUBJECT_NAME + 9 + MAX_DESCRIPTION;

        byte[] buffer = new byte[bufferSize];
        int count = 0;
        String deadlineName, description,
                subjectName;
        int dd, mm, yyyy, hh, min, remind_dd, remind_hh, remind_min;
        try{
            while(inputStream.available() > 0){
                inputStream.read(buffer, 0, bufferSize);
                deadlineName = new String(buffer, 0, MAX_DEADLINE_NAME).trim();

                int t = MAX_DEADLINE_NAME+1;

                dd = buffer[t];
                t+=1;

                mm = buffer[t];
                t+=1;

                yyyy = buffer[t];
                t+=1;

                hh = buffer[t];
                t+=1;

                min = buffer[t];
                t+=1;

                subjectName = new String(buffer, t, MAX_SUBJECT_NAME).trim();

                t += MAX_SUBJECT_NAME;

                remind_dd = buffer[t++];
                remind_hh = buffer[t++];
                remind_min = buffer[t++];

                description = new String(buffer, t, MAX_DESCRIPTION).trim();

                Deadline temp = new Deadline(new Date(yyyy, mm,dd, hh, min), subjectName,
                        deadlineName, new Date(0, 0, remind_dd, remind_hh, remind_min), description);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void setDateText(Date date, View v){
        if(cal == null)
            cal = Calendar.getInstance();

        EditText dd_editText    = (EditText)   v.findViewById(R.id.dd_editText);
        EditText mm_editText    = (EditText)   v.findViewById(R.id.mm_editText);
        EditText yyyy_editText  = (EditText) v.findViewById(R.id.yyyy_editText);
        EditText hh_editText    = (EditText)   v.findViewById(R.id.hh_editText);
        EditText min_editText   = (EditText)  v.findViewById(R.id.min_editText);

        cal.setTime(date);

        dd_editText.setText(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
        mm_editText.setText(Integer.toString(cal.get(Calendar.MONTH) + 1));
        yyyy_editText.setText(Integer.toString(cal.get(Calendar.YEAR)));
        hh_editText.setText(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
        min_editText.setText(Integer.toString(cal.get(Calendar.MINUTE)));
    }

    public static void printWarning(String message){
        Toast.makeText(MyApplication.getContext(), message, Toast.LENGTH_SHORT).show();    }


}

