package com.jsycloud.exception;

/**
 * <p>
 * </p>
 * 
 * @author fangzhihua 2014-5-7 下午3:00:20
 * @version V1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2014-5-7
 * @modify by reason:{方法名}:{原因}
 */
public class IDpsdkCoreException extends Exception {
    /** 变量/常量说明 */
    private static final long serialVersionUID = 1L;

    /** no error(const value:0) */
    public static final int IDPSDKCORE_NO_ERROR = 0;

    /** 错误码 */
    private int mErrorCode = IDPSDKCORE_NO_ERROR;

    public IDpsdkCoreException() {
    }

    public IDpsdkCoreException(String msg) {
        super(msg);
    }

    public IDpsdkCoreException(String msg, int errorCode) {
        super(msg);
        mErrorCode = errorCode;
    }

    /**
     * <p>
     * </p>
     * 
     * @author fangzhihua 2014-5-7 下午3:00:35
     * @return
     */
    public int getErrorCode() {
        return mErrorCode;
    }
}
