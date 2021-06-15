package it.polito.tdp.genes.model;

/*
* classe Adiacenza preimpostata 
* questo documento è soggetto ai relativi diritti di ©Copyright
* giugno 2021
*/ 

public class Adiacenza
{
	private Genes a1;
	private Genes a2;
	private Double peso;
	public Adiacenza(Genes a1, Genes a2, Double peso)
	{
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Genes getA1()
	{
		return a1;
	}
	public Genes getA2()
	{
		return a2;
	}
	public Double getPeso()
	{
		return peso;
	}
	@Override public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a1 == null) ? 0 : a1.hashCode());
		result = prime * result + ((a2 == null) ? 0 : a2.hashCode());
		return result;
	}
	@Override public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Adiacenza other = (Adiacenza) obj;
		if (a1 == null)
		{
			if (other.a1 != null) return false;
		}
		else if (!a1.equals(other.a1)) return false;
		if (a2 == null)
		{
			if (other.a2 != null) return false;
		}
		else if (!a2.equals(other.a2)) return false;
		return true;
	} 
	@Override public String toString()
	{
		return String.format("%s - %s (%d)", a1, a2, peso);
	}  
}