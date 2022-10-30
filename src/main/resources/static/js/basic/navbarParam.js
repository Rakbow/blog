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