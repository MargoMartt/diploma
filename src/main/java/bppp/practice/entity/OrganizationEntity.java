package bppp.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization", schema = "practice", catalog = "")
@NoArgsConstructor
public class OrganizationEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "organization_id", nullable = false)
    @Getter
    @Setter
    private int idOrganization;

    @Column(name = "organization_name", nullable = true, length = 45)
    @Getter
    @Setter
    private String organizationName;

    @Column(name = "organization_director", nullable = true, length = 45)
    @Getter
    @Setter
    private String organizationDirector;

    @Column(name = "organization_type", nullable = true, length = 45)
    @Getter
    @Setter
    private String organizationType;

    @Column(name = "organization_responsible", nullable = true, length = 45)
    @Getter
    @Setter
    private String organizationResponsible;
//
//    @OneToOne(mappedBy = "organizationEntity", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
//    @Getter
//    @Setter
//    private UserEntity userEntity;


    @OneToOne(mappedBy = "organizationEntity", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private UserEntity userEntity;
}
