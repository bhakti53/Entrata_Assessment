package TestNG;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import static org.testng.Assert.*;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Validation {
	// Driver initialization
	static WebDriver driver = new ChromeDriver();
	String entrataLogoText = "Entrata Home Page";

	@BeforeTest
	public void setup() {
		// navigate to Entrata site
		WebDriverManager.chromedriver().setup();
		driver.navigate().to("https://www.entrata.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	// 1. validate title
	@Test(priority = 1)
	public void titleValidation() {
		String expectedTitle = "Property Management Software | Entrata";
		String actualTitle = driver.getTitle();
		System.out.println(actualTitle);
		assertEquals(actualTitle, expectedTitle);
		System.out.println("title is matching with actual title");
	}

	// 2.hover on Product and validate its showing tabs
	@Test(priority = 2)
	public void validateMousehover() {
		WebElement product = driver.findElement(By.xpath("//div[contains(text(), 'Products')]"));

		Actions action = new Actions(driver);
		action.moveToElement(product).build().perform();
		WebElement property = driver.findElement(By.xpath("//a[@href='/products/property-management']"));

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		action.moveToElement(property).build().perform();
	}

	// 3.Validate color change when hovering on property management label
	@Test(priority = 3)
	public void ColorValidation() {
		WebElement property = driver.findElement(By.xpath("//a[@href='/products/property-management']"));
		String s = property.getCssValue("color");
		assertEquals("rgba(228, 33, 39, 1)", s);

		System.out.println("color is Red and value is: " + s);
	}

	// 4.Entrata logo hover (it should show tooltiptext)
	@Test(priority = 4)
	public void toolTipTextValidation() {
		String toolTipText = driver.findElement(By.xpath("//a[@title='Entrata Home Page']")).getAttribute("title");

		assertEquals(toolTipText, entrataLogoText);
		System.out.println("Mousehover title validation is successful");
	}

	// 5.Page navigation
	@Test(priority = 5)
	public void pageNavigation() throws StaleElementReferenceException {
		driver.findElement(By.xpath("//a[@href='/sign-in']")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement resident = driver.findElement(By.cssSelector(
				"#gatsby-focus-wrapper > div > div.page-content > div > div > div > div.product-text-align > div > div.button-holder > a.cta-link-default.dark-cta-link.banner-link"));
		js.executeScript("arguments[0].click();", resident);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.findElement(By.xpath("//div[@class='start-button website-button']")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

	}

	// 6.Validate form interactions

	@Test(priority = 6)
	public void formValidation() {
		JavascriptExecutor js = (JavascriptExecutor) driver; // Scroll down till the bottom of the page
		js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
		driver.findElement(By.id("name")).sendKeys("entrata");
		driver.findElement(By.id("contact-submit")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		String expectedError = "A valid Email address is required.";
		String errorMessage = driver.findElement(By.xpath("//p[contains(text(),'A valid Email address is required')]"))
				.getText();
		assertEquals(errorMessage, expectedError);
		System.out.println("Displaying error message when submitting without filling required fields");

	}

	@AfterTest
	public void afterTest() {
		driver.quit();
	}

}
