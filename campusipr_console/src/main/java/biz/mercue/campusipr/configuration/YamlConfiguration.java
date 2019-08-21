package biz.mercue.campusipr.configuration;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import biz.mercue.campusipr.util.Constants;
import biz.mercue.campusipr.util.StringUtils;
import biz.mercue.campusipr.util.YamlProperty;

@Component
public class YamlConfiguration {

	private Logger log = Logger.getLogger(YamlConfiguration.class);

	private YamlProperty config;

	public YamlProperty getConfig() {
		return config;
	}

	public YamlConfiguration() {
		loadFromFile("/filter.yml");
	}

	@SuppressWarnings("unchecked")
	private void loadFromFile(String path) {
		
		Yaml yaml = new Yaml();
		InputStream filter = null;
		InputStream userIn = null;
		try {
			filter = YamlConfiguration.class.getResourceAsStream(path);
			
			List<Map<String, String>> list = (List<Map<String, String>>) yaml.load(filter);
			InetAddress netAddress = null;
			String local_ip = null;
			//String hostName = null;
			try{   
				netAddress = InetAddress.getLocalHost();  
				if(netAddress != null) { 
					local_ip = netAddress.getHostAddress();
					log.info("local_ip :"+local_ip);
					
					Constants.LOCAL_IP = local_ip;
				}
	        }catch(UnknownHostException e){  
	            log.error("unknown host!");  
	        }  
			

			
			
			String sysUser = System.getenv("USER");
			if(StringUtils.isNULL(sysUser)) {
				sysUser = System.getenv("USERNAME");
			}
		
			log.info("get system user: " + sysUser);
			log.info(list.size());
			for(int i=0; i<list.size(); i++) {
				log.info(list.get(i).get("name"));
				Map<String, String> mMap = list.get(i);
				
				String mIp = mMap.get("ip");
				String mName = mMap.get("name");
				if (StringUtils.isNULL(mIp) || StringUtils.isNULL(local_ip) ) {
					if (mName.equals(sysUser)) {
						userIn = YamlConfiguration.class.getResourceAsStream(mMap.get("path"));
						log.info("get yaml file path: " + mMap.get("path"));
						
						Constants.IPs = mIp;
					}
				}else {
					if( !StringUtils.isNULL(mIp) && !StringUtils.isNULL(mName) && mIp.contains(local_ip) && mName.equals(sysUser)) {
						userIn = YamlConfiguration.class.getResourceAsStream(mMap.get("path"));
						log.info("get yaml file path: " + mMap.get("path"));
						
						Constants.IPs = mIp;
					}
				}
				if(userIn!=null) {
					break;
				}
			}
			
//			String sysUser = System.getenv("USER");
//			String sysUserWin = System.getenv("USERNAME");
//			log.info("get system user: " + sysUser + ", win user: " + sysUserWin);
//			log.info(list.size());
//			for(int i=0; i<list.size(); i++) {
//				log.info(list.get(i).get("name"));
//				if (list.get(i).get("name").equals(sysUser) || list.get(i).get("name").equals(sysUserWin)) {
//					userIn = YamlConfiguration.class.getResourceAsStream(list.get(i).get("path"));
//					log.info("get yaml file path: " + list.get(i).get("path"));
//				}
//			}
			
			if (userIn != null) {
				config = yaml.loadAs(userIn, YamlProperty.class);
				log.info("ip: " + config.getIp());
			}
			

		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (filter != null) {
					filter.close();				
				}
				if (userIn != null) {
					userIn.close();
				}				
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			
		}
	}
}
