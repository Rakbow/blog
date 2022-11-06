package com.rakbow.website.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-17 0:22
 * @Description:
 */
@Data
public class LoginTicket {

    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;

}
