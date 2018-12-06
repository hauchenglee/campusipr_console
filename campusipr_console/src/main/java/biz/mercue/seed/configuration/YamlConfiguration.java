package biz.mercue.seed.configuration;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import biz.mercue.seed.util.YamlProperty;

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
			
			String sysUser = System.getenv("USER");
			String sysUserWin = System.getenv("USERNAME");
			log.info("get system user: " + sysUser + ", win user: " + sysUserWin);
			log.info(list.size());
			for(int i=0; i<list.size(); i++) {
				log.info(list.get(i).get("name"));
				if (list.get(i).get("name").equals(sysUser) || list.get(i).get("name").equals(sysUserWin)) {
					userIn = YamlConfiguration.class.getResourceAsStream(list.get(i).get("path"));
					log.info("get yaml file path: " + list.get(i).get("path"));
				}
			}
			
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
