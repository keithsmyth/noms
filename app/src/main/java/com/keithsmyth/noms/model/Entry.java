package com.keithsmyth.noms.model;

import java.util.Date;

/**
 * @author keithsmyth
 */
public class Entry {

    private String objectId;
    private Date date;
    private String category;
    private int amount;
    private String description;
    private boolean inRules;
    private String comment;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInRules() {
        return inRules;
    }

    public void setInRules(boolean inRules) {
        this.inRules = inRules;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
