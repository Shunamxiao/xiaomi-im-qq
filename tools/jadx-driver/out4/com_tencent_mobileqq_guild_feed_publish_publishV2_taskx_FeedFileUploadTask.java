package com.tencent.mobileqq.guild.feed.publish.publishV2.taskx;

import com.tencent.common.app.AppInterface;
import com.tencent.mobileqq.guild.feed.api.IGuildFeedFileUploadServiceApi;
import com.tencent.mobileqq.guild.feed.util.ai;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.mobileqq.qfix.redirect.PatchRedirectCenter;
import com.tencent.mobileqq.qroute.QRoute;
import com.tencent.mobileqq.unitedconfig_android.api.IUnitedConfigManager;
import com.tencent.qphone.base.util.QLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CompletableDeferredKt;
import kotlinx.coroutines.CompletableJob;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelKt;
import kotlinx.coroutines.channels.ChannelResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import p022mqq.app.AppRuntime;
import p022mqq.app.MobileQQ;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes26.dex */
@SourceDebugExtension({"SMAP\nFeedFileUploadTask.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedFileUploadTask.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/taskx/FeedFileUploadTask\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n+ 6 RuntimeServiceEx.kt\ncom/tencent/guild/aio/util/RuntimeServiceExKt\n+ 7 MiscEx.kt\ncom/tencent/mobileqq/guild/feed/util/MiscExKt\n+ 8 Channel.kt\nkotlinx/coroutines/channels/ChannelKt\n*L\n1#1,1236:1\n996#1,8:1244\n1#2:1237\n453#3:1238\n403#3:1239\n453#3:1263\n403#3:1264\n453#3:1269\n403#3:1270\n453#3:1275\n403#3:1276\n526#3:1296\n511#3,6:1297\n1238#4,4:1240\n1238#4,4:1265\n1238#4,4:1271\n1238#4,4:1277\n1855#4,2:1284\n1179#4,2:1290\n1253#4,4:1292\n1855#4,2:1305\n167#5,3:1252\n204#5,4:1255\n204#5,4:1259\n215#5,2:1282\n215#5,2:1307\n14#6:1281\n14#6:1286\n14#6:1289\n14#6:1303\n14#6:1304\n14#6:1315\n23#7,2:1287\n523#8,6:1309\n*S KotlinDebug\n*F\n+ 1 FeedFileUploadTask.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/taskx/FeedFileUploadTask\n*L\n413#1:1244,8\n123#1:1238\n123#1:1239\n475#1:1263\n475#1:1264\n521#1:1269\n521#1:1270\n539#1:1275\n539#1:1276\n762#1:1296\n762#1:1297,6\n123#1:1240,4\n475#1:1265,4\n521#1:1271,4\n539#1:1277,4\n614#1:1284,2\n761#1:1290,2\n761#1:1292,4\n1087#1:1305,2\n419#1:1252,3\n466#1:1255,4\n467#1:1259,4\n598#1:1282,2\n1093#1:1307,2\n597#1:1281\n680#1:1286\n722#1:1289\n940#1:1303\n1085#1:1304\n1184#1:1315\n707#1:1287,2\n1151#1:1309,6\n*E\n"})
public final class FeedFileUploadTask implements com.tencent.mobileqq.guild.feed.publish.api.a {
    static IPatchRedirector $redirector_;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @NotNull
    private final ParentTaskContext f261316a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @NotNull
    private final k f261317b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    @NotNull
    private final String f261318c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @NotNull
    private final AtomicInteger f261319d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @NotNull
    private final ConcurrentMap<String, l> f261320e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @NotNull
    private final m f261321f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private boolean f261322g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    @NotNull
    private final Lazy f261323h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    @NotNull
    private final CoroutineDispatcher f261324i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    @NotNull
    private final Lazy f261325j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    @NotNull
    private final Channel<Function1<Continuation<? super Unit>, Object>> f261326k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    @Nullable
    private volatile Deferred<com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b> f261327l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    @Nullable
    private volatile CancellableContinuation<? super com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b> f261328m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    @NotNull
    private final Lazy f261329n;

    /* JADX INFO: compiled from: P */
    public static final class a implements com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.strategy.d.a {
        static IPatchRedirector $redirector_;

