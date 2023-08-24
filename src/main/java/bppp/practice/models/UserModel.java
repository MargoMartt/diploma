package bppp.practice.models;

import bppp.practice.entity.OrderEntity;
import bppp.practice.entity.OrganizationEntity;
import bppp.practice.entity.ProductEntity;
import bppp.practice.entity.UserEntity;
import bppp.practice.enums.OrderStatus;
import bppp.practice.service.OrderService;
import bppp.practice.service.OrganizationService;
import bppp.practice.service.ProductService;
import bppp.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;

@Component
public class UserModel {
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    OrganizationService organizationService;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ArrayList<OrderEntity> ordersInCart(UserEntity user) {
        return orderService.getOrdersInCart(user, OrderStatus.CART.getStatus());
    }

    public ArrayList<ProductEntity> productInCart(ArrayList<OrderEntity> orders) {
        ArrayList<ProductEntity> products = new ArrayList<>();
        for (OrderEntity ord : orders) {
            products.add(ord.getProductByProductId());
        }
        return products;
    }

    public void clearCart(UserEntity user) {
        ArrayList<OrderEntity> orders = orderService.getOrdersInCart(user, OrderStatus.CART.getStatus());
        for (OrderEntity ord : orders) {
            orderService.deleteOrder(ord.getIdOrder());
        }
    }

    public double totalCostInCart(UserEntity user) {
        double totalCost = 0;
        ArrayList<OrderEntity> orders = orderService.getOrdersInCart(user, OrderStatus.CART.getStatus());
        for (OrderEntity ord : orders) {
            totalCost += ord.getOrderCost();
        }
        return totalCost;
    }

    public void buyProduct(int id, UserEntity user, String name, String surname, String city,
                           String street, String phone, String apartment,
                           String date, String time, String type, String payment, String personType) {
        user.setUserName(name);
        user.setUserSurname(surname);
        user.setUserPhone(phone);
        userService.saveUser(user);

        OrderEntity order = orderService.getOrder(id);

        ProductEntity product = order.getProductByProductId();

        order.setOrderAddress(city + " " + street + " " + apartment);
        order.setDate(Date.valueOf(date));
        order.setTime(time);
        order.setOrderType(type);
        order.setOrderPayment(payment);
        order.setOrderCustomerType(personType);
        if (order.getOrderCount() <= product.getProductCount()) {
            product.setProductCount(product.getProductCount() - order.getOrderCount());
            productService.saveProduct(product);
            if (type.equals("Order is delivering"))
                order.setOrderStatus(OrderStatus.DELIVERING.getStatus());
            else
                order.setOrderStatus(OrderStatus.WAITING.getStatus());
        } else {
            order.setOrderStatus(OrderStatus.MANUFACTURING.getStatus());
        }
        orderService.saveOrder(order);
    }

    public String checkDeliver(int id) {
        String response;
        OrderEntity order = orderService.getOrder(id);
        ProductEntity product = order.getProductByProductId();
        if (product.getProductCount() >= order.getOrderCount())
            response = "ok";
        else response = "wait";
        return response;
    }

    public String editUser(UserEntity user, String name, String surname, String login, String phone,
                           String oldPassword, String newPassword, String newPassword2) {
        user.setUserName(name);
        user.setUserSurname(surname);
        user.setLogin(login);
        user.setUserPhone(phone);
        if (newPassword.equals("")) {
            userService.saveUser(user);
        } else {
            if (oldPassword.equals(user.getPassword())) {
                if (!oldPassword.equals(newPassword)) {
                    if (newPassword.equals(newPassword2)) {
                        user.setPassword(passwordEncoder.encode(newPassword));
                        userService.saveUser(user);
                        return "success";
                    } else {
                        userService.saveUser(user);
                        return "new passwords different!";
                    }
                } else {
                    userService.saveUser(user);
                    return "old password & new password are the same";
                }
            } else {
                userService.saveUser(user);
                return "your old password isn't correct";
            }
        }
        return "success";
    }

    public void addOrganization(String name, String type, String responsible, String director,
                                UserEntity user) {
        OrganizationEntity organization = user.getOrganizationEntity();
        if (organization==null)
            organization = new OrganizationEntity();
        organization.setOrganizationName(name);
        organization.setOrganizationResponsible(responsible);
        organization.setOrganizationDirector(director);
        organization.setOrganizationType(type);
        organizationService.saveOrganization(organization);

        user.setOrganizationEntity(organization);
        userService.saveUser(user);
    }
}
