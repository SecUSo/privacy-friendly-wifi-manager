package secuso.org.privacyfriendlywifi.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import secuso.org.privacyfriendlywifi.R;

public class ScheduleAdapter extends ArrayAdapter<String> {
    private Activity context;
    private String[] items;

    static class ScheduleViewHolder {
        public TextView text1;
        public TextView text2;
    }

    public ScheduleAdapter(Activity context, int resource, String[] items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length / 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        // reuse views
        if (listItem == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            listItem = inflater.inflate(R.layout.list_item_whitelist, null);
            // configure view holder
            ScheduleViewHolder viewHolder = new ScheduleViewHolder();
            viewHolder.text1 = (TextView) listItem.findViewById(R.id.text1);
            viewHolder.text2 = (TextView) listItem.findViewById(R.id.text2);
            listItem.setTag(viewHolder);
        }

        // fill data
        ScheduleViewHolder viewHolder = (ScheduleViewHolder) listItem.getTag();
        String item1Text = items[2 * position];
        viewHolder.text1.setText(item1Text);
        String item2Text = items[2 * position + 1];
        viewHolder.text2.setText(item2Text);

        return listItem;
    }
}
