<?php
    require_once('Objetos.php');
    require_once('DataBase.php');
    require_once('File.php');

    $db = new BancoDados(2);

    // HACK CODE
        $_SG = empty($_POST) ? $_GET : $_POST;
        $_SG = empty($HTTP_RAW_POST_DATA) ? $_SG : $HTTP_RAW_POST_DATA;
        $_SG['method'] = trim($_SG['method'], '"');
        if( !is_array( $_SG ) ){
            $_SG = json_decode($_SG, true);
        }
    if( strcasecmp( $_SG['method'], 'obter-tudo' ) == 0 ){

        $arrayTarefas = [];
        $arrayEtapas = [];
        $query = "SELECT * FROM Tarefa ORDER BY nome ASC;";
        $array = $db->selectDB($query);

        if( $array != null){
                foreach ($array as $value) {
                        $usuario=$value->USUARIO;
                        if( strcasecmp( $_SG['usuario'], $usuario ) == 0 ){
                                $cod=$value->COD;
                                $id=$value->ID;
                                $nome=$value->NOME;
                                $foto=$value->FOTO;
                                $totalEtapas=$value->TOTALETAPAS;
                                $totalEtapasCompletas=$value->TOTALETAPASCOMPLETAS;
                                $dataFimTarefa=$value->DATAFIMTAREFA;

                                $query = "SELECT * FROM Etapa WHERE IDTAREFA = $cod;";
                                $eArray = $db->selectDB($query);
                                foreach ($eArray as $key => $eValue) {
                                    
                                    $etapa = new Etapa($eValue->ID, $eValue->NOME, $eValue->DATAVENCIMENTO, $eValue->COMPLETADO, $eValue->IDTAREFA);
                                    $arrayEtapas[] = $etapa; 
                                }
                                $tarefa = new Tarefa($cod, $nome, $foto, $totalEtapas, $totalEtapasCompletas, $dataFimTarefa, $arrayEtapas);
                                $arrayTarefas[] = $tarefa;   
                        }
                }
        
        }

        header('Content-Type: application/json; charset=utf-8');
        echo json_encode( $arrayTarefas );

    }else if( strcasecmp( $_SG['method'], 'mandar-tudo' ) == 0 ){

        // LIMPANDO BANCO
        $dataTarefa = json_decode($_SG['tarefas'], true);
        $dataEtapa = json_decode($_SG['etapas'], true);

        File::writeInFile($_SG['tarefas'].$_SG['etapas'], 'w', 'texto.txt');

        $USUARIO = $dataTarefa[0]["usuario"];

        $query = "SELECT * FROM Tarefa WHERE USUARIO = '$USUARIO'";
        $arrayTarefa = $db->selectDB($query);
        
        if( sizeof($arrayTarefa) > 0 ){
        
            foreach ($arrayTarefa as $key => $value) {
                
                $query = "SELECT * FROM Etapa WHERE IDTAREFA = $value->ID;";
                $arrayEtapas = $db->selectDB($query);

                if(sizeof($arrayEtapas) > 0){
                    echo "AAaaa".$value->ID;
                    $delSql = "DELETE FROM Etapa WHERE IDTAREFA = $value->ID;";
                    $db->deleteDB($delSql);
                }    
            }

            $delSql = "DELETE FROM Tarefa WHERE USUARIO = '$USUARIO';";
            $db->deleteDB($delSql);
        }
        

        // TAREFA

        foreach ($dataTarefa as $key => $value) {

                $ID=$value["id"];
                $nome=$value["nome"];
                $foto=$value["foto"];
                $totalEtapas=$value["totalEtapas"];
                $totalEtapasCompletas=$value["totalEtapasCompletas"];
                $dataFimTarefa=$value["dataFimTarefa"];
                $usuario=$value["usuario"];

                $sql = "INSERT INTO Tarefa VALUES(null, $ID, '$nome', $foto, $totalEtapas, $totalEtapasCompletas, '$dataFimTarefa', '$usuario')";
                $CODIGO = $db->insertDB($sql);

        // ETAPA
                foreach ($dataEtapa as $key => $value) {
                    
                    $idTarefa=$value["idTarefa"];
                    if($ID == $idTarefa){
                        $id=$value["id"];
                        $nome=$value["nome"];
                        $dataVencimento=$value["dataVencimento"];
                        $completado=$value["completada"];

                        echo "DATA: ".$dataVencimento;

                        $sql = "INSERT INTO Etapa VALUES(null, $id, '$nome', '$dataVencimento', $completado, $CODIGO)";
                        $db->insertDB($sql);
                    }

                }
        }
    }
        
    // PRINT
        $query = "SELECT * FROM Tarefa ORDER BY USUARIO ASC;";
        $array = $db->selectDB($query);
        foreach ($array as $value) {

                echo "=========ITEM=========<br/>";
                echo "COD:".$value->COD."<br/>";
                echo "ID:".$value->ID."<br/>";
                echo "NOME:".$value->NOME."<br/>";
                echo "DATA:".$value->DATAFIMTAREFA."<br/>";
                echo "USUARIO:".$value->USUARIO."<br/>";
                
                $query = "SELECT * FROM Etapa WHERE IDTAREFA = $value->COD ORDER BY DATAVENCIMENTO;";
                $subArray = $db->selectDB($query);

                echo "=========SUB-ITENS=========<br/>";
                foreach ($subArray as $key => $subValue) {
                    echo "| -------ETAPA-------<br/>";
                    echo "| COD:".$subValue->COD."<br/>";
                    echo "| ID:".$subValue->ID."<br/>";
                    echo "| NOME:".$subValue->NOME."<br/>";
                    echo "| DATA:".$subValue->DATAVENCIMENTO."<br/>";
                    echo "| IDTAREFA:".$subValue->IDTAREFA."<br/>";
                }
                echo "<br/><br/>";

        }
        echo "<br/>TOTAL: ".sizeof($array)."<br/><br/>";
?>