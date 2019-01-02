package biz.mercue.campusipr.model;

public class View {
	public interface Public {}
	public interface Token extends Public {}
	
	public interface Admin extends Public {}
	public interface Role extends Public {}
	public interface Permission extends Public {}

	public interface Business extends Public {}
	public interface BusinessDetail extends Business {}
	
	public interface Patent extends Public {}
	
	public interface PatentDetail extends Patent {}
	public interface PatentHistory extends Public {}
	
	public interface Portfolio extends Public {}
	public interface PortfolioDetail extends Portfolio {}

	public interface Reminder extends Public {}
	
	public interface Banner extends Public {}
	

	

}
