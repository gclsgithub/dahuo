function Login(){};

$(document).ready(function() {

    Login.storageKeys = {
        loginId: "fw-admin-web-loginId",
        remLoginSts: "fw-admin-web-remLoginSts",
        startScreen: "fw-admin-web-startScreen"
    };

    // Restore saved loginId
    var savedLoginId = localStorage.getItem(Login.storageKeys.loginId);
    var savedRemLoginSts = localStorage.getItem(Login.storageKeys.remLoginSts);
    var savedStartScreen = localStorage.getItem(Login.storageKeys.startScreen);
    // ログインID保存
    if(savedLoginId != null) {
        $("#login_id").val(savedLoginId);
    }
    // ログイン状態を記憶
    if(savedRemLoginSts != null){
        $("#rem_login_sts").prop("checked", true);
    }
    // ログインホーム画面設定
    if(savedStartScreen != null) {
        $("#login-form #start").val(savedStartScreen);
    }

    Login._auth = function(data) {
        var passwd = '';
        if($('#login_pwd').val() != null && $('#login_pwd').val() != ''){
            passwd = CryptoJS.SHA256($('#login_pwd').val().trim()).toString().toUpperCase();
        }

        $('#hdn_login_id').val($("#login_id").val());
        $('#hdn_login_pwd').val(passwd);
        if($('#rem_login_sts').is(':checked')){
            $('#hdn_rem_login_sts').val($('#rem_login_sts').val());
        }
        var formObj = $("#login_form");
        var params = {
            type : "POST",
            data : formObj.serializeArray()
        };

        return Common.ajaxPromise(formObj.attr("action"), params);
    };

    Login._authSuccess = function(data) {
        var loginId = $("#login_id").val();
        // クッキーに保存ログインID保存
        if($('#rem_login_sts').is(':checked')){
            localStorage.setItem(Login.storageKeys.remLoginSts, true);
        }else{
            localStorage.removeItem(Login.storageKeys.remLoginSts);
        }
        var loginData = localStorage.getItem(loginId);
        if (loginData != null) {
            var jsonLoginData = JSON.parse(loginData);
            var url = $('#hdbOpenMenu').val();
            var params ={};
            params['type'] = 'POST';
            params['data'] = {
                gmMulti : jsonLoginData.gmMulti.join(','),
                openFlg : 'open'
            };
            Common.ajaxCommon(url, params, null);

            // 前回ホーム画面開く
            if(jsonLoginData.homeUrl != null) {
                window.location.href = jsonLoginData.homeUrl;
                return;
            }
        }

        window.location.href = data.homeUrl;
    };

    // Set login button click handler
    $("#btnLogin").on("click", function() {
        var msgObj = $("#err_common");
        var loginId = $("#login_id").val();

        //msgObj.empty();
        var blockerId = "login";
        Common
        .blockingPromise(blockerId, Login._auth)
        .then(Login._authSuccess)
        .fail(function(errObj) {
            if(errObj.code == "cancelled"){
                window.location.reload();
                return;
            }
            msgObj.empty();
            if((errObj.code == 'json') && typeof(errObj.data) != "undefined" && typeof(errObj.data.errors) != "undefined"){
                $.each(errObj.data.errors, function(index, value){
                    msgObj.append($("<div>", {"class": "content-box-header panel-heading", "text": value.text}));
                    if(value.args && value.args.length > 0){
                        $("[name='" + value.args.toString() + "']").focus();
                    }
                    return false;
                });
            } else {
                msgObj.append($("<div>", {"class": "content-box-header panel-heading", "text": value.text}));
            }

            msgObj.show();
            $(":input").removeAttr("disabled");
        });
    });

    $('body').keypress(function(e) {
        // エンターキーが押す
        if (e.which == 13) {
            // ログイン処理
            $('#btnLogin').trigger('click');
            return false;
        }
    });
});
