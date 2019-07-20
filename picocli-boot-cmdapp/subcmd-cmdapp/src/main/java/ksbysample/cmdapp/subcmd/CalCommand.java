package ksbysample.cmdapp.subcmd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import picocli.CommandLine.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;

@Component
@Command(name = "calc", mixinStandardHelpOptions = true,
        versionProvider = CalCommand.class,
        description = "渡された数値の加算・乗算を行うツール",
        subcommands = {
                CalCommand.AddCommand.class,
                CalCommand.MultiCommand.class
        })
public class CalCommand implements Callable<Integer>, IExitCodeExceptionMapper, IVersionProvider {

    // picocli.AutoComplete で generate Completion Script をする時に引数なしのコンストラクタが必要になる
    // のでコンストラクタインジェクションは使用しないこと
    // https://picocli.info/autocomplete.html 参照
    @Autowired
    private BuildProperties buildProperties;

    @Override
    public Integer call() {
        return ExitCode.OK;
    }

    @Override
    public int getExitCode(Throwable exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof NumberFormatException) {
            // 数値パラメータに数値以外の文字が指定された
            return 12;
        }

        return 11;
    }

    @Override
    public String[] getVersion() {
        return new String[]{buildProperties.getVersion()};
    }

    @Component
    @Command(name = "add", mixinStandardHelpOptions = true,
            versionProvider = CalCommand.class,
            description = "渡された数値を加算する")
    static class AddCommand implements Callable<Integer> {

        @Option(names = {"-a", "--avg"}, description = "平均値を算出する")
        private boolean optAvg;

        @Parameters(paramLabel = "数値", arity = "1..*", description = "加算する数値")
        private BigDecimal[] nums;

        @Override
        public Integer call() {
            BigDecimal sum =
                    Arrays.asList(nums).stream()
                            .reduce(new BigDecimal("0"), (a, v) -> a.add(v));
            Optional<BigDecimal> avg = optAvg
                    ? Optional.of(sum.divide(BigDecimal.valueOf(nums.length)))
                    : Optional.empty();
            System.out.println(avg.orElse(sum));
            return ExitCode.OK;
        }

    }

    @Component
    @Command(name = "multi", mixinStandardHelpOptions = true,
            versionProvider = CalCommand.class,
            description = "渡された数値を乗算する")
    static class MultiCommand implements Callable<Integer> {

        @Parameters(paramLabel = "数値", arity = "1..*", description = "乗算する数値")
        private BigDecimal[] nums;

        @Option(names = {"-c", "--compare"},
                description = "計算結果と比較して、計算結果 < 数値なら -1、計算結果 = 数値なら 0、計算結果 > 数値なら 1 を返す")
        private BigDecimal compareNum;

        @Override
        public Integer call() {
            BigDecimal result =
                    Arrays.asList(nums).stream()
                            .reduce(new BigDecimal("1"), (a, v) -> a.multiply(v));
            Optional<Integer> compareResult = (compareNum == null)
                    ? Optional.empty()
                    : Optional.of(result.compareTo(compareNum));
            System.out.println(compareResult.isPresent() ? compareResult.get() : result);
            return ExitCode.OK;
        }

    }

}
