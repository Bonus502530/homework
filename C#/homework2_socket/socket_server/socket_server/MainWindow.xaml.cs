using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Windows;
using System.Windows.Controls;
using System.Xml.Linq;

namespace socket_server
{
    public partial class MainWindow : Window
    {
        public Socket server;
        public List<Socket> clients = new List<Socket>();
        public bool isServerRunning = false;


        public MainWindow()
        {
            InitializeComponent();
        }

        private void btnstart_Click(object sender, RoutedEventArgs e)
        {
            //server has not built
            if (!isServerRunning)
            {
                //update inital detail on Chat
                AppendToChat("Server Start");
                isServerRunning = true;

                //build the server socket 
                server = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                server.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.ReuseAddress, true);
                server.Bind(new IPEndPoint(IPAddress.Parse(Ip.Text), int.Parse(Port.Text)));
                server.Listen(10);

                //listen the clients
                Thread serverThread = new Thread(HandleClients);
                serverThread.Start();
            }
            //if press the start twice
            else
            {
                AppendToChat("Server is already started");
            }
        }

        private void btnstop_Click(object sender, RoutedEventArgs e)
        {
            //server had built
            if (isServerRunning)
            {
                isServerRunning = false;

                //send the message to the client (doesn't disconnect) that server had stop
                foreach (Socket client in clients)
                {
                    Message jsonMessage = new Message { content = "shutdown" };
                    string jsonString = JsonConvert.SerializeObject(jsonMessage);
                    byte[] messageBytes = Encoding.ASCII.GetBytes(jsonString);
                    client.Send(messageBytes);
                    client.Close();
                }

                //clear the list and close the server and update Chat
                clients.Clear();
                server.Close();
                Dispatcher.Invoke(() => {
                    AppendToChat("Server stop");
                });
            }
            //if press the stop twice
            else
            {
                Dispatcher.Invoke(() => {
                    AppendToChat("Server is already stoped");
                });
            }
        }


        private void HandleClients()
        {
            while (isServerRunning)
            {
                try
                {
                    //listen the client
                    Socket client = server.Accept();

                    //put the new client to list
                    clients.Add(client);

                    //get the client name
                    byte[] buffer = new byte[1024];
                    int bytesRead = client.Receive(buffer);
                    string clientname = Encoding.ASCII.GetString(buffer, 0, bytesRead);
                    Dispatcher.Invoke(() => {
                        AppendToChat(clientname + $"({client.RemoteEndPoint}) connect");
                    });

                    //new thread to get the message from client
                    Thread clientThread = new Thread(() => HandleClient(client, clientname));
                    clientThread.Start();
                }
                catch (SocketException)
                {
                    break;
                }
            }
        }

        private void HandleClient(Socket client, string name)
        {
            byte[] buffer = new byte[1024];

            while (true)
            {
                try
                {
                    int bytesRead = client.Receive(buffer);

                    //Client send the message to server
                    string message = Encoding.ASCII.GetString(buffer, 0, bytesRead);
                    Message jsonMessage = JsonConvert.DeserializeObject<Message>(message);

                    //if clientsend that client has disconnected remove it from list and close the thread
                    if (jsonMessage.content == "client close")
                    {
                        clients.Remove(client);
                        client.Close();

                        break;
                    }

                    //update Chat
                    Dispatcher.Invoke(() => {
                        AppendToChat(name + $"({client.RemoteEndPoint}) : {jsonMessage.content}");
                    });

                    //tell all the clients
                    Message jsonMessage2 = new Message { content = name + $" : {jsonMessage.content}" };
                    string jsonString = JsonConvert.SerializeObject(jsonMessage2);
                    byte[] messageBytes = Encoding.ASCII.GetBytes(jsonString);

                    foreach (Socket Client in clients)
                    {
                        if (Client != client)
                        {
                            Client.Send(messageBytes);
                        }
                    }
                }

                catch (SocketException ex)
                {
                    Console.WriteLine(ex);
                    break;
                }
            }

            if (isServerRunning)
            {
                Dispatcher.Invoke(() => {
                    AppendToChat(name + " disconnect");
                });
            }
        }

        private void AppendToChat(string message)
        {
            Dispatcher.Invoke(() =>
            {
                Chat.Text += message + "\n";
            });
        }

        public class Message
        {
            public string content { get; set; }
        }
    }
}

