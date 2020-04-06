package com.gmail.ecosight;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class Camera extends AppCompatActivity {
    Button TakePicBtn, AnalyzeBtn;
    ImageView imageview;
    String pathToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        TakePicBtn = findViewById(R.id.TakePicBtn);
        if (Build.VERSION.SDK_INT>=23){
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        TakePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePictureAction();
            }
        });
        imageview = findViewById(R.id.image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == 1){
                AnalyzeBtn = findViewById(R.id.analyzeBtn);
                AnalyzeBtn.setVisibility(View.VISIBLE);

                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile );
                imageview.setImageBitmap(bitmap);

                AnalyzeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent resultIntent = new Intent(getApplicationContext(), Result.class);

                        send sendimg = new send();
                        sendimg.execute();

                        startActivity(resultIntent);
                    }
                });
            }
        }
    }

    private void takePictureAction() {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager())!=null){
            File photoFile = null;
            photoFile = createPhotoFile();
            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(Camera.this, "com.gmail.ecosight.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, 1);
            }

        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            Log.d("myLog", "Excep: "+e.toString());
        }
        return image;
    }

    //class to communicate with server via a socket
    class send extends AsyncTask<Void,Void,Void> {
        //socket variable
        Socket s;

        @Override
        protected Void doInBackground(Void...params){
            try { //TODO : change to get IP address of current machine
                s = new Socket("10.10.188.13",8000); //connects to my IP address
                InputStream input = new FileInputStream(pathToFile);

                try {
                    try {
                        //Reads bytes all together
                        int bytesRead;
                        while ((bytesRead = input.read()) != -1) {
                            s.getOutputStream().write(bytesRead); //Writes bytes to output stream
                        }
                    } finally {
                        //Flushes and closes socket
                        s.getOutputStream().flush();
                        s.close();
                    }
                } finally {
                    input.close();
                }
            } catch (UnknownHostException e) {
                System.out.println("Fail");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Fail");
                e.printStackTrace();
            }
            return null;
        }
    }
}
