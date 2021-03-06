package map.cookability;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import map.cookability.NotificationFile.NotificationActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    public static final String RECIPE_PAGE_TITLE_MESSAGE = "";
    public static final String RECIPE_PAGE_CHEF_MESSAGE = "";
    public static final String RECIPE_PAGE_IMG_SRC_MESSAGE = "";
    ListView listView ;

    String imagepath;


    @Override
    public void onClick(View view) {
    }
    ImageView profilePhoto ;
    String currentUserUID;
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
        listView = (ListView) findViewById(R.id.timeline);

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

//                bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        String chef = "7wDjxEN465VS2KBj37bbpffaCeG3";

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        currentUserUID = user.getUid();
        //currentuser.put("photourl",user.getPhotoUrl());


        CollectionReference userRef = db.collection("users");
        Query query = userRef.whereEqualTo("uid", user.getUid());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (documentSnapshots != null){
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
        if(id == R.id.search){
            Intent searchIntent = new Intent(getBaseContext(),SearchActivity.class);
            MainActivity.this.startActivity(searchIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.notification) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);

        } else if (id == R.id.appointments) {
            openAppointmentsPage();

        } else if (id == R.id.logout) {

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
            finish();

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

    public void openAppointmentsPage() {
        Intent intent = new Intent(this, Appointments.class);
        intent.putExtra("UID", currentUserUID);
        this.startActivity(intent);
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
        extras.putString("STUDENT_UID", currentUserUID);
        intent.putExtras(extras);

        this.startActivity(intent);
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
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
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
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}

