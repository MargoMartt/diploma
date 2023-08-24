package bppp.practice.models;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.ProductEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.enums.OrderStatus;
import bppp.practice.service.OrderService;
import bppp.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ProductsModel {
    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;
    public ArrayList<Integer> countProducts() {
        ArrayList<Integer> allCounts = new ArrayList<>();
        allCounts.add(productService.getCountByType("Press shapes"));
        allCounts.add(productService.getCountByType("Polymer products"));
        allCounts.add(productService.getCountByType("Film products"));
        allCounts.add(productService.getCountByType("Household chemicals"));
        allCounts.add(productService.getCountByType("Regranulate"));
        allCounts.add(productService.getCountByType("Press shapes"));
        allCounts.add(productService.getAllProducts().size());
        return allCounts;
    }
    public void inCart(UserEntity user, ProductEntity product, int count){
        OrderEntity order = new OrderEntity();
        order.setProductByProductId(product);
        order.setOrderCount(count);
        order.setUserByIdUser(user);
        order.setOrderCost(product.getProductCost()*count);
        order.setOrderStatus(OrderStatus.CART.getStatus());
        orderService.saveOrder(order);
    }
    public void inCart(UserEntity user, ProductEntity product){
        OrderEntity order = new OrderEntity();
        order.setProductByProductId(product);
        order.setOrderCount(1);
        order.setUserByIdUser(user);
        order.setOrderCost(product.getProductCost());
        order.setOrderStatus(OrderStatus.CART.getStatus());
        orderService.saveOrder(order);
    }
}
