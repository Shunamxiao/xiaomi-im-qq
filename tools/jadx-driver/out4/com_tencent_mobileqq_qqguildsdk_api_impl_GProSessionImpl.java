package com.tencent.mobileqq.qqguildsdk.api.impl;

import android.os.SystemClock;
import com.tencent.ams.mosaic.MosaicConstants$JsFunction;
import com.tencent.common.config.AppSetting;
import com.tencent.mobileqq.guild.api.IGuildDelayInitializeService;
import com.tencent.mobileqq.guild.util.TraceUtils;
import com.tencent.mobileqq.persistence.backup.impl.DBBackupServiceImpl;
import com.tencent.mobileqq.qqguildsdk.api.IGProSession;
import com.tencent.mobileqq.qqguildsdk.api.IGuildMsgService;
import com.tencent.mobileqq.qroute.QRoute;
import com.tencent.qphone.base.util.QLog;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProInitSessionConfig;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProDependsAdapter;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelDirectSessionService;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelFeedServiceNew;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGProGuildMsgService;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGProSessionListener;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildFileTransferService;
import com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGuildService;
import com.tencent.qqnt.kernelgpro.nativeinterface.IQQGProWrapperSession;
import com.tencent.qqnt.ntstartup.nativeinterface.StartupSessionConstant;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import p022mqq.app.AppRuntime;
import p022mqq.app.MobileQQ;

/* JADX INFO: loaded from: classes6.dex */
@Metadata(d1 = {"\u0000\u009a\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 <2\u00020\u0001:\u0001=B\u0007¢\u0006\u0004\b:\u0010;J\b\u0010\u0003\u001a\u00020\u0002H\u0002J\b\u0010\u0005\u001a\u00020\u0004H\u0002J\b\u0010\u0007\u001a\u00020\u0006H\u0002J\b\u0010\b\u001a\u00020\u0006H\u0002J\u0012\u0010\u000b\u001a\u00020\u00062\b\u0010\n\u001a\u0004\u0018\u00010\tH\u0016J\b\u0010\f\u001a\u00020\u0006H\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016J\b\u0010\u000f\u001a\u00020\rH\u0016J,\u0010\u0014\u001a\u00020\u00062\"\u0010\u0013\u001a\u001e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00110\u0010j\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u0011`\u0012H\u0016J\b\u0010\u0015\u001a\u00020\u0006H\u0016J\b\u0010\u0016\u001a\u00020\u0006H\u0016J\n\u0010\u0018\u001a\u0004\u0018\u00010\u0017H\u0016J\n\u0010\u001a\u001a\u0004\u0018\u00010\u0019H\u0016J\n\u0010\u001c\u001a\u0004\u0018\u00010\u001bH\u0016J\n\u0010\u001e\u001a\u0004\u0018\u00010\u001dH\u0016J\n\u0010 \u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010!\u001a\u00020\u0006H\u0016J\b\u0010#\u001a\u00020\"H\u0016R\u0018\u0010%\u001a\u0004\u0018\u00010$8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b%\u0010&R\u0014\u0010'\u001a\u00020\"8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b'\u0010(R\u0014\u0010*\u001a\u00020)8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b*\u0010+R\u0016\u0010\u000e\u001a\u00020\r8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b\u000e\u0010,R\u0016\u0010.\u001a\u00020-8\u0002@\u0002X\u0082.¢\u0006\u0006\n\u0004\b.\u0010/R\u001a\u00102\u001a\b\u0012\u0004\u0012\u000201008\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b2\u00103R\u001a\u00105\u001a\b\u0012\u0004\u0012\u000204008\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b5\u00103R\u001a\u00107\u001a\b\u0012\u0004\u0012\u000206008\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b7\u00103R\u001a\u00109\u001a\b\u0012\u0004\u0012\u000208008\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b9\u00103¨\u0006>"}, d2 = {"Lcom/tencent/mobileqq/qqguildsdk/api/impl/GProSessionImpl;", "Lcom/tencent/mobileqq/qqguildsdk/api/IGProSession;", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/IGProDependsAdapter;", "getIDependsAdapter", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/IKernelGProSessionListener;", "getSessionListener", "", "onStart", "destroy", "Lmqq/app/AppRuntime;", "appRuntime", "onCreate", MosaicConstants$JsFunction.FUNC_ON_DESTROY, "", "isDestroy", "isInit", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "sessionMap", "initBusinessModule", "start", "stop", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/IKernelGuildService;", "getGuildService", "Lre2/c;", "getGProGuildMsgService", "Lcom/tencent/qqnt/kernel/api/s;", "getFeedService", "Lre2/a;", "getGProDirectSessionService", "Lre2/h;", "getGuildFileTransferService", "notifyGProSdkStart", "Ljava/util/concurrent/CountDownLatch;", "getGProSdkStartCountDownLatch", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/IQQGProWrapperSession;", "gproWrapperSession", "Lcom/tencent/qqnt/kernelgpro/nativeinterface/IQQGProWrapperSession;", "gproStartCountDownLatch", "Ljava/util/concurrent/CountDownLatch;", "Ljava/util/concurrent/atomic/AtomicBoolean;", "isStartFinish", "Ljava/util/concurrent/atomic/AtomicBoolean;", "Z", "Lcom/tencent/mobileqq/qqguildsdk/api/impl/ServiceContent;", "serviceContent", "Lcom/tencent/mobileqq/qqguildsdk/api/impl/ServiceContent;", "Lcom/tencent/mobileqq/qqguildsdk/util/d;", "Lcom/tencent/mobileqq/qqguildsdk/api/impl/GProGuildMsgService;", "gproGuildMsgService", "Lcom/tencent/mobileqq/qqguildsdk/util/d;", "Lcom/tencent/mobileqq/qqguildsdk/api/impl/FeedServiceNew;", "feedChannelServiceNew", "Lcom/tencent/mobileqq/qqguildsdk/api/impl/GProDirectSessionService;", "gProDirectSessionService", "Lcom/tencent/mobileqq/qqguildsdk/api/impl/GuildFileTransferService;", "guildFileTransferService", "<init>", "()V", "Companion", "a", "qqguild-sdk-impl_release"}, k = 1, mv = {1, 9, 0})
public final class GProSessionImpl implements IGProSession {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    public static final String TAG = "GProSessionImpl";

