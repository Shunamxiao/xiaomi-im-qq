package com.tencent.qqnt.kernelgpro.nativeinterface;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes2.dex */
public final class FileTransferSyncReq {
    public int msgBusType;
    public long size;
    public int srcFtnBizId;
    public RichMediaC2CUserInfo c2cUserInfo = new RichMediaC2CUserInfo();
    public RichMediaGroupUserInfo groupUserInfo = new RichMediaGroupUserInfo();
    public String fileUuid = "";
    public String md5 = "";
    public String sha1 = "";
    public String filename = "";
    public UploadFileExtInfo extInfo = new UploadFileExtInfo();

    public RichMediaC2CUserInfo getC2cUserInfo() {
        return this.c2cUserInfo;
    }

    public UploadFileExtInfo getExtInfo() {
        return this.extInfo;
    }

    public String getFileUuid() {
        return this.fileUuid;
    }

    public String getFilename() {
        return this.filename;
    }

    public RichMediaGroupUserInfo getGroupUserInfo() {
        return this.groupUserInfo;
    }

    public String getMd5() {
        return this.md5;
    }

    public int getMsgBusType() {
        return this.msgBusType;
    }

    public String getSha1() {
        return this.sha1;
    }

    public long getSize() {
        return this.size;
    }

    public int getSrcFtnBizId() {
        return this.srcFtnBizId;
    }

    public String toString() {
        return "FileTransferSyncReq{msgBusType=" + this.msgBusType + ",c2cUserInfo=" + this.c2cUserInfo + ",groupUserInfo=" + this.groupUserInfo + ",srcFtnBizId=" + this.srcFtnBizId + ",fileUuid=" + this.fileUuid + ",size=" + this.size + ",md5=" + this.md5 + ",sha1=" + this.sha1 + ",filename=" + this.filename + ",extInfo=" + this.extInfo + ",}";
    }
}