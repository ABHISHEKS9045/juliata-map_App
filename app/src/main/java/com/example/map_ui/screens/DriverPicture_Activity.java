package com.example.map_ui.screens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.braver.tool.picker.BraveFilePicker;
import com.braver.tool.picker.BraveFileType;
import com.example.map_ui.R;
import com.example.map_ui.networkcom.FileUploadService;
import com.example.map_ui.networkcom.FileUtils;
import com.example.map_ui.networkcom.Imgpicker;
import com.example.map_ui.networkcom.InputStreamRequestBody;
import com.github.dhaval2404.imagepicker.ImagePicker;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class DriverPicture_Activity extends AppCompatActivity {
    ImageView back_btnDP, ContinueDP;
    ImageView Image_loadGallery;
    ImageView IVPreviewImage;
    int SELECT_PICTURE = 200;
     String user ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_picture);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        user = getIntent().getStringExtra("USER");
        back_btnDP=findViewById(R.id.back_btnDP);
        ContinueDP=findViewById(R.id.ContinueDP);
        Image_loadGallery=findViewById(R.id.Image_loadGallery);
        IVPreviewImage=findViewById(R.id.IVPreviewImage);

        back_btnDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ContinueDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImagePath==null){
                    Toast.makeText(DriverPicture_Activity.this, "Seleccione la imagen", Toast.LENGTH_SHORT).show();

                }else {
                    dialog = ProgressDialog.show(DriverPicture_Activity.this, "",
                            "Cargando. Espere por favor...", true);
//                    startActivity(new Intent(DriverPicture_Activity.this,PasswordConfiguration_Activity.class).putExtra("USER",user));
                    RequestBody requestFile =
                            RequestBody.create(
                                    MediaType.parse("image/jpeg"),
                                    imageFile
                            );
                    //call retrofit Profile uploaded Api
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://139.59.21.147:8048/api/drivers/profile_upload/").build();
                    FileUploadService service = retrofit.create(FileUploadService.class);
                    service.uploadFile(getIntent().getStringExtra("u_id"),MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile)).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            dialog.dismiss();
                            try {
                                String responseStr =response.body().string();
                                System.out.println(responseStr);
                                JSONObject jsonObject = new JSONObject(responseStr);
                                String imagePath = jsonObject.getJSONArray("data").getJSONObject(0).getString("image");
                                startActivity(new Intent(DriverPicture_Activity.this,PasswordConfiguration_Activity.class).putExtra("USER",user).putExtra("img",imagePath));

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(DriverPicture_Activity.this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(DriverPicture_Activity.this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();

                        }
                    });
//                    new CallApiRegisterDrivers().execute();
                }
            }
        });

        // take image from gallery--->
        Image_loadGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
    }
// choose images
    void imageChooser() {
        ImagePicker.with(this)
                .crop().galleryOnly()		//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
       /* Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);*/
    }
    Uri selectedImagePath=null;
    File imageFile = null;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
                selectedImagePath = data.getData();

//                 selectedImagePath from internal gellery
                if (null != selectedImagePath) {
                    try{
                     imageFile = new Imgpicker().convertFile(selectedImagePath);
                    IVPreviewImage.setImageURI(selectedImagePath);}catch (Exception e){e.printStackTrace();}
                }
        }
    }

    ProgressDialog dialog;
    private class CallApiRegisterDrivers extends AsyncTask<Void, Void, String> {
        String url;
        ProgressDialog dialog;

        public CallApiRegisterDrivers(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(DriverPicture_Activity.this, "",
                    "Cargando. Espere por favor...", true);
        }

        @Override
        protected String doInBackground(Void... voids) {

            OkHttpClient client = new OkHttpClient();
            try {
                System.out.println("path    "+selectedImagePath.toString());
                System.out.println("path    "+imageFile.getPath());
//                InputStreamRequestBody contentPart = new InputStreamRequestBody(MediaType.parse("image/*"), getContentResolver(), selectedImagePath);
//                File f = FileUtils.getFile(DriverPicture_Activity.this,selectedImagePath);
//                File f = new File(selectedImagePath.toString().replace("content","file"));
                //selected picture from image path
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);
                RequestBody requestBody = new MultipartBody.Builder()
                        // TODO add other form elements here
                        .addPart(body)
                        .build();

                Request request = new Request.Builder().url(url).post(requestBody).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        //get Driver picture
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s == null) {
                Toast.makeText(DriverPicture_Activity.this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("response   " + s);
                try {
                    JSONObject item = new JSONObject(s);
                    startActivity(new Intent(DriverPicture_Activity.this,PasswordConfiguration_Activity.class).putExtra("USER",user));
                } catch (Exception e) {
                    Toast.makeText(DriverPicture_Activity.this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


}
