import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

driver = {
    System.setProperty("webdriver.gecko.driver", "C:/geckodriver/0.19.0/geckodriver.bat")
    FirefoxOptions firefoxOptions = new FirefoxOptions()
    firefoxOptions.setHeadless(true)
    new FirefoxDriver(firefoxOptions)
}
baseUrl = "http://localhost:8080"
waiting {
    timeout = 15
}
