package com.audio.haillcall.callback;

/**
 * Created by Time on 16/5/23.
 */
public class IResult<E> {

    public void onSuccess(E e) {

    }

    public void onFailure(ResultError error, String errorMsg) {

    }

    /**
     * 错误类型
     */
    public enum ResultError {
        /**
         * http 错误
         */
        HTTP,
        /**
         * 超时
         */
        TIMEOUT,
        /**
         * 业务逻辑
         */
        LOGIC
    }
}