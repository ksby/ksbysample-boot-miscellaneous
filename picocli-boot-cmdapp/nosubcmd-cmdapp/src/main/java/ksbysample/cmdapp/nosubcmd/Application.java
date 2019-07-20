package ksbysample.cmdapp.nosubcmd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
public class Application implements CommandLineRunner, ExitCodeGenerator {

    private int exitCode;

    private final FileToolsCommand fileToolsCommand;

    private final IFactory factory;

    public Application(FileToolsCommand fileToolsCommand,
                       IFactory factory) {
        this.fileToolsCommand = fileToolsCommand;
        this.factory = factory;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(Application.class, args)));
    }

    @Override
    public void run(String... args) {
        exitCode = new CommandLine(fileToolsCommand, factory)
                .setExitCodeExceptionMapper(fileToolsCommand)
                .execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
