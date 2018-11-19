package sk.android.examscorer.Models;

/**
 * Created by sonu on 15/9/18.
 */

public class CommonModel {
    String branch_id,brancg_image,branch_heading;
    String sem_images,sem_title;
    int sem_id;
    String sub_heading,sub_name,sub_code,subject;
    String paper_title,paper_link,branch_name;
    int paper_id;
    String sp_title,sp_link,sp_code;
    int supply_papers,notes_sub_cat;
    String notes_cat_id,notes_cat_image,notes_cat_title,notes_count;
    String papers_year,papers_code;
    String branch_title;

    public CommonModel(String papers_year, String papers_code) {
        this.papers_year = papers_year;
        this.papers_code = papers_code;
    }

    public String getPapers_year() {
        return papers_year;
    }

    public void setPapers_year(String papers_year) {
        this.papers_year = papers_year;
    }

    public String getPapers_code() {
        return papers_code;
    }

    public void setPapers_code(String papers_code) {
        this.papers_code = papers_code;
    }

    public CommonModel(int notes_sub_cat, String notes_cat_id, String notes_cat_image, String notes_cat_title, String notes_count) {
        this.notes_sub_cat = notes_sub_cat;
        this.notes_cat_id = notes_cat_id;
        this.notes_cat_image = notes_cat_image;
        this.notes_cat_title = notes_cat_title;
        this.notes_count =notes_count;
    }

    public String getNotes_count() {
        return notes_count;
    }

    public void setNotes_count(String notes_count) {
        this.notes_count = notes_count;
    }

    public String getNotes_cat_id() {
        return notes_cat_id;
    }

    public void setNotes_cat_id(String notes_cat_id) {
        this.notes_cat_id = notes_cat_id;
    }

    public String getNotes_cat_image() {
        return notes_cat_image;
    }

    public void setNotes_cat_image(String notes_cat_image) {
        this.notes_cat_image = notes_cat_image;
    }

    public String getNotes_cat_title() {
        return notes_cat_title;
    }

    public void setNotes_cat_title(String notes_cat_title) {
        this.notes_cat_title = notes_cat_title;
    }

    public CommonModel(String sp_title, String sp_link, String sp_code, int supply_papers) {
        this.sp_title = sp_title;
        this.sp_link = sp_link;
        this.sp_code = sp_code;
        this.supply_papers = supply_papers;
    }

    public String getSp_title() {
        return sp_title;
    }

    public void setSp_title(String sp_title) {
        this.sp_title = sp_title;
    }

    public String getSp_link() {
        return sp_link;
    }

    public void setSp_link(String sp_link) {
        this.sp_link = sp_link;
    }

    public String getSp_code() {
        return sp_code;
    }

    public void setSp_code(String sp_code) {
        this.sp_code = sp_code;
    }

    public CommonModel(String paper_title, String paper_link, int paper_id,String branch_name) {
        this.paper_title = paper_title;
        this.paper_link = paper_link;
        this.paper_id =paper_id;
        this.branch_name =branch_name;
    }

    public CommonModel(String sub_heading, String sub_name, String sub_code, String subject) {
        this.sub_heading = sub_heading;
        this.sub_name = sub_name;
        this.sub_code = sub_code;
        this.subject = subject;

    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public int getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(int paper_id) {
        this.paper_id = paper_id;
    }

    public String getPaper_title() {
        return paper_title;
    }

    public void setPaper_title(String paper_title) {
        this.paper_title = paper_title;
    }

    public String getPaper_link() {
        return paper_link;
    }

    public void setPaper_link(String paper_link) {
        this.paper_link = paper_link;
    }

    public String getSub_heading() {
        return sub_heading;
    }

    public void setSub_heading(String sub_heading) {
        this.sub_heading = sub_heading;
    }

    public String getSub_name() {
        return sub_name;
    }

    public void setSub_name(String sub_name) {
        this.sub_name = sub_name;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public CommonModel(int sem_id, String sem_images, String sem_title) {
        this.sem_id = sem_id;
        this.sem_images = sem_images;
        this.sem_title =sem_title;
    }

    public int getSem_id() {
        return sem_id;
    }

    public void setSem_id(int sem_id) {
        this.sem_id = sem_id;
    }

    public String getSem_images() {
        return sem_images;
    }

    public void setSem_images(String sem_images) {
        this.sem_images = sem_images;
    }

    public String getBranch_title() {
        return branch_title;
    }

    public void setBranch_title(String branch_title) {
        this.branch_title = branch_title;
    }

    public CommonModel(String branch_id, String brancg_image, String branch_heading, String branch_title, int i) {
        this.branch_id = branch_id;
        this.brancg_image = brancg_image;
        this.branch_heading = branch_heading;
        this.branch_title =branch_title;

    }

    public String getSem_title() {
        return sem_title;
    }

    public void setSem_title(String sem_title) {
        this.sem_title = sem_title;
    }

    public CommonModel() {
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBrancg_image() {
        return brancg_image;
    }

    public void setBrancg_image(String brancg_image) {
        this.brancg_image = brancg_image;
    }

    public String getBranch_heading() {
        return branch_heading;
    }

    public void setBranch_heading(String branch_heading) {
        this.branch_heading = branch_heading;
    }
}
