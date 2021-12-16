package tr.com.emrecoskun.ecommerce_java.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tr.com.emrecoskun.ecommerce_java.R;
import tr.com.emrecoskun.ecommerce_java.models.Product;
import tr.com.emrecoskun.ecommerce_java.utils.DownloadImageTask;

public class CartAdapter extends BaseAdapter {
    List<Product> productList;
    private LayoutInflater inflater;

    public CartAdapter(Activity activity, List<Product> productList) {
        this.productList = productList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View cartProductView;
        cartProductView = inflater.inflate(R.layout.cart_product, null);

        TextView productName = (TextView) cartProductView.findViewById(R.id.cart_product_name);
        TextView productPrice = (TextView) cartProductView.findViewById(R.id.cart_product_price);
        ImageView productImage = (ImageView) cartProductView.findViewById(R.id.cart_product_image);

        Product product = productList.get(i);

        productName.setText(product.getName());
        productPrice.setText(product.getPrice() + "$");
//        imageView.setImageBitmap(product.getImage());

        new DownloadImageTask((ImageView) productImage)
                .execute(product.getImageUrl());

        return cartProductView;
    }
}