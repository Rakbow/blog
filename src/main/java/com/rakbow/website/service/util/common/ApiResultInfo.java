package com.rakbow.website.service.util.common;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-09-12 3:43
 * @Description:
 */
public class ApiResultInfo {
    public static final String INSERT_DATA_SUCCESS = "%s新增成功！";
    public static final String DELETE_DATA_SUCCESS = "%s删除成功！";
    public static final String UPDATE_DATA_SUCCESS = "%s更新成功！";

    public static final String GET_DATA_FAILED = "%s不存在！";

    public static final String GET_DATA_FAILED_404 = "无效的地址，或者该%s已从数据库中删除。";

    //region 登录相关
    public static final String INCORRECT_VERIFY_CODE = "验证码不正确!";
    public static final String USERNAME_ARE_EMPTY = "账号不能为空!";
    public static final String PASSWORD_ARE_EMPTY = "密码不能为空!";
    public static final String USER_NOT_EXIST = "该账号不存在!";
    public static final String USER_ARE_INACTIVATED = "该账号未激活!";
    public static final String INCORRECT_PASSWORD = "密码不正确!";
    //endregion

    //region 权限相关
    public static final String NOT_LOGIN = "未登录!";
    public static final String NOT_AUTHORITY = "当前用户无权限!";
    public static final String NOT_AUTHORITY_DENIED = "当前用户无权限访问此功能！";
    //endregion
}
