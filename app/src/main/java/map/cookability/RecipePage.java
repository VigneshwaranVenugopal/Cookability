package map.cookability;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class RecipePage extends AppCompatActivity {
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
        String imageURL = extras.getString("IMAGE_URL");

        TextView recipeTitle = findViewById(R.id.recipe_name);
        recipeTitle.setText(recipeName);
        TextView recipeCategory = findViewById(R.id.recipe_category);
        recipeCategory.setText(recipeCategoryString);
        TextView chefNameTextView = findViewById(R.id.chef_name);
        chefNameTextView.setText("by " + chefName);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        final Context context = this;
        final ImageView thumbnailImageView = findViewById(R.id.recipe_list_thumbnail);
        StorageReference gsReference = storage.getReferenceFromUrl("gs://cookability-76899.appspot.com/"+imageURL);
        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri.toString()).into(thumbnailImageView);
            }
        });


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


