package com.backend.commonservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Primary
    @Bean
    public FreeMarkerConfigurationFactoryBean factoryBean(){
        FreeMarkerConfigurationFactoryBean factoryBean = new FreeMarkerConfigurationFactoryBean();
        factoryBean.setTemplateLoaderPath("classpath:templates/");
        factoryBean.setDefaultEncoding("UTF-8");
        return factoryBean;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.example.com"); // Địa chỉ SMTP server (thay đổi với server của bạn)
        mailSender.setPort(587); // Cổng SMTP (có thể thay đổi tùy thuộc vào dịch vụ của bạn)

        // Cấu hình thông tin tài khoản email
        mailSender.setUsername("your-email@example.com"); // Thay đổi với email của bạn
        mailSender.setPassword("your-email-password"); // Thay đổi với mật khẩu email của bạn

        // Cấu hình các thuộc tính của mail
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }
}
