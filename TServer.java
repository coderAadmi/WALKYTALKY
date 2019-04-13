import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;


class ClientManager implements Runnable{
    private String name;
    private DataOutputStream dout;
    private DataInputStream din;
    private Socket client;
    private boolean isRunning;


    @Override
    public void run() {
        System.out.println("Listeneing for "+client);
        loop: while (client.isConnected())
        {
            try {
                String msg = din.readUTF();
                if(msg.length()>0)
                {
                    System.out.println(name +": "+ msg);
                    if(msg.charAt(0) == '$')
                    {
                        if(msg.charAt(1) =='b')
                        {
                            TServer.broadcast(msg);
                        }
                        else if(msg.charAt(1) == '$' && msg.charAt(2) =='Q' && msg.charAt(3) == 'U' && msg.charAt(4) == 'I' && msg.charAt(5) == 'T')
                        {
                            throw new Exception("Quit is there...");
                        }
                        else
                        {
                            String reciever[] = msg.split(" ",2);
                            TServer.sendMsgToPeer(this.name, reciever[0], reciever[1]);
                        }
                    }

                }

            } catch (Exception e) {
                try {
                    client.close();
                } catch (IOException e1) {
                }
                System.out.println(this.name +" Disconnected..."+ e);
                break loop;
            }
        }
        System.out.println("Closed .. "+ name);
        TServer.removeClient(this);
    }

    ClientManager(Socket client, String name)
    {
        this.client = client;
        this.name = name;
        isRunning = true;
        try {
            dout = new DataOutputStream(client.getOutputStream());
            din = new DataInputStream(client.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg)
    {
        try {
            dout.writeUTF(msg);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
    public String getName(){return name;}

    public boolean isConnected()
    {
        return client.isConnected();
    }
}

public class TServer {
    private ServerSocket serverSocket;
    private String IP;
    private int PORT;
    private boolean isRunning;
    private static  ArrayList<ClientManager> clientList;

    private static Dictionary client_SocketsToName;
    private static Dictionary client_NameToSocket;

    TServer(String IP, int PORT)
    {
        client_SocketsToName = new Hashtable();
        client_NameToSocket = new Hashtable();
        isRunning = true;
        this.IP = IP;
        this.PORT = PORT;

        String CLIENTS ="Clients:";

        clientList = new ArrayList<ClientManager>();

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server Started......");

            while(isRunning)
            {
                Socket client = serverSocket.accept();


                String name = (new DataInputStream(client.getInputStream())).readUTF();
                System.out.println("Connected to "+" : "+client+ " name : "+ name);
                ClientManager client1 = new ClientManager(client , name);
                clientList.add(client1);
                CLIENTS += " "+ name;
                client_SocketsToName.put(client1,name);
                client_NameToSocket.put("$"+name, client1);
                broadcast(CLIENTS);
                Thread t = new Thread( client1);
                t.start();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void sendMsgToPeer(String sender, String reciever, String message)
    {
        ClientManager c = (ClientManager) client_NameToSocket.get(reciever);
        if(c != null && c.isConnected()) {
            c.sendMessage("Sender: " + sender + "Msg: " + message);
            System.out.println("Sent message from  :" +sender + "to : "+reciever+" msg: "+message);
        }
        else{
            System.out.println("Fail to Send message from  :" +sender + "to : "+reciever+" msg: "+message);
        }
    }

    public static void broadcast(String msg)
    {
        for(ClientManager s:clientList)
        {
            if(s!=null && s.isConnected())
            {
                System.out.println("Sent to: "+s.getName());
                s.sendMessage(msg);
            }
        }
    }

    public static void removeClient(ClientManager client)
    {
        System.out.println("Removing "+client.getName());
        client_SocketsToName.remove(client);
        client_NameToSocket.remove(client.getName());
    }

    public static void main(String ar[])
    {
        new TServer("127.0.0.1",12345);
    }

}
