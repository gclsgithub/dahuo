var applicantDataTable = null;
//INIT
$(function () {
    Applicant.init();

    // windowサイズ変更  NOTICE:resizeend事件を使用します
    $(window).on("resizeend",function () {
        if(applicantDataTable != null) {
            applicantDataTable.columns.adjust().draw();
        }
    });

    Applicant.clearErrorMsg();

    // 検索ボタンクリックイベント
    $("#searchBtn").on("click", function () {
        var displayApplicantStatus = $("#search_displayApplicantStatus").val();
        var displayTransactionCls = $("#search_displayTransactionCls").val();
        var displayTrandingCls = $("#search_displayTrandingCls").val();
        var customCd = $("#search_customCd").val();
        var pageSize = $("select[name='customDataTable_length']").val();

        $("#hdn_statusCls").val(displayApplicantStatus);
        $("#hdn_transactionCls").val(displayTransactionCls);
        $("#hdn_trandingCls").val(displayTrandingCls);
        $("#hdn_customerCd").val(customCd);
        $("#hdn_pageCount").val(pageSize);

        var formObj = $("#search_applicant_form");
        Common.ajaxCommonPost(formObj.attr("action"), formObj.serializeArray(), Applicant.searchCallBack);
    });

    // 否認ボタンクリックイベント
    $("#confirm_deny").on('click', function () {
		var denialReasonCls = $("#select_denialReasonCls").val();
		$("#deny_denialReasonCls").val(denialReasonCls);
        var formObj = $("#deny_applicant_form");
        Common.ajaxCommonPost(formObj.attr("action"), formObj.serializeArray(), Applicant.denyCallBack);
    });

});

/***********************方法******************************************/

function Applicant() {}

/**
 * clear insert
 */
Applicant.clearInsertHtml = function(){
    $("#notice_customerCls").val("0000");
    $("#noticeTitle").val("");
    $("input[name='customCode']").val("");
    $("input[name='customCode']").attr("disabled", true);
    $("#importantFlg").prop("checked",false);
    $("textarea[name='noticeContent']").val("");
    $("#imageFile").val("");
    $("#imgBlock").prop("class","col-sm-2 hidden");

    // 掲載予約区分
    // 即時
    $("#openEntryDt").attr("disabled", true);
    $("#openEntryDt").val();
    $('input[name=openEntryCls]:eq(0)').prop('checked', true);

    // 掲載顧客区分
    // 全顧客
    $("#openEntryDt").val();
    $("input[name='customCode']").attr("disabled", true);
    $('input[name=openTargetCls]:eq(0)').prop('checked', true);

    Applicant.clearSingleCheck();
};



Applicant.initCallBack = function(jsonData, errorCode, errorMs) {
    applicantDataTable = $('#customDataTable').DataTable({
        "autoWidth": false,
        "scrollX": true,
        "scrollY": "400px",
        "scrollCollapse": true,
        "colReorder": {
            "order": [],
            "realtime":false
        },
        "fixedColumns":   {
            "leftColumns": 0,
            "rightColumns": 1
        },
        "columnDefs": [{
            "orderable": false,
            "targets": [13]
        }],
        "columns": [
            {
                "data": "applicantId",
                "className": "text-center"
            },
            {
                "data": "customerCode",
                "className": "text-center"
            },
            {"data": "customerName"},
            {"data": "applicantDtStr","className": "text-center"},
            {"data": null, "className": "text-center","render":function (obj){
                var outStr = "";
                if (obj.contractCls == "0000"){
                    outStr = all;
                } else if (obj.contractCls == "0100"){
                    outStr = bilateral;
                } else if (obj.contractCls == "0200"){
                    outStr = mediation;
                }
                return outStr;
            }
            },
            {"data": "friendCustomerCode", "className": "text-center"},
            {"data": null, "className": "text-center","render":function (obj){
                var outTrandingCls = "";
                if (obj.trandingCls == "0000"){
                    outTrandingCls = all;
                } else if (obj.trandingCls == "0100"){
                    outTrandingCls = buy;
                } else if (obj.trandingCls == "0200"){
                    outTrandingCls = sell;
                }
                return outTrandingCls;
            }},
            {"data": "applicantSecurityTokenCd", "className": "text-center"},
            {"data": "symbolId", "className": "text-center"},
            {"data": "applicantNumber", "className": "text-right"},
            {"data": null, "className": "text-center","render":function (obj){
                var outConsiderationDesignationCls = "";
                if (obj.considerationDesignationCls == "0100"){
                    outConsiderationDesignationCls = money;
                } else if (obj.considerationDesignationCls == "0200"){
                    outConsiderationDesignationCls = assetMoney;
                } else if (obj.considerationDesignationCls == "0300"){
                    outConsiderationDesignationCls = moneyAssetMoney;
                } else if (obj.considerationDesignationCls == "0400"){
                    outConsiderationDesignationCls = freeTransfer;
                }
                return outConsiderationDesignationCls;
            }},
            {"data": "applicantAmountStr", "className": "text-right"},
            {"data": null, "className": "text-center","render":function (obj){
                var outApplicantStatus = "";
                if (obj.applicantStatus == "0000"){
                    outApplicantStatus = all;
                } else if (obj.applicantStatus == "0100"){
                    outApplicantStatus = newApplicant;
                } else if (obj.applicantStatus == "0200"){
                    outApplicantStatus = denial;
                } else if (obj.applicantStatus == "0300"){
                    outApplicantStatus = friendConsent;
                } else if (obj.applicantStatus == "0400"){
                    outApplicantStatus = friendDeniaed;
                } else if (obj.applicantStatus == "0500"){
                    outApplicantStatus = matching;
                } else if (obj.applicantStatus == "0600"){
                    outApplicantStatus = applicationDeadline;
                }
                return outApplicantStatus;
            }},
            {
                "data": null,
                "className": "text-center",
                "render": function (obj) {
                    var str = "";
                    str += '<button type="button" class="btn deny w-50 px-2 ';
                    var contractCls = obj.contractCls;
                    var applicantStatus = obj.applicantStatus;
                    if (applicantStatus == "0200" || contractCls == "0200"){
                        str += ' btn-block disabled';
                    } else {
                        str += ' btn-danger';
                    }
                    str +='">' + deny + '</button>';
                    return str;
                }
            }
        ],
        "data": jsonData
    });

    Applicant.bindDataTableAction();

};

