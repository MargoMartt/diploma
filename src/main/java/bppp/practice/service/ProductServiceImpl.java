package bppp.practice.service;

import bppp.practice.entity.ProductEntity;
import bppp.practice.repository.OrderRepository;
import bppp.practice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional

public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

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
    public void deleteProduct(ProductEntity product) {
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public List<ProductEntity> getAllProducts() {

        ArrayList<ProductEntity> products = (ArrayList<ProductEntity>) productRepository.findAll();
        return products;
    }

    @Override
    public int getCountByType(String type) {
        return productRepository.countByTypeAndIsDeletedFalse(type);
    }

    @Override
    public int getCount() {
        return productRepository.countIsDeletedFalse();
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
    public ArrayList<ProductEntity> sortByPopularityDesc() {
        List<ProductEntity> products = productRepository.findAll();
        ArrayList<ProductEntity> sortedProducts = new ArrayList<>();
        Map<Integer, Integer> productOrderCounts = new HashMap<>();

        for (ProductEntity product : products) {
            int count = orderRepository.countByProductId(product.getProductId());
            productOrderCounts.put(product.getProductId(), count);
        }

        products.sort(Comparator.comparingInt(product -> -productOrderCounts.getOrDefault(product.getProductId(), 0)));

        for (ProductEntity product : products) {
            sortedProducts.add(product);
        }

        return sortedProducts;
    }

    @Override
    public List<ProductEntity> getAllNonDeletedProducts() {
        return productRepository.findAllByIsDeleted(false);
    }

    @Override
    public ArrayList<ProductEntity> sortByCostAscAndType(String type) {
        return productRepository.findAllByOrderByProductCostAndProductTypeAsc(type);
    }

    @Override
    public ArrayList<ProductEntity> sortByCostDescAndType(String type) {
        return productRepository.findAllByOrderByProductCostAndProductTypeDesc(type);
    }


    @Override
    public ArrayList<ProductEntity> sortByPopularityDescAndType(String type) {
        List<ProductEntity> products = productRepository.getProductEntitiesByProductType(type);
        ArrayList<ProductEntity> sortedProducts = new ArrayList<>();
        Map<Integer, Integer> productOrderCounts = new HashMap<>();

        for (ProductEntity product : products) {
            int count = orderRepository.countByProductId(product.getProductId());
            productOrderCounts.put(product.getProductId(), count);
        }

        products.sort(Comparator.comparingInt(product -> -productOrderCounts.getOrDefault(product.getProductId(), 0)));

        for (ProductEntity product : products) {
            sortedProducts.add(product);
        }

        return sortedProducts;
    }

    @Override
    public int countSalesByType(String type) {
        List<ProductEntity> products = productRepository.getProductEntitiesByProductType(type);
        int totalSales = 0;

        for (ProductEntity product : products) {
            int count = orderRepository.countByProductId(product.getProductId());
            totalSales += count;
        }

        return totalSales;
    }

}
