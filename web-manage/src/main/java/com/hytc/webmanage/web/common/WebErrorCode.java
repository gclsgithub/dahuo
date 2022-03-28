package com.hytc.webmanage.web.common;

import com.hytc.webmanage.common.web.IErrors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebErrorCode implements IErrors<WebErrorCode> {

    E_PL_0000("予期せぬエラーです。"),
    E_PL_0001("表示するデータがありません。"),
    E_PL_0002("{0}を入力してください。"),
    E_PL_0003("{0}を選択してください。"),
    E_PL_0004("{0}は{1}桁以内で入力してください。"),
    E_PL_0005("{0}は{1}以外の指定ができません。"),
    E_PL_0006("開始日は終了日より前で入力してください。"),
    ;

    // I. 以下共通 ====================================
    final private String message;
}
