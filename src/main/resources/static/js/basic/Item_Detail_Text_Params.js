import {HttpUtil} from '/js/basic/Http_Util.js';
import {RKW_Web} from '/js/basic/Rakbow_Web_Control_Strs_CN.js';

export const showBonusEditDialog = (toast, dialog, detailInfo, bonus, images) => {
    const dialogRef = dialog.open(bonusEditPanel, {
        props: {
            header: '特典信息',
            style: {
                width: '80vw',
            },
            breakpoints: {
                '960px': '75vw',
                '640px': '90vw'
            },
            modal: true
        },
        data: {
            detailInfo: detailInfo,
            images: images,
            bonus: bonus,
            toast: toast,
        },
    });
};

export const showDescriptionEditDialog = (toast, dialog, detailInfo, images) => {
    const dialogRef = dialog.open(descriptionEditPanel, {
        props: {
            header: '描述信息',
            style: {
                width: '80vw',
            },
            breakpoints: {
                '960px': '75vw',
                '640px': '90vw'
            },
            modal: true
        },
        data: {
            detailInfo: detailInfo,
            toast: toast,
            images: images,
        },
    });
};

export const showSpecEditDialog = (toast, dialog, entityId, entityType, entitySpec) => {
    const dialogRef = dialog.open(specEditPanel, {
        props: {
            header: '规格信息编辑',
            style: {
                width: '80vw',
            },
            breakpoints: {
                '960px': '75vw',
                '640px': '90vw'
            },
            modal: true
        },
        data: {
            entityId: entityId,
            entityType: entityType,
            entitySpec: entitySpec,
            toast: toast,
        },
    });
};

const bonusEditPanel = {
    template: `
        <p-blockui :blocked="editBlock">
            <md-editor-v3 v-model="bonusMd" preview-theme="github"></md-editor-v3>
            <div class="text-end mt-3 mb-2">
                <p-button icon="pi pi-times" label="取消" @click="closeBonusEditDialog"
                      class="p-button-text"></p-button>
                <p-button icon="pi pi-save" label="保存" @click="submitBonus"></p-button>
            </div>
            <p-panel>
                <div v-if="images.length != 0">
                    <p-datatable :value="images" class="p-datatable-sm" striped-rows>
                        <template #header>
                            <div class="flex flex-wrap align-items-center justify-content-between gap-2">
                                <span class="text-xl text-900 font-bold ml-10">图片列表</span>
                            </div>
                        </template>
                        <p-column header-style="width: 5%">
                        <template #body="slotProps">
                            <p-button icon="pi pi-copy" @click="copyImageUrl(slotProps.data.url)"></p-button>
                        </template>
                        </p-column>
                        <p-column header="图片" header-style="width: 8%">
                            <template #body="slotProps">
                                <img :src="slotProps.data.thumbUrl50" :alt="slotProps.data.nameEn"
                                     class="edit-image"/>
                            </template>
                        </p-column>
                        <p-column field="nameZh" header="名(中)" header-style="width: 10%"></p-column>
                        <p-column field="nameEn" header="名(英)" header-style="width: 10%"></p-column>
                        <p-column field="description" header="描述" header-style="width: 20%"></p-column>
                    </p-datatable>
                </div>
                <div v-else>
                    <span class="emptyInfo"><em>暂无图片</em></span>
                </div>
            </p-panel>
        </p-blockui>
    `,
    inject: ['dialogRef'],
    data() {
        return {
            detailInfo: {},
            images: [],
            toast: null,
            editBlock: false,
            bonusMd: "",
        }
    },
    mounted() {
        this.toast = this.dialogRef.data.toast;
        this.detailInfo = this.dialogRef.data.detailInfo;
        this.images = this.dialogRef.data.images;
        this.bonusMd = this.dialogRef.data.bonus;
    },
    watch: {

    },
    methods: {
        copyImageUrl(url) {
            copyToClip(url + '?imageMogr2/auto-orient/thumbnail/400x400');
        },
        closeBonusEditDialog() {
            this.dialogRef.close();
        },
        submitBonus() {
            this.editBlock = true;
            let json = {
                entityType: this.detailInfo.entityType,
                entityId: this.detailInfo.id,
                bonus: this.bonusMd
            };
            HttpUtil.commonVueSubmit(this.toast, UPDATE_BONUS_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        this.dialogRef.close();
                        location.reload(true);
                    }else {
                        this.editBlock = false;
                    }
                });
        }
    },
    components: {
        "p-button": primevue.button,
        "p-blockui": primevue.blockui,
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-panel": primevue.panel,
    }
};

