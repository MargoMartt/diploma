package bppp.practice.service;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public interface OrderService {
     void saveOrder(OrderEntity order);

     OrderEntity getOrder(int id);

     void deleteOrder(int id);

     List<OrderEntity> getAllOrders();

     ArrayList<OrderEntity> getOrdersInCart(UserEntity user, String type);

     ArrayList<OrderEntity> getUsersOrders(UserEntity user);

     ArrayList<OrderEntity> getOrdersBuying();

     ArrayList<OrderEntity> getOrderByProductId(int id);

    int countOfOrdersByProductID(int id);
}
