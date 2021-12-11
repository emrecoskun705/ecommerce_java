package tr.com.emrecoskun.ecommerce_java;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.util.List;

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
        txtPrice.setText(product.getPrice() + "");
//        imageView.setImageBitmap(product.getImage());

        new DownloadImageTask((ImageView) imageView)
                .execute(product.getImageUrl());

        return productView;
    }
    // This class add image
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}








