package map.cookability;

import android.os.Bundle;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class SearchActivity extends AppCompatActivity {

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

        mResultList = (RecyclerView)findViewById(R.id.card_recycler_view);
        mResultList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mResultList.setLayoutManager(layoutManager);

        mSearchBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                firebaseRecipeSearch();
            }
        });

    }

    private void firebaseRecipeSearch() {
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
    }


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
}
