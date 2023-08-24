package bppp.practice.models;

import bppp.practice.enums.OrderStatus;
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

    public void editProduct(int id, String name, String description, String image, double cost, int count, String type) {
        System.out.println(id);
        ProductEntity product = productService.getProduct(id);
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPicture(image);
        product.setProductCost(cost);
        product.setProductCount(count);
        product.setProductType(type);
        productService.saveProduct(product);
    }

    public void editProductWithoutPicture (int id, String name, String description, double cost, int count, String type) {
        ProductEntity product = productService.getProduct(id);
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductCost(cost);
        product.setProductCount(count);
        product.setProductType(type);
        productService.saveProduct(product);
    }
    public ArrayList<String> productTypes(){
        ArrayList<String> productTypes = new ArrayList<>();
        productTypes.add(ProductType.PLASTIC.getCode());
        productTypes.add(ProductType.FILM.getCode());
        productTypes.add(ProductType.HOUSEHOLD.getCode());
        productTypes.add(ProductType.REGRANULATE.getCode());
        productTypes.add(ProductType.SHAPES.getCode());
        productTypes.add(ProductType.POLYMER.getCode());
        return productTypes;
    }

    public ArrayList<String> orderStatuses(){
        ArrayList<String> orderStatuses = new ArrayList<>();
        orderStatuses.add(OrderStatus.DELIVERING.getStatus());
        orderStatuses.add(OrderStatus.MANUFACTURING.getStatus());
        orderStatuses.add(OrderStatus.WAITING.getStatus());
        orderStatuses.add(OrderStatus.DELIVERED.getStatus());
        return orderStatuses;
    }

}
