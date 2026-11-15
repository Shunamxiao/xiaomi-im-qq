package com.tencent.mobileqq.qqguildsdk.api;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.tencent.mobileqq.qqguildsdk.data.GProLocationInfo;
import com.tencent.mobileqq.qqguildsdk.data.GuildScheduleInfo;
import com.tencent.mobileqq.qqguildsdk.data.IAudioChannelMemberInfos;
import com.tencent.mobileqq.qqguildsdk.data.IGProChannelInfo;
import com.tencent.mobileqq.qqguildsdk.data.IGProClientIdentityInfo;
import com.tencent.mobileqq.qqguildsdk.data.IGProGuildInfo;
import com.tencent.mobileqq.qqguildsdk.data.IGProGuildRoleInfo;
import com.tencent.mobileqq.qqguildsdk.data.IGProOnlineMember;
import com.tencent.mobileqq.qqguildsdk.data.IGuildSpeakableThresholdPermission;
import com.tencent.mobileqq.qqguildsdk.data.ILiveRoomInfo;
import com.tencent.mobileqq.qqguildsdk.data.ay;
import com.tencent.mobileqq.qqguildsdk.data.ci;
import com.tencent.mobileqq.qqguildsdk.data.dv;
import com.tencent.mobileqq.qqguildsdk.data.ed;
import com.tencent.mobileqq.qqguildsdk.data.el;
import com.tencent.mobileqq.qqguildsdk.data.ey;
import com.tencent.mobileqq.qqguildsdk.data.fa;
import com.tencent.mobileqq.qqguildsdk.data.genc.Cdo;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProAIAppAppInfoExt;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProAppChnnPreInfo;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProClientIdentity;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProCmd0xf5aGuildInfo;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProCmd0xf5aUserInfo;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProGetContentRecommendRsp;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProGetRecentVisitGuildInfosFromCacheRsp;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProGlobalBanner;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProGuildUserState;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProInviteInfo;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProJumpToCategoryInfo;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProMVPGuildGroupCard;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProMedal;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProRecommendEssenceSvrRsp;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProScheduleInfo;
import com.tencent.mobileqq.qqguildsdk.data.genc.IGProSimpleProfile;
import com.tencent.mobileqq.qqguildsdk.data.genc.aj;
import com.tencent.mobileqq.qqguildsdk.data.genc.al;
import com.tencent.mobileqq.qqguildsdk.data.genc.an;
import com.tencent.mobileqq.qqguildsdk.data.genc.ao;
import com.tencent.mobileqq.qqguildsdk.data.genc.ar;
import com.tencent.mobileqq.qqguildsdk.data.genc.at;
import com.tencent.mobileqq.qqguildsdk.data.genc.aw;
import com.tencent.mobileqq.qqguildsdk.data.genc.bj;
import com.tencent.mobileqq.qqguildsdk.data.genc.bp;
import com.tencent.mobileqq.qqguildsdk.data.genc.bv;
import com.tencent.mobileqq.qqguildsdk.data.genc.bx;
import com.tencent.mobileqq.qqguildsdk.data.genc.ce;
import com.tencent.mobileqq.qqguildsdk.data.genc.cf;
import com.tencent.mobileqq.qqguildsdk.data.genc.cg;
import com.tencent.mobileqq.qqguildsdk.data.genc.cj;
import com.tencent.mobileqq.qqguildsdk.data.genc.ck;
import com.tencent.mobileqq.qqguildsdk.data.genc.co;
import com.tencent.mobileqq.qqguildsdk.data.genc.cs;
import com.tencent.mobileqq.qqguildsdk.data.genc.ct;
import com.tencent.mobileqq.qqguildsdk.data.genc.cu;
import com.tencent.mobileqq.qqguildsdk.data.genc.cv;
import com.tencent.mobileqq.qqguildsdk.data.genc.cw;
import com.tencent.mobileqq.qqguildsdk.data.genc.cx;
import com.tencent.mobileqq.qqguildsdk.data.genc.cy;
import com.tencent.mobileqq.qqguildsdk.data.genc.dc;
import com.tencent.mobileqq.qqguildsdk.data.genc.dd;
import com.tencent.mobileqq.qqguildsdk.data.genc.de;
import com.tencent.mobileqq.qqguildsdk.data.genc.df;
import com.tencent.mobileqq.qqguildsdk.data.genc.dg;
import com.tencent.mobileqq.qqguildsdk.data.genc.dh;
import com.tencent.mobileqq.qqguildsdk.data.genc.di;
import com.tencent.mobileqq.qqguildsdk.data.genc.dj;
import com.tencent.mobileqq.qqguildsdk.data.genc.dk;
import com.tencent.mobileqq.qqguildsdk.data.genc.dl;
import com.tencent.mobileqq.qqguildsdk.data.genc.dm;
import com.tencent.mobileqq.qqguildsdk.data.genc.dn;
import com.tencent.mobileqq.qqguildsdk.data.genc.dp;
import com.tencent.mobileqq.qqguildsdk.data.genc.dq;
import com.tencent.mobileqq.qqguildsdk.data.genc.dr;
import com.tencent.mobileqq.qqguildsdk.data.genc.ds;
import com.tencent.mobileqq.qqguildsdk.data.genc.dt;
import com.tencent.mobileqq.qqguildsdk.data.genc.du;
import com.tencent.mobileqq.qqguildsdk.data.genc.dw;
import com.tencent.mobileqq.qqguildsdk.data.genc.dx;
import com.tencent.mobileqq.qqguildsdk.data.genc.dz;
import com.tencent.mobileqq.qqguildsdk.data.genc.ea;
import com.tencent.mobileqq.qqguildsdk.data.genc.eb;
import com.tencent.mobileqq.qqguildsdk.data.genc.ec;
import com.tencent.mobileqq.qqguildsdk.data.genc.ee;
import com.tencent.mobileqq.qqguildsdk.data.genc.ef;
import com.tencent.mobileqq.qqguildsdk.data.genc.em;
import com.tencent.mobileqq.qqguildsdk.data.genc.en;
import com.tencent.mobileqq.qqguildsdk.data.genc.ew;
import com.tencent.mobileqq.qqguildsdk.data.genc.ff;
import com.tencent.mobileqq.qqguildsdk.data.genc.fl;
import com.tencent.mobileqq.qqguildsdk.data.genc.fu;
import com.tencent.mobileqq.qqguildsdk.data.genc.fv;
import com.tencent.mobileqq.qqguildsdk.data.genc.fw;
import com.tencent.mobileqq.qqguildsdk.data.genc.fx;
import com.tencent.mobileqq.qqguildsdk.data.genc.fy;
import com.tencent.mobileqq.qqguildsdk.data.genc.ge;
import com.tencent.mobileqq.qqguildsdk.data.genc.gj;
import com.tencent.mobileqq.qqguildsdk.data.genc.gl;
import com.tencent.mobileqq.qqguildsdk.data.genc.gn;
import com.tencent.mobileqq.qqguildsdk.data.genc.go;
import com.tencent.mobileqq.qqguildsdk.data.genc.hd;
import com.tencent.mobileqq.qqguildsdk.data.genc.he;
import com.tencent.mobileqq.qqguildsdk.data.genc.hf;
import com.tencent.mobileqq.qqguildsdk.data.genc.hk;
import com.tencent.mobileqq.qqguildsdk.data.genc.hl;
import com.tencent.mobileqq.qqguildsdk.data.genc.hm;
import com.tencent.mobileqq.qqguildsdk.data.genc.hn;
import com.tencent.mobileqq.qqguildsdk.data.genc.hq;
import com.tencent.mobileqq.qqguildsdk.data.genc.hx;
import com.tencent.mobileqq.qqguildsdk.data.genc.hy;
import com.tencent.mobileqq.qqguildsdk.data.genc.ib;
import com.tencent.mobileqq.qqguildsdk.data.genc.ic;
import com.tencent.mobileqq.qqguildsdk.data.genc.ie;
import com.tencent.mobileqq.qqguildsdk.data.genc.ig;
import com.tencent.mobileqq.qqguildsdk.data.genc.ii;
import com.tencent.mobileqq.qqguildsdk.data.genc.io;
import com.tencent.mobileqq.qqguildsdk.data.genc.ip;
import com.tencent.mobileqq.qqguildsdk.data.genc.is;
import com.tencent.mobileqq.qqguildsdk.data.genc.iv;
import com.tencent.mobileqq.qqguildsdk.data.genc.iw;
import com.tencent.mobileqq.qqguildsdk.data.genc.ix;
import com.tencent.mobileqq.qqguildsdk.data.genc.jc;
import com.tencent.mobileqq.qqguildsdk.data.genc.jd;
import com.tencent.mobileqq.qqguildsdk.data.genc.je;
import com.tencent.mobileqq.qqguildsdk.data.genc.jf;
import com.tencent.mobileqq.qqguildsdk.data.genc.jg;
import com.tencent.mobileqq.qqguildsdk.data.genc.jh;
import com.tencent.mobileqq.qqguildsdk.data.genc.ji;
import com.tencent.mobileqq.qqguildsdk.data.genc.jk;
import com.tencent.mobileqq.qqguildsdk.data.genc.jl;
import com.tencent.mobileqq.qqguildsdk.data.genc.jn;
import com.tencent.mobileqq.qqguildsdk.data.genc.jo;
import com.tencent.mobileqq.qqguildsdk.data.genc.jp;
import com.tencent.mobileqq.qqguildsdk.data.genc.jq;
import com.tencent.mobileqq.qqguildsdk.data.genc.jr;
import com.tencent.mobileqq.qqguildsdk.data.genc.jt;
import com.tencent.mobileqq.qqguildsdk.data.genc.ju;
import com.tencent.mobileqq.qqguildsdk.data.genc.jv;
import com.tencent.mobileqq.qqguildsdk.data.genc.jw;
import com.tencent.mobileqq.qqguildsdk.data.genc.jx;
import com.tencent.mobileqq.qqguildsdk.data.genc.jy;
import com.tencent.mobileqq.qqguildsdk.data.genc.ka;
import com.tencent.mobileqq.qqguildsdk.data.genc.kb;
import com.tencent.mobileqq.qqguildsdk.data.genc.kc;
import com.tencent.mobileqq.qqguildsdk.data.genc.kd;
import com.tencent.mobileqq.qqguildsdk.data.genc.ke;
import com.tencent.mobileqq.qqguildsdk.data.genc.kh;
import com.tencent.mobileqq.qqguildsdk.data.genc.ki;
import com.tencent.mobileqq.qqguildsdk.data.genc.kj;
import com.tencent.mobileqq.qqguildsdk.data.genc.kk;
import com.tencent.mobileqq.qqguildsdk.data.genc.kn;
import com.tencent.mobileqq.qqguildsdk.data.genc.ko;
import com.tencent.mobileqq.qqguildsdk.data.genc.kq;
import com.tencent.mobileqq.qqguildsdk.data.genc.kr;
import com.tencent.mobileqq.qqguildsdk.data.genc.ks;
import com.tencent.mobileqq.qqguildsdk.data.genc.kt;
import com.tencent.mobileqq.qqguildsdk.data.genc.ku;
import com.tencent.mobileqq.qqguildsdk.data.genc.kv;
import com.tencent.mobileqq.qqguildsdk.data.genc.kw;
import com.tencent.mobileqq.qqguildsdk.data.genc.kx;
import com.tencent.mobileqq.qqguildsdk.data.genc.ky;
import com.tencent.mobileqq.qqguildsdk.data.genc.kz;
import com.tencent.mobileqq.qqguildsdk.data.genc.r;
import com.tencent.mobileqq.qqguildsdk.observer.GPServiceObserver;
import com.tencent.mobileqq.qroute.annotation.Service;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProGetGuildAndChannelReq;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProGuild;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProNotice;
import com.tencent.qqnt.kernelgpro.nativeinterface.GProScheduleInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import p022mqq.app.api.IRuntimeService;
import re2.b;
import re2.d;
import re2.e;
import re2.f;
import re2.g;
import se2.a;
import se2.aa;
import se2.bb;
import se2.be;
import se2.bq;
import se2.bs;
import se2.bz;
import se2.c;
import se2.ca;
import se2.cb;
import se2.cc;
import se2.cd;
import se2.cl;
import se2.m;
import se2.u;
import se2.v;
import te2.Cif;
import te2.ab;
import te2.ac;
import te2.ad;
import te2.ae;
import te2.af;
import te2.ag;
import te2.ah;
import te2.ai;
import te2.ak;
import te2.am;
import te2.ap;
import te2.aq;
import te2.as;
import te2.au;
import te2.av;
import te2.ax;
import te2.az;
import te2.ba;
import te2.bc;
import te2.bd;
import te2.bf;
import te2.bg;
import te2.bh;
import te2.bi;
import te2.bk;
import te2.bl;
import te2.bm;
import te2.bn;
import te2.bo;
import te2.br;
import te2.bt;
import te2.bu;
import te2.bw;
import te2.ch;
import te2.cm;
import te2.cn;
import te2.cp;
import te2.cq;
import te2.cr;
import te2.cz;
import te2.da;
import te2.db;
import te2.dy;
import te2.eg;
import te2.eh;
import te2.ei;
import te2.ej;
import te2.ek;
import te2.eo;
import te2.ep;
import te2.eq;
import te2.er;
import te2.es;
import te2.et;
import te2.eu;
import te2.ev;
import te2.ex;
import te2.ez;
import te2.fb;
import te2.fc;
import te2.fd;
import te2.fe;
import te2.fg;
import te2.fh;
import te2.fi;
import te2.fj;
import te2.fk;
import te2.fm;
import te2.fn;
import te2.fo;
import te2.fp;
import te2.fq;
import te2.fr;
import te2.fs;
import te2.ft;
import te2.fz;
import te2.ga;
import te2.gb;
import te2.gc;
import te2.gd;
import te2.gf;
import te2.gg;
import te2.gh;
import te2.gi;
import te2.gk;
import te2.gm;
import te2.gp;
import te2.gq;
import te2.gr;
import te2.gs;
import te2.gt;
import te2.gu;
import te2.gv;
import te2.gw;
import te2.gx;
import te2.gy;
import te2.gz;
import te2.h;
import te2.ha;
import te2.hb;
import te2.hc;
import te2.hg;
import te2.hh;
import te2.hi;
import te2.hj;
import te2.ho;
import te2.hp;
import te2.hr;
import te2.hs;
import te2.ht;
import te2.hu;
import te2.hv;
import te2.hw;
import te2.hz;
import te2.i;
import te2.ia;
import te2.id;
import te2.ih;
import te2.ij;
import te2.ik;
import te2.il;
import te2.im;
import te2.in;
import te2.j;
import te2.k;
import te2.l;
import te2.n;
import te2.o;
import te2.p;
import te2.q;
import te2.s;
import te2.t;
import te2.w;
import te2.x;
import te2.y;
import te2.z;

