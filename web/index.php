<!DOCTYPE html>
<html>
<head>
	<title>NOISELESS</title>
	<link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet">
    <!-- Bootstrap core CSS -->
    <link href="Estilo/bootEstilo.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="Estilo/customBootEstilo.css" rel="stylesheet">

        <style type="text/css">
		body{
                        font-family: 'Roboto', sans-serif;
			padding: 0px;
			margin: 0px;
			background: #D50000; 
			color:  #fff;
		}
		h1 a{
			color:  #fff;
			text-decoration: none;
		}
		a:hover { 
    		text-decoration: underline;
    		color:  #ddd;
		}

		#snackbar {
	    visibility: hidden; /* Hidden by default. Visible on click */
	    min-width: 250px; /* Set a default minimum width */
	    margin-left: -125px; /* Divide value of min-width by 2 */
	    background-color: #333; /* Black background color */
	    color: #fff; /* White text color */
	    text-align: center; /* Centered text */
	    border-radius: 2px; /* Rounded borders */
	    padding: 16px; /* Padding */
	    position: fixed; /* Sit on top of the screen */
	    z-index: 1; /* Add a z-index if needed */
	    left: 50%; /* Center the snackbar */
	    bottom: 30px; /* 30px from the bottom */
	}

	/* Show the snackbar when clicking on a button (class added with JavaScript) */
	#snackbar.show {
	    visibility: visible; /* Show the snackbar */

	/* Add animation: Take 0.5 seconds to fade in and out the snackbar.
	However, delay the fade out process for 2.5 seconds */
	    -webkit-animation: fadein 0.5s, fadeout 0.5s 2.5s;
	    animation: fadein 0.5s, fadeout 0.5s 2.5s;
	}

	/* Animations to fade the snackbar in and out */
	@-webkit-keyframes fadein {
	    from {bottom: 0; opacity: 0;}
	    to {bottom: 30px; opacity: 1;}
	}

	@keyframes fadein {
	    from {bottom: 0; opacity: 0;}
	    to {bottom: 30px; opacity: 1;}
	}

	@-webkit-keyframes fadeout {
	    from {bottom: 30px; opacity: 1;}
	    to {bottom: 0; opacity: 0;}
	}

	@keyframes fadeout {
	    from {bottom: 30px; opacity: 1;}
	    to {bottom: 0; opacity: 0;}
	}
	</style>
</head>
<body>
	<div class"container-fluid">
	<h1 align="center"><a href="#">Baixar NOISELESS 0.0.1</a></h1>
    <h1 align="center"><a href="#">Baixar NOISELESS 0.0.2</a></h1>
    <h1 align="center"><a href="apks/cronograma0.2.4.zip">Baixar CRONOGRAMA 0.2.4</a></h1>
<br/>
<br/>
    <form>
      <div class="row">
      	<div class="col-sm-4"></div>
      	<div class="col-sm-4">
			<a href="javascript:void(0)" onclick="Avaliar(1)">
			<img src="img/star0.png" id="s1"></a>

			<a href="javascript:void(0)" onclick="Avaliar(2)">
			<img src="img/star0.png" id="s2"></a>

			<a href="javascript:void(0)" onclick="Avaliar(3)">
			<img src="img/star0.png" id="s3"></a>

			<a href="javascript:void(0)" onclick="Avaliar(4)">
			<img src="img/star0.png" id="s4"></a>

			<a href="javascript:void(0)" onclick="Avaliar(5)">
			<img src="img/star0.png" id="s5"></a>
			<p id="rating"><big>Sem Avaliação</big></p>
		</div>
		<div class="col-sm-4"></div>
		<div class="col-sm-4"></div>
          <div class="col-sm-4">
      		<label for="comment">Comentário:</label>
      		<textarea class="form-control" rows="5" cols="50" id="COMENTARIO" placeholder="Opcional..."></textarea>
    	  </div>
    	  <div class="col-sm-4"></div>
    	  <div class="col-sm-4"></div>
    	   <div class="col-sm-1" style="margin-top: 6px;">
            	<button type="submit" onClick="add()" class="btn btn-primary">Avaliar !</button>
            </div>

    	  <div class="col-sm-7"></div>
          </div>
      </form>
    </div>

    <div id="snackbar">Obg pela ajuda !</div>
