console.log(`{"elements": [`);
document.querySelectorAll('[data-test]')
 .forEach(function(item){
	  console.log(`{"data": "${item.getAttribute("data-test")}","value": "${item.textContent}" },`);
 })
 console.log(`]}`);