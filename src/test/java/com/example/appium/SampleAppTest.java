package com.example.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static java.lang.System.getenv;
import static org.testng.Assert.assertEquals;

public class SampleAppTest {
    private AppiumDriverLocalService server;
    private AppiumDriver<MobileElement> driver;

    @BeforeClass
    private void setUp() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String platform = getenv("APPIUM_DRIVER");
        platform = platform == null ? "ANDROID" : platform.toUpperCase();
        String path = System.getProperty("user.dir");

        if (platform.equals("ANDROID")) {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator-5554");
            capabilities.setCapability(MobileCapabilityType.APP, path + "/ApiDemos-debug.apk");

            server = new AppiumServiceBuilder().usingAnyFreePort().build();
            server.start();
            driver = new AndroidDriver<>(server, capabilities);

            ((AndroidDriver<MobileElement>) driver).startActivity(new Activity("io.appium.android.apis", ".view.TextFields"));
        } else {
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "14.0.1");
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCuiTest");
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 14 Pro Max");
            capabilities.setCapability(MobileCapabilityType.UDID, "3439B9B8-C599-407F-AADC-64F81DA9A1F3");
            capabilities.setCapability(MobileCapabilityType.APP, path + "/TestApp.app.zip");

            server = new AppiumServiceBuilder().usingAnyFreePort().build();
            server.start();
            driver = new IOSDriver<>(server, capabilities);
        }
    }

    @Test
    public void textFieldTest() {
        PageView view = new PageView(driver);
        view.setTextField("test");
        Assert.assertEquals(view.getTextField(), "test", "Text filed was not set");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
        server.stop();
    }
}
