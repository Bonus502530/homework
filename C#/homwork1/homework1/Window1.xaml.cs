using MySql.Data.MySqlClient;
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
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace WpfApp1
{
    /// <summary>
    /// Window1.xaml 的互動邏輯
    /// </summary>
    public partial class Window1 : Window{
        public static string connectionString = "server=localhost;user=root;password=;database=calculator;";
        public MySqlConnection connection = new MySqlConnection(connectionString);
        public Window1(){
            InitializeComponent();
            LoadData();
        }

        private void Button_Click(object sender, RoutedEventArgs e){
            result.Text = "";
            try
            {
                connection.Open();
                string sqlSelectQuery = "SELECT * FROM calculator";
                MySqlCommand command = new MySqlCommand(sqlSelectQuery, connection);

                // 使用 MySqlDataAdapter 执行查询并获取结果
                MySqlDataAdapter dataAdapter = new MySqlDataAdapter(command);
                DataTable dataTable = new DataTable();
                dataAdapter.Fill(dataTable);

                DataColumn combinedColumn = new DataColumn("CombinedData", typeof(string), "'ID : ' + id + ', prefix : ' + prefix + ', infix : ' + infix + ', postfix : ' + postfix + ', decimal : ' + decimalans + ', binary : ' + binaryans");
                dataTable.Columns.Add(combinedColumn);

                //foreach (DataRow row in dataTable.Rows)
                //{
                //    Console.WriteLine($"ID: {row["id"]}, prefix: {row["prefix"]}, infix: {row["infix"]}, postfix: {row["postfix"]}, decimalans: {row["decimalans"]}, binaryans: {row["binaryans"]}");
                //}

                showout.ItemsSource = dataTable.DefaultView;
                showout.DisplayMemberPath = "CombinedData";
                showout.Items.Add(dataTable.DefaultView);

            }
            catch (Exception ex) { 
                Console.WriteLine("發生異常：" + ex.Message);
            }
            finally {
                connection.Close();
            }

        }

        private void LoadData()
        {
                try
                {
                    using (MySqlConnection connection = new MySqlConnection(connectionString))
                    {
                        connection.Open();
                        string sqlSelectQuery = "SELECT id FROM calculator";

                        MySqlCommand command = new MySqlCommand(sqlSelectQuery, connection);
                        MySqlDataAdapter dataAdapter = new MySqlDataAdapter(command);
                        DataTable dataTable = new DataTable();
                        dataAdapter.Fill(dataTable);

                        showout.ItemsSource = dataTable.DefaultView;
                        showout.DisplayMemberPath = "CombinedData";
                }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("发生异常：" + ex.Message);
                }
            }

        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            if (showout.SelectedItem is DataRowView selectedRow)
            // 获取选定项
            {
                int idToDelete = Convert.ToInt32(selectedRow["id"]);

                try
                {
                    using (MySqlConnection connection = new MySqlConnection(connectionString))
                    {
                        connection.Open();
                        string sqlDeleteQuery = "DELETE FROM calculator WHERE id = @idToDelete";

                        MySqlCommand command = new MySqlCommand(sqlDeleteQuery, connection);
                        command.Parameters.AddWithValue("@idToDelete", idToDelete);

                        int rowsAffected = command.ExecuteNonQuery();
                        if (rowsAffected > 0)
                        {
                            Console.WriteLine("删除成功！");
                            result.Text = "success";
                            LoadData(); // 刷新数据
                        }
                        else
                        {
                            Console.WriteLine("删除失败！");
                        }
                    }
                }
                catch (Exception ex)
                {
                    Console.WriteLine("发生异常：" + ex.Message);
                }
            }
            else
            {
                Console.WriteLine("请选择要删除的项！");
            }
        }
    }
}
