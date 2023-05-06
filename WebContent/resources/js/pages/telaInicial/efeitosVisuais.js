

function exibirModal() {
  $('#myModal').modal('show');
}

function fecharModal() {
  $('#myModal').modal('hide');
}

function emitirSectionFormShow(){
    hiddenSectionFormValida(); 
    changeColorDivNavBarValida();
    changeColorALinkNavBarValida();
}

function validaSectionFormShow(){
    hiddenSectionFormEmitir();
    changeColorDivNavbarEmitir();
    changeColorALinkNavBarEmitir();
}

function hiddenSectionFormEmitir(){
    document.getElementById("Valida").style.display = 'block';
    document.getElementById("Emitir").style.display = 'none';
}

function hiddenSectionFormValida(){
    document.getElementById("Emitir").style.display = 'block';
    document.getElementById("Valida").style.display = 'none';
}

function changeColorDivNavBarValida(){
    document.getElementById("validaSection").classList.add("bg-white");
    document.getElementById("emitirSection").classList.remove("bg-white");
}

function changeColorDivNavbarEmitir(){
    document.getElementById("emitirSection").classList.add("bg-white");
    document.getElementById("validaSection").classList.remove("bg-white");
}

function changeColorALinkNavBarEmitir(){
    document.getElementById("emitirNavbar").classList.add("text-light");
    document.getElementById("emitirNavbar").classList.remove("text-dark");
    document.getElementById("validaNavbar").classList.remove("text-light");
    document.getElementById("validaNavbar").classList.add("text-dark");
}

function changeColorALinkNavBarValida(){
    document.getElementById("validaNavbar").classList.add("text-light");
    document.getElementById("validaNavbar").classList.remove("text-dark");
    document.getElementById("emitirNavbar").classList.remove("text-light");
    document.getElementById("emitirNavbar").classList.add("text-dark");
}

function testeNessaMerda(){
	console.log("Teste");
}