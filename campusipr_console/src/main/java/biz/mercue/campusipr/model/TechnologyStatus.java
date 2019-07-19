package biz.mercue.campusipr.model;

import javax.persistence.*;

//@Entity
//@Table(name = "technology_status")
public class TechnologyStatus extends BaseBean {
    @Id
    private String status_id;

    @ManyToOne
    @JoinColumn(name = "technology_id")
    private Technology technology;

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
        this.status_id = status_id;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }
}
