(function ($) {
  /* Corrections for content comming from NASDAQMX (press release), mainly with the class "hugin" or with some specific inline style not needed */
  if ($('body.osw').hasClass('osw-nasdaq-pressrelease')) {
    /* Remove inline style from SUP.hugin element */
    $('sup.hugin').removeAttr('style')
    /* Move bgcolor from attribute to style */
    $('.hugin[bgcolor]').each(function () {
      $(this).css({'background-color': '' + $(this).attr('bgcolor') + ''})
    })
    /* Update mailto content in order to avoid bad display on mobile */
    $('td.hugin a[href^="mailto:"]').each(function() {
      let $mailtoLink = $(this)
      if ($mailtoLink.find('u').length) {
        $mailtoLink.closest('td').each(function() {
          if (this.style.width !== '100%') {
            let $closestTD = $(this)
            $closestTD.css({'display':'inline-block'})
            $mailtoLink.css({'text-overflow':'ellipsis','white-space':'nowrap','overflow':'hidden','display':'block'})
          }
        })
      }
    })
  }
})(window.jQuery, document)
