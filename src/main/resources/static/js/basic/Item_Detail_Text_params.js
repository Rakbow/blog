import {commonVueSubmit} from '/js/basic/Http_Request.js';

export const showBonusEditDialog = (toast, dialog, detailInfo, bonus) => {
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
            bonus: bonus,
            toast: toast,
        },
    });
};

const bonusEditPanel = {
    template: `
        <p-blockui :blocked="editBlock">
            <md-editor-v3 v-model="bonusMd" preview-theme="github"></md-editor-v3>
            <div class="text-end mt-3">
                <p-button icon="pi pi-times" label="取消" @click="closeBonusEditDialog"
                      class="p-button-text"></p-button>
                <p-button icon="pi pi-save" label="保存" @click="submitBonus"></p-button>
            </div>
        </p-blockui>
    `,
    inject: ['dialogRef'],
    data() {
        return {
            detailInfo: {},
            toast: null,
            editBlock: false,
            bonusMd: "",
        }
    },
    mounted() {
        this.toast = this.dialogRef.data.toast;
        this.detailInfo = this.dialogRef.data.detailInfo;
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
                id: this.detailInfo.id,
                bonus: this.bonusMd
            };
            let url = "/db/" + entityTypeValue2LabelEn(this.detailInfo.entityType) + "/update-bonus";
            commonVueSubmit(this.toast, this.editBlock, url, json)
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
    }
};

export const showDescriptionEditDialog = (toast, dialog, detailInfo) => {
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
        },
    });
};

const descriptionEditPanel = {
    template: `
        <p-blockui :blocked="editBlock">
            <md-editor-v3 v-model="descriptionMd" preview-theme="github"></md-editor-v3>
            <div class="text-end mt-3">
                <p-button icon="pi pi-times" label="取消" @click="closeDescriptionEditDialog"
                      class="p-button-text"></p-button>
                <p-button icon="pi pi-save" label="保存" @click="submitDescription"></p-button>
            </div>
        </p-blockui>
    `,
    inject: ['dialogRef'],
    data() {
        return {
            toast: null,
            detailInfo: {},
            editBlock: false,
            descriptionMd: "",

        }
    },
    mounted() {
        this.detailInfo = this.dialogRef.data.detailInfo;
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
                id: this.detailInfo.id,
                description: this.descriptionMd
            }
            let url = "/db/" + entityTypeValue2LabelEn(this.detailInfo.entityType) + "/update-description";
            commonVueSubmit(this.toast, this.editBlock, url, json)
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
    }
};