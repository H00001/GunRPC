package top.gunplan.RPC.server;

import top.gunplan.netty.GunException;
import top.gunplan.netty.GunNettyHandle;
import protocol.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.SocketChannel;
import java.util.ServiceLoader;

/**
 * @author dosdrtt
 */
public class GunStdRPCHandle implements GunNettyHandle {

    @Override
    public GunNetOutputInterface dealDataEvent(GunNetInputInterface gunNetInputInterface) {
        final GunRPCOutputProtocl outputprotocl = new GunRPCOutputProtocl();
        final GunRPCInputProtocl inoutprotocl = ((GunRPCInputProtocl) gunNetInputInterface);
        outputprotocl.setType(RPCProtoclType.RESPONSE);
        try {
            Class<?> inst = Class.forName(inoutprotocl.getInterfaceName());
            ServiceLoader<?> spiLoader = ServiceLoader.load(inst);
            Object dubboService = spiLoader.iterator().next();
            Method realmd = null;
            for (Method md : dubboService.getClass().getMethods()) {
                if (md.getName().equals(inoutprotocl.getMethodName())) {
                    realmd = md;
                    break;
                }
            }
            assert realmd != null;
            Object oc = inoutprotocl.getParamleng() == 0 ? realmd.invoke(dubboService) : realmd.invoke(dubboService, inoutprotocl.getParameters());
            outputprotocl.setCode(RPCProtoclCode.SUCCEED);
            outputprotocl.setReturnValue(oc);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            outputprotocl.setCode(RPCProtoclCode.FAIL);
        }

        return outputprotocl;
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

    }
}
