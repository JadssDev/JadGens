package dev.jadss.jadgens.controller;

import java.util.function.Function;

@FunctionalInterface
public interface VersionMigratorRunnable<E extends VersionControlled> extends Function<E, E> {
}
