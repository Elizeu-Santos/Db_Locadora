import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DaoClientePJ {
    private Connection conn;
    private Statement st;

    private void conectar(){
        try {
            this.conn = GerenciadorConexao.pegarConexao();
            st = conn.createStatement();
        } catch (ClassNotFoundException e1) {
            System.out.println(e1.getMessage());
        }catch (SQLException e2) {
            System.out.println(e2.getMessage());
        }
    }

    private void desconectar(){
        try {
            st.close();
            conn.close();
        }catch (SQLException e) {
            System.out.println("Erro ao fechar a conexao: " 
                + e.getMessage());
        }        
    }

    public boolean inserir(ClientePJ cli){
        boolean resultado = false;
        try {
            this.conectar();
			PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO tb_clientes_pj (cod_cli_pj, nome, cnpj)"
                + "VALUES (NULL, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            );
            pst.setString(1, cli.getNome());
            pst.setString(2, cli.getCnpj());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            int idCliente = 0;
            if(rs.next()){
                idCliente = rs.getInt(1);
                PreparedStatement pstEnd = conn.prepareStatement(
                "INSERT INTO tb_enderecos_pj (cod_end, cod_cli_pj, "
                + "rua, numero, bairro, cep)"
                + "VALUES (NULL, ?, ?, ?, ?, ?)");
                
                pstEnd.setInt(1, idCliente);
                pstEnd.setString(2, cli.getEnderecoPJ().getRua());
                pstEnd.setInt(3, cli.getEnderecoPJ().getNumero());
                pstEnd.setString(4, cli.getEnderecoPJ().getBairro());
                pstEnd.setString(5, cli.getEnderecoPJ().getCep());
                pstEnd.executeUpdate();
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao inserir o registro: " 
                + e.getMessage());
        }finally{
            this.desconectar();
        }
        return resultado;
    }

    public ArrayList<ClientePJ> buscarTodos(){
        ArrayList<ClientePJ> resultados = new ArrayList<ClientePJ>();
        try {
            this.conectar();
            ResultSet rs = st.executeQuery(
                "select * from tb_clientes_pj as c, tb_enderecos_pj as e" +
                " where c.cod_cli_pj = e.cod_cli_pj" +
                " order by c.nome;"
                );
            while(rs.next()){
                ClientePJ cli = new ClientePJ();
                cli.setCodigoClientePJ(rs.getInt("cod_cli_pj"));
                cli.setNome(rs.getString("nome"));
                cli.setCnpj(rs.getString("cnpj"));

                EnderecoPJ end = new EnderecoPJ();
                end.setCodigo(rs.getInt("cod_end"));
                end.setRua(rs.getString("rua"));
                end.setNumero(rs.getInt("numero"));
                end.setBairro(rs.getString("bairro"));
                end.setCep(rs.getString("cep"));

                cli.setEnderecoPJ(end);
                resultados.add(cli);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally{
            this.desconectar();
        }
        return resultados;
    }

    public int excluir(int cod){
        int qtde = 0;
        try {
            this.conectar();

            PreparedStatement pstEnd = conn.prepareStatement(
                "DELETE FROM tb_enderecos_pj WHERE cod_cli_pj = ?"
            );
            pstEnd.setInt(1, cod);
            pstEnd.executeUpdate();

            PreparedStatement pstCli = conn.prepareStatement(
                "DELETE FROM tb_clientes_pj WHERE cod_cli_pj = ?"
            );
            pstCli.setInt(1, cod);
            pstCli.executeUpdate();
            qtde = pstCli.getUpdateCount();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }finally{
            this.desconectar();
        }
        return qtde;
    } 

    public ClientePJ consultar(int cod){
        ClientePJ cli = null;
        try {
            this.conectar();
            ResultSet rs = st.executeQuery(
            "SELECT * from tb_clientes_pj AS c, tb_enderecos_pj AS e" +
            " WHERE c.cod_cli_pj = e.cod_cli_pj" +
            " AND c.cod_cli_pj = " + cod + ";"
            );
            if(rs.next()){
                cli = new ClientePJ();
                cli.setCodigoClientePJ(rs.getInt("cod_cli_pj"));
                cli.setNome(rs.getString("nome"));
                cli.setCnpj(rs.getString("cnpj"));

                EnderecoPJ end = new EnderecoPJ();
                end.setCodigo(rs.getInt("cod_end"));
                end.setRua(rs.getString("rua"));
                end.setNumero(rs.getInt("numero"));
                end.setBairro(rs.getString("bairro"));
                end.setCep(rs.getString("cep"));

                cli.setEnderecoPJ(end);
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally{
            this.desconectar();
        }
        return cli;
    }  
    
    public int alterar(ClientePJ cli){
        int qtde = 0;
        try {
            this.conectar();
            PreparedStatement pst = conn.prepareStatement(
                "UPDATE tb_clientes_pj SET nome = ?, cnpj = ?"
                + " WHERE cod_cli_pj = ?;"
            );
            pst.setString(1, cli.getNome());
            pst.setString(2, cli.getCnpj());
            pst.setInt(3, cli.getCodigoClientePJ());
            pst.executeUpdate();

            qtde = pst.getUpdateCount();
            if(qtde > 0){
                PreparedStatement pstEnd = conn.prepareStatement(
                "UPDATE tb_enderecos_pj SET rua = ?, numero = ?,"
                + " bairro = ?, cep = ?"
                + " WHERE cod_cli_pj = ?;");
                
                pstEnd.setString(1, cli.getEnderecoPJ().getRua());
                pstEnd.setInt(2, cli.getEnderecoPJ().getNumero());
                pstEnd.setString(3, cli.getEnderecoPJ().getBairro());
                pstEnd.setString(4, cli.getEnderecoPJ().getCep());
                pstEnd.setInt(5, cli.getCodigoClientePJ());
                pstEnd.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        } finally{
            this.desconectar();
        }
        return qtde;  
    }
}
