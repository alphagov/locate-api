(function () {
  "use strict";

  var root = this,
      $ = root.jQuery,
      GOVUK = root.GOVUK;

  $('header.no-back-link').each(function (idx, elm) {
    new GOVUK.registerToVote.BackButton(elm);
  });
  $('.optional-section, .optional-section-binary').each(function (idx, elm) {
    var toggleClass = 'optional-section',
        $elm = $(elm);
    if ($elm.data('controlText') !== undefined) {
      new GOVUK.registerToVote.OptionalControl(elm, toggleClass);
    } else {
      if ($elm.data('condition') !== undefined) {
        new GOVUK.registerToVote.ConditionalControl(elm, toggleClass);
      } else {
        new GOVUK.registerToVote.OptionalInformation(elm, toggleClass);
      }
    }
  });
  $('.selectable').each(function (idx, elm) {
    var $label = $(elm),
        $control = $label.find('input[type=radio], input[type=checkbox]'),
        controlName = $control.attr('name');

    if ($control.attr('type') === 'radio') {
      // set up event monitoring for radios sharing that name
      GOVUK.registerToVote.monitorRadios($control[0]);
    }
    new GOVUK.registerToVote.MarkSelected(elm);
    $control.on('focus', function () {
      $(this).parent('label').addClass('selectable-focus');
    });
    $control.on('blur', function () {
      $(this).parent('label').removeClass('selectable-focus');
    });
  });
  $('.country-autocomplete').each(function (idx, elm) {
    GOVUK.registerToVote.autocompletes.add($(elm));
  });

  // Custom events

  var dataType = $('input:radio[name=dataType]:checked').val();
  var queryType = $('input:radio[name=queryType]:checked').val();

  $("input:radio[name=dataType]").click(function() {
    dataType = $(this).val();
  });

  $("input:radio[name=queryType]").click(function() {
    queryType = $(this).val();
  });

    $('#submit').on("click", function(e) {
         e.preventDefault();

        function printSuccess(obj) {
            var html = '<hr/><div class="warning">';
            html += "This is your credential for the locate API"
            html += '<p>Note this down now as there is no way to access it again</p>';
            html += "<p><h3>" + obj.token + "</h3></p>";
            html += '</div>';
            return html;
        }

        function printError(errors) {
            var errorHtml = '<div class="warning">There have been some errors in creating your credentials';

            for(var i in errors) {
                errorHtml += "<p>" + errors[i] + "</p>";
            }
            return errorHtml + "</div>";
        }

         var request = {
            "name": $('#name').val(),
            "email": $('#email').val(),
            "organisation": $('#organisation').val(),
            "dataType": dataType,
            "queryType": queryType
        };

        $.ajax({
             url : "/locate/create-user",
             type: "POST",
             dataType : 'json',
             contentType: "application/json",
             data : JSON.stringify(request),
             timeout : 10000
           }).
           done(function (data, status, xhrObj) {
                var obj = JSON.parse(xhrObj.responseText);
                $("#results").html(printSuccess(obj));
           }).
           fail(function (xhrObj, status, errorStr) {
                var obj = JSON.parse(xhrObj.responseText);
                $("#results").html(printError(obj));
             });
     });

}.call(this));
