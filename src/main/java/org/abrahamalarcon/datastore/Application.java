package org.abrahamalarcon.datastore;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.abrahamalarcon.datastore.aspect.audit.AuditLoggingAspect;
import org.abrahamalarcon.datastore.dao.WeatherDAO;
import org.abrahamalarcon.datastore.endpoint.DatastoreEndpoint;
import org.abrahamalarcon.datastore.endpoint.DatastoreEndpointImpl;
import org.abrahamalarcon.datastore.service.Query;
import org.abrahamalarcon.datastore.service.audit.AuditEvent;
import org.abrahamalarcon.datastore.service.audit.AuditEventFactory;
import org.abrahamalarcon.datastore.service.audit.AuditEventLogger;
import org.abrahamalarcon.datastore.service.audit.AuditEventName;
import org.abrahamalarcon.datastore.service.audit.datasource.DatasourceAuditEventLogger;
import org.abrahamalarcon.datastore.service.audit.datasource.DatasourceAuditLoggingService;
import org.abrahamalarcon.datastore.util.APIExceptionMapper;
import org.abrahamalarcon.datastore.util.BaseInputValidator;
import org.abrahamalarcon.datastore.util.CustomResponseHandler;
import org.abrahamalarcon.datastore.util.ExceptionHandlerAspect;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by AbrahamAlarcon on 12/26/2016.
 */
