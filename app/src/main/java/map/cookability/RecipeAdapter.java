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
        TextView recipeTitle =
                (TextView) rowView.findViewById(R.id.recipe_list_title);

        // Get subtitle element
        TextView category =
                (TextView) rowView.findViewById(R.id.recipe_list_subtitle);

        // Get thumbnail element
        final ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.recipe_list_thumbnail);

        Recipe recipe = (Recipe) getItem(position);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        recipeTitle.setText(recipe.title);
        category.setText(recipe.category);
        StorageReference gsReference = storage.getReferenceFromUrl("gs://cookability-76899.appspot.com/"+recipe.imageUrl);
       gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
           @Override
           public void onSuccess(Uri uri) {
               Picasso.with(mContext).load(uri.toString()).into(thumbnailImageView);
           }
       });
        return rowView;
    }



}

