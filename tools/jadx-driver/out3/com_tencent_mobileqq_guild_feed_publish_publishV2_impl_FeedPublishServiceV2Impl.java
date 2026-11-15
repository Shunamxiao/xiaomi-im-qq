package com.tencent.mobileqq.guild.feed.publish.publishV2.impl;

import android.os.Handler;
import android.os.Looper;
import com.tencent.ams.mosaic.MosaicConstants$JsFunction;
import com.tencent.biz.richframework.eventbus.SimpleEventBus;
import com.tencent.common.app.AppInterface;
import com.tencent.gamecenter.wadl.biz.entity.WadlProxyConsts;
import com.tencent.guild.aio.util.j;
import com.tencent.luggage.wxa.fg.e0;
import com.tencent.mobileqq.guild.api.IGuildTransactionManageService;
import com.tencent.mobileqq.guild.config.GuildFeedAutoResendConfigParser;
import com.tencent.mobileqq.guild.feed.event.FeedEditorEvent;
import com.tencent.mobileqq.guild.feed.event.GuildFeedDeleteEvent;
import com.tencent.mobileqq.guild.feed.nativeinterface.kt.GProStFeedKt;
import com.tencent.mobileqq.guild.feed.publish.bean.GuildMediaProgressState;
import com.tencent.mobileqq.guild.feed.publish.bean.GuildTaskProgressState;
import com.tencent.mobileqq.guild.feed.publish.event.GuildPublishSuccessGetFeedEvent;
import com.tencent.mobileqq.guild.feed.publish.event.GuildTaskProgressStateEvent;
import com.tencent.mobileqq.guild.feed.publish.h;
import com.tencent.mobileqq.guild.feed.publish.helper.GuildDbDataHelper;
import com.tencent.mobileqq.guild.feed.publish.helper.GuildTaskProgressBoxHelper;
import com.tencent.mobileqq.guild.feed.publish.n;
import com.tencent.mobileqq.guild.feed.publish.p;
import com.tencent.mobileqq.guild.feed.publish.publishV2.AbsFeedEditorEventListener;
import com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2;
import com.tencent.mobileqq.guild.feed.publish.publishV2.b;
import com.tencent.mobileqq.guild.feed.publish.publishV2.d;
import com.tencent.mobileqq.guild.feed.util.ai;
import com.tencent.mobileqq.guild.util.Logger;
import com.tencent.mobileqq.guild.util.bp;
import com.tencent.mobileqq.guild.util.dm;
import com.tencent.mobileqq.msf.sdk.AppNetConnInfo;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.mobileqq.qfix.redirect.PatchRedirectCenter;
import com.tencent.mobileqq.qqguildsdk.api.IGPSService;
import com.tencent.mobileqq.qqguildsdk.api.IGProGlobalService;
import com.tencent.mobileqq.qqguildsdk.data.IGProChannelInfo;
import com.tencent.mobileqq.qqguildsdk.data.IGProGuildInfo;
import com.tencent.mobileqq.qqguildsdk.data.IGProGuildRoleInfo;
import com.tencent.mobileqq.qqguildsdk.data.cz;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProClientIdentity;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProSimpleProfile;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProUserTag;
import com.tencent.mobileqq.qqguildsdk.observer.GPServiceObserver;
import com.tencent.mobileqq.shortvideo.gesture.DownloadInfo;
import com.tencent.qphone.base.util.QLog;
import com.tencent.qqnt.kernel.nativeinterface.en;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProChannelUserInfo;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProCustomManageTag;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProPersonalMedal;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStFeed;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStUser;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProUserIdentityData;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProUserTag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.SupervisorKt;
import kotlinx.coroutines.TimeoutKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.SharingStarted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import p022mqq.app.AppRuntime;
import p022mqq.app.MobileQQ;

/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000Ð\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010#\n\u0000\n\u0002\u0010\"\n\u0002\b\t\u0018\u0000 _2\u00020\u00012\u00020\u0002:\u0003`abB\u0007¢\u0006\u0004\b]\u0010^J6\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\bH\u0082@¢\u0006\u0004\b\f\u0010\rJ\u0018\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u000eH\u0082@¢\u0006\u0004\b\u0011\u0010\u0012J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0003H\u0002J\u0010\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0004\u001a\u00020\u0003H\u0002J\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\u0016\u001a\u00020\u00062\b\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0002J\u0017\u0010\u001a\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00192\u0006\u0010\u0018\u001a\u00020\u0003H\u0096\u0001J\u0013\u0010\u001d\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u001b\u001a\u00020\u0003H\u0096\u0001J\u0016\u0010\u001e\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u000eJ\u0016\u0010\"\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u000eJ6\u0010&\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010$\u001a\u00020#2\u0006\u0010%\u001a\u00020#H\u0096@¢\u0006\u0004\b&\u0010'J.\u0010&\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\u0006\u0010%\u001a\u00020#H\u0096@¢\u0006\u0004\b&\u0010(J\u001e\u0010)\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010%\u001a\u00020#H\u0096@¢\u0006\u0004\b)\u0010*J\u0018\u0010,\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010+\u001a\u00020\u0006H\u0016J\u001e\u0010.\u001a\b\u0012\u0004\u0012\u00020-0\n2\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0003H\u0016J\u000e\u0010/\u001a\b\u0012\u0004\u0012\u00020-0\nH\u0016J)\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\b¢\u0006\u0004\b\u0014\u00100J\u000e\u00103\u001a\b\u0012\u0004\u0012\u00020201H\u0016J\u000e\u00105\u001a\b\u0012\u0004\u0012\u00020401H\u0016J\u000e\u00106\u001a\b\u0012\u0004\u0012\u00020-01H\u0016J\u0010\u00109\u001a\u00020\u00102\u0006\u00108\u001a\u000207H\u0016J\u0010\u0010<\u001a\u00020\u00102\u0006\u0010;\u001a\u00020:H\u0016J\u0010\u0010=\u001a\u00020\u00102\u0006\u0010;\u001a\u00020:H\u0016J\b\u0010>\u001a\u00020\u0010H\u0016R\u0014\u0010@\u001a\u00020?8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b@\u0010AR\u0014\u0010C\u001a\u00020B8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bC\u0010DR>\u0010H\u001a,\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b0F\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0G0E8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bH\u0010IR*\u0010K\u001a\u0016\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0010\u0018\u00010J8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bK\u0010LR*\u0010M\u001a\u0016\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u0010\u0018\u00010J8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bM\u0010LR \u0010O\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000b0N8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bO\u0010PR \u0010R\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020Q0N8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bR\u0010PR\u001a\u0010T\u001a\b\u0012\u0004\u0012\u00020S018\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bT\u0010UR\u0016\u0010V\u001a\u00020\u00068\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bV\u0010WR8\u0010[\u001a&\u0012\f\u0012\n Y*\u0004\u0018\u00010\u00030\u0003 Y*\u0012\u0012\f\u0012\n Y*\u0004\u0018\u00010\u00030\u0003\u0018\u00010Z0X8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b[\u0010\\¨\u0006c"}, d2 = {"Lcom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl;", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/IFeedPublishServiceV2;", "", "", "guild", WadlProxyConsts.CHANNEL, "", "forceDB", "", "feedScope", "", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/b;", "queryFeedPublishStateV2s", "(Ljava/lang/String;Ljava/lang/String;ZILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Lcom/tencent/mobileqq/guild/feed/publish/bean/GuildTaskProgressState;", "progressState", "", "sequentiallyRetryPostFeedTaskWithTimeout", "(Lcom/tencent/mobileqq/guild/feed/publish/bean/GuildTaskProgressState;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "guildId", "invalidateCache", "onQuitGuild", "isNewFeed", "stateV2FromProgressState", "fileId", "Lcom/tencent/mobileqq/guild/data/publish/taskx/api/a;", "findFileTransferModel", "mainTaskId", "Lcom/tencent/mobileqq/guild/feed/publish/api/d;", "getPostFeedMainTaskFacade", "innerOnPublishBegin", "Lcom/tencent/mobileqq/guild/feed/publish/event/GuildPublishSuccessGetFeedEvent;", "event", "state", "innerOnPublishSuccess", "", "topicId", "queryTimeout", "queryFailPublishStateV2s", "(Ljava/lang/String;Ljava/lang/String;JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Ljava/lang/String;Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "queryGlobalFeedFailPublishStateV2s", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "limitUploadSize", "activePublishingFeed", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/a$b;", "finishedFeedPublishState", "finishedGlobalFeedPublishState", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;)V", "Lkotlinx/coroutines/flow/Flow;", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/a$a;", "publishBeginFlow", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/a$c;", "publishUpdateFlow", "publishEndFlow", "Lmqq/app/AppRuntime;", "appRuntime", "onCreate", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/AbsFeedEditorEventListener;", "listener", "registerEventListener", "unregisterEventListener", MosaicConstants$JsFunction.FUNC_ON_DESTROY, "Lkotlinx/coroutines/CoroutineScope;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "Lcom/tencent/mobileqq/guild/config/GuildFeedAutoResendConfigParser$Data;", DownloadInfo.spKey_Config, "Lcom/tencent/mobileqq/guild/config/GuildFeedAutoResendConfigParser$Data;", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/impl/AsyncDataMemoryStorage;", "Lkotlin/Triple;", "Lkotlinx/coroutines/Deferred;", "memoryStorage", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/impl/AsyncDataMemoryStorage;", "Lkotlin/Function2;", "_publishBeginEvent", "Lkotlin/jvm/functions/Function2;", "_publishSuccessEvent", "Ljava/util/concurrent/ConcurrentHashMap;", "sendingStatesMap", "Ljava/util/concurrent/ConcurrentHashMap;", "Lcom/tencent/mobileqq/guild/feed/event/FeedEditorEvent$OnStartPublishFeedEvent;", "sendingStatesExtMap", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/a;", "publishEvents", "Lkotlinx/coroutines/flow/Flow;", "firstActivePublishing", "Z", "", "kotlin.jvm.PlatformType", "", "retryingTaskIds", "Ljava/util/Set;", "<init>", "()V", "Companion", "a", "Holder", "b", "qqguild_feed_impl_release"}, k = 1, mv = {1, 9, 0})
@SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl\n+ 2 SimpleEventBusExt.kt\ncom/tencent/mobileqq/guild/feed/event/bus/SimpleEventBusExtKt\n+ 3 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n+ 4 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt\n+ 5 SafeCollector.common.kt\nkotlinx/coroutines/flow/internal/SafeCollector_commonKt\n+ 6 Logger.kt\ncom/tencent/mobileqq/guild/util/Logger\n+ 7 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 8 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 9 RuntimeServiceEx.kt\ncom/tencent/guild/aio/util/RuntimeServiceExKt\n*L\n1#1,609:1\n18#2:610\n34#2:611\n18#2:622\n34#2:623\n18#2:696\n34#2:697\n47#3:612\n49#3:616\n54#3:617\n57#3:621\n20#3:624\n22#3:628\n54#3:629\n57#3:633\n47#3:634\n49#3:638\n47#3:639\n49#3:643\n35#3:678\n20#3:679\n22#3:683\n35#3:684\n20#3:685\n22#3:689\n35#3:690\n20#3:691\n22#3:695\n50#4:613\n55#4:615\n50#4:618\n55#4:620\n50#4:625\n55#4:627\n50#4:630\n55#4:632\n50#4:635\n55#4:637\n50#4:640\n55#4:642\n50#4:680\n55#4:682\n50#4:686\n55#4:688\n50#4:692\n55#4:694\n106#5:614\n106#5:619\n106#5:626\n106#5:631\n106#5:636\n106#5:641\n106#5:681\n106#5:687\n106#5:693\n22#6,2:644\n22#6,2:646\n22#6,2:661\n181#6,7:663\n188#6,2:671\n190#6:674\n22#6,2:676\n766#7:648\n857#7,2:649\n1655#7,8:651\n1855#7,2:659\n1855#7:670\n1856#7:673\n1#8:675\n14#9:698\n14#9:699\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl\n*L\n129#1:610\n129#1:611\n148#1:622\n148#1:623\n458#1:696\n458#1:697\n131#1:612\n131#1:616\n143#1:617\n143#1:621\n148#1:624\n148#1:628\n150#1:629\n150#1:633\n153#1:634\n153#1:638\n173#1:639\n173#1:643\n445#1:678\n445#1:679\n445#1:683\n449#1:684\n449#1:685\n449#1:689\n453#1:690\n453#1:691\n453#1:695\n131#1:613\n131#1:615\n143#1:618\n143#1:620\n148#1:625\n148#1:627\n150#1:630\n150#1:632\n153#1:635\n153#1:637\n173#1:640\n173#1:642\n445#1:680\n445#1:682\n449#1:686\n449#1:688\n453#1:692\n453#1:694\n131#1:614\n143#1:619\n148#1:626\n150#1:631\n153#1:636\n173#1:641\n445#1:681\n449#1:687\n453#1:693\n198#1:644,2\n209#1:646,2\n335#1:661,2\n337#1:663,7\n337#1:671,2\n337#1:674\n418#1:676,2\n219#1:648\n219#1:649,2\n232#1:651,8\n256#1:659,2\n337#1:670\n337#1:673\n481#1:698\n485#1:699\n*E\n"})
public final class FeedPublishServiceV2Impl implements IFeedPublishServiceV2 {
    static IPatchRedirector $redirector_ = null;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE;

    @NotNull
    private static final String TAG = "FeedPublishServiceV2Impl";
    private final /* synthetic */ a $$delegate_0;

    @Nullable
    private Function2<? super Boolean, ? super GuildTaskProgressState, Unit> _publishBeginEvent;

