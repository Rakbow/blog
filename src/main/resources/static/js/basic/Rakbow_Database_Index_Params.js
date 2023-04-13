import {HttpUtil} from '/js/basic/Http_Util.js';

const albumIndex = {
    template: `
        <div class="grid mt-2">
            <div class="col-2" style="min-width: 300px">
                <p-panel>
                    <template #header>
                    <span class="text-start side-panel-header">
                        <i class="pi pi-filter"></i><span><strong>条件过滤</strong></span>
                    </span>
                    </template>
                    <div class="grid p-fluid">
                        <div class="col-6 p-1">
                            <label>名称</label>
                            <p-inputtext v-model.trim="queryParams.filters.name.value"></p-inputtext>
                        </div>
                        <div class="col-6 p-1">
                            <label>编号</label>
                            <p-inputtext v-model.trim="queryParams.filters.catalogNo.value"></p-inputtext>
                        </div>
                        <div class="col-6 p-1">
                            <label>所属系列</label>
                            <p-multiselect v-model="queryParams.filters.franchises.value" @change="getProducts($event.value)"
                                           :options="franchiseSet" placeholder="所属系列"
                                           option-label="label" option-value="value" display="chip" :filter="true">
                            </p-multiselect>
                        </div>
                        <div class="col-6 p-1">
                            <label>所属作品</label>
                            <p-multiselect v-model="queryParams.filters.products.value" :options="productSet"
                                           option-label="label" option-value="value" placeholder="请先选择所属系列"
                                           display="chip" :filter="true" :disabled="productSelect">
                            </p-multiselect>
                        </div>
                        <div class="col-6 p-1">
                            <label>是否包含特典</label>
                            <p-dropdown v-model="queryParams.filters.hasBonus.value" :options="hasBonusSet"
                                        placeholder="所有" option-label="label"
                                        option-value="value">
                            </p-dropdown>
                        </div>
                        <div class="col-6 p-1">
                            <label>出版形式</label>
                            <p-multiselect v-model="queryParams.filters.publishFormat.value"
                                           :options="publishFormatSet"
                                           option-label="label" option-value="value"
                                           placeholder="所有"
                                           display="chip" :filter="true">
                            </p-multiselect>
                        </div>
                        <div class="col-6 p-1">
                            <label>专辑分类</label>
                            <p-multiselect v-model="queryParams.filters.albumFormat.value"
                                           :options="albumFormatSet"
                                           option-label="label" option-value="value"
                                           placeholder="所有"
                                           display="chip" :filter="true">
                            </p-multiselect>
                        </div>
                        <div class="col-6 p-1">
                            <label>媒体格式</label>
                            <p-multiselect v-model="queryParams.filters.mediaFormat.value"
                                           :options="mediaFormatSet"
                                           option-label="label" option-value="value"
                                           placeholder="所有"
                                           display="chip" :filter="true">
                            </p-multiselect>
                        </div>
                        <div class="col-4 col-offset-2" style="text-align: right">
                            <p-button icon="pi pi-filter-slash"
                                      class="p-button-rounded p-button-info"
                                      v-tooltip.bottom="{value:'清空', class: 'short-tooltip'}"
                                      @click="clearSearch"></p-button>
                        </div>
                        <div class="col-4" style="text-align: left">
                            <p-button icon="pi pi-filter-fill"
                                      class="p-button-rounded p-button-success"
                                      v-tooltip.bottom="{value:'筛选', class: 'short-tooltip'}"
                                      @click="getAlbums"></p-button>
                        </div>
                    </div>
                </p-panel>
                <br>
                <p-panel v-if="totalLoading">
                    <template #header>
                        <span class="text-start side-panel-header">
                            <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
                        </span>
                    </template>
                    <div class="grid">
                        <span class="small_font">
                            <div class="info_bit_small small_font grid m-0 p-0"
                                 v-if="tmpList5.length != 0"
                                 v-for="(index) of tmpList5">
                                <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                    <a>
                                        <p-skeleton size="4rem"></p-skeleton>
                                    </a>
                                </div>
                                <div class="col p-0" style="height: 80px">
                                    <ul class="info_bit_small_other">
                                        <li>
                                            <a>
                                                <span class="text-truncate-2 ml-2 mr-2">
                                                    <p-skeleton width="8rem"></p-skeleton>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <span class="small_font col-6 related-item-catalog">
                                                <p-skeleton width="3rem"></p-skeleton>
                                            </span>
                                            <span class="small_font col-6 related-item-date">
                                                <p-skeleton width="3rem"></p-skeleton>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </span>
                    </div>
                </p-panel>
                <p-panel v-if="!totalLoading">
                    <template #header>
                    <span class="text-start side-panel-header">
                        <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
                    </span>
                    </template>
                    <div class="grid">
                    <span class="small_font">
                        <div class="info_bit_small small_font grid m-0 p-0"
                             v-if="justAddedItems.length != 0"
                             v-for="addedAlbum of justAddedItems">
                            <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                <a :href="'/db/album/'+ addedAlbum.id">
                                    <img class="sidebar-panel-image-small" :src="addedAlbum.cover.blackUrl"
                                         v-tooltip.right="'收录时间: ' + addedAlbum.addedTime + '编辑时间: ' + addedAlbum.editedTime">
                                </a>
                            </div>
                            <div class="col p-0" style="height: 80px">
                                <ul class="info_bit_small_other">
                                    <li>
                                        <a class="small_font"
                                           :href="'/db/album/'+ addedAlbum.id ">
                                            <span class="text-truncate-2 ml-2 mr-2">
                                                {{addedAlbum.name}}
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <span class="small_font col-6 related-item-catalog">
                                            {{addedAlbum.catalogNo ? addedAlbum.catalogNo : 'N/A'}}
                                        </span>
                                        <span class="small_font col-6 related-item-date">
                                            {{addedAlbum.releaseDate}}
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </span>
                    </div>
                </p-panel>
            </div>
            <div class="col">
                <p-dataview :value="albums" :layout="layout" :paginator="true" :rows="itemRows"
                            :always-show-paginator="totalRecords != 0"
                            :sort-order="queryParams.sortOrder" :sort-field="queryParams.sortField"
                            :lazy="true" @page="onPage($event)" :total-records="totalRecords"
                            :rows-per-page-options="[10,20]" paginator-template="FirstPageLink PrevPageLink
                        PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                            current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据">
                    <template #empty>
                        <div class="mt-2 mb-2">
                            <span class="emptyInfo">暂无符合条件的结果</span>
                        </div>
                    </template>
                    <template #header>
                        <div class="grid grid-nogutter">
                            <div class="col-6" style="text-align: left">
                                <p-dropdown v-model="sortKey" :options="sortOptions" option-label="label"
                                            placeholder="按发行时间排序"
                                            @change="onSortChange($event)"></p-dropdown>
                            </div>
                            <div class="col-6" style="text-align: right">
                                <p-dataviewlayoutoptions v-model="layout"></p-dataviewlayoutoptions>
                            </div>
                        </div>
                    </template>
                    <template #grid="slotProps">
                        <div style="width: 197px">
                            <div class="index-item-grid-card card">
                                <p-card>
                                    <template #header>
                                        <a :href="'/db/album/'+ slotProps.data.id">
                                            <div class="block-img"
                                                 :style="'background-image:url('+ slotProps.data.cover.url+');' ">
                                                <p-tag class="catalog ml-1 mt-1">
                                                    {{slotProps.data.catalogNo?slotProps.data.catalogNo:"N/A"}}
                                                </p-tag>
                                                <div class="absolute releaseDate">
                                                    <span class="text-center" style="color: white">{{slotProps.data.releaseDate}}</span>
                                                </div>
                                            </div>
                                        </a>
                                    </template>
                                    <template #title>
                                    <span class="text-truncate-2">
                                        <a :href="'/db/merch/'+ slotProps.data.id">{{slotProps.data.name}}</a>
                                    </span>
                                    </template>
                                    <!--                                <template #subtitle>-->
                                    <!--                                    <div class="grid">-->
                                    <!--                                        <span v-if="slotProps.data.price != 0" class="col-8 p-1 price-text ml-1">-->
                                    <!--                                            {{slotProps.data.price}} {{slotProps.data.currencyUnit}}-->
                                    <!--                                        </span>-->
                                    <!--                                        <span v-else class="col-8 p-1 price-text">-->
                                    <!--                                            N/A-->
                                    <!--                                        </span>-->
                                    <!--                                    </div>-->
                                    <!--                                </template>-->
                                    <template #content>
                                        <div class="grid">
                                            <div class="col-12 p-1">
                                            <span v-for="format of slotProps.data.albumFormat" style="display:inline">
                                                <p-tag class="ml-1" :value="format.label"></p-tag>
                                            </span>
                                            </div>
                                        </div>
                                    </template>
                                    <template #footer>
                                        <div class="grid">
                                            <div class="col-8 p-1 ml-1">
                                            <span v-for="product of slotProps.data.products" style="display:inline">
                                                <a class="no-text-decoration" :href="'/db/product/' + product.value">
                                                    <i class="pi pi-tag ml-1" style="color: white"
                                                       v-tooltip.bottom="product.label"></i>
                                                </a>
                                            </span>
                                            </div>
                                            <div>
                                            <span class="col-3 p-1 text-end has-bonus-tag" v-if="slotProps.data.hasBonus">
                                                <p-tag style="background: #2f364f" class="ml-1" value="特典"></p-tag>
                                            </span>
                                            </div>
                                        </div>
                                    </template>
                                </p-card>
                            </div>
                        </div>
                    </template>
                    <template #list="slotProps">
                        <div class="col-12">
                            <div class="index-list-item">
                                <a class="text-center" :href="'/db/album/'+ slotProps.data.id">
                                    <img :src="slotProps.data.cover.thumbUrl70" :alt="slotProps.data.cover.name"/>
                                </a>
                                <div class="index-list-item-detail">
                                <span class="index-list-item-name text-truncate-1">
                                    <a :href="'/db/album/'+ slotProps.data.id">{{slotProps.data.name}}</a>
                                </span>
                                    <span class="small-font" style="margin: 0 0 .5rem 0;">
                                    <b class="label">{{slotProps.data.catalogNo}}</b><span class="label">&nbsp{{slotProps.data.releaseDate}}</span>
                                </span>
                                    <span class="ml-2">
                                    <span class="has-bonus-tag" v-if="slotProps.data.hasBonus">
                                        <p-tag style="background: #001122" class="ml-1" value="特典"></p-tag>
                                    </span>
                                    <div v-for="format of slotProps.data.albumFormat" style="display:inline">
                                        <p-tag class="product-tag ml-1" :value="format.label"></p-tag>
                                    </div>
                                </span>
                                    <br/>
                                    <span v-for="product of slotProps.data.products" style="display:inline">
                                    <a class="no-text-decoration" :href="'/db/product/' + product.value">
                                        <p-chip :label="product.label"></p-chip>
                                        <!--                                        <i class="pi pi-tag ml-1" style="color: white"-->
                                        <!--                                           v-tooltip.bottom="product.label"></i>-->
                                    </a>
                                </span>
                                </div>
                            </div>
                        </div>
                    </template>
                </p-dataview>
            </div>
            <div class="col-2" style="min-width: 300px">
                <p-panel v-if="totalLoading">
                    <template #header>
                        <span class="text-start side-panel-header">
                            <i class="pi pi-list"></i>
                            <span><strong>浏览排名</strong></span>
                        </span>
                    </template>
                    <div class="grid">
                        <span class="small_font">
                            <div class="info_bit_small small_font grid m-0 p-0"
                                 v-if="tmpList10.length != 0"
                                 v-for="(index) of tmpList10">
                                <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                    <div class="col-12 p-0 m-0">
                                        <p-skeleton width="3rem" class="mt-2"></p-skeleton>
                                    </div>
                                    <span class="col-12 p-0 m-0" style="font-size: 9px">
                                        <i class="pi pi-eye mt-1 mb-1"></i><br><p-skeleton width="3rem" class="mb-1"></p-skeleton>
                                    </span>
                                </div>
                                <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                    <p-skeleton size="4rem"></p-skeleton>
                                </div>
                                <div class="col p-0" style="height: 90px">
                                    <ul class="info_bit_small_other">
                                        <li>
                                            <a>
                                                <span class="text-truncate-2 mr-2">
                                                    <p-skeleton width="8rem" class="mb-1 mt-1"></p-skeleton>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-catalog">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-date">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </span>
                    </div>
                </p-panel>
                <p-panel v-if="!totalLoading">
                    <template #header>
                    <span class="text-start side-panel-header">
                        <i class="pi pi-list"></i><span><strong>浏览排名</strong></span>
                    </span>
                    </template>
                    <div class="grid">
                    <span class="small_font">
                        <div class="info_bit_small small_font grid m-0 p-0"
                             v-if="popularItems.length != 0"
                             v-for="(album, index) of popularItems">
                            <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                <div class="col-12 p-0 m-0">
                                    <i :class="'pi remixicon ri-number-' + index"
                                       style="font-size: 2rem"></i>
                                </div>
                                <span class="col-12 p-0 m-0" style="font-size: 9px">
                                    <i class="pi pi-eye"></i><br>{{album.visitNum}}
                                </span>
                            </div>
                            <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                <a :href="'/db/album/'+ album.id">
                                    <img class="sidebar-panel-image-small" :src="album.cover.blackUrl" v-tooltip.bottom="'收录时间: ' + album.addedTime + '编辑时间: ' + album.editedTime">
                                </a>
                            </div>
                            <div class="col p-0" style="height: 90px">
                                <ul class="info_bit_small_other">
                                    <li>
                                        <a class="small_font"
                                           :href="'/db/album/'+ album.id ">
                                            <span class="text-truncate-2 mr-2">
                                                {{album.name}}
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <span class="small_font related-item-catalog">
                                            {{album.catalogNo ? album.catalogNo : 'N/A'}}
                                        </span>
                                    </li>
                                    <li>
                                        <span class="small_font related-item-date">
                                            {{album.releaseDate}}
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </span>
                    </div>
                </p-panel>
            </div>
        </div>
    `,
    mounted() {
        this.init();
        this.getAlbums();
    },
    data() {
        return {
            albums: null,
            sortOptions: [
                {label: '按发行时间正序', value: 'releaseDate'},
                {label: '按发行时间逆序', value: '!releaseDate'},
            ],
            queryParams: {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    name: {value: null},
                    catalogNo: {value: null},
                    hasBonus: {value: null},
                    publishFormat: {value: null},
                    albumFormat: {value: null},
                    mediaFormat: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                }
            },

            justAddedItems: [],
            popularItems: [],
            publishFormatSet: [],
            albumFormatSet: [],
            mediaFormatSet: [],

            itemRows: 20,
            layout: 'grid',
            sortKey: null,
            totalRecords: null,
            productSelect: true,
            productSet: null,
            hasBonusSet,
            franchiseSet: [],

            totalLoading: false,
            tmpList10,
            tmpList5,
        }
    },
    watch: {
        layout:function(newValue) {
            if (newValue === "grid") {
                this.itemRows = 20;
            } else {
                this.itemRows = 10;
            }
        },
    },
    methods: {
        init() {
            let json = {
                entityType: ENTITY.ALBUM
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_INDEX_INIT_DATA_URL, json)
                .then(res => {
                    this.justAddedItems = res.justAddedItems;
                    this.popularItems = res.popularItems;
                    this.publishFormatSet = res.publishFormatSet;
                    this.albumFormatSet = res.albumFormatSet;
                    this.mediaFormatSet = res.mediaFormatSet;
                    this.franchiseSet = res.franchiseSet;
                    this.totalLoading = false;
                })
        },
        clearSearch() {
            this.queryParams = {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    name: {value: null},
                    catalogNo: {value: null},
                    hasBonus: {value: null},
                    publishFormat: {value: null},
                    albumFormat: {value: null},
                    mediaFormat: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                }
            };
            this.getAlbums();
        },
        getAlbums() {
            this.queryParams.rows = this.itemRows;
            let json = {
                pageLabel: "index",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_ALBUMS_URL, json)
                .then(res => {
                    this.albums = res.data;
                    this.totalRecords = res.total;
                })
        },
        getProducts(data) {
            let json = {
                franchises: data,
                entityType: ENTITY.ALBUM
            };
            HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.length !== 0) {
                        this.queryParams.filters.products.value = [];
                        this.productSet = res.data;
                        this.productSelect = false;
                    }else {
                        this.productSelect = true;
                    }
                })
        },
        onPage(event) {
            this.queryParams.first = event.first;
            this.queryParams.rows = event.rows;
            this.getAlbums();
        },
        onSortChange(event) {
            const value = event.value.value;
            const sortValue = value;

            if (value.indexOf('!') === 0) {
                this.queryParams.sortOrder = -1;
                this.queryParams.sortField = value.substring(1, value.length);
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getAlbums();
            } else {
                this.queryParams.sortOrder = 1;
                this.queryParams.sortField = value;
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getAlbums();
            }
        },
    },
    components: {
        "p-dataview": primevue.dataview,
        "p-dataviewlayoutoptions": primevue.dataviewlayoutoptions,
        "p-card": primevue.card,
        "p-panel": primevue.panel,
        "p-multiselect": primevue.multiselect,

        "p-chip": primevue.chip,
        "p-inputtext": primevue.inputtext,
        "p-tag": primevue.tag,
        "p-dropdown": primevue.dropdown,
        "p-button": primevue.button,
        "p-divider": primevue.divider,
        "p-skeleton": primevue.skeleton,
    }
};

