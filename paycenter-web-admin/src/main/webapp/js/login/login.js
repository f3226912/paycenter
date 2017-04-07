function setBgSize() {
	var loginBg = document.getElementById('loginBg');
	loginBg.style.width = document.body.clientWidth;
	loginBg.style.height = document.body.clientHeight;
}

window.onload = window.onresize = setBgSize;