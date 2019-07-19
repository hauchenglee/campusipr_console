package biz.mercue.campusipr.model;

import javax.persistence.*;

//@Entity
//@Table(name = "technology_edit_history")
public class TechnologyEditHistory extends BaseBean {
    @Id
    private String history_id;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name="technology_id", referencedColumnName="technology_id")
    private Technology technology;

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public Technology getTechnology() {
        return technology;
    }

    public void setTechnology(Technology technology) {
        this.technology = technology;
    }
}
