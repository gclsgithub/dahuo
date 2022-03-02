var SC_REQUEST_CSRF = "403";
var SC_REQUEST_TIMEOUT = "408";

var COMMON_PAGE_BLOCKER_URL = {};

function Common() {}

/* ajax CSRF対策 */
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
});
/* ajax CSRF対策 */

Common.transfer = function(func, data, errorCode, jqXHR) {
    if (typeof func != 'undefined' && func instanceof Function) {
        try{
            var jsonData = jQuery.parseJSON(data);
            data = jsonData;
        }catch (e) {
            // ログイン画面に遷移する場合
            if($(data).find('#btnLogin').length > 0) {
                window.location.href = 'login';
                return;
            }
        }
        func(data, errorCode, jqXHR);
    }
};

/**
 * セッションタイムアウトエラーコードかチェック
 */
Common.isSessionTimeoutCode = function(code) {
    if (code == SC_REQUEST_CSRF || code == SC_REQUEST_TIMEOUT) {
        return true;
    }
    return false;
};

/**
 * Ajax通信共通メソッド
 */
Common.ajaxCommon = function(url, params, callBackFunc) {
    // ajax呼び出し
    $.ajax(url, params
    ).done(function(data, textStatus, jqXHR) {
        Common.transfer(callBackFunc, data, textStatus, jqXHR);
    }).fail(function(jqXHR, textStatus, errorThrown) {
        if (Common.isSessionTimeoutCode(jqXHR.status) == true) {
            // セッションタイムアウト
            var url = jqXHR.getResponseHeader('timeoutUrl');
            if (url) {
                // ログイン画面の場合
                window.location.href = url;
            } else {
                var jsonData = jQuery.parseJSON(jqXHR.responseText);
                Common.transfer(callBackFunc, jsonData, SC_REQUEST_TIMEOUT, null);
            }
        } else {
            Common.transfer(callBackFunc, null, textStatus, jqXHR);
        }
    });
};

/**
 * Ajax通信共通メソッド
 */
Common.ajaxCommonPostFile = function(url, datas, callBackFunc) {
    var params ={};
    params['type'] = 'POST';
    params['data'] = datas;
    params['contentType'] = false;
    params['processData'] = false;

    Common.ajaxCommon(url, params, callBackFunc);
};

/**
 * Ajax通信共通メソッド
 */
Common.ajaxCommonPost = function(url, datas, callBackFunc) {
    var params ={};
    params['type'] = 'POST';
    params['data'] = datas;

    Common.ajaxCommon(url, params, callBackFunc);
};

/**
 * Common.ajaxCommon wrapper that returns jQuery promise
 * @param url {string} jQuery ajax url
 * @param params {Any} jQuery ajax params
 * @returns {Promise} the ajax promise (jQuery promise)
 */
Common.ajaxPromise = function(url, params) {
    var dfd = $.Deferred();
    Common.ajaxCommon(url, params, function(data, textStatus, jqXHR) {
        var error = jqXHR.getResponseHeader('error');
        if (error) {
            var type = jqXHR.getResponseHeader('type');
            dfd.reject({code: type, status: textStatus, data: data});
            return;
        }

        if (data == null || textStatus == "error") {
            dfd.reject({
                code: jqXHR.status,
                msg: jqXHR.statusText,
                jqXHR: jqXHR
            });
            return;
        }

        if (typeof(data.resultCd) != "undefined" && data.resultCd == "1") {
            dfd.reject({
                code : data.errors[0].code,
                msg : data.errors[0].text,
                data : data
            });
            return;
        }

        dfd.resolve(data);
    });

    return dfd.promise();
};

/**
 * Calls Common.createPageBlocker
 * then calls Common.removePageBlocker when promiseSrc is resolved or rejected
 * @param {String} id
 * @param {(function():Promise) | Promise} promiseSrc
 * @return {Promise}
 */
Common.blockingPromise = function (id, promiseSrc) {
    // ブラック中処理なし
    if (COMMON_PAGE_BLOCKER_URL[id] == true) {
        return;
    }
    // ブラック作成
    Common.createPageBlocker(id);
    var _promise = promiseSrc;

    if(typeof(promiseSrc) == "function"){
        _promise = promiseSrc();
    }

    if (typeof(_promise.then) != "function") {
        Common.removePageBlocker(id);
        throw {code: "INV_PARAM", msg: "Invalid parameter. Promise expected."};
    }

    // ブラック解除
    return _promise.always(function () {
        Common.removePageBlocker(id);
    });
};

/**
 * ダブル送信ブロック処理
 */
Common.createPageBlocker = function(url) {
    COMMON_PAGE_BLOCKER_URL[url] = true;
    var obj = $("#divPageBlocker");
    if (obj != undefined && obj != null && obj.length != 0) {
        return;
    }
    var pageBlocker = $("<div ></div>")
        .prop("id", "divPageBlocker")
        .css({
            "bottom" : "0",
            "box-sizing" : "border-box",
            "height" : "100%",
            "left" : "0",
            "overflow" : "auto",
            "position" : "fixed",
            "right" : "0",
            "top" : "0",
            "width" : "100%",
            "z-index" : "99999",
            "cursor" : "wait",
            "opacity" : "1"
        });
    $("body").append(pageBlocker);
};

/**
 * ダブル送信ブロック解除処理
 */
Common.removePageBlocker = function(url) {
    if ($("#divPageBlocker").length) {
        $("#divPageBlocker").remove();
    }
    setTimeout(function() {
        delete COMMON_PAGE_BLOCKER_URL[url];
    }, 1000);
};


// 例えば：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function(fmt)
{
    var o = {
        "M+" : this.getMonth()+1,
        "d+" : this.getDate(),
        "h+" : this.getHours(),
        "m+" : this.getMinutes(),
        "s+" : this.getSeconds(),
        "q+" : Math.floor((this.getMonth()+3)/3),
        "S"  : this.getMilliseconds()
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}
