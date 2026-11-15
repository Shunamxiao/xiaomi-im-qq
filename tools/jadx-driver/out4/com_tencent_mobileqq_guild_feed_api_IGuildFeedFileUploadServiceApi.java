package com.tencent.mobileqq.guild.feed.api;

import com.tencent.mobileqq.profilecard.gallery.historyavatar.ProfileHistoryAvatarLayerFragment;
import com.tencent.mobileqq.qroute.annotation.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import p022mqq.app.api.IRuntimeService;

/* JADX INFO: compiled from: P */
/* JADX INFO: loaded from: classes26.dex */
@Service(process = {""})
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\"\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\bg\u0018\u00002\u00020\u00012\u00020\u0002J\u001c\u0010\b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0005H&J\u0010\u0010\t\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0003H&J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0003H&J\u0010\u0010\u000b\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0003H&J\u001c\u0010\f\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u00032\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0005H&J\"\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00100\u000f2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\rH&J\u001c\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00030\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00030\u0012H&¨\u0006\u0015"}, d2 = {"Lcom/tencent/mobileqq/guild/feed/api/IGuildFeedFileUploadServiceApi;", "", "Lmqq/app/api/IRuntimeService;", "", "fileId", "Lcom/tencent/mobileqq/guild/data/publish/taskx/api/a;", "model", "", "createUploadFileTask", "pauseUploadFileTask", "resumeUploadFileTask", "removeUploadFileTask", "restoreUploadFileTask", "", "fileIdSet", "", "Lcom/tencent/mobileqq/guild/data/media/d;", "batchGetUploadFileTaskInfoList", "", ProfileHistoryAvatarLayerFragment.KEY_FILE_ID_LIST, "batchGetNeedToCreateFileIds", "qqguild-api_release"}, k = 1, mv = {1, 9, 0})
public interface IGuildFeedFileUploadServiceApi extends IRuntimeService {
    @NotNull
    List<String> batchGetNeedToCreateFileIds(@NotNull List<String> fileIdList);

    @NotNull
    Map<String, com.tencent.mobileqq.guild.data.media.d> batchGetUploadFileTaskInfoList(@NotNull Set<String> fileIdSet);

    boolean createUploadFileTask(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model);

    boolean pauseUploadFileTask(@NotNull String fileId);

    boolean removeUploadFileTask(@NotNull String fileId);

    boolean restoreUploadFileTask(@NotNull String fileId, @NotNull com.tencent.mobileqq.guild.data.publish.taskx.api.a<?> model);

    boolean resumeUploadFileTask(@NotNull String fileId);

    /* synthetic */ void subscribeTaskChangeListener(@NotNull m mVar, @NotNull String str);

    /* synthetic */ void unsubscribeAllTaskChangeListener(@NotNull m mVar);

    /* synthetic */ void unsubscribeTaskChangeListener(@NotNull m mVar, @NotNull String str);
}