package com.example.shopme;

/**
 * Created by mAni on 29/08/2017.
 */

public class Product {

    private String Image , Title , Description , CompnayName , Category , Discount , Price , DiscountUntil , name , DiscountDetails , DiscountProduct , DiscountPrice , DiscountCompnayName , Type;

    public Product(){

    }

    public Product(String image, String title, String description, String compnayName, String category, String discount, String price, String discountUntil, String name, String discountDetails, String discountProduct, String discountPrice, String discountCompnayName, String type) {
        Image = image;
        Title = title;
        Description = description;
        CompnayName = compnayName;
        Category = category;
        Discount = discount;
        Price = price;
        DiscountUntil = discountUntil;
        this.name = name;
        DiscountDetails = discountDetails;
        DiscountProduct = discountProduct;
        DiscountPrice = discountPrice;
        DiscountCompnayName = discountCompnayName;
        Type = type;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCompnayName() {
        return CompnayName;
    }

    public void setCompnayName(String compnayName) {
        CompnayName = compnayName;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscountUntil() {
        return DiscountUntil;
    }

    public void setDiscountUntil(String discountUntil) {
        DiscountUntil = discountUntil;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscountDetails() {
        return DiscountDetails;
    }

    public void setDiscountDetails(String discountDetails) {
        DiscountDetails = discountDetails;
    }

    public String getDiscountProduct() {
        return DiscountProduct;
    }

    public void setDiscountProduct(String discountProduct) {
        DiscountProduct = discountProduct;
    }

    public String getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        DiscountPrice = discountPrice;
    }

    public String getDiscountCompnayName() {
        return DiscountCompnayName;
    }

    public void setDiscountCompnayName(String discountCompnayName) {
        DiscountCompnayName = discountCompnayName;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
