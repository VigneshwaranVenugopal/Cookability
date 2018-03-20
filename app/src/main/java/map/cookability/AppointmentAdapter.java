package map.cookability;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class AppointmentAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Map<String, Object>> mDataSource;

    public AppointmentAdapter(Context context, ArrayList<Map<String, Object>> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.appointment_adapter, parent, false);

        // Get title element
        TextView appointmentDate =
                (TextView) rowView.findViewById(R.id.appointment_date);


        Map<String, Object> appointment = (Map<String, Object>) getItem(position);

        String note = (String) appointment.get("note");

        appointmentDate.setText(note);
        return rowView;
    }
}

