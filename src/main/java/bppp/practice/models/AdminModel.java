package bppp.practice.models;

import bppp.practice.enums.ProductType;
import bppp.practice.entity.ProductEntity;
import bppp.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AdminModel {

    @Autowired
    ProductService productService;

    public ArrayList<ProductType> productTypes() {
        ArrayList<ProductType> productTypes = new ArrayList<>();
        productTypes.add(ProductType.PLASTIC);
        productTypes.add(ProductType.FILM);
        productTypes.add(ProductType.REGRANULATE);
        productTypes.add(ProductType.POLYMER);
        productTypes.add(ProductType.HOUSEHOLD);
        productTypes.add(ProductType.SHAPES);
        return productTypes;
    }

    public void addProduct(String name, String description, String image, double cost, int count, String type) {
        ProductEntity product = new ProductEntity();
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPicture(image);
        product.setProductCost(cost);
        product.setProductCount(count);
        product.setProductType(type);
        productService.saveProduct(product);
    }

    public void editProduct(int id, String name, String description, String image, double cost, int count, String type){
        System.out.println(id);
        ProductEntity product = productService.getProduct(id);
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPicture(image);
        product.setProductCost(cost);
        product.setProductCount(count);
        product.setProductType(type);
        productService.saveProduct(product);    }
}
