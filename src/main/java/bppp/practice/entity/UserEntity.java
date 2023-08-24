package bppp.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@Table(name = "user", schema = "practice", catalog = "")
@NoArgsConstructor
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    @Getter
    @Setter
    private int userId;

    @Column(name = "user_name", nullable = true, length = 45)
    @Getter
    @Setter
    private String userName;

    @Column(name = "user_surname", nullable = true, length = 45)
    @Getter
    @Setter
    private String userSurname;

    @Column(name = "user_phone", nullable = true, length = 45)
    @Getter
    @Setter
    private String userPhone;

    @Column(name = "login", nullable = true, length = 45)
    @Getter
    @Setter
    private String login;

    @Column(name = "password", nullable = true, length = 45)
    @Getter
    @Setter
    private String password;

    @Column(name = "status", nullable = true, length = 150)
    @Getter
    @Setter
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userIdUser", cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Collection<UserHasRoleEntity> userHasRoleByIdUser;

//    @OneToOne(fetch = FetchType.LAZY)
//    @Getter
//    @Setter
//    private OrganizationEntity organizationEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    @Getter
    @Setter
    private OrganizationEntity organizationEntity;
}
