package top.gunplan.ric.server;

import top.gunplan.ric.apis.test.anno.GunUseImpl;
import top.gunplan.netty.GunException;
import top.gunplan.netty.GunNettyHandle;
import top.gunplan.netty.protocol.GunNetInputInterface;
import top.gunplan.protocol.*;
import top.gunplan.netty.protocol.GunNetOutputInterface;
import top.gunplan.utils.AbstractGunBaseLogUtil;

import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;

/**
 * @author dosdrtt
 */
public class GunStdRicHandle implements GunNettyHandle {

    @Override
    public GunNetOutputInterface dealDataEvent(GunNetInputInterface gunNetInputInterface) {
        AbstractGunRicExecuteProtocol.ParamHelper help = new AbstractGunRicExecuteProtocol.ParamHelper();
        final GunRicOutputProtocol o = new GunRicOutputProtocol();
        final GunRicInputProtocol i = ((GunRicInputProtocol) gunNetInputInterface);
        o.setReturnValue(help);
        o.setType(RicProtocolType.RESPONSE);
        o.setSerialnumber(i.getSerialnumber());
        try {
            if (invokeMethod(help, o, i)) {
                return o;
            }
        } catch (ReflectiveOperationException e) {
            help.setObj(e.getClass().getSimpleName() + ":" + e.getLocalizedMessage());
            o.setCode(RicProtoclCode.FAIL);
        } catch (Exception exp) {
            this.dealExceptionEvent(exp);
        }
        return o;
    }

    private boolean invokeMethod(AbstractGunRicExecuteProtocol.ParamHelper help, GunRicOutputProtocol outputpol, GunRicInputProtocol inputpol) throws Exception {
        Class<?> inst = Class.forName(inputpol.gIN());
        Object rpcService = Class.forName(inst.getAnnotation(GunUseImpl.class).impl()).newInstance();
        Method realmd = inst.getMethod(inputpol.gMN(), inputpol.getParamTypeList());
        if (realmd == null) {
            outputpol.setCode(RicProtoclCode.FAIL);
            help.setObj("method not found ");
            AbstractGunBaseLogUtil.error(inputpol.gMN(), "method not found", "[PROVIDE]");
            return false;
        }
        help.setObj(inputpol.getParamleng() == 0 ? realmd.invoke(rpcService) : realmd.invoke(rpcService, inputpol.getParameters()));
        outputpol.setCode(RicProtoclCode.SUCCEED);
        return true;
    }


    @Override
    public GunNetOutputInterface dealConnEvent(SocketChannel socketChannel) throws GunException {
        return null;
    }

    @Override
    public void dealCloseEvent() {

    }

    @Override
    public void dealExceptionEvent(Exception e) {
        AbstractGunBaseLogUtil.error(e.getMessage());
    }
}