/* JADX INFO: loaded from: classes.dex */
@Keep
@Service(process = {"all"})
public interface IGPSService extends IRuntimeService, g, f, e, d, b {
    /* synthetic */ void addAgentToGuild(bp bpVar, te2.b bVar);

    /* synthetic */ void addChannelSubscribeBubbleShow(String str, String str2);

    /* synthetic */ void addGuildStateListener(@NonNull bq bqVar);

    /* synthetic */ void addGuildWithOption(ay ayVar, bs bsVar);

    /* synthetic */ void addGuilds(ArrayList arrayList, jt jtVar, fs fsVar);

    /* synthetic */ void addGuildsForGrowth(String str, ArrayList arrayList, fs fsVar);

    /* synthetic */ void addObserver(GPServiceObserver gPServiceObserver);

    /* synthetic */ void addRecentVisitGuildsListSync(ArrayList arrayList, te2.d dVar);

    @Override // re2.b
    /* synthetic */ void addSpeakOrderByUser(kk kkVar, id idVar);

    /* synthetic */ void addUserToGuildBlackList(String str, List list, @Nullable ey eyVar, ca caVar);

    /* synthetic */ void agentClickButton(aw awVar, t tVar);

    /* synthetic */ void appAuthorization(String str, a aVar);

    /* synthetic */ void applyToVisitGuildGroup(al alVar, te2.f fVar);

    /* synthetic */ void authScreenShared(String str, String str2, cl clVar);

    /* synthetic */ void batchAddGuildSearchSearchHistory(ArrayList arrayList, h hVar);

    /* synthetic */ void batchDeleteAISearchSession(r rVar, hp hpVar);

    /* synthetic */ void batchGetBotTypes(an anVar, i iVar);

    /* synthetic */ void batchGetGuildLabelInfo(ao aoVar, j jVar);

    /* synthetic */ void batchGetItemDetail(ef efVar, eg egVar);

    /* synthetic */ void batchSetBoolField(String str, HashMap map, @NonNull bz bzVar);

    /* synthetic */ void batchSetIntField(String str, HashMap map, @NonNull bz bzVar);

    /* synthetic */ void beforehandGetGuildSpeakPermission(long j15, int i15, k kVar);

    /* synthetic */ void bindAppRole(String str, String str2, int i15, c cVar);

    /* synthetic */ void browseGuildHomepage(ar arVar, l lVar);

    /* synthetic */ void canOptimizeGuildCreateInfoByAi(ht htVar);

    /* synthetic */ void cancelChannelTopMsg(String str, String str2, ArrayList arrayList, @NonNull bz bzVar);

    @Override // re2.b
    /* synthetic */ void cancelSpeakOrderByAdmin(aj ajVar, te2.e eVar);

    @Override // re2.b
    /* synthetic */ void cancelUserHandUpForAdmin(aj ajVar, te2.e eVar);

    /* synthetic */ void changeChannelCategoryName(String str, long j15, String str2, be beVar);

    /* synthetic */ void changeGuildAllowSearch(String str, boolean z15, gr grVar);

    @Override // re2.b
    /* synthetic */ void checkAVState(kk kkVar, id idVar);

    /* synthetic */ void checkAndEncryptText(ce ceVar, n nVar);

    /* synthetic */ void checkFeedAbstractInfo(at atVar, o oVar);

    /* synthetic */ void checkIsStrangerNewMember(p pVar);

    /* synthetic */ void checkLobbyAppAuthStatus(String str, long j15, long j16, q qVar);

    /* synthetic */ void checkPreventAddiction(ie ieVar, gh ghVar);

    /* synthetic */ void checkUserBannedSpeakInChannel(String str, String str2, String str3, se2.e eVar);

    /* synthetic */ void checkUserFreeGiftInfo(kn knVar, te2.r rVar);

    /* synthetic */ void clearGuildRelatedData(ArrayList arrayList);

    /* synthetic */ void clearGuildSearchSearchHistory(s sVar);

    /* synthetic */ void clearLiveBroadcastRecord(String str);

    /* synthetic */ void closeAVLobbyApp(long j15, long j16, long j17, ah ahVar);

    /* synthetic */ void closeChannelActivitySwitch(long j15, hp hpVar);

    /* synthetic */ void closeChannelTopMsg(String str, String str2, @NonNull bz bzVar);

    /* synthetic */ GProNotice convert2GProNotice(@NonNull ci ciVar);

    /* synthetic */ hq convert2GWGProNotice(@NonNull ci ciVar);

    /* synthetic */ GuildScheduleInfo convert2GuildScheduleInfo(GProScheduleInfo gProScheduleInfo);

    /* synthetic */ void convertGuildAndThirdIds(bv bvVar, x xVar);

    /* synthetic */ IGProGuildInfo convertToGuildInfo(IGProCmd0xf5aGuildInfo iGProCmd0xf5aGuildInfo, int i15, IGProCmd0xf5aUserInfo iGProCmd0xf5aUserInfo);

    /* synthetic */ el convertToGuildInviteInfo(IGProCmd0xf5aGuildInfo iGProCmd0xf5aGuildInfo);

    @Nullable
    /* synthetic */ IGProInviteInfo convertToGuildInviteInfo(bj bjVar);

    /* synthetic */ void copyToNewAIApp(com.tencent.mobileqq.qqguildsdk.data.genc.c cVar, y yVar);

    /* synthetic */ void createChannel(String str, com.tencent.mobileqq.qqguildsdk.data.d dVar, long j15, int i15, com.tencent.mobileqq.qqguildsdk.data.b bVar, se2.f fVar);

    /* synthetic */ void createChannelCategory(String str, String str2, be beVar);

    /* synthetic */ void createDirectMsgSession(@NonNull com.tencent.mobileqq.qqguildsdk.data.i iVar, @NonNull se2.g gVar);

    /* synthetic */ void createGuild(bx bxVar, z zVar);

    /* synthetic */ void createLinkChannel(String str, com.tencent.mobileqq.qqguildsdk.data.d dVar, long j15, bb bbVar);

    @Override // re2.f
    /* synthetic */ void createRole(String str, @NonNull fa faVar, @NonNull List list, @NonNull se2.h hVar);

    /* synthetic */ void createSchedule(String str, String str2, GuildScheduleInfo guildScheduleInfo, cb cbVar);

    /* synthetic */ void createScheduleLimitQuery(String str, cc ccVar);

    /* synthetic */ void createScheduleV2(com.tencent.mobileqq.qqguildsdk.data.genc.ca caVar, ab abVar);

    @Override // re2.b
    /* synthetic */ void dealHandUpRequestForAdmin(aj ajVar, boolean z15, te2.e eVar);

    /* synthetic */ void dealNotice(String str, String str2, se2.i iVar);

    @Nullable
    /* synthetic */ IGProAIAppAppInfoExt decodeAIAppInfoExt(byte[] bArr);

    @Nullable
    /* synthetic */ ILiveRoomInfo decodeGProLiveRoomInfo(byte[] bArr);

