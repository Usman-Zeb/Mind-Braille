package com.example.mindbraille;

import java.io.Serializable;

public class NewsModel implements Serializable {

    private String sourcename;
    private String title;
    private String description;
    private String URL;
    private String urltoImage;
    private String publishDate;

    public NewsModel(String sourcename, String title, String description, String URL, String urltoImage, String publishDate) {
        this.sourcename = sourcename;
        this.title = title;
        this.description = description;
        this.URL = URL;
        this.urltoImage = urltoImage;
        this.publishDate = publishDate;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getUrltoImage() {
        return urltoImage;
    }

    public void setUrltoImage(String urltoImage) {
        this.urltoImage = urltoImage;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
