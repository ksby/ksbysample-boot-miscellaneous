package ksbysample.app.samplecmdapp;

import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvRoutines;
import ksbysample.lib.doma2lib.dao.EmployeeDao;
import ksbysample.lib.doma2lib.entity.Employee;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@ConditionalOnProperty(value = {"batch.execute"}, havingValue = "EmployeeDataCsvToDbLoader")
public class EmployeeDataCsvToDbLoader implements CommandLineRunner {

    @Option(name = "-csvfile", metaVar = "<path>", usage = "specifies a path to employee csv file")
    private String csvfile;

    private final EmployeeDao employeeDao;

    private final ModelMapper modelMapper;

    public EmployeeDataCsvToDbLoader(EmployeeDao employeeDao
            , ModelMapper modelMapper) {
        this.employeeDao = employeeDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // コマンドラインオプションを解析して @Option アノテーションを付加しているフィールドに値を設定する
        CmdLineParser cmdLineParser = new CmdLineParser(this);
        cmdLineParser.parseArgument(args);

        // Univocity Parses で CSV ファイルを読み込むための準備をする
        CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\r\n");  // 改行コードは CRLF
        settings.setHeaderExtractionEnabled(true);      // ヘッダ行はスキップする
        CsvRoutines routines = new CsvRoutines(settings);

        // CSV ファイルを１行ずつ読み込み employee テーブルに insert する
        try (BufferedReader br = Files.newBufferedReader(Paths.get(this.csvfile), StandardCharsets.UTF_8)) {
            Employee employee = new Employee();
            for (EmployeeCsvRecord employeeCsvRecord : routines.iterate(EmployeeCsvRecord.class, br)) {
                modelMapper.map(employeeCsvRecord, employee);
                employeeDao.insert(employee);
            }
        }
    }

}
