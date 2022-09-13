package com.rakbow.database.service.util.common;

import com.rakbow.database.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-08-17 23:31
 * @Description: 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}
