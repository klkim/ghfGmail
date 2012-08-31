
$(document).ready(function() {

});

var pusher = new Pusher('95189161bfb8e7276632'); // Replace with your app key

$('#notificationEnabled').on('click', function() {
	var _privateChannel = $('#signInID').val().split('@')[0];
	
	//subscribe
	var channel = pusher.subscribe(_privateChannel);		
	channel.bind('notification', function(data) {
		console.log(data);
		$('#result').append('!GMail Inbox change on ' + new Date().toString() + '<br>');
	});
	
	//invoke server		
	var url = '/notification?channel=' + _privateChannel;
	
	$.get(url, function(data) {			
		alert(data);
	});
});

$('#notificationDisabled').on('click', function() {
	
});