    @Nullable
    /* synthetic */ IGProMVPGuildGroupCard decodeGuildGroupInfo(byte[] bArr);

    @Nullable
    /* synthetic */ GuildScheduleInfo decodeGuildScheduleInfo(byte[] bArr);

    /* synthetic */ void decodeInviteJumpInfo(String str, ac acVar);

    @Nullable
    /* synthetic */ IGProGetContentRecommendRsp decodeMVPFeedsRspPb(byte[] bArr);

    @Nullable
    /* synthetic */ IGProScheduleInfo decodeScheduleInfo(byte[] bArr);

    /* synthetic */ void deleteAIAppDraft(com.tencent.mobileqq.qqguildsdk.data.genc.e eVar, ae aeVar);

    /* synthetic */ void deleteAISearchSession(com.tencent.mobileqq.qqguildsdk.data.genc.s sVar, af afVar);

    /* synthetic */ void deleteFeed(com.tencent.mobileqq.qqguildsdk.data.genc.cb cbVar, ag agVar);

    /* synthetic */ void deleteGlobalAIApps(com.tencent.mobileqq.qqguildsdk.data.genc.d dVar, ad adVar);

    /* synthetic */ void deleteGuildEssence(ff ffVar, fi fiVar);

    /* synthetic */ void deleteGuildGlobalBanner(com.tencent.mobileqq.qqguildsdk.data.genc.ey eyVar, fe feVar);

    /* synthetic */ void deleteObserver(GPServiceObserver gPServiceObserver);

    @Override // re2.f
    /* synthetic */ void deleteRole(String str, String str2, @NonNull ca caVar);

    /* synthetic */ void deleteSystemNoticeMessage(jy jyVar, ai aiVar);

    /* synthetic */ void destroyGuild(String str, gr grVar);

    /* synthetic */ void doGuildCheckin(com.tencent.mobileqq.qqguildsdk.data.genc.fe feVar, fh fhVar);

    /* synthetic */ void doRealNameAuth(int i15, es esVar);

    /* synthetic */ void domainResolveByLocalDns(String str);

    /* synthetic */ void editSchedule(String str, String str2, GuildScheduleInfo guildScheduleInfo, int i15, cd cdVar);

    /* synthetic */ void editScheduleV2(com.tencent.mobileqq.qqguildsdk.data.genc.cd cdVar, te2.aj ajVar);

    /* synthetic */ void enterAudioLiveChannel(cf cfVar, ak akVar);

    /* synthetic */ void enterAudioLiveChannelAndCallbackDirect(cf cfVar, ak akVar);

    /* synthetic */ void enterGuildGroupAIO(long j15, long j16);

    /* synthetic */ void enterLiveChannel(cf cfVar, ak akVar);

    /* synthetic */ void exitAudioLiveChannel(long j15, long j16, int i15, ArrayList arrayList, te2.al alVar);

    /* synthetic */ void exitLiveChannel(cg cgVar, te2.al alVar);

    /* synthetic */ void exposeGuildFloatingLayer(cj cjVar, hp hpVar);

    /* synthetic */ void exposeGuildTaskCard(ck ckVar, hp hpVar);

    /* synthetic */ void exposeRecommends(com.tencent.mobileqq.qqguildsdk.data.genc.ci ciVar, hp hpVar);

    /* synthetic */ void fetchActiveChannelByChannelType(hd hdVar, te2.a aVar);

    /* synthetic */ void fetchActiveChannels(String str, am amVar);

    /* synthetic */ void fetchAddGuildInfo(int i15, long j15, te2.c cVar);

    /* synthetic */ void fetchAndRefreshGuildUserProfileInfo(long j15, long j16, long j17, int i15, te2.bq bqVar);

    /* synthetic */ void fetchAppInfos(String str, te2.an anVar);

    /* synthetic */ void fetchAudioLiveChannelBlockUserState(long j15, long j16, te2.ar arVar);

    /* synthetic */ void fetchAudioLiveChannelGroupList(com.tencent.mobileqq.qqguildsdk.data.genc.ah ahVar, ap apVar);

    /* synthetic */ void fetchAudioLiveChannelGroupListAndCallbackDirect(com.tencent.mobileqq.qqguildsdk.data.genc.ah ahVar, ap apVar);

    @Override // re2.b
    /* synthetic */ void fetchAudioLiveChannelUserList(com.tencent.mobileqq.qqguildsdk.data.genc.am amVar, aq aqVar);

    /* synthetic */ void fetchCategoryAdminInfoList(cs csVar, as asVar);

    /* synthetic */ void fetchCategoryAdminMemberList(String str, String str2, String str3, int i15, boolean z15, @NonNull se2.ap apVar);

    /* synthetic */ void fetchChannelAdminInfoList(String str, String str2, @NonNull se2.d dVar);

    /* synthetic */ void fetchChannelAdminInfoList(String str, @NonNull se2.d dVar);

    /* synthetic */ void fetchChannelAdminMemberList(String str, String str2, String str3, int i15, boolean z15, @NonNull se2.ap apVar);

    /* synthetic */ void fetchChannelAuthControlMemberList(long j15, long j16, int i15, byte[] bArr, int i16, te2.at atVar);

    @Override // re2.b
    /* synthetic */ void fetchChannelAuthControlRoleList(long j15, long j16, int i15, au auVar);

    /* synthetic */ void fetchChannelAuthControlUnableMemberList(long j15, long j16, int i15, byte[] bArr, int i16, te2.aw awVar);

    /* synthetic */ void fetchChannelAuthInfo(com.tencent.mobileqq.qqguildsdk.data.genc.bb bbVar, av avVar);

    /* synthetic */ void fetchChannelInfoWithCategory(String str, String str2, int i15, se2.l lVar);

    /* synthetic */ void fetchChannelInfoWithUnreadStatus(String str, boolean z15);

    /* synthetic */ void fetchChannelList(String str, m mVar);

    /* synthetic */ void fetchChannelLiveableMemberList(String str, String str2, int i15, @NonNull byte[] bArr, @NonNull ue2.e eVar);

    @Override // re2.d
    /* synthetic */ void fetchChannelLiveableRoleList(String str, String str2, @NonNull ue2.f fVar);

    /* synthetic */ void fetchChannelUnlivableMemberList(String str, String str2, int i15, @NonNull byte[] bArr, @NonNull ue2.i iVar);

    /* synthetic */ void fetchChannelUnlivableRoleList(String str, String str2, @NonNull ue2.j jVar);

    /* synthetic */ void fetchChannelUserPermission(String str, String str2, int i15, @NonNull ue2.m mVar);

    /* synthetic */ void fetchChannelVisibleRoleListWithoutLevelRole(long j15, long j16, ax axVar);

    /* synthetic */ void fetchComments(String str, String str2, long j15, long j16, long j17, long j18, se2.n nVar);

    /* synthetic */ void fetchDirectMsgBlack(String str, @NonNull se2.o oVar);

    /* synthetic */ void fetchDirectMsgStatus(@NonNull se2.p pVar);

    /* synthetic */ void fetchFeedsNotifySwitch(se2.j jVar);

    /* synthetic */ void fetchGlobalPrivacySwitch(te2.ff ffVar);

    /* synthetic */ void fetchGuestGuild(String str, boolean z15, se2.q qVar);

    /* synthetic */ void fetchGuestGuild(String str, boolean z15, boolean z16, se2.q qVar);

    /* synthetic */ void fetchGuestGuildInfoWithChannelList(int i15, dx dxVar, te2.ay ayVar);

    /* synthetic */ void fetchGuestGuildWithChannelListAndJoinSig(int i15, GProGetGuildAndChannelReq gProGetGuildAndChannelReq, se2.r rVar);

    /* synthetic */ void fetchGuildActiveLiveChannelNum(ct ctVar, az azVar);

    /* synthetic */ void fetchGuildActiveValue(long j15, fg fgVar);

    /* synthetic */ void fetchGuildBlackList(String str, String str2, byte[] bArr, ba baVar);

    /* synthetic */ void fetchGuildBottomTabExp(com.tencent.mobileqq.qqguildsdk.data.genc.ap apVar, te2.bb bbVar);

    /* synthetic */ void fetchGuildBoundGroupsWithGroupCodes(String str, ArrayList arrayList, bc bcVar);

    /* synthetic */ void fetchGuildInfoAndMemberList(fl flVar, bd bdVar);

    void fetchGuildInfoByAppIdentity(com.tencent.mobileqq.qqguildsdk.data.cd cdVar, String str, String str2, se2.s sVar);

    void fetchGuildInfoByInviteCode(String str, se2.y yVar);

    void fetchGuildInfoByInviteCodeAndContentID(String str, String str2, String str3, int i15, se2.x xVar);

    /* synthetic */ void fetchGuildInfoOnly(String str, int i15, se2.t tVar);

    /* synthetic */ void fetchGuildInfoWithSource(String str, int i15, se2.t tVar);

    /* synthetic */ void fetchGuildSearchSwitch(String str, @NonNull u uVar);

    /* synthetic */ void fetchGuildSpeakableMemberList(String str, boolean z15, byte[] bArr, @NonNull ue2.o oVar);

    @Override // re2.f
    /* synthetic */ void fetchGuildSpeakableRoleList(String str, boolean z15, @NonNull ue2.p pVar);

    /* synthetic */ void fetchGuildSpeakableRule(String str, se2.as asVar);

    /* synthetic */ void fetchGuildSpeakableThreshold(String str, v vVar);

    /* synthetic */ void fetchGuildTaskCards(com.tencent.mobileqq.qqguildsdk.data.genc.ak akVar, te2.be beVar);

    /* synthetic */ void fetchGuildWithChannels(int i15, dx dxVar, ea eaVar, se2.au auVar);

    /* synthetic */ void fetchIsLiveChannelOpen(String str, se2.z zVar);

    /* synthetic */ void fetchIsVoiceChannelOpen(String str, aa aaVar);

    /* synthetic */ void fetchJoinGuildOption(cu cuVar, bf bfVar);

    /* synthetic */ void fetchLatestComments(String str, String str2, long j15, long j16, se2.n nVar);

    /* synthetic */ void fetchLevelRolePermissions(long j15, int i15, bg bgVar);

    /* synthetic */ void fetchLiveBroadcastStatistics(ii iiVar, bh bhVar);

    /* synthetic */ void fetchLiveChannelAnchorList(String str, String str2, se2.ab abVar);

    /* synthetic */ void fetchLiveChannelBannedUserList(String str, String str2, String str3, se2.ac acVar);

    /* synthetic */ void fetchLivePushRtmpURL(hf hfVar, bi biVar);

    /* synthetic */ void fetchLiveRoomInfo(com.tencent.mobileqq.qqguildsdk.data.ag agVar, se2.ad adVar);

    /* synthetic */ void fetchLiveSecurityTips(se2.ae aeVar);

    /* synthetic */ void fetchMVPFeedsFromServer(dq dqVar, te2.bj bjVar);

