const constants = require('../config/constants');

const proposalsPage = {
  clearInputs: () => {
    cy.get(pageFactor.selectCategory).clear();
  },
  fillCategory: (category) => {
    if (category != "") {
      cy.get(pageFactor.selectCategory).type(`${category}{enter}`);
    }
  },
  verify: (restaurantNames) => {
    cy.get(pageFactor.cardHeaderRestaurant)
      .find('div.MuiCardHeader-content')
      .should(($div) => {
        expect($div).to.have.length(restaurantNames.length);
        restaurantNames.forEach((restaurantName, index) => {
          expect($div.eq(index)).to.contain(restaurantName);
        });
      })
  }, 
};

const pageFactor = {
  selectCategory: '[data-test=select_category]', 
  cardHeaderRestaurant: '[data-test=card_header_restaurant_data]', 
};

module.exports = proposalsPage;