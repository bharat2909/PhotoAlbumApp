package com.example.myphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddImageActivity extends AppCompatActivity {

    ImageView img;
    EditText title,desc;
    Button btnSave;
    ActivityResultLauncher<Intent>activityResultLauncherForSelectImage;
    Bitmap selectedImage,ScaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("ADD IMAGE");
        setContentView(R.layout.activity_add_image);

        registerActivityLauncher();

        img=findViewById(R.id.imageView2);
        title=findViewById(R.id.editTextTitle);
        desc=findViewById(R.id.editTextDesc);
        btnSave=findViewById(R.id.button);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedImage==null){
                    Toast.makeText(AddImageActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
                }else{
                    String title1 = title.getText().toString();
                    String desc1 = desc.getText().toString();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ScaledImage=makeSmall(selectedImage,200);
                    ScaledImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
                    byte [] image = byteArrayOutputStream.toByteArray();

                    Intent intent = new Intent();
                    intent.putExtra("title",title1);
                    intent.putExtra("desc",desc1);
                    intent.putExtra("image",image);
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(AddImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.
                            requestPermissions(AddImageActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }else{
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncherForSelectImage.launch(intent);
                }

            }
        });
    }

    public void registerActivityLauncher(){

        activityResultLauncherForSelectImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int ResultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(ResultCode==RESULT_OK && data!=null){
                            try {
                                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());

                                img.setImageBitmap(selectedImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1 && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncherForSelectImage.launch(intent);
        }

    }

    public Bitmap makeSmall(@NonNull Bitmap image, int maxSize)
    {
        int w=image.getWidth();
        int h=image.getHeight();

        float ratio = (float) w/(float) h;

        if(ratio>1){
            w=maxSize;
            h=(int) (w/ratio);
        }else{
            h=maxSize;
            w=(int) (h*ratio);
        }

        return Bitmap.createScaledBitmap(image,w,h,true);

    }
}