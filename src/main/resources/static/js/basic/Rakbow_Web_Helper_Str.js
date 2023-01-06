const DOMAIN_URL = "http://localhost:8083";

//region page
const HOME_INDEX_URL = DOMAIN_URL;
const DATABASE_INDEX_URL = DOMAIN_URL + "/db";

const APP_INDEX_URL = DOMAIN_URL + "/app";
const BLOG_INDEX_URL = DOMAIN_URL + "/blog";

const SERIES_INDEX_URL = DOMAIN_URL + "/db/series";
const PRODUCT_INDEX_URL = DOMAIN_URL + "/db/product/list";

const ALBUM_LIST_URL = DOMAIN_URL + "/db/album/list";
const ALBUM_INDEX_URL = DOMAIN_URL + "/db/albums";

const DISC_INDEX_URL = DOMAIN_URL + "/db/discs";
const DISC_LIST_URL = DOMAIN_URL + "/db/disc/list";

const BOOK_INDEX_URL = DOMAIN_URL + "/db/books";
const BOOK_LIST_URL = DOMAIN_URL + "/db/book/list";

const GAME_INDEX_URL = DOMAIN_URL + "/db/games";

//endregion

//region album
const DELETE_ALBUM_URL = DOMAIN_URL + "/db/album/delete";
const UPDATE_ALBUM_URL = DOMAIN_URL + "/db/album/update";
const INSERT_ALBUM_URL = DOMAIN_URL + "/db/album/add";
const GET_ALBUMS_LIST_URL = DOMAIN_URL + "/db/album/get-albums";

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

//region product
const GET_PRODUCTS_BY_SERIES_ID_URL = DOMAIN_URL + "/db/product/get-products-by-series-id";
const ADD_PRODUCT_URL = DOMAIN_URL + "/db/product/add";
const UPDATE_PRODUCT_URL = DOMAIN_URL + "/db/product/update";
const UPDATE_PRODUCT_DESCRIPTION_URL = DOMAIN_URL + "/db/product/update-description";
const UPDATE_PRODUCT_STAFF_URL = DOMAIN_URL + "/db/product/update-staffs";
const INSERT_PRODUCT_IMAGES_URL = DOMAIN_URL + "/db/product/add-images";
const UPDATE_PRODUCT_IMAGES_URL = DOMAIN_URL + "/db/product/update-images";
//endregion

//region disc
const DELETE_DISC_URL = DOMAIN_URL + "/db/disc/delete";
const UPDATE_DISC_URL = DOMAIN_URL + "/db/disc/update";
const INSERT_DISC_URL = DOMAIN_URL + "/db/disc/add";
const GET_DISCS_LIST_URL = DOMAIN_URL + "/db/disc/get-discs";

const INSERT_DISC_IMAGES_URL = DOMAIN_URL + "/db/disc/add-images";
const UPDATE_DISC_IMAGES_URL = DOMAIN_URL + "/db/disc/update-images";
const UPDATE_DISC_SPEC_URL = DOMAIN_URL + "/db/disc/update-spec";
const UPDATE_DISC_DESCRIPTION_URL = DOMAIN_URL + "/db/disc/update-description";
const UPDATE_DISC_BONUS_URL = DOMAIN_URL + "/db/disc/update-bonus";
//endregion

//region book
const DELETE_BOOK_URL = DOMAIN_URL + "/db/book/delete";
const UPDATE_BOOK_URL = DOMAIN_URL + "/db/book/update";
const INSERT_BOOK_URL = DOMAIN_URL + "/db/book/add";
const GET_BOOKS_LIST_URL = DOMAIN_URL + "/db/book/get-books";

const INSERT_BOOK_IMAGES_URL = DOMAIN_URL + "/db/book/add-images";
const UPDATE_BOOK_IMAGES_URL = DOMAIN_URL + "/db/book/update-images";
const UPDATE_BOOK_AUTHORS_URL = DOMAIN_URL + "/db/book/update-authors";
const UPDATE_BOOK_SPEC_URL = DOMAIN_URL + "/db/book/update-spec";
const UPDATE_BOOK_DESCRIPTION_URL = DOMAIN_URL + "/db/book/update-description";
const UPDATE_BOOK_BONUS_URL = DOMAIN_URL + "/db/book/update-bonus";
//endregion

