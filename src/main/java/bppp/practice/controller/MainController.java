package bppp.practice.controller;

import bppp.practice.entity.ProductEntity;
import bppp.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Controller
public class MainController {
    @Autowired
    ProductService productService;

    @RequestMapping("/")
    public String mainInfo(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<ProductEntity> products = productService.getAllProducts();

        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        return "index";
    }

    @GetMapping("/about-us")
    public String aboutUs() {
        return "about-us";
    }

    @GetMapping("/contact")
    public String contactUs() {
        return "contact-us";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

}
