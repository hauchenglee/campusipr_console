package biz.mercue.campusipr.configuration;


import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "biz.mercue.campusipr" })
public class HibernateConfiguration {

	 private Logger log = Logger.getLogger(HibernateConfiguration.class);
    
    @Autowired
    private YamlConfiguration yamlConfig;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "biz.mercue.campusipr.model" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
     }
	
    @Bean
    public DataSource dataSource() {

    	
    	ComboPooledDataSource dataSource = new ComboPooledDataSource();
    	try {
	        dataSource.setDriverClass(yamlConfig.getConfig().getJdbc().get("driverClassName"));
	        dataSource.setJdbcUrl(yamlConfig.getConfig().getJdbc().get("url"));
	        dataSource.setUser(yamlConfig.getConfig().getJdbc().get("username"));
	        dataSource.setPassword(yamlConfig.getConfig().getJdbc().get("password"));
	        
	        dataSource.setMaxPoolSize(300);
	        dataSource.setMinPoolSize(10);
	        dataSource.setAcquireIncrement(5);
	        dataSource.setMaxStatements(0);
	        dataSource.setIdleConnectionTestPeriod(60);
    	} catch (IllegalStateException e) {
    		log.error("IllegalStateException :"+e.getMessage() );

		} catch (PropertyVetoException e) {
			log.error("PropertyVetoException :"+e.getMessage() );
		}
        return dataSource;
    }
    
    private Properties hibernateProperties() {
        Properties properties = new Properties();

        properties.put("hibernate.dialect", yamlConfig.getConfig().getHibernate().get("dialect"));
        properties.put("hibernate.show_sql", yamlConfig.getConfig().getHibernate().get("show_sql"));
        properties.put("hibernate.format_sql", yamlConfig.getConfig().getHibernate().get("format_sql"));
        properties.put("hibernate.current_session_context_class", yamlConfig.getConfig().getHibernate().get("current_session_context_class"));
        
        properties.put("hibernate.connection.CharSet", yamlConfig.getConfig().getHibernate().get("connection_charset"));
        properties.put("hibernate.connection.characterEncoding", yamlConfig.getConfig().getHibernate().get("connection_character_encoding"));
        return properties;        
    }
    
	@Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }
}

