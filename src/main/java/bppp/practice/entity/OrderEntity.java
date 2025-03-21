package bppp.practice.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Getter
@Entity
@Table(name = "orders", schema = "bzpi")
@NoArgsConstructor
public class OrderEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_order", nullable = false)
    @Setter
    private int idOrder;

    @Column(name = "order_time", nullable = true, length = 45)
    @Setter
    private String time;

    @Column(name = "order_date", nullable = true, length = 45)
    @Setter
    private Date date;

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @Column(name = "order_count", nullable = true, length = 45)
    @Setter
    private int orderCount;

    @Column(name = "order_address", nullable = true, length = 45)
    @Setter
    private String orderAddress;

    @Column(name = "order_cost", nullable = true, length = 45)
    @Setter
    private Double orderCost;

    @Column(name = "order_status", nullable = true, length = 45)
    @Setter
    private String orderStatus;

    @Column(name = "order_payment", nullable = true, length = 45)
    @Setter
    private String orderPayment;

    @Column(name = "order_type", nullable = true, length = 45)
    @Setter
    private String orderType;

    @Column(name = "customer_type", nullable = true, length = 45)
    @Setter
    private String orderCustomerType;

    @Column(name = "document", nullable = true, length = 200)
    @Setter
    private String document;

    @Column(name = "user_id", insertable = false, updatable = false, nullable = false)
    @Setter
    private int userId;

    @Column(name = "product_id", insertable = false, updatable = false, nullable = false)
    @Setter
    private int productId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    @Setter
    private ProductEntity productByProductId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @Setter
    private UserEntity userByIdUser;
}
