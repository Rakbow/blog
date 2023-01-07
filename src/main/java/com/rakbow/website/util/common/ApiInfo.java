package com.rakbow.website.util.common;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-09-12 3:43
 * @Description:
 */
public class ApiInfo {

    public static final String NOT_ACTION = "未进行任何操作！";
    public static final String INSERT_DATA_SUCCESS = "%s新增成功！";
    public static final String DELETE_DATA_SUCCESS = "%s删除成功！";
    public static final String UPDATE_DATA_SUCCESS = "%s更新成功！";
    public static final String GET_DATA_FAILED = "%s不存在！";
    public static final String GET_DATA_FAILED_404 = "无效的地址，或者该%s已从数据库中删除。";

    public static final String INPUT_TEXT_EMPTY = "输入信息为空！";
    public static final String INPUT_IMAGE_EMPTY = "还没有选择图片！";
    public static final String INCORRECT_FILE_FORMAT = "文件的格式不正确！";
    public static final String UPLOAD_EXCEPTION = "上传文件失败,服务器发生异常！";

    public static final String GET_IMAGE_FAILED = "读取图片失败: ";
    public static final String GET_FILE_FAILED = "读取文件失败: ";
    public static final String IMAGE_FORMAT_EXCEPTION = "图片格式错误";
    public static final String UPDATE_IMAGES_SUCCESS = "%s图片更改成功！";
    public static final String INSERT_IMAGES_SUCCESS = "%s图片新增成功！";
    public static final String DELETE_IMAGES_SUCCESS = "%s图片删除成功！";
    public static final String COVER_COUNT_EXCEPTION = "只允许一张图片类型为封面！";
    public static final String IMAGE_NAME_EN_REPEAT_EXCEPTION = "图片英文名不能重复！";

    //region 登录相关
    public static final String INCORRECT_VERIFY_CODE = "验证码不正确!";
    public static final String USERNAME_ARE_EMPTY = "账号不能为空!";
    public static final String PASSWORD_ARE_EMPTY = "密码不能为空!";
    public static final String USER_NOT_EXIST = "该账号不存在!";
    public static final String USER_ARE_INACTIVATED = "该账号未激活!";
    public static final String INCORRECT_PASSWORD = "密码不正确!";
    //endregion

    //region 权限相关
    public static final String NOT_LOGIN = "未登录!";
    public static final String NOT_AUTHORITY = "当前用户无权限!";
    public static final String NOT_AUTHORITY_DENIED = "当前用户无权限访问此功能！";
    //endregion

    //region 专辑相关
     public static final String UPDATE_ALBUM_TRACK_INFO_SUCCESS = "专辑音轨信息更新成功！";
     public static final String UPDATE_ALBUM_ARTISTS_SUCCESS = "专辑创作者信息更新成功！";
     public static final String UPDATE_ALBUM_DESCRIPTION_SUCCESS = "专辑描述更新成功！";
     public static final String UPDATE_ALBUM_BONUS_SUCCESS = "专辑特典信息更新成功！";


     public static final String ALBUM_NAME_EMPTY = "未填写专辑名称！";
     public static final String ALBUM_RELEASE_DATE_EMPTY = "未填写发行日期！";
     public static final String ALBUM_FRANCHISES_EMPTY = "未选择专辑所属系列！";
     public static final String ALBUM_PRODUCTS_EMPTY = "未选择专辑所属作品！";
     public static final String ALBUM_PUBLISH_FORMAT_EMPTY = "未选择出版形式！";
     public static final String ALBUM_ALBUM_FORMAT_EMPTY = "未选择专辑分类！";
     public static final String ALBUM_MEDIA_FORMAT_EMPTY = "未选择媒体格式！";

    //endregion

    //region music相关

    public static final String UPDATE_MUSIC_ARTISTS_SUCCESS = "创作人员信息更新成功！";
    public static final String UPDATE_MUSIC_DESCRIPTION_SUCCESS = "描述信息更新成功！";
    public static final String UPDATE_MUSIC_LYRICS_SUCCESS = "歌词文本更新成功！";

    public static final String MUSIC_NAME_EMPTY = "未填写曲名！";
    public static final String MUSIC_AUDIO_TYPE_EMPTY = "未选择音频类型！";
    public static final String MUSIC_AUDIO_LENGTH_EMPTY = "未填写音频长度！";

    //endregion

