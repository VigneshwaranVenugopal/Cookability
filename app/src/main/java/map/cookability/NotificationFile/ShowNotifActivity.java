package map.cookability.NotificationFile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import map.cookability.R;

public class ShowNotifActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView message;
    private TextView fromName;
    private Button acceptBtn;
    private Button declineBtn;

    private ProgressBar mResponseProgressBar;

    private String fromId;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;


    private String mCurrentName;
    private String mcurrentImage;
    private String mMessage;
    private String currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notif);

        Intent intent = getIntent();
        fromId = intent.getStringExtra("fromId");
        currentId = intent.getStringExtra("currentId");

        message = (TextView) findViewById(R.id.show_notif_message);
        fromName = (TextView) findViewById(R.id.show_notif_fromName);
        acceptBtn = (Button) findViewById(R.id.show_notif_accept_btn);
        declineBtn = (Button) findViewById(R.id.show_notif_decline_btn);
        mResponseProgressBar = (ProgressBar)findViewById(R.id.responseProgressBar);


        message.setText("Appointment： " + intent.getStringExtra("message"));
        fromName.setText("From: " + intent.getStringExtra("fromName"));

        acceptBtn.setOnClickListener(this);
        declineBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.show_notif_accept_btn){
            sendNotification(true);

        }else if (view.getId() == R.id.show_notif_decline_btn){
            sendNotification(false);

        }

    }

    private void sendNotification(boolean flag) {


        mResponseProgressBar.setVisibility(View.VISIBLE);

        final String mCurrentId = mAuth.getCurrentUser().getUid();
        mFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        if (documentSnapshot.getId().equals(mCurrentId)){
                            mCurrentName = documentSnapshot.getString("name");
                            mcurrentImage = documentSnapshot.getString("image");
                            break;
                        }
                    }
                }

            }
        });
        if (flag){
            mMessage = "Yes: "+ mCurrentName + "accepts your request!";
        }else{
            mMessage = "sorry: " + mCurrentName + "declines your request!";
        }

        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("message", mMessage);
        notificationMap.put("fromId", mCurrentId);
        notificationMap.put("fromName",mCurrentName);
        notificationMap.put("fromImage",mcurrentImage);
        notificationMap.put("read","false");
        notificationMap.put("currentId",fromId);

        mFirestore.collection("Users/" + fromId+"/Notification").add(notificationMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(ShowNotifActivity.this,"Response Sent", Toast.LENGTH_LONG).show();
                mResponseProgressBar.setVisibility(View.INVISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ShowNotifActivity.this,"Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                mResponseProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

}
