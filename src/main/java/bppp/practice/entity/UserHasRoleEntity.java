package bppp.practice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_has_role", schema = "practice", catalog = "")
@IdClass(UserHasRoleEntityPK.class)
@NoArgsConstructor
public class UserHasRoleEntity {

//        @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id_user", nullable = false)
    @Getter
    @Setter
    private int userIdUser;
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "role_id_role", nullable = false)
    @Getter
    @Setter
    private int roleIdRole;

}