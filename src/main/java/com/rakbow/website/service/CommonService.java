package com.rakbow.website.service;

import com.rakbow.website.data.emun.MediaFormat;
import com.rakbow.website.data.emun.album.AlbumFormat;
import com.rakbow.website.data.emun.album.PublishFormat;
import com.rakbow.website.data.emun.book.BookType;
import com.rakbow.website.data.emun.common.Language;
import com.rakbow.website.data.emun.common.Region;
import com.rakbow.website.data.emun.game.GamePlatform;
import com.rakbow.website.data.emun.game.ReleaseType;
import com.rakbow.website.data.emun.merch.MerchCategory;
import com.rakbow.website.data.emun.product.ProductCategory;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.entity.MusicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 14:42
 * @Description: 通用业务层
 */
@Service
public class CommonService {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 刷新Redis缓存中的枚举类数据
     *
     * @author rakbow
     */
    public void refreshRedisEmunData () {

        redisUtil.set("albumFormatSet", AlbumFormat.getAlbumFormatSet());
        redisUtil.set("publishFormatSet", PublishFormat.getPublishFormatSet());
        redisUtil.set("mediaFormatSet", MediaFormat.getMediaFormatSet());
        redisUtil.set("bookTypeSet", BookType.getBookTypeSet());
        redisUtil.set("regionSet", Region.getRegionSet());
        redisUtil.set("languageSet", Language.getLanguageSet());
        redisUtil.set("merchCategorySet", MerchCategory.getMerchCategorySet());
        redisUtil.set("platformSet", GamePlatform.getGamePlatformSet());
        redisUtil.set("releaseTypeSet", ReleaseType.getReleaseTypeSet());
        redisUtil.set("ProductCategorySet", ProductCategory.getProductCategorySet());
        redisUtil.set("audioTypeSet", MusicUtil.getAudioTypeSet());

    }

}
