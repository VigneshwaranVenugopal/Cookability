package map.cookability;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by theon on 3/1/2018.
 */

public class RecipeAdapter extends BaseAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Recipe> mDataSource;

    public RecipeAdapter(Context context, ArrayList<Recipe> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataSource.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.list_row, parent, false);

        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.recipe_list_title);

        // Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.recipe_list_subtitle);

        // Get thumbnail element
        final ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.recipe_list_thumbnail);

        Recipe recipe = (Recipe) getItem(position);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        titleTextView.setText(recipe.title);
        subtitleTextView.setText(recipe.documentName);
        StorageReference gsReference = storage.getReferenceFromUrl("gs://cookability-76899.appspot.com/paneerButter1300x731.jpg");
       /* storageRef.child("gs://cookability-76899.appspot.com/paneerButter1300x731.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(mContext).load(uri.toString()).into(thumbnailImageView);
            }
        });*/

        Picasso.with(mContext).load("https://firebasestorage.googleapis.com/v0/b/cookability-76899.appspot.com/o/paneerButter1300x731.jpg?alt=media&token=726a58c7-cde0-47e9-bed6-f232e846123c").into(thumbnailImageView);

        return rowView;
    }



}

