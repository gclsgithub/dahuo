package com.hytc.webmanage.web.config;

/**
 * @RequestMapping URL定義クラス。
 */
public final class MappingMaster {
    // new出来ないように制御
    private MappingMaster() {
    }

    private static final String startsWith = "/";
    private static final String wildcard = "/**";

    /**
     * リソース系
     */
    public static class STATIC {
        public static final String js = "/js";
        public static final String js$startsWith = js + startsWith;
        public static final String js$wildcard = js + wildcard;

        public static final String css = "/css";
        public static final String css$startsWith = css + startsWith;
        public static final String css$wildcard = css + wildcard;

        public static final String font = "/font";
        public static final String font$startsWith = font + startsWith;
        public static final String font$wildcard = font + wildcard;

        public static final String img = "/img";
        public static final String img$startsWith = img + startsWith;
        public static final String img$wildcard = img + wildcard;

        public static final String mozilla = "/mozilla";
        public static final String mozilla$wildcard = mozilla + wildcard;

        public static final String adminlte = "/adminlte";
        public static final String adminlte$startsWith = adminlte + startsWith;
        public static final String adminlte$wildcard = adminlte + wildcard;

        public static final String dataTables = "/dataTables";
        public static final String dataTables$startsWith = dataTables + startsWith;
        public static final String dataTables$wildcard = dataTables + wildcard;
    }

    /**
     * 認証系。
     */
    public static class AUTH {
        public static final String LOGIN = "/login";
        public static final String LOGOUT = "/logout";
        public static final String TIMEOUT = "/timeout";
        public static final String AUTH_LOGIN = "/authLogin";
        // 権限なし
        public static final String ACCESS_DENIED = "/accessDenied";
    }

    /**
     * 共通系
     */
    // メニュー開く制御
    public static final String OPEN_MENU = "/openMenu";
    // ダッシュボード
    public static final String DASHBOARD = "/dashboard";

    /**
     * 顧客管理系
     */
    // お知らせ
    public static final String NOTICE = "/notice";
    // お知らせを検索
    public static final String SEARCH_NOTICE = "/searchNotice";
    // お知らせ登録・更新
    public static final String EDIT_NOTICE = "/editNotice";
    // お知らせを削除
    public static final String DELETE_NOTICE = "/deleteNotice";
    // お知らせを更新
    public static final String UPDATE_NOTICE = "/updateNotice";

    // 顧客情報参照
    public static final String CUSTOMER = "/customer";
    // 顧客情報参照　検索
    public static final String SEARCH_CUSTOMER = "/searchCustomer";;
    // 顧客情報参照　登録・更新
    public static final String EDIT_CUSTOMER = "/editCustomer";;
    // 顧客情報参照　削除
    public static final String DELETE_CUSTOMER = "/deleteCustomer";;
    // 顧客情報参照　更新
    public static final String UPDATE_CUSTOMER = "/updateCustomer";

    /**
    *
    */
   public static final String APPLICANT = "/applicant";
   public static final String SEARCH_APPLICANT = "/searchApplicant";
   public static final String DENY_APPLICANT = "/denyApplicant";

    /**
     * ユーザ管理系。
     */
    // パスワード強制変更
    public static final String FORCE_CHANGE_PWD = "/forceChangePwd";
    // パスワード変更処理
    public static final String DO_CHANGE_PWD = "/doChangePwd";

}
