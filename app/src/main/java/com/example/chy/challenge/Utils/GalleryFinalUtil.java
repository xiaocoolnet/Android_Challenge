package com.example.chy.challenge.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.chy.challenge.R;

import java.io.File;
import java.util.ArrayList;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;


/**
 * Created by Administrator on 2016/8/25.
 */
public class GalleryFinalUtil {

    private int picMax;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private static FunctionConfig functionConfig;

    public GalleryFinalUtil(int picMax) {
        this.picMax = picMax;
    }

    public void init(Context context,ArrayList<PhotoInfo> mPhotoList){
        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader;
        PauseOnScrollListener pauseOnScrollListener = null;
        imageLoader = new PicassoImageLoader();
        pauseOnScrollListener = new PicassoPauseOnScrollListener(false, true);
        functionConfigBuilder.setMutiSelectMaxSize(picMax);
        functionConfigBuilder.setEnableEdit(false);
        functionConfigBuilder.setRotateReplaceSource(true);
        functionConfigBuilder.setEnableCamera(true);
        functionConfigBuilder.setEnablePreview(true);
        functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
        functionConfig = functionConfigBuilder.build();

        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#64D991"))
                .setTitleBarTextColor(Color.WHITE)
                .setTitleBarIconColor(Color.WHITE)
                .setFabNornalColor(Color.parseColor("#64D991"))
                .setFabPressedColor(Color.GREEN)
                .setCheckNornalColor(Color.WHITE)
                .setCheckSelectedColor(Color.parseColor("#64D991"))
                .setIconBack(R.mipmap.ic_back_white)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(pauseOnScrollListener)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);

    }

    public  void openAblum(Context context,ArrayList<PhotoInfo> mPhotoList,int key,GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback){

        init(context,mPhotoList);
        GalleryFinal.openGalleryMuti(key, functionConfig, mOnHanlderResultCallback);


    }

    public  boolean openCamera(Context context,ArrayList<PhotoInfo> mPhotoList,int key,GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback){
        init(context, mPhotoList);


        //判断权限
        if (PermissionUtil.hasCamera(context)){
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 判断存储卡是否可以用，可用进行存储
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File file = new File(path, "tiaozhangaoxin.png");
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            GalleryFinal.openCamera(key, functionConfig, mOnHanlderResultCallback);

        }else {
            String[] perms = {"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.MOUNT_UNMOUNT_FILESYSTEMS"};

            int permsRequestCode = 200;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return false;
            }
        }

        return true;
    }




}
