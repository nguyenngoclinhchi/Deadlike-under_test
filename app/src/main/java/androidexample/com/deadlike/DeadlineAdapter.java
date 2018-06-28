package androidexample.com.deadlike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hung on 3/26/2018.
 */
public class DeadlineAdapter extends ArrayAdapter<Deadline> {
    public DeadlineAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public DeadlineAdapter(Context context, int resource, List<Deadline> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
            v = (View)((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.deadline_layout,null);
        Deadline p = getItem(position);
        if(p != null){

            TextView subjectName = (TextView) v.findViewById(R.id.subjectNameTextView);
            TextView deadlineName = (TextView) v.findViewById(R.id.nameTextView);
            TextView end = (TextView) v.findViewById(R.id.endDate_TextView);

            subjectName.setText(p.subjectName);
            deadlineName.setText(p.deadlineName);
            end.setText(p.getEndDate());


        }

        return v;
    }
}