@SpringBootApplication
@ComponentScan
@EnableAsync
@EnableScheduling
@PropertySources({
        @PropertySource("classpath:environment.properties.${spring.profiles.active}")
})
@ConfigurationProperties(prefix = "server", ignoreUnknownFields = true)
//@EnableEurekaClient
@EnableCircuitBreaker
@EnableCaching
public class Application
{
    @Autowired private Environment env;
    @Autowired private Bus bus;

    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"), new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
        return factory;
    }

    /*
    @Bean
    public JettyEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"), new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
        return factory;
    }
    */

    @Bean
    public FilterRegistrationBean logFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(logFilter());
        filterRegistrationBean.setOrder(Integer.MIN_VALUE);
        return filterRegistrationBean;
    }

    @Bean
    public Filter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter()
        {
            private AuditEventFactory auditEventFactory = auditEventFactory();
            private DatasourceAuditLoggingService datasourceAuditLoggingService = datasourceAuditLoggingService();

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException
            {
                this.beforeRequest(request);
                try
                {
                    filterChain.doFilter(request, response);
                }
                finally
                {
                    this.afterRequest(request, response);
                }
            }

            protected void beforeRequest(HttpServletRequest request) {
                AuditEvent event = auditEventFactory.create(AuditEventName.API_ENTRY_POINT, request);
                event.setDate(new Date());
                event.setMethodName(request.getRequestURI());
                event.setThreadName(Thread.currentThread().getName());
                event.setUserName(request.getHeader("ct-remote-user"));
                request.setAttribute("event", event);
            }

            protected void afterRequest(HttpServletRequest request, HttpServletResponse response) {
                AuditEvent event = (AuditEvent) request.getAttribute("event");
                event.setEndDate(new Date());
                boolean isSuccess = false;

                switch(response.getStatus()) {
                    case 200:
                        isSuccess = true;
                        break;
                    case 402:
                        isSuccess = true;
                        break;
                }

                event.setSuccess(isSuccess);
                datasourceAuditLoggingService.log(event);
            }
        };
        return filter;
    }

    @Bean
    public AuditEventFactory auditEventFactory() {
        return new AuditEventFactory();
    }

    @Bean
    public DatasourceAuditLoggingService datasourceAuditLoggingService() {
        return new DatasourceAuditLoggingService();
    }

    @Bean
    public AuditEventLogger auditEventLogger() {
        return new AuditEventLogger();
    }

    @Bean
    public DatasourceAuditEventLogger datasourceAuditEventLogger() {
        return new DatasourceAuditEventLogger();
    }

    @Bean
    public AuditLoggingAspect auditLoggingAspect() {
        return new AuditLoggingAspect();
    }

    @Bean(name = "loggingThreadPoolTaskExecutor")
    public Executor loggingThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Integer.parseInt(env.getProperty("logging.threadPoolTaskExecutor.corePoolSize")));
        executor.setMaxPoolSize(Integer.parseInt(env.getProperty("logging.threadPoolTaskExecutor.maxPoolSize")));
        executor.setQueueCapacity(Integer.parseInt(env.getProperty("logging.threadPoolTaskExecutor.queueCapacity")));
        executor.setThreadNamePrefix(env.getProperty("logging.threadPoolTaskExecutor.threadNamePrefix"));
        executor.initialize();
        return executor;
    }

    @Bean
    public DatastoreEndpoint datastoreEndpointImpl()
    {
       return new DatastoreEndpointImpl();
    }
    
    @EnableWebSecurity
    public class ConfigApplicationSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .disable()
                    .authorizeRequests()
                    .antMatchers("/**")
                    .permitAll();
        }
    }

    @Bean
    public APIExceptionMapper apiExceptionMapper() {
        return new APIExceptionMapper();
    }

    @Bean
    public List<Object> providers() {
        List<Object> providers = new ArrayList<>();
        JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
        CustomResponseHandler responseHandler = new CustomResponseHandler();
        providers.add(jsonProvider);
        providers.add(apiExceptionMapper());
        providers.add(responseHandler);
        return providers;
    }

    @Bean
    public Server jaxrsServer() {
        Swagger2Feature swagger2Feature = new Swagger2Feature();
        swagger2Feature.setContact(env.getProperty("product.contactEmail"));
        swagger2Feature.setTitle(env.getProperty("product.title"));
        swagger2Feature.setDescription(env.getProperty("product.description"));
        swagger2Feature.setVersion(env.getProperty("product.version"));
        swagger2Feature.setTermsOfServiceUrl(env.getProperty("product.termsOfServiceUrl"));
        swagger2Feature.setLicenseUrl(env.getProperty("product.termsOfServiceUrl"));
        swagger2Feature.setLicense(
                env.getProperty("product.vendorName") + " "
                        + env.getProperty("product.name") + " "
                        + env.getProperty("product.version"));

        JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
        factory.setAddress("/");
        factory.setServiceBeans(Arrays.asList(datastoreEndpointImpl()));
        factory.setBus(bus);
        factory.getFeatures().add(swagger2Feature);
        factory.setProviders(providers());
        return factory.create();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    public MessageSource errorCodeMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("error_codes");
        return messageSource;
    }

    @Bean
    public ExceptionHandlerAspect exceptionHandlerAspect()
    {
        return new ExceptionHandlerAspect();
    }

    @Bean
    public BaseInputValidator inputValidator() throws Exception {
        InputValidators inputValidators = new InputValidators();
        BaseInputValidator inputValidator = new BaseInputValidator();
        inputValidator.setValidators(inputValidators.validators());
        return inputValidator;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
    
    @Bean
    public RetryTemplate retryRestTemplate()
    {
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(Integer.parseInt(env.getProperty("weather.retry.policy.maxAttempts")));
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(Long.parseLong(env.getProperty("weather.retry.policy.backOffPeriodMillis")));
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        return retryTemplate;
    }

    @Bean
    public WeatherDAO weatherDAO()
    {
        WeatherDAO weatherDAO = new WeatherDAO();
        weatherDAO.setUrl(env.getProperty("weather.url"));
        return weatherDAO;
    }

    @Bean
    public CacheManager getEhCacheManager(){
        return  new EhCacheCacheManager(getEhCacheFactory().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean getEhCacheFactory(){
        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factoryBean.setShared(true);
        return factoryBean;
    }

    @Bean
    public Query query() {
        return new Query();
    }
    
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.addListeners(new ApplicationPidFileWriter("app.pid"));
        application.run(args);
    }

}
