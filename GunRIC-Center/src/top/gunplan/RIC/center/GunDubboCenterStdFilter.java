package top.gunplan.RIC.center;


import top.gunplan.netty.GunNettyFilter;
import top.gunplan.netty.anno.GunNetFilterOrder;
import top.gunplan.netty.impl.GunInputFilterChecker;
import top.gunplan.netty.impl.GunOutputFilterChecker;
import top.gunplan.netty.protocol.GunNetInputInterface;
import top.gunplan.protocol.AbstractGunRicProtocol;
import top.gunplan.protocol.GunRicTypeDividePacketManage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dosdrtt
 * @date 1557231535
 */
@GunNetFilterOrder(index = 2)
public class GunDubboCenterStdFilter implements GunNettyFilter {
    @Override
    public DealResult doInputFilter(GunInputFilterChecker filterDto) {
        InetSocketAddress address;
        try {
            address = (InetSocketAddress) ((SocketChannel) filterDto.getKey().channel()).getRemoteAddress();
        } catch (IOException e) {
            return DealResult.NOTDEALALLNEXT;
        }
        final GunRicDividePacketFilter.GunDividePacketDto dto = (GunRicDividePacketFilter.GunDividePacketDto) filterDto.getObject();
        if (dto.size != 0) {
            List<GunNetInputInterface> gnii = new ArrayList<>(1);
            for (int i = 0; i < dto.size; i++) {
                AbstractGunRicProtocol protocol = GunRicTypeDividePacketManage.findPackage(dto.getPackets().get(i));
                protocol.unSerialize(dto.getPackets().get(i));
                gnii.add(protocol);
            }
            GunRicCenterDto rdto = new GunRicCenterDto(address, gnii);
            //   GunRicCenterDto dto = new GunRicCenterDto(address, protocol);

            filterDto.setObject(rdto);
            return DealResult.NEXT;
        }
        return DealResult.NOTDEALALLNEXT;

    }

    @Override
    public DealResult doOutputFilter(GunOutputFilterChecker filterDto) throws Exception {
        return null;
    }
}