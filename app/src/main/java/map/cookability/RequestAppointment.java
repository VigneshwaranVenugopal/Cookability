package map.cookability;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestAppointment extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "AppointmentTag";
    Button datePickerButton, timePickerButton;
    int year, month, day, hour, minute;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    TextView appointmentDate, appointmentTime, recipeTitle;
    EditText noteEditText;
    private Button addAppointmentBtn;
    static String chefUID, studentUID, chefName, recipeName;

    private ProgressBar mMessageProgressBar;
    private String studentName;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_appointment);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        chefUID = extras.getString("CHEF_UID");
        studentUID = extras.getString("STUDENT_UID");
        chefName = extras.getString("CHEF_NAME");
        recipeName = extras.getString("RECIPE_NAME");

        recipeTitle = (TextView) findViewById(R.id.recipe_name);
        recipeTitle.setText(recipeName);
        TextView chefNameTextView = (TextView) findViewById(R.id.chef_name);
        chefNameTextView.setText(chefName);

        appointmentDate = (TextView)findViewById(R.id.appointmentDate);
        appointmentTime = (TextView)findViewById(R.id.appointmentTime);
        noteEditText = (EditText)findViewById(R.id.noteEditText);

        addAppointmentBtn = (Button)findViewById(R.id.addAppointment_btn);
        mMessageProgressBar = (ProgressBar) findViewById(R.id.messageProgressBar);

        showDateDialogOnButtonClick();
        showTimeDialogOnButtonClick();

        db.collection("users").document(studentUID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                studentName = documentSnapshot.getString("name");
            }
        });

        addAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] mCurrentName = new String[1];
                mMessageProgressBar.setVisibility(View.VISIBLE);

                long currentTime = System.currentTimeMillis()/1000;
                String date_string = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00";
                long timestampDate;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date;
                try {
                    date = formatter.parse(date_string);
                    long output=date.getTime()/1000L;
                    String str=Long.toString(output);
                    timestampDate = Long.parseLong(str) * 1000;
                } catch (ParseException e) {
                    timestampDate = currentTime;
                    e.printStackTrace();
                }


                final String appointmentTime = Long.toString(timestampDate);

                //send Notification
//                db.collection("users").document(chefUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()){
//                            DocumentSnapshot documentSnapshot = task.getResult();
//                            if (documentSnapshot != null && documentSnapshot.exists()){
//                                mCurrentName[0] = (String) documentSnapshot.getData().get("name");
//                            }
//                        }
//                    }
//                });


//                String chefUID = (String) recipe.get("chef");
//                DocumentReference docRef = db.collection("users").document(chefUID);
//                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document != null && document.exists()) {
//                                String chefName = (String) document.getData().get("name");
//                                startRecipePageActivity(recipe, chefName);
//                            } else {
//                                Log.d(TAG, "No such document");
//                            }
//                        } else {
//                            Log.d(TAG, "get failed with ", task.getException());
//                        }
//                    }
//                });

                String message = "Hi " + chefName + ", I'm " +studentName + " I want to learn how to cook " + recipeName + " !" ;

                Map<String, Object> notificationMap = new HashMap<>();
                notificationMap.put("abstr","appointment");
                notificationMap.put("message", message);
                notificationMap.put("fromId", studentUID);
                notificationMap.put("fromName",studentName);
//                notificationMap.put("fromImage",mcurrentImage);
                notificationMap.put("read","false");
                notificationMap.put("currentId",chefUID);
                notificationMap.put("currentName",chefName);
//                notificationMap.put("currentImage",mUserImage);
                notificationMap.put("timeStamp", FieldValue.serverTimestamp());

                notificationMap.put("requestedTime",appointmentTime);
                notificationMap.put("recipeName", recipeName);
                notificationMap.put("note", noteEditText.getText().toString());

                db.collection("users/" + chefUID+"/Notification").add(notificationMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(RequestAppointment.this,"Request successfully!" , Toast.LENGTH_LONG).show();

                        mMessageProgressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RequestAppointment.this,"Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

                        mMessageProgressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }

    public void showDateDialogOnButtonClick() {
        datePickerButton = (Button)findViewById(R.id.datePickerButton);
        datePickerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DATE_DIALOG_ID);
                    }
                }
        );
    }

    public void showTimeDialogOnButtonClick() {
        datePickerButton = (Button)findViewById(R.id.timePickerButton);
        datePickerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(TIME_DIALOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG_ID) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
        else if (id == TIME_DIALOG_ID) {
            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR);
            minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(this, timePickerListener, hour, minute, false);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1 + 1;
            day = i2;
            String date = year + "/" + month + "/" + day;
            appointmentDate.setText(date);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int defaultHour, int defaultMinute) {
            hour = defaultHour;
            minute = defaultMinute;
            String time = hour + ":" + minute;
            appointmentTime.setText(time);
        }
    };


    public void addAppointment(View view) {
        final Map<String, String> appointment = new HashMap<>();
        long currentTime = System.currentTimeMillis()/1000;

        String date_string = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + "00";
        long timestampDate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date;
        try {
            date = formatter.parse(date_string);
            long output=date.getTime()/1000L;
            String str=Long.toString(output);
            timestampDate = Long.parseLong(str) * 1000;
        } catch (ParseException e) {
            timestampDate = currentTime;
            e.printStackTrace();
        }


        final String appointmentTime = Long.toString(timestampDate);
        final String dateCreated = Long.toString(currentTime);
        final String note = noteEditText.getText().toString();
        final String recipe = recipeTitle.getText().toString();
        final String status = "requested";

        appointment.put("appointment_time", appointmentTime);
        appointment.put("date_created", dateCreated);
        appointment.put("note", note);
        appointment.put("recipe", recipe);
        appointment.put("status", status);
        appointment.put("chef_uid", chefUID);
        appointment.put("student_uid", studentUID);
        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }



}


