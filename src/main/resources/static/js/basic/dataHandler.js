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


var track_info = {
    "total_disc": 2,
    "total_tracks": 4,
    "disc_list": [
        {
            "catalog": "USSW-0310-1",
            "media_format": "CD",
            "album_format": "Vocal",
            "track_list": [
                {
                    "name": "Missing Promise",
                    "track_length": "4:17"
                },
                {
                    "name": "Reset",
                    "track_length": "4:00"
                },
                {
                    "name": "Missing Promise (instrumental)",
                    "track_length": "4:17"
                },
                {
                    "name": "Reset (instrumental)",
                    "track_length": "4:00"
                }
            ]
        },
        {
            "catalog": "USSW-0310-2",
            "media_format": "DVD",
            "album_format": "Vocal, Video",
            "track_list": [
                {
                    "name": "Missing Promise Music Video",
                    "track_length": "4:15"
                },
                {
                    "name": "Missing Promise メイキング映像",
                    "track_length": "18:21"
                }
            ]
        }
    ]
};

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