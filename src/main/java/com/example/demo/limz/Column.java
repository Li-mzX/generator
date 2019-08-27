/*******************************************************************************
 * @(#)Column.java 2019年08月27日 16:28
 * Copyright 2019 明医众禾科技（北京）有限责任公司. All rights reserved.
 *******************************************************************************/
package com.example.demo.limz;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <b>Application name：</b> Column.java <br>
 * <b>Application describing： </b> <br>
 * <b>Copyright：</b> Copyright &copy; 2019 明医众禾科技（北京）有限责任公司 版权所有。<br>
 * <b>Company：</b> 明医众禾科技（北京）有限责任公司 <br>
 * <b>Date：</b> 2019年08月27日 16:28 <br>
 * <b>@author：</b> <a href="mailto:limz@miyzh.com"> 李明哲 </a> <br>
 * <b>@version：</b>V1.0.0 <br>
 */
@Data
@AllArgsConstructor
public class Column {

    private String name;

    private String field;

    private String type;

    private String comment;
}