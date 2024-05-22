package bppp.practice.service;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void saveOrder(OrderEntity order) {
        orderRepository.save(order);
    }

    @Override
    public OrderEntity getOrder(int id) {
        OrderEntity order = null;
        Optional<OrderEntity> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            order = optional.get();
        }
        return order;
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderEntity> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public ArrayList<OrderEntity> getOrdersInCart(UserEntity user, String type) {
        return orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, type);
    }

    @Override
    public ArrayList<OrderEntity> getUsersOrders(UserEntity user) {
        ArrayList<OrderEntity> orders = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "Заказ в листе ожидания");
        ArrayList<OrderEntity> ordersManufacturing = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "В процессе производства");
        ArrayList<OrderEntity> ordersDelivering = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "Заказ доставляется");
        ArrayList<OrderEntity> ordersDelivered = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "Заказ доставлен");
        ArrayList<OrderEntity> ordersWaitingPayment = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "Ожидается оплата");


        ArrayList<OrderEntity> allOrders = new ArrayList<>();
        allOrders.addAll(ordersWaitingPayment);
        allOrders.addAll(ordersDelivering);
        allOrders.addAll(ordersManufacturing);
        allOrders.addAll(orders);
        allOrders.addAll(ordersDelivered);

        return allOrders;
    }

    @Override
    public ArrayList<OrderEntity> getOrdersBuying() {
        ArrayList<OrderEntity> orders = orderRepository.getOrderEntitiesByOrderStatus("Заказ в листе ожидания");
        ArrayList<OrderEntity> ordersManufacturing = orderRepository.getOrderEntitiesByOrderStatus("В процессе производства");
        ArrayList<OrderEntity> ordersDelivering = orderRepository.getOrderEntitiesByOrderStatus("Заказ доставляется");
        ArrayList<OrderEntity> ordersDelivered = orderRepository.getOrderEntitiesByOrderStatus("Заказ доставлен");
        ArrayList<OrderEntity> ordersWaitingPayment = orderRepository.getOrderEntitiesByOrderStatus("Ожидается оплата");

        ArrayList<OrderEntity> allOrders = new ArrayList<>();
        allOrders.addAll(ordersWaitingPayment);
        allOrders.addAll(orders);
        allOrders.addAll(ordersManufacturing);
        allOrders.addAll(ordersDelivering);
        allOrders.addAll(ordersDelivered);

        return allOrders;
    }

    @Override
    public ArrayList<OrderEntity> getOrderByProductId(int id) {
        return orderRepository.getOrderEntitiesByProductId(id);
    }

    @Override
    public int countOfOrdersByProductID(int id) {
        return orderRepository.countByProductId(id);
    }
}
