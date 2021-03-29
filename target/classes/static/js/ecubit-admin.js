(function($) {
  "use strict"; // Start of use strict

  // Toggle the side navigation
  $("#sidebarToggle, #sidebarToggleTop").on('click', function(e) {
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
      $('.sidebar .collapse').collapse('hide');
    };
  });

  // Close any open menu accordions when window is resized below 768px
  $(window).resize(function() {
    if ($(window).width() < 768) {
      $('.sidebar .collapse').collapse('hide');
    };
    
    // Toggle the side navigation when window is resized below 480px
    if ($(window).width() < 480 && !$(".sidebar").hasClass("toggled")) {
      $("body").addClass("sidebar-toggled");
      $(".sidebar").addClass("toggled");
      $('.sidebar .collapse').collapse('hide');
    };
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  $('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
    if ($(window).width() > 768) {
      var e0 = e.originalEvent,
        delta = e0.wheelDelta || -e0.detail;
      this.scrollTop += (delta < 0 ? 1 : -1) * 30;
      e.preventDefault();
    }
  });

  // Scroll to top button appear
  $(document).on('scroll', function() {
    var scrollDistance = $(this).scrollTop();
    if (scrollDistance > 100) {
      $('.scroll-to-top').fadeIn();
    } else {
      $('.scroll-to-top').fadeOut();
    }
  });

  // Smooth scrolling using jQuery easing
  $(document).on('click', 'a.scroll-to-top', function(e) {
    var $anchor = $(this);
    $('html, body').stop().animate({
      scrollTop: ($($anchor.attr('href')).offset().top)
    }, 1000, 'easeInOutExpo');
    e.preventDefault();
  });
  $(function () {
    $('[data-toggle="tooltip"]').tooltip()
  })
  $('.nextTab').click(function(){
    var nextId = $(this).parents('.tab-pane').next().attr("id");
  
    $('a[href$="'+nextId+'"]').tab('show');
      
    return false;
  })
  $('.prevTab').click(function(){
    var prevId = $(this).parents('.tab-pane').prev().attr("id");
  
    $('a[href$="'+prevId+'"]').tab('show');
      
    return false;
  })
  
  
  $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    
    //update progress
    var step = $(e.target).data('step');
    var percent = (parseInt(step) / 3) * 100;
    
    $('.progress-bar').css({width: percent + '%'});
    $('.progress-bar').text("Step " + step + " of 3");
    
    //e.relatedTarget // previous tab
    
  })
  
  $('.first').click(function(){
  
    $('#WizardNewPaziente a:first').tab('show')
  
  })
  $(document).ready(function(){
    // Prepare the preview for profile picture
        $("#wizard-picture").change(function(){
            readURL(this);
        });
    });
    function readURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
    
            reader.onload = function (e) {
                $('#wizardPicturePreview').attr('src', e.target.result).fadeIn('slow');
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    $(function(){
      var hash = window.location.hash;
      hash && $('ul.nav a[href="' + hash + '"]').tab('show');
    
      $('.nav-tabs a, .nav-pills a').click(function (e) {
        $(this).tab('show');
        var scrollmem = $('body').scrollTop();
        window.location.hash = this.hash;
        $('html,body').scrollTop(scrollmem);
      });
    });






    $(function () { 
  
      function moveItems(origin, dest) {
          $(origin).find(':selected').appendTo(dest);
          $(dest).find(':selected').removeAttr("selected");
          $(dest).sort_select_box();
      }
  
      function moveAllItems(origin, dest) {
          $(origin).children("option:visible").appendTo(dest);
          $(dest).find(':selected').removeAttr("selected");
          $(dest).sort_select_box();
      }
  
      $('.left').on('click', function () {
          var container = $(this).closest('.addremove-multiselect');
          moveItems($(container).find('select.multiselect.selected'), $(container).find('select.multiselect.available'));
      });
  
      $('.right').on('click', function () {
          var container = $(this).closest('.addremove-multiselect');
          moveItems($(container).find('select.multiselect.available'), $(container).find('select.multiselect.selected'));
      });
  
      $('.leftall').on('click', function () {
          var container = $(this).closest('.addremove-multiselect');
          moveAllItems($(container).find('select.multiselect.selected'), $(container).find('select.multiselect.available'));
      });
  
      $('.rightall').on('click', function () {
          var container = $(this).closest('.addremove-multiselect');
          moveAllItems($(container).find('select.multiselect.available'), $(container).find('select.multiselect.selected'));
      });
  
      $('select.multiselect.selected').on('dblclick keyup',function(e){
          if(e.which == 13 || e.type == 'dblclick') {
            var container = $(this).closest('.addremove-multiselect');
            moveItems($(container).find('select.multiselect.selected'), $(container).find('select.multiselect.available'));
          }
      });
  
      $('select.multiselect.available').on('dblclick keyup',function(e){
          if(e.which == 13 || e.type == 'dblclick') {
              var container = $(this).closest('.addremove-multiselect');
              moveItems($(container).find('select.multiselect.available'), $(container).find('select.multiselect.selected'));
          }
      }); 
  
  
  });
  
  $.fn.sort_select_box = function(){
      // Get options from select box
      var my_options =$(this).children('option');
      // sort alphabetically
      my_options.sort(function(a,b) {
          if (a.text > b.text) return 1;
          else if (a.text < b.text) return -1;
          else return 0
      })
     //replace with sorted my_options;
     $(this).empty().append( my_options );
  
     // clearing any selections
     $("#"+this.attr('id')+" option").attr('selected', false);
  }









    
}


)(jQuery); // End of use strict
