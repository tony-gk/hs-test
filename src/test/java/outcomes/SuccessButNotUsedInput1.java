package outcomes;

import org.hyperskill.hstest.v6.stage.BaseStageTest;
import org.hyperskill.hstest.v6.testcase.CheckResult;
import org.hyperskill.hstest.v6.testcase.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SuccessButNotUsedInput1 extends BaseStageTest<String> {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String firstLine = scanner.nextLine().trim();
        int firstInt = Integer.parseInt(firstLine);
        System.out.println(firstInt);
        if (firstInt != 1) {
            String secondLine = scanner.nextLine().trim();
            System.out.println(secondLine);
        }
    }

    public SuccessButNotUsedInput1() {
        super(SuccessButNotUsedInput1.class);
    }

    @Override
    public List<TestCase<String>> generate() {
        return Arrays.asList(
            new TestCase<String>()
                .setInput("1\nnotnum\n")
                .setAttach("1\n"),

            new TestCase<String>()
                .setInput("2\nnotnum\n")
                .setAttach("2\nnotnum\n")
        );
    }

    @Override
    public CheckResult check(String reply, String attach) {
        return new CheckResult(reply.equals(attach));
    }
}
