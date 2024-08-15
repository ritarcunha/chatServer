import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {//receber e enviar mensagens para o client
                        //aceitar as conexões dos clients

    private ServerSocket serverSocket;
    private String message;
    private LinkedList<Socket> conections;
    private Socket clientSocket;
    private BufferedReader bufferedReader;
    private InputStream inputStream;

    public ChatServer() throws IOException {
        this.serverSocket= new ServerSocket(9999);
        this.bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
    }

    public void readInput() throws IOException {
        //System.out.println("estou no metodo read");
        InputStream inpustream = this.clientSocket.getInputStream(); //client.getInoutstream é para ir buscar o que o client escreveu
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inpustream));//aqui ja consegue ir buscar o input do client e transforma-lo em bytes.
        message = bufferedReader.readLine();//coloco numa string, é isto que me faz ir buscar a mensagem do client
        System.out.println("vou mudar a message" + message);



    }

    public void writeInput() throws IOException {

        OutputStream outputStream= clientSocket.getOutputStream();
        PrintWriter out= new PrintWriter(outputStream,true);
        System.out.println("Message from a boring client: " + message);
        while(message!=null) {
            if (message.equals("quit")) {
                System.out.println("I'm out!");
                break;
            }
            System.out.println("vou mudar a message2" + message);
            message = bufferedReader.readLine();
            out.println(message);//esta mensagem agora tem de ser enviada para o client
        }

    }

    public void init() throws IOException {
       // while (true) {//vai ter de guardar as conexoes na lista
        //System.out.println("estou aqui 2");
            clientSocket = serverSocket.accept();//sempre que há um novo cliente ele cria uma ligaçao com o servidor
            System.out.println("conection done");
            //conections.add(clientSocket);
            //readInput();
            //writeInput();

       // }
}

    //metodo para enivar e receber mensagens
    public static void main(String[] args) throws IOException {

        ChatServer chatServer = new ChatServer();
        ExecutorService cachedPool = Executors.newCachedThreadPool();// aqui ja criei uma coisa para ir criando threads
        cachedPool.submit(chatServer.new ServerWorker(chatServer));//o start e o run ficam ativos a partir do momento que faço submit

    }

        class ServerWorker implements Runnable{

            private ChatServer chatServer;

            public ServerWorker (ChatServer chatServer) throws IOException {
                this.chatServer=chatServer;
            }

            @Override
            public void run() {
                try {
                    //System.out.println("estou aqui");
                    while(true) {
                        chatServer.init();
                        chatServer.readInput();
                        chatServer.writeInput();
                   }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }


    }






    /*

    EXERCISE
Chat Server concorrencial- múltiplos clientes
Conectados com o server
Uma mensagem e enviadas pelo cliente e todos recebem


CLIENT:
-escrever para o servidor-precisamos de streams
-ouvir o servidor (ficar à escuta)
-listen to user input (system in) do teclado
-

Precisamos de streams (input e outoutstreams), socket (Ip do servidor e porta),
Quando um metido esta a ler (read-bloqeante). Se esta a ouvir do servidor nao consegue ouvir do terminal, so se tiver uma thread. O chatClient vai ter 2 threads (main que esta a executar uma tarefa e vai precisar de outras threads para passar outras tarefas, passamos o runnable para tal). Uma thread esta a escuta do servidor e outra esta a escuta
Inputstream e outputsream para o server e para o userinput. Streams deiferentes para a consola e para o servidor. Porque sao streams diferentes?Precisamos de escrever para a consola e receber da consola (cliente-terminal, cliente-servidor).

  //o server vai ter um responsavel por so estar à escuta e enviar mensagens
        //outro para mandar mensagens ao cliente

        //O server vai ter ligações com as diferentes threads de client

        //InputStreamReader is a bridge from byte streams to character streams: It reads bytes and decodes them into characters using a specified charset.
        //o bufferedReader recebe um InputStreamReader.Reads text from a character-input stream, buffering characters so as to provide for the efficient reading of characters, arrays, and lines.
        //O InputStreamReader recebe inpustream é uma ponte que transforma streams de bytes em streams de characters
        //InputStream input stream of bytes.

        //ServerSocket serverSocket = new ServerSocket(9999);

 //o chatServer é que conhece todos os clientes (conexoes)

        //guardar numa estrutura de dados (List) as conexoes
        //ciclo while que vai recebendo e guardando as conexoes numa lista e acho que pode falar com eles
        //thread de server worker (tem as ligaçoes e fala com os clients)
    */

