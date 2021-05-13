using Ninject;
using Ninject.Modules;
using System;
using System.Reflection;

namespace TPE
{
    class Program
    {
        public static void Main(string[] args)
        {           
            try
            {
                StandardKernel kernel = new StandardKernel(new Module(false));
                IChargementParametres injectable;

                injectable = kernel.Get<IChargementParametres>();
                TicketDeCaisse ticket = new TicketDeCaisse(injectable);

                kernel = new StandardKernel(new Module(true));
                injectable = kernel.Get<IChargementParametres>();

                TicketDeCaisse ticket2 = new TicketDeCaisse(injectable);

                Console.WriteLine(ticket.GetParamClient("123456789"));
                Console.WriteLine(ticket2.GetParamClient("123456789"));
            }
            catch (Exception e) {
                Console.WriteLine("Something went wrong: " + e);
            }
            
        }
    }
    public class TicketDeCaisse
    {
        private IChargementParametres injectable;

        public TicketDeCaisse(IChargementParametres injectable)
        {
            this.injectable = injectable;
        }
        public ParamClient GetParamClient(string id)
        {
            CompteClient client = injectable.GetClientByID(id);
            return injectable.GetParamClient(client);
        }

        public void SetChargementParametres(IChargementParametres injection)
        {
            this.injectable = injection;
        }
    }
    public class Module : NinjectModule
    {
        public static bool Implementation { get; set; }
        public Module(bool v)
        {
            Implementation = v;
        }

        public override void Load()
        {
            if (Implementation == false)
            {
                Bind<IChargementParametres>().To<ChargementParametres>();
            }else
            {
                Bind<IChargementParametres>().To<ChargementParametresType2>();
            }
        }
    }
    public interface IChargementParametres
    {
        CompteClient GetClientByID(string id);
        ParamClient GetParamClient(CompteClient client);
    }

    public class ChargementParametres : IChargementParametres
    {
        public ChargementParametres() { }
        public CompteClient GetClientByID(string id)
        {
            return new CompteClient("Joseph Pasquier", id, new ParamClient("famille nombreuse"));
        }
        public ParamClient GetParamClient(CompteClient client)
        {
            return client.Param;
        }
    }
    public class ChargementParametresType2 : IChargementParametres
    {
        public ChargementParametresType2() { }
        public CompteClient GetClientByID(string id)
        {
            return new CompteClient("Stéphanie Marty", id, new ParamClient("un autre profil"));
        }
        public ParamClient GetParamClient(CompteClient client)
        {
            return client.Param;
        }
    }

    public class CompteClient
    {
        public string Name { get; set; }
        public string Id { get; set; }
        public ParamClient Param { get; set; }

        public CompteClient(string name, string id, ParamClient param)
        {
            Name = name;
            Id = id;
            Param = param;
        }
        public override string ToString()
        {
            return "Le client " + Name + ", id@" + Id + ", a pour profil " + Param;
        }
    }

    public class ParamClient
    {
        public string Param { get; set; }

        public ParamClient(string param)
        {
            Param = param;
        }
        public override string ToString()
        {
            return Param;
        }
    }
}
