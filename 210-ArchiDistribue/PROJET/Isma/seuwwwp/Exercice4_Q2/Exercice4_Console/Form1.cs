using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace Exercice4_Console
{
    public partial class Form1 : Form
    {
        public Form1(String url, String offre, String info, String info2)
        {

            InitializeComponent();


            label1.Text = offre;
            label2.Text = info;
            label3.Text = "SUIVANT";
            label4.Text = info2;
            pictureBox1.Load(url);
        }

        private void label3_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void pictureBox2_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}
