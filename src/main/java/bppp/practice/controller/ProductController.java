package bppp.practice.controller;

import bppp.practice.entity.ProductEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.enums.ProductType;
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
import java.util.stream.Collectors;

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
                              @RequestParam(name = "count") int count) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        ProductEntity product = productService.getProduct(id);

        productsModel.inCart(userDB, product, count);

        return "redirect:/product/info/{id}";

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

        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/all")
    public String viewProducts(Model model) {
        List<ProductEntity> products = productService.getAllProducts();
        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());
        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("all/sorting-products/{type}")
    public String sortProducts(@PathVariable(name = "type") String type, Model model) {
        List<ProductEntity> products = new ArrayList<>();
        List<ProductEntity> filteredProducts;
        switch (type) {
            case ("costAsc"):
                products = productService.sortByCostAsc();
                break;
            case ("costDesc"):
                products = productService.sortByCostDesc();
                break;
            case ("countDesc"):
                products = productService.sortByPopularityDesc();
                break;
        }
        filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());
        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("plasticProducts/sorting-products/{type}")
    public String sortPlasticProducts(@PathVariable(name = "type") String type, Model model) {
        List<ProductEntity> products;
        List<ProductEntity> filteredProducts = new ArrayList<>();
        switch (type) {
            case ("costAsc"):
                products = productService.sortByCostAscAndType(ProductType.PLASTIC.getCode());
                filteredProducts = products.stream()
                        .filter(product -> !product.getIsDeleted())
                        .collect(Collectors.toList());
                break;
            case ("costDesc"):
                products = productService.sortByCostDescAndType(ProductType.PLASTIC.getCode());
                filteredProducts = products.stream()
                        .filter(product -> !product.getIsDeleted())
                        .collect(Collectors.toList());
                break;
            case ("countDesc"):
                products = productService.sortByPopularityDescAndType(ProductType.PLASTIC.getCode());
                filteredProducts = products.stream()
                        .filter(product -> !product.getIsDeleted())
                        .collect(Collectors.toList());
                break;
        }
        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/pressShapes/sorting-products/{type}")
    public String sortPressShapes(@PathVariable(name = "type") String type, Model model) {
        List<ProductEntity> products;
        List<ProductEntity> filteredProducts = new ArrayList<>();
        switch (type) {
            case ("costAsc"):
                products = productService.sortByCostAscAndType(ProductType.SHAPES.getCode());
                filteredProducts = products.stream()
                        .filter(product -> !product.getIsDeleted())
                        .collect(Collectors.toList());
                break;
            case ("costDesc"):
                products = productService.sortByCostDescAndType(ProductType.SHAPES.getCode());
                filteredProducts = products.stream()
                        .filter(product -> !product.getIsDeleted())
                        .collect(Collectors.toList());
                break;
            case ("countDesc"):
                products = productService.sortByPopularityDescAndType(ProductType.SHAPES.getCode());
                filteredProducts = products.stream()
                        .filter(product -> !product.getIsDeleted())
                        .collect(Collectors.toList());
                break;
        }
        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }


    @GetMapping("/pressShapes")
    public String viewPress(Model model) {
        List<ProductEntity> products = productService.getByType("Пресс-форма");
        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/polymerProducts/sorting-products/{type}")
    public String sortPolymerProducts(@PathVariable(name = "type") String type, Model model) {
        List<ProductEntity> products = new ArrayList<>();
        List<ProductEntity> filteredProducts;
        switch (type) {
            case ("costAsc"):
                products = productService.sortByCostAscAndType(ProductType.POLYMER.getCode());
                break;
            case ("costDesc"):
                products = productService.sortByCostDescAndType(ProductType.POLYMER.getCode());
                break;
            case ("countDesc"):
                products = productService.sortByPopularityDescAndType(ProductType.POLYMER.getCode());
                break;
        }
        filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());
        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/polymerProducts")
    public String viewPolymer(Model model) {
        List<ProductEntity> products = productService.getByType("Полимерная продукция");
        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/filmProducts/sorting-products/{type}")
    public String sortFilmProducts(@PathVariable(name = "type") String type, Model model) {
        List<ProductEntity> products = new ArrayList<>();
        List<ProductEntity> filteredProducts;
        switch (type) {
            case ("costAsc"):
                products = productService.sortByCostAscAndType(ProductType.FILM.getCode());
                break;
            case ("costDesc"):
                products = productService.sortByCostDescAndType(ProductType.FILM.getCode());
                break;
            case ("countDesc"):
                products = productService.sortByPopularityDescAndType(ProductType.FILM.getCode());
                break;
        }
        filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());
        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/filmProducts")
    public String viewFilm(Model model) {
        List<ProductEntity> products = productService.getByType("Пленочная продукция");
        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/householdChemicals/sorting-products/{type}")
    public String sortHouseholdChemicals(@PathVariable(name = "type") String type, Model model) {
        List<ProductEntity> products = new ArrayList<>();
        List<ProductEntity> filteredProducts;
        switch (type) {
            case ("costAsc"):
                products = productService.sortByCostAscAndType(ProductType.HOUSEHOLD.getCode());
                break;
            case ("costDesc"):
                products = productService.sortByCostDescAndType(ProductType.HOUSEHOLD.getCode());
                break;
            case ("countDesc"):
                products = productService.sortByPopularityDescAndType(ProductType.HOUSEHOLD.getCode());
                break;
        }
        filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());
        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/householdChemicals")
    public String viewHouseholdChemicals(Model model) {
        List<ProductEntity> products = productService.getByType("Бытовая химия");
        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/regranulate/sorting-products/{type}")
    public String sortRegranulate(@PathVariable(name = "type") String type, Model model) {
        List<ProductEntity> products = new ArrayList<>();
        List<ProductEntity> filteredProducts;
        switch (type) {
            case ("costAsc"):
                products = productService.sortByCostAscAndType(ProductType.REGRANULATE.getCode());
                break;
            case ("costDesc"):
                products = productService.sortByCostDescAndType(ProductType.REGRANULATE.getCode());
                break;
            case ("countDesc"):
                products = productService.sortByPopularityDescAndType(ProductType.REGRANULATE.getCode());
                break;
        }
        filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());
        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/regranulate")
    public String viewRegranulate(Model model) {
        List<ProductEntity> products = productService.getByType("Регранулят");
        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }

    @GetMapping("/plasticProducts")
    public String viewPlastic(Model model) {
        List<ProductEntity> products = productService.getByType("Пластмассовая продукция");
        List<ProductEntity> filteredProducts = products.stream()
                .filter(product -> !product.getIsDeleted())
                .collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        ArrayList<Integer> countType = productsModel.countProducts();
        model.addAttribute("count", countType);
        return "shop-sidebar";
    }
}

