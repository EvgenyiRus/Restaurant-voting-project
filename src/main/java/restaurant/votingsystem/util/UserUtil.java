package restaurant.votingsystem.util;

import restaurant.votingsystem.model.Role;
import restaurant.votingsystem.model.User;

import java.util.Set;

public class UserUtil {

    public static User prepareToSave(User user, Set<Role> roles) {
        user.setEmail(user.getEmail().toLowerCase());
        user.setRoles(roles);
        return user;
    }
}