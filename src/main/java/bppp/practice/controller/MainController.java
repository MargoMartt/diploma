package bppp.practice.controller;

import bppp.practice.entity.ProductEntity;
import bppp.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Component
@Controller
public class MainController {
    @Autowired
    ProductService productService;

    @RequestMapping("/")
    public String mainInfo(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }
}
