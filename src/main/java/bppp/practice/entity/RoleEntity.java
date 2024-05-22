package bppp.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Entity
@Table(name = "role", schema = "bzpi")
@NoArgsConstructor

public class RoleEntity {
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "role_id", nullable = false)
    @Setter
    private int idRole;

    @Column(name = "role_name", nullable = true, length = 45)
    @Setter
    private String roleName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "roleIdRole")
    @Setter
    private Collection<UserHasRoleEntity> userHasRoleByIdRole;

}
