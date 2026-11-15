package com.tencent.mobileqq.guild.feed.api.impl;

import com.tencent.ams.mosaic.MosaicConstants$JsFunction;
import com.tencent.mobileqq.activity.recent.MsgSummary;
import com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi;
import com.tencent.mobileqq.guild.feed.util.bm;
import com.tencent.mobileqq.profilecard.gallery.historyavatar.ProfileHistoryAvatarLayerFragment;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.mobileqq.qfix.redirect.PatchRedirectCenter;
import com.tencent.qphone.base.util.QLog;
import com.tencent.qqnt.kernelgpro.nativeinterface.FileTransferSyncReq;
import com.tencent.qqnt.kernelgpro.nativeinterface.FileTransferSyncRsp;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProCreateUploadFileGroupParam;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProUploadFileGroupInfo;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProUploadFileInfo;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProUploadFileType;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProUploadStatus;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProCreateUploadFileGroupCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProFileTransferSyncCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileUploadListener;
import com.tencent.qqnt.kernelgpro.nativeinterface.UploadFileExtInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import p022mqq.app.AppRuntime;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes26.dex */
@Metadata(d1 = {"\u0000\u009e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\"\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 I2\u00020\u00012\u00020\u0002:\u0002JKB\u0013\b\u0007\u0012\b\b\u0002\u0010<\u001a\u00020;¢\u0006\u0004\bG\u0010HJ\u001c\u0010\b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0002J\u001c\u0010\n\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0002J\b\u0010\u000b\u001a\u00020\u0003H\u0002J\u0012\u0010\r\u001a\u0004\u0018\u00010\u00032\u0006\u0010\f\u001a\u00020\u0003H\u0002J\u0012\u0010\u0011\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u000f\u001a\u00020\u000eH\u0002J(\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\u0015H\u0002J\u0012\u0010\u001b\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u001a\u001a\u00020\u0019H\u0002J \u0010\u001f\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001dH\u0002J\u0010\u0010 \u001a\u00020\u00172\u0006\u0010\u0004\u001a\u00020\u0003H\u0002J\u001c\u0010$\u001a\u00020\u00172\u0006\u0010!\u001a\u00020\t2\n\u0010#\u001a\u0006\u0012\u0002\b\u00030\"H\u0002J\u0019\u0010'\u001a\u00020\u00172\u0006\u0010&\u001a\u00020%2\u0006\u0010\u0004\u001a\u00020\u0003H\u0096\u0001J\u0011\u0010(\u001a\u00020\u00172\u0006\u0010&\u001a\u00020%H\u0096\u0001J\u0019\u0010)\u001a\u00020\u00172\u0006\u0010&\u001a\u00020%2\u0006\u0010\u0004\u001a\u00020\u0003H\u0096\u0001J\u0012\u0010,\u001a\u00020\u00172\b\u0010+\u001a\u0004\u0018\u00010*H\u0016J\b\u0010-\u001a\u00020\u0017H\u0016J\u001c\u0010.\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\u0010\u0010/\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\u0010\u00100\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\u0010\u00101\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0003H\u0016J\u001c\u00102\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0005H\u0016J\"\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u000206052\f\u00104\u001a\b\u0012\u0004\u0012\u00020\u000303H\u0016J\u001c\u0010:\u001a\b\u0012\u0004\u0012\u00020\u0003082\f\u00109\u001a\b\u0012\u0004\u0012\u00020\u000308H\u0016R\u0014\u0010<\u001a\u00020;8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b<\u0010=R\u0014\u0010?\u001a\u00020>8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b?\u0010@R\u0018\u0010A\u001a\u0004\u0018\u00010*8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bA\u0010BR \u0010D\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030C8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bD\u0010ER \u0010F\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\t0C8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bF\u0010E¨\u0006L"}, d2 = {"Lcom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl;", "Lcom/tencent/mobileqq/guild/feed/api/IGuildFeedFileUploadServiceApi;", "", "", "fileId", "Lcom/tencent/mobileqq/guild/data/publish/taskx/api/a;", "model", "", "doCreateUploadFileTask", "Lcom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl$FileGroupUploadTask;", "createUploadFileGroupTask", "generateFileGroupId", "groupId", "getOriginFileIdFromGroupId", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/GProUploadFileInfo;", "fileInfo", "Lcom/tencent/mobileqq/guild/feed/data/m;", "updateTaskInfoByGProUploadFileInfo", "Lcom/tencent/mobileqq/guild/feed/data/l;", "uploadResult", "taskInfo", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/GProUploadFileType;", "taskType", "", "updateUploadResultByFileType", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/GProUploadFileGroupInfo;", "fileGroupInfo", "updateTaskInfoByGProUploadFileGroupInfo", "fileGroupId", "", "preState", "notifyTaskInfoChange", "removeCacheTask", "task", "Lcom/tencent/mobileqq/guild/feed/api/impl/af;", "taskRsp", "handleFileSyncResp", "Lcom/tencent/mobileqq/guild/feed/api/m;", "listener", "subscribeTaskChangeListener", "unsubscribeAllTaskChangeListener", "unsubscribeTaskChangeListener", "Lmqq/app/AppRuntime;", "appRuntime", "onCreate", MosaicConstants$JsFunction.FUNC_ON_DESTROY, "createUploadFileTask", "pauseUploadFileTask", "resumeUploadFileTask", "removeUploadFileTask", "restoreUploadFileTask", "", "fileIdSet", "", "Lcom/tencent/mobileqq/guild/data/media/d;", "batchGetUploadFileTaskInfoList", "", ProfileHistoryAvatarLayerFragment.KEY_FILE_ID_LIST, "batchGetNeedToCreateFileIds", "Lcom/tencent/mobileqq/guild/feed/api/impl/i;", "observable", "Lcom/tencent/mobileqq/guild/feed/api/impl/i;", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/IKernelGuildFileUploadListener;", "ntListener", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/IKernelGuildFileUploadListener;", "runtime", "Lmqq/app/AppRuntime;", "Ljava/util/concurrent/ConcurrentHashMap;", "fileIdToFileGroupIdMap", "Ljava/util/concurrent/ConcurrentHashMap;", "uploadTaskMap", "<init>", "(Lcom/tencent/mobileqq/guild/feed/api/impl/i;)V", "Companion", "a", "FileGroupUploadTask", "qqguild_feed_impl_release"}, k = 1, mv = {1, 9, 0})
@SourceDebugExtension({"SMAP\nGuildFeedFileUploadServiceApiImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GuildFeedFileUploadServiceApiImpl.kt\ncom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,676:1\n1603#2,9:677\n1855#2:686\n1856#2:688\n1612#2:689\n819#2:690\n847#2,2:691\n1#3:687\n*S KotlinDebug\n*F\n+ 1 GuildFeedFileUploadServiceApiImpl.kt\ncom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl\n*L\n246#1:677,9\n246#1:686\n246#1:688\n246#1:689\n254#1:690\n254#1:691,2\n246#1:687\n*E\n"})
public final class GuildFeedFileUploadServiceApiImpl implements IGuildFeedFileUploadServiceApi {
    static IPatchRedirector $redirector_ = null;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE;
    private static final long FINISHED_PROGRESS = 100;

    @NotNull
    private static final String TAG = "GuildFeedFileUploadServiceApiImpl";

    @NotNull
    private final ConcurrentHashMap<String, String> fileIdToFileGroupIdMap;

    @NotNull
    private final IKernelGuildFileUploadListener ntListener;

    @NotNull
    private final i observable;

    @Nullable
    private volatile AppRuntime runtime;

    @NotNull
    private final ConcurrentHashMap<String, FileGroupUploadTask> uploadTaskMap;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: P */
    @SourceDebugExtension({"SMAP\nGuildFeedFileUploadServiceApiImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GuildFeedFileUploadServiceApiImpl.kt\ncom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl$FileGroupUploadTask\n+ 2 ClassKt.kt\ncom/tencent/mobileqq/guild/feed/util/ClassKtKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,676:1\n29#2:677\n29#2:678\n29#2:679\n29#2:683\n29#2:685\n29#2:686\n29#2:687\n1620#3,3:680\n1#4:684\n*S KotlinDebug\n*F\n+ 1 GuildFeedFileUploadServiceApiImpl.kt\ncom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl$FileGroupUploadTask\n*L\n447#1:677\n542#1:678\n548#1:679\n617#1:683\n623#1:685\n646#1:686\n669#1:687\n564#1:680,3\n*E\n"})
    static final class FileGroupUploadTask {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @NotNull
        private final String f246974a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @NotNull
        private final String f246975b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @NotNull
        private final GProUploadFileType f246976c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @NotNull
        private final com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> f246977d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        @NotNull
        private com.tencent.mobileqq.guild.feed.data.m f246978e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        @NotNull
        private final Lazy f246979f;

        /* JADX INFO: compiled from: P */
        public /* synthetic */ class a {
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            public static final /* synthetic */ int[] f246980a;

            static {
                IPatchRedirector redirector = PatchRedirectCenter.getRedirector(72904);
                $redirector_ = redirector;
                if (redirector != null && redirector.hasPatch((short) 1)) {
                    redirector.redirect((short) 1);
                    return;
                }
                int[] iArr = new int[GProUploadFileType.values().length];
                try {
                    iArr[GProUploadFileType.KPICTURE.ordinal()] = 1;
                } catch (NoSuchFieldError unused) {
                }
                try {
                    iArr[GProUploadFileType.KVIDEO.ordinal()] = 2;
                } catch (NoSuchFieldError unused2) {
                }
                f246980a = iArr;
            }
        }

        /* JADX WARN: Code duplicated, block: B:19:0x0092  */
        public FileGroupUploadTask(@NotNull String groupId, @NotNull String fileId, @NotNull GProUploadFileType type, @NotNull com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> fileModel) {
            String strA;
            Intrinsics.checkNotNullParameter(groupId, "groupId");
            Intrinsics.checkNotNullParameter(fileId, "fileId");
            Intrinsics.checkNotNullParameter(type, "type");
            Intrinsics.checkNotNullParameter(fileModel, "fileModel");
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
                iPatchRedirector.redirect((short) 1, this, groupId, fileId, type, fileModel);
                return;
            }
            this.f246974a = groupId;
            this.f246975b = fileId;
            this.f246976c = type;
            this.f246977d = fileModel;
            long jB = com.tencent.mobileqq.guild.feed.util.file.b.b(fileModel);
            int iOrdinal = type.ordinal();
            Intrinsics.checkNotNullParameter(fileModel, "<this>");
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source = fileModel.getSource();
            com.tencent.mobileqq.guild.data.media.a aVar = (com.tencent.mobileqq.guild.data.media.a) (source instanceof com.tencent.mobileqq.guild.data.media.a ? source : null);
            if (aVar == null) {
                strA = null;
            } else {
                aVar = com.tencent.mobileqq.guild.util.n.f282201a.e(aVar.f()) ? aVar : null;
                if (aVar != null) {
                    strA = bm.f262348a.a(aVar.g());
                } else {
                    strA = null;
                }
            }
            String str = strA == null ? "" : strA;
            boolean zE = com.tencent.mobileqq.guild.feed.util.file.b.e(fileModel);
            Intrinsics.checkNotNullParameter(fileModel, "<this>");
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source2 = fileModel.getSource();
            com.tencent.mobileqq.guild.data.media.a aVar2 = (com.tencent.mobileqq.guild.data.media.a) (source2 instanceof com.tencent.mobileqq.guild.data.media.a ? source2 : null);
            this.f246978e = new com.tencent.mobileqq.guild.feed.data.m(fileId, -1000, iOrdinal, 0, "", null, 0, null, 0L, 0L, jB, str, zE, aVar2 != null ? com.tencent.mobileqq.guild.util.n.f282201a.c(aVar2.f()) : false, fileModel, 960, null);
            this.f246979f = LazyKt.lazy(new Function0<Boolean>() { // from class: com.tencent.mobileqq.guild.feed.api.impl.GuildFeedFileUploadServiceApiImpl$FileGroupUploadTask$needSync$2
                static IPatchRedirector $redirector_;

                {
                    super(0);
                    IPatchRedirector iPatchRedirector2 = $redirector_;
                    if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.this$0);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // kotlin.jvm.functions.Function0
                @NotNull
                public final Boolean invoke() {
                    IPatchRedirector iPatchRedirector2 = $redirector_;
                    return (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 2)) ? Boolean.valueOf(GuildFeedFileUploadServiceApiImpl.FileGroupUploadTask.a(this.this$0)) : (Boolean) iPatchRedirector2.redirect((short) 2, (Object) this);
                }
            });
        }

        public static final boolean a(FileGroupUploadTask fileGroupUploadTask) {
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source = fileGroupUploadTask.f246977d.getSource();
            if (!(source instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d)) {
                source = null;
            }
            com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d dVar = (com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d) source;
            if (dVar == null) {
                return false;
            }
            return ((dVar.b().length() > 0) && new File(dVar.b()).exists()) ? false : true;
        }

        /* JADX WARN: Code duplicated, block: B:122:0x02a7  */
        /* JADX WARN: Code duplicated, block: B:123:0x02b1  */
        /* JADX WARN: Code duplicated, block: B:125:0x02b8  */
        /* JADX WARN: Code duplicated, block: B:126:0x02c1  */
        /* JADX WARN: Code duplicated, block: B:88:0x01e6  */
        public final void b(@NotNull final Function1<? super af<GProUploadFileGroupInfo>, Unit> callback) throws Throwable {
            String string;
            String strD;
            final GProCreateUploadFileGroupParam gProCreateUploadFileGroupParam;
            UploadFileExtInfo uploadFileExtInfoE;
            FileTransferSyncReq fileTransferSyncReqC;
            re2.h hVarD;
            GProUploadFileInfo gProUploadFileInfo;
            UploadFileExtInfo uploadFileExtInfo;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 9)) {
                iPatchRedirector.redirect((short) 9, (Object) this, (Object) callback);
                return;
            }
            Intrinsics.checkNotNullParameter(callback, "callback");
            boolean zE = e();
            StringBuilder sb5 = new StringBuilder("task doCreate, ");
            String str = this.f246974a;
            sb5.append(str);
            sb5.append("-");
            String str2 = this.f246975b;
            sb5.append(str2);
            sb5.append("-");
            GProUploadFileType gProUploadFileType = this.f246976c;
            sb5.append(gProUploadFileType);
            sb5.append(", needSync = ");
            sb5.append(zE);
            QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, sb5.toString());
            boolean zE2 = e();
            com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> aVar = this.f246977d;
            if (zE2) {
                com.tencent.mobileqq.guild.data.publish.taskx.api.b source = aVar.getSource();
                if (!(source instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d)) {
                    source = null;
                }
                com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d dVar = (com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d) source;
                if (dVar != null) {
                    dVar.g(true);
                } else {
                    dVar = null;
                }
                FileTransferSyncReq fileTransferSyncReqC2 = dVar != null ? dVar.c() : null;
                GProUploadFileGroupInfo gProUploadFileGroupInfo = new GProUploadFileGroupInfo();
                gProUploadFileGroupInfo.fileGroupId = str;
                gProUploadFileGroupInfo.uploadStatus = GProUploadStatus.KUPLOADPAUSED;
                gProUploadFileGroupInfo.totalFileSize = fileTransferSyncReqC2 != null ? fileTransferSyncReqC2.size : 0L;
                gProUploadFileGroupInfo.totalUploadFileSize = 0L;
                gProUploadFileGroupInfo.errorCode = 0;
                gProUploadFileGroupInfo.svrErrorCode = 0;
                if (fileTransferSyncReqC2 == null || (uploadFileExtInfo = fileTransferSyncReqC2.extInfo) == null) {
                    uploadFileExtInfo = new UploadFileExtInfo();
                }
                gProUploadFileGroupInfo.uploadFileExtInfo = uploadFileExtInfo;
                callback.invoke(new af(0, "", gProUploadFileGroupInfo));
                return;
            }
            Intrinsics.checkNotNullParameter(aVar, "<this>");
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source2 = aVar.getSource();
            if (source2 instanceof com.tencent.mobileqq.guild.data.media.a) {
                com.tencent.mobileqq.guild.data.publish.taskx.api.b source3 = aVar.getSource();
                Intrinsics.checkNotNull(source3, "null cannot be cast to non-null type com.tencent.mobileqq.guild.data.media.GuildLocalMediaInfo");
                string = ((com.tencent.mobileqq.guild.data.media.a) source3).toString();
            } else if (source2 instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d) {
                com.tencent.mobileqq.guild.data.publish.taskx.api.b source4 = aVar.getSource();
                Intrinsics.checkNotNull(source4, "null cannot be cast to non-null type com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.GuildGenericFileTransferSource");
                string = ((com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d) source4).toString();
            } else if (source2 instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a) {
                com.tencent.mobileqq.guild.data.publish.taskx.api.b source5 = aVar.getSource();
                Intrinsics.checkNotNull(source5, "null cannot be cast to non-null type com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.AudioFileTransferSource");
                string = ((com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a) source5).toString();
            } else {
                string = "";
            }
            QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, "createGProCreateUploadFileGroupParam, " + str + "-" + str2 + "-" + gProUploadFileType + ", localMedia = " + string);
            ArrayList<GProUploadFileInfo> arrayList = new ArrayList<>();
            int i15 = a.f246980a[gProUploadFileType.ordinal()];
            if (i15 != 1) {
                if (i15 == 2) {
                    com.tencent.mobileqq.guild.data.publish.taskx.api.b source6 = aVar.getSource();
                    if (!(source6 instanceof com.tencent.mobileqq.guild.data.media.a)) {
                        source6 = null;
                    }
                    com.tencent.mobileqq.guild.data.media.a aVar2 = (com.tencent.mobileqq.guild.data.media.a) source6;
                    if (aVar2 != null) {
                        String strF = com.tencent.mobileqq.guild.feed.util.file.a.f262413a.f(GuildFeedFileUploadServiceApiImpl.TAG, aVar2);
                        if (strF.length() == 0) {
                            QLog.e(GuildFeedFileUploadServiceApiImpl.TAG, 1, "[getVideoList]: fileGroupId=" + str + ", videoCover still empty");
                            gProUploadFileInfo = null;
                        } else {
                            gProUploadFileInfo = new GProUploadFileInfo();
                            gProUploadFileInfo.fileGroupId = str;
                            gProUploadFileInfo.fileId = UUID.randomUUID().toString();
                            gProUploadFileInfo.fileType = GProUploadFileType.KPICTURE;
                            gProUploadFileInfo.isOriginal = true;
                            gProUploadFileInfo.filePath = strF;
                            gProUploadFileInfo.appType = 25;
                        }
                        if (gProUploadFileInfo != null) {
                            arrayList.add(gProUploadFileInfo);
                        }
                    }
                    QLog.e(GuildFeedFileUploadServiceApiImpl.TAG, 1, str + "-" + str2 + " get video cover failed");
                    gProCreateUploadFileGroupParam = null;
                }
                if (gProCreateUploadFileGroupParam == null) {
                    callback.invoke(new af(-1, "", null));
                    return;
                }
                hVarD = com.tencent.mobileqq.guild.feed.util.r.d();
                if (hVarD != null) {
                    hVarD.createFileGroupUpload(gProCreateUploadFileGroupParam, new IGProCreateUploadFileGroupCallback() { // from class: com.tencent.mobileqq.guild.feed.api.impl.k
                        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProCreateUploadFileGroupCallback
                        public final void onCreateUploadFileGroupResult(int i16, String str3, GProUploadFileGroupInfo gProUploadFileGroupInfo2) {
                            Function1 callback2 = callback;
                            Intrinsics.checkNotNullParameter(callback2, "$callback");
                            QLog.i("GuildFeedFileUploadServiceApiImpl", 1, "createFileGroupUpload, result = " + i16 + ", errMsg = " + str3 + ", info = " + gProUploadFileGroupInfo2);
                            GProCreateUploadFileGroupParam gProCreateUploadFileGroupParam2 = gProCreateUploadFileGroupParam;
                            if (gProCreateUploadFileGroupParam2 != null) {
                                gProCreateUploadFileGroupParam2.fileGroupInfo = gProUploadFileGroupInfo2;
                            }
                            callback2.invoke(new af(i16, str3, gProUploadFileGroupInfo2));
                        }
                    });
                } else {
                    callback.invoke(new af(-1, "guildFileTransferService is null", null));
                }
            }
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source7 = aVar.getSource();
            if (!(source7 instanceof com.tencent.mobileqq.guild.data.media.a)) {
                source7 = null;
            }
            com.tencent.mobileqq.guild.data.media.a aVar3 = (com.tencent.mobileqq.guild.data.media.a) source7;
            if (aVar3 != null && !com.tencent.mobileqq.guild.feed.util.file.b.e(aVar)) {
                com.tencent.mobileqq.guild.feed.util.file.a.f262413a.a(aVar3);
            }
            if (gProUploadFileType == GProUploadFileType.KPICTURE) {
                com.tencent.mobileqq.guild.data.publish.taskx.api.b source8 = aVar.getSource();
                if (!(source8 instanceof com.tencent.mobileqq.guild.data.media.a)) {
                    source8 = null;
                }
                com.tencent.mobileqq.guild.data.media.a aVar4 = (com.tencent.mobileqq.guild.data.media.a) source8;
                if (aVar4 == null || (strD = aVar4.h()) == null) {
                    strD = com.tencent.mobileqq.guild.feed.util.file.b.d(aVar);
                } else {
                    if (!(strD.length() > 0)) {
                        strD = null;
                    }
                    if (strD == null) {
                        strD = com.tencent.mobileqq.guild.feed.util.file.b.d(aVar);
                    }
                }
            } else {
                strD = com.tencent.mobileqq.guild.feed.util.file.b.d(aVar);
            }
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source9 = aVar.getSource();
            if (!(source9 instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d)) {
                source9 = null;
            }
            com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d dVar2 = (com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d) source9;
            String str3 = (dVar2 == null || (fileTransferSyncReqC = dVar2.c()) == null) ? null : fileTransferSyncReqC.filename;
            if (str3 == null) {
                str3 = "";
            }
            GProUploadFileInfo gProUploadFileInfo2 = new GProUploadFileInfo();
            gProUploadFileInfo2.fileGroupId = str;
            gProUploadFileInfo2.fileId = str2;
            gProUploadFileInfo2.fileType = gProUploadFileType;
            gProUploadFileInfo2.isOriginal = com.tencent.mobileqq.guild.feed.util.file.b.e(aVar);
            gProUploadFileInfo2.filePath = strD;
            gProUploadFileInfo2.fileName = str3;
            gProUploadFileInfo2.appType = 25;
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source10 = aVar.getSource();
            if (source10 instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a) {
                com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a aVar5 = (com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a) source10;
                gProUploadFileInfo2.time = aVar5.b() / ((long) 1000);
                gProUploadFileInfo2.audioCodecFormat = aVar5.a();
            }
            arrayList.add(gProUploadFileInfo2);
            ArrayList<String> arrayList2 = new ArrayList<>();
            Iterator<GProUploadFileInfo> it = arrayList.iterator();
            while (it.hasNext()) {
                arrayList2.add(it.next().fileId);
            }
            gProCreateUploadFileGroupParam = new GProCreateUploadFileGroupParam();
            GProUploadFileGroupInfo gProUploadFileGroupInfo2 = new GProUploadFileGroupInfo();
            gProUploadFileGroupInfo2.fileGroupId = str;
            gProUploadFileGroupInfo2.childFileIds = arrayList2;
            Intrinsics.checkNotNullParameter(aVar, "<this>");
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source11 = aVar.getSource();
            if (source11 instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d) {
                uploadFileExtInfoE = ((com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d) source11).d();
            } else {
                uploadFileExtInfoE = source11 instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a ? ((com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a) source11).e() : new UploadFileExtInfo();
            }
            gProUploadFileGroupInfo2.uploadFileExtInfo = uploadFileExtInfoE;
            gProCreateUploadFileGroupParam.fileGroupInfo = gProUploadFileGroupInfo2;
            gProCreateUploadFileGroupParam.fileParams = arrayList;
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source12 = aVar.getSource();
            if (gProUploadFileType == GProUploadFileType.KAUDIO && (source12 instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a)) {
                gProCreateUploadFileGroupParam.fileGroupInfo.uploadAudioExtInfo = ((com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.a) source12).d();
            }
            if (gProCreateUploadFileGroupParam == null) {
                callback.invoke(new af(-1, "", null));
                return;
            }
            hVarD = com.tencent.mobileqq.guild.feed.util.r.d();
            if (hVarD != null) {
                hVarD.createFileGroupUpload(gProCreateUploadFileGroupParam, new IGProCreateUploadFileGroupCallback() { // from class: com.tencent.mobileqq.guild.feed.api.impl.k
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProCreateUploadFileGroupCallback
                    public final void onCreateUploadFileGroupResult(int i16, String str4, GProUploadFileGroupInfo gProUploadFileGroupInfo3) {
                        Function1 callback2 = callback;
                        Intrinsics.checkNotNullParameter(callback2, "$callback");
                        QLog.i("GuildFeedFileUploadServiceApiImpl", 1, "createFileGroupUpload, result = " + i16 + ", errMsg = " + str4 + ", info = " + gProUploadFileGroupInfo3);
                        GProCreateUploadFileGroupParam gProCreateUploadFileGroupParam2 = gProCreateUploadFileGroupParam;
                        if (gProCreateUploadFileGroupParam2 != null) {
                            gProCreateUploadFileGroupParam2.fileGroupInfo = gProUploadFileGroupInfo3;
                        }
                        callback2.invoke(new af(i16, str4, gProUploadFileGroupInfo3));
                    }
                });
            } else {
                callback.invoke(new af(-1, "guildFileTransferService is null", null));
            }
        }

        @NotNull
        public final String c() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? this.f246975b : (String) iPatchRedirector.redirect((short) 3, (Object) this);
        }

        @NotNull
        public final String d() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) ? this.f246974a : (String) iPatchRedirector.redirect((short) 2, (Object) this);
        }

        public final boolean e() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 8)) ? ((Boolean) this.f246979f.getValue()).booleanValue() : ((Boolean) iPatchRedirector.redirect((short) 8, (Object) this)).booleanValue();
        }

        @NotNull
        public final com.tencent.mobileqq.guild.feed.data.m f() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 6)) ? this.f246978e : (com.tencent.mobileqq.guild.feed.data.m) iPatchRedirector.redirect((short) 6, (Object) this);
        }

        @NotNull
        public final GProUploadFileType g() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? this.f246976c : (GProUploadFileType) iPatchRedirector.redirect((short) 4, (Object) this);
        }

        public final void h(@NotNull final Function1<? super ae, Unit> callback) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 10)) {
                iPatchRedirector.redirect((short) 10, (Object) this, (Object) callback);
                return;
            }
            Intrinsics.checkNotNullParameter(callback, "callback");
            boolean zE = e();
            StringBuilder sb5 = new StringBuilder("task doPause, ");
            String str = this.f246974a;
            sb5.append(str);
            sb5.append("-");
            sb5.append(this.f246975b);
            sb5.append("-");
            sb5.append(this.f246976c);
            sb5.append(", needSync = ");
            sb5.append(zE);
            QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, sb5.toString());
            if (e()) {
                callback.invoke(new ae(-1, ""));
                return;
            }
            re2.h hVarD = com.tencent.mobileqq.guild.feed.util.r.d();
            if (hVarD != null) {
                hVarD.pauseFileGroupUpload(str, new IGProTransferFileOperateCallback() { // from class: com.tencent.mobileqq.guild.feed.api.impl.l
                    @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback
                    public final void onResult(int i15, String str2) {
                        Function1 callback2 = callback;
                        Intrinsics.checkNotNullParameter(callback2, "$callback");
                        QLog.i("GuildFeedFileUploadServiceApiImpl", 1, "pauseFileGroupUpload, result = " + i15 + ", errMsg = " + str2);
                        callback2.invoke(new ae(i15, str2));
                    }
                });
            } else {
                callback.invoke(new ae(-1, "guildFileTransferService is null"));
            }
        }

        public final void i(@NotNull final Function1<? super af<?>, Unit> callback) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 11)) {
                iPatchRedirector.redirect((short) 11, (Object) this, (Object) callback);
                return;
            }
            Intrinsics.checkNotNullParameter(callback, "callback");
            boolean zE = e();
            StringBuilder sb5 = new StringBuilder("task doResume, ");
            String str = this.f246974a;
            sb5.append(str);
            sb5.append("-");
            String str2 = this.f246975b;
            sb5.append(str2);
            sb5.append("-");
            GProUploadFileType gProUploadFileType = this.f246976c;
            sb5.append(gProUploadFileType);
            sb5.append(", needSync = ");
            sb5.append(zE);
            QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, sb5.toString());
            Unit unit = null;
            if (!e()) {
                re2.h hVarD = com.tencent.mobileqq.guild.feed.util.r.d();
                if (hVarD != null) {
                    hVarD.resumeFileGroupUpload(str, new IGProTransferFileOperateCallback() { // from class: com.tencent.mobileqq.guild.feed.api.impl.m
                        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProTransferFileOperateCallback
                        public final void onResult(int i15, String str3) {
                            Function1 callback2 = callback;
                            Intrinsics.checkNotNullParameter(callback2, "$callback");
                            QLog.i("GuildFeedFileUploadServiceApiImpl", 1, "resumeFileGroupUpload, result = " + i15 + ", errMsg = " + str3);
                            callback2.invoke(new af(i15, str3, null));
                        }
                    });
                    return;
                } else {
                    callback.invoke(new af(-1, "guildFileTransferService is null", null));
                    return;
                }
            }
            QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, "task doFileSync, " + str + "-" + str2 + "-" + gProUploadFileType + ", needSync = " + e());
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source = this.f246977d.getSource();
            if (!(source instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d)) {
                source = null;
            }
            com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d dVar = (com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.d) source;
            FileTransferSyncReq fileTransferSyncReqC = dVar != null ? dVar.c() : null;
            QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, "doFileSync, syncReq = " + fileTransferSyncReqC);
            if (fileTransferSyncReqC != null) {
                String str3 = fileTransferSyncReqC.fileUuid;
                if (!(str3 == null || str3.length() == 0) && fileTransferSyncReqC.extInfo != null) {
                    re2.h hVarD2 = com.tencent.mobileqq.guild.feed.util.r.d();
                    if (hVarD2 != null) {
                        hVarD2.fileTransferSync(fileTransferSyncReqC, new IGProFileTransferSyncCallback() { // from class: com.tencent.mobileqq.guild.feed.api.impl.n
                            @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProFileTransferSyncCallback
                            public final void onResult(int i15, String str4, FileTransferSyncRsp fileTransferSyncRsp) {
                                Function1 callback2 = callback;
                                Intrinsics.checkNotNullParameter(callback2, "$callback");
                                QLog.i("GuildFeedFileUploadServiceApiImpl", 1, "doFileSync, res = " + i15 + ", err = " + str4 + ", rsp = " + fileTransferSyncRsp);
                                if (i15 == 0 && fileTransferSyncRsp != null) {
                                    callback2.invoke(new af(i15, str4, fileTransferSyncRsp));
                                    return;
                                }
                                if (fileTransferSyncRsp == null) {
                                    fileTransferSyncRsp = new FileTransferSyncRsp();
                                }
                                callback2.invoke(new af(i15, str4, fileTransferSyncRsp));
                            }
                        });
                        unit = Unit.INSTANCE;
                    }
                    if (unit == null) {
                        callback.invoke(new af(-1, "guildFileTransferService is null", new FileTransferSyncRsp()));
                        return;
                    }
                    return;
                }
            }
            QLog.e(GuildFeedFileUploadServiceApiImpl.TAG, 1, "fileTransferSync failed, syncReq is invalid");
            callback.invoke(new af(-1, "syncReq is null", new FileTransferSyncRsp()));
        }

        public final void j(@NotNull com.tencent.mobileqq.guild.feed.data.m mVar) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 7)) {
                iPatchRedirector.redirect((short) 7, (Object) this, (Object) mVar);
            } else {
                Intrinsics.checkNotNullParameter(mVar, "<set-?>");
                this.f246978e = mVar;
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.api.impl.GuildFeedFileUploadServiceApiImpl$a, reason: from kotlin metadata */
    /* JADX INFO: compiled from: P */
    public static final class Companion {
        static IPatchRedirector $redirector_;

        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
                iPatchRedirector.redirect((short) 1, (Object) this);
            }
            IPatchRedirector iPatchRedirector2 = $redirector_;
            if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 2)) {
                return;
            }
            iPatchRedirector2.redirect((short) 2, (Object) this, (Object) defaultConstructorMarker);
        }
    }

    /* JADX INFO: compiled from: P */
    public /* synthetic */ class b {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f246981a;

        static {
            IPatchRedirector redirector = PatchRedirectCenter.getRedirector(73030);
            $redirector_ = redirector;
            if (redirector != null && redirector.hasPatch((short) 1)) {
                redirector.redirect((short) 1);
                return;
            }
            int[] iArr = new int[GProUploadFileType.values().length];
            try {
                iArr[GProUploadFileType.KVIDEO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[GProUploadFileType.KPICTURE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[GProUploadFileType.KFILE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[GProUploadFileType.KAUDIO.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            f246981a = iArr;
        }
    }

    /* JADX INFO: compiled from: P */
    @SourceDebugExtension({"SMAP\nGuildFeedFileUploadServiceApiImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GuildFeedFileUploadServiceApiImpl.kt\ncom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl$doCreateUploadFileTask$1\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,676:1\n1#2:677\n*E\n"})
    public static final class c implements Function1<af<GProUploadFileGroupInfo>, Unit> {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ FileGroupUploadTask f246982d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ GuildFeedFileUploadServiceApiImpl f246983e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f246984f;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ String f246985h;

        c(FileGroupUploadTask fileGroupUploadTask, GuildFeedFileUploadServiceApiImpl guildFeedFileUploadServiceApiImpl, String str, String str2) {
            this.f246982d = fileGroupUploadTask;
            this.f246983e = guildFeedFileUploadServiceApiImpl;
            this.f246984f = str;
            this.f246985h = str2;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, fileGroupUploadTask, guildFeedFileUploadServiceApiImpl, str, str2);
        }

        @Override // kotlin.jvm.functions.Function1
        public Unit invoke(af<GProUploadFileGroupInfo> afVar) {
            com.tencent.mobileqq.guild.feed.data.m mVarUpdateTaskInfoByGProUploadFileGroupInfo;
            af<GProUploadFileGroupInfo> callback = afVar;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) {
                Intrinsics.checkNotNullParameter(callback, "callback");
                FileGroupUploadTask fileGroupUploadTask = this.f246982d;
                int state = fileGroupUploadTask.f().getState();
                int iC = callback.c();
                String str = this.f246984f;
                GuildFeedFileUploadServiceApiImpl guildFeedFileUploadServiceApiImpl = this.f246983e;
                if (iC != 0) {
                    com.tencent.mobileqq.guild.feed.data.m mVarF = fileGroupUploadTask.f();
                    mVarF.s(3);
                    mVarF.o(callback.c());
                    GProUploadFileGroupInfo gProUploadFileGroupInfoB = callback.b();
                    if (gProUploadFileGroupInfoB != null) {
                        mVarF.r(gProUploadFileGroupInfoB.svrErrorCode);
                    }
                    String strA = callback.a();
                    if (strA == null) {
                        strA = "create task failed";
                    }
                    mVarF.p(strA);
                    guildFeedFileUploadServiceApiImpl.notifyTaskInfoChange(str, fileGroupUploadTask.f(), state);
                    guildFeedFileUploadServiceApiImpl.removeCacheTask(this.f246985h);
                } else {
                    GProUploadFileGroupInfo gProUploadFileGroupInfoB2 = callback.b();
                    if (gProUploadFileGroupInfoB2 != null && (mVarUpdateTaskInfoByGProUploadFileGroupInfo = guildFeedFileUploadServiceApiImpl.updateTaskInfoByGProUploadFileGroupInfo(gProUploadFileGroupInfoB2)) != null) {
                        guildFeedFileUploadServiceApiImpl.notifyTaskInfoChange(str, mVarUpdateTaskInfoByGProUploadFileGroupInfo, state);
                    }
                }
            } else {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) callback);
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: compiled from: P */
    @SourceDebugExtension({"SMAP\nGuildFeedFileUploadServiceApiImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GuildFeedFileUploadServiceApiImpl.kt\ncom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl$ntListener$1\n+ 2 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n*L\n1#1,676:1\n215#2,2:677\n*S KotlinDebug\n*F\n+ 1 GuildFeedFileUploadServiceApiImpl.kt\ncom/tencent/mobileqq/guild/feed/api/impl/GuildFeedFileUploadServiceApiImpl$ntListener$1\n*L\n102#1:677,2\n*E\n"})
    public static final class d implements IKernelGuildFileUploadListener {
        static IPatchRedirector $redirector_;

        d() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) GuildFeedFileUploadServiceApiImpl.this);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileUploadListener
        public void onFileCalcHashSuccess(@Nullable GProUploadFileInfo gProUploadFileInfo) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) gProUploadFileInfo);
            } else if (QLog.isDevelopLevel()) {
                QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, "onFileCalcHashSuccess fileInfo = " + com.tencent.mobileqq.guild.feed.util.file.a.f262413a.d(gProUploadFileInfo));
            }
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileUploadListener
        public void onFileGroupUploadSizeChanged(@Nullable String str, long j15, long j16) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 4)) {
                iPatchRedirector.redirect((short) 4, this, str, Long.valueOf(j15), Long.valueOf(j16));
                return;
            }
            if (QLog.isDevelopLevel()) {
                QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 4, "onFileGroupUploadSizeChanged, groupId = " + str + ", totalSize = " + j15 + ", uploadSize = " + j16);
            }
            if (str == null) {
                return;
            }
            GuildFeedFileUploadServiceApiImpl guildFeedFileUploadServiceApiImpl = GuildFeedFileUploadServiceApiImpl.this;
            FileGroupUploadTask fileGroupUploadTask = (FileGroupUploadTask) guildFeedFileUploadServiceApiImpl.uploadTaskMap.get(str);
            com.tencent.mobileqq.guild.feed.data.m mVarF = fileGroupUploadTask != null ? fileGroupUploadTask.f() : null;
            if (mVarF == null) {
                QLog.e(GuildFeedFileUploadServiceApiImpl.TAG, 1, "onFileGroupUploadSizeChanged, groupdId = " + str + ", taskInfo is null");
                return;
            }
            int state = mVarF.getState();
            mVarF.u(j15);
            mVarF.v(j16);
            mVarF.w((int) RangesKt.coerceAtMost((j16 * 100) / RangesKt.coerceAtLeast(j15, 1L), 100L));
            guildFeedFileUploadServiceApiImpl.notifyTaskInfoChange(str, mVarF, state);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileUploadListener
        public void onFileGroupUploadSpeedChanged(@Nullable String str, long j15, @Nullable HashMap<String, Long> map) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 5)) {
                iPatchRedirector.redirect((short) 5, this, str, Long.valueOf(j15), map);
                return;
            }
            if (QLog.isDevelopLevel()) {
                StringBuilder sb5 = new StringBuilder();
                if (map != null) {
                    for (Map.Entry<String, Long> entry : map.entrySet()) {
                        String key = entry.getKey();
                        sb5.append(((Object) key) + MsgSummary.STR_COLON + entry.getValue() + ", ");
                    }
                }
                QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 4, "onFileGroupUploadSpeedChanged, groupId = " + str + ", totalSpeed = " + j15 + ", eachSpeed = [" + ((Object) sb5) + "]");
            }
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileUploadListener
        public void onFileGroupUploadStatusChanged(@Nullable GProUploadFileGroupInfo gProUploadFileGroupInfo) {
            com.tencent.mobileqq.guild.feed.data.m mVarF;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 6)) {
                iPatchRedirector.redirect((short) 6, (Object) this, (Object) gProUploadFileGroupInfo);
                return;
            }
            QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, "onFileGroupUploadStatusChanged, fileGroupInfo = " + com.tencent.mobileqq.guild.feed.util.file.a.f262413a.c(gProUploadFileGroupInfo));
            if (gProUploadFileGroupInfo == null) {
                return;
            }
            GuildFeedFileUploadServiceApiImpl guildFeedFileUploadServiceApiImpl = GuildFeedFileUploadServiceApiImpl.this;
            FileGroupUploadTask fileGroupUploadTask = (FileGroupUploadTask) guildFeedFileUploadServiceApiImpl.uploadTaskMap.get(gProUploadFileGroupInfo.fileGroupId);
            int state = (fileGroupUploadTask == null || (mVarF = fileGroupUploadTask.f()) == null) ? -1000 : mVarF.getState();
            com.tencent.mobileqq.guild.feed.data.m mVarUpdateTaskInfoByGProUploadFileGroupInfo = guildFeedFileUploadServiceApiImpl.updateTaskInfoByGProUploadFileGroupInfo(gProUploadFileGroupInfo);
            if (mVarUpdateTaskInfoByGProUploadFileGroupInfo == null) {
                return;
            }
            String fileGroupId = gProUploadFileGroupInfo.fileGroupId;
            Intrinsics.checkNotNullExpressionValue(fileGroupId, "fileGroupId");
            guildFeedFileUploadServiceApiImpl.notifyTaskInfoChange(fileGroupId, mVarUpdateTaskInfoByGProUploadFileGroupInfo, state);
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileUploadListener
        public void onFileUploadStatusChanged(@Nullable GProUploadFileInfo gProUploadFileInfo) {
            com.tencent.mobileqq.guild.feed.data.m mVarF;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
                iPatchRedirector.redirect((short) 3, (Object) this, (Object) gProUploadFileInfo);
                return;
            }
            QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, "onFileUploadStatusChanged, " + com.tencent.mobileqq.guild.feed.util.file.a.f262413a.d(gProUploadFileInfo));
            if (gProUploadFileInfo == null) {
                return;
            }
            GuildFeedFileUploadServiceApiImpl guildFeedFileUploadServiceApiImpl = GuildFeedFileUploadServiceApiImpl.this;
            FileGroupUploadTask fileGroupUploadTask = (FileGroupUploadTask) guildFeedFileUploadServiceApiImpl.uploadTaskMap.get(gProUploadFileInfo.fileGroupId);
            int state = (fileGroupUploadTask == null || (mVarF = fileGroupUploadTask.f()) == null) ? -1000 : mVarF.getState();
            com.tencent.mobileqq.guild.feed.data.m mVarUpdateTaskInfoByGProUploadFileInfo = guildFeedFileUploadServiceApiImpl.updateTaskInfoByGProUploadFileInfo(gProUploadFileInfo);
            if (mVarUpdateTaskInfoByGProUploadFileInfo == null) {
                return;
            }
            String fileGroupId = gProUploadFileInfo.fileGroupId;
            Intrinsics.checkNotNullExpressionValue(fileGroupId, "fileGroupId");
            guildFeedFileUploadServiceApiImpl.notifyTaskInfoChange(fileGroupId, mVarUpdateTaskInfoByGProUploadFileInfo, state);
        }
    }

    /* JADX INFO: compiled from: P */
    public static final class e implements Function1<ae, Unit> {
        static IPatchRedirector $redirector_;

        e() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this);
        }

        @Override // kotlin.jvm.functions.Function1
        public Unit invoke(ae aeVar) {
            ae callback = aeVar;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) {
                Intrinsics.checkNotNullParameter(callback, "callback");
            } else {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) callback);
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: compiled from: P */
    public static final class f implements Function1<af<?>, Unit> {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final /* synthetic */ FileGroupUploadTask f246987d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        final /* synthetic */ GuildFeedFileUploadServiceApiImpl f246988e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        final /* synthetic */ String f246989f;

        f(FileGroupUploadTask fileGroupUploadTask, GuildFeedFileUploadServiceApiImpl guildFeedFileUploadServiceApiImpl, String str) {
            this.f246987d = fileGroupUploadTask;
            this.f246988e = guildFeedFileUploadServiceApiImpl;
            this.f246989f = str;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, fileGroupUploadTask, guildFeedFileUploadServiceApiImpl, str);
        }

        @Override // kotlin.jvm.functions.Function1
        public Unit invoke(af<?> afVar) {
            af<?> callback = afVar;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) {
                Intrinsics.checkNotNullParameter(callback, "callback");
                FileGroupUploadTask fileGroupUploadTask = this.f246987d;
                QLog.i(GuildFeedFileUploadServiceApiImpl.TAG, 1, "on resume, " + fileGroupUploadTask.d() + "- " + fileGroupUploadTask.c() + ", state = " + fileGroupUploadTask.f().getState());
                boolean z15 = callback.b() instanceof FileTransferSyncRsp;
                GuildFeedFileUploadServiceApiImpl guildFeedFileUploadServiceApiImpl = this.f246988e;
                if (z15) {
                    guildFeedFileUploadServiceApiImpl.handleFileSyncResp(fileGroupUploadTask, callback);
                } else if (callback.c() != 0) {
                    guildFeedFileUploadServiceApiImpl.notifyTaskInfoChange(this.f246989f, fileGroupUploadTask.f(), fileGroupUploadTask.f().getState());
                }
            } else {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) callback);
            }
            return Unit.INSTANCE;
        }
    }

    static {
        IPatchRedirector redirector = PatchRedirectCenter.getRedirector(73168);
        $redirector_ = redirector;
        if (redirector == null || !redirector.hasPatch((short) 16)) {
            INSTANCE = new Companion(null);
        } else {
            redirector.redirect((short) 16);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @JvmOverloads
    public GuildFeedFileUploadServiceApiImpl() {
        this(null, 1, 0 == true ? 1 : 0);
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 15)) {
            return;
        }
        iPatchRedirector.redirect((short) 15, (Object) this);
    }

    private final FileGroupUploadTask createUploadFileGroupTask(String fileId, com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model) {
        String strGenerateFileGroupId = generateFileGroupId();
        GProUploadFileType gProUploadFileTypeC = com.tencent.mobileqq.guild.feed.util.file.b.c(model);
        this.fileIdToFileGroupIdMap.put(fileId, strGenerateFileGroupId);
        FileGroupUploadTask fileGroupUploadTask = new FileGroupUploadTask(strGenerateFileGroupId, fileId, gProUploadFileTypeC, model);
        this.uploadTaskMap.put(strGenerateFileGroupId, fileGroupUploadTask);
        return fileGroupUploadTask;
    }

    private final boolean doCreateUploadFileTask(String fileId, com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model) throws Throwable {
        FileGroupUploadTask fileGroupUploadTaskCreateUploadFileGroupTask = createUploadFileGroupTask(fileId, model);
        fileGroupUploadTaskCreateUploadFileGroupTask.b(new c(fileGroupUploadTaskCreateUploadFileGroupTask, this, fileGroupUploadTaskCreateUploadFileGroupTask.d(), fileId));
        return true;
    }

    private final String generateFileGroupId() {
        String string = UUID.randomUUID().toString();
        Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private final String getOriginFileIdFromGroupId(String groupId) {
        FileGroupUploadTask fileGroupUploadTask = this.uploadTaskMap.get(groupId);
        if (fileGroupUploadTask != null) {
            return fileGroupUploadTask.c();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleFileSyncResp(FileGroupUploadTask task, af<?> taskRsp) {
        Object objB = taskRsp.b();
        FileTransferSyncRsp fileTransferSyncRsp = objB instanceof FileTransferSyncRsp ? (FileTransferSyncRsp) objB : null;
        if (fileTransferSyncRsp == null) {
            return;
        }
        int state = task.f().getState();
        com.tencent.mobileqq.guild.feed.data.m mVarF = task.f();
        mVarF.s(3);
        mVarF.o(taskRsp.c());
        String strA = taskRsp.a();
        if (strA == null) {
            strA = "";
        }
        mVarF.p(strA);
        mVarF.t(taskRsp.c());
        if (taskRsp.c() != 0) {
            notifyTaskInfoChange(task.d(), task.f(), state);
            return;
        }
        com.tencent.mobileqq.guild.feed.data.l lVarM = task.f().m();
        if (lVarM == null) {
            lVarM = new com.tencent.mobileqq.guild.feed.data.l("", null, 0, 0, null, 30, null);
        }
        String fileId = fileTransferSyncRsp.fileId;
        Intrinsics.checkNotNullExpressionValue(fileId, "fileId");
        lVarM.d(fileId);
        task.f().q(lVarM);
        notifyTaskInfoChange(task.d(), task.f(), state);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyTaskInfoChange(String fileGroupId, com.tencent.mobileqq.guild.feed.data.m taskInfo, int preState) {
        FileGroupUploadTask fileGroupUploadTask = this.uploadTaskMap.get(fileGroupId);
        if (fileGroupUploadTask == null) {
            QLog.e(TAG, 1, "notifyTaskInfoChange, " + fileGroupId + "-" + taskInfo.getFileId() + " task is null");
            return;
        }
        fileGroupUploadTask.j(taskInfo);
        if (getOriginFileIdFromGroupId(fileGroupId) != null) {
            this.observable.a(taskInfo, preState);
            return;
        }
        QLog.e(TAG, 1, "notifyTaskInfoChange, " + fileGroupId + " origin fileId not found");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void removeCacheTask(String fileId) {
        String strRemove = this.fileIdToFileGroupIdMap.remove(fileId);
        QLog.i(TAG, 1, "removeCacheTask， fileId = " + fileId + ", groupId = " + strRemove);
        if (strRemove != null) {
            this.uploadTaskMap.remove(strRemove);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void removeUploadFileTask$lambda$0(int i15, String str) {
        QLog.i(TAG, 1, "removeFileGroupUpload, result = " + i15 + ", errMsg = " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.tencent.mobileqq.guild.feed.data.m updateTaskInfoByGProUploadFileGroupInfo(GProUploadFileGroupInfo fileGroupInfo) {
        String fileGroupId = fileGroupInfo.fileGroupId;
        Intrinsics.checkNotNullExpressionValue(fileGroupId, "fileGroupId");
        String originFileIdFromGroupId = getOriginFileIdFromGroupId(fileGroupId);
        if (originFileIdFromGroupId == null) {
            QLog.i(TAG, 1, "getTaskInfoFromGProUploadFileGroupInfo failed, groupdId =  " + fileGroupInfo.fileGroupId);
            return null;
        }
        FileGroupUploadTask fileGroupUploadTask = this.uploadTaskMap.get(fileGroupInfo.fileGroupId);
        com.tencent.mobileqq.guild.feed.data.m mVarF = fileGroupUploadTask != null ? fileGroupUploadTask.f() : null;
        if (mVarF == null) {
            QLog.e(TAG, 1, "getTaskInfoFromGProUploadFileGroupInfo, " + fileGroupInfo.fileGroupId + "-" + originFileIdFromGroupId + " taskInfo is null");
            return null;
        }
        com.tencent.mobileqq.guild.feed.util.file.a aVar = com.tencent.mobileqq.guild.feed.util.file.a.f262413a;
        GProUploadStatus uploadStatus = fileGroupInfo.uploadStatus;
        Intrinsics.checkNotNullExpressionValue(uploadStatus, "uploadStatus");
        mVarF.s(aVar.b(uploadStatus));
        mVarF.u(fileGroupInfo.totalFileSize);
        mVarF.v(fileGroupInfo.totalUploadFileSize);
        mVarF.w(fileGroupInfo.uploadStatus == GProUploadStatus.KUPLOADSUCCESS ? 100 : (int) RangesKt.coerceAtMost((mVarF.k() * 100) / RangesKt.coerceAtLeast(mVarF.e(), 1L), 100L));
        mVarF.o(fileGroupInfo.errorCode);
        mVarF.r(fileGroupInfo.svrErrorCode);
        String errorMsg = fileGroupInfo.errorMsg;
        Intrinsics.checkNotNullExpressionValue(errorMsg, "errorMsg");
        mVarF.p(errorMsg);
        return mVarF;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.tencent.mobileqq.guild.feed.data.m updateTaskInfoByGProUploadFileInfo(GProUploadFileInfo fileInfo) {
        FileGroupUploadTask fileGroupUploadTask = this.uploadTaskMap.get(fileInfo.fileGroupId);
        if (fileGroupUploadTask == null) {
            QLog.e(TAG, 1, "getTaskInfoFromGProUploadFileInfo, status=" + fileInfo.uploadStatus + " " + fileInfo.fileGroupId + "-" + fileInfo.fileId + " task is null");
            return null;
        }
        com.tencent.mobileqq.guild.feed.data.m mVarF = fileGroupUploadTask.f();
        GProUploadFileType gProUploadFileTypeG = fileGroupUploadTask.g();
        if (gProUploadFileTypeG != GProUploadFileType.KUNKNOWN) {
            com.tencent.mobileqq.guild.feed.data.l lVarM = mVarF.m();
            if (lVarM == null) {
                String filePath = fileInfo.filePath;
                Intrinsics.checkNotNullExpressionValue(filePath, "filePath");
                lVarM = new com.tencent.mobileqq.guild.feed.data.l(filePath, null, 0, 0, null, 30, null);
            }
            updateUploadResultByFileType(lVarM, mVarF, fileInfo, gProUploadFileTypeG);
            mVarF.q(lVarM);
            return mVarF;
        }
        QLog.e(TAG, 1, "getTaskInfoFromGProUploadFileInfo, status=" + fileInfo.uploadStatus + " " + fileInfo.fileGroupId + "-" + fileInfo.fileId + " unknown task type");
        return null;
    }

    private final void updateUploadResultByFileType(com.tencent.mobileqq.guild.feed.data.l uploadResult, com.tencent.mobileqq.guild.feed.data.m taskInfo, GProUploadFileInfo fileInfo, GProUploadFileType taskType) {
        int[] iArr = b.f246981a;
        int i15 = iArr[taskType.ordinal()];
        if (i15 != 1) {
            if (i15 == 2) {
                String svrUrl = fileInfo.svrUrl;
                Intrinsics.checkNotNullExpressionValue(svrUrl, "svrUrl");
                uploadResult.e(svrUrl);
                return;
            }
            if (i15 == 3 || i15 == 4) {
                String svrFileId = fileInfo.svrFileId;
                Intrinsics.checkNotNullExpressionValue(svrFileId, "svrFileId");
                uploadResult.d(svrFileId);
                return;
            }
            QLog.e(TAG, 1, "updateMediaInfoByFileType, " + fileInfo.uploadStatus + ", " + taskInfo.getFileId() + " invalid task type: " + taskType);
            return;
        }
        GProUploadFileType gProUploadFileType = fileInfo.fileType;
        int i16 = gProUploadFileType == null ? -1 : iArr[gProUploadFileType.ordinal()];
        if (i16 == 1) {
            String svrFileId2 = fileInfo.svrFileId;
            Intrinsics.checkNotNullExpressionValue(svrFileId2, "svrFileId");
            uploadResult.d(svrFileId2);
            return;
        }
        if (i16 == 2) {
            String svrUrl2 = fileInfo.svrUrl;
            Intrinsics.checkNotNullExpressionValue(svrUrl2, "svrUrl");
            uploadResult.e(svrUrl2);
            return;
        }
        QLog.e(TAG, 1, "updateMediaInfoByFileType, " + fileInfo.uploadStatus + ", " + fileInfo.fileGroupId + "-" + fileInfo.fileId + ", invalid file type: " + fileInfo.fileType);
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    @NotNull
    public List<String> batchGetNeedToCreateFileIds(@NotNull List<String> fileIdList) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 14)) {
            return (List) iPatchRedirector.redirect((short) 14, (Object) this, (Object) fileIdList);
        }
        Intrinsics.checkNotNullParameter(fileIdList, "fileIdList");
        ArrayList arrayList = new ArrayList();
        for (Object obj : fileIdList) {
            if (!this.fileIdToFileGroupIdMap.containsKey((String) obj)) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    @NotNull
    public Map<String, com.tencent.mobileqq.guild.data.media.d> batchGetUploadFileTaskInfoList(@NotNull Set<String> fileIdSet) {
        com.tencent.mobileqq.guild.feed.data.m mVarF;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 13)) {
            return (Map) iPatchRedirector.redirect((short) 13, (Object) this, (Object) fileIdSet);
        }
        Intrinsics.checkNotNullParameter(fileIdSet, "fileIdSet");
        ArrayList arrayList = new ArrayList();
        for (String str : fileIdSet) {
            String str2 = this.fileIdToFileGroupIdMap.get(str);
            Pair pair = null;
            if (str2 != null) {
                Intrinsics.checkNotNull(str2);
                FileGroupUploadTask fileGroupUploadTask = this.uploadTaskMap.get(str2);
                if (fileGroupUploadTask != null && (mVarF = fileGroupUploadTask.f()) != null) {
                    pair = TuplesKt.to(str, mVarF);
                }
            }
            if (pair != null) {
                arrayList.add(pair);
            }
        }
        return MapsKt.toMap(arrayList);
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    public boolean createUploadFileTask(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 8)) {
            return ((Boolean) iPatchRedirector.redirect((short) 8, (Object) this, (Object) fileId, (Object) model)).booleanValue();
        }
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        Intrinsics.checkNotNullParameter(model, "model");
        if (this.runtime == null) {
            QLog.e(TAG, 1, "runtime is null");
            return false;
        }
        if (com.tencent.mobileqq.guild.feed.util.r.d() == null) {
            QLog.e(TAG, 1, "fileTransferService is null");
            return false;
        }
        String str = this.fileIdToFileGroupIdMap.get(fileId);
        if (str == null || str.length() == 0) {
            return doCreateUploadFileTask(fileId, model);
        }
        QLog.i(TAG, 1, ((Object) this.fileIdToFileGroupIdMap.get(fileId)) + "-" + fileId + " task already exist");
        return true;
    }

    @Override // p022mqq.app.api.IRuntimeService
    public void onCreate(@Nullable AppRuntime appRuntime) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 6)) {
            iPatchRedirector.redirect((short) 6, (Object) this, (Object) appRuntime);
            return;
        }
        this.runtime = appRuntime;
        re2.h hVarD = com.tencent.mobileqq.guild.feed.util.r.d();
        if (hVarD != null) {
            hVarD.addFileUploadListener(this.ntListener);
        }
    }

    @Override // p022mqq.app.api.IRuntimeService
    public void onDestroy() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 7)) {
            iPatchRedirector.redirect((short) 7, (Object) this);
            return;
        }
        this.runtime = null;
        re2.h hVarD = com.tencent.mobileqq.guild.feed.util.r.d();
        if (hVarD != null) {
            hVarD.c(this.ntListener);
        }
        this.fileIdToFileGroupIdMap.clear();
        this.uploadTaskMap.clear();
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    public boolean pauseUploadFileTask(@NotNull String fileId) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 9)) {
            return ((Boolean) iPatchRedirector.redirect((short) 9, (Object) this, (Object) fileId)).booleanValue();
        }
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        String str = this.fileIdToFileGroupIdMap.get(fileId);
        QLog.i(TAG, 1, "pauseUploadMediaTask, groupId= " + str + ", fileId = " + fileId);
        if (str == null) {
            return false;
        }
        FileGroupUploadTask fileGroupUploadTask = this.uploadTaskMap.get(str);
        if (fileGroupUploadTask == null) {
            QLog.e(TAG, 1, "pauseUploadMediaTask failed, task is null");
            return false;
        }
        fileGroupUploadTask.h(new e());
        return true;
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    public boolean removeUploadFileTask(@NotNull String fileId) {
        re2.h hVarD;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 11)) {
            return ((Boolean) iPatchRedirector.redirect((short) 11, (Object) this, (Object) fileId)).booleanValue();
        }
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        String str = this.fileIdToFileGroupIdMap.get(fileId);
        removeCacheTask(fileId);
        QLog.i(TAG, 1, "removeUploadMediaTask, groupId= " + str + ", fileId = " + fileId);
        if (str != null && (hVarD = com.tencent.mobileqq.guild.feed.util.r.d()) != null) {
            hVarD.removeFileGroupUpload(str, new j());
        }
        return true;
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    public boolean restoreUploadFileTask(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 12)) {
            return ((Boolean) iPatchRedirector.redirect((short) 12, (Object) this, (Object) fileId, (Object) model)).booleanValue();
        }
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        Intrinsics.checkNotNullParameter(model, "model");
        return createUploadFileTask(fileId, model);
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    public boolean resumeUploadFileTask(@NotNull String fileId) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 10)) {
            return ((Boolean) iPatchRedirector.redirect((short) 10, (Object) this, (Object) fileId)).booleanValue();
        }
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        String str = this.fileIdToFileGroupIdMap.get(fileId);
        QLog.i(TAG, 2, "resumeFileGroupUpload, groupId= " + str + ", fileId = " + fileId);
        if (str == null) {
            return false;
        }
        FileGroupUploadTask fileGroupUploadTask = this.uploadTaskMap.get(str);
        if (fileGroupUploadTask == null) {
            QLog.e(TAG, 1, "resumeUploadMediaTask failed, task is null");
            return false;
        }
        if (fileGroupUploadTask.f().getState() == 5 || fileGroupUploadTask.f().l()) {
            QLog.e(TAG, 1, "resumeUploadMediaTask failed, state = " + fileGroupUploadTask.f().getState());
            return false;
        }
        if (fileGroupUploadTask.f().f()) {
            QLog.i(TAG, 1, "resumeUploadMediaTask, task already success");
            notifyTaskInfoChange(str, fileGroupUploadTask.f(), fileGroupUploadTask.f().getState());
            return true;
        }
        if (fileGroupUploadTask.e()) {
            int state = fileGroupUploadTask.f().getState();
            fileGroupUploadTask.f().s(1);
            notifyTaskInfoChange(str, fileGroupUploadTask.f(), state);
        }
        fileGroupUploadTask.i(new f(fileGroupUploadTask, this, str));
        return true;
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    public void subscribeTaskChangeListener(@NotNull com.tencent.mobileqq.guild.feed.api.m listener, @NotNull String fileId) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
            iPatchRedirector.redirect((short) 3, (Object) this, (Object) listener, (Object) fileId);
            return;
        }
        Intrinsics.checkNotNullParameter(listener, "listener");
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        this.observable.b(listener, fileId);
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    public void unsubscribeAllTaskChangeListener(@NotNull com.tencent.mobileqq.guild.feed.api.m listener) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 4)) {
            iPatchRedirector.redirect((short) 4, (Object) this, (Object) listener);
        } else {
            Intrinsics.checkNotNullParameter(listener, "listener");
            this.observable.c(listener);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi
    public void unsubscribeTaskChangeListener(@NotNull com.tencent.mobileqq.guild.feed.api.m listener, @NotNull String fileId) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 5)) {
            iPatchRedirector.redirect((short) 5, (Object) this, (Object) listener, (Object) fileId);
            return;
        }
        Intrinsics.checkNotNullParameter(listener, "listener");
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        this.observable.d(listener, fileId);
    }

    @JvmOverloads
    public GuildFeedFileUploadServiceApiImpl(@NotNull i observable) {
        Intrinsics.checkNotNullParameter(observable, "observable");
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) observable);
            return;
        }
        this.observable = observable;
        this.ntListener = new d();
        this.fileIdToFileGroupIdMap = new ConcurrentHashMap<>();
        this.uploadTaskMap = new ConcurrentHashMap<>();
    }

    public /* synthetic */ GuildFeedFileUploadServiceApiImpl(i iVar, int i15, DefaultConstructorMarker defaultConstructorMarker) {
        this((i15 & 1) != 0 ? new i() : iVar);
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) {
            return;
        }
        iPatchRedirector.redirect((short) 2, this, iVar, Integer.valueOf(i15), defaultConstructorMarker);
    }
}