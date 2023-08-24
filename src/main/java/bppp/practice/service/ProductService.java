package bppp.practice.service;

import bppp.practice.entity.ProductEntity;
import bppp.practice.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {
    public void saveProduct(ProductEntity product);

    public ProductEntity getProduct(int id);

    public void deleteProduct(int id);

    public List<ProductEntity> getAllProducts();
    public int getCountByType(String type);
    public ArrayList<ProductEntity> getByPrice(double after, double before);
    public ArrayList<ProductEntity> getByType(String type);
    public ArrayList<ProductEntity> sortByCostAsc();
    public ArrayList<ProductEntity> sortByCostDesc();
    public ArrayList<ProductEntity> sortByCountDesc();
}
