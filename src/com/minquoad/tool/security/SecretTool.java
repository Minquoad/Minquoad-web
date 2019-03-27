package com.minquoad.tool.security;

import com.minquoad.entity.User;

public interface SecretTool {

	public String getDynamicSalt(User user);

}
