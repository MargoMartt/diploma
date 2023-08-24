package bppp.practice.controller;

import bppp.practice.enums.ProductType;
import bppp.practice.models.AdminModel;
import bppp.practice.entity.ProductEntity;
import bppp.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/productAction")
    public String productData(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "productData";
    }

    @GetMapping("/productAction/edit/{id}")
    public String editProduct(@PathVariable(name = "id") int id, Model model) {
        ProductEntity productEntity = productService.getProduct(id);
        model.addAttribute("product", productEntity);

        ArrayList<ProductType> types = adminModel.productTypes();
        model.addAttribute("types", types);
        return "productEdit";
    }

    @PostMapping("/productAction/edit/{id}")
    public String editProduct(@PathVariable(name = "id") int id,
                              @RequestParam(name = "name", required = false) String name,
                              @RequestParam(name = "description", required = false) String description,
                              @RequestParam(name = "file", required = false) MultipartFile file,
                              @RequestParam(name = "cost", required = false) double cost,
                              @RequestParam(name = "count", required = false) int count,
                              @RequestParam(name = "type", required = false) String type) throws IOException {
        System.out.println(file.getOriginalFilename().toString());
        String image = "E:/Rita/универ/6 sem/practice/practice/src/main/resources/static/images/product/" + file.getOriginalFilename();
        File destination = new File(image);
        file.transferTo(destination);

        String picture = "/images/product/" + file.getOriginalFilename();
        adminModel.editProduct(id, name, description, picture, cost, count, type);
        return "redirect:/admin/productAction";
    }

    @GetMapping("/productAction/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id, Model model) {
        productService.deleteProduct(id);
        return "redirect:/admin/productAction";
    }

    @GetMapping("/productAction/add")
    public String addProduct(Model model) {
        ArrayList<ProductType> types = adminModel.productTypes();
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
