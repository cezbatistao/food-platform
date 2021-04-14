import { Constants } from '../config/constants';

export class BasePage {

  pageFactor: Record<string, string>;

  constructor() {
    this.pageFactor = {
      boxError: '.notification-message'
    };
  }

  openUrl(page: string) {
    cy.server();
		cy.viewport(1200, 600);
		cy.visit(page);
  }

  verifyUrl(url: string) {
		cy.url({ timeout: Constants.Timeout.url }).should('include', url);
	}

	reloadPage() {
		cy.reload({timeout: Constants.Timeout.reloadPage})
	}

	returnDataTableObject(dataTable: any) {
		const forLength = dataTable.rawTable.length || 0;
		const data: any = [];
		let i: number;
		for (i = 1; i < forLength; i++) {
			const item: any = {};
			dataTable.rawTable[0].forEach((value: string, index: number) => {
				item[value] = dataTable.rawTable[i][index];
			});
			data.push(item);
		}
		cy.log(data);
		return data;
	}

	validateGenericMock(endpoint: string, statusCode: string) {
    cy.route({
      method: 'GET',
      url: statusCode,
    }).as('genericMock');
    
    cy.wait('@genericMock', { timeout: Constants.Timeout.stubby }).its('status').should('eq', parseInt(statusCode));
  }
};