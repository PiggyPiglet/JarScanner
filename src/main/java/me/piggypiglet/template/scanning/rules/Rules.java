package me.piggypiglet.template.scanning.rules;

import me.piggypiglet.template.scanning.annotations.Hidden;
import me.piggypiglet.template.scanning.rules.annotation.AnnotationUtils;
import me.piggypiglet.template.scanning.rules.annotation.AnnotationWrapper;
import me.piggypiglet.template.scanning.rules.element.ElementWrapper;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

// ------------------------------
// Copyright (c) PiggyPiglet 2021
// https://www.piggypiglet.me
// ------------------------------
public final record Rules<R>(@NotNull List<Predicate<ElementWrapper>> rules) {
    private static final Rules<?> EMPTY = new Rules<>(Collections.emptyList());

    @NotNull
    public static Rules<?> empty() {
        return EMPTY;
    }

    @NotNull
    public static <R> Builder<R> builder() {
        return new Builder<>();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<R> {
        private final List<Predicate<ElementWrapper>> rules = new ArrayList<>();

        private Builder() {
            addAnnotated(element -> !element.isAnnotationPresent(Hidden.class));
        }

        @NotNull
        public Builder<R> add(@NotNull final Predicate<ElementWrapper> rule) {
            rules.add(rule);
            return this;
        }

        @NotNull
        public Builder<R> addType(@NotNull final Predicate<Class<?>> typeRule) {
            rules.add(element -> typeRule.test(element.type()));
            return this;
        }

        @NotNull
        public Builder<R> addAnnotated(@NotNull final Predicate<AnnotatedElement> annotatedRule) {
            rules.add(element -> annotatedRule.test(element.element()));
            return this;
        }

        @NotNull
        public <T extends Annotation> Builder<R> addAnnotated(@NotNull final Class<T> annotation, @NotNull final Predicate<T> annotationRule) {
            rules.add(element -> annotationRule.test(element.element().getAnnotation(annotation)));
            return this;
        }

        @NotNull
        public <T> Builder<T> typeEquals(@NotNull final Class<T> type) {
            return (Builder<T>) addType(element -> element == type);
        }

        @NotNull
        public <T> Builder<? extends T> typeExtends(@NotNull final Class<T> type) {
            return (Builder<? extends T>) addType(type::isAssignableFrom);
        }

        @NotNull
        public <T> Builder<? super T> typeSupers(@NotNull final Class<T> type) {
            return (Builder<? super T>) addType(element -> element.isAssignableFrom(type));
        }

        @NotNull
        public Builder<R> disallowImmutableClasses() {
            return addType(element -> !Modifier.isFinal(element.getModifiers()));
        }

        @NotNull
        public Builder<R> disallowMutableClasses() {
            return addType(element -> !Modifier.isAbstract(element.getModifiers()) && !element.isInterface());
        }

        @NotNull
        public Builder<R> hasAnnotation(@NotNull final Class<? extends Annotation> annotation) {
            return hasAnnotation(new AnnotationWrapper(annotation));
        }

        @NotNull
        public Builder<R> hasAnnotation(@NotNull final Annotation annotation) {
            return hasAnnotation(new AnnotationWrapper(annotation));
        }

        @NotNull
        private Builder<R> hasAnnotation(@NotNull final AnnotationWrapper annotationWrapper) {
            return addAnnotated(element -> AnnotationUtils.isAnnotationPresent(element, annotationWrapper));
        }

        @NotNull
        public Builder<R> allowHidden() {
            rules.remove(0);
            return this;
        }

        @NotNull
        public Rules<R> build() {
            return new Rules<>(rules);
        }
    }
}
