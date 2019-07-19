package biz.mercue.campusipr.model;

import javax.persistence.*;

//@Entity
//@Table(name = "technology_inventor")
public class TechnologyInventor {
    @Id
    private String inventor_id;

    @ManyToOne
    @JoinColumn(name = "technology_id")
    private Technology technology;

    public String getInventor_id() {
        return inventor_id;
    }

    public void setInventor_id(String inventor_id) {
        this.inventor_id = inventor_id;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }
}
