package map.cookability.NotificationFile;

import android.content.Intent;
import android.icu.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import map.cookability.R;

public class ShowNotifActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mRecipeName;
    private TextView mName;
    private TextView mTime;
    private TextView mNote;
    private Button acceptBtn;
    private Button declineBtn;


    private ProgressBar mResponseProgressBar;

    private String fromId;
    private String fromName;
    private String requestedTime;
    private String note;
    private String recipeName;
//    private String fromImage;


    private String currentName;
//    private String currentImage;
    private String currentId;

    private String message;

    private FirebaseFirestore mFirestore;

    private String sendMessage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notif);

        Intent intent = getIntent();
//        message = intent.getStringExtra("message");
        fromId = intent.getStringExtra("fromId");
        fromName = intent.getStringExtra("fromName");
        recipeName = intent.getStringExtra("recipeName");
        note = intent.getStringExtra("note");
        requestedTime = intent.getStringExtra("requestedTime");

//        fromImage = intent.getStringExtra("fromImage");


        currentId = intent.getStringExtra("currentId");
        currentName = intent.getStringExtra("currentName");
//        currentImage = intent.getStringExtra("currentImage");

        mRecipeName = (TextView) findViewById(R.id.show_notif_recipe);
        mName = (TextView) findViewById(R.id.show_notif_fromName);
        mNote = (TextView)findViewById(R.id.show_notif_note);
        mTime = (TextView)findViewById(R.id.show_notif_time);

        acceptBtn = (Button) findViewById(R.id.show_notif_accept_btn);
        declineBtn = (Button) findViewById(R.id.show_notif_decline_btn);
        mResponseProgressBar = (ProgressBar)findViewById(R.id.responseProgressBar);

        mRecipeName.setText("Recipe name： " + recipeName);
        mName.setText("From: " + fromName);
        mNote.setText("Note: " + note);
        mTime.setText("Request time: " + requestedTime);

        acceptBtn.setOnClickListener(this);
        declineBtn.setOnClickListener(this);

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


        String abstr;
        if (flag){
            sendMessage = "Yes: "+ currentName + "accepts your request!";
            abstr = "accept";
        }else{
            sendMessage = "sorry: " + currentName + "declines your request!";
            abstr = "decline";
        }

        Map<String, Object> notificationMap = new HashMap<>();
        notificationMap.put("abstr", abstr);
        notificationMap.put("message", sendMessage);
        notificationMap.put("fromId", currentId);
        notificationMap.put("fromName",currentName);
//        notificationMap.put("fromImage",currentImage);
        notificationMap.put("read","false");
        notificationMap.put("currentId",fromId);
        notificationMap.put("currentName",fromName);
//        notificationMap.put("currentImage",fromImage);
        notificationMap.put("timeStamp", FieldValue.serverTimestamp());

        notificationMap.put("requireTime",requestedTime);
        notificationMap.put("recipeTitle", recipeName);
        notificationMap.put("note", note);

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
