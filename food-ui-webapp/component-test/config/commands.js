import addContext from 'mochawesome/addContext';

Cypress.Commands.add("setResolution", (size) => {
  if (Cypress._.isArray(size)) {
    cy.viewport(size[0], size[1]);
  } else {
    cy.viewport(size);
  }
});

Cypress.on('uncaught:exception', (err, runnable) => {
	// returning false here prevents Cypress from
	// failing the test
	return false;
});

Cypress.on('test:after:run', (test, runnable) => {
	if (test.state == 'failed') {
    // cy.wait(500);
    const searchRegExp = /\s/g;
    const replaceWith = '_';

    const specName = Cypress.spec.name.replace(':', '').replace('\\', '/');
    const contextName = runnable.parent.title.replace(':', '').replace('\\', '/');
    const specifyName = test.title.replace(':', '').replace('/', '');
    const screenshotFileName = `${contextName} -- ${specifyName} (failed).png`
      .replace('#', replaceWith)
      .replace('@', replaceWith)
      .replace(searchRegExp, replaceWith);

    addContext({ test }, `screenshots/${specName}/${screenshotFileName}`);
	}
});

Cypress.on('window:before:load', win => {
	fetch('https://unpkg.com/unfetch/dist/unfetch.umd.js')
    .then(stream => stream.text())
	  .then(response => {
      win.eval(response)
		  win.fetch = win.unfetch
	  })
  });