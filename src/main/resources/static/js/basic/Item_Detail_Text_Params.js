import {HttpUtil} from '/js/basic/Http_Util.js';

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
            <div class="text-center">
                图片url模板: https://img.rakbow.com/XXX/?imageMogr2/auto-orient/thumbnail/200x200
            </div>
            <p-datatable :value="images" class="p-datatable-sm" striped-rows>
                <p-column header="图片" header-style="width: 8%">
                    <template #body="slotProps">
                        <img :src="slotProps.data.thumbUrl50" :alt="slotProps.data.nameEn"
                             class="edit-image"/>
                    </template>
                </p-column>
                <p-column field="url" header="URL" header-style="width: 10%">
                    <template #body="slotProps">
                        {{slotProps.data.url.substr(22)}}
                    </template>
                </p-column>
                <p-column field="nameZh" header="名(中)" header-style="width: 10%"></p-column>
                <p-column field="nameEn" header="名(英)" header-style="width: 10%"></p-column>
                <p-column field="description" header="描述" header-style="width: 15%"></p-column>
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
            <div class="text-center">
                图片url模板: https://img.rakbow.com/XXX/?imageMogr2/auto-orient/thumbnail/200x200
            </div>
            <p-datatable :value="images" class="p-datatable-sm" striped-rows>
                <p-column header="图片" header-style="width: 8%">
                    <template #body="slotProps">
                        <img :src="slotProps.data.thumbUrl50" :alt="slotProps.data.nameEn"
                             class="edit-image"/>
                    </template>
                </p-column>
                <p-column field="url" header="URL" header-style="width: 10%">
                    <template #body="slotProps">
                        {{slotProps.data.url.substr(22)}}
                    </template>
                </p-column>
                <p-column field="nameZh" header="名(中)" header-style="width: 10%"></p-column>
                <p-column field="nameEn" header="名(英)" header-style="width: 10%"></p-column>
                <p-column field="description" header="描述" header-style="width: 15%"></p-column>
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