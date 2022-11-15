//将{label, value}选项里value数组转为label数组
const labelEn2valueArray = (xFormat, xFormatSet) => {
    let tmp = [];
    for (let item1 of xFormat) {
        for (let item2 of xFormatSet) {
            if (item1 === item2.labelEn) {
                tmp.push(item2.value);
            }
        }
    }
    return tmp;
}

const value2LabelEnArray = (xFormat, xFormatSet) => {
    let tmp = [];
    for (let item1 of xFormat) {
        for (let item2 of xFormatSet) {
            if (item1 === item2.value) {
                tmp.push(item2.labelEn);
            }
        }
    }
    return tmp;
}

//将date类型转为string类型
function dateToString(date) {
    const year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    month = month > 9 ? month : '0' + month;
    day = day > 9 ? day : '0' + day;
    return year + "/" + month + "/" + day;
};

//日元(100)转为人民币
function JPY2CNY(jpy) {
    axiosGetRequest('https://www.chinamoney.com.cn/ags/ms/cm-u-bk-ccpr/CcprHisNew?currency=100JPY/CNY')
        .then(res => {
            const result = {};
            result.price = parseFloat(jpy) * parseFloat(res.records[0].values[0]) / 100;
            result.updateDate = res.records[0].date;
            result.currentExchangeRate = parseFloat(res.records[0].values[0]);
            return result;
        })
}

//根据选项set转换label -> value
const label2value = (xFormat, xFormatSet) => {
    let tmp = [];
    for (let item1 of xFormat) {
        for (let item2 of xFormatSet) {
            if (item1 === item2.label) {
                tmp.push(item2.value);
            }
        }
    }
    return tmp;
};

//根据图片url获取图片字节大小和长宽
const getImgProp = (url) => {
    let imgProp = {
        imgSize: 0,
        width: 0,
        height: 0
    };
    fetch(url).then(res => {
        res.blob().then(result => {
            imgProp.imgSize = (result.size/1024).toFixed(2);
            var image = new Image();
            image.src = url;
            image.onload = function (){
                imgProp.width = image.naturalWidth;
                imgProp.height = image.naturalHeight;
            }
        })
    });
    return imgProp;
};

/**
 * 根据图片url转为png文件对象
 * @param url
 * @param imageName
 * @returns {Promise<unknown>}
 */
function getImageFileFromUrl(url, imageName) {
    return new Promise((resolve, reject) => {
        let blob = null;
        const xhr = new XMLHttpRequest();
        xhr.open("GET", url);
        xhr.setRequestHeader('Accept', 'image/png');
        xhr.responseType = "blob";
        // 加载时处理
        xhr.onload = () => {
            // 获取返回结果
            blob = xhr.response;
            let imgFile = new File([blob], imageName, { type: 'image/png' });
            // 返回结果
            resolve(imgFile);
        };
        xhr.onerror = (e) => {
            reject(e)
        };
        // 发送
        xhr.send();
    });
}


function countTime(time1, time2) {
    res = second2time(parseInt(time2second(time1)) + parseInt(time2second(time2)));
    console.log(res);
}

function second2time(time) {
    const h = parseInt(time / 3600)
    const minute = parseInt(time / 60 % 60)
    const second = Math.ceil(time % 60)

    const hours = h < 10 ? '0' + h : h
    const formatSecond = second > 59 ? 59 : second
    return `${hours > 0 ? `${hours}:` : ''}${minute < 10 ? '0' + minute : minute}:${formatSecond < 10 ? '0' + formatSecond : formatSecond}`;
}

function time2second(time) {
    if (strCount(time) < 2) {
        time = "00:" + time;
    }
    var hour = time.split(':')[0];
    var min = time.split(':')[1];
    var sec = time.split(':')[2];
    res = Number(hour * 3600) + Number(min * 60) + Number(sec);
    return res;
}

function strCount(str) {
    let target = ":";
    let count = 0
    if (!target) return count
    while (str.match(target)) {
        str = str.replace(target, '')
        count++
    }
    return count
}

function countTotalLength(track_info) {
    var total_length = 0;
    for (let i = 0; i < track_info.disc_list.length; i++) {
        for (let j = 0; j < track_info.disc_list[i].track_list.length; j++) {
            total_length += parseInt(time2second(track_info.disc_list[i].track_list[j].track_length));
        }
    }
    return second2time(total_length);
}