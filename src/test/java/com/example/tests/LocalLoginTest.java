
package com.example.tests;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LocalLoginTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        // Run Chrome in headless mode
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        // Use Chromium when running inside Docker
        String chromeBinary = System.getenv("CHROME_BIN");

        if (chromeBinary != null) {
            options.setBinary(chromeBinary);
        }

        // Use the ChromeDriver installed inside Docker
        String driverPath = System.getenv("CHROMEDRIVER_PATH");

        if (driverPath != null) {
            System.setProperty("webdriver.chrome.driver", driverPath);
        }

        driver = new ChromeDriver(options);

        // Open the local login webpage
        String page = new File("webpage/login.html")
                .getAbsoluteFile()
                .toURI()
                .toString();

        driver.get(page);
    }

    // Test 1: Valid Login
    @Test
    public void validLoginTest() {

        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("12345");

        driver.findElement(By.id("loginButton")).click();

        String message = driver.findElement(By.id("message")).getText();

        System.out.println("Valid Login Message: " + message);

        Assert.assertEquals(message, "Login Successful");
    }

    // Test 2: Invalid Login
    @Test
    public void invalidLoginTest() {

        driver.findElement(By.id("username")).sendKeys("wrong");
        driver.findElement(By.id("password")).sendKeys("wrong");

        driver.findElement(By.id("loginButton")).click();

        String message = driver.findElement(By.id("message")).getText();

        System.out.println("Invalid Login Message: " + message);

        Assert.assertEquals(message, "Invalid Username or Password");
    }

    // Test 3: Check Background Color
    @Test
    public void checkBackgroundColor() {

        String bgColor = driver.findElement(By.id("loginBox"))
                .getCssValue("background-color");

        System.out.println("Background Color: " + bgColor);

        Assert.assertEquals(bgColor, "rgba(173, 216, 230, 1)");
    }

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
