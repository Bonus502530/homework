using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using MySql.Data.MySqlClient;
using WpfApp1;

namespace test {
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window { 

        public static string connectionString = "server=localhost;user=root;password=;database=calculator;";

        public MainWindow() {
            InitializeComponent();
        }

        public Dictionary<char, int> prio = new Dictionary<char, int>() { { '+', 12 }, { '-', 12 }, { '*', 13 }, { '/', 13 } };

        public string inorder;

        Stack<char> symbol = new Stack<char>();
        Stack<int> ans = new Stack<int>();

        private void Button_Click_1(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "1";
        }

        private void Button_Click_2(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "2";
        }

        private void Button_Click_3(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "3";
        }

        private void Button_Click_4(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "4";
        }

        private void Button_Click_5(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "5";
        }

        private void Button_Click_6(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "6";
        }

        private void Button_Click_7(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "7";
        }

        private void Button_Click_8(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "8";
        }

        private void Button_Click_9(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "9";
        }

        private void Button_Click_0(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "0";
        }

        private void Button_Click_10(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "+";
        }

        private void Button_Click_11(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "-";
        }

        private void Button_Click_12(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "*";
        }

        private void Button_Click_13(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text = "";
        }

        private void Button_Click_14(object sender, RoutedEventArgs e) { 
            string infix;
            string infix_re;
            string prefix = "";
            string prefix_re = "";
            string postfix = "";


            TextBlock tb = textblock1;
            infix = tb.Text;
            inorder = tb.Text;
            tb.Text = "";

            char[] chars = infix.ToCharArray();
            Array.Reverse(chars, 0, chars.Length);
            infix_re = new string(chars);

            int insize = infix.Length;

            int flag = 0;

            while (flag < insize) { 
                if (!prio.ContainsKey(infix_re[flag])) { 
                    prefix_re += infix_re[flag];
                }
                else { 
                    if (symbol.Count() == 0) { 
                        symbol.Push(infix_re[flag]);
                    }
                    else { 
                        char sym = symbol.Peek();
                        int pre_priority = prio[sym];
                        int cur_priority = prio[infix_re[flag]];
                        if (pre_priority > cur_priority) { 
                            prefix_re += sym;
                            symbol.Pop();
                            prefix_re += infix_re[flag];
                        }
                        else if (pre_priority == cur_priority) { 
                            prefix_re += sym;
                            symbol.Pop();
                            symbol.Push(infix_re[flag]);
                        }
                        else { 
                            symbol.Push(infix_re[flag]);
                        }

                    }
                }
                flag++;
            }
            while (symbol.Count() != 0) { 
                prefix_re += symbol.Peek();
                symbol.Pop();
            }

            char[] chars2 = prefix_re.ToCharArray();
            Array.Reverse(chars2, 0, chars2.Length);
            prefix = new string(chars2);

            flag = 0;

            while (flag < insize) { 
                if (!prio.ContainsKey(infix[flag])) { 
                   postfix += infix[flag];
                }
                else { 
                    if (symbol.Count() == 0) { 
                        symbol.Push(infix[flag]);
                    }
                    else { 
                        char sym = symbol.Peek();
                        int pre_priority = prio[sym];
                        int cur_priority = prio[infix[flag]];
                        if (pre_priority > cur_priority) { 
                            postfix += sym;
                            symbol.Pop();
                            postfix += infix[flag];
                        }
                        else if(pre_priority == cur_priority) { 
                            postfix += sym;
                            symbol.Pop();
                            symbol.Push(infix[flag]);
                        }
                        else { 
                            symbol.Push(infix[flag]);
                        }
                        
                    }
                }
                flag++;
            }
            while (symbol.Count() != 0) { 
                postfix += symbol.Peek();
                symbol.Pop();
            }

            TextBlock pre = preorder;
            pre.Text = prefix;

            TextBlock post = postorder;
            post.Text = postfix;

            ans.Clear();
            for (int i=0; i<postfix.Length; i++) { 
                int test = (int)postfix[i];
                if (test > '0') { 
                    ans.Push((int)postfix[i]-'0');
                }
                else { 
                    if(test == '+') { 
                        int second = ans.Peek();
                        ans.Pop();
                        int first = ans.Peek();
                        ans.Pop();
                        ans.Push(first + second);
                    }
                    if (test == '-') { 
                        int second = ans.Peek();
                        ans.Pop();
                        int first = ans.Peek();
                        ans.Pop();
                        ans.Push(first - second);
                    }
                    if (test == '*') { 
                        int second = ans.Peek();
                        ans.Pop();
                        int first = ans.Peek();
                        ans.Pop();
                        ans.Push(first * second);
                    }
                    if (test == '/') {
                        int second = ans.Peek();
                        ans.Pop();
                        int first = ans.Peek();
                        ans.Pop();
                        ans.Push(first / second);
                    }
                }
            }

            int ans_decimal = ans.Peek();
            int ans_bi = ans.Peek();
            string ans_binary = Convert.ToString(ans_decimal, 2);

            TextBlock de = decimalans;
            de.Text = Convert.ToString(ans_decimal);

            TextBlock bi = binaryans;
            bi.Text = ans_binary;
        }

        private void Button_Click_15(object sender, RoutedEventArgs e) { 
            TextBlock tb = textblock1;
            tb.Text += "/";
        }

        private void Button_Click_insert(object sender, RoutedEventArgs e) {
            try
            {
                using (MySqlConnection connection = new MySqlConnection(connectionString))
                {
                    bool exist;
                    connection.Open();
                    string sqlExistQuery = "SELECT prefix FROM calculator WHERE prefix = @preorder1";
                    MySqlCommand command = new MySqlCommand(sqlExistQuery, connection);
                    command.Parameters.AddWithValue("@preorder1", preorder.Text);

                    using (MySqlDataReader reader = command.ExecuteReader())
                    {
                        bool find = reader.HasRows;
                        Console.WriteLine(find);
                        exist = find;

                        if (find)
                        {
                            MessageBox.Show("算式已存在");
                        }
                    } // 关闭数据读取器

                    if (!exist){
                        string sqlInsertQuery = "INSERT INTO calculator (prefix, infix, postfix, decimalans, binaryans) VALUES (@preorder, @textblock1, @postorder, @decimalans, @binaryans)";
                        MySqlCommand commandi = new MySqlCommand(sqlInsertQuery, connection);

                        commandi.Parameters.AddWithValue("@preorder", preorder.Text);
                        commandi.Parameters.AddWithValue("@textblock1", inorder);
                        commandi.Parameters.AddWithValue("@postorder", postorder.Text);
                        commandi.Parameters.AddWithValue("@decimalans", decimalans.Text);
                        commandi.Parameters.AddWithValue("@binaryans", binaryans.Text);
                        int rowsAffected = commandi.ExecuteNonQuery();
                        if (rowsAffected > 0)
                        {
                            Console.WriteLine("插入成功！");
                        }
                        else
                        {
                            Console.WriteLine("插入失敗！");
                        }
                    }
                }
                //using (MySqlConnection connection = new MySqlConnection(connectionString))
                //{
                //    connection.Open();
                //    string sqlExistQuery = "SELECT prefix FROM calculator WHERE prefix = @preorder1";
                //    MySqlCommand command = new MySqlCommand(sqlExistQuery, connection);
                //    command.Parameters.AddWithValue("@preorder1", preorder.Text);
                //    bool find = command.ExecuteReader().HasRows;
                //    Console.WriteLine(find);

                //    if (find){
                //        MessageBox.Show("算式已存在");
                //    }

                //    string sqlInsertQuery = "INSERT INTO calculator (prefix, infix, postfix, decimalans, binaryans) VALUES (@preorder, @textblock1, @postorder, @decimalans, @binaryans)";
                //    MySqlCommand commandi = new MySqlCommand(sqlInsertQuery, connection);

                //    commandi.Parameters.AddWithValue("@preorder", preorder.Text);
                //    commandi.Parameters.AddWithValue("@textblock1", inorder);
                //    commandi.Parameters.AddWithValue("@postorder", postorder.Text);
                //    commandi.Parameters.AddWithValue("@decimalans", decimalans.Text);
                //    commandi.Parameters.AddWithValue("@binaryans", binaryans.Text);
                //    int rowsAffected = commandi.ExecuteNonQuery();
                //    if (rowsAffected > 0)
                //    {
                //        Console.WriteLine("插入成功！");
                //    }
                //    else
                //    {
                //        Console.WriteLine("插入失敗！");
                //    }
                //}
            }
            catch (Exception ex)
            {
                Console.WriteLine("發生異常：" + ex.Message);
            }

        }



        private void Button_Click_query(object sender, RoutedEventArgs e) {
            Window1 f = new Window1();
            f.Show();
        }

    }
}