const bookIndex = {
    template: `
        
<div class="grid mt-2">
        <div class="col-2" style="min-width: 310px">
            <p-panel>
                <template #header>
            <span class="text-start side-panel-header">
                <i class="pi pi-filter"></i><span><strong>条件过滤</strong></span>
            </span>
                </template>
                <div class="grid p-fluid">
                    <div class="col-6 p-1">
                        <label>书名</label>
                        <p-inputtext v-model.trim="queryParams.filters.title.value"></p-inputtext>
                    </div>
                    <div class="col-6 p-1">
                        <label>图书类型</label>
                        <p-dropdown v-model="queryParams.filters.bookType.value" :options="bookTypeSet"
                                    placeholder="所有" option-label="label"
                                    option-value="value">
                        </p-dropdown>
                    </div>
                    <div class="col-6 p-1">
                        <label>ISBN-10</label>
                        <p-inputtext v-model.trim="queryParams.filters.isbn10.value"></p-inputtext>
                    </div>
                    <div class="col-6 p-1">
                        <label>ISBN-13</label>
                        <p-inputtext v-model.trim="queryParams.filters.isbn13.value"></p-inputtext>
                    </div>
                    <div class="col-6 p-1">
                        <label>所属系列</label>
                        <p-multiselect v-model="queryParams.filters.franchises.value" @change="getProducts($event.value)"
                                        :options="franchiseSet" placeholder="所属系列"
                                        option-label="label" option-value="value" display="chip" :filter="true">
                        </p-multiselect>
                    </div>
                    <div class="col-6 p-1">
                        <label>所属作品</label>
                        <p-multiselect v-model="queryParams.filters.products.value" :options="productSet"
                                        option-label="label" option-value="value" placeholder="请先选择所属系列"
                                        display="chip" :filter="true" :disabled="productSelect">
                        </p-multiselect>
                    </div>
                    <div class="col-6 p-1">
                        <label>是否包含特典</label>
                        <p-dropdown v-model="queryParams.filters.hasBonus.value" :options="hasBonusSet"
                                    placeholder="所有" option-label="label"
                                    option-value="value">
                        </p-dropdown>
                    </div>
                    <div class="col-6 p-1">
                        <label>出版社</label>
                        <p-inputtext v-model.trim="queryParams.filters.publisher.value"></p-inputtext>
                    </div>
                    <div class="col-6 p-1">
                        <label>区域</label>
                        <p-dropdown v-model="queryParams.filters.region.value" :options="regionSet"
                                    :filter="true" :show-clear="true" option-label="nameZh" option-value="code">
                            <template #value="slotProps">
                                <div class="country-item" v-if="slotProps.value">
                                    <span :class="'fi fi-' + slotProps.value"></span>
                                    <div class="ml-2">{{regionCode2NameZh(slotProps.value, regionSet)}}</div>
                                </div>
                                <span v-else>选择地区</span>
                            </template>
                            <template #option="slotProps">
                                <div class="country-item">
                                    <span :class="'fi fi-' + slotProps.option.code"></span>
                                    <div class="ml-2">{{slotProps.option.nameZh}}</div>
                                </div>
                            </template>
                        </p-dropdown>
                    </div>
                    <div class="col-6 p-1">
                        <label>语言</label>
                        <p-dropdown v-model="queryParams.filters.publishLanguage.value" :options="languageSet"
                                    placeholder="所有" option-label="nameZh"
                                    option-value="code">
                        </p-dropdown>
                    </div>
                    <div class="col-4 col-offset-2" style="text-align: right">
                        <p-button icon="pi pi-filter-slash"
                                    class="p-button-rounded p-button-info"
                                    v-tooltip.bottom="{value:'清空', class: 'short-tooltip'}"
                                    @click="clearSearch"></p-button>
                    </div>
                    <div class="col-4" style="text-align: left">
                        <p-button icon="pi pi-filter-fill"
                                    class="p-button-rounded p-button-success"
                                    v-tooltip.bottom="{value:'筛选', class: 'short-tooltip'}"
                                    @click="getBooks"></p-button>
                    </div>
                </div>
            </p-panel>
            <br>
            <p-panel v-if="totalLoading">
                <template #header>
                    <span class="text-start side-panel-header">
                        <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
                    </span>
                </template>
                <div class="grid">
                    <span class="small_font">
                        <div class="info_bit_small small_font grid m-0 p-0"
                             v-if="tmpList5.length != 0"
                             v-for="(index) of tmpList5">
                            <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                <a>
                                    <p-skeleton size="4rem"></p-skeleton>
                                </a>
                            </div>
                            <div class="col p-0" style="height: 80px">
                                <ul class="info_bit_small_other">
                                    <li>
                                        <a>
                                            <span class="text-truncate-2 ml-2 mr-2">
                                                <p-skeleton width="8rem"></p-skeleton>
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <span class="small_font col-6 related-item-catalog">
                                            <p-skeleton width="3rem"></p-skeleton>
                                        </span>
                                        <span class="small_font col-6 related-item-date">
                                            <p-skeleton width="3rem"></p-skeleton>
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </span>
                </div>
            </p-panel>
            <p-panel v-if="!totalLoading">
                <template #header>
            <span class="text-start side-panel-header">
                <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
            </span>
                </template>
                <div class="grid">
            <span class="small_font">
                <div class="info_bit_small small_font grid m-0 p-0"
                        v-if="justAddedItems.length != 0"
                        v-for="book of justAddedItems">
                    <div class="sidebar-panel-image-small-div book_info_bit_thumb mt-2">
                        <a :href="'/db/book/'+ book.id">
                            <img class="sidebar-panel-image-small" :src="book.cover.blackUrl"
                                    v-tooltip.right="'收录时间: ' + book.addedTime + '编辑时间: ' + book.editedTime">
                        </a>
                    </div>
                    <div class="col p-0" style="height: 80px">
                        <ul class="info_bit_small_other">
                            <li>
                                <a class="small_font"
                                    :href="'/db/book/'+ book.id ">
                                    <span class="text-truncate-2 ml-2 mr-2">
                                        {{book.title}}
                                    </span>
                                </a>
                            </li>
                            <li>
                                <span class="small_font col-6 related-item-catalog">
                                    {{book.isbn13}}
                                </span>
                                <span class="small_font col-6 related-item-date">
                                    {{book.publishDate}}
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>
            </span>
                </div>
            </p-panel>
        </div>
        <div class="col">
            <p-dataview :value="books" :layout="layout" :paginator="true" :rows="itemRows"
                        :always-show-paginator="totalRecords != 0"
                        :sort-order="queryParams.sortOrder" :sort-field="queryParams.sortField"
                        :lazy="true" @page="onPage($event)" :total-records="totalRecords"
                        :rows-per-page-options="[10,20]" paginator-template="FirstPageLink PrevPageLink
                PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                        current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据">
                <template #empty>
                    <div class="mt-2 mb-2">
                        <span class="emptyInfo">暂无符合条件的结果</span>
                    </div>
                </template>
                <template #header>
                    <div class="grid grid-nogutter">
                        <div class="col-6" style="text-align: left">
                            <p-dropdown v-model="sortKey" :options="sortOptions" option-label="label"
                                        placeholder="按出版时间排序"
                                        @change="onSortChange($event)"></p-dropdown>
                        </div>
                        <!--                        <div class="col-4 index-dataview-title">-->
                        <!--                            <h2><i class="pi iconfont icon-24gl-musicAlbum2"></i>&nbsp&nbsp专&nbsp&nbsp辑&nbsp&nbsp库&nbsp&nbsp</h2>-->
                        <!--                        </div>-->
                        <div class="col-6" style="text-align: right">
                            <p-dataviewlayoutoptions v-model="layout"></p-dataviewlayoutoptions>
                        </div>
                    </div>
                </template>
                <template #grid="slotProps">
                    <div class="index-book-grid-card">
                            <p-card>
                                <template #header>
                                    <a :href="'/db/book/'+ slotProps.data.id" style="width: 180px;display: block">
                                        <div class="book-block-img"
                                                :style="'background-image:url('+ slotProps.data.cover.url+');' ">
                                            <div class="absolute releaseDate">
                                                <span class="text-center" style="color: white">{{slotProps.data.publishDate}}</span>
                                            </div>
                                        </div>
                                    </a>
                                </template>
                                <template #title>
                            <span class="text-truncate-2">
                                <a :href="'/db/merch/'+ slotProps.data.id">{{slotProps.data.title}}</a>
                            </span>
                                </template>
                                <template #subtitle>
                            <span :class="'fi fi-' + slotProps.data.region.code"
                                    v-tooltip.bottom="{value: slotProps.data.region.nameZh, class: 'region-tooltip'}"></span>
                                    &nbsp&nbsp<span class="label" style="font-size: 9px;">{{slotProps.data.publisher}}</span>
                                </template>
                                <template #content>
                                    <div class="grid">
                                        <span class="p-1">
                                            <p-tag class="ml-1" :value="slotProps.data.bookType.nameZh"></p-tag>
                                        </span>
                                        <span class="p-1 has-bonus-tag" v-if="slotProps.data.hasBonus">
                                            <p-tag style="background: #2f364f" class="ml-1" value="特典"></p-tag>
                                        </span>
                                    </div>
                                </template>
                            </p-card>
                        </div>
                </template>
                <template #list="slotProps">
                    <div class="col-12">
                        <div class="index-list-item">
                            <a class="text-center" :href="'/db/book/'+ slotProps.data.id">
                                <img :src="slotProps.data.cover.thumbUrl70" :alt="slotProps.data.cover.name"/>
                            </a>
                            <div class="index-list-item-detail">
                        <span class="index-list-item-name text-truncate-1">
                            <a :href="'/db/book/'+ slotProps.data.id">{{slotProps.data.title}}</a>
                        </span>
                                <span class="small-font" style="margin: 0 0 .5rem 0;">
                            <b class="label">{{slotProps.data.isbn13}}</b><span class="label">&nbsp{{slotProps.data.publishDate}}</span>
                        </span><br>
                                <span :class="'fi fi-' + slotProps.data.region.code" style="margin-left: 0.5rem"
                                        v-tooltip.bottom="{value: slotProps.data.region.nameZh, class: 'region-tooltip'}"></span>
                                &nbsp&nbsp<span class="label" style="font-size: 7px">{{slotProps.data.publisher}}</span><br>
                                <span>
                            <span class="p-1">
                                <p-tag :value="slotProps.data.bookType.nameZh"></p-tag>
                            </span>
                            <span class="has-bonus-tag" v-if="slotProps.data.hasBonus">
                                <p-tag style="background: #001122" class="ml-1" value="特典"></p-tag>
                            </span>
                        </span>
                            </div>
                        </div>
                    </div>
                </template>
            </p-dataview>
        </div>
        <div class="col-2" style="min-width: 300px">
            <p-panel v-if="totalLoading">
                    <template #header>
                        <span class="text-start side-panel-header">
                            <i class="pi pi-list"></i>
                            <span><strong>浏览排名</strong></span>
                        </span>
                    </template>
                    <div class="grid">
                        <span class="small_font">
                            <div class="info_bit_small small_font grid m-0 p-0"
                                 v-if="tmpList10.length != 0"
                                 v-for="(index) of tmpList10">
                                <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                    <div class="col-12 p-0 m-0">
                                        <p-skeleton width="3rem" class="mt-2"></p-skeleton>
                                    </div>
                                    <span class="col-12 p-0 m-0" style="font-size: 9px">
                                        <i class="pi pi-eye mt-1 mb-1"></i><br><p-skeleton width="3rem" class="mb-1"></p-skeleton>
                                    </span>
                                </div>
                                <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                    <p-skeleton size="4rem"></p-skeleton>
                                </div>
                                <div class="col p-0" style="height: 90px">
                                    <ul class="info_bit_small_other">
                                        <li>
                                            <a>
                                                <span class="text-truncate-2 mr-2">
                                                    <p-skeleton width="8rem" class="mb-1 mt-1"></p-skeleton>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-catalog">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-date">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </span>
                    </div>
                </p-panel>
            <p-panel v-if="!totalLoading">
                <template #header>
            <span class="text-start side-panel-header">
                <i class="pi pi-list"></i><span><strong>浏览排名</strong></span>
            </span>
                </template>
                <div class="grid">
            <span class="small_font">
                <div class="info_bit_small small_font grid m-0 p-0"
                        v-if="popularItems.length != 0"
                        v-for="(book, index) of popularItems">
                    <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                        <div class="col-12 p-0 m-0">
                            <i :class="'pi remixicon ri-number-' + index"
                                style="font-size: 2rem"></i>
                        </div>
                        <span class="col-12 p-0 m-0" style="font-size: 9px">
                            <i class="pi pi-eye"></i><br>{{book.visitNum}}
                        </span>
                    </div>
                    <div class="sidebar-panel-image-small-div book_info_bit_thumb mt-2">
                        <a :href="'/db/book/'+ book.id">
                            <img class="sidebar-panel-image-small" :src="book.cover.blackUrl" v-tooltip.bottom="'收录时间: ' + book.addedTime + '编辑时间: ' + book.editedTime">
                        </a>
                    </div>
                    <div class="col p-0" style="height: 90px">
                        <ul class="info_bit_small_other">
                            <li>
                                <a class="small_font"
                                    :href="'/db/book/'+ book.id ">
                                    <span class="text-truncate-2 ml-2 mr-2">
                                        {{book.title}}
                                    </span>
                                </a>
                            </li>
                            <li>
                                <span class="small_font col-6 related-item-catalog">
                                    {{book.isbn13}}
                                </span>
                            </li>
                            <li>
                                <span class="small_font col-6 related-item-date">
                                    {{book.publishDate}}
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>
            </span>
                </div>
            </p-panel>
        </div>
</div>
    `,
    mounted() {
        this.init();
        this.getBooks();
    },
    data() {
        return {
            books: null,
            sortOptions: [
                {label: '按出版时间正序', value: 'publishDate'},
                {label: '按出版时间逆序', value: '!publishDate'},
            ],
            queryParams: {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    title: {value: null},
                    bookType: {value: null},
                    isbn10: {value: null},
                    isbn13: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                    publisher: {value: null},
                    hasBonus: {value: null},
                    region: {value: null},
                    publishLanguage: {value: null},
                }
            },

            justAddedItems: [],
            popularItems: [],

            bookTypeSet: [],
            regionSet: [],
            languageSet: [],
            hasBonusSet,

            itemRows: 20,
            layout: 'grid',
            sortKey: null,
            totalRecords: null,
            productSelect: true,
            productSet: null,
            franchiseSet: [],

            totalLoading: false,
            tmpList10,
            tmpList5,
        }
    },
    watch: {
        layout:function(newValue) {
            if (newValue === "grid") {
                this.itemRows = 20;
            } else {
                this.itemRows = 10;
            }
        },
    },
    methods: {
        init() {
            let json = {
                entityType: ENTITY.BOOK
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_INDEX_INIT_DATA_URL, json)
                .then(res => {
                    this.justAddedItems = res.justAddedItems;
                    this.popularItems = res.popularItems;
                    this.bookTypeSet = res.bookTypeSet;
                    this.regionSet = res.regionSet;
                    this.languageSet = res.languageSet;
                    this.franchiseSet = res.franchiseSet;
                    this.totalLoading = false;
                })
        },
        clearSearch() {
            this.queryParams = {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    title: {value: null},
                    bookType: {value: null},
                    isbn10: {value: null},
                    isbn13: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                    publisher: {value: null},
                    hasBonus: {value: null},
                    region: {value: null},
                    publishLanguage: {value: null},
                }
            };
            this.getBooks();
        },
        getBooks() {
            this.queryParams.rows = this.itemRows;
            let json = {
                pageLabel: "index",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_BOOKS_URL, json)
                .then(res => {
                    this.books = res.data;
                    this.totalRecords = res.total;
                })
        },
        getProducts(data) {
            let json = {
                franchises: data,
                entityType: ENTITY.BOOK
            };
            HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.length !== 0) {
                        this.queryParams.filters.products.value = [];
                        this.productSet = res.data;
                        this.productSelect = false;
                    }else {
                        this.productSelect = true;
                    }
                })
        },
        onPage(event) {
            this.queryParams.first = event.first;
            this.queryParams.rows = event.rows;
            this.getBooks();
        },
        onSortChange(event) {
            console.log(event);
            const value = event.value.value;
            const sortValue = value;

            if (value.indexOf('!') === 0) {
                this.queryParams.sortOrder = -1;
                this.queryParams.sortField = value.substring(1, value.length);
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getBooks();
            } else {
                this.queryParams.sortOrder = 1;
                this.queryParams.sortField = value;
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getBooks();
            }
        },
        regionCode2NameZh,
    },
    components: {
        "p-dataview": primevue.dataview,
        "p-dataviewlayoutoptions": primevue.dataviewlayoutoptions,
        "p-card": primevue.card,
        "p-panel": primevue.panel,
        "p-multiselect": primevue.multiselect,

        "p-chip": primevue.chip,
        "p-inputtext": primevue.inputtext,
        "p-tag": primevue.tag,
        "p-dropdown": primevue.dropdown,
        "p-button": primevue.button,
        "p-divider": primevue.divider,
        "p-skeleton": primevue.skeleton,
    }
};

