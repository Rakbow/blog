package com.rakbow.website.util.common;

import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.RedisKey;
import com.rakbow.website.data.emun.MetaEmun;
import com.rakbow.website.data.emun.common.CompanyRole;
import com.rakbow.website.data.emun.common.MediaFormat;
import com.rakbow.website.data.emun.entity.album.AlbumFormat;
import com.rakbow.website.data.emun.entity.album.PublishFormat;
import com.rakbow.website.data.emun.entity.book.BookType;
import com.rakbow.website.data.emun.entity.game.GamePlatform;
import com.rakbow.website.data.emun.entity.game.ReleaseType;
import com.rakbow.website.data.emun.entity.music.AudioType;
import com.rakbow.website.data.emun.entity.product.ProductCategory;
import com.rakbow.website.data.emun.entry.EntryCategory;

import java.util.*;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-19 18:56
 * @Description:
 */
public class EmunUtil {

    private static final Class[] SUPPORTED_EMUN = {
            CompanyRole.class, MediaFormat.class,
            AlbumFormat.class, PublishFormat.class,
            BookType.class,
            GamePlatform.class, ReleaseType.class,
            AudioType.class,
            ProductCategory.class,
            EntryCategory.class
    };

    private static final Map<Class, String> EMUN_REDIS_KEY_PAIR = Map.ofEntries(
            Map.entry(CompanyRole.class, RedisKey.COMPANY_SET),
            Map.entry(MediaFormat.class, RedisKey.MEDIA_FORMAT_SET),
            Map.entry(AlbumFormat.class, RedisKey.ALBUM_FORMAT_SET),
            Map.entry(PublishFormat.class, RedisKey.PUBLISH_FORMAT_SET),
            Map.entry(BookType.class, RedisKey.BOOK_TYPE_SET),
            Map.entry(GamePlatform.class, RedisKey.GAME_PLATFORM_SET),
            Map.entry(ReleaseType.class, RedisKey.RELEASE_TYPE_SET),
            Map.entry(AudioType.class, RedisKey.AUDIO_TYPE_SET),
            Map.entry(ProductCategory.class, RedisKey.PRODUCT_VISIT_RANKING),
            Map.entry(EntryCategory.class, RedisKey.ENTRY_CATEGORY_SET)
    );

    @SuppressWarnings("unchecked")
    public static Map<String, List<Attribute>> refreshRedisEmunData() {

        RedisUtil redisUtil = SpringUtil.getBean("redisUtil");

        Map<String, List<Attribute>> map = new HashMap<>();

        Arrays.asList(SUPPORTED_EMUN).forEach(emunClass -> {

            List<Attribute> attributesZh = MetaEmun.getAttributeSet(emunClass, Locale.CHINESE.getLanguage());
            List<Attribute> attributesEn = MetaEmun.getAttributeSet(emunClass, Locale.ENGLISH.getLanguage());

            String key = EMUN_REDIS_KEY_PAIR.get(emunClass);
            String zhKey = String.format(key, Locale.CHINESE.getLanguage());
            String enKey = String.format(key, Locale.ENGLISH.getLanguage());

            redisUtil.set(zhKey, attributesZh);
            redisUtil.set(enKey, attributesEn);

        });

        return map;
    }

}
