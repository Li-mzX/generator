/*******************************************************************************
 * @(#)ApplicationLicense.java 2019年08月27日 16:51
 * Copyright 2019 明医众禾科技（北京）有限责任公司. All rights reserved.
 *******************************************************************************/
package com.example.demo.limz;

import jdk.nashorn.internal.ir.IfNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Objects;

/**
 * <b>Application name：</b> ApplicationLicense.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 明医众禾科技（北京）有限责任公司 版权所有。<br>
 * <b>Company：</b> 明医众禾科技（北京）有限责任公司 <br>
 * <b>Date：</b> 2019年08月27日 16:51 <br>
 * <b>@author：</b> <a href="mailto:limz@miyzh.com"> 李明哲 </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Slf4j
public class ApplicationStartUp implements ApplicationListener<ContextRefreshedEvent> {


    /**
     * 包路径, 修改为自己的
     */
    private static final String packageName = "com.yideb.cp.settlement.entity";


    /**
     * 所有表名列表
     */
    private static final String[] tbs = new String[]{
            "sm_commission",
            "cp_finance_param"
    };

    /**
     * 生成的文件, java 在指定的包路径下,   xml 在 resources下的 mapper 下
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        String javaDir = System.getProperty("user.dir") + "\\src\\main\\java";
        String mapDir = System.getProperty("user.dir") + "\\src\\main\\resources\\mapper";
        String[] split = packageName.split("\\.");
        File fd = new File(javaDir);
        for (String s : split) {
            String[] list = fd.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    log.info("{} {}", dir, name);
                    return Objects.equals(s, name);
                }
            });
            javaDir +=( "/"+s);
            fd = new File(javaDir);
            if (list == null || list.length == 0){
                fd.mkdir();
            }
        }
        fd = new File(mapDir);
        if (!fd.exists()) {
            fd.mkdir();
        }
        Dao dao = contextRefreshedEvent.getApplicationContext().getBean("dao", Dao.class);


        try {
            for (String tbname : tbs) {
                String test = dao.test(packageName, tbname,javaDir,mapDir);
                log.info(test);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}