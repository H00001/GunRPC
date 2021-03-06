package top.gunplan.ric.protocol;

import top.gunplan.netty.GunException;
import top.gunplan.ric.protocol.exp.GunRicException;

/**
 * @author dosdrtt
 * @version 0.0.0.1
 * @see GunException
 * @since 0.0.1.5
 */
class GunObjectCannotSerializableException extends GunRicException {
    private static final long serialVersionUID = -3559997432609348176L;

    GunObjectCannotSerializableException(String why) {
        super(why);
    }
}
