package bppp.practice.repository;

import bppp.practice.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    ArrayList<ProductEntity> getProductEntitiesByProductCostAfterAndProductCostBefore(double after, double before);

    ArrayList<ProductEntity> getProductEntitiesByProductType(String type);

    ArrayList<ProductEntity> findAllByOrderByProductCostAsc();

    ArrayList<ProductEntity> findAllByOrderByProductCostDesc();

    @Query("SELECT p FROM ProductEntity p WHERE p.productType = :type ORDER BY p.productCost ASC")
    ArrayList<ProductEntity> findAllByOrderByProductCostAndProductTypeAsc(@Param("type") String type);

    @Query("SELECT p FROM ProductEntity p WHERE p.productType = :type ORDER BY p.productCost DESC")
    ArrayList<ProductEntity> findAllByOrderByProductCostAndProductTypeDesc(@Param("type") String type);

    ArrayList<ProductEntity> findAllByIsDeleted(boolean isDeleted);


    @Query("SELECT COUNT(p) FROM ProductEntity p WHERE p.productType = :type AND p.isDeleted = false")
    int countByTypeAndIsDeletedFalse(@Param("type") String type);

    @Query("SELECT COUNT(p) FROM ProductEntity p WHERE p.isDeleted = false")
    int countIsDeletedFalse();

}
