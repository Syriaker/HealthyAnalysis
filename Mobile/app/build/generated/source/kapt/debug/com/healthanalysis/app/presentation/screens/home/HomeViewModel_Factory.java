package com.healthanalysis.app.presentation.screens.home;

import com.healthanalysis.app.data.repository.NutritionRepository;
import com.healthanalysis.app.data.repository.ProfileRepository;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<NutritionRepository> nutritionRepositoryProvider;

  private final Provider<ProfileRepository> profileRepositoryProvider;

  public HomeViewModel_Factory(Provider<NutritionRepository> nutritionRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    this.nutritionRepositoryProvider = nutritionRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(nutritionRepositoryProvider.get(), profileRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<NutritionRepository> nutritionRepositoryProvider,
      Provider<ProfileRepository> profileRepositoryProvider) {
    return new HomeViewModel_Factory(nutritionRepositoryProvider, profileRepositoryProvider);
  }

  public static HomeViewModel newInstance(NutritionRepository nutritionRepository,
      ProfileRepository profileRepository) {
    return new HomeViewModel(nutritionRepository, profileRepository);
  }
}
