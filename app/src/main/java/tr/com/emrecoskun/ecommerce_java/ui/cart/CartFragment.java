package tr.com.emrecoskun.ecommerce_java.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import tr.com.emrecoskun.ecommerce_java.CheckoutActivity;
import tr.com.emrecoskun.ecommerce_java.ProductDetailsActivity;
import tr.com.emrecoskun.ecommerce_java.R;
import tr.com.emrecoskun.ecommerce_java.adapters.CartAdapter;
import tr.com.emrecoskun.ecommerce_java.databinding.FragmentCartBinding;
import tr.com.emrecoskun.ecommerce_java.models.Product;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private FragmentCartBinding binding;

    private List<Product> cartProductList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        cartProductList.add(new Product("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023", "Product name", 34.3));
        cartProductList.add(new Product("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023", "Product name", 34.3));
        cartProductList.add(new Product("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023", "Product name", 34.3));
        cartProductList.add(new Product("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023", "Product name", 34.3));



        cartViewModel =
                new ViewModelProvider(this).get(CartViewModel.class);

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // handle cart product listview
        ListView productListView = (ListView) root.findViewById(R.id.cart_product_list);
        CartAdapter cartAdapter = new CartAdapter(getActivity(), cartProductList);
        productListView.setAdapter(cartAdapter);

        Button buyButton = (Button) root.findViewById(R.id.cart_buy_button);
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