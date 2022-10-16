const DOMIAN_URL = "http://localhost:8080";

//region album
const GET_ALL_ALBUM_URL = DOMIAN_URL + "/db/album/getAll";
const DELETE_ALBUM_URL = DOMIAN_URL + "/db/album/delete";
const UPDATE_ALBUM_URL = DOMIAN_URL + "/db/album/update";
const INSERT_ALBUM_URL = DOMIAN_URL + "/db/album/insert";
const GET_ALBUM_URL = DOMIAN_URL + "/db/album/getAlbum";

const UPDATE_ALBUM_ARTISTS_URL = DOMIAN_URL + "/db/album/updateAlbumArtists";
const UPDATE_ALBUM_TRACK_INFO_URL = DOMIAN_URL + "/db/album/updateAlbumTrackInfo";
const INSERT_ALBUM_IMAGES_URL = DOMIAN_URL + "/db/album/insertAlbumImages";
const UPDATE_ALBUM_IMAGES_URL = DOMIAN_URL + "/db/album/updateAlbumImages";
const UPDATE_ALBUM_DESCRIPTION_URL = DOMIAN_URL + "/db/album/updateAlbumDescription";
const UPDATE_ALBUM_BONUS_URL = DOMIAN_URL + "/db/album/updateAlbumBonus";
//endregion

//region user
const CHECK_USER_AUTHORITY_URL = DOMIAN_URL + "/user/checkAuthority";
const LOGIN_URL = DOMIAN_URL + "/login";
//endregion

//common
const GET_ALL_PRODUCT_URL = DOMIAN_URL + "/product/getProductsBySeriesId";