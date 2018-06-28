package androidexample.com.deadlike;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Hung on 3/27/2018.
 */
public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, NotificationIntentService.class);
        intent1.setAction(intent.getAction());
        intent1.setType("text/plain");
        intent1.putExtra("pos", intent.getIntExtra("pos", 0));
        context.startService(intent1);
    }
}
