package com.example.myphotoalbum;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private  MyImagesViewModel viewModel;
    RecyclerView rv;
    FloatingActionButton fab;
    ActivityResultLauncher<Intent>activityResultLauncherForAddImage;
    ActivityResultLauncher<Intent>activityResultLauncherForUpdateImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv= findViewById(R.id.rv);
        fab=findViewById(R.id.fab);

        registerActivityLauncher();
        registerActivityLauncherUpdate();


        rv.setLayoutManager(new LinearLayoutManager(this));
        MyImagesAdapter adapter = new MyImagesAdapter();
        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(MyImagesViewModel.class);
        viewModel.getAllImages().observe(MainActivity.this, new Observer<List<MyImages>>() {
            @Override
            public void onChanged(List<MyImages> myImages) {
                adapter.setImageList(myImages);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,AddImageActivity.class);
                activityResultLauncherForAddImage.launch(intent);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int pos = viewHolder.getAdapterPosition();
                MyImages myimage = adapter.getPosition(viewHolder.getAdapterPosition());
                viewModel.delete(adapter.getPosition(viewHolder.getAdapterPosition()));

//                Snackbar snackbar = Snackbar
//                        .make(rv, "Item was removed from the list.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        adapter.restoreItem(myimage);
//                        //rv.scrollToPosition(viewHolder.getAdapterPosition());
//                    }
//                });
//
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();

            }
        }).attachToRecyclerView(rv);

        adapter.setListener(new MyImagesAdapter.onImageClickListener() {
            @Override
            public void onImageClick(MyImages myImages) {
                Intent intent = new Intent(MainActivity.this,UpdateImageActivity.class);
                intent.putExtra("id",myImages.getImageID());
                intent.putExtra("title",myImages.getImg_title());
                intent.putExtra("desc",myImages.getImg_desc());
                intent.putExtra("image",myImages.getImage());
                activityResultLauncherForUpdateImage.launch(intent);
            }
        });
    }

    public void registerActivityLauncher(){
        activityResultLauncherForAddImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int ResultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(ResultCode==RESULT_OK && data!=null){
                            String title = data.getStringExtra("title");
                            String desc= data.getStringExtra(("desc"));
                            byte [] image= data.getByteArrayExtra("image");

                            MyImages myImages = new MyImages(title,desc,image);
                            viewModel.insert(myImages);
                        }
                    }
                });
    }

    public void registerActivityLauncherUpdate(){
        activityResultLauncherForUpdateImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int ResultCode = result.getResultCode();
                        Intent data = result.getData();

                        if(ResultCode==RESULT_OK && data!=null){
                            String title = data.getStringExtra("title");
                            String desc = data.getStringExtra(("desc"));
                            byte [] image= data.getByteArrayExtra("image");
                            int id = data.getIntExtra("id",-1);

                            MyImages myImages = new MyImages(title,desc,image);
                            myImages.setImageID(id);
                            viewModel.update(myImages);
                        }

                    }
                });
    }


}