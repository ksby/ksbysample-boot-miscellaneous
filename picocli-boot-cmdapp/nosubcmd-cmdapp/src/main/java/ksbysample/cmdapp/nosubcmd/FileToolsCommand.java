package ksbysample.cmdapp.nosubcmd;

import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Callable;

@Component
@Command(name = "filetools", mixinStandardHelpOptions = true,
        version = "1.0.0",
        description = "create/delete file(s) command")
public class FileToolsCommand implements Callable<Integer>, IExitCodeExceptionMapper {

    // --create オプションと --delete オプションはいずれか一方しか指定できないようにする
    @ArgGroup(exclusive = true, multiplicity = "1")
    private Exclusive exclusive;

    static class Exclusive {

        @Option(names = {"-c", "--create"}, description = "create file(s)")
        private boolean isCreate;

        @Option(names = {"-d", "--delete"}, description = "delete file(s)")
        private boolean isDelete;

    }

    @Parameters(paramLabel = "ファイル", description = "作成あるいは削除するファイル")
    private File[] files;

    @Override
    public Integer call() {
        Arrays.asList(this.files).forEach(f -> {
            try {
                if (exclusive.isCreate) {
                    Files.createFile(Paths.get(f.getName()));
                    System.out.println(f.getName() + " is created.");
                } else if (exclusive.isDelete) {
                    Files.deleteIfExists(Paths.get(f.getName()));
                    System.out.println(f.getName() + " is deleted.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return ExitCode.OK;
    }

    @Override
    public int getExitCode(Throwable exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof FileAlreadyExistsException) {
            // 既に存在するファイルを作成しようとしている
            return 12;
        } else if (cause instanceof FileSystemException) {
            // 削除しようとしたファイルが別のプロセスでオープンされている等
            return 13;
        }
        return 11;
    }

}
