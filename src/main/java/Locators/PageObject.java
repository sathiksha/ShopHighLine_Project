package Locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageObject {
	
	
@FindBy(xpath = "//button[.//*[name()='title' and text()='Close dialog']]")
public static WebElement CloseIcon;

@FindBy(xpath = "(//input[@placeholder='Search'])[1]")	
public static WebElement SearchBox;

@FindBy(xpath = "//span[text()='air tool oil']")
public static WebElement SearchSuggestion;

@FindBy(xpath = "//li[text()='Search Results: AIR TOOL OIL']")
public static WebElement SearchResultValidation;
}
