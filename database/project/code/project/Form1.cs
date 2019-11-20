using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Data.SqlClient;

namespace project
{
    public partial class Form1 : Form
    {
        SqlConnection cnn;
        Form temp;
        // ADD book
        TextBox idBox, yearBox, titleBox, priceBox, authorBox, publishBox;
        // ADD shop 
        TextBox nameBox, emailBox;
        // Delete shop
        TextBox shopidBox, shopBox;
        public Form1()
        {
            InitializeComponent();
            load();
        }

        private void load()
        {
            cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127;");
            SqlCommand cmd = new SqlCommand("author_names", cnn);
            cmd.CommandType = CommandType.StoredProcedure;
            try
            {
                cnn.Open();
                SqlDataAdapter dr = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                dr.Fill(dt);
                
                for(int i = 0; i < dt.Rows.Count; i++)
                {
                    authorList.Items.Add(dt.Rows[i]["author_name"]);
                }
                authorList.SelectedIndex = -1;

                //warehouse names
                cmd = new SqlCommand("warehouse_names", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                dr = new SqlDataAdapter(cmd);
                dt = new DataTable();
                dr.Fill(dt);

                
                for (int i = 0; i < dt.Rows.Count; i++)
                    warehouseList.Items.Add(dt.Rows[i]["warehouse_address"].ToString());
                warehouseList.SelectedIndex = -1;

                //warehouseList.DataSource = dt;
                //warehouseList.DisplayMember = "warehouse_address";

                //publisher names
                cmd = new SqlCommand("publisher_names", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                dr = new SqlDataAdapter(cmd);
                dt = new DataTable();
                dr.Fill(dt);

                for (int i = 0; i < dt.Rows.Count; i++)
                    publisherList.Items.Add(dt.Rows[i]["publisher_name"].ToString());
                publisherList.SelectedIndex = -1;

                //publisherList.DataSource = dt;
                //publisherList.DisplayMember = "publisher_name";

                //publish years
                cmd = new SqlCommand("publish_years", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                dr = new SqlDataAdapter(cmd);
                dt = new DataTable();
                dr.Fill(dt);

                for (int i = 0; i < dt.Rows.Count; i++)
                    publishYearList.Items.Add(dt.Rows[i]["publish_year"].ToString());
                publishYearList.SelectedIndex = -1;

                //publishYearList.DataSource = dt;
                //publishYearList.DisplayMember = "publish_year";
                
                cnn.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Can not open connection !\n" +
                    "Error code :" + ex.ToString(), "Error");
            }

        }
        
        public void Retrive()
        {
            throw new NotImplementedException();
        }

        private void button11_Click(object sender, EventArgs e)
        {
            string text = emailText.Text;
            
            if (string.IsNullOrEmpty(text))
            {
                MessageBox.Show("You must enter an email first.", "Error",
                    MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            else
            {
                // burada sql serverdan alıp search etcek..
                // varsa bilgilerini yazdırcak 
                // yoksa bulunamadı bilgisi verilecek..

                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand("find_user_info", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                SqlParameter info = cmd.Parameters.Add("@email_info", SqlDbType.VarChar, 70);
                info.Direction = ParameterDirection.Input;
                info.Value = text;

                try
                {
                    cnn.Open();

                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);


                    if(dt.Rows.Count > 0)
                    {
                        nameText.Text = "" + dt.Rows[0][0];
                        phoneText.Text = "" + dt.Rows[0][1];
                        addressText.Text = "" + dt.Rows[0][2];
                        eventsText.Text = "" + dt.Rows[0][3];

                        for(int i=1; i < dt.Rows.Count; i++)
                        {
                            eventsText.AppendText(Environment.NewLine);
                            eventsText.AppendText(dt.Rows[i][3].ToString());
                        }

                        emailText.Enabled = false; nameText.Enabled = true;
                        phoneText.Enabled = true; addressText.Enabled = true;
                    }
                    else
                    {
                        MessageBox.Show("There is no user for this email address.\n" +
                            "Please check email address.", "Error", MessageBoxButtons.OK,
                            MessageBoxIcon.Error);
                    }
                    cnn.Close();
                    deleteUser.Enabled = true;
                    updateUser.Enabled = true;
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Cannot open connection !\n" +
                        "Error code : "+ ex.ToString());
                }

            }
            
        }

        private void deleteUser_Click(object sender, EventArgs e)
        {
            string text = emailText.Text;
            DialogResult result = MessageBox.Show("Are you sure to delete " +
                " " + "user from the database ?", "Confirm", MessageBoxButtons.YesNo, MessageBoxIcon.Question);

            if(result == DialogResult.Yes)
            {
                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand("delete_user", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                SqlParameter info = cmd.Parameters.Add("@email", SqlDbType.VarChar, 70);
                info.Direction = ParameterDirection.Input;
                info.Value = text;

                try
                {
                    cnn.Open();

                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);
                    
                    MessageBox.Show("User :" +text+" has deleted.", "Success", MessageBoxButtons.OK,
                        MessageBoxIcon.Information);

                    cnn.Close();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Cannot open a connection !\n Error :" +ex.ToString(),
                        "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }

            }
            else
            {
                MessageBox.Show("Deletion has been cancelled.\n", "Failure",
                    MessageBoxButtons.OK, MessageBoxIcon.None);
            }
        }

        private void updateUser_Click(object sender, EventArgs e)
        {
            string costumer = emailText.Text;
            DialogResult result = MessageBox.Show("Do you want to update all information " +
                "of costumer "+costumer+" ?","Confirm", MessageBoxButtons.YesNo);

            if(result == DialogResult.Yes)
            {
                string name = nameText.Text;
                string phone = phoneText.Text;
                string address = addressText.Text;
                
                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand("update_user", cnn);
                cmd.CommandType = CommandType.StoredProcedure;

                SqlParameter parameter1 = cmd.Parameters.Add("@email", SqlDbType.VarChar, 70);
                parameter1.Direction = ParameterDirection.Input;
                parameter1.Value = costumer;

                SqlParameter parameter2 = cmd.Parameters.Add("@name", SqlDbType.VarChar, 60);
                parameter2.Direction = ParameterDirection.Input;
                parameter2.Value = name;

                SqlParameter parameter3 = cmd.Parameters.Add("@number", SqlDbType.Int);
                parameter3.Direction = ParameterDirection.Input;
                parameter3.Value = Convert.ToInt32(phone);
                // bu nasıl olacak ??

                SqlParameter parameter4 = cmd.Parameters.Add("@address", SqlDbType.VarChar, 200);
                parameter4.Direction = ParameterDirection.Input;
                parameter4.Value = address;

                try
                {
                    cnn.Open();

                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);


                    MessageBox.Show("Information about " +costumer+" has been changed succesfully.",
                        "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);

                    cnn.Close();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Cannot open connection !\n Error: " + ex.ToString(), "Error",
                        MessageBoxButtons.OK, MessageBoxIcon.Error);
                }


            }
            else
            {
                result = MessageBox.Show("Do you want to reload all the information ?",
                    "Confirm", MessageBoxButtons.YesNo);

                if(result == DialogResult.Yes)
                {
                    button11_Click(sender, e);
                }
            }

        }

        private void viewBookButton_Click(object sender, EventArgs e)
        {
            if(selection.SelectedIndex > -1)
            {

                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand();
                if (selection.Text == "All Books")
                {
                    cmd = new SqlCommand("view_book_all", cnn);
                    cmd.CommandType = CommandType.StoredProcedure;
                }
                else
                {
                    cmd = new SqlCommand("view_book_sold", cnn);
                    cmd.CommandType = CommandType.StoredProcedure;
                }

                try
                {
                    cnn.Open();
                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);

                    bookNameList.DataSource = dt;
                    bookNameList.DisplayMember = "title";

                    cnn.Close();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Some error occured while reading data !\n" +
                        "Error code :" + ex.ToString(), "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }

            }
            else
            {
                MessageBox.Show("Please first select view type..",
                    "Warning", MessageBoxButtons.OK);
            }

            titleText.Enabled = true;
        }

        private void BookNameList_Changed(object sender, EventArgs e)
        {
            string text = bookNameList.Text;
            if (!string.Equals(text, "System.Data.DataRowView"))
            {
                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand("fill_view", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                SqlParameter info = cmd.Parameters.Add("@name", SqlDbType.VarChar, 50);
                info.Direction = ParameterDirection.Input;
                info.Value = text;

                try
                {
                    cnn.Open();
                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);

                    titleText.Enabled = true;
                    titleText.Text = text;
                    authorText.Text = dt.Rows[0][0].ToString();
                    warehouseText.Text = dt.Rows[0][1].ToString();
                    publisherText.Text = dt.Rows[0][2].ToString();
                    publishYearText.Text = dt.Rows[0][3].ToString();
                    
                    if(dt.Rows.Count > 1)
                    {
                        for(int i=1; i < dt.Rows.Count; i++)
                        {
                            if (!string.IsNullOrEmpty(dt.Rows[i][1].ToString()))
                                warehouseText.AppendText(" -- "+ dt.Rows[i][1].ToString());
                        }
                    }


                    cnn.Close();

                }
                catch
                {
                    titleText.ResetText(); authorText.ResetText();
                    warehouseText.ResetText(); publishYearText.ResetText();
                    publisherText.ResetText(); titleText.Enabled = false;

                    MessageBox.Show("We are so sorry. It looks like we are out of that book. " +
                        "Please select another book from the list.\n", "Oopss..", MessageBoxButtons.OK,
                        MessageBoxIcon.Information);
                }

            }
            
        }

        private void queryButton_Click(object sender, EventArgs e)
        {
            string type = interCat.Text;

            if (!string.IsNullOrEmpty(type))
            {
                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand();
                if (string.Equals(type, "OR"))
                {
                    cmd = new SqlCommand("search_query_or", cnn);
                    cmd.CommandType = CommandType.StoredProcedure;
                }
                else
                {
                    cmd = new SqlCommand("search_query_and", cnn);
                    cmd.CommandType = CommandType.StoredProcedure;
                }

                string a = "";
                for (int x = 0; x < authorList.CheckedItems.Count; x++)
                {
                    int b = x + 1;
                    if (b == authorList.CheckedItems.Count)
                    {
                        a = a + authorList.CheckedItems[x].ToString();
                        break;
                    }

                    a = a + authorList.CheckedItems[x].ToString() + ", ";
                }

                string w = "";
                for (int x = 0; x < warehouseList.CheckedItems.Count; x++)
                {
                    int b = x + 1;
                    if (b == warehouseList.CheckedItems.Count)
                    {
                        w = w + warehouseList.CheckedItems[x].ToString();
                        break;
                    }

                    w = w + warehouseList.CheckedItems[x].ToString() + ", ";
                }

                string p = "";
                for (int x = 0; x < publisherList.CheckedItems.Count; x++)
                {
                    int b = x + 1;
                    if (b == publisherList.CheckedItems.Count)
                    {
                        p = p + publisherList.CheckedItems[x].ToString();
                        break;
                    }

                    p = p + publisherList.CheckedItems[x].ToString() + ", ";
                }

                string py = "";
                for (int x = 0; x < publishYearList.CheckedItems.Count; x++)
                {
                    int b = x + 1;
                    if (b == publishYearList.CheckedItems.Count)
                    {
                        py = py + publishYearList.CheckedItems[x].ToString();
                        break;
                    }

                    py = py + publishYearList.CheckedItems[x].ToString() + ", ";
                }

                SqlParameter param = cmd.Parameters.Add("@author_name", SqlDbType.VarChar, 70);
                param.Direction = ParameterDirection.Input;
                param.Value = a;
                SqlParameter param2 = cmd.Parameters.Add("@ware_name", SqlDbType.VarChar, 70);
                param2.Direction = ParameterDirection.Input;
                param2.Value = w;
                SqlParameter param3 = cmd.Parameters.Add("@publisher", SqlDbType.VarChar, 70);
                param3.Direction = ParameterDirection.Input;
                param3.Value = p;
                SqlParameter param4 = cmd.Parameters.Add("@publish_year", SqlDbType.VarChar, 70);
                param4.Direction = ParameterDirection.Input;
                param4.Value = py;

                try
                {
                    cnn.Open();

                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);


                    if(dt.Rows.Count > 0)
                    {
                        temp = new Form();
                        temp.Size = new Size(500, 200);
                        DataGridView data = new DataGridView();
                        data.DataSource = dt;
                        data.Enabled = false;
                        data.IsAccessible = false;
                        data.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
                        data.Size = new Size(500, 200);

                        temp.MaximizeBox = false;
                        temp.MinimizeBox = false;
                        temp.Controls.Add(data);
                        temp.StartPosition = FormStartPosition.CenterScreen;
                        temp.Show();
                    }
                    else
                    {
                        MessageBox.Show("Cannot find selected items.");
                    }


                    cnn.Close();
                }
                catch(Exception ex)
                {
                    MessageBox.Show("Cannot open connection !\n" +
                        "Error code : "+ ex.ToString(), "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }
                
            }
            else
            {
                MessageBox.Show("Please choose one of the inter categories.\n",
                    "Warning", MessageBoxButtons.OKCancel, MessageBoxIcon.Warning);
            }

        }

        private void deleteBookButton_Click(object sender, EventArgs e)
        {
            string text = bookNameList.Text;
            DialogResult result = MessageBox.Show("Do you really want to delete "+text+" ?",
                "Confirm", MessageBoxButtons.YesNo, MessageBoxIcon.Warning);

            if(result == DialogResult.Yes)
            {
                //delete from database
                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand("delete_book", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                SqlParameter info = cmd.Parameters.Add("@name", SqlDbType.VarChar, 50);
                info.Direction = ParameterDirection.Input;
                info.Value = text;

                try
                {
                    cnn.Open();

                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);

                    //deleted...
                    cnn.Close();
                    viewBookButton_Click(sender, e);
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Some error occured while accessing data !\n" +
                        "Eror code :"+ex.ToString(), "Error", MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }


                //update list
                viewBookButton_Click(sender, e);
                MessageBox.Show(""+text+" has deleted.\n", "Success", MessageBoxButtons.OK,
                    MessageBoxIcon.Information);
            }

        }

        private void addBookButton_Click(object sender, EventArgs e)
        {
            temp = new Form();

            Label id = new Label();
            id.Text = "Please enter isbn for book :";
            id.Font = new Font("Microsoft Sans Serif", 10F);
            id.Location = new Point(temp.Location.X + 5, temp.Location.Y + 10);
            id.Size = new Size(200, 50);

            idBox = new TextBox();
            idBox.Location = new Point(id.Location.X + id.Size.Width, id.Location.Y);
            idBox.Size = new Size(150, 100);

            Label year = new Label();
            year.Text = "Please enter year for book :";
            year.Font = new Font("Microsoft Sans Serif", 10F);
            year.Location = new Point(temp.Location.X + 5, id.Location.Y + id.Size.Height);
            year.Size = new Size(200, 50);

            yearBox = new TextBox();
            yearBox.Location = new Point(year.Location.X + year.Size.Width, year.Location.Y);
            yearBox.Size = new Size(150, 100);

            // title 
            Label title = new Label();
            title.Text = "Please enter title for book :";
            title.Font = new Font("Microsoft Sans Serif", 10F);
            title.Location = new Point(temp.Location.X + 5, year.Location.Y + year.Size.Height);
            title.Size = new Size(200, 50);

            titleBox = new TextBox();
            titleBox.Location = new Point(title.Location.X + title.Size.Width, title.Location.Y);
            titleBox.Size = new Size(150, 100);

            // price
            Label price = new Label();
            price.Text = "Please enter price for book:";
            price.Font = new Font("Microsoft Sans Serif", 10F);
            price.Location = new Point(temp.Location.X + 5, title.Location.Y + title.Size.Height);
            price.Size = new Size(200, 50);

            priceBox = new TextBox();
            priceBox.Location = new Point(price.Location.X + price.Size.Width, price.Location.Y);
            priceBox.Size = new Size(150, 100);

            // author name
            Label author = new Label();
            author.Text = "Please enter author name :";
            author.Font = new Font("Microsoft Sans Serif", 10F);
            author.Location = new Point(temp.Location.X + 5, price.Location.Y + price.Size.Height);
            author.Size = new Size(200, 50);

            authorBox = new TextBox();
            authorBox.Location = new Point(author.Location.X + author.Size.Width, author.Location.Y);
            authorBox.Size = new Size(150, 100);

            // publisher name
            Label publish = new Label();
            publish.Text = "Please enter publisher :";
            publish.Font = new Font("Microsoft Sans Serif", 10F);
            publish.Location = new Point(temp.Location.X + 5, author.Location.Y + author.Size.Height);
            publish.Size = new Size(200, 50);

            publishBox = new TextBox();
            publishBox.Location = new Point(publish.Location.X + publish.Size.Width, publish.Location.Y);
            publishBox.Size = new Size(150, 100);

            // ok and cancel..
            Button ok = new Button();
            Button cancel = new Button();
            ok.Text = "Ok";
            cancel.Text = "Cancel";
            ok.Location = new Point(temp.Width - cancel.Width - 5, publishBox.Bottom + 5);
            cancel.Location = new Point(temp.Width - 5, publishBox.Bottom + 5);
            ok.Click += new EventHandler(create_book_click);
            cancel.Click += new EventHandler(cancel_click);

            temp.Size = new Size(375,350);
            temp.Text = "Create Book Screen";

            temp.Controls.Add(id);
            temp.Controls.Add(idBox);
            temp.Controls.Add(year);
            temp.Controls.Add(yearBox);
            temp.Controls.Add(title);
            temp.Controls.Add(titleBox);
            temp.Controls.Add(price);
            temp.Controls.Add(priceBox);
            temp.Controls.Add(author);
            temp.Controls.Add(authorBox);
            temp.Controls.Add(publish);
            temp.Controls.Add(publishBox);
            temp.Controls.Add(ok);
            temp.Controls.Add(cancel);

            temp.MaximizeBox = false;
            temp.StartPosition = FormStartPosition.CenterScreen;
            temp.Show();
        }

        private void create_book_click(object sender, EventArgs e)
        {
            //idBox, yearBox, titleBox, priceBox, authorBox, publishBox;
            string isbn = idBox.Text; string year = yearBox.Text;
            string ttl = titleBox.Text; string prc = priceBox.Text;
            string aut = authorBox.Text; string pub = publishBox.Text;

            if(String.IsNullOrEmpty(isbn) || String.IsNullOrEmpty(year) ||
                String.IsNullOrEmpty(ttl) || String.IsNullOrEmpty(prc) ||
                String.IsNullOrEmpty(aut) || String.IsNullOrEmpty(pub))
            {
                MessageBox.Show("The areas cannot be empty.");
            }
            else
            {
                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand("book_insert_all", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                SqlParameter parameter = cmd.Parameters.Add("@isbn", SqlDbType.Int);
                parameter.Direction = ParameterDirection.Input;
                parameter.Value = isbn;
                SqlParameter parameter2 = cmd.Parameters.Add("@publish_year", SqlDbType.Date);
                parameter2.Direction = ParameterDirection.Input;
                parameter2.Value = year;
                SqlParameter parameter3 = cmd.Parameters.Add("@title", SqlDbType.VarChar, 50);
                parameter3.Direction = ParameterDirection.Input;
                parameter3.Value = ttl;
                SqlParameter parameter4 = cmd.Parameters.Add("@price", SqlDbType.Int);
                parameter4.Direction = ParameterDirection.Input;
                parameter4.Value = prc;
                SqlParameter parameter5 = cmd.Parameters.Add("@author_name", SqlDbType.VarChar, 60);
                parameter5.Direction = ParameterDirection.Input;
                parameter5.Value = aut;
                SqlParameter parameter6 = cmd.Parameters.Add("@publisher_name", SqlDbType.VarChar, 60);
                parameter6.Direction = ParameterDirection.Input;
                parameter6.Value = pub;

                try
                {
                    cnn.Open();

                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);

                    MessageBox.Show("The book is successively added.");
                    viewBookButton_Click(sender, e);
                    cnn.Close();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Cannot open connection !\n" +
                        "Error code : " + ex.ToString(), "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                    cancel_click(sender, e);
                }
                cancel_click(sender, e);
            }

        }

        private void cancel_click(object sender, EventArgs e)
        {
            temp.Dispose();
            temp.Close();
        }
        
        private void addShopButton_Click(object sender, EventArgs e)
        {
            temp = new Form();

            Label name = new Label();
            name.Text = "Please enter name of book :";
            name.Font = new Font("Microsoft Sans Serif", 10F);
            name.Location = new Point(temp.Location.X + 5, temp.Location.Y + 10);
            name.Size = new Size(200, 50);

            nameBox = new TextBox();
            nameBox.Location = new Point(name.Location.X + name.Size.Width, name.Location.Y);
            nameBox.Size = new Size(150, 100);

            Label email = new Label();
            email.Text = "Please enter email address:";
            email.Font = new Font("Microsoft Sans Serif", 10F);
            email.Location = new Point(temp.Location.X + 5, name.Location.Y + name.Size.Height);
            email.Size = new Size(200, 50);

            emailBox = new TextBox();
            emailBox.Location = new Point(email.Location.X + email.Size.Width, email.Location.Y);
            emailBox.Size = new Size(150, 100);

            // ok and cancel..
            Button ok = new Button();
            Button cancel = new Button();
            ok.Text = "Add";
            cancel.Text = "Cancel";
            ok.Location = new Point(temp.Width - cancel.Width - 5, emailBox.Bottom + 5);
            cancel.Location = new Point(temp.Width - 5, emailBox.Bottom + 5);
            ok.Click += new EventHandler(add_shop);
            cancel.Click += new EventHandler(cancel_click);

            temp.Size = new Size(375, 150);
            temp.Text = "Create Shoplist Screen";
            //addForm.BackgroundImage = ActiveForm.BackgroundImage;
            //addForm.BackgroundImageLayout = ActiveForm.BackgroundImageLayout;

            temp.Controls.Add(name);
            temp.Controls.Add(nameBox);
            temp.Controls.Add(email);
            temp.Controls.Add(emailBox);
            temp.Controls.Add(ok);
            temp.Controls.Add(cancel);

            temp.MaximizeBox = false;
            temp.StartPosition = FormStartPosition.CenterScreen;
            temp.Show();
        }

        private void add_shop(object sender, EventArgs e)
        {
            // nameBox and emailBox..
            string ttl = nameBox.Text; string mail = emailBox.Text;
            if (string.IsNullOrEmpty(mail) || string.IsNullOrEmpty(ttl))
            {
                MessageBox.Show("Texts cannot be empty.");
            }
            else
            {
                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand("add_shoplist", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                SqlParameter parameter = cmd.Parameters.Add("@email", SqlDbType.VarChar, 70);
                parameter.Direction = ParameterDirection.Input;
                parameter.Value = mail;
                SqlParameter parameter2 = cmd.Parameters.Add("@title", SqlDbType.VarChar, 50);
                parameter2.Direction = ParameterDirection.Input;
                parameter2.Value = ttl;

                try
                {
                    cnn.Open();

                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);

                    MessageBox.Show("The book is successively added to shoplist.");
                    viewBookButton_Click(sender, e);
                    cnn.Close();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Cannot open connection !\n" +
                        "Error code : "+ ex.ToString(), "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
                }

            }

        }

        private void deleteShopButton_Click(object sender, EventArgs e)
        {
            temp = new Form();

            Label id = new Label();
            id.Text = "Please enter email address :";
            id.Font = new Font("Microsoft Sans Serif", 10F);
            id.Location = new Point(temp.Location.X + 5, temp.Location.Y + 10);
            id.Size = new Size(200, 50);

            shopidBox = new TextBox();
            shopidBox.Location = new Point(id.Location.X + id.Size.Width, id.Location.Y);
            shopidBox.Size = new Size(150, 100);

            Label shop = new Label();
            shop.Text = "Please enter book name :";
            shop.Font = new Font("Microsoft Sans Serif", 10F);
            shop.Location = new Point(temp.Location.X + 5, id.Location.Y + id.Size.Height);
            shop.Size = new Size(200, 50);

            shopBox = new TextBox();
            shopBox.Location = new Point(shop.Location.X + shop.Size.Width, shop.Location.Y);
            shopBox.Size = new Size(150, 100);
            // ok and cancel..
            Button ok = new Button();
            Button cancel = new Button();
            ok.Text = "Delete";
            cancel.Text = "Cancel";
            ok.Location = new Point(temp.Width - cancel.Width - 5, shopBox.Bottom + 5);
            cancel.Location = new Point(temp.Width - 5, shopBox.Bottom + 5);
            ok.Click += new EventHandler(delete_shop);
            cancel.Click += new EventHandler(cancel_click);

            temp.Size = new Size(375, 150);
            temp.Text = "Delete Shoplist Screen";
            //addForm.BackgroundImage = ActiveForm.BackgroundImage;
            //addForm.BackgroundImageLayout = ActiveForm.BackgroundImageLayout;

            temp.Controls.Add(id);
            temp.Controls.Add(shopidBox);
            temp.Controls.Add(shop);
            temp.Controls.Add(shopBox);
            temp.Controls.Add(ok);
            temp.Controls.Add(cancel);

            temp.MaximizeBox = false;
            temp.StartPosition = FormStartPosition.CenterScreen;
            temp.Show();
        }

        private void viewToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Dispose();
            Close();
        }

        private void toolStripMenuItem1_Click(object sender, EventArgs e)
        {
            Timer timer = new Timer();
            timer.Interval = 10000;

            temp = new Form();
            Label label = new Label();
            label.Text = "BookStore is an application to help to manage Database.\n" +
                "It is developed by\n Mahmut AKTAŞ & Muhammet ŞERAMET.\n" +
                "This form will be closed in 10 seconds.\n";
            label.Location = new Point(temp.Location.X +5, temp.Location.Y+5);
            label.Size = new Size(400,50);

            temp.Size = new Size(300, 150);
            temp.StartPosition = FormStartPosition.CenterScreen;
            temp.Controls.Add(label);
            timer.Tick += new EventHandler(timer_Tick);
            timer.Start();
            temp.Show();
        }

        private void timer_Tick(object sender, EventArgs e)
        {
            temp.Dispose();
            temp.Close();
        }

        private void delete_shop(object sender, EventArgs e)
        {
            string mail = shopidBox.Text; string bookName = shopBox.Text;

            if(string.IsNullOrEmpty(mail) || string.IsNullOrEmpty(bookName))
            {
                MessageBox.Show("Texts cannot be empty.");
            }
            else
            {
                cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
                SqlCommand cmd = new SqlCommand("delete_shop", cnn);
                cmd.CommandType = CommandType.StoredProcedure;
                SqlParameter parameter = cmd.Parameters.Add("@email", SqlDbType.VarChar, 70);
                parameter.Direction = ParameterDirection.Input;
                parameter.Value = mail;
                SqlParameter parameter2 = cmd.Parameters.Add("@title", SqlDbType.VarChar, 50);
                parameter2.Direction = ParameterDirection.Input;
                parameter2.Value = bookName;
                
                try
                {
                    cnn.Open();

                    SqlDataAdapter dr = new SqlDataAdapter(cmd);
                    DataTable dt = new DataTable();
                    dr.Fill(dt);

                    MessageBox.Show("The shop list is successively deleted.");
                    viewBookButton_Click(sender, e);

                    cnn.Close();
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Cannot open connection !\n" +
                        "Error code : "+ex.ToString(), "Error", MessageBoxButtons.OK,
                        MessageBoxIcon.Error);
                }
                
            }

        }

        private void showShopButton_Click(object sender, EventArgs e)
        {
            cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127");
            SqlCommand cmd = new SqlCommand("all_shops", cnn);
            cmd.CommandType = CommandType.StoredProcedure;
            TextBox output = new TextBox();
            output.Text = "The shopping list is:";
            output.AppendText(Environment.NewLine);
            try
            {
                cnn.Open();
                SqlDataAdapter dr = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                dr.Fill(dt);

                for(int i=0; i < dt.Rows.Count; i++)
                {
                    output.AppendText(dt.Rows[i][0].ToString() + " is ordered by " + dt.Rows[i][1].ToString());
                    output.AppendText(Environment.NewLine);
                }

                cnn.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Cannot open connection !\n Error code :" + ex.ToString(),
                    "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            MessageBox.Show(output.Text, "All Shops", MessageBoxButtons.OK, MessageBoxIcon.None);

        }

        private void authorCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            if (authorCheckBox.Checked)
                authorList.Enabled = true;
            else
                authorList.Enabled = false;
        }

        private void warehouseCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            if (warehouseCheckBox.Checked)
                warehouseList.Enabled = true;
            else
                warehouseList.Enabled = false;
        }

        private void publisherCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            if (publisherCheckBox.Checked)
                publisherList.Enabled = true;
            else
                publisherList.Enabled = false;
        }

