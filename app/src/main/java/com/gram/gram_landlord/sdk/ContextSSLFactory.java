package com.gram.gram_landlord.sdk;

import com.gram.gram_landlord.App;
import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

public class ContextSSLFactory {
    private static final String SERVER_JKS = "tls/landlord.server.bks";
    private static final String CLIENT_JKS = "tls/landlord.client.bks";
    private static final String PASSWORD = "yangshu";
    private static ContextSSLFactory factory;
    private SSLContext sslContext_server;
    private SSLContext sslContext_client;

    private ContextSSLFactory() {
        SSLContext sslContext1 = null;
        SSLContext sslContext2 = null;
        try {
            sslContext1 = SSLContext.getInstance("TLS");
            sslContext2 = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(sslContext1 != null && sslContext2 != null) {
            try {
                if(getKeyManagersServer() != null && getTrustManagersServer() != null)
                    sslContext1.init(getKeyManagersServer(), getTrustManagersServer(), null);
                if(getKeyManagersClient() != null && getTrustManagersClient() != null)
                    sslContext2.init(getKeyManagersClient(), getTrustManagersClient(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sslContext_server = sslContext1;
        sslContext_client = sslContext2;
    }

    public static ContextSSLFactory getInstance() {
        if(factory == null) factory = new ContextSSLFactory();
        return factory;
    }

    public SSLContext getServerSslContext() {
        return sslContext_server;
    }

    public SSLContext getClientSslContext() {
        return sslContext_client;
    }

    private TrustManager[] getTrustManagersServer() {
        return getTrustManagers(SERVER_JKS, PASSWORD);
    }

    private TrustManager[] getTrustManagersClient() {
        return getTrustManagers(CLIENT_JKS, PASSWORD);
    }

    private TrustManager[] getTrustManagers(String jksPath, String password) {
        InputStream is = null;
        KeyStore ks;
        TrustManagerFactory fac;
        TrustManager[] tms = null;
        try {
            fac = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            is = App.getApp().getAssets().open(jksPath);
            ks = KeyStore.getInstance("bks");
            ks.load(is, password.toCharArray());
            fac.init(ks);
            tms = fac.getTrustManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tms;
    }

    private KeyManager[] getKeyManagersServer() {
        return getKeyManagers(SERVER_JKS, PASSWORD);
    }

    private KeyManager[] getKeyManagersClient() {
        return getKeyManagers(CLIENT_JKS, PASSWORD);
    }

    private KeyManager[] getKeyManagers(String jksPath, String password) {
        InputStream is = null;
        KeyStore ks;
        KeyManagerFactory fac;
        KeyManager[] kms = null;
        try {
            fac = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            is = App.getApp().getAssets().open(jksPath);
            ks = KeyStore.getInstance("bks");
            ks.load(is, password.toCharArray());
            fac.init(ks, password.toCharArray());
            kms = fac.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return kms;
    }

}
