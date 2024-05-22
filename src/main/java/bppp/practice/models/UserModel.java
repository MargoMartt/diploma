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
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public int buyProduct(int id, UserEntity user, String name, String surname, String city,
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
        System.out.println(order.getOrderPayment());
        if (order.getOrderPayment().equals("Перевод на расчетный счет") &&
                order.getOrderCount() <= product.getProductCount()) {
            product.setProductCount(product.getProductCount() - order.getOrderCount());
            productService.saveProduct(product);
            order.setOrderStatus(OrderStatus.WAIT_PAYMENT.getStatus());
            System.out.println("hi");
        } else if (!order.getOrderPayment().equals("Перевод на расчетный счет") &&
                order.getOrderCount() <= product.getProductCount()) {
            product.setProductCount(product.getProductCount() - order.getOrderCount());
            productService.saveProduct(product);
            order.setOrderStatus(OrderStatus.WAITING.getStatus());
            System.out.println("hey");
        } else {
            order.setOrderStatus(OrderStatus.MANUFACTURING.getStatus());
            System.out.println("hoy");
        }
        orderService.saveOrder(order);
        return order.getIdOrder();
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
        if (name != null)
            user.setUserName(name);
        if (surname != null)
            user.setUserSurname(surname);
        user.setLogin(login);
        if (phone != null)
            user.setUserPhone(phone);
        if (newPassword.equals("")) {
            userService.saveUser(user);
        } else {
            String oldPasswordFromDB = user.getPassword();
            if (passwordEncoder.matches(oldPassword, oldPasswordFromDB)) {
                if (!oldPassword.equals(newPassword)) {
                    if (newPassword.equals(newPassword2)) {
                        user.setPassword(passwordEncoder.encode(newPassword));
                        userService.saveUser(user);
                        return "Пароль успешно обновлен";
                    } else {
                        userService.saveUser(user);
                        return "Новые пароли не совпадают";
                    }
                } else {
                    userService.saveUser(user);
                    return "Старый и новый пароли одинаковые";
                }
            } else {
                userService.saveUser(user);
                return "Неверно введен старый пароль";
            }
        }
        return "success";
    }

    public void addOrganization(String name, String type, String responsible, String director,
                                UserEntity user) {
        OrganizationEntity organization = user.getOrganizationEntity();
        if (organization == null)
            organization = new OrganizationEntity();
        organization.setOrganizationName(name);
        organization.setOrganizationResponsible(responsible);
        organization.setOrganizationDirector(director);
        organization.setOrganizationType(type);
        organizationService.saveOrganization(organization);

        user.setOrganizationEntity(organization);
        userService.saveUser(user);
    }


        public String downloadDocument(int id, String organizationType) {

            OrderEntity order = orderService.getOrder(id);
            UserEntity user = order.getUserByIdUser();
            ProductEntity product = productService.getProduct(order.getProductId());

            String organization;
            String name = user.getUserName() + " " + user.getUserSurname();
            if (!organizationType.equals("Физическое лицо")) {
                organization = name;
            } else {
                organization = user.getOrganizationEntity().getOrganizationType() +
                        " " + user.getOrganizationEntity().getOrganizationName();
            }

            String docxOutputFilePath = "/Users/ritamartinkevich/University/practice/src/main/resources/static/documents/document_" + id + ".docx";

            try {
                // Создаем документ DOCX
                XWPFDocument doc = new XWPFDocument();

                // Добавляем заголовок
                XWPFParagraph titleParagraph = doc.createParagraph();
                titleParagraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = titleParagraph.createRun();
                titleRun.setText("ДОГОВОР ПОСТАВКИ № " + id + " от " + order.getDate() + " г. г. " + order.getDate());
                titleRun.setBold(true);
                titleRun.setFontSize(20);
                titleRun.addBreak();

                // Добавляем информацию о сторонах
                XWPFParagraph partiesParagraph = doc.createParagraph();
                partiesParagraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun partiesRun = partiesParagraph.createRun();
                partiesRun.setText("ОАО «БЗПИ», именуемое в дальнейшем «Продавец», в лице Бычека Сергея Анатольевича, действующего на основании начальника отдела сбыта с одной стороны, и " + organization + ", именуемое в дальнейшем «Покупатель» в лице " + name + ", действующего на основании покупателя, с другой стороны, заключили настоящий Договор о нижеследующем:");
                partiesRun.setFontSize(12);
                partiesRun.addBreak();

                addContractContent(doc, order, product, organization);

                FileOutputStream fos = new FileOutputStream(new File(docxOutputFilePath));
                doc.write(fos);
                fos.close();

                return docxOutputFilePath;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private void addContractContent(XWPFDocument doc, OrderEntity order, ProductEntity product, String organization) {
            String[] sections = {
                    "ПРЕДМЕТ ДОГОВОРА",
                    "ОБЯЗАТЕЛЬСТВА СТОРОН",
                    "СРОК И ПОРЯДОК ПОСТАВКИ",
                    "ЦЕНЫ И ПОРЯДОК РАСЧЕТОВ",
                    "ОТВЕТСТВЕННОСТЬ СТОРОН",
                    "ДОПОЛНИТЕЛЬНЫЕ УСЛОВИЯ",
                    "ЦЕЛЬ ПРИОБРЕТЕНИЕ ТОВАРА",
                    "ОБЪЕКТ ПРОДАЖИ",
                    "ЮРИДИЧЕСКИЕ АДРЕСА СТОРОН"
            };

            String[] contents = {
                    "Продавец обязуется передать Покупателю для собственного потребления в согласованном договаривающимися сторонами количестве товар, а Покупатель обязуется принять и оплатить товар.\n" +

                            "Оплата Покупателем производится согласно выставленной Продавцом счет-фактуре безналичным путем на расчетный счет Продавца в банке. \n" +

                            "Счет-фактура является неотъемлемой частью договора.",

                    "Продавец обязуется:\n" +
                            "1. Передать Покупателю товар, указанный в счет-фактуре в течение пяти банковских дней со дня поступления денежных средств на расчетный счет Продавца (при изготовлении под заказ – в течение двух недель со дня согласования макета сторонами).\n" +
                            "Покупатель обязуется:\n" +
                            "1. Оплатить и принять товар, согласно выставленной Продавцом счет-фактуре.",
                    "Днем поставки товара по заявке считается дата отгрузки товара, что подтверждается товарной (товарно-транспортной) накладной.\n" +
                            "Поставка товара  – по согласованию сторон.",
                    "Поставляемый товар по каждой заявке оплачивается по ценам, указанным Продавцом в счет-фактуре, являющейся неотъемлемой частью договора.\n" +
                            "Условия поставки – оплата " + order.getOrderPayment() + " Покупателем товара на сумму " + order.getOrderCost() + ".\n" +
                            "Источник финансирования – расчетный счет покупателя.",
                    "За неисполнение или ненадлежащее исполнение настоящего договора стороны несут ответственность в соответствии с действующим законодательством Республики Беларусь.",
                    "Настоящий Договор вступает в силу с момента его подписания и действует 2 года.\n" +
                            "Договор автоматически пролонгируется на каждый последующий год, если ни одна из сторон не выразила желание прекратить его действие.\n" +
                            "Договор может быть расторгнут по взаимному согласию сторон.\n" +
                            "Стороны признают юридическую силу факсимильных копий договоров, счетов и других документов по договору с последующей заменой их на оригиналы в течение 10-ти календарных дней.",
                    "Для собственного потребления или производства.",
                    "Покупатель приобретает товар " + product.getProductName() + " в количестве " + order.getOrderCount() + " единиц.",

                    "ПРОДАВЕЦ\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t ОАО «БЗПИ» \t\t\t\t\t\t\t\t\t\t\t\t\t\t/________\n \t\t\t\t\t\t\t\t\t\t\t\t\t\t \t\t\t\t\t\t\t\t\t\t\t\t\t\t" +
                            "ПОКУПАТЕЛЬ \t\t\t\t\t\t\t\t\t\t\t\t\t\t "+ organization + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t/________\n"
            };

            // Добавляем каждый пункт договора
            for (int i = 0; i < sections.length; i++) {
                XWPFParagraph paragraph = doc.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun run = paragraph.createRun();
                run.setBold(true);
                run.setText((i + 1) + ". " + sections[i]);
                run.addBreak();
                run = paragraph.createRun();
                run.setText(contents[i]);
                run.addBreak();
                run.addBreak();
            }
        }
    }


//    public String downloadDocument(int id, String organizationType) throws IOException {
//
//        OrderEntity order = orderService.getOrder(id);
//        UserEntity user = order.getUserByIdUser();
//        ProductEntity product = productService.getProduct(order.getProductId());
//
//        String organization;
//        String name = user.getUserName() + " " + user.getUserSurname();
//        if (!organizationType.equals("Физическое лицо")) {
//            organization = name;
//        } else {
//            organization = user.getOrganizationEntity().getOrganizationType() +
//                    " " + user.getOrganizationEntity().getOrganizationName();
//        }
//
//        freemarker.template.Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
//        FileTemplateLoader ftl = new FileTemplateLoader(new File("/Users/ritamartinkevich/University/practice/src/main/resources/static/documents")); // Укажите путь к вашим шаблонам
//        cfg.setTemplateLoader(ftl);
//
//        try {
//            Template template = cfg.getTemplate("document.ftl");
//
//            String outputFilePath = "/Users/ritamartinkevich/University/practice/src/main/resources/static/documents/document_" + id + ".ftl";
//            Writer fileWriter = new FileWriter(new File(outputFilePath));
//
//            Map<String, Object> input = new HashMap<>();
//            input.put("id", id);
//            input.put("date", order.getDate());
//            input.put("organization", organization);
//            input.put("name", name);
//            input.put("payment", order.getOrderPayment());
//            input.put("cost", order.getOrderCost());
//            input.put("product", product.getProductName());
//            input.put("count", order.getOrderCount());
//
//            template.process(input, fileWriter);
//
//            fileWriter.close();
//
//            String docxOutputFilePath = "/Users/ritamartinkevich/University/practice/src/main/resources/static/documents/document_" + id + ".docx";
//
//            // Создаем документ DOCX
//            XWPFDocument doc = new XWPFDocument();
//            FileOutputStream fos = new FileOutputStream(new File(docxOutputFilePath));
//
//            // Создаем параграф и добавляем в него текст из шаблона
//            XWPFParagraph paragraph = doc.createParagraph();
//            XWPFRun run = paragraph.createRun();
//            StringWriter stringWriter = new StringWriter();
//            template.process(input, stringWriter);
//            run.setText(stringWriter.toString());
//
//            // Сохраняем документ в файл
//            doc.write(fos);
//            fos.close();
//
//            // Возвращаем путь к DOCX файлу
//            return docxOutputFilePath;
//        } catch (IOException | TemplateException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
