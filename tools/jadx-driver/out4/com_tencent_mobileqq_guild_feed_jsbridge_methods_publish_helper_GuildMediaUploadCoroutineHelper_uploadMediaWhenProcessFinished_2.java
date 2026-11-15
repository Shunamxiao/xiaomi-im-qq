package com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper;

import com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.s;
import com.tencent.mobileqq.guild.feed.publish.event.GuildOnPhotoSelectedEvent;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.Job;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes26.dex */
@Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "Lkotlinx/coroutines/Job;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
@DebugMetadata(c = "com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2", f = "GuildMediaUploadCoroutineHelper.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
final class GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Job>, Object> {
    static IPatchRedirector $redirector_;
    final /* synthetic */ GuildOnPhotoSelectedEvent $event;
    final /* synthetic */ s $publishConfigMethod;
    final /* synthetic */ Deferred<Object>[] $results;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "<anonymous>"}, k = 3, mv = {1, 9, 0})
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2$1", f = "GuildMediaUploadCoroutineHelper.kt", i = {0, 0, 0, 0}, l = {149}, m = "invokeSuspend", n = {"$this$launch", "$this$forEachIndexed$iv", "index$iv", "idx"}, s = {"L$0", "L$1", "I$0", "I$3"})
    @SourceDebugExtension({"SMAP\nGuildMediaUploadCoroutineHelper.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GuildMediaUploadCoroutineHelper.kt\ncom/tencent/mobileqq/guild/feed/jsbridge/methods/publish/helper/GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2$1\n+ 2 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n+ 3 Logger.kt\ncom/tencent/mobileqq/guild/util/Logger\n*L\n1#1,180:1\n13374#2,2:181\n13376#2:191\n18#3,2:183\n18#3,2:185\n18#3,2:187\n18#3,2:189\n*S KotlinDebug\n*F\n+ 1 GuildMediaUploadCoroutineHelper.kt\ncom/tencent/mobileqq/guild/feed/jsbridge/methods/publish/helper/GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2$1\n*L\n144#1:181,2\n144#1:191\n146#1:183,2\n150#1:185,2\n155#1:187,2\n165#1:189,2\n*E\n"})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        static IPatchRedirector $redirector_;
        final /* synthetic */ GuildOnPhotoSelectedEvent $event;
        final /* synthetic */ s $publishConfigMethod;
        final /* synthetic */ Deferred<Object>[] $results;
        int I$0;
        int I$1;
        int I$2;
        int I$3;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Deferred<Object>[] deferredArr, s sVar, GuildOnPhotoSelectedEvent guildOnPhotoSelectedEvent, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$results = deferredArr;
            this.$publishConfigMethod = sVar;
            this.$event = guildOnPhotoSelectedEvent;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, deferredArr, sVar, guildOnPhotoSelectedEvent, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
                return (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
            }
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.$results, this.$publishConfigMethod, this.$event, continuation);
            anonymousClass1.L$0 = obj;
            return anonymousClass1;
        }

        /* JADX WARN: Code duplicated, block: B:16:0x0067  */
        /* JADX WARN: Code duplicated, block: B:18:0x006d  */
        /* JADX WARN: Code duplicated, block: B:19:0x0088  */
        /* JADX WARN: Code duplicated, block: B:21:0x00a0 A[RETURN] */
        /* JADX WARN: Code duplicated, block: B:22:0x00a1  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:18:0x006d -> B:41:0x0157). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x00a1 -> B:23:0x00a8). Please report as a decompilation issue!!! */
        /*  JADX ERROR: StackOverflowError in pass: RegionMakerVisitor
            java.lang.StackOverflowError
            	at jadx.core.utils.BlockUtils.traverseSuccessorsUntil(BlockUtils.java:731)
            	at jadx.core.utils.BlockUtils.traverseSuccessorsUntil(BlockUtils.java:749)
            */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @org.jetbrains.annotations.Nullable
        public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r19) {
            /*
                Method dump skipped, instruction units count: 353
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
        }

        @Override // kotlin.jvm.functions.Function2
        @Nullable
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
        public final Object mo4invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Unit> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2(Deferred<Object>[] deferredArr, s sVar, GuildOnPhotoSelectedEvent guildOnPhotoSelectedEvent, Continuation<? super GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2> continuation) {
        super(2, continuation);
        this.$results = deferredArr;
        this.$publishConfigMethod = sVar;
        this.$event = guildOnPhotoSelectedEvent;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
            return;
        }
        iPatchRedirector.redirect((short) 1, this, deferredArr, sVar, guildOnPhotoSelectedEvent, continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
            return (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
        }
        GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2 guildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2 = new GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2(this.$results, this.$publishConfigMethod, this.$event, continuation);
        guildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2.L$0 = obj;
        return guildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
            return iPatchRedirector.redirect((short) 2, (Object) this, obj);
        }
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        return BuildersKt__Builders_commonKt.launch$default((CoroutineScope) this.L$0, null, null, new AnonymousClass1(this.$results, this.$publishConfigMethod, this.$event, null), 3, null);
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
    public final Object mo4invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Job> continuation) {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((GuildMediaUploadCoroutineHelper$uploadMediaWhenProcessFinished$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
    }
}