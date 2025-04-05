package TestCases;

import java.time.Duration;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import Locators.PageObject;
import Utils.HelperClass;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;

public class SearchValidation {

	private PageObject page;
	private Wait<WebDriver> wait;

	public SearchValidation() {
		this.page = new PageObject();
		PageFactory.initElements(HelperClass.getDriver(), page);
	}
	SoftAssert softAssert = new SoftAssert();
	
	@When("Verify whether the user has been on the HomePage")
	public void verify_whether_the_user_has_been_on_the_home_page() throws InterruptedException {

		try {
			wait = new FluentWait<>(HelperClass.getDriver()).withTimeout(Duration.ofSeconds(HelperClass.TIMEOUT))
					.pollingEvery(Duration.ofMillis(HelperClass.PollingTime)).ignoring(NoSuchElementException.class);
		
			String CurrentPage = HelperClass.getDriver().getTitle();
			System.out.println(CurrentPage);
			Assert.assertEquals(CurrentPage, HelperClass.properties.getProperty("CurrentPageTitle"));

			wait.until(ExpectedConditions.elementToBeClickable(PageObject.CloseIcon)).click();
		} catch (Exception e) {
			HelperClass.handleException(e);
		}

	}

	@And("Verify if the user can click on the search box, enter any Tool or Equipment category, and ensure that suggestions are displayed")
	public void verify_if_the_user_can_click_on_the_search_box_enter_any_tool_or_equipment_category_and_ensure_that_suggestions_are_displayed() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(PageObject.SearchBox)).sendKeys(HelperClass.properties.getProperty("SearchInput"));
			WebElement SearchSuggestion = wait.until(ExpectedConditions.elementToBeClickable(PageObject.SearchSuggestion));
			Assert.assertTrue(SearchSuggestion.isDisplayed());
		} catch (Exception e) {
			HelperClass.handleException(e);
		}

	}

	@And("Verify if the user can click on a search suggestion and ensure that the corresponding results are displayed")
	public void verify_if_the_user_can_click_on_a_search_suggestion_and_ensure_that_the_corresponding_results_are_displayed() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(PageObject.SearchSuggestion)).click();
		} catch (Exception e) {
			HelperClass.handleException(e);
		}
	}

	@And("Verify if the user can open the search result and ensure that the same product is displayed on the Product Details Page")
	public void verify_if_the_user_can_open_the_search_result_and_ensure_that_the_same_product_is_displayed_on_the_product_details_page() {
		try {
			String ResultValidation = wait.until(ExpectedConditions.elementToBeClickable(PageObject.SearchResultValidation)).getText();
		
			Assert.assertEquals(ResultValidation, HelperClass.properties.getProperty("SearchResult"));

		} catch (Exception e) {
			HelperClass.handleException(e);
		}
	}

	@And("Verify if the user fails the validation and the error scenarios are shown in the report")
	public void verify_if_the_user_fails_the_validation_and_the_error_scenarios_are_shown_in_the_report() {
		try {
			
			String ResultValidation = wait.until(ExpectedConditions.elementToBeClickable(PageObject.SearchResultValidation)).getText();
			softAssert.assertEquals(ResultValidation, HelperClass.properties.getProperty("InvalidResult"));
			softAssert.assertAll();

		} catch (Exception e) {
			HelperClass.handleException(e);
		}
	}

}
