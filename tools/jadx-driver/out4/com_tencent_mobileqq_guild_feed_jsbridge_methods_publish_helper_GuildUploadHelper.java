package com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper;

import android.media.ExifInterface;
import android.text.TextUtils;
import com.tencent.biz.richframework.eventbus.SimpleBaseEvent;
import com.tencent.biz.richframework.eventbus.SimpleEventReceiver;
import com.tencent.mobileqq.activity.photo.LocalMediaInfo;
import com.tencent.mobileqq.guild.feed.api.IGuildFeedMiscApi;
import com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.s;
import com.tencent.mobileqq.guild.feed.longfeededitor.webview.result.SelectPhotoResult;
import com.tencent.mobileqq.guild.feed.longfeededitor.webview.result.ThirdContentResult;
import com.tencent.mobileqq.guild.feed.longfeededitor.webview.result.UploadProgressResult;
import com.tencent.mobileqq.guild.feed.publish.bean.GuildMediaProgressState;
import com.tencent.mobileqq.guild.feed.publish.constants.GuildFeedPublishQualityDataBuilder;
import com.tencent.mobileqq.guild.feed.publish.event.GuildFeedThirdContentMediaEvent;
import com.tencent.mobileqq.guild.feed.publish.event.GuildMediaProgressStateEvent;
import com.tencent.mobileqq.guild.feed.publish.event.GuildOnPhotoSelectedEvent;
import com.tencent.mobileqq.guild.feed.publish.event.GuildUploadMediaOperationEvent;
import com.tencent.mobileqq.guild.feed.util.GuildFeedMediaUtils;
import com.tencent.mobileqq.guild.feed.util.as;
import com.tencent.mobileqq.guild.feed.util.bv;
import com.tencent.mobileqq.guild.util.n;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.mobileqq.qroute.QRoute;
import com.tencent.qphone.base.util.QLog;
import com.tencent.richframework.thread.RFWThreadManager;
import cooperation.qzone.QZoneShareManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.GlobalScope;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes26.dex */
public class GuildUploadHelper implements SimpleEventReceiver {
    static IPatchRedirector $redirector_;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private static volatile GuildUploadHelper f254381f;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private volatile s f254382d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private String f254383e;

