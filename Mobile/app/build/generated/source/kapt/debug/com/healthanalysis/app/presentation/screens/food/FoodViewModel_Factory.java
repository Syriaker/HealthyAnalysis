package com.healthanalysis.app.presentation.screens.food;

import com.healthanalysis.app.data.repository.NutritionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class FoodViewModel_Factory implements Factory<FoodViewModel> {
  private final Provider<NutritionRepository> nutritionRepositoryProvider;

  public FoodViewModel_Factory(Provider<NutritionRepository> nutritionRepositoryProvider) {
    this.nutritionRepositoryProvider = nutritionRepositoryProvider;
  }

  @Override
  public FoodViewModel get() {
    return newInstance(nutritionRepositoryProvider.get());
  }

  public static FoodViewModel_Factory create(
      Provider<NutritionRepository> nutritionRepositoryProvider) {
    return new FoodViewModel_Factory(nutritionRepositoryProvider);
  }

  public static FoodViewModel newInstance(NutritionRepository nutritionRepository) {
    return new FoodViewModel(nutritionRepository);
  }
}
