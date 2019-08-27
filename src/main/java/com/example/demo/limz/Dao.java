/*******************************************************************************
 * @(#)Dao.java 2019年08月27日 15:18
 * Copyright 2019 明医众禾科技（北京）有限责任公司. All rights reserved.
 *******************************************************************************/
package com.example.demo.limz;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>Application name：</b> Dao.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 明医众禾科技（北京）有限责任公司 版权所有。<br>
 * <b>Company：</b> 明医众禾科技（北京）有限责任公司 <br>
 * <b>Date：</b> 2019年08月27日 15:18 <br>
 * <b>@author：</b> <a href="mailto:limz@miyzh.com"> 李明哲 </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Repository
@Slf4j
public class Dao {

    @Autowired
    SqlSessionTemplate sessionTemplate;

    public String test(String pack, String tbname, String javaDir, String mapDir) throws Exception {
        SqlSessionFactory sqlSessionFactory = sessionTemplate.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection conn = sqlSession.getConnection();
        PreparedStatement ps;
        ResultSet rs;
        String comment = "";
        ps = conn.prepareStatement("select table_comment from information_schema.tables where table_name = ? and  table_schema = 'myscdb2-5'");
        ps.setString(1, tbname);
        rs = ps.executeQuery();
        if (rs.next()) {
            comment = rs.getString(1);
        }
        rs.close();
        ps.close();
        ps = conn.prepareStatement("select column_name,extra from information_schema.columns where table_schema = 'myscdb2-5'  and table_name = ? and column_key = 'PRI'");
        ps.setString(1, tbname);
        rs = ps.executeQuery();
        String pk = "";
        boolean isAuto = false;
        if (rs.next()) {
            pk = rs.getString(1);
            isAuto = "auto_increment".equalsIgnoreCase(rs.getString(2));

        }
        rs.close();
        ps.close();

        ps = conn.prepareStatement("select column_name,data_type,column_comment \n" +
                "\n" +
                "from information_schema.columns \n" +
                "\n" +
                "where table_name=? and table_schema='myscdb2-5'");
        ps.setString(1, tbname);
        rs = ps.executeQuery();
        List<Column> columns = new ArrayList<>();
        Set<String> typeSet = new HashSet<>();
        while (rs.next()) {
            columns.add(new Column(rs.getString(1), Tool.lineToHump(rs.getString(1)), rs.getString(2), rs.getString(3)));
            typeSet.add(getType(rs.getString(2)));
        }
        rs.close();
        ps.close();

        String clzName = Tool.lineToHump2(tbname);
        StringBuilder mapper = new StringBuilder();
        mapper.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("\n");
        mapper.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >").append("\n");
        mapper.append("<mapper namespace=\"").append(clzName).append("Mapper\" >").append("\n");
        mapper.append("  <resultMap id=\"BaseResultMap\" type=\"").append(pack).append(".").append(clzName).append("\" >").append("\n");
        String finalPk = pk;
        columns.forEach(column -> {
            if (column.getName().equalsIgnoreCase(finalPk)) {
                mapper.append("    <id column=\"").append(column.getName()).append("\" property=\"").append(column.getField()).append("\" jdbcType=\"").append(column.getType()).append("\" />").append("\n");
            } else {
                mapper.append("    <result column=\"").append(column.getName()).append("\" property=\"").append(column.getField()).append("\" jdbcType=\"").append(column.getType()).append("\" />").append("\n");
            }
        });
        mapper.append("  </resultMap>").append("\n");
        mapper.append("  <sql id=\"Base_Column_List\" >").append("\n");
        String columnsStr = columns.stream().map(Column::getName).collect(Collectors.joining(","));
        mapper.append("    ").append(columnsStr).append("\n");
        mapper.append("  </sql>").append("\n");
        mapper.append("  <select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\" >").append("\n");
        mapper.append("  select").append("\n");
        mapper.append("  <include refid=\"Base_Column_List\" />").append("\n");
        mapper.append("  from ").append(tbname).append("\n");
        mapper.append("  where ").append(pk).append(" = #{0}").append("\n");
        mapper.append("  </select>").append("\n");
        mapper.append("</mapper>").append("\n");


        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(pack).append(";\n");
        sb.append("\n");
        sb.append("import com.yideb.data.persistence.annotation.Column;").append("\n");
        sb.append("import com.yideb.data.persistence.annotation.Id;").append("\n");
        if (isAuto) {
            sb.append("import com.yideb.data.persistence.annotation.IdType;").append("\n");
        }
        sb.append("import com.yideb.data.persistence.annotation.Table;").append("\n");
        sb.append("import lombok.Data;").append("\n");
        if (typeSet.contains("BigDecimal")) {
            sb.append("import java.math.BigDecimal;").append("\n");
        }
        if (typeSet.contains("Date")) {
            sb.append("import java.util.Date;").append("\n");
        }
        sb.append("\n");
        sb.append("/**\n" + " * <b>Application name：</b> DdOrder.java <br>\n" + " * <b>Application describing：").append(comment).append(" </b> <br>\n").append(" * <b>Copyright：</b> Copyright &copy; 2019 明医众禾科技（北京）有限责任公司 版权所有。<br>\n").append(" * <b>Company：</b> 明医众禾科技（北京）有限责任公司 <br>\n").append(" * <b>Date：</b> 2019年08月27日 14:12 <br>\n").append(" * <b>@author：</b> <a href=\"mailto:limz@miyzh.com\"> 李明哲 </a> <br>\n").append(" * <b>@version：</b>V1.0.0 <br>\n").append(" */").append("\n");
        sb.append("\n");
        sb.append("@Data").append("\n");
        sb.append("@Table(\"").append(tbname).append("\")").append("\n");
        sb.append("public class ").append(clzName).append("{").append("\n");
        sb.append("\n");
        for (Column column : columns) {
            String type = column.getType();
            type = getType(type);
            sb.append("    /** ").append(column.getComment()).append(" */").append(";\n");
            if (pk.equalsIgnoreCase(column.getName())) {
                sb.append("    @Id");
                if (isAuto) {
                    sb.append("(idType = IdType.AUTO)");
                }
                sb.append("\n");
            }
            sb.append("    @Column(\"").append(column.getName()).append("\")").append("\n");
            sb.append("    private ").append(type).append(" ").append(column.getField()).append(";\n");
            sb.append("\n");
        }
        sb.append("}");
        File javaFile = new File(javaDir + "\\" + clzName + ".java");
        PrintWriter out = new PrintWriter(javaFile);
        out.write(sb.toString());
        out.flush();
        out.close();


        File mapperFile = new File(mapDir + "\\" + clzName + "Mapper.xml");
        out = new PrintWriter(mapperFile);
        out.write(mapper.toString());
        out.flush();
        out.close();

        return "成功";
    }

