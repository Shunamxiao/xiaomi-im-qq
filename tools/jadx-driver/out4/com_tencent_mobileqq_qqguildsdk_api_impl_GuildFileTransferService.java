package com.tencent.mobileqq.qqguildsdk.api.impl;

import com.tencent.qqnt.kernelgpro.nativeinterface.FileTransferSyncReq;
import com.tencent.qqnt.kernelgpro.nativeinterface.FileTransferSyncRsp;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProCreateUploadFileGroupParam;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProDownloadInfo;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProUploadFileGroupInfo;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProCreateUploadFileGroupCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProFileTransferSyncCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProGetDownloadFileInfoCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileDownloadListener;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileUploadListener;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes8.dex */
public final class GuildFileTransferService extends BaseService<Object, IKernelGuildFileTransferService> implements re2.h {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public GuildFileTransferService(@Nullable IKernelGuildFileTransferService iKernelGuildFileTransferService, @NotNull ServiceContent content) {
        super("GuildFileTransferService", content, iKernelGuildFileTransferService);
        Intrinsics.checkNotNullParameter(content, "content");
    }

    @Override // re2.h
    public void E(@NotNull final ArrayList<String> fileIds, @NotNull final IGProGetDownloadFileInfoCallback cb5) {
        Intrinsics.checkNotNullParameter(fileIds, "fileIds");
        Intrinsics.checkNotNullParameter(cb5, "cb");
        M1("getLocalDownloadInfoAsync", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$getLocalDownloadInfoAsync$1
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                cb5.onResult(i15, msg2, new ArrayList<>());
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$getLocalDownloadInfoAsync$2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$getLocalDownloadInfoAsync$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function4<IGProGetDownloadFileInfoCallback, Integer, String, ArrayList<GProDownloadInfo>, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(4, IGProGetDownloadFileInfoCallback.class, "onResult", "onResult(ILjava/lang/String;Ljava/util/ArrayList;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function4
                public /* bridge */ /* synthetic */ Unit invoke(IGProGetDownloadFileInfoCallback iGProGetDownloadFileInfoCallback, Integer num, String str, ArrayList<GProDownloadInfo> arrayList) {
                    invoke(iGProGetDownloadFileInfoCallback, num.intValue(), str, arrayList);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProGetDownloadFileInfoCallback p15, int i15, String str, ArrayList<GProDownloadInfo> arrayList) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onResult(i15, str, arrayList);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                ArrayList<String> arrayList = fileIds;
                final Function3<P1, P2, P3, Unit> function3K1 = this.K1(bypass, cb5, AnonymousClass1.INSTANCE);
                service.getLocalDownloadInfoAsyn(arrayList, new IGProGetDownloadFileInfoCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.ff
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProGetDownloadFileInfoCallback
                    public final void onResult(int i15, String str, ArrayList arrayList2) {
                        Function3 tmp0 = function3K1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.invoke(Integer.valueOf(i15), str, arrayList2);
                    }
                });
            }
        });
    }

    @Override // re2.h
    public void addFileDownloadListener(@NotNull final IKernelGuildFileDownloadListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        I1(listener, new Function1<Object, Long>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.addFileDownloadListener.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function1
            @Nullable
            public final Long invoke(@NotNull Object it) {
                Intrinsics.checkNotNullParameter(it, "it");
                IKernelGuildFileTransferService iKernelGuildFileTransferServiceP1 = GuildFileTransferService.this.P1();
                if (iKernelGuildFileTransferServiceP1 != null) {
                    GuildFileTransferService guildFileTransferService = GuildFileTransferService.this;
                    IKernelGuildFileDownloadListener iKernelGuildFileDownloadListener = listener;
                    guildFileTransferService.getClass();
                    iKernelGuildFileTransferServiceP1.addFileDownloadListener(new ey(guildFileTransferService, iKernelGuildFileDownloadListener));
                }
                return 0L;
            }
        });
    }

    @Override // re2.h
    public void addFileUploadListener(@NotNull IKernelGuildFileUploadListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        I1(listener, new Function1<Object, Long>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.addFileUploadListener.1
            {
                super(1);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function1
            @Nullable
            public final Long invoke(@NotNull Object it) {
                Intrinsics.checkNotNullParameter(it, "it");
                IKernelGuildFileTransferService iKernelGuildFileTransferServiceP1 = GuildFileTransferService.this.P1();
                if (iKernelGuildFileTransferServiceP1 == null) {
                    return null;
                }
                GuildFileTransferService guildFileTransferService = GuildFileTransferService.this;
                guildFileTransferService.getClass();
                return Long.valueOf(iKernelGuildFileTransferServiceP1.addFileUploadListener(new fe(guildFileTransferService, (IKernelGuildFileUploadListener) it)));
            }
        });
    }

    @Override // re2.h
    public void c(@NotNull IKernelGuildFileUploadListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        BaseService.S1(this, listener, false, new Function1<Long, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$removeFileUploadListener$1
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Long l15) {
                invoke(l15.longValue());
                return Unit.INSTANCE;
            }

            public final void invoke(long j15) {
                IKernelGuildFileTransferService iKernelGuildFileTransferServiceP1 = this.this$0.P1();
                if (iKernelGuildFileTransferServiceP1 != null) {
                    iKernelGuildFileTransferServiceP1.removeFileUploadListener(j15);
                }
            }
        }, 2, null);
    }

    @Override // re2.h
    public void cancelFileGroupUpload(@NotNull final String fileGroupId, @NotNull final IGProTransferFileOperateCallback callback) {
        Intrinsics.checkNotNullParameter(fileGroupId, "fileGroupId");
        Intrinsics.checkNotNullParameter(callback, "callback");
        M1("cancelFileGroupUpload", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.cancelFileGroupUpload.1
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                IGProTransferFileOperateCallback iGProTransferFileOperateCallback = callback;
                if (iGProTransferFileOperateCallback != null) {
                    iGProTransferFileOperateCallback.onResult(i15, msg2);
                }
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.cancelFileGroupUpload.2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$cancelFileGroupUpload$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function3<IGProTransferFileOperateCallback, Integer, String, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(3, IGProTransferFileOperateCallback.class, "onResult", "onResult(ILjava/lang/String;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function3
                public /* bridge */ /* synthetic */ Unit invoke(IGProTransferFileOperateCallback iGProTransferFileOperateCallback, Integer num, String str) {
                    invoke(iGProTransferFileOperateCallback, num.intValue(), str);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProTransferFileOperateCallback p15, int i15, String str) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onResult(i15, str);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                String str = fileGroupId;
                final Function2<P1, P2, Unit> function2J1 = this.J1(bypass, callback, AnonymousClass1.INSTANCE);
                service.cancelFileGroupUpload(str, new IGProTransferFileOperateCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.er
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback
                    public final void onResult(int i15, String str2) {
                        Function2 tmp0 = function2J1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.mo4invoke(Integer.valueOf(i15), str2);
                    }
                });
            }
        });
    }

    @Override // re2.h
    public void createFileGroupUpload(@NotNull final GProCreateUploadFileGroupParam createParam, @NotNull final IGProCreateUploadFileGroupCallback callback) {
        Intrinsics.checkNotNullParameter(createParam, "createParam");
        Intrinsics.checkNotNullParameter(callback, "callback");
        M1("createFileGroupUploadInfo", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.createFileGroupUpload.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                callback.onCreateUploadFileGroupResult(i15, msg2, createParam.fileGroupInfo);
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.createFileGroupUpload.2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$createFileGroupUpload$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function4<IGProCreateUploadFileGroupCallback, Integer, String, GProUploadFileGroupInfo, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(4, IGProCreateUploadFileGroupCallback.class, "onCreateUploadFileGroupResult", "onCreateUploadFileGroupResult(ILjava/lang/String;Lcom/tencent/qqnt/kernelgpro/nativeinterface/GProUploadFileGroupInfo;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function4
                public /* bridge */ /* synthetic */ Unit invoke(IGProCreateUploadFileGroupCallback iGProCreateUploadFileGroupCallback, Integer num, String str, GProUploadFileGroupInfo gProUploadFileGroupInfo) {
                    invoke(iGProCreateUploadFileGroupCallback, num.intValue(), str, gProUploadFileGroupInfo);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProCreateUploadFileGroupCallback p15, int i15, String str, GProUploadFileGroupInfo gProUploadFileGroupInfo) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onCreateUploadFileGroupResult(i15, str, gProUploadFileGroupInfo);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                GProCreateUploadFileGroupParam gProCreateUploadFileGroupParam = createParam;
                final Function3<P1, P2, P3, Unit> function3K1 = this.K1(bypass, callback, AnonymousClass1.INSTANCE);
                service.createFileGroupUpload(gProCreateUploadFileGroupParam, new IGProCreateUploadFileGroupCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.es
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProCreateUploadFileGroupCallback
                    public final void onCreateUploadFileGroupResult(int i15, String str, GProUploadFileGroupInfo gProUploadFileGroupInfo) {
                        Function3 tmp0 = function3K1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.invoke(Integer.valueOf(i15), str, gProUploadFileGroupInfo);
                    }
                });
            }
        });
    }

    @Override // re2.h
    public void fileTransferSync(@NotNull final FileTransferSyncReq req, @NotNull final IGProFileTransferSyncCallback callback) {
        Intrinsics.checkNotNullParameter(req, "req");
        Intrinsics.checkNotNullParameter(callback, "callback");
        M1("fileTransferSync", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.fileTransferSync.1
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                IGProFileTransferSyncCallback iGProFileTransferSyncCallback = callback;
                if (iGProFileTransferSyncCallback != null) {
                    iGProFileTransferSyncCallback.onResult(i15, msg2, new FileTransferSyncRsp());
                }
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.fileTransferSync.2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$fileTransferSync$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function4<IGProFileTransferSyncCallback, Integer, String, FileTransferSyncRsp, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(4, IGProFileTransferSyncCallback.class, "onResult", "onResult(ILjava/lang/String;Lcom/tencent/qqnt/kernelgpro/nativeinterface/FileTransferSyncRsp;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function4
                public /* bridge */ /* synthetic */ Unit invoke(IGProFileTransferSyncCallback iGProFileTransferSyncCallback, Integer num, String str, FileTransferSyncRsp fileTransferSyncRsp) {
                    invoke(iGProFileTransferSyncCallback, num.intValue(), str, fileTransferSyncRsp);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProFileTransferSyncCallback p15, int i15, String str, FileTransferSyncRsp fileTransferSyncRsp) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onResult(i15, str, fileTransferSyncRsp);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                FileTransferSyncReq fileTransferSyncReq = req;
                final Function3<P1, P2, P3, Unit> function3K1 = this.K1(bypass, callback, AnonymousClass1.INSTANCE);
                service.fileTransferSync(fileTransferSyncReq, new IGProFileTransferSyncCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.et
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProFileTransferSyncCallback
                    public final void onResult(int i15, String str, FileTransferSyncRsp fileTransferSyncRsp) {
                        Function3 tmp0 = function3K1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.invoke(Integer.valueOf(i15), str, fileTransferSyncRsp);
                    }
                });
            }
        });
    }

    @Override // re2.h
    @NotNull
    public List<GProDownloadInfo> getLocalDownloadInfo(@NotNull ArrayList<String> fileIds) {
        Intrinsics.checkNotNullParameter(fileIds, "fileIds");
        IKernelGuildFileTransferService iKernelGuildFileTransferServiceP1 = P1();
        ArrayList<GProDownloadInfo> localDownloadInfo = iKernelGuildFileTransferServiceP1 != null ? iKernelGuildFileTransferServiceP1.getLocalDownloadInfo(fileIds) : null;
        return localDownloadInfo == null ? CollectionsKt.emptyList() : localDownloadInfo;
    }

    @Override // re2.h
    public void pauseFileGroupUpload(@NotNull final String fileGroupId, @NotNull final IGProTransferFileOperateCallback callback) {
        Intrinsics.checkNotNullParameter(fileGroupId, "fileGroupId");
        Intrinsics.checkNotNullParameter(callback, "callback");
        M1("pauseFileGroupUpload", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.pauseFileGroupUpload.1
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                IGProTransferFileOperateCallback iGProTransferFileOperateCallback = callback;
                if (iGProTransferFileOperateCallback != null) {
                    iGProTransferFileOperateCallback.onResult(i15, msg2);
                }
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.pauseFileGroupUpload.2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$pauseFileGroupUpload$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function3<IGProTransferFileOperateCallback, Integer, String, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(3, IGProTransferFileOperateCallback.class, "onResult", "onResult(ILjava/lang/String;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function3
                public /* bridge */ /* synthetic */ Unit invoke(IGProTransferFileOperateCallback iGProTransferFileOperateCallback, Integer num, String str) {
                    invoke(iGProTransferFileOperateCallback, num.intValue(), str);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProTransferFileOperateCallback p15, int i15, String str) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onResult(i15, str);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                String str = fileGroupId;
                final Function2<P1, P2, Unit> function2J1 = this.J1(bypass, callback, AnonymousClass1.INSTANCE);
                service.pauseFileGroupUpload(str, new IGProTransferFileOperateCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.fg
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback
                    public final void onResult(int i15, String str2) {
                        Function2 tmp0 = function2J1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.mo4invoke(Integer.valueOf(i15), str2);
                    }
                });
            }
        });
    }

    @Override // re2.h
    public void removeFileDownloadListener(@NotNull final IKernelGuildFileDownloadListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        BaseService.S1(this, listener, false, new Function1<Long, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.removeFileDownloadListener.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Long l15) {
                invoke(l15.longValue());
                return Unit.INSTANCE;
            }

            public final void invoke(long j15) {
                IKernelGuildFileTransferService iKernelGuildFileTransferServiceP1 = GuildFileTransferService.this.P1();
                if (iKernelGuildFileTransferServiceP1 != null) {
                    iKernelGuildFileTransferServiceP1.removeFileDownloadListener(listener);
                }
            }
        }, 2, null);
    }

    @Override // re2.h
    public void removeFileGroupUpload(@NotNull final String fileGroupId, @NotNull final IGProTransferFileOperateCallback callback) {
        Intrinsics.checkNotNullParameter(fileGroupId, "fileGroupId");
        Intrinsics.checkNotNullParameter(callback, "callback");
        M1("removeFileGroupUpload", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.removeFileGroupUpload.1
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                IGProTransferFileOperateCallback iGProTransferFileOperateCallback = callback;
                if (iGProTransferFileOperateCallback != null) {
                    iGProTransferFileOperateCallback.onResult(i15, msg2);
                }
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.removeFileGroupUpload.2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$removeFileGroupUpload$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function3<IGProTransferFileOperateCallback, Integer, String, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(3, IGProTransferFileOperateCallback.class, "onResult", "onResult(ILjava/lang/String;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function3
                public /* bridge */ /* synthetic */ Unit invoke(IGProTransferFileOperateCallback iGProTransferFileOperateCallback, Integer num, String str) {
                    invoke(iGProTransferFileOperateCallback, num.intValue(), str);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProTransferFileOperateCallback p15, int i15, String str) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onResult(i15, str);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                String str = fileGroupId;
                final Function2<P1, P2, Unit> function2J1 = this.J1(bypass, callback, AnonymousClass1.INSTANCE);
                service.removeFileGroupUpload(str, new IGProTransferFileOperateCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.fh
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback
                    public final void onResult(int i15, String str2) {
                        Function2 tmp0 = function2J1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.mo4invoke(Integer.valueOf(i15), str2);
                    }
                });
            }
        });
    }

    @Override // re2.h
    public void resumeFileGroupUpload(@NotNull final String fileGroupId, @NotNull final IGProTransferFileOperateCallback callback) {
        Intrinsics.checkNotNullParameter(fileGroupId, "fileGroupId");
        Intrinsics.checkNotNullParameter(callback, "callback");
        M1("resumeFileGroupUpload", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.resumeFileGroupUpload.1
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                IGProTransferFileOperateCallback iGProTransferFileOperateCallback = callback;
                if (iGProTransferFileOperateCallback != null) {
                    iGProTransferFileOperateCallback.onResult(i15, msg2);
                }
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.resumeFileGroupUpload.2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$resumeFileGroupUpload$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function3<IGProTransferFileOperateCallback, Integer, String, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(3, IGProTransferFileOperateCallback.class, "onResult", "onResult(ILjava/lang/String;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function3
                public /* bridge */ /* synthetic */ Unit invoke(IGProTransferFileOperateCallback iGProTransferFileOperateCallback, Integer num, String str) {
                    invoke(iGProTransferFileOperateCallback, num.intValue(), str);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProTransferFileOperateCallback p15, int i15, String str) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onResult(i15, str);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                String str = fileGroupId;
                final Function2<P1, P2, Unit> function2J1 = this.J1(bypass, callback, AnonymousClass1.INSTANCE);
                service.resumeFileGroupUpload(str, new IGProTransferFileOperateCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.fi
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback
                    public final void onResult(int i15, String str2) {
                        Function2 tmp0 = function2J1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.mo4invoke(Integer.valueOf(i15), str2);
                    }
                });
            }
        });
    }

    @Override // re2.h
    public void startFileDownload(@NotNull final GProDownloadInfo req, @NotNull final IGProTransferFileOperateCallback cb5) {
        Intrinsics.checkNotNullParameter(req, "req");
        Intrinsics.checkNotNullParameter(cb5, "cb");
        M1("startFileDownload", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.startFileDownload.1
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                cb5.onResult(i15, msg2);
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.startFileDownload.2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$startFileDownload$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function3<IGProTransferFileOperateCallback, Integer, String, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(3, IGProTransferFileOperateCallback.class, "onResult", "onResult(ILjava/lang/String;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function3
                public /* bridge */ /* synthetic */ Unit invoke(IGProTransferFileOperateCallback iGProTransferFileOperateCallback, Integer num, String str) {
                    invoke(iGProTransferFileOperateCallback, num.intValue(), str);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProTransferFileOperateCallback p15, int i15, String str) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onResult(i15, str);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                GProDownloadInfo gProDownloadInfo = req;
                final Function2<P1, P2, Unit> function2J1 = this.J1(bypass, cb5, AnonymousClass1.INSTANCE);
                service.startFileDownload(gProDownloadInfo, new IGProTransferFileOperateCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.fj
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback
                    public final void onResult(int i15, String str) {
                        Function2 tmp0 = function2J1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.mo4invoke(Integer.valueOf(i15), str);
                    }
                });
            }
        });
    }

    @Override // re2.h
    public void stopFileDownload(@NotNull final GProDownloadInfo req, @NotNull final IGProTransferFileOperateCallback cb5) {
        Intrinsics.checkNotNullParameter(req, "req");
        Intrinsics.checkNotNullParameter(cb5, "cb");
        M1("stopFileDownload", new Function2<Integer, String, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.stopFileDownload.1
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(Integer num, String str) {
                invoke(num.intValue(), str);
                return Unit.INSTANCE;
            }

            public final void invoke(int i15, @NotNull String msg2) {
                Intrinsics.checkNotNullParameter(msg2, "msg");
                cb5.onResult(i15, msg2);
            }
        }, new Function2<IKernelGuildFileTransferService, BaseService.a, Unit>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService.stopFileDownload.2

            /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GuildFileTransferService$stopFileDownload$2$1, reason: invalid class name */
            /* JADX INFO: compiled from: P */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class AnonymousClass1 extends FunctionReferenceImpl implements Function3<IGProTransferFileOperateCallback, Integer, String, Unit> {
                public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

                AnonymousClass1() {
                    super(3, IGProTransferFileOperateCallback.class, "onResult", "onResult(ILjava/lang/String;)V", 0);
                }

                @Override // kotlin.jvm.functions.Function3
                public /* bridge */ /* synthetic */ Unit invoke(IGProTransferFileOperateCallback iGProTransferFileOperateCallback, Integer num, String str) {
                    invoke(iGProTransferFileOperateCallback, num.intValue(), str);
                    return Unit.INSTANCE;
                }

                public final void invoke(@NotNull IGProTransferFileOperateCallback p15, int i15, String str) {
                    Intrinsics.checkNotNullParameter(p15, "p0");
                    p15.onResult(i15, str);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Unit mo4invoke(IKernelGuildFileTransferService iKernelGuildFileTransferService, BaseService.a aVar) {
                invoke2(iKernelGuildFileTransferService, aVar);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull IKernelGuildFileTransferService service, @NotNull BaseService.a bypass) {
                Intrinsics.checkNotNullParameter(service, "service");
                Intrinsics.checkNotNullParameter(bypass, "bypass");
                GProDownloadInfo gProDownloadInfo = req;
                final Function2<P1, P2, Unit> function2J1 = this.J1(bypass, cb5, AnonymousClass1.INSTANCE);
                service.stopFileDownload(gProDownloadInfo, new IGProTransferFileOperateCallback() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.fk
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback
                    public final void onResult(int i15, String str) {
                        Function2 tmp0 = function2J1;
                        Intrinsics.checkNotNullParameter(tmp0, "$tmp0");
                        tmp0.mo4invoke(Integer.valueOf(i15), str);
                    }
                });
            }
        });
    }
}