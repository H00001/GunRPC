package top.gunplan.ric.center.manage.impl;

import top.gunplan.ric.center.common.GunRICChannels;
import top.gunplan.ric.center.manage.GunRicProviderClient;
import top.gunplan.ric.common.F;
import top.gunplan.ric.protocol.BaseGunRicServerInformation;
import top.gunplan.ric.protocol.GunAddressItemInterface;
import top.gunplan.ric.protocol.GunRicHelloProtocol;
import top.gunplan.ric.stand.GunRicHelloStand;
import top.gunplan.utils.GunLogger;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Set;

/**
 * GunRicProviderClientImpl
 *
 * @author frank albert
 * @version 0.0.0.4
 * @date 2019-07-19 20:02
 */
public class GunRicProviderClientImpl implements GunRicProviderClient {
    private static GunLogger logger = F.LOG.setTAG(GunRicProviderClientImpl.class);
    private volatile SocketChannel channel;
    private final GunAddressItemInterface address;
    private final Set<BaseGunRicServerInformation> cdt;
    private int reTimes = 0;


    public GunRicProviderClientImpl(GunAddressItemInterface address, BaseGunRicServerInformation cdt) {
        this.address = address;
        this.cdt = new HashSet<>();
        this.cdt.add(cdt);
    }

    @Override
    public int init() {
        SocketChannel socketChannel;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(address.getInet());
        } catch (IOException e) {
            logger.error(e);
            return -2;
        }
        this.channel = socketChannel;
        return 0;
    }


    @Override
    public int destroy() {
        try {
            channel.close();
        } catch (IOException e) {
            F.LOG.error(e);
            return -1;
        }
        return 0;
    }

    @Override
    public Channel channel() {
        return channel;
    }


    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public Set<BaseGunRicServerInformation> cdt() {
        return cdt;
    }

    @Override
    public void addCdt(BaseGunRicServerInformation c) {
        cdt.add(c);
    }

    @Override
    public GunAddressItemInterface addressInformation() {
        return address;
    }

    @Override
    public byte flag() {
        //not use
        return 0;
    }

    @Override
    public boolean doCheck() {
        if (!GunRICChannels.channelAvailable(channel)) {
            init();
            if (!GunRICChannels.channelAvailable(channel)) {
                update();
                return false;
            }
        }
        try {
            GunRicHelloStand hello = new GunRicHelloProtocol(true);
            short number = (short) hello.serialNumber();
            if (!GunRICChannels.channelWrite(channel, hello.serialize())) {
                return false;
            }
            byte[] b = GunRICChannels.channelRead(channel, 8);
            if (b != null) {
                hello.unSerialize(b);
                if (number + 1 == hello.serialNumber()) {
                    logger.info("connect is normal now");
                } else {
                    logger.info("connect is not now " + number + " " + hello.serialNumber());
                }
            } else {
                channel = null;
            }

        } catch (IOException e) {
            logger.error(e);
        }
        return true;
    }

    @Override
    public int reConnectionTimes() {
        return reTimes;
    }

    @Override
    public void clean() {
        reTimes = 0;
    }

    @Override
    public void update() {
        reTimes++;
    }


}
