package com.tencent.qqnt.kernelgpro.nativeinterface;

import java.io.Serializable;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes6.dex */
public final class GProUploadFileInfo implements Serializable {
    public int appType;
    public int errorCode;
    public long fileHeight;
    public long fileWidth;
    public boolean isOriginal;
    public long lastModifyTime;
    public int svrErrorCode;
    public long time;
    public long totalFileSize;
    public long uploadFileSize;
    long serialVersionUID = 1;
    public String fileGroupId = "";
    public String fileId = "";
    public GProUploadStatus uploadStatus = GProUploadStatus.values()[0];
    public String filePath = "";
    public String fileName = "";
    public String sha1 = "";
    public String md5 = "";
    public GProUploadFileType fileType = GProUploadFileType.values()[0];
    public String front512kMd5 = "";
    public String back512kMd5 = "";
    public ArrayList<String> cumulateSha1s = new ArrayList<>();
    public String svrFileId = "";
    public String svrUrl = "";
    public String errorMsg = "";
    public GProAudioCodecFormat audioCodecFormat = GProAudioCodecFormat.values()[0];

    public int getAppType() {
        return this.appType;
    }

    public GProAudioCodecFormat getAudioCodecFormat() {
        return this.audioCodecFormat;
    }

    public String getBack512kMd5() {
        return this.back512kMd5;
    }

    public ArrayList<String> getCumulateSha1s() {
        return this.cumulateSha1s;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public String getFileGroupId() {
        return this.fileGroupId;
    }

    public long getFileHeight() {
        return this.fileHeight;
    }

    public String getFileId() {
        return this.fileId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public GProUploadFileType getFileType() {
        return this.fileType;
    }

    public long getFileWidth() {
        return this.fileWidth;
    }

    public String getFront512kMd5() {
        return this.front512kMd5;
    }

    public boolean getIsOriginal() {
        return this.isOriginal;
    }

    public long getLastModifyTime() {
        return this.lastModifyTime;
    }

    public String getMd5() {
        return this.md5;
    }

    public String getSha1() {
        return this.sha1;
    }

    public int getSvrErrorCode() {
        return this.svrErrorCode;
    }

    public String getSvrFileId() {
        return this.svrFileId;
    }

    public String getSvrUrl() {
        return this.svrUrl;
    }

    public long getTime() {
        return this.time;
    }

    public long getTotalFileSize() {
        return this.totalFileSize;
    }

    public long getUploadFileSize() {
        return this.uploadFileSize;
    }

    public GProUploadStatus getUploadStatus() {
        return this.uploadStatus;
    }

    public String toString() {
        return "GProUploadFileInfo{fileGroupId=" + this.fileGroupId + ",fileId=" + this.fileId + ",appType=" + this.appType + ",fileWidth=" + this.fileWidth + ",fileHeight=" + this.fileHeight + ",uploadStatus=" + this.uploadStatus + ",totalFileSize=" + this.totalFileSize + ",uploadFileSize=" + this.uploadFileSize + ",isOriginal=" + this.isOriginal + ",filePath=" + this.filePath + ",fileName=" + this.fileName + ",sha1=" + this.sha1 + ",md5=" + this.md5 + ",fileType=" + this.fileType + ",lastModifyTime=" + this.lastModifyTime + ",front512kMd5=" + this.front512kMd5 + ",back512kMd5=" + this.back512kMd5 + ",cumulateSha1s=" + this.cumulateSha1s + ",svrFileId=" + this.svrFileId + ",svrUrl=" + this.svrUrl + ",errorCode=" + this.errorCode + ",errorMsg=" + this.errorMsg + ",svrErrorCode=" + this.svrErrorCode + ",time=" + this.time + ",audioCodecFormat=" + this.audioCodecFormat + ",}";
    }
}