package outcomes;

import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class FatalErrorAddInput3 extends BaseStageTest<String> {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1");
        System.out.println(scanner.nextLine());
    }

    public FatalErrorAddInput3() {
        super(FatalErrorAddInput3.class);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void before() {
        exception.expect(AssertionError.class);
        exception.expectMessage("Fatal error in test #4, please send the report to Hyperskill team.");
        exception.expectMessage("java.lang.Exception: " +
            "Dynamic input should return String or CheckResult objects only. Found: class java.lang.Integer");
    }

    @Override
    public List<TestCase<String>> generate() {
        return Arrays.asList(
            new TestCase<String>()
                .addInput(out -> {
                    return "12";
                })
                .setAttach("1\n12\n"),

            new TestCase<String>()
                .addInput(out -> {
                    return CheckResult.TRUE("34");
                })
                .setAttach("1\n34\n"),

            new TestCase<String>()
                .addInput(out -> {
                    return new CheckResult(out.equals("1\n"), "56");
                })
                .setAttach("1\n56\n"),

            new TestCase<String>()
                .addInput(out -> {
                    return 78;
                })
                .setAttach("1\n78\n")
        );
    }

    @Override
    public CheckResult check(String reply, String attach) {
        return new CheckResult(reply.equals(attach));
    }
}
