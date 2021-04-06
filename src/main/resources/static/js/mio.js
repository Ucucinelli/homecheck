$(function() {
	$("#datepicker").datepicker({
		dateFormat: "dd/mm/yy",
		onSelect: function(date) {
			//defined your own method here
		}
	});
	$("#datepicker").attr("autocomplete", "off");
})

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

