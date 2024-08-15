import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Client vai ter de ter ligacoes com o userInput
//Client vai ter de ter ligaçoes com o server
//vou ter de ter um metodo para ir criando sockets
//nao pode haver 2 metodos readLine na mesma thread (pq vao ficar os dois à escuta)


public class Client1 {

    private static Socket serverSocket;
    private String textFromClient;
    private static BufferedReader bufferedReader;
    private String name;


    public Client1(Socket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.bufferedReader= new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
    }

    public void readInput() throws IOException {//recebe input do user

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));//now it can get the message from the user
        textFromClient = bufferedReader.readLine();//fica à escuta do terminal



    }

    public void writeOutput() throws IOException {//write the input from the user
        PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);//enviar mensagens
        while(textFromClient!=null) {

            out.println(textFromClient);//it will send the message from the user to the server
            //System.out.println("Vou enviar esta mensagem: " + textFromClient);
        }


    }


    public static void main(String[] args) throws IOException {//ponto de entrada do programa


            InetAddress adress = InetAddress.getLocalHost();
            serverSocket = new Socket(adress, 9999);
            Client1 client1 = new Client1(serverSocket);
            ExecutorService cachedPool = Executors.newCachedThreadPool();


            cachedPool.submit(client1.new ClientWorker(client1)); //clientWorker threads created
            //System.out.println("estou aqui");

        String mensagemDoServer=  bufferedReader.readLine(); //Estou à escuta do servidor
        System.out.println("mensagem vinda do server: " + mensagemDoServer);


    }

        class ClientWorker implements Runnable {
            private Client1 client1;

            public ClientWorker(Client1 client1) throws IOException {
                this.client1 = client1;
            }

            @Override
            public void run() {

                try {
                    client1.readInput();
                    client1.writeOutput();

                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }


        //InerClass vai estar so à escuta do server. ClientWorker que vai ser runnable
    }