    @Nullable
    private Function2<? super GuildPublishSuccessGetFeedEvent, ? super GuildTaskProgressState, Unit> _publishSuccessEvent;

    @NotNull
    private final GuildFeedAutoResendConfigParser.Data config;
    private boolean firstActivePublishing;

    @NotNull
    private final AsyncDataMemoryStorage<Triple<String, String, Integer>, Deferred<List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>>> memoryStorage;

    @NotNull
    private final Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a> publishEvents;
    private final Set<String> retryingTaskIds;

    @NotNull
    private final CoroutineScope scope;

    @NotNull
    private final ConcurrentHashMap<String, FeedEditorEvent.OnStartPublishFeedEvent> sendingStatesExtMap;

    @NotNull
    private final ConcurrentHashMap<String, com.tencent.mobileqq.guild.feed.publish.publishV2.b> sendingStatesMap;

    /* JADX INFO: loaded from: classes22.dex */
    @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$Holder\n+ 2 Handler.kt\nandroidx/core/os/HandlerKt\n*L\n1#1,609:1\n35#2,10:610\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$Holder\n*L\n558#1:610,10\n*E\n"})
    public static final class Holder {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @NotNull
        public static final Holder f261272a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @NotNull
        private static final Handler f261273b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        @NotNull
        private static final ConcurrentHashMap<Triple<String, String, Integer>, List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b>> f261274c;

        static {
            IPatchRedirector redirector = PatchRedirectCenter.getRedirector(133062);
            $redirector_ = redirector;
            if (redirector != null && redirector.hasPatch((short) 5)) {
                redirector.redirect((short) 5);
                return;
            }
            f261272a = new Holder();
            f261273b = new Handler(Looper.getMainLooper());
            f261274c = new ConcurrentHashMap<>();
        }

