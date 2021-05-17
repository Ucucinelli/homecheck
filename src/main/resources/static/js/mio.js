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
		altFormat: "yy-mm-dd",
		altField: "#end-date",
		onSelect: function(date) {
			//defined your own method here
			//var startdate = $('#datepicker').datepicker('getDate').toISOString();
			//$("#datepicker").val(startdate);
		}
	});
	$("#modaldatepicker").attr("autocomplete", "off");
})

$(document).on("click", ".pairModal", function () {
	 $(".modal-body #modaldatepicker").val("");
	 var objectId = this.getAttribute("data-id");
     var sequence = this.getAttribute("data-sequence");

     $(".modal-body #idDispositivo").val(objectId);
     $(".modal-body #sequence").val(sequence);
     // As pointed out in comments, 
     // it is unnecessary to have to manually call the modal.
     // $('#addBookDialog').modal('show');
});

$(document).on("click", ".pairUpdateModal", function () {
	 $(".modal-body #modaldatepicker").val("");
     $(".modal-body #end-date").val("");
	 var idDispositivo = this.getAttribute("data-id");
     var sequence = this.getAttribute("data-sequence");
     var numeroSeriale = this.getAttribute("data-numeroSeriale");
     var marca = this.getAttribute("data-marca");
     var modello = this.getAttribute("data-modello");
     var btMacAddress = this.getAttribute("data-btMacAddress");
     var tipologia = this.getAttribute("data-tipologia");

     $(".modal-body #valoriDispositivo")[0].setAttribute("idDispositivo", idDispositivo);
     $(".modal-body #valoriDispositivo")[0].setAttribute("sequence", sequence);
     $(".modal-body #valoriDispositivo")[0].setAttribute("numeroSeriale", numeroSeriale);
     $(".modal-body #valoriDispositivo")[0].setAttribute("marca", marca);
     $(".modal-body #valoriDispositivo")[0].setAttribute("modello", modello);
     $(".modal-body #valoriDispositivo")[0].setAttribute("btMacAddress", btMacAddress);
     $(".modal-body #valoriDispositivo")[0].setAttribute("tipologia", tipologia);

     // As pointed out in comments, 
     // it is unnecessary to have to manually call the modal.
     // $('#addBookDialog').modal('show');
});


