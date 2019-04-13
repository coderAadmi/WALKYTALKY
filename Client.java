import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket client;
    private DataOutputStream dout;
    private DataInputStream din;
    private String name;
    private String IP;
    private int PORT;

    private boolean isClosed;

    Thread recvMsg, sendMsg;

    Client(String IP, int PORT)
    {
        isClosed = false;
        Scanner scanner = new Scanner(System.in);
        this.IP = IP;
        this.PORT = PORT;

        System.out.println("Enter your name:");
        name = scanner.nextLine();

        try {
            client = new Socket(IP, PORT);
            dout = new DataOutputStream(client.getOutputStream());
            din = new DataInputStream(client.getInputStream());

            System.out.println("Start........");

            dout.writeUTF(name);

            showMenu();

             sendMsg = new Thread(new Runnable() {
                @Override
                public void run() {

                    while (client.isConnected())
                    {
                        String msg = scanner.nextLine();
                        if(msg.charAt(0)=='$'&& msg.charAt(1) == '$' && msg.charAt(2) =='Q' && msg.charAt(3) == 'U' && msg.charAt(4) == 'I' && msg.charAt(5) == 'T')
                        {
                            isClosed = true;
                            try {
                                client.close();
                            } catch (IOException e) {
                            }
                        }
                        else {
                            try {
                                dout.writeUTF(msg);
                            } catch (IOException e) {
                                System.out.println("Shutting down...plz restart an eror occured");
                                System.exit(1);
                            }

                        }
                    }
                }
            });

             recvMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                   while (client.isConnected())
                   {
                       try {
                           String msg = din.readUTF();
                           System.out.println(msg);
                       } catch (IOException e) {
                           if(isClosed == false)
                           System.out.println("QQhutting down...plz restart an eror occured");
                           System.exit(1);
                           //e.printStackTrace();
                       }
                   }
                }
            });

            sendMsg.start();
            recvMsg.start();


        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    void showMenu()
    {
        System.out.println("Hey client!!!");
        System.out.println("To broadcast message : Type $b in the start of the message and then send ");
        System.out.println("To send to a peer : Type $ followed by the name of peer and then space and then message and send");
    }

    public static void main(String a[])
    {
        new Client("0.0.0.0",12345);
    }
}
