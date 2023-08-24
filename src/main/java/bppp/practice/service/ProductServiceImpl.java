package bppp.practice.service;

import bppp.practice.entity.ProductEntity;
import bppp.practice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Override
    public void saveProduct(ProductEntity product) {
        productRepository.save(product);
    }

    @Override
    public ProductEntity getProduct(int id) {
        ProductEntity product = null;
        Optional<ProductEntity> optional = productRepository.findById(id);
        if (optional.isPresent()) {
            product = optional.get();
        }
        return product;
    }

    @Override
    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public int getCountByType(String type) {
        return productRepository.countProductEntitiesByProductType(type);
    }

    @Override
    public ArrayList<ProductEntity> getByPrice(double after, double before) {
        return productRepository.getProductEntitiesByProductCostAfterAndProductCostBefore(after, before);
    }

    @Override
    public ArrayList<ProductEntity> getByType(String type) {
        return productRepository.getProductEntitiesByProductType(type);
    }

    @Override
    public ArrayList<ProductEntity> sortByCostAsc() {
        return productRepository.findAllByOrderByProductCostAsc();
    }

    @Override
    public ArrayList<ProductEntity> sortByCostDesc() {
        return productRepository.findAllByOrderByProductCostDesc();
    }

    @Override
    public ArrayList<ProductEntity> sortByCountDesc() {
        return productRepository.findAllByOrderByProductCountDesc();
    }
}
