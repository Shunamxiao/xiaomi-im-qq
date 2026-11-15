package com.tencent.mobileqq.guild.feed.publish.publish;

import com.tencent.mobileqq.guild.feed.data.l;
import com.tencent.mobileqq.guild.feed.data.m;
import com.tencent.mobileqq.guild.feed.nativedetail.util.n;
import com.tencent.mobileqq.qfix.redirect.IPatchRedirector;
import com.tencent.qphone.base.util.QLog;
import java.util.Iterator;
import java.util.Map;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes26.dex */
@SourceDebugExtension({"SMAP\nFeedEditorPostPreProcessor.kt\nKotlin\n*S Kotlin\n*F\n+ 1 FeedEditorPostPreProcessor.kt\ncom/tencent/mobileqq/guild/feed/publish/publish/FeedEditorPostPreProcessor\n+ 2 _Sequences.kt\nkotlin/sequences/SequencesKt___SequencesKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 ClassKt.kt\ncom/tencent/mobileqq/guild/feed/util/ClassKtKt\n*L\n1#1,198:1\n1313#2,2:199\n1#3:201\n1864#4,2:202\n1866#4:205\n1864#4,2:206\n1866#4:209\n29#5:204\n29#5:208\n*S KotlinDebug\n*F\n+ 1 FeedEditorPostPreProcessor.kt\ncom/tencent/mobileqq/guild/feed/publish/publish/FeedEditorPostPreProcessor\n*L\n41#1:199,2\n106#1:202,2\n106#1:205\n179#1:206,2\n179#1:209\n107#1:204\n180#1:208\n*E\n"})
public final class FeedEditorPostPreProcessor {
    static IPatchRedirector $redirector_;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @NotNull
    private final String f261186a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    @NotNull
    private final String f261187b;

