package com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper;

import com.tencent.mobileqq.activity.photo.LocalMediaInfo;
import com.tencent.mobileqq.guild.feed.longfeededitor.webview.result.SelectPhotoResult;
import com.tencent.mobileqq.guild.util.Logger;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: loaded from: classes25.dex */
@Metadata(d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "Lkotlinx/coroutines/Deferred;", "Lcom/tencent/mobileqq/guild/feed/longfeededitor/webview/result/SelectPhotoResult;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
@DebugMetadata(c = "com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$handleImageProcess$2", f = "GuildMediaUploadCoroutineHelper.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
final class GuildMediaUploadCoroutineHelper$handleImageProcess$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Deferred<? extends SelectPhotoResult>>, Object> {
    static IPatchRedirector $redirector_;
    final /* synthetic */ com.tencent.mobileqq.guild.feed.longfeededitor.webview.result.b $batchInfo;
    final /* synthetic */ GuildUploadHelper $instance;
    final /* synthetic */ LocalMediaInfo $localMediaInfo;
    final /* synthetic */ String $photoPath;
    final /* synthetic */ String $taskId;
    private /* synthetic */ Object L$0;
    int label;

    /* JADX INFO: renamed from: com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$handleImageProcess$2$1, reason: invalid class name */
    @Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0002\u001a\u0004\u0018\u00010\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "Lcom/tencent/mobileqq/guild/feed/longfeededitor/webview/result/SelectPhotoResult;", "<anonymous>"}, k = 3, mv = {1, 9, 0})
    @DebugMetadata(c = "com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$handleImageProcess$2$1", f = "GuildMediaUploadCoroutineHelper.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    @SourceDebugExtension({"SMAP\nGuildMediaUploadCoroutineHelper.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GuildMediaUploadCoroutineHelper.kt\ncom/tencent/mobileqq/guild/feed/jsbridge/methods/publish/helper/GuildMediaUploadCoroutineHelper$handleImageProcess$2$1\n+ 2 Logger.kt\ncom/tencent/mobileqq/guild/util/Logger\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n*L\n1#1,180:1\n181#2,7:181\n188#2,2:189\n190#2:192\n1855#3:188\n1856#3:191\n*S KotlinDebug\n*F\n+ 1 GuildMediaUploadCoroutineHelper.kt\ncom/tencent/mobileqq/guild/feed/jsbridge/methods/publish/helper/GuildMediaUploadCoroutineHelper$handleImageProcess$2$1\n*L\n79#1:181,7\n79#1:189,2\n79#1:192\n79#1:188\n79#1:191\n*E\n"})
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super SelectPhotoResult>, Object> {
        static IPatchRedirector $redirector_;
        final /* synthetic */ com.tencent.mobileqq.guild.feed.longfeededitor.webview.result.b $batchInfo;
        final /* synthetic */ GuildUploadHelper $instance;
        final /* synthetic */ LocalMediaInfo $localMediaInfo;
        final /* synthetic */ String $photoPath;
        final /* synthetic */ String $taskId;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(GuildUploadHelper guildUploadHelper, LocalMediaInfo localMediaInfo, String str, String str2, com.tencent.mobileqq.guild.feed.longfeededitor.webview.result.b bVar, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$instance = guildUploadHelper;
            this.$localMediaInfo = localMediaInfo;
            this.$taskId = str;
            this.$photoPath = str2;
            this.$batchInfo = bVar;
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
                return;
            }
            iPatchRedirector.redirect((short) 1, this, guildUploadHelper, localMediaInfo, str, str2, bVar, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        @NotNull
        public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? new AnonymousClass1(this.$instance, this.$localMediaInfo, this.$taskId, this.$photoPath, this.$batchInfo, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
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
            try {
                ArrayList arrayList = new ArrayList();
                this.$instance.i(arrayList, this.$localMediaInfo, this.$taskId, this.$photoPath);
                return new SelectPhotoResult(arrayList, this.$instance.g(), this.$batchInfo);
            } catch (Exception e15) {
                Logger logger = Logger.f281727a;
                Logger.b bVar = new Logger.b();
                String str = "handleImageProcess--error:" + e15.getMessage();
                if (str instanceof String) {
                    bVar.a().add(str);
                }
                Iterator<T> it = bVar.a().iterator();
                while (it.hasNext()) {
                    Logger.f281727a.d().e("GuildMediaUploadCoroutineHelper", 1, (String) it.next(), null);
                }
                return null;
            }
        }

        @Override // kotlin.jvm.functions.Function2
        @Nullable
        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
        public final Object mo4invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super SelectPhotoResult> continuation) {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    GuildMediaUploadCoroutineHelper$handleImageProcess$2(GuildUploadHelper guildUploadHelper, LocalMediaInfo localMediaInfo, String str, String str2, com.tencent.mobileqq.guild.feed.longfeededitor.webview.result.b bVar, Continuation<? super GuildMediaUploadCoroutineHelper$handleImageProcess$2> continuation) {
        super(2, continuation);
        this.$instance = guildUploadHelper;
        this.$localMediaInfo = localMediaInfo;
        this.$taskId = str;
        this.$photoPath = str2;
        this.$batchInfo = bVar;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
            return;
        }
        iPatchRedirector.redirect((short) 1, this, guildUploadHelper, localMediaInfo, str, str2, bVar, continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 3)) {
            return (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
        }
        GuildMediaUploadCoroutineHelper$handleImageProcess$2 guildMediaUploadCoroutineHelper$handleImageProcess$2 = new GuildMediaUploadCoroutineHelper$handleImageProcess$2(this.$instance, this.$localMediaInfo, this.$taskId, this.$photoPath, this.$batchInfo, continuation);
        guildMediaUploadCoroutineHelper$handleImageProcess$2.L$0 = obj;
        return guildMediaUploadCoroutineHelper$handleImageProcess$2;
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
        return BuildersKt__Builders_commonKt.async$default((CoroutineScope) this.L$0, null, null, new AnonymousClass1(this.$instance, this.$localMediaInfo, this.$taskId, this.$photoPath, this.$batchInfo, null), 3, null);
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
    public final Object mo4invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Deferred<? extends SelectPhotoResult>> continuation) {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((GuildMediaUploadCoroutineHelper$handleImageProcess$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
    }
}