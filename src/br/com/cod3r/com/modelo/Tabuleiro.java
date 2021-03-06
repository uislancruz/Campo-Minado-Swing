package br.com.cod3r.com.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{
	
	private int linhas;
	private int colunas;
	private int minas;
	
	private final List<Campo> campos = new ArrayList<Campo>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	public void abrir(int linha, int coluna) {
		try {
			campos.parallelStream()
			.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
			.findFirst()
			.ifPresent(c -> c.abrir());
			
		} catch (Exception e) {
			//FIXME ajustar a implementação do metodo abrir
			campos.forEach(c -> c.setAberto(true));
			throw e;
		} 
		
	}
	
	public void alternarMarcacao(int linha, int coluna) {
		campos.parallelStream()
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		.findFirst()
		.ifPresent(c -> c.alternarMarcacao());
		
	}

	
	private void gerarCampos() {
		for (int linhaFor = 0; linhaFor < linhas; linhaFor++) {
			for (int colunaFor = 0; colunaFor < colunas; colunaFor++) {
				Campo campo = new Campo(linhaFor, colunaFor);
				campo.registrarObservador(this);
				campos.add(campo);
			}
			
		}
	}
	
	private void associarVizinhos() {
		for(Campo c1:campos) {
			for(Campo c2 : campos) {
			c1.adicionarVizinho(c2);
			}
		}
	
	}
	
	private void sortearMinas() {
		long minasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		do {
			int aleatorio = (int) (Math.random()* campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream().filter(minado).count();
		}while(minasArmadas < minas);
	}
	
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado()); 
		
	}
	
	public void reiniciar() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
		
	}
	

	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if(evento == CampoEvento.EXPLODIR) {
			
			System.out.println("Perdeu....");
			
		}else if(objetivoAlcancado()) {
			
			System.out.println("Ganhou.....");
			
		}
		
	}	 
}
