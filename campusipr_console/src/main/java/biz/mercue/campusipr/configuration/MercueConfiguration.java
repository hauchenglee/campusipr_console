package biz.mercue.campusipr.configuration;

import biz.mercue.campusipr.controller.QuartzSchedulerListener;
import biz.mercue.campusipr.service.QuartzService;
import biz.mercue.campusipr.util.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.io.IOException;


@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "biz.mercue.campusipr")
public class MercueConfiguration implements WebMvcConfigurer {

    private Logger log = Logger.getLogger(MercueConfiguration.class);

    @Autowired
    QuartzService quartzService;

    @Autowired
    YamlConfiguration yamlConfig;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        registry.viewResolver(viewResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");

        Constants.IMAGE_UPLOAD_PATH = yamlConfig.getConfig().getPath().get("image");
        Constants.IMAGE_LOAD_URL = yamlConfig.getConfig().getUrl().get("image");
        Constants.VIDEO_UPLOAD_PATH = yamlConfig.getConfig().getPath().get("video");
        Constants.VIDEO_LOAD_URL = yamlConfig.getConfig().getUrl().get("video");

        Constants.FILE_UPLOAD_PATH = yamlConfig.getConfig().getPath().get("file");
        Constants.FILE_LOAD_URL = yamlConfig.getConfig().getUrl().get("file");

        Constants.IMAGEMAGICK_PATH = yamlConfig.getConfig().getPath().get("imagemagick");
        Constants.GRAPHICSMAGICK_PATH = yamlConfig.getConfig().getPath().get("graphicsmagick");


        Constants.RECAPTCHA_SECRET_KEY = yamlConfig.getConfig().getGoogle().get("recaptcha");
        //set patent key in here
        Constants.PATENT_WEB_SERVICE_TW = yamlConfig.getConfig().getPatent_api().get("patent_webservice_tw");
        Constants.PATENT_KEY_TW = yamlConfig.getConfig().getPatent_api().get("patent_key_tw");

        Constants.PATENT_WEB_SERVICE_US = yamlConfig.getConfig().getPatent_api().get("patent_webservice_us");
        Constants.PATENT_INVENTOR_WEB_SERVICE_US = yamlConfig.getConfig().getPatent_api().get("patent_inventor_webservice_us");
        Constants.PATENT_CONTEXT_WEB_SERVICE_US = yamlConfig.getConfig().getPatent_api().get("patent_context_webservice_us");

        Constants.PATENT_WEB_SERVICE_EU = yamlConfig.getConfig().getPatent_api().get("patent_webservice_eu");
        Constants.PATENT_TOKEN_EU = yamlConfig.getConfig().getPatent_api().get("patent_token_eu");


        Constants.MAIL_USER_NAME = yamlConfig.getConfig().getMail().get("mail_username");
        Constants.MAIL_PASSWORD = yamlConfig.getConfig().getMail().get("mail_pwd");
        Constants.MAIL_HOST = yamlConfig.getConfig().getMail().get("mail_host");
        Constants.MAIL_PORT = yamlConfig.getConfig().getMail().get("mail_host_port");
        Constants.MAIL_STARTTLS = yamlConfig.getConfig().getMail().get("mail_starttls");
        Constants.MAIL_FROM = yamlConfig.getConfig().getMail().get("mail_from");


        Constants.HTML_FORGET_PASSWORD = yamlConfig.getConfig().getHtml().get("forget_password");
        Constants.HTML_NEW_ACCOUNT = yamlConfig.getConfig().getHtml().get("new_account");
        Constants.HTML_ONE_PATENT_CHANGE = yamlConfig.getConfig().getHtml().get("one_patent_change");
        Constants.HTML_MULTIPLE_PATENT_CHANGE = yamlConfig.getConfig().getHtml().get("multiple_patent_change");
        Constants.HTML_ANNUITY_REMINDER = yamlConfig.getConfig().getHtml().get("annuity_reminder");

        Constants.URL_RESET_PASSWORD_TRANSFER = yamlConfig.getConfig().getHtml().get("url_reset_password_transfer");
        Constants.URL_ENABLE_PASSWORD_TRANSFER = yamlConfig.getConfig().getHtml().get("url_enable_password_transfer");
        Constants.URL_RESET_PASSWORD = yamlConfig.getConfig().getHtml().get("url_reset_password");
        Constants.URL_ENABLE_PASSWORD = yamlConfig.getConfig().getHtml().get("url_enable_password");
        Constants.URL_LOGIN = yamlConfig.getConfig().getHtml().get("url_login");

        Constants.URL_PATENT_CONTENT = yamlConfig.getConfig().getHtml().get("url_patent_content");
    }

    // request adapter
    @Bean
    public RequestInterceptorAdapter requestInterceptorAdapter() {
        return new RequestInterceptorAdapter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptorAdapter());
    }

    // password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // schedule
    @Bean
    public QuartzSchedulerListener quartzSchedulerListener() {
        QuartzSchedulerListener listener = new QuartzSchedulerListener();
        return listener;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();

        factoryBean.setSchedulerListeners(quartzSchedulerListener());

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        factoryBean.setJobFactory(jobFactory);
        factoryBean.setApplicationContextSchedulerContextKey("applicationContext");


        return factoryBean;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getResolver() throws IOException {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        //Set the maximum allowed size (in bytes) for each individual file.
        resolver.setMaxUploadSizePerFile(52428800);//50MB

        //You may also set other available properties.
        return resolver;
    }
}
