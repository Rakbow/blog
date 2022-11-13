const DOMAIN_URL = "http://localhost:8080";

//region page
const HOME_INDEX_URL = DOMAIN_URL;
const DATABASE_INDEX_URL = DOMAIN_URL + "/db";

const APP_INDEX_URL = DOMAIN_URL + "/app";
const BLOG_INDEX_URL = DOMAIN_URL + "/blog";

const SERIES_INDEX_URL = DOMAIN_URL + "/db/series";
const PRODUCT_INDEX_URL = DOMAIN_URL + "/db/product";

const ALBUM_LIST_URL = DOMAIN_URL + "/db/album/list";
const ALBUM_INDEX_URL = DOMAIN_URL + "/db/album/index";

const DISC_INDEX_URL = DOMAIN_URL + "/db/disc";
const BOOK_INDEX_URL = DOMAIN_URL + "/db/book";
const GOODS_INDEX_URL = DOMAIN_URL + "/db/goods";
const GAME_INDEX_URL = DOMAIN_URL + "/db/game";

//endregion

//region album
const GET_LIMIT_ALBUM_URL = DOMAIN_URL + "/db/album/get-album-limit";
const GET_ALL_ALBUM_URL = DOMAIN_URL + "/db/album/get-all";
const DELETE_ALBUM_URL = DOMAIN_URL + "/db/album/delete";
const UPDATE_ALBUM_URL = DOMAIN_URL + "/db/album/update";
const INSERT_ALBUM_URL = DOMAIN_URL + "/db/album/add";
const GET_ALBUM_URL = DOMAIN_URL + "/db/album/get-album";
const GET_ALBUMS_URL = DOMAIN_URL + "/db/album/get-albums";

const UPDATE_ALBUM_ARTISTS_URL = DOMAIN_URL + "/db/album/update-artists";
const UPDATE_ALBUM_TRACK_INFO_URL = DOMAIN_URL + "/db/album/update-trackInfo";
const INSERT_ALBUM_IMAGES_URL = DOMAIN_URL + "/db/album/add-images";
const UPDATE_ALBUM_IMAGES_URL = DOMAIN_URL + "/db/album/update-images";
const UPDATE_ALBUM_DESCRIPTION_URL = DOMAIN_URL + "/db/album/update-description";
const UPDATE_ALBUM_BONUS_URL = DOMAIN_URL + "/db/album/update-bonus";
//endregion

//region music
const UPDATE_MUSIC_URL = DOMAIN_URL + "/db/music/update";
const UPDATE_MUSIC_ARTISTS_URL = DOMAIN_URL + "/db/music/update-artists";
const UPDATE_MUSIC_LYRICS_TEXT_URL = DOMAIN_URL + "/db/music/update-lyrics-text";
const UPDATE_MUSIC_DESCRIPTION_URL = DOMAIN_URL + "/db/music/update-description";
//endregion

//region user
const CHECK_USER_AUTHORITY_URL = DOMAIN_URL + "/user/check-authority";
const LOGIN_URL = DOMAIN_URL + "/login";
const LOGOUT_URL = DOMAIN_URL + "/logout";
//endregion

//common
const GET_ALL_PRODUCT_URL = DOMAIN_URL + "/product/get-products";

//region header
const ENTITY_TYPE = [
    {label: '专辑', value: '1'},
    {label: 'BD/DVD', value: '2'},
    {label: '书籍', value: '3'},
    {label: '周边', value: '4'},
    {label: '游戏', value: '5'},
    {label: '系列', value: '6'},
    {label: '作品', value: '7'},
    {label: '文章', value: '8'}
];

