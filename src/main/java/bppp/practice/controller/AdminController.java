package bppp.practice.controller;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.models.AdminModel;
import bppp.practice.entity.ProductEntity;
import bppp.practice.models.Chart;
import bppp.practice.models.ProductsModel;
import bppp.practice.models.Report;
import bppp.practice.service.OrderService;
import bppp.practice.service.ProductService;
import bppp.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    ProductService productService;
    @Autowired
    AdminModel adminModel;

    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductsModel productsModel;

    @Autowired
    Report report;

    @GetMapping("/actions")
    public String adminsAction(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        model.addAttribute("user", userDB);

        ArrayList<OrderEntity> orders = orderService.getOrdersBuying();
        model.addAttribute("orders", orders);

        ArrayList<UserEntity> allUsers = (ArrayList<UserEntity>) userService.getAllUser();
        model.addAttribute("allUsers", allUsers);

        ArrayList<String> userStatus = new ArrayList<>();
        userStatus.add("Активный");
        userStatus.add("Заблокирован");
        model.addAttribute("userStatus", userStatus);

        ArrayList<String> userType = new ArrayList<>();
        for (UserEntity user : allUsers) {
            if (user.getOrganizationEntity() != null)
                userType.add("Физическое лицо");
            else userType.add("Юридическое лицо");
        }
        model.addAttribute("userType", userType);

        List<ProductEntity> products = new ArrayList<>();
        ProductEntity product = new ProductEntity("Новый продукт", "", 0.0, 0, "Пожалуйста, напишите описание продукта", "/images/product/new.png");
        products.add(product);
        products.addAll(productService.getAllNonDeletedProducts());
        model.addAttribute("products", products);

        ArrayList<String> productTypes = adminModel.productTypes();
        model.addAttribute("productTypes", productTypes);

        ArrayList<String> orderStatuses = adminModel.orderStatuses();
        model.addAttribute("orderStatuses", orderStatuses);

        return "admin";
    }

    @GetMapping("/statistics")
    public String adminsStatistics(Model model) {
        List<ProductEntity> products = new ArrayList<>();
        products.addAll(productService.sortByPopularityDesc());

        List<ProductEntity> filteredProducts;

        filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        int countType = filteredProducts.size();
        model.addAttribute("count", countType);

        ArrayList<Report> report = adminModel.productsForReport((ArrayList<ProductEntity>) filteredProducts);
        model.addAttribute("report", report);

        ArrayList<Report> oods = adminModel.calculateOods(report);
        model.addAttribute("oods", oods);

        ArrayList<Chart> chartData = adminModel.getInfoForChart();
        model.addAttribute("chartData", chartData);

        for (Chart c:chartData) {
            System.out.println(c.getProductType() + " " + c.getSalesCount() );
        }
        return "statistic";
    }
    @PostMapping("/actions/products/{id}")
    public String editProduct(@PathVariable(name = "id") int id,
                              @RequestParam(name = "name", required = false) String name,
                              @RequestParam(name = "description", required = false) String description,
                              @RequestParam(name = "file", required = false) MultipartFile file,
                              @RequestParam(name = "cost", required = false) double cost,
                              @RequestParam(name = "count", required = false) int count,
                              @RequestParam(name = "type", required = false) String type) throws IOException {
        System.out.println("FILE " + file.getOriginalFilename());
        if (file != null) {
            System.out.println("FILE: " + file.getOriginalFilename());
        } else {
            System.out.println("No file attached.");
        }

        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename(); // Получаем имя файла
            String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
            String uploadDir = "/Users/ritamartinkevich/University/practice/src/main/resources/static/images/product/";
            String filePath = uploadDir + fileName;
            File destination = new File(filePath);
            file.transferTo(destination);
            String picture = "/images/product/" + fileName;
            if (id != 0) {
                ProductEntity product = productService.getProduct(id);
                if (!Objects.equals(product.getProductPicture(), "") || product.getProductPicture() != null) {
                    adminModel.editProduct(id, name, description, picture, cost, count, type);
                } else {
                    adminModel.editProductWithoutPicture(id, name, description, cost, count, type);
                }
            } else {
                adminModel.addProduct(name, description, picture, cost, count, type);
            }
        } else {
            if (id != 0) {
                adminModel.editProductWithoutPicture(id, name, description, cost, count, type);
            } else {
                adminModel.addProduct(name, description, "", cost, count, type);
            }
        }
        return "redirect:/admin/actions";
    }


    @PostMapping("actions/orderStatus/{id}")
    public String editOrderStatus(@PathVariable(name = "id") int id, @RequestParam(name = "status") String status) {
        OrderEntity order = orderService.getOrder(id);
        order.setOrderStatus(status);
        orderService.saveOrder(order);
        return "redirect:/admin/actions";
    }

    @GetMapping("/actions/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id, Model model) {
        ProductEntity product = productService.getProduct(id);
        productService.deleteProduct(product);
        return "redirect:/admin/actions";
    }

    @PostMapping("/actions/userStatus/{id}")
    public String editUserStatus(@PathVariable(name = "id") int id, @RequestParam(name = "status") String status) {
        UserEntity user = userService.getUser(id);
        user.setStatus(status);
        userService.saveUser(user);
        return "redirect:/admin/actions";
    }


}
