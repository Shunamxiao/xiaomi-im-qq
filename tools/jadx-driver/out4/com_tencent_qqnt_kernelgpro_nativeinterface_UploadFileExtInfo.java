package com.tencent.qqnt.kernelgpro.nativeinterface;

import java.io.Serializable;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes2.dex */
public final class UploadFileExtInfo implements Serializable {
    public long guildId;
    public int source;
    long serialVersionUID = 1;
    public String feedId = "";

    public String getFeedId() {
        return this.feedId;
    }

    public long getGuildId() {
        return this.guildId;
    }

    public int getSource() {
        return this.source;
    }

    public String toString() {
        return "UploadFileExtInfo{guildId=" + this.guildId + ",source=" + this.source + ",feedId=" + this.feedId + ",}";
    }
}