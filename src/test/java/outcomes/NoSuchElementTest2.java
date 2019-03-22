package outcomes;

import org.hyperskill.hstest.v3.stage.MainMethodTest;
import org.hyperskill.hstest.v3.testcase.CheckResult;
import org.hyperskill.hstest.v3.testcase.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.NoSuchElementException;

public class NoSuchElementTest2 extends MainMethodTest {

    public static void main(String[] args) {
        throw new NoSuchElementException();
    }

    public NoSuchElementTest2() throws Exception {
        super(NoSuchElementTest2.class);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        exception.expect(AssertionError.class);
        exception.expectMessage("Exception in test #1\n\n" +
            "java.util.NoSuchElementException");
    }

    @Override
    public List<TestCase> generateTestCases() {
        return List.of(
            new TestCase()
        );
    }

    @Override
    public CheckResult check(String reply, Object clue) {
        return CheckResult.FALSE;
    }
}
