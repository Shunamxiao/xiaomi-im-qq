package com.tencent.qqnt.kernelgpro.nativeinterface;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes6.dex */
public interface IKernelGuildFileTransferService {

    /* JADX INFO: compiled from: P */
    public static final class CppProxy implements IKernelGuildFileTransferService {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final AtomicBoolean destroyed = new AtomicBoolean(false);
        private final long nativeRef;

        private CppProxy(long j15) {
            if (j15 == 0) {
                throw new RuntimeException("nativeRef is zero");
            }
            this.nativeRef = j15;
        }

        private native void nativeDestroy(long j15);

        private native void native_addFileDownloadListener(long j15, IKernelGuildFileDownloadListener iKernelGuildFileDownloadListener);

        private native long native_addFileUploadListener(long j15, IKernelGuildFileUploadListener iKernelGuildFileUploadListener);

        private native void native_cancelFileGroupUpload(long j15, String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

        private native void native_createFileGroupUpload(long j15, GProCreateUploadFileGroupParam gProCreateUploadFileGroupParam, IGProCreateUploadFileGroupCallback iGProCreateUploadFileGroupCallback);

        private native GProUploadFileGroupInfo native_decoderUploadFileGroupInfo(long j15, byte[] bArr);

        private native GProUploadFileInfo native_decoderUploadFileInfo(long j15, byte[] bArr);

        private native byte[] native_encodeUploadFileGroupInfo(long j15, GProUploadFileGroupInfo gProUploadFileGroupInfo);

        private native byte[] native_encodeUploadFileInfo(long j15, GProUploadFileInfo gProUploadFileInfo);

        private native void native_fileTransferSync(long j15, FileTransferSyncReq fileTransferSyncReq, IGProFileTransferSyncCallback iGProFileTransferSyncCallback);

        private native String native_getBaseFilePath(long j15, boolean z15);

        private native void native_getFileGroupInfo(long j15, String str, IGProGetUploadFileGroupInfoCallback iGProGetUploadFileGroupInfoCallback);

        private native ArrayList<GProDownloadInfo> native_getLocalDownloadInfo(long j15, ArrayList<String> arrayList);

        private native void native_getLocalDownloadInfoAsyn(long j15, ArrayList<String> arrayList, IGProGetDownloadFileInfoCallback iGProGetDownloadFileInfoCallback);

        private native void native_pauseFileGroupUpload(long j15, String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

        private native void native_removeFileDownloadListener(long j15, IKernelGuildFileDownloadListener iKernelGuildFileDownloadListener);

        private native void native_removeFileGroupUpload(long j15, String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

        private native void native_removeFileUploadListener(long j15, long j16);

        private native void native_resumeFileGroupUpload(long j15, String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

        private native void native_startFileDownload(long j15, GProDownloadInfo gProDownloadInfo, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

        private native void native_stopFileDownload(long j15, GProDownloadInfo gProDownloadInfo, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

        public void _djinni_private_destroy() {
            if (this.destroyed.getAndSet(true)) {
                return;
            }
            nativeDestroy(this.nativeRef);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void addFileDownloadListener(IKernelGuildFileDownloadListener iKernelGuildFileDownloadListener) {
            native_addFileDownloadListener(this.nativeRef, iKernelGuildFileDownloadListener);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public long addFileUploadListener(IKernelGuildFileUploadListener iKernelGuildFileUploadListener) {
            return native_addFileUploadListener(this.nativeRef, iKernelGuildFileUploadListener);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void cancelFileGroupUpload(String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback) {
            native_cancelFileGroupUpload(this.nativeRef, str, iGProTransferFileOperateCallback);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void createFileGroupUpload(GProCreateUploadFileGroupParam gProCreateUploadFileGroupParam, IGProCreateUploadFileGroupCallback iGProCreateUploadFileGroupCallback) {
            native_createFileGroupUpload(this.nativeRef, gProCreateUploadFileGroupParam, iGProCreateUploadFileGroupCallback);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public GProUploadFileGroupInfo decoderUploadFileGroupInfo(byte[] bArr) {
            return native_decoderUploadFileGroupInfo(this.nativeRef, bArr);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public GProUploadFileInfo decoderUploadFileInfo(byte[] bArr) {
            return native_decoderUploadFileInfo(this.nativeRef, bArr);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public byte[] encodeUploadFileGroupInfo(GProUploadFileGroupInfo gProUploadFileGroupInfo) {
            return native_encodeUploadFileGroupInfo(this.nativeRef, gProUploadFileGroupInfo);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public byte[] encodeUploadFileInfo(GProUploadFileInfo gProUploadFileInfo) {
            return native_encodeUploadFileInfo(this.nativeRef, gProUploadFileInfo);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void fileTransferSync(FileTransferSyncReq fileTransferSyncReq, IGProFileTransferSyncCallback iGProFileTransferSyncCallback) {
            native_fileTransferSync(this.nativeRef, fileTransferSyncReq, iGProFileTransferSyncCallback);
        }

        protected void finalize() throws Throwable {
            _djinni_private_destroy();
            super.finalize();
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public String getBaseFilePath(boolean z15) {
            return native_getBaseFilePath(this.nativeRef, z15);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void getFileGroupInfo(String str, IGProGetUploadFileGroupInfoCallback iGProGetUploadFileGroupInfoCallback) {
            native_getFileGroupInfo(this.nativeRef, str, iGProGetUploadFileGroupInfoCallback);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public ArrayList<GProDownloadInfo> getLocalDownloadInfo(ArrayList<String> arrayList) {
            return native_getLocalDownloadInfo(this.nativeRef, arrayList);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void getLocalDownloadInfoAsyn(ArrayList<String> arrayList, IGProGetDownloadFileInfoCallback iGProGetDownloadFileInfoCallback) {
            native_getLocalDownloadInfoAsyn(this.nativeRef, arrayList, iGProGetDownloadFileInfoCallback);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void pauseFileGroupUpload(String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback) {
            native_pauseFileGroupUpload(this.nativeRef, str, iGProTransferFileOperateCallback);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void removeFileDownloadListener(IKernelGuildFileDownloadListener iKernelGuildFileDownloadListener) {
            native_removeFileDownloadListener(this.nativeRef, iKernelGuildFileDownloadListener);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void removeFileGroupUpload(String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback) {
            native_removeFileGroupUpload(this.nativeRef, str, iGProTransferFileOperateCallback);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void removeFileUploadListener(long j15) {
            native_removeFileUploadListener(this.nativeRef, j15);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void resumeFileGroupUpload(String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback) {
            native_resumeFileGroupUpload(this.nativeRef, str, iGProTransferFileOperateCallback);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void startFileDownload(GProDownloadInfo gProDownloadInfo, IGProTransferFileOperateCallback iGProTransferFileOperateCallback) {
            native_startFileDownload(this.nativeRef, gProDownloadInfo, iGProTransferFileOperateCallback);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService
        public void stopFileDownload(GProDownloadInfo gProDownloadInfo, IGProTransferFileOperateCallback iGProTransferFileOperateCallback) {
            native_stopFileDownload(this.nativeRef, gProDownloadInfo, iGProTransferFileOperateCallback);
        }
    }

    void addFileDownloadListener(IKernelGuildFileDownloadListener iKernelGuildFileDownloadListener);

    long addFileUploadListener(IKernelGuildFileUploadListener iKernelGuildFileUploadListener);

    void cancelFileGroupUpload(String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

    void createFileGroupUpload(GProCreateUploadFileGroupParam gProCreateUploadFileGroupParam, IGProCreateUploadFileGroupCallback iGProCreateUploadFileGroupCallback);

    GProUploadFileGroupInfo decoderUploadFileGroupInfo(byte[] bArr);

    GProUploadFileInfo decoderUploadFileInfo(byte[] bArr);

    byte[] encodeUploadFileGroupInfo(GProUploadFileGroupInfo gProUploadFileGroupInfo);

    byte[] encodeUploadFileInfo(GProUploadFileInfo gProUploadFileInfo);

    void fileTransferSync(FileTransferSyncReq fileTransferSyncReq, IGProFileTransferSyncCallback iGProFileTransferSyncCallback);

    String getBaseFilePath(boolean z15);

    void getFileGroupInfo(String str, IGProGetUploadFileGroupInfoCallback iGProGetUploadFileGroupInfoCallback);

    ArrayList<GProDownloadInfo> getLocalDownloadInfo(ArrayList<String> arrayList);

    void getLocalDownloadInfoAsyn(ArrayList<String> arrayList, IGProGetDownloadFileInfoCallback iGProGetDownloadFileInfoCallback);

    void pauseFileGroupUpload(String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

    void removeFileDownloadListener(IKernelGuildFileDownloadListener iKernelGuildFileDownloadListener);

    void removeFileGroupUpload(String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

    void removeFileUploadListener(long j15);

    void resumeFileGroupUpload(String str, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

    void startFileDownload(GProDownloadInfo gProDownloadInfo, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);

    void stopFileDownload(GProDownloadInfo gProDownloadInfo, IGProTransferFileOperateCallback iGProTransferFileOperateCallback);
}