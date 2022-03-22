package com.hytc.webmanage.web.biz.notice;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.gt.fw.common.exception.FwWebBusinessException;
import jp.co.gt.fw.common.graphql.CallGraphqlApi;
import jp.co.gt.fw.util.FwBeanUtils;
import jp.co.gt.fw.util.FwDateTimeFormat;
import jp.co.gt.fw.util.FwDateTimeUtils;
import jp.co.jsto.auth.bean.FwUserDetails;
import jp.co.jsto.biz.GraphqlReqName;
import jp.co.jsto.biz.customer.notice.NoticeInfoIn;
import jp.co.jsto.biz.customer.notice.NoticeInfoOut;
import jp.co.jsto.biz.customer.notice.NoticeInfoUpdateIn;
import jp.co.jsto.biz.customer.notice.NoticeInfoUpdateOut;
import jp.co.jsto.code.NoticeStatusCls;
import jp.co.jsto.code.OpenEntryCls;
import jp.co.jsto.code.OpenTargetCls;
import jp.co.jsto.code.OperationCls;
import jp.co.jsto.web.config.convert.WebLogic;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class NoticeService {

    final private CallGraphqlApi callGraphql;

    final private WebLogic webLogic;

    @Value("${jsto.file.server.path}")
    private String serverPath;

    /**
     * お知らせ情報検索
     *
     * @param userDetails
     * @param noticeForm
     * @return
     */
    public NoticeForm searchNoticeByCondition(FwUserDetails userDetails, NoticeForm noticeForm) {
        // GrapHQL API 引数編集
        NoticeInfoIn in = FwBeanUtils.me.createCopy(noticeForm, NoticeInfoIn.class);
        in.setCompanyCd(userDetails.getCompanyCd());
        in.setUserSeqId(BigDecimal.valueOf(Integer.valueOf(userDetails.getUserId())));
        // GrapHQL API 呼出
        NoticeInfoOut noticeInfoOut = callGraphql.doQuery(GraphqlReqName.FIND_NOTICES, in, NoticeInfoOut.class);
        // GrapHQL API異常あり
        if (noticeInfoOut.isNotOK()) {
            throw new FwWebBusinessException(noticeInfoOut);
        }

        List<NoticeDispInfo> noticeInfos = new ArrayList<>();
        noticeInfos.addAll(noticeInfoOut.getNoticeInfoList().stream().map(item -> {
            NoticeDispInfo info = FwBeanUtils.me.createCopy(item, NoticeDispInfo.class);
            info.setCustomerCode(webLogic.nullToSpace(item.getCustomerBranchCd()) + "-" + webLogic.nullToSpace(item.getCustomerId()));
            info.setCustomerName(webLogic.nullToSpace(item.getFamilyName()) + webLogic.nullToSpace(item.getGivenName()));
            info.setApplicantIdStr(webLogic.resolveEnumMessage(OpenTargetCls.values(), item.getApplicantId()));
            info.setOpenEntryDtStr(webLogic.nullToSpace(FwDateTimeUtils.me.localDateTimeToString(item.getOpenEntryDt(), FwDateTimeFormat.yyyyMMddHHmm_SLASH)));
            info.setCreateDtStr(webLogic.nullToSpace(FwDateTimeUtils.me.localDateTimeToString(item.getCreateDt(), FwDateTimeFormat.yyyyMMddHHmm_SLASH)));
            info.setNoticeStatusClsStr(webLogic.resolveEnumMessage(NoticeStatusCls.values(), item.getNoticeStatusCls()));
            info.setNoticeImageByte(webLogic.pathToBase64Byte(item.getNoticeImagePath()));
            return info;
        }).collect(Collectors.toList()));

        noticeForm.setNoticeInfos(noticeInfos);
        return noticeForm;
    }

    /**
     * お知らせ情報登録・更新
     *
     * @param userDetails
     * @param form
     */
    public void doUpdateInsert(FwUserDetails userDetails, NoticeForm form) {
        String path = this.saveNoticeImg(serverPath, form.getImageFile());
        NoticeInfoUpdateIn in = FwBeanUtils.me.createCopy(form, NoticeInfoUpdateIn.class);
        in.setCompanyCd(userDetails.getCompanyCd());
        in.setUserSeqId(BigDecimal.valueOf(Integer.valueOf(userDetails.getUserId())));
        in.setNoticeStatusCls(form.getNoticeStatus());
        in.setNoticeImagePath(path);
        // 掲載日時間
        if (OpenEntryCls.RESERVED.is(form.getOpenEntryCls())) {
            LocalDateTime pareTime = FwDateTimeUtils.me.toLocalDateTime(form.getOpenEntryDt(), FwDateTimeFormat.yyyyMMddHHmm_T_HYPHEN);
            in.setOpenEntryDt(LocalDateTime.of(pareTime.getYear(),pareTime.getMonth(),pareTime.getDayOfMonth(),pareTime.getHour(),pareTime.getMinute()));
        } else if (OpenEntryCls.IMMEDIATE.is(form.getOpenEntryCls())) {
            in.setOpenEntryDt(LocalDateTime.now());
        }
        // 顧客コード編集
        if (ObjectUtils.isNotEmpty(form.getCustomCode())) {
            String[] codeArr = form.getCustomCode().split("-");
            //in.setCustomerBranchCd(codeArr[0]);
            in.setCustomerId(new BigDecimal("1"));
        }
        String content = form.getNoticeContent().replaceAll("\\r", "\\\\r").replaceAll("\\n", "\\\\n");
        String noticePath = path.replaceAll("\\\\", "/");
        in.setNoticeContent(content);
        in.setNoticeImagePath(noticePath);

        // 操作区分より、DB登録・更新を呼出す
        String reqName = GraphqlReqName.INSERT_NOTICE;
        if (OperationCls.UPDATE.is(form.getOperationCls())) {
            reqName = GraphqlReqName.UPDATE_NOTICE;
        }
        //BLを呼びます
        NoticeInfoUpdateOut out = callGraphql.doMutation(reqName, in, NoticeInfoUpdateOut.class);
        if (out.isNotOK()) {
            throw new FwWebBusinessException(out);
        }
    }

    /**
     * お知らせ情報削除
     *
     * @param userDetails
     * @param form
     */
    public void deleteNotice(FwUserDetails userDetails, NoticeForm form) {
        NoticeInfoUpdateIn in = FwBeanUtils.me.createCopy(form, NoticeInfoUpdateIn.class);
        in.setCompanyCd(userDetails.getCompanyCd());
        in.setUserSeqId(BigDecimal.valueOf(Integer.valueOf(userDetails.getUserId())));
        NoticeInfoUpdateOut out = callGraphql.doMutation(GraphqlReqName.DELETE_NOTICE, in, NoticeInfoUpdateOut.class);
        if (out.isNotOK()) {
            throw new FwWebBusinessException(out);
        }
    }

    /**
     * アップロードされたファイル保存
     *
     * @param serverPath
     * @param file
     * @return 保存先ファイルパス
     */
    public String saveNoticeImg(String serverPath, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String dFileName = UUID.randomUUID() + "_" + originalFilename;
        String savePath = serverPath + dFileName;
        try {
            File uploadFile = new File(savePath);
            File filePath = new File(serverPath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.transferTo(uploadFile);
        } catch (IOException e) {
            log.warn("the file path {} is Incorrect.", savePath);
        }
        return savePath;
    }

}

