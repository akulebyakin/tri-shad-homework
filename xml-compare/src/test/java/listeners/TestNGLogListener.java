package listeners;

import lombok.extern.log4j.Log4j2;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

@Log4j2
public class TestNGLogListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        log.info("Start test suite: {}", context.getOutputDirectory());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("=====>>>>> TEST STARTED: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("<<<<<===== TEST FINISHED SUCCESSFULLY: {}", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.info("<<<<<===== TEST FAILED: {}", result.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Test suite finished: {}", context.getOutputDirectory());
    }

}
