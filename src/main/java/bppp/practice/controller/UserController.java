package bppp.practice.controller;

import bppp.practice.entity.*;
import bppp.practice.models.UserModel;
import bppp.practice.service.OrderService;
import bppp.practice.service.OrganizationService;
import bppp.practice.service.UserHasRoleService;
import bppp.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserModel userModel;
    @Autowired
    OrderService orderService;
    @Autowired
    OrganizationService organizationService;

    @Autowired
    UserHasRoleService userHasRoleService;

    @GetMapping("/cart")
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        ArrayList<OrderEntity> orders = userModel.ordersInCart(userDB);
        ArrayList<ProductEntity> products = userModel.productInCart(orders);
        double totalCost = userModel.totalCostInCart(userDB);

        model.addAttribute("orders", orders);
        model.addAttribute("products", products);
        model.addAttribute("totalCost", totalCost);
        return "cart";
    }

    @PostMapping("/cart/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") int id) {
        orderService.deleteOrder(id);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{id}")
    public String updateProduct(@RequestParam(name = "count") int count,
                                @PathVariable(name = "id") int id) {
        OrderEntity order = orderService.getOrder(id);
        order.setOrderCount(count);
        order.setOrderCost(order.getProductByProductId().getProductCost() * count);
        orderService.saveOrder(order);
        return "redirect:/cart";
    }

    @GetMapping("/cart/clear")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        userModel.clearCart(userDB);
        return "redirect:/cart";
    }

    @GetMapping("/cart/buy/{id}")
    public String buyProduct(@PathVariable(name = "id") int id,
                             @AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        model.addAttribute("user", userDB);
        OrderEntity order = orderService.getOrder(id);
        model.addAttribute("order", order);
        String response = userModel.checkDeliver(id);
        System.out.println(response);

        String dateDeliver;
        if (response.equals("ok")) {
            dateDeliver = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else
            dateDeliver = LocalDate.now().plusWeeks(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("dateDeliver", dateDeliver);


        return "checkout";
    }

    @PostMapping("/cart/buy/{id}")
    public String buyProduct(@PathVariable(name = "id") int id,
                             @RequestParam(name = "name") String name,
                             @RequestParam(name = "surname") String surname,
                             @RequestParam(name = "city") String city,
                             @RequestParam(name = "street") String street,
                             @RequestParam(name = "phone") String phone,
                             @RequestParam(name = "apartment") String apartment,
                             @RequestParam(name = "date") String date,
                             @RequestParam(name = "time") String time,
                             @RequestParam(name = "type") String type,
                             @RequestParam(name = "payment") String payment,
                             @RequestParam(name = "personType") String personType,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {

        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        userModel.buyProduct(id, userDB, name, surname, city, street, phone, apartment, date, time, type, payment, personType);

        return "redirect:/account";

    }

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        UserHasRoleEntity role = userHasRoleService.getRoleByIdUser(userDB.getUserId());

        if (role.getRoleIdRole() == 2)
            return "redirect:/admin/actions";

        model.addAttribute("user", userDB);
        ArrayList<OrderEntity> orders = orderService.getUsersOrders(userDB);
        model.addAttribute("orders", orders);
        OrganizationEntity organization = userDB.getOrganizationEntity();
        if (organization == null)
            organization = new OrganizationEntity();
        model.addAttribute("organization", organization);

        ArrayList<String> types = new ArrayList<>();
        types.add("Individual entrepreneur");
        types.add("Unitary Enterprise");
        types.add("Limited Liability Company");
        types.add("Additional Liability Company");
        types.add("Public Corporation");
        types.add("Closed Joint Stock Company");

        model.addAttribute("types", types);

        return "my-account";
    }

    @PostMapping("/account/editUser")
    public String editUser(@RequestParam(name = "name") String name,
                           @RequestParam(name = "surname") String surname,
                           @RequestParam(name = "login") String login,
                           @RequestParam(name = "phone") String phone,
                           @RequestParam(name = "oldPassword") String oldPassword,
                           @RequestParam(name = "newPassword") String newPassword,
                           @RequestParam(name = "newPassword2") String newPassword2,
                           @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        String result = userModel.editUser(userDB, name, surname, login, phone,
                oldPassword, newPassword, newPassword2);
        System.out.println(result);
        return "redirect:/account";
    }

    @PostMapping("/account/organization")
    public String editOrganization(@RequestParam(name = "name") String name,
                                   @RequestParam(name = "type") String type,
                                   @RequestParam(name = "responsible") String responsible,
                                   @RequestParam(name = "director") String director,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        userModel.addOrganization(name, type, responsible, director, userDB);
        return "redirect:/account";
    }
}