    public FeedEditorPostPreProcessor(@NotNull String logTag, @NotNull String mainTaskId) {
        Intrinsics.checkNotNullParameter(logTag, "logTag");
        Intrinsics.checkNotNullParameter(mainTaskId, "mainTaskId");
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 1)) {
            iPatchRedirector.redirect((short) 1, (Object) this, (Object) logTag, (Object) mainTaskId);
        } else {
            this.f261186a = logTag;
            this.f261187b = mainTaskId;
        }
    }

    /* JADX WARN: Code duplicated, block: B:100:0x01f8  */
    /* JADX WARN: Code duplicated, block: B:103:0x020c  */
    /* JADX WARN: Code duplicated, block: B:111:0x023c  */
    /* JADX WARN: Code duplicated, block: B:112:0x023f  */
    /* JADX WARN: Code duplicated, block: B:114:0x0242  */
    /* JADX WARN: Code duplicated, block: B:118:0x025b  */
    /* JADX WARN: Code duplicated, block: B:120:0x0263  */
    /* JADX WARN: Code duplicated, block: B:123:0x026c  */
    /* JADX WARN: Code duplicated, block: B:126:0x0271  */
    /* JADX WARN: Code duplicated, block: B:127:0x0276  */
    /* JADX WARN: Code duplicated, block: B:130:0x0293  */
    /* JADX WARN: Code duplicated, block: B:132:0x029a  */
    /* JADX WARN: Code duplicated, block: B:133:0x02a3  */
    /* JADX WARN: Code duplicated, block: B:136:0x02af  */
    /* JADX WARN: Code duplicated, block: B:137:0x02b1  */
    /* JADX WARN: Code duplicated, block: B:140:0x02b5  */
    /* JADX WARN: Code duplicated, block: B:142:0x02b8  */
    /* JADX WARN: Code duplicated, block: B:152:0x02d8  */
    /* JADX WARN: Code duplicated, block: B:153:0x02dd  */
    /* JADX WARN: Code duplicated, block: B:155:0x02e0  */
    /* JADX WARN: Code duplicated, block: B:158:0x02eb  */
    /* JADX WARN: Code duplicated, block: B:161:0x02f3  */
    /* JADX WARN: Code duplicated, block: B:163:0x0309  */
    /* JADX WARN: Code duplicated, block: B:164:0x030b  */
    /* JADX WARN: Code duplicated, block: B:166:0x030e  */
    /* JADX WARN: Code duplicated, block: B:171:0x0319  */
    /* JADX WARN: Code duplicated, block: B:178:0x032f  */
    /* JADX WARN: Code duplicated, block: B:179:0x0331  */
    /* JADX WARN: Code duplicated, block: B:181:0x0334  */
    /* JADX WARN: Code duplicated, block: B:183:0x033e  */
    /* JADX WARN: Code duplicated, block: B:184:0x0340  */
    /* JADX WARN: Code duplicated, block: B:187:0x0344  */
    /* JADX WARN: Code duplicated, block: B:189:0x034a  */
    /* JADX WARN: Code duplicated, block: B:219:0x035c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:221:0x035c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:222:0x035c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:224:0x035c A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:225:0x0279 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:228:0x02bb A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:97:0x01f0  */
    /* JADX WARN: Code duplicated, block: B:98:0x01f5  */
    public final void a(@NotNull JSONObject feed, @NotNull com.tencent.mobileqq.guild.feed.publish.api.d.a result, @NotNull Map<String, String> overrideCoverMap) throws JSONException {
        com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b bVarA;
        Map<String, com.tencent.mobileqq.guild.data.media.d> mapEmptyMap;
        JSONArray jSONArrayOptJSONArray;
        int i15;
        int i16;
        com.tencent.mobileqq.guild.data.media.d dVar;
        m mVar;
        l lVarM;
        JSONObject jSONObjectOptJSONObject;
        JSONObject jSONObject;
        String strB;
        boolean z15;
        JSONArray jSONArrayOptJSONArray2;
        com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b bVarD;
        Map<String, com.tencent.mobileqq.guild.data.media.d> mapEmptyMap2;
        int length;
        int i17;
        JSONObject jSONObjectOptJSONObject2;
        String strOptString;
        String str;
        boolean z16;
        boolean z17;
        com.tencent.mobileqq.guild.data.media.d dVar2;
        boolean z18;
        boolean z19;
        JSONObject jSONObjectOptJSONObject3;
        JSONArray jSONArrayOptJSONArray3;
        Sequence<JSONObject> sequenceA;
        Sequence sequenceFilterNotNull;
        Sequence sequenceFilter;
        IPatchRedirector iPatchRedirector = $redirector_;
        if (iPatchRedirector != null && iPatchRedirector.hasPatch((short) 2)) {
            iPatchRedirector.redirect((short) 2, this, feed, result, overrideCoverMap);
            return;
        }
        Intrinsics.checkNotNullParameter(feed, "feed");
        Intrinsics.checkNotNullParameter(result, "result");
        Intrinsics.checkNotNullParameter(overrideCoverMap, "overrideCoverMap");
        com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b bVarB = result.b();
        Map<String, com.tencent.mobileqq.guild.data.media.d> mapH = bVarB != null ? bVarB.h() : null;
        if (mapH == null) {
            mapH = MapsKt.emptyMap();
        }
        com.tencent.mobileqq.guild.feed.publish.publishV2.taskx.model.b bVarD2 = result.d();
        Map<String, com.tencent.mobileqq.guild.data.media.d> mapH2 = bVarD2 != null ? bVarD2.h() : null;
        if (mapH2 == null) {
            mapH2 = MapsKt.emptyMap();
        }
        JSONObject jSONObjectOptJSONObject4 = feed.optJSONObject("contents");
        String str2 = this.f261186a;
        String str3 = this.f261187b;
        if (jSONObjectOptJSONObject4 != null && (jSONArrayOptJSONArray3 = jSONObjectOptJSONObject4.optJSONArray("contents")) != null && (sequenceA = n.f257581a.a(jSONArrayOptJSONArray3)) != null && (sequenceFilterNotNull = SequencesKt.filterNotNull(sequenceA)) != null && (sequenceFilter = SequencesKt.filter(sequenceFilterNotNull, FeedEditorPostPreProcessor$preprocessRewriteJSONObject$1.INSTANCE)) != null) {
            Iterator it = sequenceFilter.iterator();
            while (it.hasNext()) {
                JSONObject jSONObjectOptJSONObject5 = ((JSONObject) it.next()).optJSONObject("url_content");
                if (jSONObjectOptJSONObject5 != null) {
                    int i18 = com.tencent.mobileqq.guild.feed.nativedetail.bottom.utils.i.l.f256178a;
                    Object objRemove = jSONObjectOptJSONObject5.remove("media_task_id");
                    if (objRemove instanceof String) {
                        if (!(((CharSequence) objRemove).length() == 0)) {
                            com.tencent.mobileqq.guild.data.media.d dVar3 = mapH2.get(objRemove);
                            String url = dVar3 != null ? dVar3.getUrl() : null;
                            if (!(url == null || url.length() == 0)) {
                                switch (jSONObjectOptJSONObject5.optInt("type")) {
                                    case 6:
                                        JSONObject jSONObjectOptJSONObject6 = jSONObjectOptJSONObject5.optJSONObject("third_share_info");
                                        if (jSONObjectOptJSONObject6 != null) {
                                            jSONObjectOptJSONObject6.put("image_url", url);
                                        }
                                        break;
                                    case 7:
                                    case 8:
                                    case 9:
                                        JSONObject jSONObjectOptJSONObject7 = jSONObjectOptJSONObject5.optJSONObject("third_video_info");
                                        if (jSONObjectOptJSONObject7 != null) {
                                            jSONObjectOptJSONObject7.put("cover", url);
                                        }
                                        break;
                                }
                            } else {
                                QLog.e(str2, 1, "[replaceImageUrlToUploadResult]: mainTaskId=" + str3 + ", mediaTaskId=" + objRemove + ", imageUrl is empty, taskInfo=" + (dVar3 != null ? com.tencent.mobileqq.guild.data.media.e.a(dVar3) : null));
                            }
                        }
                    }
                    QLog.e(str2, 1, "[replaceImageUrlToUploadResult]: mainTaskId=" + str3 + ", no task id provided");
                }
            }
        }
        JSONArray jSONArrayOptJSONArray4 = feed.optJSONArray("files");
        if (jSONArrayOptJSONArray4 == null) {
            jSONArrayOptJSONArray4 = new JSONArray();
            feed.put("files", jSONArrayOptJSONArray4);
        }
        if (mapH.isEmpty() && jSONArrayOptJSONArray4.length() > 0) {
            QLog.e(str2, 1, "[preprocessRewriteJSONObject](" + str3 + "): fileTaskMap is empty, but stFilesJSONArray is not empty");
        }
        int i19 = 0;
        for (Object obj : mapH.values()) {
            int i25 = i19 + 1;
            if (i19 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            com.tencent.mobileqq.guild.data.media.d dVar4 = (com.tencent.mobileqq.guild.data.media.d) obj;
            if (!(dVar4 instanceof m)) {
                dVar4 = null;
            }
            m mVar2 = (m) dVar4;
            l lVarM2 = mVar2 != null ? mVar2.m() : null;
            if (lVarM2 == null) {
                QLog.e(str2, 1, "[preprocessRewriteJSONObject]: mainTaskId=" + str3 + ", index=" + i19 + ", fileUploadResult is empty, but fileTaskMap is not empty");
                bVarA = result.a();
                if (bVarA != null) {
                    mapEmptyMap = bVarA.h();
                } else {
                    mapEmptyMap = null;
                }
                if (mapEmptyMap == null) {
                    mapEmptyMap = MapsKt.emptyMap();
                }
                jSONArrayOptJSONArray = feed.optJSONArray("audios");
                if (mapEmptyMap.isEmpty()) {
                    if (jSONArrayOptJSONArray == null) {
                        jSONArrayOptJSONArray = new JSONArray();
                        feed.put("audios", jSONArrayOptJSONArray);
                    }
                    i15 = 0;
                    for (Object obj2 : mapEmptyMap.values()) {
                        i16 = i15 + 1;
                        if (i15 < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        dVar = (com.tencent.mobileqq.guild.data.media.d) obj2;
                        if (!(dVar instanceof m)) {
                            dVar = null;
                        }
                        mVar = (m) dVar;
                        if (mVar != null) {
                            lVarM = mVar.m();
                        } else {
                            lVarM = null;
                        }
                        if (lVarM == null) {
                            QLog.e(str2, 1, "[writeFileIdToStAudioIfNeeded]: mainTaskId=" + str3 + ", index=" + i15 + ", fileUploadResult is empty, but fileTaskMap is not empty");
                        } else {
                            jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i15);
                            if (jSONObjectOptJSONObject == null) {
                                jSONObject = new JSONObject();
                                jSONArrayOptJSONArray.put(i15, jSONObject);
                            } else {
                                jSONObject = jSONObjectOptJSONObject;
                            }
                            strB = lVarM.b();
                            if (strB.length() == 0) {
                                z15 = true;
                            } else {
                                z15 = false;
                            }
                            if (z15) {
                                strB = null;
                            }
                            if (strB != null) {
                                jSONObject.put("file_id", strB);
                            }
                            i15 = i16;
                        }
                    }
                } else if (jSONArrayOptJSONArray == null && jSONArrayOptJSONArray.length() > 0) {
                    JSONObject jSONObjectOptJSONObject8 = jSONArrayOptJSONArray.optJSONObject(0);
                    QLog.e(str2, 1, "[writeFileIdToStAudioIfNeeded]: mainTaskId=" + str3 + ", fileTaskMap is empty, but stAudios is not empty,  fileIdInStAudio=" + (jSONObjectOptJSONObject8 != null ? jSONObjectOptJSONObject8.optString("file_id") : null));
                }
                if (overrideCoverMap.isEmpty() && (jSONArrayOptJSONArray2 = feed.optJSONArray("videos")) != null) {
                    bVarD = result.d();
                    if (bVarD != null) {
                        mapEmptyMap2 = bVarD.h();
                    } else {
                        mapEmptyMap2 = null;
                    }
                    if (mapEmptyMap2 == null) {
                        mapEmptyMap2 = MapsKt.emptyMap();
                    }
                    length = jSONArrayOptJSONArray2.length();
                    for (i17 = 0; i17 < length; i17++) {
                        jSONObjectOptJSONObject2 = jSONArrayOptJSONArray2.optJSONObject(i17);
                        if (jSONObjectOptJSONObject2 == null) {
                            strOptString = jSONObjectOptJSONObject2.optString("fileId");
                            str = overrideCoverMap.get(strOptString);
                            Intrinsics.checkNotNull(strOptString);
                            if (strOptString.length() == 0) {
                                z16 = true;
                            } else {
                                z16 = false;
                            }
                            if (z16) {
                                if (str != null || str.length() == 0) {
                                    z17 = true;
                                } else {
                                    z17 = false;
                                }
                                if (!z17 && (dVar2 = mapEmptyMap2.get(str)) != null) {
                                    if (dVar2.getUrl().length() == 0) {
                                        z18 = true;
                                    } else {
                                        z18 = false;
                                    }
                                    if (z18) {
                                        if (dVar2.getMd5().length() == 0) {
                                            z19 = true;
                                        } else {
                                            z19 = false;
                                        }
                                        if (!z19) {
                                            jSONObjectOptJSONObject3 = jSONObjectOptJSONObject2.optJSONObject("cover");
                                            if (jSONObjectOptJSONObject3 == null) {
                                                jSONObjectOptJSONObject3 = new JSONObject();
                                                jSONObjectOptJSONObject2.put("cover", jSONObjectOptJSONObject3);
                                            }
                                            jSONObjectOptJSONObject3.put("imageMD5", dVar2.getMd5());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return;
            }
            JSONObject jSONObjectOptJSONObject9 = jSONArrayOptJSONArray4.optJSONObject(i19);
            if (jSONObjectOptJSONObject9 == null) {
                jSONObjectOptJSONObject9 = new JSONObject();
                jSONArrayOptJSONArray4.put(i19, jSONObjectOptJSONObject9);
            }
            jSONObjectOptJSONObject9.put("file_id", lVarM2.b());
            i19 = i25;
        }
        bVarA = result.a();
        if (bVarA != null) {
            mapEmptyMap = bVarA.h();
        } else {
            mapEmptyMap = null;
        }
        if (mapEmptyMap == null) {
            mapEmptyMap = MapsKt.emptyMap();
        }
        jSONArrayOptJSONArray = feed.optJSONArray("audios");
        if (mapEmptyMap.isEmpty()) {
            if (jSONArrayOptJSONArray == null) {
                jSONArrayOptJSONArray = new JSONArray();
                feed.put("audios", jSONArrayOptJSONArray);
            }
            i15 = 0;
            while (r0.hasNext()) {
                i16 = i15 + 1;
                if (i15 < 0) {
                    CollectionsKt.throwIndexOverflow();
                }
                dVar = (com.tencent.mobileqq.guild.data.media.d) obj2;
                if (!(dVar instanceof m)) {
                    dVar = null;
                }
                mVar = (m) dVar;
                if (mVar != null) {
                    lVarM = mVar.m();
                } else {
                    lVarM = null;
                }
                if (lVarM == null) {
                    QLog.e(str2, 1, "[writeFileIdToStAudioIfNeeded]: mainTaskId=" + str3 + ", index=" + i15 + ", fileUploadResult is empty, but fileTaskMap is not empty");
                } else {
                    jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i15);
                    if (jSONObjectOptJSONObject == null) {
                        jSONObject = new JSONObject();
                        jSONArrayOptJSONArray.put(i15, jSONObject);
                    } else {
                        jSONObject = jSONObjectOptJSONObject;
                    }
                    strB = lVarM.b();
                    if (strB.length() == 0) {
                        z15 = true;
                    } else {
                        z15 = false;
                    }
                    if (z15) {
                        strB = null;
                    }
                    if (strB != null) {
                        jSONObject.put("file_id", strB);
                    }
                    i15 = i16;
                }
            }
        } else if (jSONArrayOptJSONArray == null) {
        }
        if (overrideCoverMap.isEmpty()) {
            return;
        }
        bVarD = result.d();
        if (bVarD != null) {
            mapEmptyMap2 = bVarD.h();
        } else {
            mapEmptyMap2 = null;
        }
        if (mapEmptyMap2 == null) {
            mapEmptyMap2 = MapsKt.emptyMap();
        }
        length = jSONArrayOptJSONArray2.length();
        while (i17 < length) {
            jSONObjectOptJSONObject2 = jSONArrayOptJSONArray2.optJSONObject(i17);
            if (jSONObjectOptJSONObject2 == null) {
                strOptString = jSONObjectOptJSONObject2.optString("fileId");
                str = overrideCoverMap.get(strOptString);
                Intrinsics.checkNotNull(strOptString);
                if (strOptString.length() == 0) {
                    z16 = true;
                } else {
                    z16 = false;
                }
                if (z16) {
                    if (str != null) {
                        z17 = true;
                    } else {
                        z17 = true;
                    }
                    if (!z17) {
                        if (dVar2.getUrl().length() == 0) {
                            z18 = true;
                        } else {
                            z18 = false;
                        }
                        if (z18) {
                            if (dVar2.getMd5().length() == 0) {
                                z19 = true;
                            } else {
                                z19 = false;
                            }
                            if (!z19) {
                                jSONObjectOptJSONObject3 = jSONObjectOptJSONObject2.optJSONObject("cover");
                                if (jSONObjectOptJSONObject3 == null) {
                                    jSONObjectOptJSONObject3 = new JSONObject();
                                    jSONObjectOptJSONObject2.put("cover", jSONObjectOptJSONObject3);
                                }
                                jSONObjectOptJSONObject3.put("imageMD5", dVar2.getMd5());
                            }
                        }
                    }
                }
            }
        }
    }
}