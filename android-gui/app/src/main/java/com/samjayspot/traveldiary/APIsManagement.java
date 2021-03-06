package com.samjayspot.traveldiary;

/**
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * @samjayhk
 */

public class APIsManagement {

    public static String defaultServer = "http://52.91.190.253/api/";

    public static String getLogin() {
        return defaultServer + "users/login";
    }

    public static String getRegister() {
        return defaultServer + "users/register";
    }

    public static String getUpdatePassword() {
        return defaultServer + "/users/updatepassword";
    }

    public static String getTagsList() {
        return defaultServer + "/thread/tags";
    }

    public static String getThreadList(int tid, int pages) {
        return defaultServer + "thread/tag/" + tid + "/" + pages;
    }

    public static String getThread(int pid, int pages) {
        return defaultServer + "thread/" + pid + "/" + pages;
    }

    public static String getWriteThread() {
        return defaultServer + "thread/write";
    }

    public static String getWriteComment(int pid) {
        return defaultServer + "thread/" + pid + "/write";
    }

    public static String getUpload() {
        return defaultServer + "upload";
    }

    public static String getSearch(String keywords, int pages) {
        return defaultServer + "search/" + keywords + "/" + pages;
    }

    public static String getUpdateThread(int pid) {
        return defaultServer + "thread/" + pid + "/update";
    }

    public static String getUpdateComment(int cid) {
        return defaultServer + "thread/comment/" + cid + "/update";
    }

    public static String getDeleteThread(int pid) {
        return defaultServer + "thread/" + pid + "/delete";
    }

    public static String getDeleteComment(int cid) {
        return defaultServer + "thread/comment/" + cid + "/delete";
    }
}
