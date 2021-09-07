package me.piggypiglet.template.scanning.framework;

import me.piggypiglet.template.scanning.rules.Rules;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

// ------------------------------
// Copyright (c) PiggyPiglet 2021
// https://www.piggypiglet.me
// ------------------------------
public interface Scanner {
    @NotNull
    <T> Stream<Class<T>> classes(@NotNull final Rules<T> rules);

    @NotNull
    Stream<Parameter> parametersInConstructors(@NotNull final Rules<?> rules);

    @NotNull
    Stream<Field> fields(@NotNull final Rules<?> rules);
}
