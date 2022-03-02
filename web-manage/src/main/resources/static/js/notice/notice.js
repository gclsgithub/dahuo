var noticeDataTable = null;
//INIT
$(function () {
    Notice.init();

    // windowサイズ変更  NOTICE:resizeend事件を使用します
    $(window).on("resizeend",function () {
        if(noticeDataTable != null) {
            noticeDataTable.columns.adjust().draw();
        }
    });

    // 顧客区分変更イベント
    $("#customerCls").on('change', function () {
        var nowKbn = $(this).val();
        // 顧客別の判定
        if (nowKbn == "0200") {
            $("#customCd").attr("disabled", false);
        } else {
            $("#customCd").attr("disabled", true);
            $("#customCd").val("");
        }
    });

    // 検索ボタンクリックイベント
    $("#searchBtn").on("click", function () {
        var customerCls = $("#search_customerCls").val();
        var customCd = $("#search_customCd").val();
        var noticeTitle = $("#search_noticeTitle").val();
        var noticeStatusCls = $("#search_noticeStatusCls").val();
        var pageSize = $("select[name='customDataTable_length']").val();

        $("#hdn_customerCls").val(customerCls);
        $("#hdn_customerSearchKey").val(customCd);
        $("#hdn_noticeTitle").val(noticeTitle);
        $("#hdn_noticeStatusCls").val(noticeStatusCls);
        $("#hdn_pageCount").val(pageSize);

        var formObj = $("#search_notice_form");
        Common.ajaxCommonPost(formObj.attr("action"), formObj.serializeArray(), Notice.searchCallBack);
    });

    // 新規登録ボタンクリックイベント
    $("#newBtn").on("click", function () {
        // modals 二重チェック
        if ($("#newNotice_modal").hasClass("visible")) {
            return;
        }
        Notice.clearInsertHtml();
        // 新規登録
        $("#operationCls").val("0100");
        $("#myModalLabel > span").html(noticeInsert);
        $("#newNotice_modal").modal({backdrop:"static", keyboard: false});
    });

    // モーダル確認ボタンクリックイベント
    $("#newNotice_modal #confirm_new").on("click", function () {
        $("#newNotice_modal").css('z-index', 1040);
        var operationCls = $("#operationCls").val();
        if (operationCls == "0200"){
            $('#confirm_modal_update').modal({backdrop:"static", keyboard: false});
        } else if (operationCls == "0100"){
            $('#confirm_modal_insert').modal({backdrop:"static", keyboard: false});
        }
    });

    // 登録おしらせ確認後の操作
    $('#confirm_modal_insert').on('hidden.bs.modal', function () {
        $("#newNotice_modal").css('z-index', 1050);
        $("body").attr("class", "modal-open");
    });

    // 更新おしらせ確認後の操作
    $('#confirm_modal_update').on('hidden.bs.modal', function () {
        $("#newNotice_modal").css('z-index', 1050);
        $("body").attr("class", "modal-open");
    });

    $("#confirm_update").on("click",function (){
        $("#confirm_modal_update").modal("hide");
        var formObj = $("#edit_notice_form");
        var formData  = new FormData($(formObj)[0]);
        Common.ajaxCommonPostFile(formObj.attr("action"), formData, Notice.editCallBack);
    });

    $("#confirm_insert").on("click",function (){
        $("#confirm_modal_insert").modal("hide");
        var formObj = $("#edit_notice_form");
        var formData  = new FormData($(formObj)[0]);
        Common.ajaxCommonPostFile(formObj.attr("action"), formData, Notice.editCallBack);
    });

    // 削除ボタンクリックイベント
    $("#confirm_delete").on('click', function () {
        var formObj = $("#delete_notice_form");
        Common.ajaxCommonPost(formObj.attr("action"), formObj.serializeArray(), Notice.deleteCallBack);
    });
});

/***********************方法******************************************/

function Notice() {}

/**
 * clear insert
 */
Notice.clearInsertHtml = function(){
    $("#notice_customerCls").val("");
    $("#noticeTitle").val("");

    // 掲載予約区分
    // 即時
    $("#openEntryDt").attr("disabled", true);
    $("#openEntryDt").val("");
    $('input[name=openEntryCls]:eq(0)').prop('checked', true);

    // 掲載顧客区分
    // 全顧客
    $("input[name='customCode']").val("");
    $("input[name='customCode']").attr("disabled", true);
    $('input[name=openTargetCls]:eq(0)').prop('checked', true);

    $("#importantFlg").prop("checked", false);
    $("textarea[name='noticeContent']").val("");
    $("#imageFile").val("");
    $("#imgBlock").prop("class","col-sm-2 hidden");

    Notice.clearSingleCheck();
};

