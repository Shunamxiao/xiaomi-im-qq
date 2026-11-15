package com.tencent.qqnt.kernelgpro.nativeinterface;

import java.util.HashMap;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes6.dex */
public interface IKernelGuildFileUploadListener {
    void onFileCalcHashSuccess(GProUploadFileInfo gProUploadFileInfo);

    void onFileGroupUploadSizeChanged(String str, long j15, long j16);

    void onFileGroupUploadSpeedChanged(String str, long j15, HashMap<String, Long> map);

    void onFileGroupUploadStatusChanged(GProUploadFileGroupInfo gProUploadFileGroupInfo);

    void onFileUploadStatusChanged(GProUploadFileInfo gProUploadFileInfo);
}