const discIndex = {
    template: `
        <div class="grid mt-2">
                <div class="col-2" style="min-width: 300px">
                    <p-panel>
                        <template #header>
                            <span class="text-start side-panel-header">
                                <i class="pi pi-filter"></i><span><strong>条件过滤</strong></span>
                            </span>
                        </template>
                        <div class="grid p-fluid">
                            <div class="col-6 p-1">
                                <label>名称</label>
                                <p-inputtext v-model.trim="queryParams.filters.name.value"></p-inputtext>
                            </div>
                            <div class="col-6 p-1">
                                <label>编号</label>
                                <p-inputtext v-model.trim="queryParams.filters.catalogNo.value"></p-inputtext>
                            </div>
                            <div class="col-6 p-1">
                                <label>媒体介质</label>
                                <p-multiselect id="mediaFormat" v-model="queryParams.filters.mediaFormat.value" :options="mediaFormatSet"
                                               option-label="label" option-value="value" placeholder="选择媒体类型"
                                               display="chip">
                                </p-multiselect>
                            </div>
                            <div class="col-6 p-1">
                                <label>区域</label>
                                <p-dropdown v-model="queryParams.filters.region.value" :options="regionSet"
                                            :filter="true" :show-clear="true" option-label="nameZh" option-value="code">
                                    <template #value="slotProps">
                                        <div class="country-item" v-if="slotProps.value">
                                            <span :class="'fi fi-' + slotProps.value"></span>
                                            <div class="ml-2">{{regionCode2NameZh(slotProps.value, regionSet)}}</div>
                                        </div>
                                        <span v-else>选择地区</span>
                                    </template>
                                    <template #option="slotProps">
                                        <div class="country-item">
                                            <span :class="'fi fi-' + slotProps.option.code"></span>
                                            <div class="ml-2">{{slotProps.option.nameZh}}</div>
                                        </div>
                                    </template>
                                </p-dropdown>
                            </div>
                            <div class="col-6 p-1">
                                <label>所属系列</label>
                                <p-multiselect v-model="queryParams.filters.franchises.value" @change="getProducts($event.value)"
                                               :options="franchiseSet" placeholder="所属系列"
                                               option-label="label" option-value="value" display="chip" :filter="true">
                                </p-multiselect>
                            </div>
                            <div class="col-6 p-1">
                                <label>所属作品</label>
                                <p-multiselect v-model="queryParams.filters.products.value" :options="productSet"
                                               option-label="label" option-value="value" placeholder="请先选择所属系列"
                                               display="chip" :filter="true" :disabled="productSelect">
                                </p-multiselect>
                            </div>
                            <div class="col-6 p-1">
                                <label>含特典</label>
                                <p-dropdown v-model="queryParams.filters.hasBonus.value" :options="hasBonusSet"
                                            placeholder="所有" option-label="label"
                                            option-value="value">
                                </p-dropdown>
                            </div>
                            <div class="col-6 p-1">
                                <label>限定版</label>
                                <p-dropdown v-model="queryParams.filters.limited.value" :options="isLimitedSet"
                                            placeholder="所有" option-label="label"
                                            option-value="value">
                                </p-dropdown>
                            </div>
                            <div class="col-4 col-offset-2" style="text-align: right">
                                <p-button icon="pi pi-filter-slash"
                                          class="p-button-rounded p-button-info"
                                          v-tooltip.bottom="{value:'清空', class: 'short-tooltip'}"
                                          @click="clearSearch"></p-button>
                            </div>
                            <div class="col-4" style="text-align: left">
                                <p-button icon="pi pi-filter-fill"
                                          class="p-button-rounded p-button-success"
                                          v-tooltip.bottom="{value:'筛选', class: 'short-tooltip'}"
                                          @click="getDiscs"></p-button>
                            </div>
                        </div>
                    </p-panel>
                    <br>
                    <p-panel v-if="totalLoading">
                        <template #header>
                            <span class="text-start side-panel-header">
                                <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
                            </span>
                        </template>
                        <div class="grid">
                            <span class="small_font">
                                <div class="info_bit_small small_font grid m-0 p-0"
                                     v-if="tmpList5.length != 0"
                                     v-for="(index) of tmpList5">
                                    <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                        <a>
                                            <p-skeleton size="4rem"></p-skeleton>
                                        </a>
                                    </div>
                                    <div class="col p-0" style="height: 80px">
                                        <ul class="info_bit_small_other">
                                            <li>
                                                <a>
                                                    <span class="text-truncate-2 ml-2 mr-2">
                                                        <p-skeleton width="8rem"></p-skeleton>
                                                    </span>
                                                </a>
                                            </li>
                                            <li>
                                                <span class="small_font col-6 related-item-catalog">
                                                    <p-skeleton width="3rem"></p-skeleton>
                                                </span>
                                                <span class="small_font col-6 related-item-date">
                                                    <p-skeleton width="3rem"></p-skeleton>
                                                </span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </span>
                        </div>
                    </p-panel>
                    <p-panel v-if="!totalLoading">
                        <template #header>
                            <span class="text-start side-panel-header">
                                <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
                            </span>
                        </template>
                        <div class="grid">
                            <span class="small_font">
                                <div class="info_bit_small small_font grid m-0 p-0"
                                     v-if="justAddedItems.length != 0"
                                     v-for="disc of justAddedItems">
                                    <div class="sidebar-panel-image-small-div disc_info_bit_thumb mt-2">
                                        <a :href="'/db/disc/'+ disc.id">
                                            <img class="sidebar-panel-image-small" :src="disc.cover.blackUrl"
                                                 v-tooltip.right="'收录时间: ' + disc.addedTime + '编辑时间: ' + disc.editedTime">
                                        </a>
                                    </div>
                                    <div class="col p-0" style="height: 80px">
                                        <ul class="info_bit_small_other">
                                            <li>
                                                <a class="small_font"
                                                   :href="'/db/disc/'+ disc.id ">
                                                    <span class="text-truncate-2 ml-2 mr-2">
                                                        {{disc.name}}
                                                    </span>
                                                </a>
                                            </li>
                                            <li>
                                                <span class="small_font col-6 related-item-catalog">
                                                    {{disc.catalogNo}}
                                                </span>
                                                <span class="small_font col-6 related-item-date">
                                                    {{disc.releaseDate}}
                                                </span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </span>
                        </div>
                    </p-panel>
                </div>
                <div class="col">
                    <p-dataview :value="discs" :layout="layout" :paginator="true" :rows="itemRows"
                                :always-show-paginator="totalRecords != 0"
                                :sort-order="queryParams.sortOrder" :sort-field="queryParams.sortField"
                                :lazy="true" @page="onPage($event)" :total-records="totalRecords"
                                :rows-per-page-options="[10,20]" paginator-template="FirstPageLink PrevPageLink
                        PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                                current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据">
                        <template #empty>
                            <div class="mt-2 mb-2">
                                <span class="emptyInfo">暂无符合条件的结果</span>
                            </div>
                        </template>
                        <template #header>
                            <div class="grid grid-nogutter">
                                <div class="col-6" style="text-align: left">
                                    <p-dropdown v-model="sortKey" :options="sortOptions" option-label="label"
                                                placeholder="按发售时间排序"
                                                @change="onSortChange($event)"></p-dropdown>
                                </div>
                                <!--                        <div class="col-4 index-dataview-title">-->
                                <!--                            <h2><i class="pi iconfont icon-24gl-musicAlbum2"></i>&nbsp&nbsp专&nbsp&nbsp辑&nbsp&nbsp库&nbsp&nbsp</h2>-->
                                <!--                        </div>-->
                                <div class="col-6" style="text-align: right">
                                    <p-dataviewlayoutoptions v-model="layout"></p-dataviewlayoutoptions>
                                </div>
                            </div>
                        </template>
                        <template #grid="slotProps">
                            <div style="width: 197px">
                                <div class="index-item-grid-card card">
                                    <p-card>
                                        <template #header>
                                            <a :href="'/db/disc/'+ slotProps.data.id">
                                                <div class="block-img"
                                                     :style="'background-image:url('+ slotProps.data.cover.url+');' ">
                                                    <p-tag class="catalog ml-1 mt-1">
                                                        {{slotProps.data.catalogNo?slotProps.data.catalogNo:"N/A"}}
                                                    </p-tag>
                                                    <div class="absolute releaseDate">
                                                        <span class="text-center" style="color: white">{{slotProps.data.releaseDate}}</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </template>
                                        <template #title>
                                    <span class="text-truncate-2">
                                        <a :href="'/db/merch/'+ slotProps.data.id">{{slotProps.data.name}}</a>
                                    </span>
                                        </template>
                                        <template #content>
                                            <div class="grid">
                                                <div class="col text-start m-0 p-0">
                                                    <span :class="'fi fi-' + slotProps.data.region.code" style="margin: 0 0.25rem"
                                                          v-tooltip.bottom="{value: slotProps.data.region.nameZh, class: 'region-tooltip'}">
                                                    </span>
                                                    <span v-for="format of slotProps.data.mediaFormat" style="display:inline">
                                                        <p-tag class="mr-1" :value="format.label"></p-tag>
                                                    </span>
                                                </div>
                                                <div class="col text-end m-0 p-0">
                                                    <span class="p-1 has-bonus-tag" v-if="slotProps.data.hasBonus">
                                                        <p-tag style="background: #2f364f" value="特典"></p-tag>
                                                    </span>
                                                    <span class="p-1 limited-tag" v-if="slotProps.data.limited">
                                                        <p-tag style="background: #2f364f" value="限定"></p-tag>
                                                    </span>
                                                </div>
                                            </div>
                                        </template>
                                    </p-card>
                                </div>
                            </div>
                        </template>
                        <template #list="slotProps">
                            <div class="col-12">
                                <div class="index-list-item">
                                    <a class="text-center" :href="'/db/disc/'+ slotProps.data.id">
                                        <img :src="slotProps.data.cover.thumbUrl70" :alt="slotProps.data.cover.name"/>
                                    </a>
                                    <div class="index-list-item-detail">
                                    <span class="index-list-item-name text-truncate-1">
                                        <a :href="'/db/disc/'+ slotProps.data.id">{{slotProps.data.name}}</a>
                                    </span>
                                        <span class="small-font" style="margin: 0 0 .5rem 0;">
                                        <b class="label">{{slotProps.data.catalogNo}}</b><span class="label">&nbsp{{slotProps.data.releaseDate}}</span>
                                    </span><br>
                                        <span :class="'fi fi-' + slotProps.data.region.code" style="margin-left: 0.5rem"
                                              v-tooltip.bottom="{value: slotProps.data.region.nameZh, class: 'region-tooltip'}">
                                    </span>
                                        <span>
                                        <span v-for="format of slotProps.data.mediaFormat" style="display:inline">
                                            <p-tag class="product-tag ml-1" :value="format.label"></p-tag>
                                        </span>
                                        <span class="has-bonus-tag" v-if="slotProps.data.hasBonus">
                                            <p-tag style="background: #001122" class="ml-1" value="特典"></p-tag>
                                        </span>
                                        <span class="limited-tag" v-if="slotProps.data.limited">
                                            <p-tag style="background: #001122" class="ml-1" value="限定"></p-tag>
                                        </span>
                                    </span>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </p-dataview>
                </div>
                <div class="col-2" style="min-width: 300px">
                    <p-panel v-if="totalLoading">
                    <template #header>
                        <span class="text-start side-panel-header">
                            <i class="pi pi-list"></i>
                            <span><strong>浏览排名</strong></span>
                        </span>
                    </template>
                    <div class="grid">
                        <span class="small_font">
                            <div class="info_bit_small small_font grid m-0 p-0"
                                 v-if="tmpList10.length != 0"
                                 v-for="(index) of tmpList10">
                                <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                    <div class="col-12 p-0 m-0">
                                        <p-skeleton width="3rem" class="mt-2"></p-skeleton>
                                    </div>
                                    <span class="col-12 p-0 m-0" style="font-size: 9px">
                                        <i class="pi pi-eye mt-1 mb-1"></i><br><p-skeleton width="3rem" class="mb-1"></p-skeleton>
                                    </span>
                                </div>
                                <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                    <p-skeleton size="4rem"></p-skeleton>
                                </div>
                                <div class="col p-0" style="height: 90px">
                                    <ul class="info_bit_small_other">
                                        <li>
                                            <a>
                                                <span class="text-truncate-2 mr-2">
                                                    <p-skeleton width="8rem" class="mb-1 mt-1"></p-skeleton>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-catalog">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-date">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </span>
                    </div>
                </p-panel>
                    <p-panel v-if="!totalLoading">
                        <template #header>
                            <span class="text-start side-panel-header">
                                <i class="pi iconfont icon-Video-Disc"></i><span><strong>浏览排名</strong></span>
                            </span>
                        </template>
                        <div class="grid">
                            <span class="small_font">
                                <div class="info_bit_small small_font grid m-0 p-0"
                                     v-if="popularItems.length != 0"
                                     v-for="(disc, index) of popularItems">
                                    <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                        <div class="col-12 p-0 m-0">
                                            <i :class="'pi remixicon ri-number-' + index"
                                               style="font-size: 2rem"></i>
                                        </div>
                                        <span class="col-12 p-0 m-0" style="font-size: 9px">
                                            <i class="pi pi-eye"></i><br>{{disc.visitNum}}
                                        </span>
                                    </div>
                                    <div class="sidebar-panel-image-small-div book_info_bit_thumb mt-2">
                                        <a :href="'/db/disc/'+ disc.id">
                                            <img class="sidebar-panel-image-small" :src="disc.cover.blackUrl"
                                                 v-tooltip.bottom="'收录时间: ' + disc.addedTime + '编辑时间: ' + disc.editedTime">
                                        </a>
                                    </div>
                                    <div class="col p-0" style="height: 90px">
                                        <ul class="info_bit_small_other">
                                            <li>
                                                <a class="small_font"
                                                   :href="'/db/disc/'+ disc.id ">
                                                    <span class="text-truncate-2 ml-2 mr-2">
                                                        {{disc.name}}
                                                    </span>
                                                </a>
                                            </li>
                                            <li>
                                                <span class="small_font col-6 related-item-catalog">
                                                    {{disc.catalogNo}}
                                                </span>
                                            </li>
                                            <li>
                                                <span class="small_font col-6 related-item-date">
                                                    {{disc.releaseDate}}
                                                </span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </span>
                        </div>
                    </p-panel>
                </div>
            </div>
    `,
    mounted() {
        this.init();
        this.getDiscs();
    },
    data() {
        return {
            discs: null,
            sortOptions: [
                {label: '按发行时间正序', value: 'releaseDate'},
                {label: '按发行时间逆序', value: '!releaseDate'},
            ],
            queryParams: {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    name: {value: null},
                    catalogNo: {value: null},
                    region: {value: null},
                    mediaFormat: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                    limited: {value: null},
                    hasBonus: {value: null},
                }
            },

            justAddedItems: [],
            popularItems: [],

            mediaFormatSet: [],
            regionSet: [],

            itemRows: 20,
            layout: 'grid',
            sortKey: null,
            totalRecords: null,
            productSelect: true,
            productSet: null,
            hasBonusSet,
            isLimitedSet,
            franchiseSet: [],

            totalLoading: false,
            tmpList10,
            tmpList5,
        }
    },
    watch: {
        layout:function(newValue) {
            if (newValue === "grid") {
                this.itemRows = 20;
            } else {
                this.itemRows = 10;
            }
        },
    },
    methods: {
        init() {
            let json = {
                entityType: ENTITY.DISC
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_INDEX_INIT_DATA_URL, json)
                .then(res => {
                    this.justAddedItems = res.justAddedItems;
                    this.popularItems = res.popularItems;
                    this.mediaFormatSet = res.mediaFormatSet;
                    this.regionSet = res.regionSet;
                    this.franchiseSet = res.franchiseSet;
                    this.totalLoading = false;
                })
        },
        clearSearch() {
            this.queryParams = {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    name: {value: null},
                    catalogNo: {value: null},
                    region: {value: null},
                    mediaFormat: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                    limited: {value: null},
                    hasBonus: {value: null},
                }
            };
            this.getDiscs();
        },
        getDiscs() {
            this.queryParams.rows = this.itemRows;
            let json = {
                pageLabel: "index",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_DISCS_URL, json)
                .then(res => {
                    this.discs = res.data;
                    this.totalRecords = res.total;
                })
        },
        getProducts(data) {
            let json = {
                franchises: data,
                entityType: ENTITY.DISC
            };
            HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.length !== 0) {
                        this.queryParams.filters.products.value = [];
                        this.productSet = res.data;
                        this.productSelect = false;
                    }else {
                        this.productSelect = true;
                    }
                })
        },
        onPage(event) {
            this.queryParams.first = event.first;
            this.queryParams.rows = event.rows;
            this.getDiscs();
        },
        onSortChange(event) {
            const value = event.value.value;
            const sortValue = value;

            if (value.indexOf('!') === 0) {
                this.queryParams.sortOrder = -1;
                this.queryParams.sortField = value.substring(1, value.length);
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getDiscs();
            } else {
                this.queryParams.sortOrder = 1;
                this.queryParams.sortField = value;
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getDiscs();
            }
        },
        regionCode2NameZh,
    },
    components: {
        "p-dataview": primevue.dataview,
        "p-dataviewlayoutoptions": primevue.dataviewlayoutoptions,
        "p-card": primevue.card,
        "p-panel": primevue.panel,
        "p-multiselect": primevue.multiselect,

        "p-chip": primevue.chip,
        "p-inputtext": primevue.inputtext,
        "p-tag": primevue.tag,
        "p-dropdown": primevue.dropdown,
        "p-button": primevue.button,
        "p-divider": primevue.divider,
        "p-skeleton": primevue.skeleton,
    }
};