    //region product相关
    public static final String PRODUCT_NAME_EMPTY = "未填写作品名称！";
    public static final String PRODUCT_NAME_ZH_EMPTY = "未填写作品译名(中)！";
    public static final String PRODUCT_RELEASE_DATE_EMPTY = "未填写发行日期！";
    public static final String PRODUCT_FRANCHISE_EMPTY = "未选择作品所属系列！";
    public static final String PRODUCT_CATEGORY_EMPTY = "未选择作品分类！";
    public static final String UPDATE_PRODUCT_DESCRIPTION_SUCCESS = "描述信息更新成功！";
    public static final String UPDATE_PRODUCT_STAFFS_SUCCESS = "staff信息更新成功！";
    //endregion

    //region disc相关
    public static final String UPDATE_DISC_SPEC_SUCCESS = "碟片规格信息更新成功！";
    public static final String UPDATE_DISC_DESCRIPTION_SUCCESS = "碟片描述更新成功！";
    public static final String UPDATE_DISC_BONUS_SUCCESS = "碟片特典信息更新成功！";

    public static final String DISC_NAME_EMPTY = "未填写专辑名称！";
    public static final String DISC_RELEASE_DATE_EMPTY = "未填写发行日期！";
    public static final String DISC_FRANCHISES_EMPTY = "未选择碟片所属系列！";
    public static final String DISC_PRODUCTS_EMPTY = "未选择碟片所属作品！";
    public static final String DISC_MEDIA_FORMAT_EMPTY = "未选择媒体格式！";
    //endregion

    //region book相关
    public static final String UPDATE_BOOK_AUTHOR_SUCCESS = "图书作者信息更新成功！";
    public static final String UPDATE_BOOK_SPEC_SUCCESS = "图书规格信息更新成功！";
    public static final String UPDATE_BOOK_DESCRIPTION_SUCCESS = "图书描述更新成功！";
    public static final String UPDATE_BOOK_BONUS_SUCCESS = "图书特典信息更新成功！";

    public static final String BOOK_TITLE_EMPTY = "未填写图书名称！";
    public static final String BOOK_ISBN10_LENGTH_EXCEPTION = "ISBN-10无效！";
    public static final String BOOK_ISBN13_LENGTH_EXCEPTION = "ISBN-13无效！";
    public static final String BOOK_FRANCHISES_EMPTY = "未选择图书所属系列！";
    public static final String BOOK_PRODUCTS_EMPTY = "未选择图书所属作品！";
    public static final String BOOK_TYPE_EMPTY = "未选择图书所属分类！";
    public static final String BOOK_PUBLISH_DATE_EMPTY = "未填写出版日期！";

    //endregion

    //region merch相关
    public static final String UPDATE_MERCH_SPEC_SUCCESS = "周边规格信息更新成功！";
    public static final String UPDATE_MERCH_DESCRIPTION_SUCCESS = "周边描述更新成功！";

    public static final String MERCH_NAME_EMPTY = "未填写周边名称！";
    public static final String MERCH_FRANCHISES_EMPTY = "未选择周边所属系列！";
    public static final String MERCH_PRODUCTS_EMPTY = "未选择周边所属作品！";
    public static final String MERCH_CATEGORY_EMPTY = "未选择图书所属分类！";
    public static final String MERCH_RELEASE_DATE_EMPTY = "未填写发售日期！";

    //endregion

    //region game相关
    public static final String UPDATE_GAME_ORGANIZATIONS_SUCCESS = "相关组织信息更新成功！";
    public static final String UPDATE_GAME_STAFFS_SUCCESS = "开发制作人员信息更新成功！";
    public static final String UPDATE_GAME_DESCRIPTION_SUCCESS = "游戏描述更新成功！";
    public static final String UPDATE_GAME_BONUS_SUCCESS = "游戏特典信息更新成功！";

    public static final String GAME_NAME_EMPTY = "未填写游戏名称！";
    public static final String GAME_FRANCHISES_EMPTY = "未选择游戏所属系列！";
    public static final String GAME_PRODUCTS_EMPTY = "未选择游戏所属作品！";
    public static final String GAME_RELEASE_DATE_EMPTY = "未填写游戏发售日期！";
    public static final String GAME_RELEASE_TYPE_EMPTY = "未选择游戏发售类型！";
    public static final String GAME_PLATFORM_EMPTY = "未选择游戏所属平台！";
    public static final String GAME_REGION_EMPTY = "未选择发售地区！";

    //endregion

    //region 七牛云
    public static final String QINIU_EXCEPTION = "七牛云异常: %s";
    //endregion

}
