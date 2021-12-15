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

public class ProductAdapter extends BaseAdapter {

    List<Product> productList;
    private LayoutInflater inflater;

    public ProductAdapter(Activity activity, List<Product> productList) {
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
        View productView;
        productView = inflater.inflate(R.layout.product, null);

        TextView txtName = (TextView) productView.findViewById(R.id.productName);
        TextView txtPrice = (TextView) productView.findViewById(R.id.productPrice);
        ImageView imageView = (ImageView) productView.findViewById(R.id.productImage);
        Product product = productList.get(i);
        txtName.setText(product.getName());
        txtPrice.setText(product.getPrice() + "$");
        // default loading image
        imageView.setImageResource(R.mipmap.loading);
//        imageView.setImageBitmap(product.getImage());

        new DownloadImageTask((ImageView) imageView)
                .execute(product.getImageUrl());

        return productView;
    }
}