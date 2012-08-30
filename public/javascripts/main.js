
$(document).ready(function() {
	
	var pusher = new Pusher('95189161bfb8e7276632'); // Replace with your app key
	var channel = pusher.subscribe('gmail-channel');
	
	channel.bind('notification', function(data) {
		console.log(data);
		$('#result').append('!GMail Inbox change on ' + new Date().toString() + '<br>');
	});	
	
	$.get('/notification', function(data) {
//		console.log(data);
	});
});
