package map.cookability;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RecipePage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    public static String chefUID, studentUID, chefName, recipeName;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String recipeCategoryString = extras.getString("CATEGORY");
        recipeName = extras.getString("NAME");
        chefName = extras.getString("CHEF");
        chefUID = extras.getString("CHEF_UID");
        studentUID = extras.getString("STUDENT_UID");

        TextView recipeTitle = findViewById(R.id.recipe_name);
        recipeTitle.setText(recipeName);
        TextView recipeCategory = findViewById(R.id.recipe_category);
        recipeCategory.setText(recipeCategoryString);
        TextView chefNameTextView = findViewById(R.id.chef_name);
        chefNameTextView.setText(chefName);


    }

    public void getRecipeInfo(String recipeID) {



    }

    public void openRequestAppointment(View view) {
        Intent intent = new Intent(this, RequestAppointment.class);
        Bundle extras = new Bundle();
        extras.putString("RECIPE_NAME", recipeName);
        extras.putString("CHEF_NAME", chefName);
        extras.putString("CHEF_UID", chefUID);
        extras.putString("STUDENT_UID", studentUID);
        intent.putExtras(extras);
        this.startActivity(intent);
    }

}


