package com.rakbow.website.service;

import com.rakbow.website.dao.LoginTicketMapper;
import com.rakbow.website.dao.UserMapper;
import com.rakbow.website.entity.LoginTicket;
import com.rakbow.website.entity.User;
import com.rakbow.website.util.CookieUtil;
import com.rakbow.website.util.common.ActionResult;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonConstant;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-02 0:43
 * @Description:
 */
@Service
public class UserService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${website.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Transactional( readOnly = true )
    public User findUserById(int id) {
        return userMapper.selectUserById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

        // 验证账号
        User u = userMapper.selectUserByUsername(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectUserByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(CommonUtils.generateUUID().substring(0, 5));
        user.setPassword(CommonUtils.md5(user.getPassword() + user.getSalt()));
        user.setType(0);//设置用户类型 0-普通用户 1-管理员
        user.setStatus(0);
        user.setActivationCode(CommonUtils.generateUUID());
        //设置用户默认头像
        //user.setHeaderUrl(String.format("", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("username", user.getUsername());
        String activationUrl = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("activationUrl", activationUrl);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectUserById(userId);
        if (user.getStatus() == 1) {
            return CommonConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return CommonConstant.ACTIVATION_SUCCESS;
        } else {
            return CommonConstant.ACTIVATION_FAILURE;
        }
    }

    //登录
    public Map<String, String> login(String username, String password, int expiredSeconds) {
        Map<String, String> map = new HashMap<>();
        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("error", ApiInfo.USERNAME_ARE_EMPTY);
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("error", ApiInfo.PASSWORD_ARE_EMPTY);
            return map;
        }

        // 验证账号
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            map.put("error", ApiInfo.USER_NOT_EXIST);
            return map;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            map.put("error", ApiInfo.USER_ARE_INACTIVATED);
            return map;
        }

        // 验证密码
        password = CommonUtils.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("error", ApiInfo.INCORRECT_PASSWORD);
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommonUtils.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl) {
        return userMapper.updateHeader(userId, headerUrl);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add((GrantedAuthority) () -> {
            switch (user.getType()) {
                case 1:
                    return CommonConstant.AUTHORITY_ADMIN;
                default:
                    return CommonConstant.AUTHORITY_USER;
            }
        });
        return list;
    }

    public ActionResult checkAuthority(HttpServletRequest request){
        ActionResult res = new ActionResult();
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = findUserById(loginTicket.getUserId());
                if (user.getType() == 0) {
                    res.setErrorMessage(ApiInfo.NOT_AUTHORITY);
                }
            }else {
                res.setErrorMessage(ApiInfo.NOT_LOGIN);
            }
        } else {
            res.setErrorMessage(ApiInfo.NOT_LOGIN);
        }
        return res;
    }

}
