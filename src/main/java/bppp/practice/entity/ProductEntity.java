package bppp.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product", schema = "practice", catalog = "")
@NoArgsConstructor
public class ProductEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id", nullable = false)
    @Getter
    @Setter
    private int productId;

    @Column(name = "product_name", nullable = true, length = 45)
    @Getter
    @Setter
    private String productName;

    @Column(name = "product_type", nullable = true, length = 45)
    @Getter
    @Setter
    private String productType;

    @Column(name = "product_cost", nullable = true, length = 45)
    @Getter
    @Setter
    private Double productCost;

    @Column(name = "product_count", nullable = true, length = 45)
    @Getter
    @Setter
    private Integer productCount;

    @Column(name = "product_description", nullable = true, length = 45)
    @Getter
    @Setter
    private String productDescription;

    @Column(name = "product_picture", nullable = true, length = 100)
    @Getter
    @Setter
    private String productPicture;


}
