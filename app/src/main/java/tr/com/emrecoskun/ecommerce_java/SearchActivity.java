package tr.com.emrecoskun.ecommerce_java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tr.com.emrecoskun.ecommerce_java.adapters.ProductAdapter;
import tr.com.emrecoskun.ecommerce_java.models.Product;

public class SearchActivity extends AppCompatActivity {
    private List<Product> productList = new ArrayList<>();

    int productCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        String searchBy = getIntent().getStringExtra("searchBy");

        // Product list and product adapter connection part
        ListView productListView = (ListView) findViewById(R.id.searchProductList);
        TextView foundProductText = (TextView) findViewById(R.id.foundProduct);
        ProductAdapter productAdapter = new ProductAdapter(this, productList);
        // Log.d("nulldegil adapter", productAdapter.toString());
        productListView.setAdapter(productAdapter);

        firestore.collection("products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    // get data
                    Map<String, Object> data = doc.getData();
                    if(((String)data.get("name")).toLowerCase().contains(searchBy.toLowerCase())) {
                        Product newProduct = new Product((String) data.get("imageUrl"), (String) data.get("name"), (double) data.get("price"));
                        newProduct.setDescription((String) data.get("image"));
                        newProduct.setProductId((String) doc.getId());
                        productCount++;
                        if(productCount == 1) {
                            foundProductText.setText(productCount + " product has been found");
                        } else {
                            foundProductText.setText(productCount + " products have been found");
                        }


                        // get image
                        storageReference.child(newProduct.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newProduct.setImageUrl(uri.toString());
                                productList.add(newProduct);
                                // to fix exception of changing tabs
                                if(SearchActivity.this != null) {
                                    ProductAdapter productAdapter = new ProductAdapter(SearchActivity.this, productList);
                                    productListView.setAdapter(productAdapter);
                                }

                            }
                        });
                    }



                }

                if(productCount == 0) {
                    foundProductText.setText("No product has been found");
                }
            }
        });


        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = productList.get(i);
                Intent intent = new Intent(SearchActivity.this, ProductDetailsActivity.class);

                intent.putExtra("productId", product.getProductId());
                startActivity(intent);
            }
        });




        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
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