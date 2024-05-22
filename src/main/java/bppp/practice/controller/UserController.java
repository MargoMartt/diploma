package bppp.practice.controller;

import bppp.practice.entity.*;
import bppp.practice.models.UserModel;
import bppp.practice.service.OrderService;
import bppp.practice.service.OrganizationService;
import bppp.practice.service.UserHasRoleService;
import bppp.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails) {
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
    public ResponseEntity<Resource> buyProduct(@PathVariable(name = "id") int id,
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
                                               @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        int idOrder = userModel.buyProduct(id, userDB, name, surname, city, street, phone, apartment, date, time, type, payment, personType);

        String documentPath = userModel.downloadDocument(idOrder, personType);

        File file = new File(documentPath);
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document_" + idOrder + ".docx");

        try {
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(Files.size(file.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

//    @PostMapping("/cart/buy/{id}")
//    public void buyProduct(@PathVariable(name = "id") int id,
//                           @RequestParam(name = "name") String name,
//                           @RequestParam(name = "surname") String surname,
//                           @RequestParam(name = "city") String city,
//                           @RequestParam(name = "street") String street,
//                           @RequestParam(name = "phone") String phone,
//                           @RequestParam(name = "apartment") String apartment,
//                           @RequestParam(name = "date") String date,
//                           @RequestParam(name = "time") String time,
//                           @RequestParam(name = "type") String type,
//                           @RequestParam(name = "payment") String payment,
//                           @RequestParam(name = "personType") String personType,
//                           @AuthenticationPrincipal UserDetails userDetails,
//                           Model model,
//                           HttpServletResponse response) throws IOException {
//
//        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
//        int idOrder = userModel.buyProduct(id, userDB, name, surname, city, street, phone, apartment, date, time, type, payment, personType);
//
//        String documentPath = userModel.downloadDocument(idOrder, personType);
//
//        File file = new File(documentPath);
//        if (!file.exists()) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND);
//            return;
//        }
//
//        Resource resource = new FileSystemResource(file);
//
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document_" + idOrder + ".docx");
//        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//
//        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
//        response.flushBuffer();
//
//        // После отправки файла производим перенаправление
//        response.sendRedirect("/account");
//    }

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam(name = "tab", required = false, defaultValue = "dashboad") String tab,
                          Model model) {
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
        types.add("Индивидуальный предприниматель");
        types.add("Унитарное предприятие");
        types.add("Общество с ограниченной ответственностью");
        types.add("Общество с дополнительная ответственность");
        types.add("Открытое акционерное общество");
        types.add("Закрытое акционерное общество");

        model.addAttribute("types", types);
        model.addAttribute("activeTab", tab);

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
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        UserEntity userDB = userService.getByLogin(userDetails.getUsername());
        String result = userModel.editUser(userDB, name, surname, login, phone,
                oldPassword, newPassword, newPassword2);
        System.out.println(result);

        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("message", "Изменения успешно сохранены.");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } else {
            redirectAttributes.addFlashAttribute("message", result);
            redirectAttributes.addFlashAttribute("messageType", "error");
        }

        return "redirect:/account?tab=account-info";
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
