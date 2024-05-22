package bppp.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Table(name = "user_has_role", schema = "bzpi")
@IdClass(UserHasRoleEntityPK.class)
@NoArgsConstructor
public class UserHasRoleEntity {

//        @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id_user", nullable = false)
    @Setter
    private int userIdUser;
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "role_id_role", nullable = false)
    @Setter
    private int roleIdRole;

}