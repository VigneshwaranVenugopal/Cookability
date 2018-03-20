package map.cookability.NotificationFile;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import map.cookability.R;

public class NotificationActivity extends AppCompatActivity {
    private RecyclerView mNotifListView;

    private List<Notifications> notificationList;
    private NotifRecyclerAdapter notifRecyclerAdapter;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    public NotificationActivity(){
        //Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mNotifListView = (RecyclerView)findViewById(R.id.notif_list_view);

        notificationList = new ArrayList<>();
        notifRecyclerAdapter = new NotifRecyclerAdapter(this, notificationList);

        mNotifListView.setHasFixedSize(true);
        mNotifListView.setLayoutManager(new LinearLayoutManager(this));
        mNotifListView.setAdapter(notifRecyclerAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        notificationList.clear();

        mFirestore.collection("Users").document(mAuth.getCurrentUser().getUid()).collection("Notification").orderBy("timeStamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        Notifications curNotif = new Notifications();
                        if (documentSnapshot.getString("read").equals("true" )){
                            curNotif.setRead("true");
                        }else{
                            curNotif.setRead("false");
                        }

                        curNotif.setNotificationId(documentSnapshot.getId());
                        curNotif.setFromName(documentSnapshot.getString("fromName"));
                        curNotif.setFromId(documentSnapshot.getString("fromId"));
                        curNotif.setFromImage(documentSnapshot.getString("fromImage"));
                        curNotif.setMessage(documentSnapshot.getString("message"));
                        curNotif.setCurrentId(documentSnapshot.getString("currentId"));
                        curNotif.setCurrentName(documentSnapshot.getString("currentName"));
                        curNotif.setCurrentImage(documentSnapshot.getString("currentImage"));
                        curNotif.setAbstr(documentSnapshot.getString("abstr"));

                        notificationList.add(curNotif);
                        notifRecyclerAdapter.notifyDataSetChanged();

                    }
                }

            }
        });

    }
}
