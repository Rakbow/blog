//axios封装post请求
function axiosPostRequest(url, data) {
    const result = axios({
        method: 'post',
        url: url,
        data: data,
        header: {
            'Content-Type': 'application/json'
        },
    }).then(res => {
        return res.data;
    }).catch(error => {
        return "exception = " + error;
    });
    return result;
}

//axios封装get请求
function axiosGetRequest(url, data) {
    const result = axios({
        method: 'get',
        url: url,
        data: data,
        header: {
            'Content-Type': 'application/json'
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
        header: {
            'Content-Type': 'application/json'
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
        header: {
            'Content-Type': 'multipart/form-data'
        },
    }).then(res => {
        return res.data;
    }).catch(error => {
        return "exception = " + error;
    });
    return result;
}