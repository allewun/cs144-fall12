// CS 144 Project 4

String.prototype.trim = function () {
	return this.replace(/^\s+|\s+$/g, "");
};

// adapted from http://ejohn.org/projects/flexible-javascript-events/
function addEvent(id, type, fn) {
	var el = document.getElementById(id);
	if (el.addEventListener) {
		el.addEventListener(type, fn, false);
	}
	else if (el.attachEvent) {
		el["e"+type+fn] = fn;
		el[type+fn] = function() { el["e"+type+fn](window.event); };
		el.attachEvent("on" + type, el[type+fn]);
	}
}

function preventDefault(evt) {
	evt.preventDefault ? evt.preventDefault() : evt.returnValue = false;
}

function validateItemId(evt) {
	var value = document.getElementById("itemId").value;

	// if field contains non-numbers or is empty, then don't submit
	if (value.trim().search(/\D/g) != -1 || value.trim() === "") {
		alert("The Item ID must be a positive integer.");
		preventDefault(evt);
	}
	else {
		document.getElementById("itemId").value = value.trim();
		return true;
	}
}
