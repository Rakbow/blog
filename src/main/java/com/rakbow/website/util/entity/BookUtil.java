package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.rakbow.website.data.emun.book.BookType;
import com.rakbow.website.entity.Book;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 20:47
 * @Description:
 */
public class BookUtil {

    private static final String[] AUTHOR_LIST = new String[] {"原著", "原作", "作者"};
    private static final String[] COMIC_AUTHOR_LIST = new String[] {"作画", "作者"};

    /**
     * ISBN-10转ISBN-13
     *
     * @param isbn10，isbn书号(10位)
     * @return ISBN-13
     * @author rakbow
     */
    public static String getISBN13(String isbn10) {

        if (isbn10.length() != 10) {
            return isbn10;
        }
        String isbn13 = "978" + isbn10.substring(0, isbn10.length() - 1);
        int a = 0;
        int b = 0;
        int c;
        int d;
        for (int i = 0; i < isbn13.length(); i++) {
            int x = Integer.parseInt(isbn13.substring(i, i + 1));
            if (i % 2 == 0) {
                a += x;
            } else {
                b += x;
            }
        }
        c = a + 3 * b;
        d = 10 - c % 10;
        isbn13 += d;
        return isbn13;
    }

    /**
     * ISBN-13转ISBN-10
     *
     * @param isbn13，isbn书号(13位)
     * @return ISBN-10
     * @author rakbow
     */
    public static String getISBN10(String isbn13) {

        if (isbn13.length() != 13) {
            return isbn13;
        }

        String isbn10 = isbn13.substring(3, 12);

        int isbnSum = 0;
        int tmp = 10;

        for (int i = 3; i < isbn13.length() - 1; i++) {
            isbnSum += Integer.parseInt(isbn13.substring(i, i + 1)) * tmp;
            tmp--;
        }

        int tmp_num = 11 - isbnSum % 11;

        if (tmp_num == 10) {
            isbn10 += "X";
        } else if (tmp_num == 11) {
            isbn10 += 0;
        } else {
            isbn10 += tmp_num;
        }
        return isbn10;
    }

    /**
     * 获取图书信息中的作者
     * @author rakbow
     * @param book 图书
     * */
    public static String getAuthors(Book book) {
        JSONArray authorJson = JSON.parseArray(book.getAuthors());
        if (authorJson.size() == 0) {
            return "N/A";
        }
        for (int i = 0; i < authorJson.size(); i++) {
            if(book.getBookType() == BookType.COMIC.getIndex()) {
                for (String s : COMIC_AUTHOR_LIST) {
                    if (StringUtils.equals(authorJson.getJSONObject(i).getString("pos"), s)) {
                        List<String> authors = authorJson.getJSONObject(i).getList("name", String.class);
                        if(authors.size() > 0 && authors.size() <= 2) {
                            return String.join("/", authors);
                        }
                        if(authors.size() > 2) {
                            return authors.get(0) + " etc.";
                        }
                    }
                }
            }else {
                for (String s : AUTHOR_LIST) {
                    if (StringUtils.equals(authorJson.getJSONObject(i).getString("pos"), s)) {
                        List<String> vocals = authorJson.getJSONObject(i).getList("name", String.class);
                        return String.join("/", vocals);
                    }
                }
            }
        }
        return "N/A";
    }

}
