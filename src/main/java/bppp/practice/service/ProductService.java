package bppp.practice.service;

import bppp.practice.entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;

public interface ProductService {
     void saveProduct(ProductEntity product);
     ProductEntity getProduct(int id);
     void deleteProduct(ProductEntity product);
     List<ProductEntity> getAllProducts();int getCountByType(String type);int getCount();
     ArrayList<ProductEntity> getByPrice(double after, double before);ArrayList<ProductEntity> getByType(String type);ArrayList<ProductEntity> sortByCostAsc();ArrayList<ProductEntity> sortByCostDesc();
     ArrayList<ProductEntity> sortByPopularityDesc();
     List<ProductEntity> getAllNonDeletedProducts();
     ArrayList<ProductEntity> sortByCostAscAndType(String type);ArrayList<ProductEntity> sortByCostDescAndType(String type);
     ArrayList<ProductEntity> sortByPopularityDescAndType(String type);

    int countSalesByType (String type);
}
