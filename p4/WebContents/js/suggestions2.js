
/**
 * Provides suggestions using Google Search proxy
 * @class
 * @scope public
 */
function SearchSuggestions() {}

/**
 * Request suggestions for the given autosuggest control.
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
SearchSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
    var aSuggestions = [];
    var sTextboxValue = oAutoSuggestControl.textbox.value;

    // do AJAX magic here
    var ajaxExecute = function (xhr) {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var xml = xhr.responseXML;

                var hits = xml.getElementsByTagName("suggestion");
                for (var i = 0, j = hits.length; i < j; ++i) {
                    var hitData = hits[i].getAttribute("data");
                    if (hitData.indexOf(sTextboxValue) == 0) {
                        aSuggestions.push(hitData);
                    }
                }
            }

            //provide suggestions to the control
            oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
        }
    };

    // create XMLHttpRequest object to access our Google Suggest proxy
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/eBay/suggest", true);
    xhr.onreadystatechange = function (evt) {ajaxExecute(xhr);};
    xhr.overrideMimeType("text/xml");
    xhr.send(null);
};