    /* synthetic */ void fetchMVPFeedsFromServerWithNonUiThreadCallback(dq dqVar, te2.bj bjVar);

    /* synthetic */ void fetchMainFrameLiveCard(de deVar, bk bkVar);

    /* synthetic */ void fetchMemberListForFeed(co coVar, bl blVar);

    @Override // re2.b
    /* synthetic */ void fetchMemberListForGuildGroup(hk hkVar, bm bmVar);

    @Override // re2.f
    /* synthetic */ void fetchMemberListWithRole(String str, String str2, int i15, boolean z15, @NonNull se2.ag agVar);

    @Override // re2.f
    /* synthetic */ void fetchMemberRoleList(String str, String str2, String str3, int i15, @NonNull se2.ai aiVar);

    /* synthetic */ void fetchMyCreateGuilds(bn bnVar);

    /* synthetic */ void fetchMyFansList(hm hmVar, fz fzVar);

    /* synthetic */ void fetchMyFollowAuthorList(hn hnVar, ga gaVar);

    /* synthetic */ void fetchNewestNotice(int i15, String str, @NonNull se2.aj ajVar);

    /* synthetic */ void fetchNoGuildShareInfo(com.tencent.mobileqq.qqguildsdk.data.genc.bh bhVar, te2.v vVar);

    /* synthetic */ void fetchNoGuildShareURL(com.tencent.mobileqq.qqguildsdk.data.genc.bg bgVar, te2.u uVar);

    /* synthetic */ void fetchNonRoleMemberList(String str, String str2, String str3, String str4, int i15, boolean z15, @NonNull se2.ak akVar);

    /* synthetic */ void fetchOnlineUsers(String str, int i15, int i16, ArrayList arrayList, bo boVar);

    /* synthetic */ void fetchOtherChannelUserPermission(long j15, long j16, long j17, int i15, int i16, te2.m mVar);

    /* synthetic */ void fetchProfileAddFriendField(String str, String str2, te2.bp bpVar);

    /* synthetic */ void fetchRecommendChannel(int i15, int i16, se2.am amVar);

    /* synthetic */ void fetchRecommendEssenceInfo(long j15, int i15, br brVar);

    /* synthetic */ void fetchRemainAtSetInfo(long j15, te2.ao aoVar);

    void fetchRemainAtSetInfo(String str, se2.k kVar);

    /* synthetic */ void fetchRetentionGuildList(int i15, int i16, byte[] bArr, long j15, te2.bs bsVar);

    /* synthetic */ void fetchRole(String str, String str2, boolean z15, int i15, @NonNull se2.an anVar);

    /* synthetic */ void fetchRoleList(String str, int i15, int i16, @NonNull se2.ao aoVar);

    /* synthetic */ void fetchRoleList(String str, int i15, @NonNull se2.ao aoVar);

    @Override // re2.d
    /* synthetic */ void fetchRoleListWithPermission(String str, int i15, @NonNull ue2.a aVar);

    /* synthetic */ void fetchRoleListWithoutLevelRoleWithFilter(long j15, int i15, int i16, bt btVar);

    /* synthetic */ void fetchRoleMemberList(String str, String str2, int i15, boolean z15, @NonNull se2.ap apVar);

    @Override // re2.d
    /* synthetic */ void fetchRoleWithPermission(String str, String str2, int i15, @NonNull ue2.b bVar);

    /* synthetic */ void fetchShareButtonReq(ArrayList arrayList, boolean z15, w wVar);

    void fetchShareInfo(String str, String str2, int i15, byte[] bArr, boolean z15, ArrayList<kd> arrayList, com.tencent.mobileqq.qqguildsdk.data.genc.bc bcVar, se2.ar arVar);

    /* synthetic */ void fetchShareInfo0x10c3(long j15, long j16, cv cvVar, bu buVar);

    /* synthetic */ void fetchShareUrlReq(long j15, long j16, long j17, String str, cw cwVar, ArrayList arrayList, te2.bv bvVar);

    /* synthetic */ void fetchShareUrlReqV2(cx cxVar, te2.bv bvVar);

    /* synthetic */ void fetchSpecifiedTypeChannelsOpenState(long j15, ArrayList arrayList, int i15, bw bwVar);

    /* synthetic */ void fetchSubscribeUserGuide(te2.bx bxVar);

    /* synthetic */ void fetchUserInfo(String str, String str2, List list, boolean z15, se2.bn bnVar);

    /* synthetic */ void fetchUserInfo(String str, List list, boolean z15, se2.bn bnVar);

    /* synthetic */ void fetchUserInfo(String str, List list, boolean z15, boolean z16, se2.bn bnVar);

    /* synthetic */ void fetchUserJoinedGuildListV2(cy cyVar, te2.bz bzVar);

    /* synthetic */ void fetchUserProfileInfo(com.tencent.mobileqq.qqguildsdk.data.genc.bs bsVar, te2.ca caVar);

    /* synthetic */ void fetchVersionNews(te2.cb cbVar);

    /* synthetic */ void fetchVisibleMemberListByTinyId(String str, String str2, @NonNull List list, @NonNull se2.at atVar);

    /* synthetic */ void fetchXWordInfo(long j15, im imVar);

    /* synthetic */ void followGuildAuthor(dc dcVar, te2.cc ccVar);

    @Override // re2.b
    /* synthetic */ void getAIAppConversations(com.tencent.mobileqq.qqguildsdk.data.genc.i iVar, boolean z15, te2.cd cdVar);

    /* synthetic */ void getAIAppDraftDetail(com.tencent.mobileqq.qqguildsdk.data.genc.f fVar, te2.ce ceVar);

    /* synthetic */ void getAIAppDraftList(com.tencent.mobileqq.qqguildsdk.data.genc.g gVar, te2.cf cfVar);

    /* synthetic */ void getAIAppDraftState(com.tencent.mobileqq.qqguildsdk.data.genc.h hVar, te2.cg cgVar);

    /* synthetic */ void getAIAppPromptTemplates(em emVar, ch chVar);

    /* synthetic */ void getAIIdeasAppList(com.tencent.mobileqq.qqguildsdk.data.genc.k kVar, il ilVar);

    /* synthetic */ void getAISearchRecommendWords(com.tencent.mobileqq.qqguildsdk.data.genc.t tVar, te2.cj cjVar);

    /* synthetic */ void getAISearchSessionList(com.tencent.mobileqq.qqguildsdk.data.genc.u uVar, boolean z15, te2.ck ckVar);

    /* synthetic */ void getAISearchSessionMsgList(com.tencent.mobileqq.qqguildsdk.data.genc.v vVar, boolean z15, te2.cl clVar);

    /* synthetic */ void getAISearchShareURL(dd ddVar, cm cmVar);

    /* synthetic */ void getActiveLiveAudioChannelNum(long j15, cn cnVar);

    /* synthetic */ void getActiveLiveAudioChannelNumV2(com.tencent.mobileqq.qqguildsdk.data.aj ajVar, se2.ay ayVar);

    /* synthetic */ void getAgentGuildList(df dfVar, te2.co coVar);

    /* synthetic */ void getAllGuildSearchAIInfo(fv fvVar, cp cpVar);

    @Nullable
    /* synthetic */ IGProGuildUserState getAlreadyEnteredChannel();

    /* synthetic */ void getAppAuthorizationInfo(String str, int i15, se2.bc bcVar);

    @Nullable
    /* synthetic */ IGProAppChnnPreInfo getAppChannelPreInfo(String str, String str2);

    @Nullable
    /* synthetic */ ArrayList getAppChannelPreInfos(String str);

    @Nullable
    /* synthetic */ IGProAppChnnPreInfo getAppChnnPreInfo(String str, String str2);

    @Nullable
    /* synthetic */ ArrayList getAppInfos();

    /* synthetic */ void getAppRoleList(String str, String str2, byte[] bArr, se2.bd bdVar);

    /* synthetic */ void getAssociativeWords(String str, se2.b bVar);

    /* synthetic */ String getAvatarUrl(String str, long j15, int i15);

    /* synthetic */ void getBanners(dg dgVar, cq cqVar);

    /* synthetic */ void getBindClientService(int i15, String str, byte[] bArr, se2.bg bgVar);

    /* synthetic */ void getBindingGroupInfos(ArrayList arrayList, cr crVar);

    /* synthetic */ void getBlockRecGuilds(dh dhVar, te2.cs csVar);

    /* synthetic */ HashMap getBoardPermission(String str);

    /* synthetic */ void getCacheUserBanInfo(ew ewVar, fd fdVar);

    @Nullable
    /* synthetic */ ArrayList getCategoryChannelIdList(long j15);

    @Nullable
    /* synthetic */ ArrayList getCategoryChannelIdListByCategoryType(long j15, ArrayList arrayList);

    @Nullable
    /* synthetic */ ArrayList getCategoryChannelIdListExcludeCategoryType(long j15, ArrayList arrayList);

    @Nullable
    /* synthetic */ ArrayList getCategoryChannelInfoListByCategoryType(long j15, ArrayList arrayList);

    /* synthetic */ void getCategoryChannelInfoListByFilter(di diVar, te2.ct ctVar);

    /* synthetic */ void getCategoryChannelInfoListV2(dj djVar, te2.ct ctVar);

    /* synthetic */ void getCategoryGuilds(dk dkVar, te2.cu cuVar);

    /* synthetic */ void getCategoryPageGuilds(dl dlVar, te2.cv cvVar);

    @Override // re2.d
    @NonNull
    /* synthetic */ dv getCategoryPermission(String str, String str2);

    /* synthetic */ void getCategoryTabContent(dm dmVar, te2.cw cwVar);

    /* synthetic */ void getChannelActivity(dn dnVar, te2.cx cxVar);

    /* synthetic */ List getChannelCategoryList(String str);

    /* synthetic */ void getChannelHeartbeat(String str, String str2, long j15, se2.av avVar);

    /* synthetic */ IGProChannelInfo getChannelInfo(String str);

    /* synthetic */ void getChannelInvisibleMemberList(String str, String str2, int i15, @NonNull byte[] bArr, @NonNull ue2.c cVar);

    /* synthetic */ void getChannelInvisibleRoleList(String str, String str2, @NonNull ue2.d dVar);

    @NonNull
    /* synthetic */ List getChannelList(String str);

    @Nullable
    /* synthetic */ List getChannelListForSelector(String str, boolean z15);

    @Override // re2.d
    @NonNull
    /* synthetic */ dv getChannelPermission(String str, String str2);

    /* synthetic */ HashMap getChannelPermission(String str, int i15);

    /* synthetic */ void getChannelSpeakableMemberList(String str, String str2, int i15, @NonNull byte[] bArr, @NonNull ue2.g gVar);

    @Override // re2.d
    /* synthetic */ void getChannelSpeakableRoleList(String str, String str2, @NonNull ue2.h hVar);

    /* synthetic */ void getChannelTopMsgState(String str, String str2, @NonNull se2.bf bfVar);

    /* synthetic */ void getChannelUnspeakableMemberList(String str, String str2, int i15, @NonNull byte[] bArr, @NonNull ue2.k kVar);

