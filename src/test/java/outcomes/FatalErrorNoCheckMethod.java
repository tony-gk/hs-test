package outcomes;

import org.hyperskill.hstest.v7.stage.BaseStageTest;
import org.hyperskill.hstest.v7.testcase.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;


class FatalErrorNoCheckMethodMain {
    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}

public class FatalErrorNoCheckMethod extends BaseStageTest {

    public FatalErrorNoCheckMethod() {
        super(FatalErrorNoCheckMethodMain.class);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        exception.expect(AssertionError.class);
        exception.expectMessage("Fatal error during testing, please send the report to Hyperskill team.");
        exception.expectMessage("Can't check result: override \"check\" method");
    }

    @Override
    public List<TestCase> generate() {
        return Arrays.asList(
            new TestCase()
        );
    }
}
