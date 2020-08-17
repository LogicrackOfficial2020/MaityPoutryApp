package com.logicrack.MaityPoultry.model;

public class SelectLanguageList {

    private String video_language_id;
    private String video_language;
    private String image;

    public SelectLanguageList(String video_language_id, String video_language, String image ) {
        this.video_language_id = video_language_id;
        this.video_language = video_language;
        this.image = image;
    }

    public String getvideo_language_id() {
        return video_language_id;
    }

    public void setvideo_language_id(String video_language_id) {
        this.video_language_id = video_language_id;
    }

    public String getvideo_language() {
        return video_language;
    }

    public void setvideo_language(String video_language) {
        this.video_language = video_language;
    }



    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }
}
