package biz.mercue.campusipr.model;

public class View {
	public interface Public {}
	public interface Token extends Public {}
	
	public interface Admin extends Public {}
	public interface Role extends Public {}
	public interface Permission extends Public {}
	public interface PermissionId extends Public {}

	public interface Business extends Public {}
	public interface BusinessDetail extends Business {}
	
	public interface Patent extends Public {}
	
	public interface PatentDetail extends Patent {}
	public interface PatentEnhance extends PatentDetail {}
	public interface PatentFamily extends Patent {}
	public interface PatentHistory extends Public {}
	
	public interface Portfolio extends Public {}
	public interface PortfolioDetail extends Portfolio {}

	public interface Reminder extends Public {}
	
	public interface Banner extends Public {}
	
	public interface ExcelTask extends Public {}
	public interface FieldMap extends Patent {}
	

	

}
