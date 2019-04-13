import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

//System.setProperty("webdriver.gecko.driver", "D:/geckodriver/0.24.0/geckodriver.bat")
//System.setProperty("webdriver.chrome.driver", "D:/chromedriver/73.0.3683.68/chromedriver.exe")
driver = {
    ChromeOptions chromeOptions = new ChromeOptions()
    new ChromeDriver(chromeOptions)
//    FirefoxOptions firefoxOptions = new FirefoxOptions()
//    new FirefoxDriver(firefoxOptions)
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
