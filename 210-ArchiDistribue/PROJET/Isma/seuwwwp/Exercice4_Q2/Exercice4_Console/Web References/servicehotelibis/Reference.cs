﻿//------------------------------------------------------------------------------
// <auto-generated>
//     Ce code a été généré par un outil.
//     Version du runtime :4.0.30319.42000
//
//     Les modifications apportées à ce fichier peuvent provoquer un comportement incorrect et seront perdues si
//     le code est régénéré.
// </auto-generated>
//------------------------------------------------------------------------------

// 
// Ce code source a été automatiquement généré par Microsoft.VSDesigner, Version 4.0.30319.42000.
// 
#pragma warning disable 1591

namespace Exercice4_Console.servicehotelibis {
    using System;
    using System.Web.Services;
    using System.Diagnostics;
    using System.Web.Services.Protocols;
    using System.Xml.Serialization;
    using System.ComponentModel;
    
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Web.Services.WebServiceBindingAttribute(Name="WebService2Soap", Namespace="http://tempuri.org/")]
    public partial class WebService2 : System.Web.Services.Protocols.SoapHttpClientProtocol {
        
        private System.Threading.SendOrPostCallback AuthentificationOperationCompleted;
        
        private System.Threading.SendOrPostCallback GetListeOffreOperationCompleted;
        
        private System.Threading.SendOrPostCallback VerifyOfferOperationCompleted;
        
        private System.Threading.SendOrPostCallback BookingRegistrationOperationCompleted;
        
        private bool useDefaultCredentialsSetExplicitly;
        
        /// <remarks/>
        public WebService2() {
            this.Url = global::Exercice4_Console.Properties.Settings.Default.Exercice4_Console_servicehotelibis_WebService2;
            if ((this.IsLocalFileSystemWebService(this.Url) == true)) {
                this.UseDefaultCredentials = true;
                this.useDefaultCredentialsSetExplicitly = false;
            }
            else {
                this.useDefaultCredentialsSetExplicitly = true;
            }
        }
        
        public new string Url {
            get {
                return base.Url;
            }
            set {
                if ((((this.IsLocalFileSystemWebService(base.Url) == true) 
                            && (this.useDefaultCredentialsSetExplicitly == false)) 
                            && (this.IsLocalFileSystemWebService(value) == false))) {
                    base.UseDefaultCredentials = false;
                }
                base.Url = value;
            }
        }
        
        public new bool UseDefaultCredentials {
            get {
                return base.UseDefaultCredentials;
            }
            set {
                base.UseDefaultCredentials = value;
                this.useDefaultCredentialsSetExplicitly = true;
            }
        }
        
        /// <remarks/>
        public event AuthentificationCompletedEventHandler AuthentificationCompleted;
        
        /// <remarks/>
        public event GetListeOffreCompletedEventHandler GetListeOffreCompleted;
        
        /// <remarks/>
        public event VerifyOfferCompletedEventHandler VerifyOfferCompleted;
        
        /// <remarks/>
        public event BookingRegistrationCompletedEventHandler BookingRegistrationCompleted;
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("http://tempuri.org/Authentification", RequestNamespace="http://tempuri.org/", ResponseNamespace="http://tempuri.org/", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        public bool Authentification(string login, string password) {
            object[] results = this.Invoke("Authentification", new object[] {
                        login,
                        password});
            return ((bool)(results[0]));
        }
        
        /// <remarks/>
        public void AuthentificationAsync(string login, string password) {
            this.AuthentificationAsync(login, password, null);
        }
        
        /// <remarks/>
        public void AuthentificationAsync(string login, string password, object userState) {
            if ((this.AuthentificationOperationCompleted == null)) {
                this.AuthentificationOperationCompleted = new System.Threading.SendOrPostCallback(this.OnAuthentificationOperationCompleted);
            }
            this.InvokeAsync("Authentification", new object[] {
                        login,
                        password}, this.AuthentificationOperationCompleted, userState);
        }
        
        private void OnAuthentificationOperationCompleted(object arg) {
            if ((this.AuthentificationCompleted != null)) {
                System.Web.Services.Protocols.InvokeCompletedEventArgs invokeArgs = ((System.Web.Services.Protocols.InvokeCompletedEventArgs)(arg));
                this.AuthentificationCompleted(this, new AuthentificationCompletedEventArgs(invokeArgs.Results, invokeArgs.Error, invokeArgs.Cancelled, invokeArgs.UserState));
            }
        }
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("http://tempuri.org/GetListeOffre", RequestNamespace="http://tempuri.org/", ResponseNamespace="http://tempuri.org/", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        public string GetListeOffre(string dateDebut, string dateFin, int nombrePersonne) {
            object[] results = this.Invoke("GetListeOffre", new object[] {
                        dateDebut,
                        dateFin,
                        nombrePersonne});
            return ((string)(results[0]));
        }
        
        /// <remarks/>
        public void GetListeOffreAsync(string dateDebut, string dateFin, int nombrePersonne) {
            this.GetListeOffreAsync(dateDebut, dateFin, nombrePersonne, null);
        }
        
        /// <remarks/>
        public void GetListeOffreAsync(string dateDebut, string dateFin, int nombrePersonne, object userState) {
            if ((this.GetListeOffreOperationCompleted == null)) {
                this.GetListeOffreOperationCompleted = new System.Threading.SendOrPostCallback(this.OnGetListeOffreOperationCompleted);
            }
            this.InvokeAsync("GetListeOffre", new object[] {
                        dateDebut,
                        dateFin,
                        nombrePersonne}, this.GetListeOffreOperationCompleted, userState);
        }
        
        private void OnGetListeOffreOperationCompleted(object arg) {
            if ((this.GetListeOffreCompleted != null)) {
                System.Web.Services.Protocols.InvokeCompletedEventArgs invokeArgs = ((System.Web.Services.Protocols.InvokeCompletedEventArgs)(arg));
                this.GetListeOffreCompleted(this, new GetListeOffreCompletedEventArgs(invokeArgs.Results, invokeArgs.Error, invokeArgs.Cancelled, invokeArgs.UserState));
            }
        }
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("http://tempuri.org/VerifyOffer", RequestNamespace="http://tempuri.org/", ResponseNamespace="http://tempuri.org/", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        public string VerifyOffer(string idOffer) {
            object[] results = this.Invoke("VerifyOffer", new object[] {
                        idOffer});
            return ((string)(results[0]));
        }
        
        /// <remarks/>
        public void VerifyOfferAsync(string idOffer) {
            this.VerifyOfferAsync(idOffer, null);
        }
        
        /// <remarks/>
        public void VerifyOfferAsync(string idOffer, object userState) {
            if ((this.VerifyOfferOperationCompleted == null)) {
                this.VerifyOfferOperationCompleted = new System.Threading.SendOrPostCallback(this.OnVerifyOfferOperationCompleted);
            }
            this.InvokeAsync("VerifyOffer", new object[] {
                        idOffer}, this.VerifyOfferOperationCompleted, userState);
        }
        
        private void OnVerifyOfferOperationCompleted(object arg) {
            if ((this.VerifyOfferCompleted != null)) {
                System.Web.Services.Protocols.InvokeCompletedEventArgs invokeArgs = ((System.Web.Services.Protocols.InvokeCompletedEventArgs)(arg));
                this.VerifyOfferCompleted(this, new VerifyOfferCompletedEventArgs(invokeArgs.Results, invokeArgs.Error, invokeArgs.Cancelled, invokeArgs.UserState));
            }
        }
        
        /// <remarks/>
        [System.Web.Services.Protocols.SoapDocumentMethodAttribute("http://tempuri.org/BookingRegistration", RequestNamespace="http://tempuri.org/", ResponseNamespace="http://tempuri.org/", Use=System.Web.Services.Description.SoapBindingUse.Literal, ParameterStyle=System.Web.Services.Protocols.SoapParameterStyle.Wrapped)]
        public string BookingRegistration(string idOffre, Client client) {
            object[] results = this.Invoke("BookingRegistration", new object[] {
                        idOffre,
                        client});
            return ((string)(results[0]));
        }
        
        /// <remarks/>
        public void BookingRegistrationAsync(string idOffre, Client client) {
            this.BookingRegistrationAsync(idOffre, client, null);
        }
        
        /// <remarks/>
        public void BookingRegistrationAsync(string idOffre, Client client, object userState) {
            if ((this.BookingRegistrationOperationCompleted == null)) {
                this.BookingRegistrationOperationCompleted = new System.Threading.SendOrPostCallback(this.OnBookingRegistrationOperationCompleted);
            }
            this.InvokeAsync("BookingRegistration", new object[] {
                        idOffre,
                        client}, this.BookingRegistrationOperationCompleted, userState);
        }
        
        private void OnBookingRegistrationOperationCompleted(object arg) {
            if ((this.BookingRegistrationCompleted != null)) {
                System.Web.Services.Protocols.InvokeCompletedEventArgs invokeArgs = ((System.Web.Services.Protocols.InvokeCompletedEventArgs)(arg));
                this.BookingRegistrationCompleted(this, new BookingRegistrationCompletedEventArgs(invokeArgs.Results, invokeArgs.Error, invokeArgs.Cancelled, invokeArgs.UserState));
            }
        }
        
        /// <remarks/>
        public new void CancelAsync(object userState) {
            base.CancelAsync(userState);
        }
        
        private bool IsLocalFileSystemWebService(string url) {
            if (((url == null) 
                        || (url == string.Empty))) {
                return false;
            }
            System.Uri wsUri = new System.Uri(url);
            if (((wsUri.Port >= 1024) 
                        && (string.Compare(wsUri.Host, "localHost", System.StringComparison.OrdinalIgnoreCase) == 0))) {
                return true;
            }
            return false;
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Xml", "4.8.3752.0")]
    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [System.Xml.Serialization.XmlTypeAttribute(Namespace="http://tempuri.org/")]
    public partial class Clients {
        
        private int idClientField;
        
        private string nomClientField;
        
        private string prenomClientField;
        
        private string carteCreditField;
        
        /// <remarks/>
        public int IdClient {
            get {
                return this.idClientField;
            }
            set {
                this.idClientField = value;
            }
        }
        
        /// <remarks/>
        public string NomClient {
            get {
                return this.nomClientField;
            }
            set {
                this.nomClientField = value;
            }
        }
        
        /// <remarks/>
        public string PrenomClient {
            get {
                return this.prenomClientField;
            }
            set {
                this.prenomClientField = value;
            }
        }
        
        /// <remarks/>
        public string CarteCredit {
            get {
                return this.carteCreditField;
            }
            set {
                this.carteCreditField = value;
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    public delegate void AuthentificationCompletedEventHandler(object sender, AuthentificationCompletedEventArgs e);
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    public partial class AuthentificationCompletedEventArgs : System.ComponentModel.AsyncCompletedEventArgs {
        
        private object[] results;
        
        internal AuthentificationCompletedEventArgs(object[] results, System.Exception exception, bool cancelled, object userState) : 
                base(exception, cancelled, userState) {
            this.results = results;
        }
        
        /// <remarks/>
        public bool Result {
            get {
                this.RaiseExceptionIfNecessary();
                return ((bool)(this.results[0]));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    public delegate void GetListeOffreCompletedEventHandler(object sender, GetListeOffreCompletedEventArgs e);
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    public partial class GetListeOffreCompletedEventArgs : System.ComponentModel.AsyncCompletedEventArgs {
        
        private object[] results;
        
        internal GetListeOffreCompletedEventArgs(object[] results, System.Exception exception, bool cancelled, object userState) : 
                base(exception, cancelled, userState) {
            this.results = results;
        }
        
        /// <remarks/>
        public string Result {
            get {
                this.RaiseExceptionIfNecessary();
                return ((string)(this.results[0]));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    public delegate void VerifyOfferCompletedEventHandler(object sender, VerifyOfferCompletedEventArgs e);
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    public partial class VerifyOfferCompletedEventArgs : System.ComponentModel.AsyncCompletedEventArgs {
        
        private object[] results;
        
        internal VerifyOfferCompletedEventArgs(object[] results, System.Exception exception, bool cancelled, object userState) : 
                base(exception, cancelled, userState) {
            this.results = results;
        }
        
        /// <remarks/>
        public string Result {
            get {
                this.RaiseExceptionIfNecessary();
                return ((string)(this.results[0]));
            }
        }
    }
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    public delegate void BookingRegistrationCompletedEventHandler(object sender, BookingRegistrationCompletedEventArgs e);
    
    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("System.Web.Services", "4.8.3752.0")]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    public partial class BookingRegistrationCompletedEventArgs : System.ComponentModel.AsyncCompletedEventArgs {
        
        private object[] results;
        
        internal BookingRegistrationCompletedEventArgs(object[] results, System.Exception exception, bool cancelled, object userState) : 
                base(exception, cancelled, userState) {
            this.results = results;
        }
        
        /// <remarks/>
        public string Result {
            get {
                this.RaiseExceptionIfNecessary();
                return ((string)(this.results[0]));
            }
        }
    }
}

#pragma warning restore 1591