//region merch
const DELETE_MERCH_URL = DOMAIN_URL + "/db/merch/delete";
const UPDATE_MERCH_URL = DOMAIN_URL + "/db/merch/update";
const INSERT_MERCH_URL = DOMAIN_URL + "/db/merch/add";
const GET_MERCHS_LIST_URL = DOMAIN_URL + "/db/merch/get-merchs";

const INSERT_MERCH_IMAGES_URL = DOMAIN_URL + "/db/merch/add-images";
const UPDATE_MERCH_IMAGES_URL = DOMAIN_URL + "/db/merch/update-images";
const UPDATE_MERCH_SPEC_URL = DOMAIN_URL + "/db/merch/update-spec";
const UPDATE_MERCH_DESCRIPTION_URL = DOMAIN_URL + "/db/merch/update-description";
//endregion

//region game
const DELETE_GAME_URL = DOMAIN_URL + "/db/game/delete";
const UPDATE_GAME_URL = DOMAIN_URL + "/db/game/update";
const INSERT_GAME_URL = DOMAIN_URL + "/db/game/add";
const GET_GAMES_LIST_URL = DOMAIN_URL + "/db/game/get-games";

const INSERT_GAME_IMAGES_URL = DOMAIN_URL + "/db/game/add-images";
const UPDATE_GAME_IMAGES_URL = DOMAIN_URL + "/db/game/update-images";
const UPDATE_GAME_ORGANIZATIONS_URL = DOMAIN_URL + "/db/game/update-organizations";
const UPDATE_GAME_STAFFS_URL = DOMAIN_URL + "/db/game/update-staffs";
const UPDATE_GAME_DESCRIPTION_URL = DOMAIN_URL + "/db/game/update-description";
const UPDATE_GAME_BONUS_URL = DOMAIN_URL + "/db/game/update-bonus";
//endregion

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
            {
                label: '作品', icon: 'pi pi-fw iconfont icon-_classification', url: PRODUCT_INDEX_URL
            },
            {
                label: '专辑', icon: 'pi pi-fw iconfont icon-24gl-musicAlbum2',
                items: [
                    {label: '专辑首页', icon: 'pi pi-fw iconfont icon-24gl-musicAlbum2', url: ALBUM_INDEX_URL},
                    {label: '专辑列表', icon: 'pi pi-fw pi-list', url: ALBUM_LIST_URL}
                ]
            },
            {
                label: 'BD/DVD', icon: 'pi pi-fw iconfont icon-Video-Disc',
                items: [
                    {label: '碟片首页', icon: 'pi pi-fw iconfont icon-Video-Disc', url: DISC_INDEX_URL},
                    {label: '碟片列表', icon: 'pi pi-fw pi-list', url: DISC_LIST_URL}
                ]
            },
            {
                label: '书籍', icon: 'pi pi-fw pi-book',
                items: [
                    {label: '书籍首页', icon: 'pi pi-fw pi-book', url: ALBUM_INDEX_URL},
                    {label: '书籍列表', icon: 'pi pi-fw pi-list', url: BOOK_LIST_URL}
                ]
            },
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
            {
                label: '作品',
                icon: 'pi pi-fw iconfont icon-_classification',
                url: PRODUCT_INDEX_URL
            },
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
            {
                label: 'BD/DVD',
                icon: 'pi pi-fw iconfont icon-Video-Disc',
                items: [
                    {
                        label: '碟片首页',
                        icon: 'pi pi-fw iconfont icon-Video-Disc',
                        url: DISC_INDEX_URL
                    },
                    {
                        label: '碟片列表',
                        icon: 'pi pi-fw pi-list',
                        url: DISC_LIST_URL
                    }
                ]
            },
            {
                label: '书籍',
                icon: 'pi pi-fw pi-book',
                items: [
                    {
                        label: '书籍首页',
                        icon: 'pi pi-fw pi-book',
                        url: BOOK_INDEX_URL
                    },
                    {
                        label: '书籍列表',
                        icon: 'pi pi-fw pi-list',
                        url: BOOK_LIST_URL
                    }
                ]
            },
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