const descriptionEditPanel = {
    template: `
    <p-blockui :blocked="editBlock">
    <md-editor-v3 v-model="descriptionMd" preview-theme="github"></md-editor-v3>
    <div class="text-end mt-3 mb-2">
        <p-button icon="pi pi-times" label="取消" @click="closeDescriptionEditDialog"
              class="p-button-text"></p-button>
        <p-button icon="pi pi-save" label="保存" @click="submitDescription"></p-button>
    </div>
    <p-panel>
        <div v-if="images.length != 0">
            <p-datatable :value="images" class="p-datatable-sm" striped-rows>
                <template #header>
                    <div class="flex flex-wrap align-items-center justify-content-between gap-2">
                        <span class="text-xl text-900 font-bold">图片列表</span>
                    </div>
                </template>
                <p-column header-style="width: 5%">
                <template #body="slotProps">
                    <p-button icon="pi pi-copy" @click="copyImageUrl(slotProps.data.url)"></p-button>
                </template>
                </p-column>
                <p-column header="图片" header-style="width: 8%">
                    <template #body="slotProps">
                        <img :src="slotProps.data.thumbUrl50" :alt="slotProps.data.nameEn"
                             class="edit-image"/>
                    </template>
                </p-column>
                <p-column field="nameZh" header="名(中)" header-style="width: 10%"></p-column>
                <p-column field="nameEn" header="名(英)" header-style="width: 10%"></p-column>
                <p-column field="description" header="描述" header-style="width: 20%"></p-column>
            </p-datatable>
        </div>
        <div v-else>
            <span class="emptyInfo"><em>暂无图片</em></span>
        </div>
    </p-panel>
</p-blockui>
    `,
    inject: ['dialogRef'],
    data() {
        return {
            toast: null,
            detailInfo: {},
            images: [],
            editBlock: false,
            descriptionMd: "",

        }
    },
    mounted() {
        console.log(this.itemImageInfo)
        this.detailInfo = this.dialogRef.data.detailInfo;
        this.images = this.dialogRef.data.images;
        this.toast = this.dialogRef.data.toast;
        this.descriptionMd = this.detailInfo.description;
    },
    watch: {

    },
    methods: {
        copyImageUrl(url) {
            copyToClip(url + '?imageMogr2/auto-orient/thumbnail/400x400');
        },
        closeDescriptionEditDialog() {
            this.dialogRef.close();
        },
        submitDescription() {
            this.editBlock = true;
            let json = {
                entityType: this.detailInfo.entityType,
                entityId: this.detailInfo.id,
                description: this.descriptionMd
            }
            HttpUtil.commonVueSubmit(this.toast, UPDATE_DESCRIPTION_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        this.dialogRef.close();
                        location.reload(true);
                    }else {
                        this.editBlock = false;
                    }
                });
        }
    },
    components: {
        "p-button": primevue.button,
        "p-blockui": primevue.blockui,
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-panel": primevue.panel,
    }
};

