var identification;

$(function() {
	
	var hauteurEntete = $('#gabarit_mobile').height();
	if (hauteurEntete > 80)
		$('#gabarit_mobile').addClass('longMenu');
	else
		$('#gabarit_mobile').removeClass('longMenu');
	
	var open = false;
	var clic = false;
	
	$('#gabarit_mobile span').on('touchstart', function() {
		
		if (clic)
			return false;
		
		clic = true;
		
		if (open) {
			open = false;
			gabaritMobile_fermerMenu();
			
		} else {				
			open = true;
			gabaritMobile_ouvrirMenu();	
		}
		
		setTimeout(function() {
			clic = false;
		}, 300);
	});
	
	$(window).resize(function() {
		/* if (open) {
			open = false;
			gabaritMobile_fermerMenu();
			$('#gabarit_menu_mobile').removeClass('show');
		} */
		calculerHauteurBox();
	});

	function gabaritMobile_ouvrirMenu() {
		$('#gabarit_mobile span').addClass('activeSpan');
		$('#gabarit_menu_mobile ul:not(.gabarit_sousmenu)').show();
		$('#gabarit_menu_mobile a').click(gabaritMobile_clicRubrique);
	}

	function gabaritMobile_fermerMenu() {
		$('#gabarit_mobile span').removeClass('activeSpan');
		$('#gabarit_menu_mobile > ul > li.active_menu').removeClass('active_menu');
		$('#gabarit_menu_mobile ul.gabarit_sousmenu').hide();
	}

	function gabaritMobile_clicRubrique() {	
		var elem = $(this);

		if (elem.attr('data-idrub') == 1 || elem.attr('data-niv') == 2)
			return true;
		
		$('#gabarit_menu_mobile ul.gabarit_sousmenu').hide();
		elem.parent().children('ul.gabarit_sousmenu').show();
		$('#gabarit_menu_mobile > ul > li.active_menu').removeClass('active_menu');
		elem.parent().addClass('active_menu');
		$('#gabarit_menu_mobile a').click(gabaritMobile_clicRubrique);

		return false;
	}
	
	/**** CONNEXION/DECONNEXIO ****/
	
	var titleBox = $('#gabarit_mobile_connexion');
	var beforeBox = $('#connexionMobile');
	var box = $('#connexionMobile_contenu');
	var boxForms = $('#connexionMobile_contenu .englobeForm');
	var boxMsg = $("#formmsgConnexionMobile");
	var boxBtnConn = $("#formbuttonConnexionMobile");
	var boxLoading = $("#loadingConnexionMobile");
	var boxIdt = $("#formloginConnexionMobile");
	var boxPwd = $("#formpassConnexionMobile");
	var actionForm = $('#connexionMobile_contenu .englobeForm.actions');
	var connexionForm = $('#connexionMobile_contenu .englobeForm.connexion');
	
	function verifierConnexion() {
	
		identification = false;
		
		var p = "logged_in";
		var n = "" + document.cookie;
		var o = n.indexOf(p);
		if (o == -1 || p == "") {
			return ""
		}
		var e = n.indexOf(";", o);
		if (e == -1) {
			e = n.length
		}
		var retour = unescape(n.substring(o + p.length + 1, e))
		if (retour != "") 
			identification = true;
	}

	function actionsBox() {
		
		titleBox.addClass('login').html('Mes actions');
		beforeBox.addClass('login');
		box.find('.headerBox').html('Mes actions');		
		
		var f = {};
		f.d = (new Date()).getTime();
		f.smartphone = 'true';
		
		$.post("/user/links", f, function(e) { 
			
			actionForm.html(e);
			
			var deconnexionEnCours = false;
		
			actionForm.on('click', '#repereLogout', function(event) {
				
				event.stopPropagation();
				
				if (deconnexionEnCours)
					return
				
				var f = {};
				f.d = (new Date()).getTime();
				f.smartphone = 'true';
				
				deconnexionEnCours = true;
				
				$.post("/user/logout", f, function(p) {
					identification = false;
					fermerBox();
					connexionBox();					
					deconnexionEnCours = false;
					
					/* deco forum */
					var s = document.createElement('script');
					s.src = 'https://www.developpez.net/forums/anologout.php';
					s.type = 'text/javascript';
					document.body.appendChild(s);						
					
				});	
				
			});
			
		});
		
		boxForms.hide();
		actionForm.show();
	}
	
	function connexionBox() {		
		titleBox.removeAttr('class').html('Se connecter');
		beforeBox.removeAttr('class');
		box.find('.headerBox').html('Identification');
		actionForm.html('').hide();
		connexionForm.show();
	}
	
	verifierConnexion();

	if (identification)
		actionsBox();	
	else
		connexionBox();

	var stopConn = false;
	var validationEnCours = false;
	
	titleBox.on('touchstart', function() { 
		
		if (beforeBox.is(':visible')) {
			fermerBox();
			return;
		}
		
		if (stopConn)
			return;
		
		stopConn = true;
		
		$('body').css('overflow', 'hidden');
		
		beforeBox.show(); 
		
		calculerHauteurBox();
		
	});
	
	$("#fermerConnexionMobile").on('click', function() { fermerBox(); });
	
	loggingFormConnexionMobile();
	
	// fonction de connexion deconnexion 
	function fermerBox() {
		beforeBox.hide();
		box.removeAttr('style');
		if (!identification) {
			boxMsg.empty().hide();
			btnConnexion(false);
		}
		$('body').css('overflow', 'auto');
		stopConn = false;
	}

	function btnConnexion(actif) {
		if (actif)
			boxBtnConn.addClass('actif').val('Connexion en cours... ');
		else
			boxBtnConn.removeClass('actif').val('Je me connecte !');
	}
	
	function messageInfos(o, n) {
		var e = boxMsg;
		e.text(o).hide();
		var p = e.attr("class");
		e.removeClass(p);
		switch (n) {
		case "error":
			e.addClass("msgerror");
			break;
		case "info":
			e.addClass("msginfo");
			break
		}
		e.fadeIn(); 
	}
	
	function validerConnexion() {
		
		if (validationEnCours)
			return
		
		if (boxLoading.is("visible"))
			return
		
		if (connexionForm.is("visible"))
			return
		
		var n = boxIdt.val();
		var q = boxPwd.val();
		if (n == "" || q == "") {
			messageInfos("Veuillez saisir vos données de connexion", "info");
			return
		}
		
		validationEnCours = true;

		var s = {};
		s.d = (new Date()).getTime();
		s.username = n;
		s.password = q;
		s.remindme = 1;
		
		boxMsg.empty().hide();
		
		btnConnexion(true);
		
		boxLoading.show();
		
		try { 
		
			$.post("/user/login", s, function(p) {
				
				try { 
				
					boxIdt.val("");
					boxPwd.val("");
					btnConnexion(false);
					boxLoading.hide();
					
					if (p.state == "success") {
						
						identification = true;
						fermerBox();
						boxForms.hide();
						actionsBox();
						
						var s = document.createElement('script');
						s.src = 'https://www.developpez.net/forums/anologin2.php?rid=' + p.rid;
						s.type = 'text/javascript';
						document.body.appendChild(s);
						
					} else {
						messageInfos(p.message, "error");
					}
					
				} catch (u) {					
					messageInfos("Erreur, Impossible de se connecter", "error");
				}
				
				validationEnCours = false;
				
			});
			
		} catch (o) {
			
			messageInfos("Erreur, impossible de se connecter", "error");
			validationEnCours = false;
			
		}
	}
	
	function loggingFormConnexionMobile() { 

		boxBtnConn.on('click', validerConnexion);	
		boxPwd.keypress(function(n) { 
			if (n.keyCode == 13)
				validerConnexion();
		});
	}
	
	function calculerHauteurBox() {
		
		box.css('max-height', 'none');
		
		var hauteur_fenetre = $(window).height(); 
		var hauteurBox = box.height(); 
		
		if (hauteur_fenetre > (hauteurBox + 50)) {
			
			var calcTop = hauteurBox / 2; 
			box.css('top', 'calc(50% - '+ calcTop +'px)');
			
		} else {
			
			box.css({
				'top': '25px', 
				'max-height': (hauteur_fenetre - 50) +'px'
			});
			
		}
	}

});
