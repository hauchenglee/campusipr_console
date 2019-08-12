package biz.mercue.campusipr.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

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
	@OrderBy("create_date DESC")
	private Date create_date;
	
	@JsonView(View.Patent.class)
	private String business_id;

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

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	
	

}