const gameIndex = {
    template: `
        <div class="grid mt-2">
                <div class="col-2" style="min-width: 300px">
                    <p-panel>
                        <template #header>
                            <span class="text-start side-panel-header">
                                <i class="pi pi-filter"></i><span><strong>条件过滤</strong></span>
                            </span>
                        </template>
                        <div class="grid p-fluid">
                            <div class="col-6 p-1">
                                <label>游戏名</label>
                                <p-inputtext v-model.trim="queryParams.filters.name.value"></p-inputtext>
                            </div>
                            <div class="col-6 p-1">
                                <label>是否包含特典</label>
                                <p-dropdown v-model="queryParams.filters.hasBonus.value" :options="hasBonusSet"
                                            placeholder="所有" option-label="label"
                                            option-value="value">
                                </p-dropdown>
                            </div>
                            <div class="col-6 p-1">
                                <label>所属系列</label>
                                <p-multiselect v-model="queryParams.filters.franchises.value"
                                               @change="getProducts($event.value)"
                                               :options="franchiseSet" placeholder="所属系列"
                                               option-label="label" option-value="value" display="chip" :filter="true">
                                </p-multiselect>
                            </div>
                            <div class="col-6 p-1">
                                <label>所属作品</label>
                                <p-multiselect v-model="queryParams.filters.products.value" :options="productSet"
                                               option-label="label" option-value="value" placeholder="请先选择所属系列"
                                               display="chip" :filter="true" :disabled="productSelect">
                                </p-multiselect>
                            </div>
                            <div class="col-6 p-1">
                                <label>平台</label>
                                <p-dropdown v-model="queryParams.filters.platform.value" :options="gamePlatformSet"
                                            placeholder="所有" option-label="labelEn"
                                            option-value="value">
                                </p-dropdown>
                            </div>
                            <div class="col-6 p-1">
                                <label>区域</label>
                                <p-dropdown v-model="queryParams.filters.region.value" :options="regionSet"
                                            :filter="true" :show-clear="true" option-label="nameZh" option-value="code">
                                    <template #value="slotProps">
                                        <div class="country-item" v-if="slotProps.value">
                                            <span :class="'fi fi-' + slotProps.value"></span>
                                            <div class="ml-2">{{regionCode2NameZh(slotProps.value, regionSet)}}</div>
                                        </div>
                                        <span v-else>选择地区</span>
                                    </template>
                                    <template #option="slotProps">
                                        <div class="country-item">
                                            <span :class="'fi fi-' + slotProps.option.code"></span>
                                            <div class="ml-2">{{slotProps.option.nameZh}}</div>
                                        </div>
                                    </template>
                                </p-dropdown>
                            </div>
                            <div class="col-4 col-offset-2" style="text-align: right">
                                <p-button icon="pi pi-filter-slash"
                                          class="p-button-rounded p-button-info"
                                          v-tooltip.bottom="{value:'清空', class: 'short-tooltip'}"
                                          @click="clearSearch"></p-button>
                            </div>
                            <div class="col-4" style="text-align: left">
                                <p-button icon="pi pi-filter-fill"
                                          class="p-button-rounded p-button-success"
                                          v-tooltip.bottom="{value:'筛选', class: 'short-tooltip'}"
                                          @click="getGames"></p-button>
                            </div>
                        </div>
                    </p-panel>
                    <br>
                    <p-panel v-if="totalLoading">
                    <template #header>
                        <span class="text-start side-panel-header">
                            <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
                        </span>
                    </template>
                    <div class="grid">
                        <span class="small_font">
                            <div class="info_bit_small small_font grid m-0 p-0"
                                 v-if="tmpList5.length != 0"
                                 v-for="(index) of tmpList5">
                                <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                    <a>
                                        <p-skeleton size="4rem"></p-skeleton>
                                    </a>
                                </div>
                                <div class="col p-0" style="height: 80px">
                                    <ul class="info_bit_small_other">
                                        <li>
                                            <a>
                                                <span class="text-truncate-2 ml-2 mr-2">
                                                    <p-skeleton width="8rem"></p-skeleton>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <span class="small_font col-6 related-item-catalog">
                                                <p-skeleton width="3rem"></p-skeleton>
                                            </span>
                                            <span class="small_font col-6 related-item-date">
                                                <p-skeleton width="3rem"></p-skeleton>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </span>
                    </div>
                </p-panel>
                    <p-panel v-if="!totalLoading">
                        <template #header>
                            <span class="text-start side-panel-header">
                                <i class="pi iconfont icon-youxi"></i><span><strong>最新收录</strong></span>
                            </span>
                        </template>
                        <div class="grid">
                            <span class="small_font">
                                <div class="info_bit_small small_font grid m-0 p-0"
                                     v-if="justAddedItems.length != 0"
                                     v-for="game of justAddedItems">
                                    <div class="sidebar-panel-image-small-div game_info_bit_thumb mt-2">
                                        <a :href="'/db/game/'+ game.id">
                                            <img class="sidebar-panel-image-small" :src="game.cover.blackUrl"
                                                 v-tooltip.right="'收录时间: ' + game.addedTime + '编辑时间: ' + game.editedTime">
                                        </a>
                                    </div>
                                    <div class="col p-0" style="height: 80px">
                                        <ul class="info_bit_small_other">
                                            <li>
                                                <a class="small_font"
                                                   :href="'/db/game/'+ game.id ">
                                                    <span class="text-truncate-2 ml-2 mr-2">
                                                        {{game.name}}
                                                    </span>
                                                </a>
                                            </li>
                                            <li>
                                                <span class="small_font col-6 related-item-catalog">
                                                    {{game.releaseDate}}
                                                </span>
                                                <span class="small_font col-6 related-item-date">
                                                    <b>{{game.platform.nameEn}}</b> <span :class="'fi fi-' + game.region.code"></span>
                                                </span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </span>
                        </div>
                    </p-panel>
                </div>
                <div class="col">
                    <p-dataview :value="games" :layout="layout" :paginator="true" :rows="itemRows"
                                :always-show-paginator="totalRecords != 0"
                                :sort-order="queryParams.sortOrder" :sort-field="queryParams.sortField"
                                :lazy="true" @page="onPage($event)" :total-records="totalRecords"
                                :rows-per-page-options="[10,20]" paginator-template="FirstPageLink PrevPageLink
                        PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                                current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据">
                        <template #empty>
                            <div class="mt-2 mb-2">
                                <span class="emptyInfo">暂无符合条件的结果</span>
                            </div>
                        </template>
                        <template #header>
                            <div class="grid grid-nogutter">
                                <div class="col-6" style="text-align: left">
                                    <p-dropdown v-model="sortKey" :options="sortOptions" option-label="label"
                                                placeholder="按发售时间排序"
                                                @change="onSortChange($event)"></p-dropdown>
                                </div>
                                <!--                        <div class="col-4 index-dataview-title">-->
                                <!--                            <h2><i class="pi iconfont icon-24gl-musicAlbum2"></i>&nbsp&nbsp专&nbsp&nbsp辑&nbsp&nbsp库&nbsp&nbsp</h2>-->
                                <!--                        </div>-->
                                <div class="col-6" style="text-align: right">
                                    <p-dataviewlayoutoptions v-model="layout"></p-dataviewlayoutoptions>
                                </div>
                            </div>
                        </template>
                        <template #grid="slotProps">
                            <div style="width: 197px">
                                <div class="index-item-grid-card card">
                                    <p-card>
                                        <template #header>
                                            <a :href="'/db/game/'+ slotProps.data.id">
                                                <div class="block-img"
                                                     :style="'background-image:url('+ slotProps.data.cover.url+');' ">
                                                    <div class="absolute releaseDate">
                                                        <span class="text-center" style="color: white">{{slotProps.data.releaseDate}}</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </template>
                                        <template #title>
                                    <span class="text-truncate-2">
                                        <a :href="'/db/merch/'+ slotProps.data.id">{{slotProps.data.name}}</a>
                                    </span>
                                        </template>
                                        <template #content>
                                            <div class="grid">
                                                <div class="col text-start m-0 p-0">
                                                    <span :class="'fi fi-' + slotProps.data.region.code" style="margin: 0 0.25rem"
                                                          v-tooltip.bottom="{value: slotProps.data.region.nameZh, class: 'region-tooltip'}">
                                                    </span>
                                                    <span><p-tag class="ml-1" :value="slotProps.data.platform.nameEn"></p-tag></span>
                                                </div>
                                                <div class="col text-end m-0 p-0">
                                                    <span class="p-1 has-bonus-tag" v-if="slotProps.data.hasBonus">
                                                        <p-tag style="background: #2f364f" value="特典"></p-tag>
                                                    </span>
                                                </div>
                                            </div>
                                        </template>
                                    </p-card>
                                </div>
                            </div>
                        </template>
                        <template #list="slotProps">
                            <div class="col-12">
                                <div class="index-list-item">
                                    <a class="text-center" :href="'/db/game/'+ slotProps.data.id">
                                        <img :src="slotProps.data.cover.thumbUrl70" :alt="slotProps.data.cover.name"/>
                                    </a>
                                    <div class="index-list-item-detail">
                                    <span class="index-list-item-name text-truncate-1">
                                        <a :href="'/db/game/'+ slotProps.data.id">{{slotProps.data.name}}</a>
                                    </span>
                                        <span class="small-font" style="margin: 0 0 .5rem 0;">
                                        <span class="label">&nbsp{{slotProps.data.releaseDate}}</span>
                                    </span><br>
                                        <span :class="'fi fi-' + slotProps.data.region.code" style="margin-left: 0.5rem"
                                              v-tooltip.bottom="{value: slotProps.data.region.nameZh, class: 'region-tooltip'}">
                                    </span>
                                        <span>
                                        <span>
                                            <p-tag class="ml-1" :value="slotProps.data.platform.nameEn"></p-tag>
                                        </span>
                                        <span class="has-bonus-tag" v-if="slotProps.data.hasBonus">
                                            <p-tag style="background: #001122" class="ml-1" value="特典"></p-tag>
                                        </span>
                                    </span>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </p-dataview>
                </div>
                <div class="col-2" style="min-width: 300px">
                    <p-panel v-if="totalLoading">
                    <template #header>
                        <span class="text-start side-panel-header">
                            <i class="pi pi-list"></i>
                            <span><strong>浏览排名</strong></span>
                        </span>
                    </template>
                    <div class="grid">
                        <span class="small_font">
                            <div class="info_bit_small small_font grid m-0 p-0"
                                 v-if="tmpList10.length != 0"
                                 v-for="(index) of tmpList10">
                                <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                    <div class="col-12 p-0 m-0">
                                        <p-skeleton width="3rem" class="mt-2"></p-skeleton>
                                    </div>
                                    <span class="col-12 p-0 m-0" style="font-size: 9px">
                                        <i class="pi pi-eye mt-1 mb-1"></i><br><p-skeleton width="3rem" class="mb-1"></p-skeleton>
                                    </span>
                                </div>
                                <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                    <p-skeleton size="4rem"></p-skeleton>
                                </div>
                                <div class="col p-0" style="height: 90px">
                                    <ul class="info_bit_small_other">
                                        <li>
                                            <a>
                                                <span class="text-truncate-2 mr-2">
                                                    <p-skeleton width="8rem" class="mb-1 mt-1"></p-skeleton>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-catalog">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-date">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </span>
                    </div>
                </p-panel>
                    <p-panel v-if="!totalLoading">
                        <template #header>
                            <span class="text-start side-panel-header">
                                <i class="pi iconfont icon-youxi"></i><span><strong>浏览排名</strong></span>
                            </span>
                        </template>
                        <div class="grid">
                            <span class="small_font">
                                <div class="info_bit_small small_font grid m-0 p-0"
                                     v-if="popularItems.length != 0"
                                     v-for="(game, index) of popularItems">
                                    <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                        <div class="col-12 p-0 m-0">
                                            <i :class="'pi remixicon ri-number-' + index"
                                               style="font-size: 2rem"></i>
                                        </div>
                                        <span class="col-12 p-0 m-0" style="font-size: 9px">
                                            <i class="pi pi-eye"></i><br>{{game.visitNum}}
                                        </span>
                                    </div>
                                    <div class="sidebar-panel-image-small-div game_info_bit_thumb mt-2">
                                        <a :href="'/db/game/'+ game.id">
                                            <img class="sidebar-panel-image-small" :src="game.cover.blackUrl"
                                                 v-tooltip.bottom="'收录时间: ' + game.addedTime + '编辑时间: ' + game.editedTime">
                                        </a>
                                    </div>
                                    <div class="col p-0" style="height: 90px">
                                        <ul class="info_bit_small_other">
                                            <li>
                                                <a class="small_font"
                                                   :href="'/db/game/'+ game.id ">
                                                    <span class="text-truncate-2 ml-2 mr-2">
                                                        {{game.name}}
                                                    </span>
                                                </a>
                                            </li>
                                            <li>
                                                <span class="small_font col-6 related-item-catalog">
                                                    <b>{{game.releaseDate}}</b><br>
                                                </span>
                                            </li>
                                            <li>
                                                <span class="small_font col-6 related-item-date">
                                                    <span :class="'fi fi-' + game.region.code"></span>&nbsp{{game.platform.nameEn}}
                                                </span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </span>
                        </div>
                    </p-panel>
                </div>
            </div>
    `,
    mounted() {
        this.init();
        this.getGames();
    },
    data() {
        return {
            games: null,
            sortOptions: [
                {label: '按发售时间正序', value: 'releaseDate'},
                {label: '按发售时间逆序', value: '!releaseDate'},
            ],
            queryParams: {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    name: {value: null},
                    hasBonus: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                    platform: {value: null},
                    region: {value: null},
                }
            },

            justAddedItems: [],
            popularItems: [],

            gamePlatformSet: [],
            regionSet: [],

            itemRows: 20,
            layout: 'grid',
            sortKey: null,
            totalRecords: null,
            productSelect: true,
            productSet: null,
            hasBonusSet,
            franchiseSet: [],

            totalLoading: false,
            tmpList10,
            tmpList5,
        }
    },
    watch: {
        layout:function(newValue) {
            if (newValue === "grid") {
                this.itemRows = 20;
            } else {
                this.itemRows = 10;
            }
        },
    },
    methods: {
        init() {
            let json = {
                entityType: ENTITY.GAME
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_INDEX_INIT_DATA_URL, json)
                .then(res => {
                    this.justAddedItems = res.justAddedItems;
                    this.popularItems = res.popularItems;
                    this.gamePlatformSet = res.gamePlatformSet;
                    this.regionSet = res.regionSet;
                    this.totalLoading = false;
                })
        },
        clearSearch() {
            this.queryParams = {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    name: {value: null},
                    hasBonus: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                    platform: {value: null},
                    region: {value: null},
                }
            };
            this.getDiscs();
        },
        getGames() {
            this.queryParams.rows = this.itemRows;
            let json = {
                pageLabel: "index",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_GAMES_URL, json)
                .then(res => {
                    this.games = res.data;
                    this.totalRecords = res.total;
                })
        },
        getProducts(data) {
            let json = {
                franchises: data,
                entityType: ENTITY.GAME
            };
            HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.length !== 0) {
                        this.queryParams.filters.products.value = [];
                        this.productSet = res.data;
                        this.productSelect = false;
                    }else {
                        this.productSelect = true;
                    }
                })
        },
        onPage(event) {
            this.queryParams.first = event.first;
            this.queryParams.rows = event.rows;
            this.getGames();
        },
        onSortChange(event) {
            const value = event.value.value;
            const sortValue = value;

            if (value.indexOf('!') === 0) {
                this.queryParams.sortOrder = -1;
                this.queryParams.sortField = value.substring(1, value.length);
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getGames();
            } else {
                this.queryParams.sortOrder = 1;
                this.queryParams.sortField = value;
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getGames();
            }
        },
        regionCode2NameZh,
    },
    components: {
        "p-dataview": primevue.dataview,
        "p-dataviewlayoutoptions": primevue.dataviewlayoutoptions,
        "p-card": primevue.card,
        "p-panel": primevue.panel,
        "p-multiselect": primevue.multiselect,

        "p-chip": primevue.chip,
        "p-inputtext": primevue.inputtext,
        "p-tag": primevue.tag,
        "p-dropdown": primevue.dropdown,
        "p-button": primevue.button,
        "p-divider": primevue.divider,
        "p-skeleton": primevue.skeleton,
    }
};