Applicant.initDataTable = function() {
    var formObj = $("#search_applicant_form");
    Common.ajaxCommonPost(formObj.attr("action"), formObj.serializeArray(), Applicant.initCallBack);
};

/**
 * 画面の初期化
 */
Applicant.init = function() {
    Applicant.clearInsertHtml();

    Applicant.initDataTable();

    Applicant.clearErrorMsg();
};

/**
 * 検索後、dataTables更新
 */
Applicant.searchCallBack = function (jsonData, errorCode, errorMs) {
    // dataTables初期化
    if(applicantDataTable == null) {
        Applicant.initCallBack(jsonData, errorCode, errorMs);
    }

    // dataTables更新
    applicantDataTable.clear().draw();
    applicantDataTable.rows.add(jsonData);
    applicantDataTable.columns.adjust().draw();
};

/**
 * dataTablesの否認
 */
Applicant.bindDataTableAction = function() {

    /* 否認ボタンボタンクリック */
    $('#customDataTable_wrapper').on('click', '.deny', function(that) {
        // modals 二重チェック
        if ($("#confirm_modal_deny").hasClass("visible")) {
            return;
        }
        if ($(that.currentTarget).hasClass("disabled")){
            return;
        }
        Applicant.clearErrorMsg();
        var rowData = applicantDataTable.row($(this).parents('tr')).data();
        $("#deny_applicantId").val(rowData.applicantId);
        document.getElementById("appId").innerHTML=rowData.applicantId;
        $("#deny_applicantStatusDt").val(rowData.applicantStatusDt);
        $('#confirm_modal_deny').modal({backdrop:"static", keyboard: false});
    });

    //完了
    $("#completeConfirm").on('click',function (){
        $('#completeMsg').modal('hide');
        $("#newNotice_modal").css('z-index', 1050);
        $("#searchBtn").click();
    })
};


Applicant.denyCallBack = function(data, textStatus, request){
    //単項目エラーを消します
    Applicant.clearSingleCheck();

    var error = request.getResponseHeader('error');
    if (error) {
        //更新エラー
        var type = request.getResponseHeader('type');
        if (type === 'json') {
            //
        } else {
            $('#denyBody').html($(data).html());
        };
        $(".error").css("display","block");
    } else {
        $('#confirm_modal_deny').modal('hide');
        $('#completeMsg').modal({backdrop:"static", keyboard: false});
    }
};

Applicant.clearSingleCheck = function (){
    $(".error").css("display","none");
    $(".err_common").empty();
    $(".is-invalid").removeClass("is-invalid");
    $(".invalid-feedback").remove();
}
Applicant.clearErrorMsg = function (){
    $("#select_denialReasonCls").on("change",function (){
        $("#select_denialReasonCls").removeClass('is-invalid');
        $("#applicantSelect_error").hide();
    });
}
