package tests;

import listeners.TestNGLogListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import xml.compare.configuration.AppConfig;

@Listeners(TestNGLogListener.class)
@ContextConfiguration(classes = {AppConfig.class})
public abstract class AbstractTest extends AbstractTestNGSpringContextTests {

    @Override
    @BeforeSuite
    protected void springTestContextPrepareTestInstance() throws Exception {
        super.springTestContextPrepareTestInstance();
    }

}
