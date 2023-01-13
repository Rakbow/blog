package com.rakbow.website.data.vo.book;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 16:13
 * @Description: VO 信息量最少
 */
@Data
public class BookVOBeta {

    private int id;//主键编号
    private String title;//标题（原文）
    private String titleZh;//标题（中文）
    private String isbn13;//国际标准书号（13位）.
    private String publishDate;//出版日期
    private JSONObject bookType;//书籍类型
    private JSONObject region;//地区
    private JSONObject publishLanguage;//语言

    private JSONObject cover;//图片（json）

    private String addedTime;//收录时间
    private String editedTime;//编辑时间

    //其他
    private long visitNum;//浏览数
}
