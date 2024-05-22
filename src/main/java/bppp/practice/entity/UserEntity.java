package bppp.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Entity
@Table(name = "user", schema = "bzpi")
@NoArgsConstructor
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    @Setter
    private int userId;

    @Column(name = "user_name", nullable = true, length = 45)
    @Setter
    private String userName;

    @Column(name = "user_surname", nullable = true, length = 45)
    @Setter
    private String userSurname;

    @Column(name = "user_phone", nullable = true, length = 45)
    @Setter
    private String userPhone;

    @Column(name = "login", nullable = true, length = 45)
    @Setter
    private String login;

    @Column(name = "password", nullable = true, length = 45)
    @Setter
    private String password;

    @Column(name = "status", nullable = true, length = 150)
    @Setter
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userIdUser", cascade = CascadeType.ALL)
    @Setter
    private Collection<UserHasRoleEntity> userHasRoleByIdUser;

//    @OneToOne(fetch = FetchType.LAZY)
//    @Getter
//    @Setter
//    private OrganizationEntity organizationEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    @Setter
    private OrganizationEntity organizationEntity;
}
