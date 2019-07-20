package com.alpaca.bootstrapper.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.err;

/**
 * @Author ：lichenw
 * @Date ：Created in 21:34 2019/3/13
 * @Description：
 * @Modified By：
 */
public class MysqlGenerator {

    static final String TABLE_PREFIX = "sys_";
    static final String[] TABLE_INCLUDE = new String[]{
            "sys_Aistributor",
    };
    static final String PACKAGE_PARENT = "com.alpaca.modules";
    static final String PACKAGE_MODULE = "pomgr";

    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/mydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static final String DB_USER_NAME = "admin";
    static final String DB_PASSWORD = "abc123456";
    static final String OUT_DIR = "E:\\workSpace\\alpacaserver\\ger";


    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(getGlobalConfig());

        generator.setDataSource(getDataSourceConfig());

        generator.setStrategy(getStrategyConfig());

        generator.setPackageInfo(getPackageConfig());

        generator.setCfg(getInjectionConfig());

        generator.setTemplate(getTemplateConfig());



        generator.execute();

        err.println(generator.getCfg().getMap().get("injMp"));

    }


    private static StrategyConfig getStrategyConfig() {
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setTablePrefix(new String[]{TABLE_PREFIX});
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        strategyConfig.setInclude(TABLE_INCLUDE);
        strategyConfig.setEntityLombokModel(true);
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        strategyConfig.setLogicDeleteFieldName("LogicStatus");
        return strategyConfig;
    }

    private static TemplateConfig getTemplateConfig() {
        return new TemplateConfig();
    }

    private static InjectionConfig getInjectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("injMp", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
    }

    private static PackageConfig getPackageConfig() {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(PACKAGE_PARENT);
        packageConfig.setModuleName(PACKAGE_MODULE);
        return packageConfig;
    }

    private static DataSourceConfig getDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUrl(DB_URL);
        dataSourceConfig.setUsername(DB_USER_NAME);
        dataSourceConfig.setPassword(DB_PASSWORD);
        return dataSourceConfig;
    }

    private static GlobalConfig getGlobalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(OUT_DIR);
        globalConfig.setFileOverride(true);
        globalConfig.setActiveRecord(true);
        globalConfig.setEnableCache(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setSwagger2(true);
        globalConfig.setAuthor("lichenw");
        globalConfig.setMapperName("%sDao");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName(("%sController"));
        return globalConfig;
    }

}
