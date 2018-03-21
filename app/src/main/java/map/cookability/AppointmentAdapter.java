package map.cookability;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

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

        TextView appointmentDate = rowView.findViewById(R.id.appointment_date);
        TextView recipeName = rowView.findViewById(R.id.recipe_name);
        TextView chefNameName = rowView.findViewById(R.id.chef_name_name);
        TextView chefName = rowView.findViewById(R.id.chef_name);
        TextView studentUID = rowView.findViewById(R.id.student_uid);



        Map<String, Object> appointment = (Map<String, Object>) getItem(position);
        String appointmentTime = (String) appointment.get("appointment_time");
        long l = Long.parseLong(appointmentTime);
        String date = DateFormat.format("MM-dd-yyyy HH:mm:ss", l).toString();
        String recipeNameString = (String) appointment.get("recipe");
        String chefUIDString = (String) appointment.get("chef_uid");
        String studentUIDString = (String) appointment.get("student_uid");
        String chefNameString = (String) appointment.get("chefName");
        String chefNameString2 = "with " + chefNameString;

        appointmentDate.setText(date);
        recipeName.setText(recipeNameString);
        chefName.setText(chefUIDString);
        studentUID.setText(studentUIDString);
        chefNameName.setText(chefNameString2);
        return rowView;
    }
}

