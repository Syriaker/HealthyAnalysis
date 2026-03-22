package com.healthanalysis.app.di;

import com.healthanalysis.app.data.api.NutritionApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class NetworkModule_ProvideNutritionApiFactory implements Factory<NutritionApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideNutritionApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public NutritionApi get() {
    return provideNutritionApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideNutritionApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideNutritionApiFactory(retrofitProvider);
  }

  public static NutritionApi provideNutritionApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideNutritionApi(retrofit));
  }
}
