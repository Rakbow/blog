const DOMIAN_URL = "http://localhost:8080";

//region album
const GET_ALL_ALBUM_URL = DOMIAN_URL + "/db/album/getAll";
const DELETE_ALBUM_URL = DOMIAN_URL + "/db/album/delete";
const UPDATE_ALBUM_URL = DOMIAN_URL + "/db/album/update";
const INSERT_ALBUM_URL = DOMIAN_URL + "/db/album/insert";
const GET_ALBUM_URL = DOMIAN_URL + "/db/album/getAlbum";

const UPDATE_ALBUM_ARTISTS_URL = DOMIAN_URL + "/db/album/updateAlbumArtists";
const UPDATE_ALBUM_TRACK_INFO_URL = DOMIAN_URL + "/db/album/updateAlbumTrackInfo";
const UPDATE_ALBUM_IMAGES_URL = DOMIAN_URL + "/db/album/updateAlbumImages";
//endregion

//region user
const CHECK_USER_AUTHORITY_URL = DOMIAN_URL + "/user/isRoot";
const LOGIN_URL = DOMIAN_URL + "/login";
//endregion

//common
const GET_ALL_PRODUCT_URL = DOMIAN_URL + "/common/getProducts/";