</body>

 <script src="JS/jQuery.js"></script>

<script>

	var AVALIACAO = 0;

      function add(){
        
        var avaliacao = AVALIACAO;
        var comentario = $('#COMENTARIO').val();

        var dados ="AVALIACAO="+avaliacao+"&COMENTARIO="+comentario;

        $.ajax({
          type: "POST",
          url: "crud.php?pag=add",
          data: dados,
          sucess:function(data){
           salvo();
          }
        });
        salvo();
      }

	 function Avaliar(estrela) {
	 var url = window.location;
	 url = url.toString()
	 url = url.split("index.html");
	 url = url[0];

	 var s1 = document.getElementById("s1").src;
	 var s2 = document.getElementById("s2").src;
	 var s3 = document.getElementById("s3").src;
	 var s4 = document.getElementById("s4").src;
	 var s5 = document.getElementById("s5").src;
	 var texto = "";

	if (estrela == 5){ 
	 if (s5 == url + "img/star0.png") {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star1.png";
	 document.getElementById("s3").src = "img/star1.png";
	 document.getElementById("s4").src = "img/star1.png";
	 document.getElementById("s5").src = "img/star1.png";
	 texto = "USO TODA HORA";
	 avaliacao = 5;
	 } else {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star1.png";
	 document.getElementById("s3").src = "img/star1.png";
	 document.getElementById("s4").src = "img/star1.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "USO ALGUMAS VEZES";
	 avaliacao = 4;
	}}
	 
	 //ESTRELA 4
	if (estrela == 4){ 
	 if (s4 == url + "img/star0.png") {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star1.png";
	 document.getElementById("s3").src = "img/star1.png";
	 document.getElementById("s4").src = "img/star1.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "USO ALGUMAS VEZES";
	 avaliacao = 4;
	 } else {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star1.png";
	 document.getElementById("s3").src = "img/star1.png";
	 document.getElementById("s4").src = "img/star0.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "DÁ PRA USAR";
	 avaliacao = 3;
	}}

	//ESTRELA 3
	if (estrela == 3){ 
	 if (s3 == url + "img/star0.png") {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star1.png";
	 document.getElementById("s3").src = "img/star1.png";
	 document.getElementById("s4").src = "img/star0.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "DÁ PRA USAR";
	 avaliacao = 3;
	 } else {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star1.png";
	 document.getElementById("s3").src = "img/star0.png";
	 document.getElementById("s4").src = "img/star0.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "IDEIA BOA, MAS NÃO DÁ PRA USAR";
	 avaliacao = 2;
	}}
	 
	//ESTRELA 2
	if (estrela == 2){ 
	 if (s2 == url + "img/star0.png") {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star1.png";
	 document.getElementById("s3").src = "img/star0.png";
	 document.getElementById("s4").src = "img/star0.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "IDEIA BOA, MAS NÃO DÁ PRA USAR";
	 avaliacao = 2;
	 } else {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star0.png";
	 document.getElementById("s3").src = "img/star0.png";
	 document.getElementById("s4").src = "img/star0.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "IDEIA RUIM OU/E APP HORRIVEL";
	 avaliacao = 1;
	}}
	 
	 //ESTRELA 1
	if (estrela == 1){ 
	 if (s1 == url + "img/star0.png") {
	 document.getElementById("s1").src = "img/star1.png";
	 document.getElementById("s2").src = "img/star0.png";
	 document.getElementById("s3").src = "img/star0.png";
	 document.getElementById("s4").src = "img/star0.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "IDEIA RUIM OU/E APP HORRIVEL";
	 avaliacao = 1;
	 } else {
	 document.getElementById("s1").src = "img/star0.png";
	 document.getElementById("s2").src = "img/star0.png";
	 document.getElementById("s3").src = "img/star0.png";
	 document.getElementById("s4").src = "img/star0.png";
	 document.getElementById("s5").src = "img/star0.png";
	 texto = "LIXO";
	 avaliacao = 0;
	}}
	 
	 AVALIACAO = avaliacao;
	 document.getElementById('rating').innerHTML = texto;
	 
	}

	function salvo() {
    // Get the snackbar DIV
    var x = document.getElementById("snackbar");

    // Add the "show" class to DIV
    x.className = "show";

    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 6000);
	}	 
</script>

</html>