const specEditPanel = {
    template: `
        <p-blockui :blocked="editBlock">
            <p-panel>
                <template #header>
                    <i class="pi pi-user-plus mr-2" style="font-size: 2rem"></i>
                    <b>{{RKW_Web.Add}}</b>
                </template>
                <div class="grid">
                    <div class="col-3">
                        <div class="p-inputgroup">
                            <span class="p-inputgroup-addon">
                                <i class="pi pi-tag"></i>
                            </span>
                            <p-inputtext v-model="specItem.label" :placeholder="RKW_Web.BookDetailEditSpecLabel"></p-inputtext>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="p-inputgroup">
                            <span class="p-inputgroup-addon">
                                <i class="pi pi-users"></i>
                            </span>
                            <p-chips v-model="specItem.value" :placeholder="RKW_Web.BookDetailEditSpecValue" separator=","></p-chips>
                        </div>
                    </div>
                    <div class="col-2">
                        <p-button :label="RKW_Web.Add" icon="pi pi-save"
                                  @click="addSpecItem"></p-button>
                    </div>
                </div>
            </p-panel>
            <p-panel>
                <template #header>
                    <i class="pi pi-user-edit mr-2" style="font-size: 2rem"></i>
                    <b>{{RKW_Web.Edit}}</b>
                </template>
                <div v-if="tmpSpec.length != 0">
                    <p-datatable dataKey="id" :value="tmpSpec" responsive-layout="scroll"
                                 class="p-datatable-sm" striped-rows @row-reorder="specOnRowReorder"
                                 context-menu v-model:context-menu-selection="selectedSpecItem"
                                 @row-contextmenu="specRowMenu" edit-mode="row"
                                 v-model:editing-rows="specEditingRows" @row-edit-save="specOnRowEditSave">
                        <p-column :row-reorder="true"></p-column>
                        <p-column field="label" :header="RKW_Web.BookDetailEditSpecLabel">
                            <template #editor="{ data, field }">
                                <p-inputtext v-model="data[field]" autofocus></p-inputtext>
                            </template>
                        </p-column>
                        <p-column field="value" :header="RKW_Web.BookDetailEditSpecValue">
                            <template #body="slotProps">
                                {{slotProps.data.value.join("/")}}
                            </template>
                            <template #editor="{ data, field }">
                                <p-chips v-model="data[field]"></p-chips>
                            </template>
                        </p-column>
                        <p-column :row-editor="true" style="width:10%; min-width:8rem"
                                  bodyStyle="text-align:center"></p-column>
                    </p-datatable>
                    <p-contextmenu :model="specMenuModel" ref="specCm"></p-contextmenu>
                </div>
                <div v-else>
                    <span class="emptyInfo">{{RKW_Web.ItemDetailMessageNoSpec}}</span>
                </div>
            </p-panel>
            <div class="text-end mt-3 mb-2">
                <p-button :label="RKW_Web.Clear" icon="pi pi-trash" class="p-button-danger mr-4"
                          @click="clearSpec" :disabled="editBlock"></p-button>
                <p-button :label="RKW_Web.Cancel" icon="pi pi-times" class="mr-4"
                          @click="cancelSpecEdit" :disabled="editBlock"></p-button>
                <p-button :label="RKW_Web.Update" icon="pi pi-save" class="p-button-success mr-4"
                          @click="submitSpec" :disabled="editBlock"></p-button>
            </div>
        </p-blockui>
    `,
    inject: ['dialogRef'],
    data() {
        return {
            toast: null,
            entityId: null,
            entityType: null,
            entitySpec: [],
            editBlock: false,

            tmpSpec: [],
            selectedSpecItem: null,
            specItem: {},
            spec: [],
            specMenuModel: [{label: RKW_Web.Delete, icon: 'pi pi-fw pi-user-minus', command: () => this.deleteSpecItem(this.selectedSpecItem)}],
            specEditingRows: [],
            RKW_Web,
        }
    },
    mounted() {
        this.entityId = this.dialogRef.data.entityId;
        this.entityType = this.dialogRef.data.entityType;
        this.entitySpec = this.dialogRef.data.entitySpec;
        this.toast = this.dialogRef.data.toast;

        this.tmpSpec = JSON.parse(JSON.stringify(this.entitySpec));
    },
    watch: {

    },
    methods: {
        specOnRowReorder(ev) {
            this.tmpSpec.value = ev.value;
        },
        specRowMenu(ev) {
            this.$refs.specCm.show(ev.originalEvent);
        },
        deleteSpecItem(SpecItem) {
            this.tmpSpec = this.tmpSpec.filter((s) => s.label !== SpecItem.label);
            this.toast.add({severity: 'error', summary: RKW_Web.MessageDeleted, detail: SpecItem.value, life: 3000});
            this.selectedSpecItem = null;
        },
        specOnRowEditSave(ev) {
            let {newData, index} = ev;
            this.tmpSpec[index] = newData;
        },
        clearSpec() {
            this.tmpSpec = [];
        },
        addSpecItem() {
            this.tmpSpec.push(this.specItem);
            this.specItem = {};
        },
        cancelSpecEdit() {
            this.specItem = {};
            this.dialogRef.close();
        },

        submitSpec() {
            this.editBlock = true;
            let json = {
                entityType: this.entityType,
                entityId: this.entityId,
                spec: this.tmpSpec
            }
            HttpUtil.commonVueSubmit(this.toast, UPDATE_SPEC_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        this.dialogRef.close();
                        this.specItem = {};
                        location.reload(true);
                    }else {
                        this.editBlock = false;
                    }
                });
        }
    },
    components: {
        "p-button": primevue.button,
        "p-blockui": primevue.blockui,
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-panel": primevue.panel,
        "p-chips": primevue.chips,
        "p-inputtext": primevue.inputtext,
        "p-contextmenu": primevue.contextmenu,
    }
};