    @NotNull
    private static String currentUin = "";

    @Nullable
    private volatile IQQGProWrapperSession gproWrapperSession;
    private volatile boolean isDestroy;
    private volatile ServiceContent serviceContent;

    @NotNull
    private final CountDownLatch gproStartCountDownLatch = new CountDownLatch(1);

    @NotNull
    private final AtomicBoolean isStartFinish = new AtomicBoolean(false);

    @NotNull
    private final com.tencent.mobileqq.qqguildsdk.util.d<GProGuildMsgService> gproGuildMsgService = com.tencent.mobileqq.qqguildsdk.util.e.a(new Function0<GProGuildMsgService>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GProSessionImpl$gproGuildMsgService$1
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @Nullable
        public final GProGuildMsgService invoke() {
            IKernelGProGuildMsgService gProGuildMsgService;
            TraceUtils traceUtils = TraceUtils.f281741a;
            GProSessionImpl gProSessionImpl = this.this$0;
            if (TraceUtils.h()) {
                try {
                    com.tencent.mobileqq.guild.util.ed.c("getGroupService");
                    IQQGProWrapperSession iQQGProWrapperSession = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession);
                    gProGuildMsgService = iQQGProWrapperSession.getGProGuildMsgService();
                    com.tencent.mobileqq.guild.util.ed.d();
                } catch (Throwable th5) {
                    com.tencent.mobileqq.guild.util.ed.d();
                    throw th5;
                }
            } else {
                TraceUtils.a aVarG = traceUtils.g();
                if (aVarG == null) {
                    IQQGProWrapperSession iQQGProWrapperSession2 = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession2);
                    gProGuildMsgService = iQQGProWrapperSession2.getGProGuildMsgService();
                } else {
                    aVarG.a("getGroupService");
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    IQQGProWrapperSession iQQGProWrapperSession3 = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession3);
                    IKernelGProGuildMsgService gProGuildMsgService2 = iQQGProWrapperSession3.getGProGuildMsgService();
                    aVarG.a("getGroupService#end# " + (SystemClock.uptimeMillis() - jUptimeMillis) + " ms");
                    gProGuildMsgService = gProGuildMsgService2;
                }
            }
            ServiceContent serviceContent = this.this$0.serviceContent;
            if (serviceContent == null) {
                Intrinsics.throwUninitializedPropertyAccessException("serviceContent");
                serviceContent = null;
            }
            return new GProGuildMsgService(gProGuildMsgService, serviceContent);
        }
    });

    @NotNull
    private final com.tencent.mobileqq.qqguildsdk.util.d<FeedServiceNew> feedChannelServiceNew = com.tencent.mobileqq.qqguildsdk.util.e.a(new Function0<FeedServiceNew>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GProSessionImpl$feedChannelServiceNew$1
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @Nullable
        public final FeedServiceNew invoke() {
            IKernelFeedServiceNew feedChannelServiceNew;
            TraceUtils traceUtils = TraceUtils.f281741a;
            GProSessionImpl gProSessionImpl = this.this$0;
            if (TraceUtils.h()) {
                try {
                    com.tencent.mobileqq.guild.util.ed.c("getGroupService");
                    IQQGProWrapperSession iQQGProWrapperSession = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession);
                    feedChannelServiceNew = iQQGProWrapperSession.getFeedChannelServiceNew();
                    com.tencent.mobileqq.guild.util.ed.d();
                } catch (Throwable th5) {
                    com.tencent.mobileqq.guild.util.ed.d();
                    throw th5;
                }
            } else {
                TraceUtils.a aVarG = traceUtils.g();
                if (aVarG == null) {
                    IQQGProWrapperSession iQQGProWrapperSession2 = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession2);
                    feedChannelServiceNew = iQQGProWrapperSession2.getFeedChannelServiceNew();
                } else {
                    aVarG.a("getGroupService");
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    IQQGProWrapperSession iQQGProWrapperSession3 = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession3);
                    IKernelFeedServiceNew feedChannelServiceNew2 = iQQGProWrapperSession3.getFeedChannelServiceNew();
                    aVarG.a("getGroupService#end# " + (SystemClock.uptimeMillis() - jUptimeMillis) + " ms");
                    feedChannelServiceNew = feedChannelServiceNew2;
                }
            }
            ServiceContent serviceContent = this.this$0.serviceContent;
            if (serviceContent == null) {
                Intrinsics.throwUninitializedPropertyAccessException("serviceContent");
                serviceContent = null;
            }
            return new FeedServiceNew(feedChannelServiceNew, serviceContent);
        }
    });

    @NotNull
    private final com.tencent.mobileqq.qqguildsdk.util.d<GProDirectSessionService> gProDirectSessionService = com.tencent.mobileqq.qqguildsdk.util.e.a(new Function0<GProDirectSessionService>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GProSessionImpl$gProDirectSessionService$1
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @Nullable
        public final GProDirectSessionService invoke() {
            IKernelDirectSessionService directSessionService;
            TraceUtils traceUtils = TraceUtils.f281741a;
            GProSessionImpl gProSessionImpl = this.this$0;
            if (TraceUtils.h()) {
                try {
                    com.tencent.mobileqq.guild.util.ed.c("getGProDirectSessionService");
                    IQQGProWrapperSession iQQGProWrapperSession = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession);
                    directSessionService = iQQGProWrapperSession.getDirectSessionService();
                    com.tencent.mobileqq.guild.util.ed.d();
                } catch (Throwable th5) {
                    com.tencent.mobileqq.guild.util.ed.d();
                    throw th5;
                }
            } else {
                TraceUtils.a aVarG = traceUtils.g();
                if (aVarG == null) {
                    IQQGProWrapperSession iQQGProWrapperSession2 = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession2);
                    directSessionService = iQQGProWrapperSession2.getDirectSessionService();
                } else {
                    aVarG.a("getGProDirectSessionService");
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    IQQGProWrapperSession iQQGProWrapperSession3 = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession3);
                    IKernelDirectSessionService directSessionService2 = iQQGProWrapperSession3.getDirectSessionService();
                    aVarG.a("getGProDirectSessionService#end# " + (SystemClock.uptimeMillis() - jUptimeMillis) + " ms");
                    directSessionService = directSessionService2;
                }
            }
            ServiceContent serviceContent = this.this$0.serviceContent;
            if (serviceContent == null) {
                Intrinsics.throwUninitializedPropertyAccessException("serviceContent");
                serviceContent = null;
            }
            return new GProDirectSessionService(directSessionService, serviceContent);
        }
    });

    @NotNull
    private final com.tencent.mobileqq.qqguildsdk.util.d<GuildFileTransferService> guildFileTransferService = com.tencent.mobileqq.qqguildsdk.util.e.a(new Function0<GuildFileTransferService>() { // from class: com.tencent.mobileqq.qqguildsdk.api.impl.GProSessionImpl$guildFileTransferService$1
        {
            super(0);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // kotlin.jvm.functions.Function0
        @Nullable
        public final GuildFileTransferService invoke() {
            IKernelGuildFileTransferService guildFileTransferService;
            TraceUtils traceUtils = TraceUtils.f281741a;
            GProSessionImpl gProSessionImpl = this.this$0;
            if (TraceUtils.h()) {
                try {
                    com.tencent.mobileqq.guild.util.ed.c("guildFileTransferService");
                    IQQGProWrapperSession iQQGProWrapperSession = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession);
                    guildFileTransferService = iQQGProWrapperSession.getGuildFileTransferService();
                    com.tencent.mobileqq.guild.util.ed.d();
                } catch (Throwable th5) {
                    com.tencent.mobileqq.guild.util.ed.d();
                    throw th5;
                }
            } else {
                TraceUtils.a aVarG = traceUtils.g();
                if (aVarG == null) {
                    IQQGProWrapperSession iQQGProWrapperSession2 = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession2);
                    guildFileTransferService = iQQGProWrapperSession2.getGuildFileTransferService();
                } else {
                    aVarG.a("guildFileTransferService");
                    long jUptimeMillis = SystemClock.uptimeMillis();
                    IQQGProWrapperSession iQQGProWrapperSession3 = gProSessionImpl.gproWrapperSession;
                    Intrinsics.checkNotNull(iQQGProWrapperSession3);
                    IKernelGuildFileTransferService guildFileTransferService2 = iQQGProWrapperSession3.getGuildFileTransferService();
                    aVarG.a("guildFileTransferService#end# " + (SystemClock.uptimeMillis() - jUptimeMillis) + " ms");
                    guildFileTransferService = guildFileTransferService2;
                }
            }
            ServiceContent serviceContent = this.this$0.serviceContent;
            if (serviceContent == null) {
                Intrinsics.throwUninitializedPropertyAccessException("serviceContent");
                serviceContent = null;
            }
            return new GuildFileTransferService(guildFileTransferService, serviceContent);
        }
    });

    /* JADX INFO: renamed from: com.tencent.mobileqq.qqguildsdk.api.impl.GProSessionImpl$a, reason: from kotlin metadata */
    public static final class Companion {
        public Companion(DefaultConstructorMarker defaultConstructorMarker) {
        }
    }

    public static final class b implements IGProDependsAdapter {
    }

    public static final class c implements IKernelGProSessionListener {
        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGProSessionListener
        public void onGProSessionCreate(int i15, @Nullable String str) {
            com.tencent.qqnt.kernelgpro.nativeinterface.g.a(this, i15, str);
            QLog.i(GProSessionImpl.TAG, 1, "onGProSessionCreate");
        }

        @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IKernelGProSessionListener
        public void onGetSelfTinyId(long j15) {
            com.tencent.qqnt.kernelgpro.nativeinterface.g.b(this, j15);
            QLog.i(GProSessionImpl.TAG, 1, "onGetSelfTinyId");
        }
    }

    private final void destroy() {
        re2.c gProGuildMsgService = getGProGuildMsgService();
        if (gProGuildMsgService != null) {
            gProGuildMsgService.destroy();
        }
        this.isStartFinish.set(false);
        this.isDestroy = true;
        ServiceContent serviceContent = this.serviceContent;
        ServiceContent serviceContent2 = null;
        if (serviceContent == null) {
            Intrinsics.throwUninitializedPropertyAccessException("serviceContent");
            serviceContent = null;
        }
        serviceContent.j(null);
        this.gproWrapperSession = null;
        this.gproGuildMsgService.clear();
        this.feedChannelServiceNew.clear();
        this.gProDirectSessionService.clear();
        this.guildFileTransferService.clear();
        ServiceContent serviceContent3 = this.serviceContent;
        if (serviceContent3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("serviceContent");
        } else {
            serviceContent2 = serviceContent3;
        }
        serviceContent2.i();
    }

    private final IGProDependsAdapter getIDependsAdapter() {
        return new b();
    }

    private final IKernelGProSessionListener getSessionListener() {
        return new c();
    }

    private final void onStart() {
        QLog.i(TAG, 1, "onStart start session start！");
        notifyGProSdkStart();
        ((IGuildDelayInitializeService) MobileQQ.sMobileQQ.peekAppRuntime().getRuntimeService(IGuildDelayInitializeService.class, "")).checkSessionInitComplete();
        ((IGuildMsgService) QRoute.api(IGuildMsgService.class)).init(getGProGuildMsgService());
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    @Nullable
    public com.tencent.qqnt.kernel.api.s getFeedService() {
        if (this.gproWrapperSession == null || !this.isStartFinish.get()) {
            return null;
        }
        return this.feedChannelServiceNew.getValue();
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    @Nullable
    public re2.a getGProDirectSessionService() {
        if (this.gproWrapperSession == null || !this.isStartFinish.get()) {
            return null;
        }
        return this.gProDirectSessionService.getValue();
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    @Nullable
    public re2.c getGProGuildMsgService() {
        if (this.gproWrapperSession == null || !this.isStartFinish.get()) {
            return null;
        }
        return this.gproGuildMsgService.getValue();
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    @NotNull
    /* JADX INFO: renamed from: getGProSdkStartCountDownLatch, reason: from getter */
    public CountDownLatch getGproStartCountDownLatch() {
        return this.gproStartCountDownLatch;
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    @Nullable
    public re2.h getGuildFileTransferService() {
        if (this.gproWrapperSession == null || !this.isStartFinish.get()) {
            return null;
        }
        return this.guildFileTransferService.getValue();
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    @Nullable
    public IKernelGuildService getGuildService() {
        if (this.gproWrapperSession == null || !this.isStartFinish.get()) {
            return null;
        }
        IQQGProWrapperSession iQQGProWrapperSession = this.gproWrapperSession;
        Intrinsics.checkNotNull(iQQGProWrapperSession);
        return iQQGProWrapperSession.getGuildService();
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    public void initBusinessModule(@NotNull HashMap<String, String> sessionMap) {
        String strValueOf;
        Intrinsics.checkNotNullParameter(sessionMap, "sessionMap");
        boolean z15 = true;
        QLog.i(TAG, 1, "initBusinessModule session=" + hashCode());
        String str = sessionMap.get(StartupSessionConstant.KGPROMODULENAME);
        ServiceContent serviceContent = null;
        this.gproWrapperSession = str != null ? com.tencent.qqnt.kernelgpro.nativeinterface.m.h(str) : null;
        ServiceContent serviceContent2 = this.serviceContent;
        if (serviceContent2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("serviceContent");
        } else {
            serviceContent = serviceContent2;
        }
        serviceContent.j(sessionMap.get(StartupSessionConstant.KGPROMODULENAME));
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        String currentUid = appRuntimePeekAppRuntime.getCurrentUid();
        long longAccountUin = appRuntimePeekAppRuntime.getLongAccountUin();
        String currentUin2 = appRuntimePeekAppRuntime.getCurrentUin();
        Intrinsics.checkNotNullExpressionValue(currentUin2, "getCurrentUin(...)");
        currentUin = currentUin2;
        if (currentUid != null && currentUid.length() != 0) {
            z15 = false;
        }
        if (z15) {
            strValueOf = String.valueOf(longAccountUin);
        } else {
            INSTANCE.getClass();
            if (currentUid == null) {
                currentUid = "";
            }
            strValueOf = currentUid;
        }
        String str2 = "gpro_v1-6_" + strValueOf + DBBackupServiceImpl.DB_FILE_SUFFIX;
        String str3 = AppSetting.f98894n;
        if (str3 == null) {
            str3 = "1.1";
        }
        String str4 = str3;
        IQQGProWrapperSession iQQGProWrapperSession = this.gproWrapperSession;
        Intrinsics.checkNotNull(iQQGProWrapperSession);
        iQQGProWrapperSession.init(new GProInitSessionConfig(longAccountUin, strValueOf, str2, str4, "", "", ""), getIDependsAdapter(), getSessionListener());
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    /* JADX INFO: renamed from: isDestroy, reason: from getter */
    public boolean getIsDestroy() {
        return this.isDestroy;
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    public boolean isInit() {
        return this.isStartFinish.get();
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    public void notifyGProSdkStart() {
        this.gproStartCountDownLatch.countDown();
    }

    @Override // p022mqq.app.api.IRuntimeService
    public void onCreate(@Nullable AppRuntime appRuntime) {
        QLog.i(TAG, 1, "onCreate session=" + hashCode());
        this.serviceContent = new ServiceContent(appRuntime);
    }

    @Override // p022mqq.app.api.IRuntimeService
    public void onDestroy() {
        QLog.i(TAG, 1, "onDestroy session=" + hashCode());
        destroy();
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    public void start() {
        QLog.i(TAG, 1, "start session=" + hashCode());
        this.isStartFinish.set(true);
        onStart();
    }

    @Override // com.tencent.mobileqq.qqguildsdk.api.IGProSession
    public void stop() {
        QLog.i(TAG, 1, "stop session=" + hashCode());
    }
}