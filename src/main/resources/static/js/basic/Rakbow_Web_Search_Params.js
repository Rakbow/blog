import {HttpUtil} from '/js/basic/Http_Util.js';
import superplaceholder from '/tool/superplaceholder/superplaceholder.esm.js';

export const showSearchPanel = (dialog) => {
    const dialogRef = dialog.open(SearchPanel, {
        props: {
            header: '全站搜索',
            style: {
                width: '43vw',
            },
            class: 'search-dialog',
            // breakpoints:{
            //     '960px': '75vw',
            //     '640px': '90vw'
            // },
            modal: true
        },
        templates: {
            // header: () => {
            //     return [
            //         Vue.h('span', {class: 'search-dialog'}, '搜索面板')
            //     ]
            // }
        },
    });
};

const SearchPanel = {
    template: `
        <div class="formgrid grid search">
    <div class="col">
        <p-inputtext id="globalSearch" v-model="searchParams.keyword" @keypress="search" class="search-input"></p-inputtext>
    </div>
    <div class="col-2" style="margin: auto;">
        <p-dropdown v-model="searchParams.entityType" :options="entityType" option-label="label"
            option-value="value" placeholder="类型">
            <template #value="slotProps">
                <div class="country-item country-item-value" v-if="slotProps.value">
                    <i :class="entityTypeValue2Icon(slotProps.value)"></i>
                    <span class="ml-1">{{entityTypevalueToLabel(slotProps.value)}}</span>
                </div>
            </template>
            <template #option="slotProps">
                <div class="country-item">
                    <i :class="slotProps.option.icon"></i>
                    <span class="ml-1">{{slotProps.option.label}}</span>
                </div>
            </template>
        </p-dropdown>
    </div>                       
</div>
<div v-if="searchResult != null">
    <div v-if="searchResult.total != 0" style="height: 600px">
        <div class="text-start mt-3">
            <span>共{{searchResult.total}}条结果</span>&nbsp;&nbsp;&nbsp;
            <span>查询类型: {{searchResult.entityName}}</span>&nbsp;&nbsp;&nbsp;
            <span>关键词: {{searchResult.keyword}}</span>&nbsp;&nbsp;&nbsp;
            <span>查询时间: {{searchResult.searchTime}}</span>&nbsp;&nbsp;&nbsp;
        </div>
        <p-scrollpanel style="max-height: 500px">
            <div v-if="searchResult.entityType == 1" v-for="result of searchResult.data">
                <div class="col-12">
                    <div class="search-item">
                        <a class="text-center" :href="'/db/album/'+ result.id">
                            <img :src="result.cover"/>
                        </a>
                        <div class="search-item-detail">
                                <span class="search-item-name text-truncate-1">
                                    <a :href="'/db/album/'+ result.id">{{result.name}}</a>
                                </span>
                            <span class="small-font" style="margin: 0 0 .5rem 0;">
                                    <b class="label">{{result.catalogNo?result.catalogNo:'N/A'}}</b><span
                                    class="label">&nbsp{{result.releaseDate}}</span>
                                </span><br/>
                            <span class="grid">
                                <span class="text-start col-6">
                                    <div v-for="format of result.albumFormat" style="display:inline">
                                        <p-tag class="product-tag ml-1" :value="format.label"></p-tag>
                                    </div>
                                    <span class="has-bonus-tag" v-if="result.hasBonus">
                                        <p-tag style="background: #1B273D" class="ml-1" value="特典"></p-tag>
                                    </span>
                                </span>
                                <span class="text-end col-5">
                                    <i class="pi pi-eye" v-tooltip.bottom="{value:'浏览', class: 'short-tooltip'}"></i>
                                    <span class="ml-2 mr-4">{{result.visitCount}}</span>
                                    <i class="pi pi-thumbs-up-fill" v-tooltip.bottom="{value:'点赞', class: 'short-tooltip'}"></i>
                                    <span class="ml-2 mr-2">{{result.likeCount}}</span>
                                </span>
                            </span>
                        </div>
                    </div>
                </div>
                <p-divider></p-divider>
            </div>
            <div v-if="searchResult.entityType == 2" v-for="result of searchResult.data">
                <div class="col-12">
                    <div class="search-item">
                        <a class="text-center" :href="'/db/disc/'+ result.id">
                            <img :src="result.cover" />
                        </a>
                        <div class="search-item-detail">
                        <span class="search-item-name text-truncate-1">
                            <a :href="'/db/disc/'+ result.id">{{result.name}}</a>
                        </span>
                            <span class="small-font" style="margin: 0 0 .5rem 0;">
                            <b class="label">{{result.catalogNo?result.catalogNo:'N/A'}}</b><span class="label">&nbsp{{result.releaseDate}}</span>
                        </span><br>
                        <span :class="'fi fi-' + result.region.code" style="margin-left: 0.5rem"
                                v-tooltip.bottom="{value: result.region.nameZh, class: 'region-tooltip'}">
                        </span>
                        <span class="grid">
                            <span class="text-start col-6">
                                <span v-for="format of result.mediaFormat" style="display:inline">
                                    <p-tag class="product-tag ml-1" :value="format.label"></p-tag>
                                </span>
                                <span class="has-bonus-tag" v-if="result.hasBonus">
                                    <p-tag style="background: #1B273D" class="ml-1" value="特典"></p-tag>
                                </span>
                                <span class="limited-tag" v-if="result.limited">
                                    <p-tag style="background: #1B273D" class="ml-1" value="限定"></p-tag>
                                </span>
                            </span>
                            <span class="text-end col-5">
                                <i class="pi pi-eye" v-tooltip.bottom="{value:'浏览', class: 'short-tooltip'}"></i>
                                <span class="ml-2 mr-4">{{result.visitCount}}</span>
                                <i class="pi pi-thumbs-up-fill" v-tooltip.bottom="{value:'点赞', class: 'short-tooltip'}"></i>
                                <span class="ml-2 mr-2">{{result.likeCount}}</span>
                            </span>
                        </span>
                        </div>
                    </div>
                </div>
                <p-divider></p-divider>
            </div>
            <div v-if="searchResult.entityType == 3" v-for="result of searchResult.data">
                <div class="col-12">
                    <div class="search-item">
                        <a class="text-center" :href="'/db/book/'+ result.id">
                            <img :src="result.cover"/>
                        </a>
                        <div class="search-item-detail">
                                <span class="search-item-name text-truncate-1">
                                    <a :href="'/db/book/'+ result.id">{{result.title}}</a>
                                </span>
                            <span class="small-font" style="margin: 0 0 .5rem 0;">
                                    <b class="label">{{result.isbn13}}</b><span class="label">&nbsp{{result.publishDate}}</span>
                                </span><br>
                            <span :class="'fi fi-' + result.region.code" style="margin-left: 0.5rem"
                                    v-tooltip.bottom="{value: result.region.nameZh, class: 'region-tooltip'}"></span>
                            &nbsp&nbsp<span class="label" style="font-size: 7px">{{result.publisher}}</span><br>
                            <span class="grid">
                                <span class="text-start col-6">
                                    <span style="display:inline">
                                        <p-tag class="product-tag ml-1" :value="result.bookType.nameZh"></p-tag>
                                    </span>
                                    <span class="has-bonus-tag" v-if="result.hasBonus">
                                        <p-tag style="background: #1B273D" class="ml-1" value="特典"></p-tag>
                                    </span>
                                    <span class="limited-tag" v-if="result.limited">
                                        <p-tag style="background: #1B273D" class="ml-1" value="限定"></p-tag>
                                    </span>
                                </span>
                                <span class="text-end col-5">
                                    <i class="pi pi-eye" v-tooltip.bottom="{value:'浏览', class: 'short-tooltip'}"></i>
                                    <span class="ml-2 mr-4">{{result.visitCount}}</span>
                                    <i class="pi pi-thumbs-up-fill" v-tooltip.bottom="{value:'点赞', class: 'short-tooltip'}"></i>
                                    <span class="ml-2 mr-2">{{result.likeCount}}</span>
                                </span>
                            </span>
                        </div>
                    </div>
                </div>
                <p-divider></p-divider>
            </div>
            <div v-if="searchResult.entityType == 4" v-for="result of searchResult.data">
                <div class="col-12">
                    <div class="search-item">
                        <a class="text-center" :href="'/db/merch/'+ result.id">
                            <img :src="result.cover" />
                        </a>
                        <div class="search-item-detail">
                                <span class="search-item-name text-truncate-1">
                                    <a :href="'/db/merch/'+ result.id">{{result.name}}</a>
                                </span>
                            <span class="grid">
                                <span class="text-start col-6">
                                    <span class="small-font">
                                        <span class="label">{{result.releaseDate}}</span>
                                    </span>
                                    <span :class="'fi fi-' + result.region.code" style="margin-left: 0.5rem"
                                        v-tooltip.bottom="{value: result.region.nameZh, class: 'region-tooltip'}">
                                    </span>
                                </span>
                                <span class="text-end col-5">
                                    <i class="pi pi-eye" v-tooltip.bottom="{value:'浏览', class: 'short-tooltip'}"></i>
                                    <span class="ml-2 mr-4">{{result.visitCount}}</span>
                                    <i class="pi pi-thumbs-up-fill" v-tooltip.bottom="{value:'点赞', class: 'short-tooltip'}"></i>
                                    <span class="ml-2 mr-2">{{result.likeCount}}</span>
                                </span>
                            </span>
                        </div>
                    </div>
                </div>
                <p-divider></p-divider>
            </div>
            <div v-if="searchResult.entityType == 5" v-for="result of searchResult.data">
                <div class="col-12">
                    <div class="search-item">
                        <a class="text-center" :href="'/db/game/'+ result.id">
                            <img :src="result.cover" />
                        </a>
                        <div class="search-item-detail">
                                <span class="search-item-name text-truncate-1">
                                    <a :href="'/db/game/'+ result.id">{{result.name}}</a>
                                </span>
                            <span class="small-font" style="margin: 0 0 .5rem 0;">
                                    <span class="label">&nbsp{{result.releaseDate}}</span>
                                </span><br>
                            <span :class="'fi fi-' + result.region.code" style="margin-left: 0.5rem"
                                    v-tooltip.bottom="{value: result.region.nameZh, class: 'region-tooltip'}">
                                </span>
                                
                            <span class="grid">
                                <span class="text-start col-6">
                                    <span>
                                        <p-tag class="ml-1" :value="result.platform.nameEn"></p-tag>
                                    </span>
                                    <span class="has-bonus-tag" v-if="result.hasBonus">
                                        <p-tag style="background: #1B273D" class="ml-1" value="特典"></p-tag>
                                    </span>
                                </span>
                                <span class="text-end col-5">
                                    <i class="pi pi-eye" v-tooltip.bottom="{value:'浏览', class: 'short-tooltip'}"></i>
                                    <span class="ml-2 mr-4">{{result.visitCount}}</span>
                                    <i class="pi pi-thumbs-up-fill" v-tooltip.bottom="{value:'点赞', class: 'short-tooltip'}"></i>
                                    <span class="ml-2 mr-2">{{result.likeCount}}</span>
                                </span>
                            </span>
                        </div>
                    </div>
                </div>
                <p-divider></p-divider>
            </div>
            <p-scrolltop target="parent" :threshold="100" class="search-scrolltop" icon="pi pi-arrow-up"></p-scrolltop>
        </p-scrollpanel>
        <p-paginator
            :template="{
                default: 'FirstPageLink PrevPageLink PageLinks NextPageLinLastPageLink LastPageLink JumpToPageDropdown'
                }"
            v-model:first="searchResult.offset" :rows="searchResult.limit"
            :total-records="searchResult.total" @page="pageChange($event)">
        </p-paginator>
    </div>
    <div v-else class="text-center mt-2">
        <img src="https://img.rakbow.com/common/icon/no-results.svg" /><br><span>暂无搜索结果</span>
    </div>
</div>
    `,
    mounted() {
        superplaceholder({
            el: document.querySelector('#globalSearch'),
            sentences: [
                '例: とある飛空士への誓約',
                '例: ひぐらしのなく頃に解 SOUNDTRACK',
                '例: ひぐらしのなく頃に 初回限定版'
            ],
            options: {
                // delay between letters (in milliseconds)
                // 各个字符显示之间的延迟时间，单位毫秒。
                letterDelay: 100, // milliseconds
                // delay between sentences (in milliseconds)
                // 各个句子之间的延迟时间，单位毫秒。
                sentenceDelay: 1000,
                // should start on input focus. Set false to autostart
                // 在输入框聚焦时才开始播放，设置为false会自动开始播放。
                startOnFocus: false,
                // loop through passed sentences
                // 是否循环播放。
                loop: true,
                // Initially shuffle the passed sentences
                // 是否打乱传入的句子。
                shuffle: false,
                // Show cursor or not. Shows by default
                // 是否显示光标，默认为显示。
                showCursor: true,
                // String to show as cursor
                // 光标字符串。
                cursor: '|',
                // // Control onFocus behaviour. Default is `superplaceholder.Actions.START`
                // onFocusAction: superplaceholder.Actions.[NOTHING|START|STOP],
                // // Control onBlur behaviour. Default is `superplaceholder.Actions.STOP`
                // onBlurAction: superplaceholder.Actions.[NOTHING|START|STOP]
            }
        });
    },
    inject: ['dialogRef'],
    data() {
        return {
            searchParams: {
                keyword: "",
                entityType: 0,
                offset: 0,
                limit: 10
            },
            searchResult: null,
            entityType: ENTITY_TYPE,
        }
    },
    watch: {

    },
    methods: {
        search() {
            HttpUtil.post(null, INDEX_SEARCH_URL, this.searchParams)
                .then(res => {
                    if(res.state === 1) {
                        this.searchResult = res.data;
                    }
                });
        },
        pageChange(ev) {
            this.searchParams.offset = ev.first;
            this.searchParams.limit = ev.rows;
            this.search();
        },
        entityTypeValue2Icon,
        entityTypevalueToLabel
    },
    components: {
        "p-button": primevue.button,
        "p-inputtext": primevue.inputtext,
        "p-dropdown": primevue.dropdown,
        "p-divider": primevue.divider,
        "p-scrollpanel": primevue.scrollpanel,
        "p-chip": primevue.chip,
        "p-tag": primevue.tag,
        "p-scrolltop": primevue.scrolltop,
        "p-paginator": primevue.paginator,
    }
};