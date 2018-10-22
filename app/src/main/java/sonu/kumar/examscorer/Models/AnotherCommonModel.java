package sonu.kumar.examscorer.Models;

/**
 * Created by sonu on 23/9/18.
 */

public class AnotherCommonModel {
    String notes_sub_id,notes_catid,notes_subcat_image,notes_sub_cat_title;
    String notes_download_link,notes_download_title;
    String notes_download_link1,download_imges_title;


    public AnotherCommonModel(String notes_download_link1,String download_imges_title,int i) {
        this.notes_download_link1 = notes_download_link1;
        this.download_imges_title =download_imges_title;

    }

    public String getDownload_imges_title() {
        return download_imges_title;
    }

    public void setDownload_imges_title(String download_imges_title) {
        this.download_imges_title = download_imges_title;
    }

    public String getNotes_download_link1() {
        return notes_download_link1;
    }

    public void setNotes_download_link1(String notes_download_link1) {
        this.notes_download_link1 = notes_download_link1;
    }

    public AnotherCommonModel(String notes_download_link, String notes_download_title) {
        this.notes_download_link = notes_download_link;
        this.notes_download_title = notes_download_title;
    }

    public String getNotes_download_link() {
        return notes_download_link;
    }

    public void setNotes_download_link(String notes_download_link) {
        this.notes_download_link = notes_download_link;
    }

    public String getNotes_download_title() {
        return notes_download_title;
    }

    public void setNotes_download_title(String notes_download_title) {
        this.notes_download_title = notes_download_title;
    }

    public AnotherCommonModel() {
    }

    public AnotherCommonModel(String notes_sub_id, String notes_catid, String notes_subcat_image, String notes_sub_cat_title) {
        this.notes_sub_id = notes_sub_id;
        this.notes_catid = notes_catid;
        this.notes_subcat_image = notes_subcat_image;
        this.notes_sub_cat_title = notes_sub_cat_title;
    }

    public String getNotes_sub_id() {
        return notes_sub_id;
    }

    public void setNotes_sub_id(String notes_sub_id) {
        this.notes_sub_id = notes_sub_id;
    }

    public String getNotes_catid() {
        return notes_catid;
    }

    public void setNotes_catid(String notes_catid) {
        this.notes_catid = notes_catid;
    }

    public String getNotes_subcat_image() {
        return notes_subcat_image;
    }

    public void setNotes_subcat_image(String notes_subcat_image) {
        this.notes_subcat_image = notes_subcat_image;
    }

    public String getNotes_sub_cat_title() {
        return notes_sub_cat_title;
    }

    public void setNotes_sub_cat_title(String notes_sub_cat_title) {
        this.notes_sub_cat_title = notes_sub_cat_title;
    }
}
