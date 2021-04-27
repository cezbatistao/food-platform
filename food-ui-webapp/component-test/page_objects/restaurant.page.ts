
export class RestaurantPage {
  
  pageFactor: Record<string, string>;

  constructor() {
    this.pageFactor = {
      selectCategory: '[data-test=select_category]', 
      cardHeaderRestaurant: '[data-test=card_header_restaurant_data]', 
    };
  }

  clearInputs() {
    cy.get(this.pageFactor.selectCategory).clear();
  }

  fillCategory(category: string) {
    if (category != "") {
      cy.get(this.pageFactor.selectCategory).select(category);
    }
  }

  verify(restaurantNames: string[]) {
    cy.get(this.pageFactor.cardHeaderRestaurant)
    .should(($div) => {
      expect($div).to.have.length(restaurantNames.length);
      restaurantNames.forEach((restaurantName, index) => {
        expect($div.eq(index)).to.contain(restaurantName);
      });
    });
  }
};