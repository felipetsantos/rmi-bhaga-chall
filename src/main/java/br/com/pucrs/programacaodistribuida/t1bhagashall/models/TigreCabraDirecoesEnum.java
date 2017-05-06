package br.com.pucrs.programacaodistribuida.t1bhagashall.models;

public enum TigreCabraDirecoesEnum {
	PARA_DIREITA(0),
	PARA_DIREITA_E_PARA_BAIXO(1),
	PARA_BAIXO(2),
	PARA_ESQUERDA_E_PARA_BAIXO(3),
	PARA_ESQUERDA(4),
	PARA_ESQUERDA_PARA_CIMA(5),
	PARA_CIMA(6),
	PARA_DIREITA_E_PARA_CIMA(7);
	
	
	private final int codigo;
	TigreCabraDirecoesEnum (final int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() { return codigo; }
    
    public static TigreCabraDirecoesEnum getDirecao(int codigo) {
        for(TigreCabraDirecoesEnum e : values()) {
            if (e.getCodigo() == codigo)
                return e;
        }
        return null;
    }
}
