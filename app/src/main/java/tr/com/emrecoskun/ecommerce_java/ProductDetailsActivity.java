package tr.com.emrecoskun.ecommerce_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import tr.com.emrecoskun.ecommerce_java.utils.DownloadImageTask;

public class ProductDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        TextView productName =(TextView) findViewById(R.id.details_productName);
        ImageView imageView =(ImageView) findViewById(R.id.details_productImage);
        TextView productDescription =(TextView) findViewById(R.id.details_description);
        TextView productPrice =(TextView) findViewById(R.id.details_productPrice);

        //TODO: get product using bundle

        productName.setText("denem");
        productDescription.setText("Lorem landit cursus. ra nisl porta at. Vestibulum pellentesque ante mi, quis mollis augue.");
        productPrice.setText("40$");

        new DownloadImageTask((ImageView) imageView)
                .execute("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("product name");
        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}