import org.openqa.selenium.firefox.FirefoxDriver

driver = {
    System.setProperty("webdriver.gecko.driver", "C:/geckodriver/0.19.0/geckodriver.exe")
    new FirefoxDriver()
}
baseUrl = "http://localhost:8080"
waiting {
    timeout = 15
}
