using Newtonsoft.Json;
using System;
using System.Diagnostics;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;

namespace socket_client1
{
    public partial class MainWindow : Window
    {
        private Socket client;
        private bool isConnect = false;

        public MainWindow()
        {
            InitializeComponent();
        }

        private void btnconnect_Click(object sender, RoutedEventArgs e)
        {
            if (!isConnect)
            {
                try
                {
                    //change the text on button
                    btnconnect.Content = "disconnect";
                    isConnect = true;


                    //connect to server
                    client = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
                    client.Connect(IPAddress.Parse(Ip.Text), int.Parse(Port.Text));

                    //update chat and send the name to server 
                    string clientName = Name.Text;
                    byte[] messageBytes = Encoding.ASCII.GetBytes(clientName);
                    client.Send(messageBytes);
                    AppendToChat("Welcome " + Name.Text);

                    //build the thread to get the message from server
                    Thread receiveThread = new Thread(ReceiveMessages);
                    receiveThread.Start();
                }
                catch (SocketException)//if there are no server
                {
                    AppendToChat("Server  hasn't built");
                    isConnect = false;
                    btnconnect.Content = "connect";

                }

            }
            else//if client had connect and press disconnect
            {
                isConnect = false;
                btnconnect.Content = "connect";

                //send the disconnect message to server
                Message jsonMessage = new Message { content = "client close" };
                string jsonString = JsonConvert.SerializeObject(jsonMessage);
                byte[] messageBytes = Encoding.ASCII.GetBytes(jsonString);
                client.Send(messageBytes);

                //close the server
                client.Close();

                //update chat
                AppendToChat("Disconnect success");
            }
        }
        private void btnsend_Click(object sender, RoutedEventArgs e)
        {
            if (isConnect)
            {
                //get the sned text and send it to server
                String message = send.Text;
                Message jsonMessage = new Message { content = message };
                string jsonString = JsonConvert.SerializeObject(jsonMessage);
                byte[] messageBytes = Encoding.ASCII.GetBytes(jsonString);
                client.Send(messageBytes);
                send.Text = "";

                //update chat
                Dispatcher.Invoke(() => {
                    AppendToChat("You say:" + message);
                });
            }
            else
            {
                send.Text = "";
                //if client doesn't connect or server stop 
                Dispatcher.Invoke(() => {
                    AppendToChat("Send fail");
                });
            }

        }

        private void ReceiveMessages()
        {
            byte[] buffer = new byte[1024];

            while (true)
            {
                try
                {
                    //get the message from server
                    int bytesRead = client.Receive(buffer);
                    string message = Encoding.ASCII.GetString(buffer, 0, bytesRead);
                    Message jsonMessage = JsonConvert.DeserializeObject<Message>(message);

                    //if server had stop
                    if (jsonMessage.content.ToLower() == "shutdown")
                    {
                        //update chat and close client
                        client.Close();
                        isConnect = false;
                        Dispatcher.Invoke(() => {
                            btnconnect.Content = "connect";
                            AppendToChat("Server disconnected");
                        });
                        break;
                    }

                    //update chat
                    Dispatcher.Invoke(() => {
                        AppendToChat(jsonMessage.content);
                    });

                }
                catch (Exception ex)
                {
                    Console.WriteLine(ex.Message);
                    break;
                }
            }
        }

        private void AppendToChat(string message)
        {
            Dispatcher.Invoke(() =>
            {
                chat.Text += message + "\n";
            });
        }

        public class Message
        {
            public string content { get; set; }
        }
    }
}