const merchIndex = {
    template: `
        <div class="grid mt-2">
                <div class="col-2" style="min-width: 300px">
                    <p-panel>
                        <template #header>
                    <span class="text-start side-panel-header">
                        <i class="pi pi-filter"></i><span><strong>条件过滤</strong></span>
                    </span>
                        </template>
                        <div class="grid p-fluid">
                            <div class="col-6 p-1">
                                <label>商品名</label>
                                <p-inputtext v-model.trim="queryParams.filters.name.value"></p-inputtext>
                            </div>
                            <div class="col-6 p-1">
                                <label>区域</label>
                                <p-dropdown v-model="queryParams.filters.region.value" :options="regionSet"
                                            :filter="true" :show-clear="true" option-label="nameZh" option-value="code">
                                    <template #value="slotProps">
                                        <div class="country-item" v-if="slotProps.value">
                                            <span :class="'fi fi-' + slotProps.value"></span>
                                            <div class="ml-2">{{regionCode2NameZh(slotProps.value, regionSet)}}</div>
                                        </div>
                                        <span v-else>选择地区</span>
                                    </template>
                                    <template #option="slotProps">
                                        <div class="country-item">
                                            <span :class="'fi fi-' + slotProps.option.code"></span>
                                            <div class="ml-2">{{slotProps.option.nameZh}}</div>
                                        </div>
                                    </template>
                                </p-dropdown>
                            </div>
                            <div class="col-6 p-1">
                                <label>商品类型</label>
                                <p-dropdown v-model="queryParams.filters.category.value" :options="merchCategorySet"
                                            placeholder="所有" option-label="label"
                                            option-value="value">
                                </p-dropdown>
                            </div>
                            <div class="col-6 p-1">
                                <label class="mr-2">是否非卖品</label>
                                <p-dropdown v-model="queryParams.filters.notForSale.value" :options="isNotForSaleSet"
                                            placeholder="所有" option-label="label" option-value="value">
                                </p-dropdown>
                            </div>
                            <div class="col-6 p-1">
                                <label>所属系列</label>
                                <p-multiselect v-model="queryParams.filters.franchises.value"
                                               @change="getProducts($event.value)"
                                               :options="franchiseSet" placeholder="所属系列"
                                               option-label="label" option-value="value" display="chip" :filter="true">
                                </p-multiselect>
                            </div>
                            <div class="col-6 p-1">
                                <label>所属作品</label>
                                <p-multiselect v-model="queryParams.filters.products.value" :options="productSet"
                                               option-label="label" option-value="value" placeholder="请先选择所属系列"
                                               display="chip" :filter="true" :disabled="productSelect">
                                </p-multiselect>
                            </div>
                            <div class="col-4 col-offset-2" style="text-align: right">
                                <p-button icon="pi pi-filter-slash"
                                          class="p-button-rounded p-button-info"
                                          v-tooltip.bottom="{value:'清空', class: 'short-tooltip'}"
                                          @click="clearSearch"></p-button>
                            </div>
                            <div class="col-4" style="text-align: left">
                                <p-button icon="pi pi-filter-fill"
                                          class="p-button-rounded p-button-success"
                                          v-tooltip.bottom="{value:'筛选', class: 'short-tooltip'}"
                                          @click="getMerchs"></p-button>
                            </div>
                        </div>
                    </p-panel>
                    <br>
                    <p-panel v-if="totalLoading">
                    <template #header>
                        <span class="text-start side-panel-header">
                            <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
                        </span>
                    </template>
                    <div class="grid">
                        <span class="small_font">
                            <div class="info_bit_small small_font grid m-0 p-0"
                                 v-if="tmpList5.length != 0"
                                 v-for="(index) of tmpList5">
                                <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                    <a>
                                        <p-skeleton size="4rem"></p-skeleton>
                                    </a>
                                </div>
                                <div class="col p-0" style="height: 80px">
                                    <ul class="info_bit_small_other">
                                        <li>
                                            <a>
                                                <span class="text-truncate-2 ml-2 mr-2">
                                                    <p-skeleton width="8rem"></p-skeleton>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <span class="small_font col-6 related-item-catalog">
                                                <p-skeleton width="3rem"></p-skeleton>
                                            </span>
                                            <span class="small_font col-6 related-item-date">
                                                <p-skeleton width="3rem"></p-skeleton>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </span>
                    </div>
                </p-panel>
                    <p-panel v-if="!totalLoading">
                        <template #header>
                    <span class="text-start side-panel-header">
                        <i class="pi pi-list"></i><span><strong>最新收录</strong></span>
                    </span>
                        </template>
                        <div class="grid">
                    <span class="small_font">
                        <div class="info_bit_small small_font grid m-0 p-0"
                             v-if="justAddedItems.length != 0"
                             v-for="merch of justAddedItems">
                            <div class="sidebar-panel-image-small-div disc_info_bit_thumb mt-2">
                                <a :href="'/db/merch/'+ merch.id">
                                    <img class="sidebar-panel-image-small" :src="merch.cover.blackUrl"
                                         v-tooltip.right="'收录时间: ' + merch.addedTime + '编辑时间: ' + merch.editedTime">
                                </a>
                            </div>
                            <div class="col p-0" style="height: 80px">
                                <ul class="info_bit_small_other">
                                    <li>
                                        <a class="small_font"
                                           :href="'/db/merch/'+ merch.id ">
                                            <span class="text-truncate-2 mr-2">
                                                {{merch.name}}
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <span class="small-font">
                                            {{merch.releaseDate}}
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </span>
                        </div>
                    </p-panel>
                </div>
                <div class="col">
                    <p-dataview :value="merchs" :layout="layout" :paginator="true" :rows="itemRows"
                                :always-show-paginator="totalRecords != 0"
                                :sort-order="queryParams.sortOrder" :sort-field="queryParams.sortField"
                                :lazy="true" @page="onPage($event)" :total-records="totalRecords"
                                :rows-per-page-options="[10,20]" paginator-template="FirstPageLink PrevPageLink
                        PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                                current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据">
                        <template #empty>
                            <div class="mt-2 mb-2">
                                <span class="emptyInfo">暂无符合条件的结果</span>
                            </div>
                        </template>
                        <template #header>
                            <div class="grid grid-nogutter">
                                <div class="col-6" style="text-align: left">
                                    <p-dropdown v-model="sortKey" :options="sortOptions" option-label="label"
                                                placeholder="按发售时间排序"
                                                @change="onSortChange($event)"></p-dropdown>
                                </div>
                                <!--                        <div class="col-4 index-dataview-title">-->
                                <!--                            <h2><i class="pi iconfont icon-24gl-musicAlbum2"></i>&nbsp&nbsp专&nbsp&nbsp辑&nbsp&nbsp库&nbsp&nbsp</h2>-->
                                <!--                        </div>-->
                                <div class="col-6" style="text-align: right">
                                    <p-dataviewlayoutoptions v-model="layout"></p-dataviewlayoutoptions>
                                </div>
                            </div>
                        </template>
                        <template #grid="slotProps">
                            <div style="width: 197px">
                                <div class="index-item-grid-card card">
                                    <p-card>
                                        <template #header>
                                            <a :href="'/db/merch/'+ slotProps.data.id">
                                                <div class="block-img"
                                                     :style="'background-image:url('+ slotProps.data.cover.url+');' ">
                                                    <div class="absolute releaseDate">
                                                        <span class="text-center" style="color: white">{{slotProps.data.releaseDate}}</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </template>
                                        <template #title>
                                    <span class="text-truncate-2">
                                        <a :href="'/db/merch/'+ slotProps.data.id">{{slotProps.data.name}}</a>
                                    </span>
                                        </template>
                                    </p-card>
                                </div>
                            </div>
                        </template>
                        <template #list="slotProps">
                            <div class="col-12">
                                <div class="index-list-item">
                                    <a class="text-center" :href="'/db/merch/'+ slotProps.data.id">
                                        <img :src="slotProps.data.cover.thumbUrl70" :alt="slotProps.data.cover.name"/>
                                    </a>
                                    <div class="index-list-item-detail">
                                <span class="index-list-item-name text-truncate-1">
                                    <a :href="'/db/merch/'+ slotProps.data.id">{{slotProps.data.name}}</a>
                                </span>
                                        <span class="small-font">
                                    <span class="label">{{slotProps.data.releaseDate}}</span>
                                </span>
                                        <span :class="'fi fi-' + slotProps.data.region.code" style="margin-left: 0.5rem"
                                              v-tooltip.bottom="{value: slotProps.data.region.nameZh, class: 'region-tooltip'}">
                                </span>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </p-dataview>
                </div>
                <div class="col-2" style="min-width: 300px">
                    <p-panel v-if="totalLoading">
                    <template #header>
                        <span class="text-start side-panel-header">
                            <i class="pi pi-list"></i>
                            <span><strong>浏览排名</strong></span>
                        </span>
                    </template>
                    <div class="grid">
                        <span class="small_font">
                            <div class="info_bit_small small_font grid m-0 p-0"
                                 v-if="tmpList10.length != 0"
                                 v-for="(index) of tmpList10">
                                <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                    <div class="col-12 p-0 m-0">
                                        <p-skeleton width="3rem" class="mt-2"></p-skeleton>
                                    </div>
                                    <span class="col-12 p-0 m-0" style="font-size: 9px">
                                        <i class="pi pi-eye mt-1 mb-1"></i><br><p-skeleton width="3rem" class="mb-1"></p-skeleton>
                                    </span>
                                </div>
                                <div class="sidebar-panel-image-small-div album_info_bit_thumb mt-2">
                                    <p-skeleton size="4rem"></p-skeleton>
                                </div>
                                <div class="col p-0" style="height: 90px">
                                    <ul class="info_bit_small_other">
                                        <li>
                                            <a>
                                                <span class="text-truncate-2 mr-2">
                                                    <p-skeleton width="8rem" class="mb-1 mt-1"></p-skeleton>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-catalog">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                        <li>
                                            <span class="small_font related-item-date">
                                                <p-skeleton width="6rem" class="mb-1"></p-skeleton>
                                            </span>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </span>
                    </div>
                </p-panel>
                    <p-panel v-if="!totalLoading">
                        <template #header>
                    <span class="text-start side-panel-header">
                        <i class="pi iconfont icon-yinshuabaozhuang"></i><span><strong>浏览排名</strong></span>
                    </span>
                        </template>
                        <div class="grid">
                    <span class="small_font">
                        <div class="info_bit_small small_font grid m-0 p-0"
                             v-if="popularItems.length != 0"
                             v-for="(merch, index) of popularItems">
                            <div class="col-2 p-0 mt-2 mb-0 ml-0 mr-0 text-center">
                                <div class="col-12 p-0 m-0">
                                    <i :class="'pi remixicon ri-number-' + index"
                                       style="font-size: 2rem"></i>
                                </div>
                                <span class="col-12 p-0 m-0" style="font-size: 9px">
                                    <i class="pi pi-eye"></i><br>{{merch.visitNum}}
                                </span>
                            </div>
                            <div class="sidebar-panel-image-small-div merch_info_bit_thumb mt-2">
                                <a :href="'/db/merch/'+ merch.id">
                                    <img class="sidebar-panel-image-small" :src="merch.cover.blackUrl"
                                         v-tooltip.bottom="'收录时间: ' + merch.addedTime + '编辑时间: ' + merch.editedTime">
                                </a>
                            </div>
                            <div class="col p-0" style="height: 90px">
                                <ul class="info_bit_small_other">
                                    <li>
                                        <a class="small_font"
                                           :href="'/db/merch/'+ merch.id ">
                                            <span class="text-truncate-2 ml-2 mr-2">
                                                {{merch.name}}
                                            </span>
                                        </a>
                                    </li>
                                    <li>
                                        <span class="small_font col-6 related-item-date">
                                            {{merch.releaseDate}}
                                        </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </span>
                        </div>
                    </p-panel>
                </div>
            </div>
    `,
    mounted() {
        this.init();
        this.getMerchs();
    },
    data() {
        return {
            merchs: null,
            sortOptions: [
                {label: '按发售时间正序', value: 'releaseDate'},
                {label: '按发售时间逆序', value: '!releaseDate'},
            ],
            queryParams: {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    name: {value: null},
                    barcode: {value: null},
                    region: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                    category: {value: null},
                    notForSale: {value: null},
                }
            },

            justAddedItems: [],
            popularItems: [],

            merchCategorySet: [],
            regionSet: [],

            itemRows: 20,
            layout: 'grid',
            sortKey: null,
            totalRecords: null,
            productSelect: true,
            productSet: null,
            hasBonusSet,
            isNotForSaleSet,
            franchiseSet: [],

            totalLoading: false,
            tmpList10,
            tmpList5,
        }
    },
    watch: {
        layout:function(newValue) {
            if (newValue === "grid") {
                this.itemRows = 20;
            } else {
                this.itemRows = 10;
            }
        },
    },
    methods: {
        init() {
            let json = {
                entityType: ENTITY.MERCH
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_INDEX_INIT_DATA_URL, json)
                .then(res => {
                    this.justAddedItems = res.justAddedItems;
                    this.popularItems = res.popularItems;
                    this.merchCategorySet = res.merchCategorySet;
                    this.regionSet = res.regionSet;
                    this.totalLoading = false;
                })
        },
        clearSearch() {
            this.queryParams = {
                first: 0,
                rows: 0,
                sortField: null,
                sortOrder: 0,
                filters: {
                    name: {value: null},
                    barcode: {value: null},
                    region: {value: null},
                    franchises: {value: null},
                    products: {value: null},
                    category: {value: null},
                    notForSale: {value: null},
                }
            };
            this.getDiscs();
        },
        getMerchs() {
            this.queryParams.rows = this.itemRows;
            let json = {
                pageLabel: "index",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_MERCHS_URL, json)
                .then(res => {
                    this.merchs = res.data;
                    this.totalRecords = res.total;
                })
        },
        getProducts(data) {
            let json = {
                franchises: data,
                entityType: ENTITY.MERCH
            };
            HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.length !== 0) {
                        this.queryParams.filters.products.value = [];
                        this.productSet = res.data;
                        this.productSelect = false;
                    }else {
                        this.productSelect = true;
                    }
                })
        },
        onPage(event) {
            this.queryParams.first = event.first;
            this.queryParams.rows = event.rows;
            this.getMerchs();
        },
        onSortChange(event) {
            const value = event.value.value;
            const sortValue = value;

            if (value.indexOf('!') === 0) {
                this.queryParams.sortOrder = -1;
                this.queryParams.sortField = value.substring(1, value.length);
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getMerchs();
            } else {
                this.queryParams.sortOrder = 1;
                this.queryParams.sortField = value;
                this.queryParams.first = 0;
                this.queryParams.rows = this.itemRows;
                this.sortKey = sortValue;
                this.getMerchs();
            }
        },
        regionCode2NameZh,
    },
    components: {
        "p-dataview": primevue.dataview,
        "p-dataviewlayoutoptions": primevue.dataviewlayoutoptions,
        "p-card": primevue.card,
        "p-panel": primevue.panel,
        "p-multiselect": primevue.multiselect,

        "p-chip": primevue.chip,
        "p-inputtext": primevue.inputtext,
        "p-tag": primevue.tag,
        "p-dropdown": primevue.dropdown,
        "p-button": primevue.button,
        "p-divider": primevue.divider,
        "p-skeleton": primevue.skeleton,
    }
};

export const DATABASE_INDEX_ROUTER = [
    {
        path: '/db/albums',
        component: albumIndex
    },
    {
        path: '/db/books',
        component: bookIndex
    },
    {
        path: '/db/discs',
        component: discIndex
    },
    {
        path: '/db/games',
        component: gameIndex
    },
    {
        path: '/db/merchs',
        component: merchIndex
    }
];