package com.tencent.mobileqq.guild.feed.publish.publishV2.taskx;

import android.os.Bundle;
import androidx.core.util.Consumer;
import com.tencent.mobileqq.activity.photo.LocalMediaInfo;
import com.tencent.mobileqq.app.AppConstants;
import com.tencent.mobileqq.guild.feed.publish.bean.GuildMediaProgressState;
import com.tencent.mobileqq.guild.feed.publish.bean.GuildTaskProgressState;
import com.tencent.mobileqq.guild.feed.publish.bean.IPostFeedTaskExtElement;
import com.tencent.mobileqq.guild.feed.publish.helper.GuildTaskProgressBoxHelper;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.PostFeedMainTaskFacadeImpl.b;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.FileUploadTaskStateEntity;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.pojo.FileUploadTaskInfoEntity;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.pojo.FileUploadTaskStateEntitiesExtElement;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.pojo.GuildFileTransferModelEntity;
import com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.strategy.impl.UploadTaskStrategiesFactory;
import com.tencent.mobileqq.guild.feed.util.ai;
import com.tencent.mobileqq.guild.report.DummyPerformanceReportTask;
import com.tencent.mobileqq.guild.report.IPerformanceReportTask;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.mobileqq.qfix.redirect.PatchRedirectCenter;
import com.tencent.mobileqq.utils.NetworkUtil;
import com.tencent.qphone.base.util.BaseApplication;
import com.tencent.qphone.base.util.QLog;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProStFeed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt__JobKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes26.dex */
@SourceDebugExtension({"SMAP\nPostFeedMainTaskFacadeImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PostFeedMainTaskFacadeImpl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/taskx/PostFeedMainTaskFacadeImpl\n+ 2 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 3 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 4 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 5 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n+ 6 ClassKt.kt\ncom/tencent/mobileqq/guild/feed/util/ClassKtKt\n+ 7 MiscEx.kt\ncom/tencent/mobileqq/guild/feed/util/MiscExKt\n*L\n1#1,690:1\n484#1,2:723\n486#1:726\n487#1,9:738\n496#1:757\n497#1,3:759\n502#1,4:766\n484#1,2:770\n486#1:773\n487#1,9:785\n496#1:804\n497#1,3:806\n502#1,4:813\n484#1,2:817\n486#1:820\n487#1,9:832\n496#1:851\n497#1,3:853\n502#1,4:860\n1#2:691\n1#2:705\n1#2:763\n1#2:810\n1#2:857\n1#2:888\n1603#3,9:692\n1855#3:701\n766#3:702\n857#3,2:703\n1856#3:706\n1612#3:707\n1238#3,4:710\n1238#3,4:716\n800#3,11:727\n1603#3,9:747\n1855#3:756\n1856#3:764\n1612#3:765\n800#3,11:774\n1603#3,9:794\n1855#3:803\n1856#3:811\n1612#3:812\n800#3,11:821\n1603#3,9:841\n1855#3:850\n1856#3:858\n1612#3:859\n800#3,11:865\n1603#3,9:876\n1855#3:885\n1856#3:889\n1612#3:890\n800#3,11:891\n1179#3,2:902\n1253#3,4:904\n1238#3,4:910\n1855#3,2:914\n1855#3,2:916\n453#4:708\n403#4:709\n453#4:714\n403#4:715\n453#4:908\n403#4:909\n215#5:720\n216#5:722\n29#6:721\n29#6:725\n29#6:772\n29#6:819\n29#6:864\n23#7:758\n24#7:762\n23#7:805\n24#7:809\n23#7:852\n24#7:856\n23#7,2:886\n*S KotlinDebug\n*F\n+ 1 PostFeedMainTaskFacadeImpl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/taskx/PostFeedMainTaskFacadeImpl\n*L\n439#1:723,2\n439#1:726\n439#1:738,9\n439#1:757\n439#1:759,3\n439#1:766,4\n451#1:770,2\n451#1:773\n451#1:785,9\n451#1:804\n451#1:806,3\n451#1:813,4\n467#1:817,2\n467#1:820\n467#1:832,9\n467#1:851\n467#1:853,3\n467#1:860,4\n162#1:705\n439#1:763\n451#1:810\n467#1:857\n495#1:888\n162#1:692,9\n162#1:701\n163#1:702\n163#1:703,2\n162#1:706\n162#1:707\n289#1:710,4\n324#1:716,4\n439#1:727,11\n439#1:747,9\n439#1:756\n439#1:764\n439#1:765\n451#1:774,11\n451#1:794,9\n451#1:803\n451#1:811\n451#1:812\n467#1:821,11\n467#1:841,9\n467#1:850\n467#1:858\n467#1:859\n486#1:865,11\n495#1:876,9\n495#1:885\n495#1:889\n495#1:890\n594#1:891,11\n594#1:902,2\n594#1:904,4\n620#1:910,4\n183#1:914,2\n263#1:916,2\n289#1:708\n289#1:709\n324#1:714\n324#1:715\n620#1:908\n620#1:909\n346#1:720\n346#1:722\n347#1:721\n439#1:725\n451#1:772\n467#1:819\n485#1:864\n439#1:758\n439#1:762\n451#1:805\n451#1:809\n467#1:852\n467#1:856\n496#1:886,2\n*E\n"})
public final class PostFeedMainTaskFacadeImpl implements com.tencent.mobileqq.guild.feed.publish.api.d, com.tencent.mobileqq.guild.feed.publish.api.e {
    static IPatchRedirector $redirector_;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @NotNull
    private final com.tencent.mobileqq.guild.feed.publish.h f261338a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @NotNull
    private final CoroutineScope f261339b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f261340c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    @Nullable
    private volatile Bundle f261341d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    @NotNull
    private final com.tencent.mobileqq.guild.feed.publish.api.d.c f261342e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    @NotNull
    private final ParentTaskContext f261343f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    @NotNull
    private final com.tencent.mobileqq.guild.feed.publish.api.a f261344g;

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    @Nullable
    private com.tencent.mobileqq.guild.feed.publish.api.a f261345h;

    /* JADX INFO: renamed from: i, reason: collision with root package name */
    @Nullable
    private com.tencent.mobileqq.guild.feed.publish.api.a f261346i;

    /* JADX INFO: renamed from: j, reason: collision with root package name */
    @Nullable
    private com.tencent.mobileqq.guild.feed.publish.api.a f261347j;

    /* JADX INFO: renamed from: k, reason: collision with root package name */
    private volatile boolean f261348k;

    /* JADX INFO: renamed from: l, reason: collision with root package name */
    @NotNull
    private final Lazy f261349l;

    /* JADX INFO: renamed from: m, reason: collision with root package name */
    private volatile boolean f261350m;

    /* JADX INFO: renamed from: n, reason: collision with root package name */
    @NotNull
    private final Lazy f261351n;

    /* JADX INFO: renamed from: o, reason: collision with root package name */
    @Nullable
    private Job f261352o;

    /* JADX INFO: loaded from: classes14.dex */
    private static final class a {
        static IPatchRedirector $redirector_;

        public a(DefaultConstructorMarker defaultConstructorMarker) {
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

    /* JADX INFO: Access modifiers changed from: private */
    @SourceDebugExtension({"SMAP\nPostFeedMainTaskFacadeImpl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PostFeedMainTaskFacadeImpl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/taskx/PostFeedMainTaskFacadeImpl$GenericFileUploadTaskListener\n*L\n1#1,690:1\n678#1,9:691\n678#1,9:700\n*S KotlinDebug\n*F\n+ 1 PostFeedMainTaskFacadeImpl.kt\ncom/tencent/mobileqq/guild/feed/publish/publishV2/taskx/PostFeedMainTaskFacadeImpl$GenericFileUploadTaskListener\n*L\n652#1:691,9\n661#1:700,9\n*E\n"})
    final class b implements com.tencent.mobileqq.guild.feed.api.j {
        static IPatchRedirector $redirector_;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final int f261353a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        @NotNull
        private final ConcurrentMap<String, FileUploadTaskInfoEntity> f261354b;

        public b(int i15) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
                iPatchRedirector.redirect((short) 1, (Object) this, (Object) PostFeedMainTaskFacadeImpl.this, i15);
            } else {
                this.f261353a = i15;
                this.f261354b = new ConcurrentHashMap();
            }
        }

