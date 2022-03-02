var msgObj = $("#err_common");

$(document).ready(function() {

    msgObj.empty();
    msgObj.hide();

    $(document).on('click', '#btnChange', function() {
        msgObj.empty();
        msgObj.hide();
        $('#currentPassword').val('');
        if ($('#passWord').val().trim()) {
            $('#currentPassword').val(CryptoJS.SHA256($('#passWord').val().trim()).toString().toUpperCase());
        }
        var url = $('#login_form').attr('action');
        var params = {};
        params['type'] = 'POST';
        params['data'] = $('#login_form').serializeArray();
        Common.ajaxCommon(url, params, ForceChangePwd.callbackChangePwd);
        return false;
    });

    // クリアクリック
    $(document).on('click', '#btnClear', function() {
        $('#passWord').val('');
        $('#currentPassword').val('');
        $('#newPassword').val('');
        $('#newPasswordConfirm').val('');
        $("#err_common").hide();
        return false;
    });
});

function ForceChangePwd (){};

ForceChangePwd.callbackChangePwd = function(data, errorCode, jqXHR) {
    var error = jqXHR.getResponseHeader('error');
    if (error) {
        msgObj.show();
        if (data.errors && data.errors.length > 0) {
            msgObj.append($("<div>", {"class": "content-box-header panel-heading", "text": data.errors[0].text}));
            if(data.errors[0].args && data.errors[0].args.length > 0){
                $("[name='" + data.errors[0].args.toString() + "']").focus();
            }
            return false;
        }
        return false;
    }
    // ホーム遷移
    window.location.href = $('#homeLink').val();
};
