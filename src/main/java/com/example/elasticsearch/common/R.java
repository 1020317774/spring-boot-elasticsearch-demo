package com.example.elasticsearch.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一返回结果对象
 *
 * @author Yeeep
 */
@Data
@Accessors(chain = true)
public class R implements Serializable {
    private Boolean success;
    private Integer code;
    private String message;
    private Object data;

    /**
     * 成功类方法
     */
    public static R ok() {
        R r = new R();
        r.setSuccess(true)
                .setCode(200)
                .setMessage("接口调用成功");
        return r;
    }

    /**
     * 失败类方法
     */
    public static R error() {
        R r = new R();
        r.setSuccess(false)
                .setCode(500)
                .setMessage("服务器错误");
        return r;
    }


    /*************************链式编程*******************************/

    /**
     * 赋值success属性
     *
     * @param success 是否成功（true=成功|false=失败）
     * @return {@link R}
     */
    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }

    /**
     * 赋值message属性
     *
     * @param message 返回消息
     * @return {@link R}
     */
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 赋值status属性
     *
     * @param code Http状态码
     * @return {@link R}
     */
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 赋值data属性
     *
     * @param data 封装map格式对象
     * @return {@link R}
     */
    public R data(Object data) {
        this.setData(data);
        return this;
    }
}
