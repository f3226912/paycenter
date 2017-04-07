var returnUrl = $("#returnUrl").val();
function viewOrder(){	
	$('#successForm').attr("action", returnUrl).submit();
	    	
	//window.webkit.messageHandlers.NativeMethod.postMessage("finish");
	
	
}
