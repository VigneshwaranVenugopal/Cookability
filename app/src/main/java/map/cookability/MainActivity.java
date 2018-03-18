package map.cookability;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    public static final String RECIPE_PAGE_TITLE_MESSAGE = "";
    public static final String RECIPE_PAGE_CHEF_MESSAGE = "";
    public static final String RECIPE_PAGE_IMG_SRC_MESSAGE = "";
    @Override
    public void onClick(View view) {
    }

    private BottomNavigationView bottomNavigationView;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

                bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected( MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottombaritem_home:
                                // TODO
                                return true;
                            case R.id.bottombaritem_messages:
                                // TODO
                                return true;
                            case R.id.bottombaritem_todo:
                                Intent intent = new Intent(MainActivity.this, RequestAppointment.class);
                                MainActivity.this.startActivity(intent);
                        }
                        return false;
                    }
                });


        getData();
    }

    private void adduser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final Map<String, Object> currentuser = new HashMap<>();
        Picasso.with(getBaseContext()).load(user.getPhotoUrl()).into((ImageView)findViewById(R.id.checknow));
        TextView name= (TextView) findViewById(R.id.nameView);
        name.setText(user.getDisplayName());
        TextView email= (TextView) findViewById(R.id.emailView);
        name.setText(user.getEmail());
        Log.d("CHECK","Logged IN");
        Log.d("CHECK",user.getDisplayName());
        Log.d("CHECK",user.getEmail());

        currentuser.put("name", user.getDisplayName());
        currentuser.put("email", user.getEmail());
        currentuser.put("uid", user.getUid());
        //currentuser.put("photourl",user.getPhotoUrl());


        CollectionReference userRef = db.collection("users");
        Query query = userRef.whereEqualTo("uid", user.getUid());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (documentSnapshots != null){
                    Log.d(TAG, "onEvent: username does not exists");
                    Toast.makeText(getBaseContext(), "Username is not available", Toast.LENGTH_SHORT).show();
                    db.collection("users").document(user.getUid())
                            .set(currentuser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                }
                for (DocumentSnapshot ds: documentSnapshots){
                    if (ds.exists()){
                        // The user name already
                        //Log.d(TAG, "checkingIfusernameExist: FOUND A MATCH: " + ds.toObject(Users.class).getUsername());
                        //Toast.makeText(getBaseContext(), "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public final void getData(){

        db.collection("recipes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Recipe> eventList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                final ListView listView = (ListView) findViewById(R.id.timeline);
                                Recipe current = new Recipe(document.getId(),document.getData());
                                String e = current.title;
                                Log.d("Result", e + " => " + document.getData());
                                eventList.add(current);
                                RecipeAdapter adapter = new RecipeAdapter(getBaseContext(), eventList);
                                listView.setAdapter(adapter);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //Adding the Plus button on the Header
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_settings){

            Intent addIntent = new Intent(getBaseContext(),AddActivity.class);
            MainActivity.this.startActivity(addIntent);
            
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                            /*Bitmap myBitmap = BitmapFactory.decodeFile(imgFil1.getPath());
                            myImage.setImageBitmap(myBitmap);*/
                    getData();
                }
            }, 8000);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
//            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
//            startActivity(intent);
//            finish();
        } else if (id == R.id.notification) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.logout) {

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                adduser();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }



    private void updateUI(Object o) {
        findViewById(R.id.profile).isPressed();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void openRecipePage(View view) {
        Intent intent = new Intent(this, RecipePage.class);
        View parent = (View) view.getParent().getParent();

        TextView recipeTitleTextView = parent.findViewById(R.id.recipe_list_title);
        String recipeTitle = String.valueOf(recipeTitleTextView.getText());
        intent.putExtra(RECIPE_PAGE_TITLE_MESSAGE, recipeTitle);

        TextView chefNameTextView = parent.findViewById(R.id.recipe_id);
        String chefName = String.valueOf(chefNameTextView.getText());
        intent.putExtra(RECIPE_PAGE_CHEF_MESSAGE, chefName);

        this.startActivity(intent);
    }
}

