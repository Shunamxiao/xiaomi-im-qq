package com.tencent.mobileqq.guild.feed.publish;

import android.text.TextUtils;
import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.core.util.Supplier;
import com.tencent.biz.richframework.eventbus.SimpleEventBus;
import com.tencent.mobileqq.R;
import com.tencent.mobileqq.app.HardCodeUtil;
import com.tencent.mobileqq.app.ThreadManagerV2;
import com.tencent.mobileqq.guild.feed.event.GuildPublishReEditEvent;
import com.tencent.mobileqq.guild.feed.nativedetail.util.t;
import com.tencent.mobileqq.guild.feed.publish.bean.GuildTaskProgressState;
import com.tencent.mobileqq.guild.feed.publish.event.PostFeedFailedEvent;
import com.tencent.mobileqq.guild.feed.publish.helper.FeedPublishTaskStateHelper;
import com.tencent.mobileqq.guild.feed.publish.helper.GuildTaskProgressBoxHelper;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.PassiveCancellationException;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.PostFeedMainTaskFacadeImpl;
import com.tencent.mobileqq.guild.feed.util.bc;
import com.tencent.mobileqq.guild.mainframe.GuildFeedReportSourceInfo;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.mobileqq.utils.NetworkUtil;
import com.tencent.qphone.base.util.QLog;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStFeed;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes26.dex */
public class h implements com.tencent.mobileqq.guild.feed.publish.a, p {
    static IPatchRedirector $redirector_;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final PostFeedMainTaskFacadeImpl f261057a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f261058b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final AtomicInteger f261059c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final String f261060d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private final com.tencent.mobileqq.guild.feed.publish.publish.k f261061e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private final LinkedList<a> f261062f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private String f261063g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    private String f261064h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    private GProStFeed f261065i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    private boolean f261066j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    @GuardedBy("this")
    private boolean f261067k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    private boolean f261068l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private boolean f261069m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    private int f261070n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    private GuildFeedReportSourceInfo f261071o;

    /* JADX INFO: renamed from: p, reason: collision with root package name */
    private final int f261072p;

    /* JADX INFO: compiled from: P */
    public static abstract class a {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        protected Supplier<String> f261073a;

