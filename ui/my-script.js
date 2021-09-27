function myFunction(){
// api url
const api_url =
	"http://localhost:8090/v1/get-all";

// Defining async function
async function getapi(url) {

	// Storing response
	const response = await fetch(url,{
                                       method: 'GET',
                                       headers: { 'Content-Type': 'application/json',
                                       'Origin': 'localhost',
                                       'Access-Control-Request-Method' : 'GET',
                                       'Access-Control-Request-Headers' : 'X-Custom-Header'}
                                     });

	// Storing data in form of JSON
	var data = await response.json();
	console.log(data);
	show(data);
}
// Calling that async function
getapi(api_url);

// Function to hide the loader
function hideloader() {
	document.getElementById('loading').style.display = 'none';
}
// Function to define innerHTML for HTML table
function show(data) {
	let tab =
		`<tr>

		<th>First Name</th>
		<th>Last Name</th>
		<th>Country</th>
		</tr>`;

	// Loop to access all rows
	//console(data.list);
	var arrayLength = data.length;

	for (var i = 0; i < arrayLength; i++){
	     console.log(data[i]);
	     var r = data[i];
		tab += `<tr>

	<td>${r.firstName} </td>
	<td>${r.lastName}</td>
	<td>${r.country}</td>
</tr>`;

	}
	// Setting innerHTML as tab variable
	document.getElementById("employees").innerHTML = tab;
}
}


function saveData2(){
 var firstna = document.getElementById("fname").value;
 console.log(firstna);
  var lastna = document.getElementById("lname").value;
  console.log(lastna);
   var countr = document.getElementById("country").value;
   console.log(countr);

 var xhttp = new XMLHttpRequest();
    xhttp.open("POST", "http://localhost:8090/v1/save-user",true);
    xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    var input = JSON.stringify({
          "firstName": firstna,
          "lastName": lastna,
          "country": countr
        });
        xhttp.send(input);
}