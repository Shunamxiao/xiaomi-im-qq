package com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper;

import com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.s;
import com.tencent.mobileqq.guild.feed.publish.event.GuildOnPhotoSelectedEvent;
import com.tencent.mobileqq.mini.forward.ForwardMiniChooseFriendOption;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes26.dex */
@Metadata(d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\u0010\u0002\u001a\u00020\u0001*\u00020\u0000H\u008a@"}, d2 = {"Lkotlinx/coroutines/CoroutineScope;", "", "<anonymous>"}, k = 3, mv = {1, 9, 0})
@DebugMetadata(c = "com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1", f = "GuildMediaUploadCoroutineHelper.kt", i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 2}, l = {50, 55, 60}, m = "invokeSuspend", n = {ForwardMiniChooseFriendOption.RESULT_LIST, "taskIdMap", "totalImageCnt", "totalVideoCnt", "index$iv", "index", ForwardMiniChooseFriendOption.RESULT_LIST, "taskIdMap", "totalImageCnt", "totalVideoCnt", "index$iv", "index", "taskIdMap"}, s = {"L$0", "L$1", "I$0", "I$1", "I$2", "I$3", "L$0", "L$1", "I$0", "I$1", "I$2", "I$3", "L$0"})
@SourceDebugExtension({"SMAP\nGuildMediaUploadCoroutineHelper.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GuildMediaUploadCoroutineHelper.kt\ncom/tencent/mobileqq/guild/feed/jsbridge/methods/publish/helper/GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 Logger.kt\ncom/tencent/mobileqq/guild/util/Logger\n*L\n1#1,180:1\n1774#2,4:181\n1774#2,4:185\n1864#2,2:189\n1866#2:193\n18#3,2:191\n*S KotlinDebug\n*F\n+ 1 GuildMediaUploadCoroutineHelper.kt\ncom/tencent/mobileqq/guild/feed/jsbridge/methods/publish/helper/GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1\n*L\n35#1:181,4\n36#1:185,4\n37#1:189,2\n37#1:193\n44#1:191,2\n*E\n"})
final class GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    static IPatchRedirector $redirector_;
    final /* synthetic */ GuildOnPhotoSelectedEvent $event;
    final /* synthetic */ GuildUploadHelper $instance;
    final /* synthetic */ s $publishConfigMethod;
    int I$0;
    int I$1;
    int I$2;
    int I$3;
    Object L$0;
    Object L$1;
    Object L$2;
    Object L$3;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1(GuildOnPhotoSelectedEvent guildOnPhotoSelectedEvent, s sVar, GuildUploadHelper guildUploadHelper, Continuation<? super GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1> continuation) {
        super(2, continuation);
        this.$event = guildOnPhotoSelectedEvent;
        this.$publishConfigMethod = sVar;
        this.$instance = guildUploadHelper;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 1)) {
            return;
        }
        iPatchRedirector.redirect((short) 1, this, guildOnPhotoSelectedEvent, sVar, guildUploadHelper, continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? new GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1(this.$event, this.$publishConfigMethod, this.$instance, continuation) : (Continuation) iPatchRedirector.redirect((short) 3, (Object) this, obj, (Object) continuation);
    }

    /* JADX WARN: Code duplicated, block: B:48:0x011d  */
    /* JADX WARN: Code duplicated, block: B:50:0x0125  */
    /* JADX WARN: Code duplicated, block: B:53:0x0148  */
    /* JADX WARN: Code duplicated, block: B:55:0x0170  */
    /* JADX WARN: Code duplicated, block: B:57:0x0183  */
    /* JADX WARN: Code duplicated, block: B:62:0x01ce A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:63:0x01cf  */
    /* JADX WARN: Code duplicated, block: B:65:0x01e4  */
    /* JADX WARN: Code duplicated, block: B:67:0x01f3  */
    /* JADX WARN: Code duplicated, block: B:69:0x0228 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:70:0x0229  */
    /* JADX WARN: Code duplicated, block: B:72:0x023c  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:70:0x0229 -> B:71:0x0232). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:72:0x023c -> B:73:0x0243). Please report as a decompilation issue!!! */
    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r23) {
        /*
            Method dump skipped, instruction units count: 635
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.mobileqq.guild.feed.jsbridge.methods.publish.helper.GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }

    @Override // kotlin.jvm.functions.Function2
    @Nullable
    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method and merged with bridge method [inline-methods] */
    public final Object mo4invoke(@NotNull CoroutineScope coroutineScope, @Nullable Continuation<? super Unit> continuation) {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) ? ((GuildMediaUploadCoroutineHelper$launchBlockingUploadCoroutine$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE) : iPatchRedirector.redirect((short) 4, (Object) this, (Object) coroutineScope, (Object) continuation);
    }
}