Notice.bindInsertUpdateModal = function(){
    $("#newNotice_modal input[name='openEntryCls']").on("change",function (){
        var openEntryDtCls = $("input[name='openEntryCls']:checked").val();
        if (openEntryDtCls == "0200") {
            $("#openEntryDt").attr("disabled",false);
        } else {
            $("#openEntryDt").val("");
            $("#openEntryDt").attr("disabled",true);
        }
    });

    $("#newNotice_modal input[name='openTargetCls']").on("change", function () {
        var openTargetCls = $("input[name='openTargetCls']:checked").val();
        if (openTargetCls == "0200") {
            $("input[name='customCode']").attr("disabled", false);
        } else {
            $("input[name='customCode']").val("");
            $("input[name='customCode']").attr("disabled", true);
        }
    });

    $("#newNotice_modal #imageFile").on("change",function (){
        var imgpath = $("#imageFile").val();
        if (imgpath != undefined && imgpath != ''){
            var imgContent = $("#imageFile")[0].files[0];
            Notice.showPreview(imgContent,"preview");
            $("#imgBlock").prop("class","col-sm-2");
        } else {
            $("#imgBlock").prop("class","col-sm-2 hidden");
        }
    });

    Notice.clearErrorMsg();
};

Notice.initCallBack = function(jsonData, errorCode, errorMs) {
    noticeDataTable = $('#customDataTable').DataTable({
        "autoWidth": false,
        "colReorder": {
            "order": [],
            "realtime":false
        },
        "scrollX": true,
        "scrollY": "400px",
        "scrollCollapse": true,
        "fixedColumns":   {
            "leftColumns": 0,
            "rightColumns": 1
        },
        "columnDefs": [{
            "orderable": false,
            "targets": [7]
        }],
        "columns": [
            {
                "data": "applicantIdStr",
                "className": "text-center"
            },
            {
                "data": "customerCode"
            },
            {"data": "customerName"},
            {"data": "noticeTitle"},
            {"data": "openEntryDtStr", "className": "text-center"},
            {"data": "createDtStr", "className": "text-center"},
            {"data": "noticeStatusClsStr", "className": "text-center"},
            {
                "data": null,
                "className": "text-center",
                "render": function (obj) {
                    var str = "";
                    str += '<button type="button" class="btn btn-sm btn-warning update">' + change + '</button>';
                    str += "&nbsp;";
                    str += '<button type="button" class="btn btn-sm btn-danger delete">' + myelete + '</button>';
                    return str;
                }
            }
        ],
        "data": jsonData
    });

    Notice.bindDataTableAction();

};

Notice.initDataTable = function() {
    var formObj = $("#search_notice_form");
    Common.ajaxCommonPost(formObj.attr("action"), formObj.serializeArray(), Notice.initCallBack);
};

/**
 * 画面の初期化
 */
Notice.init = function() {
    Notice.clearInsertHtml();

    Notice.bindInsertUpdateModal();

    Notice.initDataTable();

    Notice.clearErrorMsg();
};

/**
 * 検索後、dataTables更新
 */
Notice.searchCallBack = function (jsonData, errorCode, errorMs) {
    // dataTables初期化
    if(noticeDataTable == null) {
        Notice.initCallBack(jsonData, errorCode, errorMs);
    }

    // dataTables更新
    noticeDataTable.clear().draw();
    noticeDataTable.rows.add(jsonData);
    noticeDataTable.columns.adjust().draw();
};

/**
 * dataTablesの変更・削除ボタンイベント追加
 */
