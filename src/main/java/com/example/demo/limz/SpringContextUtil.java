package com.example.demo.limz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.util.ObjectUtils.isEmpty;

/**
 * @ClassName SpringContextUtil
 * @Description 环境
 * @Author Limz
 * @Date 2019/3/11 10:50
 * @Version 1.0
 */
@Component
@Order(value=10)
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取当前环境
     *
     * @return product1
     */
    public static String getActiveProfiles() {
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        if (isEmpty(activeProfiles)) {
            return "dev";
        }
        return context.getEnvironment().getActiveProfiles()[0];
    }

    public static <T> T getBean(String configTest,Class<T> clz) {
        return context.getBean(configTest, clz);
    }

    /**
     * 是否测试环境
     */
    public static boolean isDev() {
        if(Objects.isNull(context)) {
            log.error("无法获取环境变量");
            return true;
        }
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        if (isEmpty(activeProfiles)) {
            return true;
        }
        String active = context.getEnvironment().getActiveProfiles()[0];
        return !active.contains("product");
    }
}
