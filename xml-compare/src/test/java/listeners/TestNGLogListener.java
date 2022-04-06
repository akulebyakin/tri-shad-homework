package listeners;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import xml.compare.utils.MemoryHelper;

@Log4j2
public class TestNGLogListener implements ITestListener {

    private static final Logger errorFileLog = LogManager.getLogger("ErrorFile");

    @Override
    public void onStart(ITestContext context) {
        log.info("Start test suite: {}", context.getOutputDirectory());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("=====>>>>> TEST STARTED: {}", result.getName());
        MemoryHelper.logAllMemory();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("<<<<<===== TEST FINISHED SUCCESSFULLY: {}", result.getName());
        MemoryHelper.logAllMemory();
    }

    @Override
    public void onTestFailure(ITestResult result) {
//        log.info("<<<<<===== TEST FAILED: {}", result.getName());
        errorFileLog.error("<<<<<===== TEST FAILED: {}", result.getName(), result.getThrowable());
        MemoryHelper.logAllMemory();
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Test suite finished: {}", context.getOutputDirectory());
        MemoryHelper.logAllMemory();
    }

}