    private String getType(String type) throws Exception {
        if ("bigint".equalsIgnoreCase(type)) {
            return "Long";
        }
        if ("int".equalsIgnoreCase(type)) {
            return "Integer";
        }
        if ("tinyint".equalsIgnoreCase(type)) {
            return "Integer";
        }
        if ("datetime".equalsIgnoreCase(type)) {
            return "Date";
        }
        if ("date".equalsIgnoreCase(type)) {
            return "Date";
        }
        if ("timestamp".equalsIgnoreCase(type)) {
            return "Date";
        }
        if ("char".equalsIgnoreCase(type)) {
            return "String";
        }
        if ("varchar".equalsIgnoreCase(type)) {
            return "String";
        }
        if ("decimal".equalsIgnoreCase(type)) {
            return "BigDecimal";
        }
        if ("double".equalsIgnoreCase(type)) {
            return "BigDecimal";
        }
        if ("longblob".equalsIgnoreCase(type)) {
            return "String";
        }
        if ("longtext".equalsIgnoreCase(type)) {
            return "String";
        }
        if ("mediumtext".equalsIgnoreCase(type)) {
            return "String";
        }
        if ("smallint".equalsIgnoreCase(type)) {
            return "Integer";
        }
        if ("text".equalsIgnoreCase(type)) {
            return "String";
        }
        throw new Exception("未知的类型~!!" + type);
    }

    String getFullTypeName(String type) throws Exception {
        if ("bigint".equalsIgnoreCase(type)) {
            return "java.lang.Long";
        }
        if ("int".equalsIgnoreCase(type)) {
            return "java.lang.Integer";
        }
        if ("tinyint".equalsIgnoreCase(type)) {
            return "java.lang.Integer";
        }
        if ("datetime".equalsIgnoreCase(type)) {
            return "java.util.Date";
        }
        if ("date".equalsIgnoreCase(type)) {
            return "java.util.Date";
        }
        if ("timestamp".equalsIgnoreCase(type)) {
            return "java.util.Date";
        }
        if ("char".equalsIgnoreCase(type)) {
            return "java.lang.String";
        }
        if ("varchar".equalsIgnoreCase(type)) {
            return "java.lang.String";
        }
        if ("decimal".equalsIgnoreCase(type)) {
            return "java.math.BigDecimal";
        }
        if ("double".equalsIgnoreCase(type)) {
            return "java.math.BigDecimal";
        }
        if ("longblob".equalsIgnoreCase(type)) {
            return "java.lang.String";
        }
        if ("longtext".equalsIgnoreCase(type)) {
            return "java.lang.String";
        }
        if ("mediumtext".equalsIgnoreCase(type)) {
            return "java.lang.String";
        }
        if ("smallint".equalsIgnoreCase(type)) {
            return "java.lang.Integer";
        }
        if ("text".equalsIgnoreCase(type)) {
            return "java.lang.String";
        }
        throw new Exception("未知的类型~!!" + type);
    }

}