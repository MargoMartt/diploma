package bppp.practice.controller;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.ProductEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.enums.OrderStatus;
import bppp.practice.models.ProductsModel;
import bppp.practice.service.OrderService;
import bppp.practice.service.ProductService;
import bppp.practice.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ProductsModel productsModel;
    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @GetMapping("/info/{id}")
    public String productInfo(@PathVariable(name = "id") int id, Model model) {
        ProductEntity product = productService.getProduct(id);
        model.addAttribute("product", product);

        ArrayList<ProductEntity> products = productService.getByType(product.getProductType());
        ProductEntity productSale = new ProductEntity();
        for (ProductEntity pr : products) {
            if (!pr.getProductName().equals(product.getProductName())) {
                productSale = pr;
                break;
            }
        }
        model.addAttribute("productSale", productSale);
        return "product-details";
    }

    @PostMapping("incart/{id}")
    public String addInCart(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable(name = "id") int id) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        ProductEntity product = productService.getProduct(id);
        productsModel.inCart(userDB, product);
        return "redirect:/product/all";
    }

    @PostMapping("/info/{id}")
    public String productInfo(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable(name = "id") int id,
                              @RequestParam(name = "count") int count,
                              Model model) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        ProductEntity product = productService.getProduct(id);

        productsModel.inCart(userDB, product, count);

        return "redirect:/product/info/{id}";

    }

    @GetMapping("/sorting-products/{type}")
    public String sortProducts(@PathVariable(name = "type") String type, Model model) {
        List<ProductEntity> products = new ArrayList<>();
        switch (type) {
            case ("costAsc"):
                products = productService.sortByCostAsc();
            case ("costDesc"):
                products = productService.sortByCostDesc();
            case ("countDesc"):
                products = productService.sortByCountDesc();
        }
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/filter-products")
    public String filterProducts(@RequestParam("price") String price, Model model) {

        String decodedPriceRange = java.net.URLDecoder.decode(price, StandardCharsets.UTF_8);
        String[] priceParts = decodedPriceRange.split(" - "); // Разделение строки на части

        String minPriceString = priceParts[0].replace("$", ""); // Удаление символа "$"
        String maxPriceString = priceParts[1].replace("$", ""); // Удаление символа "$"

        double minPrice = Double.parseDouble(minPriceString);
        double maxPrice = Double.parseDouble(maxPriceString);

        List<ProductEntity> products = productService.getByPrice(minPrice, maxPrice);
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/all")
    public String viewProducts(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/pressShapes")
    public String viewPress(Model model) {
        List<ProductEntity> products = productService.getByType("Press shapes");
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/polymerProducts")
    public String viewPolymer(Model model) {
        List<ProductEntity> products = productService.getByType("Polymer products");
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/filmProducts")
    public String viewFilm(Model model) {
        List<ProductEntity> products = productService.getByType("Film products");
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/householdChemicals")
    public String viewHouseholdChemicals(Model model) {
        List<ProductEntity> products = productService.getByType("Household chemicals");
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/regranulate")
    public String viewRegranulate(Model model) {
        List<ProductEntity> products = productService.getByType("Regranulate");
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/plasticProducts")
    public String viewPlastic(Model model) {
        List<ProductEntity> products = productService.getByType("Plastic products");
        model.addAttribute("products", products);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }
}

