import '../../tool/axios/axios.js';

//axios封装post请求
export class HttpUtil {
    static post(toast, url, data) {
        return axios({
            method: 'post',
            url: url,
            data: data,
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
        }).then(res => {
            if(toast !== null) {
                if(res.data.state === 0) {
                    if(res.data.message !== '') {
                        toast.add({severity: 'error', summary: 'Error', detail: res.data.message, life: 3000});
                    }
                }else {
                    if(res.data.message !== '') {
                        toast.add({severity: 'success', summary: 'Success', detail: res.data.message, life: 3000});
                    }
                }
            }
            return res.data;
        }).catch(error => {
            return "exception = " + error;
        });
    }

//axios封装get请求
    static get(toast, url) {
        return axios({
            method: 'get',
            url: url,
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
        }).then(res => {
            if(toast !== null) {
                if(res.data.state === 0) {
                    if(res.data.message !== '') {
                        toast.add({severity: 'error', summary: 'Error', detail: res.data.message, life: 3000});
                    }
                }else {
                    if(res.data.message !== '') {
                        toast.add({severity: 'success', summary: 'Success', detail: res.data.message, life: 3000});
                    }
                }
            }
            return res.data;
        }).catch(error => {
            return "exception = " + error;
        });
    }

//通用提交更新请求
    static  commonSubmit(toast, editBlock, url, data) {
        return axios({
            method: 'post',
            url: url,
            data: data,
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
        }).then(res => {
            if(res.data.state === 0) {
                editBlock.value = false;
                if(res.data.message !== '') {
                    toast.add({severity: 'error', summary: 'Error', detail: res.data.message, life: 3000});
                }
            }else {
                if(res.data.message !== '') {
                    toast.add({severity: 'success', summary: 'Success', detail: res.data.message, life: 3000});
                }
            }
            return res.data;
        }).catch(error => {
            return "exception = " + error;
        });
    }

//通用提交更新请求
    static commonVueSubmit(toast, url, data) {
        return axios({
            method: 'post',
            url: url,
            data: data,
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
        }).then(res => {
            if(res.data.state === 0) {
                if(res.data.message !== '') {
                    toast.add({severity: 'error', summary: 'Error', detail: res.data.message, life: 3000});
                }
            }else {
                if(res.data.message !== '') {
                    toast.add({severity: 'success', summary: 'Success', detail: res.data.message, life: 3000});
                }
            }
            return res.data;
        }).catch(error => {
            return "exception = " + error;
        });
    }

//axios封装delete请求
    static deleteRequest(toast, url, data) {
        return axios({
            method: 'delete',
            url: url,
            data: data,
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            },
        }).then(res => {
            if(toast !== null) {
                if(res.data.state === 0) {
                    if(res.data.message !== '') {
                        toast.add({severity: 'error', summary: 'Error', detail: res.data.message, life: 3000});
                    }
                }else {
                    if(res.data.message !== '') {
                        toast.add({severity: 'success', summary: 'Success', detail: res.data.message, life: 3000});
                    }
                }
            }
            return res.data;
        }).catch(error => {
            return "exception = " + error;
        });
    }

//axios封装表单提交
    static formPost(toast, editBlock, url, data) {
        return axios({
            method: 'post',
            url: url,
            data: data,
            headers: {
                'Content-Type': 'multipart/form-data',
                'X-Requested-With': 'XMLHttpRequest'
            },
        }).then(res => {
            if(res.data.state === 0) {
                if(res.data.message !== '') {
                    toast.add({severity: 'error', summary: 'Error', detail: res.data.message, life: 3000});
                }
            }else {
                if(res.data.message !== '') {
                    toast.add({severity: 'success', summary: 'Success', detail: res.data.message, life: 3000});
                }
            }
            return res.data;
        }).catch(error => {
            return "exception = " + error;
        });
    }
}