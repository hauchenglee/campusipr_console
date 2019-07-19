package biz.mercue.campusipr.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.List;

//@Entity
//@Table(name = "technology")
public class Technology extends BaseBean {
    @Id
    @JsonView({View.TechnologyList.class, View.TechnologyDetail.class})
    private String technology_id;

    @JsonView({View.TechnologyDetail.class})
    private String technology_name;

    @JsonView({View.TechnologyList.class, View.TechnologyDetail.class})
    private String technology_memo;

//    @JsonView({View.TechnologyList.class, View.TechnologyDetail.class})
//    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
//    @JoinTable(name = "")
//    private Business business;

    @JsonView({View.TechnologyList.class, View.TechnologyDetail.class})
    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TechnologyInventor> listInventor;

    @JsonView({View.TechnologyList.class, View.TechnologyDetail.class})
    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TechnologyStatus> listStatus;

    @JsonView({View.TechnologyList.class, View.TechnologyDetail.class})
    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TechnologyExtension> listExtension;

    @JsonView({})
    @OneToMany(mappedBy = "technology", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TechnologyEditHistory> listHistory;

    public String getTechnology_id() {
        return technology_id;
    }

    public void setTechnology_id(String technology_id) {
        this.technology_id = technology_id;
    }

    public String getTechnology_name() {
        return technology_name;
    }

    public void setTechnology_name(String technology_name) {
        this.technology_name = technology_name;
    }

    public String getTechnology_memo() {
        return technology_memo;
    }

    public void setTechnology_memo(String technology_memo) {
        this.technology_memo = technology_memo;
    }

//    public Business getBusiness() {
//        return business;
//    }
//
//    public void setBusiness(Business business) {
//        this.business = business;
//    }

    public List<TechnologyInventor> getListInventor() {
        return listInventor;
    }

    public void setListInventor(List<TechnologyInventor> listInventor) {
        this.listInventor = listInventor;
    }

    public List<TechnologyStatus> getListStatus() {
        return listStatus;
    }

    public void setListStatus(List<TechnologyStatus> listStatus) {
        this.listStatus = listStatus;
    }

    public List<TechnologyExtension> getListExtension() {
        return listExtension;
    }

    public void setListExtension(List<TechnologyExtension> listExtension) {
        this.listExtension = listExtension;
    }

    public List<TechnologyEditHistory> getListHistory() {
        return listHistory;
    }

    public void setListHistory(List<TechnologyEditHistory> listHistory) {
        this.listHistory = listHistory;
    }
}
