package com.walnutin.xtht.bracelet.mvp.model.api.service;

import com.walnutin.xtht.bracelet.mvp.model.entity.BaseJson;
import com.walnutin.xtht.bracelet.mvp.model.entity.User;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 存放关于用户的一些api
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public interface UserService {
    String BASE_URL = "http://gxd.walnutin.com:9000/";

    @GET("/users")
    Observable<List<User>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);

    //注册
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("user/register")
    Observable<UserBean> regist(@Body RequestBody route);

    //登陆
    @POST("user/login")
    Observable<UserBean> login(@Body RequestBody route);

    //网邮箱发送验证码
    @GET("user/sendCode")
    Observable<String> sendcode(@Query("email") String email);

    //验证--验证码
    @POST("user/validCode")
    Observable<String> validCode(@Body RequestBody route);

    //修改密码
    @PUT("user/resetPassword")
    Observable<UserBean> check_password(@Body RequestBody route);

    //修改个人资料
    @PUT("user/updateUserInfo")
    Observable<String> change_data(@Body RequestBody route);

   //上传头像
    @Multipart
    @POST("user/updateAvatar")
    Observable<String> post_img(@Part("token") RequestBody token,@Part MultipartBody.Part file);

}
