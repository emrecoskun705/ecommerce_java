package tr.com.emrecoskun.ecommerce_java.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tr.com.emrecoskun.ecommerce_java.SearchActivity;
import tr.com.emrecoskun.ecommerce_java.models.Product;
import tr.com.emrecoskun.ecommerce_java.adapters.ProductAdapter;
import tr.com.emrecoskun.ecommerce_java.ProductDetailsActivity;
import tr.com.emrecoskun.ecommerce_java.R;
import tr.com.emrecoskun.ecommerce_java.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    //firebase classes
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    //views
    EditText searchBar;
    ListView productListView;

    private List<Product> productList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        searchBar = (EditText) root.findViewById(R.id.searchBar);
        // add search functionality
        searchProduct();

        // Product list and product adapter connection part
        productListView = (ListView) root.findViewById(R.id.products);
        ProductAdapter productAdapter = new ProductAdapter(getActivity(), productList);
//        Log.d("nulldegil adapter", productAdapter.toString());
        productListView.setAdapter(productAdapter);

        //get products
        getProducts();

        // when click a product show its detail page
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = productList.get(i);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);

                intent.putExtra("productId", product.getProductId());
                startActivity(intent);
            }
        });


        return root;
    }

    private void getProducts() {
        firestore.collection("products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    // get data
                    Map<String, Object> data = doc.getData();
                    Product newProduct = new Product((String) data.get("imageUrl"), (String) data.get("name"), (double) data.get("price"));
                    newProduct.setDescription((String) data.get("image"));
                    newProduct.setProductId((String) doc.getId());

                    // get image
                    storageReference.child(newProduct.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newProduct.setImageUrl(uri.toString());
                            productList.add(newProduct);
                            // to fix exception of changing tabs
                            if(getActivity() != null) {
                                ProductAdapter productAdapter = new ProductAdapter(getActivity(), productList);
                                productListView.setAdapter(productAdapter);
                            }


                        }
                    });


                }
            }
        });
    }

    private void searchProduct() {
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i ==  EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("searchBy", searchBar.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}