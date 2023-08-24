package bppp.practice.service;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

public interface OrderService {
    public void saveOrder(OrderEntity order);

    public OrderEntity getOrder(int id);

    public void deleteOrder(int id);

    public List<OrderEntity> getAllOrders();

    public ArrayList<OrderEntity> getOrdersInCart(UserEntity user, String type);

    public ArrayList<OrderEntity> getUsersOrders(UserEntity user);

    public ArrayList<OrderEntity> getOrdersBuying();
}