        @Override // com.tencent.mobileqq.guild.feed.api.m
        public void a(@NotNull com.tencent.mobileqq.guild.data.media.d info) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 11)) {
                Intrinsics.checkNotNullParameter(info, "info");
            } else {
                iPatchRedirector.redirect((short) 11, (Object) this, (Object) info);
            }
        }

        @Override // com.tencent.mobileqq.guild.feed.api.j
        public void b(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 8)) {
                iPatchRedirector.redirect((short) 8, (Object) this, (Object) fileId, (Object) info);
            } else {
                Intrinsics.checkNotNullParameter(fileId, "fileId");
                Intrinsics.checkNotNullParameter(info, "info");
            }
        }

        @Override // com.tencent.mobileqq.guild.feed.api.j
        public void c(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 6)) {
                iPatchRedirector.redirect((short) 6, (Object) this, (Object) fileId, (Object) info);
                return;
            }
            Intrinsics.checkNotNullParameter(fileId, "fileId");
            Intrinsics.checkNotNullParameter(info, "info");
            this.f261354b.put(fileId, q.f261432a.c(info));
        }

        @Override // com.tencent.mobileqq.guild.feed.api.j
        public void d(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 4)) {
                iPatchRedirector.redirect((short) 4, (Object) this, (Object) fileId, (Object) info);
                return;
            }
            Intrinsics.checkNotNullParameter(fileId, "fileId");
            Intrinsics.checkNotNullParameter(info, "info");
            FileUploadTaskInfoEntity fileUploadTaskInfoEntity = (FileUploadTaskInfoEntity) ((ConcurrentHashMap) this.f261354b).get(fileId);
            if (fileUploadTaskInfoEntity == null) {
                QLog.e("PostFeedTask.Facade", 1, "[updateEntity]: " + PostFeedMainTaskFacadeImpl.this.z() + ", entity is null");
                return;
            }
            ConcurrentMap<String, FileUploadTaskInfoEntity> concurrentMap = this.f261354b;
            FileUploadTaskInfoEntity fileUploadTaskInfoEntityM302clone = fileUploadTaskInfoEntity.m302clone();
            FileUploadTaskStateEntity fileUploadTaskStateEntity = fileUploadTaskInfoEntityM302clone.uploadState;
            if (fileUploadTaskStateEntity != null) {
                fileUploadTaskStateEntity.state = 2;
                fileUploadTaskStateEntity.serverFileId = info.j();
            }
            concurrentMap.put(fileId, fileUploadTaskInfoEntityM302clone);
            PostFeedMainTaskFacadeImpl.x(PostFeedMainTaskFacadeImpl.this);
        }

        @Override // com.tencent.mobileqq.guild.feed.api.j
        public void e(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 5)) {
                iPatchRedirector.redirect((short) 5, (Object) this, (Object) fileId, (Object) info);
                return;
            }
            Intrinsics.checkNotNullParameter(fileId, "fileId");
            Intrinsics.checkNotNullParameter(info, "info");
            FileUploadTaskInfoEntity fileUploadTaskInfoEntity = (FileUploadTaskInfoEntity) ((ConcurrentHashMap) this.f261354b).get(fileId);
            if (fileUploadTaskInfoEntity == null) {
                QLog.e("PostFeedTask.Facade", 1, "[updateEntity]: " + PostFeedMainTaskFacadeImpl.this.z() + ", entity is null");
                return;
            }
            ConcurrentMap<String, FileUploadTaskInfoEntity> concurrentMap = this.f261354b;
            FileUploadTaskInfoEntity fileUploadTaskInfoEntityM302clone = fileUploadTaskInfoEntity.m302clone();
            FileUploadTaskStateEntity fileUploadTaskStateEntity = fileUploadTaskInfoEntityM302clone.uploadState;
            if (fileUploadTaskStateEntity != null) {
                fileUploadTaskStateEntity.state = 3;
            }
            concurrentMap.put(fileId, fileUploadTaskInfoEntityM302clone);
            PostFeedMainTaskFacadeImpl.x(PostFeedMainTaskFacadeImpl.this);
        }

        @Override // com.tencent.mobileqq.guild.feed.api.j
        public void f(@NotNull String str, long j15, long j16, @NotNull com.tencent.mobileqq.guild.data.media.d dVar) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 9)) {
                com.tencent.mobileqq.guild.feed.api.j.a.a(this, str, j15, j16, dVar);
            } else {
                iPatchRedirector.redirect((short) 9, this, str, Long.valueOf(j15), Long.valueOf(j16), dVar);
            }
        }

        @Override // com.tencent.mobileqq.guild.feed.api.j
        public void g(@NotNull String fileId, @Nullable com.tencent.mobileqq.guild.data.media.d dVar) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 7)) {
                iPatchRedirector.redirect((short) 7, (Object) this, (Object) fileId, (Object) dVar);
                return;
            }
            Intrinsics.checkNotNullParameter(fileId, "fileId");
            ((ConcurrentHashMap) this.f261354b).remove(fileId);
            PostFeedMainTaskFacadeImpl.x(PostFeedMainTaskFacadeImpl.this);
        }

        @Override // com.tencent.mobileqq.guild.feed.api.j
        public void h(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
            IPatchRedirector iPatchRedirector = $redirector_;
            if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 10)) {
                iPatchRedirector.redirect((short) 10, (Object) this, (Object) fileId, (Object) info);
            } else {
                Intrinsics.checkNotNullParameter(fileId, "fileId");
                Intrinsics.checkNotNullParameter(info, "info");
            }
        }

        @NotNull
        public final ConcurrentMap<String, FileUploadTaskInfoEntity> i() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? this.f261354b : (ConcurrentMap) iPatchRedirector.redirect((short) 3, (Object) this);
        }

        public final int j() {
            IPatchRedirector iPatchRedirector = $redirector_;
            return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 2)) ? this.f261353a : ((Integer) iPatchRedirector.redirect((short) 2, (Object) this)).intValue();
        }
    }

    static {
        IPatchRedirector redirector = PatchRedirectCenter.getRedirector(133480);
        $redirector_ = redirector;
        if (redirector == null || !redirector.hasPatch((short) 24)) {
            new a(null);
        } else {
            redirector.redirect((short) 24);
        }
    }

    public PostFeedMainTaskFacadeImpl(@NotNull com.tencent.mobileqq.guild.feed.publish.h mainTask) {
        Intrinsics.checkNotNullParameter(mainTask, "mainTask");
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) mainTask);
            return;
        }
        this.f261338a = mainTask;
        this.f261339b = CoroutineScopeKt.CoroutineScope(ai.a().d().plus(JobKt__JobKt.Job$default((Job) null, 1, (Object) null)));
        this.f261342e = new com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.strategy.impl.j(new Function0<Boolean>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.PostFeedMainTaskFacadeImpl$uploadResultInterceptor$1
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
                return (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 2)) ? Boolean.valueOf(this.this$0.f261338a.d()) : (Boolean) iPatchRedirector2.redirect((short) 2, (Object) this);
            }
        });
        ParentTaskContext parentTaskContext = new ParentTaskContext(z(), new Function0<Boolean>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.PostFeedMainTaskFacadeImpl$parentTaskContext$1
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
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 2)) {
                    return Boolean.valueOf(this.this$0.h() == 1);
                }
                return (Boolean) iPatchRedirector2.redirect((short) 2, (Object) this);
            }
        });
        this.f261343f = parentTaskContext;
        this.f261344g = new FeedFileUploadTask("PostFeedTask.Facade.Media", parentTaskContext, UploadTaskStrategiesFactory.f261438a.b(true));
        com.tencent.mobileqq.guild.feed.api.j jVar = new com.tencent.mobileqq.guild.feed.api.j() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.PostFeedMainTaskFacadeImpl$mediaTaskListener$1
            static IPatchRedirector $redirector_;

            {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 1)) {
                    return;
                }
                iPatchRedirector2.redirect((short) 1, (Object) this, (Object) this.f261356a);
            }

            @Override // com.tencent.mobileqq.guild.feed.api.m
            public void a(@NotNull com.tencent.mobileqq.guild.data.media.d info) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 2)) {
                    iPatchRedirector2.redirect((short) 2, (Object) this, (Object) info);
                } else {
                    Intrinsics.checkNotNullParameter(info, "info");
                    BuildersKt__Builders_commonKt.launch$default(this.f261356a.f261339b, null, null, new PostFeedMainTaskFacadeImpl$mediaTaskListener$1$onTaskChange$1(info, null), 3, null);
                }
            }

            @Override // com.tencent.mobileqq.guild.feed.api.j
            public void b(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 5)) {
                    iPatchRedirector2.redirect((short) 5, (Object) this, (Object) fileId, (Object) info);
                } else {
                    Intrinsics.checkNotNullParameter(fileId, "fileId");
                    Intrinsics.checkNotNullParameter(info, "info");
                }
            }

            @Override // com.tencent.mobileqq.guild.feed.api.j
            public void c(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 8)) {
                    iPatchRedirector2.redirect((short) 8, (Object) this, (Object) fileId, (Object) info);
                } else {
                    Intrinsics.checkNotNullParameter(fileId, "fileId");
                    Intrinsics.checkNotNullParameter(info, "info");
                }
            }

            @Override // com.tencent.mobileqq.guild.feed.api.j
            public void d(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 9)) {
                    iPatchRedirector2.redirect((short) 9, (Object) this, (Object) fileId, (Object) info);
                } else {
                    Intrinsics.checkNotNullParameter(fileId, "fileId");
                    Intrinsics.checkNotNullParameter(info, "info");
                }
            }

            @Override // com.tencent.mobileqq.guild.feed.api.j
            public void e(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 4)) {
                    iPatchRedirector2.redirect((short) 4, (Object) this, (Object) fileId, (Object) info);
                } else {
                    Intrinsics.checkNotNullParameter(fileId, "fileId");
                    Intrinsics.checkNotNullParameter(info, "info");
                }
            }

            @Override // com.tencent.mobileqq.guild.feed.api.j
            public void f(@NotNull String str, long j15, long j16, @NotNull com.tencent.mobileqq.guild.data.media.d dVar) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 6)) {
                    com.tencent.mobileqq.guild.feed.api.j.a.a(this, str, j15, j16, dVar);
                } else {
                    iPatchRedirector2.redirect((short) 6, this, str, Long.valueOf(j15), Long.valueOf(j16), dVar);
                }
            }

            @Override // com.tencent.mobileqq.guild.feed.api.j
            public void g(@NotNull String fileId, @Nullable com.tencent.mobileqq.guild.data.media.d dVar) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 3)) {
                    iPatchRedirector2.redirect((short) 3, (Object) this, (Object) fileId, (Object) dVar);
                } else {
                    Intrinsics.checkNotNullParameter(fileId, "fileId");
                    BuildersKt__Builders_commonKt.launch$default(this.f261356a.f261339b, null, null, new PostFeedMainTaskFacadeImpl$mediaTaskListener$1$onCancel$1(fileId, null), 3, null);
                }
            }

            @Override // com.tencent.mobileqq.guild.feed.api.j
            public void h(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.media.d info) {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 != null && iPatchRedirector2.hasPatch((short) 7)) {
                    iPatchRedirector2.redirect((short) 7, (Object) this, (Object) fileId, (Object) info);
                } else {
                    Intrinsics.checkNotNullParameter(fileId, "fileId");
                    Intrinsics.checkNotNullParameter(info, "info");
                }
            }
        };
        this.f261349l = LazyKt.lazy(new Function0<b>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.PostFeedMainTaskFacadeImpl$genericFileTaskListener$2
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
            public final PostFeedMainTaskFacadeImpl.b invoke() {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                return (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 2)) ? this.this$0.new b(1) : (PostFeedMainTaskFacadeImpl.b) iPatchRedirector2.redirect((short) 2, (Object) this);
            }
        });
        this.f261351n = LazyKt.lazy(new Function0<b>() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.PostFeedMainTaskFacadeImpl$audioFileTaskListener$2
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
            public final PostFeedMainTaskFacadeImpl.b invoke() {
                IPatchRedirector iPatchRedirector2 = $redirector_;
                return (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 2)) ? this.this$0.new b(3) : (PostFeedMainTaskFacadeImpl.b) iPatchRedirector2.redirect((short) 2, (Object) this);
            }
        });
        f().b(jVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:36:0x00a2  */
    /* JADX WARN: Code duplicated, block: B:38:0x00a8  */
    /* JADX WARN: Code duplicated, block: B:40:0x00d2  */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Instruction removed from duplicated block: B:38:0x00a8, please report this as an issue */
    public final Object G(com.tencent.mobileqq.guild.feed.publish.api.a aVar, int i15, String str, Continuation<? super Pair<Integer, ? extends Collection<FileUploadTaskInfoEntity>>> continuation) {
        PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1 postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1;
        PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl;
        LinkedHashMap linkedHashMap;
        Map map;
        if (continuation instanceof PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1) {
            postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1 = (PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1) continuation;
            int i16 = postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.label;
            if ((i16 & Integer.MIN_VALUE) != 0) {
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.label = i16 - Integer.MIN_VALUE;
            } else {
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1 = new PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1(this, continuation);
            }
        } else {
            postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1 = new PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1(this, continuation);
        }
        Object objG = postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i17 = postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.label;
        boolean z15 = true;
        if (i17 == 0) {
            ResultKt.throwOnFailure(objG);
            if (aVar != null) {
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.L$0 = this;
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.L$1 = str;
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.I$0 = i15;
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.label = 1;
                objG = aVar.g(postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1);
                if (objG == coroutine_suspended) {
                    return coroutine_suspended;
                }
                postFeedMainTaskFacadeImpl = this;
            } else {
                postFeedMainTaskFacadeImpl = this;
            }
            linkedHashMap = null;
            if (linkedHashMap != null && !linkedHashMap.isEmpty()) {
                z15 = false;
            }
            if (z15) {
                return TuplesKt.to(Boxing.boxInt(i15), CollectionsKt.toList(linkedHashMap.values()));
            }
            if (QLog.isColorLevel()) {
                QLog.d("PostFeedTask.Facade", 2, "[" + str + "]: " + postFeedMainTaskFacadeImpl.z() + ", task is null or empty");
            }
            return null;
        }
        if (i17 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        i15 = postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.I$0;
        str = (String) postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.L$1;
        postFeedMainTaskFacadeImpl = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntry$1.L$0;
        ResultKt.throwOnFailure(objG);
        Map map2 = (Map) objG;
        if (map2 == null || (map = MapsKt.toMap(map2)) == null) {
            linkedHashMap = null;
        } else {
            linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(map.size()));
            for (Map.Entry entry : map.entrySet()) {
                linkedHashMap.put(entry.getKey(), q.f261432a.c((com.tencent.mobileqq.guild.data.media.d) entry.getValue()));
            }
        }
        if (linkedHashMap != null) {
            z15 = false;
        }
        if (z15) {
            return TuplesKt.to(Boxing.boxInt(i15), CollectionsKt.toList(linkedHashMap.values()));
        }
        if (QLog.isColorLevel()) {
            QLog.d("PostFeedTask.Facade", 2, "[" + str + "]: " + postFeedMainTaskFacadeImpl.z() + ", task is null or empty");
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:29:0x0074  */
    /* JADX WARN: Code duplicated, block: B:31:0x007a  */
    /* JADX WARN: Code duplicated, block: B:34:0x009c  */
    /* JADX WARN: Code duplicated, block: B:37:0x00e6 A[LOOP:0: B:35:0x00e0->B:37:0x00e6, LOOP_END] */
    /* JADX WARN: Code duplicated, block: B:41:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0013  */
    /* JADX WARN: Instruction removed from duplicated block: B:31:0x007a, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:34:0x009c, please report this as an issue */
    public final Object H(com.tencent.mobileqq.guild.feed.publish.api.a aVar, b bVar, String str, Continuation<? super Pair<Integer, ? extends Collection<FileUploadTaskInfoEntity>>> continuation) {
        PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1 postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1;
        PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl;
        Map mapEmptyMap;
        LinkedHashMap linkedHashMap;
        if (continuation instanceof PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1) {
            postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1 = (PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1) continuation;
            int i15 = postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.label;
            if ((i15 & Integer.MIN_VALUE) != 0) {
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.label = i15 - Integer.MIN_VALUE;
            } else {
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1 = new PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1(this, continuation);
            }
        } else {
            postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1 = new PostFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1(this, continuation);
        }
        Object objG = postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i16 = postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.label;
        if (i16 == 0) {
            ResultKt.throwOnFailure(objG);
            if (aVar != null) {
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.L$0 = this;
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.L$1 = bVar;
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.L$2 = str;
                postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.label = 1;
                objG = aVar.g(postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1);
                if (objG == coroutine_suspended) {
                    return coroutine_suspended;
                }
                postFeedMainTaskFacadeImpl = this;
            } else {
                postFeedMainTaskFacadeImpl = this;
            }
            mapEmptyMap = MapsKt.emptyMap();
            if (mapEmptyMap.isEmpty()) {
                if (QLog.isColorLevel()) {
                    return null;
                }
                QLog.d("PostFeedTask.Facade", 2, "[" + str + "]: " + postFeedMainTaskFacadeImpl.z() + ", taskInfoMap is empty");
                return null;
            }
            QLog.i("PostFeedTask.Facade", 1, "[" + str + "]: " + postFeedMainTaskFacadeImpl.z() + ", taskInfoMap.size=" + mapEmptyMap.size());
            ConcurrentMap<String, FileUploadTaskInfoEntity> concurrentMapI = bVar.i();
            concurrentMapI.clear();
            linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(mapEmptyMap.size()));
            for (Map.Entry entry : mapEmptyMap.entrySet()) {
                linkedHashMap.put(entry.getKey(), q.f261432a.c((com.tencent.mobileqq.guild.data.media.d) entry.getValue()));
            }
            concurrentMapI.putAll(linkedHashMap);
            return TuplesKt.to(Boxing.boxInt(bVar.j()), CollectionsKt.toList(bVar.i().values()));
        }
        if (i16 != 1) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        str = (String) postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.L$2;
        bVar = (b) postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.L$1;
        postFeedMainTaskFacadeImpl = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$makeUploadTaskInfoSaveEntryWithListener$1.L$0;
        ResultKt.throwOnFailure(objG);
        Map map = (Map) objG;
        if (map == null || (mapEmptyMap = MapsKt.toMap(map)) == null) {
            mapEmptyMap = MapsKt.emptyMap();
        }
        if (mapEmptyMap.isEmpty()) {
            if (QLog.isColorLevel()) {
                return null;
            }
            QLog.d("PostFeedTask.Facade", 2, "[" + str + "]: " + postFeedMainTaskFacadeImpl.z() + ", taskInfoMap is empty");
            return null;
        }
        QLog.i("PostFeedTask.Facade", 1, "[" + str + "]: " + postFeedMainTaskFacadeImpl.z() + ", taskInfoMap.size=" + mapEmptyMap.size());
        ConcurrentMap<String, FileUploadTaskInfoEntity> concurrentMapI2 = bVar.i();
        concurrentMapI2.clear();
        linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(mapEmptyMap.size()));
        while (r10.hasNext()) {
            linkedHashMap.put(entry.getKey(), q.f261432a.c((com.tencent.mobileqq.guild.data.media.d) entry.getValue()));
        }
        concurrentMapI2.putAll(linkedHashMap);
        return TuplesKt.to(Boxing.boxInt(bVar.j()), CollectionsKt.toList(bVar.i().values()));
    }

    public static final void m(PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl) {
        boolean z15;
        b[] bVarArr = new b[2];
        b bVar = (b) postFeedMainTaskFacadeImpl.f261349l.getValue();
        if (!postFeedMainTaskFacadeImpl.f261348k) {
            bVar = null;
        }
        bVarArr[0] = bVar;
        b bVar2 = (b) postFeedMainTaskFacadeImpl.f261351n.getValue();
        if (!postFeedMainTaskFacadeImpl.f261350m) {
            bVar2 = null;
        }
        bVarArr[1] = bVar2;
        List<b> listListOfNotNull = CollectionsKt.listOfNotNull((Object[]) bVarArr);
        final ArrayList arrayList = new ArrayList();
        for (b bVar3 : listListOfNotNull) {
            Collection<FileUploadTaskInfoEntity> collectionValues = bVar3.i().values();
            ArrayList arrayList2 = new ArrayList();
            for (Object obj : collectionValues) {
                FileUploadTaskInfoEntity fileUploadTaskInfoEntity = (FileUploadTaskInfoEntity) obj;
                if (fileUploadTaskInfoEntity.fileModel == null) {
                    QLog.e("PostFeedTask.Facade", 1, "[batchSaveAllFileUploadTaskListeners]: " + fileUploadTaskInfoEntity.hashCode() + ", fileModel is null, skip, type=" + bVar3.j());
                    z15 = false;
                } else {
                    z15 = true;
                }
                if (z15) {
                    arrayList2.add(obj);
                }
            }
            if (arrayList2.isEmpty()) {
                arrayList2 = null;
            }
            Pair pair = arrayList2 != null ? TuplesKt.to(Integer.valueOf(bVar3.j()), arrayList2) : null;
            if (pair != null) {
                arrayList.add(pair);
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        GuildTaskProgressBoxHelper.g().m("signalSaveDB", postFeedMainTaskFacadeImpl.z(), new Consumer() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.p
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj2) {
                GuildTaskProgressState guildTaskProgressState = (GuildTaskProgressState) obj2;
                List<Pair> entries = arrayList;
                Intrinsics.checkNotNullParameter(entries, "$entries");
                boolean zIsDevelopLevel = QLog.isDevelopLevel();
                for (Pair pair2 : entries) {
                    int iIntValue = ((Number) pair2.component1()).intValue();
                    List list = (List) pair2.component2();
                    String strJoinToString$default = zIsDevelopLevel ? CollectionsKt.joinToString$default(list, null, null, null, 0, null, PostFeedMainTaskFacadeImpl$batchSaveAllFileUploadTaskListeners$1$1$extraInfo$1.INSTANCE, 31, null) : "";
                    QLog.d("PostFeedTask.Facade", 1, "[batchSaveAllFileUploadTaskListeners]:type=" + iIntValue + ", size=" + list.size() + ", " + strJoinToString$default);
                    Intrinsics.checkNotNull(guildTaskProgressState);
                    com.tencent.mobileqq.guild.feed.publish.helper.o.a(guildTaskProgressState, iIntValue, list);
                }
            }
        });
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0016  */
    public static final Object n(PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl, Continuation continuation) {
        PostFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1 postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1;
        GuildMediaProgressState guildMediaProgressStateX;
        postFeedMainTaskFacadeImpl.getClass();
        if (continuation instanceof PostFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1) {
            postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1 = (PostFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1) continuation;
            int i15 = postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1.label;
            if ((i15 & Integer.MIN_VALUE) != 0) {
                postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1.label = i15 - Integer.MIN_VALUE;
            } else {
                postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1 = new PostFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1(postFeedMainTaskFacadeImpl, continuation);
            }
        } else {
            postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1 = new PostFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1(postFeedMainTaskFacadeImpl, continuation);
        }
        Object objG = postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i16 = postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1.label;
        if (i16 == 0) {
            ResultKt.throwOnFailure(objG);
            com.tencent.mobileqq.guild.feed.publish.api.a aVarF = postFeedMainTaskFacadeImpl.f();
            postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1.L$0 = postFeedMainTaskFacadeImpl;
            postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1.label = 1;
            objG = aVarF.g(postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1);
            if (objG == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i16 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            postFeedMainTaskFacadeImpl = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$bindMediaProgressStatesToThisMainTaskId$1.L$0;
            ResultKt.throwOnFailure(objG);
        }
        Map map = MapsKt.toMap((Map) objG);
        if (map.isEmpty()) {
            if (QLog.isColorLevel()) {
                QLog.d("PostFeedTask.Facade", 2, "[bindMediaProgressStates]: " + postFeedMainTaskFacadeImpl.z() + ",taskInfoMap is empty");
            }
            return Unit.INSTANCE;
        }
        QLog.i("PostFeedTask.Facade", 1, "[bindMediaProgressStates]: " + postFeedMainTaskFacadeImpl.z() + ", taskInfoMap.size=" + map.size());
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            com.tencent.mobileqq.guild.data.media.d dVar = (com.tencent.mobileqq.guild.data.media.d) ((Map.Entry) it.next()).getValue();
            if (!(dVar instanceof com.tencent.mobileqq.guild.feed.data.m)) {
                dVar = null;
            }
            com.tencent.mobileqq.guild.feed.data.m mVar = (com.tencent.mobileqq.guild.feed.data.m) dVar;
            if (mVar != null && (guildMediaProgressStateX = mVar.x(postFeedMainTaskFacadeImpl.z())) != null) {
                com.tencent.mobileqq.guild.feed.publish.api.c.a().b(guildMediaProgressStateX);
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:28:0x008c  */
    /* JADX WARN: Code duplicated, block: B:31:0x009a  */
    /* JADX WARN: Code duplicated, block: B:32:0x009d  */
    /* JADX WARN: Code duplicated, block: B:38:0x00b7  */
    /* JADX WARN: Code duplicated, block: B:41:0x00db  */
    /* JADX WARN: Code duplicated, block: B:43:0x00ed  */
    /* JADX WARN: Code duplicated, block: B:45:0x00f5  */
    /* JADX WARN: Code duplicated, block: B:46:0x00fc  */
    /* JADX WARN: Code duplicated, block: B:48:0x0102  */
    /* JADX WARN: Code duplicated, block: B:50:0x010a  */
    /* JADX WARN: Code duplicated, block: B:51:0x0111  */
    /* JADX WARN: Code duplicated, block: B:53:0x0117  */
    /* JADX WARN: Code duplicated, block: B:55:0x011f  */
    /* JADX WARN: Code duplicated, block: B:60:0x013d  */
    /* JADX WARN: Code duplicated, block: B:61:0x0141  */
    /* JADX WARN: Code duplicated, block: B:63:0x01c8  */
    /* JADX WARN: Code duplicated, block: B:68:0x0126 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:69:0x0126 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:70:0x0126 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:71:0x0126 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x001a  */
    public static final Object o(PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl, Continuation continuation) {
        PostFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1 postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1;
        Map map;
        Map mapEmptyMap;
        PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl2;
        Map map2;
        Map taskInfoMap;
        com.tencent.mobileqq.guild.feed.publish.h mainTask;
        IPatchRedirector iPatchRedirector;
        Iterator it;
        long j15;
        long jA;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i25;
        IPerformanceReportTask iPerformanceReportTaskG;
        String apn;
        IPerformanceReportTask iPerformanceReportTaskReport;
        com.tencent.mobileqq.guild.data.media.d dVar;
        long jA2;
        PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl3 = postFeedMainTaskFacadeImpl;
        postFeedMainTaskFacadeImpl.getClass();
        if (continuation instanceof PostFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1) {
            postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1 = (PostFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1) continuation;
            int i26 = postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.label;
            if ((i26 & Integer.MIN_VALUE) != 0) {
                postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.label = i26 - Integer.MIN_VALUE;
            } else {
                postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1 = new PostFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1(postFeedMainTaskFacadeImpl3, continuation);
            }
        } else {
            postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1 = new PostFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1(postFeedMainTaskFacadeImpl3, continuation);
        }
        Object objG = postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i27 = postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.label;
        if (i27 != 0) {
            if (i27 == 1) {
                postFeedMainTaskFacadeImpl3 = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.L$0;
                ResultKt.throwOnFailure(objG);
            } else {
                if (i27 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                map2 = (Map) postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.L$1;
                postFeedMainTaskFacadeImpl2 = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.L$0;
                ResultKt.throwOnFailure(objG);
            }
            mapEmptyMap = (Map) objG;
            PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl4 = postFeedMainTaskFacadeImpl2;
            map = map2;
            postFeedMainTaskFacadeImpl3 = postFeedMainTaskFacadeImpl4;
            if (mapEmptyMap == null) {
                mapEmptyMap = MapsKt.emptyMap();
            }
            taskInfoMap = MapsKt.plus(map, mapEmptyMap);
            if (taskInfoMap.isEmpty()) {
                return null;
            }
            h hVar = h.f261380a;
            mainTask = postFeedMainTaskFacadeImpl3.f261338a;
            hVar.getClass();
            iPatchRedirector = h.$redirector_;
            if (iPatchRedirector == null && iPatchRedirector.hasPatch((short) 2)) {
                iPerformanceReportTaskReport = (IPerformanceReportTask) iPatchRedirector.redirect((short) 2, (Object) hVar, (Object) mainTask, (Object) taskInfoMap);
            } else {
                Intrinsics.checkNotNullParameter(mainTask, "mainTask");
                Intrinsics.checkNotNullParameter(taskInfoMap, "taskInfoMap");
                it = taskInfoMap.entrySet().iterator();
                j15 = 0;
                jA = 0;
                i15 = 0;
                i16 = 0;
                i17 = 0;
                i18 = 0;
                i19 = 0;
                i25 = 0;
                while (it.hasNext()) {
                    dVar = (com.tencent.mobileqq.guild.data.media.d) ((Map.Entry) it.next()).getValue();
                    if (dVar.i()) {
                        i15++;
                        if (dVar.f()) {
                            i18++;
                            jA2 = dVar.a();
                            j15 += jA2;
                        }
                    } else if (dVar.isVideoTask()) {
                        i16++;
                        if (dVar.f()) {
                            i19++;
                            jA2 = dVar.a();
                            j15 += jA2;
                        }
                    } else if (dVar.d()) {
                        i17++;
                        if (dVar.f()) {
                            i25++;
                            jA2 = dVar.a();
                            j15 += jA2;
                        }
                    }
                    dVar.d();
                    jA += dVar.a();
                }
                iPerformanceReportTaskG = com.tencent.mobileqq.guild.feed.publish.n.i().g(mainTask.c());
                if (iPerformanceReportTaskG == null) {
                    iPerformanceReportTaskReport = DummyPerformanceReportTask.INSTANCE;
                } else {
                    IPerformanceReportTask stageCode = iPerformanceReportTaskG.duplicate(new String[0]).setStageCode("stage_media_group_upload_begin");
                    com.tencent.mobileqq.guild.feed.nativedetail.util.n nVar = com.tencent.mobileqq.guild.feed.nativedetail.util.n.f257581a;
                    IPerformanceReportTask extra = stageCode.setExtra("upload_version", 1).setExtra("publish_task_id", mainTask.c()).setExtra("image_count", Integer.valueOf(i15)).setExtra("video_count", Integer.valueOf(i16)).setExtra("file_count", Integer.valueOf(i17)).setExtra("preload_image_count", Integer.valueOf(i18)).setExtra("preload_video_count", Integer.valueOf(i19)).setExtra("preload_file_count", Integer.valueOf(i25)).setExtra("preload_media_length", Long.valueOf(j15)).setExtra("total_media_length", Long.valueOf(jA));
                    apn = NetworkUtil.getApn(BaseApplication.getContext());
                    if (apn == null) {
                        apn = "";
                    }
                    iPerformanceReportTaskReport = extra.setParam("apn", apn).setParam(AppConstants.Key.GUILD_ID, mainTask.m().getGuildId()).report();
                }
            }
            return iPerformanceReportTaskReport;
        }
        ResultKt.throwOnFailure(objG);
        com.tencent.mobileqq.guild.feed.publish.api.a aVarF = postFeedMainTaskFacadeImpl.f();
        postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.L$0 = postFeedMainTaskFacadeImpl3;
        postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.label = 1;
        objG = aVarF.g(postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1);
        if (objG == coroutine_suspended) {
            return coroutine_suspended;
        }
        Map map3 = MapsKt.toMap((Map) objG);
        com.tencent.mobileqq.guild.feed.publish.api.a aVarB = postFeedMainTaskFacadeImpl3.b();
        if (aVarB != null) {
            postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.L$0 = postFeedMainTaskFacadeImpl3;
            postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.L$1 = map3;
            postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1.label = 2;
            Object objG2 = aVarB.g(postFeedMainTaskFacadeImpl$callReportFileGroupUploadBegin$1);
            if (objG2 == coroutine_suspended) {
                return coroutine_suspended;
            }
            postFeedMainTaskFacadeImpl2 = postFeedMainTaskFacadeImpl3;
            map2 = map3;
            objG = objG2;
            mapEmptyMap = (Map) objG;
            PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl5 = postFeedMainTaskFacadeImpl2;
            map = map2;
            postFeedMainTaskFacadeImpl3 = postFeedMainTaskFacadeImpl5;
        } else {
            map = map3;
            mapEmptyMap = null;
        }
        if (mapEmptyMap == null) {
            mapEmptyMap = MapsKt.emptyMap();
        }
        taskInfoMap = MapsKt.plus(map, mapEmptyMap);
        if (taskInfoMap.isEmpty()) {
            return null;
        }
        h hVar2 = h.f261380a;
        mainTask = postFeedMainTaskFacadeImpl3.f261338a;
        hVar2.getClass();
        iPatchRedirector = h.$redirector_;
        if (iPatchRedirector == null) {
            Intrinsics.checkNotNullParameter(mainTask, "mainTask");
            Intrinsics.checkNotNullParameter(taskInfoMap, "taskInfoMap");
            it = taskInfoMap.entrySet().iterator();
            j15 = 0;
            jA = 0;
            i15 = 0;
            i16 = 0;
            i17 = 0;
            i18 = 0;
            i19 = 0;
            i25 = 0;
            while (it.hasNext()) {
                dVar = (com.tencent.mobileqq.guild.data.media.d) ((Map.Entry) it.next()).getValue();
                if (dVar.i()) {
                    i15++;
                    if (dVar.f()) {
                        i18++;
                        jA2 = dVar.a();
                        j15 += jA2;
                    }
                } else if (dVar.isVideoTask()) {
                    i16++;
                    if (dVar.f()) {
                        i19++;
                        jA2 = dVar.a();
                        j15 += jA2;
                    }
                } else if (dVar.d()) {
                    i17++;
                    if (dVar.f()) {
                        i25++;
                        jA2 = dVar.a();
                        j15 += jA2;
                    }
                }
                dVar.d();
                jA += dVar.a();
            }
            iPerformanceReportTaskG = com.tencent.mobileqq.guild.feed.publish.n.i().g(mainTask.c());
            if (iPerformanceReportTaskG == null) {
                iPerformanceReportTaskReport = DummyPerformanceReportTask.INSTANCE;
            } else {
                IPerformanceReportTask stageCode2 = iPerformanceReportTaskG.duplicate(new String[0]).setStageCode("stage_media_group_upload_begin");
                com.tencent.mobileqq.guild.feed.nativedetail.util.n nVar2 = com.tencent.mobileqq.guild.feed.nativedetail.util.n.f257581a;
                IPerformanceReportTask extra2 = stageCode2.setExtra("upload_version", 1).setExtra("publish_task_id", mainTask.c()).setExtra("image_count", Integer.valueOf(i15)).setExtra("video_count", Integer.valueOf(i16)).setExtra("file_count", Integer.valueOf(i17)).setExtra("preload_image_count", Integer.valueOf(i18)).setExtra("preload_video_count", Integer.valueOf(i19)).setExtra("preload_file_count", Integer.valueOf(i25)).setExtra("preload_media_length", Long.valueOf(j15)).setExtra("total_media_length", Long.valueOf(jA));
                apn = NetworkUtil.getApn(BaseApplication.getContext());
                if (apn == null) {
                    apn = "";
                }
                iPerformanceReportTaskReport = extra2.setParam("apn", apn).setParam(AppConstants.Key.GUILD_ID, mainTask.m().getGuildId()).report();
            }
        } else {
            Intrinsics.checkNotNullParameter(mainTask, "mainTask");
            Intrinsics.checkNotNullParameter(taskInfoMap, "taskInfoMap");
            it = taskInfoMap.entrySet().iterator();
            j15 = 0;
            jA = 0;
            i15 = 0;
            i16 = 0;
            i17 = 0;
            i18 = 0;
            i19 = 0;
            i25 = 0;
            while (it.hasNext()) {
                dVar = (com.tencent.mobileqq.guild.data.media.d) ((Map.Entry) it.next()).getValue();
                if (dVar.i()) {
                    i15++;
                    if (dVar.f()) {
                        i18++;
                        jA2 = dVar.a();
                        j15 += jA2;
                    }
                } else if (dVar.isVideoTask()) {
                    i16++;
                    if (dVar.f()) {
                        i19++;
                        jA2 = dVar.a();
                        j15 += jA2;
                    }
                } else if (dVar.d()) {
                    i17++;
                    if (dVar.f()) {
                        i25++;
                        jA2 = dVar.a();
                        j15 += jA2;
                    }
                }
                dVar.d();
                jA += dVar.a();
            }
            iPerformanceReportTaskG = com.tencent.mobileqq.guild.feed.publish.n.i().g(mainTask.c());
            if (iPerformanceReportTaskG == null) {
                iPerformanceReportTaskReport = DummyPerformanceReportTask.INSTANCE;
            } else {
                IPerformanceReportTask stageCode3 = iPerformanceReportTaskG.duplicate(new String[0]).setStageCode("stage_media_group_upload_begin");
                com.tencent.mobileqq.guild.feed.nativedetail.util.n nVar3 = com.tencent.mobileqq.guild.feed.nativedetail.util.n.f257581a;
                IPerformanceReportTask extra3 = stageCode3.setExtra("upload_version", 1).setExtra("publish_task_id", mainTask.c()).setExtra("image_count", Integer.valueOf(i15)).setExtra("video_count", Integer.valueOf(i16)).setExtra("file_count", Integer.valueOf(i17)).setExtra("preload_image_count", Integer.valueOf(i18)).setExtra("preload_video_count", Integer.valueOf(i19)).setExtra("preload_file_count", Integer.valueOf(i25)).setExtra("preload_media_length", Long.valueOf(j15)).setExtra("total_media_length", Long.valueOf(jA));
                apn = NetworkUtil.getApn(BaseApplication.getContext());
                if (apn == null) {
                    apn = "";
                }
                iPerformanceReportTaskReport = extra3.setParam("apn", apn).setParam(AppConstants.Key.GUILD_ID, mainTask.m().getGuildId()).report();
            }
        }
        return iPerformanceReportTaskReport;
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0016  */
    public static final Object p(PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl, Map map, Continuation continuation) {
        PostFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1 postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1;
        postFeedMainTaskFacadeImpl.getClass();
        if (continuation instanceof PostFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1) {
            postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1 = (PostFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1) continuation;
            int i15 = postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.label;
            if ((i15 & Integer.MIN_VALUE) != 0) {
                postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.label = i15 - Integer.MIN_VALUE;
            } else {
                postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1 = new PostFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1(postFeedMainTaskFacadeImpl, continuation);
            }
        } else {
            postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1 = new PostFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1(postFeedMainTaskFacadeImpl, continuation);
        }
        Object objG = postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i16 = postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.label;
        if (i16 == 0) {
            ResultKt.throwOnFailure(objG);
            com.tencent.mobileqq.guild.feed.publish.api.a aVarF = postFeedMainTaskFacadeImpl.f();
            postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.L$0 = postFeedMainTaskFacadeImpl;
            postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.L$1 = map;
            postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.label = 1;
            objG = aVarF.g(postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1);
            if (objG == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            if (i16 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            map = (Map) postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.L$1;
            postFeedMainTaskFacadeImpl = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$compareAndUpdateSelectedMediaInUploadTask$1.L$0;
            ResultKt.throwOnFailure(objG);
        }
        int size = ((Map) objG).size();
        if (size == map.size()) {
            return Unit.INSTANCE;
        }
        QLog.w("PostFeedTask.Facade", 1, "[compareAndUpdateSelectedMediaInUploadTask]: inconsistency detected!!! sizeOfTaskInfoMap=" + size + ", localMediaList.size=" + map.size());
        if (size < map.size()) {
            com.tencent.mobileqq.guild.feed.publish.api.a aVarF2 = postFeedMainTaskFacadeImpl.f();
            q qVar = q.f261432a;
            LinkedHashMap linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(map.size()));
            for (Map.Entry entry : map.entrySet()) {
                linkedHashMap.put(entry.getKey(), new com.tencent.mobileqq.guild.feed.nativedetail.bottom.input.data.b((LocalMediaInfo) entry.getValue()));
            }
            aVarF2.c("compareAndUpdate", qVar.b(linkedHashMap));
        }
        return Unit.INSTANCE;
    }

    public static final Map v(PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl, JSONObject jSONObject) {
        Object objM747constructorimpl;
        postFeedMainTaskFacadeImpl.getClass();
        com.tencent.mobileqq.guild.feed.nativepublish.tool.restore.restorer.media.b bVar = new com.tencent.mobileqq.guild.feed.nativepublish.tool.restore.restorer.media.b();
        com.tencent.mobileqq.guild.feed.nativepublish.tool.restore.b bVar2 = new com.tencent.mobileqq.guild.feed.nativepublish.tool.restore.b();
        try {
            Result.Companion companion = Result.INSTANCE;
            com.tencent.mobileqq.guild.feed.nativepublish.tool.restore.restorer.c.a(bVar, jSONObject, bVar2, null, 4, null);
            objM747constructorimpl = Result.m747constructorimpl(Unit.INSTANCE);
        } catch (Throwable th5) {
            Result.Companion companion2 = Result.INSTANCE;
            objM747constructorimpl = Result.m747constructorimpl(ResultKt.createFailure(th5));
        }
        Throwable thM750exceptionOrNullimpl = Result.m750exceptionOrNullimpl(objM747constructorimpl);
        if (thM750exceptionOrNullimpl != null) {
            QLog.e("PostFeedTask.Facade", 1, "[ValidateConsistency]: restore exception: ", thM750exceptionOrNullimpl);
        }
        List<com.tencent.mobileqq.guild.feed.nativepublish.mediapreview.data.k> listD = bVar2.d();
        ArrayList<com.tencent.mobileqq.guild.feed.nativepublish.mediapreview.data.d> arrayList = new ArrayList();
        for (Object obj : listD) {
            if (obj instanceof com.tencent.mobileqq.guild.feed.nativepublish.mediapreview.data.d) {
                arrayList.add(obj);
            }
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(arrayList, 10)), 16));
        for (com.tencent.mobileqq.guild.feed.nativepublish.mediapreview.data.d dVar : arrayList) {
            Pair pair = TuplesKt.to(dVar.getTaskId(), dVar.k());
            linkedHashMap.put(pair.getFirst(), pair.getSecond());
        }
        return linkedHashMap;
    }

    /* JADX WARN: Code duplicated, block: B:29:0x00da  */
    /* JADX WARN: Code duplicated, block: B:32:0x00e9  */
    /* JADX WARN: Code duplicated, block: B:33:0x00ec  */
    /* JADX WARN: Code duplicated, block: B:38:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:7:0x0016  */
    public static final Object w(PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl, Continuation continuation) {
        PostFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1 postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1;
        Object[] objArr;
        PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl2;
        int i15;
        Object[] objArr2;
        Object[] objArr3;
        PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl3;
        Object[] objArr4;
        Object[] objArr5;
        PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl4;
        final List listListOfNotNull;
        postFeedMainTaskFacadeImpl.getClass();
        if (continuation instanceof PostFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1) {
            postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1 = (PostFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1) continuation;
            int i16 = postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.label;
            if ((i16 & Integer.MIN_VALUE) != 0) {
                postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.label = i16 - Integer.MIN_VALUE;
            } else {
                postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1 = new PostFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1(postFeedMainTaskFacadeImpl, continuation);
            }
        } else {
            postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1 = new PostFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1(postFeedMainTaskFacadeImpl, continuation);
        }
        Object objG = postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i17 = postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.label;
        int i18 = 1;
        int i19 = 2;
        if (i17 != 0) {
            if (i17 == 1) {
                i15 = postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.I$0;
                Object[] objArr6 = (Pair[]) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$2;
                Object[] objArr7 = (Pair[]) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$1;
                postFeedMainTaskFacadeImpl2 = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$0;
                ResultKt.throwOnFailure(objG);
                objArr2 = objArr6;
                objArr = objArr7;
            } else if (i17 == 2) {
                i18 = postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.I$0;
                Object[] objArr8 = (Pair[]) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$2;
                objArr = (Pair[]) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$1;
                postFeedMainTaskFacadeImpl3 = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$0;
                ResultKt.throwOnFailure(objG);
                objArr3 = objArr8;
                objArr3[i18] = objG;
                postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$0 = postFeedMainTaskFacadeImpl3;
                postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$1 = objArr;
                postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$2 = objArr;
                postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.I$0 = 2;
                postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.label = 3;
                objG = postFeedMainTaskFacadeImpl3.H(postFeedMainTaskFacadeImpl3.l(), (b) postFeedMainTaskFacadeImpl3.f261351n.getValue(), "makeAudioUploadTaskInfoSaveEntry", postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1);
                if (objG == coroutine_suspended) {
                    return coroutine_suspended;
                }
                objArr4 = objArr;
                objArr5 = objArr4;
                postFeedMainTaskFacadeImpl4 = postFeedMainTaskFacadeImpl3;
            } else {
                if (i17 != 3) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                i19 = postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.I$0;
                objArr4 = (Pair[]) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$2;
                objArr5 = (Pair[]) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$1;
                postFeedMainTaskFacadeImpl4 = (PostFeedMainTaskFacadeImpl) postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$0;
                ResultKt.throwOnFailure(objG);
            }
            objArr4[i19] = objG;
            listListOfNotNull = CollectionsKt.listOfNotNull(objArr5);
            if (listListOfNotNull.isEmpty()) {
                return Unit.INSTANCE;
            }
            GuildTaskProgressBoxHelper.g().m("saveAllUploadTaskInfo", postFeedMainTaskFacadeImpl4.z(), new Consumer() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.o
                @Override // androidx.core.util.Consumer
                public final void accept(Object obj) {
                    GuildTaskProgressState guildTaskProgressState = (GuildTaskProgressState) obj;
                    List<Pair> entryList = listListOfNotNull;
                    Intrinsics.checkNotNullParameter(entryList, "$entryList");
                    for (Pair pair : entryList) {
                        int iIntValue = ((Number) pair.component1()).intValue();
                        Collection collection = (Collection) pair.component2();
                        Intrinsics.checkNotNull(guildTaskProgressState);
                        com.tencent.mobileqq.guild.feed.publish.helper.o.a(guildTaskProgressState, iIntValue, collection);
                    }
                }
            });
            return Unit.INSTANCE;
        }
        ResultKt.throwOnFailure(objG);
        objArr = new Pair[3];
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$0 = postFeedMainTaskFacadeImpl;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$1 = objArr;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$2 = objArr;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.I$0 = 0;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.label = 1;
        Object objH = postFeedMainTaskFacadeImpl.H(postFeedMainTaskFacadeImpl.b(), (b) postFeedMainTaskFacadeImpl.f261349l.getValue(), "makeGenericFileUploadTaskInfoSaveEntry", postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1);
        if (objH == coroutine_suspended) {
            return coroutine_suspended;
        }
        postFeedMainTaskFacadeImpl2 = postFeedMainTaskFacadeImpl;
        i15 = 0;
        objG = objH;
        objArr2 = objArr;
        objArr2[i15] = objG;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$0 = postFeedMainTaskFacadeImpl2;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$1 = objArr;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$2 = objArr;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.I$0 = 1;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.label = 2;
        objG = postFeedMainTaskFacadeImpl2.G(postFeedMainTaskFacadeImpl2.k(), 2, "saveSecondMediaUploadTaskInfo", postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1);
        if (objG == coroutine_suspended) {
            return coroutine_suspended;
        }
        objArr3 = objArr;
        postFeedMainTaskFacadeImpl3 = postFeedMainTaskFacadeImpl2;
        objArr3[i18] = objG;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$0 = postFeedMainTaskFacadeImpl3;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$1 = objArr;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.L$2 = objArr;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.I$0 = 2;
        postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1.label = 3;
        objG = postFeedMainTaskFacadeImpl3.H(postFeedMainTaskFacadeImpl3.l(), (b) postFeedMainTaskFacadeImpl3.f261351n.getValue(), "makeAudioUploadTaskInfoSaveEntry", postFeedMainTaskFacadeImpl$saveAllUploadTaskInfo$1);
        if (objG == coroutine_suspended) {
            return coroutine_suspended;
        }
        objArr4 = objArr;
        objArr5 = objArr4;
        postFeedMainTaskFacadeImpl4 = postFeedMainTaskFacadeImpl3;
        objArr4[i19] = objG;
        listListOfNotNull = CollectionsKt.listOfNotNull(objArr5);
        if (listListOfNotNull.isEmpty()) {
            return Unit.INSTANCE;
        }
        GuildTaskProgressBoxHelper.g().m("saveAllUploadTaskInfo", postFeedMainTaskFacadeImpl4.z(), new Consumer() { // from class: com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.o
            @Override // androidx.core.util.Consumer
            public final void accept(Object obj) {
                GuildTaskProgressState guildTaskProgressState = (GuildTaskProgressState) obj;
                List<Pair> entryList = listListOfNotNull;
                Intrinsics.checkNotNullParameter(entryList, "$entryList");
                for (Pair pair : entryList) {
                    int iIntValue = ((Number) pair.component1()).intValue();
                    Collection collection = (Collection) pair.component2();
                    Intrinsics.checkNotNull(guildTaskProgressState);
                    com.tencent.mobileqq.guild.feed.publish.helper.o.a(guildTaskProgressState, iIntValue, collection);
                }
            }
        });
        return Unit.INSTANCE;
    }

    public static final void x(PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl) {
        Job job = postFeedMainTaskFacadeImpl.f261352o;
        if (job != null) {
            Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
        }
        postFeedMainTaskFacadeImpl.f261352o = BuildersKt__Builders_commonKt.launch$default(postFeedMainTaskFacadeImpl.f261339b, null, null, new PostFeedMainTaskFacadeImpl$signalSaveDB$1(postFeedMainTaskFacadeImpl, null), 3, null);
    }

    public static final void y(PostFeedMainTaskFacadeImpl postFeedMainTaskFacadeImpl, com.tencent.mobileqq.guild.feed.publish.task.a aVar, JSONObject jSONObject, int i15) {
        Object objM747constructorimpl;
        postFeedMainTaskFacadeImpl.getClass();
        if (aVar.b()) {
            try {
                Result.Companion companion = Result.INSTANCE;
                GProStFeed gProStFeedN = postFeedMainTaskFacadeImpl.f261338a.n();
                if (gProStFeedN != null) {
                    Intrinsics.checkNotNull(gProStFeedN);
                    com.tencent.mobileqq.guild.feed.nativeinterface.kt.b.i(gProStFeedN, jSONObject, i15 == 1);
                } else {
                    gProStFeedN = null;
                }
                objM747constructorimpl = Result.m747constructorimpl(gProStFeedN);
            } catch (Throwable th5) {
                Result.Companion companion2 = Result.INSTANCE;
                objM747constructorimpl = Result.m747constructorimpl(ResultKt.createFailure(th5));
            }
            Throwable thM750exceptionOrNullimpl = Result.m750exceptionOrNullimpl(objM747constructorimpl);
            if (thM750exceptionOrNullimpl != null) {
                QLog.e("PostFeedTask.Facade", 1, "[supplyLocalMediaInfoInFakeFeedIfNeeded]: ", thM750exceptionOrNullimpl);
            }
        }
    }

    public final boolean A() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 13)) {
            return ((Boolean) iPatchRedirector.redirect((short) 13, (Object) this)).booleanValue();
        }
        if (f().a()) {
            return true;
        }
        com.tencent.mobileqq.guild.feed.publish.api.a aVarB = b();
        return aVarB != null && aVarB.a();
    }

    public void B() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 16)) {
            iPatchRedirector.redirect((short) 16, (Object) this);
            return;
        }
        com.tencent.mobileqq.guild.feed.publish.api.a aVarB = b();
        if (aVarB != null && !this.f261348k) {
            this.f261348k = true;
            aVarB.b((b) this.f261349l.getValue());
        }
        com.tencent.mobileqq.guild.feed.publish.api.a aVarL = l();
        if (aVarL != null && !this.f261350m) {
            this.f261350m = true;
            aVarL.b((b) this.f261351n.getValue());
        }
        BuildersKt__Builders_commonKt.launch$default(this.f261339b, null, null, new PostFeedMainTaskFacadeImpl$javaCallCommitUploadTaskInfoToMainTask$1(this, null), 3, null);
    }

    public final void C(@NotNull com.tencent.mobileqq.guild.feed.publish.task.a option, @Nullable String str, @NotNull Runnable onFinish) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 21)) {
            iPatchRedirector.redirect((short) 21, this, option, str, onFinish);
            return;
        }
        Intrinsics.checkNotNullParameter(option, "option");
        Intrinsics.checkNotNullParameter(onFinish, "onFinish");
        BuildersKt__Builders_commonKt.launch$default(this.f261339b, null, null, new PostFeedMainTaskFacadeImpl$javaCallDoAsyncProcessBeforePublish$1(str, this, option, onFinish, null), 3, null);
    }

    public void D(@NotNull com.tencent.mobileqq.guild.feed.publish.api.d.a result, @NotNull Consumer<Boolean> callback) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 19)) {
            iPatchRedirector.redirect((short) 19, (Object) this, (Object) result, (Object) callback);
            return;
        }
        Intrinsics.checkNotNullParameter(result, "result");
        Intrinsics.checkNotNullParameter(callback, "callback");
        BuildersKt__Builders_commonKt.launch$default(this.f261339b, null, null, new PostFeedMainTaskFacadeImpl$javaCallInterceptUploadResult$1(callback, this, result, null), 3, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v13, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r1v14, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r1v18, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r2v25, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r2v31, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r2v5, types: [java.util.List] */
    public void E(@NotNull GuildTaskProgressState taskStateEntity) {
        ?? EmptyList;
        ?? EmptyList2;
        ?? EmptyList3;
        ArrayList<FileUploadTaskInfoEntity> arrayList;
        ArrayList<FileUploadTaskInfoEntity> arrayList2;
        ArrayList<FileUploadTaskInfoEntity> arrayList3;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 20)) {
            iPatchRedirector.redirect((short) 20, (Object) this, (Object) taskStateEntity);
            return;
        }
        Intrinsics.checkNotNullParameter(taskStateEntity, "taskStateEntity");
        f().e(new FeedMediaUploadTaskRestoreInfoQueryFilter().b(taskStateEntity));
        IPostFeedTaskExtElement iPostFeedTaskExtElementFindExtElement = taskStateEntity.findExtElement(1);
        if (!(iPostFeedTaskExtElementFindExtElement instanceof FileUploadTaskStateEntitiesExtElement)) {
            iPostFeedTaskExtElementFindExtElement = null;
        }
        FileUploadTaskStateEntitiesExtElement fileUploadTaskStateEntitiesExtElement = (FileUploadTaskStateEntitiesExtElement) iPostFeedTaskExtElementFindExtElement;
        if (fileUploadTaskStateEntitiesExtElement == null || (arrayList3 = fileUploadTaskStateEntitiesExtElement.states) == null) {
            EmptyList = CollectionsKt.emptyList();
        } else {
            EmptyList = new ArrayList();
            for (Object obj : arrayList3) {
                if (obj instanceof FileUploadTaskInfoEntity) {
                    EmptyList.add(obj);
                }
            }
        }
        if (!EmptyList.isEmpty()) {
            QLog.i("PostFeedTask.Facade", 1, "[restoreGenericFileUploadTask]: " + z() + ", uploadTaskInfoEntities.size=" + EmptyList.size() + ", first=" + CollectionsKt.firstOrNull((List<? extends Object>) EmptyList));
            ArrayList arrayList4 = new ArrayList();
            for (FileUploadTaskInfoEntity fileUploadTaskInfoEntity : (Iterable) EmptyList) {
                com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.e eVarE = q.f261432a.e(z(), fileUploadTaskInfoEntity);
                if (eVarE == null) {
                    String strZ = z();
                    GuildFileTransferModelEntity guildFileTransferModelEntity = fileUploadTaskInfoEntity.fileModel;
                    QLog.e("PostFeedTask.Facade", 1, "[restoreGenericFileUploadTask]: " + strZ + ", entity is null, fileId=" + (guildFileTransferModelEntity != null ? guildFileTransferModelEntity.fileId : null));
                }
                if (eVarE != null) {
                    arrayList4.add(eVarE);
                }
            }
            if (!arrayList4.isEmpty()) {
                e();
                com.tencent.mobileqq.guild.feed.publish.api.a aVarB = b();
                if (aVarB != null) {
                    aVarB.e(arrayList4);
                }
            }
        }
        IPostFeedTaskExtElement iPostFeedTaskExtElementFindExtElement2 = taskStateEntity.findExtElement(2);
        if (!(iPostFeedTaskExtElementFindExtElement2 instanceof FileUploadTaskStateEntitiesExtElement)) {
            iPostFeedTaskExtElementFindExtElement2 = null;
        }
        FileUploadTaskStateEntitiesExtElement fileUploadTaskStateEntitiesExtElement2 = (FileUploadTaskStateEntitiesExtElement) iPostFeedTaskExtElementFindExtElement2;
        if (fileUploadTaskStateEntitiesExtElement2 == null || (arrayList2 = fileUploadTaskStateEntitiesExtElement2.states) == null) {
            EmptyList2 = CollectionsKt.emptyList();
        } else {
            EmptyList2 = new ArrayList();
            for (Object obj2 : arrayList2) {
                if (obj2 instanceof FileUploadTaskInfoEntity) {
                    EmptyList2.add(obj2);
                }
            }
        }
        if (!EmptyList2.isEmpty()) {
            QLog.i("PostFeedTask.Facade", 1, "[restoreSecondaryMediaUploadTask]: " + z() + ", uploadTaskInfoEntities.size=" + EmptyList2.size() + ", first=" + CollectionsKt.firstOrNull((List<? extends Object>) EmptyList2));
            ArrayList arrayList5 = new ArrayList();
            for (FileUploadTaskInfoEntity fileUploadTaskInfoEntity2 : (Iterable) EmptyList2) {
                com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.e eVarE2 = q.f261432a.e(z(), fileUploadTaskInfoEntity2);
                if (eVarE2 == null) {
                    String strZ2 = z();
                    GuildFileTransferModelEntity guildFileTransferModelEntity2 = fileUploadTaskInfoEntity2.fileModel;
                    QLog.e("PostFeedTask.Facade", 1, "[restoreSecondaryMediaUploadTask]: " + strZ2 + ", entity is null, fileId=" + (guildFileTransferModelEntity2 != null ? guildFileTransferModelEntity2.fileId : null));
                }
                if (eVarE2 != null) {
                    arrayList5.add(eVarE2);
                }
            }
            if (!arrayList5.isEmpty()) {
                if (k() == null) {
                    g(UploadTaskStrategiesFactory.f261438a.d(z()));
                }
                com.tencent.mobileqq.guild.feed.publish.api.a aVarK = k();
                if (aVarK != null) {
                    aVarK.e(arrayList5);
                }
            }
        }
        IPostFeedTaskExtElement iPostFeedTaskExtElementFindExtElement3 = taskStateEntity.findExtElement(3);
        if (!(iPostFeedTaskExtElementFindExtElement3 instanceof FileUploadTaskStateEntitiesExtElement)) {
            iPostFeedTaskExtElementFindExtElement3 = null;
        }
        FileUploadTaskStateEntitiesExtElement fileUploadTaskStateEntitiesExtElement3 = (FileUploadTaskStateEntitiesExtElement) iPostFeedTaskExtElementFindExtElement3;
        if (fileUploadTaskStateEntitiesExtElement3 == null || (arrayList = fileUploadTaskStateEntitiesExtElement3.states) == null) {
            EmptyList3 = CollectionsKt.emptyList();
        } else {
            EmptyList3 = new ArrayList();
            for (Object obj3 : arrayList) {
                if (obj3 instanceof FileUploadTaskInfoEntity) {
                    EmptyList3.add(obj3);
                }
            }
        }
        if (EmptyList3.isEmpty()) {
            return;
        }
        QLog.i("PostFeedTask.Facade", 1, "[restoreAudioUploadTask]: " + z() + ", uploadTaskInfoEntities.size=" + EmptyList3.size() + ", first=" + CollectionsKt.firstOrNull((List<? extends Object>) EmptyList3));
        ArrayList arrayList6 = new ArrayList();
        for (FileUploadTaskInfoEntity fileUploadTaskInfoEntity3 : (Iterable) EmptyList3) {
            com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.e eVarE3 = q.f261432a.e(z(), fileUploadTaskInfoEntity3);
            if (eVarE3 == null) {
                String strZ3 = z();
                GuildFileTransferModelEntity guildFileTransferModelEntity3 = fileUploadTaskInfoEntity3.fileModel;
                QLog.e("PostFeedTask.Facade", 1, "[restoreAudioUploadTask]: " + strZ3 + ", entity is null, fileId=" + (guildFileTransferModelEntity3 != null ? guildFileTransferModelEntity3.fileId : null));
            }
            if (eVarE3 != null) {
                arrayList6.add(eVarE3);
            }
        }
        if (true ^ arrayList6.isEmpty()) {
            a();
            com.tencent.mobileqq.guild.feed.publish.api.a aVarL = l();
            if (aVarL != null) {
                aVarL.e(arrayList6);
            }
        }
    }

    public void F(@NotNull Consumer<Object> callback) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 17)) {
            iPatchRedirector.redirect((short) 17, (Object) this, (Object) callback);
        } else {
            Intrinsics.checkNotNullParameter(callback, "callback");
            BuildersKt__Builders_commonKt.launch$default(this.f261339b, null, null, new PostFeedMainTaskFacadeImpl$javaCallUploadTaskAwait$1(this, callback, null), 3, null);
        }
    }

    public void I(@Nullable CancellationException cancellationException) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 23)) {
            iPatchRedirector.redirect((short) 23, (Object) this, (Object) cancellationException);
            return;
        }
        CoroutineScopeKt.cancel(this.f261339b, cancellationException);
        f().cancel(cancellationException);
        com.tencent.mobileqq.guild.feed.publish.api.a aVarB = b();
        if (aVarB != null) {
            aVarB.cancel(cancellationException);
        }
        com.tencent.mobileqq.guild.feed.publish.api.a aVarK = k();
        if (aVarK != null) {
            aVarK.cancel(cancellationException);
        }
        com.tencent.mobileqq.guild.feed.publish.api.a aVarL = l();
        if (aVarL != null) {
            aVarL.cancel(cancellationException);
        }
    }

    public void J(boolean z15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 22)) {
            iPatchRedirector.redirect((short) 22, (Object) this, z15);
            return;
        }
        if (z15) {
            f().release();
            com.tencent.mobileqq.guild.feed.publish.api.a aVarB = b();
            if (aVarB != null) {
                aVarB.release();
            }
            com.tencent.mobileqq.guild.feed.publish.api.a aVarK = k();
            if (aVarK != null) {
                aVarK.release();
            }
            com.tencent.mobileqq.guild.feed.publish.api.a aVarL = l();
            if (aVarL != null) {
                aVarL.release();
            }
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    public void a() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 15)) {
            iPatchRedirector.redirect((short) 15, (Object) this);
            return;
        }
        if (l() != null) {
            return;
        }
        synchronized (this) {
            if (l() == null) {
                com.tencent.mobileqq.guild.feed.publish.api.a aVarC = UploadTaskStrategiesFactory.f261438a.c(z());
                IPatchRedirector iPatchRedirector2 = $redirector_;
                if (iPatchRedirector2 == null || !iPatchRedirector2.hasPatch((short) 12)) {
                    this.f261347j = aVarC;
                } else {
                    iPatchRedirector2.redirect((short) 12, (Object) this, (Object) aVarC);
                }
            }
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    @Nullable
    public com.tencent.mobileqq.guild.feed.publish.api.a b() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 8)) ? this.f261345h : (com.tencent.mobileqq.guild.feed.publish.api.a) iPatchRedirector.redirect((short) 8, (Object) this);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    public void c(@Nullable Bundle bundle) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 6)) {
            this.f261341d = bundle;
        } else {
            iPatchRedirector.redirect((short) 6, (Object) this, (Object) bundle);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    @Nullable
    public Bundle d() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 5)) ? this.f261341d : (Bundle) iPatchRedirector.redirect((short) 5, (Object) this);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    public void e() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 14)) {
            iPatchRedirector.redirect((short) 14, (Object) this);
        } else {
            if (b() != null) {
                return;
            }
            synchronized (this) {
                if (b() == null) {
                    this.f261345h = new FeedFileUploadTask("PostFeedTask.Generic", this.f261343f, UploadTaskStrategiesFactory.f261438a.a());
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    @NotNull
    public com.tencent.mobileqq.guild.feed.publish.api.a f() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 7)) ? this.f261344g : (com.tencent.mobileqq.guild.feed.publish.api.a) iPatchRedirector.redirect((short) 7, (Object) this);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    public void g(@Nullable com.tencent.mobileqq.guild.feed.publish.api.a aVar) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 10)) {
            this.f261346i = aVar;
        } else {
            iPatchRedirector.redirect((short) 10, (Object) this, (Object) aVar);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    public int h() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 3)) ? this.f261340c : ((Integer) iPatchRedirector.redirect((short) 3, (Object) this)).intValue();
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    public void i(int i15) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 4)) {
            this.f261340c = i15;
        } else {
            iPatchRedirector.redirect((short) 4, (Object) this, i15);
        }
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.e
    public void j(@NotNull String traceId, @NotNull Function2<? super Integer, ? super String, Unit> callback) {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 18)) {
            iPatchRedirector.redirect((short) 18, (Object) this, (Object) traceId, (Object) callback);
            return;
        }
        Intrinsics.checkNotNullParameter(traceId, "traceId");
        Intrinsics.checkNotNullParameter(callback, "callback");
        BuildersKt__Builders_commonKt.launch$default(this.f261339b, ai.a().b(), null, new PostFeedMainTaskFacadeImpl$javaCallAsyncPreCheck$1(traceId, this, callback, null), 2, null);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    @Nullable
    public com.tencent.mobileqq.guild.feed.publish.api.a k() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 9)) ? this.f261346i : (com.tencent.mobileqq.guild.feed.publish.api.a) iPatchRedirector.redirect((short) 9, (Object) this);
    }

    @Override // com.tencent.mobileqq.guild.feed.publish.api.d
    @Nullable
    public com.tencent.mobileqq.guild.feed.publish.api.a l() {
        IPatchRedirector iPatchRedirector = $redirector_;
        return (iPatchRedirector == null || !iPatchRedirector.hasPatch((short) 11)) ? this.f261347j : (com.tencent.mobileqq.guild.feed.publish.api.a) iPatchRedirector.redirect((short) 11, (Object) this);
    }

    @NotNull
    public String z() {
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
            return (String) iPatchRedirector.redirect((short) 2, (Object) this);
        }
        String strC = this.f261338a.c();
        return strC == null ? "" : strC;
    }
}