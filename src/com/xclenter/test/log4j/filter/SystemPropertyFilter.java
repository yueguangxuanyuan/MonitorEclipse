package com.xclenter.test.log4j.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

@Plugin(name = "SystemPropertyFilter", category = "Core", elementType = "filter", printObject = true)
public class SystemPropertyFilter extends AbstractFilter {

	private final String property;
	private final String value;

	private SystemPropertyFilter(final String property, final String value,
			final Result onMatch, final Result onMismatch) {
		super(onMatch, onMismatch);
		this.property = property;
		this.value = value;
	}

	@Override
	public Result filter(final Logger logger, final Level level,
			final Marker marker, final String msg, final Object... params) {
		return filter(level);
	}

	@Override
	public Result filter(final Logger logger, final Level level,
			final Marker marker, final Object msg, final Throwable t) {
		return filter(level);
	}

	@Override
	public Result filter(final Logger logger, final Level level,
			final Marker marker, final Message msg, final Throwable t) {
		return filter(level);
	}

	@Override
	public Result filter(final LogEvent event) {
		return filter(event.getLevel());
	}

	private Result filter(final Level level) {
		String sysValue = System.getProperty(property);
		boolean ifSatisfy = sysValue == null ? false : sysValue.equals(value);
		return ifSatisfy ? onMatch : onMismatch;
	}

	/**
	 * Create a LevelRangeFilter.
	 * 
	 * @param minLevelName
	 *            The minimum log Level.
	 * @param maxLevelName
	 *            The maximum log level.
	 * @param match
	 *            The action to take on a match.
	 * @param mismatch
	 *            The action to take on a mismatch.
	 * @return The created ThresholdFilter.
	 */
	@PluginFactory
	public static SystemPropertyFilter createFilter(
			@PluginAttribute("property") final String property,
			@PluginAttribute("value") final String value,
			@PluginAttribute("onMatch") final String match,
			@PluginAttribute("onMismatch") final String mismatch) {
		final Result onMatch = Result.toResult(match, Result.NEUTRAL);
		final Result onMismatch = Result.toResult(mismatch, Result.DENY);
		return new SystemPropertyFilter(property, value, onMatch, onMismatch);
	}

}