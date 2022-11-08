package shpp.mentor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class errJsonBuilderTest {

    @Test
    void errLogPrep() {
        String actual = errJsonBuilder.errLogPrep("{errors=[:");
        String exspected = "{errors=[]}";
        Assertions.assertEquals(actual,exspected);
    }
}