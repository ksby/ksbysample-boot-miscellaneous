package ksbysample.lib.samplelib;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StrNumUtilsTest {

    @Test
    @DisplayName("plusメソッドのテスト")
    void plus() {
        assertThat(StrNumUtils.plus("1", "2")).isEqualTo("3");
    }

}
