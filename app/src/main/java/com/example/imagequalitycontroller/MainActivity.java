package com.example.imagequalitycontroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_REQ_CODE = 51;
    private static final int INTENT_REQ_CODE = 101;
    Uri uri;
    Button btnChoose, btnReduce;
    TextView txtPath, txtSizeDiff,txtChoosedQuality;
    ImageView imgChoosenImage;
    SeekBar seekBar;
    String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnChoose.setOnClickListener(this);
        btnReduce.setOnClickListener(this);
        txtChoosedQuality.setText("0");

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtChoosedQuality.setText(progress+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    void init() {
        btnChoose = findViewById(R.id.btnChooseImage);
        btnReduce = findViewById(R.id.btnReduce);
        txtPath = findViewById(R.id.TV_outPath);
        imgChoosenImage = findViewById(R.id.imgViewImage);
        txtSizeDiff = findViewById(R.id.TV_DiffInSize);
        seekBar=findViewById(R.id.qualitySeekBar);
        txtChoosedQuality=findViewById(R.id.txtChoosedQuality);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChooseImage:
                chooseImage();
                break;
            case R.id.btnReduce:
                reduceImageSize();
                break;


        }
    }

    private void openOutPutDirectory() {
        if (filePath.length()>2)
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri mydir = Uri.parse(filePath);
            intent.setDataAndType(mydir,"application/*");    // or use */*
            startActivity(intent);
        }
    }

    private void reduceImageSize() {
        if (uri == null) {
            Toast.makeText(this, "please Choose Image First", Toast.LENGTH_SHORT).show();
            return;
        }
        if (uri.toString().length() > 1) {
            Controller.setContext(getApplicationContext());
            String outImagePath = Controller.reduceBitmab(uri,seekBar.getProgress());
            filePath=outImagePath;
            txtPath.setText("Image Save At: " + outImagePath);


            try {
                long oldSizeInKB = (getContentResolver().openAssetFileDescriptor(uri, "r").getLength() / 1024);
                long newSizeInKB = (getContentResolver().openAssetFileDescriptor(Uri.fromFile(new File(outImagePath)), "r").getLength() / 1024);
                txtSizeDiff.setText("old size : " + oldSizeInKB + " KB" + "\nnew size : " + newSizeInKB + " KB");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    private void chooseImage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQ_CODE);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, INTENT_REQ_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "accept Permission to open Gallery", Toast.LENGTH_SHORT).show();
            } else {
                chooseImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri imagePath = data.getData();
                    imgChoosenImage.setImageURI(imagePath);
                    uri = imagePath;
                }

            }

        }
    }
}