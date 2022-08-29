package com.rakbow.blog.util;

import com.rakbow.blog.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Project_name: blog
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
