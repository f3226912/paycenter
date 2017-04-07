KISSY.add("gallery/image-dd/1.0/asdbase",
function() {
	function d() {}
	d.prototype = {
		ATTR: function(d, e) {
			if (! (void 0 === d || null === d)) {
				var h = this.ATTRS || {};
				return void 0 === h[d] ? void 0 === e ? void 0 : h[d] = e: void 0 === e ? h[d] : h[d] = e
			}
		},
		DATA: function(d, e) {
			if (! (void 0 === d || null === d)) {
				var h = this.DATAS || {};
				return void 0 === h[d] ? void 0 === e ? void 0 : h[d] = e: void 0 === e ? h[d] : h[d] = e
			}
		},
		STATU: function(d, e) {
			if (! (void 0 === d || null === d)) {
				var h = this.STATUS || {};
				return void 0 === h[d] ? void 0 === e ? void 0 : h[d] = e: void 0 === e ? h[d] : h[d] = e
			}
		}
	};
	return d
});
KISSY.add("gallery/image-dd/1.0/index",
function(d, w, e, h) {
	function s(a) {
		this.config = d.clone(x);
		this.ATTRS = d.clone(o);
		this.STATUS = d.clone(y);
		this.DATAS = d.clone(z);
		d.mix(this.config, {
			ele: a ? d.isArray(a) ? a: [a] : []
		});
		this._init()
	}
	function t(a) {
		a.halt();
		if (m < o.DEGRADATION) return++m,
		!1;
		m = 0;
		var c = p(a),
		a = c.x - this.ATTR("mousedownCoo").x,
		c = c.y - this.ATTR("mousedownCoo").y,
		c = parseInt(this.ATTR("activeImgPos").top, 10) + c,
		a = parseInt(this.ATTR("activeImgPos").left, 10) + a;
		this.ATTR("dragInfoX").push(a);
		this.ATTR("dragInfoY").push(c);
		this.ATTR("dragInfoTime").push((new Date).getTime());
		g.css(this.ATTR("activeImg"), "top", c + "px");
		g.css(this.ATTR("activeImg"), "left", a + "px")
	}
	function i(a) {
		a.halt();
		if (q < o.WHEEL_STEP) return++q,
		!1;
		q = 0;
		this.cleanRecords();
		var c = "";
		a.deltaY && (c = 0 < a.deltaY ? "zoom": "shrunk");
		if ("" == c) return ! 1;
		if (c != u) return u = c,
		!1;
		var b = l(this.ATTR("activeImg"), "width"),
		d = l(this.ATTR("activeImg"), "height");
		if ("shrunk" == c && b < this.ATTR("defaultMinWidth") || "zoom" == c && b > this.ATTR("defaultMaxWidth")) return ! 1;
		var a = p(a),
		r = parseInt(this.ATTR("activeImg").style.left, 10),
		g = parseInt(this.ATTR("activeImg").style.top, 10),
		j = l(this.ATTR("activeImg"), "left"),
		e = l(this.ATTR("activeImg"), "top"),
		f = b,
		n = 3 * o.WHEEL_PIX * (b / this.ATTR("initWidth")),
		i = n * (d / b);
		if (a.x >= r && a.x <= r + b && a.y >= g && a.y <= g + d) {
			var k = 1,
			m = k = 1,
			k = (a.x - r) / b,
			m = (a.y - g) / d;
			"shrunk" == c ? (j += n * k, e += i * m) : (j -= n * k, e -= i * m)
		} else k = 0.5,
		"shrunk" == c ? (j += n * k, e += i * k) : (j -= n * k, e -= i * k);
		f = "shrunk" == c ? f - n: f + n;
		this.ATTR("anim") && this.ATTR("anim").isRunning && this.ATTR("anim").stop(!1);
		this.ATTR("anim", (new h(this.ATTR("activeImg"), {
			left: parseInt(1E3 * j, 10) / 1E3 + "px",
			top: parseInt(1E3 * e, 10) / 1E3 + "px",
			width: parseInt(f, 10) + "px"
		},
		0.1)).run())
	}
	function v(a) {
		var c = p(a),
		a = c.x - this.ATTR("mousedownCoo").x,
		c = c.y - this.ATTR("mousedownCoo").y,
		c = parseInt(this.ATTR("activeImgPos").top, 10) + c,
		a = parseInt(this.ATTR("activeImgPos").left, 10) + a;
		this.ATTR("dragInfoX").push(a);
		this.ATTR("dragInfoY").push(c);
		this.ATTR("dragInfoTime").push((new Date).getTime());
		this.cancelEvent();
		this.afterUserDrag_MyShowTime()
	}
	function p(a) {
		return {
			x: a.pageX || a.clientX + (document.documentElement.scrollLeft || document.body.scrollLeft),
			y: a.pageY || a.clientY + (document.documentElement.scrollTop || document.body.scrollTop)
		}
	}
	function l(a, c) {
		var b = "";
		if (d.UA && d.UA.ie && "IMG" == a.tagName && "height" == c) return a.offsetHeight;
		window.getComputedStyle ? b = window.getComputedStyle(a, null)[c] : (c = A(c), b = a.currentStyle[c]);
		b = b.replace(/[^0-9+-\.]/g, "");
		return parseInt(b, 10) ? 0 - (0 - b) : 0
	}
	function A(a) {
		return a.replace(/-([a-z])/gi,
		function(a, b) {
			return b.toUpperCase()
		})
	}
	var g = d.DOM,
	f = d.Event,
	o = {
		WHEEL_STEP: 1,
		WHEEL_PIX: 50,
		SHOW_DOWN: 0,
		MOUSE_MOVE_AUTO_CLOSE_TIMER: 5E3,
		DEGRADATION: 2,
		activeImg: null,
		activeImgPos: {
			left: 0,
			top: 0
		},
		mousedownCoo: {},
		anim: null,
		initAnim: null,
		bigImgObj: new Image,
		initWidth: 0,
		defaultMaxWidth: 1E4,
		defaultMinWidth: 50,
		ieIframeMask: null,
		dragInfoX: [],
		dragInfoY: [],
		dragInfoTime: [],
		autoSlideAnim: null,
		popupMask: null,
		popupBd: null,
		popupOpacityBg: null,
		popupBox: null,
		popupHd: null
	},
	y = {
		inited: !1
	},
	z = {
		POPUP_HD_TPL: '<div class="box-hd close-rt-wrap" ><a href="#" title="按退出键，也可以关闭哦" class="close-rt J_Close" id="J_CloseImageDD"></a></div>',
		POPUP_IMG: '<img title="鼠标滚轮可以放大图片" class="G_K" style="width:{{showWidth}}px;left:{{left}}px;top:{{top}}px;" src="{{imgSrc}}"  />',
		POPUP_TPL: '<div class="img-dd-mask">                    <div class="img-dd-opacity-bg"></div>                 </div>',
		POPUP_BOX_TPL: '<div class="img-dd-box"></div>',
		POPUP_IFRAME_TPL: '<iframe class="ie-popup-mask hidden"></iframe>'
	},
	x = {
		ele: []
	},
	m = o.DEGRADATION,
	q = o.WHEEL_STEP,
	u = "";
	d.augment(s, w, d.EventTarget, {
		_init: function() {
			var a = this;
			d.each(a.config.ele,
			function(c) {
				a._bindEvent(c)
			})
		},
		add: function(a, c) {
			var b = this,
			e = b.config;
			d.isArray(a) ? (e.ele = e.ele.concate(a), d.each(a,
			function(a) {
				b._bindEvent(a, c)
			})) : (e.ele.push(a), b._bindEvent(a, c))
		},
		_bindEvent: function(a, c) {
			var b = this;
			if (null == a) return ! 1;
			
			if(!a.forUpdate){				
			 f.on(a, "click",
				function(a) {
					var d = a.target;
					"IMG" != d.tagName.toUpperCase() || c && !g.hasClass(d, c) || "G_K" == d.className || (a.halt(), !0 != b.STATU("inited") && (b._createPopup(), b._initHTMLElement(), b._bindPopupMousedown(), b.STATU("inited", !0)), b._showPopupImg(d.getAttribute("data-original-url"), d.getAttribute("src")), g.show(b.ATTR("popupMask")), g.show(b.ATTR("popupBd")), g.show(b.ATTR("ieIframeMask")), g.show(b.ATTR("closeBtn")))
				});
			}else{
				/*如果是存在的数据，则使用则去掉了halt()方法。*/
				$(a).bind("click",function(a){
				   	var d = a.target;
				    "IMG" != d.tagName.toUpperCase() || c && !g.hasClass(d, c) || "G_K" == d.className || ( !0 != b.STATU("inited") && (b._createPopup(), b._initHTMLElement(), b._bindPopupMousedown(), b.STATU("inited", !0)), b._showPopupImg(d.getAttribute("data-original-url"), d.getAttribute("src")), g.show(b.ATTR("popupMask")), g.show(b.ATTR("popupBd")), g.show(b.ATTR("ieIframeMask")), g.show(b.ATTR("closeBtn")))
				});
			}
		 
			
		 
			
			
		},
		_showPopupImg: function(a, c) {
			var b = this,
			f = document.body.clientWidth || doucment.doucmentElement.clientWidth;
			b.ATTR("popupBd").innerHTML = e(b.DATA("POPUP_IMG")).render({
				imgSrc: c,
				imgAlt: "图片大图",
				showWidth: parseInt(f / 2, 10),
				left: parseInt(f / 4, 10),
				top: 0
			});
			d.UA.ie && 6 == d.UA.ie && (b.ATTR("popupMask").style.height = (document.body.scrollHeight || document.documentElement.scrollHeight) + "px", b.ATTR("popupMask").style.width = (document.body.scrollWidth || document.documentElement.scrollWidth) + "px");
			b.ATTR("initWidth", parseInt(f / 2, 10));
			b.cleanRecords(!0);
			b.ATTR("activeImg", g.get("IMG", b.ATTR("popupBd")));
			b.ATTR("initAnim") && b.ATTR("initAnim").stop(!1);
			b.ATTR("initAnim", (new h(b.ATTR("activeImg"), {
				top: (document.body.scrollTop || document.documentElement.scrollTop) + 30 + "px"
			},
			1, "easeOutStrong")).run());
			b.ATTR("bigImgObj").onload = null;
			if (a || "" != a) b.ATTR("bigImgObj", null),
			b.ATTR("bigImgObj", new Image),
			b.ATTR("bigImgObj").onload = function() {
				b.ATTR("activeImg").src = a
			};
			b.ATTR("bigImgObj").src = a;
			b.registerWheelEvent()
		},
		registerWheelEvent: function() {
			f.on(document, "DOMMouseScroll", i, this);
			f.on(document, "mousewheel", i, this);
			f.on(document, "keyup", this.closePopup, this)
		},
		cancelWheelEvent: function() {
			f.remove(document, "DOMMouseScroll", i, this);
			f.remove(document, "mousewheel", i, this);
			f.remove(document, "keyup", this.closePopup, this)
		},
		registerEvent: function() {
			f.on(document, "mouseup", v, this);
			f.on(document, "mousemove", t, this)
		},
		cancelEvent: function() {
			f.remove(document, "mouseup", v, this);
			f.remove(document, "mousemove", t, this)
		},
		_createPopup: function() {
			this.ATTR("popupMask", g.create(this.DATA("POPUP_TPL")));
			this.ATTR("popupBd", g.create(this.DATA("POPUP_BOX_TPL")));
			this.ATTR("closeBtn", g.create(this.DATA("POPUP_HD_TPL")));
			d.UA.ie && 6 == d.UA.ie && (this.ATTR("ieIframeMask", g.create(this.DATA("POPUP_IFRAME_TPL"))), this.ATTR("ieIframeMask").style.width = document.documentElement.scrollWidth + "px", this.ATTR("ieIframeMask").style.height = document.documentElement.scrollHeight + "px", document.body.appendChild(this.ATTR("ieIframeMask")));
			document.body.appendChild(this.ATTR("popupMask"));
			document.body.appendChild(this.ATTR("popupBd"));
			this.ATTR("popupMask").appendChild(this.ATTR("closeBtn"))
		},
		_bindPopupMousedown: function() {
			var a = this;
			f.on(a.ATTR("popupBd"), "mousedown",
			function(c) {
				var b = c.target;
				"IMG" == b.tagName.toUpperCase() && (c.halt(), a.ATTR("initAnim") && a.ATTR("initAnim").stop(!1), a.ATTR("initAnim", null), a.cleanRecords(!0), a.ATTR("dragInfoX").push(l(b, "left")), a.ATTR("dragInfoY").push(l(b, "top")), a.ATTR("dragInfoTime").push((new Date).getTime()), a.ATTR("activeImg", b), a.ATTR("mousedownCoo", p(c)), a.ATTR("activeImgPos", {
					left: l(b, "left"),
					top: l(b, "top")
				}), a.registerEvent())
			});
			f.on(a.ATTR("closeBtn"), "click",
			function(c) {
				c.halt();
				a.closePopup()
			});
			f.on(a.ATTR("popupMask"), "click",
			function(c) {
				d.UA.ie && 6 == d.UA.ie && g.hasClass(c.target, "img-dd-opacity-bg") && (c.halt(), a.closePopup())
			});
			return ! 0
		},
		_initHTMLElement: function() {
			this.ATTR("popupOpacityBg", g.get(".img-dd-opacity-bg", this.ATTR("popupMask")));
			this.ATTR("popupBox", g.get(".img-dd-box", this.ATTR("popupMask")));
			this.ATTR("popupHd", g.get(".box-hd", this.ATTR("popupMask")))
		},
		afterUserDrag_MyShowTime: function() {
			this.ATTR("dragInfoX");
			this.slide_straightLine();
			return ! 1
		},
		slide_straightLine: function() {
			var a = 0,
			c = 0,
			b, d, e, g = this.ATTR("dragInfoX");
			e = this.ATTR("dragInfoY");
			var f = g.length,
			a = this.ATTR("dragInfoTime"),
			c = 1;
			for (t0 = a[f - 1]; c < f && 50 > t0 - a[f - ++c];);
			2 == c && 500 < t0 - a[f - c] || (c <= f ? (t1 = a[f - c + 1], g = g[f - 1] - g[f - c], e = e[f - 1] - e[f - c], a = 1E3 * (g / (t0 - t1)), c = 1E3 * (e / (t0 - t1)), 0 < g ? b = !0 : b = !1, 0 < e ? d = !0 : d = !1) : (c = a = 100, g[f - 1] > g[0] ? b = !0 : b = !1, e[f - 1] > e[0] ? d = !0 : d = !1), e = parseInt(Math.abs(NaN), 10), b = (b ? "+=": "-=") + a * a / 2 / 1E5, d = (d ? "+=": "-=") + c * c / 2 / 1E5, this.ATTR("autoSlideAnim", (new h(this.ATTR("activeImg"), {
				left: b,
				top: d
			},
			e, "easeOutStrong")).run()))
		},
		cleanRecords: function() {
			this.ATTR("dragInfoX", []);
			this.ATTR("dragInfoY", []);
			this.ATTR("dragInfoTime", []);
			this.ATTR("autoSlideAnim") && this.ATTR("autoSlideAnim").isRunning && this.ATTR("autoSlideAnim").stop(!1)
		},
		closePopup: function(a) {
			if (void 0 != a && a instanceof KISSY.EventObject) {
				var c = a.keyCode || a.charCode;
				if (a.altKey || 27 != c) return ! 1
			}
			this.ATTR("activeImg", null);
			this.cancelWheelEvent();
			this.cancelEvent();
			g.hide(this.ATTR("popupMask"));
			g.hide(this.ATTR("popupBd"));
			g.hide(this.ATTR("closeBtn"));
			g.hide(this.ATTR("ieIframeMask"))
		},
		destory: function() {}
	});
	return s
},
{
	requires: ["./asdbase", "gallery/template/1.0/index", "anim", "./assets/image-dd.css"]
});