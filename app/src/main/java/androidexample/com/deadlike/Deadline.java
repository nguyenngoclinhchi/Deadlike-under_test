package androidexample.com.deadlike;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Hung on 3/26/2018.
 */
public class Deadline {
    public String deadlineName;
    public Date end;
    public String subjectName;
    public String description;

    public Date remindBefore;

    private static Calendar cal = MyApplication.cal;

    private static ArrayList<Deadline> deadlineList = new ArrayList<Deadline>();
    private int noti_id;

    private static final String pattern = new String("dd/MM/yyyy");
    private final int MAX_SUBJECT_NAME  = 50;
    private final int MAX_DEADLINE_NAME = 50;
    private final int MAX_DESCRIPTION   = 80;


    private static String CUSTOM_ACTION = "androidexample.com.deadlike.DEADLINE_NOTI";


    public static ArrayList<Deadline> getDeadlineList(){
        return deadlineList;
    }

    private Deadline(){};

    public Deadline(Date end, String subjectName,
                     String deadlineName, Date remindBefore, String description){
        this.end = end;
        this.subjectName = subjectName;
        this.deadlineName = deadlineName;

        this.description = description;

        this.remindBefore = remindBefore;
        if(deadlineList == null){
            deadlineList = new ArrayList<Deadline>();
        }
        addToDeadlineList(this);
        noti_id = MyApplication.NOTI_ID;
        MyApplication.NOTI_ID++;
        setNotification();
    }

    public Deadline(Date end, String subjectName, String deadlineName, Date remindBefore){
        this(end, subjectName, deadlineName, remindBefore, "");
    }

    private void addToDeadlineList(Deadline curDeadline){
        //Sap xep theo thu tu thoi gian, cang som thi dung truoc

        if(deadlineList.isEmpty()){
            deadlineList.add(curDeadline);
            return;
        }
        for (int i = 0; i < deadlineList.size(); i++) {
            Deadline deadline = deadlineList.get(i);
            if(deadline.end.compareTo(curDeadline.end) > 0){
                deadlineList.add(i, curDeadline);
                return;
            }
        }

            deadlineList.add(curDeadline);
    }

    public String getEndDate(){

        DateFormat df = new SimpleDateFormat(pattern);

        String endDay = df.format(end);
        return endDay;
    }

    private void writeToFile(FileOutputStream outputStream){
        try{
            byte[] buffer = deadlineName.getBytes();

            int len = buffer.length;
            outputStream.write(buffer, 0, len);
            for(int i = 0; i < MAX_DEADLINE_NAME - len + 1; i++){
                      outputStream.write(0);
            }


            buffer = new byte[1];
            cal.setTime(end);
            int value = cal.get(Calendar.DAY_OF_MONTH);
            buffer[0] = (byte) (value);
            outputStream.write(buffer);


            value = cal.get(Calendar.MONTH);
            buffer[0] = (byte) (value);
            outputStream.write(buffer);

            value = cal.get(Calendar.YEAR);
            buffer[0] = (byte) (value - 1900);
            outputStream.write(buffer);

            value = cal.get(Calendar.HOUR_OF_DAY);
            buffer[0] = (byte) (value);
            outputStream.write(buffer);

            value = cal.get(Calendar.MINUTE);
            buffer[0] = (byte) (value);
            outputStream.write(buffer);

            buffer = subjectName.getBytes();
            len = buffer.length;
            outputStream.write(buffer, 0, len);
            for(int i = 0; i < MAX_SUBJECT_NAME - len; i++){
                outputStream.write(0);
            }

            cal.setTime(remindBefore);


            value = cal.get(Calendar.DAY_OF_MONTH);
            buffer[0] = (byte) (value);
            outputStream.write(buffer[0]);

            value = cal.get(Calendar.HOUR_OF_DAY);
            buffer[0] = (byte) (value);
            outputStream.write(buffer[0]);

            value = cal.get(Calendar.MINUTE);
            buffer[0] = (byte) (value);
            outputStream.write(buffer[0]);

            buffer = description.getBytes();
            len = buffer.length;
            outputStream.write(buffer, 0, len);
            for(int i = 0; i < MAX_DESCRIPTION - len; i++){
                outputStream.write(0);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void updateDeadlineList(Context context, String filename){


        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            for(Deadline d : Deadline.getDeadlineList()){
                d.writeToFile(outputStream);
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public static void updateDeadlineList(Context context){
        updateDeadlineList(context, "deadlist.dat");
    }
    public int getPos(){
        return deadlineList.indexOf(this);

    }

    private void setNotification(){
        Deadline d = this;
        Context context = MyApplication.getContext();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(Integer.toString(d.noti_id));

        intent.setType("text/plain");
        intent.putExtra("pos", d.getPos());

        Date remindTime = getRemindTime();
        cal.setTime(remindTime);

        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, d.noti_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);

    }
    private Date getRemindTime(){
        cal.setTime(this.end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(this.remindBefore);
        //Bad code
        cal.add(Calendar.DAY_OF_MONTH, -temp.get(Calendar.DAY_OF_YEAR));
        cal.add(Calendar.HOUR_OF_DAY, -temp.get(Calendar.HOUR_OF_DAY));
        cal.add(Calendar.MINUTE, -temp.get(Calendar.MINUTE));

        return cal.getTime();
    }
    public int getNoti_ID(){
        return noti_id;
    }

    public void removeDeadline(){
        Deadline d = this;

        Context context = MyApplication.getContext();
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(Integer.toString(d.noti_id));

        intent.setType("text/plain");
        intent.putExtra("pos", d.getPos());

        Date remindTime = getRemindTime();
        cal.setTime(remindTime);

        PendingIntent.getBroadcast
                (context, noti_id, intent, PendingIntent.FLAG_UPDATE_CURRENT).cancel();

        deadlineList.remove(this);

        updateDeadlineList(context);


    }
}
