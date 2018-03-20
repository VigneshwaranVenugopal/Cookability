package map.cookability;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class Appointments extends AppCompatActivity {
    public static String UID;
    private static final String TAG = "Debug Tag";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointments);
        Bundle extras = getIntent().getExtras();
        UID = extras.getString("UID");
        getAppointments();
    }

    public void getAppointments() {
        db.collection("appointments").whereEqualTo("student_uid", UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Map<String, Object>> eventList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                final ListView listView = (ListView) findViewById(R.id.timeline);
                                Map<String, Object> current = document.getData();
                                eventList.add(current);
                                AppointmentAdapter adapter = new AppointmentAdapter(getBaseContext(), eventList);
                                listView.setAdapter(adapter);
                                String test = (String) current.get("status");
                                Log.d(TAG,  test);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}


