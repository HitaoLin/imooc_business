package com.example.lib_audio.mediaplayer.core;

import com.example.lib_audio.mediaplayer.exception.AudioQueueEmptyException;
import com.example.lib_audio.mediaplayer.model.AudioBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;

/**
 * 控制播放逻辑类
 */
public class AudioController {
    /**
     * 播放方式
     */
    public enum PlayMode {
        /**
         * 列表循环
         */
        LOOP,
        /**
         * 随机
         */
        RANDOM,
        /**
         * 单曲循环
         */
        REPEAT
    }

    /**
     * 单例方法
     *
     * @return
     */
    public static AudioController getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static AudioController instance = new AudioController();
    }

    private AudioPlayer mAudioPlayer;//核心播放器
    private ArrayList<AudioBean> mQueue;//歌曲队列
    private int mQueueIndex;//当前播放歌曲索引
    private PlayMode mPlayMode;//循环模式

    private AudioController() {
        mAudioPlayer = new AudioPlayer();
        mQueue = new ArrayList<>();
        mQueueIndex = 0;
        mPlayMode = PlayMode.LOOP;
    }

    public ArrayList<AudioBean> getQueue() {
        return mQueue == null ? new ArrayList<AudioBean>() : mQueue;
    }

    /**
     * 设置播放队列
     *
     * @param queue
     */
    public void setQueue(ArrayList<AudioBean> queue) {
        mQueue = queue;
    }

    public void setQueue(ArrayList<AudioBean> queue, int queueIndex) {
        mQueue.addAll(queue);
        mQueueIndex = queueIndex;
    }

    /**
     * 添加单一歌曲到指定位置
     * @param bean
     */
    public void addAudio(AudioBean bean){
        this.addAudio(0,bean);
    }

    public void addAudio(int index,AudioBean bean){
        if (mQueue == null){
            throw new AudioQueueEmptyException("当前播放队列为空");
        }
        int query = queryAudio(bean);
        if (query <= -1){
            //没有添加过
            addCustomAudio(index,bean);
            setPlayIndex(index);
        }else {
            AudioBean currentBean = getNowPlaying();
            if (!currentBean.id.equals(bean.id)){
                //已经添加过且在播放中
                setPlayIndex(query);
            }
        }
    }

    private void addCustomAudio(int index, AudioBean bean) {
    }

    private int queryAudio(AudioBean bean) {
        return 0;
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    /**
     * 对外提供设置播放方式
     *
     * @param playMode
     */
    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    public void setPlayIndex(int index) {
        if (mQueue == null) {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
        mQueueIndex = index;
        play();
    }

    /**
     * 对外提供的获取当前歌曲信息
     * @return
     */
    public AudioBean getNowPlaying() {
        return getPlaying(mQueueIndex);
    }

    private AudioBean getPlaying(int index) {
        if (mQueue != null && !mQueue.isEmpty() && mQueueIndex >= 0 && index < mQueue.size()) {
            return mQueue.get(index);
        } else {
            throw new AudioQueueEmptyException("当前播放队列为空,请先设置播放队列.");
        }
    }

    /**
     * 对外提供的play方法
     */
    public void play() {
        AudioBean bean = getNowPlaying();
        mAudioPlayer.load(bean);
    }

    public void pause() {
        mAudioPlayer.pause();
    }

    public void resume() {
        mAudioPlayer.resume();
    }

    public void release() {
        mAudioPlayer.release();
        EventBus.getDefault().unregister(this);
    }

    public int getPlayIndex() {
        return mQueueIndex;
    }

    /**
     * 播放下一首歌曲
     */
    public void next() {
        AudioBean bean = getNextPlaying();
        mAudioPlayer.load(bean);
    }

    private AudioBean getNextPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mQueueIndex = (mQueueIndex + 1) % mQueue.size();
                break;
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                break;
            case REPEAT:
                break;
        }
        return getPlaying(mQueueIndex);
    }

    /**
     * 播放上一首歌曲
     */
    public void previous() {
        AudioBean bean = getPreviousPlaying();
        mAudioPlayer.load(bean);
    }

    private AudioBean getPreviousPlaying() {
        switch (mPlayMode) {
            case LOOP:
                mQueueIndex = (mQueueIndex + 1) % mQueue.size();
                break;
            case RANDOM:
                mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
                break;
            case REPEAT:
                break;
        }
        return getPlaying(mQueueIndex);
    }

    /**
     * 自动切换播放/暂停
     */
    public void playOrPause() {
        if (isStartState()) {
            pause();
        } else if (isPauseState()) {
            resume();
        }
    }

    /**
     * 对外提供是否播放中状态
     */
    public boolean isStartState() {
        return CustomMediaPlayer.Status.STARTED == getStatus();
    }

    /**
     * 对外提提供是否暂停状态
     */
    public boolean isPauseState() {
        return CustomMediaPlayer.Status.PAUSED == getStatus();
    }

    /*
     * 获取播放器当前状态
     */
    private CustomMediaPlayer.Status getStatus() {
        return mAudioPlayer.getStatus();
    }

}
