package tr.com.emrecoskun.ecommerce_java;

import android.graphics.Bitmap;

public class Product {
    //TODO: Remove bitmap if it is still redundant later
    private Bitmap image;
    private String imageUrl;
    private String name;
    private double price;

    public Product(String imageUrl, String name, double price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.price = price;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
