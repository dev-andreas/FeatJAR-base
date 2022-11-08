/*
 * Copyright (C) 2022 Sebastian Krieter, Elias Kuiter
 *
 * This file is part of util.
 *
 * util is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3.0 of the License,
 * or (at your option) any later version.
 *
 * util is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with util. If not, see <https://www.gnu.org/licenses/>.
 *
 * See <https://github.com/FeatureIDE/FeatJAR-util> for further information.
 */
package de.featjar.base.log;

/**
 * Prepends a log message with the location of the logging code.
 * To this end, looks for the most recent element on the stack that does not belong
 * to {@link Thread} or to the {@link de.featjar.base.log} package and prints it.
 *
 * @author Elias Kuiter
 */
public class CallerFormatter implements Formatter {
    @Override
    public String getPrefix() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // start at 1 to skip the entry for "getStackTrace"
        for (int i = 1; i < stackTrace.length; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            if (stackTraceElement.getClassName().startsWith(getClass().getPackageName()))
                continue;
            return "[" + stackTraceElement + "]\t";
        }
        return "";
    }
}