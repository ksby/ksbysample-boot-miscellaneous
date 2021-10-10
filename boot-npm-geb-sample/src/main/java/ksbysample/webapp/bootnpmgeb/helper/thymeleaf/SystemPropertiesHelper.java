package ksbysample.webapp.bootnpmgeb.helper.thymeleaf;

import org.springframework.boot.system.SystemProperties;
import org.springframework.stereotype.Component;

/**
 * System Property 値取得用 Helper クラス
 */
@Component("sph")
public class SystemPropertiesHelper {

    /**
     * System#getProperty を呼び出して指定された system property の値を取得する
     *
     * @param properties 値を取得する system property 名
     * @return 取得した system property の値
     */
    public String getProperty(String... properties) {
        return SystemProperties.get(properties);
    }

}
