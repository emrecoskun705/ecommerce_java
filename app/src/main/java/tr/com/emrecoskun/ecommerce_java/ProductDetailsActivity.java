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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.emrecoskun.ecommerce_java.adapters.ProductAdapter;
import tr.com.emrecoskun.ecommerce_java.models.Product;
import tr.com.emrecoskun.ecommerce_java.utils.DownloadImageTask;

public class ProductDetailsActivity extends AppCompatActivity {

    // Firebase objects that is needed
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    //views for product detail
    TextView productName;
    ImageView imageView;
    TextView productDescription;
    TextView productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // initialize all firebase functionality for this class
        initializeFirebase();

        // get needed views
        productName =(TextView) findViewById(R.id.details_productName);
        imageView =(ImageView) findViewById(R.id.details_productImage);
        productDescription =(TextView) findViewById(R.id.details_description);
        productPrice =(TextView) findViewById(R.id.details_productPrice);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        String productId = getIntent().getStringExtra("productId");

        getProductDetailByID(productId, actionBar);



        Button cartButton = findViewById(R.id.addToCart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userId = currentUser.getUid();

                DocumentReference reference = firestore.collection("carts").document(userId);

                Map<String, Object> data = new HashMap<>();


                reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        List<String> productIdList = (ArrayList<String>) task.getResult().get("products");
                        if(productIdList == null) {
                            List<String> list = new ArrayList<>();
                            list.add(productId);
                            data.put("products", list);
                            reference.set(data);
                        } else {

                            data.put("products", productIdList);
                            reference.update("products", FieldValue.arrayUnion(productId));
                        }
                        Toast.makeText(ProductDetailsActivity.this, "Product added to cart",
                                Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


        // Customize the back button
        actionBar.setHomeAsUpIndicator(R.drawable.back);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void getProductDetailByID(String productId, ActionBar actionBar) {
        firestore.collection("products").document(productId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    Product newProduct = new Product((String) task.getResult().get("imageUrl"), (String) task.getResult().get("name"), (double) task.getResult().get("price"));
                    newProduct.setDescription((String) task.getResult().get("description"));
                    newProduct.setProductId(productId);
                    // default loading image
                    imageView.setImageResource(R.mipmap.loading);

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

    private void initializeFirebase() {
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
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