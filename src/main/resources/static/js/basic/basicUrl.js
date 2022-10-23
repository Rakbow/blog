const DOMAIN_URL = "http://localhost:8080";

//region page
const HOME_INDEX_URL = DOMAIN_URL;
const DATABASE_INDEX_URL = DOMAIN_URL + "/db";

const APP_INDEX_URL = DOMAIN_URL + "/app";
const BLOG_INDEX_URL = DOMAIN_URL + "/blog";

const SERIES_INDEX_URL = DOMAIN_URL + "/db/series";
const PRODUCT_INDEX_URL = DOMAIN_URL + "/db/product";

const ALBUM_LIST_URL = DOMAIN_URL + "/db/album/list";
const ALBUM_INDEX_URL = DOMAIN_URL + "/db/album/card";

const DISC_INDEX_URL = DOMAIN_URL + "/db/disc";
const BOOK_INDEX_URL = DOMAIN_URL + "/db/book";
const GOODS_INDEX_URL = DOMAIN_URL + "/db/goods";
const GAME_INDEX_URL = DOMAIN_URL + "/db/game";

//endregion

//region album
const GET_ALL_ALBUM_URL = DOMAIN_URL + "/db/album/getAll";
const DELETE_ALBUM_URL = DOMAIN_URL + "/db/album/delete";
const UPDATE_ALBUM_URL = DOMAIN_URL + "/db/album/update";
const INSERT_ALBUM_URL = DOMAIN_URL + "/db/album/insert";
const GET_ALBUM_URL = DOMAIN_URL + "/db/album/getAlbum";

const UPDATE_ALBUM_ARTISTS_URL = DOMAIN_URL + "/db/album/updateAlbumArtists";
const UPDATE_ALBUM_TRACK_INFO_URL = DOMAIN_URL + "/db/album/updateAlbumTrackInfo";
const INSERT_ALBUM_IMAGES_URL = DOMAIN_URL + "/db/album/insertAlbumImages";
const UPDATE_ALBUM_IMAGES_URL = DOMAIN_URL + "/db/album/updateAlbumImages";
const UPDATE_ALBUM_DESCRIPTION_URL = DOMAIN_URL + "/db/album/updateAlbumDescription";
const UPDATE_ALBUM_BONUS_URL = DOMAIN_URL + "/db/album/updateAlbumBonus";
//endregion

//region user
const CHECK_USER_AUTHORITY_URL = DOMAIN_URL + "/user/checkAuthority";
const LOGIN_URL = DOMAIN_URL + "/login";
const LOGOUT_URL = DOMAIN_URL + "/logout";
//endregion

//common
const GET_ALL_PRODUCT_URL = DOMAIN_URL + "/product/getProductsBySeriesId";