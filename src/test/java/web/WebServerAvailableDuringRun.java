package web;

import org.hyperskill.hstest.v5.common.Utils;
import org.hyperskill.hstest.v5.mocks.web.WebPage;
import org.hyperskill.hstest.v5.mocks.web.WebServerMock;
import org.hyperskill.hstest.v5.stage.BaseStageTest;
import org.hyperskill.hstest.v5.testcase.CheckResult;
import org.hyperskill.hstest.v5.testcase.TestCase;

import java.util.Arrays;
import java.util.List;

public class WebServerAvailableDuringRun extends BaseStageTest<String> {

    public static void main(String[] args) {
        System.out.println(Utils.getUrlPage("http://127.0.0.1:45678/123"));
    }

    public WebServerAvailableDuringRun() {
        super(WebServerAvailableDuringRun.class);
    }

    @Override
    public List<TestCase<String>> generate() {
        return Arrays.asList(
            new TestCase<String>()
                .setAttach("test web server")
                .runWith(new WebServerMock(45678)
                    .setPage("/123", new WebPage().setContent("test web server")))
        );
    }

    @Override
    public CheckResult check(String reply, String clue) {
        return new CheckResult(reply.trim().equals(clue.trim()));
    }

}
