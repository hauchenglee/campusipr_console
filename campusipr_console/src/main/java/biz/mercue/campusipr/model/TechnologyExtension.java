package biz.mercue.campusipr.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;

//@Entity
//@Table(name = "technology_extension")
public class TechnologyExtension extends BaseBean {
    @Id
    @JsonView(View.TechnologyDetail.class)
    private String extension_id;

    @ManyToOne
    @JoinColumn(name = "technology_id", referencedColumnName="technology_id")
    private Technology technology;

    @JsonView(View.TechnologyDetail.class)
    private String extension_school_no;

    @JsonView(View.TechnologyDetail.class)
    private String extension_appl_year;

    //補助單位
    @JsonView(View.TechnologyDetail.class)
    private String extension_subsidy_unit;

    //補助編號
    @JsonView(View.TechnologyDetail.class)
    private String extension_subsidy_num;

    //補助計畫名稱
    @JsonView(View.TechnologyDetail.class)
    private String extension_subsidy_plan;

    //事務所
    @JsonView(View.TechnologyDetail.class)
    private String extension_agent;

    //事務所編號
    @JsonView(View.TechnologyDetail.class)
    private String extension_agent_num;

    @JsonView(View.TechnologyDetail.class)
    private String extension_memo;

    public String getExtension_id() {
        return extension_id;
    }

    public void setExtension_id(String extension_id) {
        this.extension_id = extension_id;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }

    public String getExtension_school_no() {
        return extension_school_no;
    }

    public void setExtension_school_no(String extension_school_no) {
        this.extension_school_no = extension_school_no;
    }

    public String getExtension_appl_year() {
        return extension_appl_year;
    }

    public void setExtension_appl_year(String extension_appl_year) {
        this.extension_appl_year = extension_appl_year;
    }

    public String getExtension_subsidy_unit() {
        return extension_subsidy_unit;
    }

    public void setExtension_subsidy_unit(String extension_subsidy_unit) {
        this.extension_subsidy_unit = extension_subsidy_unit;
    }

    public String getExtension_subsidy_num() {
        return extension_subsidy_num;
    }

    public void setExtension_subsidy_num(String extension_subsidy_num) {
        this.extension_subsidy_num = extension_subsidy_num;
    }

    public String getExtension_subsidy_plan() {
        return extension_subsidy_plan;
    }

    public void setExtension_subsidy_plan(String extension_subsidy_plan) {
        this.extension_subsidy_plan = extension_subsidy_plan;
    }

    public String getExtension_agent() {
        return extension_agent;
    }

    public void setExtension_agent(String extension_agent) {
        this.extension_agent = extension_agent;
    }

    public String getExtension_agent_num() {
        return extension_agent_num;
    }

    public void setExtension_agent_num(String extension_agent_num) {
        this.extension_agent_num = extension_agent_num;
    }

    public String getExtension_memo() {
        return extension_memo;
    }

    public void setExtension_memo(String extension_memo) {
        this.extension_memo = extension_memo;
    }
}
