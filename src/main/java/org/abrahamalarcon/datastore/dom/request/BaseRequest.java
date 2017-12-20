package org.abrahamalarcon.datastore.dom.request;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class BaseRequest implements Serializable
{
	protected String getString(String string)
	{
		return StringUtils.isEmpty(string) ? null : string;
	}
}
