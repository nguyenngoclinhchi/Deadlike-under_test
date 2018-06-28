package androidexample.com.deadlike;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Hung on 3/27/2018.
 */
public class NotificationIntentService extends IntentService {

    public NotificationIntentService(){
        super("NotificationIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        /*
        intent.putExtra("singleImage", uri.toString());
        Uri myUri = Uri.parse(extras.getString("singleImage"));
        * */

        int pos = intent.getIntExtra("pos", 0);
        Deadline d = Deadline.getDeadlineList().get(pos);
        String deadlineName = d.deadlineName;
        String subjectName =  d.subjectName;
        String deadtime = d.getEndDate();

        builder.setContentTitle(deadlineName);
        builder.setContentText(subjectName.concat("\n").concat(deadtime));


        builder.setSmallIcon(R.mipmap.ic_launcher);



        Intent notifyIntent = new Intent(this, ShowInfo_Activity.class);
        notifyIntent.putExtra("pos", pos);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, d.getNoti_ID(), notifyIntent, PendingIntent.FLAG_ONE_SHOT);

        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();

        notificationCompat.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationCompat.priority = Notification.PRIORITY_MAX;


        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(d.getNoti_ID(), notificationCompat);

    }
}
