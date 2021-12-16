package tr.com.emrecoskun.ecommerce_java.ui.cart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tr.com.emrecoskun.ecommerce_java.CheckoutActivity;
import tr.com.emrecoskun.ecommerce_java.ProductDetailsActivity;
import tr.com.emrecoskun.ecommerce_java.R;
import tr.com.emrecoskun.ecommerce_java.adapters.CartAdapter;
import tr.com.emrecoskun.ecommerce_java.adapters.ProductAdapter;
import tr.com.emrecoskun.ecommerce_java.databinding.FragmentCartBinding;
import tr.com.emrecoskun.ecommerce_java.models.Product;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;

    private List<Product> cartProductList = new ArrayList<>();

    double cartTotalPrice = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        // handle cart product listview
        ListView productListView = (ListView) root.findViewById(R.id.cart_product_list);
        TextView totalPrice = (TextView) root.findViewById(R.id.cart_total_price);
        Button buyButton = (Button) root.findViewById(R.id.cart_buy_button);
        buyButton.setVisibility(View.INVISIBLE);

        // get cart products for user
        firestore.collection("carts").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if ((ArrayList<String>) task.getResult().get("products") != null) {
                        for (String i : (ArrayList<String>) task.getResult().get("products")) {
                            buyButton.setVisibility(View.VISIBLE);
                            firestore.collection("products").document(i).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> data = task.getResult().getData();
                                        Product newProduct = new Product((String) data.get("imageUrl"), (String) data.get("name"), (double) data.get("price"));
                                        newProduct.setDescription((String) data.get("image"));
                                        newProduct.setProductId((String) task.getResult().getId());

                                        // set total price text
                                        cartTotalPrice += (double) data.get("price");
                                        totalPrice.setText("Total Price: " + String.format("%.2f", cartTotalPrice) + "$");

                                        // get image
                                        storageReference.child(newProduct.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                newProduct.setImageUrl(uri.toString());
                                                cartProductList.add(newProduct);
                                                // to fix exception of changing tabs
                                                if (getActivity() != null) {
                                                    CartAdapter cartAdapter = new CartAdapter(getActivity(), cartProductList);
                                                    productListView.setAdapter(cartAdapter);
                                                }

                                            }
                                        });
                                    }
                                }
                            });
                        }

                    } else {
                        Toast.makeText(getActivity(), "Your cart is empty.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                //TODO: put the cart info in here
                intent.putExtra("cart", 2);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}