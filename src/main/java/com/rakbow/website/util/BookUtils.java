package com.rakbow.website.util;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.book.BookType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 20:47
 * @Description:
 */
public class BookUtils {

    /**
     * 获取图书分类数组
     *
     * @return list 图书分类数组
     * @author rakbow
     */
    public static List<JSONObject> getBookTypeSet() {
        List<JSONObject> list = new ArrayList<>();
        for (BookType bookType : BookType.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", bookType.getNameZh());
            jsonObject.put("labelEn", bookType.getNameEn());
            jsonObject.put("value", bookType.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

    /**
     * ISBN-10转ISBN-13
     *
     * @param isbn_10
     * @return ISBN-13
     * @author rakbow
     */
    public static String getISBN13(String isbn_10) {
        if (isbn_10.length() != 10) {
            return isbn_10;
        }
        String isbn_13 = "978" + isbn_10.substring(0, isbn_10.length() - 1);
        int a = 0;
        int b = 0;
        int c;
        int d;
        for (int i = 0; i < isbn_13.length(); i++) {
            int x = Integer.parseInt(isbn_13.substring(i, i + 1));
            if (i % 2 == 0) {
                a += x;
            } else {
                b += x;
            }
        }
        c = a + 3 * b;
        d = 10 - c % 10;
        isbn_13 += d;
        return isbn_13;
    }

    /**
     * ISBN-13转ISBN-10
     *
     * @param isbn_13
     * @return ISBN-10
     * @author rakbow
     */
    public static String getISBN10(String isbn_13) {

        if (isbn_13.length() != 13) {
            return isbn_13;
        }

        String isbn_10 = isbn_13.substring(3, 12);

        int isbnSum = 0;
        int tmp = 10;

        for (int i = 3; i < isbn_13.length() - 1; i++) {
            isbnSum += Integer.parseInt(isbn_13.substring(i, i + 1)) * tmp;
            tmp--;
        }

        int tmp_num = 11 - isbnSum % 11;

        if (tmp_num == 10) {
            isbn_10 += "X";
        } else if (tmp_num == 11) {
            isbn_10 += 0;
        } else {
            isbn_10 += tmp_num;
        }
        return isbn_10;
    }

}
