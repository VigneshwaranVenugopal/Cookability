package map.cookability;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 0;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";

    ImageView mImageView;
    VideoView mVideoView;
    Uri file;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://cookability-76899.appspot.com");
    StorageReference storageReference = storage.getReference();
    Uri photoURI;
    final String imagename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button takePhoto = (Button) findViewById(R.id.takePhoto);
        Button submit = (Button) findViewById(R.id.submitButton);
        //Button takeVideo = (Button) findViewById(R.id.takeVideo);
        mImageView = (ImageView) findViewById(R.id.mImageView);

        //mVideoView = (VideoView) findViewById(R.id.mVideoView);
        setSupportActionBar(toolbar);


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ImageView myImage = (ImageView) findViewById(R.id.mImageView);
                dispatchTakePictureIntent();
                File imgFile = new  File("/sdcard/Android/data/assignment1.cooking/files/Pictures/temp.jpg");
                final File imgFil1 = new  File(imagepath);
                Log.d("YES",imagepath);
                if(imgFil1.exists()){
                    Log.d("NO",imagepath);


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            /*Bitmap myBitmap = BitmapFactory.decodeFile(imgFil1.getPath());
                            myImage.setImageBitmap(myBitmap);*/
                            Picasso.with(getBaseContext()).load(imgFil1).into(myImage);
                        }
                    }, 5000);
                };
                Log.d("YES",imagepath);
            }
        });
        final EditText name = (EditText) findViewById(R.id.EditTextName);

        final Spinner category = (Spinner) findViewById(R.id.categorySpinner);

        final Spinner nonveg = (Spinner) findViewById(R.id.nonvegSpinner);

        final EditText ingridients = (EditText) findViewById(R.id.ingridientsText);

        final EditText steps = (EditText) findViewById(R.id.stepsText);
        final Map<String, String> recipe = new HashMap<>();


            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final String recipeName = name.getText().toString();
                    final String recipeCategory = category.getSelectedItem().toString();
                    final String nonvegText = nonveg.getSelectedItem().toString();
                    final String ingridientsText = ingridients.getText().toString();
                    final String stepsText = steps.getText().toString();
                    Log.d("RHINO",recipeName);

                    recipe.put("name", recipeName);
                    recipe.put("category", recipeCategory);
                    recipe.put("nonveg", nonvegText);
                    recipe.put("ingridients", ingridientsText);
                    recipe.put("steps", stepsText);
                    recipe.put("imageurl", imagename);
                    recipe.put("chef", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    db.collection("recipes")
                            .add(recipe)
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
                    try {
                        upload();
                        finish();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            });
    }
    static final int REQUEST_TAKE_PHOTO = 1;
    UploadTask uploadTask;

    public void upload() throws FileNotFoundException {
        final InputStream stream = new FileInputStream(new File(imagepath));
        final StorageReference riversRef = storageReference
                .child(imagename);
        uploadTask = riversRef.putStream(stream);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("res","no");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("res","hurray");

            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                File imagePath = new File(this.getFilesDir(), "images");
                File newFile = new File(imagePath, "default_image.jpg");
                photoURI = FileProvider.getUriForFile(this,
                        "map.cookability",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    String mCurrentPhotoPath;
    String imageFileName;
    String imagepath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        imagepath = image.getPath().replace("storage/emulated/0","sdcard");
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

}