        Holder() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this);
        }

        public final void a(@NotNull com.tencent.mobileqq.guild.feed.publish.publishV2.a.b finish) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) finish);
                return;
            }
            Intrinsics.checkNotNullParameter(finish, "finish");
            int scope = finish.a().e().getScope();
            String guildId = finish.a().e().getGuildId();
            String channelId = finish.a().e().getChannelId();
            String strC = finish.a().c();
            boolean z15 = scope == 2;
            QLog.i(FeedPublishServiceV2Impl.TAG, 1, "Holder.add: mainTaskId=" + strC + " guildId=" + guildId + " channelId=" + channelId + " feedScope=" + scope + " isGlobalFeed=" + z15);
            ConcurrentHashMap<Triple<String, String, Integer>, List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b>> concurrentHashMap = f261274c;
            if (z15) {
                Triple<String, String, Integer> triple = new Triple<>("", "", Integer.valueOf(scope));
                List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> listEmptyList = concurrentHashMap.get(triple);
                if (listEmptyList == null) {
                    listEmptyList = CollectionsKt.emptyList();
                } else {
                    Intrinsics.checkNotNull(listEmptyList);
                }
                concurrentHashMap.put(triple, CollectionsKt.plus((Collection<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.a.b>) listEmptyList, finish));
            } else {
                Triple<String, String, Integer> triple2 = new Triple<>(guildId, channelId, Integer.valueOf(scope));
                List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> listEmptyList2 = concurrentHashMap.get(triple2);
                if (listEmptyList2 == null) {
                    listEmptyList2 = CollectionsKt.emptyList();
                } else {
                    Intrinsics.checkNotNull(listEmptyList2);
                }
                concurrentHashMap.put(triple2, CollectionsKt.plus((Collection<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.a.b>) listEmptyList2, finish));
                Triple<String, String, Integer> triple3 = new Triple<>(guildId, "", Integer.valueOf(scope));
                List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> listEmptyList3 = concurrentHashMap.get(triple3);
                if (listEmptyList3 == null) {
                    listEmptyList3 = CollectionsKt.emptyList();
                } else {
                    Intrinsics.checkNotNull(listEmptyList3);
                }
                concurrentHashMap.put(triple3, CollectionsKt.plus((Collection<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.a.b>) listEmptyList3, finish));
            }
            QLog.i(FeedPublishServiceV2Impl.TAG, 1, "Holder.add: finishedMap size=" + concurrentHashMap.size());
            Handler handler = f261273b;
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(new Runnable() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$Holder$add$$inlined$postDelayed$default$1
                static IPatchRedirector $redirector_;

                {
                    IPatchRedirector iPatchRedirector2 = $redirector_;
                    if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector2.redirect((short) 1, (Object) this);
                }

                @Override // java.lang.Runnable
                public final void run() {
                    IPatchRedirector iPatchRedirector2 = $redirector_;
                    if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 2)) {
                        FeedPublishServiceV2Impl.Holder.f261272a.b();
                    } else {
                        iPatchRedirector2.redirect((short) 2, (Object) this);
                    }
                }
            }, 3000L);
        }

        public final void b() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
                iPatchRedirector.redirect((short) 3, (Object) this);
                return;
            }
            ConcurrentHashMap<Triple<String, String, Integer>, List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b>> concurrentHashMap = f261274c;
            QLog.i(FeedPublishServiceV2Impl.TAG, 1, "Holder.clear: clearing finishedMap size=" + concurrentHashMap.size());
            concurrentHashMap.clear();
        }

        @NotNull
        public final List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> c(@NotNull String guild2, @NotNull String channel, int i15) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 4)) {
                return (List) iPatchRedirector.redirect((short) 4, this, guild2, channel, Integer.valueOf(i15));
            }
            Intrinsics.checkNotNullParameter(guild2, "guild");
            Intrinsics.checkNotNullParameter(channel, "channel");
            boolean z15 = i15 == 2;
            Integer numValueOf = Integer.valueOf(i15);
            List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> listEmptyList = f261274c.get(z15 ? new Triple("", "", numValueOf) : new Triple(guild2, channel, numValueOf));
            if (listEmptyList == null) {
                listEmptyList = CollectionsKt.emptyList();
            }
            QLog.i(FeedPublishServiceV2Impl.TAG, 1, "Holder.get: guild=" + guild2 + " channel=" + channel + " feedScope=" + i15 + " isGlobalFeed=" + z15 + " result size=" + listEmptyList.size());
            return listEmptyList;
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$a, reason: from kotlin metadata */
    @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$Companion\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,609:1\n766#2:610\n857#2,2:611\n1#3:613\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$Companion\n*L\n605#1:610\n605#1:611,2\n*E\n"})
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

        public static final long a(Companion companion, GuildTaskProgressState guildTaskProgressState) {
            companion.getClass();
            List<GuildMediaProgressState> mediaProgressStates = guildTaskProgressState.getMediaProgressStates();
            Intrinsics.checkNotNullExpressionValue(mediaProgressStates, "getMediaProgressStates(...)");
            ArrayList arrayList = new ArrayList();
            for (Object obj : mediaProgressStates) {
                if (((GuildMediaProgressState) obj).getState() != 3) {
                    arrayList.add(obj);
                }
            }
            Iterator it = arrayList.iterator();
            long jY = 0;
            while (it.hasNext()) {
                jY += en.y(((GuildMediaProgressState) it.next()).getPath());
            }
            return jY;
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$activePublishingFeed$3, reason: invalid class name */
    /* JADX INFO: loaded from: classes22.dex */
    @Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "<anonymous>"}, k = 3, mv = {1, 9, 0})
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$activePublishingFeed$3", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {613, e0.CTRL_INDEX, 390}, m = "invokeSuspend", n = {}, s = {})
    @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$activePublishingFeed$3\n+ 2 ext.kt\ncom/tencent/mobileqq/guild/feed/part/ExtKt\n+ 3 Logger.kt\ncom/tencent/mobileqq/guild/util/Logger\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,609:1\n52#2,4:610\n22#3,2:614\n22#3,2:617\n22#3,2:619\n22#3,2:621\n181#3,7:623\n188#3,2:631\n190#3:634\n22#3,2:635\n1855#4:616\n1855#4:630\n1856#4:633\n1856#4:637\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$activePublishingFeed$3\n*L\n354#1:610,4\n357#1:614,2\n368#1:617,2\n373#1:619,2\n378#1:621,2\n383#1:623,7\n383#1:631,2\n383#1:634\n388#1:635,2\n359#1:616\n383#1:630\n383#1:633\n359#1:637\n*E\n"})
    static final class AnonymousClass3 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        static IPatchRedirector $redirector_;
        final /* synthetic */ Job $job;
        final /* synthetic */ boolean $limitUploadSize;
        final /* synthetic */ String $queryId;
        Object L$0;
        Object L$1;
        boolean Z$0;
        int label;
        final /* synthetic */ FeedPublishServiceV2Impl this$0;

        /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$activePublishingFeed$3$a */
        static final class a<T> implements GuildDbDataHelper.a {
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ Continuation<List<GuildTaskProgressState>> f261275a;

            /* JADX WARN: Multi-variable type inference failed */
            a(Continuation<? super List<GuildTaskProgressState>> continuation) {
                this.f261275a = continuation;
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector.redirect((short) 1, (Object) this, (Object) continuation);
            }

            @Override // com.tencent.mobileqq.guild.feed.publish.helper.GuildDbDataHelper.a
            public void onResult(Object obj) {
                List list = (List) obj;
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) {
                    this.f261275a.resumeWith(Result.m747constructorimpl(list));
                } else {
                    iPatchRedirector.redirect((short) 2, (Object) this, (Object) list);
                }
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Job job, String str, FeedPublishServiceV2Impl feedPublishServiceV2Impl, boolean z15, Continuation<? super AnonymousClass3> continuation) {
            super(2, continuation);
            this.$job = job;
            this.$queryId = str;
            this.this$0 = feedPublishServiceV2Impl;
            this.$limitUploadSize = z15;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, job, str, feedPublishServiceV2Impl, Boolean.valueOf(z15), continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? new AnonymousClass3(this.$job, this.$queryId, this.this$0, this.$limitUploadSize, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
        }

        /* JADX WARN: Code duplicated, block: B:27:0x00b9  */
        /* JADX WARN: Code duplicated, block: B:29:0x00ce  */
        /* JADX WARN: Code duplicated, block: B:31:0x00d4  */
        /* JADX WARN: Code duplicated, block: B:33:0x00e0  */
        /* JADX WARN: Code duplicated, block: B:38:0x00f7  */
        /* JADX WARN: Code duplicated, block: B:41:0x00fe  */
        /* JADX WARN: Code duplicated, block: B:42:0x0100  */
        /* JADX WARN: Code duplicated, block: B:44:0x010a  */
        /* JADX WARN: Code duplicated, block: B:47:0x0112  */
        /* JADX WARN: Code duplicated, block: B:49:0x0115  */
        /* JADX WARN: Code duplicated, block: B:50:0x012e  */
        /* JADX WARN: Code duplicated, block: B:52:0x0138  */
        /* JADX WARN: Code duplicated, block: B:53:0x013d  */
        /* JADX WARN: Code duplicated, block: B:56:0x0152  */
        /* JADX WARN: Code duplicated, block: B:57:0x016c  */
        /* JADX WARN: Code duplicated, block: B:60:0x017c  */
        /* JADX WARN: Code duplicated, block: B:61:0x0195  */
        /* JADX WARN: Code duplicated, block: B:63:0x01a7 A[ADDED_TO_REGION] */
        /* JADX WARN: Code duplicated, block: B:75:0x023a A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:76:0x023b  */
        /* JADX WARN: Code duplicated, block: B:79:0x0259 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:80:0x025a  */
        /* JADX WARN: Code duplicated, block: B:84:0x0267 A[SYNTHETIC] */
        /* JADX WARN: Code duplicated, block: B:85:0x01fd A[ADDED_TO_REGION, EDGE_INSN: B:85:0x01fd->B:73:0x01fd BREAK  A[LOOP:0: B:25:0x00b3->B:72:0x01f7], REMOVE, SYNTHETIC] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:80:0x025a -> B:81:0x025f). Please report as a decompilation issue!!! */
        /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached at block B:60:0x017c
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
            	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
            	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
            */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @org.jetbrains.annotations.Nullable
        public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r21) {
            /*
                Method dump skipped, instruction units count: 623
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl.AnonymousClass3.invokeSuspend(java.lang.Object):java.lang.Object");
        }

        @Override // kotlin.jvm.functions.Function2
        @Nullable
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
        public final Object mo4invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Unit> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((AnonymousClass3) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
        }
    }

    @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$OnQuitGuildObserver\n+ 2 Logger.kt\ncom/tencent/mobileqq/guild/util/Logger\n*L\n1#1,609:1\n22#2,2:610\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$OnQuitGuildObserver\n*L\n597#1:610,2\n*E\n"})
    public static final class b extends GPServiceObserver {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        @NotNull
        public static final b f261276d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        @Nullable
        private static IGProGlobalService f261277e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        @Nullable
        private static Function1<? super String, Unit> f261278f;

        static {
            IPatchRedirector redirector = PatchRedirectCenter.getRedirector(133063);
            $redirector_ = redirector;
            if (redirector == null || !redirector.hasPatch((short) 7)) {
                f261276d = new b();
            } else {
                redirector.redirect((short) 7);
            }
        }

        b() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this);
        }

        private final void c(String str) {
            if (str == null || str.length() == 0) {
                return;
            }
            Logger.f281727a.d().i(FeedPublishServiceV2Impl.TAG, 1, "innerDelete: " + str);
            Function1<? super String, Unit> function1 = f261278f;
            if (function1 != null) {
                function1.invoke(str);
            }
        }

        public final void b() {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
                iPatchRedirector.redirect((short) 3, (Object) this);
                return;
            }
            IGProGlobalService iGProGlobalService = f261277e;
            if (iGProGlobalService != null) {
                iGProGlobalService.deleteObserver(this);
            }
        }

        public final void d(@NotNull AppRuntime appRuntime, @NotNull Function1<? super String, Unit> cb5) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                iPatchRedirector.redirect((short) 2, (Object) this, (Object) appRuntime, (Object) cb5);
                return;
            }
            Intrinsics.checkNotNullParameter(appRuntime, "appRuntime");
            Intrinsics.checkNotNullParameter(cb5, "cb");
            f261278f = cb5;
            IGProGlobalService iGProGlobalService = (IGProGlobalService) appRuntime.getRuntimeService(IGProGlobalService.class, "");
            f261277e = iGProGlobalService;
            if (iGProGlobalService != null) {
                iGProGlobalService.addObserver(this);
            }
        }

        @Override // com.tencent.mobileqq.qqguildsdk.observer.GPServiceObserver
        protected void onBeKickFromGuild(@Nullable String str, int i15) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 5)) {
                c(str);
            } else {
                iPatchRedirector.redirect((short) 5, (Object) this, (Object) str, i15);
            }
        }

        @Override // com.tencent.mobileqq.qqguildsdk.observer.GPServiceObserver
        protected void onDestoryGuild(@Nullable String str) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 6)) {
                c(str);
            } else {
                iPatchRedirector.redirect((short) 6, (Object) this, (Object) str);
            }
        }

        @Override // com.tencent.mobileqq.qqguildsdk.observer.GPServiceObserver
        protected void onRemoveGuild(@Nullable String str) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) {
                c(str);
            } else {
                iPatchRedirector.redirect((short) 4, (Object) this, (Object) str);
            }
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$onCreate$1, reason: invalid class name and case insensitive filesystem */
    @Metadata(d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0003\u001a\u00020\u00022\u0006\u0010\u0001\u001a\u00020\u0000H\u008a@"}, d2 = {"Lcom/tencent/mobileqq/guild/feed/event/GuildFeedDeleteEvent;", "event", "", "<anonymous>"}, k = 3, mv = {1, 9, 0})
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$onCreate$1", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$onCreate$1\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,609:1\n288#2,2:610\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$onCreate$1\n*L\n459#1:610,2\n*E\n"})
    static final class C94481 extends SuspendLambda implements Function2<GuildFeedDeleteEvent, Continuation<? super Unit>, Object> {
        static IPatchRedirector $redirector_;
        /* synthetic */ Object L$0;
        int label;

        C94481(Continuation<? super C94481> continuation) {
            super(2, continuation);
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) FeedPublishServiceV2Impl.this, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
                return (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
            }
            C94481 c94481 = FeedPublishServiceV2Impl.this.new C94481(continuation);
            c94481.L$0 = obj;
            return c94481;
        }

        @Override // kotlin.jvm.functions.Function2
        @Nullable
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
        public final Object mo4invoke(@NotNull GuildFeedDeleteEvent guildFeedDeleteEvent, @Nullable Continuation<? super Unit> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((C94481) create(guildFeedDeleteEvent, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) guildFeedDeleteEvent, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            Object next;
            String str;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            GuildFeedDeleteEvent guildFeedDeleteEvent = (GuildFeedDeleteEvent) this.L$0;
            Set setEntrySet = FeedPublishServiceV2Impl.this.sendingStatesMap.entrySet();
            Intrinsics.checkNotNullExpressionValue(setEntrySet, "<get-entries>(...)");
            Iterator it = setEntrySet.iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!Intrinsics.areEqual(((com.tencent.mobileqq.guild.feed.publish.publishV2.b) ((Map.Entry) next).getValue()).d().idd, guildFeedDeleteEvent.getFeedId()));
            Map.Entry entry = (Map.Entry) next;
            if (entry != null && (str = (String) entry.getKey()) != null) {
                FeedPublishServiceV2Impl feedPublishServiceV2Impl = FeedPublishServiceV2Impl.this;
                feedPublishServiceV2Impl.sendingStatesMap.remove(str);
            }
            Holder.f261272a.b();
            FeedPublishServiceV2Impl feedPublishServiceV2Impl2 = FeedPublishServiceV2Impl.this;
            String guildId = guildFeedDeleteEvent.getGuildId();
            Intrinsics.checkNotNullExpressionValue(guildId, "getGuildId(...)");
            String channelId = guildFeedDeleteEvent.getChannelId();
            Intrinsics.checkNotNullExpressionValue(channelId, "getChannelId(...)");
            FeedPublishServiceV2Impl.invalidateCache$default(feedPublishServiceV2Impl2, guildId, channelId, null, 4, null);
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$onCreate$2, reason: invalid class name */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    /* synthetic */ class AnonymousClass2 extends FunctionReferenceImpl implements Function1<String, Unit> {
        static IPatchRedirector $redirector_;

        AnonymousClass2(Object obj) {
            super(1, obj, FeedPublishServiceV2Impl.class, "onQuitGuild", "onQuitGuild(Ljava/lang/String;)V", 0);
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, obj);
        }

        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(String str) {
            invoke2(str);
            return Unit.INSTANCE;
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final void invoke2(@NotNull String p15) {
            Intrinsics.checkNotNullParameter(p15, "p0");
            ((FeedPublishServiceV2Impl) this.receiver).onQuitGuild(p15);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$onQuitGuild$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes22.dex */
    @Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "<anonymous>"}, k = 3, mv = {1, 9, 0})
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$onQuitGuild$1", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {471}, m = "invokeSuspend", n = {}, s = {})
    @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$onQuitGuild$1\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,609:1\n1603#2,9:610\n1855#2:619\n1856#2:621\n1612#2:622\n1#3:620\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$onQuitGuild$1\n*L\n471#1:610,9\n471#1:619\n471#1:621\n471#1:622\n471#1:620\n*E\n"})
    static final class C94491 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        static IPatchRedirector $redirector_;
        final /* synthetic */ String $guild;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C94491(String str, Continuation<? super C94491> continuation) {
            super(2, continuation);
            this.$guild = str;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, FeedPublishServiceV2Impl.this, str, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? FeedPublishServiceV2Impl.this.new C94491(this.$guild, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i15 = this.label;
            if (i15 == 0) {
                ResultKt.throwOnFailure(obj);
                FeedPublishServiceV2Impl feedPublishServiceV2Impl = FeedPublishServiceV2Impl.this;
                String str = this.$guild;
                this.label = 1;
                obj = feedPublishServiceV2Impl.queryFeedPublishStateV2s(str, "", true, 0, this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i15 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            ArrayList arrayList = new ArrayList();
            Iterator it = ((Iterable) obj).iterator();
            while (it.hasNext()) {
                String mainTaskId = ((com.tencent.mobileqq.guild.feed.publish.publishV2.b) it.next()).e().getMainTaskId();
                if (mainTaskId != null) {
                    arrayList.add(mainTaskId);
                }
            }
            GuildTaskProgressBoxHelper.g().l(arrayList);
            GuildDbDataHelper.o().n(arrayList);
            FeedPublishServiceV2Impl.this.invalidateCache(this.$guild);
            return Unit.INSTANCE;
        }

        @Override // kotlin.jvm.functions.Function2
        @Nullable
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
        public final Object mo4invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Unit> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((C94491) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes12.dex */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {272}, m = "queryFailPublishStateV2s", n = {}, s = {})
    static final class C94501 extends ContinuationImpl {
        static IPatchRedirector $redirector_;
        int label;
        /* synthetic */ Object result;

        C94501(Continuation<? super C94501> continuation) {
            super(continuation);
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) FeedPublishServiceV2Impl.this, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FeedPublishServiceV2Impl.this.queryFailPublishStateV2s(null, null, 0L, 0L, this);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes22.dex */
    @Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/b;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {273}, m = "invokeSuspend", n = {}, s = {})
    static final class C94512 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>>, Object> {
        static IPatchRedirector $redirector_;
        final /* synthetic */ String $channel;
        final /* synthetic */ String $guild;
        final /* synthetic */ long $topicId;
        int label;
        final /* synthetic */ FeedPublishServiceV2Impl this$0;

        /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$2$1, reason: invalid class name */
        @Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/b;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
        @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$2$1", f = "FeedPublishServiceV2Impl.kt", i = {0}, l = {276}, m = "invokeSuspend", n = {"entries"}, s = {"L$0"})
        @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$queryFailPublishStateV2s$2$1\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,609:1\n766#2:610\n857#2,2:611\n766#2:613\n857#2,2:614\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$queryFailPublishStateV2s$2$1\n*L\n277#1:610\n277#1:611,2\n278#1:613\n278#1:614,2\n*E\n"})
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>>, Object> {
            static IPatchRedirector $redirector_;
            final /* synthetic */ String $channel;
            final /* synthetic */ String $guild;
            final /* synthetic */ long $topicId;
            Object L$0;
            int label;
            final /* synthetic */ FeedPublishServiceV2Impl this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(long j15, FeedPublishServiceV2Impl feedPublishServiceV2Impl, String str, String str2, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.$topicId = j15;
                this.this$0 = feedPublishServiceV2Impl;
                this.$guild = str;
                this.$channel = str2;
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector.redirect((short) 1, this, Long.valueOf(j15), feedPublishServiceV2Impl, str, str2, continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @NotNull
            public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
                IPatchRedirector iPatchRedirector = $redirector_;
                return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? new AnonymousClass1(this.$topicId, this.this$0, this.$guild, this.$channel, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Object mo4invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
                return invoke2(coroutineScope, (Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>>) continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @Nullable
            public final Object invokeSuspend(@NotNull Object obj) {
                Set<String> set;
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                    return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                }
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                int i15 = this.label;
                if (i15 == 0) {
                    ResultKt.throwOnFailure(obj);
                    Set<String> setA = com.tencent.mobileqq.guild.feed.topic.storage.b.f262181a.a(this.$topicId).a();
                    FeedPublishServiceV2Impl feedPublishServiceV2Impl = this.this$0;
                    String str = this.$guild;
                    String str2 = this.$channel;
                    this.L$0 = setA;
                    this.label = 1;
                    Object objQueryFeedPublishStateV2s = feedPublishServiceV2Impl.queryFeedPublishStateV2s(str, str2, false, 0, this);
                    if (objQueryFeedPublishStateV2s == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    set = setA;
                    obj = objQueryFeedPublishStateV2s;
                } else {
                    if (i15 != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    set = (Set) this.L$0;
                    ResultKt.throwOnFailure(obj);
                }
                ArrayList arrayList = new ArrayList();
                for (Object obj2 : (Iterable) obj) {
                    if (set.contains(((com.tencent.mobileqq.guild.feed.publish.publishV2.b) obj2).c())) {
                        arrayList.add(obj2);
                    }
                }
                ArrayList arrayList2 = new ArrayList();
                for (Object obj3 : arrayList) {
                    if (((com.tencent.mobileqq.guild.feed.publish.publishV2.b) obj3).e().getScope() != 2) {
                        arrayList2.add(obj3);
                    }
                }
                return arrayList2;
            }

            @Nullable
            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final Object invoke2(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
                IPatchRedirector iPatchRedirector = $redirector_;
                return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C94512(long j15, FeedPublishServiceV2Impl feedPublishServiceV2Impl, String str, String str2, Continuation<? super C94512> continuation) {
            super(2, continuation);
            this.$topicId = j15;
            this.this$0 = feedPublishServiceV2Impl;
            this.$guild = str;
            this.$channel = str2;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, Long.valueOf(j15), feedPublishServiceV2Impl, str, str2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? new C94512(this.$topicId, this.this$0, this.$guild, this.$channel, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        /* JADX INFO: renamed from: invoke */
        public /* bridge */ /* synthetic */ Object mo4invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
            return invoke2(coroutineScope, (Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>>) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) throws Throwable {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i15 = this.label;
            if (i15 == 0) {
                ResultKt.throwOnFailure(obj);
                CoroutineDispatcher coroutineDispatcherA = ai.a().a();
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.$topicId, this.this$0, this.$guild, this.$channel, null);
                this.label = 1;
                obj = BuildersKt.withContext(coroutineDispatcherA, anonymousClass1, this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i15 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return obj;
        }

        @Nullable
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((C94512) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$4, reason: invalid class name */
    /* JADX INFO: loaded from: classes12.dex */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {291}, m = "queryFailPublishStateV2s", n = {}, s = {})
    static final class AnonymousClass4 extends ContinuationImpl {
        static IPatchRedirector $redirector_;
        int label;
        /* synthetic */ Object result;

        AnonymousClass4(Continuation<? super AnonymousClass4> continuation) {
            super(continuation);
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) FeedPublishServiceV2Impl.this, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FeedPublishServiceV2Impl.this.queryFailPublishStateV2s(null, null, 0L, this);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$5, reason: invalid class name */
    /* JADX INFO: loaded from: classes22.dex */
    @Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/b;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$5", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {292}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass5 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>>, Object> {
        static IPatchRedirector $redirector_;
        final /* synthetic */ String $channel;
        final /* synthetic */ String $guild;
        final /* synthetic */ long $joinGuildTime;
        int label;

        /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$5$1, reason: invalid class name */
        @Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/b;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
        @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFailPublishStateV2s$5$1", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {293}, m = "invokeSuspend", n = {}, s = {})
        @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$queryFailPublishStateV2s$5$1\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,609:1\n766#2:610\n857#2,2:611\n1549#2:613\n1620#2,3:614\n766#2:617\n857#2,2:618\n766#2:620\n857#2,2:621\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$queryFailPublishStateV2s$5$1\n*L\n296#1:610\n296#1:611,2\n297#1:613\n297#1:614,3\n300#1:617\n300#1:618,2\n301#1:620\n301#1:621,2\n*E\n"})
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>>, Object> {
            static IPatchRedirector $redirector_;
            final /* synthetic */ String $channel;
            final /* synthetic */ String $guild;
            final /* synthetic */ long $joinGuildTime;
            int label;
            final /* synthetic */ FeedPublishServiceV2Impl this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(FeedPublishServiceV2Impl feedPublishServiceV2Impl, String str, String str2, long j15, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = feedPublishServiceV2Impl;
                this.$guild = str;
                this.$channel = str2;
                this.$joinGuildTime = j15;
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector.redirect((short) 1, this, feedPublishServiceV2Impl, str, str2, Long.valueOf(j15), continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @NotNull
            public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
                IPatchRedirector iPatchRedirector = $redirector_;
                return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? new AnonymousClass1(this.this$0, this.$guild, this.$channel, this.$joinGuildTime, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Object mo4invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
                return invoke2(coroutineScope, (Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>>) continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @Nullable
            public final Object invokeSuspend(@NotNull Object obj) {
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                    return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                }
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                int i15 = this.label;
                if (i15 == 0) {
                    ResultKt.throwOnFailure(obj);
                    FeedPublishServiceV2Impl feedPublishServiceV2Impl = this.this$0;
                    String str = this.$guild;
                    String str2 = this.$channel;
                    this.label = 1;
                    obj = feedPublishServiceV2Impl.queryFeedPublishStateV2s(str, str2, false, 0, this);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    if (i15 != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ResultKt.throwOnFailure(obj);
                }
                FeedPublishServiceV2Impl feedPublishServiceV2Impl2 = this.this$0;
                ArrayList arrayList = new ArrayList();
                Iterator it = ((List) obj).iterator();
                while (true) {
                    boolean z15 = false;
                    if (!it.hasNext()) {
                        break;
                    }
                    Object next = it.next();
                    com.tencent.mobileqq.guild.feed.publish.publishV2.b bVar = (com.tencent.mobileqq.guild.feed.publish.publishV2.b) next;
                    if (d.c(bVar.e()) && Companion.a(FeedPublishServiceV2Impl.INSTANCE, bVar.e()) > feedPublishServiceV2Impl2.config.getUploadLimitByte()) {
                        z15 = true;
                    }
                    if (z15) {
                        arrayList.add(next);
                    }
                }
                ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList, 10));
                Iterator it4 = arrayList.iterator();
                while (it4.hasNext()) {
                    arrayList2.add(((com.tencent.mobileqq.guild.feed.publish.publishV2.b) it4.next()).e());
                }
                n.i().e(arrayList2);
                ArrayList arrayList3 = new ArrayList();
                for (Object obj2 : (Iterable) obj) {
                    if (((com.tencent.mobileqq.guild.feed.publish.publishV2.b) obj2).e().getScope() != 2) {
                        arrayList3.add(obj2);
                    }
                }
                long j15 = this.$joinGuildTime;
                ArrayList arrayList4 = new ArrayList();
                for (Object obj3 : arrayList3) {
                    long j16 = 1000;
                    if (((((com.tencent.mobileqq.guild.feed.publish.publishV2.b) obj3).e().getTriggerPublishTimeNs() / j16) / j16) / j16 > j15) {
                        arrayList4.add(obj3);
                    }
                }
                return CollectionsKt.take(arrayList4, 50);
            }

            @Nullable
            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final Object invoke2(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
                IPatchRedirector iPatchRedirector = $redirector_;
                return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass5(String str, String str2, long j15, Continuation<? super AnonymousClass5> continuation) {
            super(2, continuation);
            this.$guild = str;
            this.$channel = str2;
            this.$joinGuildTime = j15;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, FeedPublishServiceV2Impl.this, str, str2, Long.valueOf(j15), continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? FeedPublishServiceV2Impl.this.new AnonymousClass5(this.$guild, this.$channel, this.$joinGuildTime, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        /* JADX INFO: renamed from: invoke */
        public /* bridge */ /* synthetic */ Object mo4invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
            return invoke2(coroutineScope, (Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>>) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) throws Throwable {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i15 = this.label;
            if (i15 == 0) {
                ResultKt.throwOnFailure(obj);
                CoroutineDispatcher coroutineDispatcherA = ai.a().a();
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(FeedPublishServiceV2Impl.this, this.$guild, this.$channel, this.$joinGuildTime, null);
                this.label = 1;
                obj = BuildersKt.withContext(coroutineDispatcherA, anonymousClass1, this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i15 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return obj;
        }

        @Nullable
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((AnonymousClass5) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryFeedPublishStateV2s$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes12.dex */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl", f = "FeedPublishServiceV2Impl.kt", i = {0, 0, 0, 1, 1, 1}, l = {229, 229}, m = "queryFeedPublishStateV2s", n = {"guild", WadlProxyConsts.CHANNEL, "sendingList", "guild", WadlProxyConsts.CHANNEL, "sendingList"}, s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2"})
    static final class C94521 extends ContinuationImpl {
        static IPatchRedirector $redirector_;
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C94521(Continuation<? super C94521> continuation) {
            super(continuation);
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) FeedPublishServiceV2Impl.this, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FeedPublishServiceV2Impl.this.queryFeedPublishStateV2s(null, null, false, 0, this);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryGlobalFeedFailPublishStateV2s$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes12.dex */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {313}, m = "queryGlobalFeedFailPublishStateV2s", n = {}, s = {})
    static final class C94531 extends ContinuationImpl {
        static IPatchRedirector $redirector_;
        int label;
        /* synthetic */ Object result;

        C94531(Continuation<? super C94531> continuation) {
            super(continuation);
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) FeedPublishServiceV2Impl.this, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FeedPublishServiceV2Impl.this.queryGlobalFeedFailPublishStateV2s(0L, this);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryGlobalFeedFailPublishStateV2s$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes22.dex */
    @Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/b;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryGlobalFeedFailPublishStateV2s$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {314}, m = "invokeSuspend", n = {}, s = {})
    static final class C94542 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>>, Object> {
        static IPatchRedirector $redirector_;
        int label;

        /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryGlobalFeedFailPublishStateV2s$2$1, reason: invalid class name */
        @Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "Lcom/tencent/mobileqq/guild/feed/publish/publishV2/b;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
        @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$queryGlobalFeedFailPublishStateV2s$2$1", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {315}, m = "invokeSuspend", n = {}, s = {})
        @SourceDebugExtension({"SMAP\nFeedPublishServiceV2Impl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$queryGlobalFeedFailPublishStateV2s$2$1\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,609:1\n766#2:610\n857#2,2:611\n1549#2:613\n1620#2,3:614\n*S KotlinDebug\n*F\n+ 1 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl$queryGlobalFeedFailPublishStateV2s$2$1\n*L\n319#1:610\n319#1:611,2\n320#1:613\n320#1:614,3\n*E\n"})
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>>, Object> {
            static IPatchRedirector $redirector_;
            int label;
            final /* synthetic */ FeedPublishServiceV2Impl this$0;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(FeedPublishServiceV2Impl feedPublishServiceV2Impl, Continuation<? super AnonymousClass1> continuation) {
                super(2, continuation);
                this.this$0 = feedPublishServiceV2Impl;
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector.redirect((short) 1, (Object) this, (Object) feedPublishServiceV2Impl, (Object) continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @NotNull
            public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
                IPatchRedirector iPatchRedirector = $redirector_;
                return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? new AnonymousClass1(this.this$0, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
            }

            @Override // kotlin.jvm.functions.Function2
            /* JADX INFO: renamed from: invoke */
            public /* bridge */ /* synthetic */ Object mo4invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
                return invoke2(coroutineScope, (Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>>) continuation);
            }

            @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
            @Nullable
            public final Object invokeSuspend(@NotNull Object obj) {
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                    return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                }
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                int i15 = this.label;
                if (i15 == 0) {
                    ResultKt.throwOnFailure(obj);
                    FeedPublishServiceV2Impl feedPublishServiceV2Impl = this.this$0;
                    this.label = 1;
                    obj = feedPublishServiceV2Impl.queryFeedPublishStateV2s("", "", false, 2, this);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    if (i15 != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ResultKt.throwOnFailure(obj);
                }
                FeedPublishServiceV2Impl feedPublishServiceV2Impl2 = this.this$0;
                List list = (List) obj;
                QLog.i(FeedPublishServiceV2Impl.TAG, 1, "queryGlobalFeedFailPublishStateV2s: queryFeedPublishStateV2s result size=" + list.size());
                ArrayList arrayList = new ArrayList();
                for (Object obj2 : list) {
                    com.tencent.mobileqq.guild.feed.publish.publishV2.b bVar = (com.tencent.mobileqq.guild.feed.publish.publishV2.b) obj2;
                    if (d.c(bVar.e()) && Companion.a(FeedPublishServiceV2Impl.INSTANCE, bVar.e()) > feedPublishServiceV2Impl2.config.getUploadLimitByte()) {
                        arrayList.add(obj2);
                    }
                }
                ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList, 10));
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    arrayList2.add(((com.tencent.mobileqq.guild.feed.publish.publishV2.b) it.next()).e());
                }
                n.i().e(arrayList2);
                return CollectionsKt.take((Iterable) obj, 50);
            }

            @Nullable
            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final Object invoke2(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
                IPatchRedirector iPatchRedirector = $redirector_;
                return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
            }
        }

        C94542(Continuation<? super C94542> continuation) {
            super(2, continuation);
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) FeedPublishServiceV2Impl.this, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? FeedPublishServiceV2Impl.this.new C94542(continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        /* JADX INFO: renamed from: invoke */
        public /* bridge */ /* synthetic */ Object mo4invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
            return invoke2(coroutineScope, (Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>>) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) throws Throwable {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i15 = this.label;
            if (i15 == 0) {
                ResultKt.throwOnFailure(obj);
                CoroutineDispatcher coroutineDispatcherA = ai.a().a();
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(FeedPublishServiceV2Impl.this, null);
                this.label = 1;
                obj = BuildersKt.withContext(coroutineDispatcherA, anonymousClass1, this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i15 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return obj;
        }

        @Nullable
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((C94542) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
        }
    }

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$sequentiallyRetryPostFeedTaskWithTimeout$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: loaded from: classes22.dex */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {400}, m = "sequentiallyRetryPostFeedTaskWithTimeout", n = {}, s = {})
    static final class C94551 extends ContinuationImpl {
        static IPatchRedirector $redirector_;
        int label;
        /* synthetic */ Object result;

        C94551(Continuation<? super C94551> continuation) {
            super(continuation);
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) FeedPublishServiceV2Impl.this, (Object) continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @Nullable
        public final Object invokeSuspend(@NotNull Object obj) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                return iPatchRedirector.redirect((short) 2, (Object) this, obj);
            }
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FeedPublishServiceV2Impl.this.sequentiallyRetryPostFeedTaskWithTimeout(null, this);
        }
    }

    static {
        IPatchRedirector redirector = PatchRedirectCenter.getRedirector(133211);
        $redirector_ = redirector;
        if (redirector == null || !redirector.hasPatch((short) 20)) {
            INSTANCE = new Companion(null);
        } else {
            redirector.redirect((short) 20);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public FeedPublishServiceV2Impl() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
            iPatchRedirector.redirect((short) 1, (Object) this);
            return;
        }
        this.$$delegate_0 = new a();
        CoroutineScope CoroutineScope = CoroutineScopeKt.CoroutineScope(SupervisorKt.SupervisorJob$default((Job) null, 1, (Object) null).plus(ai.a().e()));
        this.scope = CoroutineScope;
        this.config = GuildFeedAutoResendConfigParser.INSTANCE.a();
        this.memoryStorage = new AsyncDataMemoryStorage<>(new FeedPublishServiceV2Impl$memoryStorage$1(this, null), null, 2, 0 == true ? 1 : 0);
        this.sendingStatesMap = new ConcurrentHashMap<>();
        this.sendingStatesExtMap = new ConcurrentHashMap<>();
        SimpleEventBus simpleEventBus = SimpleEventBus.getInstance();
        Intrinsics.checkNotNullExpressionValue(simpleEventBus, "getInstance(...)");
        final Flow flowOnEach = FlowKt.onEach(FlowKt.callbackFlow(new FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$eventFlow$1(simpleEventBus, null)), new FeedPublishServiceV2Impl$publishEvents$1$startFlow$1(this, null));
        Flow<Void> flow = new Flow<Void>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$1
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$1$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n+ 3 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl\n*L\n1#1,222:1\n48#2:223\n131#3:224\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261296d;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$1$2$1, reason: invalid class name */
                /* JADX INFO: loaded from: classes12.dex */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$1$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {223}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector) {
                    this.f261296d = flowCollector;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        anonymousClass1.label = 1;
                        if (this.f261296d.emit(null, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261295d);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super Void> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261295d.collect(new AnonymousClass2(flowCollector), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        };
        final Flow flowCallbackFlow = FlowKt.callbackFlow(new FeedPublishServiceV2Impl$publishEvents$1$beginFlow$1(this, null));
        Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a.C4772a> flow2 = new Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a.C4772a>(this) { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$mapNotNull$1
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            final /* synthetic */ FeedPublishServiceV2Impl f261306e;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$mapNotNull$1$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n+ 3 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl\n*L\n1#1,222:1\n55#2:223\n56#2:226\n144#3,2:224\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261307d;

                /* JADX INFO: renamed from: e, reason: collision with root package name */
                final /* synthetic */ FeedPublishServiceV2Impl f261308e;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$mapNotNull$1$2$1, reason: invalid class name */
                /* JADX INFO: loaded from: classes12.dex */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$mapNotNull$1$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {226}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector, FeedPublishServiceV2Impl feedPublishServiceV2Impl) {
                    this.f261307d = flowCollector;
                    this.f261308e = feedPublishServiceV2Impl;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector, (Object) feedPublishServiceV2Impl);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        b bVar = (b) obj;
                        this.f261308e.sendingStatesMap.put(bVar.c(), bVar);
                        com.tencent.mobileqq.guild.feed.publish.publishV2.a.C4772a c4772a = new com.tencent.mobileqq.guild.feed.publish.publishV2.a.C4772a(bVar);
                        anonymousClass1.label = 1;
                        if (this.f261307d.emit(c4772a, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                this.f261306e = this;
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261305d, (Object) this);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super com.tencent.mobileqq.guild.feed.publish.publishV2.a.C4772a> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261305d.collect(new AnonymousClass2(flowCollector, this.f261306e), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        };
        SimpleEventBus simpleEventBus2 = SimpleEventBus.getInstance();
        Intrinsics.checkNotNullExpressionValue(simpleEventBus2, "getInstance(...)");
        final Flow flowCallbackFlow2 = FlowKt.callbackFlow(new FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$eventFlow$2(simpleEventBus2, null));
        final Flow<GuildTaskProgressStateEvent> flow3 = new Flow<GuildTaskProgressStateEvent>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$filter$1
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$filter$1$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n+ 3 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl\n*L\n1#1,222:1\n21#2:223\n22#2:225\n149#3:224\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261294d;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$filter$1$2$1, reason: invalid class name */
                /* JADX INFO: loaded from: classes12.dex */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$filter$1$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {223}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    Object L$1;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector) {
                    this.f261294d = flowCollector;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        if (!((GuildTaskProgressStateEvent) obj).isInitTrigger()) {
                            anonymousClass1.label = 1;
                            if (this.f261294d.emit(obj, anonymousClass1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261293d);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super GuildTaskProgressStateEvent> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261293d.collect(new AnonymousClass2(flowCollector), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        };
        final Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.b> flow4 = new Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.b>(this) { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$mapNotNull$2
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            final /* synthetic */ FeedPublishServiceV2Impl f261310e;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$mapNotNull$2$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n+ 3 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl\n*L\n1#1,222:1\n55#2:223\n56#2:226\n151#3,2:224\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261311d;

                /* JADX INFO: renamed from: e, reason: collision with root package name */
                final /* synthetic */ FeedPublishServiceV2Impl f261312e;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$mapNotNull$2$2$1, reason: invalid class name */
                /* JADX INFO: loaded from: classes12.dex */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$mapNotNull$2$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {226}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector, FeedPublishServiceV2Impl feedPublishServiceV2Impl) {
                    this.f261311d = flowCollector;
                    this.f261312e = feedPublishServiceV2Impl;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector, (Object) feedPublishServiceV2Impl);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    b bVarA;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        GuildTaskProgressStateEvent guildTaskProgressStateEvent = (GuildTaskProgressStateEvent) obj;
                        b bVar = (b) this.f261312e.sendingStatesMap.get(guildTaskProgressStateEvent.getTaskProgressState().getMainTaskId());
                        if (bVar != null) {
                            GuildTaskProgressState guildTaskProgressStateM300clone = guildTaskProgressStateEvent.getTaskProgressState().m300clone();
                            Intrinsics.checkNotNullExpressionValue(guildTaskProgressStateM300clone, "clone(...)");
                            bVarA = b.a(bVar, false, null, null, guildTaskProgressStateM300clone, null, 23, null);
                        } else {
                            bVarA = null;
                        }
                        if (bVarA != null) {
                            anonymousClass1.label = 1;
                            if (this.f261311d.emit(bVarA, anonymousClass1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                this.f261310e = this;
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261309d, (Object) this);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super b> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261309d.collect(new AnonymousClass2(flowCollector, this.f261310e), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        };
        Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a> flow5 = new Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a>(this) { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$2
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            final /* synthetic */ FeedPublishServiceV2Impl f261298e;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$2$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n+ 3 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl\n*L\n1#1,222:1\n48#2:223\n154#3,8:224\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261299d;

                /* JADX INFO: renamed from: e, reason: collision with root package name */
                final /* synthetic */ FeedPublishServiceV2Impl f261300e;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$2$2$1, reason: invalid class name */
                /* JADX INFO: loaded from: classes12.dex */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$2$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {223}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector, FeedPublishServiceV2Impl feedPublishServiceV2Impl) {
                    this.f261299d = flowCollector;
                    this.f261300e = feedPublishServiceV2Impl;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector, (Object) feedPublishServiceV2Impl);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                /* JADX WARN: Multi-variable type inference failed */
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
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        b bVar = (b) obj;
                        FeedPublishServiceV2Impl feedPublishServiceV2Impl = this.f261300e;
                        feedPublishServiceV2Impl.sendingStatesMap.put(bVar.c(), bVar);
                        if (d.a(bVar.e())) {
                            feedPublishServiceV2Impl.sendingStatesMap.remove(bVar.c());
                            feedPublishServiceV2Impl.sendingStatesExtMap.remove(bVar.c());
                        }
                        Object bVar2 = d.a(bVar.e()) ? new com.tencent.mobileqq.guild.feed.publish.publishV2.a.b(bVar) : new com.tencent.mobileqq.guild.feed.publish.publishV2.a.c(bVar);
                        anonymousClass1.label = 1;
                        if (this.f261299d.emit(bVar2, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                this.f261298e = this;
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261297d, (Object) this);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super com.tencent.mobileqq.guild.feed.publish.publishV2.a> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261297d.collect(new AnonymousClass2(flowCollector, this.f261298e), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        };
        final Flow flowCallbackFlow3 = FlowKt.callbackFlow(new FeedPublishServiceV2Impl$publishEvents$1$endFlow$1(this, null));
        this.publishEvents = FlowKt.shareIn(FlowKt.onEach(FlowKt.flowOn(FlowKt.onEach(FlowKt.filterNotNull(FlowKt.merge(flow, flow2, flow5, new Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b>(this) { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$3
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: e, reason: collision with root package name */
            final /* synthetic */ FeedPublishServiceV2Impl f261302e;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$3$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n+ 3 FeedPublishServiceV2Impl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/impl/FeedPublishServiceV2Impl\n*L\n1#1,222:1\n48#2:223\n174#3,5:224\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261303d;

                /* JADX INFO: renamed from: e, reason: collision with root package name */
                final /* synthetic */ FeedPublishServiceV2Impl f261304e;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$3$2$1, reason: invalid class name */
                /* JADX INFO: loaded from: classes12.dex */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEvents$lambda$7$$inlined$map$3$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {223}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector, FeedPublishServiceV2Impl feedPublishServiceV2Impl) {
                    this.f261303d = flowCollector;
                    this.f261304e = feedPublishServiceV2Impl;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector, (Object) feedPublishServiceV2Impl);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        b bVar = (b) obj;
                        FeedPublishServiceV2Impl feedPublishServiceV2Impl = this.f261304e;
                        feedPublishServiceV2Impl.sendingStatesMap.remove(bVar.c());
                        feedPublishServiceV2Impl.sendingStatesExtMap.remove(bVar.c());
                        com.tencent.mobileqq.guild.feed.publish.publishV2.a.b bVar2 = new com.tencent.mobileqq.guild.feed.publish.publishV2.a.b(bVar);
                        FeedPublishServiceV2Impl.Holder.f261272a.a(bVar2);
                        anonymousClass1.label = 1;
                        if (this.f261303d.emit(bVar2, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                this.f261302e = this;
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261301d, (Object) this);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261301d.collect(new AnonymousClass2(flowCollector, this.f261302e), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        })), new FeedPublishServiceV2Impl$publishEvents$1$1(this, null)), Dispatchers.getIO()), new FeedPublishServiceV2Impl$publishEvents$1$2(null)), CoroutineScope, SharingStarted.INSTANCE.getEagerly(), 0);
        this.firstActivePublishing = true;
        this.retryingTaskIds = Collections.synchronizedSet(new LinkedHashSet());
    }

    public static /* synthetic */ void invalidateCache$default(FeedPublishServiceV2Impl feedPublishServiceV2Impl, String str, String str2, Integer num, int i15, Object obj) {
        if ((i15 & 4) != 0) {
            num = null;
        }
        feedPublishServiceV2Impl.invalidateCache(str, str2, num);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onQuitGuild(String guild2) {
        BuildersKt__Builders_commonKt.launch$default(this.scope, null, null, new C94491(guild2, null), 3, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:103:0x02d5  */
    /* JADX WARN: Code duplicated, block: B:106:0x02dd  */
    /* JADX WARN: Code duplicated, block: B:107:0x02e2  */
    /* JADX WARN: Code duplicated, block: B:110:0x02e9  */
    /* JADX WARN: Code duplicated, block: B:113:0x0301 A[LOOP:2: B:111:0x02fb->B:113:0x0301, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:115:0x034f  */
    /* JADX WARN: Code duplicated, block: B:119:0x01b1 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:121:0x019a A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:53:0x015f  */
    /* JADX WARN: Code duplicated, block: B:54:0x0162  */
    /* JADX WARN: Code duplicated, block: B:59:0x01a0  */
    /* JADX WARN: Code duplicated, block: B:64:0x01c5  */
    /* JADX WARN: Code duplicated, block: B:65:0x01cd  */
    /* JADX WARN: Code duplicated, block: B:69:0x01d5  */
    /* JADX WARN: Code duplicated, block: B:71:0x01d8 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:72:0x01da  */
    /* JADX WARN: Code duplicated, block: B:73:0x01e9  */
    /* JADX WARN: Code duplicated, block: B:74:0x01eb  */
    /* JADX WARN: Code duplicated, block: B:76:0x01ee  */
    /* JADX WARN: Code duplicated, block: B:77:0x01fb A[PHI: r15
  0x01fb: PHI (r15v4 'simpleProfile' com.tencent.mobileqq.qqguildsdk.data.genc.IGProSimpleProfile) = 
  (r15v3 'simpleProfile' com.tencent.mobileqq.qqguildsdk.data.genc.IGProSimpleProfile)
  (r15v6 'simpleProfile' com.tencent.mobileqq.qqguildsdk.data.genc.IGProSimpleProfile)
 binds: [B:75:0x01ec, B:73:0x01e9] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:79:0x01fe  */
    /* JADX WARN: Code duplicated, block: B:7:0x001f  */
    /* JADX WARN: Code duplicated, block: B:80:0x020d  */
    /* JADX WARN: Code duplicated, block: B:82:0x0211  */
    /* JADX WARN: Code duplicated, block: B:86:0x021e  */
    /* JADX WARN: Code duplicated, block: B:89:0x0228  */
    /* JADX WARN: Code duplicated, block: B:90:0x022d  */
    /* JADX WARN: Code duplicated, block: B:92:0x0230  */
    /* JADX WARN: Code duplicated, block: B:93:0x0232  */
    /* JADX WARN: Code duplicated, block: B:97:0x023f  */
    /* JADX WARN: Code duplicated, block: B:99:0x0247  */
    public final Object queryFeedPublishStateV2s(String str, String str2, boolean z15, int i15, Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
        C94521 c94521;
        List arrayList;
        String str3;
        List list;
        String str4;
        String str5;
        List listEmptyList;
        HashSet hashSet;
        ArrayList arrayList2;
        String strA;
        IGPSService iGPSServiceF;
        IGProSimpleProfile simpleProfile;
        IGProSimpleProfile simpleProfile2;
        IGProGuildRoleInfo roleInfo;
        String avatarUrl;
        String avatarPendantUrl;
        IGProChannelInfo iGProChannelInfoK;
        String channelName;
        Iterator it;
        com.tencent.mobileqq.guild.feed.publish.publishV2.b bVar;
        String str6;
        String displayTagName;
        int color;
        GProUserIdentityData gProUserIdentityData;
        String str7 = str;
        String str8 = str2;
        if (continuation instanceof C94521) {
            c94521 = (C94521) continuation;
            int i16 = c94521.label;
            if ((i16 & Integer.MIN_VALUE) != 0) {
                c94521.label = i16 - Integer.MIN_VALUE;
            } else {
                c94521 = new C94521(continuation);
            }
        } else {
            c94521 = new C94521(continuation);
        }
        Object objA = c94521.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i17 = c94521.label;
        String str9 = "";
        if (i17 != 0) {
            if (i17 == 1) {
                List list2 = (List) c94521.L$2;
                str8 = (String) c94521.L$1;
                String str10 = (String) c94521.L$0;
                ResultKt.throwOnFailure(objA);
                arrayList = list2;
                str7 = str10;
            } else {
                if (i17 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                list = (List) c94521.L$2;
                str8 = (String) c94521.L$1;
                str3 = (String) c94521.L$0;
                ResultKt.throwOnFailure(objA);
            }
            listEmptyList = (List) objA;
            if (listEmptyList == null) {
                arrayList = list;
                str7 = str3;
                listEmptyList = CollectionsKt.emptyList();
                str4 = str7;
                str5 = str8;
                list = arrayList;
            } else {
                str5 = str8;
                str4 = str3;
            }
            QLog.i(TAG, 1, "queryFeedPublishStateV2s: cacheList size=" + listEmptyList.size());
            List listPlus = CollectionsKt.plus((Collection) list, (Iterable) listEmptyList);
            hashSet = new HashSet();
            arrayList2 = new ArrayList();
            for (Object obj : listPlus) {
                if (hashSet.add(((com.tencent.mobileqq.guild.feed.publish.publishV2.b) obj).c())) {
                    arrayList2.add(obj);
                }
            }
            strA = dm.a();
            Intrinsics.checkNotNullExpressionValue(strA, "accountTinyId(...)");
            iGPSServiceF = bp.f();
            if (iGPSServiceF != null) {
                simpleProfile = iGPSServiceF.getSimpleProfile(str4, strA, 100);
            } else {
                simpleProfile = null;
            }
            if (str5.length() > 0) {
                simpleProfile2 = simpleProfile;
                if (simpleProfile2 != null) {
                    roleInfo = iGPSServiceF.getRoleInfo(simpleProfile2.getGuildId(), simpleProfile2.getRoleId());
                } else {
                    roleInfo = null;
                }
            } else if (iGPSServiceF != null) {
                simpleProfile2 = simpleProfile;
                roleInfo = iGPSServiceF.getMemberTopRole(str4, str5, strA, "0", 0);
            } else {
                simpleProfile2 = simpleProfile;
                roleInfo = null;
            }
            if (simpleProfile2 != null) {
                avatarUrl = iGPSServiceF.getFullAvatarUrl(new cz(strA, simpleProfile2.getAvatarMeta()), 0);
            } else {
                avatarUrl = null;
            }
            if (avatarUrl == null) {
                avatarUrl = "";
            }
            if (simpleProfile2 != null || (avatarPendantUrl = iGPSServiceF.getFullUserAvatarPendantUrl(simpleProfile2.getAvatarPendant())) == null) {
                avatarPendantUrl = "";
            }
            Intrinsics.checkNotNull(avatarPendantUrl);
            iGProChannelInfoK = dm.k(str5);
            if (iGProChannelInfoK != null) {
                channelName = iGProChannelInfoK.getChannelName();
            } else {
                channelName = null;
            }
            if (channelName == null) {
                channelName = "";
            } else {
                Intrinsics.checkNotNull(channelName);
            }
            it = arrayList2.iterator();
            while (it.hasNext()) {
                bVar = (com.tencent.mobileqq.guild.feed.publish.publishV2.b) it.next();
                if (simpleProfile2 != null) {
                    GProStFeed gProStFeedD = bVar.d();
                    Intrinsics.checkNotNullParameter(gProStFeedD, "<this>");
                    Intrinsics.checkNotNullParameter(simpleProfile2, "simpleProfile");
                    Intrinsics.checkNotNullParameter(avatarUrl, "avatarUrl");
                    Intrinsics.checkNotNullParameter(avatarPendantUrl, "avatarPendantUrl");
                    Intrinsics.checkNotNullParameter(channelName, "channelName");
                    GProStUser poster = gProStFeedD.poster;
                    str6 = str9;
                    Intrinsics.checkNotNullExpressionValue(poster, "poster");
                    Intrinsics.checkNotNullParameter(poster, "<this>");
                    Intrinsics.checkNotNullParameter(simpleProfile2, "simpleProfile");
                    Intrinsics.checkNotNullParameter(avatarUrl, "avatarUrl");
                    Intrinsics.checkNotNullParameter(avatarPendantUrl, "avatarPendantUrl");
                    poster.idd = String.valueOf(simpleProfile2.getTinyId());
                    poster.nick = simpleProfile2.getDisplayName();
                    poster.icon.iconUrl = avatarUrl;
                    GProPersonalMedal gProPersonalMedal = poster.personalMedal;
                    gProPersonalMedal.startTime = simpleProfile2.getPersonalMedal().getStartTime();
                    gProPersonalMedal.endTime = simpleProfile2.getPersonalMedal().getEndTime();
                    gProPersonalMedal.url = simpleProfile2.getPersonalMedal().getUrl();
                    poster.avatarPendant = avatarPendantUrl;
                    GProChannelUserInfo gProChannelUserInfo = poster.channelUserInfo;
                    j jVar = j.f116304a;
                    IGProClientIdentity clientIdentityBytes = simpleProfile2.getClientIdentityBytes();
                    Intrinsics.checkNotNullExpressionValue(clientIdentityBytes, "getClientIdentityBytes(...)");
                    gProChannelUserInfo.clientIdentityBytes = jVar.y(clientIdentityBytes);
                    GProCustomManageTag gProCustomManageTag = poster.manageTag;
                    if (roleInfo != null || (displayTagName = roleInfo.getDisplayTagName()) == null) {
                        displayTagName = str6;
                    }
                    gProCustomManageTag.tagName = displayTagName;
                    GProCustomManageTag gProCustomManageTag2 = poster.manageTag;
                    if (roleInfo != null) {
                        color = roleInfo.getColor();
                    } else {
                        color = 0;
                    }
                    gProCustomManageTag2.color = color;
                    gProUserIdentityData = poster.userIdentityData;
                    if (gProUserIdentityData != null) {
                        ArrayList<IGProUserTag> tags = simpleProfile2.getUserIdentity().getTags();
                        Intrinsics.checkNotNullExpressionValue(tags, "getTags(...)");
                        for (IGProUserTag iGProUserTag : tags) {
                            ArrayList<GProUserTag> arrayList3 = gProUserIdentityData.tags;
                            GProUserTag gProUserTag = new GProUserTag();
                            gProUserTag.tagType = iGProUserTag.getTagType();
                            gProUserTag.iconUrl = iGProUserTag.getIconUrl();
                            gProUserTag.displayText = iGProUserTag.getDisplayText();
                            gProUserTag.textColor = iGProUserTag.getTextColor();
                            gProUserTag.backgroundColor = iGProUserTag.getBackgroundColor();
                            arrayList3.add(gProUserTag);
                            gProUserIdentityData = gProUserIdentityData;
                        }
                    }
                    j jVar2 = j.f116304a;
                    poster.authorMedals = jVar2.z(simpleProfile2.getAuthorMedal());
                    poster.authorOfficialCertification = jVar2.A(simpleProfile2.getAuthorOfficialCertification());
                    gProStFeedD.channelInfo.name = channelName;
                } else {
                    str6 = str9;
                    GProStFeed gProStFeedD2 = bVar.d();
                    String strA2 = dm.a();
                    Intrinsics.checkNotNullExpressionValue(strA2, "accountTinyId(...)");
                    com.tencent.mobileqq.guild.feed.nativeinterface.kt.b.a(gProStFeedD2, str4, str5, strA2);
                }
                it = it;
                str9 = str6;
            }
            return arrayList2;
        }
        ResultKt.throwOnFailure(objA);
        boolean z16 = i15 == 2;
        QLog.i(TAG, 1, "queryFeedPublishStateV2s: start guild=" + str7 + " channel=" + str8 + " forceDB=" + z15 + " feedScope=" + i15 + " isGlobalFeed=" + z16);
        Collection<com.tencent.mobileqq.guild.feed.publish.publishV2.b> collectionValues = this.sendingStatesMap.values();
        Intrinsics.checkNotNullExpressionValue(collectionValues, "<get-values>(...)");
        List mutableList = CollectionsKt.toMutableList((Collection) collectionValues);
        arrayList = new ArrayList();
        for (Object obj2 : mutableList) {
            com.tencent.mobileqq.guild.feed.publish.publishV2.b bVar2 = (com.tencent.mobileqq.guild.feed.publish.publishV2.b) obj2;
            if (!z16 ? !(Intrinsics.areEqual(bVar2.e().getGuildId(), str7) && Intrinsics.areEqual(bVar2.e().getChannelId(), str8) && bVar2.e().getScope() == i15) : bVar2.e().getScope() != i15) {
                arrayList.add(obj2);
            }
        }
        QLog.i(TAG, 1, "queryFeedPublishStateV2s: sendingList size=" + arrayList.size());
        Triple<String, String, Integer> triple = z16 ? new Triple<>("", "", Boxing.boxInt(i15)) : new Triple<>(str7, str8, Boxing.boxInt(i15));
        AsyncDataMemoryStorage<Triple<String, String, Integer>, Deferred<List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>>> asyncDataMemoryStorage = this.memoryStorage;
        c94521.L$0 = str7;
        c94521.L$1 = str8;
        c94521.L$2 = arrayList;
        c94521.label = 1;
        objA = asyncDataMemoryStorage.a(triple, z15, c94521);
        if (objA == coroutine_suspended) {
            return coroutine_suspended;
        }
        Deferred deferred = (Deferred) objA;
        if (deferred != null) {
            c94521.L$0 = str7;
            c94521.L$1 = str8;
            c94521.L$2 = arrayList;
            c94521.label = 2;
            objA = deferred.await(c94521);
            if (objA == coroutine_suspended) {
                return coroutine_suspended;
            }
            str3 = str7;
            list = arrayList;
            listEmptyList = (List) objA;
            if (listEmptyList == null) {
                arrayList = list;
                str7 = str3;
                listEmptyList = CollectionsKt.emptyList();
                str4 = str7;
                str5 = str8;
                list = arrayList;
            } else {
                str5 = str8;
                str4 = str3;
            }
        } else {
            listEmptyList = CollectionsKt.emptyList();
            str4 = str7;
            str5 = str8;
            list = arrayList;
        }
        QLog.i(TAG, 1, "queryFeedPublishStateV2s: cacheList size=" + listEmptyList.size());
        List listPlus2 = CollectionsKt.plus((Collection) list, (Iterable) listEmptyList);
        hashSet = new HashSet();
        arrayList2 = new ArrayList();
        while (r1.hasNext()) {
            if (hashSet.add(((com.tencent.mobileqq.guild.feed.publish.publishV2.b) obj).c())) {
                arrayList2.add(obj);
            }
        }
        strA = dm.a();
        Intrinsics.checkNotNullExpressionValue(strA, "accountTinyId(...)");
        iGPSServiceF = bp.f();
        if (iGPSServiceF != null) {
            simpleProfile = iGPSServiceF.getSimpleProfile(str4, strA, 100);
        } else {
            simpleProfile = null;
        }
        if (str5.length() > 0) {
            simpleProfile2 = simpleProfile;
            if (simpleProfile2 != null) {
                roleInfo = iGPSServiceF.getRoleInfo(simpleProfile2.getGuildId(), simpleProfile2.getRoleId());
            } else {
                roleInfo = null;
            }
        } else if (iGPSServiceF != null) {
            simpleProfile2 = simpleProfile;
            roleInfo = iGPSServiceF.getMemberTopRole(str4, str5, strA, "0", 0);
        } else {
            simpleProfile2 = simpleProfile;
            roleInfo = null;
        }
        if (simpleProfile2 != null) {
            avatarUrl = iGPSServiceF.getFullAvatarUrl(new cz(strA, simpleProfile2.getAvatarMeta()), 0);
        } else {
            avatarUrl = null;
        }
        if (avatarUrl == null) {
            avatarUrl = "";
        }
        if (simpleProfile2 != null) {
            avatarPendantUrl = "";
        } else {
            avatarPendantUrl = "";
        }
        Intrinsics.checkNotNull(avatarPendantUrl);
        iGProChannelInfoK = dm.k(str5);
        if (iGProChannelInfoK != null) {
            channelName = iGProChannelInfoK.getChannelName();
        } else {
            channelName = null;
        }
        if (channelName == null) {
            channelName = "";
        } else {
            Intrinsics.checkNotNull(channelName);
        }
        it = arrayList2.iterator();
        while (it.hasNext()) {
            bVar = (com.tencent.mobileqq.guild.feed.publish.publishV2.b) it.next();
            if (simpleProfile2 != null) {
                GProStFeed gProStFeedD3 = bVar.d();
                Intrinsics.checkNotNullParameter(gProStFeedD3, "<this>");
                Intrinsics.checkNotNullParameter(simpleProfile2, "simpleProfile");
                Intrinsics.checkNotNullParameter(avatarUrl, "avatarUrl");
                Intrinsics.checkNotNullParameter(avatarPendantUrl, "avatarPendantUrl");
                Intrinsics.checkNotNullParameter(channelName, "channelName");
                GProStUser poster2 = gProStFeedD3.poster;
                str6 = str9;
                Intrinsics.checkNotNullExpressionValue(poster2, "poster");
                Intrinsics.checkNotNullParameter(poster2, "<this>");
                Intrinsics.checkNotNullParameter(simpleProfile2, "simpleProfile");
                Intrinsics.checkNotNullParameter(avatarUrl, "avatarUrl");
                Intrinsics.checkNotNullParameter(avatarPendantUrl, "avatarPendantUrl");
                poster2.idd = String.valueOf(simpleProfile2.getTinyId());
                poster2.nick = simpleProfile2.getDisplayName();
                poster2.icon.iconUrl = avatarUrl;
                GProPersonalMedal gProPersonalMedal2 = poster2.personalMedal;
                gProPersonalMedal2.startTime = simpleProfile2.getPersonalMedal().getStartTime();
                gProPersonalMedal2.endTime = simpleProfile2.getPersonalMedal().getEndTime();
                gProPersonalMedal2.url = simpleProfile2.getPersonalMedal().getUrl();
                poster2.avatarPendant = avatarPendantUrl;
                GProChannelUserInfo gProChannelUserInfo2 = poster2.channelUserInfo;
                j jVar3 = j.f116304a;
                IGProClientIdentity clientIdentityBytes2 = simpleProfile2.getClientIdentityBytes();
                Intrinsics.checkNotNullExpressionValue(clientIdentityBytes2, "getClientIdentityBytes(...)");
                gProChannelUserInfo2.clientIdentityBytes = jVar3.y(clientIdentityBytes2);
                GProCustomManageTag gProCustomManageTag3 = poster2.manageTag;
                if (roleInfo != null) {
                    displayTagName = str6;
                } else {
                    displayTagName = str6;
                }
                gProCustomManageTag3.tagName = displayTagName;
                GProCustomManageTag gProCustomManageTag4 = poster2.manageTag;
                if (roleInfo != null) {
                    color = roleInfo.getColor();
                } else {
                    color = 0;
                }
                gProCustomManageTag4.color = color;
                gProUserIdentityData = poster2.userIdentityData;
                if (gProUserIdentityData != null) {
                    ArrayList<IGProUserTag> tags2 = simpleProfile2.getUserIdentity().getTags();
                    Intrinsics.checkNotNullExpressionValue(tags2, "getTags(...)");
                    while (r1.hasNext()) {
                        ArrayList<GProUserTag> arrayList4 = gProUserIdentityData.tags;
                        GProUserTag gProUserTag2 = new GProUserTag();
                        gProUserTag2.tagType = iGProUserTag.getTagType();
                        gProUserTag2.iconUrl = iGProUserTag.getIconUrl();
                        gProUserTag2.displayText = iGProUserTag.getDisplayText();
                        gProUserTag2.textColor = iGProUserTag.getTextColor();
                        gProUserTag2.backgroundColor = iGProUserTag.getBackgroundColor();
                        arrayList4.add(gProUserTag2);
                        gProUserIdentityData = gProUserIdentityData;
                    }
                }
                j jVar4 = j.f116304a;
                poster2.authorMedals = jVar4.z(simpleProfile2.getAuthorMedal());
                poster2.authorOfficialCertification = jVar4.A(simpleProfile2.getAuthorOfficialCertification());
                gProStFeedD3.channelInfo.name = channelName;
            } else {
                str6 = str9;
                GProStFeed gProStFeedD4 = bVar.d();
                String strA3 = dm.a();
                Intrinsics.checkNotNullExpressionValue(strA3, "accountTinyId(...)");
                com.tencent.mobileqq.guild.feed.nativeinterface.kt.b.a(gProStFeedD4, str4, str5, strA3);
            }
            it = it;
            str9 = str6;
        }
        return arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    public final Object sequentiallyRetryPostFeedTaskWithTimeout(GuildTaskProgressState guildTaskProgressState, Continuation<? super Unit> continuation) {
        C94551 c94551;
        if (continuation instanceof C94551) {
            c94551 = (C94551) continuation;
            int i15 = c94551.label;
            if ((i15 & Integer.MIN_VALUE) != 0) {
                c94551.label = i15 - Integer.MIN_VALUE;
            } else {
                c94551 = new C94551(continuation);
            }
        } else {
            c94551 = new C94551(continuation);
        }
        Object objWithTimeoutOrNull = c94551.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i16 = c94551.label;
        if (i16 == 0) {
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
            FeedPublishServiceV2Impl$sequentiallyRetryPostFeedTaskWithTimeout$result$1 feedPublishServiceV2Impl$sequentiallyRetryPostFeedTaskWithTimeout$result$1 = new FeedPublishServiceV2Impl$sequentiallyRetryPostFeedTaskWithTimeout$result$1(this, guildTaskProgressState, null);
            c94551.label = 1;
            objWithTimeoutOrNull = TimeoutKt.withTimeoutOrNull(3600000L, feedPublishServiceV2Impl$sequentiallyRetryPostFeedTaskWithTimeout$result$1, c94551);
            if (objWithTimeoutOrNull == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i16 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
        }
        Logger.f281727a.d().i(TAG, 1, "activePublishingFeed: finish " + objWithTimeoutOrNull);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.tencent.mobileqq.guild.feed.publish.publishV2.b stateV2FromProgressState(boolean isNewFeed, GuildTaskProgressState progressState) {
        try {
            Result.Companion companion = Result.INSTANCE;
            if (progressState == null) {
                return null;
            }
            GProStFeed fakeStFeed = progressState.getFakeStFeed();
            if (fakeStFeed != null) {
                Intrinsics.checkNotNull(fakeStFeed);
                String mainTaskId = progressState.getMainTaskId();
                Intrinsics.checkNotNullExpressionValue(mainTaskId, "getMainTaskId(...)");
                com.tencent.mobileqq.guild.feed.nativeinterface.kt.b.c(fakeStFeed, mainTaskId, progressState.getTriggerPublishTimeNs(), progressState.isEdit());
            } else {
                String strOptString = new JSONObject(progressState.mJson).optString("jsonFeed");
                JSONObject jSONObject = new JSONObject(strOptString);
                GProStFeedKt gProStFeedKt = GProStFeedKt.f257607a;
                Intrinsics.checkNotNull(strOptString);
                Intrinsics.checkNotNullParameter(jSONObject, "<this>");
                fakeStFeed = GProStFeedKt.a(gProStFeedKt, strOptString, jSONObject.optInt("feed_type", 1) == 1, jSONObject.optString("patternInfo"), jSONObject.optJSONObject("third_bar"), null, 16, null);
                if (fakeStFeed == null) {
                    return null;
                }
                String mainTaskId2 = progressState.getMainTaskId();
                Intrinsics.checkNotNullExpressionValue(mainTaskId2, "getMainTaskId(...)");
                com.tencent.mobileqq.guild.feed.nativeinterface.kt.b.c(fakeStFeed, mainTaskId2, progressState.getTriggerPublishTimeNs(), progressState.isEdit());
            }
            GProStFeed gProStFeed = fakeStFeed;
            String mTaskId = progressState.mTaskId;
            Intrinsics.checkNotNullExpressionValue(mTaskId, "mTaskId");
            return new com.tencent.mobileqq.guild.feed.publish.publishV2.b(isNewFeed, mTaskId, gProStFeed, progressState, null, 16, null);
        } catch (Throwable th5) {
            Result.Companion companion2 = Result.INSTANCE;
            Object objM747constructorimpl = Result.m747constructorimpl(ResultKt.createFailure(th5));
            return (com.tencent.mobileqq.guild.feed.publish.publishV2.b) (Result.m753isFailureimpl(objM747constructorimpl) ? null : objM747constructorimpl);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    public void activePublishingFeed(@NotNull String guildId, boolean limitUploadSize) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 9)) {
            iPatchRedirector.redirect((short) 9, this, guildId, Boolean.valueOf(limitUploadSize));
            return;
        }
        Intrinsics.checkNotNullParameter(guildId, "guildId");
        Logger.f281727a.d().i(TAG, 1, "activePublishingFeed: begin guildId:" + guildId + " wifi:" + AppNetConnInfo.isWifiConn() + " limitUploadSize:" + limitUploadSize + " config:" + this.config);
        if (!GuildFeedAutoResendConfigParser.INSTANCE.b()) {
            Logger.b bVar = new Logger.b();
            bVar.a().add("activePublishingFeed: config disable");
            Iterator<T> it = bVar.a().iterator();
            while (it.hasNext()) {
                Logger.f281727a.d().e(TAG, 1, (String) it.next(), null);
            }
            return;
        }
        if (this.config.getResendAllGuild() && AppNetConnInfo.isWifiConn() && this.firstActivePublishing) {
            this.firstActivePublishing = false;
            guildId = "";
        }
        BuildersKt__Builders_commonKt.launch$default(this.scope, null, null, new AnonymousClass3(FlowKt.launchIn(FlowKt.merge(FlowKt.onEach(publishBeginFlow(), new FeedPublishServiceV2Impl$activePublishingFeed$job$1(this, null)), FlowKt.onEach(publishEndFlow(), new FeedPublishServiceV2Impl$activePublishingFeed$job$2(this, null))), this.scope), guildId, this, limitUploadSize, null), 3, null);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @Nullable
    public com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> findFileTransferModel(@NotNull String fileId) {
        com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model;
        Map<String, com.tencent.mobileqq.guild.data.media.d> mapD;
        com.tencent.mobileqq.guild.data.media.d dVar;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
            return (com.tencent.mobileqq.guild.data.publish.taskx.api.a) iPatchRedirector.redirect((short) 2, (Object) this, (Object) fileId);
        }
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        a aVar = this.$$delegate_0;
        aVar.getClass();
        IPatchRedirector iPatchRedirector2 = a.$redirector_;
        if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 3)) {
            return (com.tencent.mobileqq.guild.data.publish.taskx.api.a) iPatchRedirector2.redirect((short) 3, (Object) aVar, (Object) fileId);
        }
        Intrinsics.checkNotNullParameter(fileId, "fileId");
        Collection<p> collectionJ = n.i().j();
        Intrinsics.checkNotNullExpressionValue(collectionJ, "getTaskFacadeList(...)");
        Iterator<T> it = collectionJ.iterator();
        do {
            model = null;
            if (!it.hasNext()) {
                break;
            }
            com.tencent.mobileqq.guild.feed.publish.api.a aVarB = ((p) it.next()).b().b();
            if (aVarB != null && (mapD = aVarB.d()) != null && (dVar = mapD.get(fileId)) != null) {
                model = dVar.getModel();
            }
        } while (model == null);
        return model;
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @NotNull
    public List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> finishedFeedPublishState(@NotNull String guild2, @NotNull String channel) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 10)) {
            return (List) iPatchRedirector.redirect((short) 10, (Object) this, (Object) guild2, (Object) channel);
        }
        Intrinsics.checkNotNullParameter(guild2, "guild");
        Intrinsics.checkNotNullParameter(channel, "channel");
        return Holder.f261272a.c(guild2, channel, 0);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @NotNull
    public List<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> finishedGlobalFeedPublishState() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 11)) ? Holder.f261272a.c("", "", 2) : (List) iPatchRedirector.redirect((short) 11, (Object) this);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @Nullable
    public com.tencent.mobileqq.guild.feed.publish.api.d getPostFeedMainTaskFacade(@NotNull String mainTaskId) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
            return (com.tencent.mobileqq.guild.feed.publish.api.d) iPatchRedirector.redirect((short) 3, (Object) this, (Object) mainTaskId);
        }
        Intrinsics.checkNotNullParameter(mainTaskId, "mainTaskId");
        a aVar = this.$$delegate_0;
        aVar.getClass();
        IPatchRedirector iPatchRedirector2 = a.$redirector_;
        if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
            return (com.tencent.mobileqq.guild.feed.publish.api.d) iPatchRedirector2.redirect((short) 2, (Object) aVar, (Object) mainTaskId);
        }
        Intrinsics.checkNotNullParameter(mainTaskId, "mainTaskId");
        h hVarH = n.i().h(mainTaskId);
        if (hVarH != null) {
            return hVarH.b();
        }
        return null;
    }

    public final void innerOnPublishBegin(boolean isNewFeed, @NotNull GuildTaskProgressState progressState) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 4)) {
            iPatchRedirector.redirect((short) 4, this, Boolean.valueOf(isNewFeed), progressState);
            return;
        }
        Intrinsics.checkNotNullParameter(progressState, "progressState");
        Logger.f281727a.d().i(TAG, 1, "saveTaskStFeed " + isNewFeed + " " + progressState);
        Function2<? super Boolean, ? super GuildTaskProgressState, Unit> function2 = this._publishBeginEvent;
        if (function2 != null) {
            function2.mo4invoke(Boolean.valueOf(isNewFeed), progressState);
        }
    }

    public final void innerOnPublishSuccess(@NotNull GuildPublishSuccessGetFeedEvent event, @NotNull GuildTaskProgressState state) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 5)) {
            iPatchRedirector.redirect((short) 5, (Object) this, (Object) event, (Object) state);
            return;
        }
        Intrinsics.checkNotNullParameter(event, "event");
        Intrinsics.checkNotNullParameter(state, "state");
        Logger.f281727a.d().i(TAG, 1, "innerOnPublishSuccess mainTaskId=" + event.getMainTaskId() + ", " + state);
        Function2<? super GuildPublishSuccessGetFeedEvent, ? super GuildTaskProgressState, Unit> function2 = this._publishSuccessEvent;
        if (function2 != null) {
            function2.mo4invoke(event, state);
        }
    }

    public final void invalidateCache(@NotNull String guildId, @NotNull String channel, @Nullable Integer feedScope) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 12)) {
            iPatchRedirector.redirect((short) 12, this, guildId, channel, feedScope);
            return;
        }
        Intrinsics.checkNotNullParameter(guildId, "guildId");
        Intrinsics.checkNotNullParameter(channel, "channel");
        if (feedScope != null && feedScope.intValue() == 2) {
            this.memoryStorage.c(new Triple<>("", "", 2));
            return;
        }
        if (feedScope != null && feedScope.intValue() == 0) {
            this.memoryStorage.c(new Triple<>(guildId, channel, 0));
            this.memoryStorage.c(new Triple<>(guildId, "", 0));
        } else {
            this.memoryStorage.c(new Triple<>("", "", 2));
            this.memoryStorage.c(new Triple<>(guildId, channel, 0));
            this.memoryStorage.c(new Triple<>(guildId, "", 0));
        }
    }

    @Override // p022mqq.app.api.IRuntimeService
    public void onCreate(@NotNull AppRuntime appRuntime) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 16)) {
            iPatchRedirector.redirect((short) 16, (Object) this, (Object) appRuntime);
            return;
        }
        Intrinsics.checkNotNullParameter(appRuntime, "appRuntime");
        n.i().l();
        SimpleEventBus simpleEventBus = SimpleEventBus.getInstance();
        Intrinsics.checkNotNullExpressionValue(simpleEventBus, "getInstance(...)");
        FlowKt.launchIn(FlowKt.onEach(FlowKt.callbackFlow(new FeedPublishServiceV2Impl$onCreate$$inlined$eventFlow$1(simpleEventBus, null)), new C94481(null)), this.scope);
        b.f261276d.d(appRuntime, new AnonymousClass2(this));
    }

    @Override // p022mqq.app.api.IRuntimeService
    public void onDestroy() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 19)) {
            iPatchRedirector.redirect((short) 19, (Object) this);
        } else {
            CoroutineScopeKt.cancel$default(this.scope, null, 1, null);
            b.f261276d.b();
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @NotNull
    public Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a.C4772a> publishBeginFlow() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 13)) {
            return (Flow) iPatchRedirector.redirect((short) 13, (Object) this);
        }
        final Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a> flow = this.publishEvents;
        return new Flow<Object>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishBeginFlow$$inlined$filterIsInstance$1
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishBeginFlow$$inlined$filterIsInstance$1$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n*L\n1#1,222:1\n21#2:223\n35#2:224\n22#2:225\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261286d;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishBeginFlow$$inlined$filterIsInstance$1$2$1, reason: invalid class name */
                /* JADX INFO: loaded from: classes12.dex */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishBeginFlow$$inlined$filterIsInstance$1$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {224}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    Object L$1;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector) {
                    this.f261286d = flowCollector;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        if (obj instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.a.C4772a) {
                            anonymousClass1.label = 1;
                            if (this.f261286d.emit(obj, anonymousClass1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261285d);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super Object> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261285d.collect(new AnonymousClass2(flowCollector), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        };
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @NotNull
    public Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a.b> publishEndFlow() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 15)) {
            return (Flow) iPatchRedirector.redirect((short) 15, (Object) this);
        }
        final Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a> flow = this.publishEvents;
        return new Flow<Object>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEndFlow$$inlined$filterIsInstance$1
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEndFlow$$inlined$filterIsInstance$1$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n*L\n1#1,222:1\n21#2:223\n35#2:224\n22#2:225\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261288d;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEndFlow$$inlined$filterIsInstance$1$2$1, reason: invalid class name */
                /* JADX INFO: loaded from: classes12.dex */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishEndFlow$$inlined$filterIsInstance$1$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {224}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    Object L$1;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector) {
                    this.f261288d = flowCollector;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        if (obj instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.a.b) {
                            anonymousClass1.label = 1;
                            if (this.f261288d.emit(obj, anonymousClass1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261287d);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super Object> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261287d.collect(new AnonymousClass2(flowCollector), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        };
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @NotNull
    public Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a.c> publishUpdateFlow() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 14)) {
            return (Flow) iPatchRedirector.redirect((short) 14, (Object) this);
        }
        final Flow<com.tencent.mobileqq.guild.feed.publish.publishV2.a> flow = this.publishEvents;
        return new Flow<Object>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishUpdateFlow$$inlined$filterIsInstance$1
            static IPatchRedirector $redirector_;

            /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishUpdateFlow$$inlined$filterIsInstance$1$2, reason: invalid class name */
            @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1\n+ 2 Transform.kt\nkotlinx/coroutines/flow/FlowKt__TransformKt\n*L\n1#1,222:1\n21#2:223\n35#2:224\n22#2:225\n*E\n"})
            public static final class AnonymousClass2<T> implements FlowCollector {
                static IPatchRedirector $redirector_;

                /* JADX INFO: renamed from: d, reason: collision with root package name */
                final /* synthetic */ FlowCollector f261314d;

                /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishUpdateFlow$$inlined$filterIsInstance$1$2$1, reason: invalid class name */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl$publishUpdateFlow$$inlined$filterIsInstance$1$2", f = "FeedPublishServiceV2Impl.kt", i = {}, l = {224}, m = "emit", n = {}, s = {})
                @SourceDebugExtension({"SMAP\nEmitters.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Emitters.kt\nkotlinx/coroutines/flow/FlowKt__EmittersKt$unsafeTransform$1$1$emit$1\n*L\n1#1,222:1\n*E\n"})
                public static final class AnonymousClass1 extends ContinuationImpl {
                    static IPatchRedirector $redirector_;
                    Object L$0;
                    Object L$1;
                    int label;
                    /* synthetic */ Object result;

                    public AnonymousClass1(Continuation continuation) {
                        super(continuation);
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                            return;
                        }
                        iPatchRedirector.redirect((short) 1, (Object) this, (Object) AnonymousClass2.this, (Object) continuation);
                    }

                    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                    @Nullable
                    public final Object invokeSuspend(@NotNull Object obj) {
                        IPatchRedirector iPatchRedirector = $redirector_;
                        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
                        }
                        this.result = obj;
                        this.label |= Integer.MIN_VALUE;
                        return AnonymousClass2.this.emit(null, this);
                    }
                }

                public AnonymousClass2(FlowCollector flowCollector) {
                    this.f261314d = flowCollector;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                        return;
                    }
                    iPatchRedirector.redirect((short) 1, (Object) this, (Object) flowCollector);
                }

                /* JADX WARN: Code duplicated, block: B:13:0x0023  */
                @Override // kotlinx.coroutines.flow.FlowCollector
                @Nullable
                public final Object emit(Object obj, @NotNull Continuation continuation) {
                    AnonymousClass1 anonymousClass1;
                    IPatchRedirector iPatchRedirector = $redirector_;
                    if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                        return iPatchRedirector.redirect((short) 2, (Object) this, obj, (Object) continuation);
                    }
                    if (continuation instanceof AnonymousClass1) {
                        anonymousClass1 = (AnonymousClass1) continuation;
                        int i15 = anonymousClass1.label;
                        if ((i15 & Integer.MIN_VALUE) != 0) {
                            anonymousClass1.label = i15 - Integer.MIN_VALUE;
                        } else {
                            anonymousClass1 = new AnonymousClass1(continuation);
                        }
                    } else {
                        anonymousClass1 = new AnonymousClass1(continuation);
                    }
                    Object obj2 = anonymousClass1.result;
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    int i16 = anonymousClass1.label;
                    if (i16 == 0) {
                        ResultKt.throwOnFailure(obj2);
                        if (obj instanceof com.tencent.mobileqq.guild.feed.publish.publishV2.a.c) {
                            anonymousClass1.label = 1;
                            if (this.f261314d.emit(obj, anonymousClass1) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                    } else {
                        if (i16 != 1) {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                        ResultKt.throwOnFailure(obj2);
                    }
                    return Unit.INSTANCE;
                }
            }

            {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261313d);
            }

            @Override // kotlinx.coroutines.flow.Flow
            @Nullable
            public Object collect(@NotNull FlowCollector<? super Object> flowCollector, @NotNull Continuation continuation) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    return iPatchRedirector2.redirect((short) 2, (Object) this, (Object) flowCollector, (Object) continuation);
                }
                Object objCollect = this.f261313d.collect(new AnonymousClass2(flowCollector), continuation);
                return objCollect == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objCollect : Unit.INSTANCE;
            }
        };
    }

    /* JADX WARN: Code duplicated, block: B:13:0x0042  */
    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @Nullable
    public Object queryFailPublishStateV2s(@NotNull String str, @NotNull String str2, long j15, long j16, @NotNull Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
        C94501 c94501;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 6)) {
            return iPatchRedirector.redirect((short) 6, this, str, str2, Long.valueOf(j15), Long.valueOf(j16), continuation);
        }
        if (continuation instanceof C94501) {
            c94501 = (C94501) continuation;
            int i15 = c94501.label;
            if ((i15 & Integer.MIN_VALUE) != 0) {
                c94501.label = i15 - Integer.MIN_VALUE;
            } else {
                c94501 = new C94501(continuation);
            }
        } else {
            c94501 = new C94501(continuation);
        }
        C94501 c94502 = c94501;
        Object objWithTimeoutOrNull = c94502.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i16 = c94502.label;
        if (i16 == 0) {
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
            C94512 c94512 = new C94512(j15, this, str, str2, null);
            c94502.label = 1;
            objWithTimeoutOrNull = TimeoutKt.withTimeoutOrNull(j16, c94512, c94502);
            if (objWithTimeoutOrNull == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i16 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
        }
        List list = (List) objWithTimeoutOrNull;
        if (list != null) {
            return list;
        }
        List listEmptyList = CollectionsKt.emptyList();
        QLog.e(TAG, 1, "queryFailPublishStateV2s: db query timeout");
        return listEmptyList;
    }

    /* JADX WARN: Code duplicated, block: B:13:0x0034  */
    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @Nullable
    public Object queryGlobalFeedFailPublishStateV2s(long j15, @NotNull Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
        C94531 c94531;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 8)) {
            return iPatchRedirector.redirect((short) 8, this, Long.valueOf(j15), continuation);
        }
        if (continuation instanceof C94531) {
            c94531 = (C94531) continuation;
            int i15 = c94531.label;
            if ((i15 & Integer.MIN_VALUE) != 0) {
                c94531.label = i15 - Integer.MIN_VALUE;
            } else {
                c94531 = new C94531(continuation);
            }
        } else {
            c94531 = new C94531(continuation);
        }
        Object objWithTimeoutOrNull = c94531.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i16 = c94531.label;
        if (i16 == 0) {
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
            QLog.i(TAG, 1, "queryGlobalFeedFailPublishStateV2s: start (global, no guild/channel) timeout=" + j15);
            C94542 c94542 = new C94542(null);
            c94531.label = 1;
            objWithTimeoutOrNull = TimeoutKt.withTimeoutOrNull(j15, c94542, c94531);
            if (objWithTimeoutOrNull == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i16 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
        }
        List list = (List) objWithTimeoutOrNull;
        if (list == null) {
            List listEmptyList = CollectionsKt.emptyList();
            QLog.e(TAG, 1, "queryGlobalFeedFailPublishStateV2s: db query timeout");
            return listEmptyList;
        }
        QLog.i(TAG, 1, "queryGlobalFeedFailPublishStateV2s: success result size=" + list.size());
        return list;
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    public void registerEventListener(@NotNull AbsFeedEditorEventListener listener) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 17)) {
            iPatchRedirector.redirect((short) 17, (Object) this, (Object) listener);
            return;
        }
        Intrinsics.checkNotNullParameter(listener, "listener");
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        IGuildTransactionManageService iGuildTransactionManageService = (IGuildTransactionManageService) (appInterface != null ? appInterface.getRuntimeService(IGuildTransactionManageService.class, "") : null);
        if (iGuildTransactionManageService != null) {
            iGuildTransactionManageService.addTransaction(listener);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    public void unregisterEventListener(@NotNull AbsFeedEditorEventListener listener) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 18)) {
            iPatchRedirector.redirect((short) 18, (Object) this, (Object) listener);
            return;
        }
        Intrinsics.checkNotNullParameter(listener, "listener");
        AppRuntime appRuntimePeekAppRuntime = MobileQQ.sMobileQQ.peekAppRuntime();
        AppInterface appInterface = appRuntimePeekAppRuntime instanceof AppInterface ? (AppInterface) appRuntimePeekAppRuntime : null;
        IGuildTransactionManageService iGuildTransactionManageService = (IGuildTransactionManageService) (appInterface != null ? appInterface.getRuntimeService(IGuildTransactionManageService.class, "") : null);
        if (iGuildTransactionManageService != null) {
            iGuildTransactionManageService.removeTransaction(listener.getId());
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x003c  */
    @Override // com.tencent.mobileqq.guild.feed.publish.publishV2.IFeedPublishServiceV2
    @Nullable
    public Object queryFailPublishStateV2s(@NotNull String str, @NotNull String str2, long j15, @NotNull Continuation<? super List<com.tencent.mobileqq.guild.feed.publish.publishV2.b>> continuation) {
        AnonymousClass4 anonymousClass4;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 7)) {
            return iPatchRedirector.redirect((short) 7, this, str, str2, Long.valueOf(j15), continuation);
        }
        if (continuation instanceof AnonymousClass4) {
            anonymousClass4 = (AnonymousClass4) continuation;
            int i15 = anonymousClass4.label;
            if ((i15 & Integer.MIN_VALUE) != 0) {
                anonymousClass4.label = i15 - Integer.MIN_VALUE;
            } else {
                anonymousClass4 = new AnonymousClass4(continuation);
            }
        } else {
            anonymousClass4 = new AnonymousClass4(continuation);
        }
        AnonymousClass4 anonymousClass5 = anonymousClass4;
        Object objWithTimeoutOrNull = anonymousClass5.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i16 = anonymousClass5.label;
        if (i16 == 0) {
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
            IGProGuildInfo iGProGuildInfoQ = dm.q(str);
            AnonymousClass5 anonymousClass6 = new AnonymousClass5(str, str2, iGProGuildInfoQ != null ? iGProGuildInfoQ.getJoinTime() : Long.MAX_VALUE, null);
            anonymousClass5.label = 1;
            objWithTimeoutOrNull = TimeoutKt.withTimeoutOrNull(j15, anonymousClass6, anonymousClass5);
            if (objWithTimeoutOrNull == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i16 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(objWithTimeoutOrNull);
        }
        List list = (List) objWithTimeoutOrNull;
        if (list != null) {
            return list;
        }
        List listEmptyList = CollectionsKt.emptyList();
        QLog.e(TAG, 1, "queryFailPublishStateV2s: db query timeout");
        return listEmptyList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void invalidateCache(String guildId) {
        this.memoryStorage.d(new Function1<Triple<? extends String, ? extends String, ? extends Integer>, Boolean>(guildId) { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.impl.FeedPublishServiceV2Impl.invalidateCache.1
            static IPatchRedirector $redirector_;
            final /* synthetic */ String $guildId;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
                this.$guildId = guildId;
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector.redirect((short) 1, (Object) this, (Object) guildId);
            }

            @NotNull
            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final Boolean invoke2(@NotNull Triple<String, String, Integer> triple) {
                IPatchRedirector iPatchRedirector = $redirector_;
                if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
                    return (Boolean) iPatchRedirector.redirect((short) 2, (Object) this, (Object) triple);
                }
                Intrinsics.checkNotNullParameter(triple, "<name for destructuring parameter 0>");
                return Boolean.valueOf(Intrinsics.areEqual(triple.component1(), this.$guildId));
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Boolean invoke(Triple<? extends String, ? extends String, ? extends Integer> triple) {
                return invoke2((Triple<String, String, Integer>) triple);
            }
        });
    }
}