    /* synthetic */ void getChannelUnspeakableRoleList(String str, String str2, @NonNull ue2.l lVar);

    /* synthetic */ int getChannelUserPermission(@NonNull String str, @NonNull String str2);

    /* synthetic */ void getChannelVisibleAllMemberList(String str, String str2, int i15, @NonNull byte[] bArr, @NonNull ue2.n nVar);

    /* synthetic */ void getChannelVisibleMemberList(String str, String str2, int i15, @NonNull byte[] bArr, @NonNull ue2.n nVar);

    @Override // re2.d
    /* synthetic */ void getChannelVisibleRoleList(String str, String str2, @NonNull ax axVar);

    /* synthetic */ void getCommonGuilds(Cdo cdo, te2.cy cyVar);

    /* synthetic */ void getConfigTabContent(dp dpVar, cz czVar);

    /* synthetic */ void getContributionList(dr drVar, da daVar);

    /* synthetic */ void getCurrPlaySong(long j15, long j16, db dbVar);

    /* synthetic */ void getDynamicGuildInfo(ds dsVar, te2.dc dcVar);

    /* synthetic */ void getDynamicUserInfo(dt dtVar, te2.dd ddVar);

    /* synthetic */ com.tencent.mobileqq.qqguildsdk.data.Cdo getFaceAuthInfo();

    /* synthetic */ void getFbOpInfoByGuildID(du duVar, boolean z15, te2.de deVar);

    /* synthetic */ void getFootprintList(com.tencent.mobileqq.qqguildsdk.data.genc.dv dvVar, te2.df dfVar);

    /* synthetic */ void getFriendsInGuild(dz dzVar, te2.dg dgVar);

    /* synthetic */ void getFriendsRecommend(dw dwVar, te2.dh dhVar);

    /* synthetic */ String getFullAvatarUrl(com.tencent.mobileqq.qqguildsdk.data.cz czVar, int i15);

    @Nullable
    /* synthetic */ String getFullGuildUserUserAvatarUrl(String str, String str2, int i15);

    @Override // re2.g
    @Nullable
    /* synthetic */ String getFullUserAvatarPendantUrl(String str);

    /* synthetic */ void getGlobalAIAppList(com.tencent.mobileqq.qqguildsdk.data.genc.j jVar, te2.di diVar);

    /* synthetic */ List getGroupGuildList();

    /* synthetic */ void getGuidePageInfo(long j15, String str, te2.dj djVar);

    /* synthetic */ void getGuildAIAppThirdPartySigs(com.tencent.mobileqq.qqguildsdk.data.genc.m mVar, te2.ci ciVar);

    /* synthetic */ String getGuildAvatarUrl(String str, int i15);

    /* synthetic */ void getGuildBindableGroups(te2.dk dkVar);

    /* synthetic */ void getGuildBindingInfo(long j15, te2.dl dlVar);

    /* synthetic */ void getGuildBoundGroupsFirstPage(String str, int i15, te2.dm dmVar);

    @Nullable
    /* synthetic */ ArrayList getGuildChannelInfos(long j15);

    /* synthetic */ void getGuildCheckinInfo(com.tencent.mobileqq.qqguildsdk.data.genc.fe feVar, fh fhVar);

    /* synthetic */ void getGuildCreatorTasks(String str, boolean z15, te2.dn dnVar);

    /* synthetic */ void getGuildEssenceTabStatus(long j15, te2.Cdo cdo);

    /* synthetic */ void getGuildFeedExperiment(com.tencent.mobileqq.qqguildsdk.data.genc.fg fgVar, te2.dp dpVar);

    /* synthetic */ void getGuildFeedsValidCalendar(fw fwVar, fj fjVar);

    /* synthetic */ void getGuildFloatingLayer(com.tencent.mobileqq.qqguildsdk.data.genc.db dbVar, te2.dq dqVar);

    @Nullable
    /* synthetic */ IGProGlobalBanner getGuildGlobalBanner(String str);

    /* synthetic */ void getGuildGroupIds(long j15, int i15, te2.dr drVar);

    /* synthetic */ void getGuildGroups(long j15, int i15, te2.ds dsVar);

    /* synthetic */ void getGuildHomeCategories(ea eaVar, te2.dt dtVar);

    /* synthetic */ void getGuildHomeSections(long j15, te2.du duVar);

    /* synthetic */ String getGuildIdOf(String str);

    @Nullable
    /* synthetic */ IGProGuildInfo getGuildInfo(String str);

    @Nullable
    /* synthetic */ GProGuild getGuildInfoFromCache(String str);

    /* synthetic */ void getGuildLableInfo(eb ebVar, te2.dv dvVar);

    /* synthetic */ void getGuildLastRank(com.tencent.mobileqq.qqguildsdk.data.genc.eg egVar, ei eiVar);

    /* synthetic */ List getGuildList();

    /* synthetic */ void getGuildMainFrameHeaderInfo(long j15, se2.aw awVar);

    @Nullable
    /* synthetic */ IGProClientIdentityInfo getGuildMemberClientIdentity(String str, String str2, String str3);

    @NonNull
    /* synthetic */ String getGuildMemberName(String str, String str2);

    @NonNull
    /* synthetic */ Map getGuildMemberNames(String str, List list);

    /* synthetic */ void getGuildModeSwitchLimit(long j15, te2.dw dwVar);

    /* synthetic */ String getGuildName(String str);

    /* synthetic */ void getGuildNum(long j15, te2.dx dxVar);

    @Override // re2.d
    @NonNull
    /* synthetic */ dv getGuildPermission(String str);

    /* synthetic */ void getGuildRecommend(byte[] bArr, se2.bm bmVar);

    @Nullable
    /* synthetic */ long getGuildRoleLimit(long j15);

    /* synthetic */ long getGuildRoleLimit(String str);

    /* synthetic */ void getGuildSearch(gj gjVar, fp fpVar);

    /* synthetic */ void getGuildSearchActivePage(fu fuVar, dy dyVar);

    /* synthetic */ void getGuildSearchNetSugWords(fx fxVar, te2.fl flVar);

    /* synthetic */ void getGuildSearchRankingList(fy fyVar, te2.dz dzVar);

    /* synthetic */ void getGuildSearchTabs(com.tencent.mobileqq.qqguildsdk.data.genc.fz fzVar, fm fmVar);

    /* synthetic */ ed getGuildSpeakPermission(String str, int i15);

    /* synthetic */ IGuildSpeakableThresholdPermission getGuildSpeakableThresholdPermission(String str);

    @Nullable
    /* synthetic */ com.tencent.mobileqq.qqguildsdk.data.ds getGuildState(String str);

    @Nullable
    /* synthetic */ String getGuildUserAvatarPendantUrl(String str);

    @Nullable
    /* synthetic */ String getGuildUserAvatarUrl(String str, String str2, int i15);

    @Nullable
    /* synthetic */ Map getGuildUserAvatarUrls(String str, List list, int i15);

    /* synthetic */ void getGuildUserBannedStatus(ko koVar, te2.ea eaVar);

    @Nullable
    /* synthetic */ ArrayList getGuildUserChannelCategoryAdminList(String str, String str2);

    @Nullable
    /* synthetic */ IGProClientIdentity getGuildUserClientIdentity(String str, String str2);

    @NonNull
    /* synthetic */ String getGuildUserDisplayName(String str, String str2);

    @NonNull
    /* synthetic */ String getGuildUserDisplayName(String str, String str2, String str3);

    @NonNull
    /* synthetic */ String getGuildUserDisplayNameInternal(String str, String str2);

    @Nullable
    /* synthetic */ String getGuildUserGuildAvatarUrl(long j15, long j16, int i15);

    @Nullable
    /* synthetic */ int getGuildUserLevelRoleId(String str, String str2);

    @Nullable
    /* synthetic */ IGProMedal getGuildUserMedal(String str);

    @Nullable
    /* synthetic */ String getGuildUserMemberName(String str, String str2);

    @NonNull
    /* synthetic */ String getGuildUserNick(String str, String str2);

    @Nullable
    /* synthetic */ String getGuildUserNickname(String str);

    /* synthetic */ void getGuildUserRank(en enVar, er erVar);

    @Nullable
    /* synthetic */ long getGuildUserTopRoleId(String str, String str2);

    @NonNull
    /* synthetic */ Map getGuildUsersNicks(String str, List list);

    /* synthetic */ void getGuildVerifyShareInfo(kq kqVar, in inVar);

    @Nullable
    /* synthetic */ int getGuildVisitorVisiblePermission(int i15);

    /* synthetic */ void getGuildsInContact(te2.eb ebVar);

    /* synthetic */ void getHotEventContent(ec ecVar, boolean z15, te2.ec ecVar2);

    /* synthetic */ void getHotEventListContent(com.tencent.mobileqq.qqguildsdk.data.genc.ed edVar, boolean z15, String str, te2.ed edVar2);

    /* synthetic */ void getHotLive(ee eeVar, te2.ee eeVar2);

    /* synthetic */ void getHotSearchWords(ArrayList arrayList, int i15, te2.ef efVar);

    /* synthetic */ boolean getIsChannelListFirstLoadingCallbacked();

    /* synthetic */ boolean getIsGuildListFirstLoadingCallbacked();

    /* synthetic */ List getJumpChannelList(String str, String str2);

    @Nullable
    /* synthetic */ IGProJumpToCategoryInfo getJumpToCategory(String str);

    /* synthetic */ void getLabelGuilds(ky kyVar, eh ehVar);

    @Override // re2.b
    /* synthetic */ void getLatestConversationResult(com.tencent.mobileqq.qqguildsdk.data.genc.l lVar, ej ejVar);

    @Deprecated
    /* synthetic */ void getLiveAudioPollingCtx(String str, String str2, se2.az azVar);

    /* synthetic */ void getLiveAudioPollingCtxV2(com.tencent.mobileqq.qqguildsdk.data.aj ajVar, se2.ba baVar);

    /* synthetic */ void getManageAgentInfo(com.tencent.mobileqq.qqguildsdk.data.genc.eh ehVar, ik ikVar);

    /* synthetic */ void getMemberInfoByOpenId(String str, long j15, boolean z15, boolean z16, se2.bj bjVar);

    /* synthetic */ IGProGuildRoleInfo getMemberLevelRole(String str, String str2, String str3);

    @Nullable
    /* synthetic */ IGProGuildRoleInfo getMemberLevelRoleInfo(long j15, long j16, long j17);

    /* synthetic */ IGProGuildRoleInfo getMemberTopRole(String str, String str2, String str3, String str4, int i15);

    @Nullable
    /* synthetic */ IGProGuildRoleInfo getMemberTopRoleInfo(long j15, long j16, long j17, long j18, int i15);

    /* synthetic */ void getMemoryList(com.tencent.mobileqq.qqguildsdk.data.genc.ei eiVar, ek ekVar);

    /* synthetic */ void getMoreNoticeList(int i15, String str, int i16, se2.bk bkVar);

