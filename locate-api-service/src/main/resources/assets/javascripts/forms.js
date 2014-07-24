(function () {
  "use strict"

  var root = this,
      $ = root.jQuery,
      GOVUK = root.GOVUK,
      ENTER = 13,
      MarkSelected,
      monitorRadios,
      BackButton;

  BackButton = function (elm) {
    if (elm) {
      this.$header = $(elm);
      this.setup();
      this.bindEvents();
    }
  };

  BackButton.prototype.setup = function () {
    this.$link = $(
                  '<a class="back-to-previous" href="#">' +
                    message('back_button') +
                    ' <span class="visuallyhidden"> ' +
                    message('back_button_non_visual') +
                    '</span>' +
                  '</a>'
                );
    this.$header.before(this.$link);
    this.$header.removeClass('no-back-link');
  };

  BackButton.prototype.bindEvents = function () {
    this.$link.on("click", function(e) {
      e.preventDefault();
      root.history.back();
      return false;
    });
  };


  // Constructor to allow the label wrapping radios/checkboxes to be styled to reflect their status
  // Uses this document-level event:
  //
  //  radio:*radio-name-attribute*
  //
  //  ie. radio:address.address
  MarkSelected = function (elm) {
    var _this = this,
        controlType,
        isMonitored;

    this.$label = $(elm);
    this.$control = this.$label.find('input[type=radio], input[type=checkbox]');
    controlType = this.$control.attr('type');
    if (controlType === 'radio') {
      $(document).on('radio:' + this.$control.attr('name'), function (e, data) {
        _this.toggle(data);
      });
    }
    if (controlType === 'checkbox') {
      this.$control.on('click', function () {
        _this.toggle({
          'selectedControl' : _this.$control
        });
      });
    }
    if (this.$control.is(':checked')) {
      this.$label.addClass('selected');
    }
  };

  MarkSelected.prototype.toggle = function (eventData) {
    var isChecked = this.$control.is(':checked');

    if (isChecked) {
      this.$label.addClass('selected');
    } else {
      this.$label.removeClass('selected');
    }
  };

  monitorRadios = (function () {
    var monitor;

    monitor = function (elm) {
      var groupName = elm.name,
          $fieldset = $(elm).closest('fieldset');

      if ($.inArray(groupName, monitor.radioGroups) === -1) {
        monitor.radioGroups.push(groupName);
        // older browsers can not detect change events on radio buttons attach a click also
        $fieldset.on('click change', function (e) {
          var target = e.target;
          if (target.type && target.type === 'radio') {
            $(document).trigger('radio:' + target.name,
              { 
                "selectedControl" : target,
                "fieldset" : this
              }
            );
          }
        });
      }
    };
    monitor.radioGroups = [];
    return monitor;
  }());



  GOVUK.registerToVote.MarkSelected = MarkSelected;
  GOVUK.registerToVote.monitorRadios = monitorRadios;
  GOVUK.registerToVote.BackButton = BackButton;
}.call(this));
