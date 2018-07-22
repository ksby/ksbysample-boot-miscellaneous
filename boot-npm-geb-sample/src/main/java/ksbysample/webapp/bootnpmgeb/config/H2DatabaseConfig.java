package ksbysample.webapp.bootnpmgeb.config;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

/**
 * Tomcat 内で in-memory モードで起動した H2 Database に外部のプロセスから接続するための
 * TCP サーバを起動する JavaConfig クラス。develop, unittest 環境でのみ起動する。
 */
@Configuration
@Profile({"develop", "unittest"})
public class H2DatabaseConfig {

    // TCP port for remote connections, default 9092
    @Value("${h2.tcp.port:9092}")
    private String h2TcpPort;

    /**
     * TCP connection to connect with SQL clients to the embedded h2 database.
     * Connect to "jdbc:h2:tcp://localhost:9092/mem:testdb", username "sa", password empty.
     */
    @Bean
    public Server h2TcpServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", h2TcpPort).start();
    }

}
