package com.zgy.graduation.graduationproject.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.util.DeviceUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhangguoyu on 2015/4/7.
 */
public class findPestKindActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private ImageView pestPicture;
    private Button choose_way;

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/tempPicture.jpg";// temp
    // file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store the big
    // bitmap
    public static String userPhoto = String.format("%spicture%s",
            DeviceUtil.getSDcardDir()
                    + DeviceUtil.DEFAULTBASEPATH, File.separator);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_findpestkind);
        comm_title.setText(getString(R.string.find_pest_kind));
        back_main.setVisibility(View.VISIBLE);

        pestPicture = (ImageView) findViewById(R.id.pestPicture);
        choose_way = (Button) findViewById(R.id.choose_way);
        choose_way.setOnClickListener(this);

    }


    //保存到sd卡
    public static void savePhotoToSDCard(String path, String photoName,
        Bitmap photoBitmap) {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File photoFile = new File(path, photoName); // 在指定路径下创建文件
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                            fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //对返回的结果进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 0:

                    if (data != null) {

                        Bitmap bp = data.getParcelableExtra("data");
                        if (null == bp) {
                            ContentResolver resolver = getContentResolver();
                            //照片的原始资源地址
                            Uri originalUri = data.getData();
                            bp = decodeUriAsBitmap(originalUri);
                        }
                        pestPicture.setImageBitmap(bp);
                        keepPicture(bp);
//                        savePhotoToSDCard(userPhoto, "image.jpg", bp);
                    } else {
                        Log.e(TAG, "CHOOSE_SMALL_PICTURE: data = " + data);
                    }
                    break;
                case 1:
                    if (imageUri != null) {
                        Bitmap bmp = decodeUriAsBitmap(imageUri);
                        pestPicture.setImageBitmap(bmp);
//                        savePhotoToSDCard(userPhoto, "image.jpg", bmp);
                        keepPicture(bmp);
                    } else {
                        Log.e(TAG, "CROP_SMALL_PICTURE: data = " + data);
                    }
                    break;


                default:
                    break;
            }
        }

    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public void createDialog() {
        new AlertDialog.Builder(mContext)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intentFromGallery = new Intent();
                                intentFromGallery.setType("image/*"); // 设置文件类型
                                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intentFromGallery, 0);

                                break;
                            case 1:
                                Intent intentFromCapture = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                // 判断存储卡是否可以用，可用进行存储

                                intentFromCapture.putExtra(
                                        MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(Environment
                                                .getExternalStorageDirectory(),
                                                IMAGE_FILE_NAME)));

                                startActivityForResult(intentFromCapture, 1);


                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_way:
                createDialog();
                break;
        }
    }

    public void keepPicture(Bitmap photo) {
        FileOutputStream b = null;
        String userPhoto = String.format("%sphoto%s%s.jpg", DeviceUtil.getSDcardDir() + DeviceUtil.DEFAULTBASEPATH,
                File.separator,
                "temp");
        File file = new File(userPhoto);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();// 创建文件夹
        }
        try {
            b = new FileOutputStream(new File(userPhoto));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}