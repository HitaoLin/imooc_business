package com.example.imooc_business.model;

/**
 * 首页枚举
 */
public enum CHANNEL {

    MY("我的", 0x01),

    DISCORY("发现", 0x02),

    FRIEND("朋友", 0x03);

    //所有类型标识
    public static final int MINE_ID = 0x01;
    public static final int DISCORY_ID = 0x02;
    public static final int FRIEND_ID = 0x03;

    public final String key;
    public final int value;

    CHANNEL(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }
}