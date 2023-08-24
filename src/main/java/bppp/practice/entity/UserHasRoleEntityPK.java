package bppp.practice.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserHasRoleEntityPK implements Serializable {
    private int userIdUser;

    private int roleIdRole;

    public UserHasRoleEntityPK() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserHasRoleEntityPK that = (UserHasRoleEntityPK) o;
        return userIdUser == that.userIdUser && roleIdRole == that.roleIdRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdUser, roleIdRole);
    }
}
