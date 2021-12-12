package tr.com.emrecoskun.ecommerce_java.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tr.com.emrecoskun.ecommerce_java.MainActivity;
import tr.com.emrecoskun.ecommerce_java.Product;
import tr.com.emrecoskun.ecommerce_java.ProductAdapter;
import tr.com.emrecoskun.ecommerce_java.R;
import tr.com.emrecoskun.ecommerce_java.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private List<Product> productList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //TODO: Remove these, it is only for testing
        productList.add(new Product("https://upload.wikimedia.org/wikipedia/commons/e/ed/Gouraud_low.gif", "Product name", 34.3));
        productList.add(new Product("https://upload.wikimedia.org/wikipedia/commons/e/ed/Gouraud_low.gif", "Product name", 34.3));
        productList.add(new Product("https://upload.wikimedia.org/wikipedia/commons/e/ed/Gouraud_low.gif", "Product name", 34.3));
        productList.add(new Product("https://upload.wikimedia.org/wikipedia/commons/e/ed/Gouraud_low.gif", "Product name", 34.3));




        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /*final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        // Product list and product adapter connection part
        ListView productListView = (ListView) root.findViewById(R.id.products);
        ProductAdapter productAdapter = new ProductAdapter(getActivity(), productList);
        Log.d("nulldegil adapter", productAdapter.toString());
        productListView.setAdapter(productAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}