const NOT_LOGIN_NAVBAR_ITEMS = [
    {
        label: '首页', icon: 'pi pi-fw pi-home', url: DOMAIN_URL
    },
    {
        label: '数据库',
        icon: 'pi pi-fw iconfont icon-database',
        items: [
            {
                label: '数据库首页',
                icon: 'pi pi-fw iconfont icon-database',
                url: DATABASE_INDEX_URL
            },
            // {
            //     label: '系列', icon: 'pi pi-fw iconfont icon-_classification', url: SERIES_INDEX_URL
            // },
            // {
            //     label: '作品', icon: 'pi pi-fw iconfont icon-_classification', url: PRODUCT_INDEX_URL
            // },
            {
                label: '专辑', icon: 'pi pi-fw iconfont icon-24gl-musicAlbum2',
                items: [
                    {label: '专辑首页', icon: 'pi pi-fw iconfont icon-24gl-musicAlbum2', url: ALBUM_INDEX_URL},
                    {label: '专辑列表', icon: 'pi pi-fw pi-list', url: ALBUM_LIST_URL}
                ]
            },
            // {
            //     label: 'BD/DVD', icon: 'pi pi-fw iconfont icon-Video-Disc',
            //     items: [
            //         {label: '碟片首页', icon: 'pi pi-fw iconfont icon-Video-Disc', url: DISC_INDEX_URL},
            //         {label: '碟片列表', icon: 'pi pi-fw pi-list'}
            //     ]
            // },
            // {
            //     label: '书籍', icon: 'pi pi-fw pi-book',
            //     items: [
            //         {label: '书籍首页', icon: 'pi pi-fw pi-book', url: ALBUM_INDEX_URL},
            //         {label: '书籍列表', icon: 'pi pi-fw pi-list'}
            //     ]
            // },
            // {
            //     label: '周边', icon: 'pi pi-fw iconfont icon-yinshuabaozhuang',
            //     items: [
            //         {label: '周边首页', icon: 'pi pi-fw iconfont icon-yinshuabaozhuang', url: DISC_INDEX_URL},
            //         {label: '周边列表', icon: 'pi pi-fw pi-list'}
            //     ]
            // },
            // {
            //     label: '游戏', icon: 'pi pi-fw iconfont icon-youxi',
            //     items: [
            //         {label: '游戏首页', icon: 'pi pi-fw iconfont icon-youxi', url: GAME_INDEX_URL},
            //         {label: '游戏列表', icon: 'pi pi-fw pi-list'}
            //     ]
            // }
        ]
    },
    {
        label: '博客', icon: 'pi pi-fw pi-book', url: BLOG_INDEX_URL
    },
    {
        label: '应用', icon: 'pi iconfont icon-yingyong', url: APP_INDEX_URL
    },
    {
        label: '后台', icon: 'pi iconfont icon-login', url: LOGIN_URL
    }
];

const LOGIN_NAVBAR_ITEMS = [
    {
        label: '首页',
        icon: 'pi pi-fw pi-home',
        url: DOMAIN_URL
    },
    {
        label: '数据库',
        icon: 'pi pi-fw iconfont icon-database',
        items: [
            {
                label: '数据库首页',
                icon: 'pi pi-fw iconfont icon-database',
                url: DATABASE_INDEX_URL
            },
            // {
            //     label: '系列',
            //     icon: 'pi pi-fw iconfont icon-_classification',
            //     url: SERIES_INDEX_URL
            // },
            // {
            //     label: '作品',
            //     icon: 'pi pi-fw iconfont icon-_classification',
            //     url: PRODUCT_INDEX_URL
            // },
            {
                label: '专辑',
                icon: 'pi pi-fw iconfont icon-24gl-musicAlbum2',
                items: [
                    {
                        label: '专辑首页',
                        icon: 'pi pi-fw iconfont icon-24gl-musicAlbum2',
                        url: ALBUM_INDEX_URL
                    },
                    {
                        label: '专辑列表',
                        icon: 'pi pi-fw pi-list',
                        url: ALBUM_LIST_URL
                    }
                ]
            },
            // {
            //     label: 'BD/DVD',
            //     icon: 'pi pi-fw iconfont icon-Video-Disc',
            //     items: [
            //         {
            //             label: '碟片首页',
            //             icon: 'pi pi-fw iconfont icon-Video-Disc',
            //             url: DISC_INDEX_URL
            //         },
            //         {
            //             label: '碟片列表',
            //             icon: 'pi pi-fw pi-list'
            //         }
            //     ]
            // },
            // {
            //     label: '书籍',
            //     icon: 'pi pi-fw pi-book',
            //     items: [
            //         {
            //             label: '书籍首页',
            //             icon: 'pi pi-fw pi-book',
            //             url: ALBUM_INDEX_URL
            //         },
            //         {
            //             label: '书籍列表',
            //             icon: 'pi pi-fw pi-list'
            //         }
            //     ]
            // },
            // {
            //     label: '周边',
            //     icon: 'pi pi-fw iconfont icon-yinshuabaozhuang',
            //     items: [
            //         {
            //             label: '周边首页',
            //             icon: 'pi pi-fw iconfont icon-yinshuabaozhuang',
            //             url: DISC_INDEX_URL
            //         },
            //         {
            //             label: '周边列表',
            //             icon: 'pi pi-fw pi-list'
            //         }
            //     ]
            // },
            // {
            //     label: '游戏',
            //     icon: 'pi pi-fw iconfont icon-youxi',
            //     items: [
            //         {
            //             label: '游戏首页',
            //             icon: 'pi pi-fw iconfont icon-youxi',
            //             url: GAME_INDEX_URL
            //         },
            //         {
            //             label: '游戏列表',
            //             icon: 'pi pi-fw pi-list'
            //         }
            //     ]
            // }
        ]
    },
    {
        label: '博客',
        icon: 'pi pi-fw pi-book',
        url: BLOG_INDEX_URL
    },
    {
        label: '应用',
        icon: 'pi iconfont icon-yingyong',
        url: APP_INDEX_URL
    },
    {
        label: '登出', icon: 'pi iconfont icon-logout', url: LOGOUT_URL
    }
];
//endregion