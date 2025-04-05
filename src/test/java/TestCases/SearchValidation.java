package TestCases;

import java.time.Duration;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.asserts.SoftAssert;

import Locators.PageObject;
import Utils.HelperClass;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

public class SearchValidation {

    private PageObject page;
    private Wait<WebDriver> wait;
    private WebDriver driver;
    private SoftAssert softAssert;

    public SearchValidation() {
        this.driver = HelperClass.getDriver();
        this.page = new PageObject();
        PageFactory.initElements(driver, page);
        this.wait = new FluentWait<>(driver)
                        .withTimeout(Duration.ofSeconds(HelperClass.TIMEOUT))
                        .pollingEvery(Duration.ofMillis(HelperClass.PollingTime))
                        .ignoring(NoSuchElementException.class);
        this.softAssert = new SoftAssert();
    }

    @When("Verify whether the user has been on the HomePage")
    public void verify_whether_the_user_has_been_on_the_home_page() throws InterruptedException {
        try {
            String currentPage = driver.getTitle();
            System.out.println("Page Title: " + currentPage);
            softAssert.assertEquals(currentPage, HelperClass.properties.getProperty("current_page_title"), "Home page title mismatch");
            
            WebElement closeIcon = wait.until(ExpectedConditions.visibilityOf(page.CloseIcon));
            wait.until(ExpectedConditions.elementToBeClickable(page.CloseIcon)).click();
           
        } catch (Exception e) {
            HelperClass.handleException(e);
        }
    }

    @And("Verify if the user can click on the search box, enter any Tool or Equipment category, and ensure that suggestions are displayed")
    public void verify_if_the_user_can_click_on_the_search_box_enter_any_tool_or_equipment_category_and_ensure_that_suggestions_are_displayed() {
        try {
            WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(page.SearchBox));
            searchBox.sendKeys(HelperClass.properties.getProperty("search_input"));
            WebElement searchSuggestion = wait.until(ExpectedConditions.visibilityOf(page.SearchSuggestion));
            softAssert.assertTrue(searchSuggestion.isDisplayed(), "Search suggestion not displayed!");
          
        } catch (Exception e) {
            HelperClass.handleException(e);
        }
    }

    @And("Verify if the user can click on a search suggestion and ensure that the corresponding results are displayed")
    public void verify_if_the_user_can_click_on_a_search_suggestion_and_ensure_that_the_corresponding_results_are_displayed() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(page.SearchSuggestion)).click();
                      
        } catch (Exception e) {
            HelperClass.handleException(e);
        }
    }

    @And("Verify if the user can open the search result and ensure that the same product is displayed on the Product Details Page")
    public void verify_if_the_user_can_open_the_search_result_and_ensure_that_the_same_product_is_displayed_on_the_product_details_page() {
        try {
            String resultValidation = wait.until(ExpectedConditions.visibilityOf(page.SearchResultValidation)).getText();
            softAssert.assertEquals(resultValidation, HelperClass.properties.getProperty("search_result"), "Search result text mismatch");
         
        } catch (Exception e) {
            HelperClass.handleException(e);
        }
    }
}