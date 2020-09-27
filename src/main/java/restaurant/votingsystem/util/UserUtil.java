package restaurant.votingsystem.util;

import restaurant.votingsystem.model.Role;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.to.UserTo;

import java.util.Set;

public class UserUtil {

    public static User prepareToSave(User user, Set<Role> roles) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setRoles(roles);
        return user;
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}