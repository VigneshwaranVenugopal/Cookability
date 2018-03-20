package map.cookability;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button call ;
    ArrayList<Map<String, Object>> eventList = new ArrayList<>();
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
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String, Object> current = document.getData();
                                getAppointmentInfo(current);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public String getAppointmentInfo(final Map<String, Object> appointment) {
        String chefUID = (String) appointment.get("chef_uid");
        String chefName = "";
        DocumentReference docRef = db.collection("users").document(chefUID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Map<String, Object> user = document.getData();
                        String chefName = (String) user.get("name");
                        final ListView listView = (ListView) findViewById(R.id.timeline);
                        AppointmentAdapter adapter = new AppointmentAdapter(getBaseContext(), eventList);
                        Map<String, Object> appointmentWithInfo = appointment;
                        appointmentWithInfo.put("chefName", chefName);
                        eventList.add(appointmentWithInfo);
                        listView.setAdapter(adapter);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return chefName;
    }



    public void openButton(View view){

        TextView chefUID = findViewById(R.id.chef_name);
        Intent intent = new Intent(this, LoginActivity.class);

        String chef = chefUID.getText().toString();

        Bundle extras = new Bundle();
        extras.putString("CHEF",chef);
        intent.putExtras(extras);

        this.startActivity(intent);
    }




}


