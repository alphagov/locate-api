// Scripts to ensure requests made to the application from pressing the back button.
(function () {
  "use strict";

  var controls,
      idx;

  // 1. Make browsers that ignore no-cache headers (and still use bfcache) to not use bfcache
  if (typeof window.addEventListener !== 'undefined') {
    window.addEventListener('unload', function(){}, false);
  }

  // 2. Force browsers to honour checked="checked" attributes on radios/checkboxes for fresh-page
  //    requests coming from pressing the back button
  if (typeof document.querySelectorAll !== 'undefined') {
    controls = document.querySelectorAll('input[type="radio"], input[type="checkbox"]');
    idx = controls.length;
    while (idx--) {
      var checked = controls[idx].getAttribute('checked');
      if (checked !== null && checked !== "") {
        controls[idx].checked = 'checked';
      }
    }
  }
}.call(this));
