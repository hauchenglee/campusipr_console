package biz.mercue.campusipr.model;

import java.util.Date;
import java.util.List;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="patent_status")
@AssociationOverrides({
    @AssociationOverride(name = "primaryKey.patent",
        joinColumns = @JoinColumn(name = "patent_id")),
    @AssociationOverride(name = "primaryKey.status",
        joinColumns = @JoinColumn(name = "status_id")) })
public class PatentStatus extends BaseBean {
	
	@EmbeddedId
	private PatentStatusId primaryKey = new PatentStatusId();
	
	@Id
	@Temporal(TemporalType.DATE)
	@JsonView(View.Patent.class)
	private Date create_date;

	public PatentStatusId getPrimaryKey() {
        return primaryKey;
    }
	
	public void setPrimaryKey(PatentStatusId primaryKey) {
	        this.primaryKey = primaryKey;
	}
	
	@Transient
//	@JsonView({View.Patent.class,View.PortfolioDetail.class})
    public Patent getPatent() {
        return getPrimaryKey().getPatent();
    }
 
    public void setPatent(Patent patent) {
        getPrimaryKey().setPatent(patent);
    }
    
	@Transient
	@JsonView({View.Patent.class,View.PortfolioDetail.class})
    public Status getStatus() {
        return getPrimaryKey().getStatus();
    }
 
    public void setStatus(Status status) {
        getPrimaryKey().setStatus(status);
    }

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	
	

}
