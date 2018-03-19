package map.cookability;

import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

        mRecipeDatabase = FirebaseDatabase.getInstance().getReference("Users");

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

        //initViews();
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

//    private void initViews(){
//        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(layoutManager);
//    }
//    private void loadJSON(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        RequestInterface request = retrofit.create(RequestInterface.class);
//        Call<JSONResponse> call = request.getJSON();
//        call.enqueue(new Callback<JSONResponse>() {
//            @Override
//            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
//
//                JSONResponse jsonResponse = response.body();
//                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
//                mAdapter = new DataAdapter(mArrayList);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<JSONResponse> call, Throwable t) {
//                Log.d("Error",t.getMessage());
//            }
//        });
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.searchbar, menu);
//
//        MenuItem search = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
//        search(searchView);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void search(SearchView searchView) {
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                if (mAdapter != null) mAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//    }

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
