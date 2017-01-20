package com.widget.howard.bobwidget.util;

/**
 * Created by Daisy on 17/1/3.
 */

public class Constant {

    /**
     * 页面间的传参和接口的传参key
     */
    public static class ArgParam {
    }

    /**
     * 用于放SharedPreferences的key
     */
    public static class SpKey {
    }

    /**
     * 手机网络状态
     */
    public static enum NetworkType {
        OFFLINE(-1, "无网络"), WIFI(1, "WIFI网络"), MOBILE(2, "移动网络");
        private final String desc;
        private final int value;

        private NetworkType(int value, String desc) {
            this.desc = desc;
            this.value = value;
        }

        public static NetworkType valueOf(int valueOf) {    //    手写的从int到enum的转换函数
            switch (valueOf) {
                case -1:
                    return OFFLINE;
                case 1:
                    return WIFI;
                case 2:
                    return MOBILE;
                default:
                    return null;
            }
        }

        public static NetworkType descOf(String descOf) {    //    手写的从int到enum的转换函数
            switch (descOf) {
                case "无网络":
                    return OFFLINE;
                case "WIFI网络":
                    return WIFI;
                case "移动网络":
                    return MOBILE;
                default:
                    return null;
            }
        }

        public String getDesc() {
            return desc;
        }

        public int getValue() {
            return value;
        }
    }
}
