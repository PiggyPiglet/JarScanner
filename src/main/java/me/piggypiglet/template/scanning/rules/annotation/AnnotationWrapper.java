package me.piggypiglet.template.scanning.rules.annotation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

// ------------------------------
// Copyright (c) PiggyPiglet 2021
// https://www.piggypiglet.me
// ------------------------------
public final class AnnotationWrapper {
    private final Class<? extends Annotation> annotationClass;
    private final Annotation annotationInstance;

    public AnnotationWrapper(@NotNull final Class<? extends Annotation> annotation) {
        this(annotation, null);
    }

    public AnnotationWrapper(@NotNull final Annotation annotation) {
        this(null, annotation);
    }

    private AnnotationWrapper(@Nullable final Class<? extends Annotation> annotationClass, @Nullable final Annotation annotationInstance) {
        this.annotationClass = annotationClass;
        this.annotationInstance = annotationInstance;
    }

    @Nullable
    public Class<? extends Annotation> annotationClass() {
        return annotationClass;
    }

    @Nullable
    public Annotation annotationInstance() {
        return annotationInstance;
    }
}