        a() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) FeedFileUploadTask.this);
        }

        @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.strategy.d.a
        public boolean a() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? FeedFileUploadTask.this.a() : ((Boolean) iPatchRedirector.redirect((short) 3, (Object) this)).booleanValue();
        }

        @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.strategy.d.a
        public void b() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) {
                FeedFileUploadTask.B(FeedFileUploadTask.this);
            } else {
                iPatchRedirector.redirect((short) 4, (Object) this);
            }
        }

        @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.strategy.d.a
        @NotNull
        public String c() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) ? FeedFileUploadTask.this.P() : (String) iPatchRedirector.redirect((short) 2, (Object) this);
        }

        @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.strategy.d.a
        public void d() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 5)) {
                iPatchRedirector.redirect((short) 5, (Object) this);
            } else {
                FeedFileUploadTask feedFileUploadTask = FeedFileUploadTask.this;
                FeedFileUploadTask.J(feedFileUploadTask, feedFileUploadTask.f261319d.incrementAndGet(), false);
            }
        }
    }

    /* JADX INFO: compiled from: P */
    /* JADX INFO: loaded from: classes13.dex */
    public static final class b {
        static IPatchRedirector $redirector_;

        public b(DefaultConstructorMarker defaultConstructorMarker) {
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

    static {
        IPatchRedirector redirector = PatchRedirectCenter.getRedirector(133319);
        $redirector_ = redirector;
        if (redirector == null || !redirector.hasPatch((short) 17)) {
            new b(null);
        } else {
            redirector.redirect((short) 17);
        }
    }

    public FeedFileUploadTask(@NotNull String moduleLogTag, @NotNull ParentTaskContext parentTaskContext, @NotNull k config) {
        com.tencent.mobileqq.guild.feed.api.j jVarInvoke;
        Intrinsics.checkNotNullParameter(moduleLogTag, "moduleLogTag");
        Intrinsics.checkNotNullParameter(parentTaskContext, "parentTaskContext");
        Intrinsics.checkNotNullParameter(config, "config");
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
            iPatchRedirector.redirect((short) 1, this, moduleLogTag, parentTaskContext, config);
            return;
        }
        this.f261316a = parentTaskContext;
        this.f261317b = config;
        String str = moduleLogTag + ".FileUploadTask";
        this.f261318c = str;
        this.f261319d = new AtomicInteger(0);
        this.f261320e = new ConcurrentHashMap();
        m mVar = new m(str, P());
        Function1<ParentTaskContext, com.tencent.mobileqq.guild.feed.api.j> function1C = getConfig().c();
        if (function1C != null && (jVarInvoke = function1C.invoke(parentTaskContext)) != null) {
            mVar.i(jVarInvoke);
        }
        this.f261321f = mVar;
        this.f261322g = true;
        this.f261323h = LazyKt.lazy(new Function0<Boolean>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$isSerialQueueOn$2
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
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return (Boolean) iPatchRedirector2.redirect((short) 2, (Object) this);
                }
                boolean zIsSwitchOn = ((IUnitedConfigManager) QRoute.api(IUnitedConfigManager.class)).isSwitchOn("switch_to_serial_queue", true);
                QLog.d(this.this$0.f261318c, 1, "[isSerialQueueOn]: " + this.this$0.P() + ", " + zIsSwitchOn);
                return Boolean.valueOf(zIsSwitchOn);
            }
        });
        this.f261324i = ai.b(ai.a().d());
        this.f261325j = LazyKt.lazy(new Function0<CoroutineScope>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$scope$2
            static IPatchRedirector $redirector_;

            {
                super(0);
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.this$0);
            }

            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final CoroutineScope invoke() {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return (CoroutineScope) iPatchRedirector2.redirect((short) 2, (Object) this);
                }
                CoroutineDispatcher coroutineDispatcher = this.this$0.f261324i;
                final CompletableJob completableJobSupervisorJob$default = SupervisorKt.SupervisorJob$default((Job) null, 1, (Object) null);
                final FeedFileUploadTask feedFileUploadTask = this.this$0;
                completableJobSupervisorJob$default.invokeOnCompletion(new Function1<Throwable, Unit>(feedFileUploadTask) { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$scope$2$1$1
                    static IPatchRedirector $redirector_;
                    final /* synthetic */ FeedFileUploadTask this$0;

                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                        this.this$0 = feedFileUploadTask;
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 == null || !iPatchRedirector3.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector3.redirect((short) 1, (Object) this, (Object) this.$this_apply, (Object) feedFileUploadTask);
                    }

                    @Override // kotlin.jvm.functions.Function1
                    public /* bridge */ /* synthetic */ Unit invoke(Throwable th5) {
                        invoke2(th5);
                        return Unit.INSTANCE;
                    }

                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2(@Nullable Throwable th5) {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 2)) {
                            iPatchRedirector3.redirect((short) 2, (Object) this, (Object) th5);
                        } else if (this.$this_apply.isCancelled()) {
                            FeedFileUploadTask.A(this.this$0, th5);
                        }
                    }
                });
                CoroutineScope CoroutineScope = CoroutineScopeKt.CoroutineScope(coroutineDispatcher.plus(completableJobSupervisorJob$default));
                FeedFileUploadTask feedFileUploadTask2 = this.this$0;
                if (FeedFileUploadTask.z(feedFileUploadTask2)) {
                    BuildersKt__Builders_commonKt.launch$default(CoroutineScope, null, null, new FeedFileUploadTask$startOperationConsumer$1(feedFileUploadTask2, null), 3, null);
                }
                return CoroutineScope;
            }
        });
        this.f261326k = ChannelKt.Channel$default(Integer.MAX_VALUE, null, new Function1<Function1<? super Continuation<? super Unit>, ? extends Object>, Unit>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$operationChannel$1
            static IPatchRedirector $redirector_;

            {
                super(1);
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.this$0);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Function1<? super Continuation<? super Unit>, ? extends Object> function1) {
                invoke2(function1);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(@NotNull Function1<? super Continuation<? super Unit>, ? extends Object> it) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    iPatchRedirector2.redirect((short) 2, (Object) this, (Object) it);
                    return;
                }
                Intrinsics.checkNotNullParameter(it, "it");
                QLog.w(this.this$0.f261318c, 1, "[operationChannel]: " + this.this$0.P() + ", op dropped on undelivered");
            }
        }, 2, null);
        getConfig().b().g(new a());
        getConfig().b().i(str);
        this.f261329n = LazyKt.lazy(new Function0<FeedFileUploadTask$serviceListener$2.AnonymousClass1>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$serviceListener$2
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
            /* JADX WARN: Type inference failed for: r0v1, types: [com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$serviceListener$2$1] */
            @Override // kotlin.jvm.functions.Function0
            @NotNull
            public final AnonymousClass1 invoke() {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                return (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 2)) ? new com.tencent.mobileqq.guild.feed.api.j() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$serviceListener$2.1
                    static IPatchRedirector $redirector_;

                    /* JADX INFO: renamed from: a, reason: collision with root package name */
                    private final /* synthetic */ m f261331a;

                    /* JADX INFO: renamed from: b, reason: collision with root package name */
                    @NotNull
                    private final Channel<com.tencent.mobileqq.guild.data.media.d> f261332b;

                    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$serviceListener$2$1$1, reason: invalid class name and collision with other inner class name */
                    /* JADX INFO: compiled from: P */
                    @Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "<anonymous>"}, k = 3, mv = {1, 9, 0})
                    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$serviceListener$2$1$1", f = "FeedFileUploadTask.kt", i = {0}, l = {1244}, m = "invokeSuspend", n = {"$this$consume$iv$iv"}, s = {"L$1"})
                    @SourceDebugExtension({"SMAP\nFeedFileUploadTask.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedFileUploadTask.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/taskx/FeedFileUploadTask$serviceListener$2$1$1\n+ 2 Channels.common.kt\nkotlinx/coroutines/channels/ChannelsKt__Channels_commonKt\n*L\n1#1,1236:1\n103#2:1237\n80#2,6:1238\n104#2,2:1244\n90#2:1246\n86#2,4:1247\n*S KotlinDebug\n*F\n+ 1 FeedFileUploadTask.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/taskx/FeedFileUploadTask$serviceListener$2$1$1\n*L\n1033#1:1237\n1033#1:1238,6\n1033#1:1244,2\n1033#1:1246\n1033#1:1247,4\n*E\n"})
                    static final class C47731 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
                        static IPatchRedirector $redirector_;
                        Object L$0;
                        Object L$1;
                        Object L$2;
                        int label;
                        final /* synthetic */ FeedFileUploadTask this$1;

                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        C47731(FeedFileUploadTask feedFileUploadTask, Continuation<? super C47731> continuation) {
                            super(2, continuation);
                            this.this$1 = feedFileUploadTask;
                            IPatchRedirector iPatchRedirector = $redirector_;
                            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                                return;
                            }
                            iPatchRedirector.redirect((short) 1, this, AnonymousClass1.this, feedFileUploadTask, continuation);
                        }

                        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                        @NotNull
                        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
                            IPatchRedirector iPatchRedirector = $redirector_;
                            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? AnonymousClass1.this.new C47731(this.this$1, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
                        }

                        /* JADX WARN: Code duplicated, block: B:20:0x0058 A[RETURN] */
                        /* JADX WARN: Code duplicated, block: B:21:0x0059  */
                        /* JADX WARN: Code duplicated, block: B:24:0x0068 A[Catch: all -> 0x00ec, TryCatch #1 {all -> 0x00ec, blocks: (B:22:0x0060, B:24:0x0068, B:26:0x0084, B:27:0x00cb, B:29:0x00d4, B:30:0x00d7), top: B:43:0x0060 }] */
                        /* JADX WARN: Code duplicated, block: B:26:0x0084 A[Catch: all -> 0x00ec, TryCatch #1 {all -> 0x00ec, blocks: (B:22:0x0060, B:24:0x0068, B:26:0x0084, B:27:0x00cb, B:29:0x00d4, B:30:0x00d7), top: B:43:0x0060 }] */
                        /* JADX WARN: Code duplicated, block: B:27:0x00cb A[Catch: all -> 0x00ec, TryCatch #1 {all -> 0x00ec, blocks: (B:22:0x0060, B:24:0x0068, B:26:0x0084, B:27:0x00cb, B:29:0x00d4, B:30:0x00d7), top: B:43:0x0060 }] */
                        /* JADX WARN: Code duplicated, block: B:29:0x00d4 A[Catch: all -> 0x00ec, TryCatch #1 {all -> 0x00ec, blocks: (B:22:0x0060, B:24:0x0068, B:26:0x0084, B:27:0x00cb, B:29:0x00d4, B:30:0x00d7), top: B:43:0x0060 }] */
                        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x0059 -> B:43:0x0060). Please report as a decompilation issue!!! */
                        /*  JADX ERROR: StackOverflowError in pass: RegionMakerVisitor
                            java.lang.StackOverflowError
                            	at jadx.core.dex.attributes.AttributeStorage.contains(AttributeStorage.java:90)
                            	at jadx.core.dex.attributes.AttrNode.contains(AttrNode.java:98)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:60)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:117)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:109)
                            	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:69)
                            	at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:111)
                            */
                        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                        @org.jetbrains.annotations.Nullable
                        public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r14) {
                            /*
                                Method dump skipped, instruction units count: 246
                                To view this dump change 'Code comments level' option to 'DEBUG'
                            */
                            throw new UnsupportedOperationException("Method not decompiled: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$serviceListener$2.AnonymousClass1.C47731.invokeSuspend(java.lang.Object):java.lang.Object");
                        }

                        @Override // kotlin.jvm.functions.Function2
                        @Nullable
                        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
                        public final Object mo4invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Unit> continuation) {
                            IPatchRedirector iPatchRedirector = $redirector_;
                            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((C47731) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
                        }
                    }

                    {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 1)) {
                            iPatchRedirector3.redirect((short) 1, (Object) this, (Object) this.f261333c);
                            return;
                        }
                        this.f261331a = this.f261333c.f261321f;
                        this.f261332b = ChannelKt.Channel$default(Integer.MAX_VALUE, null, new Function1<com.tencent.mobileqq.guild.data.media.d, Unit>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask$serviceListener$2$1$taskInfoChangeChannel$1
                            static IPatchRedirector $redirector_;

                            {
                                super(1);
                                IPatchRedirector iPatchRedirector4 = $redirector_;
                                if (iPatchRedirector4 == null || !iPatchRedirector4.hasPatch((short) 1)) {
                                    return;
                                }
                                iPatchRedirector4.redirect((short) 1, (Object) this, (Object) this.this$0);
                            }

                            @Override // kotlin.jvm.functions.Function1
                            public /* bridge */ /* synthetic */ Unit invoke(com.tencent.mobileqq.guild.data.media.d dVar) {
                                invoke2(dVar);
                                return Unit.INSTANCE;
                            }

                            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                            public final void invoke2(@NotNull com.tencent.mobileqq.guild.data.media.d it) {
                                IPatchRedirector iPatchRedirector4 = $redirector_;
                                if (iPatchRedirector4 != null && iPatchRedirector4.hasPatch((short) 2)) {
                                    iPatchRedirector4.redirect((short) 2, (Object) this, (Object) it);
                                    return;
                                }
                                Intrinsics.checkNotNullParameter(it, "it");
                                QLog.w(this.this$0.f261318c, 1, "[onUndeliveredElement]: " + this.this$0.P() + ", info=" + com.tencent.mobileqq.guild.data.media.e.a(it));
                            }
                        }, 2, null);
                        BuildersKt__Builders_commonKt.launch$default(this.f261333c.Q(), null, null, new C47731(this.f261333c, null), 3, null);
                    }

                    /* JADX WARN: Code duplicated, block: B:27:0x005b  */
                    @Override // com.tencent.mobileqq.guild.feed.api.m
                    public void a(@NotNull com.tencent.mobileqq.guild.data.media.d info) {
                        boolean z15;
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 9)) {
                            iPatchRedirector3.redirect((short) 9, (Object) this, (Object) info);
                            return;
                        }
                        Intrinsics.checkNotNullParameter(info, "info");
                        Intrinsics.checkNotNullParameter(info, "<this>");
                        if ((!info.isActive() && !info.isCompleted()) || info.isCompleted() || info.isCancelled()) {
                            z15 = true;
                        } else {
                            z15 = false;
                            if (info.isActive()) {
                                long jB = com.tencent.mobileqq.guild.data.media.e.b(info);
                                if ((jB == 0 && info.k() == 0) || jB == 50 || jB == 100) {
                                    z15 = true;
                                }
                            }
                        }
                        FeedFileUploadTask feedFileUploadTask = this.f261333c;
                        if (z15) {
                            QLog.d(feedFileUploadTask.f261318c, 1, "[onTaskChange]: parentId=" + feedFileUploadTask.P() + ", info=" + com.tencent.mobileqq.guild.data.media.e.a(info));
                        }
                        Object objMo2268trySendJP2dKIU = this.f261332b.mo2268trySendJP2dKIU(info);
                        if (objMo2268trySendJP2dKIU instanceof ChannelResult.Failed) {
                            Throwable thM2277exceptionOrNullimpl = ChannelResult.m2277exceptionOrNullimpl(objMo2268trySendJP2dKIU);
                            QLog.w(feedFileUploadTask.f261318c, 4, "[onTaskChange]: parentId=" + feedFileUploadTask.P() + ", trySend failure, info.id=" + info.getFileId() + " throwable=" + thM2277exceptionOrNullimpl);
                        }
                    }

                    @Override // com.tencent.mobileqq.guild.feed.api.j
                    public void b(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 4)) {
                            iPatchRedirector3.redirect((short) 4, (Object) this, (Object) fileId, (Object) info);
                            return;
                        }
                        Intrinsics.checkNotNullParameter(fileId, "fileId");
                        Intrinsics.checkNotNullParameter(info, "info");
                        this.f261331a.b(fileId, info);
                    }

                    @Override // com.tencent.mobileqq.guild.feed.api.j
                    public void c(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 7)) {
                            iPatchRedirector3.redirect((short) 7, (Object) this, (Object) fileId, (Object) info);
                            return;
                        }
                        Intrinsics.checkNotNullParameter(fileId, "fileId");
                        Intrinsics.checkNotNullParameter(info, "info");
                        this.f261331a.c(fileId, info);
                    }

                    @Override // com.tencent.mobileqq.guild.feed.api.j
                    public void d(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 8)) {
                            iPatchRedirector3.redirect((short) 8, (Object) this, (Object) fileId, (Object) info);
                            return;
                        }
                        Intrinsics.checkNotNullParameter(fileId, "fileId");
                        Intrinsics.checkNotNullParameter(info, "info");
                        this.f261331a.d(fileId, info);
                    }

                    @Override // com.tencent.mobileqq.guild.feed.api.j
                    public void e(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 3)) {
                            iPatchRedirector3.redirect((short) 3, (Object) this, (Object) fileId, (Object) info);
                            return;
                        }
                        Intrinsics.checkNotNullParameter(fileId, "fileId");
                        Intrinsics.checkNotNullParameter(info, "info");
                        this.f261331a.e(fileId, info);
                    }

                    @Override // com.tencent.mobileqq.guild.feed.api.j
                    public void f(@NotNull String fileId, long j15, long j16, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 5)) {
                            iPatchRedirector3.redirect((short) 5, this, fileId, Long.valueOf(j15), Long.valueOf(j16), info);
                            return;
                        }
                        Intrinsics.checkNotNullParameter(fileId, "fileId");
                        Intrinsics.checkNotNullParameter(info, "info");
                        this.f261331a.f(fileId, j15, j16, info);
                    }

                    @Override // com.tencent.mobileqq.guild.feed.api.j
                    public void g(@NotNull String fileId, @Nullable com.tencent.mobileqq.guild.data.media.d dVar) {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 2)) {
                            iPatchRedirector3.redirect((short) 2, (Object) this, (Object) fileId, (Object) dVar);
                        } else {
                            Intrinsics.checkNotNullParameter(fileId, "fileId");
                            this.f261331a.g(fileId, dVar);
                        }
                    }

                    @Override // com.tencent.mobileqq.guild.feed.api.j
                    public void h(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                        IPatchRedirector iPatchRedirector3 = $redirector_;
                        if (iPatchRedirector3 != null && iPatchRedirector3.hasPatch((short) 6)) {
                            iPatchRedirector3.redirect((short) 6, (Object) this, (Object) fileId, (Object) info);
                            return;
                        }
                        Intrinsics.checkNotNullParameter(fileId, "fileId");
                        Intrinsics.checkNotNullParameter(info, "info");
                        this.f261331a.h(fileId, info);
                    }
                } : (AnonymousClass1) iPatchRedirector2.redirect((short) 2, (Object) this);
            }
        });
    }

    public static final void A(FeedFileUploadTask feedFileUploadTask, Throwable th5) {
        QLog.i(feedFileUploadTask.f261318c, 1, "[onCancelCleanUp]: parentId=" + feedFileUploadTask.P() + ", cause=" + th5 + " notifyCanceled=" + feedFileUploadTask.f261322g);
        feedFileUploadTask.getConfig().b().m();
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi = (IGuildFeedFileUploadServiceApi) (appInterface != null ? appInterface.getRuntimeService(IGuildFeedFileUploadServiceApi.class, "") : null);
        if (iGuildFeedFileUploadServiceApi != null) {
            iGuildFeedFileUploadServiceApi.unsubscribeAllTaskChangeListener(feedFileUploadTask.R());
            Iterator<T> it = feedFileUploadTask.O().iterator();
            while (it.hasNext()) {
                iGuildFeedFileUploadServiceApi.removeUploadFileTask((String) it.next());
            }
        }
        if (feedFileUploadTask.f261322g && (!feedFileUploadTask.O().isEmpty())) {
            m mVar = feedFileUploadTask.f261321f;
            if (!mVar.j() || (th5 instanceof PassiveCancellationException)) {
                return;
            }
            for (Map.Entry entry : ((ConcurrentHashMap) feedFileUploadTask.f261320e).entrySet()) {
                String str = (String) entry.getKey();
                l lVar = (l) entry.getValue();
                Intrinsics.checkNotNull(str);
                mVar.g(str, lVar.c());
            }
        }
    }

    public static final boolean B(FeedFileUploadTask feedFileUploadTask) {
        feedFileUploadTask.getClass();
        return feedFileUploadTask.N(new FeedFileUploadTask$pauseAllTask$1(feedFileUploadTask, null));
    }

    public static final void D(FeedFileUploadTask feedFileUploadTask, int i15, List list) {
        String str = "[restoreSelectedInternal](" + i15 + "):parentId=" + feedFileUploadTask.P() + ", restoreList=" + list.size();
        String str2 = feedFileUploadTask.f261318c;
        QLog.i(str2, 1, str);
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi = (IGuildFeedFileUploadServiceApi) (appInterface != null ? appInterface.getRuntimeService(IGuildFeedFileUploadServiceApi.class, "") : null);
        if (iGuildFeedFileUploadServiceApi == null) {
            return;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.e eVar = (com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.e) it.next();
            iGuildFeedFileUploadServiceApi.subscribeTaskChangeListener(feedFileUploadTask.R(), eVar.a());
            int iA = feedFileUploadTask.getConfig().a().a(str2, eVar.a(), eVar.b().getSource());
            if (iA == 0 && iGuildFeedFileUploadServiceApi.restoreUploadFileTask(eVar.a(), eVar.b())) {
                feedFileUploadTask.W(i15, iGuildFeedFileUploadServiceApi, eVar.a(), eVar.b());
            } else {
                String strP = feedFileUploadTask.P();
                String strA = eVar.a();
                com.tencent.mobileqq.guild.data.media.d dVarC = eVar.c();
                QLog.e(str2, 1, "[restoreSelectedInternal](" + i15 + "): restore failed parentId=" + strP + ", fileId=" + strA + ", taskInfo=" + (dVarC != null ? com.tencent.mobileqq.guild.data.media.e.a(dVarC) : null));
                String strA2 = eVar.a();
                com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> aVarB = eVar.b();
                Integer numValueOf = Integer.valueOf(iA);
                if (!(numValueOf.intValue() != 0)) {
                    numValueOf = null;
                }
                feedFileUploadTask.V(i15, iGuildFeedFileUploadServiceApi, strA2, aVarB, numValueOf != null ? numValueOf.intValue() : -5);
            }
        }
    }

    public static final void H(FeedFileUploadTask feedFileUploadTask) {
        feedFileUploadTask.getConfig().b().m();
    }

    /* JADX WARN: Code duplicated, block: B:27:0x00a4  */
    /* JADX WARN: Code duplicated, block: B:29:0x00d9  */
    /* JADX WARN: Code duplicated, block: B:30:0x00dc  */
    /* JADX WARN: Code duplicated, block: B:33:0x00f2  */
    /* JADX WARN: Code duplicated, block: B:34:0x0162  */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:30:0x00dc -> B:31:0x00ea). Please report as a decompilation issue!!! */
    /*  JADX ERROR: StackOverflowError in pass: RegionMakerVisitor
        java.lang.StackOverflowError
        	at jadx.core.utils.BlockUtils.traverseSuccessorsUntil(BlockUtils.java:731)
        	at jadx.core.utils.BlockUtils.traverseSuccessorsUntil(BlockUtils.java:749)
        */
    public static final java.lang.Object I(com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask r19, int r20, java.util.Map r21, java.util.Set r22, kotlin.coroutines.Continuation r23) {
        /*
            Method dump skipped, instruction units count: 560
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask.I(com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.FeedFileUploadTask, int, java.util.Map, java.util.Set, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static final boolean J(FeedFileUploadTask feedFileUploadTask, int i15, boolean z15) {
        feedFileUploadTask.getClass();
        return feedFileUploadTask.N(new FeedFileUploadTask$triggerAllPendingTask$1(feedFileUploadTask, i15, z15, null));
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0016  */
    public static final Object K(FeedFileUploadTask feedFileUploadTask, int i15, boolean z15, Continuation continuation) {
        FeedFileUploadTask$triggerAllPendingTaskUndispatched$1 feedFileUploadTask$triggerAllPendingTaskUndispatched$1;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi;
        List<l> arrayList;
        FeedFileUploadTask feedFileUploadTask2;
        Iterator<String> it;
        feedFileUploadTask.getClass();
        if (continuation instanceof FeedFileUploadTask$triggerAllPendingTaskUndispatched$1) {
            feedFileUploadTask$triggerAllPendingTaskUndispatched$1 = (FeedFileUploadTask$triggerAllPendingTaskUndispatched$1) continuation;
            int i16 = feedFileUploadTask$triggerAllPendingTaskUndispatched$1.label;
            if ((i16 & Integer.MIN_VALUE) != 0) {
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1.label = i16 - Integer.MIN_VALUE;
            } else {
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1 = new FeedFileUploadTask$triggerAllPendingTaskUndispatched$1(feedFileUploadTask, continuation);
            }
        } else {
            feedFileUploadTask$triggerAllPendingTaskUndispatched$1 = new FeedFileUploadTask$triggerAllPendingTaskUndispatched$1(feedFileUploadTask, continuation);
        }
        Object obj = feedFileUploadTask$triggerAllPendingTaskUndispatched$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i17 = feedFileUploadTask$triggerAllPendingTaskUndispatched$1.label;
        if (i17 != 0) {
            if (i17 == 1) {
                z15 = feedFileUploadTask$triggerAllPendingTaskUndispatched$1.Z$0;
                i15 = feedFileUploadTask$triggerAllPendingTaskUndispatched$1.I$0;
                it = (Iterator) feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$3;
                arrayList = (List) feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$2;
                iGuildFeedFileUploadServiceApi = (IGuildFeedFileUploadServiceApi) feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$1;
                feedFileUploadTask2 = (FeedFileUploadTask) feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$0;
                ResultKt.throwOnFailure(obj);
            } else {
                if (i17 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
        }
        ResultKt.throwOnFailure(obj);
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        iGuildFeedFileUploadServiceApi = (IGuildFeedFileUploadServiceApi) (appInterface != null ? appInterface.getRuntimeService(IGuildFeedFileUploadServiceApi.class, "") : null);
        if (iGuildFeedFileUploadServiceApi == null) {
            return Unit.INSTANCE;
        }
        arrayList = new ArrayList<>();
        feedFileUploadTask2 = feedFileUploadTask;
        it = feedFileUploadTask.O().iterator();
        while (it.hasNext()) {
            String next = it.next();
            l lVar = (l) ((ConcurrentHashMap) feedFileUploadTask2.f261320e).get(next);
            if (lVar == null) {
                QLog.e(feedFileUploadTask2.f261318c, 1, "[triggerAllPendingTask](" + i15 + "): parentId=" + feedFileUploadTask2.P() + ", taskInfo of fileId=" + next + " not found, inconsistency error happen!");
            } else if ((lVar.c().l() || lVar.d()) && z15) {
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$0 = feedFileUploadTask2;
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$1 = iGuildFeedFileUploadServiceApi;
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$2 = arrayList;
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$3 = it;
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1.I$0 = i15;
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1.Z$0 = z15;
                feedFileUploadTask$triggerAllPendingTaskUndispatched$1.label = 1;
                if (feedFileUploadTask2.X(i15, iGuildFeedFileUploadServiceApi, lVar, feedFileUploadTask$triggerAllPendingTaskUndispatched$1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                arrayList.add(lVar);
            }
        }
        QLog.i(feedFileUploadTask2.f261318c, 1, "[triggerAllPendingTaskUndispatched](" + i15 + "): isRetryFail=" + z15 + ", taskCount=" + ((ConcurrentHashMap) feedFileUploadTask2.f261320e).size() + ", pendingWrapperList.size=" + arrayList.size());
        feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$0 = null;
        feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$1 = null;
        feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$2 = null;
        feedFileUploadTask$triggerAllPendingTaskUndispatched$1.L$3 = null;
        feedFileUploadTask$triggerAllPendingTaskUndispatched$1.label = 2;
        if (feedFileUploadTask2.Z(iGuildFeedFileUploadServiceApi, arrayList, z15, feedFileUploadTask$triggerAllPendingTaskUndispatched$1) == coroutine_suspended) {
            return coroutine_suspended;
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:36:0x00a7  */
    /* JADX WARN: Code duplicated, block: B:38:0x00ae  */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object L(IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi, String str, com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> aVar, Continuation<? super f> continuation) {
        FeedFileUploadTask$callServiceToCreateTaskWithPreChecked$1 feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi2;
        int i15;
        FeedFileUploadTask feedFileUploadTask;
        if (continuation instanceof FeedFileUploadTask$callServiceToCreateTaskWithPreChecked$1) {
            feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1 = (FeedFileUploadTask$callServiceToCreateTaskWithPreChecked$1) continuation;
            int i16 = feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.label;
            if ((i16 & Integer.MIN_VALUE) != 0) {
                feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.label = i16 - Integer.MIN_VALUE;
            } else {
                feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1 = new FeedFileUploadTask$callServiceToCreateTaskWithPreChecked$1(this, continuation);
            }
        } else {
            feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1 = new FeedFileUploadTask$callServiceToCreateTaskWithPreChecked$1(this, continuation);
        }
        Object obj = feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i17 = feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.label;
        if (i17 == 0) {
            ResultKt.throwOnFailure(obj);
            com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.strategy.a aVarA = getConfig().a();
            com.tencent.mobileqq.guild.data.publish.taskx.api.b source = aVar.getSource();
            String str2 = this.f261318c;
            int iA = aVarA.a(str2, str, source);
            if (iA == -3) {
                FeedUploadFileSandboxFallback feedUploadFileSandboxFallback = FeedUploadFileSandboxFallback.f261335a;
                feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.L$0 = this;
                feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.L$1 = iGuildFeedFileUploadServiceApi;
                feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.L$2 = str;
                feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.L$3 = aVar;
                feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.I$0 = iA;
                feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.label = 1;
                Object objB = feedUploadFileSandboxFallback.b(str2, str, aVar, feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1);
                if (objB == coroutine_suspended) {
                    return coroutine_suspended;
                }
                iGuildFeedFileUploadServiceApi2 = iGuildFeedFileUploadServiceApi;
                i15 = iA;
                obj = objB;
                feedFileUploadTask = this;
            } else if (iA != 0) {
                return new f(iA, aVar);
            }
            return !iGuildFeedFileUploadServiceApi.createUploadFileTask(str, aVar) ? new f(-7, aVar) : new f(0, aVar);
        }
        if (i17 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        i15 = feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.I$0;
        aVar = (com.tencent.mobileqq.guild.data.publish.taskx.api.a) feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.L$3;
        str = (String) feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.L$2;
        iGuildFeedFileUploadServiceApi2 = (IGuildFeedFileUploadServiceApi) feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.L$1;
        feedFileUploadTask = (FeedFileUploadTask) feedFileUploadTask$callServiceToCreateTaskWithPreChecked$1.L$0;
        ResultKt.throwOnFailure(obj);
        com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> aVar2 = (com.tencent.mobileqq.guild.data.publish.taskx.api.a) obj;
        if (aVar2 != null) {
            if (!(feedFileUploadTask.getConfig().a().a(feedFileUploadTask.f261318c, str, aVar2.getSource()) == 0)) {
                aVar2 = null;
            }
            if (aVar2 != null) {
                aVar = aVar2;
                iGuildFeedFileUploadServiceApi = iGuildFeedFileUploadServiceApi2;
                if (!iGuildFeedFileUploadServiceApi.createUploadFileTask(str, aVar)) {
                }
            }
        }
        return new f(i15, aVar);
    }

    private final boolean M(IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi, l lVar, int i15) {
        if (lVar.c().f()) {
            lVar.e();
            return true;
        }
        com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model = lVar.c().getModel();
        String str = this.f261318c;
        Integer numValueOf = model != null ? Integer.valueOf(getConfig().a().a(str, lVar.c().getFileId(), model.getSource())) : null;
        if (numValueOf != null && numValueOf.intValue() == 0 && iGuildFeedFileUploadServiceApi.resumeUploadFileTask(lVar.c().getFileId())) {
            lVar.e();
            if (QLog.isDevelopLevel()) {
                QLog.d(str, 4, "[callServiceToResumeTask](" + i15 + "): resume " + lVar.c().getFileId() + ", successfully");
            }
            return true;
        }
        QLog.e(str, 1, "[callServiceToResumeTask](" + i15 + "): resume task[" + lVar.c().getFileId() + "] failed, checkResult=" + numValueOf);
        lVar.g(true);
        StringBuilder sb5 = new StringBuilder("callServiceToResumeTask(");
        sb5.append(i15);
        sb5.append(") -> resume failed, checkResult=");
        sb5.append(numValueOf);
        lVar.f(sb5.toString());
        lVar.h((numValueOf != null && numValueOf.intValue() == 0) ? -6 : -3);
        return false;
    }

    private final boolean N(Function1<? super Continuation<? super Unit>, ? extends Object> function1) {
        boolean zIsActive = CoroutineScopeKt.isActive(Q());
        String str = this.f261318c;
        if (!zIsActive) {
            QLog.w(str, 1, "[enqueue]: " + P() + ", scope inactive, drop op");
            return false;
        }
        if (!((Boolean) this.f261323h.getValue()).booleanValue()) {
            BuildersKt__Builders_commonKt.launch$default(Q(), null, null, new FeedFileUploadTask$enqueue$1(function1, null), 3, null);
            return true;
        }
        Object objMo2268trySendJP2dKIU = this.f261326k.mo2268trySendJP2dKIU(function1);
        if (objMo2268trySendJP2dKIU instanceof ChannelResult.Failed) {
            Throwable thM2277exceptionOrNullimpl = ChannelResult.m2277exceptionOrNullimpl(objMo2268trySendJP2dKIU);
            QLog.w(str, 1, "[enqueue]: " + P() + ", trySend failed, " + thM2277exceptionOrNullimpl);
        }
        return ChannelResult.m2283isSuccessimpl(objMo2268trySendJP2dKIU);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Set<String> O() {
        return ((ConcurrentHashMap) this.f261320e).keySet();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String P() {
        return this.f261316a.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CoroutineScope Q() {
        return (CoroutineScope) this.f261325j.getValue();
    }

    private final FeedFileUploadTask$serviceListener$2.AnonymousClass1 R() {
        return (FeedFileUploadTask$serviceListener$2.AnonymousClass1) this.f261329n.getValue();
    }

    private final com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b S(int i15, com.tencent.mobileqq.guild.data.media.d dVar, String str) {
        String errMsg;
        String fileId;
        if (dVar == null || (errMsg = dVar.getErrMsg()) == null) {
            errMsg = "";
        }
        ConcurrentMap<String, l> concurrentMap = this.f261320e;
        LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(concurrentMap.size()));
        Iterator<T> it = concurrentMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            linkedHashMap.put(entry.getKey(), ((l) entry.getValue()).c());
        }
        int errCode = dVar != null ? dVar.getErrCode() : 0;
        if (dVar == null || (fileId = dVar.getFileId()) == null) {
            fileId = "";
        }
        com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b bVar = new com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b(-1, errMsg, linkedHashMap, errCode, fileId, dVar != null ? dVar.b() : 0, dVar != null ? dVar.h() : 0);
        bVar.j("seq=" + i15 + ", " + str + ", summary={" + a0(concurrentMap) + "}");
        return bVar;
    }

    private final com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b T(int i15, l lVar, String str) {
        com.tencent.mobileqq.guild.data.media.d dVarC = lVar.c();
        Integer numValueOf = Integer.valueOf(lVar.b());
        if (!(numValueOf.intValue() != 0)) {
            numValueOf = null;
        }
        int iIntValue = numValueOf != null ? numValueOf.intValue() : -2;
        String strA = lVar.a();
        ConcurrentMap<String, l> concurrentMap = this.f261320e;
        LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(concurrentMap.size()));
        Iterator<T> it = concurrentMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            linkedHashMap.put(entry.getKey(), ((l) entry.getValue()).c());
        }
        com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b bVar = new com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b(iIntValue, strA, MapsKt.toMap(linkedHashMap), 0, dVarC.getFileId(), 0, 0, 96, null);
        bVar.k(lVar.c());
        bVar.j("seq=" + i15 + ", " + str + " summary={" + a0(concurrentMap) + "}");
        return bVar;
    }

    private final com.tencent.mobileqq.guild.data.media.d U(int i15, com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> aVar) {
        com.tencent.mobileqq.guild.data.media.d dVarB = getConfig().e().b(aVar);
        if (dVarB == null) {
            QLog.e(this.f261318c, 1, "[makeInitialStateTaskInfo](" + i15 + "): init task info is null, please check the type adapter's strategy!!");
        }
        return dVarB;
    }

    private final void V(int i15, IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi, String str, com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> aVar, int i16) {
        iGuildFeedFileUploadServiceApi.unsubscribeTaskChangeListener(R(), str);
        com.tencent.mobileqq.guild.data.media.d dVarU = U(i15, aVar);
        if (dVarU == null) {
            return;
        }
        ConcurrentMap<String, l> concurrentMap = this.f261320e;
        l lVar = new l(dVarU);
        lVar.g(true);
        lVar.h(i16);
        lVar.f("create task failed, seq=" + i15 + ", fileId=" + str + " path=" + getConfig().d().a(aVar.getSource()));
        ((ConcurrentHashMap) concurrentMap).put(str, lVar);
    }

    private final void W(int i15, IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi, String str, com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> aVar) {
        com.tencent.mobileqq.guild.data.media.d dVarU = U(i15, aVar);
        if (dVarU == null) {
            return;
        }
        this.f261320e.put(str, new l(dVarU));
        boolean zA = a();
        boolean zBooleanValue = this.f261316a.a().invoke().booleanValue();
        getConfig().b().j(zBooleanValue);
        String str2 = "[triggerResumeIfNeeded](" + i15 + "): parentId=" + P() + ", fileId=" + str + ", parentTaskAllowPreUploadNow=" + zBooleanValue;
        String str3 = this.f261318c;
        QLog.d(str3, 1, str2);
        if (zA || getConfig().b().c()) {
            getConfig().b().l();
            if (iGuildFeedFileUploadServiceApi.resumeUploadFileTask(str)) {
                return;
            }
            QLog.e(str3, 1, "[triggerResumeIfNeeded](" + i15 + "): resume failed parentId=" + P() + ", fileId=" + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:35:0x00f6  */
    /* JADX WARN: Code duplicated, block: B:37:0x011a  */
    /* JADX WARN: Code duplicated, block: B:38:0x011f  */
    /* JADX WARN: Code duplicated, block: B:44:0x01a7  */
    /* JADX WARN: Code duplicated, block: B:45:0x01c2  */
    /* JADX WARN: Code duplicated, block: B:7:0x0019  */
    /* JADX WARN: Instruction removed from duplicated block: B:35:0x00f6, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:44:0x01a7, please report this as an issue */
    public final Object X(int i15, IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi, l lVar, Continuation<? super Unit> continuation) {
        FeedFileUploadTask$recreateTaskInternal$1 feedFileUploadTask$recreateTaskInternal$1;
        com.tencent.mobileqq.guild.data.media.d dVarC;
        String fileId;
        com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model;
        Ref.IntRef intRef;
        int i16;
        int i17;
        l lVar2;
        FeedFileUploadTask feedFileUploadTask;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi2;
        int i18;
        f fVar;
        FeedFileUploadTask feedFileUploadTask2;
        Object objL;
        StringBuilder sb5;
        int i19;
        String str;
        com.tencent.mobileqq.guild.data.publish.taskx.api.b source;
        int iB;
        if (continuation instanceof FeedFileUploadTask$recreateTaskInternal$1) {
            feedFileUploadTask$recreateTaskInternal$1 = (FeedFileUploadTask$recreateTaskInternal$1) continuation;
            int i25 = feedFileUploadTask$recreateTaskInternal$1.label;
            if ((i25 & Integer.MIN_VALUE) != 0) {
                feedFileUploadTask$recreateTaskInternal$1.label = i25 - Integer.MIN_VALUE;
            } else {
                feedFileUploadTask$recreateTaskInternal$1 = new FeedFileUploadTask$recreateTaskInternal$1(this, continuation);
            }
        } else {
            feedFileUploadTask$recreateTaskInternal$1 = new FeedFileUploadTask$recreateTaskInternal$1(this, continuation);
        }
        Object obj = feedFileUploadTask$recreateTaskInternal$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i26 = feedFileUploadTask$recreateTaskInternal$1.label;
        String strA = null;
        if (i26 == 0) {
            ResultKt.throwOnFailure(obj);
            dVarC = lVar.c();
            fileId = dVarC.getFileId();
            model = dVarC.getModel();
            intRef = new Ref.IntRef();
            iGuildFeedFileUploadServiceApi.subscribeTaskChangeListener(R(), fileId);
            i16 = 4;
            if (iGuildFeedFileUploadServiceApi.removeUploadFileTask(fileId)) {
                intRef.element++;
                if (model != null) {
                    feedFileUploadTask$recreateTaskInternal$1.L$0 = this;
                    feedFileUploadTask$recreateTaskInternal$1.L$1 = iGuildFeedFileUploadServiceApi;
                    lVar2 = lVar;
                    feedFileUploadTask$recreateTaskInternal$1.L$2 = lVar2;
                    feedFileUploadTask$recreateTaskInternal$1.L$3 = dVarC;
                    feedFileUploadTask$recreateTaskInternal$1.L$4 = fileId;
                    feedFileUploadTask$recreateTaskInternal$1.L$5 = model;
                    feedFileUploadTask$recreateTaskInternal$1.L$6 = intRef;
                    feedFileUploadTask$recreateTaskInternal$1.L$7 = this;
                    i17 = i15;
                    feedFileUploadTask$recreateTaskInternal$1.I$0 = i17;
                    feedFileUploadTask$recreateTaskInternal$1.I$1 = 4;
                    feedFileUploadTask$recreateTaskInternal$1.label = 1;
                    objL = L(iGuildFeedFileUploadServiceApi, fileId, model, feedFileUploadTask$recreateTaskInternal$1);
                    if (objL == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    feedFileUploadTask = this;
                    iGuildFeedFileUploadServiceApi2 = iGuildFeedFileUploadServiceApi;
                    i18 = 4;
                    feedFileUploadTask2 = feedFileUploadTask;
                } else {
                    i17 = i15;
                    lVar2 = lVar;
                    feedFileUploadTask = this;
                    iGuildFeedFileUploadServiceApi2 = iGuildFeedFileUploadServiceApi;
                    i18 = 4;
                    fVar = null;
                    feedFileUploadTask2 = feedFileUploadTask;
                }
                if (fVar == null && fVar.b() == 0) {
                    intRef.element++;
                    com.tencent.mobileqq.guild.data.media.d dVarU = feedFileUploadTask2.U(i17, fVar.a());
                    if (dVarU != null) {
                        ConcurrentMap<String, l> concurrentMap = feedFileUploadTask2.f261320e;
                        lVar2.i(dVarU);
                        ((ConcurrentHashMap) concurrentMap).put(fileId, lVar2);
                        intRef.element++;
                        if (feedFileUploadTask2.M(iGuildFeedFileUploadServiceApi2, lVar2, i17)) {
                            intRef.element++;
                        }
                    }
                } else {
                    iGuildFeedFileUploadServiceApi2.unsubscribeTaskChangeListener(feedFileUploadTask2.R(), fileId);
                    lVar2.g(true);
                    lVar2.f("[recreateTaskInternal](" + i17 + ") -> create task failed, localMedia=" + model);
                    if (fVar != null) {
                        iB = fVar.b();
                    } else {
                        iB = -4;
                    }
                    lVar2.h(iB);
                }
                i16 = i18;
            } else {
                i17 = i15;
                lVar2 = lVar;
                feedFileUploadTask = this;
            }
            sb5 = new StringBuilder("[recreateTaskInternal](" + i17 + "): parentId=" + feedFileUploadTask.P() + ", ");
            sb5.append("fileId=" + dVarC.getFileId() + ", execStep=" + intRef.element + ", ");
            boolean zD = lVar2.d();
            StringBuilder sb6 = new StringBuilder("wrapper.isInconsistent=");
            sb6.append(zD);
            sb6.append(", ");
            sb5.append(sb6.toString());
            sb5.append("wrapper.inconsistencyMessage=" + lVar2.a());
            i19 = intRef.element;
            str = feedFileUploadTask.f261318c;
            if (i16 != i19) {
                sb5.append(", model=" + model);
                QLog.e(str, 1, sb5);
            } else {
                if (model != null && (source = model.getSource()) != null) {
                    strA = feedFileUploadTask.getConfig().d().a(source);
                }
                sb5.append(", path=" + strA);
                QLog.i(str, 1, sb5.toString());
            }
            return Unit.INSTANCE;
        }
        if (i26 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        i18 = feedFileUploadTask$recreateTaskInternal$1.I$1;
        int i27 = feedFileUploadTask$recreateTaskInternal$1.I$0;
        feedFileUploadTask2 = (FeedFileUploadTask) feedFileUploadTask$recreateTaskInternal$1.L$7;
        intRef = (Ref.IntRef) feedFileUploadTask$recreateTaskInternal$1.L$6;
        model = (com.tencent.mobileqq.guild.data.publish.taskx.api.a) feedFileUploadTask$recreateTaskInternal$1.L$5;
        fileId = (String) feedFileUploadTask$recreateTaskInternal$1.L$4;
        dVarC = (com.tencent.mobileqq.guild.data.media.d) feedFileUploadTask$recreateTaskInternal$1.L$3;
        l lVar3 = (l) feedFileUploadTask$recreateTaskInternal$1.L$2;
        iGuildFeedFileUploadServiceApi2 = (IGuildFeedFileUploadServiceApi) feedFileUploadTask$recreateTaskInternal$1.L$1;
        FeedFileUploadTask feedFileUploadTask3 = (FeedFileUploadTask) feedFileUploadTask$recreateTaskInternal$1.L$0;
        ResultKt.throwOnFailure(obj);
        objL = obj;
        lVar2 = lVar3;
        i17 = i27;
        feedFileUploadTask = feedFileUploadTask3;
        fVar = (f) objL;
        if (fVar == null) {
            iGuildFeedFileUploadServiceApi2.unsubscribeTaskChangeListener(feedFileUploadTask2.R(), fileId);
            lVar2.g(true);
            lVar2.f("[recreateTaskInternal](" + i17 + ") -> create task failed, localMedia=" + model);
            if (fVar != null) {
                iB = fVar.b();
            } else {
                iB = -4;
            }
            lVar2.h(iB);
        } else {
            iGuildFeedFileUploadServiceApi2.unsubscribeTaskChangeListener(feedFileUploadTask2.R(), fileId);
            lVar2.g(true);
            lVar2.f("[recreateTaskInternal](" + i17 + ") -> create task failed, localMedia=" + model);
            if (fVar != null) {
                iB = fVar.b();
            } else {
                iB = -4;
            }
            lVar2.h(iB);
        }
        i16 = i18;
        sb5 = new StringBuilder("[recreateTaskInternal](" + i17 + "): parentId=" + feedFileUploadTask.P() + ", ");
        sb5.append("fileId=" + dVarC.getFileId() + ", execStep=" + intRef.element + ", ");
        boolean zD2 = lVar2.d();
        StringBuilder sb7 = new StringBuilder("wrapper.isInconsistent=");
        sb7.append(zD2);
        sb7.append(", ");
        sb5.append(sb7.toString());
        sb5.append("wrapper.inconsistencyMessage=" + lVar2.a());
        i19 = intRef.element;
        str = feedFileUploadTask.f261318c;
        if (i16 != i19) {
            sb5.append(", model=" + model);
            QLog.e(str, 1, sb5);
        } else {
            if (model != null) {
                strA = feedFileUploadTask.getConfig().d().a(source);
            }
            sb5.append(", path=" + strA);
            QLog.i(str, 1, sb5.toString());
        }
        return Unit.INSTANCE;
    }

    private final void Y(int i15, Continuation<? super com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b> continuation, com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b bVar) {
        QLog.i(this.f261318c, 1, "[resumeAwait](" + i15 + "): " + P() + ", result=" + bVar);
        continuation.resumeWith(Result.m747constructorimpl(bVar));
        this.f261328m = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object Z(IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi, List<l> list, boolean z15, Continuation<? super Unit> continuation) {
        FeedFileUploadTask$resumeOrRecreatePendingTasks$1 feedFileUploadTask$resumeOrRecreatePendingTasks$1;
        Map linkedHashMap;
        Set set;
        FeedFileUploadTask feedFileUploadTask;
        Iterator it;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi2;
        int i15;
        if (continuation instanceof FeedFileUploadTask$resumeOrRecreatePendingTasks$1) {
            feedFileUploadTask$resumeOrRecreatePendingTasks$1 = (FeedFileUploadTask$resumeOrRecreatePendingTasks$1) continuation;
            int i16 = feedFileUploadTask$resumeOrRecreatePendingTasks$1.label;
            if ((i16 & Integer.MIN_VALUE) != 0) {
                feedFileUploadTask$resumeOrRecreatePendingTasks$1.label = i16 - Integer.MIN_VALUE;
            } else {
                feedFileUploadTask$resumeOrRecreatePendingTasks$1 = new FeedFileUploadTask$resumeOrRecreatePendingTasks$1(this, continuation);
            }
        } else {
            feedFileUploadTask$resumeOrRecreatePendingTasks$1 = new FeedFileUploadTask$resumeOrRecreatePendingTasks$1(this, continuation);
        }
        Object obj = feedFileUploadTask$resumeOrRecreatePendingTasks$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i17 = feedFileUploadTask$resumeOrRecreatePendingTasks$1.label;
        if (i17 == 0) {
            ResultKt.throwOnFailure(obj);
            int iIncrementAndGet = this.f261319d.incrementAndGet();
            if (z15) {
                List<l> list2 = list;
                linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(list2, 10)), 16));
                for (l lVar : list2) {
                    Pair pair = TuplesKt.to(lVar.c().getFileId(), lVar);
                    linkedHashMap.put(pair.getFirst(), pair.getSecond());
                }
                LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                for (Map.Entry entry : linkedHashMap.entrySet()) {
                    if (((l) entry.getValue()).c().f()) {
                        linkedHashMap2.put(entry.getKey(), entry.getValue());
                    }
                }
                Set setKeySet = linkedHashMap2.keySet();
                Set setMinus = SetsKt.minus(linkedHashMap.keySet(), (Iterable) setKeySet);
                set = CollectionsKt.toSet(iGuildFeedFileUploadServiceApi.batchGetNeedToCreateFileIds(CollectionsKt.toList(setMinus)));
                String str = this.f261318c;
                QLog.i(str, 1, "[resumeOrRecreatePendingTasks](" + iIncrementAndGet + "): alreadyFinishedSet=" + setKeySet + ", needToCreateFileIdSet=" + set + ", notFinishedSet=" + setMinus);
                QLog.i(str, 1, "[resumeOrRecreatePendingTasks](" + iIncrementAndGet + "): alreadyFinishedSet.size=" + setKeySet.size() + " needToCreateFileIdSet.size=" + set.size() + ", pendingMap.size=" + linkedHashMap.size());
                feedFileUploadTask = this;
                it = set.iterator();
                iGuildFeedFileUploadServiceApi2 = iGuildFeedFileUploadServiceApi;
                i15 = iIncrementAndGet;
            } else {
                Iterator<l> it4 = list.iterator();
                while (it4.hasNext()) {
                    M(iGuildFeedFileUploadServiceApi, it4.next(), iIncrementAndGet);
                }
            }
            return Unit.INSTANCE;
        }
        if (i17 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        i15 = feedFileUploadTask$resumeOrRecreatePendingTasks$1.I$0;
        Iterator it5 = (Iterator) feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$4;
        Set set2 = (Set) feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$3;
        linkedHashMap = (Map) feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$2;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi3 = (IGuildFeedFileUploadServiceApi) feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$1;
        feedFileUploadTask = (FeedFileUploadTask) feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$0;
        ResultKt.throwOnFailure(obj);
        it = it5;
        iGuildFeedFileUploadServiceApi2 = iGuildFeedFileUploadServiceApi3;
        set = set2;
        while (it.hasNext()) {
            l lVar2 = (l) linkedHashMap.get((String) it.next());
            if (lVar2 != null) {
                feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$0 = feedFileUploadTask;
                feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$1 = iGuildFeedFileUploadServiceApi2;
                feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$2 = linkedHashMap;
                feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$3 = set;
                feedFileUploadTask$resumeOrRecreatePendingTasks$1.L$4 = it;
                feedFileUploadTask$resumeOrRecreatePendingTasks$1.I$0 = i15;
                feedFileUploadTask$resumeOrRecreatePendingTasks$1.label = 1;
                if (feedFileUploadTask.X(i15, iGuildFeedFileUploadServiceApi2, lVar2, feedFileUploadTask$resumeOrRecreatePendingTasks$1) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            }
        }
        for (Map.Entry entry2 : linkedHashMap.entrySet()) {
            String str2 = (String) entry2.getKey();
            l lVar3 = (l) entry2.getValue();
            if (!set.contains(str2)) {
                feedFileUploadTask.M(iGuildFeedFileUploadServiceApi2, lVar3, i15);
            }
        }
        return Unit.INSTANCE;
    }

    private final String a0(Map<String, l> map) {
        if (map.isEmpty()) {
            return "";
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (Map.Entry<String, l> entry : map.entrySet()) {
            arrayList.add(Integer.valueOf(entry.getValue().c().getState()));
            arrayList2.add(entry.getKey());
        }
        return "states=" + CollectionsKt.joinToString$default(arrayList, null, "[", "]", 0, null, null, 57, null) + ", fileIds=" + CollectionsKt.joinToString$default(arrayList2, null, "[", "]", 0, null, null, 57, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final void k(FeedFileUploadTask feedFileUploadTask, int i15, com.tencent.mobileqq.guild.data.media.d dVar) {
        int i16;
        l lVar;
        l lVar2;
        CancellableContinuation<? super com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b> cancellableContinuation = feedFileUploadTask.f261328m;
        int i17 = 0;
        if ((cancellableContinuation != null && cancellableContinuation.isActive()) != true) {
            QLog.w(feedFileUploadTask.f261318c, 1, "[fuseTaskChange](" + i15 + "): parentId=" + feedFileUploadTask.P() + ", awaitContinuation is not active, but current state is awaiting");
            return;
        }
        l lVar3 = null;
        if (dVar != null) {
            if (!dVar.l()) {
                l lVar4 = (l) ((ConcurrentHashMap) feedFileUploadTask.f261320e).get(dVar.getFileId());
                lVar2 = lVar4 != null && lVar4.d() ? lVar4 : null;
            }
            feedFileUploadTask.Y(i15, cancellableContinuation, lVar2 != null ? feedFileUploadTask.T(i15, lVar2, "") : feedFileUploadTask.S(i15, dVar, ""));
            return;
        }
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) feedFileUploadTask.f261320e;
        if (concurrentHashMap.isEmpty()) {
            i16 = 0;
        } else {
            Iterator it = concurrentHashMap.entrySet().iterator();
            i16 = 0;
            while (it.hasNext()) {
                if (((l) ((Map.Entry) it.next()).getValue()).c().f()) {
                    i16++;
                }
            }
        }
        if (!concurrentHashMap.isEmpty()) {
            Iterator it4 = concurrentHashMap.entrySet().iterator();
            while (it4.hasNext()) {
                if (((l) ((Map.Entry) it4.next()).getValue()).c().l()) {
                    i17++;
                }
            }
        }
        if (i16 == concurrentHashMap.size()) {
            int i18 = 0;
            String str = "";
            LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(concurrentHashMap.size()));
            for (Map.Entry entry : concurrentHashMap.entrySet()) {
                linkedHashMap.put(entry.getKey(), ((l) entry.getValue()).c());
            }
            feedFileUploadTask.Y(i15, cancellableContinuation, new com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b(i18, str, linkedHashMap, 0, null, 0, 0, 112, null));
            return;
        }
        if (i17 > 0) {
            Iterator it5 = concurrentHashMap.entrySet().iterator();
            do {
                if (!it5.hasNext()) {
                    lVar = null;
                    break;
                } else {
                    Map.Entry entry2 = (Map.Entry) it5.next();
                    lVar = ((l) entry2.getValue()).c().l() ? (l) entry2.getValue() : null;
                }
            } while (lVar == null);
            feedFileUploadTask.Y(i15, cancellableContinuation, feedFileUploadTask.S(i15, lVar != null ? lVar.c() : null, "successCount=" + i16 + ", failCount=" + i17));
            return;
        }
        for (Map.Entry entry3 : concurrentHashMap.entrySet()) {
            l lVar5 = ((l) entry3.getValue()).d() ? (l) entry3.getValue() : null;
            if (lVar5 != null) {
                lVar3 = lVar5;
                break;
            }
        }
        if (lVar3 != null) {
            feedFileUploadTask.Y(i15, cancellableContinuation, feedFileUploadTask.T(i15, lVar3, "successCount=" + i16 + ", failCount=" + i17));
        }
    }

    public static final void l(FeedFileUploadTask feedFileUploadTask) {
        feedFileUploadTask.f261327l = null;
    }

    public static final void m(FeedFileUploadTask feedFileUploadTask) {
        feedFileUploadTask.getClass();
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi = (IGuildFeedFileUploadServiceApi) (appInterface != null ? appInterface.getRuntimeService(IGuildFeedFileUploadServiceApi.class, "") : null);
        if (iGuildFeedFileUploadServiceApi == null) {
            return;
        }
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) feedFileUploadTask.f261320e;
        Iterator it = concurrentHashMap.entrySet().iterator();
        while (it.hasNext()) {
            String str = (String) ((Map.Entry) it.next()).getKey();
            Intrinsics.checkNotNull(str);
            if (!iGuildFeedFileUploadServiceApi.removeUploadFileTask(str)) {
                QLog.w(feedFileUploadTask.f261318c, 1, "[clearTaskInfoMapBothSelfAndService]: remove " + str + " failed!");
            }
        }
        concurrentHashMap.clear();
    }

    public static final Object n(FeedFileUploadTask feedFileUploadTask, int i15, Continuation continuation) {
        if (!((Boolean) feedFileUploadTask.f261323h.getValue()).booleanValue()) {
            return Unit.INSTANCE;
        }
        CompletableDeferred completableDeferredCompletableDeferred$default = CompletableDeferredKt.CompletableDeferred$default(null, 1, null);
        if (feedFileUploadTask.N(new FeedFileUploadTask$drainOperations$2(completableDeferredCompletableDeferred$default, null))) {
            Object objAwait = completableDeferredCompletableDeferred$default.await(continuation);
            return objAwait == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objAwait : Unit.INSTANCE;
        }
        QLog.w(feedFileUploadTask.f261318c, 1, "[drainOperations](" + i15 + "): " + feedFileUploadTask.P() + ", barrier not enqueued, skip waiting");
        return Unit.INSTANCE;
    }

    public static final void o(FeedFileUploadTask feedFileUploadTask, int i15, com.tencent.mobileqq.guild.data.media.d dVar) {
        boolean z15;
        feedFileUploadTask.getClass();
        boolean zIsCancelled = dVar.isCancelled();
        String str = feedFileUploadTask.f261318c;
        if (zIsCancelled) {
            QLog.w(str, 1, "[fuseTaskChangeInfo](" + i15 + "): fileId=" + dVar.getFileId() + ", task is cancelled");
        }
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) feedFileUploadTask.f261320e;
        l lVar = (l) concurrentHashMap.get(dVar.getFileId());
        if (lVar == null) {
            String fileId = dVar.getFileId();
            l lVar2 = new l(dVar);
            lVar2.g(dVar.isCancelled());
            lVar2.f(lVar2.d() ? "fuseTaskChange -> task is cancelled" : "");
            concurrentHashMap.put(fileId, lVar2);
        } else {
            lVar.i(dVar);
            lVar.g(dVar.isCancelled());
            lVar.f(lVar.d() ? "fuseTaskChange -> task is cancelled" : "");
        }
        if (!concurrentHashMap.isEmpty()) {
            Iterator it = concurrentHashMap.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    z15 = true;
                    break;
                } else if (!((l) ((Map.Entry) it.next()).getValue()).c().isCompleted()) {
                    z15 = false;
                    break;
                }
            }
        } else {
            z15 = true;
            break;
        }
        if (z15) {
            QLog.i(str, 1, "[fuseTaskChange](" + i15 + "): all tasks are completed,  count=" + concurrentHashMap.size());
        }
    }

    public static final boolean z(FeedFileUploadTask feedFileUploadTask) {
        return ((Boolean) feedFileUploadTask.f261323h.getValue()).booleanValue();
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    public boolean a() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 4)) {
            return ((Boolean) iPatchRedirector.redirect((short) 4, (Object) this)).booleanValue();
        }
        Deferred<com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b> deferred = this.f261327l;
        if (deferred != null) {
            return !deferred.isCompleted() && !deferred.isCancelled();
        }
        return false;
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    public void b(@NotNull com.tencent.mobileqq.guild.feed.api.j listener) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 13)) {
            iPatchRedirector.redirect((short) 13, (Object) this, (Object) listener);
        } else {
            Intrinsics.checkNotNullParameter(listener, "listener");
            this.f261321f.i(listener);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    public void c(@NotNull String source, @NotNull Map<String, ? extends com.tencent.mobileqq.guild.data.publish.taskx.api.a<?>> fileModels) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 9)) {
            iPatchRedirector.redirect((short) 9, (Object) this, (Object) source, (Object) fileModels);
            return;
        }
        Intrinsics.checkNotNullParameter(source, "source");
        Intrinsics.checkNotNullParameter(fileModels, "fileModels");
        boolean zIsActive = CoroutineScopeKt.isActive(Q());
        int andIncrement = this.f261319d.getAndIncrement();
        QLog.i(this.f261318c, 1, "[submit](" + andIncrement + "): parentId=" + P() + ", source=" + source + ", select.size=" + fileModels.size() + ", before.size=" + O().size() + ", scope.isActive=" + zIsActive);
        if (zIsActive) {
            N(new FeedFileUploadTask$submit$1(fileModels, this, andIncrement, null));
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    public void cancel(@Nullable CancellationException cancellationException) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 11)) {
            iPatchRedirector.redirect((short) 11, (Object) this, (Object) cancellationException);
            return;
        }
        QLog.i(this.f261318c, 1, "[cancel](" + this.f261319d.getAndIncrement() + "): parentId=" + P() + ", message=" + (cancellationException != null ? cancellationException.getMessage() : null));
        CoroutineScopeKt.cancel(Q(), cancellationException);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    @NotNull
    public Map<String, com.tencent.mobileqq.guild.data.media.d> d() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
            return (Map) iPatchRedirector.redirect((short) 3, (Object) this);
        }
        ConcurrentMap<String, l> concurrentMap = this.f261320e;
        LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(concurrentMap.size()));
        Iterator<T> it = concurrentMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            linkedHashMap.put(entry.getKey(), ((l) entry.getValue()).c());
        }
        return linkedHashMap;
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    public void e(@NotNull List<com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.e> restoreInfoList) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 10)) {
            iPatchRedirector.redirect((short) 10, (Object) this, (Object) restoreInfoList);
            return;
        }
        Intrinsics.checkNotNullParameter(restoreInfoList, "restoreInfoList");
        boolean zIsActive = CoroutineScopeKt.isActive(Q());
        int andIncrement = this.f261319d.getAndIncrement();
        QLog.i(this.f261318c, 1, "[restore](" + andIncrement + "): parentId=" + P() + " restoreInfoList.size=" + restoreInfoList.size() + ", before.size=" + O().size() + ", scope.isActive=" + zIsActive);
        if (zIsActive) {
            N(new FeedFileUploadTask$restore$1(this, andIncrement, restoreInfoList, null));
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    public void f(@NotNull com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> file) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 5)) {
            iPatchRedirector.redirect((short) 5, (Object) this, (Object) file);
            return;
        }
        Intrinsics.checkNotNullParameter(file, "file");
        int andIncrement = this.f261319d.getAndIncrement();
        String fileId = file.getFileId();
        QLog.i(this.f261318c, 1, "[submitSingleFile](" + andIncrement + "): parentId=" + P() + ", fileId=" + fileId + ", file=" + getConfig().d().a(file.getSource()) + " before.size=" + O().size() + ", scope.isActive=" + CoroutineScopeKt.isActive(Q()));
        N(new FeedFileUploadTask$submitSingleFile$1(this, fileId, andIncrement, file, null));
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    @Nullable
    public Object g(@NotNull Continuation<? super Map<String, ? extends com.tencent.mobileqq.guild.data.media.d>> continuation) {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 12)) ? BuildersKt.withContext(Q().getCoroutineContext(), new FeedFileUploadTask$getTaskInfoMapSuspended$2(this, null), continuation) : iPatchRedirector.redirect((short) 12, (Object) this, (Object) continuation);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    @NotNull
    public k getConfig() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) ? this.f261317b : (k) iPatchRedirector.redirect((short) 2, (Object) this);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    @Nullable
    public Object h(boolean z15, @NotNull Continuation<? super com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b> continuation) {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 15)) ? BuildersKt.withContext(Q().getCoroutineContext(), new FeedFileUploadTask$awaitAll$2(this, z15, null), continuation) : iPatchRedirector.redirect((short) 15, this, Boolean.valueOf(z15), continuation);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    public void i(@NotNull com.tencent.mobileqq.guild.feed.api.j listener) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 14)) {
            iPatchRedirector.redirect((short) 14, (Object) this, (Object) listener);
        } else {
            Intrinsics.checkNotNullParameter(listener, "listener");
            this.f261321f.k(listener);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.a
    public void release() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 16)) {
            iPatchRedirector.redirect((short) 16, (Object) this);
            return;
        }
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        IGuildFeedFileUploadServiceApi iGuildFeedFileUploadServiceApi = (IGuildFeedFileUploadServiceApi) (appInterface != null ? appInterface.getRuntimeService(IGuildFeedFileUploadServiceApi.class, "") : null);
        if (iGuildFeedFileUploadServiceApi != null) {
            iGuildFeedFileUploadServiceApi.unsubscribeAllTaskChangeListener(R());
        }
        this.f261322g = false;
        cancel(new CancellationException("release"));
    }
}