const constants = require('../config/constants');

const basePage = {
	openUrl: page => {
		cy.server();
		cy.viewport(1200, 600);
		cy.visit(page);
	},
	verifyUrl: url => {
		cy.url({ timeout: constants.properties.timeout.url }).should('include', url);
	},
	reloadPage: () => {
		cy.reload({timeout: constants.properties.timeout.reloadPage})
	},
	returnDataTableObject: dataTable => {
		const forLength = dataTable.rawTable.length || 0;
		const data = [];
		let i;
		for (i = 1; i < forLength; i++) {
			const item = {};
			dataTable.rawTable[0].forEach((value, index) => {
				item[value] = dataTable.rawTable[i][index];
			});
			data.push(item);
		}
		cy.log(data);
		return data;
	},
	validateGenericMock: (endpoint, statusCode) => {
    cy.route({
      method: 'GET',
      url: statusCode,
    }).as('genericMock');
    
    cy.wait('@genericMock', { timeout: constants.properties.timeout.stubby }).its('status').should('eq', parseInt(statusCode));
  }
};

const pageFactor = {
	boxError: '.notification-message'
};

module.exports = basePage;