    /* synthetic */ void getMyTabContent(com.tencent.mobileqq.qqguildsdk.data.genc.ej ejVar, te2.el elVar);

    /* synthetic */ void getNavigationStatus(com.tencent.mobileqq.qqguildsdk.data.genc.ek ekVar, te2.em emVar);

    @Nullable
    /* synthetic */ IGProOnlineMember getOnlineMemberInfo(String str);

    /* synthetic */ void getOpenShareInfo(kz kzVar, te2.en enVar);

    /* synthetic */ int getPermissionChangeFilter(@NonNull String str, @NonNull String str2);

    @Nullable
    /* synthetic */ ArrayList getPersonalSignatureTemplate();

    @Nullable
    /* synthetic */ IGProAppChnnPreInfo getPollingChannelOfApp(String str, String str2);

    @Nullable
    /* synthetic */ ILiveRoomInfo getPollingChannelOfLive(String str, String str2);

    @Nullable
    /* synthetic */ IAudioChannelMemberInfos getPollingChannelOfVoice(String str, String str2);

    /* synthetic */ com.tencent.mobileqq.qqguildsdk.data.dw getPollingCtxCache(long j15, long j16, int i15);

    /* synthetic */ void getPollingCtxs(String str, ArrayList arrayList, int i15, se2.bl blVar);

    @Nullable
    /* synthetic */ com.tencent.mobileqq.qqguildsdk.data.dp getPollingGroupInteractiveLikeInfo(long j15, long j16);

    /* synthetic */ void getPopupInfo(long j15, int i15, eo eoVar);

    /* synthetic */ void getPostPublishPermission(ic icVar, gf gfVar);

    /* synthetic */ void getPreviewNavigation(com.tencent.mobileqq.qqguildsdk.data.genc.el elVar, ep epVar);

    /* synthetic */ void getProfileSwitch(eq eqVar);

    @NotNull
    /* synthetic */ com.tencent.mobileqq.qqguildsdk.data.ew getQQMsgListChannel(@NotNull String str, @NotNull String str2);

    @Override // re2.e
    @NotNull
    /* synthetic */ List getQQMsgListChannels();

    @Nullable
    /* synthetic */ ArrayList getQQMsgListGuilds();

    /* synthetic */ void getRealTimeUserBanInfo(ew ewVar, fd fdVar);

    @Nullable
    /* synthetic */ IGProGetRecentVisitGuildInfosFromCacheRsp getRecentVisitGuildInfosListSync(int i15, int i16);

    @Nullable
    /* synthetic */ IGProRecommendEssenceSvrRsp getRecommendEssenceSvrRsp(String str, String str2);

    @Nullable
    /* synthetic */ void getRecommendGuildInfoFromCache(se2.ax axVar);

    /* synthetic */ void getRecommendQuickJoin(com.tencent.mobileqq.qqguildsdk.data.genc.eo eoVar, et etVar);

    /* synthetic */ void getRecommendTabContent(com.tencent.mobileqq.qqguildsdk.data.genc.ep epVar, eu euVar);

    /* synthetic */ void getRecommendTabGuildMorePageItems(dl dlVar, boolean z15, ev evVar);

    /* synthetic */ void getRecommendTagList(com.tencent.mobileqq.qqguildsdk.data.genc.eq eqVar, te2.ew ewVar);

    /* synthetic */ void getRiskMemberRedPoint(com.tencent.mobileqq.qqguildsdk.data.genc.er erVar, ex exVar);

    @Nullable
    /* synthetic */ IGProGuildRoleInfo getRoleInfo(long j15, long j16);

    @Override // re2.f
    @Nullable
    /* synthetic */ IGProGuildRoleInfo getRoleInfo(String str, String str2);

    @Nullable
    /* synthetic */ ArrayList getRolePermission(long j15, long j16);

    /* synthetic */ void getScheduleDetail(String str, String str2, String str3, se2.ce ceVar);

    /* synthetic */ void getScheduleList(String str, String str2, long j15, se2.cf cfVar);

    /* synthetic */ void getScheduleListNew(String str, String str2, int i15, long j15, String str3, se2.cg cgVar);

    /* synthetic */ void getScheduleUsers(String str, String str2, String str3, int i15, byte[] bArr, se2.ci ciVar);

    /* synthetic */ void getSchedulesStatus(String str, String str2, long j15, long j16, se2.ch chVar);

    /* synthetic */ void getSelectChannelIDs(com.tencent.mobileqq.qqguildsdk.data.genc.es esVar, ez ezVar);

    @NonNull
    /* synthetic */ String getSelfGuildMemberName(String str);

    @Nullable
    /* synthetic */ int getSelfLiveChannelBanStatus(String str, String str2);

    String getSelfTinyId();

    @Nullable
    /* synthetic */ int getShareButtonFromCache(long j15, long j16, boolean z15);

    @Nullable
    /* synthetic */ IGProSimpleProfile getSimpleProfile(String str, String str2, int i15);

    /* synthetic */ void getSimpleProfileAsync(com.tencent.mobileqq.qqguildsdk.data.genc.et etVar, te2.fa faVar);

    @Nullable
    /* synthetic */ ArrayList getSortedGuildIdsFromCache();

    /* synthetic */ List getSortedGuildList();

    /* synthetic */ void getSortedGuilds(com.tencent.mobileqq.qqguildsdk.data.genc.eu euVar, fb fbVar);

    /* synthetic */ int getSpeakLimit(String str, String str2);

    /* synthetic */ List getSpecificTypeChannelList(String str, int i15);

    /* synthetic */ void getTopAgentRank(com.tencent.mobileqq.qqguildsdk.data.genc.ev evVar, fc fcVar);

    /* synthetic */ List getTopGuildList();

    /* synthetic */ void getUserList(String str, long j15, boolean z15, int i15, se2.bo boVar);

    /* synthetic */ void getUserList(String str, Object obj, boolean z15, int i15, @NonNull se2.bh bhVar);

    /* synthetic */ void getUserLiveInfo(int i15, se2.bp bpVar);

    /* synthetic */ void guildSearchReport(ge geVar, fo foVar);

    /* synthetic */ void guildSearchSetSwitch(gl glVar, fq fqVar);

    /* synthetic */ boolean hasSetSpeakableThreshold(String str);

    /* synthetic */ void initGetNoticeList(int i15, String str, boolean z15, se2.br brVar);

    /* synthetic */ void interruptAISearchSession(com.tencent.mobileqq.qqguildsdk.data.genc.w wVar, hp hpVar);

    /* synthetic */ void inviteJoinSingleGuild(bp bpVar, ft ftVar);

    @Override // re2.b
    /* synthetic */ void inviteUserHandUpForAdmin(aj ajVar, te2.e eVar);

    @Override // re2.b
    /* synthetic */ void inviteUserQueueForAdmin(aj ajVar, te2.e eVar);

    boolean isAllGuildAndChannelFetchCompleted();

    /* synthetic */ boolean isChannelAdmin(String str, String str2);

    /* synthetic */ boolean isGProSDKInitCompleted();

    /* synthetic */ boolean isGuest(IGProGuildInfo iGProGuildInfo);

    /* synthetic */ boolean isGuest(String str);

    @Nullable
    /* synthetic */ boolean isGuildMember(String str);

    /* synthetic */ boolean isGuildTroop(String str);

    @Override // re2.e
    /* synthetic */ boolean isQQMsgListChannel(@NotNull String str, @NotNull String str2);

    @Nullable
    /* synthetic */ boolean isQQMsgListGuild(long j15);

    @Nullable
    /* synthetic */ boolean isQQMsgListReady();

    /* synthetic */ void joinChannelByGuildTask(gn gnVar, fr frVar);

    /* synthetic */ void kickAudioChannelUsers(String str, String str2, ArrayList arrayList, se2.bt btVar);

    /* synthetic */ void kickGuildUsers(String str, List list, boolean z15, @Nullable ey eyVar, ca caVar);

    /* synthetic */ void loadGProUserProfileInfo(String str, String str2, se2.bv bvVar);

    /* synthetic */ void loadGuildSearchSearchHistory(te2.fv fvVar);

    @Override // re2.f
    /* synthetic */ void loadMemberRoleList(String str, String str2, @NonNull se2.bu buVar);

    /* synthetic */ void loadPartRoleMemberList(long j15, te2.ey eyVar);

    /* synthetic */ void loadUserInfo(String str, List list, se2.bn bnVar, List list2, boolean z15);

    /* synthetic */ void loadUserList(String str, long j15, se2.bo boVar);

    /* synthetic */ void loginLiveSvr(he heVar, te2.fw fwVar);

    byte[] lz4DecompressBuf(byte[] bArr);

    /* synthetic */ void makeAISearchShareImageAddHiddenLabel(com.tencent.mobileqq.qqguildsdk.data.genc.ga gaVar, fn fnVar);

    /* synthetic */ void modifyChannelInfo(com.tencent.mobileqq.qqguildsdk.data.genc.bq bqVar, te2.fx fxVar);

    /* synthetic */ void modifyGuildSpeakableMemberList(String str, List list, List list2, @NonNull gr grVar);

    @Override // re2.f
    /* synthetic */ void modifyGuildSpeakableRoleList(String str, List list, List list2, gr grVar);

    /* synthetic */ void modifyGuildSpeakableRule(String str, com.tencent.mobileqq.qqguildsdk.data.av avVar, gr grVar);

    /* synthetic */ void modifyScheduleInviteStatus(String str, String str2, String str3, int i15, se2.cj cjVar);

    /* synthetic */ void modifyScheduleInviteStatusV2(hl hlVar, te2.fy fyVar);

    void onSrvPushMsg(int i15, int i16, com.tencent.mobileqq.qqguildsdk.data.eu euVar, byte[] bArr);

    /* synthetic */ void openAVLobbyApp(com.tencent.mobileqq.qqguildsdk.data.genc.bz bzVar, te2.aa aaVar);

    /* synthetic */ void openCommunityIdentity(hx hxVar, gb gbVar);

    /* synthetic */ void optimizeGuildInfo(hy hyVar, gc gcVar);

    @Override // re2.b
    /* synthetic */ void performAIAppConversationButton(com.tencent.mobileqq.qqguildsdk.data.genc.b bVar, te2.ge geVar);

    /* synthetic */ void pinGuildGroupInRecentContact(long j15, long j16, int i15, int i16, hp hpVar);

    /* synthetic */ void prepareLiveBroadcast(com.tencent.mobileqq.qqguildsdk.data.genc.id idVar, gg ggVar);

    @Override // re2.b
    /* synthetic */ void queryAVState(kk kkVar, id idVar);

    /* synthetic */ void queryApplyAddGuildStatus(String str, gr grVar);

    /* synthetic */ void queryChannelOnlineMemberList(ig igVar, gi giVar);

    /* synthetic */ void queryGuildEssence(ff ffVar, fi fiVar);

    /* synthetic */ void queryGuildVisitor(ff ffVar, fi fiVar);

