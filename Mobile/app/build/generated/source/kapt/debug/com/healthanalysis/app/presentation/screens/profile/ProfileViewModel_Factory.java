package com.healthanalysis.app.presentation.screens.profile;

import com.healthanalysis.app.data.local.TokenManager;
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
public final class ProfileViewModel_Factory implements Factory<ProfileViewModel> {
  private final Provider<ProfileRepository> profileRepositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public ProfileViewModel_Factory(Provider<ProfileRepository> profileRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public ProfileViewModel get() {
    return newInstance(profileRepositoryProvider.get(), tokenManagerProvider.get());
  }

  public static ProfileViewModel_Factory create(
      Provider<ProfileRepository> profileRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new ProfileViewModel_Factory(profileRepositoryProvider, tokenManagerProvider);
  }

  public static ProfileViewModel newInstance(ProfileRepository profileRepository,
      TokenManager tokenManager) {
    return new ProfileViewModel(profileRepository, tokenManager);
  }
}
