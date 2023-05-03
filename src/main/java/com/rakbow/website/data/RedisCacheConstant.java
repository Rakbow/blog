package com.rakbow.website.data;

import com.rakbow.website.data.emun.common.EntityType;
import io.netty.handler.ssl.SslHandshakeTimeoutException;

import java.io.File;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-17 18:53
 * @Description: redis缓存常量
 */
public class RedisCacheConstant {

    //region common

    public static final String SPLIT = ":";
    private static final String OPTIONS = "options";
    private static final String VISIT_RANK = "visit_rank";
    private static final String COMMON = "common";
    private static final String ALBUM = "album";
    private static final String BOOK = "book";
    private static final String DISC = "disc";
    private static final String GAME = "game";
    private static final String MERCH = "merch";
    private static final String MUSIC = "music";
    private static final String PRODUCT = "product";
    private static final String ZH = "zh";
    private static final String EN = "en";
    //endregion

    //region options

    //common
    public static final String LANGUAGE_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "language";
    public static final String REGION_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "region";
    public static final String MEDIA_FORMAT_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "mediaFormat";
    public static final String FRANCHISE_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "franchise";
    public static final String PRODUCT_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "product";
    public static final String COMPANY_ROLE_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "companyRole";
    public static final String ENTRY_CATEGORY_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "entryCategory";
    public static final String COMPANY_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "company";
    public static final String PERSONNEL_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "personnel";
    public static final String MERCHANDISE_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "merchandise";
    public static final String ROLE_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "role";

    public static final String LANGUAGE_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "language";
    public static final String REGION_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "region";
    public static final String MEDIA_FORMAT_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "mediaFormat";
    public static final String FRANCHISE_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "franchise";
    public static final String PRODUCT_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "product";
    public static final String COMPANY_ROLE_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "companyRole";
    public static final String ENTRY_CATEGORY_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "entryCategory";
    public static final String COMPANY_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "company";
    public static final String PERSONNEL_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "personnel";
    public static final String MERCHANDISE_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "merchandise";
    public static final String ROLE_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "role";

    //album
    public static final String ALBUM_FORMAT_SET_ZH = OPTIONS + SPLIT + ALBUM + SPLIT + ZH + SPLIT + "albumFormatSet";
    public static final String PUBLISH_FORMAT_SET_ZH = OPTIONS + SPLIT + ALBUM + SPLIT + ZH + SPLIT + "publishFormatSet";
    public static final String ALBUM_FORMAT_SET_EN = OPTIONS + SPLIT + ALBUM + SPLIT + EN + SPLIT + "albumFormatSet";
    public static final String PUBLISH_FORMAT_SET_EN = OPTIONS + SPLIT + ALBUM + SPLIT + EN + SPLIT + "publishFormatSet";

    //book
    public static final String BOOK_TYPE_SET_ZH = OPTIONS + SPLIT + BOOK + SPLIT + ZH + SPLIT + "bookTypeSet";
    public static final String BOOK_TYPE_SET_EN = OPTIONS + SPLIT + BOOK + SPLIT + EN + SPLIT + "bookTypeSet";

    //game
    public static final String RELEASE_TYPE_SET_ZH = OPTIONS + SPLIT + GAME + SPLIT + ZH + SPLIT + "releaseTypeSet";
    public static final String GAME_PLATFORM_SET_ZH = OPTIONS + SPLIT + GAME + SPLIT + ZH + SPLIT + "gamePlatformSet";
    public static final String PLATFORM_SET_ZH = OPTIONS + SPLIT + GAME + SPLIT + ZH + SPLIT + "gamePlatformSet";

    public static final String RELEASE_TYPE_SET_EN = OPTIONS + SPLIT + GAME + SPLIT + EN + SPLIT + "releaseTypeSet";
    public static final String GAME_PLATFORM_SET_EN = OPTIONS + SPLIT + GAME + SPLIT + EN + SPLIT + "gamePlatformSet";
    public static final String PLATFORM_SET_EN = OPTIONS + SPLIT + GAME + SPLIT + EN + SPLIT + "gamePlatformSet";

    //goods
    public static final String MERCH_CATEGORY_SET_ZH = OPTIONS + SPLIT + MERCH + SPLIT + ZH + SPLIT + "merchCategorySet";
    public static final String MERCH_CATEGORY_SET_EN = OPTIONS + SPLIT + MERCH + SPLIT + EN + SPLIT + "merchCategorySet";

    //music
    public static final String AUDIO_TYPE_SET_ZH = OPTIONS + SPLIT + COMMON + SPLIT + ZH + SPLIT + "audioTypeSet";
    public static final String AUDIO_TYPE_SET_EN = OPTIONS + SPLIT + COMMON + SPLIT + EN + SPLIT + "audioTypeSet";

    //product
    public static final String PRODUCT_CATEGORY_SET_ZH = OPTIONS + SPLIT + PRODUCT + SPLIT + ZH + SPLIT + "productCategorySet";
    public static final String PRODUCT_CATEGORY_SET_EN = OPTIONS + SPLIT + PRODUCT + SPLIT + EN + SPLIT + "productCategorySet";





    //endregion

    //region ranking

    public static final String ALBUM_VISIT_RANKING = VISIT_RANK + SPLIT + "ALBUM";
    public static final String BOOK_VISIT_RANKING = VISIT_RANK + SPLIT + "BOOK";
    public static final String DISC_VISIT_RANKING = VISIT_RANK + SPLIT + "DISC";
    public static final String GAME_VISIT_RANKING = VISIT_RANK + SPLIT + "GAME";
    public static final String MERCH_VISIT_RANKING = VISIT_RANK + SPLIT + "MERCH";
    public static final String MUSIC_VISIT_RANKING = VISIT_RANK + SPLIT + "MUSIC";
    public static final String PRODUCT_VISIT_RANKING = VISIT_RANK + SPLIT + "PRODUCT";
    public static final String FRANCHISE_VISIT_RANKING = VISIT_RANK + SPLIT + "FRANCHISE";

    //endregion

    //region index cover urls

    public static final String INDEX_COVER_URL = "INDEX_COVER_URL";

    //endregion

    //region entity related item

    public static final String ENTITY_RELATED_ITEM = "entity_related_item";

    //endregion

}
