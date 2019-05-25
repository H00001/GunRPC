package top.gunplan.ric.provider;

import top.gunplan.netty.GunException;
import top.gunplan.netty.protocol.GunNetInputInterface;
import top.gunplan.netty.protocol.GunNetOutputInterface;
import top.gunplan.ric.protocol.*;

/**
 * @author dosdrtt
 * @concurrent AbstractGunRicBaseProviderHandle
 */
public abstract class AbstractGunRicBaseProviderHandle implements GunRicBaseHandle {
    /**
     * @param protocol GunRicInputProtocol
     * @return AbstractGunRicProtocol
     */
    @Override
    public abstract GunNetOutputInterface dealEvent(GunRicInputProtocol protocol);

    @Override
    public AbstractGunRicProtocol dealEvent(GunRicRegisterProtocol protocol) {
        throw new GunInvidaProtocolExection("GunRicRegisterProtocol", "GunRIC-Provicer");
    }

    @Override
    public AbstractGunRicProtocol dealEvent(GunRicGetAddressProcotol protocol) {
        throw new GunInvidaProtocolExection("GunRicRegisterProtocol", "GunRIC-Provicer");
    }

    @Override
    public GunNetOutputInterface dealDataEvent(GunNetInputInterface var1) throws GunException {
        if (var1 instanceof GunRicHelloProtocol) {
            /**
             *   hello do not need packet because hello send have interval
             */
            return dealEvent((GunRicHelloProtocol) (var1));
        } else if (var1 instanceof GunRicInputProtocol) {

            return dealEvent((GunRicInputProtocol) (var1));
        }
        return null;
    }
}
