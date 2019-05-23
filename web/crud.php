<?php

  include ("DataBase.php");

  $db = new BancoDados(1);

  //EDITAR COMPONENTES
  $pag=isset($_GET['pag'])?$_GET['pag']:'';

  if($pag == 'add'){

    $avaliacao = $_POST['AVALIACAO'];
    $comentario = $_POST['COMENTARIO'];

    $sql = "INSERT INTO Avaliacao VALUES(null, '$avaliacao', '$comentario')";
    $array = $db->insertDB($sql);

  }


?>
<!-- ICONES -->
<script src="../JS/feather.min.js"></script>
<script>
  feather.replace();
</script>
