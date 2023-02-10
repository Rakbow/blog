//axios封装post请求
function axiosPostRequest(url, data) {
    return axios({
        method: 'post',
        url: url,
        data: data,
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
    }).then(res => {
        return res.data;
    }).catch(error => {
        return "exception = " + error;
    });
}

//axios封装get请求
function axiosGetRequest(url, data) {
    const result = axios({
        method: 'get',
        url: url,
        data: data,
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
    }).then(res => {
        return res.data;
    }).catch(error => {
        return "exception = " + error;
    });
    return result;
}

//axios封装delete请求
function axiosDeleteRequest(url, data) {
    const result = axios({
        method: 'delete',
        url: url,
        data: data,
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        },
    }).then(res => {
        return res.data;
    }).catch(error => {
        return "exception = " + error;
    });
    return result;
}

//axios封装自定义请求
function axiosRequest(url, data) {
    const result = axios({
        method: 'post',
        url: url,
        data: data,
        headers: {
            'Content-Type': 'multipart/form-data',
            'X-Requested-With': 'XMLHttpRequest'
        },
    }).then(res => {
        return res.data;
    }).catch(error => {
        return "exception = " + error;
    });
    return result;
}