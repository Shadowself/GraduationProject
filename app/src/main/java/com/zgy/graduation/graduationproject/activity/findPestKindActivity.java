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
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.zgy.graduation.graduationproject.R;
import com.zgy.graduation.graduationproject.bean.ResData;
import com.zgy.graduation.graduationproject.http.HttpAsyncTaskManager;
import com.zgy.graduation.graduationproject.http.StringTaskHandler;
import com.zgy.graduation.graduationproject.util.DeviceUtil;
import com.zgy.graduation.graduationproject.util.ReqCmd;
import com.zgy.graduation.graduationproject.util.StringUtils;
import com.zgy.graduation.graduationproject.util.SweetAlertDialogUtils;
import com.zgy.graduation.graduationproject.util.ViewUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by zhangguoyu on 2015/4/7.
 */
public class findPestKindActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private ImageView pestPicture;
    private Button choose_way;
    private Button postPicture;
    private EditText pestText;

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/tempPicture.jpg";// temp
    // file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store the big
    // bitmap
    public static String picturePath = String.format("%sphoto%s%s.jpg", DeviceUtil.getSDcardDir() + DeviceUtil.DEFAULTBASEPATH,
            File.separator,
            "temp");


    private String userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_findpestkind);
        comm_title.setText(getString(R.string.find_pest_kind));
        back_main.setVisibility(View.VISIBLE);

        pestPicture = (ImageView) findViewById(R.id.pestPicture);
        choose_way = (Button) findViewById(R.id.choose_way);
        choose_way.setOnClickListener(this);

        postPicture = (Button) findViewById(R.id.postPicture);
        postPicture.setOnClickListener(this);

        pestText = (EditText)findViewById(R.id.pestText);

        userPhoto = String.format("%sphoto%s%s.jpg", DeviceUtil.getSDcardDir() + DeviceUtil.DEFAULTBASEPATH,
                File.separator,
                "temp");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_way:
                createDialog();
                break;
            case R.id.postPicture:
                String describe = pestText.getText().toString().trim();
                File file = new File(userPhoto);
                if(StringUtils.isNotBlank(describe) && file.exists()){
                    postPictureToServer(describe);
                }else{
                    ViewUtil.showToast(mContext,getString(R.string.pestInfo_empty));
                }
                break;
        }
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
                        keepPictureToSDCard(bp);
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
                        keepPictureToSDCard(bmp);
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

    public void keepPictureToSDCard(Bitmap photo) {
        FileOutputStream b = null;

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

    public void postPictureToServer(String describe){
        String url = getString(R.string.postPest_url);

        List<String> pestInfo = new ArrayList<String>();
        pestInfo.add(preferencesUtil.getString(ReqCmd.STOREHOUSEID));
        pestInfo.add(describe);
        pestInfo.add(userPhoto);
        SweetAlertDialogUtils.showProgressDialog(this, getString(R.string.logining), false);
        HttpAsyncTaskManager httpAsyncTaskManager = new HttpAsyncTaskManager(mContext);
        httpAsyncTaskManager.requestMapStream(url, pestInfo, new StringTaskHandler() {
            @Override
            public void onFinish() {
                SweetAlertDialogUtils.closeProgressDialog();
            }

            @Override
            public void onNetError() {
                ViewUtil.showToast(mContext, getString(R.string.network_error));
            }

            @Override
            public void onSuccess(String result) {
                try {
                    ResData resData = JSONObject.parseObject(result, ResData.class);
                    switch (resData.getCode_()) {
                        // resData.getcode_()=0;
                        case ReqCmd.RESULTCODE_SUCCESS:
                            ViewUtil.showToast(mContext, resData.getMessage_());
                            File file = new File(userPhoto);
                            if(file.exists()){
                                file.delete();
                            }

                            new SweetAlertDialog(mContext)
                                    .setTitleText(getString(R.string.pestInfo))
                                    .setContentText(resData.getData())
                                    .show();

//                            finish();

                            break;
                        default:
                            ViewUtil.showToast(mContext, resData.getMessage_());
                            break;
                    }
                } catch (Exception e) {
                    Log.e(TAG,e.toString());
                }

            }

            @Override
            public void onFail() {
                ViewUtil.showToast(mContext, getString(R.string.server_error));
            }
        });

    }

}
