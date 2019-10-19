package outcomes;

import org.hyperskill.hstest.v7.stage.BaseStageTest;
import org.hyperskill.hstest.v7.testcase.CheckResult;
import org.hyperskill.hstest.v7.testcase.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


class TestCurrTestNumMain {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int test = s.nextInt();
        System.out.print(test);
    }
}

public class TestCurrTestNum extends BaseStageTest<Integer> {

    public TestCurrTestNum() {
        super(TestCurrTestNumMain.class);
    }

    @Override
    public List<TestCase<Integer>> generate() {
        return Arrays.asList(
            new TestCase<Integer>()
                .setInput("1")
                .setAttach(1),

            new TestCase<Integer>()
                .setInput("2")
                .setAttach(2)
        );
    }

    @Override
    public CheckResult check(String reply, Integer attach) {
        int testNum = BaseStageTest.getCurrTestRun().getTestNum();
        if (reply.equals("1") && attach == 1
            || reply.equals("2") && attach == 2) {
            return CheckResult.TRUE;
        }
        throw new NullPointerException();
    }
}