        public a() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this);
        }

        public void a() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) {
                return;
            }
            iPatchRedirector.redirect((short) 2, (Object) this);
        }

        public abstract void b(int i15);
    }

    public h(@NonNull String str, String str2, String str3, int i15, boolean z15, int i16, GuildFeedReportSourceInfo guildFeedReportSourceInfo, int i17) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
            iPatchRedirector.redirect((short) 1, this, str, str2, str3, Integer.valueOf(i15), Boolean.valueOf(z15), Integer.valueOf(i16), guildFeedReportSourceInfo, Integer.valueOf(i17));
            return;
        }
        this.f261059c = new AtomicInteger(0);
        this.f261062f = new LinkedList<>();
        this.f261064h = "";
        this.f261068l = true;
        this.f261069m = true;
        this.f261060d = str;
        this.f261058b = z15;
        this.f261061e = new com.tencent.mobileqq.guild.feed.publish.publish.k(this.f261058b, str2, str3, this);
        this.f261070n = i16;
        this.f261071o = guildFeedReportSourceInfo;
        this.f261072p = i17;
        O(i15);
        this.f261057a = new PostFeedMainTaskFacadeImpl(this);
    }

    private synchronized void E(boolean z15, int i15, @NonNull String str) {
        this.f261067k = false;
        this.f261057a.i(0);
        long j15 = i15;
        int i16 = bc.f262335d;
        boolean z16 = j15 == 10027;
        if (z15 || z16) {
            String str2 = "onSuccess";
            if (z16) {
                QLog.w("PostFeedTask.MainTask", 1, "[onRequestResponse]: " + l() + " isDuplicateError");
                str2 = "onDuplicateRequest";
            }
            O(3);
            this.f261057a.J(true);
            GuildTaskProgressBoxHelper.g().o(str2, this.f261060d, this.f261059c.get(), 0, "");
            return;
        }
        O(5);
        this.f261057a.J(false);
        if (t.n((int) j15) || j15 == -2 || j15 == -1) {
            str = HardCodeUtil.qqStr(R.string.f182467z94);
        } else if (j15 == 1006002) {
            str = HardCodeUtil.qqStr(R.string.z_7);
        } else if (j15 == 20066) {
            str = HardCodeUtil.qqStr(R.string.z9u);
        }
        GuildTaskProgressBoxHelper.g().o("onPublishFailed", this.f261060d, this.f261059c.get(), Integer.valueOf(i15), str);
        T(i15, str);
    }

    private void T(int i15, String str) {
        String strQqStr;
        PostFeedFailedEvent postFeedFailedEvent = new PostFeedFailedEvent(this.f261060d, this.f261058b, i15, str);
        SimpleEventBus.getInstance().dispatchEvent(postFeedFailedEvent);
        if (!this.f261069m || postFeedFailedEvent.isFailToastInterceptedByExternal()) {
            QLog.d("PostFeedTask.MainTask", 1, "[showToastOnRequestResponseIfNeeded]: needShowFailToast= " + this.f261069m + " intercept source = " + postFeedFailedEvent.getInterceptFailToastFromSource());
            return;
        }
        if (TextUtils.isEmpty(str)) {
            if (i15 != 20015) {
                strQqStr = i15 != 20016 ? HardCodeUtil.qqStr(R.string.z_z) : HardCodeUtil.qqStr(R.string.z_c);
            } else {
                strQqStr = HardCodeUtil.qqStr(R.string.z_b);
            }
            str = strQqStr;
        }
        t.t(false, str);
    }

    private void V() {
        O(5);
        GuildTaskProgressBoxHelper.g().o("networkUnavailable", this.f261060d, this.f261059c.get(), null, null);
        SimpleEventBus.getInstance().dispatchEvent(new PostFeedFailedEvent(this.f261060d, this.f261058b, -1, "网络异常"));
        if (this.f261058b) {
            SimpleEventBus.getInstance().dispatchEvent(new GuildPublishReEditEvent(-1L, "网络异常", ""), true);
        }
        if (d()) {
            ThreadManagerV2.getUIHandlerV2().postDelayed(new e(), 200L);
        }
        com.tencent.mobileqq.guild.feed.nativepublish.report.a.f259242a.g(this.f261060d, 400003, "", 0L);
    }

    public static void f(final h hVar, boolean z15) {
        boolean z16;
        hVar.getClass();
        FeedPublishTaskStateHelper.d(hVar, z15);
        if (NetworkUtil.isNetworkAvailable()) {
            z16 = false;
        } else {
            QLog.e("PostFeedTask.MainTask", 1, "[publishFeed]: " + hVar.l() + " network unavailable");
            hVar.V();
            z16 = true;
        }
        PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl = hVar.f261057a;
        if (z16) {
            postFeedMainTaskFacadeImpl.B();
            return;
        }
        hVar.O(1);
        if (!postFeedMainTaskFacadeImpl.A()) {
            postFeedMainTaskFacadeImpl.B();
            postFeedMainTaskFacadeImpl.F(new Consumer() { // from class: com.tencent.mobileqq.guild.feed.publish.d
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    h.i(this.f261054d, obj);
                }
            });
            return;
        }
        QLog.w("PostFeedTask.MainTask", 1, "[publishFeed]: " + hVar.l() + ", isActive=" + hVar.f261067k + ", waiting upload task result now!");
    }

    public static void g(h hVar, com.tencent.mobileqq.guild.feed.publish.api.d.a aVar, Boolean bool) {
        hVar.getClass();
        if (bool.booleanValue()) {
            hVar.f261067k = false;
            hVar.f261057a.i(0);
            hVar.O(4);
            GuildTaskProgressBoxHelper.g().o("onUploadFailed", hVar.f261060d, hVar.f261059c.get(), -1, "");
            return;
        }
        synchronized (hVar) {
            hVar.O(2);
            boolean zIsNetworkAvailable = NetworkUtil.isNetworkAvailable();
            QLog.i("PostFeedTask.MainTask", 1, "[triggerRealPublishRequest]: " + hVar.l() + ", networkAvailable = " + zIsNetworkAvailable + ", mediaUploadResult = " + aVar.c());
            com.tencent.mobileqq.guild.feed.nativepublish.report.a.f259242a.i(hVar.f261060d, hVar.f261061e.getGuildId(), hVar.f261061e.getChannelId());
            if (zIsNetworkAvailable) {
                GuildTaskProgressBoxHelper.g().o("onPublishFeed", hVar.f261060d, hVar.f261059c.get(), null, null);
                hVar.f261061e.o(hVar.f261063g, aVar, hVar.f261064h, hVar.f261070n, hVar.f261071o);
            } else {
                hVar.V();
            }
        }
    }

    public static void i(final h hVar, Object obj) {
        hVar.getClass();
        if (obj instanceof com.tencent.mobileqq.guild.feed.publish.api.d.a) {
            final com.tencent.mobileqq.guild.feed.publish.api.d.a aVar = (com.tencent.mobileqq.guild.feed.publish.api.d.a) obj;
            hVar.f261057a.D(aVar, new Consumer() { // from class: com.tencent.mobileqq.guild.feed.publish.f
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj2) {
                    h.g(this.f261055d, aVar, (Boolean) obj2);
                }
            });
        }
    }

    private void j(int i15, @NonNull String str, @NonNull String str2) {
        if (this.f261058b) {
            if (TextUtils.isEmpty(str)) {
                long j15 = i15;
                if (j15 == 10023) {
                    str = "编辑失败，当前暂无权限。";
                } else if (j15 == 10027) {
                    str = "重复发表";
                } else if (j15 == 10015) {
                    str = "发帖频率过快，请稍后重试！";
                } else if (j15 == 20015) {
                    str = HardCodeUtil.qqStr(R.string.z_b);
                } else if (j15 == 20016) {
                    str = HardCodeUtil.qqStr(R.string.z_c);
                }
            }
            SimpleEventBus.getInstance().dispatchEvent(new GuildPublishReEditEvent(i15, str, str2), true);
        }
    }

    public synchronized void A() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 19)) {
            iPatchRedirector.redirect((short) 19, (Object) this);
            return;
        }
        this.f261067k = false;
        QLog.i("PostFeedTask.MainTask", 1, "[onAsyncPreCheckSuccess]: " + l());
    }

    public void B() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 27)) {
            iPatchRedirector.redirect((short) 27, (Object) this);
            return;
        }
        this.f261057a.J(this.f261059c.get() == 3);
        com.tencent.mobileqq.guild.feed.publish.publish.k kVar = this.f261061e;
        if (kVar != null) {
            kVar.m();
        }
        Iterator<a> it = this.f261062f.iterator();
        while (it.hasNext()) {
            it.next().a();
        }
    }

    public void C(@Nullable CancellationException cancellationException) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 28)) {
            iPatchRedirector.redirect((short) 28, (Object) this, (Object) cancellationException);
            return;
        }
        if (!(cancellationException instanceof PassiveCancellationException)) {
            this.f261061e.u(true);
        }
        this.f261057a.I(cancellationException);
    }

    @Deprecated
    public void D(String str) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 31)) {
            return;
        }
        iPatchRedirector.redirect((short) 31, (Object) this, (Object) str);
    }

    public synchronized void F() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 17)) {
            this.f261067k = true;
        } else {
            iPatchRedirector.redirect((short) 17, (Object) this);
        }
    }

    @Deprecated
    public void G(String str) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 30)) {
            return;
        }
        iPatchRedirector.redirect((short) 30, (Object) this, (Object) str);
    }

    public synchronized void H(com.tencent.mobileqq.guild.feed.publish.task.a aVar) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 20)) {
            iPatchRedirector.redirect((short) 20, (Object) this, (Object) aVar);
            return;
        }
        final boolean zA = aVar.a();
        String str = this.f261063g;
        if (this.f261067k) {
            QLog.e("PostFeedTask.MainTask", 1, "[publishFeed]: is active now, " + l() + ", isNewFeed =" + zA);
            return;
        }
        StringBuilder sb5 = new StringBuilder("[publishFeed]: ");
        sb5.append(l());
        sb5.append(", json.length =");
        sb5.append(str != null ? str.length() : 0);
        sb5.append(", isNewFeed = ");
        sb5.append(zA);
        QLog.i("PostFeedTask.MainTask", 1, sb5.toString());
        this.f261066j = true;
        this.f261067k = true;
        this.f261061e.u(false);
        this.f261057a.i(2);
        O(6);
        this.f261057a.C(aVar, this.f261063g, new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.b
            @Override // java.lang.Runnable
            public final void run() {
                h.f(this.f261050d, zA);
            }
        });
    }

    public void I(String str, String str2) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) {
            this.f261061e.p(str, str2);
        } else {
            iPatchRedirector.redirect((short) 2, (Object) this, (Object) str, (Object) str2);
        }
    }

    public void J(GuildTaskProgressState guildTaskProgressState) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 33)) {
            iPatchRedirector.redirect((short) 33, (Object) this, (Object) guildTaskProgressState);
        } else {
            if (guildTaskProgressState == null) {
                return;
            }
            this.f261057a.E(guildTaskProgressState);
        }
    }

    public void K(@Nullable GProStFeed gProStFeed) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 36)) {
            this.f261065i = gProStFeed;
        } else {
            iPatchRedirector.redirect((short) 36, (Object) this, (Object) gProStFeed);
        }
    }

    public void L(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 39)) {
            iPatchRedirector.redirect((short) 39, (Object) this, z15);
            return;
        }
        this.f261058b = z15;
        com.tencent.mobileqq.guild.feed.publish.publish.k kVar = this.f261061e;
        if (kVar != null) {
            kVar.r(z15);
        }
    }

    public void M(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 6)) {
            this.f261061e.s(z15);
        } else {
            iPatchRedirector.redirect((short) 6, (Object) this, z15);
        }
    }

    public void N(String str) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 34)) {
            this.f261063g = str;
        } else {
            iPatchRedirector.redirect((short) 34, (Object) this, (Object) str);
        }
    }

    public synchronized void O(int i15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 24)) {
            iPatchRedirector.redirect((short) 24, (Object) this, i15);
            return;
        }
        this.f261059c.set(i15);
        if (i15 == 5 || i15 == 4) {
            this.f261067k = false;
        }
        Iterator<a> it = this.f261062f.iterator();
        while (it.hasNext()) {
            it.next().b(i15);
        }
    }

    public void P(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) {
            this.f261061e.t(z15);
        } else {
            iPatchRedirector.redirect((short) 3, (Object) this, z15);
        }
    }

    public void Q(String str) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 13)) {
            this.f261064h = str;
        } else {
            iPatchRedirector.redirect((short) 13, (Object) this, (Object) str);
        }
    }

    public void R(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 43)) {
            this.f261069m = z15;
        } else {
            iPatchRedirector.redirect((short) 43, (Object) this, z15);
        }
    }

    public void S(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 41)) {
            this.f261068l = z15;
        } else {
            iPatchRedirector.redirect((short) 41, (Object) this, z15);
        }
    }

    public void U(int i15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 21)) {
            iPatchRedirector.redirect((short) 21, (Object) this, i15);
            return;
        }
        QLog.i("PostFeedTask.MainTask", 1, "[triggerUploadAllTask]: " + l() + " newState = " + i15);
        O(i15);
        GuildTaskProgressBoxHelper.g().o("triggerUploadAllTask", this.f261060d, this.f261059c.get(), null, null);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.a
    public void a(boolean z15, int i15, @NonNull String str, @NonNull String str2) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 22)) {
            iPatchRedirector.redirect((short) 22, this, Boolean.valueOf(z15), Integer.valueOf(i15), str, str2);
            return;
        }
        try {
            E(z15, i15, str);
        } finally {
            j(i15, str, str2);
            com.tencent.mobileqq.guild.feed.nativepublish.report.a.f259242a.g(this.f261060d, z15 ? 0 : 400002, str, i15);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.p
    @NonNull
    public com.tencent.mobileqq.guild.feed.publish.api.d b() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? this.f261057a : (com.tencent.mobileqq.guild.feed.publish.api.d) iPatchRedirector.redirect((short) 4, (Object) this);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.a
    @NonNull
    public String c() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 26)) ? this.f261060d : (String) iPatchRedirector.redirect((short) 26, (Object) this);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.a
    public boolean d() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 44)) ? this.f261069m : ((Boolean) iPatchRedirector.redirect((short) 44, (Object) this)).booleanValue();
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.a
    public boolean e() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 42)) ? this.f261068l : ((Boolean) iPatchRedirector.redirect((short) 42, (Object) this)).booleanValue();
    }

    public int k() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 10)) ? this.f261070n : ((Integer) iPatchRedirector.redirect((short) 10, (Object) this)).intValue();
    }

    public String l() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 40)) {
            return (String) iPatchRedirector.redirect((short) 40, (Object) this);
        }
        return "mainTaskId = " + this.f261060d + ", state = " + this.f261059c + " mIsActive=" + this.f261067k + ", isTriggerPublish=" + this.f261066j + ", isEdit=" + this.f261058b;
    }

    @NonNull
    public com.tencent.mobileqq.guild.data.m m() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 8)) ? this.f261061e : (com.tencent.mobileqq.guild.data.m) iPatchRedirector.redirect((short) 8, (Object) this);
    }

    @Nullable
    public GProStFeed n() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 35)) ? this.f261065i : (GProStFeed) iPatchRedirector.redirect((short) 35, (Object) this);
    }

    public boolean o() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 7)) ? this.f261061e.k() : ((Boolean) iPatchRedirector.redirect((short) 7, (Object) this)).booleanValue();
    }

    public String p() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 32)) ? this.f261063g : (String) iPatchRedirector.redirect((short) 32, (Object) this);
    }

    public int q() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 25)) ? this.f261059c.get() : ((Integer) iPatchRedirector.redirect((short) 25, (Object) this)).intValue();
    }

    @Nullable
    public GuildFeedReportSourceInfo r() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 11)) ? this.f261071o : (GuildFeedReportSourceInfo) iPatchRedirector.redirect((short) 11, (Object) this);
    }

    public int s() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 12)) ? this.f261072p : ((Integer) iPatchRedirector.redirect((short) 12, (Object) this)).intValue();
    }

    @NonNull
    public com.tencent.mobileqq.guild.feed.publish.api.e t() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 5)) ? this.f261057a : (com.tencent.mobileqq.guild.feed.publish.api.e) iPatchRedirector.redirect((short) 5, (Object) this);
    }

    public String u(String str) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 37)) {
            return (String) iPatchRedirector.redirect((short) 37, (Object) this, (Object) str);
        }
        com.tencent.mobileqq.guild.data.media.d dVar = this.f261057a.f().d().get(str);
        if (dVar != null) {
            return dVar.isVideoTask() ? "video" : "pic";
        }
        return "";
    }

    public synchronized void v(@NonNull a aVar) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 38)) {
            iPatchRedirector.redirect((short) 38, (Object) this, (Object) aVar);
        } else {
            aVar.f261073a = new c(this);
            this.f261062f.add(aVar);
        }
    }

    public boolean w() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 15)) ? this.f261067k : ((Boolean) iPatchRedirector.redirect((short) 15, (Object) this)).booleanValue();
    }

    public boolean x() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 9)) ? this.f261058b : ((Boolean) iPatchRedirector.redirect((short) 9, (Object) this)).booleanValue();
    }

    public boolean y() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 23)) ? this.f261066j : ((Boolean) iPatchRedirector.redirect((short) 23, (Object) this)).booleanValue();
    }

    public synchronized void z(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 18)) {
            iPatchRedirector.redirect((short) 18, (Object) this, z15);
            return;
        }
        QLog.e("PostFeedTask.MainTask", 1, "[onAsyncPreCheckFailed]: " + l());
        this.f261066j = true;
        this.f261067k = true;
        this.f261061e.u(false);
        this.f261057a.i(2);
        O(6);
        FeedPublishTaskStateHelper.d(this, z15);
        this.f261067k = false;
        this.f261057a.i(0);
        O(5);
        GuildTaskProgressBoxHelper.g().p(this.f261060d, this.f261059c.get());
    }
}