package ksbysample.cmdapp.samplecmdapp

import spock.lang.Specification

class SampleCmdappApplicationTest extends Specification {

    def "sample-cmdapp を実行すると'★★★ 3'が出力される"() {
        setup:
        def buf = new ByteArrayOutputStream(1024)
        System.out = new PrintStream(buf)

        when:
        SampleCmdappApplication.main()

        then:
        buf.toString().contains("★★★ 3")
    }

}
