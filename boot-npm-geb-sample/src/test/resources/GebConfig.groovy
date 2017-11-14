import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.htmlunit.HtmlUnitDriver

System.setProperty("webdriver.gecko.driver", "C:/geckodriver/0.19.0/geckodriver.bat")
System.setProperty("webdriver.chrome.driver", "C:/chromedriver/2.33/chromedriver.exe")
driver = {
//    FirefoxOptions firefoxOptions = new FirefoxOptions()
//    firefoxOptions.setHeadless(true)
//    new FirefoxDriver(firefoxOptions)
    new HtmlUnitDriver(true)
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

    htmlunit {
        new HtmlUnitDriver(true)
    }

}
