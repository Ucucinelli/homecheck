$(function() {
	$("#datepicker").datepicker({
		
		dateFormat: "dd/mm/yy",
		onSelect: function(date) {
			//defined your own method here
			//var startdate = $('#datepicker').datepicker('getDate').toISOString();
			//$("#datepicker").val(startdate);
		}
	});
	$("#datepicker").attr("autocomplete", "off");
	
	$("#modaldatepicker").datepicker({
		
		dateFormat: "dd/mm/yy",
		onSelect: function(date) {
			//defined your own method here
			//var startdate = $('#datepicker').datepicker('getDate').toISOString();
			//$("#datepicker").val(startdate);
		}
	});
	$("#modaldatepicker").attr("autocomplete", "off");
})

$(document).on("click", ".pairModal", function () {
	 var objectId = this.getAttribute("data-id");
     var sequence = this.getAttribute("data-sequence");

     $(".modal-body #idDispositivo").val(objectId);
     $(".modal-body #sequence").val(sequence);
     // As pointed out in comments, 
     // it is unnecessary to have to manually call the modal.
     // $('#addBookDialog').modal('show');
});


$(document).on("click", ".unpairModal", function () {
	 var objectId = this.getAttribute("data-id");
     var sequence = this.getAttribute("data-sequence");

     $(".modal-body #idDispositivoUnpair").val(objectId);
     $(".modal-body #sequenceUnpair").val(sequence);
     // As pointed out in comments, 
     // it is unnecessary to have to manually call the modal.
     // $('#addBookDialog').modal('show');
});

function pair() {
    var idDispositivo = $("#idDispositivo").val();
    var numberSequence = $("#sequence").val();
    var sequence = "devices["+numberSequence+"].selezionato";
    var dataAssegnazione = $("#modaldatepicker").val();
    var termineAssegnazione = "devices["+numberSequence+"].termineAssegnazioneString";
    $("#"+idDispositivo).parent().children(".termineAssegnazione").children().append(dataAssegnazione);
    $("#"+idDispositivo).append("<input type='hidden' class='paired' value='true' name="+"'"+sequence+"'"+"/>");
    $("#"+idDispositivo).append("<input type='hidden' class='paired' value="+"'"+dataAssegnazione+"'"+"name='"+termineAssegnazione+"'/>");
    $("#"+idDispositivo).parent().children(".action").empty();
    $("#"+idDispositivo).parent().children(".action").append("<span class='btn-sm btn-primary btn-circle btn-icon'>"+
    "<i class='fas fa-check' data-toggle='tooltip' data-placement='top' title='' data-original-title='Sensore Associato'>"+
    "</i></span> <a href='#' class='btn-sm btn-google btn-circle btn-icon unpairModal' data-toggle='modal' "+
    "data-target='#unpairModal' data-id='"+idDispositivo+"'"+"data-sequence='"+numberSequence+"'"+">"+
    "<i class='fas fa-times'  data-toggle='tooltip' data-placement='top' title='' data-original-title='Unpair Sensore'></i></a>");
    $("#modaldatepicker").val("");
    
}


function unpair() {
	var idDispositivo = $("#idDispositivoUnpair").val();
    var numberSequence = $("#sequenceUnpair").val();
    $("#"+idDispositivo).children(".paired").remove();
    $("#"+idDispositivo).parent().children(".termineAssegnazione").children().empty();
	$("#"+idDispositivo).parent().children(".action").empty();
    $("#"+idDispositivo).parent().children(".action").append
       ("<a href='#my_modal' data-id='"+idDispositivo+"'"+ "data-sequence='"+numberSequence+"'"+ 
        "class='btn-sm btn-info btn-circle btn-icon openModal' data-toggle='modal'"+
        "data-target='#AssociaDeviceModal'><i class='fas fa-exchange-alt'"+ 
        "data-toggle='tooltip' data-placement='top' title='' data-original-title='Associa Sensore'></i></a>");
    
}

function sendtoken() {
	var _token = $('meta[name="_jwt"]').attr('content');

	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader("token", _token);
	});
}

function sendtoken2() {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});

	$.ajax({
		url: "http://localhost:9999/api/login",
		method: 'POST',
		success: function(result) {

		},
		error: function(result) {
			console.log(result);
		}
	});
}


function sendtoken3() {



	var formData = new FormData();
	formData.append("password", "Passw0rd!");
	formData.append("username", "u.cucinelli@ecubit.it");
	$.ajax({
		url: "http://localhost:9999/api/login",
		type: 'post',
		headers: {
			'Authorization': 'Basic xxxxxxxxxxxxx',
			'X-CSRF-TOKEN': 'xxxxxxxxxxxxxxxxxxxx',
			'Content-Type': 'application/json'
		},
		cache: false,
		contentType: false,
		processData: false,
		data: formData,
		success: function(data) {
			// ..... any success code you want
		}
	});

}

