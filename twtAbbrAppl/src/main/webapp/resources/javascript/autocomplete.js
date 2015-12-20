/*
Code based on: http://www.webreference.com/programming/javascript/gr/column5/index.html
Modified by Peter Bryant (for RimuHosting.com and Pingability.com)
*/

function AutoComplete(oText, oDiv, nMaxSize, fLoad){
	// initialize member variables
	this.oText = oText; // the text box
	this.oDiv = oDiv; // a hidden <div> for the popup auto-complete
	this.nMaxSize = nMaxSize;
	this.fLoad = fLoad;
	// attach handlers to the text-box
	this.oText.AutoComplete = this;
	this.oText.onkeyup = function() {this.AutoComplete.onchange();};
//	this.oText.onblur = function() {this.AutoComplete.onBlur();};
}

AutoComplete.prototype.onchange = function() {
	var txt = this.oText.value;
	// invoke the loader function
	this.fLoad(txt);
}

AutoComplete.prototype.repopulate = function(aStr) {
	// count the number of strings that match the text-box value.
	var nCount = aStr.length;
	
	// if a suitable number then show the popup-div
	if(this.nMaxSize >0 && nCount>this.nMaxSize || nCount==0) {
		this.oDiv.innerHTML = "";
		this.oDiv.style.visibility = "hidden";
		return;
	}
	
	// clear the popup div.
	while ( this.oDiv.hasChildNodes() ) {
		this.oDiv.removeChild(this.oDiv.firstChild);
	}
	// add each string to the popup-div
	var i, n = aStr.length;
	for ( i = 0; i < n; i++ ) {
		var oDiv = document.createElement('div');
		oDiv.className = "acNoHighlight";
		this.oDiv.appendChild(oDiv);
		oDiv.innerHTML = aStr[i];
		oDiv.onmousedown = AutoComplete.prototype.onDivMouseDown;
		oDiv.onmouseover = AutoComplete.prototype.onDivMouseOver;
		oDiv.onmouseout = AutoComplete.prototype.onDivMouseOut;
		oDiv.AutoComplete = this;
	}
	this.oDiv.style.overflow="auto";
	this.oDiv.style.visibility = "visible";
}

AutoComplete.prototype.onBlur = function () {
	this.oDiv.style.visibility = "hidden";
}

AutoComplete.prototype.onDivMouseDown = function() {
	// set the text-box value to the word
	this.AutoComplete.oText.value = this.innerHTML;
	this.AutoComplete.oDiv.style.visibility = "hidden";
}

AutoComplete.prototype.onDivMouseOver = function() {
	// assumes the existence of a CSS style called AutoCompleteHighlight
	this.className = "acHighlighted";
}

AutoComplete.prototype.onDivMouseOut = function() {
	// assumes the existence of a CSS style called AutoCompleteBackground
	this.className = "acNoHighlight";
}