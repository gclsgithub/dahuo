$(function() {
  // メニュークリック
  $(".t-menu").click(function(e) {
    e.preventDefault();
    if($('#headerUserId').length == 1) {
        var userId = $('#headerUserId').val();
        var loginData = localStorage.getItem(userId);
        if(loginData != null){
            var jsonLoginData = JSON.parse(loginData);
            var gmMulti = jsonLoginData.gmMulti;
            if(gmMulti == null) {
                gmMulti = [];
                gmMulti.push($(this).attr("index"));
            } else {
                var idx = $(this).attr("index");
                var newGmMulti = gmMulti.filter(function(n){
                    return n !== idx;
                });
                if(newGmMulti.length == gmMulti.length){
                    gmMulti.push(idx);
                }else{
                    gmMulti = newGmMulti;
                }
            }
            jsonLoginData['gmMulti'] = gmMulti;
            localStorage.setItem(userId, JSON.stringify(jsonLoginData));
        }
    }

    // メインメニュークラス状態保存
    var url = $('#hdbOpenMenu').val();
    var params ={};
    params['type'] = 'POST';
    params['data'] = {
        gmMulti : $(this).attr("index")
    };
    Common.ajaxCommon(url, params, null);
  });

  if($('#headerUserId').length == 1 && typeof funcId !== "undefined") {
      var gmIdx = ''+funcId;
      var userId = $('#headerUserId').val();
      var pathname = location.pathname;
      if($('#menu_block').find('[href="'+pathname+'"]').length != 0){
          var loginData = localStorage.getItem(userId);
          if(loginData != null){
              var jsonLoginData = JSON.parse(loginData);
              var gmMulti = jsonLoginData.gmMulti;
              if(gmMulti == null) {
                  gmMulti = [];
                  gmMulti.push(gmIdx);
              } else {
                  gmMulti = gmMulti.filter(function(n){
                      return n !== gmIdx;
                  });
                  gmMulti.push(gmIdx);
              }
              jsonLoginData['gmMulti'] = gmMulti;
              // 権限なし画面に遷移は設定しない
              if(pathname.indexOf('accessDenied') == -1){
                  jsonLoginData['homeUrl'] = pathname;
              }
              localStorage.setItem(userId, JSON.stringify(jsonLoginData));
          } else {
              loginData = {};
              var gmMulti = [];
              gmMulti.push(gmIdx);
              loginData['gmMulti'] = gmMulti;
              // 権限なし画面に遷移は設定しない
              if(pathname.indexOf('accessDenied') == -1){
                  loginData['homeUrl'] = pathname;
              }
              localStorage.setItem(userId, JSON.stringify(loginData));

              // メインメニュークラス状態保存
              var url = $('#hdbOpenMenu').val();
              var params ={};
              params['type'] = 'POST';
              params['data'] = {
                  gmMulti : gmIdx,
                  openFlg : 'open'
              };
              Common.ajaxCommon(url, params, null);
          }
      }
  }
});
