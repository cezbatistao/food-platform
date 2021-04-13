import { Then } from "cypress-cucumber-preprocessor/steps";
const basePage = require('../page_objects/base.page');

Then('I access application', () => {
	basePage.openUrl("/");
});

Then('I redirect to {string}', (url) => {
	basePage.verifyUrl(url);
});

Then('reload the page', () => {
	basePage.reloadPage();
});

Then('stay on page {string}', (url) => {
	basePage.verifyUrl(url);
});

Then('have a message error with content {string}', (mensagemDeErro) => {
	basePage.verifyBoxError(mensagemDeErro);
});

Then('{string}', (genericStep) => {
  cy.log("Generic step:", genericStep)
});