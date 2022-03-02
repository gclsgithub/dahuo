
// 顧客情報データテーブル
var customerDataTable = null;

//INIT
$(function () {
	// 顧客情報管理画面の初期化
    Customer.init();

    // windowサイズ変更  Customer:resizeend事件を使用します
    $(window).on("resizeend",function () {
        if(customerDataTable != null) {
            customerDataTable.columns.adjust().draw();
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
    $("#customerSearch").on("click", function () {

		// 顧客コード
		var customerCd = $("#search_customerCd").val();
	    // 部店コード
		var branchCd = customerCd.split('-')[0];
		// 顧客ID
		var customerId = customerCd.split('-')[1];
		// 顧客姓
		var familyName = $("#search_customerName").val();
	    // 顧客名
		var givenName = $("#search_customerName").val();
	    // 顧客氏名
		var customerName = $("#search_customerName").val();
	    // 顧客区分
		var displayCustomerCls = $("#search_customerCls").val();
		// 口座管理区分
		var displayAccountManagementCls = $("#search_accountManagementCls").val();

		// 顧客コード
        $("#hdn_customerCd").val(customerCd);
	    // 部店コード
        $("#hdn_branchCd").val(branchCd);
		// 顧客ID
		$("#hdn_customerId").val(customerId);
		// 顧客姓
		$("#hdn_familyName").val(familyName);
	    // 顧客名
		$("#hdn_givenName").val(givenName);
		// 顧客氏名
		$("#hdn_customerName").val(customerName);
		// 顧客区分
		$("#hdn_displayCustomerCls").val(displayCustomerCls);
		// 口座管理区分
		$("#hdn_displayAccountManagementCls").val(displayAccountManagementCls);

        var formObj = $("#search_customer_form");
        Common.ajaxCommonPost(formObj.attr("action"), formObj.serializeArray(), Customer.searchCallBack);
    });

});

/***********************方法******************************************/

function Customer() {}

/**
 * clear insert
 */
Customer.clearInsertHtml = function(){
	$("#customerCd").val("");
    $("#customerName").val("");

    $("input[name='search_customerCls']").attr("disabled", true);
    $("input[name='search_customerCls']").val("");
    $('input[name=search_customerCls]:eq(1)').prop('checked', true);

    $("input[name='search_accountManagementCls']").val("");
    $("input[name='search_accountManagementCls']").attr("disabled", true);
    $('input[name=search_accountManagementCls]:eq(2)').prop('checked', true);
};

Customer.bindInsertUpdateModal = function(){
};

Customer.initCallBack = function(jsonData, errorCode, errorMs) {
    customerDataTable = $('#customDataTable').DataTable({
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
            "targets": [10]
        }],
        "columns": [
            {"data": "accountManagementCls", "className": "text-center"},
            {"data": "customerCd", "className": "text-center"},
            {"data": "customerCls", "className": "text-center"},
            {"data": "customerName"},
            {"data": "mobilePhoneNumber", "className": "text-center"},
            {"data": "mailAddress"},
            {"data": "definitiveRegistrationDateStr", "className": "text-center"},
            {"data": "confirmationDocumentSubmitDateStr", "className": "text-center"},
            {"data": "approvalDateStr", "className": "text-center"},
            {"data": "authenticationDateStr", "className": "text-center"},
            {
                "data": null,
                "className": "text-center",
                "render": function (obj) {
                    var str = "";
                    str += '<button type="button" class="btn btn-sm btn-warning update">' + details + '</button>';
                    str += "&nbsp;";
                    str += '<button type="button" class="btn btn-sm btn-danger delete">' + change + '</button>';
                    return str;
                }
            }
        ],
        "data": jsonData
    });

    Customer.bindDataTableAction();
};

Customer.initDataTable = function() {
    var formObj = $("#search_customer_form");
    Common.ajaxCommonPost(formObj.attr("action"), formObj.serializeArray(), Customer.initCallBack);
};

/**
 * 顧客情報管理画面の初期化
 */
Customer.init = function() {

	// clear insert
	Customer.clearInsertHtml();

	Customer.bindInsertUpdateModal();

    Customer.initDataTable();

    Customer.clearErrorMsg();
};

/**
 * 検索後、dataTables更新
 */
Customer.searchCallBack = function (jsonData, errorCode, errorMs) {
	// dataTables初期化
    if(customerDataTable == null) {
        Customer.initCallBack(jsonData, errorCode, errorMs);
    }

    // dataTables更新
    customerDataTable.clear().draw();
    customerDataTable.rows.add(jsonData);
    customerDataTable.columns.adjust().draw();
};

/**
 * dataTablesの変更・削除ボタンイベント追加
 */
Customer.bindDataTableAction = function() {
};

Customer.editCallBack = function (data, textStatus, request) {
};


Customer.deleteCallBack = function(data, textStatus, request){
};

Customer.showPreview = function (source,previewId){
};

Customer.clearSingleCheck = function (){
};

Customer.clearErrorMsg = function (){
}
