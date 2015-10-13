package com.example.nore.turndown.backEnd.services;

import com.example.nore.turndown.backEnd.models.Reporte2;
import com.example.nore.turndown.backEnd.models.Usuario2;
import com.example.nore.turndown.entity.dao.Usuario;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by NORE on 11/07/2015.
 */
public interface ReportService {
    public static final String BASE_URL = "http://mimservice-contactres.rhcloud.com/ReportRest/webresources/generic";

    @POST("/")
    void verifyUser(@Body Usuario2 user, Callback<Usuario2> cb);

    @POST("/test/{id}")
    void uploadReport(@Path("id") int id, @Body Usuario2 user, Callback<Usuario2> cb);

    @POST("/report/{user}")
    void uploadReportService(@Path("user") String usuario, @Body Reporte2 reporte, Callback<Reporte2> cb);

    @Multipart
    @POST("/prime")
    public void uploadImage(@Part("id") TypedString description, @Part("file") TypedFile imagen, Callback<String> cb);

    @Multipart
    @POST("/prime")
    public Response uploadImage2(@Part("id") TypedString description, @Part("file") TypedFile imagen);

    @POST("/markRep/{id}")
    public Response markRep(@Path("id") Integer id,@Body String body);
}
