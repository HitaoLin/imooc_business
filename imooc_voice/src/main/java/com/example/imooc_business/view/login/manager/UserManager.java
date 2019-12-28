package com.example.imooc_business.view.login.manager;

import com.example.imooc_business.model.user.User;

/**
 * 单例管理登录用户信息
 */
public class UserManager {

    private static UserManager mInstance;
    private User mUser;

    public static UserManager getInstance() {
        //双检查机制
        if (mInstance == null) {
            //加锁，保证线程安全--加类的字节码锁，每个类对应的字节码是唯一的
            synchronized (UserManager.class) {
                if (mInstance == null) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存用户信息到内存
     */
    public void saveUser(User user){
        mUser = user;
        saveLocal(user);
    }

    /**
     * 持久化用户信息
     * @param user
     */
    private void saveLocal(User user){

    }

    /**
     * 获取用户信息
     * @return
     */
    public User getUser(){
        return mUser;
    }

    /**
     * 从本地获取
     * @return
     */
    private User getLocal(){
        return null;
    }

    /**
     * 判断是否登录过
     * @return
     */
    public boolean hasLogin(){
        return getUser() == null ? false : true;
    }

    public  void  removeUser(){
        mUser = null;
    }

    /**
     * 从库中删除用户信息
     */
    private void removeLocal(){

    }
}
