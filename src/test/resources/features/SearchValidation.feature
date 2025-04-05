@AnimationValidation
Feature: End To End Validation of the functionality of the ShopHighLineWarren site

@SearchValidation
Scenario: Validating the Search functionality
    When Verify whether the user has been on the HomePage
    And Verify if the user can click on the search box, enter any Tool or Equipment category, and ensure that suggestions are displayed
    And Verify if the user can click on a search suggestion and ensure that the corresponding results are displayed
    And Verify if the user can open the search result and ensure that the same product is displayed on the Product Details Page