    GuildUploadHelper() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
            return;
        }
        iPatchRedirector.redirect((short) 1, (Object) this);
    }

    static void b(GuildUploadHelper instance, GuildOnPhotoSelectedEvent event) {
        instance.getClass();
        b bVar = b.f254388a;
        s publishConfigMethod = instance.f254382d;
        bVar.getClass();
        IPatchRedirector iPatchRedirector = b.$redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
            iPatchRedirector.redirect((short) 2, bVar, event, instance, publishConfigMethod);
            return;
        }
        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(instance, "instance");
        Intrinsics.checkNotNullParameter(publishConfigMethod, "publishConfigMethod");
        BuildersKt__Builders_commonKt.launch$default(GlobalScope.INSTANCE, null, null, new GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1(event, publishConfigMethod, instance, null), 3, null);
    }

    static void c(GuildUploadHelper guildUploadHelper, GuildFeedThirdContentMediaEvent guildFeedThirdContentMediaEvent) {
        String str;
        String str2;
        guildUploadHelper.getClass();
        String str3 = "Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod";
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(guildFeedThirdContentMediaEvent.getJsonFeed());
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(QZoneShareManager.QZONE_SHARE_IMAGES);
            JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("videos");
            QLog.i("Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod", 1, "handleOnThirdContentMediaEvent");
            for (LocalMediaInfo localMediaInfo : guildFeedThirdContentMediaEvent.getLocalMediaInfos()) {
                map2.put(localMediaInfo.path, localMediaInfo);
            }
            ArrayList arrayList = new ArrayList();
            if (jSONArrayOptJSONArray == null || jSONArrayOptJSONArray.length() <= 0) {
                str = "Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod";
            } else {
                int i15 = 0;
                while (i15 < jSONArrayOptJSONArray.length()) {
                    JSONObject jSONObject2 = jSONArrayOptJSONArray.getJSONObject(i15);
                    if (jSONObject2 != null) {
                        String string = jSONObject2.getString("picUrl");
                        str2 = str3;
                        LocalMediaInfo localMediaInfo2 = (LocalMediaInfo) map2.get(string);
                        if (localMediaInfo2 != null) {
                            String strF = guildUploadHelper.f();
                            arrayList.add(strF);
                            map.put(strF, localMediaInfo2);
                            jSONObject2.put("picId", strF);
                            jSONObject2.put("picUrl", "https://qun.qq.com/guildFeedPublish/localMedia" + string);
                        }
                    } else {
                        str2 = str3;
                    }
                    i15++;
                    str3 = str2;
                }
                str = str3;
                jSONObject.put(QZoneShareManager.QZONE_SHARE_IMAGES, jSONArrayOptJSONArray);
            }
            if (jSONArrayOptJSONArray2 != null && jSONArrayOptJSONArray2.length() > 0) {
                for (int i16 = 0; i16 < jSONArrayOptJSONArray2.length(); i16++) {
                    JSONObject jSONObject3 = jSONArrayOptJSONArray2.getJSONObject(i16);
                    if (jSONObject3 != null) {
                        String string2 = jSONObject3.getString("playUrl");
                        LocalMediaInfo localMediaInfo3 = (LocalMediaInfo) map2.get(string2);
                        if (localMediaInfo3 != null) {
                            String strF2 = guildUploadHelper.f();
                            arrayList.add(strF2);
                            map.put(strF2, localMediaInfo3);
                            jSONObject3.put("fileId", strF2);
                            jSONObject3.put("playUrl", "");
                            jSONObject3.put("videoID", string2);
                            JSONObject jSONObject4 = jSONObject3.getJSONObject("cover");
                            if (jSONObject4 != null && TextUtils.isEmpty(jSONObject4.optString("picUrl")) && !TextUtils.isEmpty(string2)) {
                                String strA = GuildFeedMediaUtils.a(string2);
                                jSONObject4.put("picUrl", "https://qun.qq.com/guildFeedPublish/localMedia" + strA);
                                GuildFeedMediaUtils.d(string2, strA, null);
                            }
                        }
                    }
                }
                jSONObject.put("videos", jSONArrayOptJSONArray2);
            }
            QLog.d(str, 1, "handleOnThirdContentMediaEvent: mainTaskId[" + guildUploadHelper.f254383e + "] info[" + map + "]", "");
            guildUploadHelper.f254382d.f(new ThirdContentResult(jSONObject, arrayList).c());
            guildUploadHelper.k(false, map);
        } catch (JSONException unused) {
        }
    }

    static void d(GuildUploadHelper guildUploadHelper, GuildMediaProgressStateEvent guildMediaProgressStateEvent) {
        String errMsg;
        int i15;
        String str;
        int i16;
        s sVar = guildUploadHelper.f254382d;
        if (sVar == null) {
            GuildMediaProgressState mediaProgressState = guildMediaProgressStateEvent.getMediaProgressState();
            StringBuilder sb5 = new StringBuilder("[handleProgressEvent]: thread problem happens, configMethod is null,progressStateEvent.taskId = ");
            sb5.append(mediaProgressState != null ? mediaProgressState.getTaskId() : null);
            QLog.e("Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod", 1, sb5.toString());
            return;
        }
        GuildMediaProgressState mediaProgressState2 = guildMediaProgressStateEvent.getMediaProgressState();
        if (mediaProgressState2.getState() == 0) {
            return;
        }
        String taskId = mediaProgressState2.getTaskId();
        int progress = mediaProgressState2.getProgress();
        if (mediaProgressState2.getState() == 3) {
            i15 = 0;
            str = "";
            i16 = 0;
        } else if (mediaProgressState2.getState() == 1) {
            i15 = 0;
            str = "";
            i16 = 1;
        } else {
            int errCode = mediaProgressState2.getErrCode();
            if (errCode == -447) {
                errMsg = "video".equals(mediaProgressState2.mType) ? "该视频超出限制" : "该图片超出限制";
            } else {
                errMsg = mediaProgressState2.getErrMsg();
            }
            i15 = errCode;
            str = errMsg;
            i16 = 2;
        }
        sVar.f(new UploadProgressResult(taskId, !TextUtils.equals(mediaProgressState2.getType(), "pic") ? 1 : 0, i16, progress, i15, str).c());
    }

    public static GuildUploadHelper h() {
        if (f254381f == null) {
            synchronized (GuildUploadHelper.class) {
                if (f254381f == null) {
                    f254381f = new GuildUploadHelper();
                }
            }
        }
        return f254381f;
    }

    public void e() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
            iPatchRedirector.redirect((short) 3, (Object) this);
        } else if (TextUtils.isEmpty(this.f254383e)) {
            this.f254383e = "GuildMain" + System.nanoTime();
        }
    }

    public String f() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 6)) {
            return (String) iPatchRedirector.redirect((short) 6, (Object) this);
        }
        return "GuildSon" + System.nanoTime();
    }

    public String g() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? this.f254383e : (String) iPatchRedirector.redirect((short) 4, (Object) this);
    }

    @Override // com.tencent.biz.richframework.eventbus.SimpleEventReceiver
    public ArrayList<Class> getEventClass() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 9)) {
            return (ArrayList) iPatchRedirector.redirect((short) 9, (Object) this);
        }
        ArrayList<Class> arrayList = new ArrayList<>();
        arrayList.add(GuildMediaProgressStateEvent.class);
        arrayList.add(GuildOnPhotoSelectedEvent.class);
        arrayList.add(GuildFeedThirdContentMediaEvent.class);
        return arrayList;
    }

    void i(List<SelectPhotoResult.SelectPhotoBean> list, LocalMediaInfo localMediaInfo, String str, String str2) {
        int i15 = 0;
        try {
            int attributeInt = new ExifInterface(str2).getAttributeInt("Orientation", 1);
            if (attributeInt == 3) {
                i15 = 180;
            } else if (attributeInt == 6) {
                i15 = 90;
            } else if (attributeInt == 8) {
                i15 = 270;
            }
        } catch (IOException e15) {
            e15.printStackTrace();
        }
        int i16 = localMediaInfo.mediaWidth;
        int i17 = localMediaInfo.mediaHeight;
        if (i15 == 90 || i15 == 270) {
            i16 = i17;
            i17 = i16;
        }
        QLog.i("Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod", 1, "handleSelectedImageFile: rotate[" + i15 + "] width[" + i16 + "] height[" + i17 + "]");
        list.add(new SelectPhotoResult.SelectPhotoBean(str, str2, i16, i17));
    }

    public void j(s sVar) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
            iPatchRedirector.redirect((short) 2, (Object) this, (Object) sVar);
        } else {
            this.f254382d = sVar;
            bv.c().d(this);
        }
    }

    void k(boolean z15, Map<String, LocalMediaInfo> map) {
        GuildUploadMediaOperationEvent guildUploadMediaOperationEvent = new GuildUploadMediaOperationEvent();
        guildUploadMediaOperationEvent.setMainTaskId(this.f254383e);
        guildUploadMediaOperationEvent.setTaskIdLocalMediaInfoMap(map);
        guildUploadMediaOperationEvent.setOriginalPic(z15);
        bv.c().b(guildUploadMediaOperationEvent, true);
        StringBuilder sb5 = new StringBuilder();
        for (Map.Entry<String, LocalMediaInfo> entry : map.entrySet()) {
            entry.getKey();
            LocalMediaInfo value = entry.getValue();
            n nVar = n.f282201a;
            if (nVar.d(value)) {
                sb5.append("p-");
            } else if (nVar.f(value)) {
                sb5.append("v-");
            }
        }
        as.a(new GuildFeedPublishQualityDataBuilder().setKeyEventId("CH_E_PICKER_EXIT_NORMAL").setTraceId(h().g()).setExt1(sb5.substring(0, sb5.length() - 1)).setExt2(String.valueOf(map.size())).setExt4(sb5.substring(0, sb5.length() - 1)));
    }

    public void l() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 7)) {
            iPatchRedirector.redirect((short) 7, (Object) this);
            return;
        }
        bv.c().e(this);
        this.f254383e = null;
        this.f254382d = null;
    }

    public void m(String str) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 5)) {
            iPatchRedirector.redirect((short) 5, (Object) this, (Object) str);
            return;
        }
        String str2 = this.f254383e;
        this.f254383e = str;
        try {
            QLog.i("Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod", 1, "[reBindReportTask]: old=" + str2 + ", new=" + str);
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
                ((IGuildFeedMiscApi) QRoute.api(IGuildFeedMiscApi.class)).syncCallGetSimpleDataMethod("sync_bind_report_task", str2, str);
            }
        } catch (Exception e15) {
            QLog.e("Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod", 1, "[tryReBindReportTask]: ", e15);
        }
    }

    @Override // com.tencent.biz.richframework.eventbus.SimpleEventReceiver
    public void onReceiveEvent(SimpleBaseEvent simpleBaseEvent) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 8)) {
            iPatchRedirector.redirect((short) 8, (Object) this, (Object) simpleBaseEvent);
        } else {
            QLog.d("Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod", 1, "onReceiveEvent-> ", simpleBaseEvent);
            RFWThreadManager.getInstance().execOnSubThread(new Runnable(simpleBaseEvent) { // from class: com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildUploadHelper.1
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ SimpleBaseEvent f254384d;

                {
                    this.f254384d = simpleBaseEvent;
                    IPatchRedirector iPatchRedirector2 = $redirector_;
                    if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector2.redirect((short) 1, (Object) this, (Object) GuildUploadHelper.this, (Object) simpleBaseEvent);
                }

                @Override // java.lang.Runnable
                public void run() {
                    IPatchRedirector iPatchRedirector2 = $redirector_;
                    if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                        iPatchRedirector2.redirect((short) 2, (Object) this);
                        return;
                    }
                    if (GuildUploadHelper.this.f254382d == null) {
                        QLog.d("Guild_Feed_GuildUploadHelper-Guild_Feed_BaseGuildJsBridgeMethod", 1, "mPublishConfigMethod is null, so return!");
                        return;
                    }
                    SimpleBaseEvent simpleBaseEvent2 = this.f254384d;
                    if (simpleBaseEvent2 instanceof GuildMediaProgressStateEvent) {
                        GuildUploadHelper.d(GuildUploadHelper.this, (GuildMediaProgressStateEvent) simpleBaseEvent2);
                    } else if (simpleBaseEvent2 instanceof GuildOnPhotoSelectedEvent) {
                        GuildUploadHelper.b(GuildUploadHelper.this, (GuildOnPhotoSelectedEvent) simpleBaseEvent2);
                    } else if (simpleBaseEvent2 instanceof GuildFeedThirdContentMediaEvent) {
                        GuildUploadHelper.c(GuildUploadHelper.this, (GuildFeedThirdContentMediaEvent) simpleBaseEvent2);
                    }
                }
            });
        }
    }
}