    /* synthetic */ void queryLiveBroadcastRecord(te2.gj gjVar);

    /* synthetic */ void queryPauseLiveBroadcast(ib ibVar, ia iaVar);

    /* synthetic */ void queryTinyIdByLiveUID(List list, se2.bw bwVar);

    /* synthetic */ void refreshAppChannelPreInfos(String str, boolean z15, hp hpVar);

    /* synthetic */ void refreshChannelInfo(String str, String str2, boolean z15, int i15);

    /* synthetic */ void refreshGuildList(boolean z15);

    /* synthetic */ void refreshGuildState();

    /* synthetic */ void refreshGuildUserProfileInfo(String str, String str2, String str3);

    /* synthetic */ void refreshGuildUserProfileInfoV2(go goVar);

    /* synthetic */ void refreshPollingData();

    /* synthetic */ void refreshPollingData(@NonNull String str, int i15);

    /* synthetic */ void refreshPollingData(@NonNull String str, @NonNull String str2);

    /* synthetic */ void removeBanBeforeBroadcast(String str, String str2, se2.bx bxVar);

    /* synthetic */ void removeChannel(String str, String str2, int i15, ca caVar);

    /* synthetic */ void removeChannelCategory(String str, long j15, boolean z15, be beVar);

    /* synthetic */ void removeGuild(com.tencent.mobileqq.qqguildsdk.data.genc.ik ikVar, gr grVar);

    @Nullable
    /* synthetic */ boolean removeGuildGlobalBanner(String str);

    /* synthetic */ void removeGuildSearchSearchHistory(com.tencent.mobileqq.qqguildsdk.data.genc.gh ghVar, gk gkVar);

    /* synthetic */ void removeGuildStateListener(@NonNull bq bqVar);

    /* synthetic */ void removeGuildTaskCard(com.tencent.mobileqq.qqguildsdk.data.genc.il ilVar, hp hpVar);

    /* synthetic */ void removeMemberFromGuildBlackList(String str, @NonNull List list, String str2, bz bzVar);

    /* synthetic */ void removeRiskMembers(com.tencent.mobileqq.qqguildsdk.data.genc.im imVar, te2.gl glVar);

    @Override // re2.b
    /* synthetic */ void removeSpeakOrderByUser(kk kkVar, id idVar);

    /* synthetic */ void reportAISearchRecommendWords(com.tencent.mobileqq.qqguildsdk.data.genc.ad adVar, hp hpVar);

    @Override // re2.b
    /* synthetic */ void reportAVState(com.tencent.mobileqq.qqguildsdk.data.genc.ai aiVar, id idVar);

    /* synthetic */ void reportCurrentGuild(long j15);

    /* synthetic */ void reportFeedShareData(String str, String str2, int i15, com.tencent.mobileqq.qqguildsdk.data.genc.cp cpVar, int i16, ip ipVar, gm gmVar);

    /* synthetic */ void reportFeedback(com.tencent.mobileqq.qqguildsdk.data.genc.cq cqVar, hp hpVar);

    /* synthetic */ void reportInteractiveLikesInGroup(com.tencent.mobileqq.qqguildsdk.data.genc.fa faVar, te2.gn gnVar);

    /* synthetic */ void reportJoinRecommendGuild(te2.go goVar);

    /* synthetic */ void reportPreventAddictionInstructions(com.tencent.mobileqq.qqguildsdk.data.genc.ia iaVar, gd gdVar);

    /* synthetic */ void reportSecurity(com.tencent.mobileqq.qqguildsdk.data.genc.in inVar, int i15, gp gpVar);

    /* synthetic */ void reportShareAtta(io ioVar, hp hpVar);

    void reportShareInfo(String str, String str2, int i15, String str3, int i16, bz bzVar);

    /* synthetic */ void reportUserViewGuild(ArrayList arrayList, String str, String str2, int i15, hp hpVar);

    /* synthetic */ void requestBeginBroadcast(String str, String str2, String str3, int i15, com.tencent.mobileqq.qqguildsdk.data.bn bnVar, ca caVar);

    /* synthetic */ void requestPubAccountMsgCount(gq gqVar);

    /* synthetic */ void riskMemberList(is isVar, gs gsVar);

    /* synthetic */ void saveCustomApplication(iv ivVar, gt gtVar);

    /* synthetic */ void saveGuidNavigation(iw iwVar, gu guVar);

    /* synthetic */ void searchAgents(jc jcVar, gv gvVar);

    /* synthetic */ void searchBlackUserFromServer(jd jdVar, gw gwVar);

    /* synthetic */ void searchChannelContent(String str, String str2, String str3, byte[] bArr, jg jgVar, je jeVar, gx gxVar);

    /* synthetic */ void searchGuild(String str, byte[] bArr, int i15, int i16, int i17, gy gyVar);

    /* synthetic */ void searchGuildContent(com.tencent.mobileqq.qqguildsdk.data.genc.gc gcVar, fk fkVar);

    /* synthetic */ void searchGuildFeeds(jf jfVar, gx gxVar);

    /* synthetic */ void searchGuildMembersBySourceId(com.tencent.mobileqq.qqguildsdk.data.genc.fn fnVar, gz gzVar);

    /* synthetic */ void searchMsgSeqsFromServer(@NonNull com.tencent.mobileqq.qqguildsdk.data.be beVar, @NonNull se2.cm cmVar);

    /* synthetic */ void searchUnion(String str, byte[] bArr, int i15, int i16, int i17, int i18, int i19, se2.cn cnVar);

    @Override // re2.b
    /* synthetic */ void sendAIAppChatInfo(com.tencent.mobileqq.qqguildsdk.data.genc.a aVar, ha haVar);

    /* synthetic */ void sendAudioOrLiveNotice(jh jhVar, hb hbVar);

    /* synthetic */ void sendChannelHeartbeat(com.tencent.mobileqq.qqguildsdk.data.genc.gs gsVar);

    /* synthetic */ void sendOperationSaveMsg(ix ixVar, hc hcVar);

    /* synthetic */ void sendScheduleArkMessage(ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3, com.tencent.mobileqq.qqguildsdk.data.ct ctVar, long j15, int i15, String str, String str2, String str3, byte[] bArr, se2.ck ckVar);

    /* synthetic */ void setAISearchMsgFeedbackStatus(com.tencent.mobileqq.qqguildsdk.data.genc.ab abVar, hp hpVar);

    /* synthetic */ void setAVChannelInviteSpeakSwitch(long j15, long j16, com.tencent.mobileqq.qqguildsdk.data.genc.gt gtVar, hp hpVar);

    /* synthetic */ void setAVChannelSpeakMode(long j15, long j16, String str, kx kxVar, ho hoVar);

    @Override // re2.b
    /* synthetic */ void setAVChannelSpeakOrder(long j15, long j16, kr krVar, hp hpVar);

    /* synthetic */ void setChannelAuthControl(long j15, long j16, int i15, int i16, gr grVar);

    /* synthetic */ void setChannelAuthControlMemberList(long j15, long j16, ArrayList arrayList, ArrayList arrayList2, int i15, gr grVar);

    @Override // re2.b
    /* synthetic */ void setChannelAuthControlRoleList(long j15, long j16, ArrayList arrayList, ArrayList arrayList2, int i15, gr grVar);

    /* synthetic */ void setChannelAuthInfo(com.tencent.mobileqq.qqguildsdk.data.genc.gq gqVar, te2.he heVar);

    /* synthetic */ void setChannelBannedSpeak(String str, int i15, ca caVar);

    /* synthetic */ void setChannelCategory(String str, String str2, long j15, be beVar);

    /* synthetic */ void setChannelCategoryInfo(String str, com.tencent.mobileqq.qqguildsdk.data.b bVar, ArrayList arrayList, be beVar);

    /* synthetic */ void setChannelCategoryOrder(ji jiVar, te2.hd hdVar);

    /* synthetic */ void setChannelCategoryOrder(String str, ArrayList arrayList, be beVar);

    /* synthetic */ void setChannelHeartBeatScene(com.tencent.mobileqq.qqguildsdk.data.genc.gr grVar);

    /* synthetic */ void setChannelHeartbeat(com.tencent.mobileqq.qqguildsdk.data.genc.gs gsVar);

    /* synthetic */ void setChannelLiveable(String str, String str2, int i15, @NonNull ca caVar);

    /* synthetic */ void setChannelLiveableMemberList(String str, String str2, @Nullable List list, @Nullable List list2, @NonNull ca caVar);

    @Override // re2.d
    /* synthetic */ void setChannelLiveableRoleList(String str, String str2, @Nullable List list, @Nullable List list2, @NonNull ca caVar);

    /* synthetic */ void setChannelName(String str, String str2, String str3, se2.cp cpVar);

    /* synthetic */ void setChannelSlowMode(String str, int i15, ca caVar);

    /* synthetic */ void setChannelSpeakable(String str, String str2, int i15, @NonNull ca caVar);

    /* synthetic */ void setChannelSpeakableMemberList(String str, String str2, @Nullable List list, @Nullable List list2, @NonNull ca caVar);

    @Override // re2.d
    /* synthetic */ void setChannelSpeakableRoleList(String str, String str2, @Nullable List list, @Nullable List list2, @NonNull ca caVar);

    /* synthetic */ void setChannelTopMsg(String str, String str2, long j15, int i15, int i16, @NonNull bz bzVar);

    /* synthetic */ void setChannelUserNumLimit(long j15, long j16, int i15, boolean z15, int i16, gr grVar);

    /* synthetic */ void setChannelVisible(String str, String str2, int i15, @NonNull ca caVar);

    /* synthetic */ void setChannelVisibleMemberList(String str, String str2, @Nullable List list, @Nullable List list2, @NonNull ca caVar);

    @Override // re2.d
    /* synthetic */ void setChannelVisibleRoleList(String str, String str2, @Nullable List list, @Nullable List list2, @NonNull ca caVar);

    /* synthetic */ void setClientShowConfig(String str, int i15, int i16, se2.cq cqVar);

    /* synthetic */ void setDirectMsgBlack(String str, boolean z15, @NonNull ca caVar);

    /* synthetic */ void setDirectMsgNotifyForGuild(int i15, boolean z15, @NonNull ca caVar);

    /* synthetic */ void setDirectMsgNotifyType(String str, int i15, @NonNull ca caVar);

    /* synthetic */ void setDirectMsgStatus(int i15, int i16, @NonNull ca caVar);

    /* synthetic */ void setEnteredChannelState(long j15, long j16, boolean z15);

    /* synthetic */ void setFeedCommentsNotifySwitch(int i15, se2.j jVar);

    void setGender(int i15, bz bzVar);

    /* synthetic */ void setGlobalPrivacySwitch(jl jlVar, te2.ff ffVar);

    /* synthetic */ void setGuildAuditInfo(com.tencent.mobileqq.qqguildsdk.data.genc.fc fcVar, te2.g gVar);

    /* synthetic */ void setGuildBoundGroups(jr jrVar, te2.hf hfVar);

