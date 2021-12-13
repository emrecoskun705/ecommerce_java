package tr.com.emrecoskun.ecommerce_java.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import tr.com.emrecoskun.ecommerce_java.models.Product;
import tr.com.emrecoskun.ecommerce_java.adapters.ProductAdapter;
import tr.com.emrecoskun.ecommerce_java.ProductDetailsActivity;
import tr.com.emrecoskun.ecommerce_java.R;
import tr.com.emrecoskun.ecommerce_java.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private List<Product> productList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //TODO: Remove these, it is only for testing
        productList.add(new Product("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023", "Product name", 34.3));
        productList.add(new Product("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023", "Product name", 34.3));
        productList.add(new Product("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023", "Product name", 34.3));
        productList.add(new Product("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone11-black-select-2019_GEO_EMEA?wid=470&hei=556&fmt=png-alpha&.v=1567021766023", "Product name", 34.3));




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
//        Log.d("nulldegil adapter", productAdapter.toString());
        productListView.setAdapter(productAdapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = productList.get(i);
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);

                //TODO: put the product in here
                intent.putExtra("product", 2);
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