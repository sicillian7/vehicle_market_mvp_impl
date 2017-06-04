package com.nixxie.vehiclemarket.di.modules;

import android.content.Context;

import com.nixxie.vehiclemarket.BuildConfig;
import com.nixxie.vehiclemarket.api.CompanyApiImpl;
import com.nixxie.vehiclemarketmvc.network.VehicleRepository;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



@Module
public class NetworkModule {

    private String mBaseUrl;
    private static final MediaType MEDIA_JSON = MediaType.parse("application/json");

    public NetworkModule(String mBaseUrl) {
        this.mBaseUrl = mBaseUrl;
    }

    @Singleton
    @Provides
    GsonConverterFactory provideConverterFactory(){
        return GsonConverterFactory.create();
    }



    @Provides
    @Named("baseUrl")
    String getBaseUrl() {
        return mBaseUrl;
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpMock(Context context){
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {

                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        HttpUrl url = request.url().newBuilder().addQueryParameter("wa_key",BuildConfig.apiKey).build();
                        request = request.newBuilder().url(url).build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

    @Singleton
    @Provides
    RxJava2CallAdapterFactory provideRxJavaAdapter(){
        return  RxJava2CallAdapterFactory.create();
    }


    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient client, GsonConverterFactory converter, RxJava2CallAdapterFactory adapterFactory){
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(converter)
                .addCallAdapterFactory(adapterFactory)
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    VehicleRepository provideVehicleApiReposotory(Retrofit retrofit){
        return new CompanyApiImpl(retrofit);
    }
}
