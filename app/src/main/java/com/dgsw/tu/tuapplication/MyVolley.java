package com.dgsw.tu.tuapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.navercorp.volleyextensions.volleyer.factory.DefaultRequestQueueFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.navercorp.volleyextensions.volleyer.Volleyer.volleyer;

public class MyVolley {

    public String SERVER_IP = "http://10.80.163.235:8080";
    private String userId = "test03";
    private String userName = "배준현";
    private String proFileImg = "/images/profile/yb7b7.jpg";

    JSONObject response;

    RequestQueue requestQueue;
    private ImageLoader imageLoader = null;


    public interface VolleyResponse {
        public void onResponse(Object obj);
    }

    VolleyResponse responseListener = null;

    private static MyVolley self = null;

    public static MyVolley getinstance(Context context) {
        if (self == null) {
            self = new MyVolley(context);
        }
        return self;
    }

    private MyVolley(Context context) {
        requestQueue = DefaultRequestQueueFactory.create(context);
        requestQueue.start();

        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {

                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

                cache.put(url, bitmap);
            }
        };

        RequestQueue tempRequestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(tempRequestQueue, imageCache);

    }

    public void login(String id, String pw, String token, Response.Listener<String> response) {
        volleyer(requestQueue).post(SERVER_IP+"/login")
                .addStringPart("id", id)
                .addStringPart("pw", pw)
                .addStringPart("token", token)
                .withListener(response)
                .execute();
        Log.v("self", String.valueOf(response));
    }

    public void loginEx(String id, String pw, String token, Response.Listener<String> response, Response.ErrorListener responseError) {
        volleyer(requestQueue).post(SERVER_IP+"/login")
                .addStringPart("id", id)
                .addStringPart("pw", pw)
                .addStringPart("token", token)
                .withListener(response)
                .withErrorListener(responseError)
                .execute();
        Log.v("self", String.valueOf(response));
    }

    public void register(String userName, String id, String pw, File profileImg, String token, Response.Listener<String> response) {
        if (profileImg != null) {
            volleyer(requestQueue).post(SERVER_IP+"/register")
                    .addStringPart("userName", userName)
                    .addStringPart("id", id)
                    .addStringPart("pw", pw)
                    .addFilePart("profile", profileImg)
                    .addStringPart("token", token)
                    .withListener(response)
                    .withErrorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("Error", "Error");
                        }
                    })
                    .execute();
            Log.v("self", String.valueOf(response));
        } else {
            volleyer(requestQueue).post(SERVER_IP+"/register")
                    .addStringPart("userName", userName)
                    .addStringPart("id", id)
                    .addStringPart("pw", pw)
                    .withListener(response)
                    .withErrorListener(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("Error", "Error");
                        }
                    })
                    .execute();
        }
    }

    public void registerEx(String userName, String id, String pw, File profileImg, String token, Response.Listener<String> response, Response.ErrorListener errorResponse) {
        if (profileImg != null) {
            volleyer(requestQueue).post(SERVER_IP+"/register")
                    .addStringPart("userName", userName)
                    .addStringPart("id", id)
                    .addStringPart("pw", pw)
                    .addFilePart("profile", profileImg)
                    .addStringPart("token", token)
                    .withListener(response)
                    .withErrorListener(errorResponse)
                    .execute();
            Log.v("self", String.valueOf(response));
        } else {
            volleyer(requestQueue).post(SERVER_IP+"/register")
                    .addStringPart("userName", userName)
                    .addStringPart("id", id)
                    .addStringPart("pw", pw)
                    .withListener(response)
                    .withErrorListener(errorResponse)
                    .execute();
        }
    }


    public void write(File image, String content, float latitude, float lat, String addr, String userName, String userID, Response.Listener<String> response) {
        volleyer(requestQueue).post(SERVER_IP+"/write")
                .addFilePart("image", image)
                .addStringPart("content", content)
                .addStringPart("latitude", String.valueOf(latitude))
                .addStringPart("lat", String.valueOf(lat))
                .addStringPart("addr", addr)
                .addStringPart("userName", userName)
                .addStringPart("userID", userID)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error","Error");
                    }
                })
                .execute();
        Log.v("self", String.valueOf(response));
    }

    public void writeEx(File image, String content, float lat, float longi, String addr, String userName, String userID, Response.Listener<String> response, Response.ErrorListener errorListener) {
        volleyer(requestQueue).post(SERVER_IP+"/write")
                .addFilePart("image", image)
                .addStringPart("content", content)
                .addStringPart("latitude", String.valueOf(lat)) // 위도
                .addStringPart("lat", String.valueOf(longi)) // 경도
                .addStringPart("addr", addr)
                .addStringPart("userName", userName)
                .addStringPart("userID", userID)
                .withListener(response)
                .withErrorListener(errorListener)
                .execute();
        Log.v("self", String.valueOf(response));
    }

    public void reWrite(String content, String idx, Response.Listener<String> response){
        volleyer(requestQueue).post(SERVER_IP+"/reWrite")
                .addStringPart("content", content)
                .addStringPart("idx", idx)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error","Error");
                    }
                })
                .execute();
        Log.v("self", String.valueOf(response));
    }

    public void removePost(String idx, Response.Listener<String> response){
        volleyer(requestQueue).post(SERVER_IP+"/removePost")
                .addStringPart("idx", idx)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error","Error");
                    }
                })
                .execute();
        Log.v("self", String.valueOf(response));
    }

    public void getMyTimeline(Response.Listener<String> response) {
        volleyer(requestQueue).post(SERVER_IP+"/showMyTimeline")
                .addStringPart("username", userName)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
        Log.v("self", String.valueOf(response));
    }


    public void getMyTimelineEx(String id, Response.Listener<String> response, Response.ErrorListener errorListener){
        volleyer(requestQueue).post(SERVER_IP+"/showMyTimeline")
                .addStringPart("id", id)
                .withListener(response)
                .withErrorListener(errorListener)
                .execute();
    }

    public void getMyOldTimelineEx(String id, String idx, Response.Listener<String> response, Response.ErrorListener errorListener){
        volleyer(requestQueue).post(SERVER_IP+"/showOldMytimeline")
                .addStringPart("id", id)
                .addStringPart("idx", idx)
                .withListener(response)
                .withErrorListener(errorListener)
                .execute();
    }

    public void getPost(String idx, Response.Listener<String> response){
        volleyer(requestQueue).post(SERVER_IP+"/showPost")
                .addStringPart("idx", idx)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void getTimeline(Response.Listener<String> response){
        volleyer(requestQueue).post(SERVER_IP+"/showTimeline")
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void getTimelineEx(String id, Response.Listener<String> response, Response.ErrorListener errorListener){
        volleyer(requestQueue).post(SERVER_IP+"/showTimeline")
                .addStringPart("id", id)
                .withListener(response)
                .withErrorListener(errorListener)
                .execute();
    }

    public void getOldTimelineEx(String id, String idx, Response.Listener<String> response, Response.ErrorListener errorListener){
        volleyer(requestQueue).post(SERVER_IP+"/showOldTimeline")
                .addStringPart("id", id)
                .addStringPart("idx", idx)
                .withListener(response)
                .withErrorListener(errorListener)
                .execute();
    }

    public void getOtherTimeline(Response.Listener<String> response, String search, String userId){
        volleyer(requestQueue).post(SERVER_IP+"/showOtherTimeline")
                .addStringPart("search", search)
                .addStringPart("userID", userId)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void editProfile(Response.Listener<String> response, String userName, String pw, File profile){
        volleyer(requestQueue).post(SERVER_IP+"/editProfile")
                .addStringPart("username", userName)
                .addStringPart("pw", pw)
                .addFilePart("profile", profile)
                .addStringPart("userID", userId)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void addFriend(Response.Listener<String> response, String receiverId){
        volleyer(requestQueue).post(SERVER_IP+"/addFriend")
                .addStringPart("senderID", userId)
                .addStringPart("receiverID", receiverId)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void addFriendEx(String receiverId, Response.Listener<String> response, Response.ErrorListener errorListener){
        volleyer(requestQueue).post(SERVER_IP+"/addFriend")
                .addStringPart("senderID", userId)
                .addStringPart("receiverID", receiverId)
                .withListener(response)
                .withErrorListener(errorListener)
                .execute();
    }

    public void showFriends(Response.Listener<String> response){
        volleyer(requestQueue).post(SERVER_IP+"/showFriends")
                .addStringPart("userID", userId)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void deleteFriend(Response.Listener<String> response, String receiverId){
        volleyer(requestQueue).post(SERVER_IP+"/deleteFriend")
                .addStringPart("senderID", userId)
                .addStringPart("receiverID", receiverId)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void share(Response.Listener<String> response, String postId){
        volleyer(requestQueue).post(SERVER_IP+"/share")
                .addStringPart("postID", postId)
                .addStringPart("sharer", userId)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void shareEx( String postId, Response.Listener<String> response, Response.ErrorListener errorListener){
        volleyer(requestQueue).post(SERVER_IP+"/share")
                .addStringPart("postID", postId)
                .addStringPart("sharer", userId)
                .withListener(response)
                .withErrorListener(errorListener)
                .execute();
    }

    public void search(String keyword, Response.Listener<String> response){
        volleyer(requestQueue).post(SERVER_IP+"/search")
                .addStringPart("keyword", keyword)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("Error","Error");
                    }
                })
                .execute();
        Log.v("self", String.valueOf(response));
    }

    public void oldSearch(String keyword, String idx, Response.Listener<String> response, Response.ErrorListener errorListener){
        volleyer(requestQueue).post(SERVER_IP+"/oldSearch")
                .addStringPart("keyword", keyword)
                .addStringPart("idx", idx)
                .withListener(response)
                .withErrorListener(errorListener)
                .execute();
    }
	
    public void checkPassword(Response.Listener<String> response, String password){
        volleyer(requestQueue).post(SERVER_IP+"/checkPassword")
                .addStringPart("userID", userId)
                .addStringPart("password", password)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void friendRequestList(Response.Listener<String> response){
        volleyer(requestQueue).post(SERVER_IP+"/friendRequestList")
                .addStringPart("userID", userId)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
    }

    public void refuseFriend(String receiverId, Response.Listener<String> response){
        volleyer(requestQueue).post(SERVER_IP+"/refuseFriend")
                .addStringPart("senderID", userId)
                .addStringPart("receiverID", receiverId)
                .withListener(response)
                .withErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("tag", volleyError.getMessage());
                    }
                })
                .execute();
        Log.v("self", String.valueOf(response));
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }


    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProFileImg() {
        return proFileImg;
    }

    public void setProfileImg(String proFileImg) {
        this.proFileImg = proFileImg;
    }
}
