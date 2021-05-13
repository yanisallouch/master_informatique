using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using Service_Web.Calculatrice;

namespace Service_Web
{
    /// <summary>
    /// Description résumée de WebService1
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // Pour autoriser l'appel de ce service Web depuis un script à l'aide d'ASP.NET AJAX, supprimez les marques de commentaire de la ligne suivante. 
    // [System.Web.Script.Services.ScriptService]
    public class WebService1 : System.Web.Services.WebService
    {

        CalculatorWebService calculatrice = new CalculatorWebService();

        [WebMethod]
        public int add(int a, int b)
        {
            return calculatrice.Add(a, b);
        }

        [WebMethod]
        public int sub(int a, int b)
        {
            return calculatrice.Subtract(a, b);
        }
    }
}
