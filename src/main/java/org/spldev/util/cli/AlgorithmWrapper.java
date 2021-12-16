/* -----------------------------------------------------------------------------
 * Util Lib - Miscellaneous utility functions.
 * Copyright (C) 2021  Sebastian Krieter
 * 
 * This file is part of Util Lib.
 * 
 * Util Lib is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Util Lib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Util Lib.  If not, see <https://www.gnu.org/licenses/>.
 * 
 * See <https://github.com/skrieter/utils> for further information.
 * -----------------------------------------------------------------------------
 */
package org.spldev.util.cli;

import org.spldev.util.data.Result;
import org.spldev.util.extension.Extension;

import java.util.List;
import java.util.ListIterator;

/**
 * Interface for an algorithm to run via a {@link CLIFunction}.
 *
 * @author Sebastian Krieter
 */
public abstract class AlgorithmWrapper<T> implements Extension {

	public Result<T> parseArguments(List<String> args) {
		final T algorithm = createAlgorithm();
		try {
			for (final ListIterator<String> iterator = args.listIterator(); iterator.hasNext();) {
				final String arg = iterator.next();
				if (!parseArgument(algorithm, arg, iterator)) {
					throw new IllegalArgumentException("Unknown argument " + arg);
				}
			}
			return Result.of(algorithm);
		} catch (final Exception e) {
			return Result.empty(e);
		}
	}

	protected abstract T createAlgorithm();

	protected boolean parseArgument(T algorithm, String arg, ListIterator<String> iterator)
		throws IllegalArgumentException {
		return false;
	}

	public abstract String getName();

	public abstract String getHelp();

}