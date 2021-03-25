//import { Then } from "cypress-cucumber-preprocessor/steps";
declare const Given, When, Then;
import { BasePage } from '../page_objects/base.page';

const basePage: BasePage = new BasePage();

When('I access application', () => {
	basePage.openUrl("/");
});

When('I redirect to {string}', (url: string) => {
	basePage.verifyUrl(url);
});

When('reload the page', () => {
	basePage.reloadPage();
});

When('stay on page {string}', (url: string) => {
	basePage.verifyUrl(url);
});

When('{string}', (genericStep: any) => {
  cy.log("Generic step:", genericStep)
});