    /* synthetic */ void setGuildCommonUnNotifyFlag(long j15, int i15, int i16, te2.hk hkVar);

    /* synthetic */ void setGuildCoverFontColorId(String str, int i15, se2.cr crVar);

    /* synthetic */ void setGuildCreatorTaskSkipStatus(String str, hp hpVar);

    /* synthetic */ void setGuildFeedExperiment(com.tencent.mobileqq.qqguildsdk.data.genc.fh fhVar, hg hgVar);

    /* synthetic */ void setGuildGlobalBanner(com.tencent.mobileqq.qqguildsdk.data.genc.ey eyVar, fe feVar);

    /* synthetic */ void setGuildGlobalPermission(jk jkVar, gr grVar);

    /* synthetic */ void setGuildGroupMemberList(jn jnVar, hh hhVar);

    /* synthetic */ void setGuildListSort(ArrayList arrayList, se2.ct ctVar);

    /* synthetic */ void setGuildListTop(String str, int i15, int i16, hi hiVar);

    /* synthetic */ void setGuildMemberName(String str, String str2, gr grVar);

    /* synthetic */ void setGuildName(String str, String str2, se2.cs csVar);

    /* synthetic */ void setGuildNeedRealNameForVisitor(String str, boolean z15, hp hpVar);

    /* synthetic */ void setGuildNewCommonSwitch(jo joVar, hj hjVar);

    /* synthetic */ void setGuildNumSearchSwitch(long j15, int i15, gr grVar);

    /* synthetic */ void setGuildProfile(String str, String str2, ca caVar);

    /* synthetic */ void setGuildPublishPostMustChooseChannelSwitch(long j15, int i15, gr grVar);

    /* synthetic */ void setGuildQRCodePeriod(String str, int i15, ca caVar);

    /* synthetic */ void setGuildQRCodeSwitch(long j15, int i15, gr grVar);

    /* synthetic */ void setGuildRulesDirect(long j15, ArrayList arrayList, gr grVar);

    /* synthetic */ void setGuildShutUp(String str, long j15, @NonNull ca caVar);

    /* synthetic */ void setGuildSpeakableThreshold(String str, com.tencent.mobileqq.qqguildsdk.data.eo eoVar, gr grVar);

    /* synthetic */ void setGuildUnNotifyFlag(String str, int i15, se2.cu cuVar);

    /* synthetic */ void setGuildVisibilityForVisitor(String str, boolean z15, @NonNull bz bzVar);

    /* synthetic */ void setGuildWeakNotifyDisplay(String str, int i15, se2.cv cvVar);

    /* synthetic */ void setJoinGuildOption(jp jpVar, gr grVar);

    /* synthetic */ void setJumpToCategory(com.tencent.mobileqq.qqguildsdk.data.genc.hb hbVar, gr grVar);

    /* synthetic */ void setListenTogetherPlayOpt(long j15, long j16, int i15, te2.fu fuVar);

    /* synthetic */ void setListenTogetherPlayVolumeByAdmin(long j15, long j16, int i15, te2.fu fuVar);

    /* synthetic */ void setLiveChannelAnchorList(String str, String str2, List list, List list2, ca caVar);

    /* synthetic */ void setLiveChannelBannedUser(String str, String str2, String str3, String str4, int i15, bz bzVar);

    void setLocation(GProLocationInfo gProLocationInfo, bz bzVar);

    @Override // re2.f
    /* synthetic */ void setMemberRoles(String str, String str2, String str3, String str4, @NonNull List list, @NonNull List list2, @NonNull ca caVar);

    /* synthetic */ void setMemberShutUp(String str, String str2, long j15, @NonNull ca caVar);

    /* synthetic */ void setMyBirthday(short s15, short s16, short s17, hp hpVar);

    /* synthetic */ void setMyGuildCommonSwitch(com.tencent.mobileqq.qqguildsdk.data.genc.ax axVar, te2.hl hlVar);

    /* synthetic */ void setMyMsgNotifyType(String str, String str2, int i15, se2.co coVar);

    /* synthetic */ void setMyPersonalSignature(String str, hp hpVar);

    /* synthetic */ void setMyProfileShowTypeSwitch(String str, int i15, int i16, hp hpVar);

    /* synthetic */ void setMyVoiceNetworkQuality(String str, String str2, int i15);

    /* synthetic */ void setMyVoiceSysMicStatus(String str, String str2, int i15);

    /* synthetic */ void setNickName(String str, boolean z15, hp hpVar);

    /* synthetic */ void setOnLogin(boolean z15);

    /* synthetic */ void setProfileSwitch(int i15, boolean z15, hp hpVar);

    @Override // re2.e
    /* synthetic */ void setQQMsgListChannel(@NotNull String str, @NotNull String str2, int i15, @NotNull bz bzVar);

    /* synthetic */ void setQQMsgListGuild(long j15, int i15, int i16, te2.hm hmVar);

    @Override // re2.f
    /* synthetic */ void setRoleInfo(String str, String str2, @NonNull fa faVar, @NonNull ca caVar);

    /* synthetic */ void setRoleMembers(String str, String str2, String str3, String str4, @NonNull List list, @NonNull List list2, @NonNull ca caVar);

    @Override // re2.f
    /* synthetic */ void setRoleOrder(String str, @NonNull List list, @NonNull ca caVar);

    @Override // re2.f
    /* synthetic */ void setRoleSpeakableChannels(String str, String str2, @NonNull List list, @NonNull List list2, @NonNull bz bzVar);

    @Override // re2.f
    /* synthetic */ void setRoleVisibleChannels(String str, String str2, @NonNull List list, @NonNull List list2, @NonNull bz bzVar);

    /* synthetic */ void setScreenSharedInGuild(String str, String str2, boolean z15, cl clVar);

    /* synthetic */ void setSearchSwitch(jq jqVar, te2.hn hnVar);

    void setSelfTinyId(String str);

    /* synthetic */ void setShowArchive(String str, boolean z15, se2.cx cxVar);

    /* synthetic */ void setShowPresence(String str, boolean z15, se2.cx cxVar);

    /* synthetic */ void setTopicSquareEntranceSwitch(long j15, boolean z15, gr grVar);

    /* synthetic */ void setUserLiveInfo(int i15, String str, String str2, int i16, se2.cw cwVar);

    /* synthetic */ void setUserMuteSeatInGuild(String str, String str2, String str3, boolean z15, bz bzVar);

    /* synthetic */ void setUserPermissionInGuild(long j15, long j16, long j17, boolean z15, gr grVar);

    /* synthetic */ void setUserType(String str, se2.cz czVar);

    /* synthetic */ void setUserTypeWithUserConfigure(boolean z15, Cif cif);

    /* synthetic */ void setUserVoicelessToMe(String str, boolean z15, bz bzVar);

    /* synthetic */ void startLiveBroadcast(ju juVar, te2.hq hqVar);

    /* synthetic */ void startPollingForDiscoverState();

    @Override // re2.b
    /* synthetic */ void stopAIAppChat(com.tencent.mobileqq.qqguildsdk.data.genc.p pVar, hr hrVar);

    /* synthetic */ void stopPollingForDiscoverState(boolean z15);

    /* synthetic */ void subscribePolling(@NonNull String str);

    /* synthetic */ void subscribePollingChannels(@NonNull List list);

    /* synthetic */ void subscribePollingSchedule(jv jvVar);

    /* synthetic */ void superAdminBanUser(jw jwVar, hp hpVar);

    /* synthetic */ void superAdminUnListFeed(com.tencent.mobileqq.qqguildsdk.data.genc.bf bfVar, hs hsVar);

    /* synthetic */ void switchAVLobbyApp(jx jxVar, te2.aa aaVar);

    /* synthetic */ void terminateLiveBroadcast(com.tencent.mobileqq.qqguildsdk.data.genc.hg hgVar, hu huVar);

    /* synthetic */ void terminateLiveStream(String str, String str2, int i15, int i16, se2.cy cyVar);

    /* synthetic */ void terminateLiveStream(String str, String str2, int i15, se2.cy cyVar);

    @Override // re2.b
    /* synthetic */ void topSpeakOrderByAdmin(aj ajVar, te2.e eVar);

    /* synthetic */ void transThirdPlatformURL(ka kaVar, hv hvVar);

    /* synthetic */ void transThirdVideoURL(kb kbVar, hw hwVar);

    /* synthetic */ void transferGuildOwner(kc kcVar, hp hpVar);

    /* synthetic */ void tryUnmuteMicrophone(String str, String str2, boolean z15, @NonNull bz bzVar);

    /* synthetic */ void ubsubscribePollingChannels(@NonNull List list);

    /* synthetic */ void unFollowGuildAuthor(ke keVar, te2.hx hxVar);

    /* synthetic */ void unSubscribePollingChannelsByPollType(int i15);

    /* synthetic */ void unsubscribePolling(@NonNull String str);

    /* synthetic */ void updateAIAppDraftState(com.tencent.mobileqq.qqguildsdk.data.genc.q qVar, te2.hy hyVar);

    /* synthetic */ void updateAudioLiveChannelTheme(ki kiVar, hp hpVar);

    /* synthetic */ void updateAuthorInfo(kh khVar, hz hzVar);

    /* synthetic */ void updateForumChannelSortMode(String str, String str2, int i15, @NonNull ca caVar);

    /* synthetic */ void updateGuildEssence(ff ffVar, fi fiVar);

    /* synthetic */ void updateGuildTaskCardProgress(kj kjVar, hp hpVar);

    /* synthetic */ void updateGuildVisitor(ff ffVar, fi fiVar);

    /* synthetic */ void upgradeGroupFeed(ArrayList arrayList, int i15, te2.ib ibVar);

    /* synthetic */ void upsertGuildSearchSearchHistory(com.tencent.mobileqq.qqguildsdk.data.genc.gh ghVar, te2.ic icVar);

    @Override // re2.b
    /* synthetic */ void userDealSpeakInvitation(kk kkVar, id idVar);

    @Override // re2.b
    /* synthetic */ void userHandUp(kk kkVar, id idVar);

    /* synthetic */ void userOnlineReport(com.tencent.mobileqq.qqguildsdk.data.genc.fp fpVar, te2.ie ieVar);

    /* synthetic */ void userShareScreen(kk kkVar, id idVar);

    /* synthetic */ void voiceChannelAuth0x10b6(String str, String str2, int i15, boolean z15, int i16, se2.da daVar);

    /* synthetic */ void voiceSmobaGameCreateRoom(ks ksVar, kw kwVar, te2.ig igVar);

    /* synthetic */ void voiceSmobaGameDissmissRoom(kt ktVar, hp hpVar);

    /* synthetic */ void voiceSmobaGameEnterRoom(long j15, int i15, long j16, long j17, ih ihVar);

    /* synthetic */ void voiceSmobaGameKickOutRoom(ku kuVar, ij ijVar);

    /* synthetic */ void voiceSmobaGameQuitRoom(kv kvVar, te2.ii iiVar);
}