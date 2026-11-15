package com.tencent.qqnt.kernel.api;

import com.tencent.hippy.qq.utils.WebSSOUtils;
import com.tencent.mobileqq.qroute.QRouteApi;
import com.tencent.mobileqq.qroute.annotation.QAPI;
import java.util.Set;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import p022mqq.app.MSFServlet;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H&J\u0010\u0010\u0005\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00070\u0006H&J&\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u00042\b\u0010\r\u001a\u0004\u0018\u00010\tH&JF\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u00042\b\u0010\r\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00112\b\u0010\u0014\u001a\u0004\u0018\u00010\u0004H&J\b\u0010\u0015\u001a\u00020\u000fH&¨\u0006\u0016"}, d2 = {"Lcom/tencent/qqnt/kernel/api/IServletAPI;", "Lcom/tencent/mobileqq/qroute/QRouteApi;", "getNTCmd", "", "", "getPushMsgServlet", "Ljava/lang/Class;", "Lmqq/app/MSFServlet;", "maybeMockNerveCordSSO", "", "requestId", "", "cmd", "body", "onNerveCordSSOReply", "", WebSSOUtils.CALLBACK_COLUMN_SSORET, "", "trpcRet", "trpcFuncCode", "errorMsg", "setServletKernelInit", "kernel_api_release"}, k = 1, mv = {1, 9, 0}, xi = 48)
@QAPI(process = {"all"})
public interface IServletAPI extends QRouteApi {
    @NotNull
    Set<String> getNTCmd();

    @NotNull
    Class<? extends MSFServlet> getPushMsgServlet();

    @Nullable
    byte[] maybeMockNerveCordSSO(long requestId, @Nullable String cmd, @Nullable byte[] body);

    void onNerveCordSSOReply(long requestId, @Nullable String cmd, @Nullable byte[] body, int ssoRet, int trpcRet, int trpcFuncCode, @Nullable String errorMsg);

    void setServletKernelInit();
}