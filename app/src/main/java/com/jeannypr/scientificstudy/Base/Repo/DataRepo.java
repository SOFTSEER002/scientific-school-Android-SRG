package com.jeannypr.scientificstudy.Base.Repo;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.jeannypr.scientificstudy.BuildConfig.BASE_URL;

public class DataRepo<T> {
    public T service;
    private static UserPreference preference;

    public T getService() {
        return service;
    }

    public DataRepo(Class<T> cls, final Context context) {
        retrofit2.Retrofit retrofit = setUpRetroFit(null, context, null);
        service = retrofit.create(cls);
    }

    public DataRepo(Class<T> cls, final Context context, String baseUrl) {
        retrofit2.Retrofit retrofit = setUpRetroFit(baseUrl, context, null);
        service = retrofit.create(cls);
    }

    public DataRepo(Class<T> cls, final Context context, String baseUrl, String authenticationToken) {
        retrofit2.Retrofit retrofit = setUpRetroFit(baseUrl, context, authenticationToken);
        service = retrofit.create(cls);
    }

    private retrofit2.Retrofit setUpRetroFit(String baseUrl, Context context, final String authenticationToken) {
        try {
            preference = UserPreference.getInstance(context);

            okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
            //interceptor registration
            builder.addInterceptor(new okhttp3.Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    String schoolCode = preference.getSchoolCode();
                    //   initializeSSLContext(context);
                    schoolCode = schoolCode.replaceAll("\\s", "");

                    if (schoolCode.equals("")) {
                        String msg = "School code can not be empty!";
                        return new Response.Builder()
                                .code(101) //Simply put whatever value you want to designate to aborted request.
                                .protocol(Protocol.HTTP_1_0)
                                .body(new RealResponseBody("plain/text", msg.length(), null))
                                .message(msg)
                                .request(chain.request())
                                .build();
                    } else {
                       /* okhttp3.Request request = chain.request().newBuilder()
                                .header("Authkey", "123")
                                .header("DeviceId", "7878")
                                .header("Content-Type", "application/json")
                                .header("SchoolCode", schoolCode)
                                .build();*/
                        okhttp3.Request.Builder builder = chain.request().newBuilder()
                                .header("Authkey", "123")
                                .header("DeviceId", "7878")
                                .header("Content-Type", "application/json")
                                .header("SchoolCode", schoolCode);
                        if (authenticationToken != null)
                            builder.header(ApiConstants.SPT_AUTHENTICATION_TOKEN, authenticationToken);

                        okhttp3.Request request = builder.build();
                        return chain.proceed(request);
                    }
                }
            });

        /*
        //Loggin interceptor
        okhttp3.logging.HttpLoggingInterceptor loggingInterceptor = new okhttp3.logging.HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.interceptors().add(loggingInterceptor); */

            builder.connectTimeout(ApiConstants.TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);

            okhttp3.OkHttpClient client = getOkhttp3Client(context, builder);

            retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(baseUrl == null || baseUrl.equals("") ? BASE_URL : baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit;

        } catch (Exception ex) {
            Log.d("DataRepo", ex.getMessage());
            return null;
        }
    }

    private okhttp3.OkHttpClient getOkhttp3Client(Context context, okhttp3.OkHttpClient.Builder builder) {
        if (Build.VERSION.SDK_INT < 22) {
            try {

                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream cert = context.getResources().openRawResource(R.raw.scientificstudy);
                Certificate ca;
                try {
                    ca = cf.generateCertificate(cert);
                } finally {
                    cert.close();
                }

                // creating a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                // creating a TrustManager that trusts the CAs in our KeyStore
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                // creating an SSLSocketFactory that uses our TrustManager
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, tmf.getTrustManagers(), null);


                okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
                try {
                    X509TrustManager trustManager = (X509TrustManager) tmf.getTrustManagers()[0];
                    client = builder
                            .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                            .build();
                    client.socketFactory();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return client;
            } catch (Exception e) {
                return builder.build();
            }
        }
        return builder.build();
    }
    /*public static void initializeSSLContext(Context mContext){
        try {
            SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ProviderInstaller.installIfNeeded(mContext.getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {

        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {

//            try {
//
//                client.sslSocketFactory(new TLSSocketFactory(), TrustManagerUtility.getTrustManager());
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (KeyStoreException e) {
//                e.printStackTrace();
//            }

            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.1");
                sc.init(null, new TrustManager[]{TrustManagerUtility.getTrustManager()}, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()),TrustManagerUtility.getTrustManager());

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2,TlsVersion.TLS_1_1)

                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }*/
}