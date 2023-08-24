package bppp.practice.controller;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.OrganizationEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.enums.OrderStatus;
import bppp.practice.enums.ProductType;
import bppp.practice.models.AdminModel;
import bppp.practice.entity.ProductEntity;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/actions")
    public String adminsAction(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        model.addAttribute("user", userDB);

        ArrayList<OrderEntity> orders = orderService.getOrdersBuying();
        model.addAttribute("orders", orders);

        ArrayList<UserEntity> allUsers = (ArrayList<UserEntity>) userService.getAllUser();
        model.addAttribute("allUsers", allUsers);

        List<ProductEntity> products = new ArrayList<>();
        ProductEntity product = new ProductEntity("New product", "", 0.0, 0, "Please, write product description", "/images/product/new.png");
        products.add(product);
        products.addAll(productService.getAllProducts());
        model.addAttribute("products", products);

        ArrayList<String> productTypes = adminModel.productTypes();
        model.addAttribute("productTypes", productTypes);

        ArrayList<String> orderStatuses = adminModel.orderStatuses();
        model.addAttribute("orderStatuses", orderStatuses);

        return "admin";
    }

    @PostMapping("actions/orderStatus/{id}")
    public String editOrderStatus(@PathVariable(name = "id") int id, @RequestParam(name = "status") String status) {
        OrderEntity order = orderService.getOrder(id);
        order.setOrderStatus(status);
        orderService.saveOrder(order);
        return "redirect:/admin/actions";
    }

    @PostMapping("/actions/products/{id}")
    public String editProduct(@PathVariable(name = "id") int id,
                              @RequestParam(name = "name", required = false) String name,
                              @RequestParam(name = "description", required = false) String description,
                              @RequestParam(name = "file", required = false) MultipartFile file,
                              @RequestParam(name = "cost", required = false) double cost,
                              @RequestParam(name = "count", required = false) int count,
                              @RequestParam(name = "type", required = false) String type) throws IOException {
        String picture = "";
        if (file != null) {
            String image = "E:/Rita/универ/6 sem/practice/practice/src/main/resources/static/images/product/" + file.getOriginalFilename();
            File destination = new File(image);
            file.transferTo(destination);
            picture = "/images/product/" + file.getOriginalFilename();
        }
        if (picture != "" && id != 0) {
            adminModel.editProduct(id, name, description, picture, cost, count, type);
        }
        if (picture == "" && id != 0)
            adminModel.editProductWithoutPicture(id, name, description, cost, count, type);

        if (id == 0) {
            adminModel.addProduct(name, description, picture, cost, count, type);
        }

        return "redirect:/admin/actions";
    }

    @GetMapping("/productAction/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id, Model model) {
        productService.deleteProduct(id);
        return "redirect:/admin/productAction";
    }

    @GetMapping("/productAction/add")
    public String addProduct(Model model) {
        ArrayList<String> types = adminModel.productTypes();
        model.addAttribute("types", types);
        return "productAdd";
    }

    @PostMapping("/productAction/add")
    public String addProduct(@RequestParam(name = "name", required = false) String name,
                             @RequestParam(name = "description", required = false)
                             String description,
                             @RequestParam(name = "file", required = false) MultipartFile file,
                             @RequestParam(name = "cost", required = false)
                             double cost,
                             @RequestParam(name = "count", required = false)
                             int count,
                             @RequestParam(name = "type", required = false)
                             String type) throws IOException {
        System.out.println(file.getOriginalFilename().toString());
        String image = "E:/Rita/универ/6 sem/practice/practice/src/main/resources/static/images/product/" + file.getOriginalFilename();
        File destination = new File(image);
        file.transferTo(destination);

        String picture = "/images/product/" + file.getOriginalFilename();
        adminModel.addProduct(name, description, picture, cost, count, type);
        return "redirect:/admin/productAction";
    }


}
