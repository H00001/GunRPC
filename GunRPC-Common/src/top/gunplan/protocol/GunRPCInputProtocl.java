package top.gunplan.protocol;

import top.gunplan.netty.GunException;
import top.gunplan.utils.GunBytesUtil;

/**
 * @author dosdrtt
 */
public final class GunRPCInputProtocl extends AbstractGunRPCExecuteProtocol {
    private ParamHelper[] helpers;

    private int paramlen = 0;

    public ParamHelper[] getParaHelpers() {
        return helpers;
    }

    private boolean analyizeParams(int paramlen, GunBytesUtil.GunReadByteUtil util) {
        helpers = new ParamHelper[paramlen];
        for (int i = 0; i < paramlen; i++) {
            helpers[i] = readOnceParam(util);
        }
        return true;
    }

    public void setParamLen(int len) {
        paramlen = len;
        helpers = new ParamHelper[len];
    }


    private boolean writeParam(GunBytesUtil.GunWriteByteUtil util) {
        for (int i = 0; i < paramlen; i++) {
            Object fil = helpers[i].obj;
            writeOnceParam(util, fil);
        }
        return true;
    }


    public byte[] serialize() {

        int len = 9 + methodName.length() + interfaceName.length() + otherCount;
        byte[] serize = new byte[len];
        GunBytesUtil.GunWriteByteUtil serizUtil = new GunBytesUtil.GunWriteByteUtil(serize);
        publicSet(serizUtil);
        serizUtil.writeByte((byte) interfaceName.length());
        serizUtil.write(interfaceName);
        serizUtil.writeByte((byte) methodName.length());
        serizUtil.write(methodName);
        serizUtil.writeByte((byte) paramlen);
        if (!writeParam(serizUtil)) {
            throw new GunException("write Param error");
        }
        serizUtil.write(endFlage);
        return serize;
    }


    public void pushParam(Object obj) {
        otherCount += 1;
        otherCount = addLenByParam(otherCount, obj);
        helpers[now++] = new ParamHelper(obj, RPCProtoclParamType.valuefrom(obj.getClass()).clazz);

    }

    private int now;

    private int otherCount = 0;
    private String methodName;
    private String interfaceName;


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public byte getParamleng() {
        return (byte) paramlen;
    }


    @Override
    public boolean unSerialize(byte[] in) {
        GunBytesUtil.GunReadByteUtil unserizutil = new GunBytesUtil.GunReadByteUtil(in);
        publicUnSet(unserizutil);
        int interlen = unserizutil.readByte();
        this.interfaceName = new String(unserizutil.readByte(interlen));
        int methodlen = unserizutil.readByte();
        this.methodName = new String(unserizutil.readByte(methodlen));
        this.paramlen = unserizutil.readByte();
        return paramlen == 0 ? checkEnd(unserizutil) : analyizeParams(paramlen, unserizutil);


    }


    public Class<?>[] getParamTypeList() {
        Class<?>[] var2 = new Class<?>[paramlen];
        for (int i = 0; i < paramlen; i++) {
            var2[i] = helpers[i].clazz;
        }
        return var2;
    }

    public Object[] getParameters() {
        Object[] var2 = new Object[paramlen];
        for (int i = 0; i < paramlen; i++) {
            var2[i] = helpers[i].obj;
        }
        return var2;
    }
}