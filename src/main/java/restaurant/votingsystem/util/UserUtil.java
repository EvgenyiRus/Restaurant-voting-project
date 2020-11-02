package restaurant.votingsystem.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import restaurant.votingsystem.model.Role;
import restaurant.votingsystem.model.User;
import restaurant.votingsystem.to.UserTo;

import java.util.Set;

public class UserUtil {

    private UserUtil() {
    }

    public static User prepareToSave(User user, Set<Role> roles, PasswordEncoder passwordEncoder) {
        user.setEmail(user.getEmail().toLowerCase());
        String password = user.getPassword();
        user.setPassword(StringUtils.hasText(password) ? passwordEncoder.encode(password) : password);
        user.setRoles(roles);
        return user;
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}