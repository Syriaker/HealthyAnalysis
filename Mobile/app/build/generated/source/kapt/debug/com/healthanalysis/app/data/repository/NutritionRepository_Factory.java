package com.healthanalysis.app.data.repository;

import com.healthanalysis.app.data.api.NutritionApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class NutritionRepository_Factory implements Factory<NutritionRepository> {
  private final Provider<NutritionApi> nutritionApiProvider;

  public NutritionRepository_Factory(Provider<NutritionApi> nutritionApiProvider) {
    this.nutritionApiProvider = nutritionApiProvider;
  }

  @Override
  public NutritionRepository get() {
    return newInstance(nutritionApiProvider.get());
  }

  public static NutritionRepository_Factory create(Provider<NutritionApi> nutritionApiProvider) {
    return new NutritionRepository_Factory(nutritionApiProvider);
  }

  public static NutritionRepository newInstance(NutritionApi nutritionApi) {
    return new NutritionRepository(nutritionApi);
  }
}
