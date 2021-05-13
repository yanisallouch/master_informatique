using System;
using System.Collections.Generic;
using System.Text;
using System.Web.Services;

namespace TDWS {

    [WebService(Namespace = "http://mondomaine.fr/monAppli")]
    class ServiceDate {
        [WebMethod]
        public String getDate1(){
            return "11/03/2021";
        }

        [WebMethod]
        public String getDate2(Format f){
            string res = f switch {
                Format.JMA => "11/03/2021",
                Format.HMS => "08:22:03",
                Format.HMSJMA => "08:22:03;11/03/2021",
                _ => "Unsupported"
            };
            return res;
        }

        [WebMethod]
        public Date getDate3(){
            return new Date(11,03,2021);
        }
    }

    public class Date {
        public int jour;
        public int mois;
        public int annee;

        public Date() {}

        public Date(int j,int m,int a) {
            jour=j;
            mois=m;
            annee=a;
        }
    }

    public enum Format {
        JMA, // jour mois annee
        HMS, // Heure, minutes, secondes
        HMSJMA
    }
}