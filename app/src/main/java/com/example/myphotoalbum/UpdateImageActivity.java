package com.example.myphotoalbum;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateImageActivity extends AppCompatActivity {

    ImageView img,image1;
    EditText title,desc;
    Button btnSave;
    private String title1,desc1;
    private byte[] image;
    private int id;

    ActivityResultLauncher<Intent> activityResultLauncherForUpdateImage;
    Bitmap selectedImage,ScaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("UPDATE IMAGE");
        setContentView(R.layout.activity_update_image);

        registerActivityLauncher();

        img=findViewById(R.id.imageViewUpdate);
        title=findViewById(R.id.editTextTitle1);
        desc=findViewById(R.id.editTextDesc1);
        btnSave=findViewById(R.id.button1);

        id=getIntent().getIntExtra("id",-1);
        title1=getIntent().getStringExtra("title");
        desc1=getIntent().getStringExtra("desc");
        image = getIntent().getByteArrayExtra("image");

        title.setText(title1);
        desc.setText(desc1);
        img.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateData();

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncherForUpdateImage.launch(intent);

            }
        });
    }

    public void updateData(){

        if(id==-1){
            Toast.makeText(UpdateImageActivity.this, "There is a Problem!", Toast.LENGTH_SHORT).show();
        }else{

            String title2 = title.getText().toString();
            String desc2 = desc.getText().toString();

            Intent intent = new Intent();
            intent.putExtra("title",title2);
            intent.putExtra("desc",desc2);
            intent.putExtra("id",id);
            if(selectedImage==null){
                intent.putExtra("image",image);
            }else{
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ScaledImage=makeSmall(selectedImage,200);
                ScaledImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
                byte [] image1 = byteArrayOutputStream.toByteArray();
                intent.putExtra("image",image1);
            }

            setResult(RESULT_OK,intent);
            finish();
        }



    }

    public void registerActivityLauncher(){

        activityResultLauncherForUpdateImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
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