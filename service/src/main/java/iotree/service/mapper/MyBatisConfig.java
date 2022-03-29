package iotree.service.mapper;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class MyBatisConfig {

    /**
     * DatabaseIdProvider만 Bean으로 생성해서 적용되도록 하였다.
     * 이렇게 VendorDatabaseIdProvider를 Bean으로 설정하면 자동으로 주입되어 적용된다.
     * 각 property의 value 값이 mapper 선언의 databaseId로 지정되면 loading 되어 적용된다.
     */
    @Bean
    DatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MariaDB", "mariadb");
        properties.setProperty("MySQL", "mysql");
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("SQL Server", "sqlserver");
        properties.setProperty("PostgreSql", "postgresql");
        properties.setProperty("DB2", "db2");
        properties.setProperty("HSQL", "hsqldb");
        properties.setProperty("H2", "h2");
        properties.setProperty("Derby", "derby");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }
}
