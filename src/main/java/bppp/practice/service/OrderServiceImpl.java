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
        ArrayList<OrderEntity> orders = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "Order in waiting list");
        ArrayList<OrderEntity> ordersManufacturing = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "In the manufacturing process");
        ArrayList<OrderEntity> ordersDelivering = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "Order is delivering");
        ArrayList<OrderEntity> ordersDelivered = orderRepository.getOrderEntitiesByUserByIdUserAndOrderStatus(user, "Order delivered");

        ArrayList<OrderEntity> allOrders = new ArrayList<>();
        allOrders.addAll(orders);
        allOrders.addAll(ordersManufacturing);
        allOrders.addAll(ordersDelivering);
        allOrders.addAll(ordersDelivered);

        return allOrders;
    }

    @Override
    public ArrayList<OrderEntity> getOrdersBuying() {
        ArrayList<OrderEntity> orders = orderRepository.getOrderEntitiesByOrderStatus("Order in waiting list");
        ArrayList<OrderEntity> ordersManufacturing = orderRepository.getOrderEntitiesByOrderStatus("In the manufacturing process");
        ArrayList<OrderEntity> ordersDelivering = orderRepository.getOrderEntitiesByOrderStatus("Order is delivering");
        ArrayList<OrderEntity> ordersDelivered = orderRepository.getOrderEntitiesByOrderStatus("Order delivered");

        ArrayList<OrderEntity> allOrders = new ArrayList<>();
        allOrders.addAll(orders);
        allOrders.addAll(ordersManufacturing);
        allOrders.addAll(ordersDelivering);
        allOrders.addAll(ordersDelivered);

        return allOrders;
    }
}
