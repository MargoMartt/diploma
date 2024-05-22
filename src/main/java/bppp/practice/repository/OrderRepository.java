package bppp.practice.repository;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
     ArrayList<OrderEntity> getOrderEntitiesByUserByIdUserAndOrderStatus(UserEntity user, String status);
     ArrayList<OrderEntity> getOrderEntitiesByOrderStatus(String status);
     ArrayList<OrderEntity> getOrderEntitiesByProductId(int id);

     int countByProductId(int id);

}
