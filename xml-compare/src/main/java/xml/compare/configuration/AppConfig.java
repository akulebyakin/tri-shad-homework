package xml.compare.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"xml.compare"}, lazyInit = true)
@PropertySource("classpath:${TEST_PROPERTIES_FILE:test.properties}")
public class AppConfig {

}
