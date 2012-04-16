package ru.mmb.terminal.activity;

import static ru.mmb.terminal.activity.Constants.REQUEST_CODE_DEFAULT_ACTIVITY;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public abstract class CurrentState
{
	private final List<StateChangeListener> listeners = new ArrayList<StateChangeListener>();

	private final String prefix;

	public CurrentState(String prefix)
	{
		this.prefix = prefix;
	}

	public abstract void save(Bundle savedInstanceState);

	public abstract void load(Bundle savedInstanceState);

	protected abstract void update();

	public void prepareStartActivityIntent(Intent intent)
	{
		prepareStartActivityIntent(intent, REQUEST_CODE_DEFAULT_ACTIVITY);
	}

	public void prepareStartActivityIntent(Intent intent, int activityRequestId)
	{
		// default do nothing
		// override in subclasses if needed
	}

	public void loadFromIntent(Intent data)
	{
		Bundle extras = data.getExtras();
		if (extras == null) return;
		loadFromExtrasBundle(extras);
	}

	protected void loadFromExtrasBundle(Bundle extras)
	{
		// default do nothing
		// override in subclasses if needed
	}

	public void saveToSharedPreferences(SharedPreferences preferences)
	{
	}

	public void loadFromSharedPreferences(SharedPreferences preferences)
	{
	}

	public void addStateChangeListener(StateChangeListener listener)
	{
		if (!listeners.contains(listener)) listeners.add(listener);
	}

	public void removeStateChangeListener(StateChangeListener listener)
	{
		listeners.remove(listener);
	}

	protected void fireStateChanged()
	{
		for (StateChangeListener listener : listeners)
		{
			listener.onStateChange();
		}
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void initialize(Activity activity, Bundle savedInstanceState)
	{
		if (savedInstanceState == null)
		{
			loadFromSharedPreferences(activity.getPreferences(Context.MODE_PRIVATE));
			loadFromIntent(activity.getIntent());
		}
		else
		{
			load(savedInstanceState);
		}
		update();
	}
}