Notice.bindDataTableAction = function() {
    /* 変更ボタンボタンクリック */
    $('#customDataTable_wrapper').on('click', '.update', function() {
        // modals 二重チェック
        if ($("#newNotice_modal").hasClass("visible")) {
            return;
        }
        var rowData = noticeDataTable.row($(this).parents('tr')).data();
        // タイトル
        $("#noticeId").val(rowData.noticeId);
        // ステータス
        $("#notice_customerCls").val(rowData.noticeStatusCls);
        // 掲載予約区分
        var openEntryCls = rowData.openEntryCls;
        $("input[name='openEntryCls']").val([openEntryCls]);
        if (openEntryCls == "0200") {            // 掲載予約
            var openEntryTime = new Date(rowData.openEntryDtStr).Format("yyyy-MM-ddThh:mm");
            $("#openEntryDt").val(openEntryTime);
            $("#openEntryDt").attr("disabled",false);
        } else {            // 即時
            $("#openEntryDt").val("");
            $("#openEntryDt").attr("disabled", true);
        }
        // タイトル
        $("#noticeTitle").val(rowData.noticeTitle);
        // 掲載顧客区分
        var openTargetCls = rowData.openTargetCls;
        $("input[name='openTargetCls']").val([openTargetCls]);
        if ( openTargetCls == "0200"){
            // 顧客別
            $("input[name='customCode']").attr("disabled", false);
            $("input[name='customCode']").val(rowData.customerCode);
        } else if (openTargetCls == "0100"){
            // 全顧客
            $("input[name='customCode']").attr("disabled", true);
            $("input[name='customCode']").val("");
        }
        // 重要なお知らせ
        if (importantFlg == "Y") {
            $("#importantFlg").prop("checked", true);
        }else {
            $("#importantFlg").prop("checked", false);
        }
        // 内容
        $("textarea[name='noticeContent']").val(rowData.noticeContent);
        // サムネイル画像
        $("#preview").attr("src","data:image/png;base64," + rowData.noticeImageByte)
        $("#imgBlock").prop("class","col-sm-2");

        // 変更
        $("#operationCls").val("0200");
        $("#myModalLabel > span").html(noticeUpdate);
        $("#newNotice_modal").modal({backdrop:"static", keyboard: false});

        Notice.clearSingleCheck();
    });

    /* 削除ボタンボタンクリック */
    $('#customDataTable_wrapper').on('click', '.delete', function() {
        // modals 二重チェック
        if ($("#confirm_modal_delete").hasClass("visible")) {
            return;
        }
        var rowData = noticeDataTable.row($(this).parents('tr')).data();
        $("#delete_noticeId").val(rowData.noticeId);
        $('#confirm_modal_delete').modal({backdrop:"static", keyboard: false});
    });

    //完了
    $("#completeConfirm").on('click',function (){
        $('#completeMsg').modal('hide');
        $("#newNotice_modal").css('z-index', 1050);
        $("#searchBtn").click();
    })
};

Notice.editCallBack = function (data, textStatus, request) {
    //単項目エラーを消します
    Notice.clearSingleCheck();

    var divObj = $('#newNotice_modal');
    var error = request.getResponseHeader('error');
    if (error) {
        //更新エラー
        var type = request.getResponseHeader('type');
        if (type === 'json') {
            var msgObj = $(".err_common");
            msgObj.empty();
            $.each(data.errors, function(index, value){
                msgObj.append("<span class='error invalid-feedback'>"+value.text+"</span>");
            })

            msgObj.show();
        } else {
            $('#inserUpdateModalBody').html($(data).html());
        };
        $(".error").css("display","block");
        Notice.bindInsertUpdateModal();

        // 掲載予約区分
        var openEntryCls = $("input[name='openEntryCls']:checked").val();
        if (openEntryCls == "0200") {
            // 掲載予約
            $("#openEntryDt").attr("disabled",false);
            $('input[name=openEntryCls]:eq(1)').prop('checked', true);
        } else {
            // 即時
            $("#openEntryDt").attr("disabled", true);
            $("#openEntryDt").val();
            $('input[name=openEntryCls]:eq(0)').prop('checked', true);
        }
        // 掲載顧客区分
        var openTargetCls = $("input[name='openTargetCls']:checked").val();
        if ( openTargetCls == "0200"){
            // 顧客別
            $("input[name='customCode']").attr("disabled", false);
            $('input[name=openTargetCls]:eq(1)').prop('checked', true);
        } else if (openTargetCls == "0100"){
            // 全顧客
            $("#openEntryDt").val();
            $("input[name='customCode']").attr("disabled", true);
            $('input[name=openTargetCls]:eq(0)').prop('checked', true);
        }
    } else {
        // 登録・更新成功
        $(".err_common").empty();
        $('#newNotice_modal').modal('hide');
        $('#completeMsg').modal({backdrop:"static", keyboard: false});
    }
};


Notice.deleteCallBack = function(data, textStatus, request){
    $('#confirm_modal_delete').modal('hide');
    $('#completeMsg').modal({backdrop:"static", keyboard: false});
};

Notice.showPreview = function (source,previewId){
    var url =window.URL.createObjectURL(source);
    $("#"+previewId).attr('src',url);
};

Notice.clearSingleCheck = function (){
    $(".error").css("display","none");
    $(".err_common").empty();
    $(".is-invalid").removeClass("is-invalid");
    $(".invalid-feedback").remove();
};

Notice.clearErrorMsg = function (){
    $("input[name='customCode']").on("blur",function (){
        $("input[name='customCode']").removeClass('is-invalid');
        $("#customCode_error").hide();
    });
    $("#openTargetCls1").on("change",function (){
        $("input[name='customCode']").removeClass('is-invalid');
        $("#customCode_error").hide();
    });
    $("#noticeTitle").on("blur",function (){
        $("#noticeTitle").removeClass('is-invalid');
        $("#noticeTitle_error").hide();
    });
    $("input[name='openEntryCls']").on("change",function (){
        $("#openEntryDt").removeClass('is-invalid');
        $("#openEntryDt_error").hide();
    });
    $("#openEntryDt").on("change",function (){
        $("#openEntryDt").removeClass('is-invalid');
        $("#openEntryDt_error").hide();
    });
};
