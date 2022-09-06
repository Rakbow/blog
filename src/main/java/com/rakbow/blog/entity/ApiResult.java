package com.rakbow.blog.entity;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-09-04 3:45
 * @Description:
 */
public class ApiResult {

    public int code;//操作代码
    public int state;//操作状态 0-失败
    public String data;//响应数据
    public String msg;//错误信息

    public ApiResult(){
        this.state = 1;
        this.msg = "";
    }

}