$(document).on("click", ".unpairUpdateModal", function () {
	 var idDispositivo = this.getAttribute("data-id");
     var sequence = this.getAttribute("data-sequence");
     var numeroSeriale = this.getAttribute("data-numeroSeriale");
     var marca = this.getAttribute("data-marca");
     var modello = this.getAttribute("data-modello");
     var btMacAddress = this.getAttribute("data-btMacAddress");
     var tipologia = this.getAttribute("data-tipologia");

     $(".modal-body #valoriDispositivo")[0].setAttribute("idDispositivo", idDispositivo);
     $(".modal-body #valoriDispositivo")[0].setAttribute("sequence", sequence);
     $(".modal-body #valoriDispositivo")[0].setAttribute("numeroSeriale", numeroSeriale);
     $(".modal-body #valoriDispositivo")[0].setAttribute("marca", marca);
     $(".modal-body #valoriDispositivo")[0].setAttribute("modello", modello);
     $(".modal-body #valoriDispositivo")[0].setAttribute("btMacAddress", btMacAddress);
     $(".modal-body #valoriDispositivo")[0].setAttribute("tipologia", tipologia);

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
}

function pairUpdate() {
    var idDispositivo = $(".modal-body #valoriDispositivo")[0].getAttribute("idDispositivo");
    var numberSequence = $(".modal-body #valoriDispositivo")[0].getAttribute("sequence");
    var numeroSeriale = $(".modal-body #valoriDispositivo")[0].getAttribute("numeroSeriale");
    var numeroSerialeText = numeroSeriale != "null"? numeroSeriale:"";
    var marca = $(".modal-body #valoriDispositivo")[0].getAttribute("marca");
    var marcaText = marca != "null"? marca:"";
    var modello = $(".modal-body #valoriDispositivo")[0].getAttribute("modello");
    var modelloText = modello != "null"? modello:"";
    var tipologia = $(".modal-body #valoriDispositivo")[0].getAttribute("tipologia");
    var tipologiaText = tipologia != "null"? tipologia:"";
    var btMacAddress = $(".modal-body #valoriDispositivo")[0].getAttribute("btMacAddress");
    var btMacAddressText = btMacAddress != "null"? btMacAddress:"";
    var sequence = "devices["+numberSequence+"].selezionato";
    var dataTermineAssegnazione = $("#modaldatepicker").val();
    var dataTermineShow = $("#end-date").val();
    var termineAssegnazione = "devices["+numberSequence+"].termineAssegnazioneString";
    var idSequence = "devices["+numberSequence+"].id";

    $("#"+idDispositivo).parent().remove();
    $("#dispositiviAssociati").find("tbody").append("<tr> <td id="+idDispositivo+" scope='row'><input type='hidden' name="+idSequence+" value="+idDispositivo+" /><input type='hidden' class='paired' name="+sequence+" value=true /><input type='hidden' class='paired' name="+termineAssegnazione+" value="+dataTermineAssegnazione+"/><span>"+numeroSerialeText+"</span> </td>"+
    "<td><span>"+marcaText+"</span></td>"+
	"<td><span>"+modelloText+"</span></td>"+
	"<td><span>"+tipologiaText+"</span></td>"+
    "<td><span>"+btMacAddressText+"</span></td>"+
	"<td><span></span></td>"+
    "<td class='termineAssegnazione'><span>"+dataTermineShow+"</span></td>"+
    "<td class='action'> <span class='btn-sm btn-primary btn-circle btn-icon' ><i class='fas fa-check'  data-toggle='tooltip' data-placement='top' title='' data-original-title='Sensore Associato'></i></span>"+
    "<a href='#' data-id="+idDispositivo+ " data-numeroSeriale=" +numeroSeriale+ " data-marca="+marca+ " data-modello="+modello+" data-tipologia="+tipologia+ " data-btMacAddress="+btMacAddress+" data-sequence="+numberSequence+ 
    " class='btn-sm btn-google btn-circle btn-icon unpairUpdateModal' data-toggle='modal' data-target='#unpairModal'><i class='fas fa-times'  data-toggle='tooltip' data-placement='top' title='' data-original-title='Unpair Sensore'></i></a></td></tr>");   
}



function unpairUpdate() {
    var idDispositivo = $(".modal-body #valoriDispositivo")[0].getAttribute("idDispositivo");
    var numberSequence = $(".modal-body #valoriDispositivo")[0].getAttribute("sequence");
    var numeroSeriale = $(".modal-body #valoriDispositivo")[0].getAttribute("numeroSeriale");
    var numeroSerialeText = numeroSeriale != "null"? numeroSeriale:"";
    var marca = $(".modal-body #valoriDispositivo")[0].getAttribute("marca");
    var marcaText = marca != "null"? marca:"";
    var modello = $(".modal-body #valoriDispositivo")[0].getAttribute("modello");
    var modelloText = modello != "null"? modello:"";
    var tipologia = $(".modal-body #valoriDispositivo")[0].getAttribute("tipologia");
    var tipologiaText = tipologia != "null"? tipologia:"";
    var btMacAddress = $(".modal-body #valoriDispositivo")[0].getAttribute("btMacAddress");
    var btMacAddressText = btMacAddress != "null"? btMacAddress:"";
    var sequence = "devices["+numberSequence+"].selezionato";
    var idSequence = "devices["+numberSequence+"].id";
    $("#"+idDispositivo).parent().remove();
    $("#dispositiviDisponibili").find("tbody").append("<tr> <td id="+idDispositivo+" scope='row'><input type='hidden' name="+idSequence+" value="+idDispositivo+" /><input type='hidden' class='unpaired' name="+sequence+" value=false /><span>"+numeroSerialeText+"</span> </td>"+
    "<td><span>"+marcaText+"</span></td>"+
	"<td><span>"+modelloText+"</span></td>"+
	"<td><span>"+tipologiaText+"</span></td>"+
    "<td><span>"+btMacAddressText+"</span></td>"+
    "<td class='action'><a href='#my_modal' data-id="+idDispositivo+ " data-numeroSeriale=" +numeroSeriale+ " data-marca="+marca+ " data-modello="+modello+" data-tipologia="+tipologia+ " data-btMacAddress="+btMacAddress+" data-sequence="+numberSequence+ 
	" class='btn-sm btn-info btn-circle btn-icon pairUpdateModal' data-toggle='modal' data-target='#AssociaDeviceModal'>"+
	"<i class='fas fa-exchange-alt' data-toggle='tooltip' data-placement='top' title=''"+
    "data-original-title='Associa Sensore'></i></a></td></tr>");
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


$(document).on("click", ".deletePatient", function () {
	  var url = this.getAttribute("href");
     $(".modal-footer #removePatientFromList")[0].setAttribute("href", url);

     // As pointed out in comments, 
     // it is unnecessary to have to manually call the modal.
     // $('#addBookDialog').modal('show');
});

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

