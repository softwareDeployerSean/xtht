package com.walnutin.xtht.bracelet.mvp.model.entity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 如果你服务器返回的数据固定为这种方式(字段名可根据服务器更改)
 * 替换范型即可重用BaseJson
 * Created by jess on 26/09/2016 15:19
 * Contact with jess.yan.effort@gmail.com
 */

public class BaseJson<T> implements Serializable {

    /**
     * /**
     * code : 1
     * msg : 密码修改成功
     */

    public String code;
    public T msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getMsg() {
        return msg;
    }

    public void setMsg(T msg) {
        this.msg = msg;
    }




    /**
     * @return
     * @author ygh
     * <p/>
     * 2015/4/27
     * <p/>
     * 解析参数，主要的解析步骤还是需要在具体的requestSuccess方法里做解析。
     */
    public static BaseJson analyseReponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) return null;
        BaseJson responseMessage = new BaseJson();
        try {
            if (jsonObject.has("code")) {
                responseMessage.code = jsonObject.getString("code");
            }
            if (jsonObject.has("msg")) {
                responseMessage.msg = jsonObject.get("msg");
            }
            jsonObject = null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseMessage;
    }


}
