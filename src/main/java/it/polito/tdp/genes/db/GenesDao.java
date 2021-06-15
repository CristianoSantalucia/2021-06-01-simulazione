package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;

public class GenesDao
{

	public List<Genes> getAllGenes()
	{
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{

				Genes genes = new Genes(res.getString("GeneID"), res.getString("Essential"), res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void getVertici(Map<String, Genes> vertici)
	{
		String sql = "SELECT g.* "
				+ "FROM genes g "
				+ "WHERE g.Essential = 'Essential' "
				+ "GROUP BY g.GeneID ";

		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{
				Genes genes = new Genes(res.getString("GeneID"), res.getString("Essential"), res.getInt("Chromosome"));
				vertici.putIfAbsent(genes.getGeneId(), genes);
			}
			res.close();
			st.close();
			conn.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<Adiacenza> getAdiacenze (Map<String, Genes> vertici)
	{
		String sql = "SELECT i.GeneID1 id1, i.GeneID2 id2, ABS(i.Expression_Corr) peso "
					+ "FROM interactions i "
					+ "WHERE i.GeneID1 <> i.GeneID2 "
					+ "		 AND i.GeneID1 IN (SELECT g.GeneID "
					+ "						    FROM genes g "
					+ "						    WHERE g.Essential = 'Essential' "
					+ "							GROUP BY g.GeneID) "
					+ "		 AND i.GeneID2 IN (SELECT g.GeneID "
					+ "							FROM genes g "
					+ "							WHERE g.Essential = 'Essential' "
					+ "							GROUP BY g.GeneID) "
					+ "GROUP BY i.GeneID1, i.GeneID2" ;

		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try
		{
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next())
			{
				Genes g1 = vertici.get(res.getString("id1"));
				Genes g2 = vertici.get(res.getString("id2"));
				Double peso = res.getDouble("peso");
				Adiacenza a = new Adiacenza(g1, g2, peso);
				if (!result.contains(a))
					result.add(a);
			}
			res.close();
			st.close();
			conn.close();
			return result; 
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null; 
		}
	}
}
