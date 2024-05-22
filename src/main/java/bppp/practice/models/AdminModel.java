package bppp.practice.models;

import bppp.practice.enums.OrderStatus;
import bppp.practice.enums.ProductType;
import bppp.practice.entity.ProductEntity;
import bppp.practice.service.OrderService;
import bppp.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AdminModel {

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    public void addProduct(String name, String description, String image, double cost, int count, String type) {
        ProductEntity product = new ProductEntity();
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPicture(image);
        product.setProductCost(cost);
        product.setProductCount(count);
        product.setProductType(type);
        product.setIsDeleted(false);
        productService.saveProduct(product);
    }

    public void editProduct(int id, String name, String description, String image, double cost, int count, String type) {
        System.out.println(id);
        ProductEntity product = productService.getProduct(id);
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPicture(image);
        product.setProductCost(cost);
        product.setProductCount(count);
        product.setProductType(type);
        productService.saveProduct(product);
    }

    public void editProductWithoutPicture(int id, String name, String description, double cost, int count, String type) {
        ProductEntity product = productService.getProduct(id);
        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductCost(cost);
        product.setProductCount(count);
        product.setProductType(type);
        productService.saveProduct(product);
    }

    public ArrayList<String> productTypes() {
        ArrayList<String> productTypes = new ArrayList<>();
        productTypes.add(ProductType.PLASTIC.getCode());
        productTypes.add(ProductType.FILM.getCode());
        productTypes.add(ProductType.HOUSEHOLD.getCode());
        productTypes.add(ProductType.REGRANULATE.getCode());
        productTypes.add(ProductType.SHAPES.getCode());
        productTypes.add(ProductType.POLYMER.getCode());
        return productTypes;
    }

    public ArrayList<String> orderStatuses() {
        ArrayList<String> orderStatuses = new ArrayList<>();
        orderStatuses.add(OrderStatus.DELIVERING.getStatus());
        orderStatuses.add(OrderStatus.MANUFACTURING.getStatus());
        orderStatuses.add(OrderStatus.WAITING.getStatus());
        orderStatuses.add(OrderStatus.DELIVERED.getStatus());
        return orderStatuses;
    }

    public ArrayList<Report> productsForReport(ArrayList<ProductEntity> products) {
        ArrayList<Report> report = new ArrayList<>();
        for (ProductEntity product : products) {
            Report rep = new Report();
            int count = orderService.countOfOrdersByProductID(product.getProductId());
            rep.setName(product.getProductName());
            rep.setPhoto(product.getProductPicture());
            rep.setCount(count);
            rep.setCost(product.getProductCost());
            rep.setSum(product.getProductCost() * count);
            rep.setStockCount(product.getProductCount());
            report.add(rep);
        }
        return report;
    }

    public ArrayList<Report> calculateOods(ArrayList<Report> reports) {
        ArrayList<Report> report = new ArrayList<>();
        for (Report rep : reports) {
            Double ood = (rep.getStockCount() * 0.3) + (rep.getCount() * 0.5) + (rep.getSum() * 0.2);
            rep.setOod(ood);
            report.add(rep);
        }
        return report;
    }

    public ArrayList<Chart> getInfoForChart() {
        ArrayList<Chart> charts = new ArrayList<>();
        Chart chart = new Chart();

        int count = productService.countSalesByType(ProductType.PLASTIC.getCode());
        chart.setSalesCount(count);
        chart.setProductType(ProductType.PLASTIC.getCode());
        charts.add(chart);
        chart = new Chart();

        count = productService.countSalesByType(ProductType.SHAPES.getCode());
         chart.setSalesCount(count);
        chart.setProductType(ProductType.SHAPES.getCode());
        charts.add(chart);
        chart = new Chart();

        count = productService.countSalesByType(ProductType.POLYMER.getCode());
         chart.setSalesCount(count);
        chart.setProductType(ProductType.POLYMER.getCode());
        charts.add(chart);
        chart = new Chart();

        count = productService.countSalesByType(ProductType.REGRANULATE.getCode());
         chart.setSalesCount(count);
        chart.setProductType(ProductType.REGRANULATE.getCode());
        charts.add(chart);
        chart = new Chart();

        count = productService.countSalesByType(ProductType.FILM.getCode());
         chart.setSalesCount(count);
        chart.setProductType(ProductType.FILM.getCode());
        charts.add(chart);
        chart = new Chart();

        count = productService.countSalesByType(ProductType.HOUSEHOLD.getCode());
         chart.setSalesCount(count);
        chart.setProductType(ProductType.HOUSEHOLD.getCode());
        charts.add(chart);

        return charts;
    }
}
