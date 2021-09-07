package me.piggypiglet.template.guice.objects;

import com.google.inject.Key;
import org.jetbrains.annotations.NotNull;

// ------------------------------
// Copyright (c) PiggyPiglet 2021
// https://www.piggypiglet.me
// ------------------------------
public final record Binding<T>(@NotNull Key<? super T> key, @NotNull T instance) {
}
