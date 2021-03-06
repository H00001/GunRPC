package top.gunplan.ric.center;


import top.gunplan.netty.impl.property.GunNettyCoreProperty;
import top.gunplan.netty.observe.GunNettyDefaultObserve;
import top.gunplan.ric.center.context.F;
import top.gunplan.ric.center.record.*;

/**
 * GunRicCenterObserve
 *
 * @author dosdrtt
 */
public class GunRicCenterObserve extends GunNettyDefaultObserve {
    private GunRicCenterRecordManager manage = GunRicCenterStdRecordManage.Instance.getHinstance();

    @Override
    public boolean onBooting(GunNettyCoreProperty gunProperty) {
        super.onBooting(gunProperty);
        manage.registerFirst(new GunRicCenterPathRecord(null));
        AbstractGunRicProxyRecord r2 = new GunRicCenterFileRecord(new GunRicCenterRedisRecord(new GunRicCenterInlineBufferRecord(null)));
        manage.registerLoop(r2);
        try {
            return GunRicRegisterManage.loadRegister();
        } catch (Exception e) {
            F.LOG.error(e);
            return true;
        }
    }

    @Override
    public void onBooted(GunNettyCoreProperty property) {
        super.onBooted(property);
    }
}
