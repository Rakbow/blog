package com.rakbow.website.service.util.common;

import com.rakbow.website.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Project_name: website
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
