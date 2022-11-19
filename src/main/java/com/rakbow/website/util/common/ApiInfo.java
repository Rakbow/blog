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

     public static final String UPDATE_ALBUM_IMAGES_SUCCESS = "专辑图片更改成功！";
     public static final String INSERT_ALBUM_IMAGES_SUCCESS = "专辑图片新增成功！";
     public static final String DELETE_ALBUM_IMAGES_SUCCESS = "专辑图片删除成功！";
     public static final String UPDATE_ALBUM_TRACK_INFO_SUCCESS = "专辑音轨信息更新成功！";
     public static final String UPDATE_ALBUM_ARTISTS_SUCCESS = "专辑音乐创作信息更新成功！";
     public static final String UPDATE_ALBUM_DESCRIPTION_SUCCESS = "专辑描述更新成功！";
     public static final String UPDATE_ALBUM_BONUS_SUCCESS = "专辑特典信息更新成功！";


     public static final String ALBUM_NAME_EMPTY = "未填写专辑名称！";
     public static final String ALBUM_RELEASE_DATE_EMPTY = "未填写发行日期！";
     public static final String ALBUM_SERIES_EMPTY = "未选择专辑所属系列！";
     public static final String ALBUM_PRODUCTS_EMPTY = "未选择专辑所属作品！";
     public static final String ALBUM_PUBLISH_FORMAT_EMPTY = "未选择出版形式！";
     public static final String ALBUM_ALBUM_FORMAT_EMPTY = "未选择专辑分类！";
     public static final String ALBUM_MEDIA_FORMAT_EMPTY = "未选择媒体格式！";

     public static final String ALBUM_COVER_COUNT_EXCEPTION = "只允许一张图片类型为封面！";
     public static final String ALBUM_IMAGE_NAME_EN_REPEAT_EXCEPTION = "图片英文名不能重复！";

    //endregion

    //region music相关

    public static final String UPDATE_MUSIC_ARTISTS_SUCCESS = "创作人员信息更新成功！";
    public static final String UPDATE_MUSIC_DESCRIPTION_SUCCESS = "描述信息更新成功！";
    public static final String UPDATE_MUSIC_LYRICS_SUCCESS = "歌词文本更新成功！";

    public static final String MUSIC_NAME_EMPTY = "未填写曲名！";
    public static final String MUSIC_AUDIO_TYPE_EMPTY = "未选择音频类型！";
    public static final String MUSIC_AUDIO_LENGTH_EMPTY = "未填写音频长度！";

    //endregion

}
