(function () {
  "use strict";

  var root = this,
      $ = root.jQuery,
      GOVUK = root.GOVUK,
      validation,
      message = GOVUK.registerToVote.messages;

  validation = {
    init : function () {
      var _this = this,
          $submits = $('.validation-submit');

      this.fields.init();
      $('.validate').each(function (idx, elm) {
        _this.fields.add($(elm), _this);
      });
      this.events.bind('validate', function (e, eData) {
        return _this.handler(eData.$source);
      });
      $(document)
        .on('click', '.validation-submit', function (e) {
          return _this.handler($(e.target));
        })
        .on('click', '.validation-message a', function (e) {
          return _this.goToControl($(e.target));
        });
    },
    handler : function ($source) {
      var formName = validation.getFormFromField($source).action,
          rules = [],
          rulesStr = "",
          names;

      names = $source.data('validationSources');
      if (names && names !== null) {
        names = names.split(' ');
        return this.validate(names, $source);
      }
      return true;
    },
    getFormFromField : function ($field) {
      if ($field[0].nodeName.toLowerCase() === 'form') {
        return $field[0];
      } else if (typeof $field[0].form !== 'undefined') {
        return $field[0].form;
      } else {
        return $field.closest('form')[0];
      }
    },
    fields : (function () {
      var items = [],
          _this = this,
          ItemObj = function (props) {
            var prop;

            for (prop in props) {
              if (props.hasOwnProperty(prop)) {
                this[prop] = props[prop];
              }
            };
          },
          objTypes = {
            'field' : function (props) { ItemObj.call(this, props); },
            'fieldset' : function (props) { ItemObj.call(this, props); },
            'association' : function (props) { ItemObj.call(this, props); }
          },
          _makeItemObj = function ($source, obj) {
            obj.name = $source.data('validationName');
            obj.rules = $source.data('validationRules').split(' ');

            return new objTypes[obj.type](obj);
          };

      return {
        init : function () {
          var objType,
              rules,
              rule;

          for(objType in objTypes) {
            rules = validation.rules[objType];
            for (rule in rules) {
              objTypes[objType].prototype[rule] = rules[rule];
            }
          };
        },
        addField : function ($source) {
          var itemObj = _makeItemObj($source, {
            'type' : 'field',
            '$source' : $source
          });
          items.push(itemObj);
        },
        addFieldset : function ($source) {
          var childNames = $source.data('validationChildren').split(' '),
              itemObj;

          itemObj = _makeItemObj($source, {
            'type' : 'fieldset',
            '$source' : $source,
            'children' : childNames
          });
          items.push(itemObj);
        },
        addAssociation : function ($source) {
          var memberNames = $source.data('validationMembers').split(' '),
              itemObj;

          itemObj = _makeItemObj($source, {
            'type' : 'association',
            'members' : memberNames
          });
          items.push(itemObj);
        },
        remove : function (name) {
          var result = [],
              item,
              itemObj;

          $.each(items, function(idx, itemObj) {
            if (itemObj.name !== name) {
              result.push(itemObj);
            }
          });
          items = result;
        },
        add : function ($source) {
          var type = $source.data('validationType');

          if (type !== null) {
            switch (type) {
              case 'association' :
                this.addAssociation($source);
                break;
              case 'fieldset' :
                this.addFieldset($source);
                break;
              case 'field' :
                this.addField($source);
                break;
              default :
                return;
            }
          }
        },
        getNames : function (names) {
          var result = [],
              name,
              item,
              a,b,i,j;

          for (a = 0, b = names.length; a < b; a++) {
            name = names[a];
            for (i = 0, j = items.length; i < j; i++) {
              if (items[i].name === name) {
                result.push(items[i]);
              }
            }
          }
          return result;
        },
        getAll : function () {
          return items;
        }
      };
    }()),
    applyRules : function (fieldObj) {
      var failedRules = false,
          sourcesToMark = {
            'association' : 'members',
            'fieldset' : '$source',
            'field' : '$source'
          },
          $source,
          i,j;

      $source = fieldObj[sourcesToMark[fieldObj.type]];
      // rules are applied in the order given
      for (i = 0, j = fieldObj.rules.length; i < j; i++) {
        var rule = fieldObj.rules[i];

        failedRules = fieldObj[rule]();
        if (failedRules.length) {
          break;
        }
      }
      return failedRules;
    },
    fieldsetCascade : function (name, rule) {
      var cascades = validation.cascades,
          exists = function (obj, prop) {
            return (typeof obj[prop] !== 'undefined');
          };

      if (exists(cascades, name) && exists(cascades[name], rule)) {
        return cascades[name][rule];
      }
      return false;
    },
    validate : function (names, $triggerElement) {
      var _this = this,
          invalidFields = [],
          invalidField,
          _addAnyCascades,
          fields;

      _addAnyCascades = function (field, rule, invalidFields) {
        var fieldsetCascade = validation.fieldsetCascade(field.name, rule),
            prefix = validation.rules[field.type][rule].prefix;

        if (fieldsetCascade) {
          $.merge(invalidFields, fieldsetCascade.apply(field));
        }
        return invalidFields;
      };
      fields = this.fields.getNames(names);
      $.each(fields, function (idx, field) {
        var failedRules;

        failedRules = _this.applyRules(field);
        if (failedRules.length) {
          $.merge(invalidFields, failedRules);
        }
      });

      if (invalidFields.length) {
        this.mark.invalidFields(invalidFields);
        this.notify(invalidFields, $triggerElement);
        this.events.trigger('invalid', { 'invalidFields' :  invalidFields });
        return false;
      } else {
        this.unMark.validFields();
        this.notify([], $triggerElement);
        return true;
      }
    },
    makeInvalid : function (invalidFields, $triggerElement) {
      this.mark.invalidFields(invalidFields);
      this.notify(invalidFields, $triggerElement);
      this.events.trigger('invalid', { 'invalidFields' :  invalidFields });
    },
    events : {
      trigger : function (evt, eData) {
        $(document).trigger('validation.' + evt, eData);
      },
      bind : function (evt, func) {
        $(document).on('validation.' + evt, func);
      }
    },
    mark : {
      field : function (fieldObj) {
        var $validationWrapper = fieldObj.$source.closest('.validation-wrapper');

        fieldObj.$source.addClass('invalid');
        if ($validationWrapper.length) {
          $validationWrapper.addClass('invalid');
        }
      },
      invalidFields : function (invalidFields) {
        var mark = this;

        validation.unMark.validFields();
        $.each(invalidFields, function (idx, fieldObj) {
          if (typeof fieldObj.$source !== 'undefined') {
            mark.field(fieldObj);
          }
        });
      }
    },
    unMark : {
      field : function (fieldObj) {
        var $validationWrapper = fieldObj.$source.closest('.validation-wrapper');

        fieldObj.$source.removeClass('invalid');
        if ($validationWrapper.length) {
          $validationWrapper.removeClass('invalid');
        }
      },
      validFields : function (validFields) {
        var unMark = this;

        if (validFields === undefined) { validFields = validation.fields.getAll(); }
        $.each(validFields, function (idx, fieldObj) {
          if (typeof fieldObj.$source !== 'undefined') {
            unMark.field(fieldObj);
          }
        });
      }
    },
    messageField : function ($field, message) {
      var $label = $field.parent('label'), // base assumption: label relationship as implied by parentage
          field = $field[0]

      if (!$label.length) { // if label not parent use for attribute for relationship binding
        $label = $(field.form).find('label[for="' + field.id + '"]');
        if (!$label.length) {
          return;
        }
      }
      $label.append('<span class="validation-message">' + message + '</span>');
    },
    notify : function (invalidFields, $validationTrigger) {
      var message = Mustache.compile('<div class="validation-message visible">{{#block}}{{message}}{{/block}}</div>'),
          _this = this,
          $lastElement,
          name,
          rule,
          element,
          $label,
          idToLinkTo,
          _getFirstChild;

      _getFirstChild = function (field) {
        var group = _this.fields.getNames([field.name])[0],
            firstChildName,
            firstChild;

        if (group.type === 'association') {
          firstChildName = group.members[0];
        } else {
          firstChildName = group.children[0];
        }
        firstChild = _this.fields.getNames([firstChildName])[0];
        if (firstChild.type !== 'field') {
          firstChild = _getFirstChild(firstChild);
        }
        return firstChild;
      };

      $('.validation-message').remove();
      if (typeof $validationTrigger[0].form === 'undefined' || !invalidFields.length) { return; }
      $lastElement = $($validationTrigger[0].form).find('.validation-submit').last();
      if (invalidFields.length) {
        $.each(invalidFields, function (idx, field) {
          var messageData = {};

          name = field.name;
          rule = field.rule;
          // only show messages if we have some defined
          if ((typeof _this.messages[name] !== 'undefined') && (typeof _this.messages[name][rule] !== 'undefined')) {
            messageData.message = _this.messages[name][rule];
            // if the field is a form control, add a message to its label and link to it
            if (field.$source && field.$source[0].id) {
              idToLinkTo = field.$source[0].id;
              _this.messageField(field.$source, _this.messages[name][rule]);
            } else {
              idToLinkTo = _getFirstChild(field).$source[0].id;
            }
            messageData.block = function () {
              return function (message, render) {
                return '<a href="#' + idToLinkTo + '">' + render(message) + '</a>';
              };
            };
            // add page validation message
            $(message(messageData)).insertBefore($lastElement);
          }
        });
      }
    },
    goToControl : function ($messageLink) {
      var relatedFormControl = document.getElementById($messageLink.attr('href').split('#')[1]);

      if (relatedFormControl !== null) {
        relatedFormControl.focus();
      }
      return false;
    },
    rules : (function () {
      var _fieldType,
          _selectValue,
          _radioValue,
          _getFieldValue,
          _getInvalidDataFromFields,
          rules;

      _fieldType = function ($field) {
        var type = $field[0].nodeName.toLowerCase();

        return (type === 'input') ? $field[0].type : type;
      };
      _selectValue = function ($field) {
        var idx = $field[0].selectedIndex;

        return $field.find('option:eq(' + idx + ')').val();
      };
      _radioValue = function ($field) {
        var radioName = $field.attr('name'),
            $radios = $($field[0].form).find('input[type=radio]'),
            $selectedRadio = false;

        $radios.each(function (idx, elm) {
          if ((elm.name === radioName) && elm.checked) { $selectedRadio = $(elm); }
        });

        return ($selectedRadio) ? $selectedRadio.val() : '';
      };
      _getFieldValue = function ($field) {
        switch (_fieldType($field)) {
          case 'text':
            return $field.val();
          case 'checkbox':
            return ($field.is(':checked')) ? $field.val() : '';
          case 'select':
            return _selectValue($field);
          case 'radio':
            return _radioValue($field);
          default:
            return $field.val();
        }
      };
      // Format an array of field objects into one where they are marked as invalid against a single rule
      _getInvalidDataFromFields = function (fields, rule) {
        return $.map(fields, function (item, idx) {
          return {
            'name' : item.name,
            'rule' : rule,
            '$source' : item.$source
          };
        });
      };
      /*
       * Rules should run as a field method with access to all the properties of that field type
       * They should return an array of failed rules. If there are no failures this array should be empty.
      */
      rules = {
        'field' : {
          'nonEmpty' : function () {
            if (this.$source.is(':hidden')) { return []; }
            if (_getFieldValue(this.$source) === '') {
              return _getInvalidDataFromFields([this], 'nonEmpty');
            } else {
              return [];
            }
          },
          'telephone' : function () {
            var entry = _getFieldValue(this.$source);

            if (this.$source.is(':hidden')) { return []; }
            if (entry.replace(/[\s|\-]/g, "").match(/^\+?\d+$/) === null) {
              return _getInvalidDataFromFields([this], 'telephone');
            } else {
              return [];
            }
          },
          'email' : function () {
            var entry = _getFieldValue(this.$source);

            if (this.$source.is(':hidden')) { return []; }
            if (entry.match(/^.+@[^@.]+(\.[^@.]+)+$/) === null) {
              return _getInvalidDataFromFields([this], 'email');
            } else {
              return [];
            }
          },
          'nino' : function () {
            var entry = _getFieldValue(this.$source),
                match;

            match = entry
                    .toUpperCase()
                    .replace(/[\s|\-]/g, "")
                    .match(/^[A-CEGHJ-PR-TW-Za-ceghj-pr-tw-z]{1}[A-CEGHJ-NPR-TW-Za-ceghj-npr-tw-z]{1}[0-9]{6}[A-DFMa-dfm]{0,1}$/);

            if (match === null) {
              return _getInvalidDataFromFields([this], 'nino');
            } else {
              return [];
            }
          },
          'postcode' : function () {
            var entry = _getFieldValue(this.$source),
                match;

            match = entry
                      .toUpperCase()
                      .replace(/\s/g, "")
                      .match(/^((GIR0AA)|((([A-PR-UW-Z][0-9][0-9]?)|(([A-PR-UW-Z][A-HK-Y][0-9][0-9]?)|(([A-PR-UW-Z][0-9][A-HJKSTUW])|([A-PR-UW-Z][A-HK-Z][0-9][ABEHMNPRVWXY]))))[0-9][A-BD-HJLNP-UW-Z]{2}))$/);
            if (match === null) {
              return _getInvalidDataFromFields([this], 'postcode');
            } else {
              return [];
            }
          },
          'smallText' : function () {
            var entry = _getFieldValue(this.$source),
                maxLen = 256;

            if (entry.length > maxLen) {
              return _getInvalidDataFromFields([this], 'smallText');
            } else {
              return [];
            }
          },
          'largeText' : function () {
            var entry = _getFieldValue(this.$source),
                maxLen = 500;

            if (entry.length > maxLen) {
              return _getInvalidDataFromFields([this], 'largeText');
            } else {
              return [];
            }
          },
          'firstNameText' : function () {
            var entry = _getFieldValue(this.$source),
                maxLen = 35;

            if (entry.length > maxLen) {
              return _getInvalidDataFromFields([this], 'firstNameText');
            } else {
              return [];
            }
          },
          'middleNameText' : function () {
            var entry = _getFieldValue(this.$source),
                maxLen = 100;

            if (entry.length > maxLen) {
              return _getInvalidDataFromFields([this], 'middleNameText');
            } else {
              return [];
            }
          },
          'lastNameText' : function () {
            var entry = _getFieldValue(this.$source),
                maxLen = 35;

            if (entry.length > maxLen) {
              return _getInvalidDataFromFields([this], 'lastNameText');
            } else {
              return [];
            }
          },
          'prevFirstNameText' : function () {
            var entry = _getFieldValue(this.$source),
                maxLen = 35;

            if (entry.length > maxLen) {
              return _getInvalidDataFromFields([this], 'prevFirstNameText');
            } else {
              return [];
            }
          },
          'prevMiddleNameText' : function () {
            var entry = _getFieldValue(this.$source),
                maxLen = 100;

            if (entry.length > maxLen) {
              return _getInvalidDataFromFields([this], 'prevMiddleNameText');
            } else {
              return [];
            }
          },
          'prevLastNameText' : function () {
            var entry = _getFieldValue(this.$source),
                maxLen = 35;

            if (entry.length > maxLen) {
              return _getInvalidDataFromFields([this], 'prevLastNameText');
            } else {
              return [];
            }
          },
          'validCountry' : function () {
            var entry = _getFieldValue(this.$source),
                countries = GOVUK.registerToVote.countries,
                isValid = false;

            $.each(countries, function (idx, country) {
              if (!isValid) {
                $.each(country.tokens, function (idx, token) {
                  if (entry.toLowerCase() === token.toLowerCase()) {
                    isValid = true;
                    return false;
                  }
                });
              }
            });
            if (!isValid) {
              return _getInvalidDataFromFields([this], 'validCountry');
            } else {
              return [];
            }
          }
        },
        'fieldset' : {
          'atLeastOneNonEmpty' : function () {
            var oneFilled = false,
                childFields = validation.fields.getNames(this.children),
                _fieldIsShowing;

            _fieldIsShowing = function (fieldObj) {
              return !fieldObj.$source.is(':hidden');
            };
            if (this.$source.is(':hidden')) { return []; }
            $.each(childFields, function (idx, fieldObj) {
              var method = 'nonEmpty',
                  isFilledFailedRules;

              if (fieldObj.type === 'fieldset') {
                if ($.inArray('allNonEmpty', fieldObj.rules) > -1) {
                  method = 'allNonEmpty';
                } else {
                  method = 'radioNonEmpty';
                }
              }
              isFilledFailedRules = fieldObj[method]();
              if (_fieldIsShowing(fieldObj) && !isFilledFailedRules.length) {
                oneFilled = true;
              }
            });
            if (!oneFilled) {
              return _getInvalidDataFromFields([this], 'atLeastOneNonEmpty');
            } else {
              return [];
            }
          },
          'atLeastOneTextEntry' : function () {
            var childFields = validation.fields.getNames(this.children),
                totalTextFields = 0,
                totalInvalidFields,
                oneHasEntry = false,
                _checkChildren,
                _fieldHasEntry;

            _checkChildren = function (children) {
              $.each(children, function (idx, child) {
                if (child.children) {
                  _checkChildren(validation.fields.getNames(child.children));
                } else {
                  if (_fieldHasEntry(child)) {
                    return false;
                  }
                }
              });
            };

            _fieldHasEntry = function (field) {
              var hasRule = $.inArray('nonEmpty', field.rules) > -1,
                  fieldType = field.$source.attr('type'),
                  invalidRules;

              if ((fieldType === 'text') && hasRule) {
                invalidRules = field.nonEmpty();

                if (invalidRules.length) {
                  return false;
                } else {
                  oneHasEntry = true;
                  return true;
                }
              }
            };

            _checkChildren(childFields);
            if (!oneHasEntry) {
              return _getInvalidDataFromFields([this], 'atLeastOneTextEntry');
            } else {
              return [];
            }
          },
          'atLeastOneChecked' :  function () {
            var invalidRules = this.atLeastOneNonEmpty();

            $.map(invalidRules, function (fieldObj, idx) {
              if (fieldObj.$source[0].nodeName.toLowerCase() !== 'input') { return fieldObj; }
            });
            if (!invalidRules.length) { return []; }
            return invalidRules;
          },
          'radioNonEmpty' : function () {
            var radioOptions = validation.fields.getNames(this.children),
                oneSelected = false;

            $.each(radioOptions, function (idx, radioOption) {
              if (radioOption.$source.is(':checked')) {
                oneSelected = true;
                return false;
              }
            });
            if (oneSelected) {
              return [];
            } else {
              return _getInvalidDataFromFields([this], 'radioNonEmpty');
            }
          },
          'checkedOtherIsValid' : function () {
            var childFields = validation.fields.getNames(this.children),
                otherIsChecked = false,
                otherCountries,
                otherCountriesFailedRules,
                i,j;

            // get checkbox &
            for (i = 0, j = childFields.length; i < j; i++) {
              var fieldObj = childFields[i];

              if (fieldObj.name === 'otherCountries') {
                otherCountries = fieldObj;
              } else if (fieldObj.name === 'other') {
                otherIsChecked = (_getFieldValue(fieldObj.$source) !== '');
              } else {
                continue;
              }
            }
            if (!otherIsChecked) {
              return [];
            } else {
              otherCountriesFailedRules = validation.applyRules(otherCountries);
              if (otherCountriesFailedRules.length) {
                return otherCountriesFailedRules;
              } else {
                return [];
              }
            }
          },
          'allNonEmpty' : function () {
            var childFields = validation.fields.getNames(this.children),
                childFailedRules = [],
                fieldsThatNeedInput = 0,
                emptyFields = 0,
                rulesToReport,
                _fieldIsShowing,
                fieldsetObj,
                i,j;

            _fieldIsShowing = function (fieldObj) {
              return !fieldObj.$source.is(':hidden');
            };
            if (!_fieldIsShowing(this)) { return []; }
            // validate against the nonEmpty rules of children
            for (i = 0, j = childFields.length; i < j; i++) {
              var fieldObj = childFields[i],
                  method = 'nonEmpty',
                  isFilledFailedRules;

              if (fieldObj.type === 'fieldset') {
                if ($.inArray('allNonEmpty', fieldObj.rules) > -1) {
                  method = 'allNonEmpty';
                } else {
                  method = 'radioNonEmpty';
                }
              }
              isFilledFailedRules = fieldObj[method]();
              if ($.inArray(method, fieldObj.rules) === -1) { continue; }
              fieldsThatNeedInput++;
              isFilledFailedRules = fieldObj[method]();
              if (_fieldIsShowing(fieldObj) && isFilledFailedRules.length) {
                emptyFields++;
                $.merge(childFailedRules, isFilledFailedRules);
              }
            }
            if (childFailedRules.length) {
              if (emptyFields < fieldsThatNeedInput) {
                // message for each child field
                rulesToReport = childFailedRules;
                fieldsetObj = {
                  'name' : this.name,
                  '$source' : this.$source
                };
              } else { // message from the fieldset level
                rulesToReport = _getInvalidDataFromFields(childFailedRules, 'allNonEmpty');
                fieldsetObj = {
                  'name' : this.name,
                  'rule' : 'allNonEmpty',
                  '$source' : this.$source
                };
              }
              if (this.$source.hasClass('inline-fields')) {
                rulesToReport.push(fieldsetObj);
              }
              return rulesToReport;
            } else {
              return [];
            }
          },
          'fieldOrExcuse' : function () {
            var childFields = validation.fields.getNames(this.children),
                field = childFields[0],
                excuse = childFields[1],
                fieldFailedRules = validation.applyRules(field),
                excuseFailedRules = validation.applyRules(excuse),
                fieldIsInvalid,
                excuseIsInvalid,
                _fieldIsShowing;

            _fieldIsShowing = function (fieldObj) {
              return !fieldObj.$source.is(':hidden');
            };
            fieldIsInvalid = ((fieldFailedRules.length > 0) && _fieldIsShowing(field));
            excuseIsInvalid = (excuseFailedRules.length > 0);
            if (fieldIsInvalid) {
              if (!_fieldIsShowing(excuse)) {
                return [{ 'name' : this.name, 'rule' : 'fieldOrExcuse', '$source' : field.$source }];
              } else { // excuse is showing
                if (!excuseIsInvalid) {
                  return [];
                }
              }
            } else {
              return [];
            }
          },
          'atLeastOneCountry' : function () {
            var countryValidationFields,
                $countryTextboxes,
                $filledCountries;

            if (this.$source.is(':hidden')) { return []; }
            countryValidationFields = GOVUK.registerToVote.validation.fields.getNames(this.children);
            $filledCountries = $.map(countryValidationFields, function (field, idx) {
              if ($countryTextboxes === undefined) {
                $countryTextboxes = $(field.$source);
              } else {
                $countryTextboxes = $countryTextboxes.add(field.$source);
              }
              return (_getFieldValue(field.$source) === '') ? null : field.$source;
            });
            if ($filledCountries.length === 0) {
              return _getInvalidDataFromFields([this], 'atLeastOneCountry');
            } else {
              return [];
            }
          },
          'allCountriesValid' : function () {
            var countries = GOVUK.registerToVote.countries,
                countryValidationFields,
                getInvalidCountries,
                $invalidCountries;

            getInvalidCountries = function (countryValidationFields) {
              var invalidCountryObj,
                  $results;

              $.each(countryValidationFields, function (idx, field) {
                var entry = _getFieldValue(field.$source),
                    entryIsValidCountry;

                if (entry === '') { return true; }
                invalidCountryObj = field.validCountry();
                if (invalidCountryObj.length) {
                  if ($results === undefined) {
                    $results = $(field.$source);
                  } else {
                    $results = $results.add(field.$source);
                  }
                }
              });
              return ($results !== undefined) ? $results : false;
            };

            if (this.$source.is(':hidden')) { return []; }
            countryValidationFields = GOVUK.registerToVote.validation.fields.getNames(this.children);
            $invalidCountries = getInvalidCountries(countryValidationFields);
            if ($invalidCountries) {
              return _getInvalidDataFromFields([this, { 'name' : 'country', '$source' : $invalidCountries }], 'allCountriesValid');
            } else {
              return [];
            }
          },
          'correctAge' : function () {
            var children = validation.fields.getNames(this.children),
                day = parseInt(_getFieldValue(children[0].$source), 10),
                month = parseInt(_getFieldValue(children[1].$source), 10),
                year = parseInt(_getFieldValue(children[2].$source), 10),
                dob = (new Date(year, (month - 1), day)).getTime(),
                now = (new Date()).getTime(),
                minAge = 16,
                maxAge = 115,
                age = now - dob;

            age = Math.floor((((((age / 1000) / 60) / 60) / 24) / 365.25));
            isValid = ((age >= minAge) && (age <= maxAge));
            if (!isValid) {
              return _getInvalidDataFromFields(children, 'correctAge');
            } else {
              return [];
            }
          },
          'max5Countries' : function () {
            var totalCountryFields,
                totalCountries,
                getPrimaryCountries,
                getOtherCountries;

            getPrimaryCountries = function () {
              return GOVUK.registerToVote.validation.fields.getNames(['british', 'irish']);
            };

            getOtherCountries = function () {
              var otherCountries = GOVUK.registerToVote.validation.fields.getNames(['otherCountries']);

              return GOVUK.registerToVote.validation.fields.getNames(otherCountries[0].children);
            };

            totalCountryFields = $.merge(getPrimaryCountries(), getOtherCountries());
            totalCountries = $.grep(totalCountryFields, function (field, idx) {
              return (field.nonEmpty().length === 0);
            });

            if (totalCountries.length > 5) {
              return _getInvalidDataFromFields([this], 'max5Countries');
            } else {
              return [];
            }
          },
          'atLeastOneValid' : function () {
            var children = validation.fields.getNames(this.children),
                totalInvalidRules = [];

            $.each(children, function (idx, child) {
              var invalidRules = validation.applyRules(child);

              if (invalidRules.length) {
                $.merge(totalInvalidRules, invalidRules);
              }
            });
            return totalInvalidRules;
          },
          'firstChildValid' : function () {
            var children = validation.fields.getNames(this.children),
                firstChildVaildRules = validation.applyRules(children[0]);

            if (firstChildVaildRules.length) {
              return _getInvalidDataFromFields([this], 'firstChildValid');
            } else {
              return [];
            }
          }
        },
        'association' : {
          'fieldsetOrExcuse' : function () {
            var memberFields = validation.fields.getNames(this.members),
                fieldset = memberFields[0],
                excuse = memberFields[1],
                fieldsetFailedRules = validation.applyRules(fieldset),
                excuseFailedRules = validation.applyRules(excuse),
                fieldsetIsInvalid,
                excuseIsInvalid,
                _fieldIsShowing;

            _fieldIsShowing = function (fieldObj) {
              return !fieldObj.$source.is(':hidden');
            };
            fieldsetIsInvalid = ((fieldsetFailedRules.length > 0) && _fieldIsShowing(fieldset));
            excuseIsInvalid = (excuseFailedRules.length > 0);
            if (fieldsetIsInvalid) {
              if (!_fieldIsShowing(excuse)) {
                return fieldsetFailedRules;
              } else { // excuse is showing
                if (excuseIsInvalid) {
                  return fieldsetFailedRules;
                } else { // excuse is valid
                  return [];
                }
              }
            } else {
              return [];
            }
          },
          'dateOfBirthOrExcuse' : function () {
            var memberFields = validation.fields.getNames(this.members),
                dateOfBirthField = memberFields[0],
                excuseField = memberFields[1],
                dateOfBirthInvalidRules = validation.applyRules(dateOfBirthField),
                excuseInvalidRules = validation.applyRules(excuseField),
                _fieldIsShowing,
                _entryInDateOfBirth,
                _entryInExcuse;

            _fieldIsShowing = function (fieldObj) {
              return !fieldObj.$source.is(':hidden');
            };

            _entryInDateOfBirth = function () {
              return dateOfBirthInvalidRules.length < 4;
            };

            _entryInExcuse = function () {
              return excuseInvalidRules.length < 2;
            };

            if (!_entryInDateOfBirth()) {
              if ((!_fieldIsShowing(excuseField)) || (!_entryInExcuse())) {
                return dateOfBirthInvalidRules;
              } else {
                return excuseInvalidRules;
              }
            } else {
              return dateOfBirthInvalidRules;
            }
          },
          'allNonEmpty' : function () {
            var memberFields = validation.fields.getNames(this.members),
                memberFailedRules = [],
                fieldsThatNeedInput = 0,
                emptyFields = 0,
                rulesToReport,
                _fieldIsShowing,
                fieldsetObj,
                i,j;

            _fieldIsShowing = function (fieldObj) {
              return !fieldObj.$source.is(':hidden');
            };
            // validate against the nonEmpty rules of children
            for (i = 0, j = memberFields.length; i < j; i++) {
              var fieldObj = memberFields[i],
                  method = 'nonEmpty',
                  isFilledFailedRules;

              if (fieldObj.type === 'fieldset') {
                if ($.inArray('allNonEmpty', fieldObj.rules) > -1) {
                  method = 'allNonEmpty';
                } else {
                  method = 'radioNonEmpty';
                }
              }
              if ($.inArray(method, fieldObj.rules) === -1) { continue; }
              fieldsThatNeedInput++;
              isFilledFailedRules = fieldObj[method]();
              if (_fieldIsShowing(fieldObj) && isFilledFailedRules.length) {
                emptyFields++;
                $.merge(memberFailedRules, isFilledFailedRules);
              }
            }
            if (memberFailedRules.length) {
              if (emptyFields < fieldsThatNeedInput) {
                // message for each child field
                rulesToReport = memberFailedRules;
                fieldsetObj = {
                  'name' : this.name,
                  '$source' : this.$source
                };
              } else { // message from the fieldset level
                rulesToReport = _getInvalidDataFromFields(memberFailedRules, 'allNonEmpty');
                fieldsetObj = {
                  'name' : this.name,
                  'rule' : 'allNonEmpty'
                };
              }
              rulesToReport.push(fieldsetObj);
              return rulesToReport;
            } else {
              return [];
            }
          },
          'allValid' : function () {
            var memberFields = validation.fields.getNames(this.members),
                memberFailedRules = [],
                fieldObj,
                failedRules,
                _fieldIsShowing,
                i,j;

            _fieldIsShowing = function (fieldObj) {
              return !fieldObj.$source.is(':hidden');
            };
            for (i = 0, j = memberFields.length; i < j; i++) {
              fieldObj = memberFields[i];

              failedRules = validation.applyRules(fieldObj);
              if (failedRules.length) {
                $.merge(memberFailedRules, failedRules);
              }
            }
            if (_fieldIsShowing(fieldObj)) {
              return memberFailedRules;
            }
            return [];
          }
        }
      };
      return rules;
    }()),
    messages : {
      'fullName' : {
        'allNonEmpty' : message('ordinary_name_error_enterFullName')
      },
      'firstName' : {
        'nonEmpty' : message('ordinary_name_error_enterFirstName'),
        'firstNameText' : message('ordinary_name_error_firstNameTooLong')
      },
      'middleName' : {
        'middleNameText' : message('ordinary_name_error_middleNamesTooLong')
      },
      'lastName' : {
        'nonEmpty' : message('ordinary_name_error_enterLastName'),
        'lastNameText' : message('ordinary_name_error_lastNameTooLong')
      },
      'previousQuestion' : {
        'atLeastOneNonEmpty' : message('ordinary_previousName_error_answerThis')
      },
      'previousName' : {
        'allNonEmpty' : message('ordinary_previousName_error_enterFullName')
      },
      'previousFirstName' : {
        'nonEmpty' : message('ordinary_previousName_error_enterFirstName'),
        'prevFirstNameText' : message('ordinary_previousName_error_firstNameTooLong')
      },
      'previousMiddleName' : {
        'prevMiddleNameText' : message('ordinary_previousName_error_middleNamesTooLong')
      },
      'previousLastName' : {
        'nonEmpty' : message('ordinary_previousName_error_enterLastName'),
        'prevLastNameText' : message('ordinary_previousName_error_lastNameTooLong')
      },
      'dateOfBirthDate' : {
        'allNonEmpty' : message('ordinary_dob_error_enterDateOfBirth')
      },
      'day' : {
        'nonEmpty' : message('ordinary_dob_error_enterDay')
      },
      'month' : {
        'nonEmpty' : message('ordinary_dob_error_enterMonth')
      },
      'year' : {
        'nonEmpty' : message('ordinary_dob_error_enterYear')
      },
      'dateOfBirthExcuseReason' : {
        'nonEmpty' : message('ordinary_dob_error_provideReason')
      },
      'excuseAgeAttempt' : {
        'radioNonEmpty' : message('ordinary_dob_error_selectRange')
      },
      'otherAddressQuestion' : {
        'atLeastOneNonEmpty' : message('ordinary_otheraddr_error_pleaseAnswer')
      },
      'contact' : {
        'atLeastOneNonEmpty' : message('ordinary_contact_error_pleaseAnswer')
      },
      'phoneNumber' : {
        'nonEmpty' : message('ordinary_contact_error_enterYourPhoneNo')
      },
      'emailAddress' : {
        'nonEmpty' : message('ordinary_contact_error_enterYourEmail'),
        'email' : message('ordinary_contact_error_pleaseEnterValidEmail')
      },
      'nationality' : {
        'atLeastOneNonEmpty' : message('ordinary_nationality_error_pleaseAnswer')
      },
      'otherCountries' : {
        'atLeastOneCountry' : message('ordinary_nationality_error_pleaseAnswer'),
        'allCountriesValid' : message('ordinary_nationality_error_notValid')
      },
      'ninoCode' : {
        'nonEmpty' : message('ordinary_nino_error_noneEntered'),
        'nino' : message('ordinary_nino_error_incorrectFormat')
      },
      'postalVote' : {
        'atLeastOneNonEmpty' : message('ordinary_postalVote_error_answerThis')
      },
      'waysToVote' : {
        'atLeastOneNonEmpty' : 'Please answer this question'
      },
      'previouslyRegistered' : {
        'atLeastOneNonEmpty' : 'Please answer this question'
      },
      'countrySelect' : {
        'nonEmpty' : 'Please select your country'
      },
      'correspondenceAddressLinesFieldSet' : {
        'atLeastOneNonEmpty' : 'Please enter your address'
      },
      'deliveryMethod' : {
        'atLeastOneNonEmpty' : 'Please answer this question'
      },
      'country' : {
        'atLeastOneNonEmpty' : message('ordinary_country_error_pleaseAnswer'),
        'max5Countries' : message('ordinary_nationality_error_noMoreFiveCountries')
      },
      'postcode' : {
        'nonEmpty' : message('ordinary_address_error_pleaseEnterYourPostcode'),
        'postcode' : message('ordinary_address_error_postcodeIsNotValid')
      },
      'addressSelect' : {
        'nonEmpty' : message('ordinary_address_error_pleaseSelectYourAddress')
      },
      'addressManual' :  {
        'atLeastOneTextEntry' :  message('ordinary_address_error_pleaseAnswer')
      },
      'manualAddressMultiline' : {
        'firstChildValid' : message('ordinary_address_error_lineOneIsRequired')
      },
      'city' : {
        'nonEmpty' : message('ordinary_address_error_cityIsRequired')
      },
      'previousAddress' : {
        'atLeastOneNonEmpty' : message('ordinary_address_error_pleaseAnswer')
      },
      'statement' : {
        'atLeastOneNonEmpty' : 'Please answer this question'
      },
      'BFPOAddressLinesFieldSet' : {
        'atLeastOneNonEmpty' : 'Please enter the address'
      },
      'BFPOAddressPostcode' : {
        'nonEmpty' : 'Please enter the postcode'
      },
      'otherAddressLinesFieldSet' : {
         'atLeastOneNonEmpty' : 'Please enter the address'
      },
      'otherAddressPostcode' : {
        'nonEmpty' : 'Please enter the postcode'
      },
      'otherAddressCountry' : {
        'nonEmpty' : 'Please enter the country'
      },
      'contactAddress' :{
        'atLeastOneNonEmpty' : 'Please answer this question'
      },
      'serviceNumberAndRank' : {
        'allNonEmpty' : 'Please answer this question'
      },
      'service' : {
        'atLeastOneNonEmpty' : 'Please answer this question'
      },
      'regiment' : {
        'nonEmpty' : 'Please enter the regiment or corps'
      },
      'job' : {
        'allNonEmpty' : 'Please answer this question'
      },
      'jobTitle' : {
        'nonEmpty' : 'Please enter the job title or rank'
      },
      'govDepartment' : {
        'nonEmpty' : 'Please enter the government department, agency or body'
      },
      'hasUkAddress' : {
         'atLeastOneNonEmpty' : 'Please answer this question'
      },
      'registeredAbroad' : {
        'atLeastOneNonEmpty' : 'Please answer this question'
      }
    }
  };

  GOVUK.registerToVote.validation = validation;
}.call(this));
