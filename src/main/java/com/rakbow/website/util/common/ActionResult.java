package com.rakbow.website.util.common;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-09-30 11:14
 * @Description:
 */
public class ActionResult {

    public boolean state;//操作状态
    public String message;//错误信息

    public ActionResult() {
        this.state = true;
        this.message = "";
    }

    public ActionResult(boolean state, String message) {
        this.state = state;
        this.message = message;
    }

    public void setErrorMessage(String error) {
        this.state = false;
        this.message = error;
    }

}
