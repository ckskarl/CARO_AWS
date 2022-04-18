/*var checkbox = document.querySelector("input[name=checkbox]");
 
 checkbox.addEventListener('change', function() {
 if (this.checked) {
 console.log("Checkbox is checked..");
 } else {
 console.log("Checkbox is not checked..");
 }
 });*/



function makeChecked(el) {
    var name = el.name;

    if (name.toString().includes("date")) {
        document.getElementById("date_selection_checkbox").checked = true;
    } else if (name.toString().includes("cost")) {
        document.getElementById("cost_selection_checkbox").checked = true;
    } else {
        document.getElementById(name + "_checkbox").checked = true;
    }
}


function exportCSV(inputCSV) {
    //alert("Start exporting");
    var today = new Date();
    var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
    var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    var blob = new Blob([inputCSV],
            {type: "text/plain;charset=utf-8"});
    saveAs(blob, "Report_"+date+"_"+time+".csv");
    alert("Export Finished.");
}