import java.util.ArrayList;
import java.util.Scanner;

public class GerenciadorClientePJ{
    Scanner scanner;
    DaoClientePJ daoClientePJ;

    public GerenciadorClientePJ(){
        scanner = new Scanner(System.in);
        daoClientePJ = new DaoClientePJ();
    }

    public void menu(){
        int opcao = -1;
        while(opcao != 0){
			System.out.println("\n------------------------------------------");
			System.out.println("GERENCIAMENTO DE CLIENTES PJ");
			System.out.println("[1] Cadastrar");
			System.out.println("[2] Consultar");
			System.out.println("[3] Alterar");
			System.out.println("[4] Excluir");
			System.out.println("[5] Listar todos");
			System.out.println("[0] Voltar ao menu anterior");
			System.out.println("\n------------------------------------------");

            try{
                opcao = Integer.parseInt(scanner.nextLine());
            }catch(Exception e){
                System.out.println("Erro... informe um numero inteiro. " 
                    + e.getMessage());
            }

            if(opcao == 1){
                this.cadastrar();
            }else if(opcao == 2){
                this.consultar();
            }else if(opcao == 3){
                this.alterar();
            }else if(opcao == 4){
                this.excluir();
            }else if(opcao == 5){
                this.listarTodos();
            }
        }
    }

    public void cadastrar(){
        ClientePJ cliPJ = new ClientePJ();
		
		System.out.println("\n--------------------------------------------------------------");
		System.out.println("[Cadastro de Clientes PJ]");
		System.out.println("Nome: ");
		cliPJ.setNome(scanner.nextLine());
		System.out.println("CNPJ: ");
		cliPJ.setCnpj(scanner.nextLine());

        EnderecoPJ endPJ = new EnderecoPJ();
		System.out.println("[Endereço]");
		System.out.println("Rua: ");
		endPJ.setRua(scanner.nextLine());
		System.out.println("Número: ");
		endPJ.setNumero(Integer.parseInt(scanner.nextLine()));       
		System.out.println("Bairro: ");
		endPJ.setBairro(scanner.nextLine());
		System.out.println("CEP: ");
		endPJ.setCep(scanner.nextLine());
        
        cliPJ.setEnderecoPJ(endPJ);
        boolean inserido = daoClientePJ.inserir(cliPJ);//passa o objeto para a camada model
        if(inserido){
            System.out.println("Inserido com sucesso.");
        }
    }

    private void listarTodos(){
        ArrayList<ClientePJ> resultado = daoClientePJ.buscarTodos();
        System.out.println("---------------------------");
        System.out.println("Clientes PJ cadastrados:");
        for(ClientePJ cli : resultado){
            System.out.println("Nome: " + cli.getNome()
                + ", CNPJ: " + cli.getCnpj()
                + ", Codigo: " + cli.getCodigoClientePJ());
            
            System.out.println("    [Endereço]: "
            + ", Rua: " + cli.getEnderecoPJ().getRua()
            + ", Número: " + cli.getEnderecoPJ().getNumero()
            + ", Bairro: " + cli.getEnderecoPJ().getBairro()
            + ", CEP: " + cli.getEnderecoPJ().getCep());
        }
    }

    public void excluir(){
        System.out.println("---------------------------");
        System.out.println("[Exclusão de Clientes PJ]");
        System.out.println("Codigo: ");
        int codigo = Integer.parseInt(scanner.nextLine()); 
        int qtde = daoClientePJ.excluir(codigo);
        if(qtde > 0){
            System.out.println("Excluído com sucesso");
        }else{
            System.out.println("Não encontrado...");
        }
    }

    private void consultar(){
        System.out.println("---------------------------");
        System.out.println("[Consulta de cLIENTES pj]");
        System.out.println("Codigo: ");
        int codigo = Integer.parseInt(scanner.nextLine());

        ClientePJ cli = daoClientePJ.consultar(codigo);
        if(cli != null){
            System.out.println("Nome: " + cli.getNome()
                + ", CNPJ: " + cli.getCnpj()
                + ", Codigo: " + cli.getCodigoClientePJ());
            System.out.println("     [Endereço]: "
                + ", Rua: " + cli.getEnderecoPJ().getRua()
                + ", Num: " + cli.getEnderecoPJ().getNumero()
                + ", Bairro: " + cli.getEnderecoPJ().getBairro()
                + ", CEP: " + cli.getEnderecoPJ().getCep());
        }else{
            System.out.println("Não encontrado...");
        }
    } 

    private void alterar(){
        System.out.println("---------------------------");
        System.out.println("[Alteração de Clientes PJ]");
        System.out.println("Codigo: ");
        int codigo = Integer.parseInt(scanner.nextLine());

        ClientePJ cli = daoClientePJ.consultar(codigo);
        if(cli != null){
			System.out.println("Dados do Cliente PJ");
			System.out.println("Codigo: [" 
                + cli.getCodigoClientePJ() + "]");

			System.out.println("Nome: [" + cli.getNome() + "]");
            String nome = scanner.nextLine().trim();
            if(!nome.isEmpty()){
                cli.setNome(nome);
            }

			System.out.println("CNPJ: [" + cli.getCnpj() + "]");
            String cnpj = scanner.nextLine().trim();
            if(!cnpj.isEmpty()){
               cli.setCnpj(cnpj);
            }

            System.out.println("[ENDEREÇO]");
            System.out.println("Rua: [" 
                + cli.getEnderecoPJ().getRua() + "]");
            String rua = scanner.nextLine().trim();
            if(!rua.isEmpty()){
               cli.getEnderecoPJ().setRua(rua);
            }

            System.out.println("Numero: [" 
                + cli.getEnderecoPJ().getNumero() + "]");
            String numero = scanner.nextLine().trim();
            if(!numero.isEmpty()){
               cli.getEnderecoPJ().setNumero(
                    Integer.parseInt(numero));
            }

			System.out.println("Bairro: [" 
                + cli.getEnderecoPJ().getBairro() + "]");
            String bairro = scanner.nextLine().trim();
            if(!bairro.isEmpty()){
               cli.getEnderecoPJ().setBairro(bairro);
            }

            System.out.println("CEP: [" 
                + cli.getEnderecoPJ().getCep() + "]");
            String cep = scanner.nextLine().trim();
            if(!cep.isEmpty()){
                cli.getEnderecoPJ().setCep(cep);
            }
		
            int qtdeAlterado = daoClientePJ.alterar(cli);
            if(qtdeAlterado > 0){
                System.out.println("Atualizado!");
            }
        }else{
            System.out.println("Não encontrado...");
        }
    }     
}
