//if (window.console) {
//  console.log("Welcome to your Play application's JavaScript!");
//}

 $(function() {
	$("#user-profile-lnk").on("click", "div img", function () {
		console.log("mailbox clicked");
		return false;
	});
});