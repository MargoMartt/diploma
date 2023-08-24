package bppp.practice.repository;

import bppp.practice.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    public int countProductEntitiesByProductType(String type);
    public ArrayList<ProductEntity> getProductEntitiesByProductCostAfterAndProductCostBefore(double after, double before);
    public ArrayList<ProductEntity> getProductEntitiesByProductType(String type);

    public ArrayList<ProductEntity> findAllByOrderByProductCostAsc();
    public ArrayList<ProductEntity> findAllByOrderByProductCostDesc();
    public ArrayList<ProductEntity> findAllByOrderByProductCountDesc();
}
