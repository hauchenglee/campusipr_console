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

import com.zaxxer.hikari.HikariDataSource;


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

    	
    	 HikariDataSource dataSource = new HikariDataSource();

	     dataSource.setDataSourceClassName(yamlConfig.getConfig().getJdbc().get("driverClassName"));
	     dataSource.addDataSourceProperty("url",yamlConfig.getConfig().getJdbc().get("url"));
	     dataSource.addDataSourceProperty("user", yamlConfig.getConfig().getJdbc().get("username"));
	     dataSource.addDataSourceProperty("password",yamlConfig.getConfig().getJdbc().get("password"));
	     dataSource.setMaximumPoolSize(300);
	     dataSource.setMinimumIdle(10);
	     dataSource.setConnectionTimeout(20000);
	     dataSource.setIdleTimeout(30000);
	     dataSource.setMaxLifetime(1800000);
	     dataSource.setConnectionTestQuery("SELECT 1");
//    	 dataSource.addDataSourceProperty("cachePrepStmts", true);
//    	 dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
//    	 dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
//    	 dataSource.addDataSourceProperty("useServerPrepStmts", true);
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

