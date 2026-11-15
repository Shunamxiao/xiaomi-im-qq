package com.tencent.mobileqq.guild.feed.publish.publish;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.google.gson.GsonBuilder;
import com.tencent.biz.richframework.eventbus.SimpleEventBus;
import com.tencent.biz.richframework.network.request.BaseRequest;
import com.tencent.common.config.AppSetting;
import com.tencent.mobileqq.R;
import com.tencent.mobileqq.app.ThreadManagerV2;
import com.tencent.mobileqq.guild.data.m;
import com.tencent.mobileqq.guild.feed.IGuildFeedPublishQualityReporterApi;
import com.tencent.mobileqq.guild.feed.event.GuildFeedDeleteEvent;
import com.tencent.mobileqq.guild.feed.event.GuildFeedDetailTopUpdateEvent;
import com.tencent.mobileqq.guild.feed.manager.GuildFeedDetailDataCacheManager;
import com.tencent.mobileqq.guild.feed.nativedetail.util.n;
import com.tencent.mobileqq.guild.feed.nativedetail.util.t;
import com.tencent.mobileqq.guild.feed.publish.constants.GuildFeedPublishQualityDataBuilder;
import com.tencent.mobileqq.guild.feed.publish.event.GuildPublishSuccessGetFeedEvent;
import com.tencent.mobileqq.guild.feed.publish.helper.FeedPublishTaskStateHelper;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.scene.FeedEditorBusinessEnum;
import com.tencent.mobileqq.guild.feed.util.GuildPreferUtils;
import com.tencent.mobileqq.guild.feed.util.ac;
import com.tencent.mobileqq.guild.feed.util.ah;
import com.tencent.mobileqq.guild.feed.util.bc;
import com.tencent.mobileqq.guild.feed.util.r;
import com.tencent.mobileqq.guild.mainframe.GuildFeedReportSourceInfo;
import com.tencent.mobileqq.guild.util.IQQGuildUtilApi;
import com.tencent.mobileqq.guild.util.Logger;
import com.tencent.mobileqq.guild.util.bw;
import com.tencent.mobileqq.guild.util.dm;
import com.tencent.mobileqq.mini.servlet.MiniAppGetGameTaskTicketServlet;
import com.tencent.mobileqq.pskey.api.IPskeyManager;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.mobileqq.qfix.redirect.PatchRedirectCenter;
import com.tencent.mobileqq.qqguildsdk.api.IGPSService;
import com.tencent.mobileqq.qqguildsdk.data.IGProChannelInfo;
import com.tencent.mobileqq.qroute.QRoute;
import com.tencent.mobileqq.util.SSODebugUtil;
import com.tencent.mobileqq.widget.QQToast;
import com.tencent.qphone.base.util.BaseApplication;
import com.tencent.qphone.base.util.QLog;
import com.tencent.qqnt.kernel.api.s;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProFeedEntry;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProHttpReqInfo;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStAlterFeedReq;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStAlterFeedRsp;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStChannelSign;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStClientContent;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStClientImageContent;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStClientVideoContent;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStCommonExt;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStFeed;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStGetFeedDetailReq;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStGetFeedDetailRsp;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStPublishFeedReq;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStPublishFeedRsp;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStRecomInfo;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProTotalCollect;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProTotalPrefer;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProAlterFeedCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGProGetFeedDetailCallback;
import com.tencent.qqnt.kernelgpro.nativeinterface.IGPropublishFeedCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import p022mqq.app.AppRuntime;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes26.dex */
public class k implements m {
    static IPatchRedirector $redirector_;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private static Integer f261217p;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f261218d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private boolean f261219e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private boolean f261220f;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private String f261221h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private String f261222i;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    @NonNull
    private final com.tencent.mobileqq.guild.feed.publish.a f261223m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private boolean f261224n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private final FeedEditorPostPreProcessor f261225o;

    /* JADX INFO: compiled from: P */
    class a implements db2.a {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ GProHttpReqInfo f261226a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ Runnable f261227b;

        a(GProHttpReqInfo gProHttpReqInfo, Runnable runnable) {
            this.f261226a = gProHttpReqInfo;
            this.f261227b = runnable;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, k.this, gProHttpReqInfo, runnable);
        }

