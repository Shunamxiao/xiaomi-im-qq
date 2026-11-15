package com.tencent.mobileqq.guild.feed.util;

import androidx.collection.LruCache;
import com.tencent.common.app.AppInterface;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.mobileqq.qfix.redirect.PatchRedirectCenter;
import com.tencent.mobileqq.qqguildsdk.api.IGPSService;
import com.tencent.mobileqq.qqguildsdk.api.IGProSession;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProCategoryChannelInfoList;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProChannel;
import com.tencent.mobileqq.qqguildsdk.data.genc.dj;
import java.util.ArrayList;
import java.util.Iterator;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import p022mqq.app.AppRuntime;
import p022mqq.app.MobileQQ;
import te2.ct;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes8.dex */
@SourceDebugExtension({"SMAP\nFetchServiceEx.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FetchServiceEx.kt\ncom/tencent/mobileqq/guild/feed/util/FetchServiceExKt\n+ 2 RuntimeServiceEx.kt\ncom/tencent/guild/aio/util/RuntimeServiceExKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 CancellableContinuation.kt\nkotlinx/coroutines/CancellableContinuationKt\n*L\n1#1,65:1\n14#2:66\n14#2:68\n14#2:69\n14#2:79\n1#3:67\n314#4,9:70\n323#4,2:80\n*S KotlinDebug\n*F\n+ 1 FetchServiceEx.kt\ncom/tencent/mobileqq/guild/feed/util/FetchServiceExKt\n*L\n17#1:66\n30#1:68\n32#1:69\n41#1:79\n40#1:70,9\n40#1:80,2\n*E\n"})
public final class r {
    static IPatchRedirector $redirector_;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @NotNull
    private static final LruCache<Long, String> f262457a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final /* synthetic */ int f262458b = 0;

    /* JADX INFO: compiled from: P */
    /* JADX INFO: loaded from: classes22.dex */
    @SourceDebugExtension({"SMAP\nFetchServiceEx.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FetchServiceEx.kt\ncom/tencent/mobileqq/guild/feed/util/FetchServiceExKt$findHiddenChannelId$2$2\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,65:1\n288#2,2:66\n*S KotlinDebug\n*F\n+ 1 FetchServiceEx.kt\ncom/tencent/mobileqq/guild/feed/util/FetchServiceExKt$findHiddenChannelId$2$2\n*L\n53#1:66,2\n*E\n"})
    static final class a implements ct {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ CancellableContinuation<String> f262459a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ long f262460b;

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
        a(CancellableContinuation<? super String> cancellableContinuation, long j15) {
            this.f262459a = cancellableContinuation;
            this.f262460b = j15;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, cancellableContinuation, Long.valueOf(j15));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // te2.ct
        public final void onGetCategoryChannelInfoListV2(ArrayList<IGProCategoryChannelInfoList> arrayList) {
            IGProChannel iGProChannel;
            String string;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) arrayList);
                return;
            }
            CancellableContinuation<String> cancellableContinuation = this.f262459a;
            if (cancellableContinuation.isActive()) {
                Intrinsics.checkNotNull(arrayList);
                Iterator<T> it = arrayList.iterator();
                do {
                    iGProChannel = null;
                    Object obj = null;
                    iGProChannel = null;
                    if (!it.hasNext()) {
                        break;
                    }
                    ArrayList<IGProChannel> channelInfoList = ((IGProCategoryChannelInfoList) it.next()).getChannelInfoList();
                    if (channelInfoList != null) {
                        Intrinsics.checkNotNull(channelInfoList);
                        for (Object obj2 : channelInfoList) {
                            IGProChannel iGProChannel2 = (IGProChannel) obj2;
                            if ((iGProChannel2.getHiddenPostChannel() == 1 && iGProChannel2.getChannelType() == 7) != false) {
                                obj = obj2;
                                break;
                            }
                        }
                        iGProChannel = (IGProChannel) obj;
                    }
                } while (iGProChannel == null);
                if (iGProChannel == null || (string = Long.valueOf(iGProChannel.getChannelId()).toString()) == null) {
                    string = "";
                }
                if (string.length() > 0) {
                    r.f262457a.put(Long.valueOf(this.f262460b), string);
                }
                cancellableContinuation.resumeWith(Result.m747constructorimpl(string));
            }
        }
    }

    static {
        IPatchRedirector redirector = PatchRedirectCenter.getRedirector(135581);
        $redirector_ = redirector;
        if (redirector == null || !redirector.hasPatch((short) 1)) {
            f262457a = new LruCache<>(20);
        } else {
            redirector.redirect((short) 1);
        }
    }

    @Nullable
    public static final Object b(long j15, @NotNull Continuation<? super String> continuation) throws Throwable {
        String str = f262457a.get(Boxing.boxLong(j15));
        if (str != null) {
            return str;
        }
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        cancellableContinuationImpl.initCancellability();
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        IGPSService iGPSService = (IGPSService) (appInterface != null ? appInterface.getRuntimeService(IGPSService.class, "") : null);
        if (iGPSService != null) {
            dj djVar = new dj();
            djVar.m(j15);
            djVar.i(true);
            djVar.k(true);
            iGPSService.getCategoryChannelInfoListV2(djVar, new a(cancellableContinuationImpl, j15));
        }
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    @Nullable
    public static final com.tencent.qqnt.kernel.api.s c() {
        IGProSession iGProSessionE = e();
        if (iGProSessionE == null) {
            return null;
        }
        if (!iGProSessionE.isInit()) {
            iGProSessionE = null;
        }
        if (iGProSessionE != null) {
            return iGProSessionE.getFeedService();
        }
        return null;
    }

    @Nullable
    public static final re2.h d() {
        IGProSession iGProSessionE = e();
        if (iGProSessionE == null) {
            return null;
        }
        if (!iGProSessionE.isInit()) {
            iGProSessionE = null;
        }
        if (iGProSessionE != null) {
            return iGProSessionE.getGuildFileTransferService();
        }
        return null;
    }

    @Nullable
    public static final IGProSession e() {
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        return (IGProSession) (appInterface != null ? appInterface.getRuntimeService(IGProSession.class, "") : null);
    }
}