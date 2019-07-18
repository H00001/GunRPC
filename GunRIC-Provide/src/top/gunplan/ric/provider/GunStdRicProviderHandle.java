package top.gunplan.ric.provider;

import top.gunplan.ric.apis.test.anno.GunUseImpl;
import top.gunplan.ric.protocol.*;
import top.gunplan.ric.stand.GunRicInvokeReqStand;
import top.gunplan.ric.stand.GunRicInvokeResStand;
import top.gunplan.utils.AbstractGunBaseLogUtil;

import java.lang.reflect.Method;


import static top.gunplan.ric.protocol.RicProtocolCode.FAIL;
import static top.gunplan.ric.provider.GunRicProviderException.GunRicProviderErrorType.INVOKE_ERROR;


/**
 * @author dosdrtt
 */
public class GunStdRicProviderHandle implements GunRicInvoker<GunRicInvokeReqStand>, GunRicCommonRealDeal<GunRicInvokeReqStand, GunRicInvokeResStand> {

    @Override
    public GunRicInvokeResStand dealDataEvent(final GunRicInvokeReqStand i) {
        AbstractGunRicExecuteProtocol.ParamHelper helper = new AbstractGunRicExecuteProtocol.ParamHelper();
        final GunRicOutputProtocol o = new GunRicOutputProtocol(i);
        try {
            helper = invokeMethod(i);
            o.setResult(helper);
        } catch (ReflectiveOperationException e) {
            helper.setObj(e.getClass().getSimpleName() + ":" + e.getMessage());
            AbstractGunBaseLogUtil.error(e);
            o.setCode(FAIL);
        }
        return o;

    }

    @Override
    public AbstractGunRicExecuteProtocol.ParamHelper invokeMethod(GunRicInvokeReqStand inputProtocol) throws ReflectiveOperationException {
        AbstractGunRicExecuteProtocol.ParamHelper help = new AbstractGunRicExecuteProtocol.ParamHelper();
        Class<?> inst = Class.forName(inputProtocol.interfaceName());
        Object rpcService = Class.forName(inst.getAnnotation(GunUseImpl.class).impl()).getDeclaredConstructor().newInstance();
        Method instMethod = inst.getMethod(inputProtocol.methodName(), inputProtocol.paramTypes());
        if (instMethod == null) {
            help.setObj("method not found");
            AbstractGunBaseLogUtil.error(inputProtocol.methodName(), "method not found", "[PROVIDE]");
            throw new GunRicProviderException("method not found", INVOKE_ERROR);
        }
        help.setObj(inputProtocol.paramLength() == 0 ? instMethod.invoke(rpcService) : instMethod.invoke(rpcService, inputProtocol.parameters()));
        return help;
    }


}