        @Override // db2.a
        public void onFail(@NonNull String str) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
                iPatchRedirector.redirect((short) 3, (Object) this, (Object) str);
                return;
            }
            QLog.i("PostFeedTask.RequestSenderTask", 1, "[getPskey]: mainTaskId = " + k.this.f261223m.c() + ", error: " + str);
            this.f261227b.run();
        }

        @Override // db2.a
        public void onSuccess(@NonNull Map<String, String> map) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) map);
                return;
            }
            String str = map.get("pd.qq.com");
            if (TextUtils.isEmpty(str)) {
                QLog.i("PostFeedTask.RequestSenderTask", 1, "[getPskey]: mainTaskId = " + k.this.f261223m.c() + ", success, but it is empty");
            } else {
                this.f261226a.pskey = str;
            }
            this.f261227b.run();
        }
    }

    /* JADX INFO: compiled from: P */
    class b implements db2.a {
        static IPatchRedirector $redirector_;

        b() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) k.this);
        }

        @Override // db2.a
        public void onFail(@NonNull String str) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
                iPatchRedirector.redirect((short) 3, (Object) this, (Object) str);
                return;
            }
            QLog.e("PostFeedTask.RequestSenderTask", 1, "[replacePsk]: mainTaskId=" + k.this.f261223m.c() + ", error: " + str);
        }

        @Override // db2.a
        public void onSuccess(@NonNull Map<String, String> map) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) map);
                return;
            }
            QLog.i("PostFeedTask.RequestSenderTask", 1, "[replacePSK]: mainTaskId=" + k.this.f261223m.c() + " success, contain= " + map.containsKey("pd.qq.com"));
        }
    }

    static {
        IPatchRedirector redirector = PatchRedirectCenter.getRedirector(139255);
        $redirector_ = redirector;
        if (redirector == null || !redirector.hasPatch((short) 14)) {
            return;
        }
        redirector.redirect((short) 14);
    }

    public k(boolean z15, String str, String str2, @NonNull com.tencent.mobileqq.guild.feed.publish.a aVar) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
            iPatchRedirector.redirect((short) 1, this, Boolean.valueOf(z15), str, str2, aVar);
            return;
        }
        this.f261224n = false;
        this.f261218d = z15;
        this.f261221h = str;
        this.f261222i = str2;
        this.f261223m = aVar;
        this.f261225o = new FeedEditorPostPreProcessor("PostFeedTask.RequestSenderTask", aVar.c());
    }

    public static /* synthetic */ void a(final k kVar, final GProStPublishFeedReq gProStPublishFeedReq, s sVar) {
        kVar.getClass();
        try {
            QLog.i("PostFeedTask.RequestSenderTask", 1, "[publishFeedRequest] advanced_settings=" + new JSONObject(gProStPublishFeedReq.jsonFeed).optJSONObject("advanced_settings"));
        } catch (Exception unused) {
        }
        sVar.publishFeed(gProStPublishFeedReq, new IGPropublishFeedCallback() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.c
            @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGPropublishFeedCallback
            public final void onResult(int i15, String str, boolean z15, GProStPublishFeedRsp gProStPublishFeedRsp) {
                k.e(this.f261194a, gProStPublishFeedReq, i15, str, z15, gProStPublishFeedRsp);
            }
        });
        kVar.v("CH_E_PUBLISH_FEEDS_START");
    }

    public static /* synthetic */ void b(final k kVar, final GProStAlterFeedReq gProStAlterFeedReq, s sVar) {
        kVar.getClass();
        try {
            QLog.i("PostFeedTask.RequestSenderTask", 1, "[alterFeedRequest] advanced_settings=" + new JSONObject(gProStAlterFeedReq.jsonFeed).optJSONObject("advanced_settings"));
        } catch (Exception unused) {
        }
        sVar.alterFeed(gProStAlterFeedReq, new IGProAlterFeedCallback() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.d
            @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProAlterFeedCallback
            public final void onResult(int i15, String str, boolean z15, GProStAlterFeedRsp gProStAlterFeedRsp) {
                k.f(this.f261196a, gProStAlterFeedReq, i15, str, z15, gProStAlterFeedRsp);
            }
        });
        kVar.v("CH_E_MODIFY_FEEDS_START");
    }

    public static /* synthetic */ void c(k kVar, int i15, String str, GProStPublishFeedReq gProStPublishFeedReq, GProStPublishFeedRsp gProStPublishFeedRsp) {
        kVar.getClass();
        boolean z15 = i15 == 0;
        QLog.i("PostFeedTask.RequestSenderTask", 1, "[publishFeedRequest]  onReceive | isSuccess = " + z15 + " | retCode = " + i15 + " | errMsg = " + str + " | mainTaskId = " + kVar.f261223m.c());
        kVar.l(gProStPublishFeedReq, z15, i15, str, gProStPublishFeedRsp != null ? gProStPublishFeedRsp.feed : new GProStFeed());
    }

    public static void d(final k kVar, final GuildPublishSuccessGetFeedEvent guildPublishSuccessGetFeedEvent) {
        kVar.getClass();
        SimpleEventBus.getInstance().dispatchEvent(guildPublishSuccessGetFeedEvent);
        QLog.i("PostFeedTask.RequestSenderTask", 1, "[fetchFeedDetailAndDispatchResult] mainTaskId = " + guildPublishSuccessGetFeedEvent.getMainTaskId() + ", isTaskCancelled: " + kVar.f261224n);
        if (kVar.f261224n) {
            ah.f262302a.b(new ah.a(BaseApplication.getContext(), Long.parseLong(kVar.getGuildId()), Long.parseLong(kVar.getChannelId()), guildPublishSuccessGetFeedEvent.getStFeed().idd, guildPublishSuccessGetFeedEvent.getStFeed().poster.idd, guildPublishSuccessGetFeedEvent.getStFeed().createTime, new Function0() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.j
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    k kVar2 = this.f261215d;
                    kVar2.getClass();
                    com.tencent.mobileqq.guild.feed.localfakefeed.a aVar = com.tencent.mobileqq.guild.feed.localfakefeed.a.f254502a;
                    GuildPublishSuccessGetFeedEvent guildPublishSuccessGetFeedEvent2 = guildPublishSuccessGetFeedEvent;
                    SimpleEventBus.getInstance().dispatchEvent(new GuildFeedDeleteEvent(aVar.c(guildPublishSuccessGetFeedEvent2.getMainTaskId()), kVar2.getGuildId(), kVar2.getChannelId()));
                    SimpleEventBus.getInstance().dispatchEvent(new GuildFeedDeleteEvent(guildPublishSuccessGetFeedEvent2.getStFeed().idd, kVar2.getGuildId(), kVar2.getChannelId()));
                    QLog.i("PostFeedTask.RequestSenderTask", 1, "deleteFeedAfterPublished: feedId:" + guildPublishSuccessGetFeedEvent2.getStFeed().idd + " mainTaskId: " + guildPublishSuccessGetFeedEvent2.getMainTaskId());
                    return null;
                }
            }, null));
        } else if (guildPublishSuccessGetFeedEvent.showSuccessToast()) {
            t.q(2, R.string.z_j);
        }
    }

    public static /* synthetic */ void e(final k kVar, final GProStPublishFeedReq gProStPublishFeedReq, final int i15, final String str, boolean z15, final GProStPublishFeedRsp gProStPublishFeedRsp) {
        kVar.q(i15);
        ThreadManagerV2.getUIHandlerV2().post(new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.f
            @Override // java.lang.Runnable
            public final void run() {
                k.c(this.f261203d, i15, str, gProStPublishFeedReq, gProStPublishFeedRsp);
            }
        });
    }

    public static /* synthetic */ void f(final k kVar, final GProStAlterFeedReq gProStAlterFeedReq, final int i15, final String str, boolean z15, final GProStAlterFeedRsp gProStAlterFeedRsp) {
        kVar.q(i15);
        ThreadManagerV2.getUIHandlerV2().post(new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.e
            @Override // java.lang.Runnable
            public final void run() {
                k.g(this.f261198d, i15, str, gProStAlterFeedReq, gProStAlterFeedRsp);
            }
        });
    }

    public static /* synthetic */ void g(k kVar, int i15, String str, GProStAlterFeedReq gProStAlterFeedReq, GProStAlterFeedRsp gProStAlterFeedRsp) {
        kVar.getClass();
        boolean z15 = i15 == 0;
        QLog.i("PostFeedTask.RequestSenderTask", 1, "[AlterFeedRequest]  onReceive | isSuccess = " + z15 + " | retCode =" + i15 + " | errMsg = " + str + " | mainTaskId = " + kVar.f261223m.c());
        kVar.l(gProStAlterFeedReq, z15, i15, str, gProStAlterFeedRsp != null ? gProStAlterFeedRsp.feed : new GProStFeed());
    }

    public static /* synthetic */ void h(k kVar, String str, int i15) {
        if (dm.C(kVar.f261221h)) {
            com.tencent.mobileqq.guild.feed.nativedetail.util.m.f257580a.a(kVar.f261221h, kVar.f261222i, str, i15);
            return;
        }
        GuildFeedDetailTopUpdateEvent guildFeedDetailTopUpdateEvent = new GuildFeedDetailTopUpdateEvent();
        guildFeedDetailTopUpdateEvent.setGuildId(kVar.f261221h);
        guildFeedDetailTopUpdateEvent.setChannelId(kVar.f261222i);
        guildFeedDetailTopUpdateEvent.setTopType(i15);
        SimpleEventBus.getInstance().dispatchEvent(guildFeedDetailTopUpdateEvent);
    }

    private void j(long j15, String str, String str2, String str3) {
        ((IGuildFeedPublishQualityReporterApi) QRoute.api(IGuildFeedPublishQualityReporterApi.class)).uploadQualityReport(new GuildFeedPublishQualityDataBuilder().setKeyEventId(str2).setTraceId(this.f261223m.c()).setExt1(BaseRequest.generateTraceId()).setExt2(String.valueOf(new Random().nextInt())).setExt3(bc.k()).setExt4(this.f261221h).setExt5(this.f261222i).setExt6(str3).setKeyRetCode(String.valueOf(j15)).setDesc(str));
    }

    private void l(Object obj, boolean z15, long j15, String str, GProStFeed gProStFeed) {
        if (obj instanceof GProStPublishFeedReq) {
            j(j15, str, "CH_E_PUBLISH_FEEDS_RESULT", null);
        } else if (obj instanceof GProStAlterFeedReq) {
            j(j15, str, "CH_E_MODIFY_FEEDS_RESULT", null);
        }
        String str2 = gProStFeed.idd;
        com.tencent.mobileqq.guild.feed.publish.a aVar = this.f261223m;
        aVar.a(z15, (int) j15, str, str2);
        final int i15 = 1;
        if (!z15) {
            int i16 = bc.f262335d;
            if (!(j15 == 10027)) {
                if (!(j15 == 10009 || j15 == 10047)) {
                    if (!(j15 == 20027)) {
                        return;
                    }
                }
                if (aVar.d()) {
                    QQToast.makeText(BaseApplication.getContext(), 1, str, 0).show();
                    return;
                }
                return;
            }
        }
        final GuildPublishSuccessGetFeedEvent guildPublishSuccessGetFeedEvent = new GuildPublishSuccessGetFeedEvent();
        guildPublishSuccessGetFeedEvent.setMainTaskId(aVar.c());
        guildPublishSuccessGetFeedEvent.setShowSuccessToast(aVar.e());
        guildPublishSuccessGetFeedEvent.setStFeed(gProStFeed);
        guildPublishSuccessGetFeedEvent.setEdit(this.f261218d);
        guildPublishSuccessGetFeedEvent.setGuildId(this.f261221h);
        guildPublishSuccessGetFeedEvent.setChannelId(this.f261222i);
        s sVarC = r.c();
        if (sVarC != null) {
            GProStGetFeedDetailReq gProStGetFeedDetailReq = new GProStGetFeedDetailReq();
            gProStGetFeedDetailReq.from = 0;
            gProStGetFeedDetailReq.renderSticker = true;
            gProStGetFeedDetailReq.detailType = 1;
            gProStGetFeedDetailReq.feedId = guildPublishSuccessGetFeedEvent.getStFeed().idd;
            gProStGetFeedDetailReq.createTime = guildPublishSuccessGetFeedEvent.getStFeed().createTime;
            gProStGetFeedDetailReq.userId = guildPublishSuccessGetFeedEvent.getStFeed().poster.idd;
            gProStGetFeedDetailReq.channelSign = guildPublishSuccessGetFeedEvent.getStFeed().channelInfo.sign;
            gProStGetFeedDetailReq.contentType = 2;
            gProStGetFeedDetailReq.needExternalComment = true;
            gProStGetFeedDetailReq.notReportReadCnt = true;
            sVarC.getFeedDetail(gProStGetFeedDetailReq, false, new IGProGetFeedDetailCallback() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.g
                /* JADX WARN: Type inference failed for: r4v2 */
                /* JADX WARN: Type inference failed for: r4v3, types: [boolean, int] */
                /* JADX WARN: Type inference failed for: r4v9 */
                @Override // com.tencent.qqnt.kernelgpro.nativeinterface.IGProGetFeedDetailCallback
                public final void onResult(int i17, String str3, boolean z16, GProStGetFeedDetailRsp gProStGetFeedDetailRsp) throws JSONException {
                    ?? r15;
                    GProStFeed gProStFeed2;
                    final k kVar = this.f261208a;
                    kVar.getClass();
                    final GuildPublishSuccessGetFeedEvent guildPublishSuccessGetFeedEvent2 = guildPublishSuccessGetFeedEvent;
                    if (i17 != 0 || gProStGetFeedDetailRsp == null || (gProStFeed2 = gProStGetFeedDetailRsp.feed) == null) {
                        QLog.e("PostFeedTask.RequestSenderTask", 1, "fetchFeedDetailAndDispatchResult|result= " + i17, " errMsg=", str3);
                    } else {
                        guildPublishSuccessGetFeedEvent2.setStFeed(gProStFeed2);
                        guildPublishSuccessGetFeedEvent2.setFeedFullDetail(true);
                        GuildFeedDetailDataCacheManager.m().d(gProStGetFeedDetailRsp.feed);
                    }
                    GProStFeed svrStFeed = guildPublishSuccessGetFeedEvent2.getStFeed();
                    String key = guildPublishSuccessGetFeedEvent2.getMainTaskId();
                    GuildPreferUtils guildPreferUtils = GuildPreferUtils.f262258a;
                    Intrinsics.checkNotNullParameter(svrStFeed, "svrStFeed");
                    Intrinsics.checkNotNullParameter(key, "key");
                    if (GuildPreferUtils.f(key)) {
                        GProTotalPrefer gProTotalPrefer = svrStFeed.totalPrefer;
                        gProTotalPrefer.preferStatus = 1;
                        gProTotalPrefer.preferCountWithoutLike++;
                        String guildUserDisplayNameInternal = ((IGPSService) ((IQQGuildUtilApi) QRoute.api(IQQGuildUtilApi.class)).runtimeService(IGPSService.class)).getGuildUserDisplayNameInternal(String.valueOf(svrStFeed.channelInfo.sign.guildId), bc.k());
                        Intrinsics.checkNotNullExpressionValue(guildUserDisplayNameInternal, "getGuildUserDisplayNameInternal(...)");
                        if (guildUserDisplayNameInternal.length() > 0) {
                            svrStFeed.externalLikeUserList.add(GuildPreferUtils.e(svrStFeed, guildUserDisplayNameInternal));
                        }
                        GuildPreferUtils.a aVar2 = new GuildPreferUtils.a();
                        aVar2.f262259a = 0;
                        aVar2.f262260b = 2000;
                        aVar2.f262261c = svrStFeed.idd;
                        GProStChannelSign gProStChannelSign = svrStFeed.channelInfo.sign;
                        aVar2.f262262d = gProStChannelSign.guildId;
                        aVar2.f262263e = gProStChannelSign.channelId;
                        GuildPreferUtils.b(aVar2);
                        GuildPreferUtils.i(key, false);
                        Logger.a aVarD = Logger.f281727a.d();
                        GProStChannelSign gProStChannelSign2 = svrStFeed.channelInfo.sign;
                        r15 = 1;
                        aVarD.i("Guild_Feed_GuildPreferUtils", 1, "supplyLocalFeedPrefer guildId:" + gProStChannelSign2.guildId + " channelId:" + gProStChannelSign2.channelId + " key:" + key + " feedId:" + svrStFeed.idd);
                    } else {
                        r15 = 1;
                    }
                    GProStFeed svrStFeed2 = guildPublishSuccessGetFeedEvent2.getStFeed();
                    String key2 = guildPublishSuccessGetFeedEvent2.getMainTaskId();
                    ac acVar = ac.f262293a;
                    Intrinsics.checkNotNullParameter(svrStFeed2, "svrStFeed");
                    Intrinsics.checkNotNullParameter(key2, "key");
                    if (ac.d(key2)) {
                        GProTotalCollect gProTotalCollect = svrStFeed2.totalCollect;
                        gProTotalCollect.isCollected = r15;
                        gProTotalCollect.collectCount += r15;
                        String idd = svrStFeed2.idd;
                        Intrinsics.checkNotNullExpressionValue(idd, "idd");
                        GProStCommonExt gProStCommonExt = new GProStCommonExt();
                        com.tencent.mobileqq.guild.feed.nativepublish.utils.j.c(gProStCommonExt);
                        GProStRecomInfo gProStRecomInfo = svrStFeed2.recomInfo;
                        String str4 = gProStRecomInfo != null ? gProStRecomInfo.recomJson : null;
                        if (str4 == null) {
                            str4 = "";
                        }
                        com.tencent.mobileqq.guild.feed.nativepublish.utils.j.a(str4, gProStCommonExt);
                        Unit unit = Unit.INSTANCE;
                        ac.e(new ac.a(idd, true, gProStCommonExt), null);
                        ac.f(key2, false);
                        Logger.a aVarD2 = Logger.f281727a.d();
                        GProStChannelSign gProStChannelSign3 = svrStFeed2.channelInfo.sign;
                        aVarD2.i("Guild_Feed_GuildFeedCollectUtils", 1, "supplyLocalFeedCollect guildId:" + gProStChannelSign3.guildId + " channelId:" + gProStChannelSign3.channelId + " key:" + key2 + " feedId:" + svrStFeed2.idd);
                    }
                    com.tencent.mobileqq.guild.feed.emotionreaction.e.g(guildPublishSuccessGetFeedEvent2.getStFeed(), guildPublishSuccessGetFeedEvent2.getMainTaskId());
                    FeedPublishTaskStateHelper.c(guildPublishSuccessGetFeedEvent2);
                    ThreadManagerV2.getUIHandlerV2().post(new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.i
                        @Override // java.lang.Runnable
                        public final void run() {
                            k.d(kVar, guildPublishSuccessGetFeedEvent2);
                        }
                    });
                }
            });
        }
        if (this.f261218d) {
            final String str3 = gProStFeed.idd;
            n nVar = n.f257581a;
            boolean zE = nVar.e(this.f261221h, str3, "", Long.valueOf(gProStFeed.topInfo.topTimestamp));
            IGProChannelInfo iGProChannelInfoK = dm.k(this.f261222i);
            Boolean boolValueOf = (iGProChannelInfoK == null || iGProChannelInfoK.isHiddenPostChannel()) ? null : Boolean.valueOf(nVar.e(this.f261221h, str3, this.f261222i, Long.valueOf(gProStFeed.topInfo.channelTopTimestamp)));
            if (zE || (boolValueOf != null && boolValueOf.booleanValue())) {
                boolean zEquals = Boolean.TRUE.equals(boolValueOf);
                if (zE && zEquals) {
                    i15 = 3;
                } else if (zEquals) {
                    i15 = 2;
                }
                ThreadManagerV2.getUIHandlerV2().postDelayed(new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.h
                    @Override // java.lang.Runnable
                    public final void run() {
                        k.h(this.f261210d, str3, i15);
                    }
                }, 500L);
            }
        }
    }

    private void n(JSONObject jSONObject, GProStCommonExt gProStCommonExt) throws JSONException {
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("req_ext_info");
        if (jSONArrayOptJSONArray == null || jSONArrayOptJSONArray.length() <= 0) {
            return;
        }
        for (int i15 = 0; i15 < jSONArrayOptJSONArray.length(); i15++) {
            JSONObject jSONObject2 = jSONArrayOptJSONArray.getJSONObject(i15);
            String string = jSONObject2.getString("key");
            String string2 = jSONObject2.getString("value");
            GProFeedEntry gProFeedEntry = new GProFeedEntry();
            gProFeedEntry.key = string;
            gProFeedEntry.value = string2;
            gProStCommonExt.mapInfoList.add(gProFeedEntry);
        }
    }

    private void q(long j15) {
        AppRuntime appRuntimeN;
        if (j15 == 4003 && (appRuntimeN = dm.N()) != null) {
            ((IPskeyManager) appRuntimeN.getRuntimeService(IPskeyManager.class, "")).replacePskey(new String[]{"pd.qq.com"}, new b());
        }
    }

    private void v(String str) {
        ((IGuildFeedPublishQualityReporterApi) QRoute.api(IGuildFeedPublishQualityReporterApi.class)).uploadQualityReport(new GuildFeedPublishQualityDataBuilder().setKeyEventId(str).setTraceId(this.f261223m.c()).setExt3(bc.k()).setExt4(this.f261221h).setExt5(this.f261222i).setKeyRetCode("0"));
    }

    private void w(@NonNull com.tencent.mobileqq.guild.feed.publish.bean.a aVar, @NonNull GProStClientContent gProStClientContent, @NonNull com.tencent.mobileqq.guild.feed.publish.api.d.a aVar2, @NonNull GProStCommonExt gProStCommonExt, @NonNull Map<String, String> map) throws Exception {
        JSONObject jSONObject = new JSONObject(aVar.jsonFeed);
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("medias");
        if (jSONArrayOptJSONArray != null) {
            for (int i15 = 0; i15 < jSONArrayOptJSONArray.length(); i15++) {
                try {
                    JSONObject jSONObject2 = new JSONObject(jSONArrayOptJSONArray.getJSONObject(i15).getString("mediaInfo"));
                    if (jSONObject2.has("mediaType")) {
                        if (jSONObject2.getInt("mediaType") == 0) {
                            GProStClientImageContent gProStClientImageContent = new GProStClientImageContent();
                            gProStClientImageContent.taskId = jSONObject2.getString(MiniAppGetGameTaskTicketServlet.KEY_TASKID);
                            JSONObject jSONObject3 = new JSONObject(jSONObject2.getString("svrContentJson"));
                            gProStClientImageContent.url = jSONObject3.getString("picUrl");
                            gProStClientImageContent.isOrig = jSONObject3.getBoolean("is_orig");
                            gProStClientImageContent.origSize = jSONObject3.getInt("orig_size");
                            gProStClientImageContent.isGif = jSONObject3.getBoolean("is_gif");
                            gProStClientImageContent.md5 = jSONObject3.getString("imageMD5");
                            gProStClientContent.clientImageContents.add(gProStClientImageContent);
                        } else {
                            GProStClientVideoContent gProStClientVideoContent = new GProStClientVideoContent();
                            gProStClientVideoContent.taskId = jSONObject2.getString(MiniAppGetGameTaskTicketServlet.KEY_TASKID);
                            gProStClientVideoContent.coverUrl = com.tencent.mobileqq.guild.feed.publish.helper.a.a(jSONObject2.optString("overrideCoverTaskId"), jSONObject2.optString("videoCover"), aVar2);
                            gProStClientVideoContent.videoId = jSONObject2.getString(MiniAppGetGameTaskTicketServlet.KEY_TASKID);
                            gProStClientContent.clientVideoContents.add(gProStClientVideoContent);
                        }
                    }
                } catch (JSONException e15) {
                    QLog.e("PostFeedTask.RequestSenderTask", 1, "[parseClientContentByJson]: exception = ", e15);
                }
            }
        }
        this.f261225o.a(jSONObject, aVar2, map);
        jSONObject.remove("medias");
        jSONObject.remove("publishExtra");
        aVar.jsonFeed = jSONObject.toString();
        try {
            n(jSONObject, gProStCommonExt);
        } catch (Exception e16) {
            QLog.e("PostFeedTask.RequestSenderTask", 1, "process common extra info error: " + e16);
        }
        StringBuilder sb5 = new StringBuilder("[transformPostJson]: mainTaskId = ");
        sb5.append(this.f261223m.c());
        sb5.append(", jsBean.jsonFeed.length = ");
        String str = aVar.jsonFeed;
        sb5.append(str != null ? Integer.valueOf(str.length()) : null);
        QLog.i("PostFeedTask.RequestSenderTask", 1, sb5.toString());
    }

    private void x(GProHttpReqInfo gProHttpReqInfo, Runnable runnable) {
        AppRuntime appRuntimeN = dm.N();
        if (appRuntimeN == null) {
            runnable.run();
            return;
        }
        gProHttpReqInfo.appid = String.valueOf(AppSetting.e());
        if (f261217p == null) {
            SSODebugUtil.isTestEnv();
            f261217p = 0;
        }
        gProHttpReqInfo.routeId = String.valueOf(f261217p);
        gProHttpReqInfo.env = 0;
        ((IPskeyManager) appRuntimeN.getRuntimeService(IPskeyManager.class, "")).getPskey(new String[]{"pd.qq.com"}, new a(gProHttpReqInfo, runnable));
    }

    @Override // com.tencent.mobileqq.guild.data.m
    @NonNull
    public String getChannelId() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 4)) {
            return (String) iPatchRedirector.redirect((short) 4, (Object) this);
        }
        String str = this.f261222i;
        return str != null ? str : "";
    }

    @Override // com.tencent.mobileqq.guild.data.m
    @NonNull
    public String getGuildId() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
            return (String) iPatchRedirector.redirect((short) 3, (Object) this);
        }
        String str = this.f261221h;
        return str != null ? str : "";
    }

    public boolean k() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 7)) ? this.f261220f : ((Boolean) iPatchRedirector.redirect((short) 7, (Object) this)).booleanValue();
    }

    public void m() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 12)) {
            return;
        }
        iPatchRedirector.redirect((short) 12, (Object) this);
    }

    /* JADX WARN: Code duplicated, block: B:102:0x0086 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:105:0x01c4 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:106:0x0188 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:107:0x01e9 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:17:0x0077 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:18:0x0078  */
    /* JADX WARN: Code duplicated, block: B:23:0x0094  */
    /* JADX WARN: Code duplicated, block: B:24:0x0095  */
    /* JADX WARN: Code duplicated, block: B:27:0x009c A[Catch: Exception -> 0x00d9, TryCatch #2 {Exception -> 0x00d9, blocks: (B:21:0x0086, B:25:0x0096, B:27:0x009c, B:29:0x00a2, B:31:0x00ab, B:34:0x00b2, B:36:0x00cb, B:38:0x00d1), top: B:102:0x0086 }] */
    /* JADX WARN: Code duplicated, block: B:29:0x00a2 A[Catch: Exception -> 0x00d9, TryCatch #2 {Exception -> 0x00d9, blocks: (B:21:0x0086, B:25:0x0096, B:27:0x009c, B:29:0x00a2, B:31:0x00ab, B:34:0x00b2, B:36:0x00cb, B:38:0x00d1), top: B:102:0x0086 }] */
    /* JADX WARN: Code duplicated, block: B:30:0x00aa  */
    /* JADX WARN: Code duplicated, block: B:33:0x00b1  */
    /* JADX WARN: Code duplicated, block: B:34:0x00b2 A[Catch: Exception -> 0x00d9, TryCatch #2 {Exception -> 0x00d9, blocks: (B:21:0x0086, B:25:0x0096, B:27:0x009c, B:29:0x00a2, B:31:0x00ab, B:34:0x00b2, B:36:0x00cb, B:38:0x00d1), top: B:102:0x0086 }] */
    /* JADX WARN: Code duplicated, block: B:47:0x0114  */
    /* JADX WARN: Code duplicated, block: B:49:0x011f  */
    /* JADX WARN: Code duplicated, block: B:51:0x013e A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:52:0x0140  */
    /* JADX WARN: Code duplicated, block: B:53:0x0164  */
    /* JADX WARN: Code duplicated, block: B:56:0x0178  */
    /* JADX WARN: Code duplicated, block: B:60:0x01bc  */
    /* JADX WARN: Code duplicated, block: B:65:0x0213  */
    /* JADX WARN: Code duplicated, block: B:70:0x024d  */
    /* JADX WARN: Code duplicated, block: B:78:0x02a3  */
    /* JADX WARN: Code duplicated, block: B:81:0x02aa  */
    /* JADX WARN: Code duplicated, block: B:83:0x02bb  */
    /* JADX WARN: Code duplicated, block: B:86:0x02cd  */
    /* JADX WARN: Code duplicated, block: B:88:0x02d3  */
    /* JADX WARN: Code duplicated, block: B:89:0x02db  */
    /* JADX WARN: Code duplicated, block: B:91:0x037e  */
    /* JADX WARN: Code duplicated, block: B:92:0x0382  */
    /* JADX WARN: Code duplicated, block: B:95:0x038b  */
    /* JADX WARN: Instruction removed from duplicated block: B:49:0x011f, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:52:0x0140, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:89:0x02db, please report this as an issue */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public void o(String str, com.tencent.mobileqq.guild.feed.publish.api.d.a aVar, String str2, int i15, GuildFeedReportSourceInfo guildFeedReportSourceInfo) throws JSONException {
        com.tencent.mobileqq.guild.feed.publish.bean.a aVar2;
        String str3;
        HashMap map;
        JSONArray jSONArrayOptJSONArray;
        int i16;
        JSONObject jSONObjectOptJSONObject;
        String strOptString;
        String strOptString2;
        Map<String, com.tencent.mobileqq.guild.data.media.d> mapH;
        List<String> list;
        IPatchRedirector iPatchRedirector;
        com.tencent.mobileqq.guild.feed.publish.a aVar3;
        GProStClientContent gProStClientContent;
        ArrayList<GProStClientImageContent> arrayList;
        ArrayList<GProStClientVideoContent> arrayList2;
        com.tencent.mobileqq.guild.data.media.d dVar;
        Map<String, com.tencent.mobileqq.guild.data.media.d> map2;
        boolean z15;
        GProStClientImageContent gProStClientImageContent;
        String md5;
        GProStClientContent gProStClientContent2;
        GProStCommonExt gProStCommonExt;
        final s sVarC;
        final GProStPublishFeedReq gProStPublishFeedReq;
        FeedEditorBusinessEnum feedEditorBusinessEnumA;
        boolean z16;
        final s sVarC2;
        final GProStAlterFeedReq gProStAlterFeedReq;
        IPatchRedirector iPatchRedirector2 = $redirector_;
        if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 8)) {
            iPatchRedirector2.redirect((short) 8, this, str, aVar, str2, Integer.valueOf(i15), guildFeedReportSourceInfo);
            return;
        }
        if (!TextUtils.isEmpty(str)) {
            try {
                aVar2 = (com.tencent.mobileqq.guild.feed.publish.bean.a) new GsonBuilder().setLenient().create().fromJson(str, com.tencent.mobileqq.guild.feed.publish.bean.a.class);
            } catch (Exception e15) {
                QLog.e("PostFeedTask.RequestSenderTask", 1, "[tryMakeJsBean]: json = " + str.length(), e15);
                aVar2 = null;
            }
            if (aVar2 == null) {
                return;
            }
            str3 = aVar2.jsonFeed;
            map = new HashMap();
            if (!TextUtils.isEmpty(str3)) {
                try {
                    jSONArrayOptJSONArray = new JSONObject(str3).optJSONArray("medias");
                    if (jSONArrayOptJSONArray == null) {
                        for (i16 = 0; i16 < jSONArrayOptJSONArray.length(); i16++) {
                            jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i16);
                            if (jSONObjectOptJSONObject != null) {
                                strOptString = jSONObjectOptJSONObject.optString("mediaInfo");
                            } else {
                                strOptString = null;
                            }
                            if (TextUtils.isEmpty(strOptString)) {
                                JSONObject jSONObject = new JSONObject(strOptString);
                                strOptString2 = jSONObject.optString(MiniAppGetGameTaskTicketServlet.KEY_TASKID);
                                String strOptString3 = jSONObject.optString("overrideCoverTaskId");
                                if (strOptString2.isEmpty() && !strOptString3.isEmpty()) {
                                    map.put(strOptString2, strOptString3);
                                }
                            }
                        }
                    }
                } catch (Exception e16) {
                    QLog.e("PostFeedTask.CoverHelper", 1, "[parseLocalOverrideCoverMap] error", e16);
                }
            }
            mapH = aVar.c().h();
            list = aVar2.taskIds;
            iPatchRedirector = $redirector_;
            aVar3 = this.f261223m;
            if (iPatchRedirector == null && iPatchRedirector.hasPatch((short) 9)) {
                gProStClientContent2 = (GProStClientContent) iPatchRedirector.redirect((short) 9, this, mapH, list, map, aVar);
            } else {
                gProStClientContent = new GProStClientContent();
                if (mapH.isEmpty()) {
                    QLog.e("PostFeedTask.RequestSenderTask", 1, "mainTaskId=" + aVar3.c() + "taskList == null || taskList.isEmpty()");
                } else if (list == null) {
                    QLog.e("PostFeedTask.RequestSenderTask", 1, "[parseClientContent]: mainTaskId= " + aVar3.c() + "taskIds is NULL! taskList size: " + mapH.size());
                } else {
                    arrayList = new ArrayList<>();
                    arrayList2 = new ArrayList<>();
                    for (String str4 : list) {
                        dVar = mapH.get(str4);
                        if (dVar == null) {
                            map2 = mapH;
                            z15 = true;
                            QLog.i("PostFeedTask.RequestSenderTask", 1, "[parseClientContent] taskIds doesn't contain: " + str4 + ", taskIds size: " + list.size() + ", mainTaskId: " + aVar3.c());
                        } else {
                            map2 = mapH;
                            if (dVar.isVideoTask()) {
                                GProStClientVideoContent gProStClientVideoContent = new GProStClientVideoContent();
                                gProStClientVideoContent.taskId = str4;
                                gProStClientVideoContent.coverUrl = com.tencent.mobileqq.guild.feed.publish.helper.a.a((String) map.get(str4), dVar.getCoverUrl(), aVar);
                                gProStClientVideoContent.videoId = dVar.g();
                                arrayList2.add(gProStClientVideoContent);
                                mapH = map2;
                            } else {
                                gProStClientImageContent = new GProStClientImageContent();
                                gProStClientImageContent.taskId = str4;
                                gProStClientImageContent.url = dVar.getUrl();
                                gProStClientImageContent.isOrig = dVar.isRaw();
                                gProStClientImageContent.origSize = (int) dVar.e();
                                gProStClientImageContent.isGif = dVar.c();
                                md5 = dVar.getMd5();
                                if (!TextUtils.isEmpty(md5)) {
                                    gProStClientImageContent.md5 = md5;
                                }
                                arrayList.add(gProStClientImageContent);
                                z15 = true;
                                QLog.i("PostFeedTask.RequestSenderTask", 1, "parseClientContent add image taskId | mainTaskId = " + aVar3.c() + " | taskId = " + gProStClientImageContent.taskId);
                            }
                        }
                        mapH = map2;
                    }
                    gProStClientContent.clientImageContents = arrayList;
                    gProStClientContent.clientVideoContents = arrayList2;
                }
                gProStClientContent2 = gProStClientContent;
            }
            gProStCommonExt = new GProStCommonExt();
            if (aVar3.c() != null) {
                GProFeedEntry gProFeedEntry = new GProFeedEntry();
                gProFeedEntry.key = com.qzone.album.qun.business.model.b.CLIENTKEY;
                gProFeedEntry.value = aVar3.c();
                gProStCommonExt.mapInfoList.add(gProFeedEntry);
            }
            com.tencent.mobileqq.guild.feed.nativepublish.utils.j.b(guildFeedReportSourceInfo, gProStCommonExt);
            com.tencent.mobileqq.guild.feed.nativepublish.utils.j.c(gProStCommonExt);
            com.tencent.mobileqq.guild.feed.nativepublish.utils.j.a("", gProStCommonExt);
            bc.a(gProStCommonExt, str2);
            try {
                w(aVar2, gProStClientContent2, aVar, gProStCommonExt, map);
            } catch (Exception e17) {
                QLog.e("PostFeedTask.RequestSenderTask", 1, "[publishFeed]: transformPostJson failed, taskId=" + aVar3.c() + " exception: \n" + e17);
            }
            if (this.f261218d) {
                sVarC2 = r.c();
                if (sVarC2 != null) {
                    gProStAlterFeedReq = new GProStAlterFeedReq();
                    gProStAlterFeedReq.from = 0;
                    gProStAlterFeedReq.jsonFeed = aVar2.jsonFeed;
                    gProStAlterFeedReq.clientContent = gProStClientContent2;
                    if (i15 == 7) {
                        gProStAlterFeedReq.from = 1;
                    }
                    gProStAlterFeedReq.extInfo = gProStCommonExt;
                    x(gProStAlterFeedReq.httpReqInfo, new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.a
                        @Override // java.lang.Runnable
                        public final void run() {
                            k.b(this.f261188d, gProStAlterFeedReq, sVarC2);
                        }
                    });
                }
            } else {
                sVarC = r.c();
                if (sVarC == null) {
                    gProStPublishFeedReq = new GProStPublishFeedReq();
                    gProStPublishFeedReq.feed.channelInfo.sign.guildId = bw.d(getGuildId(), 0L);
                    gProStPublishFeedReq.feed.channelInfo.sign.channelId = bw.d(getChannelId(), 0L);
                    gProStPublishFeedReq.feed.clientTaskId = aVar3.c();
                    gProStPublishFeedReq.jsonFeed = aVar2.jsonFeed;
                    gProStPublishFeedReq.clientContent = gProStClientContent2;
                    gProStPublishFeedReq.from = 0;
                    QLog.d("PostFeedTask.RequestSenderTask", 4, "[publishFeedRequest] businessType = " + i15);
                    QLog.d("PostFeedTask.RequestSenderTask", 4, "[publishFeedRequest] jsonFeed = " + gProStPublishFeedReq.jsonFeed);
                    QLog.d("PostFeedTask.RequestSenderTask", 4, "[publishFeedRequest] guildId = " + gProStPublishFeedReq.feed.channelInfo.sign.guildId);
                    QLog.d("PostFeedTask.RequestSenderTask", 4, "[publishFeedRequest] channelId = " + gProStPublishFeedReq.feed.channelInfo.sign.channelId);
                    feedEditorBusinessEnumA = FeedEditorBusinessEnum.INSTANCE.a(i15);
                    gProStPublishFeedReq.from = feedEditorBusinessEnumA.getPublishFeedRequestFrom();
                    if (feedEditorBusinessEnumA == FeedEditorBusinessEnum.TROOP_ENTRY) {
                        z16 = true;
                        gProStPublishFeedReq.needSyncGroup = true;
                    } else {
                        z16 = true;
                    }
                    gProStPublishFeedReq.extInfo = gProStCommonExt;
                    if (this.f261219e) {
                        gProStPublishFeedReq.needCreateOwnGuild = z16;
                    }
                    x(gProStPublishFeedReq.httpReqInfo, new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.b
                        @Override // java.lang.Runnable
                        public final void run() {
                            k.a(this.f261191d, gProStPublishFeedReq, sVarC);
                        }
                    });
                    return;
                }
                QLog.d("PostFeedTask.RequestSenderTask", 1, "publishFeedRequest feedService == null");
            }
        }
        QLog.e("PostFeedTask.RequestSenderTask", 1, "[tryMakeJsBean]: json is empty");
        aVar2 = null;
        if (aVar2 == null) {
            return;
        }
        str3 = aVar2.jsonFeed;
        map = new HashMap();
        if (!TextUtils.isEmpty(str3)) {
            jSONArrayOptJSONArray = new JSONObject(str3).optJSONArray("medias");
            if (jSONArrayOptJSONArray == null) {
                while (i16 < jSONArrayOptJSONArray.length()) {
                    jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i16);
                    if (jSONObjectOptJSONObject != null) {
                        strOptString = jSONObjectOptJSONObject.optString("mediaInfo");
                    } else {
                        strOptString = null;
                    }
                    if (TextUtils.isEmpty(strOptString)) {
                        JSONObject jSONObject2 = new JSONObject(strOptString);
                        strOptString2 = jSONObject2.optString(MiniAppGetGameTaskTicketServlet.KEY_TASKID);
                        String strOptString4 = jSONObject2.optString("overrideCoverTaskId");
                        if (strOptString2.isEmpty()) {
                        }
                    }
                }
            }
        }
        mapH = aVar.c().h();
        list = aVar2.taskIds;
        iPatchRedirector = $redirector_;
        aVar3 = this.f261223m;
        if (iPatchRedirector == null) {
            gProStClientContent = new GProStClientContent();
            if (mapH.isEmpty()) {
                QLog.e("PostFeedTask.RequestSenderTask", 1, "mainTaskId=" + aVar3.c() + "taskList == null || taskList.isEmpty()");
            } else if (list == null) {
                QLog.e("PostFeedTask.RequestSenderTask", 1, "[parseClientContent]: mainTaskId= " + aVar3.c() + "taskIds is NULL! taskList size: " + mapH.size());
            } else {
                arrayList = new ArrayList<>();
                arrayList2 = new ArrayList<>();
                while (r16.hasNext()) {
                    dVar = mapH.get(str4);
                    if (dVar == null) {
                        map2 = mapH;
                        z15 = true;
                        QLog.i("PostFeedTask.RequestSenderTask", 1, "[parseClientContent] taskIds doesn't contain: " + str4 + ", taskIds size: " + list.size() + ", mainTaskId: " + aVar3.c());
                    } else {
                        map2 = mapH;
                        if (dVar.isVideoTask()) {
                            GProStClientVideoContent gProStClientVideoContent2 = new GProStClientVideoContent();
                            gProStClientVideoContent2.taskId = str4;
                            gProStClientVideoContent2.coverUrl = com.tencent.mobileqq.guild.feed.publish.helper.a.a((String) map.get(str4), dVar.getCoverUrl(), aVar);
                            gProStClientVideoContent2.videoId = dVar.g();
                            arrayList2.add(gProStClientVideoContent2);
                            mapH = map2;
                        } else {
                            gProStClientImageContent = new GProStClientImageContent();
                            gProStClientImageContent.taskId = str4;
                            gProStClientImageContent.url = dVar.getUrl();
                            gProStClientImageContent.isOrig = dVar.isRaw();
                            gProStClientImageContent.origSize = (int) dVar.e();
                            gProStClientImageContent.isGif = dVar.c();
                            md5 = dVar.getMd5();
                            if (!TextUtils.isEmpty(md5)) {
                                gProStClientImageContent.md5 = md5;
                            }
                            arrayList.add(gProStClientImageContent);
                            z15 = true;
                            QLog.i("PostFeedTask.RequestSenderTask", 1, "parseClientContent add image taskId | mainTaskId = " + aVar3.c() + " | taskId = " + gProStClientImageContent.taskId);
                        }
                    }
                    mapH = map2;
                }
                gProStClientContent.clientImageContents = arrayList;
                gProStClientContent.clientVideoContents = arrayList2;
            }
            gProStClientContent2 = gProStClientContent;
        } else {
            gProStClientContent = new GProStClientContent();
            if (mapH.isEmpty()) {
                QLog.e("PostFeedTask.RequestSenderTask", 1, "mainTaskId=" + aVar3.c() + "taskList == null || taskList.isEmpty()");
            } else if (list == null) {
                QLog.e("PostFeedTask.RequestSenderTask", 1, "[parseClientContent]: mainTaskId= " + aVar3.c() + "taskIds is NULL! taskList size: " + mapH.size());
            } else {
                arrayList = new ArrayList<>();
                arrayList2 = new ArrayList<>();
                while (r16.hasNext()) {
                    dVar = mapH.get(str4);
                    if (dVar == null) {
                        map2 = mapH;
                        z15 = true;
                        QLog.i("PostFeedTask.RequestSenderTask", 1, "[parseClientContent] taskIds doesn't contain: " + str4 + ", taskIds size: " + list.size() + ", mainTaskId: " + aVar3.c());
                    } else {
                        map2 = mapH;
                        if (dVar.isVideoTask()) {
                            GProStClientVideoContent gProStClientVideoContent3 = new GProStClientVideoContent();
                            gProStClientVideoContent3.taskId = str4;
                            gProStClientVideoContent3.coverUrl = com.tencent.mobileqq.guild.feed.publish.helper.a.a((String) map.get(str4), dVar.getCoverUrl(), aVar);
                            gProStClientVideoContent3.videoId = dVar.g();
                            arrayList2.add(gProStClientVideoContent3);
                            mapH = map2;
                        } else {
                            gProStClientImageContent = new GProStClientImageContent();
                            gProStClientImageContent.taskId = str4;
                            gProStClientImageContent.url = dVar.getUrl();
                            gProStClientImageContent.isOrig = dVar.isRaw();
                            gProStClientImageContent.origSize = (int) dVar.e();
                            gProStClientImageContent.isGif = dVar.c();
                            md5 = dVar.getMd5();
                            if (!TextUtils.isEmpty(md5)) {
                                gProStClientImageContent.md5 = md5;
                            }
                            arrayList.add(gProStClientImageContent);
                            z15 = true;
                            QLog.i("PostFeedTask.RequestSenderTask", 1, "parseClientContent add image taskId | mainTaskId = " + aVar3.c() + " | taskId = " + gProStClientImageContent.taskId);
                        }
                    }
                    mapH = map2;
                }
                gProStClientContent.clientImageContents = arrayList;
                gProStClientContent.clientVideoContents = arrayList2;
            }
            gProStClientContent2 = gProStClientContent;
        }
        gProStCommonExt = new GProStCommonExt();
        if (aVar3.c() != null) {
            GProFeedEntry gProFeedEntry2 = new GProFeedEntry();
            gProFeedEntry2.key = com.qzone.album.qun.business.model.b.CLIENTKEY;
            gProFeedEntry2.value = aVar3.c();
            gProStCommonExt.mapInfoList.add(gProFeedEntry2);
        }
        com.tencent.mobileqq.guild.feed.nativepublish.utils.j.b(guildFeedReportSourceInfo, gProStCommonExt);
        com.tencent.mobileqq.guild.feed.nativepublish.utils.j.c(gProStCommonExt);
        com.tencent.mobileqq.guild.feed.nativepublish.utils.j.a("", gProStCommonExt);
        bc.a(gProStCommonExt, str2);
        w(aVar2, gProStClientContent2, aVar, gProStCommonExt, map);
        if (this.f261218d) {
            sVarC2 = r.c();
            if (sVarC2 != null) {
                gProStAlterFeedReq = new GProStAlterFeedReq();
                gProStAlterFeedReq.from = 0;
                gProStAlterFeedReq.jsonFeed = aVar2.jsonFeed;
                gProStAlterFeedReq.clientContent = gProStClientContent2;
                if (i15 == 7) {
                    gProStAlterFeedReq.from = 1;
                }
                gProStAlterFeedReq.extInfo = gProStCommonExt;
                x(gProStAlterFeedReq.httpReqInfo, new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.a
                    @Override // java.lang.Runnable
                    public final void run() {
                        k.b(this.f261188d, gProStAlterFeedReq, sVarC2);
                    }
                });
            }
        } else {
            sVarC = r.c();
            if (sVarC == null) {
                gProStPublishFeedReq = new GProStPublishFeedReq();
                gProStPublishFeedReq.feed.channelInfo.sign.guildId = bw.d(getGuildId(), 0L);
                gProStPublishFeedReq.feed.channelInfo.sign.channelId = bw.d(getChannelId(), 0L);
                gProStPublishFeedReq.feed.clientTaskId = aVar3.c();
                gProStPublishFeedReq.jsonFeed = aVar2.jsonFeed;
                gProStPublishFeedReq.clientContent = gProStClientContent2;
                gProStPublishFeedReq.from = 0;
                QLog.d("PostFeedTask.RequestSenderTask", 4, "[publishFeedRequest] businessType = " + i15);
                QLog.d("PostFeedTask.RequestSenderTask", 4, "[publishFeedRequest] jsonFeed = " + gProStPublishFeedReq.jsonFeed);
                QLog.d("PostFeedTask.RequestSenderTask", 4, "[publishFeedRequest] guildId = " + gProStPublishFeedReq.feed.channelInfo.sign.guildId);
                QLog.d("PostFeedTask.RequestSenderTask", 4, "[publishFeedRequest] channelId = " + gProStPublishFeedReq.feed.channelInfo.sign.channelId);
                feedEditorBusinessEnumA = FeedEditorBusinessEnum.INSTANCE.a(i15);
                gProStPublishFeedReq.from = feedEditorBusinessEnumA.getPublishFeedRequestFrom();
                if (feedEditorBusinessEnumA == FeedEditorBusinessEnum.TROOP_ENTRY) {
                    z16 = true;
                    gProStPublishFeedReq.needSyncGroup = true;
                } else {
                    z16 = true;
                }
                gProStPublishFeedReq.extInfo = gProStCommonExt;
                if (this.f261219e) {
                    gProStPublishFeedReq.needCreateOwnGuild = z16;
                }
                x(gProStPublishFeedReq.httpReqInfo, new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publish.b
                    @Override // java.lang.Runnable
                    public final void run() {
                        k.a(this.f261191d, gProStPublishFeedReq, sVarC);
                    }
                });
                return;
            }
            QLog.d("PostFeedTask.RequestSenderTask", 1, "publishFeedRequest feedService == null");
        }
    }

    public void p(String str, String str2) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
            iPatchRedirector.redirect((short) 2, (Object) this, (Object) str, (Object) str2);
        } else {
            this.f261221h = str;
            this.f261222i = str2;
        }
    }

    public void r(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 10)) {
            this.f261218d = z15;
        } else {
            iPatchRedirector.redirect((short) 10, (Object) this, z15);
        }
    }

    public void s(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 6)) {
            this.f261220f = z15;
        } else {
            iPatchRedirector.redirect((short) 6, (Object) this, z15);
        }
    }

    public void t(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 5)) {
            this.f261219e = z15;
        } else {
            iPatchRedirector.redirect((short) 5, (Object) this, z15);
        }
    }

    public void u(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 11)) {
            this.f261224n = z15;
        } else {
            iPatchRedirector.redirect((short) 11, (Object) this, z15);
        }
    }
}