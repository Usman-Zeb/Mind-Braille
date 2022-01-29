package com.example.mindbraille.models;

import java.io.Serializable;
import java.util.ArrayList;

public class MailModel implements Serializable {

    private String senderName;
    private String senderMail;
    private String subject;
    private String recTime;
    private String bodyPreview;
    private String body;
    private ArrayList<String> toRecipients;
    private ArrayList<String> ccRecipients;
    private String id;

    public MailModel(String senderName, String senderMail,String subject, String recTime, String bodyPreview,String body) {
        this.senderName = senderName;
        this.senderMail = senderMail;
        this.recTime = recTime;
        this.bodyPreview = bodyPreview;
        this.subject = subject;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getToRecipients() {
        return toRecipients;
    }

    public void setToRecipients(ArrayList<String> toRecipients) {
        this.toRecipients = toRecipients;
    }

    public ArrayList<String> getCcRecipients() {
        return ccRecipients;
    }

    public void setCcRecipients(ArrayList<String> ccRecipients) {
        this.ccRecipients = ccRecipients;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderMail() {
        return senderMail;
    }

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }

    public String getRecTime() {
        return recTime;
    }

    public void setRecTime(String recTime) {
        this.recTime = recTime;
    }

    public String getBodyPreview() {
        return bodyPreview;
    }

    public void setBodyPreview(String bodyPreview) {
        this.bodyPreview = bodyPreview;
    }
}