        private void yearCheckBox_CheckedChanged(object sender, EventArgs e)
        {
            if (yearCheckBox.Checked)
                publishYearList.Enabled = true;
            else
                publishYearList.Enabled = false;
        }

        private void setTitleButton_Click(object sender, EventArgs e)
        {
            string new_name = titleText.Text;
            string old_name = bookNameList.Text;
       
            if (titleText.Enabled)
            {

                if(!string.Equals(old_name, new_name))
                {
                    DialogResult result = MessageBox.Show("Do you really want to change the title " +
                        "of the book "+old_name+" to new "+new_name+" ?", "Confirm Changes",
                        MessageBoxButtons.YesNo, MessageBoxIcon.Question);


                    if(result == DialogResult.Yes)
                    {
                        cnn = new SqlConnection(@"server=MUHAMMET-PC;Trusted_Connection=yes;database=bookstore127;");
                        SqlCommand cmd = new SqlCommand("change_book_title", cnn);
                        cmd.CommandType = CommandType.StoredProcedure;
                        SqlParameter oldTitle = cmd.Parameters.Add("@oldtitle", SqlDbType.VarChar, 50);
                        oldTitle.Direction = ParameterDirection.Input;
                        oldTitle.Value = old_name;
                        SqlParameter newTitle = cmd.Parameters.Add("@newtitle", SqlDbType.VarChar, 50);
                        newTitle.Direction = ParameterDirection.Input;
                        newTitle.Value = new_name;

                        try
                        {
                            cnn.Open();

                            SqlDataAdapter dr = new SqlDataAdapter(cmd);
                            DataTable dt = new DataTable();
                            dr.Fill(dt);
                            MessageBox.Show("The title has changed succesfully.", "Success", MessageBoxButtons.OK);

                            cnn.Close();
                            viewBookButton_Click(sender, e);
                        }
                        catch (Exception ex)
                        {
                            MessageBox.Show("Cannot open connection !\n" +
                                "Error code :" + ex.ToString(), "Error", MessageBoxButtons.OK,
                                MessageBoxIcon.Error);
                        }
                        
                    }
                    else
                    {
                        titleText.Text = old_name;
                    }
                    
                }
                else
                {
                    MessageBox.Show("Old name and the new name cannot be same.", "Error",
                        MessageBoxButtons.OK, MessageBoxIcon.Error);
                }

            }
            else
            {
                MessageBox.Show("Cannot change the title now !\n", "Title Text Is not Enabled",
                    MessageBoxButtons.OK, MessageBoxIcon.Stop);
            }

        }

    }
}
