package me.piggypiglet.template.bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.piggypiglet.template.bootstrap.exceptions.BootstrapHaltException;
import me.piggypiglet.template.bootstrap.framework.Registerable;
import me.piggypiglet.template.guice.ExceptionalInjector;
import me.piggypiglet.template.guice.modules.InitialModule;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

// ------------------------------
// Copyright (c) PiggyPiglet 2021
// https://www.piggypiglet.me
// ------------------------------
public final class TemplateBootstrap extends JavaPlugin {
    private static final List<Class<? extends Registerable>> REGISTERABLES = List.of(
            //registerables ay
    );

    @Override
    public void onEnable() {
        final InitialModule initialModule = new InitialModule(this);
        final AtomicReference<Injector> injector = new AtomicReference<>(new ExceptionalInjector(Guice.createInjector(initialModule)));

        for (final Class<? extends Registerable> registerableClass : REGISTERABLES) {
            final Registerable registerable = injector.get().getInstance(registerableClass);
            registerable.run(injector.get());

            final BootstrapHaltException halt = registerable.halt();

            if (halt != null) {
                throw halt;
            }

            registerable.createModule()
                    .map(injector.get()::createChildInjector)
                    .ifPresent(injector::set);
        }
    }
}
