package com.example.youngkaaa.multirecycleradapter;

/**
 * Created by : youngkaaa on 2016/10/10.
 * Contact me : 645326280@qq.com
 */

public class ContentBean {
    private String picUrl;
    private String content;

    public ContentBean(String picId, String content){
        this.setPicUrl(picId);
        this.setContent(content);
    }


    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
