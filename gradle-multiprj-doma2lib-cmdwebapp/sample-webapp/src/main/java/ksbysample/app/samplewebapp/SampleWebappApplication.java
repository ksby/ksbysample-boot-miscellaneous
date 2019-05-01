package ksbysample.app.samplewebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
        basePackages = {"ksbysample.lib", "ksbysample.app"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM,
                        classes = AutoConfigurationExcludeFilter.class)})
public class SampleWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleWebappApplication.class, args);
    }

}
