package com.rakbow.website.data;

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

    //endregion

    //region options

    public static final String MEDIA_FORMAT_SET = OPTIONS + SPLIT + "mediaFormatSet";
    public static final String ALBUM_FORMAT_SET = OPTIONS + SPLIT + "albumFormatSet";
    public static final String PUBLISH_FORMAT_SET = OPTIONS + SPLIT + "publishFormatSet";
    public static final String BOOK_TYPE_SET = OPTIONS + SPLIT + "bookTypeSet";
    public static final String REGION_SET = OPTIONS + SPLIT + "regionSet";
    public static final String LANGUAGE_SET = OPTIONS + SPLIT + "languageSet";
    public static final String RELEASE_TYPE_SET = OPTIONS + SPLIT + "releaseTypeSet";
    public static final String GAME_PLATFORM_SET = OPTIONS + SPLIT + "gamePlatformSet";
    public static final String MERCH_CATEGORY_SET = OPTIONS + SPLIT + "merchCategorySet";
    public static final String AUDIO_TYPE_SET = OPTIONS + SPLIT + "audioTypeSet";
    public static final String PRODUCT_CATEGORY_SET = OPTIONS + SPLIT + "productCategorySet";
    public static final String PLATFORM_SET = OPTIONS + SPLIT + "platformSet";
    public static final String FRANCHISE_SET = OPTIONS + SPLIT + "franchiseSet";
    public static final String PRODUCT_SET = OPTIONS + SPLIT + "productSet";
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

}
