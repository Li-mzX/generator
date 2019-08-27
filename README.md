# generator
明医众禾专用, 生成 实体类 和 mapper.xml

下载到本地后, 用IDEA 打开

找到 com.example.demo.limz.ApplicationStartUp  这个类
打开它 找到以下 


    /**
     * 包路径, 修改为自己的
     */
    private static final String packageName = "com.yideb.cp.settlement.entity";


    /**
     * 你想转化的所有表名列表
     */
    private static final String[] tbs = new String[]{
            "sm_commission",
            "cp_finance_param"
    };

修改完成后, 找到  com.example.demo.DemoApplication  打开它

右键 > Run 

随便切个别的东西再切回来让IDEA 刷新文件列表

在你写的包路径下,找到你所有的java类文件
会带有所有本次建中大哥写的注解

在resources/mapper 下 找到所有的xml文件, 复制到你自己的项目
有基本的map 和一个  selectByPrimaryKey 的方法, 其余的自己写

** 默认库为 207 myscdb2-5 开发库, 在application.yml中, 可以自己修改