package com.example.map_ui.networkcom;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
// file Upload url FileUploadaed library
public interface FileUploadService {
    @Multipart
    @POST("{fullUrl}")
    Call<ResponseBody> uploadFile(@Path(value = "fullUrl", encoded = true) String fullUrl,
            @Part MultipartBody.Part file);
}
