package top.gunplan.ric.center.manage;

import top.gunplan.ric.protocol.BaseGunRicCdt;
import top.gunplan.ric.protocol.GunAddressItemInterface;

import java.util.Set;
import java.util.stream.Stream;

/**
 * GunRicClientManager
 *
 * @author frank albert
 * @version 0.0.0.2
 * @date 2019-07-19 20:13
 */

public interface GunRicClientManager<U extends GunRicClient> {

    Set<U> clientList();

    int normalSize();

    GunProviderAliveCheckResult aliveCheck();

    void register(GunAddressItemInterface user, BaseGunRicCdt cdt);

    U removeById(long id);

    void remove(U u);


    void inforToRecorder(Stream<U> stream);
}
