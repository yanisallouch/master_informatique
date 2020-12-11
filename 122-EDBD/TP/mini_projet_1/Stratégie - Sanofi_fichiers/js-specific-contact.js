(function ($) {
  /* Change the Select Theme label only for fr language as by now the field title should be the same as there is only one configuration for the maito in the save action */
  /* Specific for the FR language */
  if ($('html').attr('lang') === 'fr') {
    /* Specific for the webform osw-form-corpo-contact */
    if ($('form.osw-form-corpo-contact').length) {
      /* Update Select Theme Label */
      let $themeLabel = $('form.osw-form-corpo-contact div.osw-form-selecttarget-label > label')
      if ($themeLabel.length) {
        $themeLabel.text('Je veux contacter Sanofi concernant')
      }
    }
  }
})(window.jQuery, document)
