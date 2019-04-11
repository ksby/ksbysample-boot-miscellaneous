import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

driver = {
    FirefoxOptions firefoxOptions = new FirefoxOptions()
    firefoxOptions.setHeadless(true)
    new FirefoxDriver(firefoxOptions)
}
baseUrl = "http://localhost:8080"
waiting {
    timeout = 15
}

environments {

    chrome {
        driver = {
            ChromeOptions chromeOptions = new ChromeOptions()
            chromeOptions.setHeadless(true)
            new ChromeDriver(chromeOptions)
        }
    }

    firefox {
        driver = {
            FirefoxOptions firefoxOptions = new FirefoxOptions()
            firefoxOptions.setHeadless(true)
            new FirefoxDriver(firefoxOptions)
        }
    }

}
