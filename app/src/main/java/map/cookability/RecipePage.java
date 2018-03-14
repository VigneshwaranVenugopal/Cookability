package map.cookability;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


public class RecipePage extends AppCompatActivity {
    public static final String APPOINTMENT_MESSAGE = "";
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_page);
        Intent intent = getIntent();
        String msg = intent.getStringExtra(MainActivity.RECIPE_PAGE_TITLE_MESSAGE);
        TextView recipeTitle = (TextView) findViewById(R.id.recipe_title);
        recipeTitle.setText(msg);


        String msg2 = intent.getStringExtra(MainActivity.RECIPE_PAGE_CHEF_MESSAGE);
        TextView chefName = (TextView) findViewById(R.id.chef_name);
        chefName.setText(msg2);
    }

    public void openRequestAppointment(View view) {
        Intent intent = new Intent(this, RequestAppointment.class);
        View parent = (View) view.getParent();
        TextView recipeTitleTextView = parent.findViewById(R.id.recipe_title);
        String recipeTitle = String.valueOf(recipeTitleTextView.getText());
        intent.putExtra(APPOINTMENT_MESSAGE, recipeTitle);
        this.startActivity(intent);
    }

}


