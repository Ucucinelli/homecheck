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
    $('[data-toggle="tooltip"]').tooltip();
    $('[data-toggle="popover"]').popover();
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
  
     // set selections
     $("#"+this.attr('id')+" option").prop('selected', true);
  }

  $("input#pwd").on("focus keyup", function () {
         
  });
   
  $("input#pwd").blur(function () {
           
  });
  $("input#pwd").on("focus keyup", function () {
          var score = 0;
          var a = $(this).val();
          var desc = new Array();
   
          // strength desc
          desc[0] = "Too short";
      desc[1] = "Weak";
      desc[2] = "Good";
      desc[3] = "Strong";
      desc[4] = "Best";
           
  });
   
  $("input#pwd").blur(function () {
   
  });
  $("input#pwd").on("focus keyup", function () {
          var score = 0;
          var a = $(this).val();
          var desc = new Array();
   
          // strength desc
          desc[0] = "Too short";
          desc[1] = "Weak";
          desc[2] = "Good";
          desc[3] = "Strong";
          desc[4] = "Best";
           
          // password length
          if (a.length >= 6) {
              $("#length").removeClass("invalid").addClass("valid");
              score++;
          } else {
              $("#length").removeClass("valid").addClass("invalid");
          }
   
          // at least 1 digit in password
          if (a.match(/\d/)) {
              $("#pnum").removeClass("invalid").addClass("valid");
              score++;
          } else {
              $("#pnum").removeClass("valid").addClass("invalid");
          }
   
          // at least 1 capital & lower letter in password
          if (a.match(/[A-Z]/) && a.match(/[a-z]/)) {
              $("#capital").removeClass("invalid").addClass("valid");
              score++;
          } else {
              $("#capital").removeClass("valid").addClass("invalid");
          }
   
          // at least 1 special character in password {
          if ( a.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) ) {
                  $("#spchar").removeClass("invalid").addClass("valid");
                  score++;
          } else {
                  $("#spchar").removeClass("valid").addClass("invalid");
          }
   
   
          if(a.length > 0) {
                  //show strength text
                  $("#passwordDescription").text(desc[score]);
                  // show indicator
                  $("#passwordStrength").removeClass().addClass("strength"+score);
          } else {
                  $("#passwordDescription").text("Password not entered");
                  $("#passwordStrength").removeClass().addClass("strength"+score);
          }
  });
   
  $("input#pwd").blur(function () {
   
  });
  
  // setting password Strength
  $("input#pwd").on("focus keyup", function () {
          var score = 0;
          var a = $(this).val();
          var desc = new Array();
   
          // strength desc
          desc[0] = "Troppo corta";
          desc[1] = "Debole";
          desc[2] = "Buona";
          desc[3] = "Forte";
          desc[4] = "Ottimo";
   
          $("#pwd_strength_wrap").fadeIn(400);
           
          // password length
          if (a.length >= 8) {
              $("#length").removeClass("invalid").addClass("valid");
              score++;
          } else {
              $("#length").removeClass("valid").addClass("invalid");
          }
   
          // at least 1 digit in password
          if (a.match(/\d/)) {
              $("#pnum").removeClass("invalid").addClass("valid");
              score++;
          } else {
              $("#pnum").removeClass("valid").addClass("invalid");
          }
   
          // at least 1 capital & lower letter in password
          if (a.match(/[A-Z]/) && a.match(/[a-z]/)) {
              $("#capital").removeClass("invalid").addClass("valid");
              score++;
          } else {
              $("#capital").removeClass("valid").addClass("invalid");
          }
   
          // at least 1 special character in password {
          if ( a.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) ) {
                  $("#spchar").removeClass("invalid").addClass("valid");
                  score++;
          } else {
                  $("#spchar").removeClass("valid").addClass("invalid");
          }
   
   
          if(a.length > 0) {
                  //show strength text
                  $("#passwordDescription").text(desc[score]);
                  // show indicator
                  $("#passwordStrength").removeClass().addClass("strength"+score);
          } else {
                  $("#passwordDescription").text("Password non inserita");
                  $("#passwordStrength").removeClass().addClass("strength"+score);
          }
  });
   
  $("input#pwd").blur(function () {
          $("#pwd_strength_wrap").fadeOut(400);
  });


  // tag chip
  $(document).on("click", ".tagchipclose" , function() {
    $(this).closest('.tagchip').fadeOut( 500, function() { 
      $(this).remove();
    });
});

// calendar http://eureka2.github.io/ab-datepicker/
$(document).ready(function() {
  $('.date').datepicker({
  firstDayOfWeek: 1, // The first day of week is Monday
  weekDayFormat: 'short', // Only first letter for the weekday names
  inputFormat: 'YYYY/MM/DD',
  outputFormat: 'yy/MM/dd',
  titleFormat: 'EEEE d MMMM y',
  markup: 'bootstrap4',
  theme: 'bootstrap',
  modal: false
});
});

// multiple select custom



    
}


)(jQuery); // End of use strict
