const cucumber = require('cypress-cucumber-preprocessor').default;
const webpack = require('@cypress/webpack-preprocessor')
const fs = require('fs');

module.exports = (on, config) => {
  on('before:browser:launch', (browser = {}, launchOptions) => {
    if (browser.family === 'chromium' && browser.name !== 'electron') {
      launchOptions.args.push('--disable-dev-shm-usage')
    }

    return launchOptions
  }),
  on('after:screenshot', (details) => {
    //console.log("*************************************") 
    //console.log(details) // print all details to terminal
    //console.log("*************************************") 

    const searchRegExp = /\s/g;
    const replaceWith = '_';

    const newPath = details.path
      .replace('#', replaceWith)
      .replace('@', replaceWith)
      .replace(searchRegExp, replaceWith);

    return new Promise((resolve, reject) => {
      // fs.rename moves the file to the existing directory 'new/path/to'
      // and renames the image to 'screenshot.png'
      fs.rename(details.path, newPath, (err) => {
        if (err) return reject(err)

        // because we renamed and moved the image, resolve with the new path
        // so it is accurate in the test results
        //fs.chmodSync(newPath, 0755);
        resolve({ path: newPath })
      })
    })
  }),
  on('file:preprocessor', cucumber()),
  require('cypress-plugin-retries/lib/plugin')(on)
}