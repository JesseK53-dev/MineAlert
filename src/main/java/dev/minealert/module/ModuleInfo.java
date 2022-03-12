package dev.minealert.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleInfo {

    String moduleName();

    String moduleDesc();

    ModuleType moduleType();
}
