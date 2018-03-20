package map.cookability;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SearchActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText mSearchField;
    private ImageButton mSearchBtn;
    private RecyclerView mResultList;

    private DatabaseReference mRecipeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRecipeDatabase = FirebaseDatabase.getInstance().getReference("recipes");

        mSearchField = (EditText)findViewById(R.id.search_field);
        mSearchBtn = (ImageButton)findViewById(R.id.search_button);

//        mResultList = (RecyclerView)findViewById(R.id.card_recycler_view);
//        mResultList.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        mResultList.setLayoutManager(layoutManager);
//
        mSearchBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String ed_text = mSearchField.getText().toString().trim();
                if(ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null)
                    mSearchField.setText("Enter String");
                    else
                getData();
                InputMethodManager mgr = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
            }
        });

    }

    /*private void firebaseRecipeSearch() {
        FirebaseRecyclerAdapter<Recipe, RecipeViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Recipe, RecipeViewHolder>(
                        Recipe.class,
                        R.layout.content_search,
                        RecipeViewHolder.class,
                        mRecipeDatabase
        ){


            @Override
            protected void populateViewHolder(RecipeViewHolder viewHolder, Recipe model, int position) {

                viewHolder.setDetails(model.getTitle(), model.getCategory(), model.getImageUrl());
                //viewHolder.setDetails(model.getName(), model.getType(), model.getImage());
            }
        };

        mResultList.setAdapter(firebaseRecyclerAdapter);
    }*/


    // View Holder class
    class RecipeViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String dishName, String dishCate, String dishImg){
            TextView dish_name = (TextView)mView.findViewById(R.id.dish_name_search);
            TextView dish_category = (TextView)mView.findViewById(R.id.category_search);
            ImageView dish_image = (ImageView)mView.findViewById(R.id.dish_image_search);

            dish_name.setText(dishName);
            dish_category.setText(dishCate);
            Glide.with(getApplicationContext()).load(dishImg).into(dish_image);
        }
    }


    public final void getData(){

        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Recipe> eventList = new ArrayList<>();
                            final ListView listView = (ListView) findViewById(R.id.lisr);
                            for (DocumentSnapshot document : task.getResult()) {
                                Recipe current = new Recipe(document.getId(),document.getData());
                                String e = current.title;
                                Log.d("Result", e + " => " + document.getData());
                                if(current.title.contains(mSearchField.getText()))
                                eventList.add(current);
                            }
                            RecipeAdapter adapter = new RecipeAdapter(getBaseContext(), eventList);
                            listView.setAdapter(adapter);
                        } else {
                            Log.d("ERROR TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void openRecipePage(final View view) {
        View parent = (View) view.getParent().getParent();
        TextView recipeIDTextView = parent.findViewById(R.id.recipe_id);
        String recipeID = recipeIDTextView.getText().toString();
        DocumentReference docRef = db.collection("recipes").document(recipeID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Map<String, Object> recipe = document.getData();
                        getChefUID(recipe);
                    } else {
                        Log.d("New", "No such document");
                    }
                } else {
                    Log.d("New", "get failed with ", task.getException());
                }
            }
        });
    }

    public void getChefUID(final Map recipe) {
        String chefUID = (String) recipe.get("chef");
        DocumentReference docRef = db.collection("users").document(chefUID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String chefName = (String) document.getData().get("name");
                        startRecipePageActivity(recipe, chefName);
                    } else {
                        Log.d("NEW", "No such document");
                    }
                } else {
                    Log.d("NEW", "get failed with ", task.getException());
                }
            }
        });
    }

    public void startRecipePageActivity(Map recipe, String chefName) {
        Intent intent = new Intent(this, RecipePage.class);

        String recipeTitle = (String) recipe.get("name");
        String category = (String) recipe.get("category");
        String chefUID = (String) recipe.get("chef");
        String imageURL = (String) recipe.get("imageurl");

        Bundle extras = new Bundle();
        extras.putString("NAME",recipeTitle);
        extras.putString("CATEGORY", category);
        extras.putString("IMAGE_URL", imageURL);
        extras.putString("CHEF", chefName);
        extras.putString("CHEF_UID", chefUID);
        extras.putString("STUDENT_UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        intent.putExtras(extras);

        this.startActivity(intent);
    }

}
