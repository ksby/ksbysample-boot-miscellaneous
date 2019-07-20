package ksbysample.cmdapp.subcmd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.IFactory;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;

@SpringBootApplication
public class Application implements CommandLineRunner, ExitCodeGenerator {

    private int exitCode;

    private final CalCommand calCommand;

    private final IFactory factory;

    public Application(CalCommand calCommand
            , IFactory factory) {
        this.calCommand = calCommand;
        this.factory = factory;
    }

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(Application.class, args)));
    }

    @Override
    public void run(String... args) {
        CommandLine commandLine = new CommandLine(calCommand, factory);

        // subcommand が指定されていない場合にはエラーメッセージと usage を表示する
        try {
            ParseResult parsed = commandLine.parseArgs(args);
            if (parsed.subcommand() == null &&
                    !parsed.isUsageHelpRequested() &&
                    !parsed.isVersionHelpRequested()) {
                System.err.println("Error: at least 1 command and 1 subcommand found.");
                commandLine.usage(System.out);
                exitCode = ExitCode.USAGE;
                return;
            }
        } catch (ParameterException ignored) {
            // CommandLine#parseArgs で ParameterException が throw されても
            // CommandLine#execute を実行しないと subcommand の usage が表示されないので
            // ここでは何もしない
        }

        exitCode = commandLine
                .setExitCodeExceptionMapper(calCommand)
                .execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

}
