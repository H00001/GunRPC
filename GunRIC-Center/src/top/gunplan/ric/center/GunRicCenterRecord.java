package top.gunplan.ric.center;

import top.gunplan.netty.GunHandle;
import top.gunplan.ric.protocol.GunAddressItem;

/**
 * GunRicCenterRecord
 * @date 2019/05/28
 * @author dosdrtt
 */
public interface GunRicCenterRecord extends GunHandle {
    /**
     * when record first request
     *
     * @param g       ric record from information
     * @param address address info
     */

    void firstAdd(GunRicCdtInterface g, GunAddressItem address);

    /**
     * when record next request
     *
     * @param g       ric record from information
     * @param address address info
     */
    void nextAdd(GunRicCdtInterface g, GunAddressItem address);


}
