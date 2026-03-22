package com.healthanalysis.app.data.repository;

import com.healthanalysis.app.data.api.ProfileApi;
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
public final class ProfileRepository_Factory implements Factory<ProfileRepository> {
  private final Provider<ProfileApi> profileApiProvider;

  public ProfileRepository_Factory(Provider<ProfileApi> profileApiProvider) {
    this.profileApiProvider = profileApiProvider;
  }

  @Override
  public ProfileRepository get() {
    return newInstance(profileApiProvider.get());
  }

  public static ProfileRepository_Factory create(Provider<ProfileApi> profileApiProvider) {
    return new ProfileRepository_Factory(profileApiProvider);
  }

  public static ProfileRepository newInstance(ProfileApi profileApi) {
    return new ProfileRepository(profileApi);
  }
}
