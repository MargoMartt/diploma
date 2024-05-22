package bppp.practice.models;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.ProductEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.enums.OrderStatus;
import bppp.practice.service.OrderService;
import bppp.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
        allCounts.add(productService.getCountByType("Пластмассовая продукция"));
        allCounts.add(productService.getCountByType("Полимерная продукция"));
        allCounts.add(productService.getCountByType("Пленочная продукция"));
        allCounts.add(productService.getCountByType("Бытовая химия"));
        allCounts.add(productService.getCountByType("Регранулят"));
        allCounts.add(productService.getCountByType("Пресс-форма"));
        allCounts.add(productService.getCount());
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
