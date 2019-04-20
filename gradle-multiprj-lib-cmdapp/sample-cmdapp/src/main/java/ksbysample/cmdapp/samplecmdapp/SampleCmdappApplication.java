package ksbysample.cmdapp.samplecmdapp;

import ksbysample.lib.samplelib.StrNumUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleCmdappApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SampleCmdappApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("★★★ " + StrNumUtils.plus("1", "2"));
    }

}
