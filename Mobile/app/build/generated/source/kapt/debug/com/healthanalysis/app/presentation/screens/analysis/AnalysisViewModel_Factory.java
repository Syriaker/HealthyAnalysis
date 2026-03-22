package com.healthanalysis.app.presentation.screens.analysis;

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
public final class AnalysisViewModel_Factory implements Factory<AnalysisViewModel> {
  @Override
  public AnalysisViewModel get() {
    return newInstance();
  }

  public static AnalysisViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AnalysisViewModel newInstance() {
    return new AnalysisViewModel();
  }

  private static final class InstanceHolder {
    private static final AnalysisViewModel_Factory INSTANCE = new AnalysisViewModel_Factory();
  }
}
