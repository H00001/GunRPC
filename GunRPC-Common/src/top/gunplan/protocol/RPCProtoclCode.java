package top.gunplan.protocol;

/**
 * @author dosdrtt
 */

public enum RPCProtoclCode {
    /**
     *
     */
    SUCCEED(0x00), WARNNING(0x02), FAIL(0x19);
    int value;

    RPCProtoclCode(int i) {
        this.value = i;
    }

    public static RPCProtoclCode valuefrom(int val) {
        RPCProtoclCode[] types = values();
        for (RPCProtoclCode tp : types) {
            if (tp.value == val) {
                return tp;
            }
        }
        return null;
    }
}
