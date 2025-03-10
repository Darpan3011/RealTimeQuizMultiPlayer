package com.darpan.realtimemultiplayerquiz.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan("com.darpan.realtimemultiplayerquiz.model");
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties hibernateProperties = new Properties();
        hibernateProperties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        hibernateProperties.put(Environment.SHOW_SQL, "true");
        hibernateProperties.put(Environment.HBM2DDL_AUTO, "update");

        entityManagerFactoryBean.setJpaProperties(hibernateProperties);
        return entityManagerFactoryBean;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.darpan.realtimemultiplayerquiz.model");

        Properties hibernateProperties = new Properties();
        hibernateProperties.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        hibernateProperties.put(Environment.SHOW_SQL, "true");
        hibernateProperties.put(Environment.HBM2DDL_AUTO, "update");

        sessionFactory.setHibernateProperties(hibernateProperties);
        return sessionFactory;
    }

    @Bean
    public SessionFactory getSessionFactory(LocalSessionFactoryBean sessionFactory) {
        return sessionFactory.getObject();
    }
}
