import {commonVueSubmit, formRequest} from '/js/basic/Http_Request.js';

export const showImageEditDialog = (toast, dialog, itemImageInfo, detailInfo) => {
    const dialogRef = dialog.open(imageEditPanel, {
        props: {
            header: '图片管理',
            style: {
                width: '1200px',
            },
            modal: true
        },
        data: {
            itemImageInfo: itemImageInfo,
            detailInfo: detailInfo,
            toast: toast,
        },
    });
};

const imageEditPanel = {
    template: `
        <p-blockui :blocked="editBlock">
            <p-panel>
                <template #header>
                    <i class="pi pi-plus-circle mr-2" style="font-size: 2rem"></i>
                    <b>新增</b>
                </template>
                <div class="grid">
                    <div class="col-5">
                        <div class="grid">
                            <div class="col-6 text-start">
                                <p-fileupload
                                        mode="basic"
                                        :custom-upload="true"
                                        accept="image/*"
                                        :auto="true"
                                        choose-label="上传图片"
                                        :max-file-size="2000000" :preview-width="100"
                                        invalid-file-size-message="{0}大小已超过{1}"
                                        @uploader="onUpload"
                                        @select="selectFile">
                                </p-fileupload>
                            </div>
                            <div class="col-6 text-end">
                                <p-button class="ml-2 p-button-text" icon="pi pi-trash"
                                          label="清空所选" @click="clearUploadedImage"></p-button>
                            </div>
                        </div>
                        <div class="formgrid grid mt-2">
                            <div class="field col-6">
                                <div class="p-inputgroup">
                                                <span class="p-inputgroup-addon">
                                                    <i class="pi pi-image"></i>
                                                </span>
                                    <p-inputtext v-model="imageInfo.nameZh" placeholder="图片名(中)"></p-inputtext>
                                </div>
                            </div>
                            <div class="field col-6">
                                <div class="p-inputgroup">
                                                <span class="p-inputgroup-addon">
                                                    <i class="pi pi-image"></i>
                                                </span>
                                    <p-inputtext v-model="imageInfo.nameEn" placeholder="图片名(英)"></p-inputtext>
                                </div>
                            </div>
                            <div class="field col-6">
                                <p-dropdown v-model="imageInfo.type" :options="imageTypes" option-label="label"
                                            option-value="value" placeholder="选择图片类型"
                                            style="width: 224px"></p-dropdown>
                            </div>
                            <div class="field col-6">
                                <p-textarea v-model="imageInfo.description" rows="1" cols="20"
                                            :auto-resize="true" placeholder="图片描述"
                                            style="width: 224px"></p-textarea>
                            </div>
                        </div>
                        <div class="formgrid grid mt-2">
                            <div class="field col" align="left">
                                <p-button label="新增图片" icon="pi pi-save"
                                          @click="save2imageInfos"></p-button>
                            </div>
                            <div class="field col" align="right">
                                <p-button label="提交新增" icon="pi pi-save"
                                          @click="submitImages" class="p-button-success"></p-button>
                            </div>
                        </div>
                    </div>
                    <div class="col-2">
                        <p-divider layout="vertical"></p-divider>
                    </div>
                    <div class="col-5">
                        <div class="field">
                            <span v-if=" imageHtml == '' " class="emptyInfo">还未选择图片</span>
                            <section>
                                <div id="imgBox" v-html="imageHtml"></div>
                            </section>
                        </div>
                    </div>
                </div>
            </p-panel>
            <p-panel>
                <template #header>
                    <i class="pi pi-pencil mr-2" style="font-size: 2rem"></i>
                    <b>编辑</b>
                </template>
                <div v-if="itemImageInfo.images.length != 0">
                    <p-datatable :value="itemImageInfo.images" class="p-datatable-sm"
                                 @row-reorder="imgRowReorder" edit-mode="row" striped-rows
                                 :resizable-columns="true" column-resize-mode="expand"
                                 v-model:editing-rows="editingImages" @row-edit-save="imgRowEditSave"
                                 v-model:selection="selectedImage">
                        <template #header>
                            <p-button icon="pi pi-trash" class="p-button-danger"
                                      @click="confirmDeleteSelectedImage"></p-button>
                        </template>
                        <p-column selection-mode="multiple" header-style="width: 4%"></p-column>
                        <p-column :row-reorder="true" header-style="width: 3%"></p-column>
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
                        <p-column field="nameZh" header="名(中)" header-style="width: 10%">
                            <template #editor="{ data, field }">
                                <p-inputtext v-model="data[field]" autofocus style="width: 100px"></p-inputtext>
                            </template>
                        </p-column>
                        <p-column field="nameEn" header="名(英)" header-style="width: 10%">
                            <template #editor="{ data, field }">
                                <p-inputtext v-model="data[field]" autofocus style="width: 100px"></p-inputtext>
                            </template>
                        </p-column>
                        <p-column field="type" header="类型" header-style="width: 8%">
                            <template #editor="{ data, field }">
                                <p-dropdown v-model="data[field]" :options="imageTypes" option-label="label"
                                            option-value="value" placeholder="图片类型">

                                </p-dropdown>
                            </template>
                            <template #body="slotProps">
                                {{getImageTypeLabel(slotProps.data.type)}}
                            </template>
                        </p-column>
                        <p-column field="description" header="描述" header-style="width: 15%">
                            <template #editor="{ data, field }">
                                <p-inputtext v-model="data[field]" autofocus style="width: 100px"></p-inputtext>
                            </template>
                        </p-column>
                        <p-column field="uploadTime" header="上传时间" header-style="width: 10%"></p-column>
                        <p-column field="uploadUser" header="上传用户"></p-column>
                        <p-column :row-editor="true" header-style="width: 15%"></p-column>
                    </p-datatable>
                </div>
                <div v-else>
                    <span class="emptyInfo"><em>暂无图片</em></span>
                </div>
            </p-panel>
        </p-blockui>
        <div class="text-end mt-3">
            <p-button icon="pi pi-times" label="取消" @click="closeImageEditDialog"
                  class="p-button-text" :disabled="editBlock"></p-button>
            <p-button v-if="itemImageInfo.images.length != 0" 
            icon="pi pi-save" label="提交更新" @click="updateImage" :disabled="editBlock"></p-button>
        </div>
        <p-dialog :modal="true" v-model:visible="deleteImageDialog" header="删除图片"
              :breakpoints="{'960px': '75vw', '640px': '90vw'}" :style="{width: '50vw'}">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem"></i>
                <span>确定删除所选的图片？</span>
            </div>
            <template #footer>
                <p-button label="取消" icon="pi pi-times" class="p-button-text"
                          @click="cancelDeleteSelectedImage"></p-button>
                <p-button label="确认删除" icon="pi pi-check" class="p-button-text"
                          @click="deleteImage"></p-button>
            </template>
        </p-dialog>
    `,
    inject: ['dialogRef'],
    data() {
        return {
            toast: null,
            itemImageInfo: {
                images: [],
                cover: {},
                displayImages: [],
                otherImages: []
            },
            detailInfo: {},
            imageTypes,
            editBlock: false,
            editingImages: [],
            activeIndex: 0,
            displayCustom: false,
            galleriaItemClass: "",
            selectedImage: [],
            deleteImageDialog: false,
            imageInfo: {
                image: null,
                nameZh: "",
                nameEn: "",
                description: "",
                type: 0
            },
            imageInfos: [],
            displayImageEditDialog: false,
            imageHtml: "",
        }
    },
    mounted() {
        this.itemImageInfo = this.dialogRef.data.itemImageInfo;
        this.detailInfo = this.dialogRef.data.detailInfo;
        this.toast = this.dialogRef.data.toast;
    },
    watch: {

    },
    methods: {
        //获取对应图片的类型显示名
        getImageTypeLabel,
        imgRowReorder(ev) {
            this.itemImageInfo.images = ev.value;
        },
        imgRowEditSave(ev) {
            let {newData, index} = ev;
            this.itemImageInfo.images[index] = newData;
        },
        imageClick(index) {
            this.activeIndex = index;
            this.displayCustom = true;
        },
        updateImage() {
            this.editBlock = true;
            let json = {
                entityType: this.detailInfo.entityType,
                entityId: this.detailInfo.id,
                images: this.itemImageInfo.images,
                action: "1"
            };
            commonVueSubmit(this.toast, UPDATE_IMAGES_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        this.closeImageEditDialog();
                        location.reload(true);
                    }else {
                        this.editBlock = false;
                    }
                }).catch(err => {
                console.error(err);
            });
        },
        confirmDeleteSelectedImage() {
            this.deleteImageDialog = true;
        },
        cancelDeleteSelectedImage() {
            this.selectedImage = [];
            this.deleteImageDialog = false;
        },
        deleteImage() {
            this.editBlock = true;
            let json = {
                entityType: this.detailInfo.entityType,
                entityId: this.detailInfo.id,
                images: this.selectedImage,
                action: "2"
            };
            commonVueSubmit(this.toast, UPDATE_IMAGES_URL, json)
                .then(res => {
                    if (res.state === 1) {
                        this.deleteImageDialog = false;
                        this.closeImageEditDialog();
                        this.selectedImage = [];
                        location.reload(true);
                    }else {
                        this.editBlock = false;
                    }
                }).catch(err => {
                console.error(err);
            });
        },
        closeImageEditDialog() {
            this.imageInfo = {};
            this.imageInfos = [];
            this.dialogRef.close();
            document.getElementById("imgBox").innerHTML = "";
        },
        showImage() {
            this.imageHtml = "";
            //<![CDATA[
            this.imageHtml += "<div class='grid' style='max-height: 200px;overflow-y: auto' >";
            for (const img of this.imageInfos) {
                this.imageHtml += '<div class="col-6" style="max-height: 100px">';
                this.imageHtml += '<img src="' + img.image.objectURL + '" style="max-height: 90px" />';
                this.imageHtml += '</div>';
                this.imageHtml += '<div class="col-6" style="max-height: 100px">';
                this.imageHtml += '<span>图片类型: ' + (img.type === "1" ? "封面" : (img.type === "0" ? "展示" : "其他")) + '</span><br>';
                this.imageHtml += '<span>图片名(中): ' + img.nameZh + '</span><br>';
                this.imageHtml += '<span>图片名(英): ' + img.nameEn + '</span><br>';
                this.imageHtml += '<span>描述: ' + img.description + '</span><br>';
                this.imageHtml += '</div>';
            }
            this.imageHtml += "</div>";
            //]]>
            document.getElementById("imgBox").innerHTML = this.imageHtml;
        },
        selectFile(ev) {
            this.imageInfo.image = ev.files[0];
        },
        onUpload(ev) {
            this.toast.add({severity: 'info', summary: 'Success', detail: IMAGE_SELECTED_INFO, life: 3000});
        },
        checkImageInfo(ev) {
            if (typeof this.imageInfo.description == "undefined") {
                this.imageInfo.description = "";
            }
            if (this.imageInfo.image === null) {
                this.toast.add({severity: 'error', summary: 'Error', detail: IMAGE_EMPTY_EXCEPTION, life: 3000});
            }
            if (typeof this.imageInfo.nameZh == "undefined") {
                this.toast.add({severity: 'error', summary: 'Error', detail: IMAGE_NAME_ZH_EMPTY_EXCEPTION, life: 3000});
                return false;
            }
            if (typeof this.imageInfo.nameEn == "undefined") {
                this.toast.add({severity: 'error', summary: 'Error', detail: IMAGE_NAME_EN_EMPTY_EXCEPTION, life: 3000});
                return false;
            }
            if (typeof this.imageInfo.type == "undefined") {
                this.toast.add({severity: 'error', summary: 'Error', detail: IMAGE_TYPE_EMPTY_EXCEPTION, life: 3000});
                return false;
            }
            return true;
        },
        save2imageInfos() {
            if (this.checkImageInfo()) {
                this.imageInfos.push(this.imageInfo);
                this.showImage();
                this.imageInfo = {};
            }
        },
        clearUploadedImage() {
            this.imageInfos = [];
            document.getElementById("imgBox").innerHTML = "";
        },
        submitImages() {
            this.editBlock = true;
            const formData = new FormData();
            for (const img of this.imageInfos) {
                formData.append("images", img.image);
            }
            formData.append("entityType", this.detailInfo.entityType);
            formData.append("entityId", this.detailInfo.id);
            formData.append("imageInfos", JSON.stringify(this.imageInfos));

            formRequest(this.toast, this.editBlock, INSERT_IMAGES_URL, formData)
                .then(res => {
                    if (res.state === 1) {
                        this.closeImageEditDialog();
                        location.reload(true);
                    }
                }).catch(err => {
                console.error(err);
            });
        },
    },
    components: {
        "p-button": primevue.button,
        "p-inputtext": primevue.inputtext,
        "p-dropdown": primevue.dropdown,
        "p-blockui": primevue.blockui,
        "p-panel": primevue.panel,
        "p-fileupload": primevue.fileupload,
        "p-textarea": primevue.textarea,
        "p-divider": primevue.divider,
        "p-datatable": primevue.datatable,
        "p-column": primevue.column,
        "p-dialog": primevue.dialog,
    }
};