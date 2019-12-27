package com.example.lib_image_loader.app;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.lib_image_loader.R;
import com.example.lib_image_loader.image.CustomRequestListener;
import com.example.lib_image_loader.image.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 图片加载类，与外界的唯一通信类，
 * 支持各种view，notification，appwidget，viewgroup加载图
 */
public class ImageLoaderManager {

    private ImageLoaderManager() {

    }

    public static class SingletonHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    public static ImageLoaderManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 为ImageView加载图片
     */
    public void displayImageForView(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(imageView);
    }

    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.b4y)
                .error(R.mipmap.b4y)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .priority(Priority.NORMAL);
        return options;
    }

    /**
     * 为imageview加载圆形图片
     *
     * @param imageView
     * @param url
     */
    public void displayImageForCircle(final ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new BitmapImageViewTarget(imageView) {
                    //将imageview包装成target
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(imageView.getResources(), resource);
                        drawable.setCircular(true);
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    /**
     * 完成为viewgroup设置背景并模糊处理
     *
     * @param group
     * @param url
     */
    public void displayImageForViewGroup(final ViewGroup group, String url) {
        Glide.with(group.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        final Bitmap bitmap = resource;
                        Observable.just(resource).map(new Function<Bitmap, Object>() {
                            @Override
                            public Drawable apply(Bitmap bitmap) throws Exception {
                                //将Bitmap进行模糊处理并转为drawable
                                Drawable drawable = new BitmapDrawable(Utils.doBlur(resource, 100, true));
                                return drawable;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object o) throws Exception {
                                        group.setBackground((Drawable) o);
                                    }
                                })
                        /**
                         * date:2019.12.27
                         * desc:视频写法--报错暂时无法解决
                         */
//                        .subscribe(new Consumer<Drawable>() {
//                            @Override
//                            public void accept(Drawable drawable) throws Exception {
//                                group.setBackground(drawable);
//                            }
//                        })
                        ;
                    }
                });
    }

    /**
     * 为非view加载图片
     */
    private void displayImageForTarget(Context context, Target target, String url) {
        this.displayImageForTarget(context, target, url, null);
    }

    /**
     * 为非view加载图片
     *
     * @param context
     * @param target
     * @param url
     * @param requestListener
     */
    private void displayImageForTarget(Context context, Target target, String url, CustomRequestListener requestListener) {
        Glide.with(context).asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .fitCenter()
                .into(target);
    }

    /**
     * 为notification中的id控件加载图片
     *
     * @param context
     * @param rv
     * @param id              要加载的控件id
     * @param notification
     * @param NOTIFICATION_ID
     * @param url
     */
    public void displayImageForNotification(Context context, RemoteViews rv, int id, Notification notification, int NOTIFICATION_ID, String url) {
        this.displayImageForTarget(context, initNotificationTarget(context, id, rv, notification, NOTIFICATION_ID), url);
    }

    //构造一个NotificationTarget
    private NotificationTarget initNotificationTarget(Context context, int id, RemoteViews rv, Notification notification, int NOTIFICATION_ID) {

        NotificationTarget target = new NotificationTarget(context, id, rv, notification, NOTIFICATION_ID);
        return target;
    }

}
