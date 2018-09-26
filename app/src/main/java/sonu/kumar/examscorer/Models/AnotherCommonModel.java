package sonu.kumar.examscorer.Models;

/**
 * Created by sonu on 23/9/18.
 */

public class AnotherCommonModel {
    String notes_sub_id,notes_catid,notes_subcat_image,notes_sub_cat_title;
    String notes_pdf_link,notes_pdf_title;

    public AnotherCommonModel(String notes_pdf_link, String notes_pdf_title) {
        this.notes_pdf_link = notes_pdf_link;
        this.notes_pdf_title = notes_pdf_title;
    }

    public String getNotes_pdf_link() {
        return notes_pdf_link;
    }

    public void setNotes_pdf_link(String notes_pdf_link) {
        this.notes_pdf_link = notes_pdf_link;
    }

    public String getNotes_pdf_title() {
        return notes_pdf_title;
    }

    public void setNotes_pdf_title(String notes_pdf_title) {
        this.notes_pdf_title = notes_pdf_title;
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
