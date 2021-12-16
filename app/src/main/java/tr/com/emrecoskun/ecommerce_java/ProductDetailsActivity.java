package tr.com.emrecoskun.ecommerce_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import tr.com.emrecoskun.ecommerce_java.adapters.ProductAdapter;
import tr.com.emrecoskun.ecommerce_java.models.Product;
import tr.com.emrecoskun.ecommerce_java.utils.DownloadImageTask;

public class ProductDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        TextView productName =(TextView) findViewById(R.id.details_productName);
        ImageView imageView =(ImageView) findViewById(R.id.details_productImage);
        TextView productDescription =(TextView) findViewById(R.id.details_description);
        TextView productPrice =(TextView) findViewById(R.id.details_productPrice);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        if(getIntent().hasExtra("productId")) {
            String productId = getIntent().getStringExtra("productId");
            firestore.collection("products").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        Product newProduct = new Product((String) task.getResult().get("imageUrl"), (String) task.getResult().get("name"), (double) task.getResult().get("price"));
                        newProduct.setDescription((String) task.getResult().get("description"));
                        newProduct.setProductId(productId);

                        // get image
                        storageReference.child(newProduct.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newProduct.setImageUrl(uri.toString());
                                new DownloadImageTask((ImageView) imageView)
                                        .execute(newProduct.getImageUrl());
                            }
                        });

                        productName.setText(newProduct.getName());
                        productDescription.setText(newProduct.getDescription());
                        productPrice.setText(newProduct.getPrice() + "$");
                        actionBar.setTitle(newProduct.getName());
                    }
                }
            });
        }

        Button cartButton = findViewById(R.id.addToCart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: implement add to cart functionality
            }
        });


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