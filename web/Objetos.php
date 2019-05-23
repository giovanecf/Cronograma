<?php
 class Tarefa{
    
    public $id;
    public $nome;
    public $foto;
    public $totalEtapasCompletas;
    public $totalEtapas;
    public $dataFimTarefa;
    public $usuario;
    public $etapas;

    /*Método construtor do banco de dados*/

    public function __construct($id, $nome, $foto, $totalEtapasCompletas, $totalEtapas, $dataFimTarefa, $etapas){

        $this->id = $id;
        $this->nome = $nome;
        $this->foto = $foto;
        $this->totalEtapasCompletas = $totalEtapasCompletas;
        $this->totalEtapas = $totalEtapas;
        $this->dataFimTarefa = $dataFimTarefa;
        $this->etapas = $etapas;
        //$this->usuario = $usuario;
    }

    /*Evita que a classe seja clonada*/
    private function __clone(){}

    /*Método que destroi a conexão com banco de dados e remove da memória todas as variáveis setadas*/
    public function __destruct() {}


    /*Metodos que trazem o conteudo da variavel desejada
    @return   $xxx = conteudo da variavel solicitada*/
    public function getID(){return $this->id;}
    public function getNome(){return $this->nome;}
    public function getFoto(){return $this->foto;}
    public function getTotalEtapasCompletas(){return $this->totalEtapasCompletas;}
    public function getTotalEtapas(){return $this->totalEtapas;}
    public function getDataFimEtapa(){return $this->dataFimTarefa;}
    public function getUsuario(){return $this->usuario;}

    public function setID( $var ){$this->id = $var;}
    public function setNome( $var ){$this->nome = $var;}
    public function setFoto( $var ){$this->foto = $var;}
    public function setTotalEtapasCompletas( $var ){return $this->totalEtapasCompletas = $var;}
    public function setTotalEtapas( $var ){$this->totalEtapas = $var;}
    public function setDataFimEtapa( $var ){$this->dataFimTarefa = $var;}
    public function setUsuario( $var ){$this->usuario = $var;}

}

class Etapa{
    
    public $id;
    public $nome;
    public $dataVencimento;
    public $completado;
    public $idTarefa;

    /*Método construtor do banco de dados*/

    public function __construct($id, $nome, $dataVencimento, $completado, $idTarefa){

        $this->id = $id;
        $this->nome = $nome;
        $this->dataVencimento = $dataVencimento;
        $this->completado = $completado;
        $this->idTarefa = $idTarefa;
    }

    /*Evita que a classe seja clonada*/
    private function __clone(){}

    /*Método que destroi a conexão com banco de dados e remove da memória todas as variáveis setadas*/
    public function __destruct() {}


    /*Metodos que trazem o conteudo da variavel desejada
    @return   $xxx = conteudo da variavel solicitada*/
    public function getID(){return $this->id;}
    public function getNome(){return $this->nome;}
    public function getDataVencimento(){return $this->dataVencimento;}
    public function getCompletado(){return $this->completado;}
    public function getIDTarefa(){return $this->idTarefa;}

    public function setID( $var ){$this->id = $var;}
    public function setNome( $var ){$this->nome = $var;}
    public function setDataVencimento( $var ){$this->dataVencimento = $var;}
    public function setCompletado( $var ){return $this->completado = $var;}
    public function setIDTarefa( $var ){$this->idTarefa = $var;}

}

?>
