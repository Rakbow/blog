import {HttpUtil} from '/js/basic/Http_Util.js';

const {useToast} = primevue.usetoast;
const Tooltip = primevue.tooltip;

const entryDbList = {
    template: `
        <p-toast></p-toast>
<p-datatable ref="dt" :value="items" class="p-datatable-sm" :always-show-paginator="items != 0"
                :lazy="true" v-model:filters="filters" :total-records="totalRecords" :loading="loading"
                :resizable-columns="true" column-resize-mode="expand"
                @page="onPage($event)" @sort="onSort($event)" @filter="onFilter($event)"
                filter-display="row" 
                :global-filter-fields="['name','nameZh','nameEn']"
                :paginator="true" :rows="10" striped-rows
                v-model:selection="selectedItems" dataKey="id"
                :scrollable="true" scroll-height="flex" :rows-per-page-options="[10,25,50]" show-gridlines
                paginator-template="FirstPageLink PrevPageLink PageLinks NextPageLink
                            LastPageLink CurrentPageReport RowsPerPageDropdown"
                current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据"
                responsive-layout="scroll">
    <template #header>
        <p-blockui :blocked="editBlock" class="grid">
            <div class="col-9" v-if="editAuth > 1">
                <p-button label="新增" icon="pi pi-plus" class="p-button-success p-button-sm mr-2"
                            @click="openNewDialog" style="width: 6em"></p-button>
                <p-button label="删除" icon="pi pi-trash" class="p-button-danger p-button-sm mr-2" @click="confirmDeleteSelected"
                            :disabled="!selectedItems || !selectedItems.length" style="width: 6em"></p-button>
                <p-button label="导出(CSV)" icon="pi pi-external-link" class="ml-2 p-button-help p-button-sm"
                            @click="exportCSV($event)" style="width: 8em"></p-button>
            </div>
            <div class="col-3">
                <p-multiselect :model-value="selectedColumns" :options="columns" option-label="header"
                            @update:model-value="onToggle" class=" text-end"
                            placeholder="可选显示列" style="width: 20em"></p-multiselect>
            </div>
        </p-blockui>
    </template>
    <template #empty>
                <span class="emptyInfo">
                    未检索到符合条件的数据
                </span>
    </template>
    <template #loading>
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
        <span>加载中，别急~</span>
    </template>
    <p-column selection-mode="multiple" style="flex: 0 0 3rem" :exportable="false" v-if="editAuth > 1"></p-column>
    <p-column header="序号" field="id" exportHeader="Entry Id" :sortable="true" style="flex: 0 0 5rem">
        <template #body="slotProps" v-if="editAuth > 1">
            <p-button class="p-button-link" @click="openEditDialog(slotProps.data)">
                {{slotProps.data.id}}
            </p-button>
        </template>
    </p-column>
    <p-column header="原名" field="name" :show-filter-menu="false"
                style="flex: 0 0 10rem">
        <template #body="slotProps">
            <a :href="'/db/entry/' + slotProps.data.id">
                {{slotProps.data.name}}
            </a>
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
        </template>
    </p-column>
    <p-column header="名称(中)" field="nameZh" :show-filter-menu="false" style="flex: 0 0 10rem">
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
        </template>
    </p-column>
    <p-column header="名称(英)" field="nameEn" :show-filter-menu="false" style="flex: 0 0 10rem">
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
        </template>
    </p-column>
    <p-column header="别名" field="alias" style="flex: 0 0 10rem">
        <template #body="slotProps">
            <ul class="px-4">
                <li v-for="item in slotProps.data.alias">
                    {{item}}
                </li>
            </ul>
        </template>
    </p-column>
    <p-column header="分类" field="category" :show-filter-menu="false" style="flex: 0 0 7rem">
        <template #body="slotProps">
            {{slotProps.data.category.label}}
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-dropdown v-model="filterModel.value" @change="filterCallback()" 
            :options="entryCategorySet" option-label="label" option-value="value" style="width: 5rem" >
            </p-dropdown>
        </template>
    </p-column>
    <p-column v-for="(col, index) of selectedColumns" :field="col.field"
                :header="col.header" :key="col.field + '_' + index" :sortable="true">
    </p-column>
</p-datatable>
<p-dialog :modal="true" v-model:visible="displayNewDialog" :style="{width: '600px'}" header="新增数据"
            class="p-fluid">
    <p-blockui :blocked="editBlock">
        <p-panel header="基础信息">
            <div class="field">
                <label>原名<span style="color: red">*</span></label>
                <p-inputtext v-model="item.name"></p-inputtext>
            </div>
            <div class="field">
                <label>名称(中)<span style="color: red">*</span></label>
                <p-inputtext v-model="item.nameZh"></p-inputtext>
            </div>
            <div class="field">
                <label>名称(英)<span style="color: red">*</span></label>
                <p-inputtext v-model="item.nameEn"></p-inputtext>
            </div>
            <div class="formgrid grid">
                <div class="field col-9">
                    <label>别名</label>
                    <p-chips v-model="item.alias" separator=","></p-chips>
                </div>
                <div class="field col-3">
                    <label>分类<span style="color: red">*</span></label>
                    <p-dropdown v-model="item.category" :options="entryCategorySet"
                        option-label="label" option-value="value">
                    </p-dropdown>
                </div>
            </div>
            <div class="field">
                <label>链接</label>
                <p-chips v-model="item.links" separator=",">
                    <template #chip="slotProps">
                        <div>
                            <i class="pi pi-link" style="font-size: 14px"></i>
                            <span>&nbsp&nbsp{{slotProps.value}}</span>
                        </div>
                    </template>
                </p-chips>
            </div>
            <div class="field">
                <label>备注</label>
                <p-textarea id="remark" v-model="item.remark" rows="3" cols="20" :auto-resize="true"></p-textarea>
            </div>
        </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text" @click="closeNewDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text" @click="submitNewItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="displayEditDialog" :style="{width: '600px'}" header="编辑数据"
            class="p-fluid">
    <p-blockui :blocked="editBlock">
        <p-panel header="基础信息">
            <div class="field">
                <label>原名<span style="color: red">*</span></label>
                <p-inputtext v-model="itemEdit.name"></p-inputtext>
            </div>
            <div class="field">
                <label>名称(中)<span style="color: red">*</span></label>
                <p-inputtext v-model="itemEdit.nameZh"></p-inputtext>
            </div>
            <div class="field">
                <label>名称(英)<span style="color: red">*</span></label>
                <p-inputtext v-model="itemEdit.nameEn"></p-inputtext>
            </div>
            <div class="formgrid grid">
                <div class="field col-9">
                    <label>别名</label>
                    <p-chips v-model="itemEdit.alias" separator=","></p-chips>
                </div>
                <div class="field col-3">
                    <label>分类<span style="color: red">*</span></label>
                    <p-dropdown v-model="itemEdit.category" :options="entryCategorySet"
                        option-label="label" option-value="value">
                    </p-dropdown>
                </div>
            </div>
            <div class="field">
                <label>链接</label>
                <p-chips v-model="itemEdit.links" separator=",">
                    <template #chip="slotProps">
                        <div>
                            <i class="pi pi-link" style="font-size: 14px"></i>
                            <span>&nbsp&nbsp{{slotProps.value}}</span>
                        </div>
                    </template>
                </p-chips>
            </div>
            <div class="field">
                <label>备注</label>
                <p-textarea id="remark" v-model="itemEdit.remark" rows="3" cols="20" :auto-resize="true"></p-textarea>
            </div>
        </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                    @click="closeEditDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text"
                    @click="submitEditItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="deleteDialog" :style="{width: '450px'}" header="删除数据">
    <p-blockui :blocked="editBlock">
        <div class="confirmation-content">
            <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"></i>
            <span v-if="item">确定删除所选的数据？</span>
        </div>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                    @click="deleteDialog = false" :disabled="editBlock"></p-button>
        <p-button label="确认删除" icon="pi pi-check" class="p-button-text"
                    @click="deleteSelectedItems" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
    `,
    mounted() {
        this.initData();
        this.init();
    },
    data() {
        return {
            //region common
            toast: useToast(),
            loading: false,
            dt: null,
            totalLoading: false,
            editAuth: null,
            //endregion

            //region search param
            totalRecords: 0,
            queryParams: {},
            filters: {
                'name': {value: ''},
                'nameZh': {value: ''},
                'nameEn': {value: ''},
                'category': {value: null}
            },
            //endregion

            //region basic
            item: {},
            items: [],

            entryCategorySet: [],
            columns: [
                {field: 'remark', header: '备注'},
                {field: 'addedTime', header: '收录时间'},
                {field: 'editedTime', header: '编辑时间'},
            ],
            //endregion

            //region edit
            itemEdit: {},
            editBlock: false,
            selectedItems: null,
            selectedColumns: null,
            displayNewDialog: false,
            displayEditDialog: false,
            deleteDialog: false,
            //endregion

        }
    },
    methods: {
        //region common
        exportCSV() {
            HttpUtil.get(null, CHECK_USER_AUTHORITY_URL)
                .then(res => {
                    if (res.state === 1) {
                        this.$refs.dt.exportCSV();
                    }
                })
        },
        //初始化
        initData() {
            this.loading = true;
            let json = {
                entityType: ENTITY.ENTRY
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_LIST_INIT_DATA_URL, json)
                .then(res => {
                    this.editAuth = res.editAuth;
                    this.entryCategorySet = res.entryCategorySet;
                    this.loading = false;
                    this.totalLoading = false;
                })
        },
        init() {
            this.loading = true;
            this.queryParams = {
                first: (this.queryParams !== {}?this.queryParams.first:0),
                rows: this.$refs.dt.rows,
                sortField: null,
                sortOrder: null,
                filters: this.filters
            };
            this.getItems();
            this.loading = false;
        },
        //endregion

        //region search
        onToggle(val) {
            this.selectedColumns = this.columns.filter(col => val.includes(col));
        },
        onPage(ev) {
            this.queryParams = ev;
            this.getItems();
        },
        onSort(ev) {
            this.queryParams = ev;
            this.getItems();
        },
        onFilter() {
            this.queryParams.filters = this.filters;
            this.queryParams.first = 0;
            this.getItems();
        },
        getItems() {
            let json = {
                pageLabel: "list",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_ENTRIES_URL, json)
                .then(res => {
                    this.items = res.data;
                    this.totalRecords = res.total;
                })
        },
        //endregion

        //region edit
        getNameByCode,
        //打开删除确认面板
        confirmDeleteSelected() {
            this.deleteDialog = true;
        },
        deleteSelectedItems() {
            let deleteItemIds = [];
            this.selectedItems.forEach(
                item => {
                    deleteItemIds.push(item.id);
                }
            );
            this.editBlock = true;
            HttpUtil.deleteRequest(this.toast, DELETE_ENTRY_URL, deleteItemIds)
                .then(res => {
                    if (res.state === 1) {
                        this.deleteDialog = false;
                        this.selectedItems = null;
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //打开编辑数据面板
        openEditDialog(data) {
            let dataTmp = JSON.parse(JSON.stringify(data));
            this.itemEdit = dataTmp;
            this.itemEdit.category = dataTmp.category.value;
            this.displayEditDialog = true;
        },
        //关闭编辑数据面板
        closeEditDialog() {
            this.displayEditDialog = false;
            this.itemEdit = {};
        },
        //保存编辑数据
        submitEditItem() {
            this.editBlock = true;
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ENTRY_URL, this.itemEdit)
                .then(res => {
                    if (res.state === 1) {
                        this.itemEdit = {};
                        this.closeEditDialog();
                        this.init();
                    }
                    this.editBlock = false;
                }).catch(e => {
                console.error(e);
            });
        },
        //打开新增数据面板
        openNewDialog() {
            this.item = {};
            this.displayNewDialog = true;
        },
        //关闭新增数据面板
        closeNewDialog() {
            this.item = {};
            this.displayNewDialog = false;
        },
        //提交新增数据
        submitNewItem() {
            HttpUtil.commonVueSubmit(this.toast, INSERT_ENTRY_URL, this.item)
                .then(res => {
                    if (res.state === 1) {
                        this.item = {};
                        this.closeNewDialog();
                        this.init();
                    }
                    this.editBlock = false;
                });
        }
        //endregion

    },
    components: {
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-dialog": primevue.dialog,
        "p-textarea": primevue.textarea,
        "p-toolbar": primevue.toolbar,
        "p-toast": primevue.toast,
        "p-panel": primevue.panel,
        "p-inputtext": primevue.inputtext,
        "p-button": primevue.button,
        "p-dropdown": primevue.dropdown,
        "p-blockui": primevue.blockui,
        "p-chips": primevue.chips,
        "p-multiselect": primevue.multiselect,
    }
}

const albumDbList = {
    template: `
        <p-toast></p-toast>
        <p-datatable ref="dt" :value="albums" class="p-datatable-sm" :always-show-paginator="albums != 0"
                     :lazy="true" v-model:filters="filters" :total-records="totalRecords" :loading="loading"
                     @page="onPage($event)" @sort="onSort($event)" @filter="onFilter($event)"
                     filter-display="row" 
                     :global-filter-fields="['name','catalogNo']"
                     :paginator="true" :rows="10" striped-rows
                     :resizable-columns="true" column-resize-mode="expand"
                     v-model:selection="selectedItems" dataKey="id"
                     :scrollable="true" scroll-height="flex" :rows-per-page-options="[10,25,50]" show-gridlines
                     paginator-template="FirstPageLink PrevPageLink PageLinks NextPageLink
                                 LastPageLink CurrentPageReport RowsPerPageDropdown"
                     current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据"
                     responsive-layout="scroll">
            <template #header>
                <p-blockui :blocked="editBlock" class="grid">
                    <div class="col-9" v-if="editAuth > 1">
                        <p-button label="新增" icon="pi pi-plus" class="p-button-success p-button-sm mr-2"
                                  @click="openNewDialog" style="width: 6em"></p-button>
                        <p-button label="删除" icon="pi pi-trash" class="p-button-danger p-button-sm mr-2" @click="confirmDeleteSelected"
                                  :disabled="!selectedItems || !selectedItems.length" style="width: 6em"></p-button>
                        <p-selectbutton v-model="itemsStatus" :options="statusOptions" aria-labelledby="single"
                                  :unselectable="false" @change="updateItemsStatus($event.value)"
                                  :disabled="!selectedItems || !selectedItems.length" v-if="editAuth > 2"></p-selectbutton>
                        <p-button label="导出(CSV)" icon="pi pi-external-link" class="ml-2 p-button-help p-button-sm"
                                  @click="exportCSV($event)" style="width: 8em"></p-button>
                    </div>
                    <div class="col-3">
                        <p-multiselect :model-value="selectedColumns" :options="columns" option-label="header"
                                   @update:model-value="onToggle" class=" text-end"
                                   placeholder="可选显示列" style="width: 20em"></p-multiselect>
                    </div>
                </p-blockui>
            </template>
            <template #empty>
                        <span class="emptyInfo">
                            未检索到符合条件的数据
                        </span>
            </template>
            <template #loading>
                <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
                <span>加载中，别急~</span>
            </template>
            <p-column selection-mode="multiple" style="flex: 0 0 3rem" :exportable="false" v-if="editAuth > 1"></p-column>
            <p-column header="序号" field="id" exportHeader="Album Id" :sortable="true" style="flex: 0 0 5rem">
                <template #body="slotProps" v-if="editAuth > 1">
                    <p-button class="p-button-link" @click="openEditDialog(slotProps.data)">
                        {{slotProps.data.id}}
                    </p-button>
                </template>
            </p-column>
            <p-column header="状态" field="status" :sortable="true" style="flex: 0 0 5rem" v-if="editAuth > 2">
                <template #body="{data}">
                    <p-button class="p-button-link" @click="updateStatus(data.id, data.status)" :disabled="editBlock">
                        <i class="pi" :class="{'true-icon pi-circle-fill': data.status, 'false-icon pi-circle-fill': !data.status}"></i>
                    </p-button>
                </template>
            </p-column>
            <p-column header="专辑编号" field="catalogNo" :show-filter-menu="false"
                      style="flex: 0 0 10rem">
                <template #filter="{filterModel,filterCallback}">
                    <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
                </template>
            </p-column>
            <p-column header="专辑原名" field="name" :show-filter-menu="false"
                      style="flex: 0 0 25rem">
                <template #body="slotProps">
                    <a :href="'/db/album/' + slotProps.data.id">
                        {{slotProps.data.name}}
                    </a>
                </template>
                <template #filter="{filterModel,filterCallback}">
                    <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
                </template>
            </p-column>
            <p-column header="发行时间" field="releaseDate" :sortable="true" style="flex: 0 0 7rem"></p-column>
            <p-column header="发售价格" field="price" :sortable="true" style="flex: 0 0 7rem">
                <template #body="slotProps">
                    {{slotProps.data.price}} {{slotProps.data.currencyUnit}}
                </template>
            </p-column>
            <p-column header="所属系列" field="franchises" :show-filter-menu="false" style="flex: 0 0 10rem">
                <template #body="slotProps">
                    <ul>
                        <li v-for="franchise in slotProps.data.franchises">
                            <a :href="'/db/franchise/' + franchise.value ">
                                {{franchise.label}}
                            </a>
                        </li>
                    </ul>
                </template>
                <template #filter="{filterModel,filterCallback}">
                    <p-multiselect v-model="filterModel.value" @change="getProducts($event), filterCallback()"
                                   :options="franchiseSet" placeholder="所属系列"
                                   option-label="label" option-value="value" display="chip" :filter="true"
                                   style="width: 9rem">
                    </p-multiselect>
                </template>
            </p-column>
            <p-column header="所属作品" filter-field="products" :show-filter-menu="false"
                      style="flex: 0 0 16rem">
                <template #body="slotProps">
                    <ul>
                        <li v-for="product in slotProps.data.products">
                            <a :href="'/db/product/' + product.value ">
                                {{product.label}}
                            </a>
                        </li>
                    </ul>
                </template>
                <template #filter="{filterModel,filterCallback}">
                    <p-multiselect v-model="filterModel.value" @change="filterCallback()"
                                   :options="productSet" placeholder="选择作品"
                                   option-label="label" option-value="value" display="chip" :filter="true"
                                   :disabled="productSelect" style="width: 15rem">
                    </p-multiselect>
                </template>
            </p-column>
            <p-column header="出版形式" filter-field="publishFormat" :show-filter-menu="false"
                      style="flex: 0 0 9rem">
                <template #body="slotProps">
                    <ul>
                        <li v-for="data in slotProps.data.publishFormat">
                            {{data.label}}
                        </li>
                    </ul>
                </template>
                <template #filter="{filterModel,filterCallback}">
                    <p-multiselect v-model="filterModel.value" @change="filterCallback()" style="width: 8rem"
                                   :options="publishFormatSet" option-label="label" option-value="value"
                                   display="chip" :filter="true">
                    </p-multiselect>
                </template>
            </p-column>
            <p-column header="专辑分类" filter-field="albumFormat" :show-filter-menu="false"
                      style="flex: 0 0 8rem">
                <template #body="slotProps">
                    <ul>
                        <li v-for="data in slotProps.data.albumFormat">
                            {{data.label}}
                        </li>
                    </ul>
                </template>
                <template #filter="{filterModel,filterCallback}">
                    <p-multiselect v-model="filterModel.value" @change="filterCallback()" style="width: 7rem"
                                   :options="albumFormatSet" option-label="label" option-value="value"
                                   display="chip" :filter="true">
                    </p-multiselect>
                </template>
            </p-column>
            <p-column header="媒体类型" filter-field="mediaFormat" :show-filter-menu="false"
                      style="flex: 0 0 8rem">
                <template #body="slotProps">
                    <ul>
                        <li v-for="data in slotProps.data.mediaFormat">
                            {{data.label}}
                        </li>
                    </ul>
                </template>
                <template #filter="{filterModel,filterCallback}">
                    <p-multiselect v-model="filterModel.value" @change="filterCallback()" style="width: 7rem"
                                   :options="mediaFormatSet" option-label="label" option-value="value"
                                   display="chip" :filter="true">
                    </p-multiselect>
                </template>
            </p-column>
            <p-column header="特典" field="hasBonus" data-type="boolean" body-class="text-center"
                      style="flex: 0 0 3rem">
                <template #body="{data}">
                    <i class="pi" :class="{'true-icon pi-check-circle': data.hasBonus, 'false-icon pi-times-circle': !data.hasBonus}"></i>
                </template>
                <template #filter="{filterModel,filterCallback}">
                    <p-tristatecheckbox v-model="filterModel.value" @change="filterCallback()"></p-tristatecheckbox>
                </template>
            </p-column>
            <p-column v-for="(col, index) of selectedColumns" :field="col.field"
                      :header="col.header" :key="col.field + '_' + index" :sortable="true">
            </p-column>
        </p-datatable>
        
    <p-dialog :modal="true" v-model:visible="displayNewDialog" :style="{width: '800px'}" header="新增数据"
              class="p-fluid">
        <p-blockui :blocked="editBlock">
            <p-panel header="基础信息">
                <div class="formgrid grid">
                    <div class="field col">
                        <label>专辑名称<span style="color: red">*</span></label>
                        <p-inputtext id="name" v-model.trim="album.name"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>专辑名称(英语)</label>
                        <p-inputtext id="nameEn" v-model.trim="album.nameEn"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>专辑名称(中文)</label>
                        <p-inputtext id="nameZh" v-model.trim="album.nameZh"></p-inputtext>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col">
                        <label>专辑编号</label>
                        <p-inputtext id="catalogNo" v-model.trim="album.catalogNo"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>商品条形码</label>
                        <p-inputtext id="barcode" v-model.trim="album.barcode"></p-inputtext>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col-6">
                        <label>发行时间<span style="color: red">*</span></label>
                        <p-calendar id="releaseDate" v-model="album.releaseDate" date-format="yy/mm/dd"
                                    :show-button-bar="true"
                                    :show-icon="true"></p-calendar>
                    </div>
                    <div class="field col-3">
                        <label>发行价格</label>
                        <p-inputnumber id="price" v-model="album.price"></p-inputnumber>
                    </div>
                    <div class="field col-3">
                        <label>货币单位</label>
                        <p-dropdown v-model="album.currencyUnit" :options="currencyUnitSet"
                                    option-label="label" option-value="value" placeholder="单位">
                        </p-dropdown>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col-4">
                        <label class="mb-3">所属系列<span style="color: red">*</span></label>
                        <p-multiselect v-model="album.franchises" @change="getProducts($event)"
                                       :options="franchiseSet" placeholder="所属系列"
                                       option-label="label" option-value="value" display="chip" :filter="true">
                        </p-multiselect>
                    </div>
                    <div class="field col-6">
                        <label class="mb-3">所属作品<span style="color: red">*</span></label>
                        <p-multiselect v-model="album.products" :options="productSet"
                                       option-label="label" option-value="value" placeholder="请先选择所属系列"
                                       display="chip" :filter="true" :disabled="productSelect">
                        </p-multiselect>
                    </div>
                    <div class="field col">
                        <div class="col-12">
                            <label class="mb-3">特典</label>
                        </div>
                        <div class="col-12 mt-4">
                            <p-inputswitch v-model="album.hasBonus" :true-value="1" :false-value="0"></p-inputswitch>
                        </div>
                    </div>
                </div>
            <div class="formgrid grid">
                <div class="field col-4">
                    <label class="mb-3">出版形式<span style="color: red">*</span></label>
                    <p-multiselect id="publishFormat" v-model="album.publishFormat" :options="publishFormatSet"
                                   option-label="label" option-value="value" placeholder="选择出版形式"
                                   display="chip" :filter="true">
                    </p-multiselect>
                </div>
                <div class="field col-4">
                    <label class="mb-3">专辑分类<span style="color: red">*</span></label>
                    <p-multiselect id="albumFormat" v-model="album.albumFormat" :options="albumFormatSet"
                                   option-label="label" option-value="value" placeholder="选择专辑分类"
                                   display="chip" :filter="true">
                    </p-multiselect>
                </div>
                <div class="field col-4">
                    <label class="mb-3">媒体类型<span style="color: red">*</span></label>
                    <p-multiselect id="mediaFormat" v-model="album.mediaFormat" :options="mediaFormatSet"
                                   option-label="label" option-value="value" placeholder="选择媒体类型"
                                   display="chip" :filter="true">
                    </p-multiselect>
                </div>
            </div>
            <div class="field">
                <label>备注</label>
                <p-textarea id="remark" v-model="album.remark" rows="3" cols="20" :auto-resize="true"></p-textarea>
            </div>
        </p-panel>
        </p-blockui>
        <template #footer>
            <p-button label="取消" icon="pi pi-times" class="p-button-text" @click="closeNewDialog" :disabled="editBlock"></p-button>
            <p-button label="保存" icon="pi pi-check" class="p-button-text" @click="submitNewItem" :disabled="editBlock"></p-button>
        </template>
    </p-dialog>
    <p-dialog :modal="true" v-model:visible="displayEditDialog" :style="{width: '800px'}" header="编辑数据"
              class="p-fluid">
        <p-blockui :blocked="editBlock">
            <p-panel header="基础信息">
                <div class="formgrid grid">
                    <div class="field col">
                        <label>专辑名称<span style="color: red">*</span></label>
                        <p-inputtext id="name" v-model.trim="itemEdit.name"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>专辑名称(英语)</label>
                        <p-inputtext id="nameEn" v-model.trim="itemEdit.nameEn"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>专辑名称(中文)</label>
                        <p-inputtext id="nameZh" v-model.trim="itemEdit.nameZh"></p-inputtext>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col">
                        <label>专辑编号</label>
                        <p-inputtext id="catalogNo" v-model.trim="itemEdit.catalogNo"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>商品条形码</label>
                        <p-inputtext id="barcode" v-model.trim="itemEdit.barcode"></p-inputtext>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col-6">
                        <label>发行时间<span style="color: red">*</span></label>
                        <p-calendar id="releaseDate" v-model="itemEdit.releaseDate" date-format="yy/mm/dd"
                                    :show-button-bar="true"
                                    :show-icon="true"></p-calendar>
                    </div>
                    <div class="field col-3">
                        <label>发行价格</label>
                        <p-inputnumber id="price" v-model="itemEdit.price"></p-inputnumber>
                    </div>
                    <div class="field col-3">
                        <label>货币单位</label>
                        <p-dropdown v-model="itemEdit.currencyUnit" :options="currencyUnitSet"
                                    option-label="label" option-value="value" placeholder="单位">
                        </p-dropdown>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col-4">
                        <label class="mb-3">所属系列<span style="color: red">*</span></label>
                        <p-multiselect v-model="itemEdit.franchises" @change="getProducts($event)"
                                    :options="franchiseSet" placeholder="所属系列"
                                    option-label="label" option-value="value" display="chip" :filter="true">
                        </p-multiselect>
                    </div>
                    <div class="field col-6">
                        <label class="mb-3">所属作品<span style="color: red">*</span></label>
                        <p-multiselect v-model="itemEdit.products" :options="productSet"
                                    option-label="label" option-value="value" placeholder="选择所属作品"
                                    display="chip" :filter="true" :disabled="productSelect">
                        </p-multiselect>
                    </div>
                    <div class="field col">
                        <div class="col-12">
                            <label class="mb-3">特典</label>
                        </div>
                        <div class="col-12 mt-4">
                            <p-inputswitch v-model="itemEdit.hasBonus" :true-value="1"
                                        :false-value="0"></p-inputswitch>
                        </div>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col-4">
                        <label class="mb-3">出版形式<span style="color: red">*</span></label>
                        <p-multiselect id="publishFormat" v-model="itemEdit.publishFormat" :options="publishFormatSet"
                                    option-label="label" option-value="value" placeholder="选择出版形式"
                                    display="chip">
                        </p-multiselect>
                    </div>
                    <div class="field col-4">
                        <label class="mb-3">专辑分类<span style="color: red">*</span></label>
                        <p-multiselect id="albumFormat" v-model="itemEdit.albumFormat" :options="albumFormatSet"
                                    option-label="label" option-value="value" placeholder="选择专辑分类"
                                    display="chip">
                        </p-multiselect>
                    </div>
                    <div class="field col-4">
                        <label class="mb-3">媒体类型<span style="color: red">*</span></label>
                        <p-multiselect id="mediaFormat" v-model="itemEdit.mediaFormat" :options="mediaFormatSet"
                                    option-label="label" option-value="value" placeholder="选择媒体类型"
                                    display="chip">
                        </p-multiselect>
                    </div>
                </div>
                <div class="field">
                    <label>备注</label>
                    <p-textarea id="remark" v-model="itemEdit.remark" rows="3" cols="20"
                                :auto-resize="true"></p-textarea>
                </div>
            </p-panel>
        </p-blockui>
        <template #footer>
            <p-button label="取消" icon="pi pi-times" class="p-button-text"
                      @click="closeEditDialog" :disabled="editBlock"></p-button>
            <p-button label="保存" icon="pi pi-check" class="p-button-text"
                      @click="submitEditItem" :disabled="editBlock"></p-button>
        </template>
    </p-dialog>
    <p-dialog :modal="true" v-model:visible="deleteDialog" :style="{width: '450px'}" header="删除数据">
        <p-blockui :blocked="editBlock">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"></i>
                <span v-if="album">确定删除所选的数据？</span>
            </div>
        </p-blockui>
        <template #footer>
            <p-button label="取消" icon="pi pi-times" class="p-button-text"
                      @click="deleteDialog = false" :disabled="editBlock"></p-button>
            <p-button label="确认删除" icon="pi pi-check" class="p-button-text"
                      @click="deleteSelectedItems" :disabled="editBlock"></p-button>
        </template>
    </p-dialog>
    `,
    mounted() {
        this.initData();
        this.init();
    },
    data() {
        return {
            //region common
            toast: useToast(),
            loading: false,
            dt: null,
            totalLoading: false,
            editAuth: null,
            //endregion

            //region search param
            totalRecords: 0,
            queryParams: {},
            filters: {
                'catalogNo': {value: '', matchMode: 'contains'},
                'name': {value: '', matchMode: 'contains'},
                'hasBonus': {value: null},
                'publishFormat': {value: null},
                'albumFormat': {value: null},
                'mediaFormat': {value: null},
                'franchises': {value: null},
                'products': {value: null},
            },
            //endregion

            //region basic
            album: {},
            albums: [],
            productSelect: true,
            productSet: [],
            franchiseSet: [],

            currencyUnitSet,
            mediaFormatSet: [],
            albumFormatSet: [],
            publishFormatSet: [],
            columns: [
                {field: 'barcode', header: '商品条形码'},
                {field: 'nameZh', header: '专辑名称(中文)'},
                {field: 'nameEn', header: '专辑名称(英文)'},
                {field: 'remark', header: '备注'},
            ],
            //endregion

            //region edit
            itemEdit: {},
            editBlock: false,
            itemsStatus: null,
            statusOptions,
            selectedItems: null,
            selectedColumns: null,
            displayNewDialog: false,
            displayEditDialog: false,
            deleteDialog: false,
            //endregion

        }
    },
    methods: {
        //region common
        updateItemsStatus(value) {
            this.editBlock = true;
            let json = {
                entityType: ENTITY.ALBUM,
                items: this.selectedItems,
                status: value
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEMS_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                    this.editBlock = false;
                })

        },
        exportCSV() {
            HttpUtil.get(null, CHECK_USER_AUTHORITY_URL)
                .then(res => {
                    if (res.state === 1) {
                        this.$refs.dt.exportCSV();
                    }
                })
        },
        //初始化
        initData() {
            this.loading = true;
            let json = {
                entityType: ENTITY.ALBUM
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_LIST_INIT_DATA_URL, json)
                .then(res => {
                    this.editAuth = res.editAuth;
                    this.mediaFormatSet = res.mediaFormatSet;
                    this.albumFormatSet = res.albumFormatSet;
                    this.publishFormatSet = res.publishFormatSet;
                    this.franchiseSet = res.franchiseSet;
                    this.totalLoading = false;
                    this.loading = false;
                })
        },
        init() {
            this.loading = true;
            this.queryParams = {
                first: (this.queryParams !== {}?this.queryParams.first:0),
                rows: this.$refs.dt.rows,
                sortField: null,
                sortOrder: null,
                filters: this.filters
            };
            this.getAlbums();
            this.loading = false;
        },
        //endregion

        //region search
        onToggle(val) {
            this.selectedColumns = this.columns.filter(col => val.includes(col));
        },
        onPage(ev) {
            this.queryParams = ev;
            this.getAlbums();
        },
        onSort(ev) {
            this.queryParams = ev;
            this.getAlbums();
        },
        onFilter() {
            this.queryParams.filters = this.filters;
            this.queryParams.first = 0;
            this.getAlbums();
        },
        getAlbums() {
            let json = {
                pageLabel: "list",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_ALBUMS_URL, json)
                .then(res => {
                    this.albums = res.data;
                    this.totalRecords = res.total;
                })
        },
        //endregion

        //region edit
        updateStatus(id, status) {
            let json = {
                entityType: ENTITY.ALBUM,
                entityId: id,
                status: !status
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEM_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                })
        },
        //打开删除确认面板
        confirmDeleteSelected() {
            this.deleteDialog = true;
        },
        deleteSelectedItems() {
            this.editBlock = true;
            HttpUtil.deleteRequest(this.toast, DELETE_ALBUM_URL, this.selectedItems)
                .then(res => {
                    if (res.state === 1) {
                        this.deleteDialog = false;
                        this.selectedItems = null;
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //打开编辑数据面板
        openEditDialog(data) {
            let dataTmp = JSON.parse(JSON.stringify(data));
            this.itemEdit = dataTmp;
            this.itemEdit.hasBonus = dataTmp.hasBonus ? 1 : 0;
            this.itemEdit.mediaFormat = label2value(dataTmp.mediaFormat, this.mediaFormatSet).concat();
            this.itemEdit.albumFormat = label2value(dataTmp.albumFormat, this.albumFormatSet).concat();
            this.itemEdit.publishFormat = label2value(dataTmp.publishFormat, this.publishFormatSet).concat();
            this.itemEdit.franchises = label2value(dataTmp.franchises, this.franchiseSet).concat();
            let json = {
                franchises: this.itemEdit.franchises,
                entityType: ENTITY.ALBUM
            };
            HttpUtil.post(this.toast, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        if (res.data.length !== 0) {
                            this.productSet = res.data;
                            this.productSelect = false;
                        } else {
                            this.productSelect = true;
                        }
                        this.itemEdit.products = label2value(this.itemEdit.products, this.productSet).concat();
                        this.displayEditDialog = true;
                    }
                });
        },
        //关闭编辑数据面板
        closeEditDialog() {
            this.displayEditDialog = false;
            this.itemEdit = {};
            this.init();
        },
        //保存编辑数据
        submitEditItem() {
            this.editBlock = true;
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ALBUM_URL, this.itemEdit)
                .then(res => {
                    if (res.state === 1) {
                        this.itemEdit = {};
                        this.closeEditDialog();
                        this.init();
                    }
                    this.editBlock = false;
                }).catch(e => {
                console.error(e);
            });
        },
        //打开新增数据面板
        openNewDialog() {
            this.album = {};
            this.productSelect = true;
            this.displayNewDialog = true;
        },
        //关闭新增数据面板
        closeNewDialog() {
            this.album = {};
            this.displayNewDialog = false;
        },
        //提交新增数据
        submitNewItem() {
            HttpUtil.commonVueSubmit(this.toast, INSERT_ALBUM_URL, this.album)
                .then(res => {
                    if (res.state === 1) {
                        this.album = {};
                        this.closeNewDialog();
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //根据系列id获取该系列所有作品
        getProducts(ev) {
            if (ev.value.length !== 0) {
                let json = {
                    franchises: ev.value,
                    entityType: ENTITY.ALBUM
                };
                HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                    .then(res => {
                        if (res.state === 1) {
                            if (res.data.length !== 0) {
                                this.productSet = res.data;
                                this.productSelect = false;
                            } else {
                                this.productSelect = true;
                                this.itemEdit.products = [];
                                this.album.products = [];
                            }
                        }
                    });
            } else {
                this.productSelect = true;
                this.itemEdit.products = [];
                this.album.products = [];
            }
        },
        //endregion

    },
    components: {
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-calendar": primevue.calendar,
        "p-inputnumber": primevue.inputnumber,
        "p-dialog": primevue.dialog,
        "p-textarea": primevue.textarea,
        "p-toolbar": primevue.toolbar,
        "p-toast": primevue.toast,
        "p-panel": primevue.panel,
        "p-divider": primevue.divider,
        "p-inputswitch": primevue.inputswitch,
        "p-multiselect": primevue.multiselect,
        "p-inputtext": primevue.inputtext,
        "p-button": primevue.button,
        "p-dropdown": primevue.dropdown,
        "p-tristatecheckbox": primevue.tristatecheckbox,
        "p-blockui": primevue.blockui,
        "p-selectbutton": primevue.selectbutton,
    }
}

const bookDbList = {
    template: `
        <p-toast></p-toast>
<p-datatable ref="dt" :value="books" class=" p-datatable-sm" :always-show-paginator="books != 0"
                :lazy="true" v-model:filters="filters" :total-records="totalRecords" :loading="loading"
                @page="onPage($event)" @sort="onSort($event)" @filter="onFilter($event)"
                filter-display="row"
                :globalFilterFields="['title','isbn10','isbn13']"
                :paginator="true" :rows="10" striped-rows
                :resizable-columns="true" column-resize-mode="expand"
                v-model:selection="selectedItems" dataKey="id"
                :scrollable="true" scroll-height="flex" :rows-per-page-options="[10,25,50]" show-gridlines
                paginator-template="FirstPageLink PrevPageLink PageLinks NextPageLink
                            LastPageLink CurrentPageReport RowsPerPageDropdown"
                current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据"
                responsive-layout="scroll">
    <template #header>
        <p-blockui :blocked="editBlock" class="grid">
            <div class="col-9" v-if="editAuth > 1">
                <p-button label="新增" icon="pi pi-plus" class="p-button-success p-button-sm mr-2"
                            @click="openNewDialog" style="width: 6em"></p-button>
                <p-button label="删除" icon="pi pi-trash" class="p-button-danger p-button-sm mr-2" @click="confirmDeleteSelected"
                            :disabled="!selectedItems || !selectedItems.length" style="width: 6em"></p-button>
                <p-selectbutton v-model="itemsStatus" :options="statusOptions" aria-labelledby="single"
                                  :unselectable="false" @change="updateItemsStatus($event.value)"
                                  :disabled="!selectedItems || !selectedItems.length" v-if="editAuth > 2"></p-selectbutton>
                <p-button label="导出(CSV)" icon="pi pi-external-link" class="ml-2 p-button-help p-button-sm"
                            @click="exportCSV($event)" style="width: 8em"></p-button>
            </div>
            <div class="col-3">
                <p-multiselect :model-value="selectedColumns" :options="columns" option-label="header"
                                @update:model-value="onToggle" class=" text-end"
                                placeholder="可选显示列" style="width: 20em"></p-multiselect>
            </div>
        </p-blockui>
    </template>
    <template #empty>
        <span class="emptyInfo">
            未检索到符合条件的数据
        </span>
    </template>
    <template #loading>
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
                <span>加载中，别急~</span>
        <i class="pi pi-spin pi-spinner mr-2" style="font-size: 2rem"></i>
        <span>加载中，别急~</span>
    </template>
    <p-column selection-mode="multiple" style="flex: 0 0 3rem" :exportable="false" v-if="editAuth > 1"></p-column>
    <p-column header="序号" field="id" exportHeader="Album Id" :sortable="true" style="flex: 0 0 5rem">
        <template #body="slotProps" v-if="editAuth > 1">
            <p-button class="p-button-link" @click="openEditDialog(slotProps.data)">
                {{slotProps.data.id}}
            </p-button>
        </template>
    </p-column>
    <p-column header="状态" field="status" :sortable="true" style="flex: 0 0 5rem" v-if="editAuth > 2">
                <template #body="{data}">
                    <p-button class="p-button-link" @click="updateStatus(data.id, data.status)" :disabled="editBlock">
                        <i class="pi" :class="{'true-icon pi-circle-fill': data.status, 'false-icon pi-circle-fill': !data.status}"></i>
                    </p-button>
                </template>
            </p-column>
    <p-column header="ISBN-10" field="isbn10" :show-filter-menu="false"
                style="flex: 0 0 8rem">
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                            ></p-inputtext>
        </template>
    </p-column>
    <p-column header="ISBN-13" field="isbn13" :show-filter-menu="false"
                style="flex: 0 0 10rem">
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                            ></p-inputtext>
        </template>
    </p-column>
    <p-column header="书名" field="title" :show-filter-menu="false"
                style="flex: 0 0 20rem">
        <template #body="slotProps">
            <a :href="'/db/book/' + slotProps.data.id">
                {{slotProps.data.title}}
            </a>
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                            ></p-inputtext>
        </template>
    </p-column>
    <p-column header="分类" field="bookType" :show-filter-menu="false" style="flex: 0 0 6rem">
        <template #body="slotProps">
            {{slotProps.data.bookType.nameZh}}
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-dropdown v-model="filterModel.value" @change="filterCallback()"
                        :options="bookTypeSet" option-label="label" option-value="value"
                        style="width: 5rem" >
            </p-dropdown>
        </template>
    </p-column>
    <p-column header="出版社" field="publisher" :show-filter-menu="false"
                style="flex: 0 0 12rem">
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                            ></p-inputtext>
        </template>
    </p-column>
    <p-column header="出版时间" field="publishDate" :sortable="true" style="flex: 0 0 7rem"></p-column>
    <p-column header="出版价格" field="price" :sortable="true" style="flex: 0 0 7rem">
        <template #body="slotProps">
            {{slotProps.data.price}} {{slotProps.data.currencyUnit}}
        </template>
    </p-column>
    <p-column header="特典" field="hasBonus" data-type="boolean" body-class="text-center"
                style="flex: 0 0 5rem">
        <template #body="{data}">
            <i class="pi"
                :class="{'true-icon pi-check-circle': data.hasBonus, 'false-icon pi-times-circle': !data.hasBonus}"></i>
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-tristatecheckbox v-model="filterModel.value" @change="filterCallback()"></p-tristatecheckbox>
        </template>
    </p-column>
    <p-column header="所属系列" field="franchises" :show-filter-menu="false" style="flex: 0 0 10rem">
        <template #body="slotProps">
            <ul class="px-4">
                <li v-for="franchise in slotProps.data.franchises">
                    <a :href="'/db/franchise/' + franchise.value ">
                        {{franchise.label}}
                    </a>
                </li>
            </ul>
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-multiselect v-model="filterModel.value" @change="getProducts($event), filterCallback()"
                            :options="franchiseSet" placeholder="所属系列"
                            option-label="label" option-value="value" display="chip" :filter="true"
                            style="width: 9rem">
            </p-multiselect>
        </template>

    </p-column>
    <p-column header="所属作品" filter-field="products" :show-filter-menu="false"
                style="flex: 0 0 16rem">
        <template #body="slotProps">
            <ul class="px-4">
                <li v-for="data in slotProps.data.products">
                    <a :href="'/db/product/' + data.value">
                        {{data.label}}
                    </a>
                </li>
            </ul>
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-multiselect v-model="filterModel.value" @change="filterCallback()"
                            :options="productSet" placeholder="选择作品"
                            option-label="label" option-value="value" display="chip" :filter="true"
                            :disabled="productSelect" style="width: 15rem">
            </p-multiselect>
        </template>

    </p-column>
    <p-column header="地区" field="region" :show-filter-menu="false" style="flex: 0 0 9rem">
        <template #body="slotProps">
            <span :class="'fi fi-' + slotProps.data.region.code"></span> ({{slotProps.data.region.nameZh}})
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-dropdown v-model="filterModel.value" @change="filterCallback()"
                        :options="regionSet" option-label="nameZh" option-value="code"
                        style="width: 8rem" >
                <template #value="slotProps">
                    <div class="country-item" v-if="slotProps.value">
                        <span :class="'fi fi-' + slotProps.value"></span>
                        <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
        </template>
    </p-column>
    <p-column header="语言" field="publishLanguage" :show-filter-menu="false" style="flex: 0 0 7rem">
        <template #body="slotProps">
            {{slotProps.data.publishLanguage.nameZh}}
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-dropdown v-model="filterModel.value" @change="filterCallback()"
                        :options="languageSet" option-label="nameZh" option-value="code"
                        style="width: 6rem" >
            </p-dropdown>
        </template>
    </p-column>
    <p-column v-for="(col, index) of selectedColumns" :field="col.field"
                :header="col.header" :key="col.field + '_' + index" :sortable="true">
    </p-column>
</p-datatable>

<p-dialog :modal="true" v-model:visible="displayNewDialog" header="新增数据"
          :style="{width: '600px'}" class="p-fluid">
          <p-blockui :blocked="editBlock">
    <p-panel header="基础信息">
        <div class="formgrid grid">
        <div class="field col">
            <label>ISBN-10<span style="color: red">*</span></label>
            <div class="p-inputgroup">
                <p-inputtext v-model.trim="book.isbn10"></p-inputtext>
                <p-button icon="pi pi-sync" class="p-button-warning"
                            @click="ISBNInterconvert('add', 'isbn10', book.isbn13)"
                            v-tooltip.bottom="{value:'生成ISBN-10', class: 'common-tooltip'}"></p-button>
            </div>
        </div>
        <div class="field col">
            <label>ISBN-13<span style="color: red">*</span></label>
            <div class="p-inputgroup">
                <p-inputtext v-model.trim="book.isbn13"></p-inputtext>
                <p-button icon="pi pi-sync" class="p-button-warning"
                            @click="ISBNInterconvert('add', 'isbn13', book.isbn10)"
                            v-tooltip.bottom="{value:'生成ISBN-13', class: 'common-tooltip'}"></p-button>
            </div>
        </div>
        </div>
        <div class="field">
            <label>书名<span style="color: red">*</span></label>
            <p-inputtext v-model.trim="book.title"></p-inputtext>
        </div>
        <div class="field">
            <label>书名(中)</label>
            <p-inputtext v-model.trim="book.titleZh"></p-inputtext>
        </div>
        <div class="field">
            <label>书名(英)</label>
            <p-inputtext v-model.trim="book.titleEn"></p-inputtext>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label class="mb-3">图书分类<span style="color: red">*</span></label>
                <p-dropdown v-model="book.bookType" :options="bookTypeSet"
                            option-label="label" option-value="value">
                </p-dropdown>
            </div>
            <div class="field col">
                <label class="mb-3">地区<span style="color: red">*</span></label>
                <p-dropdown v-model="book.region" :options="regionSet" :filter="true"
                            :show-clear="true" option-label="nameZh" option-value="code">
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            <div class="field col">
                <label class="mb-3">语言<span style="color: red">*</span></label>
                <p-dropdown v-model="book.publishLanguage" :options="languageSet"
                            option-label="nameZh" option-value="code">
                </p-dropdown>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col-4">
                <label class="mb-3">所属系列<span style="color: red">*</span></label>
                <p-multiselect v-model="book.franchises" @change="getProducts($event)"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true">
                </p-multiselect>
            </div>
            <div class="field col-6">
                <label class="mb-3">所属作品<span style="color: red">*</span></label>
                <p-multiselect v-model="book.products" :options="productSet"
                               option-label="label" option-value="value" placeholder="请先选择所属系列"
                               display="chip" :filter="true" :disabled="productSelect">
                </p-multiselect>
            </div>
            <div class="field col-2">
                <div class="col-12">
                    <label class="mb-3">特典</label>
                </div>
                <div class="col-12 mt-4">
                    <p-inputswitch v-model="book.hasBonus" :true-value="1"
                                   :false-value="0"></p-inputswitch>
                </div>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label>发行时间<span style="color: red">*</span></label>
                <p-calendar v-model="book.publishDate" date-format="yy/mm/dd"
                            :show-button-bar="true"
                            :show-icon="true"></p-calendar>
            </div>
            <div class="field col">
                <label>发行价格</label>
                <p-inputnumber v-model="book.price"></p-inputnumber>
            </div>
            <div class="field col">
                <label>出版社</label>
                <p-inputtext v-model.trim="book.publisher"></p-inputtext>
            </div>
        </div>
        <div class="field">
            <label>简介</label>
            <p-textarea v-model="book.summary" rows="3" cols="20"
                        :auto-resize="true"></p-textarea>
        </div>
        <div class="field">
            <label>备注</label>
            <p-textarea v-model="book.remark" rows="3" cols="20"
                        :auto-resize="true"></p-textarea>
        </div>
    </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text" @click="closeNewDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text" @click="submitNewItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="displayEditDialog" header="编辑数据"
          :style="{width: '600px'}" class="p-fluid">
          <p-blockui :blocked="editBlock">
    <p-panel header="基础信息">
        <div class="formgrid grid">
            <div class="field col">
                <label>ISBN-10<span style="color: red">*</span></label>
                <div class="p-inputgroup">
                    <p-inputtext v-model.trim="itemEdit.isbn10"></p-inputtext>
                    <p-button icon="pi pi-sync" class="p-button-warning"
                                @click="ISBNInterconvert('edit', 'isbn10', itemEdit.isbn13)"
                                v-tooltip.bottom="{value:'生成ISBN-10', class: 'common-tooltip'}"></p-button>
                </div>
            </div>
            <div class="field col">
                <label>ISBN-13<span style="color: red">*</span></label>
                <div class="p-inputgroup">
                    <p-inputtext v-model.trim="itemEdit.isbn13"></p-inputtext>
                    <p-button icon="pi pi-sync" class="p-button-warning"
                                @click="ISBNInterconvert('edit', 'isbn13', itemEdit.isbn10)"
                                v-tooltip.bottom="{value:'生成ISBN-13', class: 'common-tooltip'}"></p-button>
                </div>
            </div>
        </div>
        <div class="field">
            <label>书名<span style="color: red">*</span></label>
            <p-inputtext v-model.trim="itemEdit.title"></p-inputtext>
        </div>
        <div class="field">
            <label>书名(中)</label>
            <p-inputtext v-model.trim="itemEdit.titleZh"></p-inputtext>
        </div>
        <div class="field">
            <label>书名(英)</label>
            <p-inputtext v-model.trim="itemEdit.titleEn"></p-inputtext>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label class="mb-3">图书分类<span style="color: red">*</span></label>
                <p-dropdown v-model="itemEdit.bookType" :options="bookTypeSet"
                            option-label="label" option-value="value">
                </p-dropdown>
            </div>
            <div class="field col">
                <label class="mb-3">地区<span style="color: red">*</span></label>
                <p-dropdown v-model="itemEdit.region" :options="regionSet" :filter="true"
                            :show-clear="true" option-label="nameZh" option-value="code">
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            <div class="field col">
                <label class="mb-3">语言<span style="color: red">*</span></label>
                <p-dropdown v-model="itemEdit.publishLanguage" :options="languageSet"
                            option-label="nameZh" option-value="code">
                </p-dropdown>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col-4">
                <label class="mb-3">所属系列<span style="color: red">*</span></label>
                <p-multiselect v-model="itemEdit.franchises" @change="getProducts($event)"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true">
                </p-multiselect>
            </div>
            <div class="field col-6">
                <label class="mb-3">所属作品<span style="color: red">*</span></label>
                <p-multiselect v-model="itemEdit.products" :options="productSet"
                               option-label="label" option-value="value" placeholder="选择所属作品"
                               display="chip" :filter="true" :disabled="productSelect">
                </p-multiselect>
            </div>
            <div class="field col-2">
                <div class="col-12">
                    <label class="mb-3">特典</label>
                </div>
                <div class="col-12 mt-4">
                    <p-inputswitch v-model="itemEdit.hasBonus" :true-value="1"
                                   :false-value="0"></p-inputswitch>
                </div>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label>发行时间<span style="color: red">*</span></label>
                <p-calendar v-model="itemEdit.publishDate" date-format="yy/mm/dd"
                            :show-button-bar="true"
                            :show-icon="true"></p-calendar>
            </div>
            <div class="field col">
                <label>发行价格</label>
                <p-inputnumber v-model="itemEdit.price"></p-inputnumber>
            </div>
            <div class="field col">
                <label>出版社</label>
                <p-inputtext v-model.trim="itemEdit.publisher"></p-inputtext>
            </div>
        </div>
        <div class="field">
            <label>简介</label>
            <p-textarea v-model="itemEdit.summary" rows="3" cols="20"
                        :auto-resize="true"></p-textarea>
        </div>
        <div class="field">
            <label>备注</label>
            <p-textarea v-model="itemEdit.remark" rows="3" cols="20"
                        :auto-resize="true"></p-textarea>
        </div>
    </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                  @click="closeEditDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text"
                  @click="submitEditItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="deleteDialog" header="删除数据"
          :style="{width: '450px'}">
    <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"></i>
        <span>确定删除所选图书？</span>
    </div>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                  @click="deleteDialog = false" :disabled="editBlock"></p-button>
        <p-button label="确认删除" icon="pi pi-check" class="p-button-text"
                  @click="deleteSelectedItems" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
    `,
    mounted() {
        this.initData();
        this.init();
    },
    data() {
        return {
            //region common
            toast: useToast(),
            loading: false,
            dt: null,
            totalLoading: false,
            editAuth: null,
            //endregion

            //region search param
            totalRecords: 0,
            queryParams: {},
            filters: {
                'title': {value: ''},
                'isbn10': {value: ''},
                'isbn13': {value: ''},
                'publisher': {value: ''},
                'region': {value: null},
                'publishLanguage': {value: null},
                'bookType': {value: null},
                'franchises': {value: null},
                'products': {value: null},
                'hasBonus': {value: null},
            },
            //endregion

            //region basic
            book: {},
            books: [],
            productSelect: true,
            productSet: [],
            franchiseSet: [],

            bookTypeSet: [],
            regionSet: [],
            languageSet: [],
            columns: [
                {field: 'titleZh', header: '书名(中)'},
                {field: 'titleEn', header: '书名(英)'},
                {field: 'remark', header: '备注'},
                {field: 'addedTime', header: '收录时间'},
                {field: 'editedTime', header: '编辑时间'},
            ],
            //endregion

            //region edit
            itemEdit: {},
            editBlock: false,
            itemsStatus: null,
            statusOptions,
            selectedItems: null,
            selectedColumns: null,
            displayNewDialog: false,
            displayEditDialog: false,
            deleteDialog: false,
            //endregion

        }
    },
    methods: {
        //region common
        updateItemsStatus(value) {
            this.editBlock = true;
            let json = {
                entityType: ENTITY.BOOK,
                items: this.selectedItems,
                status: value
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEMS_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                    this.editBlock = false;
                })

        },
        exportCSV() {
            HttpUtil.get(null, CHECK_USER_AUTHORITY_URL)
                .then(res => {
                    if (res.state === 1) {
                        this.$refs.dt.exportCSV();
                    }
                })
        },
        //初始化
        initData() {
            this.loading = true;
            let json = {
                entityType: ENTITY.BOOK
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_LIST_INIT_DATA_URL, json)
                .then(res => {
                    this.editAuth = res.editAuth;
                    this.bookTypeSet = res.bookTypeSet;
                    this.regionSet = res.regionSet;
                    this.languageSet = res.languageSet;
                    this.franchiseSet = res.franchiseSet;
                    this.loading = false;
                    this.totalLoading = false;
                })
        },
        init() {
            this.loading = true;
            this.queryParams = {
                first: (this.queryParams !== {}?this.queryParams.first:0),
                rows: this.$refs.dt.rows,
                sortField: null,
                sortOrder: null,
                filters: this.filters
            };
            this.getBooks();
            this.loading = false;
        },
        //endregion

        //region search
        onToggle(val) {
            this.selectedColumns = this.columns.filter(col => val.includes(col));
        },
        onPage(ev) {
            this.queryParams = ev;
            this.getBooks();
        },
        onSort(ev) {
            this.queryParams = ev;
            this.getBooks();
        },
        onFilter() {
            this.queryParams.filters = this.filters;
            this.queryParams.first = 0;
            this.getBooks();
        },
        getBooks() {
            let json = {
                pageLabel: "list",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_BOOKS_URL, json)
                .then(res => {
                    this.books = res.data;
                    this.totalRecords = res.total;
                })
        },
        //endregion

        //region edit
        updateStatus(id, status) {
            let json = {
                entityType: ENTITY.BOOK,
                entityId: id,
                status: !status
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEM_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                })
        },
        getNameByCode,
        //打开删除确认面板
        confirmDeleteSelected() {
            this.deleteDialog = true;
        },
        deleteSelectedItems() {
            this.editBlock = true;
            HttpUtil.deleteRequest(this.toast, DELETE_BOOK_URL, this.selectedItems)
                .then(res => {
                    if (res.state === 1) {
                        this.deleteDialog = false;
                        this.selectedItems = null;
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //打开编辑数据面板
        openEditDialog(data) {
            let dataTmp = JSON.parse(JSON.stringify(data));
            this.itemEdit = dataTmp;
            this.itemEdit.bookType = dataTmp.bookType.id;
            this.itemEdit.region = dataTmp.region.code;
            this.itemEdit.publishLanguage = dataTmp.publishLanguage.code;
            this.itemEdit.hasBonus = dataTmp.hasBonus ? 1 : 0;
            this.itemEdit.franchises = label2value(dataTmp.franchises, this.franchiseSet).concat();
            let json = {
                franchises: this.itemEdit.franchises,
                entityType: ENTITY.BOOK
            };
            HttpUtil.post(this.toast, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        if (res.data.length !== 0) {
                            this.productSet = res.data;
                            this.productSelect = false;
                        } else {
                            this.productSelect = true;
                        }
                        this.itemEdit.products = label2value(this.itemEdit.products, this.productSet).concat();
                        this.displayEditDialog = true;
                    }
                });
        },
        //关闭编辑数据面板
        closeEditDialog() {
            this.displayEditDialog = false;
            this.itemEdit = {};
        },
        //保存编辑数据
        submitEditItem() {
            this.editBlock = true;
            HttpUtil.commonVueSubmit(this.toast, UPDATE_BOOK_URL, this.itemEdit)
                .then(res => {
                    if (res.state === 1) {
                        this.itemEdit = {};
                        this.closeEditDialog();
                        this.init();
                    }
                    this.editBlock = false;
                }).catch(e => {
                console.error(e);
            });
        },
        //打开新增数据面板
        openNewDialog() {
            this.book = {};
            this.productSelect = true;
            this.displayNewDialog = true;
        },
        //关闭新增数据面板
        closeNewDialog() {
            this.book = {};
            this.displayNewDialog = false;
        },
        //提交新增数据
        submitNewItem() {
            HttpUtil.commonVueSubmit(this.toast, INSERT_BOOK_URL, this.book)
                .then(res => {
                    if (res.state === 1) {
                        this.book = {};
                        this.closeNewDialog();
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //根据系列id获取该系列所有作品
        getProducts(ev) {
            if (ev.value.length !== 0) {
                let json = {
                    franchises: ev.value,
                    entityType: ENTITY.BOOK
                };
                HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                    .then(res => {
                        if (res.state === 1) {
                            if (res.data.length !== 0) {
                                this.productSet = res.data;
                                this.productSelect = false;
                            } else {
                                this.productSelect = true;
                                this.itemEdit.products = [];
                                this.book.products = [];
                            }
                        }
                    });
            } else {
                this.productSelect = true;
                this.itemEdit.products = [];
                this.book.products = [];
            }
        },
        ISBNInterconvert(method, label, isbn) {
            this.editBlock = true;
            let json = {
                label: label,
                isbn: isbn
            };
            HttpUtil.commonVueSubmit(this.toast, BOOK_GENERATE_ISBN_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        if(method === 'add') {
                            if(label === 'isbn13') {
                                this.book.isbn13 = res.data;
                            }
                            if(label === 'isbn10') {
                                this.book.isbn10 = res.data;
                            }
                        }
                        if(method === 'edit') {
                            if(label === 'isbn13') {
                                this.itemEdit.isbn13 = res.data;
                            }
                            if(label === 'isbn10') {
                                this.itemEdit.isbn10 = res.data;
                            }
                        }
                    }
                    this.editBlock = false;
                })
        }
        //endregion

    },
    components: {
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-calendar": primevue.calendar,
        "p-inputnumber": primevue.inputnumber,
        "p-dialog": primevue.dialog,
        "p-textarea": primevue.textarea,
        "p-toolbar": primevue.toolbar,
        "p-toast": primevue.toast,
        "p-panel": primevue.panel,
        "p-divider": primevue.divider,
        "p-inputswitch": primevue.inputswitch,
        "p-multiselect": primevue.multiselect,
        "p-inputtext": primevue.inputtext,
        "p-button": primevue.button,
        "p-dropdown": primevue.dropdown,
        "p-tristatecheckbox": primevue.tristatecheckbox,
        "p-blockui": primevue.blockui,
        "p-selectbutton": primevue.selectbutton,
    }
}

const discDbList = {
    template: `
    <p-toast></p-toast>
    <p-datatable ref="dt" :value="discs" class=" p-datatable-sm" :always-show-paginator="discs != 0"
                 :lazy="true" v-model:filters="filters" :total-records="totalRecords" :loading="loading"
                 @page="onPage($event)" @sort="onSort($event)" @filter="onFilter($event)"
                 filter-display="row"
                 :globalFilterFields="['name','catalogNo']"
                 :paginator="true" :rows="10" striped-rows
                 :resizable-columns="true" column-resize-mode="expand"
                 v-model:selection="selectedItems" dataKey="id"
                 :scrollable="true" scroll-height="flex" :rows-per-page-options="[10,25,50]" show-gridlines
                 paginator-template="FirstPageLink PrevPageLink PageLinks NextPageLink
                             LastPageLink CurrentPageReport RowsPerPageDropdown"
                 current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据"
                 responsive-layout="scroll">
            <template #header>
            <p-blockui :blocked="editBlock" class="grid">
                <div class="col-9" v-if="editAuth > 1">
                        <p-button label="新增" icon="pi pi-plus" class="p-button-success p-button-sm mr-2"
                                    @click="openNewDialog" style="width: 6em"></p-button>
                        <p-button label="删除" icon="pi pi-trash" class="p-button-danger p-button-sm mr-2" @click="confirmDeleteSelected"
                                    :disabled="!selectedItems || !selectedItems.length" style="width: 6em"></p-button>
                        <p-selectbutton v-model="itemsStatus" :options="statusOptions" aria-labelledby="single"
                                  :unselectable="false" @change="updateItemsStatus($event.value)"
                                  :disabled="!selectedItems || !selectedItems.length" v-if="editAuth > 2"></p-selectbutton>
                        <p-button label="导出(CSV)" icon="pi pi-external-link" class="ml-2 p-button-help p-button-sm"
                                    @click="exportCSV($event)" style="width: 8em"></p-button>
                </div>
                <div class="col-3">
                    <p-multiselect :model-value="selectedColumns" :options="columns" option-label="header"
                                    @update:model-value="onToggle" class=" text-end"
                                    placeholder="可选显示列" style="width: 20em"></p-multiselect>
                </div>
            </p-blockui>
        </template>
        <template #empty>
            <span class="emptyInfo">
                未检索到符合条件的数据
            </span>
        </template>
        <template #loading>
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
                <span>加载中，别急~</span>
        <i class="pi pi-spin pi-spinner mr-2" style="font-size: 2rem"></i>
        <span>加载中，别急~</span>
    </template>
        <p-column selection-mode="multiple" style="flex: 0 0 3rem" :exportable="false" v-if="editAuth > 1"></p-column>
        <p-column header="序号" field="id" exportHeader="Disc Id" :sortable="true" style="flex: 0 0 5rem">
            <template #body="slotProps" v-if="editAuth > 1">
                <p-button class="p-button-link" @click="openEditDialog(slotProps.data)">
                    {{slotProps.data.id}}
                </p-button>
            </template>
        </p-column> 
        <p-column header="状态" field="status" :sortable="true" style="flex: 0 0 5rem" v-if="editAuth > 2">
                <template #body="{data}">
                    <p-button class="p-button-link" @click="updateStatus(data.id, data.status)" :disabled="editBlock">
                        <i class="pi" :class="{'true-icon pi-circle-fill': data.status, 'false-icon pi-circle-fill': !data.status}"></i>
                    </p-button>
                </template>
            </p-column>       
        <p-column header="碟片编号" field="catalogNo" :show-filter-menu="false"
                  style="flex: 0 0 10rem">
            <template #filter="{filterModel,filterCallback}">
                <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
            </template>
        </p-column>
        <p-column header="商品名" field="name" :show-filter-menu="false"
                  style="flex: 0 0 25rem">
            <template #body="slotProps">
                <a :href="'/db/disc/' + slotProps.data.id">
                    {{slotProps.data.name}}
                </a>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
            </template>
        </p-column>
        <p-column header="发行时间" field="releaseDate" :sortable="true" style="flex: 0 0 7rem"></p-column>
        <p-column header="发售价格" field="price" :sortable="true" style="flex: 0 0 7rem">
            <template #body="slotProps">
                {{slotProps.data.price}} {{slotProps.data.currencyUnit}}
            </template>
        </p-column>
        <p-column header="所属系列" field="franchises" :show-filter-menu="false" style="flex: 0 0 10rem">
            <template #body="slotProps">
                <ul class="px-4">
                    <li v-for="franchise in slotProps.data.franchises">
                        <a :href="'/db/franchise/' + franchise.value ">
                            {{franchise.label}}
                        </a>
                    </li>
                </ul>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="getProducts($event), filterCallback()"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true"
                               style="width: 9rem">
                </p-multiselect>
            </template>
        </p-column>
        <p-column header="所属作品" filter-field="products" :show-filter-menu="false"
                  style="flex: 0 0 16rem">
            <template #body="slotProps">
                <ul class="px-4">
                    <li v-for="data in slotProps.data.products">
                        <a :href="'/db/product/' + data.value">
                            {{data.label}}
                        </a>
                    </li>
                </ul>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="filterCallback()"
                               :options="productSet" placeholder="选择作品"
                               option-label="label" option-value="value" display="chip" :filter="true"
                               :disabled="productSelect" style="width: 15rem" >
                </p-multiselect>
            </template>
        </p-column>
        <p-column header="地区" field="region" :show-filter-menu="false" style="flex: 0 0 9rem">
            <template #body="slotProps">
                <span :class="'fi fi-' + slotProps.data.region.code"></span>  ({{slotProps.data.region.nameZh}})
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-dropdown v-model="filterModel.value" @change="filterCallback()"
                            :options="regionSet" option-label="nameZh" option-value="code"
                            style="width: 8rem" >
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            </template>
        </p-column>
        <p-column header="媒体类型" filter-field="mediaFormat" :show-filter-menu="false"
                  style="flex: 0 0 9rem">
            <template #body="slotProps">
                <ul class="px-4">
                    <li v-for="data in slotProps.data.mediaFormat">
                        {{data.label}}
                    </li>
                </ul>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="filterCallback()" style="width: 8rem"
                               :options="mediaFormatSet" option-label="label" option-value="value"
                               display="chip" :filter="true">
                </p-multiselect>
            </template>
        </p-column>
        <p-column header="限定版" field="limited" data-type="boolean" body-class="text-center"
                  style="flex: 0 0 5rem">
            <template #body="{data}">
                <i class="pi" :class="{'true-icon pi-check-circle': data.limited, 'false-icon pi-times-circle': !data.limited}"></i>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-tristatecheckbox v-model="filterModel.value" @change="filterCallback()"></p-tristatecheckbox>
            </template>
        </p-column>
        <p-column header="特典" field="hasBonus" data-type="boolean" body-class="text-center"
                  style="flex: 0 0 5rem">
            <template #body="{data}">
                <i class="pi" :class="{'true-icon pi-check-circle': data.hasBonus, 'false-icon pi-times-circle': !data.hasBonus}"></i>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-tristatecheckbox v-model="filterModel.value" @change="filterCallback()"></p-tristatecheckbox>
            </template>
        </p-column>
        <p-column v-for="(col, index) of selectedColumns" :field="col.field"
                  :header="col.header" :key="col.field + '_' + index" :sortable="true">
        </p-column>
    </p-datatable>

<p-dialog :modal="true" v-model:visible="displayNewDialog" header="新增数据"
          :style="{width: '600px'}" class="p-fluid">
          <p-blockui :blocked="editBlock">
    <p-panel header="基础信息">
        <div class="formgrid grid">
            <div class="field col">
                <label>碟片编号</label>
                <p-inputtext v-model.trim="disc.catalogNo"></p-inputtext>
            </div>
            <div class="field col">
                <label>商品条形码</label>
                <p-inputtext v-model.trim="disc.barcode"></p-inputtext>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label>商品名<span style="color: red">*</span></label>
                <p-inputtext v-model.trim="disc.name"></p-inputtext>
            </div>
            <div class="field col">
                <label>商品名(中)</label>
                <p-inputtext v-model.trim="disc.nameZh"></p-inputtext>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col-4">
                <label>商品名(英)</label>
                <p-inputtext v-model.trim="disc.nameEn"></p-inputtext>
            </div>
            <div class="field col-4">
                <label>地区<span style="color: red">*</span></label>
                <p-dropdown v-model="disc.region" :options="regionSet" :filter="true"
                            :show-clear="true" option-label="nameZh" option-value="code">
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            <div class="field col-2">
                <div class="col-12">
                    <label class="mb-3">特典</label>
                </div>
                <div class="col-12 mt-3">
                    <p-inputswitch v-model="disc.hasBonus" :true-value="1"
                                   :false-value="0"></p-inputswitch>
                </div>
            </div>
            <div class="field col-2">
                <div class="col-12">
                    <label class="mb-3">限定版</label>
                </div>
                <div class="col-12 mt-3">
                    <p-inputswitch v-model="disc.limited" :true-value="1"
                                   :false-value="0"></p-inputswitch>
                </div>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col-4">
                <label class="mb-3">所属系列<span style="color: red">*</span></label>
                <p-multiselect v-model="disc.franchises" @change="getProducts($event)"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true">
                </p-multiselect>
            </div>
            <div class="field col-4">
                <label class="mb-3">所属作品<span style="color: red">*</span></label>
                <p-multiselect v-model="disc.products" :options="productSet"
                               option-label="label" option-value="value" placeholder="选择所属作品"
                               display="chip" :filter="true" :disabled="productSelect">
                </p-multiselect>
            </div>
            <div class="field col-4">
                <label class="mb-3">媒体类型<span style="color: red">*</span></label>
                <p-multiselect v-model="disc.mediaFormat" :options="mediaFormatSet"
                               option-label="label" option-value="value" placeholder="选择媒体类型"
                               display="chip">
                </p-multiselect>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label>发行时间<span style="color: red">*</span></label>
                <p-calendar v-model="disc.releaseDate" date-format="yy/mm/dd"
                            :show-button-bar="true"
                            :show-icon="true"></p-calendar>
            </div>
            <div class="field col">
                <label>发行价格</label>
                <p-inputnumber v-model="disc.price"></p-inputnumber>
            </div>
            <div class="field col-3">
                <label>货币单位</label>
                <p-dropdown v-model="itemEdit.currencyUnit" :options="currencyUnitSet"
                            option-label="label" option-value="value">
                </p-dropdown>
            </div>
        </div>
        <div class="field">
            <label>备注</label>
            <p-textarea v-model="disc.remark" rows="3" cols="20"
                        :auto-resize="true"></p-textarea>
        </div>
    </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text" @click="closeNewDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text" @click="submitNewItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="displayEditDialog" header="编辑数据"
          :style="{width: '600px'}" class="p-fluid">
          <p-blockui :blocked="editBlock">
    <p-panel header="基础信息">
        <div class="formgrid grid">
            <div class="field col">
                <label>碟片编号</label>
                <p-inputtext v-model.trim="itemEdit.catalogNo"></p-inputtext>
            </div>
            <div class="field col">
                <label>商品条形码</label>
                <p-inputtext v-model.trim="itemEdit.barcode"></p-inputtext>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label>商品名<span style="color: red">*</span></label>
                <p-inputtext v-model.trim="itemEdit.name"></p-inputtext>
            </div>
            <div class="field col">
                <label>商品名(中)</label>
                <p-inputtext v-model.trim="itemEdit.nameZh"></p-inputtext>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col-4">
                <label>商品名(英)</label>
                <p-inputtext v-model.trim="itemEdit.nameEn"></p-inputtext>
            </div>
            <div class="field col-4">
                <label>地区<span style="color: red">*</span></label>
                <p-dropdown v-model="itemEdit.region" :options="regionSet" :filter="true"
                            :show-clear="true" option-label="nameZh" option-value="code">
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            <div class="field col-2">
                <div class="col-12">
                    <label class="mb-3">特典</label>
                </div>
                <div class="col-12 mt-3">
                    <p-inputswitch v-model="itemEdit.hasBonus" :true-value="1"
                                   :false-value="0"></p-inputswitch>
                </div>
            </div>
            <div class="field col-2">
                <div class="col-12">
                    <label class="mb-3">限定版</label>
                </div>
                <div class="col-12 mt-3">
                    <p-inputswitch v-model="itemEdit.limited" :true-value="1"
                                   :false-value="0"></p-inputswitch>
                </div>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col-4">
                <label class="mb-3">所属系列<span style="color: red">*</span></label>
                <p-multiselect v-model="itemEdit.franchises" @change="getProducts($event)"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true">
                </p-multiselect>
            </div>
            <div class="field col-4">
                <label class="mb-3">所属作品<span style="color: red">*</span></label>
                <p-multiselect v-model="itemEdit.products" :options="productSet"
                               option-label="label" option-value="value" placeholder="选择所属作品"
                               display="chip" :filter="true" :disabled="productSelect">
                </p-multiselect>
            </div>
            <div class="field col-4">
                <label class="mb-3">媒体类型<span style="color: red">*</span></label>
                <p-multiselect v-model="itemEdit.mediaFormat" :options="mediaFormatSet"
                               option-label="label" option-value="value" placeholder="选择媒体类型"
                               display="chip">
                </p-multiselect>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label>发行时间<span style="color: red">*</span></label>
                <p-calendar v-model="itemEdit.releaseDate" date-format="yy/mm/dd"
                            :show-button-bar="true"
                            :show-icon="true"></p-calendar>
            </div>
            <div class="field col">
                <label>发行价格</label>
                <p-inputnumber v-model="itemEdit.price"></p-inputnumber>
            </div>
            <div class="field col-3">
                <label>货币单位</label>
                <p-dropdown v-model="itemEdit.currencyUnit" :options="currencyUnitSet"
                            option-label="label" option-value="value">
                </p-dropdown>
            </div>
        </div>
        <div class="field">
            <label>备注</label>
            <p-textarea v-model="itemEdit.remark" rows="3" cols="20"
                        :auto-resize="true"></p-textarea>
        </div>
    </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                  @click="closeEditDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text"
                  @click="submitEditItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="deleteDialog" header="删除数据"
          :style="{width: '450px'}">
    <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"></i>
        <span>确定删除所选商品？</span>
    </div>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                  @click="deleteDialog = false" :disabled="editBlock"></p-button>
        <p-button label="确认删除" icon="pi pi-check" class="p-button-text"
                  @click="deleteSelectedItems" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
    `,
    mounted() {
        this.initData();
        this.init();
    },
    data() {
        return {
            //region common
            toast: useToast(),
            loading: false,
            dt: null,
            totalLoading: false,
            editAuth: null,
            //endregion

            //region search param
            totalRecords: 0,
            queryParams: {},
            filters: {
                'catalogNo': {value: ''},
                'name': {value: ''},
                'region': {value: ''},
                'franchises': {value: null},
                'products': {value: null},
                'mediaFormat': {value: null},
                'limited': {value: null},
                'hasBonus': {value: null},
            },
            //endregion

            //region basic
            disc: {},
            discs: [],
            productSelect: true,
            productSet: [],
            franchiseSet: [],

            mediaFormatSet: [],
            regionSet: [],
            currencyUnitSet,
            columns: [
                {field: 'nameEn', header: '商品名(英)'},
                {field: 'nameZh', header: '商品名(中)'},
                {field: 'barcode', header: '商品条形码'},
                {field: 'remark', header: '备注'},
                {field: 'addedTime', header: '收录时间'},
                {field: 'editedTime', header: '编辑时间'},
            ],
            //endregion

            //region edit
            itemEdit: {},
            editBlock: false,
            itemsStatus: null,
            statusOptions,
            selectedItems: null,
            selectedColumns: null,
            displayNewDialog: false,
            displayEditDialog: false,
            deleteDialog: false,
            //endregion

        }
    },
    methods: {
        //region common
        updateItemsStatus(value) {
            this.editBlock = true;
            let json = {
                entityType: ENTITY.DISC,
                items: this.selectedItems,
                status: value
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEMS_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                    this.editBlock = false;
                })

        },
        exportCSV() {
            HttpUtil.get(null, CHECK_USER_AUTHORITY_URL)
                .then(res => {
                    if (res.state === 1) {
                        this.$refs.dt.exportCSV();
                    }
                })
        },
        //初始化
        initData() {
            this.loading = true;
            let json = {
                entityType: ENTITY.DISC
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_LIST_INIT_DATA_URL, json)
                .then(res => {
                    this.editAuth = res.editAuth;
                    this.mediaFormatSet = res.mediaFormatSet;
                    this.regionSet = res.regionSet;
                    this.franchiseSet = res.franchiseSet;
                    this.loading = false;
                    this.totalLoading = false;
                })
        },
        init() {
            this.loading = true;
            this.queryParams = {
                first: (this.queryParams !== {}?this.queryParams.first:0),
                rows: this.$refs.dt.rows,
                sortField: null,
                sortOrder: null,
                filters: this.filters
            };
            this.getDiscs();
            this.loading = false;
        },
        //endregion

        //region search
        onToggle(val) {
            this.selectedColumns = this.columns.filter(col => val.includes(col));
        },
        onPage(ev) {
            this.queryParams = ev;
            this.getDiscs();
        },
        onSort(ev) {
            this.queryParams = ev;
            this.getDiscs();
        },
        onFilter() {
            this.queryParams.filters = this.filters;
            this.queryParams.first = 0;
            this.getDiscs();
        },
        getDiscs() {
            let json = {
                pageLabel: "list",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_DISCS_URL, json)
                .then(res => {
                    this.discs = res.data;
                    this.totalRecords = res.total;
                })
        },
        //endregion

        //region edit
        updateStatus(id, status) {
            let json = {
                entityType: ENTITY.DISC,
                entityId: id,
                status: !status
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEM_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                })
        },
        getNameByCode,
        //打开删除确认面板
        confirmDeleteSelected() {
            this.deleteDialog = true;
        },
        deleteSelectedItems() {
            this.editBlock = true;
            HttpUtil.deleteRequest(this.toast, DELETE_DISC_URL, this.selectedItems)
                .then(res => {
                    if (res.state === 1) {
                        this.deleteDialog = false;
                        this.selectedItems = null;
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //打开编辑数据面板
        openEditDialog(data) {
            let dataTmp = JSON.parse(JSON.stringify(data));
            this.itemEdit = dataTmp;
            this.itemEdit.mediaFormat = label2value(dataTmp.mediaFormat, this.mediaFormatSet).concat();
            this.itemEdit.hasBonus = dataTmp.hasBonus ? 1 : 0;
            this.itemEdit.limited = dataTmp.limited ? 1 : 0;
            this.itemEdit.region = dataTmp.region.code;
            this.itemEdit.franchises = label2value(dataTmp.franchises, this.franchiseSet).concat();
            let json = {
                franchises: this.itemEdit.franchises,
                entityType: ENTITY.DISC
            };
            HttpUtil.post(this.toast, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        if (res.data.length !== 0) {
                            this.productSet = res.data;
                            this.productSelect = false;
                        } else {
                            this.productSelect = true;
                        }
                        this.itemEdit.products = label2value(this.itemEdit.products, this.productSet).concat();
                        this.displayEditDialog = true;
                    }
                });
        },
        //关闭编辑数据面板
        closeEditDialog() {
            this.displayEditDialog = false;
            this.itemEdit = {};
        },
        //保存编辑数据
        submitEditItem() {
            this.editBlock = true;
            HttpUtil.commonVueSubmit(this.toast, UPDATE_DISC_URL, this.itemEdit)
                .then(res => {
                    if (res.state === 1) {
                        this.itemEdit = {};
                        this.closeEditDialog();
                        this.init();
                    }
                    this.editBlock = false;
                }).catch(e => {
                console.error(e);
            });
        },
        //打开新增数据面板
        openNewDialog() {
            this.disc = {};
            this.productSelect = true;
            this.displayNewDialog = true;
        },
        //关闭新增数据面板
        closeNewDialog() {
            this.disc = {};
            this.displayNewDialog = false;
        },
        //提交新增数据
        submitNewItem() {
            HttpUtil.commonVueSubmit(this.toast, INSERT_DISC_URL, this.disc)
                .then(res => {
                    if (res.state === 1) {
                        this.disc = {};
                        this.closeNewDialog();
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //根据系列id获取该系列所有作品
        getProducts(ev) {
            if (ev.value.length !== 0) {
                let json = {
                    franchises: ev.value,
                    entityType: ENTITY.DISC
                };
                HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                    .then(res => {
                        if (res.state === 1) {
                            if (res.data.length !== 0) {
                                this.productSet = res.data;
                                this.productSelect = false;
                            } else {
                                this.productSelect = true;
                                this.itemEdit.products = [];
                                this.disc.products = [];
                            }
                        }
                    });
            } else {
                this.productSelect = true;
                this.itemEdit.products = [];
                this.disc.products = [];
            }
        },
        //endregion

    },
    components: {
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-calendar": primevue.calendar,
        "p-inputnumber": primevue.inputnumber,
        "p-dialog": primevue.dialog,
        "p-textarea": primevue.textarea,
        "p-toolbar": primevue.toolbar,
        "p-toast": primevue.toast,
        "p-panel": primevue.panel,
        "p-divider": primevue.divider,
        "p-inputswitch": primevue.inputswitch,
        "p-multiselect": primevue.multiselect,
        "p-inputtext": primevue.inputtext,
        "p-button": primevue.button,
        "p-dropdown": primevue.dropdown,
        "p-tristatecheckbox": primevue.tristatecheckbox,
        "p-blockui": primevue.blockui,
        "p-selectbutton": primevue.selectbutton,
    }
}

const gameDbList = {
    template: `
        <p-toast></p-toast>
    <p-datatable ref="dt" :value="games" class=" p-datatable-sm" :always-show-paginator="games != 0"
                 :lazy="true" v-model:filters="filters" :total-records="totalRecords" :loading="loading"
                 @page="onPage($event)" @sort="onSort($event)" @filter="onFilter($event)"
                 filter-display="row"
                 :globalFilterFields="['name']"
                 :paginator="true" :rows="10" striped-rows
                 :resizable-columns="true" column-resize-mode="expand"
                 v-model:selection="selectedItems" dataKey="id"
                 :scrollable="true" scroll-height="flex" :rows-per-page-options="[10,25,50]" show-gridlines
                 paginator-template="FirstPageLink PrevPageLink PageLinks NextPageLink
                             LastPageLink CurrentPageReport RowsPerPageDropdown"
                 current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据"
                 responsive-layout="scroll">
        <template #header>
            <p-blockui :blocked="editBlock" class="grid">
                <div class="col-9" v-if="editAuth > 1">
                    <p-button label="新增" icon="pi pi-plus" class="p-button-success p-button-sm mr-2"
                                @click="openNewDialog" style="width: 6em"></p-button>
                    <p-button label="删除" icon="pi pi-trash" class="p-button-danger p-button-sm mr-2" @click="confirmDeleteSelected"
                                :disabled="!selectedItems || !selectedItems.length" style="width: 6em"></p-button>
                    <p-selectbutton v-model="itemsStatus" :options="statusOptions" aria-labelledby="single"
                                  :unselectable="false" @change="updateItemsStatus($event.value)"
                                  :disabled="!selectedItems || !selectedItems.length" v-if="editAuth > 2"></p-selectbutton>
                    <p-button label="导出(CSV)" icon="pi pi-external-link" class="ml-2 p-button-help p-button-sm"
                                @click="exportCSV($event)" style="width: 8em"></p-button>
                </div>
                <div class="col-3">
                    <p-multiselect :model-value="selectedColumns" :options="columns" option-label="header"
                                    @update:model-value="onToggle" class=" text-end"
                                    placeholder="可选显示列" style="width: 20em"></p-multiselect>
                </div>
            </p-blockui>
        </template>
        <template #empty>
            <span class="emptyInfo">
                未检索到符合条件的数据
            </span>
        </template>
        <template #loading>
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
                <span>加载中，别急~</span>
        <i class="pi pi-spin pi-spinner mr-2" style="font-size: 2rem"></i>
        <span>加载中，别急~</span>
    </template>
        <p-column selection-mode="multiple" style="flex: 0 0 3rem" :exportable="false" v-if="editAuth > 1"></p-column>
        <p-column header="序号" field="id" exportHeader="Game Id" :sortable="true" style="flex: 0 0 5rem">
            <template #body="slotProps" v-if="editAuth > 1">
                <p-button class="p-button-link" @click="openEditDialog(slotProps.data)">
                    {{slotProps.data.id}}
                </p-button>
            </template>
        </p-column>
        <p-column header="状态" field="status" :sortable="true" style="flex: 0 0 5rem" v-if="editAuth > 2">
                <template #body="{data}">
                    <p-button class="p-button-link" @click="updateStatus(data.id, data.status)" :disabled="editBlock">
                        <i class="pi" :class="{'true-icon pi-circle-fill': data.status, 'false-icon pi-circle-fill': !data.status}"></i>
                    </p-button>
                </template>
            </p-column>
        <p-column header="游戏名" field="name" :show-filter-menu="false"
                  style="flex: 0 0 20rem">
            <template #body="slotProps">
                <a :href="'/db/game/' + slotProps.data.id">
                    {{slotProps.data.name}}
                </a>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
            </template>
        </p-column>
        <p-column header="发售时间" field="releaseDate" :sortable="true" style="flex: 0 0 7rem"></p-column>
        <p-column header="所属系列" field="franchises" :show-filter-menu="false" style="flex: 0 0 10rem">
            <template #body="slotProps">
                <ul class="px-4">
                    <li v-for="franchise in slotProps.data.franchises">
                        <a :href="'/db/franchise/' + franchise.value ">
                            {{franchise.label}}
                        </a>
                    </li>
                </ul>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="getProducts($event), filterCallback()"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true"
                               style="width: 9rem">
                </p-multiselect>
            </template>
        </p-column>
        <p-column header="所属作品" filter-field="products" :show-filter-menu="false"
                  style="flex: 0 0 16rem">
            <template #body="slotProps">
                <ul class="px-4">
                    <li v-for="product in slotProps.data.products">
                        <a :href="'/db/product/' + product.value ">
                            {{product.label}}
                        </a>
                    </li>
                </ul>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="filterCallback()"
                               :options="productSet" placeholder="选择作品"
                               option-label="label" option-value="value" display="chip" :filter="true"
                               :disabled="productSelect" style="width: 15rem" >
                </p-multiselect>
            </template>
        </p-column>
        <p-column header="发售类型" field="releaseType" :show-filter-menu="false" style="flex: 0 0 9rem">
            <template #body="slotProps">
                {{slotProps.data.releaseType.nameZh}}
            </template>
        </p-column>
        <p-column header="发售平台" field="platform" :show-filter-menu="false" style="flex: 0 0 9rem">
            <template #body="slotProps">
                {{slotProps.data.platform.nameEn}}
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-dropdown v-model="filterModel.value" @change="filterCallback()"
                            :options="gamePlatformSet" option-label="labelEn" option-value="value"
                            style="width: 8rem">
                </p-dropdown>
            </template>
        </p-column>
        <p-column header="地区" field="region" :show-filter-menu="false" style="flex: 0 0 9rem">
            <template #body="slotProps">
                {{slotProps.data.region.nameZh}} <span :class="'fi fi-' + slotProps.data.region.code"></span>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-dropdown v-model="filterModel.value" @change="filterCallback()"
                            :options="regionSet" option-label="nameZh" option-value="code"
                            style="width: 8rem" >
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            </template>
        </p-column>
        <p-column header="特典" field="hasBonus" data-type="boolean" body-class="text-center"
                  style="flex: 0 0 5rem">
            <template #body="{data}">
                <i class="pi" :class="{'true-icon pi-check-circle': data.hasBonus, 'false-icon pi-times-circle': !data.hasBonus}"></i>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-tristatecheckbox v-model="filterModel.value" @change="filterCallback()"></p-tristatecheckbox>
            </template>
        </p-column>
        <p-column v-for="(col, index) of selectedColumns" :field="col.field"
                  :header="col.header" :key="col.field + '_' + index" :sortable="true">
        </p-column>
    </p-datatable>

<p-dialog :modal="true" v-model:visible="displayNewDialog" header="新增数据"
          :style="{width: '600px'}" class="p-fluid">
          <p-blockui :blocked="editBlock">
    <p-panel header="基础信息">
        <div class="field">
            <label>游戏名<span style="color: red">*</span></label>
            <p-inputtext v-model.trim="game.name"></p-inputtext>
        </div>
        <div class="field">
            <label>游戏名(中)</label>
            <p-inputtext v-model.trim="game.nameZh"></p-inputtext>
        </div>
        <div class="field">
            <label>游戏名(英)</label>
            <p-inputtext v-model.trim="game.nameEn"></p-inputtext>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label>商品条形码</label>
                <p-inputtext v-model.trim="game.barcode"></p-inputtext>
            </div>
            <div class="field col">
                <label>发行时间<span style="color: red">*</span></label>
                <p-calendar v-model="game.releaseDate" date-format="yy/mm/dd"
                            :show-button-bar="true"
                            :show-icon="true"></p-calendar>
            </div>
            <div class="field col-2">
                <div class="col-12">
                    <label class="mb-3">特典</label>
                </div>
                <div class="col-12 mt-3">
                    <p-inputswitch v-model="game.hasBonus" :true-value="1"
                                   :false-value="0"></p-inputswitch>
                </div>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label class="mb-3">发售类型<span style="color: red">*</span></label>
                <p-dropdown v-model="game.releaseType" :options="releaseTypeSet"
                            option-label="labelZh" option-value="value">
                </p-dropdown>
            </div>
            <div class="field col">
                <label class="mb-3">发售平台<span style="color: red">*</span></label>
                <p-dropdown v-model="game.platform" :options="gamePlatformSet"
                            option-label="labelEn" option-value="value">
                </p-dropdown>
            </div>
            <div class="field col">
                <label class="mb-3">地区<span style="color: red">*</span></label>
                <p-dropdown v-model="game.region" :options="regionSet" :filter="true"
                            :show-clear="true" option-label="nameZh" option-value="code">
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
        </div>
        <div class="formgrid grid">
            <div class="field col-4">
                <label class="mb-3">所属系列<span style="color: red">*</span></label>
                <p-multiselect v-model="game.franchises" @change="getProducts($event)"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true">
                </p-multiselect>
            </div>
            <div class="field col-8">
                <label class="mb-3">所属产品<span style="color: red">*</span></label>
                <p-multiselect v-model="game.products" :options="productSet"
                               option-label="label" option-value="value" placeholder="请先选择所属系列"
                               display="chip" :filter="true" :disabled="productSelect">
                </p-multiselect>
            </div>
        </div>
        <div class="field">
            <label>备注</label>
            <p-textarea v-model="game.remark" rows="3" cols="20"
                        :auto-resize="true"></p-textarea>
        </div>
    </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
         @click="closeNewDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text"
         @click="submitNewItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="displayEditDialog" header="编辑数据"
          :style="{width: '600px'}" class="p-fluid">
          <p-blockui :blocked="editBlock">
    <p-panel header="基础信息">
        <div class="field">
            <label>游戏名<span style="color: red">*</span></label>
            <p-inputtext v-model.trim="itemEdit.name"></p-inputtext>
        </div>
        <div class="field">
            <label>游戏名(中)</label>
            <p-inputtext v-model.trim="itemEdit.nameZh"></p-inputtext>
        </div>
        <div class="field">
            <label>游戏名(英)</label>
            <p-inputtext v-model.trim="itemEdit.nameEn"></p-inputtext>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label>商品条形码</label>
                <p-inputtext v-model.trim="itemEdit.barcode"></p-inputtext>
            </div>
            <div class="field col">
                <label>发行时间<span style="color: red">*</span></label>
                <p-calendar v-model="itemEdit.releaseDate" date-format="yy/mm/dd"
                            :show-button-bar="true"
                            :show-icon="true"></p-calendar>
            </div>
            <div class="field col-2">
                <div class="col-12">
                    <label class="mb-3">特典</label>
                </div>
                <div class="col-12 mt-3">
                    <p-inputswitch v-model="itemEdit.hasBonus" :true-value="1"
                                   :false-value="0"></p-inputswitch>
                </div>
            </div>
        </div>
        <div class="formgrid grid">
            <div class="field col">
                <label class="mb-3">发售类型<span style="color: red">*</span></label>
                <p-dropdown v-model="itemEdit.releaseType" :options="releaseTypeSet"
                            option-label="labelZh" option-value="value">
                </p-dropdown>
            </div>
            <div class="field col">
                <label class="mb-3">发售平台<span style="color: red">*</span></label>
                <p-dropdown v-model="itemEdit.platform" :options="gamePlatformSet"
                            option-label="labelEn" option-value="value">
                </p-dropdown>
            </div>
            <div class="field col">
                <label class="mb-3">地区<span style="color: red">*</span></label>
                <p-dropdown v-model="itemEdit.region" :options="regionSet" :filter="true"
                            :show-clear="true" option-label="nameZh" option-value="code">
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
        </div>
        <div class="formgrid grid">
            <div class="field col-4">
                <label class="mb-3">所属系列<span style="color: red">*</span></label>
                <p-multiselect v-model="itemEdit.franchises" @change="getProducts($event)"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true">
                </p-multiselect>
            </div>
            <div class="field col-8">
                <label class="mb-3">所属作品<span style="color: red">*</span></label>
                <p-multiselect v-model="itemEdit.products" :options="productSet"
                               option-label="label" option-value="value" placeholder="选择所属作品"
                               display="chip" :filter="true" :disabled="productSelect">
                </p-multiselect>
            </div>
        </div>
        <div class="field">
            <label>备注</label>
            <p-textarea v-model="itemEdit.remark" rows="3" cols="20"
                        :auto-resize="true"></p-textarea>
        </div>
    </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                  @click="closeEditDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text"
                  @click="submitEditItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="deleteDialog" header="删除数据"
          :style="{width: '450px'}">
    <div class="confirmation-content">
        <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"></i>
        <span>确定删除所选游戏？</span>
    </div>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                  @click="deleteDialog = false" :disabled="editBlock"></p-button>
        <p-button label="确认删除" icon="pi pi-check" class="p-button-text"
                  @click="deleteSelectedItems" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
    `,
    mounted() {
        this.initData();
        this.init();
    },
    data() {
        return {
            //region common
            toast: useToast(),
            loading: false,
            dt: null,
            totalLoading: false,
            editAuth: null,
            //endregion

            //region search param
            totalRecords: 0,
            queryParams: {},
            filters: {
                'name': {value: ''},
                'hasBonus': {value: null},
                'region': {value: null},
                'platform': {value: null},
                'franchises': {value: null},
                'products': {value: null},
            },
            //endregion

            //region basic
            game: {},
            games: [],
            productSelect: true,
            productSet: [],
            franchiseSet: [],

            releaseTypeSet: [],
            gamePlatformSet: [],
            regionSet: [],
            columns: [
                {field: 'nameZh', header: '游戏名(中)'},
                {field: 'nameEn', header: '游戏名(英)'},
                {field: 'remark', header: '备注'},
                {field: 'addedTime', header: '收录时间'},
                {field: 'editedTime', header: '编辑时间'},
            ],
            //endregion

            //region edit
            itemEdit: {},
            editBlock: false,
            itemsStatus: null,
            statusOptions,
            selectedItems: null,
            selectedColumns: null,
            displayNewDialog: false,
            displayEditDialog: false,
            deleteDialog: false,
            //endregion

        }
    },
    methods: {
        //region common
        updateItemsStatus(value) {
            this.editBlock = true;
            let json = {
                entityType: ENTITY.GAME,
                items: this.selectedItems,
                status: value
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEMS_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                    this.editBlock = false;
                })

        },
        exportCSV() {
            HttpUtil.get(null, CHECK_USER_AUTHORITY_URL)
                .then(res => {
                    if (res.state === 1) {
                        this.$refs.dt.exportCSV();
                    }
                })
        },
        //初始化
        initData() {
            this.loading = true;
            let json = {
                entityType: ENTITY.GAME
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_LIST_INIT_DATA_URL, json)
                .then(res => {
                    this.editAuth = res.editAuth;
                    this.gamePlatformSet = res.gamePlatformSet;
                    this.releaseTypeSet = res.releaseTypeSet;
                    this.regionSet = res.regionSet;
                    this.franchiseSet = res.franchiseSet;
                    this.loading = false;
                    this.totalLoading = false;
                })
        },
        init() {
            this.loading = true;
            this.queryParams = {
                first: (this.queryParams !== {}?this.queryParams.first:0),
                rows: this.$refs.dt.rows,
                sortField: null,
                sortOrder: null,
                filters: this.filters
            };
            this.getGames();
            this.loading = false;
        },
        //endregion

        //region search
        onToggle(val) {
            this.selectedColumns = this.columns.filter(col => val.includes(col));
        },
        onPage(ev) {
            this.queryParams = ev;
            this.getGames();
        },
        onSort(ev) {
            this.queryParams = ev;
            this.getGames();
        },
        onFilter() {
            this.queryParams.filters = this.filters;
            this.queryParams.first = 0;
            this.getGames();
        },
        getGames() {
            let json = {
                pageLabel: "list",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_GAMES_URL, json)
                .then(res => {
                    this.games = res.data;
                    this.totalRecords = res.total;
                })
        },
        //endregion

        //region edit
        updateStatus(id, status) {
            let json = {
                entityType: ENTITY.GAME,
                entityId: id,
                status: !status
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEM_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                })
        },
        getNameByCode,
        //打开删除确认面板
        confirmDeleteSelected() {
            this.deleteDialog = true;
        },
        deleteSelectedItems() {
            this.editBlock = true;
            HttpUtil.deleteRequest(this.toast, DELETE_GAME_URL, this.selectedItems)
                .then(res => {
                    if (res.state === 1) {
                        this.deleteDialog = false;
                        this.selectedItems = null;
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //打开编辑数据面板
        openEditDialog(data) {
            let dataTmp = JSON.parse(JSON.stringify(data));
            this.itemEdit = dataTmp;
            this.itemEdit.releaseType = dataTmp.releaseType.id;
            this.itemEdit.region = dataTmp.region.code;
            this.itemEdit.platform = dataTmp.platform.id;
            this.itemEdit.hasBonus = dataTmp.hasBonus ? 1 : 0;
            this.itemEdit.franchises = label2value(dataTmp.franchises, this.franchiseSet).concat();
            let json = {
                franchises: this.itemEdit.franchises,
                entityType: ENTITY.GAME
            };
            HttpUtil.post(this.toast, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        if (res.data.length !== 0) {
                            this.productSet = res.data;
                            this.productSelect = false;
                        } else {
                            this.productSelect = true;
                        }
                        this.itemEdit.products = label2value(this.itemEdit.products, this.productSet).concat();
                        this.displayEditDialog = true;
                    }
                });
        },
        //关闭编辑数据面板
        closeEditDialog() {
            this.displayEditDialog = false;
            this.itemEdit = {};
        },
        //保存编辑数据
        submitEditItem() {
            this.editBlock = true;
            HttpUtil.commonVueSubmit(this.toast, UPDATE_GAME_URL, this.itemEdit)
                .then(res => {
                    if (res.state === 1) {
                        this.itemEdit = {};
                        this.closeEditDialog();
                        this.init();
                    }
                    this.editBlock = false;
                }).catch(e => {
                console.error(e);
            });
        },
        //打开新增数据面板
        openNewDialog() {
            this.game = {};
            this.productSelect = true;
            this.displayNewDialog = true;
        },
        //关闭新增数据面板
        closeNewDialog() {
            this.game = {};
            this.displayNewDialog = false;
        },
        //提交新增数据
        submitNewItem() {
            HttpUtil.commonVueSubmit(this.toast, INSERT_GAME_URL, this.game)
                .then(res => {
                    if (res.state === 1) {
                        this.game = {};
                        this.closeNewDialog();
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //根据系列id获取该系列所有作品
        getProducts(ev) {
            if (ev.value.length !== 0) {
                let json = {
                    franchises: ev.value,
                    entityType: ENTITY.GAME
                };
                HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                    .then(res => {
                        if (res.state === 1) {
                            if (res.data.length !== 0) {
                                this.productSet = res.data;
                                this.productSelect = false;
                            } else {
                                this.productSelect = true;
                                this.itemEdit.products = [];
                                this.game.products = [];
                            }
                        }
                    });
            } else {
                this.productSelect = true;
                this.itemEdit.products = [];
                this.game.products = [];
            }
        },
        //endregion

    },
    components: {
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-calendar": primevue.calendar,
        "p-inputnumber": primevue.inputnumber,
        "p-dialog": primevue.dialog,
        "p-textarea": primevue.textarea,
        "p-toolbar": primevue.toolbar,
        "p-toast": primevue.toast,
        "p-panel": primevue.panel,
        "p-divider": primevue.divider,
        "p-inputswitch": primevue.inputswitch,
        "p-multiselect": primevue.multiselect,
        "p-inputtext": primevue.inputtext,
        "p-button": primevue.button,
        "p-dropdown": primevue.dropdown,
        "p-tristatecheckbox": primevue.tristatecheckbox,
        "p-blockui": primevue.blockui,
        "p-selectbutton": primevue.selectbutton,
    }
}

const merchDbList = {
    template: `
        <p-toast></p-toast>
    <p-datatable ref="dt" :value="merchs" class=" p-datatable-sm" :always-show-paginator="merchs != 0"
                 :lazy="true" v-model:filters="filters" :total-records="totalRecords" :loading="loading"
                 @page="onPage($event)" @sort="onSort($event)" @filter="onFilter($event)"
                 filter-display="row"
                 :globalFilterFields="['name', 'nameZh', 'barcode']"
                 :paginator="true" :rows="10" striped-rows
                 :resizable-columns="true" column-resize-mode="expand"
                 v-model:selection="selectedItems" dataKey="id"
                 :scrollable="true" scroll-height="flex" :rows-per-page-options="[10,25,50]" show-gridlines
                 paginator-template="FirstPageLink PrevPageLink PageLinks NextPageLink
                             LastPageLink CurrentPageReport RowsPerPageDropdown"
                 current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据"
                 responsive-layout="scroll">
        <template #header>
            <p-blockui :blocked="editBlock" class="grid">
                <div class="col-9" v-if="editAuth > 1">
                    <p-button label="新增" icon="pi pi-plus" class="p-button-success p-button-sm mr-2"
                              @click="openNewDialog" style="width: 6em"></p-button>
                    <p-button label="删除" icon="pi pi-trash" class="p-button-danger p-button-sm mr-2" @click="confirmDeleteSelected"
                              :disabled="!selectedItems || !selectedItems.length" style="width: 6em"></p-button>
                    <p-selectbutton v-model="itemsStatus" :options="statusOptions" aria-labelledby="single"
                                  :unselectable="false" @change="updateItemsStatus($event.value)"
                                  :disabled="!selectedItems || !selectedItems.length" v-if="editAuth > 2"></p-selectbutton>
                    <p-button label="导出(CSV)" icon="pi pi-external-link" class="ml-2 p-button-help p-button-sm"
                              @click="exportCSV($event)" style="width: 8em"></p-button>
                </div>
                <div class="col-3">
                    <p-multiselect :model-value="selectedColumns" :options="columns" option-label="header"
                                   @update:model-value="onToggle" class=" text-end"
                                   placeholder="可选显示列" style="width: 20em"></p-multiselect>
                </div>
            </p-blockui>
        </template>
        <template #empty>
                <span class="emptyInfo">
                    未检索到符合条件的数据
                </span>
        </template>
        <template #loading>
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
                <span>加载中，别急~</span>
        <i class="pi pi-spin pi-spinner mr-2" style="font-size: 2rem"></i>
        <span>加载中，别急~</span>
    </template>
        <p-column selection-mode="multiple" style="flex: 0 0 3rem" :exportable="false" v-if="editAuth > 1"></p-column>
        <p-column header="序号" field="id" exportHeader="Album Id" :sortable="true" style="flex: 0 0 5rem">
            <template #body="slotProps" v-if="editAuth > 1">
                <p-button class="p-button-link" @click="openEditDialog(slotProps.data)">
                    {{slotProps.data.id}}
                </p-button>
            </template>
        </p-column>
        <p-column header="状态" field="status" :sortable="true" style="flex: 0 0 5rem" v-if="editAuth > 2">
                <template #body="{data}">
                    <p-button class="p-button-link" @click="updateStatus(data.id, data.status)" :disabled="editBlock">
                        <i class="pi" :class="{'true-icon pi-circle-fill': data.status, 'false-icon pi-circle-fill': !data.status}"></i>
                    </p-button>
                </template>
            </p-column>
        <p-column header="商品名" field="name" :show-filter-menu="false"
                  style="flex: 0 0 10rem">
            <template #body="slotProps">
                <a :href="'/db/merch/' + slotProps.data.id">
                    {{slotProps.data.name}}
                </a>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                ></p-inputtext>
            </template>
        </p-column>
        <p-column header="商品条形码" field="barcode" :show-filter-menu="false"
                  style="flex: 0 0 10rem">
            <template #filter="{filterModel,filterCallback}">
                <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"
                ></p-inputtext>
            </template>
        </p-column>
        <p-column header="商品类型" field="category" :show-filter-menu="false" style="flex: 0 0 9rem">
            <template #body="slotProps">
                {{slotProps.data.category.nameZh}}
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-dropdown v-model="filterModel.value" @change="filterCallback()"
                            :options="merchCategorySet" option-label="label" option-value="value"
                            style="width: 8rem" placeholder="类型" >
                </p-dropdown>
            </template>
        </p-column>
        <p-column header="地区" field="region" :show-filter-menu="false" style="flex: 0 0 9rem">
            <template #body="slotProps">
                <span :class="'fi fi-' + slotProps.data.region.code"></span>  ({{slotProps.data.region.nameZh}})
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-dropdown v-model="filterModel.value" @change="filterCallback()"
                            :options="regionSet" option-label="nameZh" option-value="code"
                            style="width: 8rem" >
                    <template #value="slotProps">
                        <div class="country-item" v-if="slotProps.value">
                            <span :class="'fi fi-' + slotProps.value"></span>
                            <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            </template>
        </p-column>
        <p-column header="发售时间" field="releaseDate" :sortable="true" style="flex: 0 0 7rem"></p-column>
        <p-column header="发售价格" field="price" :sortable="true" style="flex: 0 0 7rem">
            <template #body="slotProps">
                {{slotProps.data.price}} {{slotProps.data.currencyUnit}}
            </template>
        </p-column>
        <p-column header="非卖品" field="notForSale" data-type="boolean" body-class="text-center"
                  style="flex: 0 0 4rem">
            <template #body="{data}">
                <i class="pi" :class="{'true-icon pi-check-circle': data.notForSale, 'false-icon pi-times-circle': !data.notForSale}"></i>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-tristatecheckbox v-model="filterModel.value" @change="filterCallback()"></p-tristatecheckbox>
            </template>
        </p-column>
        <p-column header="所属系列" field="franchises" :show-filter-menu="false" style="flex: 0 0 10rem">
            <template #body="slotProps">
                <ul class="px-4">
                    <li v-for="franchise in slotProps.data.franchises">
                        <a :href="'/db/franchise/' + franchise.value ">
                            {{franchise.label}}
                        </a>
                    </li>
                </ul>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="getProducts($event), filterCallback()"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true"
                               style="width: 9rem">
                </p-multiselect>
            </template>

        </p-column>
        <p-column header="所属作品" filter-field="products" :show-filter-menu="false"
                  style="flex: 0 0 16rem">
            <template #body="slotProps">
                <ul class="px-4">
                    <li v-for="data in slotProps.data.products">
                        <a :href="'/db/product/' + data.value">
                            {{data.label}}
                        </a>
                    </li>
                </ul>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="filterCallback()"
                               :options="productSet" placeholder="选择作品"
                               option-label="label" option-value="value" display="chip" :filter="true"
                               :disabled="productSelect" style="width: 15rem" >
                </p-multiselect>
            </template>
        </p-column>
        <p-column v-for="(col, index) of selectedColumns" :field="col.field"
                  :header="col.header" :key="col.field + '_' + index" :sortable="true">
        </p-column>
    </p-datatable>
    <p-dialog :modal="true" v-model:visible="displayNewDialog" header="新增数据"
              :style="{width: '600px'}" class="p-fluid">
              <p-blockui :blocked="editBlock">
        <p-panel header="基础信息">
            <div class="formgrid grid">
                <div class="field col">
                    <label>商品名<span style="color: red">*</span></label>
                    <p-inputtext v-model.trim="merch.name"></p-inputtext>
                </div>
                <div class="field col">
                    <label>商品名(中)</label>
                    <p-inputtext v-model.trim="merch.nameZh"></p-inputtext>
                </div>
            </div>
            <div class="formgrid grid">
                <div class="field col">
                    <label>商品名(英)</label>
                    <p-inputtext v-model.trim="merch.nameEn"></p-inputtext>
                </div>
                <div class="field col">
                    <label>商品条形码</label>
                    <p-inputtext v-model.trim="merch.barcode"></p-inputtext>
                </div>
            </div>
            <div class="formgrid grid">
                <div class="field col">
                    <label>商品分类<span style="color: red">*</span></label>
                    <p-dropdown v-model="merch.category" :options="merchCategorySet"
                                option-label="label" option-value="value">
                    </p-dropdown>
                </div>
                <div class="field col">
                    <label>地区<span style="color: red">*</span></label>
                    <p-dropdown v-model="merch.region" :options="regionSet" :filter="true"
                                :show-clear="true" option-label="nameZh" option-value="code">
                        <template #value="slotProps">
                            <div class="country-item" v-if="slotProps.value">
                                <span :class="'fi fi-' + slotProps.value"></span>
                                <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            </div>
            <div class="formgrid grid">
                <div class="field col-4">
                    <label class="mb-3">所属系列<span style="color: red">*</span></label>
                    <p-multiselect v-model="merch.franchises" @change="getProducts($event)"
                                   :options="franchiseSet" placeholder="所属系列"
                                   option-label="label" option-value="value" display="chip" :filter="true">
                    </p-multiselect>
                </div>
                <div class="field col-8">
                    <label class="mb-3">所属作品<span style="color: red">*</span></label>
                    <p-multiselect v-model="merch.products" :options="productSet"
                                   option-label="label" option-value="value" placeholder="选择所属作品"
                                   display="chip" :filter="true" :disabled="productSelect">
                    </p-multiselect>
                </div>
            </div>
            <div class="formgrid grid">
                <div class="field col">
                    <label>发售时间<span style="color: red">*</span></label>
                    <p-calendar v-model="merch.releaseDate" date-format="yy/mm/dd"
                                :show-button-bar="true"
                                :show-icon="true"></p-calendar>
                </div>
                <div class="field col-3">
                    <label>发售价格</label>
                    <p-inputnumber v-model="merch.price"></p-inputnumber>
                </div>
                <div class="field col-3">
                    <label>单位</label>
                    <p-dropdown v-model="merch.currencyUnit" :options="currencyUnitSet"
                                option-label="label" option-value="value" placeholder="单位">
                    </p-dropdown>
                </div>
                <div class="field col-2">
                    <div class="col-12">
                        <label class="mb-3">非卖品</label>
                    </div>
                    <div class="col-12 mt-3">
                        <p-inputswitch v-model="merch.notForSale" :true-value="1"
                                       :false-value="0"></p-inputswitch>
                    </div>
                </div>
            </div>
            <div class="field">
                <label>备注</label>
                <p-textarea v-model="itemEdit.remark" rows="3" cols="20"
                            :auto-resize="true"></p-textarea>
            </div>
        </p-panel>
        </p-blockui>
        <template #footer>
            <p-button label="取消" icon="pi pi-times" class="p-button-text"
             @click="closeNewDialog" :disabled="editBlock"></p-button>
            <p-button label="保存" icon="pi pi-check" class="p-button-text"
             @click="submitNewItem" :disabled="editBlock"></p-button>
        </template>
    </p-dialog>
    <p-dialog :modal="true" v-model:visible="displayEditDialog" header="编辑数据"
              :style="{width: '600px'}" class="p-fluid">
              <p-blockui :blocked="editBlock">
        <p-panel header="基础信息">
            <div class="formgrid grid">
                <div class="field col">
                    <label>商品名<span style="color: red">*</span></label>
                    <p-inputtext v-model.trim="itemEdit.name"></p-inputtext>
                </div>
                <div class="field col">
                    <label>商品名(中)</label>
                    <p-inputtext v-model.trim="itemEdit.nameZh"></p-inputtext>
                </div>
            </div>
            <div class="formgrid grid">
                <div class="field col">
                    <label>商品名(英)</label>
                    <p-inputtext v-model.trim="itemEdit.nameEn"></p-inputtext>
                </div>
                <div class="field col">
                    <label>商品条形码</label>
                    <p-inputtext v-model.trim="itemEdit.barcode"></p-inputtext>
                </div>
            </div>
            <div class="formgrid grid">
                <div class="field col">
                    <label>商品分类<span style="color: red">*</span></label>
                    <p-dropdown v-model="itemEdit.category" :options="merchCategorySet"
                                option-label="label" option-value="value">
                    </p-dropdown>
                </div>
                <div class="field col">
                    <label>地区<span style="color: red">*</span></label>
                    <p-dropdown v-model="itemEdit.region" :options="regionSet" :filter="true"
                                :show-clear="true" option-label="nameZh" option-value="code">
                        <template #value="slotProps">
                            <div class="country-item" v-if="slotProps.value">
                                <span :class="'fi fi-' + slotProps.value"></span>
                                <div class="ml-2">{{getNameByCode(slotProps.value, regionSet)}}</div>
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
            </div>
            <div class="formgrid grid">
                <div class="field col-4">
                    <label class="mb-3">所属系列<span style="color: red">*</span></label>
                    <p-multiselect v-model="itemEdit.franchises" @change="getProducts($event)"
                                   :options="franchiseSet" placeholder="所属系列"
                                   option-label="label" option-value="value" display="chip" :filter="true">
                    </p-multiselect>
                </div>
                <div class="field col-8">
                    <label class="mb-3">所属作品<span style="color: red">*</span></label>
                    <p-multiselect v-model="itemEdit.products" :options="productSet"
                                   option-label="label" option-value="value" placeholder="选择所属作品"
                                   display="chip" :filter="true" :disabled="productSelect">
                    </p-multiselect>
                </div>
            </div>
            <div class="formgrid grid">
                <div class="field col">
                    <label>发售时间<span style="color: red">*</span></label>
                    <p-calendar v-model="itemEdit.releaseDate" date-format="yy/mm/dd"
                                :show-button-bar="true"
                                :show-icon="true"></p-calendar>
                </div>
                <div class="field col-3">
                    <label>发售价格</label>
                    <p-inputnumber v-model="itemEdit.price"></p-inputnumber>
                </div>
                <div class="field col-3">
                    <label>单位</label>
                    <p-dropdown v-model="itemEdit.currencyUnit" :options="currencyUnitSet"
                                option-label="label" option-value="value" placeholder="单位">
                    </p-dropdown>
                </div>
                <div class="field col-2">
                    <div class="col-12">
                        <label class="mb-3">非卖品</label>
                    </div>
                    <div class="col-12 mt-3">
                        <p-inputswitch v-model="itemEdit.notForSale" :true-value="1"
                                       :false-value="0"></p-inputswitch>
                    </div>
                </div>
            </div>
            <div class="field">
                <label>备注</label>
                <p-textarea v-model="itemEdit.remark" rows="3" cols="20"
                            :auto-resize="true"></p-textarea>
            </div>
        </p-panel>
        </p-blockui>
        <template #footer>
            <p-button label="取消" icon="pi pi-times" class="p-button-text"
                      @click="closeEditDialog" :disabled="editBlock"></p-button>
            <p-button label="保存" icon="pi pi-check" class="p-button-text"
                      @click="submitEditItem" :disabled="editBlock"></p-button>
        </template>
    </p-dialog>
    <p-dialog :modal="true" v-model:visible="deleteDialog" header="删除数据"
              :style="{width: '450px'}">
        <div class="confirmation-content">
            <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"></i>
            <span>确定删除所选周边商品？</span>
        </div>
        <template #footer>
            <p-button label="取消" icon="pi pi-times" class="p-button-text"
                      @click="deleteDialog = false" :disabled="editBlock"></p-button>
            <p-button label="确认删除" icon="pi pi-check" class="p-button-text"
                      @click="deleteSelectedItems" :disabled="editBlock"></p-button>
        </template>
    </p-dialog>
    `,
    mounted() {
        this.initData();
        this.init();
    },
    data() {
        return {
            //region common
            toast: useToast(),
            loading: false,
            dt: null,
            totalLoading: false,
            editAuth: null,
            //endregion

            //region search param
            totalRecords: 0,
            queryParams: {},
            filters: {
                'name': {value: ''},
                'barcode': {value: ''},
                'region': {value: ''},
                'category': {value: null},
                'franchises': {value: null},
                'products': {value: null},
                'notForSale': {value: null},
            },
            //endregion

            //region basic
            merch: {},
            merchs: [],
            productSelect: true,
            productSet: [],
            franchiseSet: [],

            currencyUnitSet,
            merchCategorySet: [],
            regionSet: [],
            columns: [
                {field: 'nameZh', header: '商品名(中)'},
                {field: 'nameEn', header: '商品名(英)'},
                {field: 'remark', header: '备注'},
                {field: 'addedTime', header: '收录时间'},
                {field: 'editedTime', header: '编辑时间'},
            ],
            //endregion

            //region edit
            itemEdit: {},
            editBlock: false,
            itemsStatus: null,
            statusOptions,
            selectedItems: null,
            selectedColumns: null,
            displayNewDialog: false,
            displayEditDialog: false,
            deleteDialog: false,
            //endregion

        }
    },
    methods: {
        //region common
        updateItemsStatus(value) {
            this.editBlock = true;
            let json = {
                entityType: ENTITY.MERCH,
                items: this.selectedItems,
                status: value
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEMS_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                    this.editBlock = false;
                })

        },
        exportCSV() {
            HttpUtil.get(null, CHECK_USER_AUTHORITY_URL)
                .then(res => {
                    if (res.state === 1) {
                        this.$refs.dt.exportCSV();
                    }
                })
        },
        //初始化
        initData() {
            this.loading = true;
            let json = {
                entityType: ENTITY.MERCH
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_LIST_INIT_DATA_URL, json)
                .then(res => {
                    this.editAuth = res.editAuth;
                    this.merchCategorySet = res.merchCategorySet;
                    this.regionSet = res.regionSet;
                    this.franchiseSet = res.franchiseSet;
                    this.loading = false;
                    this.totalLoading = false;
                })
        },
        init() {
            this.loading = true;
            this.queryParams = {
                first: (this.queryParams !== {}?this.queryParams.first:0),
                rows: this.$refs.dt.rows,
                sortField: null,
                sortOrder: null,
                filters: this.filters
            };
            this.getMerchs();
            this.loading = false;
        },
        //endregion

        //region search
        onToggle(val) {
            this.selectedColumns = this.columns.filter(col => val.includes(col));
        },
        onPage(ev) {
            this.queryParams = ev;
            this.getMerchs();
        },
        onSort(ev) {
            this.queryParams = ev;
            this.getMerchs();
        },
        onFilter() {
            this.queryParams.filters = this.filters;
            this.queryParams.first = 0;
            this.getMerchs();
        },
        getMerchs() {
            let json = {
                pageLabel: "list",
                queryParams: this.queryParams
            }
            HttpUtil.post(null, GET_MERCHS_URL, json)
                .then(res => {
                    this.merchs = res.data;
                    this.totalRecords = res.total;
                })
        },
        //endregion

        //region edit
        updateStatus(id, status) {
            let json = {
                entityType: ENTITY.MERCH,
                entityId: id,
                status: !status
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEM_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                })
        },
        getNameByCode,
        //打开删除确认面板
        confirmDeleteSelected() {
            this.deleteDialog = true;
        },
        deleteSelectedItems() {
            this.editBlock = true;
            HttpUtil.deleteRequest(this.toast, DELETE_MERCH_URL, this.selectedItems)
                .then(res => {
                    if (res.state === 1) {
                        this.deleteDialog = false;
                        this.selectedItems = null;
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //打开编辑数据面板
        openEditDialog(data) {
            let dataTmp = JSON.parse(JSON.stringify(data));
            this.itemEdit = dataTmp;
            this.itemEdit.category = dataTmp.category.id;
            this.itemEdit.region = dataTmp.region.code;
            this.itemEdit.notForSale = dataTmp.notForSale ? 1 : 0;
            this.itemEdit.franchises = label2value(dataTmp.franchises, this.franchiseSet).concat();
            let json = {
                franchises: this.itemEdit.franchises,
                entityType: ENTITY.MERCH
            };
            HttpUtil.post(this.toast, GET_PRODUCT_SET_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        if (res.data.length !== 0) {
                            this.productSet = res.data;
                            this.productSelect = false;
                        } else {
                            this.productSelect = true;
                        }
                        this.itemEdit.products = label2value(this.itemEdit.products, this.productSet).concat();
                        this.displayEditDialog = true;
                    }
                });
        },
        //关闭编辑数据面板
        closeEditDialog() {
            this.displayEditDialog = false;
            this.itemEdit = {};
        },
        //保存编辑数据
        submitEditItem() {
            this.editBlock = true;
            HttpUtil.commonVueSubmit(this.toast, UPDATE_MERCH_URL, this.itemEdit)
                .then(res => {
                    if (res.state === 1) {
                        this.itemEdit = {};
                        this.closeEditDialog();
                        this.init();
                    }
                    this.editBlock = false;
                }).catch(e => {
                console.error(e);
            });
        },
        //打开新增数据面板
        openNewDialog() {
            this.merch = {};
            this.productSelect = true;
            this.displayNewDialog = true;
        },
        //关闭新增数据面板
        closeNewDialog() {
            this.merch = {};
            this.displayNewDialog = false;
        },
        //提交新增数据
        submitNewItem() {
            HttpUtil.commonVueSubmit(this.toast, INSERT_MERCH_URL, this.merch)
                .then(res => {
                    if (res.state === 1) {
                        this.merch = {};
                        this.closeNewDialog();
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //根据系列id获取该系列所有作品
        getProducts(ev) {
            if (ev.value.length !== 0) {
                let json = {
                    franchises: ev.value,
                    entityType: ENTITY.MERCH
                };
                HttpUtil.post(null, GET_PRODUCT_SET_URL, json)
                    .then(res => {
                        if (res.state === 1) {
                            if (res.data.length !== 0) {
                                this.productSet = res.data;
                                this.productSelect = false;
                            } else {
                                this.productSelect = true;
                                this.itemEdit.products = [];
                                this.merch.products = [];
                            }
                        }
                    });
            } else {
                this.productSelect = true;
                this.itemEdit.products = [];
                this.merch.products = [];
            }
        },
        //endregion

    },
    components: {
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-calendar": primevue.calendar,
        "p-inputnumber": primevue.inputnumber,
        "p-dialog": primevue.dialog,
        "p-textarea": primevue.textarea,
        "p-toolbar": primevue.toolbar,
        "p-toast": primevue.toast,
        "p-panel": primevue.panel,
        "p-divider": primevue.divider,
        "p-inputswitch": primevue.inputswitch,
        "p-multiselect": primevue.multiselect,
        "p-inputtext": primevue.inputtext,
        "p-button": primevue.button,
        "p-dropdown": primevue.dropdown,
        "p-tristatecheckbox": primevue.tristatecheckbox,
        "p-blockui": primevue.blockui,
        "p-selectbutton": primevue.selectbutton,
    }
}

const productDbList = {
    template: `
        <p-toast></p-toast>
    <p-datatable ref="dt" :value="products" class=" p-datatable-sm" :always-show-paginator="products != 0"
                 :lazy="true" v-model:filters="filters" :total-records="totalRecords" :loading="loading"
                 @page="onPage($event)" @sort="onSort($event)" @filter="onFilter($event)"
                 v-model:selection="selectedItems"
                 filter-display="row" :globalFilterFields="['name','nameZh']" dataKey="id"
                 :paginator="true" :rows="10" striped-rows :resizable-columns="true" column-resize-mode="expand"
                 :scrollable="true" scroll-height="flex" :rows-per-page-options="[10,25,50]" show-gridlines
                 paginator-template="FirstPageLink PrevPageLink PageLinks NextPageLink
                             LastPageLink CurrentPageReport RowsPerPageDropdown"
                 current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据"
                 responsive-layout="scroll">
        <template #header>
            <p-blockui :blocked="editBlock" class="grid">
                <div class="col-9" v-if="editAuth > 1">
                    <p-button label="新增" icon="pi pi-plus" class="p-button-success p-button-sm mr-2"
                              @click="openNewDialog" style="width: 6em"></p-button>
                    <p-selectbutton v-model="itemsStatus" :options="statusOptions" aria-labelledby="single"
                                  :unselectable="false" @change="updateItemsStatus($event.value)"
                                  :disabled="!selectedItems || !selectedItems.length" v-if="editAuth > 2"></p-selectbutton>
                    <p-button label="导出(CSV)" icon="pi pi-external-link" class="ml-2 p-button-help p-button-sm"
                              @click="exportCSV($event)" style="width: 8em"></p-button>
                </div>
                <div class="col-3">
                    <p-multiselect :model-value="selectedColumns" :options="columns" option-label="header"
                                   @update:model-value="onToggle" class=" text-end"
                                   placeholder="可选显示列" style="width: 20em"></p-multiselect>
                </div>
            </p-blockui>
        </template>
        <template #empty>
            <span class="emptyInfo">
                未检索到符合条件的数据
            </span>
        </template>
        <template #loading>
            <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
                    <span>加载中，别急~</span>
            <i class="pi pi-spin pi-spinner mr-2" style="font-size: 2rem"></i>
        <span>加载中，别急~</span>
    </template>
        <p-column selection-mode="multiple" style="flex: 0 0 3rem" :exportable="false" v-if="editAuth > 1"></p-column>
        <p-column header="序号" field="id" exportHeader="Product Id" :sortable="true" style="flex: 0 0 5rem">
            <template #body="slotProps" v-if="editAuth > 2">
                <p-button class="p-button-link" @click="openEditDialog(slotProps.data)">
                    {{slotProps.data.id}}
                </p-button>
            </template>
        </p-column>
        <p-column header="状态" field="status" :sortable="true" style="flex: 0 0 5rem" v-if="editAuth > 2">
                <template #body="{data}">
                    <p-button class="p-button-link" @click="updateStatus(data.id, data.status)" :disabled="editBlock">
                        <i class="pi" :class="{'true-icon pi-circle-fill': data.status, 'false-icon pi-circle-fill': !data.status}"></i>
                    </p-button>
                </template>
            </p-column>
        <p-column header="作品原名" field="name" :show-filter-menu="false" style="flex: 0 0 20rem">
            <template #body="slotProps">
                <a :href="'/db/product/' + slotProps.data.id">
                    {{slotProps.data.name}}
                </a>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
            </template>
        </p-column>
        <p-column header="作品名称(中)" field="nameZh" :show-filter-menu="false" style="flex: 0 0 15rem">
            <template #filter="{filterModel,filterCallback}">
                <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
            </template>
        </p-column>
        <p-column header="发行时间" field="releaseDate" :sortable="true" style="flex: 0 0 7rem"></p-column>
        <p-column header="作品分类" field="category" :show-filter-menu="false" style="flex: 0 0 9rem">
            <template #body="slotProps">
                {{slotProps.data.category.nameZh}}
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="filterCallback()"
                               :options="productCategorySet" option-label="label" option-value="value"
                               display="chip" :filter="true">
                </p-multiselect>
            </template>
        </p-column>
        <p-column header="所属系列" field="franchise" :show-filter-menu="false" style="flex: 0 0 10rem">
            <template #body="slotProps">
                <a :href="'/db/franchise/' + slotProps.data.franchise.value">
                    {{slotProps.data.franchise.label}}
                </a>
            </template>
            <template #filter="{filterModel,filterCallback}">
                <p-multiselect v-model="filterModel.value" @change="filterCallback()"
                               :options="franchiseSet" placeholder="所属系列"
                               option-label="label" option-value="value" display="chip" :filter="true"
                               style="width: 9rem">
                </p-multiselect>
            </template>
        </p-column>
        <p-column v-for="(col, index) of selectedColumns" :field="col.field"
                  :header="col.header" :key="col.field + '_' + index">
        </p-column>
    </p-datatable>
    <p-dialog :modal="true" v-model:visible="displayNewDialog" :style="{width: '600px'}" header="新增数据"
              class="p-fluid">
        <p-blockui :blocked="editBlock">
            <p-panel header="基础信息">
                <div class="formgrid grid">
                    <div class="field col">
                        <label>作品名称<span style="color: red">*</span></label>
                        <p-inputtext v-model.trim="product.name"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>作品名称(中文)<span style="color: red">*</span></label>
                        <p-inputtext v-model.trim="product.nameZh"></p-inputtext>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col">
                        <label>作品名称(英语)</label>
                        <p-inputtext v-model.trim="product.nameEn"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>发行时间<span style="color: red">*</span></label>
                        <p-calendar v-model="product.releaseDate" date-format="yy/mm/dd"
                                    :show-button-bar="true" :show-icon="true"></p-calendar>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col">
                        <label class="mb-3">作品分类<span style="color: red">*</span></label>
                        <p-dropdown v-model="product.category" :options="productCategorySet"
                                    placeholder="选择作品分类" option-label="label" option-value="value">
                        </p-dropdown>

                    </div>
                    <div class="field col">
                        <label class="mb-3">所属系列<span style="color: red">*</span></label>
                        <p-dropdown v-model="product.franchise" :options="franchiseSet"
                                    placeholder="选择所属系列" option-label="label" option-value="value">
                        </p-dropdown>
                    </div>
                </div>
                <div class="field">
                    <label>备注</label>
                    <p-textarea v-model="product.remark" rows="3" cols="20" :auto-resize="true"></p-textarea>
                </div>
            </p-panel>
        </p-blockui>
        <template #footer>
            <p-button label="取消" icon="pi pi-times" class="p-button-text"
                      @click="closeNewDialog" :disabled="editBlock"></p-button>
            <p-button label="保存" icon="pi pi-check" class="p-button-text"
                      @click="submitNewItem" :disabled="editBlock"></p-button>
        </template>
    </p-dialog>
    <p-dialog :modal="true" v-model:visible="displayEditDialog" :style="{width: '600px'}" header="编辑数据"
              class="p-fluid">
        <p-blockui :blocked="editBlock">
            <p-panel header="基础信息">
                <div class="formgrid grid">
                    <div class="field col">
                        <label>作品名称<span style="color: red">*</span></label>
                        <p-inputtext v-model.trim="itemEdit.name"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>作品名称(中文)<span style="color: red">*</span></label>
                        <p-inputtext v-model.trim="itemEdit.nameZh"></p-inputtext>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col">
                        <label>作品名称(英语)</label>
                        <p-inputtext v-model.trim="itemEdit.nameEn"></p-inputtext>
                    </div>
                    <div class="field col">
                        <label>发行时间<span style="color: red">*</span></label>
                        <p-calendar v-model="itemEdit.releaseDate" date-format="yy/mm/dd"
                                    :show-button-bar="true" :show-icon="true"></p-calendar>
                    </div>
                </div>
                <div class="formgrid grid">
                    <div class="field col">
                        <label class="mb-3">作品分类<span style="color: red">*</span></label>
                        <p-dropdown v-model="itemEdit.category" :options="productCategorySet"
                                    placeholder="选择作品分类" option-label="label" option-value="value">
                        </p-dropdown>
    
                    </div>
                    <div class="field col">
                        <label class="mb-3">所属系列<span style="color: red">*</span></label>
                        <p-dropdown v-model="itemEdit.franchise" :options="franchiseSet"
                                    placeholder="选择所属系列" option-label="label" option-value="value">
                        </p-dropdown>
                    </div>
                </div>
                <div class="field">
                    <label>备注</label>
                    <p-textarea v-model="itemEdit.remark" rows="3" cols="20" :auto-resize="true"></p-textarea>
                </div>
            </p-panel>
        </p-blockui>
        <template #footer>
            <p-button label="取消" icon="pi pi-times" class="p-button-text"
                      @click="closeEditDialog" :disabled="editBlock"></p-button>
            <p-button label="保存" icon="pi pi-check" class="p-button-text"
                      @click="submitEditItem" :disabled="editBlock"></p-button>
        </template>
    </p-dialog>
    `,
    mounted() {
        this.initData();
        this.init();
    },
    data() {
        return {
            //region common
            toast: useToast(),
            loading: false,
            dt: null,
            totalLoading: false,
            editAuth: null,
            //endregion

            //region search param
            totalRecords: 0,
            queryParams: {},
            filters: {
                'name': {value: ''},
                'nameZh': {value: ''},
                'franchise': {value: null},
                'category': {value: null},
            },
            //endregion

            //region basic
            product: {},
            products: [],
            franchiseSet: [],
            selectedItems: null,
            productCategorySet: [],
            columns: [
                {field: 'nameEn', header: '作品名称(英文)'},
                {field: 'remark', header: '备注'},
                {field: 'addedTime', header: '收录时间'},
                {field: 'editedTime', header: '编辑时间'},
            ],
            //endregion

            //region edit
            itemEdit: {},
            editBlock: false,
            itemsStatus: null,
            statusOptions,
            selectedColumns: null,
            displayNewDialog: false,
            displayEditDialog: false,
            //endregion

        }
    },
    methods: {
        //region common
        updateItemsStatus(value) {
            this.editBlock = true;
            let json = {
                entityType: ENTITY.PRODUCT,
                items: this.selectedItems,
                status: value
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEMS_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                    this.editBlock = false;
                })

        },
        exportCSV() {
            HttpUtil.get(null, CHECK_USER_AUTHORITY_URL)
                .then(res => {
                    if (res.state === 1) {
                        this.$refs.dt.exportCSV();
                    }
                })
        },
        //初始化
        initData() {
            this.loading = true;
            let json = {
                entityType: ENTITY.PRODUCT
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_LIST_INIT_DATA_URL, json)
                .then(res => {
                    this.editAuth = res.editAuth;
                    this.productCategorySet = res.productCategorySet;
                    this.franchiseSet = res.franchiseSet;
                    this.loading = false;
                    this.totalLoading = false;
                })
        },
        init() {
            this.loading = true;
            this.queryParams = {
                first: (this.queryParams !== {}?this.queryParams.first:0),
                rows: this.$refs.dt.rows,
                sortField: null,
                sortOrder: null,
                filters: this.filters
            };
            this.getProducts();
            this.loading = false;
        },
        //endregion

        //region search
        onToggle(val) {
            this.selectedColumns = this.columns.filter(col => val.includes(col));
        },
        onPage(ev) {
            this.queryParams = ev;
            this.getProducts();
        },
        onSort(ev) {
            this.queryParams = ev;
            this.getProducts();
        },
        onFilter() {
            this.queryParams.filters = this.filters;
            this.queryParams.first = 0;
            this.getProducts();
        },
        getProducts() {
            HttpUtil.post(null, GET_PRODUCTS_URL, {queryParams: this.queryParams})
                .then(res => {
                    this.products = res.data;
                    this.totalRecords = res.total;
                })
        },
        //endregion

        //region edit
        updateStatus(id, status) {
            let json = {
                entityType: ENTITY.PRODUCT,
                entityId: id,
                status: !status
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEM_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                })
        },
        //打开编辑数据面板
        openEditDialog(data) {
            let dataTmp = JSON.parse(JSON.stringify(data));
            this.itemEdit = dataTmp;
            this.itemEdit.franchise = dataTmp.franchise.value;
            this.itemEdit.category = dataTmp.category.id;
            this.displayEditDialog = true;
        },
        //关闭编辑数据面板
        closeEditDialog() {
            this.displayEditDialog = false;
            this.itemEdit = {};
        },
        //保存编辑数据
        submitEditItem() {
            this.editBlock = true;
            HttpUtil.commonVueSubmit(this.toast, UPDATE_PRODUCT_URL, this.itemEdit)
                .then(res => {
                    if (res.state === 1) {
                        this.itemEdit = {};
                        this.closeEditDialog();
                        this.init();
                    }
                    this.editBlock = false;
                }).catch(e => {
                console.error(e);
            });
        },
        //打开新增数据面板
        openNewDialog() {
            this.product = {};
            this.displayNewDialog = true;
        },
        //关闭新增数据面板
        closeNewDialog() {
            this.product = {};
            this.displayNewDialog = false;
        },
        //提交新增数据
        submitNewItem() {
            HttpUtil.commonVueSubmit(this.toast, ADD_PRODUCT_URL, this.product)
                .then(res => {
                    if (res.state === 1) {
                        this.product = {};
                        this.closeNewDialog();
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //endregion

    },
    components: {
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-calendar": primevue.calendar,
        "p-inputnumber": primevue.inputnumber,
        "p-dialog": primevue.dialog,
        "p-textarea": primevue.textarea,
        "p-toolbar": primevue.toolbar,
        "p-toast": primevue.toast,
        "p-panel": primevue.panel,
        "p-divider": primevue.divider,
        "p-inputswitch": primevue.inputswitch,
        "p-multiselect": primevue.multiselect,
        "p-inputtext": primevue.inputtext,
        "p-button": primevue.button,
        "p-dropdown": primevue.dropdown,
        "p-tristatecheckbox": primevue.tristatecheckbox,
        "p-blockui": primevue.blockui,
        "p-selectbutton": primevue.selectbutton,
    }
}

const franchiseDbList = {
    template: `
        <p-toast></p-toast>
<p-datatable ref="dt" :value="franchises" class=" p-datatable-sm" :always-show-paginator="franchises != 0"
                :lazy="true" v-model:filters="filters" :total-records="totalRecords" :loading="loading"
                @page="onPage($event)" @sort="onSort($event)" @filter="onFilter($event)"
                v-model:selection="selectedItems"
                filter-display="row" :globalFilterFields="['name','nameZh']" dataKey="id"
                :paginator="true" :rows="10" striped-rows :resizable-columns="true" column-resize-mode="expand"
                :scrollable="true" scroll-height="flex" :rows-per-page-options="[10,25,50]" show-gridlines
                paginator-template="FirstPageLink PrevPageLink PageLinks NextPageLink
                                LastPageLink CurrentPageReport RowsPerPageDropdown"
                current-page-report-template="当前显示第【{first}】至【{last}】条数据，总【{totalRecords}】条数据"
                responsive-layout="scroll">
    <template #header>
        <p-blockui :blocked="editBlock" class="grid">
            <div class="col-9" v-if="editAuth > 1">
                <p-button label="新增" icon="pi pi-plus" class="p-button-success p-button-sm mr-2"
                            @click="openNewDialog" style="width: 6em"></p-button>
                <p-selectbutton v-model="itemsStatus" :options="statusOptions" aria-labelledby="single"
                                  :unselectable="false" @change="updateItemsStatus($event.value)"
                                  :disabled="!selectedItems || !selectedItems.length" v-if="editAuth > 2"></p-selectbutton>
                <p-button label="导出(CSV)" icon="pi pi-external-link" class="ml-2 p-button-help p-button-sm"
                            @click="exportCSV($event)" style="width: 8em"></p-button>
            </div>
            <div class="col-3">
                <p-multiselect :model-value="selectedColumns" :options="columns" option-label="header"
                                @update:model-value="onToggle" class=" text-end"
                                placeholder="可选显示列" style="width: 20em"></p-multiselect>
            </div>
        </p-blockui>
    </template>
    <template #empty>
            <span class="emptyInfo">
                未检索到符合条件的数据
            </span>
    </template>
    <template #loading>
        <i class="pi pi-spin pi-spinner" style="font-size: 2rem"></i>
                <span>加载中，别急~</span>
        <i class="pi pi-spin pi-spinner mr-2" style="font-size: 2rem"></i>
        <span>加载中，别急~</span>
    </template>
    <p-column selection-mode="multiple" style="flex: 0 0 3rem" :exportable="false" v-if="editAuth > 1"></p-column>
    <p-column header="序号" field="id" exportHeader="Franchise Id" :sortable="true" style="flex: 0 0 5rem">
        <template #body="slotProps" v-if="editAuth > 2">
            <p-button class="p-button-link" @click="openEditDialog(slotProps.data)">
                {{slotProps.data.id}}
            </p-button>
        </template>
    </p-column>
    <p-column header="状态" field="status" :sortable="true" style="flex: 0 0 5rem" v-if="editAuth > 2">
                <template #body="{data}">
                    <p-button class="p-button-link" @click="updateStatus(data.id, data.status)" :disabled="editBlock">
                        <i class="pi" :class="{'true-icon pi-circle-fill': data.status, 'false-icon pi-circle-fill': !data.status}"></i>
                    </p-button>
                </template>
            </p-column>
    <p-column header="名称" field="name" :show-filter-menu="false" style="flex: 0 0 15rem">
        <template #body="slotProps">
            <a :href="'/db/franchise/' + slotProps.data.id">
                {{slotProps.data.name}}
            </a>
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
        </template>
    </p-column>
    <p-column header="名称(中)" field="nameZh" :show-filter-menu="false" style="flex: 0 0 15rem">
        <template #filter="{filterModel,filterCallback}">
            <p-inputtext type="text" v-model="filterModel.value" @keydown.enter="filterCallback()"></p-inputtext>
        </template>
    </p-column>
    <p-column header="名称(英)" field="nameEn" style="flex: 0 0 15rem"></p-column>
    <p-column header="起始时间" field="originDate" :sortable="true" style="flex: 0 0 7rem"></p-column>
    <p-column header="元系列" field="metaLabel" data-type="boolean" body-class="text-center"
                style="flex: 0 0 4rem">
        <template #body="{data}">
            <i class="pi" :class="{'true-icon pi-check-circle': data.metaLabel, 'false-icon pi-times-circle': !data.metaLabel}"></i>
        </template>
        <template #filter="{filterModel,filterCallback}">
            <p-tristatecheckbox v-model="filterModel.value" @change="filterCallback()"></p-tristatecheckbox>
        </template>
    </p-column>
    <p-column v-for="(col, index) of selectedColumns" :field="col.field"
                :header="col.header" :key="col.field + '_' + index">
    </p-column>
</p-datatable>
<p-dialog :modal="true" v-model:visible="displayNewDialog" :style="{width: '600px'}" header="新增数据"
            class="p-fluid">
    <p-blockui :blocked="editBlock">
        <p-panel header="基础信息">
            <div class="formgrid grid">
                <div class="field col">
                    <label>名称<span style="color: red">*</span></label>
                    <p-inputtext v-model.trim="franchise.name"></p-inputtext>
                </div>
                <div class="field col">
                    <label>名称(中)<span style="color: red">*</span></label>
                    <p-inputtext v-model.trim="franchise.nameZh"></p-inputtext>
                </div>
            </div>
            <div class="formgrid grid">
                <div class="field col">
                    <label>名称(英)</label>
                    <p-inputtext v-model.trim="franchise.nameEn"></p-inputtext>
                </div>
                <div class="field col">
                    <label>发售时间<span style="color: red">*</span></label>
                    <p-calendar v-model="franchise.originDate" date-format="yy/mm/dd"
                                :show-button-bar="true"
                                :show-icon="true"></p-calendar>
                </div>
            </div>
            <div class="field">
                <label>备注</label>
                <p-textarea v-model="franchise.remark" rows="3" cols="20"
                            :auto-resize="true"></p-textarea>
            </div>
        </p-panel>
        <p-panel header="子系列">
            <div class="formgrid grid">
                <div class="field col-3">
                    <div class="col-12">
                        <label class="mb-3">是否为元系列<span style="color: red">*</span></label>
                    </div>
                    <div class="col-12 mt-4">
                        <p-inputswitch v-model="franchise.metaLabel"></p-inputswitch>
                    </div>
                </div>
                <div class="field col-9" v-if="franchise.metaLabel">
                    <label class="mb-3">子系列</label>
                    <p-multiselect v-model="franchise.childFranchises" :options="franchiseSet" placeholder="子系列"
                                    option-label="label" option-value="value" display="chip" :filter="true">
                    </p-multiselect>
                </div>
            </div>
        </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                    @click="closeNewDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text"
                    @click="submitNewItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
<p-dialog :modal="true" v-model:visible="displayEditDialog" :style="{width: '600px'}" header="编辑数据"
            class="p-fluid">
    <p-blockui :blocked="editBlock">
        <p-panel header="基础信息">
            <div class="formgrid grid">
                <div class="field col">
                    <label>名称<span style="color: red">*</span></label>
                    <p-inputtext v-model.trim="itemEdit.name"></p-inputtext>
                </div>
                <div class="field col">
                    <label>名称(中)<span style="color: red">*</span></label>
                    <p-inputtext v-model.trim="itemEdit.nameZh"></p-inputtext>
                </div>
            </div>
            <div class="formgrid grid">
                <div class="field col">
                    <label>名称(英)</label>
                    <p-inputtext v-model.trim="itemEdit.nameEn"></p-inputtext>
                </div>
                <div class="field col">
                    <label>发售时间<span style="color: red">*</span></label>
                    <p-calendar v-model="itemEdit.originDate" date-format="yy/mm/dd"
                                :show-button-bar="true"
                                :show-icon="true"></p-calendar>
                </div>
            </div>
            <div class="field">
                <label>备注</label>
                <p-textarea v-model="itemEdit.remark" rows="3" cols="20"
                            :auto-resize="true"></p-textarea>
            </div>
        </p-panel>
        <p-panel header="子系列" v-if="itemEdit.metaLabel">
            <div class="field col">
                <label class="mb-3">子系列</label>
                <p-multiselect v-model="itemEdit.childFranchises" :options="franchiseSet" placeholder="子系列"
                                option-label="label" option-value="value" display="chip" :filter="true">
                </p-multiselect>
            </div>
        </p-panel>
    </p-blockui>
    <template #footer>
        <p-button label="取消" icon="pi pi-times" class="p-button-text"
                    @click="closeEditDialog" :disabled="editBlock"></p-button>
        <p-button label="保存" icon="pi pi-check" class="p-button-text"
                    @click="submitEditItem" :disabled="editBlock"></p-button>
    </template>
</p-dialog>
    `,
    mounted() {
        this.initData();
        this.init();
    },
    data() {
        return {
            //region common
            toast: useToast(),
            loading: false,
            dt: null,
            totalLoading: false,
            editAuth: null,
            //endregion

            //region search param
            totalRecords: 0,
            queryParams: {},
            filters: {
                'name': {value: ''},
                'nameZh': {value: ''},
                'metaLabel': {value: null},
            },
            //endregion

            //region basic
            franchise: {},
            franchises: [],
            franchiseSet: [],

            columns: [
                {field: 'remark', header: '备注'},
                {field: 'addedTime', header: '收录时间'},
                {field: 'editedTime', header: '编辑时间'},
            ],
            //endregion

            //region edit
            itemEdit: {},
            editBlock: false,
            itemsStatus: null,
            statusOptions,
            selectedItems: null,
            selectedColumns: null,
            displayNewDialog: false,
            displayEditDialog: false,
            //endregion

        }
    },
    methods: {
        //region common
        updateItemsStatus(value) {
            this.editBlock = true;
            let json = {
                entityType: ENTITY.FRANCHISE,
                items: this.selectedItems,
                status: value
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEMS_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                    this.editBlock = false;
                })

        },
        exportCSV() {
            HttpUtil.get(null, CHECK_USER_AUTHORITY_URL)
                .then(res => {
                    if (res.state === 1) {
                        this.$refs.dt.exportCSV();
                    }
                })
        },
        //初始化
        initData() {
            this.loading = true;
            let json = {
                entityType: ENTITY.FRANCHISE
            };
            this.totalLoading = true;
            HttpUtil.post(null, GET_LIST_INIT_DATA_URL, json)
                .then(res => {
                    this.editAuth = res.editAuth;
                    this.franchiseSet = res.franchiseSet;
                    this.loading = false;
                    this.totalLoading = false;
                })
        },
        init() {
            this.loading = true;
            this.queryParams = {
                first: (this.queryParams !== {}?this.queryParams.first:0),
                rows: this.$refs.dt.rows,
                sortField: null,
                sortOrder: null,
                filters: this.filters
            };
            this.getFranchises();
            this.loading = false;
        },
        //endregion

        //region search
        onToggle(val) {
            this.selectedColumns = this.columns.filter(col => val.includes(col));
        },
        onPage(ev) {
            this.queryParams = ev;
            this.getFranchises();
        },
        onSort(ev) {
            this.queryParams = ev;
            this.getFranchises();
        },
        onFilter() {
            this.queryParams.filters = this.filters;
            this.queryParams.first = 0;
            this.getFranchises();
        },
        getFranchises() {
            HttpUtil.post(null, GET_FRANCHISES_URL, {queryParams: this.queryParams})
                .then(res => {
                    this.franchises = res.data;
                    this.totalRecords = res.total;
                })
        },
        //endregion

        //region edit
        updateStatus(id, status) {
            let json = {
                entityType: ENTITY.FRANCHISE,
                entityId: id,
                status: !status
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_ITEM_STATUS_URL, json)
                .then(res => {
                    if(res.state === 1) {
                        this.init();
                    }
                })
        },
        //打开编辑数据面板
        openEditDialog(data) {
            this.itemEdit = JSON.parse(JSON.stringify(data));
            this.displayEditDialog = true;
        },
        //关闭编辑数据面板
        closeEditDialog() {
            this.displayEditDialog = false;
            this.itemEdit = {};
        },
        //保存编辑数据
        submitEditItem() {
            this.editBlock = true;
            HttpUtil.commonVueSubmit(this.toast, UPDATE_FRANCHISE_URL, this.itemEdit)
                .then(res => {
                    if (res.state === 1) {
                        this.itemEdit = {};
                        this.closeEditDialog();
                        this.init();
                    }
                    this.editBlock = false;
                }).catch(e => {
                console.error(e);
            });
        },
        //打开新增数据面板
        openNewDialog() {
            this.franchise = {
                metaLabel: false,
                childFranchises: []
            };
            this.displayNewDialog = true;
        },
        //关闭新增数据面板
        closeNewDialog() {
            this.franchise = {};
            this.displayNewDialog = false;
        },
        //提交新增数据
        submitNewItem() {
            HttpUtil.commonVueSubmit(this.toast, ADD_FRANCHISE_URL, this.product)
                .then(res => {
                    if (res.state === 1) {
                        this.franchise = {};
                        this.closeNewDialog();
                        this.init();
                    }
                    this.editBlock = false;
                });
        },
        //endregion

    },
    components: {
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-calendar": primevue.calendar,
        "p-inputnumber": primevue.inputnumber,
        "p-dialog": primevue.dialog,
        "p-textarea": primevue.textarea,
        "p-toolbar": primevue.toolbar,
        "p-toast": primevue.toast,
        "p-panel": primevue.panel,
        "p-divider": primevue.divider,
        "p-inputswitch": primevue.inputswitch,
        "p-multiselect": primevue.multiselect,
        "p-inputtext": primevue.inputtext,
        "p-button": primevue.button,
        "p-dropdown": primevue.dropdown,
        "p-tristatecheckbox": primevue.tristatecheckbox,
        "p-blockui": primevue.blockui,
        "p-selectbutton": primevue.selectbutton,
    }
}

export const DATABASE_LIST_ROUTER = [
    {
        path: '/db/list',
        component: entryDbList
    },
    {
        path: '/db/list/entry',
        component: entryDbList
    },
    {
        path: '/db/list/album',
        component: albumDbList
    },
    {
        path: '/db/list/book',
        component: bookDbList
    },
    {
        path: '/db/list/disc',
        component: discDbList
    },
    {
        path: '/db/list/game',
        component: gameDbList
    },
    {
        path: '/db/list/merch',
        component: merchDbList
    },
    {
        path: '/db/list/product',
        component: productDbList
    },
    {
        path: '/db/list/franchise',
        component: franchiseDbList
    }
];