package com.healthanalysis.app.presentation.screens.tips;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class TipsViewModel_Factory implements Factory<TipsViewModel> {
  @Override
  public TipsViewModel get() {
    return newInstance();
  }

  public static TipsViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TipsViewModel newInstance() {
    return new TipsViewModel();
  }

  private static final class InstanceHolder {
    private static final TipsViewModel_Factory INSTANCE = new TipsViewModel_Factory();
  }
}
