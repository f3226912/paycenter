<%
	String BASE_PATH = "/";
    String path = request.getContextPath();
    String CONTEXT = request.getScheme() + "://"
        + request.getServerName() + ":" + request.getServerPort()
        + path + "/";
	request.setAttribute("CONTEXT",CONTEXT);

  	String imgUrl = "http://imgxlc.zym999.com/";
	request.setAttribute("imgUrlHost",imgUrl);
%>
