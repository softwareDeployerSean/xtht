package com.walnutin.xtht.bracelet.mvp.model.api.service;

import com.walnutin.xtht.bracelet.mvp.model.entity.User;
import com.walnutin.xtht.bracelet.mvp.model.entity.UserBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 存放关于用户的一些api
 * Created by jess on 8/5/16 12:05
 * contact with jess.yan.effort@gmail.com
 */
public interface UserService {
    String BASE_URL = "http://gxd.walnutin.com:9000/";


    String HEADER_API_VERSION = "Accept: application/vnd.github.v3+json";

    @Headers({HEADER_API_VERSION})
    @GET("/users")
    Observable<List<User>> getUsers(@Query("since") int lastIdQueried, @Query("per_page") int perPage);

    //注册
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("user/register")
    Observable<UserBean> regist(@Body RequestBody route);
    @POST("user/login")
    Observable<UserBean> login(@Body RequestBody route);


}
